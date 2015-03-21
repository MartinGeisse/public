/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.halp.project;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.halp.Problem;

/**
 * Represents the problem of creating a programming project.
 */
public class CreateProjectProblem implements Problem {

	/**
	 * the rootFolder
	 */
	private File rootFolder = new File(".");

	/**
	 * the ide
	 */
	private String ide;

	/**
	 * the sourceControl
	 */
	private String sourceControl;

	/**
	 * the aspects
	 */
	private Map<String, Object> aspects = new HashMap<String, Object>();

	/**
	 * Getter method for the rootFolder.
	 * @return the rootFolder
	 */
	public File getRootFolder() {
		return rootFolder;
	}

	/**
	 * Setter method for the rootFolder.
	 * @param rootFolder the rootFolder to set
	 */
	public void setRootFolder(File rootFolder) {
		this.rootFolder = rootFolder;
	}

	/**
	 * Getter method for the ide.
	 * @return the ide
	 */
	public String getIde() {
		return ide;
	}

	/**
	 * Setter method for the ide.
	 * @param ide the ide to set
	 */
	public void setIde(String ide) {
		this.ide = ide;
	}

	/**
	 * Getter method for the sourceControl.
	 * @return the sourceControl
	 */
	public String getSourceControl() {
		return sourceControl;
	}

	/**
	 * Setter method for the sourceControl.
	 * @param sourceControl the sourceControl to set
	 */
	public void setSourceControl(String sourceControl) {
		this.sourceControl = sourceControl;
	}

	/**
	 * Getter method for the aspects.
	 * @return the aspects
	 */
	public Map<String, Object> getAspects() {
		return aspects;
	}

	/**
	 * Setter method for the aspects.
	 * @param aspects the aspects to set
	 */
	public void setAspects(Map<String, Object> aspects) {
		this.aspects = aspects;
	}

}
