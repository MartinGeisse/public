/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.ui;

import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.features.ecosim.model.TerminalModelElement;
import name.martingeisse.webide.features.simvm.editor.SameSimulationFilter;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;
import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This panel presents terminal I/O.
 */
public class TerminalPanel extends Panel {

	/**
	 * the terminalOutput
	 */
	private String terminalOutput;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public TerminalPanel(final String id, IModel<ISimulationModelElement> model) {
		super(id, model);
		add(new Label("terminalOutput", new PropertyModel<String>(this, "terminalOutput")).setOutputMarkupId(true));

		// initialize runtime state
		TerminalModelElement terminalModelElement = (TerminalModelElement)model.getObject();
		this.terminalOutput = terminalModelElement.getUiModel().getOutput();
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
		response.render(OnDomReadyHeaderItem.forScript("initializeTerminalPanel()"));
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
		if (event.getType().equals(EcosimEvents.TERMINAL_OUTPUT)) {
			terminalOutput = (String)event.getData();
			target.add(get("terminalOutput"));
		}
	}
	
}
