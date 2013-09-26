/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.component;

import java.util.List;

import name.martingeisse.admin.navigation.NavigationUtil;
import name.martingeisse.admon.navigation.NavigationNode;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This component uses a list model of element type {@link NavigationNode}
 * and shows links for all nodes. It does not have its own markup
 * but is attached to a container element in the markup of outer components.
 * 
 * This component expects two nested component elements inside its markup; the
 * outer one should be a link with the wicket:id="link", and the inner
 * one a span or div with the wicket:id="title". A link and a label are
 * attached to these, respectively, and the markup is repeated for each node.
 * 
 * This class has a maximum nesting depth that determines how many node
 * sub-levels are shown. Each sub-level of each node is recursively handled
 * by an instance of this class. The maximum nesting depth should correspond
 * to the nesting depth of the component tags in the markup. The default
 * value is 0 (only the list from the model is shown, no sub-levels).
 * If the value is greater than 0 then the sub-list-view is attached to a
 * component tag with the wicket:id="children" within the item (but outside
 * the link).
 */
public final class NavigationMenuView extends ListView<NavigationNode> {

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
	 * @param maximumNestingDepth the maximum nesting depth
	 */
	public NavigationMenuView(final String id, final int maximumNestingDepth) {
		super(id);
		this.maximumNestingDepth = maximumNestingDepth;
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 * @param maximumNestingDepth the maximum nesting depth
	 */
	public NavigationMenuView(final String id, final IModel<List<NavigationNode>> model, final int maximumNestingDepth) {
		super(id, model);
		this.maximumNestingDepth = maximumNestingDepth;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		currentPagePath = StringUtils.defaultString(NavigationUtil.getNavigationPathForPage(getPage()));
		super.onBeforeRender();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
	 */
	@Override
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

		// create sub-lists
		if (maximumNestingDepth > 0) {
			final IModel<List<NavigationNode>> childrenModel = new PropertyModel<List<NavigationNode>>(item.getModel(), "children");
			item.add(new NavigationMenuView("children", childrenModel, maximumNestingDepth - 1));
		}

	}

}
