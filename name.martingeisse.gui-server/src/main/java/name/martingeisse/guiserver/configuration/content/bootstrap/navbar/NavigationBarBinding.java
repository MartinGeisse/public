/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.bootstrap.navbar;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfigurationList;
import name.martingeisse.guiserver.configuration.content.basic.LinkConfiguration;
import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.builder.XmlParserBuilder;
import name.martingeisse.guiserver.xml.content.AbstractMultiChildParser;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.element.ClassInstanceElementParser;
import name.martingeisse.guiserver.xml.element.NameSelectedElementParser;
import name.martingeisse.guiserver.xml.element.ElementParser;
import name.martingeisse.guiserver.xml.element.ElementParserWrapper;

import com.google.common.collect.ImmutableList;

/**
 * The XML-element-to-navigation-bar-configuration-binding.
 */
public final class NavigationBarBinding extends ClassInstanceElementParser<NavigationBarConfiguration> {

	/**
	 * Constructor.
	 */
	public NavigationBarBinding(XmlParserBuilder<ComponentGroupConfiguration> builder) {
		super(chooseConstructor(), new AttributeParser<?>[0], createContentBinding(builder));
	}

	/**
	 * 
	 */
	private static Constructor<NavigationBarConfiguration> chooseConstructor() {
		try {
			return NavigationBarConfiguration.class.getConstructor(NavigationBarContents.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 */
	private static ContentParser<NavigationBarContents> createContentBinding(XmlParserBuilder<ComponentGroupConfiguration> builder) {
		Map<String, ElementParser<? extends NavigationBarChildElement>> childElementObjectBindings = new HashMap<>();
		childElementObjectBindings.put("brandLink", new ElementParserWrapper<ComponentGroupConfiguration, NavigationBarChildElement>(builder.getComponentBinding("link")) {
			@Override
			protected NavigationBarChildElement wrapResult(ComponentGroupConfiguration original) {
				return new NavigationBarChildElement.BrandLink((LinkConfiguration)original);
			}
		});
		childElementObjectBindings.put("link", new ElementParserWrapper<ComponentGroupConfiguration, NavigationBarChildElement>(builder.getComponentBinding("link")) {
			@Override
			protected NavigationBarChildElement wrapResult(ComponentGroupConfiguration original) {
				return new NavigationBarChildElement.NavigationLink((LinkConfiguration)original);
			}
		});
		NameSelectedElementParser<NavigationBarChildElement> childElementObjectBinding = new NameSelectedElementParser<>(childElementObjectBindings);
		String[] nameFilter = {
			"brandLink", "link"
		};
		return new AbstractMultiChildParser<NavigationBarChildElement, NavigationBarContents>(true, nameFilter, childElementObjectBinding) {
			@Override
			protected NavigationBarContents mapChildrenToResult(List<NavigationBarChildElement> children) {
				LinkConfiguration brandLink = null;
				List<LinkConfiguration> navigationLinks = new ArrayList<>();
				for (NavigationBarChildElement child : children) {
					if (child instanceof NavigationBarChildElement.BrandLink) {
						if (brandLink == null) {
							brandLink = ((NavigationBarChildElement.BrandLink)child).getLinkConfiguration();
						} else {
							throw new RuntimeException("cannot define multiple brand links for a navigation bar");
						}
					} else if (child instanceof NavigationBarChildElement.NavigationLink) {
						navigationLinks.add(((NavigationBarChildElement.NavigationLink)child).getLinkConfiguration());
					} else {
						throw new RuntimeException("unknown subclass of NavigationBarChildElement: " + child.getClass());
					}
				}
				ImmutableList<ComponentGroupConfiguration> navigationLinkList = ImmutableList.<ComponentGroupConfiguration> copyOf(navigationLinks);
				return new NavigationBarContents(brandLink, new ComponentGroupConfigurationList(navigationLinkList));
			}
		};
	}

}
