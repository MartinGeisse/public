/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.instruction.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.esdk.picoblaze.simulator.PicoblazeSimulatorException;
import name.martingeisse.esdk.picoblaze.simulator.instruction.InstructionLevelPicoblazeSimulator;
import name.martingeisse.esdk.picoblaze.simulator.port.AggregatePicoblazePortHandler;
import name.martingeisse.swtlib.application.AbstractSingleWindowApplication;
import name.martingeisse.swtlib.layout.RubberLayout;
import name.martingeisse.swtlib.util.TabItemBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

/**
 * Wraps an instruction-level PicoBlaze simulator and its user interface.
 * This class is intended to be used in the main method of a PicoBlaze
 * model application.
 * 
 * This class uses its own {@link AggregatePicoblazePortHandler} as the
 * default port handler of the PicoBlaze model. The port handler should
 * not be replaced.
 */
public class InstructionLevelPicoblazeSimulatorApplication extends AbstractSingleWindowApplication {
	
	/**
	 * the contributors
	 */
	private List<IInstructionLevelPicoblazeSimulatorApplicationContributor> contributors;

	/**
	 * the simulator
	 */
	private InstructionLevelPicoblazeSimulator simulator;
	
	/**
	 * the portHandler
	 */
	private AggregatePicoblazePortHandler portHandler;
	
	/**
	 * the tabFolder
	 */
	private TabFolder tabFolder;
	
	/**
	 * Constructor.
	 */
	public InstructionLevelPicoblazeSimulatorApplication() {
		contributors = new ArrayList<IInstructionLevelPicoblazeSimulatorApplicationContributor>();
	}
	
	/**
	 * Getter method for the simulator.
	 * @return the simulator
	 */
	public InstructionLevelPicoblazeSimulator getSimulator() {
		return simulator;
	}
	
	/**
	 * Getter method for the portHandler.
	 * @return the portHandler
	 */
	public AggregatePicoblazePortHandler getPortHandler() {
		return portHandler;
	}
	
	/**
	 * Loads the specified PSMBIN file and creates and uses an instruction memory
	 * from the instructions contained in the file.
	 * @param file the file to load
	 * @throws IOException on I/O errors
	 */
	public void setInstructionMemoryFromPsmBinFile(File file) throws IOException {
		simulator.setInstructionMemoryFromPsmBinFile(file);
	}
	
	/**
	 * Adds the specified contributor to this application.
	 * @param contributor the contributor to add
	 */
	public void addContributor(IInstructionLevelPicoblazeSimulatorApplicationContributor contributor) {
		contributors.add(contributor);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.swtlib.application.AbstractSingleWindowApplication#create()
	 */
	@Override
	public void create() {
		
		// low-level initialization, SWT stuff
		super.create();
		handleAllEvents();
		Shell shell = getShell();
		
		// initialize all contributors
		for (IInstructionLevelPicoblazeSimulatorApplicationContributor contributor : contributors) {
			contributor.initialize(this);
		}
		
		// create the simulation model
		portHandler = new AggregatePicoblazePortHandler();
		for (IInstructionLevelPicoblazeSimulatorApplicationContributor contributor : contributors) {
			contributor.registerPeripheralDevices(portHandler);
		}
		simulator = new InstructionLevelPicoblazeSimulator();
		simulator.setPortHandler(portHandler);

		// create GUI components
		shell.setLayout(new RubberLayout(800, 600));
		tabFolder = new TabFolder(shell, SWT.TOP);
		for (IInstructionLevelPicoblazeSimulatorApplicationContributor contributor : contributors) {
			contributor.createTabItems(new TabItemBuilder(tabFolder));
		}

		// initialize the GUI
		tabFolder.pack();
		shell.pack();

	}
	
	/**
	 * Performs a single step, either executing one instruction or performing interrupt entry.
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void step() throws PicoblazeSimulatorException {
		simulator.performInstructionCycle();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.swtlib.application.AbstractSingleDisplayApplication#idle()
	 */
	@Override
	protected void idle() {
		try {
			step();
		} catch (PicoblazeSimulatorException e) {
			throw new RuntimeException(e);
		}
		// super.idle();
	}
	
}
