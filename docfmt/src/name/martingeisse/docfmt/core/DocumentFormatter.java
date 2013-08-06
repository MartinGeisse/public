/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.docfmt.core;

import name.martingeisse.docfmt.dfault.DefaultPluginResolver;

/**
 * Main entry point for the document formatter system.
 */
public final class DocumentFormatter {

	/**
	 * the inputProducer
	 */
	private final IInputProducer inputProducer;
	
	/**
	 * the outputConsumer
	 */
	private final IOutputConsumer outputConsumer;
	
	/**
	 * the pluginResolver
	 */
	private final IPluginResolver pluginResolver;

	/**
	 * Constructor for custom input/output but standard plugin resolution.
	 * 
	 * @param inputProducer the input producer
	 * @param outputConsumer the output consumer
	 */
	public DocumentFormatter(IInputProducer inputProducer, IOutputConsumer outputConsumer) {
		this(inputProducer, outputConsumer, new DefaultPluginResolver());
	}

	/**
	 * Most detailed constructor that allows the caller to specify all
	 * formatting behavior manually.
	 * 
	 * @param inputProducer the input producer
	 * @param outputConsumer the output consumer
	 * @param pluginResolver the plugin resolver
	 */
	public DocumentFormatter(IInputProducer inputProducer, IOutputConsumer outputConsumer, IPluginResolver pluginResolver) {
		this.inputProducer = inputProducer;
		this.outputConsumer = outputConsumer;
		this.pluginResolver = pluginResolver;
	}

	/**
	 * Getter method for the inputProducer.
	 * @return the inputProducer
	 */
	public IInputProducer getInputProducer() {
		return inputProducer;
	}
	
	/**
	 * Getter method for the outputConsumer.
	 * @return the outputConsumer
	 */
	public IOutputConsumer getOutputConsumer() {
		return outputConsumer;
	}
	
	/**
	 * Getter method for the pluginResolver.
	 * @return the pluginResolver
	 */
	public IPluginResolver getPluginResolver() {
		return pluginResolver;
	}
	
}
