/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.reporting.parser.adapter.TabularKeyCountAdapterFactory;

import org.xml.sax.Attributes;

/**
 * This class keeps track of {@link IParserStateFactory} and {@link IParserReturnTypeAdapterFactory}
 * objects contributed by parser modules. It is in turn used by parser modules to create states
 * and adapters, allowing to glue together unrelated parser modules.
 */
public final class StateFactoryRegistry implements IParserReturnTypeAdapterProvider {

	/**
	 * the stateFactories
	 */
	private final Map<String, IParserStateFactory> stateFactories;
	
	/**
	 * the adapterFactories
	 */
	private final List<IParserReturnTypeAdapterFactory> adapterFactories;
	
	/**
	 * Constructor.
	 */
	public StateFactoryRegistry() {
		this.stateFactories = new HashMap<String, IParserStateFactory>();
		this.adapterFactories = new ArrayList<IParserReturnTypeAdapterFactory>();
		
		// default adapter factories
		adapterFactories.add(new TabularKeyCountAdapterFactory());
	}
	
	/**
	 * Registers a state factory for an XML namespace.
	 * @param xmlNamespaceUri the XML namespace URI
	 * @param stateFactory the state factory to register
	 */
	public void registerStateFactory(String xmlNamespaceUri, IParserStateFactory stateFactory) {
		IParserStateFactory old = stateFactories.put(xmlNamespaceUri, stateFactory);
		if (old != null) {
			throw new RuntimeException("more than one state factory registered for XML namespace: " + xmlNamespaceUri);
		}
	}

	/**
	 * Registers a parser return type adapter factory.
	 * @param returnTypeAdapterFactory the adapter factory to register
	 */
	public void registerReturnTypeAdapterFactory(IParserReturnTypeAdapterFactory returnTypeAdapterFactory) {
		adapterFactories.add(returnTypeAdapterFactory);
	}

	/**
	 * Creates a parser state, making use of supported parser state factories and return type adapter
	 * factories as needed.
	 * @param expectedReturnType the return type expected by the parent state
	 * @param namespaceUri the namespace URI of the element that started the state
	 * @param name the local name of the element that started the state
	 * @param attributes the attributes of the element that started the state
	 * @return the parser state
	 */
	public IParserState createParserState(Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes) {
		IParserStateFactory stateFactory = stateFactories.get(namespaceUri);
		if (stateFactory == null) {
			throw new ParserException("unknown namespace: " + namespaceUri);
		}
		return stateFactory.createParserState(this, expectedReturnType, namespaceUri, name, attributes);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserReturnTypeAdapterProvider#findReturnTypeAdapterFactory(java.lang.Class, java.lang.Class)
	 */
	@Override
	public IParserReturnTypeAdapterFactory findReturnTypeAdapterFactory(Class<?> expectedReturnTypeByParent, Class<?> supportedReturnType) {
		if (NopReturnTypeAdapterFactory.instance.supportsAdapter(expectedReturnTypeByParent, supportedReturnType)) {
			return NopReturnTypeAdapterFactory.instance;
		}
		for (IParserReturnTypeAdapterFactory factory : adapterFactories) {
			if (factory.supportsAdapter(expectedReturnTypeByParent, supportedReturnType)) {
				return factory;
			}
		}
		return null;
	}

}
