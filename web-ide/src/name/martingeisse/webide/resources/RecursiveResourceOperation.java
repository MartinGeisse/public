/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;


/**
 * This class can be used to deal with a whole resource tree
 * recursively in a simple way. It invokes subclass methods
 * for each file and folder.
 */
public class RecursiveResourceOperation {

	/**
	 * This is the main entry point that invokes this operation on
	 * the specified resource: Handles the resource with the specified resource handle.
	 * The default implementation calls handleResourceByType for the resource.
	 * 
	 * @param resourceHandle the resource handle of the resource to handle
	 */
	public void handle(ResourceHandle resourceHandle) {
		handleResourceByType(resourceHandle);
	}

	
	/**
	 * This method calls one of handleFolder, handleFile or handleSpecial,
	 * depending on the resource's type.
	 * 
	 * @param resourceHandle the resource handle of the resource to handle
	 */
	protected final void handleResourceByType(ResourceHandle resourceHandle) {
		if (resourceHandle.isFolder()) {
			handleFolder(resourceHandle);
		} else if (resourceHandle.isFile()) {
			handleFile(resourceHandle);
		} else {
			handleSpecial(resourceHandle);
		}
	}
	
	/**
	 * Handles a folder. The default implementation invokes handleChildren
	 * to handle all children recursively.
	 * 
	 * @param resourceHandle the resource handle of the resource to handle
	 */
	protected void handleFolder(ResourceHandle resourceHandle) {
		handleChildren(resourceHandle);
	}
	
	/**
	 * Invokes handleResource on all children of the specified folder.
	 * 
	 * @param resourceHandle the resource handle of the resource to handle
	 */
	protected final void handleChildren(ResourceHandle resourceHandle) {
		for (ResourceHandle child : resourceHandle.getChildrenArray()) {
			handle(child);
		}
	}

	/**
	 * Handles a file.
	 * The default implementation does nothing.
	 * 
	 * @param resourceHandle the resource handle of the resource to handle
	 */
	protected void handleFile(ResourceHandle resourceHandle) {
	}

	/**
	 * Handles a special resource (non-folder, non-file).
	 * The default implementation does nothing.
	 * 
	 * @param resourceHandle the resource handle of the resource to handle
	 */
	protected void handleSpecial(ResourceHandle resourceHandle) {
	}
	
}
