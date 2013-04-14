/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import javax.annotation.Nullable;

import name.martingeisse.webide.application.WebIdeApplication;
import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.features.simvm.simulation.Simulation;
import name.martingeisse.webide.ipc.EventListenerMetadata;
import name.martingeisse.webide.ipc.IIpcEventListener;
import name.martingeisse.webide.ipc.IpcEvent;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.atmosphere.AtmosphereBehavior;
import org.apache.wicket.atmosphere.AtmosphereEvent;
import org.apache.wicket.atmosphere.EventBus;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.google.common.base.Predicate;

/**
 * The actual Wicket component that encapsulates the SimVM UI.
 */
class EditorPanel extends Panel {

	/**
	 * the anchorResource
	 */
	private final ResourceHandle anchorResource;

	/**
	 * the terminalOutput
	 */
	private String terminalOutput;

	/**
	 * Constructor.
	 * @param id the Wicket id
	 * @param model the document model
	 * @param anchorResource the anchor resource of the simulation
	 */
	public EditorPanel(final String id, final IModel<SimulationModel> model, final ResourceHandle anchorResource) {
		super(id, model);
		this.anchorResource = anchorResource;

		add(new Link<Void>("startButton") {

			@Override
			public void onClick() {
				try {
					Simulation.getOrCreate(EditorPanel.this.anchorResource, true);
				} catch (final Simulation.StaleSimulationException e) {
				}
			}

			@Override
			public boolean isVisible() {
				return !isRunning();
			}

		});

		add(new Link<Void>("terminateButton") {

			@Override
			public void onClick() {
				final Simulation simulation = Simulation.getExisting(EditorPanel.this.anchorResource);
				if (simulation != null) {
					try {
						simulation.terminate();
					} catch (final Simulation.StaleSimulationException e) {
					}
				}
			}

			/* (non-Javadoc)
			 * @see org.apache.wicket.Component#isVisible()
			 */
			@Override
			public boolean isVisible() {
				return isRunning();
			}

		});

		add(new Label("terminalOutput", new PropertyModel<String>(this, "terminalOutput")).setOutputMarkupId(true));
	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private SimulationModel getSimulationModel() {
		return (SimulationModel)getDefaultModelObject();
	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean isRunning() {
		return (Simulation.getExisting(EditorPanel.this.anchorResource) != null);
	}

	/**
	 * Subscribes to {@link IpcEvent}s.
	 * @param target the request handler
	 * @param event the event
	 */
	// @Subscribe(/*filter = MyFilter.class*/)
	public void receiveMessage(final AjaxRequestTarget target, final IpcEvent event) {
//		if (event.getType().equals(EcosimEvents.TERMINAL_OUTPUT)) {
//			final Simulation simulation = Simulation.getExisting(EditorPanel.this.anchorResource);
//			terminalOutput = (String)event.getData();
//			target.add(get("terminalOutput"));
//		}
	}

	/**
	 * Getter method for the terminalOutput.
	 * @return the terminalOutput
	 */
	public String getTerminalOutput() {
		return terminalOutput;
	}

	/**
	 * TODO: document me
	 *
	 */
	public static class MyFilter implements Predicate<AtmosphereEvent> {

		/* (non-Javadoc)
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(@Nullable AtmosphereEvent input) {
			if (input == null) {
				return false;
			}
			System.out.println("* " + input.getPayload());
			return false;
		}
		
	}
	
}
