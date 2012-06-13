/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;

/**
 * Implementations know how to validate objects. An object is validated
 * by passing it to the validate() method of this interface, along with
 * a {@link ValidationReportBuilder} that is used to log validation
 * errors. The current context of the report builder is expected to
 * correspond to the object passed to this validator.
 * 
 * A validator creates an error in the validation report for every error
 * it encounters. If no error is created for a specific location, then
 * that location is valid. If the whole report is empty, validation
 * has succeeded.
 * 
 * Validators should in general be stateless, so they can be re-used
 * concurrently (even in different threads at the same time). However, 
 * this requirement is strict only when validators are used within the
 * validation framework (for example, when they are called from validation
 * annotations). For purely local validation, validators could be stateful.
 * It is still recommended to make them stateless for the sake of
 * uniformity.
 * 
 * Validators distinguish between 'expected' and 'unexpected' errors during
 * validation. Basically, a validator expects the object to validate to
 * contain certain types of errors, but not any type of error. For example,
 * a validator that checks the minimum and maximum length of strings would
 * distinguish between:
 * - valid inputs, i.e. strings with an accepted length
 * - invalid inputs with expected errors, e.g. strings with an invalid length
 * - unexpected errors, e.g. a null argument or an object that is not a string
 * Validators will log expected errors to the validation log, but throw
 * an exception for unexpected errors. If such an exception is thrown, the
 * validation report must be treated as incomplete, since the whole validation
 * process was interrupted. The caller should make sure that such errors do
 * not occur, or at least treat them differently from validation errors.
 */
public interface IValidator extends Serializable {

	/**
	 * Validates the specified object.
	 * @param objectToValidate the object to validate. Must not be null.
	 * @param validationReportBuilder the validation report builder that is used to log validation
	 * errors encountered during validation. Must not be null.
	 */
	public void validate(Object objectToValidate, ValidationReportBuilder validationReportBuilder);
	
}
