/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfigurationList;
import name.martingeisse.guiserver.configuration.content.IConfigurationSnippet;
import name.martingeisse.guiserver.configuration.elements.ConfigurationElement;
import name.martingeisse.guiserver.configuration.elements.ConfigurationElementContent;
import name.martingeisse.guiserver.configuration.elements.FormUrlConfiguration;
import name.martingeisse.guiserver.configuration.elements.PageConfiguration;
import name.martingeisse.guiserver.configuration.elements.PanelConfiguration;
import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding;
import name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler;
import name.martingeisse.guiserver.xmlbind.result.MarkupContent;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Helper class to generate the configuration from the configuration files.
 * 
 * Instances of this class should not be used by two callers at the same time.
 */
final class ConfigurationBuilder {

	/**
	 * the xmlContentObjectBinding
	 */
	private final XmlContentObjectBinding<MarkupContent<ComponentConfiguration>> xmlContentObjectBinding;

	/**
	 * the elements
	 */
	private final Map<Class<? extends ConfigurationElement>, Map<String, ConfigurationElement>> elements = new HashMap<>();

	/**
	 * the pathSegments
	 */
	private final Stack<String> pathSegments = new Stack<>();

	/**
	 * the snippets
	 */
	private final List<IConfigurationSnippet> snippets = new ArrayList<>();

	/**
	 * Constructor.
	 * @param xmlContentObjectBinding the XML-content-to-object-binding
	 */
	public ConfigurationBuilder(XmlContentObjectBinding<MarkupContent<ComponentConfiguration>> xmlContentObjectBinding) {
		this.xmlContentObjectBinding = xmlContentObjectBinding;
	}

	/**
	 * Builds the configuration using the files in the specified root folder and its subfolders.
	 * @param rootFolder the root folder
	 * @return the configuration elements
	 * @throws IOException on I/O errors
	 */
	public void build(File rootFolder) throws IOException {
		elements.clear();
		pathSegments.clear();
		snippets.clear();
		if (!rootFolder.isDirectory()) {
			throw new RuntimeException("root configuration folder " + rootFolder + " doesn't exist or is not a folder");
		}
		handleFolder(rootFolder);
	}

	/**
	 * Getter method for the elements.
	 * @return the elements
	 */
	public Map<Class<? extends ConfigurationElement>, Map<String, ConfigurationElement>> getElements() {
		return ImmutableMap.copyOf(elements);
	}

	/**
	 * Getter method for the snippets.
	 * @return the snippetss
	 */
	public List<IConfigurationSnippet> getSnippets() {
		return ImmutableList.copyOf(snippets);
	}

	/**
	 * 
	 */
	private void handleFolder(File folder) throws IOException {
		for (File element : folder.listFiles()) {
			pathSegments.push(element.getName());
			if (element.isDirectory()) {
				handleFolder(element);
			} else if (element.isFile()) {
				handleFile(element);
			} else {
				throw new RuntimeException("configuration folder contains non-file, non-folder element: " + getPath());
			}
			pathSegments.pop();
		}
	}

	/**
	 * 
	 */
	private void handleFile(File file) throws IOException {
		String filename = file.getName();
		if (filename.endsWith(PageConfiguration.CONFIGURATION_FILENAME_SUFFIX)) {
			String path = getPath(PageConfiguration.CONFIGURATION_FILENAME_SUFFIX);

			// parse the file to obtain markup content
			MarkupContent<ComponentConfiguration> markupContent;
			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				XMLStreamReader xmlStreamReader = XMLInputFactory.newFactory().createXMLStreamReader(fileInputStream);
				DatabindingXmlStreamReader reader = new DatabindingXmlStreamReader(xmlStreamReader);
				reader.expectSpecialDocumentElement("page");
				markupContent = xmlContentObjectBinding.parse(reader);
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}

			// assemble the final component configuration tree from the markup content
			String wicketMarkup;
			ComponentConfigurationList components;
			try {
				StringWriter stringWriter = new StringWriter();
				XMLStreamWriter markupWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
				markupWriter.writeStartDocument();
				markupWriter.writeStartElement("html");
				markupWriter.writeStartElement("body");
				List<ComponentConfiguration> componentAccumulator = new ArrayList<>();
				ConfigurationAssembler<ComponentConfiguration> assembler = new ConfigurationAssembler<>(markupWriter, componentAccumulator, snippets);
				markupContent.assemble(assembler);
				markupWriter.writeEndElement();
				markupWriter.writeEndElement();
				markupWriter.writeEndDocument();
				wicketMarkup = stringWriter.toString();
				components = new ComponentConfigurationList(ImmutableList.copyOf(componentAccumulator));
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
			
			// TODO
			System.out.println("------------------------------------------");
			System.out.println(wicketMarkup);
			System.out.println("------------------------------------------");
			// TODO
			
			ConfigurationElementContent content = new ConfigurationElementContent(wicketMarkup, components);
			PageConfiguration pageConfiguration = new PageConfiguration(path, content);
			putElement(path, pageConfiguration);
		} else if (filename.endsWith(PanelConfiguration.CONFIGURATION_FILENAME_SUFFIX)) {
			String path = getPath(PanelConfiguration.CONFIGURATION_FILENAME_SUFFIX);
			putElement(path, new PanelConfiguration(path));
		} else if (filename.endsWith(FormUrlConfiguration.CONFIGURATION_FILENAME_SUFFIX)) {
			String path = getPath(FormUrlConfiguration.CONFIGURATION_FILENAME_SUFFIX);
			putElement(path, new FormUrlConfiguration(path));
		} else {
			throw new RuntimeException("ConfigurationBuilder doesn't understand configuration element: " + getPath());
		}
	}

	/**
	 * 
	 */
	private String getPath() {
		if (pathSegments.isEmpty()) {
			return "/";
		}
		StringBuilder builder = new StringBuilder();
		for (String segment : pathSegments) {
			builder.append('/').append(segment);
		}
		return builder.toString();
	}

	/**
	 * 
	 */
	private String getPath(String suffixToRemove) {
		String path = getPath();
		if (!path.endsWith(suffixToRemove)) {
			throw new RuntimeException("configuration file path " + path + " doesn't end with " + suffixToRemove);
		}
		return path.substring(0, path.length() - suffixToRemove.length());
	}

	/**
	 * 
	 */
	private void putElement(String path, ConfigurationElement element) {
		Class<? extends ConfigurationElement> elementClass = element.getClass();
		Map<String, ConfigurationElement> subMap = elements.get(elementClass);
		if (subMap == null) {
			subMap = new HashMap<>();
			elements.put(elementClass, subMap);
		}
		subMap.put(path, element);
	}

}
