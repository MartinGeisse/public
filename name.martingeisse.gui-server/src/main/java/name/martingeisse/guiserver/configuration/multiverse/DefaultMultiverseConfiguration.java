/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.multiverse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import name.martingeisse.guiserver.application.wicket.GuiWicketApplication;
import name.martingeisse.guiserver.configuration.storage.MultiverseStorage;
import name.martingeisse.guiserver.configuration.storage.SimpleUniverseStorage;
import name.martingeisse.guiserver.configuration.storage.UniverseStorage;
import name.martingeisse.guiserver.configuration.storage.http.ApacheHttpdStorageEngine;
import name.martingeisse.guiserver.configuration.storage.http.HttpFolder;
import name.martingeisse.guiserver.configuration.storage.http.HttpStorageEngine;
import name.martingeisse.guiserver.configuration.storage.multiverse.SingularMultiverseStorage;
import name.martingeisse.guiserver.template.TemplateParser;

/**
 * Default implementation of {@link MultiverseConfiguration}.
 */
public class DefaultMultiverseConfiguration implements MultiverseConfiguration {

	/**
	 * the serialNumberAllocator
	 */
	private final AtomicInteger serialNumberAllocator;

	/**
	 * the hyperspaceConfiguration
	 */
	private final HyperspaceConfiguration hyperspaceConfiguration;

	/**
	 * the universeConfigurations
	 */
	private final Map<String, UniverseConfiguration> universeConfigurations;
	
	/**
	 * Constructor.
	 */
	public DefaultMultiverseConfiguration() {
		
		// initialize helper objects
		serialNumberAllocator = new AtomicInteger();
		
		// initialize hyperspace
		HttpStorageEngine httpStorageEngine = new ApacheHttpdStorageEngine("http://localhost/geisse/demo-gui");
		UniverseStorage universeStorage = new SimpleUniverseStorage(new HttpFolder(httpStorageEngine, "/", "/"));
		MultiverseStorage multiverseStorage = new SingularMultiverseStorage(universeStorage);
		hyperspaceConfiguration = new DefaultHyperspaceConfiguration(multiverseStorage);
		
		// initialize universes
		universeConfigurations = new HashMap<>();
		loadUniverseConfiguration("foo", false);
		loadUniverseConfiguration("bar", false);
		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.MultiverseConfiguration#getHyperspaceConfiguration()
	 */
	@Override
	public HyperspaceConfiguration getHyperspaceConfiguration() {
		return hyperspaceConfiguration;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.MultiverseConfiguration#getUniverseConfiguration(java.lang.String)
	 */
	@Override
	public UniverseConfiguration getUniverseConfiguration(String id) {
		return universeConfigurations.get(id);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.MultiverseConfiguration#reloadUniverseConfiguration(java.lang.String)
	 */
	@Override
	public void reloadUniverseConfiguration(String id) {
		loadUniverseConfiguration(id, true);
	}

	/**
	 * 
	 */
	private void loadUniverseConfiguration(String id, boolean checkExists) {
		if (checkExists && universeConfigurations.get(id) == null) {
			return;
		}
		DefaultUniverseConfigurationBuilder builder = new DefaultUniverseConfigurationBuilder(TemplateParser.INSTANCE);
		UniverseStorage universeStorage = hyperspaceConfiguration.getMultiverseStorage().getUniverseStorage(id);
		int serialNumber = serialNumberAllocator.incrementAndGet();
		UniverseConfiguration universeConfiguration;
		try {
			universeConfiguration = builder.build(universeStorage, serialNumber);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		GuiWicketApplication.get().resetConfigurationDefinedRequestMapper();
		universeConfiguration.mountWicketUrls(GuiWicketApplication.get().getConfigurationDefinedRequestMapper());
		universeConfigurations.put(id, universeConfiguration);
	}
	
}
