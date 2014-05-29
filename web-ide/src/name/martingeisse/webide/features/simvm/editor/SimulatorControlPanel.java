/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import name.martingeisse.webide.features.simvm.simulation.SimulatedVirtualMachine;
import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.features.simvm.simulation.SimulationEvents;
import name.martingeisse.webide.features.simvm.simulation.SimulationState;
import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This panel can be used as the "right" panel in the tab bar of
 * a {@link CompositeSimulationModelElementPanel} and provides
 * buttons to start and stop the simulation.
 */
public class SimulatorControlPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public SimulatorControlPanel(final String id, final IModel<SimulatedVirtualMachine> model) {
		super(id, model);
		setOutputMarkupId(true);

		// create components
		add(new AjaxLink<Void>("startButton") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				getVirtualMachine().startSimulation();
				getVirtualMachine().resume();
				if (getVirtualMachine().getState() != SimulationState.STOPPED) {
					target.add(SimulatorControlPanel.this);
				}
			}

			/* (non-Javadoc)
			 * @see org.apache.wicket.ajax.markup.html.AjaxLink#onComponentTag(org.apache.wicket.markup.ComponentTag)
			 */
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if (getVirtualMachine().getState() != SimulationState.STOPPED) {
					tag.append("style", "display: none", "; ");
				}
			}

		});
		add(new AjaxLink<Void>("terminateButton") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				getVirtualMachine().terminate();
				if (getVirtualMachine().getState() == SimulationState.STOPPED) {
					target.add(SimulatorControlPanel.this);
				}
			}
			
			/* (non-Javadoc)
			 * @see org.apache.wicket.ajax.markup.html.AjaxLink#onComponentTag(org.apache.wicket.markup.ComponentTag)
			 */
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if (getVirtualMachine().getState() == SimulationState.STOPPED) {
					tag.append("style", "display: none", "; ");
				}
			}

		});
	}

	/**
	 * @return the {@link IModel} for the {@link SimulatedVirtualMachine}
	 */
	@SuppressWarnings("unchecked")
	public final IModel<SimulatedVirtualMachine> getVirtualMachineModel() {
		return (IModel<SimulatedVirtualMachine>)getDefaultModel();
	}

	/**
	 * @return the {@link SimulatedVirtualMachine}
	 */
	public final SimulatedVirtualMachine getVirtualMachine() {
		return getVirtualMachineModel().getObject();
	}

	/**
	 * Subscribes to {@link IpcEvent}s.
	 * @param target the request handler
	 * @param message the event message
	 */
	@Subscribe(filter = SameSimulationFilter.class)
	public void onSimulatorGeneratedEvent(final AjaxRequestTarget target, final SimulationEventMessage message) {
		IpcEvent event = message.getEvent();
		String type = event.getType();
		if (type.equals(SimulationEvents.EVENT_TYPE_START) || type.equals(SimulationEvents.EVENT_TYPE_SUSPEND) || type.equals(SimulationEvents.EVENT_TYPE_TERMINATE)) {
			// System.out.println("* " + getRunningSimulation());
//			TODO das reicht noch nicht. Dieses Panel wird neu gerendert *bevor* die Simulation endgültig beendet wurde (-> race condition!)
			target.add(this);
		}
	}
	
}
