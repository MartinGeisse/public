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
import name.martingeisse.guiserver.xml.builder.XmlBindingBuilder;
import name.martingeisse.guiserver.xml.content.AbstractMultiChildObjectBinding;
import name.martingeisse.guiserver.xml.content.XmlContentObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xml.element.ElementNameSelectedObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementObjectBindingWrapper;

import com.google.common.collect.ImmutableList;

/**
 * The XML-element-to-navigation-bar-configuration-binding.
 */
public final class NavigationBarBinding extends ElementClassInstanceBinding<NavigationBarConfiguration> {

	/**
	 * Constructor.
	 */
	public NavigationBarBinding(XmlBindingBuilder<ComponentGroupConfiguration> builder) {
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
	private static XmlContentObjectBinding<NavigationBarContents> createContentBinding(XmlBindingBuilder<ComponentGroupConfiguration> builder) {
		Map<String, ElementObjectBinding<? extends NavigationBarChildElement>> childElementObjectBindings = new HashMap<>();
		childElementObjectBindings.put("brandLink", new ElementObjectBindingWrapper<ComponentGroupConfiguration, NavigationBarChildElement>(builder.getComponentBinding("link")) {
			@Override
			protected NavigationBarChildElement wrapResult(ComponentGroupConfiguration original) {
				return new NavigationBarChildElement.BrandLink((LinkConfiguration)original);
			}
		});
		childElementObjectBindings.put("link", new ElementObjectBindingWrapper<ComponentGroupConfiguration, NavigationBarChildElement>(builder.getComponentBinding("link")) {
			@Override
			protected NavigationBarChildElement wrapResult(ComponentGroupConfiguration original) {
				return new NavigationBarChildElement.NavigationLink((LinkConfiguration)original);
			}
		});
		ElementNameSelectedObjectBinding<NavigationBarChildElement> childElementObjectBinding = new ElementNameSelectedObjectBinding<>(childElementObjectBindings);
		String[] nameFilter = {
			"brandLink", "link"
		};
		return new AbstractMultiChildObjectBinding<NavigationBarChildElement, NavigationBarContents>(true, nameFilter, childElementObjectBinding) {
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
