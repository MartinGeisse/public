/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package test;

import java.io.File;
import java.io.IOException;

import name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor;
import name.martingeisse.esdk.picoblaze.simulator.instruction.app.InstructionLevelPicoblazeSimulatorApplication;
import name.martingeisse.esdk.picoblaze.simulator.port.AggregatePicoblazePortHandler;
import name.martingeisse.picotetris.model.canvas.TetrisBlockDisplay;
import name.martingeisse.swtlib.layout.CenterLayout;
import name.martingeisse.swtlib.util.TabItemBuilder;

import org.eclipse.swt.widgets.Composite;

/**
 *
 */
public class PicoblazeAppMain {

	/**
	 * @param args ...
	 * @throws Exception ...
	 */
	public static void main(String[] args) throws Exception {

		// create the app with the contributor added -- this must be done before manually adding components
		// to the prototyping panel
		InstructionLevelPicoblazeSimulatorApplication app = new InstructionLevelPicoblazeSimulatorApplication();
//		PrototypingPanelContributor contributor = new PrototypingPanelContributor("Foo");
//		app.addContributor(contributor);
		
		app.addContributor(new MyContributor());
		app.create();
		
//		// add components to the prototyping panel
//		PrototypingPanel prototypingPanel = contributor.getPrototypingPanel();
//		
//		GenericBusSlaveLight light1 = new GenericBusSlaveLight(prototypingPanel);
//		app.getPortHandler().addEntry(0, light1);
//
//		GenericBusSlaveLight light2 = new GenericBusSlaveLight(prototypingPanel);
//		light2.setX(1);
//		app.getPortHandler().addEntry(1, light2);
		
		// load the PicoBlaze code
		app.setInstructionMemoryFromPsmBinFile(new File("pico-test/test3.psmbin"));
		
		// run the simulation app
		app.open();
		app.mainLoop();
		app.dispose();
		app.exit();
		
	}

	private static class MyContributor implements IInstructionLevelPicoblazeSimulatorApplicationContributor {

		/**
		 * the display
		 */
		private TetrisBlockDisplay display = new TetrisBlockDisplay();
		
		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor#initialize(name.martingeisse.esdk.picoblaze.simulator.instruction.app.InstructionLevelPicoblazeSimulatorApplication)
		 */
		@Override
		public void initialize(InstructionLevelPicoblazeSimulatorApplication application) {
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor#createTabItems(name.martingeisse.swtlib.util.TabItemBuilder)
		 */
		@Override
		public void createTabItems(TabItemBuilder tabItemBuilder) {
			Composite container = new Composite(tabItemBuilder.getParent(), 0);
			container.setLayout(new CenterLayout(false, false));
			tabItemBuilder.createTabItem("Display", container);
			try {
				display.createCanvas(container);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor#registerPeripheralDevices(name.martingeisse.esdk.picoblaze.simulator.port.AggregatePicoblazePortHandler)
		 */
		@Override
		public void registerPeripheralDevices(AggregatePicoblazePortHandler portHandler) {
			portHandler.addEntry(0, 64, display.getRowContentsPortHandler());
			portHandler.addEntry(64, 1, display.getRowRegisterPortHandler());
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor#setEnableFastSimulationMode(boolean)
		 */
		@Override
		public void setEnableFastSimulationMode(boolean enable) {
		}
		
	}
}
