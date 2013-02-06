/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.tools;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Launcher class for user-written applications. This launcher will
 * not have access to any libraries beyond the JRE libraries,
 * except the MySQL JDBC driver!
 */
public class AppLauncher extends ClassLoader {

	/**
	 * the databaseConnection
	 */
	private static Connection databaseConnection;
	
	/**
	 * the classPath
	 */
	private static String classPath;
	
	/**
	 * the resourceCache
	 */
	private Map<String, Object[]> resourceCache = new HashMap<String, Object[]>();
	
	/**
	 * Main method.
	 * @param launcherArgs command-line arguments. The first argument must be
	 * the name of the main class to launch; remaining arguments will
	 * be passed to the main method of that class.
	 * @throws Exception on errors
	 */
	public static void main(final String[] launcherArgs) throws Exception {

		// handle command-line arguments
		if (launcherArgs.length < 2) {
			System.err.println("wrong command-line arguments, expected <classpath> <main-class> ...");
			return;
		}
		classPath = launcherArgs[0];
		final String mainClassName = launcherArgs[1];
		final String[] applicationArgs = new String[launcherArgs.length - 2];
		System.arraycopy(launcherArgs, 2, applicationArgs, 0, applicationArgs.length);

		// prepare database access
		String databaseUrl = "jdbc:mysql://localhost/webide?zeroDateTimeBehavior=convertToNull&useTimezone=false&characterEncoding=utf8&characterSetResults=utf8";
		try {
			databaseConnection = DriverManager.getConnection(databaseUrl, "root", "");
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
		
		// find the resource record for the class path root
		
		// load the main class using a class loader that accesses the database
		try {
			AppLauncher launcher = new AppLauncher();
			Class<?> mainClass = launcher.loadClass(mainClassName);
			Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
			mainMethod.invoke(null, (Object)applicationArgs);
		} finally {
			databaseConnection.close();
		}

	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	public Class<?> findClass(final String name) throws ClassNotFoundException {
		final byte[] classFileData = loadClassData(name);
		return defineClass(name, classFileData, 0, classFileData.length);
	}

	/**
	 * 
	 */
	private byte[] loadClassData(final String name) throws ClassNotFoundException {
		String classFilePath = classPath + '/' + name.replace('.', '/') + ".class";
		Object[] file = loadResource(classFilePath);
		return (byte[])(file[3]);
	}

	/**
	 * 
	 */
	private Object[] loadResource(final String path) throws ClassNotFoundException {
		Object[] cached = resourceCache.get(path);
		if (cached == null) {
			if (path.equals("/")) {
				cached = loadResourceInternal("`parent_id` IS NULL");
			} else {
				int slashIndex = path.lastIndexOf('/');
				Object[] parentResource = loadResource(slashIndex == -1 ? "/" : path.substring(0, slashIndex));
				String name = (slashIndex == -1 ? path : path.substring(slashIndex + 1));
				cached = loadResourceInternal("`parent_id` = " + parentResource[0] + " AND `name` = '" + name + "'"); // TODO injection
			}
			resourceCache.put(path, cached);
		}
		return cached;
	}

	/**
	 * 
	 */
	private Object[] loadResourceInternal(String whereCondition) throws ClassNotFoundException {
		Statement statement = null;
		try {
			statement = databaseConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT `id`, `name`, `type`, `contents` FROM `workspace_resources` WHERE " + whereCondition);
			if (!resultSet.next()) {
				throw new ClassNotFoundException("could not find workspace resource WHERE " + whereCondition);
			}
			return new Object[] {resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getBytes(4)};
		} catch (SQLException e) {
			throw new ClassNotFoundException("SQL error", e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}		
	}
	
}
