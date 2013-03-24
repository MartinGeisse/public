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

package org.eclipse.jetty.annotations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;

import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.JarScanner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * AnnotationParser
 * 
 * Use asm to scan classes for annotations. A SAX-style parsing is done, with
 * a handler being able to be registered to handle each annotation type.
 */
public class AnnotationParser
{
    private static final Logger LOG = Log.getLogger(AnnotationParser.class);
 
    protected Set<String> _parsedClassNames = new HashSet<String>();
    protected List<Handler> _handlers = new ArrayList<Handler>();
    
    public static String normalize (String name)
    {
        if (name==null)
            return null;
        
        if (name.startsWith("L") && name.endsWith(";"))
            name = name.substring(1, name.length()-1);
        
        if (name.endsWith(".class"))
            name = name.substring(0, name.length()-".class".length());
        
        return name.replace('/', '.');
    }
    

    
    public abstract class Value
    {
        String _name;
        
        public Value (String name)
        {
            _name = name;
        }
        
        public String getName()
        {
            return _name;
        }
        
        public abstract Object getValue();
           
    }
    
   
 
    
    public class SimpleValue extends Value
    {
        Object _val;
        
        public SimpleValue(String name)
        {
            super(name);
        }
        
        public void setValue(Object val)
        {
            _val=val;
        } 
        public Object getValue()
        {
            return _val;
        } 
        
        public String toString()
        {
            return "("+getName()+":"+_val+")";
        }
    }
    
    public class ListValue extends Value
    {
        List<Value> _val;
        
        public ListValue (String name)
        {
            super(name);
            _val = new ArrayList<Value>();
        }
      
        public Object getValue()
        {
            return _val;
        }
        
        public List<Value> getList()
        {
            return _val;
        }
        
        public void addValue (Value v)
        {
            _val.add(v);
        }
        
        public int size ()
        {
            return _val.size();
        }
        
        public String toString()
        {
            StringBuffer buff = new StringBuffer();
            buff.append("(");
            buff.append(getName());
            buff.append(":");
            for (Value n: _val)
            {
                buff.append(" "+n.toString());
            }
            buff.append(")");
            
            return buff.toString();
        }
    }
    
    
    
    /**
     * Handler
     *
     * Signature for all handlers that respond to parsing class files.
     */
    public interface Handler
    {
       
    }
    
    
    
    /**
     * DiscoverableAnnotationHandler
     *
     * Processes an annotation when it is discovered on a class.
     */
    public interface DiscoverableAnnotationHandler extends Handler
    {
        /**
         * Process an annotation that was discovered on a class
         * @param className
         * @param version
         * @param access
         * @param signature
         * @param superName
         * @param interfaces
         * @param annotation
         * @param values
         */
        public void handleClass (String className, int version, int access, 
                                 String signature, String superName, String[] interfaces, 
                                 String annotation, List<Value>values);
        
        /**
         * Process an annotation that was discovered on a method
         * @param className
         * @param methodName
         * @param access
         * @param desc
         * @param signature
         * @param exceptions
         * @param annotation
         * @param values
         */
        public void handleMethod (String className, String methodName, int access,  
                                  String desc, String signature,String[] exceptions, 
                                  String annotation, List<Value>values);
        
        
        /**
         * Process an annotation that was discovered on a field
         * @param className
         * @param fieldName
         * @param access
         * @param fieldType
         * @param signature
         * @param value
         * @param annotation
         * @param values
         */
        public void handleField (String className, String fieldName,  int access, 
                                 String fieldType, String signature, Object value, 
                                 String annotation, List<Value>values);
        
        
        /**
         * Get the name of the annotation processed by this handler. Can be null
         * 
         * @return
         */
        public String getAnnotationName();
    }
    
    
    
    /**
     * ClassHandler
     *
     * Responds to finding a Class
     */
    public interface ClassHandler extends Handler
    {
        public void handle (String className, int version, int access, String signature, String superName, String[] interfaces);
    }
    
    
    
