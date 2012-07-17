/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.pages.PanelPage;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * This class links to a {@link PanelPage}. Only supports creating the
 * panel on-the-fly, not re-using an existing panel (that would be
 * pointless for a navigation node).
 */
public final class PanelPageNavigationHandler extends AbstractNavigationNodeHandler {

	/**
	 * the panelClass
	 */
	private Class<? extends Panel> panelClass;

	/**
	 * the model
	 */
	private IModel<?> model;

	/**
	 * the modelIsOptional
	 */
	private boolean modelIsOptional;

	/**
	 * Constructor.
	 */
	public PanelPageNavigationHandler() {
	}

	/**
	 * Constructor.
	 * @param panelClass see {@link PanelPage} for a description of this parameter
	 * @param model see {@link PanelPage} for a description of this parameter
	 * @param modelIsOptional see {@link PanelPage} for a description of this parameter
	 */
	public PanelPageNavigationHandler(final Class<? extends Panel> panelClass, final IModel<?> model, final boolean modelIsOptional) {
		this.panelClass = panelClass;
		this.model = model;
		this.modelIsOptional = modelIsOptional;
	}

	/**
	 * Getter method for the panelClass.
	 * @return the panelClass
	 */
	public Class<? extends Panel> getPanelClass() {
		return panelClass;
	}

	/**
	 * Setter method for the panelClass.
	 * @param panelClass the panelClass to set
	 */
	public void setPanelClass(final Class<? extends Panel> panelClass) {
		this.panelClass = panelClass;
	}

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	public IModel<?> getModel() {
		return model;
	}

	/**
	 * Setter method for the model.
	 * @param model the model to set
	 */
	public void setModel(final IModel<?> model) {
		this.model = model;
	}

	/**
	 * Getter method for the modelIsOptional.
	 * @return the modelIsOptional
	 */
	public boolean isModelIsOptional() {
		return modelIsOptional;
	}

	/**
	 * Setter method for the modelIsOptional.
	 * @param modelIsOptional the modelIsOptional to set
	 */
	public void setModelIsOptional(final boolean modelIsOptional) {
		this.modelIsOptional = modelIsOptional;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createClone()
	 */
	@Override
	public INavigationNodeHandler createClone() {
		return new PanelPageNavigationHandler(panelClass, model, modelIsOptional);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(final String id, NavigationNode node) {
		return new MyLink(id, panelClass, model, modelIsOptional);
	}

	/**
	 * Link implementation for nodes of this type.
	 */
	private static class MyLink extends StatelessLink<Void> {
	
		/**
		 * the panelClass
		 */
		private Class<? extends Panel> panelClass;

		/**
		 * the model
		 */
		private IModel<?> model;

		/**
		 * the modelIsOptional
		 */
		private boolean modelIsOptional;

		/**
		 * Constructor.
		 */
		MyLink(String id, Class<? extends Panel> panelClass, IModel<?> model, boolean modelIsOptional) {
			super(id);
			this.panelClass = panelClass;
			this.model = model;
			this.modelIsOptional = modelIsOptional;
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.link.Link#onClick()
		 */
		@Override
		public void onClick() {
			RequestCycle.get().setResponsePage(new PanelPage(panelClass, model, modelIsOptional));
		}
		
	}
}
