/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.util;

/**
 * Helps managing OS-specific code.
 */
public enum OperatingSystemSelector {

	/**
	 * All Windows OSes
	 */
	WINDOWS("", new String[] {"jinput-dx8_64.dll", "jinput-dx8.dll", "jinput-raw_64.dll", "jinput-raw.dll", "lwjgl.dll", "lwjgl64.dll", "OpenAL32.dll", "OpenAL64.dll"}),

	/**
	 * All Linux OSes
	 */
	LINUX("", new String[] {"libjinput-linux.so", "libjinput-linux64.so", "liblwjgl.so", "liblwjgl64.so", "libopenal.so", "libopenal64.so"}),

	/**
	 * Mac OS X
	 */
	MAC("", new String[] {"libjinput-osx.jnilib", "liblwjgl.jnilib", "openal.dylib"}),

	/**
	 * Solaris
	 */
	SOLARIS("", new String[] {"liblwjgl.so", "liblwjgl64.so", "libopenal.so", "libopenal64.so"});

	/**
	 * the hostOs
	 */
	private static OperatingSystemSelector hostOs = null;

	/**
	 * static initializer
	 */
	static {
		final String name = System.getProperty("os.name").toLowerCase();
		if (name.indexOf("windows") >= 0) {
			hostOs = WINDOWS;
		} else if (name.indexOf("linux") >= 0) {
			hostOs = LINUX;
		} else if (name.indexOf("mac os x") >= 0) {
			hostOs = MAC;
		} else if (name.indexOf("solaris") >= 0 || name.indexOf("sun os") >= 0) {
			hostOs = SOLARIS;
		} else {
			throw new RuntimeException("operating system not recognized, os.name: \"" + System.getProperty("os.name") + "\"");
		}
	}

	/**
	 * Getter method for the hostOs.
	 * @return the hostOs
	 */
	public static OperatingSystemSelector getHostOs() {
		return hostOs;
	}

	/**
	 * the nativeLibraryPath
	 */
	private final String nativeLibraryPath;

	/**
	 * the nativeLibraryFileNames
	 */
	private final String[] nativeLibraryFileNames;

	/**
	 * Constructor.
	 * @param nativeLibraryPath
	 * @param nativeLibraryFileNames
	 */
	private OperatingSystemSelector(final String nativeLibraryPath, final String[] nativeLibraryFileNames) {
		this.nativeLibraryPath = nativeLibraryPath;
		this.nativeLibraryFileNames = nativeLibraryFileNames;
	}

	/**
	 * Returns the path to the folder that contains the native libraries
	 * for this OS.
	 * @return the path
	 */
	public String getNativeLibraryPath() {
		return nativeLibraryPath;
	}

	/**
	 * Returns the file names in the OS-specific native library folder to load.
	 * @return the file names
	 */
	public String[] getNativeLibraryFileNames() {
		return nativeLibraryFileNames;
	}
}