    /**
     * MethodHandler
     *
     * Responds to finding a Method
     */
    public interface MethodHandler extends Handler
    {
        public void handle (String className, String methodName, int access,  String desc, String signature,String[] exceptions);
    }
    
    
    /**
     * FieldHandler
     *
     * Responds to finding a Field
     */
    public interface FieldHandler extends Handler
    {
        public void handle (String className, String fieldName, int access, String fieldType, String signature, Object value);
    }
    
    
    
    /**
     * MyAnnotationVisitor
     *
     * ASM Visitor for Annotations
     */
    public class MyAnnotationVisitor implements AnnotationVisitor
    {
        List<Value> _annotationValues;
        String _annotationName;
        
        public MyAnnotationVisitor (String annotationName, List<Value> values)
        {
            _annotationValues = values;
            _annotationName = annotationName;
        }
        
        public List<Value> getAnnotationValues()
        {
            return _annotationValues;
        }

        /** 
         * Visit a single-valued (name,value) pair for this annotation
         * @see org.objectweb.asm.AnnotationVisitor#visit(java.lang.String, java.lang.Object)
         */
        public void visit(String aname, Object avalue)
        {
           SimpleValue v = new SimpleValue(aname);
           v.setValue(avalue);
           _annotationValues.add(v);
        }

        /** 
         * Visit a (name,value) pair whose value is another Annotation
         * @see org.objectweb.asm.AnnotationVisitor#visitAnnotation(java.lang.String, java.lang.String)
         */
        public AnnotationVisitor visitAnnotation(String name, String desc)
        {
            String s = normalize(desc);
            ListValue v = new ListValue(s);
            _annotationValues.add(v);
            MyAnnotationVisitor visitor = new MyAnnotationVisitor(s, v.getList());
            return visitor; 
        }

        /** 
         * Visit an array valued (name, value) pair for this annotation
         * @see org.objectweb.asm.AnnotationVisitor#visitArray(java.lang.String)
         */
        public AnnotationVisitor visitArray(String name)
        {
            ListValue v = new ListValue(name);
            _annotationValues.add(v);
            MyAnnotationVisitor visitor = new MyAnnotationVisitor(null, v.getList());
            return visitor; 
        }

        /** 
         * Visit a enum-valued (name,value) pair for this annotation
         * @see org.objectweb.asm.AnnotationVisitor#visitEnum(java.lang.String, java.lang.String, java.lang.String)
         */
        public void visitEnum(String name, String desc, String value)
        {
            //TODO
        }
        
        public void visitEnd()
        {   
        }
    }
    
    

    
    /**
     * MyClassVisitor
     *
     * ASM visitor for a class.
     */
    public class MyClassVisitor extends EmptyVisitor
    {
        String _className;
        int _access;
        String _signature;
        String _superName;
        String[] _interfaces;
        int _version;


        public void visit (int version,
                           final int access,
                           final String name,
                           final String signature,
                           final String superName,
                           final String[] interfaces)
        {     
            _className = normalize(name);
            _access = access;
            _signature = signature;
            _superName = superName;
            _interfaces = interfaces;
            _version = version;
            
            _parsedClassNames.add(_className);
            //call all registered ClassHandlers
            String[] normalizedInterfaces = null;
            if (interfaces!= null)
            {
                normalizedInterfaces = new String[interfaces.length];
                int i=0;
                for (String s : interfaces)
                    normalizedInterfaces[i++] = normalize(s);
            }

            for (Handler h : AnnotationParser.this._handlers)
            {
                if (h instanceof ClassHandler)
                {
                    ((ClassHandler)h).handle(_className, _version, _access, _signature, normalize(_superName), normalizedInterfaces);
                }
            }
        }

