/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;

import name.martingeisse.wicket.autoform.componentfactory.IAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriber;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * Default autoform panel.
 */
public class AutoformPanel extends Panel {

	/**
	 * the beanDescription
	 */
	private final IAutoformBeanDescription beanDescription;
	
	/**
	 * the propertyComponentFactory
	 */
	private final IAutoformPropertyComponentFactory propertyComponentFactory;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param bean the bean to show in the autoform
	 * @param beanDescriber the bean describer that extracts meta-data from the bean
	 * @param propertyComponentFactory the factory used to create components for the bean properties
	 */
	public AutoformPanel(final String id, final Serializable bean, final IAutoformBeanDescriber beanDescriber, final IAutoformPropertyComponentFactory propertyComponentFactory) {
		super(id, Model.of(bean));
		this.beanDescription = beanDescriber.describe(bean);
		this.propertyComponentFactory = propertyComponentFactory;
		createComponents();
	}
	
	/**
	 * 
	 */
	private void createComponents() {
		
		// create the form component
		Form<?> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				AutoformPanel.this.onSuccessfulSubmit();
			}
		};
		add(form);

		// create the property repeater
		ListView<IAutoformPropertyDescription> rows = new ListView<IAutoformPropertyDescription>("rows", beanDescription.getPropertyDescriptions()) {
			@Override
			protected void populateItem(ListItem<IAutoformPropertyDescription> item) {
				IAutoformPropertyDescription propertyDescription = item.getModelObject();
				item.add(new Label("errorMessage", "*** DUMMY ERROR MESSAGE ***"));
				item.add(new Label("keyLabel", propertyDescription.getDisplayName()));
				item.add(propertyComponentFactory.createPropertyComponent("valueComponent", propertyDescription));
			}
		};
		form.add(rows);
		
	}
	
	/**
	 * Getter method for the beanDescription.
	 * @return the beanDescription
	 */
	public IAutoformBeanDescription getBeanDescription() {
		return beanDescription;
	}
	
	/**
	 * @return the form used by this autoform panel
	 */
	public Form<?> getForm() {
		return (Form<?>)get("form");
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
//	public void setErrorMessage(String propertyName, String message) {
//		if (message == null) {
//			getValidationReport().remove(propertyName);
//		} else {
//			ValidationError error = new ValidationError(propertyName, message);
//			getValidationReport().add(error);
//		}
//	}	
	
	/**
	 * Creates a key/value layout whose keys are the property names and whose values are components
	 * created by the component factory.
	 * @param id the wicket id
	 * @param beanDescriptor the bean that contains the properties
	 * @param componentFactory the value component factory
	 * @param validationReport the validation report that contains the error messages, or null to show now error messages.
	 */
//	protected KeyValueLayout createKeyComponentsLayout(String id, IAutoformBeanDescription beanDescriptor, IAutoformPropertyComponentFactory componentFactory, ValidationReport validationReport) {
//		
//		/** create the layout **/
//		final KeyValueLayout leftLayout = new KeyValueLayout(id);
//		for (final IAutoformPropertyDescription propertyDescriptor : beanDescriptor.getPropertyDescriptors()) {
//			final Component component = componentFactory.createPropertyComponent(KeyValueLayout.getValueComponentId(), propertyDescriptor);
//			final String displayName = propertyDescriptor.getDisplayName();
//			if (validationReport == null) {
//				leftLayout.addItem(propertyDescriptor.getName(), displayName, component);
//			} else {
//				// TODO: use a proper validation report entry model
//				leftLayout.addItem(propertyDescriptor.getName(), displayName, component, new PropertyModel<String>(validationReport, "validationErrors." + propertyDescriptor.getName() + ".messageKey"));
//			}
//			propertyDescriptor.setAssignedComponent(component);
//		}
//		
//		return leftLayout;
//	}

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
		System.out.println("*** SUBMIT ***");
	}
	
}
