/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;

import com.google.common.collect.ImmutableList;

import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel;
import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel.AbstractTabInfo;

/**
 * Represents a tab panel.
 */
public final class TabPanelConfiguration extends AbstractComponentConfiguration {

	/**
	 * the parameterName
	 */
	private final String parameterName;
	
	/**
	 * the tabs
	 */
	private final List<TabEntry> tabs;

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

	/**
	 * Adds a tab.
	 * 
	 * @param tabEntry the TabEntry object that describes the tab.
	 */
	public void addTab(TabEntry tabEntry) {
		tabs.add(tabEntry);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		final WebMarkupContainer tabPanelContainer = new WebMarkupContainer(getId() + "-container");
		final PageParameterDrivenTabPanel tabPanel = new PageParameterDrivenTabPanel(getId(), parameterName) {
			
			@Override
			protected Component createBody(String id, String selector) {
				for (TabEntry tabEntry : tabs) {
					AbstractTabInfo tabInfo = tabEntry.getTabInfo();
					if (tabInfo instanceof TabInfo) {
						if (selector.equals(((TabInfo)tabInfo).getSelector())) {
							return tabEntry.buildTabEntry(id, tabPanelContainer);
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
			
		};
		for (TabEntry tab : tabs) {
			tabPanel.addTab(tab.getTabInfo());
		}
		tabPanelContainer.add(tabPanel);
		return tabPanelContainer;
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
}