        public AnnotationVisitor visitAnnotation (String desc, boolean visible)
        {                
            MyAnnotationVisitor visitor = new MyAnnotationVisitor(normalize(desc), new ArrayList<Value>())
            {
                public void visitEnd()
                {   
                    super.visitEnd();

                    //call all AnnotationHandlers with classname, annotation name + values
                    for (Handler h : AnnotationParser.this._handlers)
                    {
                        if (h instanceof DiscoverableAnnotationHandler)
                        {
                            DiscoverableAnnotationHandler dah = (DiscoverableAnnotationHandler)h;
                            if (_annotationName.equalsIgnoreCase(dah.getAnnotationName()))
                                dah.handleClass(_className, _version, _access, _signature, _superName, _interfaces, _annotationName, _annotationValues);
                        }
                    }
                }
            };
            
            return visitor;
        }

        public MethodVisitor visitMethod (final int access,
                                          final String name,
                                          final String methodDesc,
                                          final String signature,
                                          final String[] exceptions)
        {   

            return new EmptyVisitor ()
            {
                public AnnotationVisitor visitAnnotation(String desc, boolean visible)
                {
                    MyAnnotationVisitor visitor = new MyAnnotationVisitor (normalize(desc), new ArrayList<Value>())
                    {
                        public void visitEnd()
                        {   
                            super.visitEnd();
                            //call all AnnotationHandlers with classname, method, annotation name + values
                            for (Handler h : AnnotationParser.this._handlers)
                            {
                                if (h instanceof DiscoverableAnnotationHandler)
                                {
                                    DiscoverableAnnotationHandler dah = (DiscoverableAnnotationHandler)h;
                                    if (_annotationName.equalsIgnoreCase(dah.getAnnotationName()))
                                        dah.handleMethod(_className, name, access, methodDesc, signature, exceptions, _annotationName, _annotationValues);
                                }
                            }
                        }
                    };
                   
                    return visitor;
                }
            };
        }

        public FieldVisitor visitField (final int access,
                                        final String fieldName,
                                        final String fieldType,
                                        final String signature,
                                        final Object value)
        {

            return new EmptyVisitor ()
            {
                public AnnotationVisitor visitAnnotation(String desc, boolean visible)
                {
                    MyAnnotationVisitor visitor = new MyAnnotationVisitor(normalize(desc), new ArrayList<Value>())
                    {
                        public void visitEnd()
                        {
                            super.visitEnd();
                            for (Handler h : AnnotationParser.this._handlers)
                            {
                                if (h instanceof DiscoverableAnnotationHandler)
                                {
                                    DiscoverableAnnotationHandler dah = (DiscoverableAnnotationHandler)h;
                                    if (_annotationName.equalsIgnoreCase(dah.getAnnotationName()))
                                        dah.handleField(_className, fieldName, access, fieldType, signature, value, _annotationName, _annotationValues);
                                }
                            }
                        }
                    };
                    return visitor;
                }
            };
        }
    }
    
    
    /**
     * Register a handler that will be called back when the named annotation is
     * encountered on a class.
     * 
     * @deprecated see registerHandler(Handler)
     * @param annotationName
     * @param handler
     */
    public void registerAnnotationHandler (String annotationName, DiscoverableAnnotationHandler handler)
    {
        _handlers.add(handler);
    }
    
    
    /**
     * @deprecated
     * @param annotationName
     * @return
     */
    public List<DiscoverableAnnotationHandler> getAnnotationHandlers(String annotationName)
    {
        List<DiscoverableAnnotationHandler> handlers = new ArrayList<DiscoverableAnnotationHandler>();
        for (Handler h:_handlers)
        {
            if (h instanceof DiscoverableAnnotationHandler)
            {
                DiscoverableAnnotationHandler dah = (DiscoverableAnnotationHandler)h;
                if (annotationName.equals(dah.getAnnotationName()))
                    handlers.add(dah);
            }
        }
 
        return handlers;
    }

    /**
     * @deprecated
     * @return
     */
    public List<DiscoverableAnnotationHandler> getAnnotationHandlers()
    {
        List<DiscoverableAnnotationHandler> allAnnotationHandlers = new ArrayList<DiscoverableAnnotationHandler>();
        for (Handler h:_handlers)
        {
            if (h instanceof DiscoverableAnnotationHandler)
            allAnnotationHandlers.add((DiscoverableAnnotationHandler)h);
        }
        return allAnnotationHandlers;
    }

