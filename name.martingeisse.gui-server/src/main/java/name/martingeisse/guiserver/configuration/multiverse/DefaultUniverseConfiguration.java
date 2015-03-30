/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.multiverse;

import java.util.List;
import java.util.Map;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.configuration.storage.StorageException;
import name.martingeisse.guiserver.template.IConfigurationSnippet;

import org.apache.wicket.request.mapper.ICompoundRequestMapper;

/**
 * This singleton class provides the data from the configuration.
 */
public final class DefaultUniverseConfiguration implements UniverseConfiguration {

	/**
	 * the serialNumber
	 */
	private final int serialNumber;
	
	/**
	 * the elements
	 */
	private final Map<String, Element> elements;
	
	/**
	 * the snippets
	 */
	private final List<IConfigurationSnippet> snippets;
	
	/**
	 * Constructor.
	 * 
	 * @param serialNumber the serial number for this configuration
	 * @throws StorageException on errors in the storage system
	 * @throws ConfigurationException on errors in a configuration file
	 */
	public DefaultUniverseConfiguration(int serialNumber, Map<String, Element> elements, List<IConfigurationSnippet> snippets) throws StorageException, ConfigurationException {
		this.serialNumber = serialNumber;
		this.elements = elements;
		this.snippets = snippets;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.UniverseConfiguration#getSerialNumber()
	 */
	@Override
	public int getSerialNumber() {
		return serialNumber;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.UniverseConfiguration#mountWicketUrls(org.apache.wicket.request.mapper.ICompoundRequestMapper)
	 */
	@Override
	public void mountWicketUrls(ICompoundRequestMapper compoundRequestMapper) {
		for (Element element : elements.values()) {
			element.mountWicketUrls(compoundRequestMapper);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.UniverseConfiguration#getElement(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T extends Element> T getElement(Class<T> type, String path) {
		T element = getElementOrNull(type, path);
		if (element == null) {
			throw new RuntimeException("configuration element with type " + type + " and path " + path + " doesn't exist");
		}
		return element;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.UniverseConfiguration#getElementOrNull(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T extends Element> T getElementOrNull(Class<T> type, String path) {
		Element element = getElement(path);
		return (type.isInstance(element) ? type.cast(element) : null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.UniverseConfiguration#getElement(java.lang.String)
	 */
	@Override
	public Element getElement(String path) {
		Element element = getElementOrNull(path);
		if (element == null) {
			throw new RuntimeException("configuration element with path " + path + " doesn't exist");
		}
		return element;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.UniverseConfiguration#getElementOrNull(java.lang.String)
	 */
	@Override
	public Element getElementOrNull(String path) {
		return elements.get(path);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.UniverseConfiguration#getSnippet(int)
	 */
	@Override
	public IConfigurationSnippet getSnippet(int handle) {
		return snippets.get(handle);
	}
	
}
