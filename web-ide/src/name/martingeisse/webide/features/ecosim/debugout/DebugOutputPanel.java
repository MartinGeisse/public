/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.debugout;

import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.features.simvm.editor.SameSimulationFilter;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;
import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.features.simvm.simulation.SimulationEvents;
import name.martingeisse.webide.ipc.IpcEvent;
import name.martingeisse.webide.util.PushAppendLabel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * This panel shows the debug output in the web UI.
 */
public class DebugOutputPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public DebugOutputPanel(final String id, IModel<ISimulationModelElement> model) {
		super(id, model);
		setOutputMarkupId(true);
		add(new PushAppendLabel("debugOutput", new MyOutputModel(), "%s.parent()"));
	}
	
	/**
	 * @return the model element for this panel
	 */
	public DebugOutputModelElement getDebugOutputModelElement() {
		return (DebugOutputModelElement)getDefaultModelObject();
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
		if (eventType.equals(EcosimEvents.DEBUG_OUTPUT)) {
			((PushAppendLabel)get("debugOutput")).onModelAppend(target);
		} else if (eventType.equals(SimulationEvents.EVENT_TYPE_START)) {
			target.add(this);
		}
	}
	
	/**
	 * This model caches the debug output while a request is in progress.
	 */
	class MyOutputModel extends LoadableDetachableModel<String> {
		@Override
		protected String load() {
			return getDebugOutputModelElement().getController().getOutput();
		}
	}
	
}
