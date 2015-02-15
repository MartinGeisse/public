/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.common.terms.Multiplicity;
import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.xmlbind.attribute.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xmlbind.attribute.BindAttribute;
import name.martingeisse.guiserver.xmlbind.element.BindComponentElement;
import name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler;
import name.martingeisse.guiserver.xmlbind.result.MarkupContent;
import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;

/**
 * Represents a tab panel.
 */
@BindComponentElement(localName = "tabPanel", attributes = {
	@BindAttribute(name = "parameter", optionality = AttributeValueBindingOptionality.OPTIONAL),
}, childObjectMultiplicity = Multiplicity.NONZERO, childObjectElementNameFilter = {
	"tab"
})
public final class TabPanelConfiguration extends AbstractComponentConfiguration implements IConfigurationSnippet, UrlSubpathComponentConfiguration {

	/**
	 * the parameterName
	 */
	private final String parameterName;

	/**
	 * the tabs
	 */
	private final List<TabEntry> tabs;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Constructor.
	 * @param parameterName the name of the tab-selecting parameter
	 * @param tabs the tabs
	 */
	public TabPanelConfiguration(String parameterName, List<TabEntry> tabs) {
		this.parameterName = parameterName;
		this.tabs = tabs;
	}

	/**
	 * Getter method for the parameterName.
	 * @return the parameterName
	 */
	public String getParameterName() {
		String id = getId();
		if (id == null) {
			throw new IllegalStateException("cannot determine parameter name before an ID has been assigned");
		}
		return (parameterName == null ? id : parameterName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		XMLStreamWriter writer = assembler.getMarkupWriter();
		String id = getId();
		writer.writeStartElement("div");
		writer.writeAttribute("wicket:id", id + "-container");
		writer.writeEmptyElement("div");
		writer.writeAttribute("wicket:id", id);
		for (TabEntry tab : tabs) {
			tab.assemble(assembler);
		}
		writer.writeEndElement();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int handle) {
		this.snippetHandle = handle;
	}

	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}

	/**
	 * Getter method for the tabs.
	 * @return the tabs
	 */
	List<TabEntry> getTabs() {
		return tabs;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		final WebMarkupContainer tabPanelContainer = new WebMarkupContainer(getId() + "-container");
		final PageParameterDrivenTabPanel tabPanel = new MyTabPanel(getId(), getParameterName(), this, tabPanelContainer);
		for (TabEntry tab : tabs) {
			tabPanel.addTab(tab.getTabInfo());
		}
		tabPanelContainer.add(tabPanel);
		return tabPanelContainer;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#accept(name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitor)
	 */
	@Override
	public void accept(IComponentConfigurationVisitor visitor) {
		if (visitor.beginVisit(this)) {
			for (TabEntry tab : tabs) {
				tab.accept(visitor);
			}
			visitor.endVisit(this);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.UrlSubpathComponentConfiguration#getSubpathSegmentParameterNames()
	 */
	@Override
	public String[] getSubpathSegmentParameterNames() {
		return new String[] {
			getParameterName()
		};
	}

	/**
	 * Represents a tab in the panel.
	 */
	public static final class TabEntry extends AbstractContainerConfiguration {

		/**
		 * the title
		 */
		private final String title;
		
		/**
		 * the selector
		 */
		private final String selector;

		/**
		 * the tabInfo
		 */
		private final PageParameterDrivenTabPanel.AbstractTabInfo tabInfo;

		/**
		 * Constructor.
		 * @param title the title text
		 * @param selector the selector
		 * @param markupContent the markup content
		 */
		public TabEntry(String title, String selector, MarkupContent<ComponentConfiguration> markupContent) {
			super(markupContent);
			this.title = title;
			this.selector = selector;
			this.tabInfo = new PageParameterDrivenTabPanel.TabInfo(title, selector);
		}

		/**
		 * Getter method for the title.
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}
		
		/**
		 * Getter method for the selector.
		 * @return the selector
		 */
		public String getSelector() {
			return selector;
		}
		
		/**
		 * Getter method for the tabInfo.
		 * @return the tabInfo
		 */
		public PageParameterDrivenTabPanel.AbstractTabInfo getTabInfo() {
			return tabInfo;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#assembleContainerIntro(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
		 */
		@Override
		protected void assembleContainerIntro(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
			writeOpeningComponentTag(assembler, "wicket:fragment");
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.configurationNew.content.ComponentConfiguration#buildComponent()
		 */
		@Override
		public Component buildComponent() {
			return null;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
		 */
		@Override
		protected MarkupContainer buildContainer() {
			throw new UnsupportedOperationException();
		}

		/**
		 * Builds the component for a TabEntry.
		 * 
		 * @return the component
		 */
		public MarkupContainer buildTabEntry(String callingMarkupId, MarkupContainer markupProvider) {
			MarkupContainer container = new Fragment(callingMarkupId, getId(), markupProvider);
			getChildren().buildAndAddComponents(container);
			return container;
		}

	}

	/**
	 * Specialized tab panel implementation.
	 */
	public static final class MyTabPanel extends PageParameterDrivenTabPanel {

		/**
		 * the markupProvider
		 */
		private final WebMarkupContainer markupProvider;

		/**
		 * the snippetHandle
		 */
		private final int snippetHandle;

		/**
		 * the cachedTabPanelConfiguration
		 */
		private transient TabPanelConfiguration cachedTabPanelConfiguration;

		/**
		 * Constructor.
		 * @param id the wicket id
		 * @param parameterName the name of the page parameter that contains the tab selector
		 * @param tabPanelConfiguration the configuration object
		 * @param markupProvider the component that contains the markup fragments for the tabs
		 */
		public MyTabPanel(String id, String parameterName, TabPanelConfiguration tabPanelConfiguration, WebMarkupContainer markupProvider) {
			super(id, parameterName);
			this.snippetHandle = tabPanelConfiguration.getSnippetHandle();
			this.cachedTabPanelConfiguration = tabPanelConfiguration;
			this.markupProvider = markupProvider;
		}

		/**
		 * Getter method for the tab panel configuration.
		 * @return the tab panel configuration.
		 */
		public final TabPanelConfiguration getTabPanelConfiguration() {
			if (cachedTabPanelConfiguration == null) {
				cachedTabPanelConfiguration = (TabPanelConfiguration)Configuration.getInstance().getSnippet(snippetHandle);
			}
			return cachedTabPanelConfiguration;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel#createBody(java.lang.String, java.lang.String)
		 */
		@Override
		protected Component createBody(String id, String selector) {
			for (TabEntry tabEntry : getTabPanelConfiguration().getTabs()) {
				AbstractTabInfo tabInfo = tabEntry.getTabInfo();
				if (tabInfo instanceof TabInfo) {
					if (selector.equals(((TabInfo)tabInfo).getSelector())) {
						return tabEntry.buildTabEntry(id, markupProvider);
					}
				} else {
					throw new RuntimeException("cannot handle AbstractTabInfo objects other than TabInfo right now");
				}
			}
			return new EmptyPanel(id);
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.panel.Panel#newMarkupSourcingStrategy()
		 */
		@Override
		protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
			return new PanelMarkupSourcingStrategy(true);
		}

	}

}
