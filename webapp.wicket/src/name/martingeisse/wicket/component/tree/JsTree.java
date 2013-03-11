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
import org.apache.wicket.markup.html.list.AbstractItem;
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
	 * the rootItems
	 */
	private transient Item<T>[] rootItems;

	/**
	 * the contextMenu
	 */
	private final ContextMenu<List<T>> contextMenu;

	/**
	 * the commandHandlerBindings
	 */
	private final Map<CommandVerb, JsTreeCommandHandlerBinding<T, ?>> commandHandlerBindings;

	/**
	 * the keyBindings
	 */
	private final List<JsTreeKeyBinding> keyBindings;

	/**
	 * the doubleClickCommandVerb
	 */
	private CommandVerb doubleClickCommandVerb;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param treeProvider the tree provider
	 */
	public JsTree(final String id, final ITreeProvider<T> treeProvider) {
		this(id, treeProvider, null);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param treeProvider the tree provider
	 * @param contextMenu the context menu, or null for none
	 */
	public JsTree(final String id, final ITreeProvider<T> treeProvider, final ContextMenu<List<T>> contextMenu) {
		super(id);
		this.treeProvider = treeProvider;
		setOutputMarkupId(true);
		this.contextMenu = contextMenu;
		add(new TreeAjaxBehavior<T>(this));
		this.commandHandlerBindings = new HashMap<CommandVerb, JsTreeCommandHandlerBinding<T, ?>>();
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
	public <P> void bindCommandHandler(final CommandVerb commandVerb, final IJsTreeCommandHandler<T, P> handler) {
		final JsTreeCommandHandlerBinding<T, P> binding = new JsTreeCommandHandlerBinding<T, P>(commandVerb, handler, null);
		commandHandlerBindings.put(commandVerb, binding);
	}

	/**
	 * Binds a command verb to a handler, using the specified interceptor.
	 * @param commandVerb the command verb
	 * @param handler the handler
	 * @param interceptor the interceptor, or null for none
	 */
	public <P> void bindCommandHandler(final CommandVerb commandVerb, final IJsTreeCommandHandler<T, P> handler, final IJavascriptInteractionInterceptor<P> interceptor) {
		final JsTreeCommandHandlerBinding<T, P> binding = new JsTreeCommandHandlerBinding<T, P>(commandVerb, handler, interceptor);
		commandHandlerBindings.put(commandVerb, binding);
	}

	/**
	 * Binds a key to a command verb. The command verb itself should be bound to a handler
	 * to make the key have any effect.
	 * 
	 * @param hotkey the hotkey to bind (jQuery hotkey names)
	 * @param commandVerb the command verb to issue by pressing the key
	 */
	public void bindHotkey(final String hotkey, final CommandVerb commandVerb) {
		keyBindings.add(new JsTreeKeyBinding(hotkey, commandVerb));
	}

	/**
	 * Getter method for the doubleClickCommandVerb.
	 * @return the doubleClickCommandVerb
	 */
	public CommandVerb getDoubleClickCommandVerb() {
		return doubleClickCommandVerb;
	}

	/**
	 * Setter method for the doubleClickCommandVerb.
	 * @param doubleClickCommandVerb the doubleClickCommandVerb to set
	 */
	public void setDoubleClickCommandVerb(final CommandVerb doubleClickCommandVerb) {
		this.doubleClickCommandVerb = doubleClickCommandVerb;
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
		rootItems = generateItems(treeProvider.getRoots());
		super.onBeforeRender();
	}

	private Item<T>[] generateItems(final Iterator<? extends T> nodes) {
		// TODO: will cause problems if the same page instance gets rendered
		// in two browser tabs and an AJAX request arrives for outdated item indices
		// maybe delegate wicket id generation to a strategy
		final List<Item<T>> items = new ArrayList<Item<T>>();
		while (nodes.hasNext()) {
			final T node = nodes.next();
			final Item<T> item = new Item<T>(Integer.toString(size()), treeProvider.model(node));
			add(item);
			items.add(item);
			populateItem(item);
			item.treeChildren = generateItems(treeProvider.getChildren(node));
		}
		final Item<T>[] itemArray = GenericTypeUtil.unsafeCast(new Item[items.size()]);
		return items.toArray(itemArray);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		WicketHeadUtil.includeClassJavascript(response, JsTree.class);

		final TreeAjaxBehavior<T> behavior = getBehavior();
		final StringBuilder builder = new StringBuilder();
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
		for (final JsTreeKeyBinding keyBinding : keyBindings) {
			builder.append("		'").append(keyBinding.getHotkey()).append("': function() {\n");
			getBehavior().buildCommandVerbInteraction(builder, keyBinding.getCommandVerb());
			builder.append("		},\n");
		}
		builder.append("	},\n");
		builder.append("});\n");
		response.render(OnDomReadyHeaderItem.forScript(builder.toString()));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#onRender()
	 */
	@Override
	protected void onRender() {
		final Response response = getResponse();
		response.write("<div id=\"" + getMarkupId() + "\">");
		renderItems(rootItems);
		response.write("</div>");
	}

	/**
	 * Renders the nodes recursively, generating JsTree markup.
	 */
	private void renderItems(final Item<T>[] items) {
		final Response response = getResponse();
		response.write("<ul>");
		for (final Item<T> item : items) {
			response.write("<li class=\"jstree-open\">");
			item.render();
			renderItems(item.treeChildren);
			response.write("<div style=\"display: none\">" + item.getId() + "</div>");
			response.write("</li>");
		}
		response.write("</ul>");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#getMarkup(org.apache.wicket.Component)
	 */
	@Override
	public IMarkupFragment getMarkup(final Component child) {
		return getMarkup();
	}

	/**
	 * Populates an item with components.
	 * @param item the item to populate
	 */
	protected abstract void populateItem(Item<T> item);

	/**
	 * This method is invoked when the client-side scripts notify the server about
	 * a user interaction. The default implementation does nothing.
	 * 
	 * @param target the ART
	 * @param interaction a string describing the interaction, e.g. "dblclick"
	 * for a double-click
	 * @param selectedNodes the selected tree nodes
	 * @param data the JSON-parsed extra input parameter, or null if not present in the request
	 */
	protected void onInteraction(final String interaction, final List<T> selectedNodes, final Object data) {
	}

	/**
	 * This method is invoked when the client-side scripts send a command verb
	 * to the server. It looks up the appropriate command handler and invokes it.
	 * @param data the JSON-parsed extra input parameter, or null if not present in the request
	 */
	final void onCommandVerb(final CommandVerb commandVerb, final List<T> selectedNodes, final Object data) {
		final JsTreeCommandHandlerBinding<T, ?> binding = commandHandlerBindings.get(commandVerb);
		if (binding != null) {
			binding.invoke(selectedNodes, data);
		}
	}

	/**
	 * Called for the "dblclick" interaction in addition to onInteraction(). Checks if a
	 * double-click command verb is set, and if so, fires it.
	 * @param data the JSON-parsed extra input parameter, or null if not present in the request
	 */
	final void onDoubleClick(final List<T> selectedNodes, final Object data) {
		if (doubleClickCommandVerb != null) {
			onCommandVerb(doubleClickCommandVerb, selectedNodes, data);
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
	public List<T> lookupSelectedNodes(final String specifier) {
		return getBehavior().lookupSelectedNodes(specifier);
	}

	/**
	 * Returns the interceptor (if any) to use for the specified command verb.
	 */
	final IJavascriptInteractionInterceptor<?> getInterceptor(final CommandVerb commandVerb) {
		final JsTreeCommandHandlerBinding<T, ?> binding = commandHandlerBindings.get(commandVerb);
		return (binding == null ? null : binding.getInterceptor());
	}

	/**
	 * This class is used as a component for the selectable part of an item.
	 * The item knows its tree children but they are not child components.
	 *
	 * @param <T> the tree node type
	 */
	public static class Item<T> extends AbstractItem {

		Item<T>[] treeChildren;

		Item(final String id, final IModel<?> model) {
			super(id, model);
		}

		/**
		 * @return the model for this item
		 */
		@SuppressWarnings("unchecked")
		public final IModel<T> getModel() {
			return (IModel<T>)getDefaultModel();
		}

		/**
		 * @return the model object for this item
		 */
		@SuppressWarnings("unchecked")
		public final T getModelObject() {
			return (T)getDefaultModelObject();
		}

	}

}
