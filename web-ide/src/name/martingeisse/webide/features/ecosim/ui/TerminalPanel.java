/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.ui;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.features.ecosim.model.TerminalModelElement;
import name.martingeisse.webide.features.simvm.editor.SameSimulationFilter;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;
import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.features.simvm.simulation.SimulationEvents;
import name.martingeisse.webide.ipc.IpcEvent;
import name.martingeisse.wicket.atmosphere.AtmosphereUtil;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * This panel shows a terminal in the web UI.
 */
public class TerminalPanel extends Panel {

	/**
	 * the terminalOutput
	 */
	private String terminalOutput;
	
	/**
	 * the lastDeltaUpdatePosition
	 */
	private long lastDeltaUpdatePosition;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public TerminalPanel(final String id, IModel<ISimulationModelElement> model) {
		super(id, model);
		add(new Label("terminalOutput", new PropertyModel<String>(this, "terminalOutput")).setOutputMarkupId(true));
		add(new MyInputBehavior());
		this.terminalOutput = getTerminalModelElement().getTerminal().getOutput();
		this.lastDeltaUpdatePosition = 0;
	}
	
	/**
	 * @return the model element for this panel
	 */
	public TerminalModelElement getTerminalModelElement() {
		return (TerminalModelElement)getDefaultModelObject();
	}

	/**
	 * Getter method for the terminalOutput.
	 * @return the terminalOutput
	 */
	public String getTerminalOutput() {
		return terminalOutput;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		MyInputBehavior inputBehavior = getBehaviors(MyInputBehavior.class).get(0);
		CharSequence callbackFunction = inputBehavior.getCallbackFunction(CallbackParameter.explicit("inputText"));
		response.render(OnDomReadyHeaderItem.forScript("initializeTerminalPanel(" + callbackFunction + ")"));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("class", "auto-focus");
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onAfterRender()
	 */
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		if (AtmosphereUtil.isRequestForMostRecentResource(this)) {
			lastDeltaUpdatePosition = terminalOutput.length();
		}
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
			if (AtmosphereUtil.isRequestForMostRecentResource(this)) {
				terminalOutput = getTerminalModelElement().getTerminal().getOutput();
				if (lastDeltaUpdatePosition < terminalOutput.length()) {
					String delta = terminalOutput.substring((int)lastDeltaUpdatePosition);
					String escapedDelta = JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(delta);
					String markupId = get("terminalOutput").getMarkupId();
					target.appendJavaScript("terminalAppend('" + markupId + "', '" + escapedDelta + "');");
				}
				lastDeltaUpdatePosition = terminalOutput.length();
			}
		} else if (eventType.equals(SimulationEvents.EVENT_TYPE_START)) {
			terminalOutput = "";
			lastDeltaUpdatePosition = 0;
			// we need to add (this), not just the terminalOutput label, to trigger onAfterRender().
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
	
}
