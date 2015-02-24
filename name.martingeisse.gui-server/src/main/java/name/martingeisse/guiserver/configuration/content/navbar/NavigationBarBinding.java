/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.navbar;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfigurationList;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.xml.attribute.AttributeValueBinding;
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
	public NavigationBarBinding(XmlBindingBuilder<ComponentConfiguration> builder) {
		super(chooseConstructor(), new AttributeValueBinding<?>[0], createContentBinding(builder));
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
	private static XmlContentObjectBinding<NavigationBarContents> createContentBinding(XmlBindingBuilder<ComponentConfiguration> builder) {
		Map<String, ElementObjectBinding<? extends NavigationBarChildElement>> childElementObjectBindings = new HashMap<>();
		childElementObjectBindings.put("brandLink", new ElementObjectBindingWrapper<ComponentConfiguration, NavigationBarChildElement>(builder.getComponentBinding("link")) {
			@Override
			protected NavigationBarChildElement wrapResult(ComponentConfiguration original) {
				return new NavigationBarChildElement.BrandLink((LinkConfiguration)original);
			}
		});
		childElementObjectBindings.put("link", new ElementObjectBindingWrapper<ComponentConfiguration, NavigationBarChildElement>(builder.getComponentBinding("link")) {
			@Override
			protected NavigationBarChildElement wrapResult(ComponentConfiguration original) {
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
				ImmutableList<ComponentConfiguration> navigationLinkList = ImmutableList.<ComponentConfiguration> copyOf(navigationLinks);
				return new NavigationBarContents(brandLink, new ComponentConfigurationList(navigationLinkList));
			}
		};
	}

}
