/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.customization.Main.MyMaxLength;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 *
 */
public class MyValidator implements IValidator<String> {

	/**
	 * the maxLength
	 */
	private int maxLength;
	
	/**
	 * Constructor.
	 */
	public MyValidator() {
		this(5);
	}
	
	/**
	 * Constructor.
	 * @param a the annotation
	 */
	public MyValidator(MyMaxLength a) {
		this(a.value());
	}
	
	/**
	 * Constructor.
	 * @param maxLength ...
	 */
	public MyValidator(int maxLength) {
		this.maxLength = maxLength;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void validate(IValidatable<String> validatable) {
		String value = validatable.getValue();
		if (value.length() > maxLength) {
			ValidationError error = new ValidationError();
			error.addMessageKey("max.length");
			validatable.error(error);
		}
	}
	
}
