/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import name.martingeisse.ecosim.bus.Bus;
import name.martingeisse.ecosim.cpu.Cpu;
import name.martingeisse.ecosim.devices.chardisplay.CharacterDisplayController;
import name.martingeisse.ecosim.devices.disk.Disk;
import name.martingeisse.ecosim.devices.keyboard.KeyboardController;
import name.martingeisse.ecosim.devices.memory.Ram;
import name.martingeisse.ecosim.devices.memory.Rom;
import name.martingeisse.ecosim.devices.timer.Timer;
import name.martingeisse.webide.features.ecosim.console.ConsoleModelElement;
import name.martingeisse.webide.features.ecosim.debugout.DebugOutputModelElement;
import name.martingeisse.webide.features.ecosim.terminal.TerminalModelElement;
import name.martingeisse.webide.features.simvm.model.AbstractCompositeSimulationModelElement;
import name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.ipc.IIpcEventOutbox;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * The primary model element for the Eco32 simulator.
 */
public class EcosimPrimaryModelElement extends AbstractCompositeSimulationModelElement<IEcosimModelElement> implements IPrimarySimulationModelElement {

	/**
	 * the INSTRUCTIONS_PER_DEVICE_TICK
	 */
	public static final int INSTRUCTIONS_PER_DEVICE_TICK = 500;
	
	/**
	 * the STEPS_PER_BATCH
	 */
	public static final int STEPS_PER_BATCH = 100;
	
	/**
	 * the simulationModel
	 */
	private SimulationModel simulationModel;
	
	/**
	 * the bus
	 */
	private Bus bus;

	/**
	 * the cpu
	 */
	private Cpu cpu;
	
	/**
	 * the remainingInstructionsForThisTick
	 */
	private int remainingInstructionsForThisTick;
	
	/**
	 * the ram
	 */
	private Ram ram;
	
	/**
	 * the rom
	 */
	private Rom rom;
	
	/**
	 * the disk
	 */
	private Disk disk;
	
	/**
	 * the characterDisplay
	 */
	private CharacterDisplayController characterDisplay;
	
	/**
	 * the keyboard
	 */
	private KeyboardController keyboard;
	
	/**
	 * the timer
	 */
	private Timer timer;
	
	/**
	 * Constructor.
	 */
	public EcosimPrimaryModelElement() {
	}
	
	/**
	 * Getter method for the simulationModel.
	 * @return the simulationModel
	 */
	public SimulationModel getSimulationModel() {
		return simulationModel;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.AbstractCompositeSimulationModelElement#initialize(name.martingeisse.webide.features.simvm.model.SimulationModel, name.martingeisse.webide.ipc.IIpcEventOutbox)
	 */
	@Override
	public void initialize(SimulationModel simulationModel, IIpcEventOutbox eventOutbox) {
		this.simulationModel = simulationModel;
		getSubElements().add(new EcosimModelElementPlaceholder("CPU", "... cpu panel ..."));
		getSubElements().add(new EcosimModelElementPlaceholder("Memory", "... memory panel ..."));
		getSubElements().add(new TerminalModelElement(eventOutbox));
		getSubElements().add(new ConsoleModelElement(eventOutbox));
		getSubElements().add(new EcosimModelElementPlaceholder("Disk", "... disk panel ..."));
		getSubElements().add(new DebugOutputModelElement(eventOutbox));
		super.initialize(simulationModel, eventOutbox);
		
		bus = new Bus();
		cpu = new Cpu();
		remainingInstructionsForThisTick = INSTRUCTIONS_PER_DEVICE_TICK;
		cpu.setBus(bus);
		for (IEcosimModelElement subElement : getSubElements()) {
			EcosimContributedDevice[] contributedDevices = subElement.getContributedDevices();
			if (contributedDevices != null) {
				for (EcosimContributedDevice contributedDevice : contributedDevices) {
					bus.add(contributedDevice.getBaseAddress(), contributedDevice.getDevice(), contributedDevice.getInterruptIndices());
				}
			}
		}
		
		
		// TODO ----
		try {
			
			ram = new Ram(25);
			bus.add(0x00000000, ram, new int[] {});
	
			rom = new Rom(21);
			rom.readContentsFromFile(new File("resource/simulator/monitor.bin"));
			bus.add(0x20000000, rom, new int[] {});
	
			disk = new Disk(new File("resource/simulator/disk.img"));
			bus.add(0x30400000, disk, new int[] {
				8
			});
	
			/*
			characterDisplay = new CharacterDisplayController();
			bus.add(0x30100000, characterDisplay, new int[] {});
			characterDisplay.setUserInterface(new ICharacterDisplay() {
				@Override
				public void update(ICharacterDisplayHost host, int x, int y) {
				}
			});
	
			keyboard = new KeyboardController();
			bus.add(0x30200000, keyboard, new int[] {
				4
			});
			keyboard.setUserInterface(new IKeyboard() {
				
				@Override
				public byte receiveByte() throws IllegalStateException {
					return 0;
				}
				
				@Override
				public boolean hasInput() {
					return false;
				}
				
			});
			*/
	
			// TODO graphics
			// TODO block console
			
			timer = new Timer();
			bus.add(0x30000000, timer, new int[] {
				14
			});
		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// TODO ----
		
		
		bus.buildBusMap();
		// TODO: what's the use of this? cpu.setExtensionHandler(new CpuExtensionHandler(cpu, bus, disk));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.AbstractCompositeSimulationModelElement#loadRuntimeState(java.util.Map)
	 */
	@Override
	protected void loadRuntimeState(Map<String, Object> stateObject) {
		ram.clearContents();
		cpu.reset();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#singleStep()
	 */
	@Override
	public void singleStep() {
		cpu.step();
		remainingInstructionsForThisTick--;
		if (remainingInstructionsForThisTick == 0) {
			remainingInstructionsForThisTick = INSTRUCTIONS_PER_DEVICE_TICK;
			bus.tick();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#batchStep()
	 */
	@Override
	public void batchStep() {
		for (int i=0; i<STEPS_PER_BATCH; i++) {
			singleStep();
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#createComponent(java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public Component createComponent(String id, IModel<ISimulationModelElement> thisModel) {
		@SuppressWarnings("unchecked")
		IModel<EcosimPrimaryModelElement> model = (IModel<EcosimPrimaryModelElement>)(IModel<?>)thisModel;
		return new EcosimPanel(id, model);
	}
	
}
