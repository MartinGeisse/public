/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This class is used to assemble Javascript source code.
 */
public class JavascriptAssembler extends SourceCodeAssembler {

	/**
	 * Application code can set this field to install a default date
	 * formatter used by new instances of {@link JavascriptAssembler}.
	 * Note however that access to this field is not synchronized automatically.
	 */
	public static DateTimeFormatter defaultDateFormatter = DateTimeFormat.mediumDate().withZone(DateTimeZone.UTC);

	/**
	 * Application code can set this field to install a default datetime
	 * formatter used by new instances of {@link JavascriptAssembler}.
	 * Note however that access to this field is not synchronized automatically.
	 */
	public static DateTimeFormatter defaultDateTimeFormatter = DateTimeFormat.mediumDateTime().withZone(DateTimeZone.UTC);

	/**
	 * 
	 */
	private boolean firstCollectionElement;

	/**
	 * the dateFormatter
	 */
	private DateTimeFormatter dateFormatter;

	/**
	 * the dateFormatter
	 */
	private DateTimeFormatter dateTimeFormatter;

	/**
	 * Constructor.
	 */
	public JavascriptAssembler() {
		firstCollectionElement = false;
		dateFormatter = defaultDateFormatter;
		dateTimeFormatter = defaultDateTimeFormatter;
	}

	/**
	 * Getter method for the dateFormatter.
	 * @return the dateFormatter
	 */
	public DateTimeFormatter getDateFormatter() {
		return dateFormatter;
	}

	/**
	 * Setter method for the dateFormatter.
	 * @param dateFormatter the dateFormatter to set
	 */
	public void setDateFormatter(DateTimeFormatter dateFormatter) {
		this.dateFormatter = dateFormatter;
	}

