/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.content.bootstrap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfigurationList;
import name.martingeisse.guiserver.configuration.content.ConfigurationAssembler;
import name.martingeisse.guiserver.configuration.content.IComponentGroupConfigurationVisitor;
import name.martingeisse.guiserver.configuration.content.MarkupContent;
import name.martingeisse.guiserver.gui.NavigationBar;
import name.martingeisse.guiserver.xml.builder.BindElement;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;

import com.google.common.collect.ImmutableList;

/**
 * The configuration for a navigation bar.
 */
@RegisterComponentElement(localName = "navbar")
@StructuredElement
public final class NavigationBarConfiguration extends AbstractSingleContainerConfiguration {

	/**
	 * the brandLink
	 */
	private BrandLinkWrapper brandLink;

	/**
	 * Getter method for the brandLink.
	 * @return the brandLink
	 */
	public BrandLinkWrapper getBrandLink() {
		return brandLink;
	}

	/**
	 * Setter method for the brandLink.
	 * @param brandLink the brandLink to set
	 */
	@BindElement(localName = "brandLink")
	public void setBrandLink(BrandLinkWrapper brandLink) {
		this.brandLink = brandLink;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration#setMarkupContent(name.martingeisse.guiserver.xml.result.MarkupContent)
	 */
	@Override
	@BindElement(localName = "main")
	public void setMarkupContent(MarkupContent<ComponentGroupConfiguration> markupContent) {
		super.setMarkupContent(markupContent);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration#assemble(name.martingeisse.guiserver.xml.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		assembler.getMarkupWriter().writeStartElement("div");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId() + "-container");
		super.assemble(assembler);
		if (brandLink != null) {
			assembler.getMarkupWriter().writeStartElement("div");
			assembler.getMarkupWriter().writeAttribute("wicket:id", "brandLinkFragment");
			
			
			
			brandLink.getConfiguration().assemble(assembler);
			
			
			List<ComponentGroupConfiguration> childrenAccumulator = new ArrayList<ComponentGroupConfiguration>();
			ConfigurationAssembler<ComponentGroupConfiguration> subAssembler = assembler.withComponentGroupAccumulator(childrenAccumulator);
			markupContent.assemble(subAssembler);
			this.children = new ComponentGroupConfigurationList(ImmutableList.copyOf(childrenAccumulator));
			
			
			
			
			assembler.getMarkupWriter().writeEndElement();
		}
		assembler.getMarkupWriter().writeEndElement();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#accept(name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitor)
	 */
	@Override
	public void accept(IComponentGroupConfigurationVisitor visitor) {
		if (visitor.beginVisit(this)) {
			if (brandLink != null) {
				brandLink.getConfiguration().accept(visitor);
			}
			getChildren().accept(visitor);
			visitor.endVisit(this);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		WebMarkupContainer container = new WebMarkupContainer(getComponentId() + "-container");
		Fragment brandLinkFragment;
		if (brandLink == null) {
			brandLinkFragment = null;
		} else {
			brandLinkFragment = new Fragment(NavigationBar.BRAND_LINK_WICKET_ID, "brandLinkFragment", container);
			// TODO add children
		}
		container.add(new NavigationBar(getComponentId(), brandLinkFragment));
		return container;
	}

	/**
	 *
	 */
	public static final class BrandLinkWrapper {

		/**
		 * the configuration
		 */
		private final ComponentGroupConfiguration configuration;

		/**
		 * Constructor.
		 * @param configuration the configuration
		 */
		public BrandLinkWrapper(ComponentGroupConfiguration configuration) {
			this.configuration = configuration;
		}

		/**
		 * Getter method for the configuration.
		 * @return the configuration
		 */
		public ComponentGroupConfiguration getConfiguration() {
			return configuration;
		}

	}
}
