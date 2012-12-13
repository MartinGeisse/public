/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.resources;

/**
 * This enum type selects an icon for a resource.
 */
public enum ResourceIconSelector {

	/**
	 * icon for a file without warnings / errors
	 */
	FILE_OK("brick.png"),
	
	/**
	 * icon for a file with warnings but without errors
	 */
	FILE_WARNING("brick_error.png"),
	
	/**
	 * icon for a file with errors
	 */
	FILE_ERROR("brick_delete.png"),
	
	/**
	 * icon for a folder without warnings / errors
	 */
	FOLDER_OK("folder.png"),
	
	/**
	 * icon for a folder with warnings but without errors
	 */
	FOLDER_WARNING("folder_error.png"),
	
	/**
	 * icon for a folder without warnings / errors
	 */
	FOLDER_ERROR("folder_delete.png"),
	
	/**
	 * used if no other icon is appropriate
	 */
	MISSING_ICON("cross.png");

	/**
	 * the filename
	 */
	private final String filename;
	
	/**
	 * Constructor.
	 * @param filename the simple icon filename
	 */
	private ResourceIconSelector(String filename) {
		this.filename = filename;
	}

	/**
	 * Getter method for the filename.
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * Chosses an icon for a resource.
	 * @param type the resource type
	 * @param hasErrors whether the resource has errors
	 * @param hasWarnings whether the resource has warnings (ignored if hasErrors is true)
	 * @return the icon selector
	 */
	public static ResourceIconSelector choose(ResourceType type, boolean hasErrors, boolean hasWarnings) {
		switch (type) {
		
		case MOUNT_SPACE:
		case WORKSPACE:
		case PROJECT:
			return MISSING_ICON;
			
		case FOLDER:
			return (hasErrors ? FOLDER_ERROR : hasWarnings ? FOLDER_WARNING : FOLDER_OK);
			
		case FILE:
			return (hasErrors ? FILE_ERROR : hasWarnings ? FILE_WARNING : FILE_OK);
			
		default:
			return MISSING_ICON;
			
		}
	}
	
}
