/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.EmptyStackException;
import java.util.Stack;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This is a utility class to generate XML. It keeps a current context which can be an arbitrary
 * object, and allows to turn properties of that object into XML constructs. It also keeps
 * a stack of previous contexts to simplify XML generation from nested objects.
 */
public class ContextAwareXmlWriter extends XmlWriter {

	/**
	 * the currentContext
	 */
	private Object currentContext;

	/**
	 * the contextStack
	 */
	private Stack<Object> contextStack;

	/**
	 * Constructor.
	 * @param outputWriter the output writer used to actually write something
	 */
	public ContextAwareXmlWriter(final PrintWriter outputWriter) {
		super(outputWriter);
	}

	/**
	 * Constructor.
	 * @param outputWriterToWrap the output writer used to actually write something.
	 * Will be wrapped in a {@link PrintWriter}.
	 */
	public ContextAwareXmlWriter(final Writer outputWriterToWrap) {
		super(outputWriterToWrap);
	}

	/**
	 * Getter method for the currentContext.
	 * @return the currentContext
	 */
	public final Object getCurrentContext() {
		return currentContext;
	}

	/**
	 * Setter method for the currentContext.
	 * @param currentContext the currentContext to set
	 */
	public final void setCurrentContext(final Object currentContext) {
		this.currentContext = currentContext;
	}

	/**
	 * Initializes the context stack if not yet done.
	 */
	private final void lazyInitializeContextStack() {
		if (contextStack == null) {
			contextStack = new Stack<Object>();
		}
	}
	
	/**
	 * Pushes the current context on the context stack and uses the specified
	 * new context as the current context.
	 * @param newContext the new context to enter
	 */
	public final void enterContext(final Object newContext) {
		lazyInitializeContextStack();
		contextStack.push(currentContext);
	}

	/**
	 * Pops the top of the context stack and uses it as the new current context.
	 * @throws EmptyStackException if the context stack is empty
	 */
	public final void leaveContext() throws EmptyStackException {
		lazyInitializeContextStack();
		currentContext = contextStack.pop();
	}

	/**
	 * Writes an element with the specified name whose text content is the string-ified value
	 * of the property with the same name of the current context.
	 * @param name the property and element name
	 */
	public final void writePropertyElement(String name) {
		writePropertyElement(name, name);
	}

	/**
	 * Writes an element with the specified element name whose text content is the string-ified value
	 * of the property with the specified property name of the current context. If the property value
	 * is null, a nil element is written.
	 * @param propertyName the name of the property of the current context
	 * @param elementName the name of the element to write
	 */
	public final void writePropertyElement(String propertyName, String elementName) {
		try {
			Object value = PropertyUtils.getProperty(currentContext, propertyName);
			String valueText = (value == null) ? null : value.toString();
			writeValueElementOrNil(elementName, valueText);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Writes an opening tag with the specified name and uses the value of the property with the
	 * same name of the current context as a new context, pushing the old one on the context stack.
	 * Use leaveElementContext() to return to the old context.
	 * @param name the property and element name
	 */
	public void enterElementPropertyContext(String name) {
		enterElementPropertyContext(name, name);
	}

	/**
	 * Writes an opening tag with the specified element name and uses the value of the property with the
	 * specified property name of the current context as a new context, pushing the old one on the context stack.
	 * Use leaveElementContext() to return to the old context.
	 * 
	 * @param propertyName the name of the property of the current context. The property value must not be null.
	 * @param elementName the name of the element to write the opening tag for
	 */
	public void enterElementPropertyContext(String propertyName, String elementName) {
		try {
			enterContext(PropertyUtils.getProperty(currentContext, propertyName));
			writeOpeningTag(elementName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Leaves the current context, restoring the previous one from the context stack, and closes the
	 * associated element.
	 * @param elementName the name of the element to close
	 */
	public void leaveElementContext(String elementName) {
		leaveContext();
		writeClosingTag(elementName);
	}

	/**
	 * Uses the value of the property with the specified name of the current context as a new context,
	 * pushing the old one on the context stack. Use leaveContext() to return to the old context.
	 * @param propertyName the property name
	 */
	public void enterEmbeddedPropertyContext(String propertyName) {
		try {
			enterContext(PropertyUtils.getProperty(currentContext, propertyName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
