//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.start;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/*-------------------------------------------*/
/**
 * <p>
 * Main start class. This class is intended to be the main class listed in the MANIFEST.MF of the start.jar archive. It
 * allows an application to be started with the command "java -jar start.jar".
 * </p>
 * 
 * <p>
 * The behaviour of Main is controlled by the parsing of the {@link Config} "org/eclipse/start/start.config" file
 * obtained as a resource or file.
 * </p>
 */
public class Main
{
    private static final String START_LOG_FILENAME = "start.log";
    private static final SimpleDateFormat START_LOG_ROLLOVER_DATEFORMAT = new SimpleDateFormat("yyyy_MM_dd-HHmmSSSSS.'" + START_LOG_FILENAME + "'");

    private static final int EXIT_USAGE = 1;
    private static final int ERR_LOGGING = -1;
    private static final int ERR_INVOKE_MAIN = -2;
    private static final int ERR_NOT_STOPPED = -4;
    private static final int ERR_UNKNOWN = -5;
    private boolean _showUsage = false;
    private boolean _dumpVersions = false;
    private boolean _listConfig = false;
    private boolean _listOptions = false;
    private boolean _dryRun = false;
    private boolean _exec = false;
    private final Config _config = new Config();
    private final Set<String> _sysProps = new HashSet<String>();
    private final List<String> _jvmArgs = new ArrayList<String>();
    private String _startConfig = null;

    private String _jettyHome;

    public static void main(String[] args)
    {
        try
        {
            Main main = new Main();
            List<String> arguments = main.expandCommandLine(args);
            List<String> xmls = main.processCommandLine(arguments);
            if (xmls != null)
                main.start(xmls);
        }
        catch (Throwable e)
        {
            usageExit(e,ERR_UNKNOWN);
        }
    }

    Main() throws IOException
    {
        _jettyHome = System.getProperty("jetty.home",".");
        _jettyHome = new File(_jettyHome).getCanonicalPath();
    }

    public List<String> expandCommandLine(String[] args) throws Exception
    {
        List<String> arguments = new ArrayList<String>();

        // add the command line args and look for start.ini args
        boolean ini = false;
        for (String arg : args)
        {
            if (arg.startsWith("--ini=") || arg.equals("--ini"))
            {
                ini = true;
                if (arg.length() > 6)
                {
                    arguments.addAll(loadStartIni(new File(arg.substring(6))));
                }
            }
            else if (arg.startsWith("--config="))
            {
                _startConfig = arg.substring(9);
            }
            else
            {
                arguments.add(arg);
            }
        }

        // if no non-option inis, add the start.ini and start.d
        if (!ini)
        {
            arguments.addAll(0,parseStartIniFiles());
        }

        return arguments;
    }

    List<String> parseStartIniFiles()
    {
        List<String> ini_args = new ArrayList<String>();
        File start_ini = new File(_jettyHome,"start.ini");
        if (start_ini.exists())
            ini_args.addAll(loadStartIni(start_ini));

        File start_d = new File(_jettyHome,"start.d");
        if (start_d.isDirectory())
        {
            File[] inis = start_d.listFiles(new FilenameFilter()
            {
                public boolean accept(File dir, String name)
                {
                    return name.toLowerCase(Locale.ENGLISH).endsWith(".ini");
                }
            });
            Arrays.sort(inis);
            for (File i : inis)
                ini_args.addAll(loadStartIni(i));
        }
        return ini_args;
    }

