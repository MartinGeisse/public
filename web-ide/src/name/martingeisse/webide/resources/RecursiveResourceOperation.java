/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.io.File;

/**
 * This class can be used to deal with a whole resource tree
 * recursively in a simple way. It invokes subclass methods
 * for each file and folder.
 */
public class RecursiveResourceOperation {

	/**
	 * Handles the specified resource.
	 * 
	 * @param path the path of the resource to handle
	 */
	public final void handle(ResourcePath path) {
		handle(path, Workspace.map(path));
	}
	
	/**
	 * Handles the specified resource.
	 * 
	 * @param resource the resource to handle
	 */
	public final void handle(File resource) {
		handle(Workspace.map(resource), resource);
	}

	/**
	 * This is the main entry point that invokes this operation
	 * on the specified resource. The default implementation
	 * calls handleResourceByType for the resource.
	 * 
	 * @param path the path of the resource to handle
	 * @param resource the resource to handle
	 */
	protected void handle(ResourcePath path, File resource) {
		handleResourceByType(path, resource);
	}
	
	/**
	 * This method calls one of handleFolder, handleFile or handleSpecial,
	 * depending on the resource's type.
	 * 
	 * @param path the path of the resource to handle
	 * @param resource the resource to handle
	 */
	protected final void handleResourceByType(ResourcePath path, File resource) {
		if (resource.isDirectory()) {
			handleFolder(path, resource);
		} else if (resource.isFile()) {
			handleFile(path, resource);
		} else {
			handleSpecial(path, resource);
		}
	}
	
	/**
	 * Handles a folder. The default implementation invokes handleChildren
	 * to handle all children recursively.
	 * 
	 * @param path the path of the resource to handle
	 * @param folder the folder to handle
	 */
	protected void handleFolder(ResourcePath path, File folder) {
		handleChildren(path, folder);
	}
	
	/**
	 * Invokes handleResource on all children of the specified folder.
	 * 
	 * @param path the path of the resource to handle
	 * @param folder the folder whose children to handle
	 */
	protected final void handleChildren(ResourcePath path, File folder) {
		for (File child : folder.listFiles()) {
			handle(path.appendSegment(child.getName(), false), child);
		}
	}

	/**
	 * Handles a file.
	 * The default implementation does nothing.
	 * 
	 * @param path the path of the resource to handle
	 * @param file the file to handle
	 */
	protected void handleFile(ResourcePath path, File file) {
	}

	/**
	 * Handles a special resource (non-folder, non-file).
	 * The default implementation does nothing.
	 * 
	 * @param path the path of the resource to handle
	 * @param special the special resource to handle
	 */
	protected void handleSpecial(ResourcePath path, File special) {
	}
	
}
