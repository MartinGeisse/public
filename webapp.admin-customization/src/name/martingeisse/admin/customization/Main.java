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
import name.martingeisse.admin.application.hooks.ISchemaAwareContributor;
import name.martingeisse.admin.application.security.SecurityConfiguration;
import name.martingeisse.admin.component.page.login.NopLoginPage;
import name.martingeisse.admin.customization.incubator.NavigationTabBarFactory;
import name.martingeisse.admin.customization.pagebar.BasicPageBarFactory;
import name.martingeisse.admin.customization.reflist.SettingPanel;
import name.martingeisse.admin.entity.EntityCapabilities;
import name.martingeisse.admin.entity.EntityConfiguration;
import name.martingeisse.admin.entity.EntityDescriptorModel;
import name.martingeisse.admin.entity.EntitySelection;
import name.martingeisse.admin.entity.component.instance.NavigationMountedEntityAutoformPanel;
import name.martingeisse.admin.entity.component.instance.RawEntityPresentationPanel;
import name.martingeisse.admin.entity.component.list.datatable.populator.PopulatorColumnDescriptor;
import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.list.EntityConditions;
import name.martingeisse.admin.entity.property.ExplicitEntityPropertyFilter;
import name.martingeisse.admin.entity.property.SingleEntityPropertyFilter;
import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.entity.schema.IEntityListFieldOrder;
import name.martingeisse.admin.entity.schema.IEntityNavigationContributor;
import name.martingeisse.admin.entity.schema.annotation.AnnotatedClassEntityAnnotationContributor;
import name.martingeisse.admin.entity.schema.annotation.IEntityAnnotatedClassResolver;
import name.martingeisse.admin.entity.schema.naming.IEntityNameMappingStrategy;
import name.martingeisse.admin.entity.schema.naming.PrefixEliminatingEntityNameMappingStrategy;
import name.martingeisse.admin.entity.schema.orm.EntityOrmMapping;
import name.martingeisse.admin.entity.schema.orm.IEntityOrmMapper;
import name.martingeisse.admin.entity.schema.search.IEntitySearchContributor;
import name.martingeisse.admin.entity.schema.search.IEntitySearchStrategy;
import name.martingeisse.admin.navigation.NavigationConfiguration;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;
import name.martingeisse.admin.navigation.handler.EntityListPanelHandler;
import name.martingeisse.admin.navigation.handler.PanelPageNavigationHandler;
import name.martingeisse.admin.navigation.handler.PopulatorBasedEntityListHandler;
import name.martingeisse.admin.navigation.handler.UrlNavigationHandler;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.MysqlDatabaseDescriptor;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.wicket.autoform.AutoformPanel;
import name.martingeisse.wicket.autoform.annotation.structure.AutoformPropertyOrder;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformAssociatedValidator;
import name.martingeisse.wicket.autoform.annotation.validation.palette.MinIntegerValue;
import name.martingeisse.wicket.autoform.componentfactory.DefaultAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.describe.DefaultAutoformBeanDescriber;
import name.martingeisse.wicket.populator.RowFieldPopulator;

import org.joda.time.DateTimeZone;

