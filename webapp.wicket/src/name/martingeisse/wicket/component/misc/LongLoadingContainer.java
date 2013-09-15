/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * A container that can be used to display data that takes a
 * long time to load. It will show a loading indicator
 * initially, while sending an AJAX request immediately that
 * will eventually return with the loaded data.
 * 
 * The data is represented by a {@link ListenableFuture}.
 * 
 * The loading indicator is the replacement in terms of the
 * {@link ReplacementBorder} from which this component inherits.
 * Extend this component and provide child markup to use a
 * different loading indicator.
 * 
 * @param <T> the data type
 */
public class LongLoadingContainer<T> extends ReplacementBorder {

	/**
	 * Cached not-finished state of the future. Used to make all parts
	 * of this component behave consistently during a request, even
	 * if the future gets done while the request is in progress.
	 */
	private transient boolean useReplacement;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public LongLoadingContainer(String id, IModel<ListenableFuture<T>> model) {
		super(id, model);
	}

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	@SuppressWarnings("unchecked")
	public IModel<ListenableFuture<T>> getModel() {
		return (IModel<ListenableFuture<T>>)getDefaultModel();
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
	 * @see name.martingeisse.wicket.component.misc.ReplacementBorder#onConfigure()
	 */
	@Override
	protected void onConfigure() {
		ListenableFuture<T> future = getModel().getObject();
		useReplacement = !future.isDone();
		super.onConfigure();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.misc.ReplacementBorder#useReplacement()
	 */
	@Override
	protected boolean useReplacement() {
		return useReplacement;
	}
	
	/**
	 * 
	 */
	private void waitForFuture() {
		try {
			ListenableFuture<T> future = getModel().getObject();
			future.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
			if (component.isEnabledInHierarchy() && useReplacement) {
				response.render(OnDomReadyHeaderItem.forScript(getCallbackScript(component)));
			}
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		@Override
		protected void respond(AjaxRequestTarget target) {
			System.out.println("1 ***");
			waitForFuture();
			System.out.println("2 ***");
			target.add(LongLoadingContainer.this);
			System.out.println("3 ***");
		}
		
	}
	
}
