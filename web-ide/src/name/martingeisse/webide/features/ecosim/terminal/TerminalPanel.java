/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.terminal;

import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.features.simvm.editor.SameSimulationFilter;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;
import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.features.simvm.simulation.SimulationEvents;
import name.martingeisse.webide.ipc.IpcEvent;
import name.martingeisse.webide.util.PushAppendLabel;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * This panel shows a terminal in the web UI.
 */
public class TerminalPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public TerminalPanel(final String id, IModel<ISimulationModelElement> model) {
		super(id, model);
		setOutputMarkupId(true);
		add(new PushAppendLabel("terminalOutput", new MyOutputModel(), "%s.parent()"));
		add(new MyInputBehavior());
	}
	
	/**
	 * @return the model element for this panel
	 */
	public TerminalModelElement getTerminalModelElement() {
		return (TerminalModelElement)getDefaultModelObject();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		MyInputBehavior inputBehavior = getBehaviors(MyInputBehavior.class).get(0);
		CharSequence callbackFunction = inputBehavior.getCallbackFunction(CallbackParameter.explicit("inputText"));
		response.render(OnDomReadyHeaderItem.forScript("initializeTerminalPanel(" + callbackFunction + ", '" + getMarkupId() + "')"));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("class", "auto-focus");
	}
	
	/**
	 * Subscribes to {@link IpcEvent}s.
	 * @param target the request handler
	 * @param message the event message
	 */
	@Subscribe(filter = SameSimulationFilter.class)
	public void onSimulatorGeneratedEvent(final AjaxRequestTarget target, final SimulationEventMessage message) {
		final IpcEvent event = message.getEvent();
		final String eventType = event.getType();
		if (eventType.equals(EcosimEvents.TERMINAL_OUTPUT)) {
			((PushAppendLabel)get("terminalOutput")).onModelAppend(target);
		} else if (eventType.equals(SimulationEvents.EVENT_TYPE_START)) {
			target.add(this);
		}
	}
	
	/**
	 * This behavior generates a callback URL for terminal input.
	 */
	class MyInputBehavior extends AbstractDefaultAjaxBehavior {

		/* (non-Javadoc)
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		@Override
		protected void respond(AjaxRequestTarget target) {
			final IRequestParameters parameters = RequestCycle.get().getRequest().getRequestParameters();
			final String inputText = parameters.getParameterValue("inputText").toString();
			TerminalModelElement terminalModelElement = (TerminalModelElement)TerminalPanel.this.getDefaultModel().getObject();
			terminalModelElement.getTerminal().notifyUserInput(inputText);
		}
		
	}
	
	/**
	 * This model caches the terminal output while a request is in progress.
	 */
	class MyOutputModel extends LoadableDetachableModel<String> {
		@Override
		protected String load() {
			return getTerminalModelElement().getTerminal().getOutput();
		}
	}
	
}
