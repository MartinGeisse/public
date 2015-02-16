/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.element;

import java.lang.reflect.Constructor;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.AttributeValueBinding;
import name.martingeisse.guiserver.xml.content.XmlContentObjectBinding;

/**
 * Binds an XML element to a constructor of a Java class.
 */
public class ElementClassInstanceBinding<T> implements ElementObjectBinding<T> {

	/**
	 * the constructor
	 */
	private final Constructor<? extends T> constructor;

	/**
	 * the attributeBindings
	 */
	private final AttributeValueBinding<?>[] attributeBindings;

	/**
	 * the contentBinding
	 */
	private final XmlContentObjectBinding<?> contentBinding;

	/**
	 * Constructor.
	 * @param constructor the constructor of the target class to call
	 * @param attributeBindings the attribute bindings
	 * @param contentBinding the content binding, or null if no content is allowed for the element
	 */
	public ElementClassInstanceBinding(Class<? extends T> targetClass, AttributeValueBinding<?>[] attributeBindings, XmlContentObjectBinding<?> contentBinding) {
		this(chooseConstructor(targetClass), attributeBindings, contentBinding);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> chooseConstructor(Class<? extends T> targetClass) {
		if (targetClass.getConstructors().length != 1) {
			throw new RuntimeException("expecting exactly one constructor in class " + targetClass);
		}
		return (Constructor<T>)targetClass.getConstructors()[0];
	}

	/**
	 * Constructor.
	 * @param constructor the constructor of the target class to call
	 * @param attributeBindings the attribute bindings
	 * @param contentBinding the content binding, or null if no content is allowed for the element
	 */
	public ElementClassInstanceBinding(Constructor<? extends T> constructor, AttributeValueBinding<?>[] attributeBindings, XmlContentObjectBinding<?> contentBinding) {

		// assign fields
		this.constructor = constructor;
		this.attributeBindings = attributeBindings;
		this.contentBinding = contentBinding;
		
		// make sure that the constructor matches our expectations
		Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
		if (constructorParameterTypes.length != getExpectedArgumentCount()) {
			throw new RuntimeException("number of constructor arguments doesn't match binding annotation for class " + constructor.getDeclaringClass());
		}
		
	}
	
	/**
	 * 
	 */
	private int getExpectedArgumentCount() {
		return attributeBindings.length + (contentBinding == null ? 0 : 1);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public final T parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		String elementLocalName = reader.getLocalName();
		
		// create the constructor argument array
		int argumentCount = getExpectedArgumentCount();
		Object[] arguments = new Object[argumentCount];
		
		// bind attribute values
		for (int i=0; i<attributeBindings.length; i++) {
			arguments[i] = attributeBindings[i].parse(reader);
		}
		
		// parse element content (if supported)
		reader.next();
		if (contentBinding == null) {
			reader.skipWhitespace();
			if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
				throw new RuntimeException("unexpected content in element " + elementLocalName);
			}
		} else {
			arguments[arguments.length - 1] = contentBinding.parse(reader);
		}
		reader.next();

		// create the instance
		try {
			return constructor.newInstance(arguments);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
