/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.wicket.component.contextmenu.ContextMenu;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Response;

/**
 * Implements a Javascript/JsTree-based tree component.
 * 
 * The markup element for this component should contain
 * the markup for each tree node. The tree backbone
 * is generated automatically outside that markup.
 * The populateItem(Item) method is called to
 * generate components for each node.
 * 
 * @param <T> the underlying node type
 */
public abstract class JsTree<T> extends WebMarkupContainer {

	/**
	 * the treeProvider
	 */
	private final ITreeProvider<T> treeProvider;

	/**
	 * the rootInfos
	 */
	private transient NodeInfo<T>[] rootInfos;
	
	/**
	 * the contextMenu
	 */
	private final ContextMenu<List<T>> contextMenu;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param treeProvider the tree provider
	 */
	public JsTree(String id, ITreeProvider<T> treeProvider) {
		this(id, treeProvider, null);
	}
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param treeProvider the tree provider
	 * @param contextMenu the context menu, or null for none
	 */
	public JsTree(String id, ITreeProvider<T> treeProvider, ContextMenu<List<T>> contextMenu) {
		super(id);
		this.treeProvider = treeProvider;
		setOutputMarkupId(true);
		this.contextMenu = contextMenu;
		add(new TreeAjaxBehavior<T>(this));
	}

	/**
	 * Getter method for the treeProvider.
	 * @return the treeProvider
	 */
	public ITreeProvider<T> getTreeProvider() {
		return treeProvider;
	}
	
	/**
	 * Getter method for the contextMenu.
	 * @return the contextMenu
	 */
	public ContextMenu<List<T>> getContextMenu() {
		return contextMenu;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		removeAll();
		rootInfos = generateNodeInfos(treeProvider.getRoots());
		generateItems(rootInfos, 0);
		super.onBeforeRender();
	}

	/**
	 * Generates the internal NodeInfo objects.
	 */
	private NodeInfo<T>[] generateNodeInfos(Iterator<? extends T> nodes) {
		List<NodeInfo<T>> nodeInfos = new ArrayList<NodeInfo<T>>();
		while (nodes.hasNext()) {
			T node = nodes.next();
			NodeInfo<T> nodeInfo = new NodeInfo<T>();
			nodeInfo.model = treeProvider.model(node);
			nodeInfo.children = generateNodeInfos(treeProvider.getChildren(node));
			nodeInfos.add(nodeInfo);
		}
		NodeInfo<T>[] nodeInfoArray = GenericTypeUtil.unsafeCast(new NodeInfo<?>[nodeInfos.size()]);
		return nodeInfos.toArray(nodeInfoArray);
	}

	/**
	 * Generates item components for all node infos and returns the new item counter.
	 */
	private int generateItems(NodeInfo<T>[] nodeInfos, int counter) {
		for (NodeInfo<T> nodeInfo : nodeInfos) {
			Item<T> item = new Item<T>(Integer.toString(counter), counter, nodeInfo.model);
			add(item);
			populateItem(item);
			nodeInfo.item = item;
			nodeInfo.index = counter;
			counter++;
			counter += generateItems(nodeInfo.children, counter);
		}
		return counter;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		WicketHeadUtil.includeClassJavascript(response, JsTree.class);
		
		@SuppressWarnings("unchecked")
		TreeAjaxBehavior<T> behavior = getBehaviors(TreeAjaxBehavior.class).get(0);
		StringBuilder builder = new StringBuilder();
		builder.append("$('#").append(getMarkupId()).append("').createJsTree({\n");
		builder.append("	ajaxCallback: ").append(behavior.getCallbackSpecifier()).append(",\n");
		builder.append("	contextMenuData: ");
		if (contextMenu != null) {
			ContextMenu.renderHead(response);
			contextMenu.buildCreateExpression(builder, "#" + getMarkupId(), behavior);
		} else {
			builder.append("null");
		}
		builder.append(",\n");
		
		builder.append("});\n");
		response.render(OnDomReadyHeaderItem.forScript(builder.toString()));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#onRender()
	 */
	@Override
	protected void onRender() {
		Response response = getResponse();
		response.write("<div id=\"" + getMarkupId() + "\">");
		renderNodes(rootInfos);
		response.write("</div>");
	}
	
	/**
	 * Renders the nodes recursively, generating JsTree markup.
	 */
	private void renderNodes(NodeInfo<T>[] nodeInfos) {
		Response response = getResponse();
		response.write("<ul>");
		for (NodeInfo<T> nodeInfo : nodeInfos) {
			response.write("<li class=\"jstree-open\">");
			nodeInfo.item.render();
			renderNodes(nodeInfo.children);
			response.write("<div style=\"display: none\">" + nodeInfo.index + "</div>");
			response.write("</li>");
		}
		response.write("</ul>");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#getMarkup(org.apache.wicket.Component)
	 */
	@Override
	public IMarkupFragment getMarkup(Component child) {
		return getMarkup();
	}

	/**
	 * Populates an item with components.
	 * @param item the item to populate
	 */
	protected abstract void populateItem(Item<T> item);

	/**
	 * Caches the data structure for each node.
	 */
	private static final class NodeInfo<T> {
		IModel<T> model;
		NodeInfo<T>[] children;
		Item<T> item;
		int index;
	}

	/**
	 * This method is invoked when the client-side scripts notify the server about
	 * a user interaction. The default implementation does nothing.
	 * 
	 * @param target the ART
	 * @param interaction a string describing the interaction, e.g. "dblclick"
	 * for a double-click
	 * @param selectedNodes the selected tree nodes
	 */
	protected void onInteraction(AjaxRequestTarget target, String interaction, List<T> selectedNodes) {
	}
	
}