import com.google.common.base.CaseFormat;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

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
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/phorum?zeroDateTimeBehavior=convertToNull&useTimezone=false", "root", "");
		MetaDataExporter exporter = new MetaDataExporter();
		exporter.setTargetFolder(new File("generated"));
		exporter.setPackageName("phorum");
		exporter.setSerializerClass(MetaDataSerializer.class);
		exporter.setBeanSerializer(new BeanSerializer(true));
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

		//
		// The database. Note about "useTimezone=false": This is needed so we can load local dates and
		// datetimes from the database. JDBC uses an inherently broken model here, which we're trying to
		// circumvent. Speaking in Joda-Time terms, the database stores a local date or datetime value,
		// NOT an instant -- that is, the timezone is implicit, not stored. JDBC, however, always
		// wants to convert this to an instant that is expressed as a java.sql.Date or java.sql.Timestamp,
		// i.e. an instant that is represented in UTC, using an implicit timezone whose origin depends
		// on the JDBC driver. To obtain a local value without too much hassle, we set "useTimezone=false"
		// to make this conversion a NOP, i.e. make JDBC assume that the database value is a local value
		// that is to be interpreted as UTC, so the conversion just takes this value and adds "UTC" to it.
		// We then take the value and create a local value as a Joda object by throwing away the "UTC" again.
		//
		// NOTE: This description is specific to MySQL. For other databases, we basically need to do
		// whatever is necessary to read the local value from the database as a local value expressed
		// as a Joda object, circumventing JDBC's stupidity and implicit rules.
		//
		// NOTE: The server stores a server-wide time zone setting. One *could* use that to interpret
		// database values as instants instead of local values. However, (1) this setting cannot be
		// trusted to be correct since it's not used in many places in DB-side time handling (i.e.
		// we might very well have to deal with an incorrectly configured database and existing
		// applications that know how to work around it), and (2) we'll have to deal with values in the
		// DB that are meant as local values, and might even be part of a (local value, time zone) pair.
		// Applying the server time zone to such values would be incorrect. So we'll only deal with the
		// server time zone as much as needed to get rid of it and obtain the local values that are
		// actually stored in the DB tables.
		//
		// If part of the application wants to store an instant in the database -- a quite common task --
		// we'll rather have the application convert it manually, possibly providing access to
		// automatic type conversion (e.g. an implicitly converting ISqlDateTimeTypeInfo). The
		// admin framework should not do this -- it would be an implicit conversion layer that adds
		// confusion, and it would be unclear to the application developer how the time zone used
		// for that conversion is related to the SQL server time zone setting.
		//
		// NOTE: Actually we don't absolutely *need* that parameter for MySQL since it's the default.
		//
		final MysqlDatabaseDescriptor phorumDatabase = new MysqlDatabaseDescriptor();
		phorumDatabase.setDisplayName("Phorum database");
		phorumDatabase.setUrl("jdbc:mysql://localhost/phorum?zeroDateTimeBehavior=convertToNull&useTimezone=false");
		phorumDatabase.setUsername("root");
		phorumDatabase.setPassword("");
		phorumDatabase.setDefaultTimeZone(DateTimeZone.forID("Europe/Berlin"));
		// phorumDatabase.setDefaultTimeZone(DateTimeZone.forID("Europe/London"));
		ApplicationConfiguration.get().addDatabase(phorumDatabase);
		EntityConnectionManager.initializeDatabaseDescriptors(phorumDatabase);
		
		// plugins / capabilities
		ApplicationConfiguration.get().addPlugin(new DefaultPlugin());
		// ApplicationConfiguration.get().addPlugin(new FixedNameEntityReferenceDetector("extensions", "group_id", "extension_groups"));
		ApplicationConfiguration.get().addPlugin(new CustomizationPlugin());
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
		EntityConfiguration generalEntityConfiguration = new EntityConfiguration();
