/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

/**
 * This expression accesses a field of an object.
 */
public class FieldAccess extends AbstractVariableExpression {

	/**
	 * the objectExpression
	 */
	private final Expression objectExpression;
	
	/**
	 * the fieldName
	 */
	private final String fieldName;

	/**
	 * Constructor.
	 * @param objectExpression the expression that determines the object that contains the field
	 * @param fieldName the name of the field to access
	 */
	public FieldAccess(Expression objectExpression, String fieldName) {
		this.objectExpression = objectExpression;
		this.fieldName = fieldName;
	}

	/**
	 * Getter method for the objectExpression.
	 * @return the objectExpression
	 */
	public Expression getObjectExpression() {
		return objectExpression;
	}

	/**
	 * Getter method for the fieldName.
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	
}
