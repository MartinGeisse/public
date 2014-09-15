/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.picoblaze.simulator.instruction.app;

import name.martingeisse.esdk.picoblaze.simulator.port.AggregatePicoblazePortHandler;
import name.martingeisse.swtlib.util.TabItemBuilder;

/**
 * This class is the basis for plug-ins for the instruction-level
 * simulator application.
 */
public interface IInstructionLevelPicoblazeSimulatorApplicationContributor {

	/**
	 * Initializes this contributor. No features of the framework are
	 * accessible at this point, but this contributor may store the
	 * simulator application reference for later use.
	 * @param application the application that uses this contributor
	 */
	public void initialize(InstructionLevelPicoblazeSimulatorApplication application);
	
	/**
	 * Creates the tab items for the simulator GUI.
	 * @param tabItemBuilder the tab folder to add the tab items to.
	 */
	public void createTabItems(TabItemBuilder tabItemBuilder);
	
	/**
	 * Registers the peripheral devices contributed by this object with the specified
	 * aggregate port handler.
	 * @param portHandler the port handler where devices shall be attached.
	 */
	public void registerPeripheralDevices(AggregatePicoblazePortHandler portHandler);

	/**
	 * Enables or disables fast simulation mode. In this mode, UI components that
	 * display highly transient values (such as CPU registers) should not
	 * react to UI update notifications. The exact decision of which events are
	 * visualized in the GUI is left to the individual components.
	 * @param enable whether to enable or disable fast simulation mode
	 */
	public void setEnableFastSimulationMode(boolean enable);
	
}
