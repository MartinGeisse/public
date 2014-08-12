/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.stdform;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 * A {@link StandardFormPanel} whose properties refer to a Java bean. The bean
 * is obtained from and saved to the model object. See
 * {@link BeanStandardFormPanel} for the common case where the model object
 * IS the bean being edited.
 * 
 * Since property models are possibly built using the supplied bean model,
 * that model cannot be changed after construction.
 * 
 * @param <M> the model type
 * @param <B> the bean type
 */
public abstract class BeanObtainingStandardFormPanel<M, B> extends AbstractPropertyBasedStandardFormPanel<M> {

	/**
	 * the beanModel
	 */
	private final IModel<B> beanModel;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the bean model
	 * @param stateless whether to use a stateless form
	 */
	public BeanObtainingStandardFormPanel(final String id, final IModel<M> model, final boolean stateless) {
		super(id, model, stateless);
		this.beanModel = new LoadableDetachableModel<B>() {
			@Override
			protected B load() {
				return loadBean(getModel().getObject());
			}
		};
		addModelUpdateListener(new ModelUpdateListener() {
			@Override
			public void updateModel() {
				saveBean(getModel().getObject(), beanModel.getObject());
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();
		beanModel.detach();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#setDefaultModel(org.apache.wicket.model.IModel)
	 */
	@Override
	public final MarkupContainer setDefaultModel(IModel<?> model) {
		throw new UnsupportedOperationException("cannot change BeanObtainingStandardFormPanel model after construction");
	}

	/**
	 * Creates a model for a property of the bean being edited.
	 * @param propertyName the name of the property
	 * @return the property model
	 */
	@Override
	public <P> PropertyModel<P> createPropertyModel(String propertyName) {
		return new PropertyModel<>(beanModel, propertyName);
	}
	
	/**
	 * Loads the bean, using the provided model object. That object is the same as
	 * returned by the model from {@link #getModel()}.
	 * @return the bean
	 */
	protected abstract B loadBean(M modelObject);

	/**
	 * Saves the bean being edited back to the model object. If the bean as actually an object that
	 * was shared by the model object (instead of copied / newly created), then this method might
	 * be a no-op because all modifications to the bean are "shared back" implicitly.
	 * 
	 * @param modelObject the model object
	 * @param bean the bean to save
	 */
	protected abstract void saveBean(M modelObject, B bean);
	
}