    public List<String> processCommandLine(List<String> arguments) throws Exception
    {
        // The XML Configuration Files to initialize with
        List<String> xmls = new ArrayList<String>();

        // Process the arguments
        int startup = 0;
        for (String arg : arguments)
        {
            if ("--help".equals(arg) || "-?".equals(arg))
            {
                _showUsage = true;
                continue;
            }

            if ("--stop".equals(arg))
            {
                int port = Integer.parseInt(Config.getProperty("STOP.PORT","-1"));
                String key = Config.getProperty("STOP.KEY",null);
                int timeout = Integer.parseInt(Config.getProperty("STOP.WAIT","0"));
                stop(port,key,timeout);
                return null;
            }

            if ("--version".equals(arg) || "-v".equals(arg) || "--info".equals(arg))
            {
                _dumpVersions = true;
                continue;
            }

            if ("--list-modes".equals(arg) || "--list-options".equals(arg))
            {
                _listOptions = true;
                continue;
            }

            if ("--list-config".equals(arg))
            {
                _listConfig = true;
                continue;
            }

            if ("--exec-print".equals(arg) || "--dry-run".equals(arg))
            {
                _dryRun = true;
                continue;
            }

            if ("--exec".equals(arg))
            {
                _exec = true;
                continue;
            }

            // Special internal indicator that jetty was started by the jetty.sh Daemon
            if ("--daemon".equals(arg))
            {
                File startDir = new File(System.getProperty("jetty.logs","logs"));
                if (!startDir.exists() || !startDir.canWrite())
                    startDir = new File(".");

                File startLog = new File(startDir,START_LOG_ROLLOVER_DATEFORMAT.format(new Date()));

                if (!startLog.exists() && !startLog.createNewFile())
                {
                    // Output about error is lost in majority of cases.
                    System.err.println("Unable to create: " + startLog.getAbsolutePath());
                    // Toss a unique exit code indicating this failure.
                    usageExit(ERR_LOGGING);
                }

                if (!startLog.canWrite())
                {
                    // Output about error is lost in majority of cases.
                    System.err.println("Unable to write to: " + startLog.getAbsolutePath());
                    // Toss a unique exit code indicating this failure.
                    usageExit(ERR_LOGGING);
                }
                PrintStream logger = new PrintStream(new FileOutputStream(startLog,false));
                System.setOut(logger);
                System.setErr(logger);
                System.out.println("Establishing " + START_LOG_FILENAME + " on " + new Date());
                continue;
            }

            if (arg.startsWith("--pre="))
            {
                xmls.add(startup++,arg.substring(6));
                continue;
            }

            if (arg.startsWith("-D"))
            {
                String[] assign = arg.substring(2).split("=",2);
                _sysProps.add(assign[0]);
                switch (assign.length)
                {
                    case 2:
                        System.setProperty(assign[0],assign[1]);
                        break;
                    case 1:
                        System.setProperty(assign[0],"");
                        break;
                    default:
                        break;
                }
                continue;
            }

            if (arg.startsWith("-"))
            {
                _jvmArgs.add(arg);
                continue;
            }

            // Is this a Property?
            if (arg.indexOf('=') >= 0)
            {
                String[] assign = arg.split("=",2);

                switch (assign.length)
                {
                    case 2:
                        if ("OPTIONS".equals(assign[0]))
                        {
                            String opts[] = assign[1].split(",");
                            for (String opt : opts)
                                _config.addActiveOption(opt.trim());
                        }
                        else
                        {
                            this._config.setProperty(assign[0],assign[1]);
                        }
                        break;
                    case 1:
                        this._config.setProperty(assign[0],null);
                        break;
                    default:
                        break;
                }

                continue;
            }

            // Anything else is considered an XML file.
            if (xmls.contains(arg))
            {
                System.out.println("WARN: Argument '" + arg + "' specified multiple times. Check start.ini?");
                System.out.println("Use \"java -jar start.jar --help\" for more information.");
            }
            xmls.add(arg);
        }

        return xmls;
    }