    /**
     * @deprecated see registerHandler(Handler)
     * @param handler
     */
    public void registerClassHandler (ClassHandler handler)
    {
        _handlers.add(handler);
    }
    
    
    
    /**
     * Add a particular handler
     * 
     * @param h
     */
    public void registerHandler(Handler h)
    {
        if (h == null)
            return;
        
        _handlers.add(h);
    }
    
    
    /**
     * Add a list of handlers
     * 
     * @param handlers
     */
    public void registerHandlers(List<? extends Handler> handlers)
    {
        if (handlers == null)
            return;
        _handlers.addAll(handlers);
    }
    
    
    /**
     * Remove a particular handler
     * 
     * @param h
     * @return
     */
    public boolean deregisterHandler(Handler h)
    {
        return _handlers.remove(h);
    }
    
    
    /**
     * Remove all registered handlers
     */
    public void clearHandlers()
    {
        _handlers.clear();
    }
    

    /**
     * True if the class has already been processed, false otherwise
     * @param className
     * @return
     */
    public boolean isParsed (String className)
    {
        return _parsedClassNames.contains(className);
    }
    
    
    
    /**
     * Parse a given class
     * 
     * @param className
     * @param resolver
     * @throws Exception
     */
    public void parse (String className, ClassNameResolver resolver) 
    throws Exception
    {
        if (className == null)
            return;
        
        if (!resolver.isExcluded(className))
        {
            if (!isParsed(className) || resolver.shouldOverride(className))
            {
                className = className.replace('.', '/')+".class";
                URL resource = Loader.getResource(this.getClass(), className, false);
                if (resource!= null)
                {
                    Resource r = Resource.newResource(resource);
                    scanClass(r.getInputStream());
                }
            }
        }
    }
    
    
    
    /**
     * Parse the given class, optionally walking its inheritance hierarchy
     * 
     * @param clazz
     * @param resolver
     * @param visitSuperClasses
     * @throws Exception
     */
    public void parse (Class clazz, ClassNameResolver resolver, boolean visitSuperClasses)
    throws Exception
    {
        Class cz = clazz;
        while (cz != null)
        {
            if (!resolver.isExcluded(cz.getName()))
            {
                if (!isParsed(cz.getName()) || resolver.shouldOverride(cz.getName()))
                {
                    String nameAsResource = cz.getName().replace('.', '/')+".class";
                    URL resource = Loader.getResource(this.getClass(), nameAsResource, false);
                    if (resource!= null)
                    {
                        Resource r = Resource.newResource(resource);
                        scanClass(r.getInputStream());
                    }
                }
            }
            if (visitSuperClasses)
                cz = cz.getSuperclass();
            else
                cz = null;
        }
    }
    
    
    
