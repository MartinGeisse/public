/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.IConfigurationSnippet;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.xmlbind.content.MarkupContentBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementNameSelectedObjectBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding;
import name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler;
import name.martingeisse.guiserver.xmlbind.result.MarkupContent;

/**
 * TODO: document me
 *
 */
public class TestMain {

	public static void main(String[] args) throws Exception {
		
		Map<String, ElementObjectBinding<ComponentConfiguration>> bindings = new HashMap<>();
		bindings.put("link", new ElementClassInstanceBinding<>(LinkConfiguration.class, attributeBindings, contentBinding));
		ElementNameSelectedObjectBinding<ComponentConfiguration> elementObjectBinding = new ElementNameSelectedObjectBinding<>(bindings); 
		MarkupContentBinding<ComponentConfiguration> markupContentBinding = new MarkupContentBinding<>(elementObjectBinding);
		
		MarkupContent<ComponentConfiguration> markupContent;
		try (FileInputStream fileInputStream = new FileInputStream("resource/demo-gui-2/index.page.xml")) {
			XMLStreamReader xmlStreamReader = XMLInputFactory.newFactory().createXMLStreamReader(fileInputStream);
			DatabindingXmlStreamReader reader = new DatabindingXmlStreamReader(xmlStreamReader);
			reader.next();
			reader.next();
			markupContent = markupContentBinding.parse(reader);
		}
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		XMLStreamWriter markupWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(byteArrayOutputStream);
		List<ComponentConfiguration> componentAccumulator = new ArrayList<>();
		List<IConfigurationSnippet> snippetAccumulator = new ArrayList<>();
		ConfigurationAssembler<ComponentConfiguration> assembler = new ConfigurationAssembler<>(markupWriter, componentAccumulator, snippetAccumulator);
		markupContent.assemble(assembler);
		
		System.out.println(new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
	}
	
}
