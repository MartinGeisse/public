/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

/**
 * This structure contains the result values for stat() and fstat() syscalls.
 */
public class StatResult {

	/**
	 * the deviceId
	 */
	public int deviceId;
	
	/**
	 * the inodeId
	 */
	public int inodeId;
	
	/**
	 * the mode
	 */
	public int mode;
	
	/**
	 * the linkCount
	 */
	public int linkCount;
	
	/**
	 * the userId
	 */
	public int userId;
	
	/**
	 * the groupId
	 */
	public int groupId;
	
	/**
	 * the targetDeviceId
	 */
	public int targetDeviceId;
	
	/**
	 * the size
	 */
	public int size;
	
	/**
	 * the lastAccessedTime
	 */
	public int lastAccessedTime;
	
	/**
	 * the lastModifiedTime
	 */
	public int lastModifiedTime;
	
	/**
	 * the lastChangedTime
	 */
	public int lastChangedTime;

	/**
	 * Creates a stat result for a regular file.
	 * @param inodeId the inode ID
	 * @param size the file size
	 * @param time the time that is used for all three timestamps (accessed / modified / changed)
	 * @return the stat result
	 */
	public static StatResult createForRegularFile(int inodeId, int size, int time) {
		StatResult result = new StatResult();
		result.deviceId = 0;
		result.inodeId = inodeId;
		result.mode = 0666;
		result.linkCount = 1;
		result.userId = 42;
		result.groupId = 42;
		result.targetDeviceId = 0;
		result.size = size;
		result.lastAccessedTime = time;
		result.lastModifiedTime = time;
		result.lastChangedTime = time;
		return result;
	}
	
}
