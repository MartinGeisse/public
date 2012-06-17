/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.wicket.autoform.annotation.AutoformAssociatedValidator;
import name.martingeisse.wicket.autoform.annotation.AutoformValidator;
import name.martingeisse.wicket.autoform.componentfactory.IAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriber;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescription;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription;
import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

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
			
			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<IAutoformPropertyDescription> item) {
				IAutoformPropertyDescription propertyDescription = item.getModelObject();
				item.add(new Label("keyLabel", propertyDescription.getDisplayName()));
				item.add(propertyComponentFactory.createPropertyComponent("valueComponent", propertyDescription, createValidators(propertyDescription), new ValidationErrorAcceptor(item)));
			}
			
		};
		rows.setReuseItems(true);
		form.add(rows);
		
	}
	
	/**
	 *
	 */
	private static class ValidationErrorAcceptor implements IValidationErrorAcceptor {

		/**
		 * the item
		 */
		private ListItem<IAutoformPropertyDescription> item;
		
		/**
		 * Constructor.
		 */
		ValidationErrorAcceptor(ListItem<IAutoformPropertyDescription> item) {
			this.item = item;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor#acceptValidationErrorsFrom(org.apache.wicket.Component)
		 */
		@Override
		public void acceptValidationErrorsFrom(Component component) {
			item.add(new ComponentFeedbackPanel("errorMessage", component));
		}
		
	}
	
	/**
	 * @param property
	 * @return
	 */
	private IValidator<?>[] createValidators(IAutoformPropertyDescription property) {
		List<IValidator<?>> validators = new ArrayList<IValidator<?>>();
		
		// check for AutoformValidator
		AutoformValidator autoformValidatorAnnotation = property.getAnnotation(AutoformValidator.class);
		if (autoformValidatorAnnotation != null) {
			for (Class<? extends IValidator<?>> validatorClass : autoformValidatorAnnotation.value()) {
				try {
					validators.add(validatorClass.newInstance());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		// check for annotations tagged with AutoformAssociatedValidator
		for (Annotation annotation : property.getAnnotations()) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			AutoformAssociatedValidator associatedValidatorAnnotation = annotationType.getAnnotation(AutoformAssociatedValidator.class);
			if (associatedValidatorAnnotation != null) {
				try {
					Class<? extends IValidator<?>> validatorClass = associatedValidatorAnnotation.value();
					Constructor<? extends IValidator<?>> constructor = validatorClass.getConstructor(annotationType);
					validators.add(constructor.newInstance(annotation));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return validators.toArray(new IValidator<?>[validators.size()]);
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
	 * This method is invoked when the user successfully submits the form (i.e. without validation errors).
	 * The default implementation does nothing.
	 */
	protected void onSuccessfulSubmit() {
		System.out.println("*** SUBMIT ***");
	}
	
}
