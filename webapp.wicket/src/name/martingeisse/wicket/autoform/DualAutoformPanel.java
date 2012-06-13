/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import org.apache.wicket.markup.html.form.Form;

/**
 * This panel shows a read/write "destination" autoform and a
 * read-only "source" autoform side-by-side.
 */
public class DualAutoformPanel extends AbstractAutoformPanelBase {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param destinationBean the read-write destination bean to show in the autoform
	 * @param destinationBeanDescriber the bean describer that extracts meta-data from the destination bean
	 * @param destinationPropertyComponentFactory the factory used to create components for the destination bean properties
	 * @param sourceBean the read-only source bean to show in the autoform
	 * @param sourceBeanDescriber the bean describer that extracts meta-data from the source bean
	 * @param sourcePropertyComponentFactory the factory used to create components for the source bean properties
	 */
	public DualAutoformPanel(String id,
			Object destinationBean, IAutoformBeanDescriber destinationBeanDescriber, IAutoformPropertyComponentFactory destinationPropertyComponentFactory,
			Object sourceBean, IAutoformBeanDescriber sourceBeanDescriber, IAutoformPropertyComponentFactory sourcePropertyComponentFactory) {
		super(id, destinationBeanDescriber.describe(destinationBean));
		IAutoformBeanDescriptor sourceBeanDescriptor = createSourceBeanDescriptor(sourceBean, sourceBeanDescriber);
		
		Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				super.onSubmit();
				DualAutoformPanel.this.onSubmitAttempt();
			}
		};
		form.add(createKeyComponentsLayout("leftLayout", getMainBeanDescriptor(), destinationPropertyComponentFactory, getValidationReport()));
		form.add(createKeyComponentsLayout("rightLayout", sourceBeanDescriptor, sourcePropertyComponentFactory, null));
		add(form);
	}
	
	private IAutoformBeanDescriptor createSourceBeanDescriptor(Object sourceBean, IAutoformBeanDescriber sourceBeanDescriber) {
		IAutoformBeanDescriptor sourceBeanDescriptor = sourceBeanDescriber.describe(sourceBean);
		for (IAutoformPropertyDescriptor propertyDescriptor : sourceBeanDescriptor.getPropertyDescriptors()) {
			propertyDescriptor.setReadOnlyOverride(true);
		}
		return sourceBeanDescriptor;
	}

}
