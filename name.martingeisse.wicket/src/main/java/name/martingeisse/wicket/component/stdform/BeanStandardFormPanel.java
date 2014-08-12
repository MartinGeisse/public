/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.stdform;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * A {@link StandardFormPanel} whose own model refers to a bean that
 * contains most or all properties being edited.
 * 
 * Since property models are built using the supplied bean model,
 * that model cannot be changed after construction.
 * 
 * @param <T> the bean type
 */
public class BeanStandardFormPanel<T> extends AbstractPropertyBasedStandardFormPanel<T> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the bean model
	 * @param stateless whether to use a stateless form
	 */
	public BeanStandardFormPanel(final String id, final IModel<T> model, final boolean stateless) {
		super(id, model, stateless);
	}

	/**
	 * Getter method for the bean.
	 * @return the bean
	 */
	public final T getBean() {
		return getModel().getObject();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#setDefaultModel(org.apache.wicket.model.IModel)
	 */
	@Override
	public final MarkupContainer setDefaultModel(IModel<?> model) {
		throw new UnsupportedOperationException("cannot change BeanStandardFormPanel model after construction");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.onlinecourses.components.stdform.AbstractPropertyBasedStandardFormPanel#createPropertyModel(java.lang.String)
	 */
	@Override
	public <P> PropertyModel<P> createPropertyModel(String propertyName) {
		return new PropertyModel<>(getDefaultModel(), propertyName);
	}
	
}