    private void usage()
    {
        String usageResource = "org/eclipse/jetty/start/usage.txt";
        InputStream usageStream = getClass().getClassLoader().getResourceAsStream(usageResource);

        if (usageStream == null)
        {
            System.err.println("ERROR: detailed usage resource unavailable");
            usageExit(EXIT_USAGE);
        }

        BufferedReader buf = null;
        try
        {
            buf = new BufferedReader(new InputStreamReader(usageStream));
            String line;

            while ((line = buf.readLine()) != null)
            {
                if (line.endsWith("@") && line.indexOf('@') != line.lastIndexOf('@'))
                {
                    String indent = line.substring(0,line.indexOf("@"));
                    String info = line.substring(line.indexOf('@'),line.lastIndexOf('@'));

                    if (info.equals("@OPTIONS"))
                    {
                        List<String> sortedOptions = new ArrayList<String>();
                        sortedOptions.addAll(_config.getSectionIds());
                        Collections.sort(sortedOptions);

                        for (String option : sortedOptions)
                        {
                            if ("*".equals(option) || option.trim().length() == 0)
                                continue;
                            System.out.print(indent);
                            System.out.println(option);
                        }
                    }
                    else if (info.equals("@CONFIGS"))
                    {
                        File etc = new File(System.getProperty("jetty.home","."),"etc");
                        if (!etc.exists() || !etc.isDirectory())
                        {
                            System.out.print(indent);
                            System.out.println("Unable to find/list " + etc);
                            continue;
                        }

                        File configs[] = etc.listFiles(new FileFilter()
                        {
                            public boolean accept(File path)
                            {
                                if (!path.isFile())
                                {
                                    return false;
                                }

                                String name = path.getName().toLowerCase(Locale.ENGLISH);
                                return (name.startsWith("jetty") && name.endsWith(".xml"));
                            }
                        });

                        List<File> configFiles = new ArrayList<File>();
                        configFiles.addAll(Arrays.asList(configs));
                        Collections.sort(configFiles);

                        for (File configFile : configFiles)
                        {
                            System.out.print(indent);
                            System.out.print("etc/");
                            System.out.println(configFile.getName());
                        }
                    }
                    else if (info.equals("@STARTINI"))
                    {
                        List<String> ini = loadStartIni(new File(_jettyHome,"start.ini"));
                        if (ini != null && ini.size() > 0)
                        {
                            for (String a : ini)
                            {
                                System.out.print(indent);
                                System.out.println(a);
                            }
                        }
                        else
                        {
                            System.out.print(indent);
                            System.out.println("none");
                        }
                    }
                }
                else
                {
                    System.out.println(line);
                }
            }
        }
        catch (IOException e)
        {
            usageExit(e,EXIT_USAGE);
        }
        finally
        {
            close(buf);
        }
        System.exit(EXIT_USAGE);
    }

