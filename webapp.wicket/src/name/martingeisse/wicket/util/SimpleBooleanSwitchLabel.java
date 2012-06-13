/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * This label shows one of two constant values, depending on the value of a boolean model.
 */
public class SimpleBooleanSwitchLabel extends Label {

	/**
	 * the switchModel
	 */
	private IModel<Boolean> switchModel;
	
	/**
	 * the onTrueValue
	 */
	private String onTrueValue;
	
	/**
	 * the onFalseValue
	 */
	private String onFalseValue;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param switchModel the model that switches between the two values
	 * @param onTrueValue the value to show if the switch model returns true
	 * @param onFalseValue the value to show if the switch model returns false
	 */
	public SimpleBooleanSwitchLabel(String id, IModel<Boolean> switchModel, String onTrueValue, String onFalseValue) {
		super(id);
		this.switchModel = switchModel;
		this.onTrueValue = onTrueValue;
		this.onFalseValue = onFalseValue;
		setDefaultModel(new MyModel());
	}

	/**
	 * Specialized IModel implementation for this class. Note that the component cannot
	 * itself implement IModel. Trying to do so confuses the detach() method of
	 * class Component and the IModel interface and causes infinite recursion when
	 * detaching the model/component.
	 */
	private class MyModel implements IModel<String> {

		/* (non-Javadoc)
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		@Override
		public void detach() {
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.model.IModel#getObject()
		 */
		@Override
		public String getObject() {
			return (switchModel.getObject() ? onTrueValue : onFalseValue);
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
		 */
		@Override
		public void setObject(String object) {
		}

	}
}
