/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel;
import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel.AbstractTabInfo;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;

import com.google.common.collect.ImmutableList;

/**
 * Represents a tab panel.
 */
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
	 * @param id the wicket id
	 * @param parameterName
	 */
	public TabPanelConfiguration(String id, String parameterName) {
		super(id);
		this.parameterName = parameterName;
		this.tabs = new ArrayList<>();
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
	 * Adds a tab.
	 * 
	 * @param tabEntry the TabEntry object that describes the tab.
	 */
	public void addTab(TabEntry tabEntry) {
		tabs.add(tabEntry);
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
		final PageParameterDrivenTabPanel tabPanel = new MyTabPanel(getId(), parameterName, this, tabPanelContainer);
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
		return new String[] {parameterName};
	}

	/**
	 * Represents a tab in the panel.
	 */
	public static final class TabEntry extends AbstractContainerConfiguration {

		/**
		 * the tabInfo
		 */
		private final PageParameterDrivenTabPanel.AbstractTabInfo tabInfo;

		/**
		 * Constructor.
		 * @param id the wicket id
		 * @param tabInfo the tab info
		 * @param children the children
		 */
		public TabEntry(String id, AbstractTabInfo tabInfo, ImmutableList<ComponentConfiguration> children) {
			super(id, children);
			this.tabInfo = tabInfo;
		}

		/**
		 * Getter method for the tabInfo.
		 * @return the tabInfo
		 */
		public PageParameterDrivenTabPanel.AbstractTabInfo getTabInfo() {
			return tabInfo;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.configurationNew.content.ComponentConfiguration#buildComponent()
		 */
		@Override
		public Component buildComponent() {
			throw new UnsupportedOperationException();
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
