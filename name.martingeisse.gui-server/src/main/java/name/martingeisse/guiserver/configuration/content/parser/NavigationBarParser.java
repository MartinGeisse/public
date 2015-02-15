///**
// * Copyright (c) 2013 Shopgate GmbH
// */
//
//package name.martingeisse.guiserver.configuration.content.parser;
//
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.XMLStreamReader;
//import javax.xml.stream.XMLStreamWriter;
//
//import com.google.common.collect.ImmutableList;
//
//import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
//import name.martingeisse.guiserver.configuration.content.NavigationBarConfiguration;
//import name.martingeisse.guiserver.xml.ContentStreams;
//import name.martingeisse.guiserver.xml.IElementParser;
//
///**
// * The parser for a {@link NavigationBarConfiguration}.
// */
//public final class NavigationBarParser implements IElementParser<ComponentConfiguration> {
//
//	/* (non-Javadoc)
//	 * @see name.martingeisse.guiserver.xml.IElementParser#parse(name.martingeisse.guiserver.xml.ContentStreams)
//	 */
//	@Override
//	public void parse(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
//		XMLStreamWriter writer = streams.getWriter();
//		XMLStreamReader reader = streams.getReader();
//		
//		// handle the opening tag
//		String componentId = ("navbar" + streams.getComponentAccumulatorSize());
//		writer.writeStartElement("div");
//		writer.writeAttribute("wicket:id", componentId);
//		streams.next();
//		streams.skipWhitespace();
//		
//		// check for an optional brand link
//		ComponentConfiguration brandLink = null;
//		String brandLinkElementLocalName = streams.recognizeStartSpecialElement();
//		if (brandLinkElementLocalName != null && brandLinkElementLocalName.equals("brandLink")) {
//			streams.next();
//			streams.skipWhitespace();
//			String linkElementLocalName = streams.recognizeStartSpecialElement();
//			if (linkElementLocalName == null) {
//				throw new RuntimeException("link-related special tag expected inside brandLink element");
//			}
//			streams.beginComponentAccumulator();
//			DefaultContentParser.INSTANCE.parse(streams);
//			ImmutableList<ComponentConfiguration> brandLinkList = streams.finishComponentAccumulator();
//			if (brandLinkList.size() != 1) {
//				throw new RuntimeException("expected exactly 1 brandLink");
//			}
//			brandLink = brandLinkList.get(0);
//			streams.skipWhitespace();
//			streams.next();
//		}
//
//		// parse the navigation bar contents
//		streams.beginComponentAccumulator();
//		DefaultContentParser.INSTANCE.parse(streams);
//		streams.addComponent(new NavigationBarConfiguration(componentId, streams.finishComponentAccumulator(), brandLink));
//
//		// finish the input and output elements
//		writer.writeEndElement();
//		reader.next();
//		
//	}
//
//}
