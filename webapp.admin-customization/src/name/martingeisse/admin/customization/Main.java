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
import name.martingeisse.admin.customization.incubator.NavigationTabBarFactory;
import name.martingeisse.admin.customization.pagebar.BasicPageBarFactory;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.GeneralEntityConfiguration;
import name.martingeisse.admin.entity.component.instance.RawEntityPresentationPanel;
import name.martingeisse.admin.entity.component.list.datatable.populator.PopulatorColumnDescriptor;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.EntityConditions;
import name.martingeisse.admin.entity.list.EntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.property.ExplicitEntityPropertyFilter;
import name.martingeisse.admin.entity.property.SingleEntityPropertyFilter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.entity.schema.IEntityListFieldOrder;
import name.martingeisse.admin.entity.schema.IEntityNavigationContributor;
import name.martingeisse.admin.entity.schema.PrefixEliminatingEntityNameMappingStrategy;
import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;
import name.martingeisse.admin.entity.schema.database.MysqlDatabaseDescriptor;
import name.martingeisse.admin.entity.schema.reference.FixedNameEntityReferenceDetector;
import name.martingeisse.admin.entity.schema.search.IEntitySearchContributor;
import name.martingeisse.admin.entity.schema.search.IEntitySearchStrategy;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;
import name.martingeisse.admin.navigation.handler.EntityListPanelHandler;
import name.martingeisse.admin.navigation.handler.PopulatorBasedEntityListHandler2;
import name.martingeisse.admin.navigation.handler.UrlNavigationHandler;
import name.martingeisse.admin.readonly.BaselineReadOnlyRendererContributor;
import name.martingeisse.wicket.autoform.AutoformPanel;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformAssociatedValidator;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformValidator;
import name.martingeisse.wicket.autoform.componentfactory.DefaultAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.describe.DefaultAutoformBeanDescriber;
import name.martingeisse.wicket.populator.RowFieldPopulator;

import com.mysema.query.types.Ops;

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

		// --- code generation start ---
		/*
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/phpbb", "root", "");
		MetaDataExporter exporter = new MetaDataExporter();
		exporter.setTargetFolder(new File("generated"));
		exporter.setPackageName("foo");
		exporter.export(connection.getMetaData());  
		connection.close();
		System.exit(0);
		*/
		// --- code generation end ---
		
		
		//		DatabaseDescriptor mainDatabase = new DatabaseDescriptor();
		//		mainDatabase.setDisplayName("main database");
		//		mainDatabase.setUrl("jdbc:postgresql://localhost/admintest");
		//		mainDatabase.setUsername("postgres");
		//		mainDatabase.setPassword("postgres");
		//		ApplicationConfiguration.addDatabase(mainDatabase);

		// the database
//		final AbstractDatabaseDescriptor phpbbDatabase = new MysqlDatabaseDescriptor();
//		phpbbDatabase.setDisplayName("phpBB database");
//		phpbbDatabase.setUrl("jdbc:mysql://localhost/phpbb");
//		phpbbDatabase.setUsername("root");
//		phpbbDatabase.setPassword("");
//		ApplicationConfiguration.get().addDatabase(phpbbDatabase);

		// the database
		final AbstractDatabaseDescriptor phorumDatabase = new MysqlDatabaseDescriptor();
		phorumDatabase.setDisplayName("Phorum database");
		phorumDatabase.setUrl("jdbc:mysql://localhost/phorum");
		phorumDatabase.setUsername("root");
		phorumDatabase.setPassword("");
		ApplicationConfiguration.get().addDatabase(phorumDatabase);
		
		// plugins / capabilities
		ApplicationConfiguration.get().addPlugin(new DefaultPlugin());
		ApplicationConfiguration.get().addPlugin(new FixedNameEntityReferenceDetector("extensions", "group_id", "extension_groups"));
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
//		generalEntityConfiguration.setEntityNameMappingStrategy(new PrefixEliminatingEntityNameMappingStrategy("phpbb_"));
		generalEntityConfiguration.setEntityNameMappingStrategy(new PrefixEliminatingEntityNameMappingStrategy("phorum_"));
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
				mainEntityInstanceNode.setHandler(new EntityInstancePanelHandler(RawEntityPresentationPanel.class));
			}
		});
		
		// test raw entity presentation column order
