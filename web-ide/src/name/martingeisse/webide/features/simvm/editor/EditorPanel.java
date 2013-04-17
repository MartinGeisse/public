/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nullable;

import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.features.ecosim.EcosimPrimaryModelElement;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.features.simvm.simulation.Simulation;
import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.features.simvm.simulation.SimulationEvents;
import name.martingeisse.webide.ipc.IpcEvent;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.atmosphere.AtmosphereBehavior;
import org.apache.wicket.atmosphere.AtmosphereEvent;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
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
	 * This map maps Atmosphere resource UUIDs (each corresponding to a page with an
	 * EditorPanel) to {@link ResourceHandle} objects which correspond to running
	 * simulations. This allows to filter Atmosphere events without building a
	 * request cycle first.
	 * 
	 * The map may contain mappings for pages which don't show a simulation anymore
	 * (or which don't event exist anymore) without bad effects (other than consuming
	 * a small amount of memory); the only important invariant is that the mapping for
	 * existing pages which still show a running simulation must be correct and
	 * up-to-date.
	 */
	static final ConcurrentMap<String, ResourceHandle> editorPageSimulationAnchors = new ConcurrentHashMap<String, ResourceHandle>();
	
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
		setOutputMarkupId(true);
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
				return (getRunningSimulation() == null);
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
				return (getRunningSimulation() != null);
			}

		});

		add(new Label("terminalOutput", new PropertyModel<String>(this, "terminalOutput")).setOutputMarkupId(true));
		Simulation simulation = getRunningSimulation();
		if (simulation != null) {
			SimulationModel runningSimulationModel = simulation.getSimulationModel();
			EcosimPrimaryModelElement primaryElement = (EcosimPrimaryModelElement)runningSimulationModel.getPrimaryElement();
			this.terminalOutput = primaryElement.getTerminalUserInterface().getOutput();
		}
	}

	/**
	 * Getter method for the anchorResource.
	 * @return the anchorResource
	 */
	public ResourceHandle getAnchorResource() {
		return anchorResource;
	}
	
	/**
	 * @return the simulation model
	 */
	public SimulationModel getSimulationModel() {
		return (SimulationModel)getDefaultModelObject();
	}

	/**
	 * @return the running simulation, or null if not running
	 */
	public Simulation getRunningSimulation() {
		return Simulation.getExisting(EditorPanel.this.anchorResource);
	}
	
	/**
	 * Subscribes to {@link IpcEvent}s.
	 * @param target the request handler
	 * @param message the event message
	 */
	@Subscribe(filter = MyFilter.class)
	public void onSimulatorGeneratedEvent(final AjaxRequestTarget target, final SimulationEventMessage message) {
		IpcEvent event = message.getEvent();
		String type = event.getType();
		if (type.equals(SimulationEvents.EVENT_TYPE_START) || type.equals(SimulationEvents.EVENT_TYPE_SUSPEND) || type.equals(SimulationEvents.EVENT_TYPE_TERMINATE)) {
			TODO das reicht noch nicht. Dieses Panel wird neu gerendert *bevor* die Simulation endgültig beendet wurde (-> race condition!)
			target.add(this);
		} else if (event.getType().equals(EcosimEvents.TERMINAL_OUTPUT)) {
			terminalOutput = (String)event.getData();
			target.add(get("terminalOutput"));
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onAfterRender()
	 */
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		
		// store the UUID to simulation mapping in case this panel gets rendered into a page
		// with a pre-existing connection
		String uuid = AtmosphereBehavior.getUUID(getPage());
		if (uuid != null) {
			editorPageSimulationAnchors.put(uuid, anchorResource);
		}
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
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript("initializeSimvmEditorPanel()"));
	}
	
	/**
	 * Filters atmosphere events with {@link SimulationEventMessage}s to build a Wicket
	 * context only for pages that show the *same* simulation.
	 */
	public static class MyFilter implements Predicate<AtmosphereEvent> {

		/* (non-Javadoc)
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(@Nullable AtmosphereEvent input) {
			
			String currentUuid = input.getResource().uuid();
			// String originalUuid = (String)input.getResource().getRequest().getAttribute(ApplicationConfig.SUSPENDED_ATMOSPHERE_RESOURCE_UUID);
			ResourceHandle anchorResourceForUuid = editorPageSimulationAnchors.get(currentUuid);
			SimulationEventMessage message = (SimulationEventMessage)input.getPayload();
			ResourceHandle anchorResourceForSimulation = message.getSimulation().getResourceHandle();
			
			// return true;
			if (anchorResourceForUuid == null || anchorResourceForSimulation == null) {
				return false;
			} else {
				return anchorResourceForUuid.equals(anchorResourceForSimulation);
			}
			
		}
		
	}
	
}
