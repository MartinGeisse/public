/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;

/**
 * A container that can be used to display data that takes a
 * long time to load. It will show a loading indicator
 * initially, while sending an AJAX request immediately that
 * will eventually return with the loaded data. Specifically,
 * this component will render its border body only for AJAX
 * requests, not for normal requests, and it will include a
 * scriptlet for the AJAX re-render request in the page.
 * Not rendering the body is done using {@link #setVisible(boolean)}.
 * 
 * If accessing the body's model causes a blocking load to
 * occur, the body should make sure that it accesses its model
 * only when visible -- the body will be initialized and
 * configured as usual, but invisible. In concrete terms this
 * means that access to its model should be restricted to
 * methods like onBeforeRender() and rendering itself.
 * 
 * The calling code and/or subclass should cause a fire-and-forget
 * prefetch request for the body's model data when receiving the
 * initial page request, if in any way possible. This speeds
 * things up a little since the time between the initial page
 * request and the AJAX request is also used to load the body's
 * data. If and how such a prefetch request is possible is
 * specific to the calling code or subclass, and thus out of
 * scope for this class.
 * 
 * The loading indicator is the replacement in terms of the
 * {@link ReplacementBorder} from which this component inherits.
 * Extend this component and provide child markup to use a
 * different loading indicator.
 */
public class LongLoadingContainer extends ReplacementBorder {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public LongLoadingContainer(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public LongLoadingContainer(String id, IModel<?> model) {
		super(id, model);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.misc.ReplacementBorder#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new MyAjaxBehavior());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.misc.ReplacementBorder#useReplacement()
	 */
	@Override
	protected boolean useReplacement() {
		return !AjaxRequestUtil.isAjax();
	}
	
	/**
	 * Causes the loading AJAX callback.
	 */
	final class MyAjaxBehavior extends AbstractDefaultAjaxBehavior {
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)
		 */
		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			super.renderHead(component, response);
			if (component.isEnabledInHierarchy() && !AjaxRequestUtil.isAjax()) {
				response.render(OnDomReadyHeaderItem.forScript(getCallbackScript(component)));
			}
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		@Override
		protected void respond(AjaxRequestTarget target) {
			target.add(LongLoadingContainer.this);
		}
		
	}
	
}
