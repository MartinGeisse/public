/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import org.apache.commons.io.FileUtils;

/**
 * Helper class that extracts LWJGL native libraries to a temporary folder
 * because Java is too stupid to load native libraries from inside a
 * JAR file.
 */
public final class LwjglNativeLibraryHelper {

	/**
	 * the resourcePath
	 */
	private static String resourcePath;
	
	/**
	 * the tempFolder
	 */
	private static File tempFolder;

	/**
	 * Extracts LWJGL libraries to a folder and 
	 * @throws Exception on errors
	 */
	public static void prepareNativeLibraries() throws Exception {
		
		// Unfortunately, Java is also too stupid to create a temp directory...
	    tempFolder = File.createTempFile("miner-launcher-", "");
	    deleteRecursively(tempFolder);
	    tempFolder.mkdir();
		System.out.println("temp: " + tempFolder.getAbsolutePath());
	    
	    // detect which set of native libraries to load, then extract the files
		resourcePath = OperatingSystemSelector.getHostOs().getNativeLibraryPath();
		System.out.println("native library path: " + resourcePath);
		for (String fileName : OperatingSystemSelector.getHostOs().getNativeLibraryFileNames()) {
			extractFile(fileName);
		}
	    
		// make Java use our libraries
		System.setProperty("java.library.path", tempFolder.getAbsolutePath());
		final Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
		fieldSysPath.setAccessible(true);
		fieldSysPath.set(null, null);
		
	}

	/**
	 * 
	 */
	private static void deleteRecursively(File file) throws IOException {
		if (file.isDirectory()) {
			for (File sub : file.listFiles()) {
				deleteRecursively(sub);
			}
		}
		file.delete();
	}
	
	/**
	 * 
	 */
	private static void extractFile(String name) throws IOException {
		String fullResourceName = resourcePath + name;
		try (InputStream inputStream = LwjglNativeLibraryHelper.class.getClassLoader().getResourceAsStream(fullResourceName)) {
			if (inputStream == null) {
				throw new RuntimeException("resource not found: " + fullResourceName);
			}
			FileUtils.copyInputStreamToFile(inputStream, new File(tempFolder, name));
		}
	}
	
}
