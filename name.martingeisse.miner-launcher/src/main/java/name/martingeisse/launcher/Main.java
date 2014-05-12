/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

/**
 * The launcher -- downloads the up-to-date client and starts it.
 */
public class Main {

	/**
	 * Main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("name.martingeisse.miner.serverBaseUrl", "LIVE");
		System.setProperty("name.martingeisse.miner.serverName", "LIVE");
		String clientDownloadUrl = "http://vshg03.mni.fh-giessen.de/martin/miner-data/miner-client-3.jar";
		
		URLClassLoader classLoader = new URLClassLoader(new URL[] {new URL(clientDownloadUrl)}, Main.class.getClassLoader());
		Properties launchProperties = getLaunchProperties(classLoader);
		String mainClassName = launchProperties.getProperty("Main-Class");
		if (mainClassName == null) {
			throw new Exception("no Main-Class property in downloaded launcher properties");
		}
		Class<?> mainClass = classLoader.loadClass(mainClassName);
		mainClass.getMethod("main", String[].class).invoke(null, (Object)args);
	}

	/**
	 * 
	 */
	private static Properties getLaunchProperties(ClassLoader classLoader) throws IOException {
		Properties launchProperties = new Properties();
		InputStream inputStream = classLoader.getResourceAsStream("launch.properties");
		if (inputStream == null) {
			System.err.println("could not load client bundle");
			System.exit(1);
		}
		launchProperties.load(inputStream);
		inputStream.close();
		return launchProperties;
	}
	
}
