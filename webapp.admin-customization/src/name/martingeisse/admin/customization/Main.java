/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import java.util.Arrays;
import java.util.Comparator;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.DefaultPlugin;
import name.martingeisse.admin.application.Launcher;
import name.martingeisse.admin.application.capabilities.ExplicitEntityPropertyFilter;
import name.martingeisse.admin.application.capabilities.PrefixEliminatingEntityDisplayNameStrategy;
import name.martingeisse.admin.application.capabilities.SingleEntityPropertyFilter;
import name.martingeisse.admin.customization.multi.IdOnlyGlobalEntityListPresenter;
import name.martingeisse.admin.multi.populator.EntityFieldPopulator;
import name.martingeisse.admin.multi.populator.IEntityCellPopulator;
import name.martingeisse.admin.multi.populator.MultiCellPopulator;
import name.martingeisse.admin.multi.populator.PopulatorBasedGlobalEntityListPresenter;
import name.martingeisse.admin.readonly.BaselineReadOnlyRendererContributor;
import name.martingeisse.admin.schema.AbstractDatabaseDescriptor;
import name.martingeisse.admin.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.schema.MysqlDatabaseDescriptor;


/**
 * The main class.
 */
public class Main {

	/**
	 * The main method
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
//		DatabaseDescriptor mainDatabase = new DatabaseDescriptor();
//		mainDatabase.setDisplayName("main database");
//		mainDatabase.setUrl("jdbc:postgresql://localhost/admintest");
//		mainDatabase.setUsername("postgres");
//		mainDatabase.setPassword("postgres");
//		ApplicationConfiguration.addDatabase(mainDatabase);
		
		AbstractDatabaseDescriptor phpbbDatabase = new MysqlDatabaseDescriptor();
		phpbbDatabase.setDisplayName("phpBB database");
		phpbbDatabase.setUrl("jdbc:mysql://localhost/phpbb");
		phpbbDatabase.setUsername("root");
		phpbbDatabase.setPassword("");
		ApplicationConfiguration.addDatabase(phpbbDatabase);
		ApplicationConfiguration.setEntityDisplayNameStrategy(new PrefixEliminatingEntityDisplayNameStrategy("phpbb_"));
		
		ApplicationConfiguration.addPlugin(new DefaultPlugin());
		ApplicationConfiguration.addPlugin(new CustomizationPlugin());
		ApplicationConfiguration.addPlugin(new BaselineReadOnlyRendererContributor());
		ApplicationConfiguration.addPlugin(new PrintNameAction());
		ApplicationConfiguration.addPlugin(new SingleEntityPropertyFilter(1, null, "modificationTimestamp", false));
		ApplicationConfiguration.addPlugin(new SingleEntityPropertyFilter(1, null, "modificationUser_id", false));
		ApplicationConfiguration.addPlugin(new SingleEntityPropertyFilter(1, "User", "lastLoginAttemptTimestamp", false));
//		ApplicationConfiguration.addPlugin(new SingleEntityOverviewPresenter(OverviewPanel.class, 1));
		ApplicationConfiguration.addPlugin(new IdOnlyGlobalEntityListPresenter());
		ApplicationConfiguration.addPlugin(new PopulatorBasedGlobalEntityListPresenter("pop", "Populator-Based", Arrays.<IEntityCellPopulator>asList(
			new EntityFieldPopulator("Role Description", "role_description"),
			new EntityFieldPopulator("Role Order", "role_order"),
			new MultiCellPopulator("Test",
				new EntityFieldPopulator(null, "role_description"),
				new EntityFieldPopulator(null, "role_order")
			)
		)));
		
		ExplicitEntityPropertyFilter userPropertyFilter = new ExplicitEntityPropertyFilter(2, "User");
		userPropertyFilter.getVisiblePropertyNames().add("id");
		userPropertyFilter.getVisiblePropertyNames().add("name");
		ApplicationConfiguration.addPlugin(userPropertyFilter);
		
		ApplicationConfiguration.setRawEntityListFieldOrder(new Comparator<EntityPropertyDescriptor>() {
			@Override
			public int compare(EntityPropertyDescriptor o1, EntityPropertyDescriptor o2) {
				if (o1.getName().equals("id")) {
					return -1;
				} else if (o2.getName().equals("id")) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		Launcher.launch();
		
	}
	
}
