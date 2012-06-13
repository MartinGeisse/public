/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.form.Form;

/**
 * This panel is the main component for autoforms.
 */
public class AutoformPanel extends AbstractAutoformPanelBase {

	/**
	 * the form
	 */
	private final Form<?> form;

	/**
	 * the keyValueLayout
	 */
	private final KeyValueLayout keyValueLayout;
	
	/**
	 * the submitButtonText
	 */
	private String submitButtonText;

	/**
	 * This constructor invokes the default bean describer implicitly and uses the default property component factory.
	 * @param id the wicket id
	 * @param bean the bean to show in the autoform
	 */
	public AutoformPanel(final String id, final Object bean) {
		this(id, bean, DefaultAutoformPropertyComponentFactory.instance);
	}

	/**
	 * This constructor invokes the default bean describer implicitly.
	 * @param id the wicket id
	 * @param bean the bean to show in the autoform
	 * @param propertyComponentFactory the factory used to create components for the bean properties
	 */
	public AutoformPanel(final String id, final Object bean, final IAutoformPropertyComponentFactory propertyComponentFactory) {
		this(id, bean, DefaultAutoformBeanDescriber.instance, propertyComponentFactory);
	}

	/**
	 * This constructor invokes the specified bean describer implicitly and uses the default property component factory.
	 * @param id the wicket id
	 * @param bean the bean to show in the autoform
	 * @param beanDescriber the bean describer that extracts meta-data from the bean
	 */
	public AutoformPanel(final String id, final Object bean, final IAutoformBeanDescriber beanDescriber) {
		this(id, bean, beanDescriber, DefaultAutoformPropertyComponentFactory.instance);
	}

	/**
	 * This constructor invokes the specified bean describer implicitly.
	 * @param id the wicket id
	 * @param bean the bean to show in the autoform
	 * @param beanDescriber the bean describer that extracts meta-data from the bean
	 * @param propertyComponentFactory the factory used to create components for the bean properties
	 */
	public AutoformPanel(final String id, final Object bean, final IAutoformBeanDescriber beanDescriber, final IAutoformPropertyComponentFactory propertyComponentFactory) {
		this(id, beanDescriber.describe(bean), propertyComponentFactory);
	}

	/**
	 * This constructor uses the default property component factory.
	 * @param id the wicket id
	 * @param beanDescriptor the bean descriptor
	 */
	public AutoformPanel(final String id, final IAutoformBeanDescriptor beanDescriptor) {
		this(id, beanDescriptor, DefaultAutoformPropertyComponentFactory.instance);
	}

	/**
	 * Baseline constructor.
	 * @param id the wicket id
	 * @param beanDescriptor the bean descriptor
	 * @param propertyComponentFactory the factory used to create components for the bean properties
	 */
	public AutoformPanel(final String id, final IAutoformBeanDescriptor beanDescriptor, final IAutoformPropertyComponentFactory propertyComponentFactory) {
		super(id, beanDescriptor);

		this.keyValueLayout = createKeyComponentsLayout("layout", getMainBeanDescriptor(), propertyComponentFactory, getValidationReport());

		form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				super.onSubmit();
				AutoformPanel.this.onSubmitAttempt();
			}
		};
		form.add(keyValueLayout);
		add(form);
		
		form.add(new WebComponent("submitButton") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if (submitButtonText != null) {
					tag.put("value", submitButtonText);
				}
			}
		});
	}

	/**
	 * Getter method for the form.
	 * @return the form
	 */
	public Form<?> getForm() {
		return form;
	}

	/**
	 * Getter method for the keyValueLayout.
	 * @return the keyValueLayout
	 */
	public KeyValueLayout getKeyValueLayout() {
		return keyValueLayout;
	}

	/**
	 * Getter method for the submitButtonText.
	 * @return the submitButtonText
	 */
	public String getSubmitButtonText() {
		return submitButtonText;
	}

	/**
	 * Setter method for the submitButtonText.
	 * @param submitButtonText the submitButtonText to set
	 */
	public void setSubmitButtonText(String submitButtonText) {
		this.submitButtonText = submitButtonText;
	}

}
