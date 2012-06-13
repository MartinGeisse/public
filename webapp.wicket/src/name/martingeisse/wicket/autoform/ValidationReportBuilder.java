/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * This class is used to conveniently fill a validation report with errors.
 * It keeps the validation report to build as well as a context stack that is
 * used to construct location keys.
 * 
 * Initially, the context stack is empty. Generating an error in this state
 * produces an empty location key. Whenever a context is pushed on the
 * stack, the current location becomes the previous location with a new
 * location key segment appended, separated by a dot (the dot is omitted
 * between the empty context and the first segment). Popping off the stack
 * restores the previous state. This allows to produce location keys that are
 * dot-separated context "paths".
 * 
 * For example, the sequence
 * 		push "foo"
 * 		push "bar"
 * 		pop
 * 		push "baz"
 * 		generate error
 * produces the location key "foo.baz" and stores it in the generated
 * validation error.
 */
public final class ValidationReportBuilder implements Serializable {

	/**
	 * the validation report
	 */
	private final ValidationReport report;

	/**
	 * the context stack. Each element on this stack contains the full location
	 * key to use, such that a validation error can be created by looking just at
	 * the top of stack (instead of storing just one segment per stack element,
	 * which would require to scan the whole stack to create a location key).
	 */
	private final Stack<String> contextStack;

	/**
	 * Creates a new instance with an implicitly created validation report.
	 */
	public ValidationReportBuilder() {
		this(new ValidationReport());
	}

	/**
	 * Creates a new instance with the specified validation report.
	 * @param report the validation report to use. Must not be null.
	 */
	public ValidationReportBuilder(ValidationReport report) {
		if (report == null) {
			throw new IllegalArgumentException("report is null");
		}
		this.report = report;
		this.contextStack = new Stack<String>();
	}

	/**
	 * Getter method for the validation report.
	 * @return the validation report
	 */
	public ValidationReport getReport() {
		return report;
	}

	/**
	 * Enters a new context that is the previous context, augmented by the specified segment.
	 * The new context is pushed onto the context stac and used until popped off the stack again.
	 * @param segment the context segment
	 */
	public void enterContext(String segment) {
		if (segment == null) {
			throw new IllegalArgumentException("segment is null");
		}
		contextStack.push(contextStack.isEmpty() ? segment : (contextStack.peek() + "." + segment));
	}
	
	/**
	 * Pops the topmost context off the context stack
	 * @throws EmptyStackException if the context stack is empty
	 */
	public void leaveContext() throws EmptyStackException {
		contextStack.pop();
	}
	
	/**
	 * Returns the location string that would be used for a validation error if one was created right now.
	 * @return the current location
	 */
	public String getLocation() {
		return contextStack.isEmpty() ? "" : contextStack.peek();
	}
	
	/**
	 * Creates a validation error using the current location and adds it to the report.
	 * @param messageKey the message key to use for the validation error
	 * @param arguments the arguments to use for the validation error
	 */
	public void createErrorForCurrentContext(String messageKey, String... arguments) {
		report.add(new ValidationError(getLocation(), messageKey, arguments));
	}
	
	/**
	 * Creates a validation error using an ad-hoc subcontext and adds it to the report.
	 * This method is equivalent to the sequence
	 * 		enterContext(subContext);
	 * 		createErrorForCurrentContext(messageKey, arguments);
	 * 		leaveContext();
	 * and is provided for convenience only.
	 * @param subContext the ad-hoc subcontext segment
	 * @param messageKey the message key to use for the validation error
	 * @param arguments the arguments to use for the validation error
	 */
	public void createErrorForSubContext(String subContext, String messageKey, String... arguments) {
		enterContext(subContext);
		createErrorForCurrentContext(messageKey, arguments);
		leaveContext();
	}

}
