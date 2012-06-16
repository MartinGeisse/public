/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.validation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A validation report is the result of a validation run. It contains
 * a set of validation errors, indexed by their location.
 * 
 * At most one error can exist for any one location. Trying to add
 * another validation error to a report for a location that already
 * has an error in that report is allowed but has no effect. In other
 * words, a validation report contains the first error added for each
 * location; later errors are lost.
 * 
 * A validation report can be cleared and re-used. This makes it possible
 * to use a shared validation report object to communicate validation
 * errors to other program modules. A validation report can also be used
 * multiple times, accumulation errors from different validation runs.
 */
public class ValidationReport implements Iterable<ValidationError>, Serializable {

	/**
	 * the validation errors, indexed by location key.
	 */
	private Map<String, ValidationError> validationErrors;
	
	/**
	 * Constructor.
	 */
	public ValidationReport() {
		this.validationErrors = new HashMap<String, ValidationError>();
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ValidationError> iterator() {
		return validationErrors.values().iterator();
	}

	/**
	 * Tries to add the specified validation error to this report. The argument must not be null.
	 * 
	 * If there is already a validation error in this report for the location key of the
	 * argument, then this validation report remains unchanged and nothing happens.
	 * Otherwise, the argument is added to this report.
	 * 
	 * @param validationError the validation error to add
	 * @return Returns true if the argument was added, i.e. if this report did not previously
	 * contain any validation error for the location key of the argument. Returns false if there
	 * was already an error for that location key, indicating that this report was not modified.
	 */
	public boolean add(ValidationError validationError) {
		if (validationError == null) {
			throw new IllegalArgumentException("validationError is null");
		}
		if (validationErrors.get(validationError.getLocationKey()) == null) {
			validationErrors.put(validationError.getLocationKey(), validationError);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the validation error stored for the specified location, or null if no such
	 * validation error is stored in this report.
	 * @param location the location key used to find the validation error
	 * @return the validation error, or null.
	 */
	public ValidationError get(String location) {
		return validationErrors.get(location);
	}

	/**
	 * Clears this validation report, removing all validation errors.
	 */
	public void clear() {
		validationErrors.clear();
	}
	
	/**
	 * Returns true if and only if this report is empty.
	 * @return whether this report is empty.
	 */
	public boolean isEmpty() {
		return validationErrors.isEmpty();
	}

	/**
	 * Removes the validation error stored for the specified location, if any.
	 * @param location the location where the validation error shall be removed
	 */
	public void remove(String location) {
		validationErrors.remove(location);
	}
	
}
