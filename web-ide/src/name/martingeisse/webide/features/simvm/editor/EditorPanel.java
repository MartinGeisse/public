/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nullable;

import name.martingeisse.webide.document.Document;
import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.features.ecosim.EcosimPrimaryModelElement;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.features.simvm.simulation.SimulatedVirtualMachine;
import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.features.simvm.simulation.SimulationEvents;
import name.martingeisse.webide.features.simvm.simulation.SimulationState;
import name.martingeisse.webide.ipc.IpcEvent;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.commons.lang3.ObjectUtils;
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
	 * the terminalOutput
	 */
	private String terminalOutput;

	/**
	 * Constructor.
	 * @param id the Wicket id
	 * @param model the document model
	 */
	public EditorPanel(final String id, final IModel<Document> model) {
		super(id, model);
		setOutputMarkupId(true);

		// create components
		add(new Link<Void>("startButton") {

			@Override
			public void onClick() {
				getVirtualMachine().startSimulation();
				getVirtualMachine().resume();
			}

			@Override
			public boolean isVisible() {
				return (getVirtualMachine().getState() == SimulationState.STOPPED);
			}

		});
		add(new Link<Void>("terminateButton") {

			@Override
			public void onClick() {
				getVirtualMachine().terminate();
			}

			/* (non-Javadoc)
			 * @see org.apache.wicket.Component#isVisible()
			 */
			@Override
			public boolean isVisible() {
				return (getVirtualMachine().getState() != SimulationState.STOPPED);
			}

		});
		add(new Label("terminalOutput", new PropertyModel<String>(this, "terminalOutput")).setOutputMarkupId(true));
		
		// initialize runtime state
		SimulatedVirtualMachine virtualMachine = getVirtualMachine();
		SimulationModel runningSimulationModel = virtualMachine.getSimulationModel();
		EcosimPrimaryModelElement primaryElement = (EcosimPrimaryModelElement)runningSimulationModel.getPrimaryElement();
		this.terminalOutput = primaryElement.getTerminalUserInterface().getOutput();
		
	}

	/**
	 * @return the {@link IModel} for the {@link Document}
	 */
	@SuppressWarnings("unchecked")
	public final IModel<Document> getDocumentModel() {
		return (IModel<Document>)getDefaultModel();
	}
	
	/**
	 * @return the {@link Document}
	 */
	public final Document getDocument() {
		return getDocumentModel().getObject();
	}
	
	/**
	 * Getter method for the anchorResource.
	 * @return the anchorResource
	 */
	public final ResourceHandle getAnchorResource() {
		return getDocument().getResourceHandle();
	}
	
	/**
	 * @return the {@link SimulatedVirtualMachine}
	 */
	public final SimulatedVirtualMachine getVirtualMachine() {
		return (SimulatedVirtualMachine)getDocument().getBody();
	}
	
	/**
	 * @return the {@link SimulationModel}
	 */
	public final SimulationModel getSimulationModel() {
		return getVirtualMachine().getSimulationModel();
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
			// System.out.println("* " + getRunningSimulation());
//			TODO das reicht noch nicht. Dieses Panel wird neu gerendert *bevor* die Simulation endgültig beendet wurde (-> race condition!)
			target.add(this);
		} else if (event.getType().equals(EcosimEvents.TERMINAL_OUTPUT)) {
			terminalOutput = (String)event.getData();
			target.add(get("terminalOutput"));
		}
		System.out.println("+++ event: " + type);
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
			editorPageSimulationAnchors.put(uuid, getAnchorResource());
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
			String eventTargetUuid = input.getResource().uuid();
			// String originalUuid = (String)input.getResource().getRequest().getAttribute(ApplicationConfig.SUSPENDED_ATMOSPHERE_RESOURCE_UUID);
			ResourceHandle eventTargetResourceHandle = editorPageSimulationAnchors.get(eventTargetUuid);
			SimulationEventMessage message = (SimulationEventMessage)input.getPayload();
			ResourceHandle affectedResourceHandle = message.getVirtualMachine().getDocument().getResourceHandle();
			return ObjectUtils.equals(eventTargetResourceHandle, affectedResourceHandle);
		}
		
	}
	
}
