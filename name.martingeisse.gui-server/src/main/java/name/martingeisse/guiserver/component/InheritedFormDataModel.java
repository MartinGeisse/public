/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.component;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

/**
 * The inherited version of {@link FormDataModel}.
 */
public final class InheritedFormDataModel<T> extends AbstractPropertyModel<T> implements IWrapModel<T> {

	/**
	 * the owner
	 */
	private final Component owner;

	/**
	 * Constructor.
	 * @param wrappedModel the wrapped model
	 * @param owner the component that inherited this model
	 */
	public InheritedFormDataModel(IModel<?> wrappedModel, Component owner) {
		super(wrappedModel);
		this.owner = owner;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IWrapModel#getWrappedModel()
	 */
	@Override
	public IModel<?> getWrappedModel() {
		return getChainedModel();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractPropertyModel#propertyExpression()
	 */
	@Override
	protected String propertyExpression() {
		List<FieldPathBehavior> behaviors = owner.getBehaviors(FieldPathBehavior.class);
		if (behaviors.size() != 1) {
			throw new RuntimeException("expected exactly one FieldPathBehavior");
		}
		FieldPathBehavior behavior = behaviors.get(0);
		return behavior.getPath();
	}

}
