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
import name.martingeisse.wicket.autoform.annotation.validation.AutoformAssociatedValidator;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformValidator;
import name.martingeisse.wicket.autoform.componentfactory.IAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriber;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriptor;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor;
import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;
import name.martingeisse.wicket.panel.MultiComponentFeedbackPanel;
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
	 * the beanDescriptor
	 */
	private final IAutoformBeanDescriptor beanDescriptor;

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
		this.beanDescriptor = beanDescriber.describe(bean);
		this.propertyComponentFactory = propertyComponentFactory;
		createComponents();
	}

	/**
	 * 
	 */
	private void createComponents() {

		// create the form component
		final Form<?> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				AutoformPanel.this.onSuccessfulSubmit();
			}
		};
		add(form);

		// create the property repeater
		final ListView<IAutoformPropertyDescriptor> rows = new ListView<IAutoformPropertyDescriptor>("rows", beanDescriptor.getPropertyDescriptors()) {

			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(final ListItem<IAutoformPropertyDescriptor> item) {
				final IAutoformPropertyDescriptor propertyDescriptor = item.getModelObject();
				item.add(new Label("keyLabel", propertyDescriptor.getDisplayName()));
				item.add(propertyComponentFactory.createPropertyComponent("valueComponent", propertyDescriptor, createValidators(propertyDescriptor), new ValidationErrorAcceptor(item)));
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
		private final ListItem<IAutoformPropertyDescriptor> item;

		/**
		 * Constructor.
		 */
		ValidationErrorAcceptor(final ListItem<IAutoformPropertyDescriptor> item) {
			this.item = item;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor#acceptValidationErrorsFrom(org.apache.wicket.Component)
		 */
		@Override
		public void acceptValidationErrorsFrom(final Component component) {
			item.add(new ComponentFeedbackPanel("errorMessage", component));
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor#acceptValidationErrorsFromMultiple(org.apache.wicket.Component[])
		 */
		@Override
		public void acceptValidationErrorsFromMultiple(Component... components) {
			item.add(new MultiComponentFeedbackPanel("errorMessage", components));
		}
		
	}

	/**
	 * @param property
	 * @return
	 */
	private IValidator<?>[] createValidators(final IAutoformPropertyDescriptor property) {
		final List<IValidator<?>> validators = new ArrayList<IValidator<?>>();

		// check for AutoformValidator
		final AutoformValidator autoformValidatorAnnotation = property.getAnnotation(AutoformValidator.class);
		if (autoformValidatorAnnotation != null) {
			for (final Class<? extends IValidator<?>> validatorClass : autoformValidatorAnnotation.value()) {
				try {
					validators.add(validatorClass.newInstance());
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		// check for annotations tagged with AutoformAssociatedValidator
		for (final Annotation annotation : property.getAnnotations()) {
			final Class<? extends Annotation> annotationType = annotation.annotationType();
			final AutoformAssociatedValidator associatedValidatorAnnotation = annotationType.getAnnotation(AutoformAssociatedValidator.class);
			if (associatedValidatorAnnotation != null) {
				try {
					final Class<? extends IValidator<?>> validatorClass = associatedValidatorAnnotation.value();
					final Constructor<? extends IValidator<?>> constructor = validatorClass.getConstructor(annotationType);
					if (constructor == null) {
						throw new IllegalStateException("associated validator class " + validatorClass + " has no constructor(" + annotationType + ")");
					}
					validators.add(constructor.newInstance(annotation));
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return validators.toArray(new IValidator<?>[validators.size()]);
	}

	/**
	 * Getter method for the beanDescriptor.
	 * @return the beanDescriptor
	 */
	public IAutoformBeanDescriptor getBeanDescriptor() {
		return beanDescriptor;
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
	}

}
