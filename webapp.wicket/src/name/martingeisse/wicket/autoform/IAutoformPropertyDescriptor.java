/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

import name.martingeisse.common.terms.DisplayName;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This interface is used by the autoform classes to get information about
 * bean properties.
 */
public interface IAutoformPropertyDescriptor extends Serializable {

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName();

	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public Class<?> getType();

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	public IModel<?> getModel();
	
	/**
	 * Getter method for the bean model.
	 * @return the bean model
	 */
	public IModel<?> getBeanModel();

	/**
	 * Getter method for the readOnly.
	 * @return the readOnly
	 */
	public boolean isReadOnly();

	/**
	 * Getter method for the readOnlyOverride.
	 * @return the readOnlyOverride
	 */
	public Boolean getReadOnlyOverride();

	/**
	 * Setter method for the readOnlyOverride.
	 * @param readOnlyOverride the readOnlyOverride to set
	 */
	public void setReadOnlyOverride(Boolean readOnlyOverride);

	/**
	 * Getter method for the displayNameOverride.
	 * @return the displayNameOverride
	 */
	public String getDisplayNameOverride();

	/**
	 * Setter method for the displayNameOverride.
	 * @param displayNameOverride the displayNameOverride to set
	 */
	public void setDisplayNameOverride(String displayNameOverride);

	/**
	 * Getter method for the annotationProvider.
	 * @return the annotationProvider
	 */
	public AnnotatedElement getAnnotationProvider();

	/**
	 * @return the user-visible name of the property, respecting any {@link DisplayName} annotation if present
	 */
	public String getDisplayName();

	/**
	 * Getter method for the assignedComponent.
	 * @return the assignedComponent
	 */
	public Panel getAssignedComponent();

	/**
	 * Setter method for the assignedComponent.
	 * @param assignedComponent the assignedComponent to set
	 */
	public void setAssignedComponent(Panel assignedComponent);

}
