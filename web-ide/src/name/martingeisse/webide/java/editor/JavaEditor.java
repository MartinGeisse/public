/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.editor;

import java.nio.charset.Charset;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QFiles;
import name.martingeisse.webide.resources.MarkerData;
import name.martingeisse.webide.workbench.IEditor;

import org.apache.wicket.Component;
import org.apache.wicket.model.PropertyModel;

import com.mysema.query.sql.SQLQuery;

/**
 * {@link IEditor} implementation for the Java source code editor.
 */
public class JavaEditor implements IEditor {

	/**
	 * the workspaceResourcePath
	 */
	private String workspaceResourcePath;
	
	/**
	 * the contents
	 */
	private String contents;
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#initialize(java.lang.String)
	 */
	@Override
	public void initialize(final String workspaceResourcePath) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		final Object resultObject = query.from(QFiles.files).where(QFiles.files.name.eq(workspaceResourcePath)).singleResult(QFiles.files.contents);
		final byte[] encodedContents = (byte[])(((Object[])resultObject)[0]);
		this.workspaceResourcePath = workspaceResourcePath;
		this.contents = new String(encodedContents, Charset.forName("utf-8"));
	}

	/**
	 * Getter method for the contents.
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}
	
	/**
	 * Setter method for the contents.
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(final String id) {
		return new JavaEditorPanel(id, new PropertyModel<String>(this, "contents"), workspaceResourcePath);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#getWorkspaceResourcePath()
	 */
	@Override
	public String getWorkspaceResourcePath() {
		return workspaceResourcePath;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#getDocument()
	 */
	@Override
	public Object getDocument() {
		return contents;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#updateMarkers(java.util.List)
	 */
	@Override
	public void updateMarkers(final List<MarkerData> markers) {
		// TODO: not yet needed -- the java editor panel adds a future for the compilation result and markers itself
	}

}