    /**
     * Parse the given classes
     * 
     * @param classNames
     * @param resolver
     * @throws Exception
     */
    public void parse (String[] classNames, ClassNameResolver resolver)
    throws Exception
    {
        if (classNames == null)
            return;
       
        parse(Arrays.asList(classNames), resolver); 
    }
    
    
    /**
     * Parse the given classes
     * 
     * @param classNames
     * @param resolver
     * @throws Exception
     */
    public void parse (List<String> classNames, ClassNameResolver resolver)
    throws Exception
    {
        for (String s:classNames)
        {
            if ((resolver == null) || (!resolver.isExcluded(s) &&  (!isParsed(s) || resolver.shouldOverride(s))))
            {            
                s = s.replace('.', '/')+".class"; 
                URL resource = Loader.getResource(this.getClass(), s, false);
                if (resource!= null)
                {
                    Resource r = Resource.newResource(resource);
                    scanClass(r.getInputStream());
                }
            }
        }
    }
    
    
    /**
     * Parse all classes in a directory
     * 
     * @param dir
     * @param resolver
     * @throws Exception
     */
    public void parse (Resource dir, ClassNameResolver resolver)
    throws Exception
    {
        if (!dir.isDirectory() || !dir.exists())
            return;
        
        
        String[] files=dir.list();
        for (int f=0;files!=null && f<files.length;f++)
        {
            try 
            {
                Resource res = dir.addPath(files[f]);
                if (res.isDirectory())
                    parse(res, resolver);
                String name = res.getName();
                if (name.endsWith(".class"))
                {
                    if ((resolver == null)|| (!resolver.isExcluded(name) && (!isParsed(name) || resolver.shouldOverride(name))))
                    {
                        Resource r = Resource.newResource(res.getURL());
                        scanClass(r.getInputStream());
                    }

                }
            }
            catch (Exception ex)
            {
                LOG.warn(Log.EXCEPTION,ex);
            }
        }
    }
    
    
    /**
     * Parse classes in the supplied classloader. 
     * Only class files in jar files will be scanned.
     * 
     * @param loader
     * @param visitParents
     * @param nullInclusive
     * @param resolver
     * @throws Exception
     */
    public void parse (ClassLoader loader, boolean visitParents, boolean nullInclusive, final ClassNameResolver resolver)
    throws Exception
    {
        if (loader==null)
            return;
        
        if (!(loader instanceof URLClassLoader))
            return; //can't extract classes?
       
        JarScanner scanner = new JarScanner()
        {
            public void processEntry(URI jarUri, JarEntry entry)
            {   
                try
                {
                    String name = entry.getName();
                    if (name.toLowerCase(Locale.ENGLISH).endsWith(".class"))
                    {
                        String shortName =  name.replace('/', '.').substring(0,name.length()-6);
                        if ((resolver == null)
                             ||
                            (!resolver.isExcluded(shortName) && (!isParsed(shortName) || resolver.shouldOverride(shortName))))
                        {

                            Resource clazz = Resource.newResource("jar:"+jarUri+"!/"+name);                     
                            scanClass(clazz.getInputStream());
                        }
                    }
                }
                catch (Exception e)
                {
                    LOG.warn("Problem processing jar entry "+entry, e);
                }
            }
            
        };

        scanner.scan(null, loader, nullInclusive, visitParents);
    }
    
    
    /**
     * Parse classes in the supplied url of jar files.
     * 
     * @param uris
     * @param resolver
     * @throws Exception
     */
    public void parse (URI[] uris, final ClassNameResolver resolver)
    throws Exception
    {
        if (uris==null)
            return;
        
        JarScanner scanner = new JarScanner()
        {
            public void processEntry(URI jarUri, JarEntry entry)
            {   
                try
                {
                    String name = entry.getName();
                    if (name.toLowerCase(Locale.ENGLISH).endsWith(".class"))
                    {
                        String shortName =  name.replace('/', '.').substring(0,name.length()-6);

                        if ((resolver == null)
                             ||
                            (!resolver.isExcluded(shortName) && (!isParsed(shortName) || resolver.shouldOverride(shortName))))
                        {
                            Resource clazz = Resource.newResource("jar:"+jarUri+"!/"+name);                     
                            scanClass(clazz.getInputStream());

                        }
                    }
                }
                catch (Exception e)
                {
                    LOG.warn("Problem processing jar entry "+entry, e);
                }
            }
            
        };        
        scanner.scan(null, uris, true);
    }
    
    /**
     * Parse a particular resource
     * @param uri
     * @param resolver
     * @throws Exception
     */
    public void parse (URI uri, final ClassNameResolver resolver)
    throws Exception
    {
        if (uri == null)
            return;
        URI[] uris = {uri};
        parse(uris, resolver);
    }

    
    
    /**
     * Use ASM on a class
     * 
     * @param is
     * @throws IOException
     */
    protected void scanClass (InputStream is)
    throws IOException
    {
        ClassReader reader = new ClassReader(is);
        reader.accept(new MyClassVisitor(), ClassReader.SKIP_CODE|ClassReader.SKIP_DEBUG|ClassReader.SKIP_FRAMES);
    }
}

