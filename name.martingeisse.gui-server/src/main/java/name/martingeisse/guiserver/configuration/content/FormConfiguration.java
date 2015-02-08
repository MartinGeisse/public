/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.guiserver.gui.ConfigurationDefinedForm;
import name.martingeisse.guiserver.gui.FormDataModel;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

import com.google.common.collect.ImmutableList;

/**
 * Represents a form on a page. The form loads initial
 * field values using models, validates submitted values,
 * and if validation passes, sends the values to a backend
 * URL.
 * 
 * The values read from models are only used to initialize
 * the form fields. Once constructed, the form is backed
 * by a form field object (a {@link Map}) that stores
 * form values, such that modified values are not immediately
 * reflected in other components that pull values from the
 * same models.
 */
public final class FormConfiguration extends AbstractContainerConfiguration implements IConfigurationSnippet {

	/**
	 * the backendUrl
	 */
	private final String backendUrl;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param children the children
	 * @param backendUrl the backend URL
	 */
	public FormConfiguration(String id, ComponentConfigurationList children, String backendUrl) {
		super(id, children);
		this.backendUrl = backendUrl;
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param children the children
	 * @param backendUrl the backend URL
	 */
	public FormConfiguration(String id, ImmutableList<ComponentConfiguration> children, String backendUrl) {
		super(id, children);
		this.backendUrl = backendUrl;
	}

	/**
	 * Getter method for the backendUrl.
	 * @return the backendUrl
	 */
	public String getBackendUrl() {
		return backendUrl;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int handle) {
		this.snippetHandle = handle;
	}
	
	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		HashMap<String, Object> formData = new HashMap<>();
		IModel<Map<String, Object>> formDataModel = new FormDataModel<Map<String, Object>>(formData);
		return new ConfigurationDefinedForm(getId(), this, formDataModel);
	}

}