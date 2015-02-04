/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;

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
		return new PageParameterDrivenTabPanel(getId(), parameterName) {
			@Override
			protected Component createBody(String id, String selector) {
				for (TabEntry tabEntry : tabs) {
					AbstractTabInfo tabInfo = tabEntry.getTabInfo();
					if (tabInfo instanceof TabInfo) {
						if (selector.equals(((TabInfo)tabInfo).getSelector())) {
							return tabEntry.buildComponent();
						}
					} else {
						throw new RuntimeException("cannot handle AbstractTabInfo objects other than TabInfo right now");
					}
				}
				return new EmptyPanel(id);
			}
		};
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
		 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
		 */
		@Override
		protected MarkupContainer buildContainer() {
			return new Fragment(PageParameterDrivenTabPanel.TAB_BODY_MARKUP_ID, getId(), markupProvider);
		}

	}
}