    public void invokeMain(ClassLoader classloader, String classname, List<String> args) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException
    {
        Class<?> invoked_class = null;

        try
        {
            invoked_class = classloader.loadClass(classname);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        if (Config.isDebug() || invoked_class == null)
        {
            if (invoked_class == null)
            {
                System.err.println("ClassNotFound: " + classname);
            }
            else
            {
                System.err.println(classname + " " + invoked_class.getPackage().getImplementationVersion());
            }

            if (invoked_class == null)
            {
                usageExit(ERR_INVOKE_MAIN);
                return;
            }
        }

        String argArray[] = args.toArray(new String[0]);

        Class<?>[] method_param_types = new Class[]
        { argArray.getClass() };

        Method main = invoked_class.getDeclaredMethod("main",method_param_types);
        Object[] method_params = new Object[]
        { argArray };
        main.invoke(null,method_params);
    }

    /* ------------------------------------------------------------ */
    public static void close(Closeable c)
    {
        if (c == null)
        {
            return;
        }
        try
        {
            c.close();
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
        }
    }

    /* ------------------------------------------------------------ */
    public void start(List<String> xmls) throws IOException, InterruptedException
    {
        // Load potential Config (start.config)
        List<String> configuredXmls = loadConfig(xmls);

        // No XML defined in start.config or command line. Can't execute.
        if (configuredXmls.isEmpty())
        {
            throw new FileNotFoundException("No XML configuration files specified in start.config or command line.");
        }

        // Normalize the XML config options passed on the command line.
        configuredXmls = resolveXmlConfigs(configuredXmls);

        // Get Desired Classpath based on user provided Active Options.
        Classpath classpath = _config.getActiveClasspath();

        System.setProperty("java.class.path",classpath.toString());
        ClassLoader cl = classpath.getClassLoader();
        if (Config.isDebug())
        {
            System.err.println("java.class.path=" + System.getProperty("java.class.path"));
            System.err.println("jetty.home=" + System.getProperty("jetty.home"));
            System.err.println("java.home=" + System.getProperty("java.home"));
            System.err.println("java.io.tmpdir=" + System.getProperty("java.io.tmpdir"));
            System.err.println("java.class.path=" + classpath);
            System.err.println("classloader=" + cl);
            System.err.println("classloader.parent=" + cl.getParent());
            System.err.println("properties=" + Config.getProperties());
        }

        // Show the usage information and return
        if (_showUsage)
        {
            usage();
            return;
        }

        // Show the version information and return
        if (_dumpVersions)
        {
            showClasspathWithVersions(classpath);
            return;
        }

        // Show all options with version information
        if (_listOptions)
        {
            showAllOptionsWithVersions();
            return;
        }

        if (_listConfig)
        {
            listConfig();
            return;
        }

        // Show Command Line to execute Jetty
        if (_dryRun)
        {
            CommandLineBuilder cmd = buildCommandLine(classpath,configuredXmls);
            System.out.println(cmd.toString());
            return;
        }

        // execute Jetty in another JVM
        if (_exec)
        {
            CommandLineBuilder cmd = buildCommandLine(classpath,configuredXmls);

            ProcessBuilder pbuilder = new ProcessBuilder(cmd.getArgs());
            final Process process = pbuilder.start();
            Runtime.getRuntime().addShutdownHook(new Thread()
            {
                @Override
                public void run()
                {
                    Config.debug("Destroying " + process);
                    process.destroy();
                }
            });

            copyInThread(process.getErrorStream(),System.err);
            copyInThread(process.getInputStream(),System.out);
            copyInThread(System.in,process.getOutputStream());
            process.waitFor();
            System.exit(0); // exit JVM when child process ends.
            return;
        }

        if (_jvmArgs.size() > 0 || _sysProps.size() > 0)
        {
            System.err.println("WARNING: System properties and/or JVM args set.  Consider using --dry-run or --exec");
        }

        // Set current context class loader to what is selected.
        Thread.currentThread().setContextClassLoader(cl);

        // Invoke the Main Class
        try
        {
            // Get main class as defined in start.config
            String classname = _config.getMainClassname();

            // Check for override of start class (via "jetty.server" property)
            String mainClass = System.getProperty("jetty.server");
            if (mainClass != null)
            {
                classname = mainClass;
            }

            // Check for override of start class (via "main.class" property)
            mainClass = System.getProperty("main.class");
            if (mainClass != null)
            {
                classname = mainClass;
            }

            Config.debug("main.class=" + classname);

            invokeMain(cl,classname,configuredXmls);
        }
        catch (Exception e)
        {
            usageExit(e,ERR_INVOKE_MAIN);
        }
    }

    private void copyInThread(final InputStream in, final OutputStream out)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    byte[] buf = new byte[1024];
                    int len = in.read(buf);
                    while (len > 0)
                    {
                        out.write(buf,0,len);
                        len = in.read(buf);
                    }
                }
                catch (IOException e)
                {
                    // e.printStackTrace();
                }
            }

        }).start();
    }

    private String resolveXmlConfig(String xmlFilename) throws FileNotFoundException
    {
        if (!xmlFilename.toLowerCase(Locale.ENGLISH).endsWith(".xml"))
        {
            // Nothing to resolve.
            return xmlFilename;
        }

        File xml = new File(xmlFilename);
        if (xml.exists() && xml.isFile())
        {
            return xml.getAbsolutePath();
        }

        xml = new File(_jettyHome,fixPath(xmlFilename));
        if (xml.exists() && xml.isFile())
        {
            return xml.getAbsolutePath();
        }

        xml = new File(_jettyHome,fixPath("etc/" + xmlFilename));
        if (xml.exists() && xml.isFile())
        {
            return xml.getAbsolutePath();
        }

        throw new FileNotFoundException("Unable to find XML Config: " + xmlFilename);
    }

    CommandLineBuilder buildCommandLine(Classpath classpath, List<String> xmls) throws IOException
    {
        CommandLineBuilder cmd = new CommandLineBuilder(findJavaBin());

        for (String x : _jvmArgs)
        {
            cmd.addArg(x);
        }
        cmd.addRawArg("-Djetty.home=" + _jettyHome);

        // Special Stop/Shutdown properties
        ensureSystemPropertySet("STOP.PORT");
        ensureSystemPropertySet("STOP.KEY");

        // System Properties
        for (String p : _sysProps)
        {
            String v = System.getProperty(p);
            cmd.addEqualsArg("-D" + p,v);
        }

        cmd.addArg("-cp");
        cmd.addRawArg(classpath.toString());
        cmd.addRawArg(_config.getMainClassname());

        // Check if we need to pass properties as a file
        Properties properties = Config.getProperties();
        if (properties.size() > 0)
        {
            File prop_file = File.createTempFile("start",".properties");
            if (!_dryRun)
                prop_file.deleteOnExit();
            properties.store(new FileOutputStream(prop_file),"start.jar properties");
            cmd.addArg(prop_file.getAbsolutePath());
        }

        for (String xml : xmls)
        {
            cmd.addRawArg(xml);
        }
        return cmd;
    }

    /**
     * Ensure that the System Properties are set (if defined as a System property, or start.config property, or
     * start.ini property)
     * 
     * @param key
     *            the key to be sure of
     */
    private void ensureSystemPropertySet(String key)
    {
        if (_sysProps.contains(key))
        {
            return; // done
        }

        Properties props = Config.getProperties();
        if (props.containsKey(key))
        {
            String val = props.getProperty(key,null);
            if (val == null)
            {
                return; // no value to set
            }
            // setup system property
            _sysProps.add(key);
            System.setProperty(key,val);
        }
    }

    private String findJavaBin()
    {
        File javaHome = new File(System.getProperty("java.home"));
        if (!javaHome.exists())
        {
            return null;
        }

        File javabin = findExecutable(javaHome,"bin/java");
        if (javabin != null)
        {
            return javabin.getAbsolutePath();
        }

        javabin = findExecutable(javaHome,"bin/java.exe");
        if (javabin != null)
        {
            return javabin.getAbsolutePath();
        }

        return "java";
    }

    private File findExecutable(File root, String path)
    {
        String npath = path.replace('/',File.separatorChar);
        File exe = new File(root,npath);
        if (!exe.exists())
        {
            return null;
        }
        return exe;
    }

    private void showAllOptionsWithVersions()
    {
        Set<String> sectionIds = _config.getSectionIds();

        StringBuffer msg = new StringBuffer();
        msg.append("There ");
        if (sectionIds.size() > 1)
        {
            msg.append("are ");
        }
        else
        {
            msg.append("is ");
        }
        msg.append(String.valueOf(sectionIds.size()));
        msg.append(" OPTION");
        if (sectionIds.size() > 1)
        {
            msg.append("s");
        }
        msg.append(" available to use.");
        System.out.println(msg);
        System.out.println("Each option is listed along with associated available classpath entries,  in the order that they would appear from that mode.");
        System.out.println("Note: If using multiple options (eg: 'Server,servlet,webapp,jms,jmx') "
                + "then overlapping entries will not be repeated in the eventual classpath.");
        System.out.println();
        System.out.printf("${jetty.home} = %s%n",_jettyHome);
        System.out.println();

        for (String sectionId : sectionIds)
        {
            if (Config.DEFAULT_SECTION.equals(sectionId))
            {
                System.out.println("GLOBAL option (Prepended Entries)");
            }
            else if ("*".equals(sectionId))
            {
                System.out.println("GLOBAL option (Appended Entries) (*)");
            }
            else
            {
                System.out.printf("Option [%s]",sectionId);
                if (Character.isUpperCase(sectionId.charAt(0)))
                {
                    System.out.print(" (Aggregate)");
                }
                System.out.println();
            }
            System.out.println("-------------------------------------------------------------");

            Classpath sectionCP = _config.getSectionClasspath(sectionId);

            if (sectionCP.isEmpty())
            {
                System.out.println("Empty option, no classpath entries active.");
                System.out.println();
                continue;
            }

            int i = 0;
            for (File element : sectionCP.getElements())
            {
                String elementPath = element.getAbsolutePath();
                if (elementPath.startsWith(_jettyHome))
                {
                    elementPath = "${jetty.home}" + elementPath.substring(_jettyHome.length());
                }
                System.out.printf("%2d: %20s | %s\n",i++,getVersion(element),elementPath);
            }

            System.out.println();
        }
    }

    private void showClasspathWithVersions(Classpath classpath)
    {
        // Iterate through active classpath, and fetch Implementation Version from each entry (if present)
        // to dump to end user.

        System.out.println("Active Options: " + _config.getActiveOptions());

        if (classpath.count() == 0)
        {
            System.out.println("No version information available show.");
            return;
        }

        System.out.println("Version Information on " + classpath.count() + " entr" + ((classpath.count() > 1)?"ies":"y") + " in the classpath.");
        System.out.println("Note: order presented here is how they would appear on the classpath.");
        System.out.println("      changes to the OPTIONS=[option,option,...] command line option will be reflected here.");

        int i = 0;
        for (File element : classpath.getElements())
        {
            String elementPath = element.getAbsolutePath();
            if (elementPath.startsWith(_jettyHome))
            {
                elementPath = "${jetty.home}" + elementPath.substring(_jettyHome.length());
            }
            System.out.printf("%2d: %20s | %s\n",i++,getVersion(element),elementPath);
        }
    }

    private String fixPath(String path)
    {
        return path.replace('/',File.separatorChar);
    }

    private String getVersion(File element)
    {
        if (element.isDirectory())
        {
            return "(dir)";
        }

        if (element.isFile())
        {
            String name = element.getName().toLowerCase(Locale.ENGLISH);
            if (name.endsWith(".jar"))
            {
                return JarVersion.getVersion(element);
            }

            if (name.endsWith(".zip"))
            {
                return getZipVersion(element);
            }
        }

        return "";
    }

    private String getZipVersion(File element)
    {
        // TODO - find version in zip file. Look for META-INF/MANIFEST.MF ?
        return "";
    }

    private List<String> resolveXmlConfigs(List<String> xmls) throws FileNotFoundException
    {
        List<String> ret = new ArrayList<String>();
        for (String xml : xmls)
        {
            ret.add(resolveXmlConfig(xml));
        }

        return ret;
    }

    private void listConfig()
    {
        InputStream cfgstream = null;
        try
        {
            cfgstream = getConfigStream();
            byte[] buf = new byte[4096];

            int len = 0;

            while (len >= 0)
            {
                len = cfgstream.read(buf);
                if (len > 0)
                    System.out.write(buf,0,len);
            }
        }
        catch (Exception e)
        {
            usageExit(e,ERR_UNKNOWN);
        }
        finally
        {
            close(cfgstream);
        }
    }

    /**
     * Load Configuration.
     * 
     * No specific configuration is real until a {@link Config#getCombinedClasspath(java.util.Collection)} is used to
     * execute the {@link Class} specified by {@link Config#getMainClassname()} is executed.
     * 
     * @param xmls
     *            the command line specified xml configuration options.
     * @return the list of xml configurations arriving via command line and start.config choices.
     */
    private List<String> loadConfig(List<String> xmls)
    {
        InputStream cfgstream = null;
        try
        {
            // Pass in xmls.size into Config so that conditions based on "nargs" work.
            _config.setArgCount(xmls.size());

            cfgstream = getConfigStream();

            // parse the config
            _config.parse(cfgstream);

            _jettyHome = Config.getProperty("jetty.home",_jettyHome);
            if (_jettyHome != null)
            {
                _jettyHome = new File(_jettyHome).getCanonicalPath();
                System.setProperty("jetty.home",_jettyHome);
            }

            // Collect the configured xml configurations.
            List<String> ret = new ArrayList<String>();
            ret.addAll(xmls); // add command line provided xmls first.
            for (String xmlconfig : _config.getXmlConfigs())
            {
                // add xmlconfigs arriving via start.config
                if (!ret.contains(xmlconfig))
                {
                    ret.add(xmlconfig);
                }
            }

            return ret;
        }
        catch (Exception e)
        {
            usageExit(e,ERR_UNKNOWN);
            return null; // never executed (just here to satisfy javac compiler)
        }
        finally
        {
            close(cfgstream);
        }
    }

    private InputStream getConfigStream() throws FileNotFoundException
    {
        String config = _startConfig;
        if (config == null || config.length() == 0)
        {
            config = System.getProperty("START","org/eclipse/jetty/start/start.config");
        }

        Config.debug("config=" + config);

        // Look up config as resource first.
        InputStream cfgstream = getClass().getClassLoader().getResourceAsStream(config);

        // resource not found, try filesystem next
        if (cfgstream == null)
        {
            cfgstream = new FileInputStream(config);
        }

        return cfgstream;
    }

    /**
     * Stop a running jetty instance.
     */
    public void stop(int port, String key)
    {
        stop(port,key,0);
    }

    public void stop(int port, String key, int timeout)
    {
        int _port = port;
        String _key = key;

        try
        {
            if (_port <= 0)
            {
                System.err.println("STOP.PORT system property must be specified");
            }
            if (_key == null)
            {
                _key = "";
                System.err.println("STOP.KEY system property must be specified");
                System.err.println("Using empty key");
            }

            Socket s = new Socket(InetAddress.getByName("127.0.0.1"),_port);
            if (timeout > 0)
                s.setSoTimeout(timeout * 1000);
            try
            {
                OutputStream out = s.getOutputStream();
                out.write((_key + "\r\nstop\r\n").getBytes());
                out.flush();

                if (timeout > 0)
                {
                    System.err.printf("Waiting %,d seconds for jetty to stop%n",timeout);
                    LineNumberReader lin = new LineNumberReader(new InputStreamReader(s.getInputStream()));
                    String response;
                    while ((response = lin.readLine()) != null)
                    {
                        Config.debug("Received \"" + response + "\"");
                        if ("Stopped".equals(response))
                            System.err.println("Server reports itself as Stopped");
                    }
                }
            }
            finally
            {
                s.close();
            }
        }
        catch (SocketTimeoutException e)
        {
            System.err.println("Timed out waiting for stop confirmation");
            System.exit(ERR_UNKNOWN);
        }
        catch (ConnectException e)
        {
            usageExit(e,ERR_NOT_STOPPED);
        }
        catch (Exception e)
        {
            usageExit(e,ERR_UNKNOWN);
        }
    }

    static void usageExit(Throwable t, int exit)
    {
        t.printStackTrace(System.err);
        System.err.println();
        System.err.println("Usage: java -jar start.jar [options] [properties] [configs]");
        System.err.println("       java -jar start.jar --help  # for more information");
        System.exit(exit);
    }

    static void usageExit(int exit)
    {
        System.err.println();
        System.err.println("Usage: java -jar start.jar [options] [properties] [configs]");
        System.err.println("       java -jar start.jar --help  # for more information");
        System.exit(exit);
    }

    /**
     * Convert a start.ini format file into an argument list.
     */
    static List<String> loadStartIni(File ini)
    {
        if (!ini.exists())
        {
            System.err.println("Warning - can't find ini file: " + ini);
            // No start.ini found, skip load.
            return Collections.emptyList();
        }

        List<String> args = new ArrayList<String>();

        FileReader reader = null;
        BufferedReader buf = null;
        try
        {
            reader = new FileReader(ini);
            buf = new BufferedReader(reader);

            String arg;
            while ((arg = buf.readLine()) != null)
            {
                arg = arg.trim();
                if (arg.length() == 0 || arg.startsWith("#"))
                {
                    continue;
                }
                args.add(arg);
            }
        }
        catch (IOException e)
        {
            usageExit(e,ERR_UNKNOWN);
        }
        finally
        {
            Main.close(buf);
            Main.close(reader);
        }

        return args;
    }

    void addJvmArgs(List<String> jvmArgs)
    {
        _jvmArgs.addAll(jvmArgs);
    }
}
