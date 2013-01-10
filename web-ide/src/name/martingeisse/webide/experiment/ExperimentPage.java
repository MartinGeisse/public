/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.experiment;

import name.martingeisse.wicket.component.tree.JsTree;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;

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
	
}
