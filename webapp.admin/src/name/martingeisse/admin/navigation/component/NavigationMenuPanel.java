/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.component;

import java.util.List;

import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This component uses a list model of element type {@link NavigationNode}
 * and shows links for all nodes. It has its own markup that is repeated
 * at each nesting level. 
 * 
 * This class has a maximum nesting depth that determines how many node
 * sub-levels are shown. Each sub-level of each node is recursively handled
 * by an instance of this class. Since the markup from this class is
 * repeated at each nesting level, the supported depth is infinite in
 * principle, and the default value for the maximum nesting depth is
 * in fact infinite (actually, {@link Integer#MAX_VALUE}.
 * 
 * Subclass markup: No combined markup inheritance via wicket:extend
 * is supported (i.e. the markup for this component does not contain a
 * wicket:child), but subclasses may override the whole markup.
 */
public final class NavigationMenuPanel extends Panel {

	/**
	 * the maximumNestingDepth
	 */
	private final int maximumNestingDepth;

	/**
	 * the currentPagePath
	 */
	private transient String currentPagePath;

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public NavigationMenuPanel(final String id) {
		this(id, Integer.MAX_VALUE);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param maximumNestingDepth the maximum nesting depth
	 */
	public NavigationMenuPanel(final String id, final int maximumNestingDepth) {
		super(id);
		this.maximumNestingDepth = maximumNestingDepth;
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public NavigationMenuPanel(final String id, final IModel<List<NavigationNode>> model) {
		this(id, model, Integer.MAX_VALUE);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 * @param maximumNestingDepth the maximum nesting depth
	 */
	public NavigationMenuPanel(final String id, final IModel<List<NavigationNode>> model, final int maximumNestingDepth) {
		super(id, model);
		this.maximumNestingDepth = maximumNestingDepth;
	}

	/**
	 * Returns the model for the node list.
	 * @return the model
	 */
	@SuppressWarnings("unchecked")
	public IModel<List<NavigationNode>> getModel() {
		return (IModel<List<NavigationNode>>)getDefaultModel();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new ListView<NavigationNode>("nodes", getModel()) {
			@Override
			protected void populateItem(final ListItem<NavigationNode> item) {
				NavigationMenuPanel.this.populateItem(item);
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		currentPagePath = StringUtils.defaultString(NavigationUtil.getNavigationPathForPage(getPage()));
		super.onBeforeRender();
	}

	/**
	 * This method is used to populate the node items.
	 * @param item the item to populate
	 */
	protected void populateItem(final ListItem<NavigationNode> item) {

		// create the node link
		final NavigationNode node = item.getModelObject();
		final AbstractLink link = node.createLink("link");
		link.add(new Label("title", node.getTitle()));
		item.add(link);

		// mark or disable the link based on the current location
		final String nodePath = node.getPath();
		if (nodePath.equals(currentPagePath)) {
			link.setEnabled(false);
		} else if (node.isEqualOrPlausibleAncestorOf(currentPagePath)) {
			link.add(new AttributeAppender("style", "color: red"));
		}

		// create a panel for the children, but hide if the maximum nesting level is reached
		final IModel<List<NavigationNode>> childrenModel = new PropertyModel<List<NavigationNode>>(item.getModel(), "children");
		final NavigationMenuPanel childrenPanel = new NavigationMenuPanel("children", childrenModel, maximumNestingDepth - 1);
		item.add(childrenPanel);
		if (maximumNestingDepth == 0) {
			childrenPanel.setVisible(false);
		}

	}

}