	/**
	 * Getter method for the dateTimeFormatter.
	 * @return the dateTimeFormatter
	 */
	public DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatter;
	}

	/**
	 * Setter method for the dateTimeFormatter.
	 * @param dateTimeFormatter the dateTimeFormatter to set
	 */
	public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter = dateTimeFormatter;
	}
	
	/**
	 * Appends the specified code fragment to the builder.
	 * @param code the code fragment to append
	 * @return this
	 */
	public final JavascriptAssembler append(String code) {
		if (code == null) {
			throw new IllegalArgumentException("code argument is null");
		}
		getBuilder().append(code);
		return this;
	}
	
	/**
	 * Appends the specified identifier to the builder.
	 * @param name the identifier to append
	 * @return this
	 */
	public final JavascriptAssembler appendIdentifier(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name argument is null");
		}
		JavascriptAssemblerUtil.appendIdentifier(getBuilder(), name);
		return this;
	}

	/**
	 * Appends the null literal to the builder.
	 * @return this
	 */
	public final JavascriptAssembler appendNullLiteral() {
		getBuilder().append("null");
		return this;
	}

	/**
	 * Appends the specified string literal to the builder.
	 * @param value the value of the literal to append (must not be null)
	 * @return this
	 */
	public final JavascriptAssembler appendStringLiteral(String value) {
		if (value == null) {
			throw new IllegalArgumentException("value argument is null");
		}
		JavascriptAssemblerUtil.appendStringLiteral(getBuilder(), value);
		return this;
	}

	/**
	 * Appends the specified string literal to the builder, or the null literal if
	 * the argument is null.
	 * @param value the value of the literal to append (may be null)
	 * @return this
	 */
	public final JavascriptAssembler appendStringLiteralOrNull(String value) {
		if (value == null) {
			appendNullLiteral();
		} else {
			JavascriptAssemblerUtil.appendStringLiteral(getBuilder(), value);
		}
		return this;
	}

	/**
	 * Appends the specified boolean literal to the builder.
	 * @param value the value of the literal to append
	 * @return this
	 */
	public final JavascriptAssembler appendBooleanLiteral(boolean value) {
		JavascriptAssemblerUtil.appendBooleanLiteral(getBuilder(), value);
		return this;
	}

	/**
	 * Appends the specified numeric literal to the builder.
	 * @param value the value of the literal to append
	 * @return this
	 */
	public final JavascriptAssembler appendNumericLiteral(int value) {
		getBuilder().append(value);
		return this;
	}

	/**
	 * Appends the specified numeric literal to the builder.
	 * @param value the value of the literal to append
	 * @return this
	 */
	public final JavascriptAssembler appendNumericLiteral(long value) {
		getBuilder().append(value);
		return this;
	}

	/**
	 * Appends the specified numeric literal to the builder.
	 * @param value the value of the literal to append
	 * @return this
	 */
	public final JavascriptAssembler appendNumericLiteral(double value) {
		getBuilder().append(value);
		return this;
	}

	/**
	 * Appends the specified numeric literal to the builder.
	 * @param value the value of the literal to append
	 * @return this
	 */
	public final JavascriptAssembler appendNumericLiteral(Number value) {
		if (value == null) {
			throw new IllegalArgumentException("value argument is null");
		}
		getBuilder().append(value.toString());
		return this;
	}

	/**
	 * Appends the specified numeric literal to the builder, or the null literal if
	 * the argument is null.
	 * @param value the value of the literal to append (may be null)
	 * @return this
	 */
	public final JavascriptAssembler appendNumericLiteralOrNull(Number value) {
		if (value == null) {
			appendNullLiteral();
		} else {
			appendNumericLiteral(value);
		}
		return this;
	}
	
	/**
	 * Appends the date contained in the specified instant according
	 * to the date formatter that is set for this assembler.
	 * @param value the instant to extract the date from
	 * @return this
	 */
	public final JavascriptAssembler appendDateLiteral(ReadableInstant value) {
		appendJodaLiteral(value, dateFormatter);
		return this;
	}

	/**
	 * Appends the date contained in the specified instant according
	 * to the date formatter that is set for this assembler,
	 * or the null literal if the argument is null.
	 * @param value the instant to extract the date from (may be null)
	 * @return this
	 */
	public final JavascriptAssembler appendDateLiteralOrNull(ReadableInstant value) {
		appendJodaLiteralOrNull(value, dateFormatter);
		return this;
	}
	
	/**
	 * Appends the datetime contained in the specified instant according
	 * to the datetime formatter that is set for this assembler.
	 * @param value the instant to extract the datetime from
	 * @return this
	 */
	public final JavascriptAssembler appendDateTimeLiteral(ReadableInstant value) {
		appendJodaLiteral(value, dateTimeFormatter);
		return this;
	}
	
	/**
	 * Appends the datetime contained in the specified instant according
	 * to the datetime formatter that is set for this assembler,
	 * or the null literal if the argument is null.
	 * @param value the instant to extract the datetime from (may be null)
	 * @return this
	 */
	public final JavascriptAssembler appendDateTimeLiteralOrNull(ReadableInstant value) {
		appendJodaLiteralOrNull(value, dateTimeFormatter);
		return this;
	}

	/**
	 * Uses the specified Joda-Time instant and formatter to append a string literal.
	 * 
	 * @param value the instant to append
	 * @param formatter the formatter used to turn the instant into a string
	 * @return this
	 */
	public final JavascriptAssembler appendJodaLiteral(ReadableInstant value, DateTimeFormatter formatter) {
		if (value == null) {
			throw new IllegalArgumentException("value argument is null");
		}
		appendStringLiteral(formatter.print(value));
		return this;
	}

	/**
	 * Uses the specified Joda-Time instant and formatter to append a string literal,
	 * or the null literal if the argument is null.
	 * 
	 * @param value the instant to append (may be null)
	 * @param formatter the formatter used to turn the instant into a string
	 * @return this
	 */
	public final JavascriptAssembler appendJodaLiteralOrNull(ReadableInstant value, DateTimeFormatter formatter) {
		if (value == null) {
			appendNullLiteral();
		} else {
			appendStringLiteral(formatter.print(value));
		}
		return this;
	}
	
	/**
	 * Appends the specified date according to the date formatter
	 * that is set for this assembler.
	 * @param value the date
	 * @return this
	 */
	public final JavascriptAssembler appendDateLiteral(ReadablePartial value) {
		appendJodaLiteral(value, dateFormatter);
		return this;
	}
	
	/**
	 * Appends the specified date according to the date formatter
	 * that is set for this assembler,
	 * or the null literal if the argument is null.
	 * @param value the date (may be null)
	 * @return this
	 */
	public final JavascriptAssembler appendDateLiteralOrNull(ReadablePartial value) {
		appendJodaLiteralOrNull(value, dateFormatter);
		return this;
	}

	/**
	 * Appends the specified datetime according to the datetime formatter
	 * that is set for this assembler.
	 * @param value the datetime
	 * @return this
	 */
	public final JavascriptAssembler appendDateTimeLiteral(ReadablePartial value) {
		appendJodaLiteral(value, dateTimeFormatter);
		return this;
	}
	
	/**
	 * Appends the specified datetime according to the datetime formatter
	 * that is set for this assembler,
	 * or the null literal if the argument is null.
	 * @param value the datetime (may be null)
	 * @return this
	 */
	public final JavascriptAssembler appendDateTimeLiteralOrNull(ReadablePartial value) {
		appendJodaLiteralOrNull(value, dateTimeFormatter);
		return this;
	}

	/**
	 * Uses the specified Joda-Time partial and formatter to append a string literal.
	 * 
	 * @param value the partial to append
	 * @param formatter the formatter used to turn the partial into a string
	 * @return this
	 */
	public final JavascriptAssembler appendJodaLiteral(ReadablePartial value, DateTimeFormatter formatter) {
		if (value == null) {
			throw new IllegalArgumentException("value argument is null");
		}
		appendStringLiteral(formatter.print(value));
		return this;
	}

	/**
	 * Uses the specified Joda-Time partial and formatter to append a string literal,
	 * or the null literal if the argument is null.
	 * 
	 * @param value the partial to append (may be null)
	 * @param formatter the formatter used to turn the partial into a string
	 * @return this
	 */
	public final JavascriptAssembler appendJodaLiteralOrNull(ReadablePartial value, DateTimeFormatter formatter) {
		if (value == null) {
			appendNullLiteral();
		} else {
			appendStringLiteral(formatter.print(value));
		}
		return this;
	}

	/**
	 * Appends the specified object property name to the builder.
	 * Note that no escaping is done for the name, so the name
	 * must conform to identifier syntax.
	 * @param name the name of the property
	 * @return this
	 */
	public final JavascriptAssembler appendPropertyName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name argument is null");
		}
		JavascriptAssemblerUtil.appendStringLiteral(getBuilder(), name);
		return this;
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
	 * @return this
	 */
	public final JavascriptAssembler beginList() {
		beginCollection('[');
		return this;
	}

	/**
	 * Ends the current list expression.
	 * @return this
	 */
	public final JavascriptAssembler endList() {
		endCollection(']');
		return this;
	}

	/**
	 * This method must be called before each list element.
	 * @return this
	 */
	public final JavascriptAssembler prepareListElement() {
		prepareCollectionElement();
		return this;
	}

	/**
	 * Begins a new object expression.
	 * @return this
	 */
	public final JavascriptAssembler beginObject() {
		beginCollection('{');
		return this;
	}

	/**
	 * Ends the current object expression.
	 * @return this
	 */
	public final JavascriptAssembler endObject() {
		endCollection('}');
		return this;
	}

	/**
	 * This method must be called before each object property.
	 * @param name the name of the property
	 * @return this
	 */
	public final JavascriptAssembler prepareObjectProperty(String name) {
		prepareCollectionElement();
		appendPropertyName(name);
		getBuilder().append(": ");
		return this;
	}

	/**
	 * Appends the specified primitive-typed literal to the builder.
	 * Supported Java types are: {@link String}, {@link Number},
	 * {@link Boolean}, and null values.
	 * 
	 * @param value the value of the literal to append
	 * @return this
	 */
	public final JavascriptAssembler appendPrimitive(Object value) {
		if (value == null) {
			appendNullLiteral();
		} else if (value instanceof String) {
			appendStringLiteral((String)value);
		} else if (value instanceof Number) {
			appendNumericLiteral((Number)value);
		} else if (value instanceof Boolean) {
			appendBooleanLiteral((Boolean)value);
		} else if (value instanceof ReadableInstant) {
			appendDateTimeLiteral((ReadableInstant)value);
		} else if (value instanceof LocalDate) {
			appendDateLiteral((LocalDate)value);
		} else if (value instanceof LocalDateTime) {
			appendDateTimeLiteral((LocalDateTime)value);
		} else if (value instanceof ReadablePartial) {
			appendDateTimeLiteral((ReadablePartial)value);
		} else {
			appendCustomPrimitive(value);
		}
		return this;
	}

	/**
	 * This method is invoked by {@link #appendPrimitive(Object)} for
	 * unknown types. It should either convert the value to a valid
	 * JSON expression or, if the value has an unknown type, call
	 * {@link #unknownPrimitiveTypeException(Object)} and throw the
	 * returned exception.
	 * 
	 * The default implementation knows no custom type and thus always
	 * throws the exception.
	 * 
	 * @param value the value of the literal to append
	 */
	protected void appendCustomPrimitive(Object value) {
		throw unknownPrimitiveTypeException(value);
	}

	/**
	 * Creates an {@link IllegalArgumentException} about a value with an
	 * unknown primitive type that was passed to {@link #appendPrimitive(Object)}.
	 * @param value the value
	 * @return the exception
	 */
	protected final IllegalArgumentException unknownPrimitiveTypeException(Object value) {
		return new IllegalArgumentException("not a JSON-compatible primitive value: " + value);
	}
	
	/**
	 * Appends an array of primitive values as a Javascript array, using
	 * {@link #appendPrimitive(Object)} for each element.
	 * @param value the array to append
	 * @return this
	 */
	public final JavascriptAssembler appendPrimitiveArray(Object[] value) {
		beginList();
		for (Object element : value) {
			prepareListElement();
			appendPrimitive(element);
		}
		endList();
		return this;
	}
	
	/**
	 * Appends an {@link Iterable} of primitive values as a Javascript array, using
	 * {@link #appendPrimitive(Object)} for each element.
	 * @param value the iterable to append
	 * @return this
	 */
	public final JavascriptAssembler appendPrimitiveArray(Iterable<?> value) {
		beginList();
		for (Object element : value) {
			prepareListElement();
			appendPrimitive(element);
		}
		endList();
		return this;
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
	 * 
	 * @return this
	 */
	public final JavascriptAssembler beginNestedScope() {
		appendIndentedLine("(function() {");
		incrementIndentation();
		return this;
	}

	/**
	 * Ends a nested scope started by beginNestedScope(). See that method for
	 * a discussion of the purpose of nested scopes.
	 * 
	 * @return this
	 */
	public final JavascriptAssembler endNestedScope() {
		decrementIdentation();
		appendIndentedLine("})();");
		return this;
	}

}