//		EntityConfigurationUtil.getGeneralEntityConfiguration().setEntityListFieldOrder(new IEntityListFieldOrder() {
//			@Override
//			public int compare(EntityPropertyDescriptor prop1, EntityPropertyDescriptor prop2) {
//				return prop1.getName().compareTo(prop2.getName());
//			}
//		});
		
		// test entity search support
		EntityConfigurationUtil.addEntitySearchContributor(new IEntitySearchContributor() {
			
			@Override
			public IEntitySearchStrategy getSearchStrategy(EntityDescriptor entity) {
				return new IEntitySearchStrategy() {
					@Override
					public IEntityListFilter createFilter(EntityDescriptor entity, String searchTerm) {
						EntityConditions conditions = new EntityConditions();
						conditions.addFieldComparison("name", Ops.LIKE, "%" + searchTerm.replace("%", "") + "%");
						return new EntityListFilter(conditions);
					}
				};
			}
			
			@Override
			public int getScore(EntityDescriptor entity) {
				return (entity.getPropertiesByName().get("name") == null ? Integer.MIN_VALUE : 0);
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
		root.setPageBarFactory(new BasicPageBarFactory());
		root.getChildFactory().createChild("home-dummy", "Home", new UrlNavigationHandler("/"));
		final NavigationNode sub1 = root.getChildFactory().createFirstChildHandlerChild("sub-one", "Sub One");
		sub1.getChildFactory().createNavigationFolderChild("s1-sub-one", "s1 Sub One");
		sub1.getChildFactory().createNavigationFolderChild("s1-sub-two", "s1 Sub Two");
		sub1.getChildFactory().createNavigationFolderChild("s1-sub-three", "s1 Sub Three");
		
		
//		sub1sub1.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "acl_roles").setId("roles").setTitle("ACL: Roles"));
//		sub1.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "acl_users").setId("users").setTitle("ACL: Users"));
//		root.createFolderChild("sub-two", "Sub Two");
//		root.createFolderChild("sub-three", "Sub Three");
//		root.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "acl_users").setId("users1").setTitle("Users-1"));
//		root.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "acl_users").setId("users2").setTitle("Users-2"));
//		root.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "acl_users").setId("users3").setTitle("Users-3"));
		
//		final NavigationNode roles = root.createChild(new GlobalEntityListNavigationHandler("acl_roles").setId("roles").setTitle("Roles"));
//		roles.createChild(new EntityInstancePresentationNavigationHandler("acl_roles", "default").setId("${id}").setTitle("Instance"));
		
//		root.createChild(new BookmarkableEntityListNavigationHandler(TODO_REMOVE_RawEntityListPage.class, "acl_roles").setCanonicalEntityListNode(true));

//		IEntityListFilter myFilter = new IEntityListFilter() {
//			@Override
//			public boolean evaluate(EntityInstance input) {
//				Object id = input.getId();
//				if (id instanceof Integer) {
//					int intId = (Integer)id;
//					return intId < 15;
//				}
//				return true;
//			}
//		};
//		IExpression idCondition1 = new BinaryExpression(new ColumnReference("t", "role_id"), BinaryOperator.LESS_THAN, new IntegerLiteral(15));
//		IExpression idCondition2 = new BinaryExpression(new ColumnReference("t", "role_id"), BinaryOperator.NOT_EQUAL, new IntegerLiteral(7));
//		IExpression idConditions = new BinaryExpression(idCondition1, BinaryOperator.AND, idCondition2);
//		IEntityListFilter myFilter = new EntityListFilter(idConditions);
		
		/*
		root.getChildFactory().createEntityListPanelChild("acl_roles", "Roles*", MyPopulatorListPanel.class, "acl_roles");
		
		{
			IEntityCellPopulator description = new EntityFieldPopulator("Role Description", "role_description");
			IEntityCellPopulator order = new EntityFieldPopulator("Role Order", "role_order");
			@SuppressWarnings("unchecked")
			IEntityCellPopulator multi = new MultiCellPopulator("Test", new EntityFieldPopulator(null, "role_description"), new EntityFieldPopulator(null, "role_order"));
			root.getChildFactory().createChild("X", "Roles**", new PopulatorBasedEntityListHandler("acl_roles", description, order, multi));
		}
		
		root.getChildFactory().createEntityListPanelChild("roles_all", "Roles", RawEntityListPanel.class, "acl_roles");

		{
			Path<Integer> roleId = EntityListFilterUtils.fieldPath(Integer.class, "role_id");
			NumberExpression<Integer> mod2 = Expressions.numberOperation(Integer.class, Ops.MOD, roleId, Expressions.constant(2));
			Predicate even = Expressions.predicate(Ops.EQ, mod2, Expressions.constant(0));
			root.getChildFactory().createEntityListPanelChild("roles_even", "Even", RawEntityListPanel.class, "acl_roles", even);
		}

		{
			Path<Integer> roleId = EntityListFilterUtils.fieldPath(Integer.class, "role_id");
			NumberExpression<Integer> mod2 = Expressions.numberOperation(Integer.class, Ops.MOD, roleId, Expressions.constant(2));
			Predicate odd = Expressions.predicate(Ops.EQ, mod2, Expressions.constant(1));
			root.getChildFactory().createEntityListPanelChild("roles_odd", "Odd", RawEntityListPanel.class, "acl_roles", odd);
		}

		{
			EntityConditions conditions = new EntityConditions();
			conditions.addFieldComparison("role_id", Ops.LT, 10);
			root.getChildFactory().createEntityListPanelChild("roles_lt10", "<10", RawEntityListPanel.class, "acl_roles", conditions);
		}

//		{
//			MultiCondition condition = new MultiCondition();
//			condition.addFieldInString("role_type", new String[] {"a_", "f_"});
//			root.createChild(new EntityListPanelHandler(RawEntityListPanel.class, "acl_roles").setFilter(condition).setId("roles_cond").setTitle("ConditionTest"));
//		}
		*/

		root.getChildFactory().createChild("tabletest", "TableTest", new EntityListPanelHandler(RenderedEntityDataTablePanelTest.class, "settings"));
		
		{
			PopulatorColumnDescriptor column1 = new PopulatorColumnDescriptor("name", "name", new RowFieldPopulator<EntityInstance>("name"));
			PopulatorColumnDescriptor column2 = new PopulatorColumnDescriptor("value", new RowFieldPopulator<EntityInstance>("data"));
			PopulatorColumnDescriptor[] columns = new PopulatorColumnDescriptor[] {column1, column2};
			root.getChildFactory().createChild("tabletest2", "TableTest2", new PopulatorBasedEntityListHandler2("settings", columns));
		}
		
		NavigationTabBarFactory.apply(root.findChildById("sub-one"));
		
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
