/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.instruction.app;

import name.martingeisse.esdk.picoblaze.simulator.port.AggregatePicoblazePortHandler;
import name.martingeisse.swtlib.layout.CenterLayout;
import name.martingeisse.swtlib.panel.prototyping.PrototypingPanel;
import name.martingeisse.swtlib.util.TabItemBuilder;

import org.eclipse.swt.widgets.Composite;

/**
 * This contributor adds a single {@link PrototypingPanel} for
 * quick prototyping.
 * 
 * Subclasses may implement fitPrototypingPanel() to add actual components.
 * It is also possible to add components outside of subclasses, but only after
 * the application UI has been create()'d.
 */
public class PrototypingPanelContributor implements IInstructionLevelPicoblazeSimulatorApplicationContributor {

	/**
	 * the tabName
	 */
	private String tabName;
	
	/**
	 * the application
	 */
	private InstructionLevelPicoblazeSimulatorApplication application;
	
	/**
	 * the prototypingPanel
	 */
	private PrototypingPanel prototypingPanel;
	
	/**
	 * Constructor.
	 * @param tabName the name of the contributed tab
	 */
	public PrototypingPanelContributor(String tabName) {
		this.tabName = tabName;
	}
	
	/**
	 * Getter method for the application.
	 * @return the application
	 */
	public final InstructionLevelPicoblazeSimulatorApplication getApplication() {
		return application;
	}
	
	/**
	 * Getter method for the prototypingPanel.
	 * @return the prototypingPanel
	 */
	public final PrototypingPanel getPrototypingPanel() {
		return prototypingPanel;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor#initialize(name.martingeisse.esdk.picoblaze.simulator.instruction.app.InstructionLevelPicoblazeSimulatorApplication)
	 */
	@Override
	public final void initialize(InstructionLevelPicoblazeSimulatorApplication application) {
		this.application = application;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor#createTabItems(name.martingeisse.swtlib.util.TabItemBuilder)
	 */
	@Override
	public final void createTabItems(TabItemBuilder tabItemBuilder) {
		Composite centerPanel = new Composite(tabItemBuilder.getParent(), 0);
		centerPanel.setLayout(new CenterLayout(false, false));
		tabItemBuilder.createTabItem(tabName, centerPanel);
		this.prototypingPanel = new PrototypingPanel(centerPanel, 10, 10);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor#registerPeripheralDevices(name.martingeisse.esdk.picoblaze.simulator.port.AggregatePicoblazePortHandler)
	 */
	@Override
	public final void registerPeripheralDevices(AggregatePicoblazePortHandler portHandler) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.app.IInstructionLevelPicoblazeSimulatorApplicationContributor#setEnableFastSimulationMode(boolean)
	 */
	@Override
	public final void setEnableFastSimulationMode(boolean enable) {
	}
	
	/**
	 * This method must be implemented to create port sub-handlers and associated components on
	 * the prototyping panel.
	 * @param portHandler the PicoBlaze's port handler that sends INPUT and OUTPUT operations
	 * @param prototypingPanel the prototyping panel that contains UI components
	 */
	protected void fitPrototypingPanel(AggregatePicoblazePortHandler portHandler, PrototypingPanel prototypingPanel) {
	}

}
