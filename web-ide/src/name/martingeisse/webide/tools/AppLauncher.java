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
	 * Main method.
	 * @param launcherArgs command-line arguments. The first argument must be
	 * the name of the main class to launch; remaining arguments will
	 * be passed to the main method of that class.
	 * @throws Exception on errors
	 */
	public static void main(final String[] launcherArgs) throws Exception {

		// handle command-line arguments
		if (launcherArgs.length == 0) {
			System.err.println("main class name missing");
			return;
		}
		final String mainClassName = launcherArgs[0];
		final String[] applicationArgs = new String[launcherArgs.length - 1];
		System.arraycopy(launcherArgs, 0, applicationArgs, 0, applicationArgs.length);

		// prepare database access
		String databaseUrl = "jdbc:mysql://localhost/webide?zeroDateTimeBehavior=convertToNull&useTimezone=false&characterEncoding=utf8&characterSetResults=utf8";
		try {
			databaseConnection = DriverManager.getConnection(databaseUrl, "root", "");
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
		
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
		Statement statement = null;
		try {
			String classFileName = name + ".class";
			statement = databaseConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT contents FROM files WHERE name = '" + classFileName + "'");
			if (!resultSet.next()) {
				throw new ClassNotFoundException("could not find database class file: " + classFileName);
			}
			byte[] contents = resultSet.getBytes(1);
			return contents;
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
