/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.experiment;

import name.martingeisse.webide.workbench.WorkbenchPage;
import name.martingeisse.wicket.component.tree.JsTree;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 */
public class ExperimentPage extends WebPage {

	/**
	 * Constructor.
	 */
	public ExperimentPage() {
		add(new JsTree<Integer>("tree", new MyTreeProvider(60, 35)) {
			@Override
			protected void populateItem(Item<Integer> item) {
				item.add(new Label("value", item.getModelObject()));
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WorkbenchPage.class, "common.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WorkbenchPage.class, "jquery.jstree.js")));
		super.renderHead(response);
	}
	
}
