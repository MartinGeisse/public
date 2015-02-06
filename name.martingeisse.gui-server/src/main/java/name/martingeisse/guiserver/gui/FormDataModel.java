/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IWrapModel;

/**
 * The model used for form fields. This is similar to a {@link CompoundPropertyModel},
 * but the field path isn't taken from the component ID but from a {@link FieldPathBehavior}.
 */
public final class FormDataModel<T> extends AbstractReadOnlyModel<T> implements IComponentInheritedModel<T> {

	/**
	 * the value
	 */
	private final T value;
	
	/**
	 * Constructor.
	 * @param value the model value
	 */
	public FormDataModel(T value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public T getObject() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IComponentInheritedModel#wrapOnInheritance(org.apache.wicket.Component)
	 */
	@Override
	public <W> IWrapModel<W> wrapOnInheritance(Component component) {
		return new InheritedFormDataModel<W>(this, component);
	}

}
