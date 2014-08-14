/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.frontend.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * Shows a tabbed panel that uses a page parameter to select the
 * "active" tab, and generates {@link BookmarkablePageLink}s
 * to the current page (i.e. page class and parameters) with just
 * the tab parameter replaced.
 * 
 * Subclasses must provide the list of tabs (label and parameter
 * value) as well as a method to generate the tab contents based
 * on the current parameter value.
 * 
 * This class uses Bootstrap styles. Subclasses can override the styles.
 * 
 * This class is useful because:
 * 
 * - Wicket's {@link TabbedPanel} and {@link AjaxTabbedPanel} use stateful
 *   pages, which is dangerous if the page state can contain stale data
 *   (an indication that such state should be moved from page state to models,
 *   so using this class in such a situation is a dirty quick fix).
 * 
 * - a pure Javascript+AJAX tabbed panel would forget its current tab on
 *   reloading the page
 *   
 * - a pure Javascript (non-AJAX) tabbed panel would also forget its state AND
 *   would have to render all tab contents up-front.
 * 
 */
public abstract class PageParameterDrivenTabPanel extends Panel {

	/**
	 * the parameterName
	 */
	private final String parameterName;

	/**
	 * the tabInfos
	 */
	private final List<AbstractTabInfo> tabInfos = new ArrayList<>();

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param parameterName the name of the page parameter that contains the tab selector
	 */
	public PageParameterDrivenTabPanel(String id, String parameterName) {
		super(id);
		this.parameterName = parameterName;
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param parameterName the name of the page parameter that contains the tab selector
	 * @param model the model
	 */
	public PageParameterDrivenTabPanel(String id, String parameterName, IModel<?> model) {
		super(id, model);
		this.parameterName = parameterName;
	}

	/**
	 * Getter method for the tabInfos.
	 * @return the tabInfos
	 */
	public final List<AbstractTabInfo> getTabInfos() {
		return tabInfos;
	}

	/**
	 * Convenience method to add a tab of arbitrary type.
	 * @param tabInfo the info record for the tab
	 */
	public final void addTab(AbstractTabInfo tabInfo) {
		tabInfos.add(tabInfo);
	}

	/**
	 * Convenience method to add a simple tab.
	 * @param selector the selector that is used as the parameter value
	 * @param title the tab title
	 */
	public final void addTab(String selector, String title) {
		tabInfos.add(new TabInfo(selector, title));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();

		// add tab headers
		add(new ListView<AbstractTabInfo>("tabHeaders", new PropertyModel<List<AbstractTabInfo>>(this, "tabInfos")) {
			@Override
			protected void populateItem(ListItem<AbstractTabInfo> item) {
				AbstractTabInfo tabInfo = item.getModelObject();
				item.add(tabInfo.createTabHeaderComponent(PageParameterDrivenTabPanel.this, "tabHeader"));
			}
		});

		// add tab body
		StringValue currentTabSelector = getPage().getPageParameters().get(parameterName);
		add(createBody("tabBody", currentTabSelector));

	}

	/**
	 * Creates the actual tab body for the current selector. This is usually a {@link Panel}
	 * or {@link Fragment} since the component tag is empty.
	 * 
	 * @param id the wicket id to use
	 * @param selector the current tab selector
	 * @return the tab body component
	 */
	protected abstract Component createBody(String id, StringValue selector);

	/**
	 * Creates a link that opens a tab.
	 * @param id the wicket id
	 * @param selector the selector for the tab to open
	 * @return the link
	 */
	protected final Link<?> createTabLink(String id, Object selector) {
		Page page = getPage();
		PageParameters parameters = new PageParameters(page.getPageParameters());
		parameters.remove(parameterName).add(parameterName, selector);
		return new BookmarkablePageLink<>(id, page.getClass(), parameters);
	}

	/**
	 * Base class for "tab info" records that can be added to the panel to make
	 * tabs selectable.
	 */
	public static abstract class AbstractTabInfo {

		/**
		 * Creates the component for the tab header.
		 * @param tabPanel the tab panel that contains this tab
		 * @param id the wicket id
		 * @return the tab header component
		 */
		protected abstract Component createTabHeaderComponent(PageParameterDrivenTabPanel tabPanel, String id);

	}

	/**
	 * Describes a simple tab.
	 */
	public static final class TabInfo extends AbstractTabInfo {

		/**
		 * the selector
		 */
		private final Object selector;

		/**
		 * the title
		 */
		private final String title;

		/**
		 * Constructor.
		 * @param selector the tab selector that is used as the parameter value
		 * @param title the tab title
		 */
		public TabInfo(Object selector, String title) {
			this.selector = selector;
			this.title = title;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.papyros.frontend.components.PageParameterDrivenTabPanel.AbstractTabInfo#createTabHeaderComponent(name.martingeisse.papyros.frontend.components.PageParameterDrivenTabPanel, java.lang.String)
		 */
		@Override
		protected Component createTabHeaderComponent(PageParameterDrivenTabPanel tabPanel, String id) {
			Link<?> link = tabPanel.createTabLink("link", selector);
			link.add(new Label("title", title));
			Fragment fragment = new Fragment(id, "simpleTabHeader", tabPanel);
			fragment.add(link);
			return fragment;
		}

	}

	/**
	 * Describes a sub-menu that contains a list of {@link AbstractTabInfo} objects.
	 */
	public static final class SubmenuInfo extends AbstractTabInfo {

		/**
		 * the tabInfos
		 */
		private final List<AbstractTabInfo> tabInfos = new ArrayList<>();

		/**
		 * Getter method for the tabInfos.
		 * @return the tabInfos
		 */
		public List<AbstractTabInfo> getTabInfos() {
			return tabInfos;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.papyros.frontend.components.PageParameterDrivenTabPanel.AbstractTabInfo#createTabHeaderComponent(name.martingeisse.papyros.frontend.components.PageParameterDrivenTabPanel, java.lang.String)
		 */
		@Override
		protected Component createTabHeaderComponent(PageParameterDrivenTabPanel tabPanel, String id) {
			throw new NotImplementedException("");
		}

	}

}
