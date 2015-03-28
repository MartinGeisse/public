/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.basic.form;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.component.ConfigurationDefinedForm;
import name.martingeisse.guiserver.component.FormDataModel;
import name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

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
@StructuredElement
@RegisterComponentElement(localName = "form")
public class FormConfiguration extends AbstractSingleContainerConfiguration implements IConfigurationSnippet {

	/**
	 * the backendUrl
	 */
	private String backendUrl;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Getter method for the backendUrl.
	 * @return the backendUrl
	 */
	public String getBackendUrl() {
		return backendUrl;
	}
	
	/**
	 * Setter method for the backendUrl.
	 * @param backendUrl the backendUrl to set
	 */
	@BindAttribute(name = "backendUrl")
	public void setBackendUrl(String backendUrl) {
		this.backendUrl = backendUrl;
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
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#assembleContainerIntro(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	protected void assembleContainerIntro(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		writeOpeningComponentTag(assembler, "form");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		HashMap<String, Object> formData = new HashMap<>();
		IModel<Map<String, Object>> formDataModel = new FormDataModel<Map<String, Object>>(formData);
		return new ConfigurationDefinedForm(getComponentId(), this, formDataModel);
	}

}
