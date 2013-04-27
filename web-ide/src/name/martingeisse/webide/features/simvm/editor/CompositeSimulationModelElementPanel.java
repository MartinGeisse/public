/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import java.util.List;

import name.martingeisse.webide.features.simvm.model.AbstractCompositeSimulationModelElement;
import name.martingeisse.webide.features.simvm.model.ICompositeSimulationModelElement;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This panel is used for the UI of {@link AbstractCompositeSimulationModelElement}.
 * It shows a tab panel with one tab for each sub-element.
 * 
 * The panel is also able to show simulator controls in case it is used for
 * a primary model element.
 * 
 * @param <S> the sub-element type
 */
public class CompositeSimulationModelElementPanel<S extends ISimulationModelElement> extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public CompositeSimulationModelElementPanel(String id, IModel<ICompositeSimulationModelElement<S>> model) {
		super(id, model);
		IModel<List<S>> subElementsModel = new PropertyModel<List<S>>(model, "subElements");
		add(new ListView<S>("tabButtons", subElementsModel) {
			@Override
			protected void populateItem(ListItem<S> item) {
				S element = item.getModelObject();
				item.add(new Label("text", element.getTitle()));
			}
		});
		add(new ListView<S>("tabPanes", subElementsModel) {
			@Override
			protected void populateItem(ListItem<S> item) {
				@SuppressWarnings("unchecked")
				IModel<ISimulationModelElement> model = (IModel<ISimulationModelElement>)(IModel<?>)item.getModel();
				item.add(model.getObject().createComponent("tabPane", model));
			}
		});
		add(createRightPanel("rightPanel"));
	}

	/**
	 * @return the model for the simulation model element
	 */
	@SuppressWarnings("unchecked")
	public IModel<? extends ICompositeSimulationModelElement<S>> getModel() {
		return (IModel<? extends ICompositeSimulationModelElement<S>>)getDefaultModel();
	}

	/**
	 * @return the simulation model element that is displayed by this panel
	 */
	public ICompositeSimulationModelElement<S> getSimulationModelElement() {
		return getModel().getObject();
	}
	
	/**
	 * This method allows to insert custom components to the right of the tab
	 * buttons. By default, it creates an empty component.
	 * 
	 * @param id the wicket id
	 * @return the component
	 */
	protected Component createRightPanel(String id) {
		return new WebComponent(id);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript("initializeSimvmEditorPanel()"));
	}
	
}