//		generalEntityConfiguration.setEntityNameMappingStrategy(new PrefixEliminatingEntityNameMappingStrategy("phpbb_"));
		IEntityNameMappingStrategy.PARAMETER_KEY.set(new PrefixEliminatingEntityNameMappingStrategy("phorum_"));
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
		EntityConfiguration.parameterKey.set(generalEntityConfiguration);
		
		// entity navigation contributors
		EntityCapabilities.entityNavigationContributorCapability.add(new IEntityNavigationContributor() {
			@Override
			public void contributeNavigationNodes(EntityDescriptor entity, NavigationNode mainEntityInstanceNode) {
				if (entity.getName().equals("settings")) {
					mainEntityInstanceNode.setHandler(new EntityInstancePanelHandler(SettingPanel.class));
				} else {
					mainEntityInstanceNode.setHandler(new EntityInstancePanelHandler(RawEntityPresentationPanel.class));
				}
				mainEntityInstanceNode.getChildFactory().createEntityInstancePanelChild("edit", "Edit", NavigationMountedEntityAutoformPanel.class);
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
		EntityCapabilities.entitySearchContributorCapability.add(new IEntitySearchContributor() {
			
			@Override
			public IEntitySearchStrategy getSearchStrategy(EntityDescriptor entity) {
				return new IEntitySearchStrategy() {
					@Override
					public Predicate createFilter(EntityDescriptor entity, String searchTerm) {
						EntityConditions conditions = new EntityConditions(entity);
						conditions.addFieldComparison("name", Ops.LIKE, "%" + searchTerm.replace("%", "") + "%");
						return conditions;
					}
				};
			}
			
			@Override
			public int getScore(EntityDescriptor entity) {
				return (entity.getProperties().get("name") == null ? Integer.MIN_VALUE : 0);
			}
			
		});
		
		// security
		SecurityConfiguration.parameterKey.set(new SecurityConfiguration());
		NopLoginPage.bypass = false;
		
		// entity autoforms
		IEntityAnnotatedClassResolver classResolver = new EntityAutoformAnnotatedClassResolver("name.martingeisse.admin.customization.entity");
		EntityCapabilities.entityAnnotationContributorCapability.add(new AnnotatedClassEntityAnnotationContributor(classResolver));
		
		// initialize navigation only after the application schema has been built
		ISchemaAwareContributor.CAPABILITY_KEY.add(new ISchemaAwareContributor() {
			@Override
			public void contribute() {
				buildNavigation();
			}
		});
		
		// initialize specific entity code mapping
		IEntityOrmMapper.PARAMETER_KEY.set(new IEntityOrmMapper() {
			@Override
			public EntityOrmMapping map(EntityDescriptor entityDescriptor) {
				try {
					String baseName = entityDescriptor.getName();
					String suffix = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, baseName);
					String className = "phorum.Phorum" + suffix;
					String qclassName = "phorum.QPhorum" + suffix;
					return new EntityOrmMapping(entityDescriptor, Class.forName(className), GenericTypeUtil.<Class<? extends RelationalPath<?>>> unsafeCast(Class.forName(qclassName)));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		// run
		Launcher.launch();

	}

	/**
	 * 
	 */
	private static void buildNavigation() {
		final NavigationNode root = NavigationConfiguration.navigationTreeParameter.get().getRoot();
		root.setPageBarFactory(new BasicPageBarFactory());
		root.getChildFactory().createChild("home-dummy", "Home", new UrlNavigationHandler("/"));
		final NavigationNode sub1 = root.getChildFactory().createFirstChildHandlerChild("sub-one", "Sub One");
		sub1.getChildFactory().createNavigationFolderChild("s1-sub-one", "s1 Sub One");
		sub1.getChildFactory().createNavigationFolderChild("s1-sub-two", "s1 Sub Two");
		sub1.getChildFactory().createNavigationFolderChild("s1-sub-three", "s1 Sub Three");
		
		try {
//			MySQLQuery query = new MySQLQuery(EntityConnectionManager.getDefaultDatabaseDescriptor().createJdbcConnection());
//			Object entityInstance = query.from(QPhorumSettings.phorumSettings).singleResult(QPhorumSettings.phorumSettings);
			IEntityInstance entityInstance = new EntitySelection(new EntityDescriptorModel("settings")).fetchSingleInstance(false);
			System.out.println("*** RESULT: " + entityInstance.getClass());
		} catch (Exception e) {
			System.out.println("*** ERROR: " + e);
		}
		
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
			RelationalPath<?> entityPath = ApplicationSchema.instance.findRequiredEntity("settings").getQueryBuilder().getDefaultPath();
			Path<Comparable<?>> namePath = GenericTypeUtil.unsafeCast(Expressions.path(String.class, entityPath, "name"));
			
			PopulatorColumnDescriptor column1 = new PopulatorColumnDescriptor("name", namePath, new RowFieldPopulator<IEntityInstance>("name"));
			PopulatorColumnDescriptor column2 = new PopulatorColumnDescriptor("value", new RowFieldPopulator<IEntityInstance>("data"));
			PopulatorColumnDescriptor[] columns = new PopulatorColumnDescriptor[] {column1, column2};
			root.getChildFactory().createChild("tabletest2", "TableTest2", new PopulatorBasedEntityListHandler("settings", columns));
		}

		root.getChildFactory().createChild("autoform-test", "Autoform", new PanelPageNavigationHandler(MyAutoformPanel.class, null, true, false));
		NavigationTabBarFactory.apply(root.findChildById("sub-one"));
		
	}

	/**
	 *
	 */
	@AutoformPropertyOrder({"name1", "name2", "name3"})
	public static class MyAutoformBean implements Serializable {

		/**
		 * the name1
		 */
		private int name1;

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
//		@MyMaxLength(4)
		@MinIntegerValue(3)
		public int getName1() {
			return name1;
		}

		/**
		 * Setter method for the name1.
		 * @param name1 the name1 to set
		 */
		public void setName1(final int name1) {
			this.name1 = name1;
		}

		/**
		 * Getter method for the name2.
		 * @return the name2
		 */
//		@AutoformValidator(MyValidator.class)
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
		
		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.autoform.AutoformPanel#onSuccessfulSubmit()
		 */
		@Override
		protected void onSuccessfulSubmit() {
			System.out.println("*** SUBMIT ***");
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
