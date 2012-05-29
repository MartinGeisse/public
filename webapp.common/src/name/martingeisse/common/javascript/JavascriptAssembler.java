/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript;

/**
 * This class is used to assemble Javascript source code.
 */
public class JavascriptAssembler extends SourceCodeAssembler {

	/**
	 * 
	 */
	private boolean firstCollectionElement;
	
	/**
	 * Constructor.
	 */
	public JavascriptAssembler() {
		firstCollectionElement = false;
	}

	/**
	 * Appends the specified identifier to the builder.
	 * @param name the identifier to append
	 */
	public void appendIdentifier(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name argument is null");
		}
		JavascriptAssemblerUtil.appendIdentifier(getBuilder(), name);
	}
	
	/**
	 * Appends the null literal to the builder.
	 */
	public void appendNullLiteral() {
		getBuilder().append("null");
	}
	
	/**
	 * Appends the specified string literal to the builder.
	 * @param value the value of the literal to append (must not be null)
	 */
	public void appendStringLiteral(String value) {
		if (value == null) {
			throw new IllegalArgumentException("value argument is null");
		}
		JavascriptAssemblerUtil.appendStringLiteral(getBuilder(), value);
	}
	
	/**
	 * Appends the specified string literal to the builder, or the null literal if
	 * the argument is null.
	 * @param value the value of the literal to append (may be null)
	 */
	public void appendStringLiteralOrNull(String value) {
		if (value == null) {
			appendNullLiteral();
		} else {
			JavascriptAssemblerUtil.appendStringLiteral(getBuilder(), value);
		}
	}
	
	/**
	 * Appends the specified boolean literal to the builder.
	 * @param value the value of the literal to append
	 */
	public void appendBooleanLiteral(boolean value) {
		JavascriptAssemblerUtil.appendBooleanLiteral(getBuilder(), value);
	}

	/**
	 * Appends the specified numeric literal to the builder.
	 * @param value the value of the literal to append
	 */
	public void appendNumericLiteral(int value) {
		getBuilder().append(value);
	}

	/**
	 * Appends the specified numeric literal to the builder.
	 * @param value the value of the literal to append
	 */
	public void appendNumericLiteral(double value) {
		getBuilder().append(value);
	}

	/**
	 * Appends the specified numeric literal to the builder.
	 * @param value the value of the literal to append
	 */
	public void appendNumericLiteral(Number value) {
		if (value == null) {
			throw new IllegalArgumentException("value argument is null");
		}
		getBuilder().append(value.toString());
	}

	/**
	 * Appends the specified object property name to the builder.
	 * Note that no escaping is done for the name, so the name
	 * must conform to identifier syntax.
	 * @param name the name of the property
	 */
	public void appendPropertyName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name argument is null");
		}
		JavascriptAssemblerUtil.appendIdentifier(getBuilder(), name);
	}

	/**
	 * Common handling to begin a list or object.
	 * @param mark the punctuation mark to use
	 */
	private void beginCollection(char mark) {
		getBuilder().append(mark);
		firstCollectionElement = true;
	}
	
	/**
	 * Common handling to end a list or object.
	 * @param mark the punctuation mark to use
	 */
	private void endCollection(char mark) {
		getBuilder().append(mark);
		
		/**
		 * This statement is necessary to handle the special
		 * case that the first element of a collection is an
		 * empty collection. Without this statement, the inner
		 * collection sets the first-flag to true and never resets
		 * it since it is empty. The outer collection then
		 * misinterprets this and omits the comma for the second
		 * element.
		 */
		firstCollectionElement = false;
	}

	/**
	 * Common handling to start a new collection element.
	 * This method handles comma-separation of elements.
	 */
	private void prepareCollectionElement() {
		if (firstCollectionElement) {
			firstCollectionElement = false;
		} else {
			getBuilder().append(", ");
		}
	}
	
	/**
	 * Begins a new list expression.
	 */
	public void beginList() {
		beginCollection('[');
	}
	
	/**
	 * Ends the current list expression.
	 */
	public void endList() {
		endCollection(']');
	}
	
	/**
	 * This method must be called before each list element.
	 */
	public void prepareListElement() {
		prepareCollectionElement();
	}
	
	/**
	 * Begins a new object expression.
	 */
	public void beginObject() {
		beginCollection('{');
	}
	
	/**
	 * Ends the current object expression.
	 */
	public void endObject() {
		endCollection('}');
	}
	
	/**
	 * This method must be called before each object property.
	 * @param name the name of the property
	 */
	public void prepareObjectProperty(String name) {
		prepareCollectionElement();
		appendPropertyName(name);
		getBuilder().append(": ");
	}

	/**
	 * Begins a nested variable scope by using an anonymous function.
	 * This is useful in two ways. First, it shields the outer scope
	 * from the variables defined in the inner scope.
	 * 
	 * Second, it creates a new scope each time the scope statement is
	 * executed, which is necessary to capture the state of the current
	 * iteration in a for loop when using a closure within the loop.
	 * (Blocks, and especially the iteration block of a for loop, do
	 * not create nested variable scopes themselves in Javascript). The
	 * correct way to use a nested scope for this purpose is to use
	 * this method within the for loop, define variables for all state
	 * to be captured, initialize them with values from the outer scope,
	 * then define the actual closure and have if refer only to variables
	 * from the nested scope.
	 * 
	 * Consider the following nonworking example:
	 * 
	 * functions = [];
	 * for (var i=0; i<3; i++) {
	 *   var value = i;
	 *     functions[i] = function() {
	 *       alert('value: ' + value);
	 *   }
	 * }
	 * 
	 * Even though the value of i is copied into the "value" variable,
	 * the for loop does not create a nested scope on its own, so "value"
	 * refers to the same variable in all three iterations, hence all
	 * functions output the value 2 which is the last value written into
	 * the "value" variable.
	 * 
	 * The following example works:
	 * 
	 * functions = [];
	 * for (var i=0; i<3; i++) {
	 * 
	 *   // this line is generated by beginNestedScope()
	 *   (function() {
	 * 
	 *     // the caller must manually copy all state that shall be captured
	 *     // into variables from the nested scope ...
	 *     var value = i;
	 *     
	 *     // ... then build the actual closure
	 *     functions[i] = function() {
	 *       alert('value: ' + value);
	 *     }
	 *     
	 *   // this line is generated by endNestedScope()
	 *   })();
	 *   
	 * }
	 * 
	 * This method creates complete lines including indentation and
	 * also increments indentation for the nested scope.
	 */
	public void beginNestedScope() {
		appendIndentedLine("(function() {");
		incrementIndentation();
	}
	
	/**
	 * Ends a nested scope started by beginNestedScope(). See that method for
	 * a discussion of the purpose of nested scopes.
	 */
	public void endNestedScope() {
		decrementIdentation();
		appendIndentedLine("})();");
	}
	
}
