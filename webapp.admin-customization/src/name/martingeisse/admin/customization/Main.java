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

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.DefaultPlugin;
import name.martingeisse.admin.application.Launcher;
import name.martingeisse.admin.component.pageborder.PageBorderFactory;
import name.martingeisse.admin.customization.pageborder.BasicPageBorder;
import name.martingeisse.admin.customization.pageborder.EntityInstancePageBorder;
import name.martingeisse.admin.customization.pageborder.TestBorder;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.GeneralEntityConfiguration;
import name.martingeisse.admin.entity.IEntityListFieldOrder;
import name.martingeisse.admin.entity.IEntityNavigationContributor;
import name.martingeisse.admin.entity.PrefixEliminatingEntityNameMappingStrategy;
import name.martingeisse.admin.entity.component.instance.RawEntityPresentationPanel;
import name.martingeisse.admin.entity.component.list.raw.RawEntityListPanel;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.property.ExplicitEntityPropertyFilter;
import name.martingeisse.admin.entity.property.SingleEntityPropertyFilter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;
import name.martingeisse.admin.entity.schema.database.MysqlDatabaseDescriptor;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.handler.BookmarkableEntityInstanceNavigationHandler;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;
import name.martingeisse.admin.navigation.handler.EntityListPanelHandler;
import name.martingeisse.admin.navigation.handler.UrlNavigationHandler;
import name.martingeisse.admin.readonly.BaselineReadOnlyRendererContributor;
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

		// the database
		final AbstractDatabaseDescriptor phpbbDatabase = new MysqlDatabaseDescriptor();
		phpbbDatabase.setDisplayName("phpBB database");
		phpbbDatabase.setUrl("jdbc:mysql://localhost/phpbb");
		phpbbDatabase.setUsername("root");
		phpbbDatabase.setPassword("");
		ApplicationConfiguration.get().addDatabase(phpbbDatabase);

		// plugins / capabilities
		ApplicationConfiguration.get().addPlugin(new DefaultPlugin());
		ApplicationConfiguration.get().addPlugin(new CustomizationPlugin());
		ApplicationConfiguration.get().addPlugin(new BaselineReadOnlyRendererContributor());
		ApplicationConfiguration.get().addPlugin(new SingleEntityPropertyFilter(1, null, "modificationTimestamp", false));
		ApplicationConfiguration.get().addPlugin(new SingleEntityPropertyFilter(1, null, "modificationUser_id", false));
		ApplicationConfiguration.get().addPlugin(new SingleEntityPropertyFilter(1, "User", "lastLoginAttemptTimestamp", false));
//		ApplicationConfiguration.get().addPlugin(new GlobalEntityListPresenter("ids", "IDs only", IdOnlyGlobalEntityListPanel.class));
//		ApplicationConfiguration.get().addPlugin(new GlobalEntityListPresenter("roleList", "Role List", RoleOrderListPanel.class));
//		ApplicationConfiguration.get().addPlugin(new GlobalEntityListPresenter("popdata", "Populator / DataView", PopulatorDataViewPanel.class));
		ApplicationConfiguration.get().addPlugin(new MySchemaStringResourceContributor());

		final ExplicitEntityPropertyFilter userPropertyFilter = new ExplicitEntityPropertyFilter(2, "User");
		userPropertyFilter.getVisiblePropertyNames().add("id");
		userPropertyFilter.getVisiblePropertyNames().add("name");
		ApplicationConfiguration.get().addPlugin(userPropertyFilter);
		
		// general parameters
//		PagesConfigurationUtil.setPageBorderFactory(new name.martingeisse.admin.customization.PageBorderFactory());
		
		// entity parameters
		GeneralEntityConfiguration generalEntityConfiguration = new GeneralEntityConfiguration();
		generalEntityConfiguration.setEntityNameMappingStrategy(new PrefixEliminatingEntityNameMappingStrategy("phpbb_"));
		generalEntityConfiguration.setEntityListFieldOrder(new IEntityListFieldOrder() {
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
		EntityConfigurationUtil.setGeneralEntityConfiguration(generalEntityConfiguration);
		
		// entity navigation contributors
		EntityConfigurationUtil.addEntityNavigationContributor(new IEntityNavigationContributor() {
			@Override
			public void contributeNavigationNodes(EntityDescriptor entity, NavigationNode mainEntityInstanceNode) {
				
				// add entity-local navigation
				mainEntityInstanceNode.setPageBorderFactory(new PageBorderFactory(EntityInstancePageBorder.class));

				// test
				BookmarkableEntityInstanceNavigationHandler handler;
				
				handler = new EntityInstancePanelHandler(RawEntityPresentationPanel.class);				
				handler.getImplicitPageParameters().add("presenter", "default");
				NavigationNode node = mainEntityInstanceNode.createChild(handler.setId("default").setTitle("Default"));
				node.setPageBorderFactory(new PageBorderFactory(TestBorder.class));
				
				handler = new EntityInstancePanelHandler(RawEntityPresentationPanel.class);				
				handler.getImplicitPageParameters().add("presenter", "default");
				NavigationNode node2 = node.createChild(handler.setId("default").setTitle("Default"));
				node2.setPageBorderFactory(new PageBorderFactory(TestBorder.class));

			}
		});
		
		// run
		buildNavigation();
		Launcher.launch();

	}

	/**
	 * 
	 */
	private static void buildNavigation() {
		final NavigationNode root = NavigationConfigurationUtil.getNavigationTree().getRoot();
		root.setPageBorderFactory(new PageBorderFactory(BasicPageBorder.class));
		root.createChild(new UrlNavigationHandler("/").setId("home-dummy").setTitle("Home"));
		final NavigationNode sub1 = root.createGlobalNavigationFolderChild("sub-one", "Sub One");
		sub1.createGlobalNavigationFolderChild("s1-sub-one", "s1 Sub One");
//		sub1sub1.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "phpbb_acl_roles").setId("roles").setTitle("ACL: Roles"));
//		sub1.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "phpbb_acl_users").setId("users").setTitle("ACL: Users"));
//		root.createFolderChild("sub-two", "Sub Two");
//		root.createFolderChild("sub-three", "Sub Three");
//		root.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "phpbb_acl_users").setId("users1").setTitle("Users-1"));
//		root.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "phpbb_acl_users").setId("users2").setTitle("Users-2"));
//		root.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "phpbb_acl_users").setId("users3").setTitle("Users-3"));
		
//		final NavigationNode roles = root.createChild(new GlobalEntityListNavigationHandler("phpbb_acl_roles").setId("roles").setTitle("Roles"));
//		roles.createChild(new EntityInstancePresentationNavigationHandler("phpbb_acl_roles", "default").setId("${id}").setTitle("Instance"));
		
//		root.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "phpbb_acl_roles").setCanonicalEntityListNode(true));

		IEntityListFilter myFilter = new IEntityListFilter() {
			@Override
			public boolean evaluate(EntityInstance input) {
				Object id = input.getId();
				if (id instanceof Integer) {
					int intId = (Integer)id;
					return intId < 15;
				}
				return true;
			}
		};
		
		root.createChild(new EntityListPanelHandler(MyPopulatorListPanel.class, "phpbb_acl_roles").setTitle("Roles*"));
		root.createChild(new EntityListPanelHandler(RawEntityListPanel.class, "phpbb_acl_roles").setId("A").setTitle("Roles+"));
		root.createChild(new EntityListPanelHandler(RawEntityListPanel.class, "phpbb_acl_roles").setFilter(myFilter).setId("B").setTitle("Roles-"));
	}

	/**
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
