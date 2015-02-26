/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic.form;

import java.util.List;

import org.apache.wicket.validation.IValidator;

import com.google.common.collect.ImmutableList;

/**
 * Keeps the common meta-data fields for all form fields.
 */
public final class FormFieldMetadata {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the required
	 */
	private final boolean required;

	/**
	 * the validators
	 */
	private final List<IValidator<?>> validators;
	
	/**
	 * Constructor.
	 * @param name the field name
	 * @param required whether the field value is required to complete the form
	 */
	public FormFieldMetadata(String name, boolean required, List<IValidator<?>> validators) {
		this.name = name;
		this.required = required;
		this.validators = ImmutableList.copyOf(validators);
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the required.
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Getter method for the validators.
	 * @return the validators
	 */
	public List<IValidator<?>> getValidators() {
		return validators;
	}
	
}
