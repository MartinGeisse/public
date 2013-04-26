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
import name.martingeisse.ecosim.devices.chardisplay.CharacterDisplay;
import name.martingeisse.ecosim.devices.disk.Disk;
import name.martingeisse.ecosim.devices.keyboard.Keyboard;
import name.martingeisse.ecosim.devices.memory.Ram;
import name.martingeisse.ecosim.devices.memory.Rom;
import name.martingeisse.ecosim.devices.output.OutputDevice;
import name.martingeisse.ecosim.devices.terminal.Terminal;
import name.martingeisse.ecosim.devices.timer.Timer;
import name.martingeisse.webide.features.ecosim.ui.TerminalUserInterface;
import name.martingeisse.webide.features.simvm.model.AbstractCompositeSimulationModelElement;
import name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.ipc.IIpcEventOutbox;

import org.apache.wicket.Component;

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
	 * the terminal
	 */
	private Terminal terminal;
	
	/**
	 * the terminalUserInterface
	 */
	private TerminalUserInterface terminalUserInterface;
	
	/**
	 * the characterDisplay
	 */
	private CharacterDisplay characterDisplay;
	
	/**
	 * the keyboard
	 */
	private Keyboard keyboard;
	
	/**
	 * the output
	 */
	private OutputDevice output;
	
	/**
	 * the timer
	 */
	private Timer timer;
	
	/**
	 * Constructor.
	 */
	public EcosimPrimaryModelElement() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.AbstractCompositeSimulationModelElement#initialize(name.martingeisse.webide.features.simvm.model.SimulationModel, name.martingeisse.webide.ipc.IIpcEventOutbox)
	 */
	@Override
	public void initialize(SimulationModel simulationModel, IIpcEventOutbox eventOutbox) {
		super.initialize(simulationModel, eventOutbox);
		bus = new Bus();
		cpu = new Cpu();
		remainingInstructionsForThisTick = INSTRUCTIONS_PER_DEVICE_TICK;
		cpu.setBus(bus);
		for (IEcosimModelElement subElement : getSubElements()) {
			for (EcosimContributedDevice contributedDevice : subElement.getContributedDevices()) {
				bus.add(contributedDevice.getBaseAddress(), contributedDevice.getDevice(), contributedDevice.getInterruptIndices());
			}
		}
		
		
		// TODO ----
		try {
			
			ram = new Ram(25);
			bus.add(0x00000000, ram, new int[] {});
	
			rom = new Rom(21);
			// rom.readContentsFromFile(new File("/Users/martin/workspace/eco32-0.20/monitor/monitor/monitor.bin"));
			rom.readContentsFromFile(new File("resource/simulator/monitor.bin"));
			bus.add(0x20000000, rom, new int[] {});
	
			// Disk disk = new Disk(new File("/Users/martin/workspace/eco32-0.20/build/run/disk.img"));
			disk = new Disk(new File("resource/simulator/disk.img"));
			bus.add(0x30400000, disk, new int[] {
				8
			});
	
			terminal = new Terminal();
			bus.add(0x30300000, terminal, new int[] {
				1, 0
			});
			terminalUserInterface = new TerminalUserInterface(eventOutbox);
			terminal.setUserInterface(terminalUserInterface);
	
			characterDisplay = new CharacterDisplay();
			bus.add(0x30100000, characterDisplay, new int[] {});
	
			keyboard = new Keyboard();
			bus.add(0x30200000, keyboard, new int[] {
				4
			});
	
			// TODO graphics
			// TODO block console
			
			output = new OutputDevice(new File("testout.bin"));
			bus.add(0x3F000000, output, new int[] {});
	
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
		terminalUserInterface.clearOutput();
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
	
	/**
	 * Getter method for the terminalUserInterface.
	 * @return the terminalUserInterface
	 */
	public TerminalUserInterface getTerminalUserInterface() {
		return terminalUserInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(String id) {
		// CompositeSimulationModelElementPanel panel = new CompositeSimulationModelElementPanel(id, simulationModel, true);
		// TODO
		// return panel;
		return null;
	}
	
}
