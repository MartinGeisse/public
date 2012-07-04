/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Comparator;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.DefaultPlugin;
import name.martingeisse.admin.application.Launcher;
import name.martingeisse.admin.application.capabilities.ExplicitEntityPropertyFilter;
import name.martingeisse.admin.application.capabilities.PrefixEliminatingEntityNameMappingStrategy;
import name.martingeisse.admin.application.capabilities.SingleEntityPropertyFilter;
import name.martingeisse.admin.customization.multi.IdOnlyGlobalEntityListPanel;
import name.martingeisse.admin.customization.multi.PopulatorDataViewPanel;
import name.martingeisse.admin.customization.multi.RoleOrderListPanel;
import name.martingeisse.admin.multi.GlobalEntityListPresenter;
import name.martingeisse.admin.multi.populator.EntityFieldPopulator;
import name.martingeisse.admin.multi.populator.IEntityCellPopulator;
import name.martingeisse.admin.multi.populator.MultiCellPopulator;
import name.martingeisse.admin.multi.populator.PopulatorBasedGlobalEntityListPresenter;
import name.martingeisse.admin.navigation.EntityListPageNavigationBackMapper;
import name.martingeisse.admin.navigation.GlobalEntityListNavigationLeaf;
import name.martingeisse.admin.navigation.NavigationFolder;
import name.martingeisse.admin.navigation.PanelPageNavigationLeaf;
import name.martingeisse.admin.navigation.UrlNavigationLeaf;
import name.martingeisse.admin.readonly.BaselineReadOnlyRendererContributor;
import name.martingeisse.admin.schema.AbstractDatabaseDescriptor;
import name.martingeisse.admin.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.schema.MysqlDatabaseDescriptor;
import name.martingeisse.wicket.autoform.AutoformPanel;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformAssociatedValidator;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformValidator;
import name.martingeisse.wicket.autoform.componentfactory.DefaultAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.describe.DefaultAutoformBeanDescriber;


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
	public static void main(final String[] args) throws Exception {

		//		DatabaseDescriptor mainDatabase = new DatabaseDescriptor();
		//		mainDatabase.setDisplayName("main database");
		//		mainDatabase.setUrl("jdbc:postgresql://localhost/admintest");
		//		mainDatabase.setUsername("postgres");
		//		mainDatabase.setPassword("postgres");
		//		ApplicationConfiguration.addDatabase(mainDatabase);

		final AbstractDatabaseDescriptor phpbbDatabase = new MysqlDatabaseDescriptor();
		phpbbDatabase.setDisplayName("phpBB database");
		phpbbDatabase.setUrl("jdbc:mysql://localhost/phpbb");
		phpbbDatabase.setUsername("root");
		phpbbDatabase.setPassword("");
		ApplicationConfiguration.addDatabase(phpbbDatabase);
		ApplicationConfiguration.setEntityNameMappingStrategy(new PrefixEliminatingEntityNameMappingStrategy("phpbb_"));

		ApplicationConfiguration.addPlugin(new DefaultPlugin());
		ApplicationConfiguration.addPlugin(new CustomizationPlugin());
		ApplicationConfiguration.addPlugin(new BaselineReadOnlyRendererContributor());
		ApplicationConfiguration.addPlugin(new PrintNameAction());
		ApplicationConfiguration.addPlugin(new SingleEntityPropertyFilter(1, null, "modificationTimestamp", false));
		ApplicationConfiguration.addPlugin(new SingleEntityPropertyFilter(1, null, "modificationUser_id", false));
		ApplicationConfiguration.addPlugin(new SingleEntityPropertyFilter(1, "User", "lastLoginAttemptTimestamp", false));
		ApplicationConfiguration.addPlugin(new GlobalEntityListPresenter("ids", "IDs only", IdOnlyGlobalEntityListPanel.class));
		ApplicationConfiguration.addPlugin(new GlobalEntityListPresenter("roleList", "Role List", RoleOrderListPanel.class));
		ApplicationConfiguration.addPlugin(new PopulatorBasedGlobalEntityListPresenter("pop", "Populator-Based", Arrays.<IEntityCellPopulator> asList(new EntityFieldPopulator("Role Description", "role_description"), new EntityFieldPopulator("Role Order", "role_order"), new MultiCellPopulator("Test", new EntityFieldPopulator(null, "role_description"), new EntityFieldPopulator(null, "role_order")))));
		ApplicationConfiguration.addPlugin(new GlobalEntityListPresenter("popdata", "Populator / DataView", PopulatorDataViewPanel.class));
		ApplicationConfiguration.addPlugin(new EntityListPageNavigationBackMapper());
		ApplicationConfiguration.addPlugin(new MySchemaStringResourceContributor());
		
		

		final ExplicitEntityPropertyFilter userPropertyFilter = new ExplicitEntityPropertyFilter(2, "User");
		userPropertyFilter.getVisiblePropertyNames().add("id");
		userPropertyFilter.getVisiblePropertyNames().add("name");
		ApplicationConfiguration.addPlugin(userPropertyFilter);

		ApplicationConfiguration.setRawEntityListFieldOrder(new Comparator<EntityPropertyDescriptor>() {
			@Override
			public int compare(final EntityPropertyDescriptor o1, final EntityPropertyDescriptor o2) {
				if (o1.getName().equals("id")) {
					return -1;
				} else if (o2.getName().equals("id")) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		ApplicationConfiguration.setPageBorderFactory(new PageBorderFactory());

		buildNavigation();
		Launcher.launch();

	}

	/**
	 * 
	 */
	private static void buildNavigation() {
		final NavigationFolder root = ApplicationConfiguration.getNavigationTree().getRoot();
		root.initChild(new UrlNavigationLeaf("/"), "Home");
		final NavigationFolder sub1 = root.addNewSubfolder("Sub One");
		final NavigationFolder sub1sub1 = sub1.addNewSubfolder("s1 Sub One");
		sub1sub1.initChild(new GlobalEntityListNavigationLeaf("phpbb_acl_roles"), "ACL: Roles");
		sub1.initChild(new GlobalEntityListNavigationLeaf("phpbb_acl_users"), "ACL: Users");
		root.addNewSubfolder("Sub Two");
		root.addNewSubfolder("Sub Three");

		root.initChild(new PanelPageNavigationLeaf(MyAutoformPanel.class, null, true), "Test");
	}

	/**
	 * TODO: document me
	 *
	 */
	public static class MyAutoformBean implements Serializable {

		/**
		 * the name1
		 */
		private String name1;

		/**
		 * the name2
		 */
		private String name2;

		/**
		 * the name3
		 */
		private String name3;

		/**
		 * Getter method for the name1.
		 * @return the name1
		 */
		@MyMaxLength(4)
		public String getName1() {
			return name1;
		}

		/**
		 * Setter method for the name1.
		 * @param name1 the name1 to set
		 */
		public void setName1(final String name1) {
			this.name1 = name1;
		}

		/**
		 * Getter method for the name2.
		 * @return the name2
		 */
		@AutoformValidator(MyValidator.class)
		public String getName2() {
			return name2;
		}

		/**
		 * Setter method for the name2.
		 * @param name2 the name2 to set
		 */
		public void setName2(final String name2) {
			this.name2 = name2;
		}

		/**
		 * Getter method for the name3.
		 * @return the name3
		 */
		public String getName3() {
			return name3;
		}

		/**
		 * Setter method for the name3.
		 * @param name3 the name3 to set
		 */
		public void setName3(final String name3) {
			this.name3 = name3;
		}

	}

	/**
	 *
	 */
	public static class MyAutoformPanel extends AutoformPanel {

		/**
		 * Constructor.
		 * @param id the wicket id
		 */
		public MyAutoformPanel(final String id) {
			super(id, new MyAutoformBean(), DefaultAutoformBeanDescriber.instance, DefaultAutoformPropertyComponentFactory.instance);
		}

	}

	/**
	 *
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@AutoformAssociatedValidator(MyValidator.class)
	public static @interface MyMaxLength {

		/**
		 * @return the max length
		 */
		public int value();
		
	}
	
}
