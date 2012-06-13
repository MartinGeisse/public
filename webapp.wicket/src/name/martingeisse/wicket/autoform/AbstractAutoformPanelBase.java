/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.lang.reflect.AnnotatedElement;

import name.martingeisse.wicket.autoform.annotation.AutoformRequiredProperty;
import name.martingeisse.wicket.autoform.annotation.AutoformStringLengthValidation;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Common base handling for simple autoform and dual autoform handling. This class defines
 * the notion of a "main" bean that is the subject of the autoform. The main bean is also
 * the one being validated.
 */
public abstract class AbstractAutoformPanelBase extends Panel {

	/**
	 * the mainBeanDescriptor
	 */
	private final IAutoformBeanDescription mainBeanDescriptor;

	/**
	 * the validationReport
	 */
	private final ValidationReport validationReport;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param mainBeanDescriptor the main bean descriptor
	 */
	public AbstractAutoformPanelBase(String id, IAutoformBeanDescription mainBeanDescriptor) {
		super(id);
		this.mainBeanDescriptor = mainBeanDescriptor;
		this.validationReport = new ValidationReport();
	}

	/**
	 * Getter method for the mainBeanDescriptor.
	 * @return the mainBeanDescriptor
	 */
	public IAutoformBeanDescription getMainBeanDescriptor() {
		return mainBeanDescriptor;
	}

	/**
	 * Getter method for the validationReport.
	 * @return the validationReport
	 */
	public ValidationReport getValidationReport() {
		return validationReport;
	}

	/**
	 * Sets or clears the displayed error message for a property. This method is a
	 * convenience method for dealing with the map returned by getMainPropertyErrorMessages().
	 * 
	 * Note that autoform validation resets all messages, so any custom message
	 * must be re-added to remain. 
	 * 
	 * @param propertyName the name of the property
	 * @param message the message to show, or null to clear the message for that property
	 */
	public void setErrorMessage(String propertyName, String message) {
		if (message == null) {
			getValidationReport().remove(propertyName);
		} else {
			ValidationError error = new ValidationError(propertyName, message);
			getValidationReport().add(error);
		}
	}

	/**
	 * Creates a key/value layout whose keys are the property names and whose values are components
	 * created by the component factory.
	 * @param id the wicket id
	 * @param beanDescriptor the bean that contains the properties
	 * @param componentFactory the value component factory
	 * @param validationReport the validation report that contains the error messages, or null to show now error messages.
	 */
	protected KeyValueLayout createKeyComponentsLayout(String id, IAutoformBeanDescription beanDescriptor, IAutoformPropertyComponentFactory componentFactory, ValidationReport validationReport) {
		
		/** create the layout **/
		final KeyValueLayout leftLayout = new KeyValueLayout(id);
		for (final IAutoformPropertyDescription propertyDescriptor : beanDescriptor.getPropertyDescriptors()) {
			final Component component = componentFactory.createPropertyComponent(KeyValueLayout.getValueComponentId(), propertyDescriptor);
			final String displayName = propertyDescriptor.getDisplayName();
			if (validationReport == null) {
				leftLayout.addItem(propertyDescriptor.getName(), displayName, component);
			} else {
				// TODO: use a proper validation report entry model
				leftLayout.addItem(propertyDescriptor.getName(), displayName, component, new PropertyModel<String>(validationReport, "validationErrors." + propertyDescriptor.getName() + ".messageKey"));
			}
			propertyDescriptor.setAssignedComponent(component);
		}
		
		return leftLayout;
	}

	/**
	 * This method checks the simplified validation defined for autoforms, and if successful, invokes onSuccessfulSubmit().
	 */
	protected void onSubmitAttempt() {
		onSuccessfulSubmit();
//		getValidationReport().clear();
//		for (final IAutoformPropertyDescription propertyDescriptor : getMainBeanDescriptor().getPropertyDescriptors()) {
//			final AnnotatedElement annotationProvider = propertyDescriptor.getAnnotationProvider();
//
//			if (annotationProvider.getAnnotation(AutoformRequiredProperty.class) != null) {
//				if (propertyDescriptor.getModel().getObject() == null) {
//					setErrorMessage(propertyDescriptor.getName(), "Bitte füllen Sie dieses Feld aus.");
//				}
//			}
//
//			if (annotationProvider.getAnnotation(AutoformStringLengthValidation.class) != null) {
//				if (propertyDescriptor.getType() != String.class) {
//					throw new IllegalStateException("@AutoformStringLengthValidation cannot be attached to other property types than String.");
//				}
//				final String value = (String)propertyDescriptor.getModel().getObject();
//				if (value != null) {
//					final int maxLength = annotationProvider.getAnnotation(AutoformStringLengthValidation.class).value();
//					if (value.length() > maxLength) {
//						setErrorMessage(propertyDescriptor.getName(), "Der eingegebene Wert ist zu lang.");
//					}
//				}
//			}
//
//		}
//		if (getValidationReport().isEmpty()) {
//			onSuccessfulSubmit();
//		}
	}

	/**
	 * This method is invoked when the user successfully submits the form (i.e. without validation errors).
	 * The default implementation does nothing.
	 */
	protected void onSuccessfulSubmit() {
	}

}
