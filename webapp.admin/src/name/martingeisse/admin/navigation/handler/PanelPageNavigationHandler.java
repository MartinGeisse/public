/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.component.page.PanelPage;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
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
	 * the allowModelLessConstructor
	 */
	private boolean allowModelLessConstructor;

	/**
	 * the preferModelLessConstructor
	 */
	private boolean preferModelLessConstructor;

	/**
	 * Constructor.
	 */
	public PanelPageNavigationHandler() {
	}

	/**
	 * Constructor.
	 * @param panelClass see {@link PanelPage} for a description of this parameter
	 * @param model see {@link PanelPage} for a description of this parameter
	 * @param allowModelLessConstructor see {@link PanelPage} for a description of this parameter
	 * @param preferModelLessConstructor see {@link PanelPage} for a description of this parameter
	 */
	public PanelPageNavigationHandler(final Class<? extends Panel> panelClass, final IModel<?> model, final boolean allowModelLessConstructor, final boolean preferModelLessConstructor) {
		this.panelClass = panelClass;
		this.model = model;
		this.allowModelLessConstructor = allowModelLessConstructor;
		this.preferModelLessConstructor = preferModelLessConstructor;
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
	 * Getter method for the allowModelLessConstructor.
	 * @return the allowModelLessConstructor
	 */
	public boolean isAllowModelLessConstructor() {
		return allowModelLessConstructor;
	}

	/**
	 * Setter method for the allowModelLessConstructor.
	 * @param allowModelLessConstructor the allowModelLessConstructor to set
	 */
	public void setAllowModelLessConstructor(final boolean allowModelLessConstructor) {
		this.allowModelLessConstructor = allowModelLessConstructor;
	}

	/**
	 * Getter method for the preferModelLessConstructor.
	 * @return the preferModelLessConstructor
	 */
	public boolean isPreferModelLessConstructor() {
		return preferModelLessConstructor;
	}

	/**
	 * Setter method for the preferModelLessConstructor.
	 * @param preferModelLessConstructor the preferModelLessConstructor to set
	 */
	public void setPreferModelLessConstructor(final boolean preferModelLessConstructor) {
		this.preferModelLessConstructor = preferModelLessConstructor;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(final String id, final NavigationNode node) {
		return new MyLink(id, panelClass, model, allowModelLessConstructor, preferModelLessConstructor);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createReplaceHandlerException(name.martingeisse.admin.navigation.NavigationNode, org.apache.wicket.Component)
	 */
	@Override
	public ReplaceHandlerException createReplaceHandlerException(NavigationNode node, Component context) {
		return new RestartResponseException(new PanelPage(panelClass, model, allowModelLessConstructor, preferModelLessConstructor));
	}

	/**
	 * Link implementation for nodes of this type.
	 */
	private static class MyLink extends StatelessLink<Void> {

		/**
		 * the panelClass
		 */
		private final Class<? extends Panel> panelClass;

		/**
		 * the model
		 */
		private final IModel<?> model;

		/**
		 * the allowModelLessConstructor
		 */
		private boolean allowModelLessConstructor;

		/**
		 * the preferModelLessConstructor
		 */
		private boolean preferModelLessConstructor;

		/**
		 * Constructor.
		 */
		MyLink(final String id, final Class<? extends Panel> panelClass, final IModel<?> model, final boolean allowModelLessConstructor, final boolean preferModelLessConstructor) {
			super(id);
			this.panelClass = panelClass;
			this.model = model;
			this.allowModelLessConstructor = allowModelLessConstructor;
			this.preferModelLessConstructor = preferModelLessConstructor;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.link.Link#onClick()
		 */
		@Override
		public void onClick() {
			RequestCycle.get().setResponsePage(new PanelPage(panelClass, model, allowModelLessConstructor, preferModelLessConstructor));
		}

	}
}
