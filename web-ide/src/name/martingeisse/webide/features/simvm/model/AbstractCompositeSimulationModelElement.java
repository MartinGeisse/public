/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import name.martingeisse.webide.ipc.IpcEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Base class for a model element that consists of other
 * elements, possibly adding its own behavior. This class
 * passes most method calls to all its sub-elements.
 * 
 * Persistent state is one of the exceptions. The state of
 * this object is always a JSON object, with the property
 * "subElements" reserved for an array that contains the
 * states of the sub-elements. Other properties can be
 * used by subclasses at will.
 * 
 * @param <S> the sub-element type
 */
public abstract class AbstractCompositeSimulationModelElement<S extends ISimulationModelElement> implements ISimulationModelElement {

	/**
	 * the subElements
	 */
	private final List<S> subElements;
	
	/**
	 * Constructor.
	 */
	public AbstractCompositeSimulationModelElement() {
		this.subElements = new ArrayList<S>();
	}
	
	/**
	 * Getter method for the subElements.
	 * @return the subElements
	 */
	public final List<S> getSubElements() {
		return subElements;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#initialize(name.martingeisse.webide.features.simvm.model.SimulationModel)
	 */
	@Override
	public void initialize(SimulationModel simulationModel) {
		for (S subElement : subElements) {
			subElement.initialize(simulationModel);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#saveState()
	 */
	@Override
	public final Object saveState() {
		@SuppressWarnings("unchecked")
		Map<String, Object> stateObject = new JSONObject();
		saveState(stateObject);
		@SuppressWarnings("unchecked")
		List<Object> subElementStates = new JSONArray();
		for (S subElement : subElements) {
			subElementStates.add(subElement.saveState());
		}
		stateObject.put("subElements", subElementStates);
		return stateObject;
	}
	
	/**
	 * Saves subclass-specific state. The default implementation does nothing.
	 * @param stateObject the object to save to
	 */
	protected void saveState(Map<String, Object> stateObject) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#loadState(java.lang.Object)
	 */
	@Override
	public final void loadState(Object state) {
		
		// check main state object
		if (!(state instanceof JSONObject)) {
			throw new IllegalArgumentException("state argument must be a JSONObject");
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> stateObject = (JSONObject)state;
		
		// check sub-element state array
		Object subState = stateObject.get("subElements");
		if (!(subState instanceof JSONArray)) {
			throw new IllegalArgumentException("subElements property of the state object must be a JSONArray");
		}
		@SuppressWarnings("unchecked")
		List<Object> subElementStates = (JSONArray)subState;
		if (subElementStates.size() != subElements.size()) {
			throw new IllegalArgumentException("subElements property of the state object has wrong size (" + subElementStates.size() +
				" should be " + subElements.size() + ")");
		}
		
		// load state
		loadState(stateObject);
		Iterator<S> modelElementIterator = subElements.iterator();
		Iterator<Object> subStateIterator = subElementStates.iterator();
		while (modelElementIterator.hasNext()) {
			modelElementIterator.next().loadState(subStateIterator.next());
		}
		
	}

	/**
	 * Loads subclass-specific state. The default implementation does nothing.
	 * @param stateObject the object to load from
	 */
	protected void loadState(Map<String, Object> stateObject) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#deleteState()
	 */
	@Override
	public void deleteState() {
		for (S subElement : subElements) {
			subElement.deleteState();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#handleEvent(name.martingeisse.webide.ipc.IpcEvent)
	 */
	@Override
	public void handleEvent(IpcEvent<?> event) {
		for (S subElement : subElements) {
			subElement.handleEvent(event);
		}
	}

}
