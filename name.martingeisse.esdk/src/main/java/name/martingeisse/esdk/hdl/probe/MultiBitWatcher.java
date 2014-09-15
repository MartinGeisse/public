/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.probe;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.esdk.hdl.core.IClockSignalConsumer;
import name.martingeisse.esdk.hdl.core.IValueSource;

/**
 * This watcher keeps a list of bit value sources and prints the
 * values in the same order as the sources in the list.
 */
public final class MultiBitWatcher implements IClockSignalConsumer {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the source
	 */
	private List<IValueSource<Boolean>> sources;
	
	/**
	 * the values
	 */
	private boolean[] values;

	/**
	 * Constructor
	 * @param name the name used for output
	 */
	public MultiBitWatcher(final String name) {
		this.name = name;
		this.sources = new ArrayList<IValueSource<Boolean>>();
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter method for the sources.
	 * @return the sources
	 */
	public List<IValueSource<Boolean>> getSources() {
		return sources;
	}

	/**
	 * Adds a value source to the current list of sources.
	 * @param source the source to add.
	 */
	public void addSource(IValueSource<Boolean> source) {
		sources.add(source);
	}
	
	/**
	 * Setter method for the sources.
	 * @param sources the sources to set
	 */
	public void setSources(final List<IValueSource<Boolean>> sources) {
		this.sources = sources;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#computeNextState()
	 */
	@Override
	public void computeNextState() {
		this.values = new boolean[sources.size()];
		int i = 0;
		for (IValueSource<Boolean> source : sources) {
			values[i] = source.getValue();
			i++;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#enterNextState()
	 */
	@Override
	public void enterNextState() {
		System.out.print(name + ": ");
		for (boolean value : values) {
			System.out.print(value ? '1' : '0');
		}
		System.out.println();
	}

}
