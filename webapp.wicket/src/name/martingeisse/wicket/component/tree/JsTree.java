/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.terms.CommandVerb;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.wicket.component.contextmenu.ContextMenu;
import name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.Component;
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
	 * the commandHandlerBindings
	 */
	private final Map<CommandVerb, JsTreeCommandHandlerBinding<T>> commandHandlerBindings;
	
	/**
	 * the keyBindings
	 */
	private final List<JsTreeKeyBinding> keyBindings;

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
		this.commandHandlerBindings = new HashMap<CommandVerb, JsTreeCommandHandlerBinding<T>>();
		this.keyBindings = new ArrayList<JsTreeKeyBinding>();
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
	
	/**
	 * Binds a command verb to a handler.
	 * @param commandVerb the command verb
	 * @param handler the handler
	 */
	public void bindCommandHandler(CommandVerb commandVerb, IJsTreeCommandHandler<T> handler) {
		JsTreeCommandHandlerBinding<T> binding = new JsTreeCommandHandlerBinding<T>(commandVerb, handler, null);
		commandHandlerBindings.put(commandVerb, binding);
	}
	
	/**
	 * Binds a command verb to a handler, using the specified interceptor.
	 * @param commandVerb the command verb
	 * @param handler the handler
	 * @param interceptor the interceptor, or null for none
	 */
	public void bindCommandHandler(CommandVerb commandVerb, IJsTreeCommandHandler<T> handler, IJavascriptInteractionInterceptor interceptor) {
		JsTreeCommandHandlerBinding<T> binding = new JsTreeCommandHandlerBinding<T>(commandVerb, handler, interceptor);
		commandHandlerBindings.put(commandVerb, binding);
	}
	
	/**
	 * Binds a key to a command verb. The command verb itself should be bound to a handler
	 * to make the key have any effect.
	 * 
	 * @param hotkey the hotkey to bind (jQuery hotkey names)
	 * @param commandVerb the command verb to issue by pressing the key
	 */
	public void bindHotkey(String hotkey, CommandVerb commandVerb) {
		keyBindings.add(new JsTreeKeyBinding(hotkey, commandVerb));
	}
	
	/**
	 * @return the AJAX behavior
	 */
	@SuppressWarnings("unchecked")
	protected TreeAjaxBehavior<T> getBehavior() {
		return getBehaviors(TreeAjaxBehavior.class).get(0);
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
		
		TreeAjaxBehavior<T> behavior = getBehavior();
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
		builder.append("	,\n");
		builder.append("	hotkeys: {\n");
		builder.append("	},\n");
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
	protected void onInteraction(String interaction, List<T> selectedNodes) {
	}
	
	/**
	 * This method is invoked when the client-side scripts send a command verb
	 * to the server. It looks up the appropriate command handler and invokes it.
	 */
	final void onCommandVerb(CommandVerb commandVerb, List<T> selectedNodes) {
		JsTreeCommandHandlerBinding<T> binding = commandHandlerBindings.get(commandVerb);
		if (binding != null) {
			binding.getHandler().handleCommand(commandVerb, selectedNodes);
		}
	}
	
	/**
	 * Converts a list of node indices from JSON to the node type T. This method is mainly useful
	 * if a node list specifier has been submitted by other means than normal tree / context menu
	 * interaction (e.g. as a parameter during a file upload).
	 * 
	 * @param specifier specifier the node index list
	 * @return the nodes
	 */
	public List<T> lookupSelectedNodes(String specifier) {
		return getBehavior().lookupSelectedNodes(specifier);
	}
	
	/**
	 * Returns the interceptor (if any) to use for the specified command verb.
	 */
	final IJavascriptInteractionInterceptor getInterceptor(CommandVerb commandVerb) {
		JsTreeCommandHandlerBinding<T> binding = commandHandlerBindings.get(commandVerb);
		return (binding == null ? null : binding.getInterceptor());
	}
	
}
