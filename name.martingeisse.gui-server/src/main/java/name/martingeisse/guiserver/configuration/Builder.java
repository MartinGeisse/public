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

import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfigurationList;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.template.Template;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;

import com.google.common.collect.ImmutableList;

/**
 * Helper class to generate the configuration from the configuration files.
 * 
 * Instances of this class should not be used by two callers at the same time.
 */
final class Builder {

	/**
	 * the xmlContentObjectBinding
	 */
	private final ContentParser<MarkupContent<ComponentGroupConfiguration>> xmlContentObjectBinding;

	/**
	 * the elements
	 */
	private final Map<String, ConfigurationElement> elements = new HashMap<>();

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
	public Builder(File configurationRoot, ContentParser<MarkupContent<ComponentGroupConfiguration>> xmlContentObjectBinding) {
		this.xmlContentObjectBinding = xmlContentObjectBinding;
	}

	/**
	 * Builds the configuration using the files in the specified root folder and its subfolders.
	 * @param rootFolder the root folder
	 * @return the configuration elements
	 * @throws IOException on I/O errors
	 */
	public Configuration build(File rootFolder) throws IOException, ConfigurationException {
		elements.clear();
		pathSegments.clear();
		snippets.clear();
		if (!rootFolder.isDirectory()) {
			throw new RuntimeException("root configuration folder " + rootFolder + " doesn't exist or is not a folder");
		}
		handleFolder(rootFolder);
		return new Configuration(elements, snippets);
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
			MarkupContent<ComponentGroupConfiguration> markupContent;
			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				XMLStreamReader xmlStreamReader = XMLInputFactory.newFactory().createXMLStreamReader(fileInputStream);
				MyXmlStreamReader reader = new MyXmlStreamReader(xmlStreamReader);
				reader.expectSpecialDocumentElement("page");
				markupContent = xmlContentObjectBinding.parse(reader);
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}

			// assemble the final component configuration tree from the markup content
			String wicketMarkup;
			ComponentGroupConfigurationList components;
			try {
				StringWriter stringWriter = new StringWriter();
				XMLStreamWriter markupWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
				markupWriter.writeStartDocument();
				markupWriter.writeStartElement("html");
				markupWriter.writeStartElement("body");
				List<ComponentGroupConfiguration> componentGroupAccumulator = new ArrayList<>();
				ConfigurationAssembler<ComponentGroupConfiguration> assembler = new ConfigurationAssembler<>(markupWriter, componentGroupAccumulator, snippets);
				markupContent.assemble(assembler);
				markupWriter.writeEndElement();
				markupWriter.writeEndElement();
				markupWriter.writeEndDocument();
				wicketMarkup = stringWriter.toString();
				components = new ComponentGroupConfigurationList(ImmutableList.copyOf(componentGroupAccumulator));
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
			
			// TODO
			System.out.println("------------------------------------------");
			System.out.println(wicketMarkup);
			System.out.println("------------------------------------------");
			// TODO
			
			Template content = new Template(wicketMarkup, components);
			PageConfiguration pageConfiguration = new PageConfiguration(path, content);
			elements.put(path, pageConfiguration);
		} else if (filename.endsWith(PanelConfiguration.CONFIGURATION_FILENAME_SUFFIX)) {
			String path = getPath(PanelConfiguration.CONFIGURATION_FILENAME_SUFFIX);
			elements.put(path, new PanelConfiguration(path));
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

}
