/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.osm.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import name.martingeisse.osm.data.OsmRelation.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Loads an OSM world from an XML file.
 */
public final class OsmLoader {

	/**
	 * Invokes this service.
	 * 
	 * @param file the XML file to load from
	 * @return the loaded world
	 */
	public OsmWorld loadWorld(File file) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
				XMLReader xmlReader = XMLReaderFactory.createXMLReader();
				MyContentHandler contentHandler = new MyContentHandler();
				xmlReader.setContentHandler(contentHandler);
				InputSource inputSource = new InputSource(inputStreamReader);
				xmlReader.parse(inputSource);
				return contentHandler.world;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 */
	static class MyContentHandler extends DefaultHandler {

		/**
		 * the world
		 */
		final OsmWorld world = new OsmWorld();
		
		/**
		 * the currentNode
		 */
		private OsmNode currentNode = null;
		
		/**
		 * the currentWay
		 */
		private OsmWay currentWay = null;
		
		/**
		 * the currentRelation
		 */
		private OsmRelation currentRelation = null;
		
		/**
		 * the currentTaggable
		 */
		private OsmTaggable currentTaggable = null;
		
		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			switch (localName) {
			
			case "node":
				currentTaggable = currentNode = new OsmNode();
				currentNode.setId(Long.parseLong(attributes.getValue("id")));
				currentNode.setLatitude(Double.parseDouble(attributes.getValue("lat")));
				currentNode.setLongitude(Double.parseDouble(attributes.getValue("lon")));
				break;
				
			case "way":
				currentTaggable = currentWay = new OsmWay();
				currentWay.setId(Long.parseLong(attributes.getValue("id")));
				break;
				
			case "relation":
				currentTaggable = currentRelation = new OsmRelation();
				currentRelation.setId(Long.parseLong(attributes.getValue("id")));
				break;
				
			case "tag":
				currentTaggable.getTags().put(attributes.getValue("k"), attributes.getValue("v"));
				break;
				
			case "nd":
				currentWay.getNodes().add(world.getNodes().get(Long.parseLong(attributes.getValue("ref"))));
				break;
				
			case "member":
				Map<Long, ? extends OsmRelationMember> worldObjectMap;
				switch (attributes.getValue("type")) {
				
				case "node":
					worldObjectMap = world.getNodes();
					break;
					
				case "way":
					worldObjectMap = world.getWays();
					break;
					
				case "relation":
					worldObjectMap = world.getRelations();
					break;

				default:
					System.err.println("unknown relation member type: " + attributes.getValue("type"));
					return;
				}
				Entry entry = new Entry();
				entry.setMember(worldObjectMap.get(Long.parseLong(attributes.getValue("ref"))));
				entry.setRole(attributes.getValue("role"));
				currentRelation.getEntries().add(entry);
				break;
				
			}
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			switch (localName) {
			
			case "node":
				world.getNodes().put(currentNode.getId(), currentNode);
				currentTaggable = currentNode = null;
				break;
				
			case "way":
				world.getWays().put(currentWay.getId(), currentWay);
				currentTaggable = currentWay = null;
				break;
				
			case "relation":
				world.getRelations().put(currentRelation.getId(), currentRelation);
				currentTaggable = currentRelation = null;
				break;
				
			}
		}
		
	}

}
