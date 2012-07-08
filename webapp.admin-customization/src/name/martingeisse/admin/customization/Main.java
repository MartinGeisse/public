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

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.DefaultPlugin;
import name.martingeisse.admin.application.Launcher;
import name.martingeisse.admin.application.wicket.AbstractWebApplicationInitializationContributor;
import name.martingeisse.admin.customization.mount.MountTestPage;
import name.martingeisse.admin.customization.multi.IdOnlyGlobalEntityListPanel;
import name.martingeisse.admin.customization.multi.PopulatorDataViewPanel;
import name.martingeisse.admin.customization.multi.RoleOrderListPanel;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.PrefixEliminatingEntityNameMappingStrategy;
import name.martingeisse.admin.entity.multi.GlobalEntityListPresenter;
import name.martingeisse.admin.entity.multi.IEntityListFieldOrder;
import name.martingeisse.admin.entity.multi.populator.EntityFieldPopulator;
import name.martingeisse.admin.entity.multi.populator.IEntityCellPopulator;
import name.martingeisse.admin.entity.multi.populator.MultiCellPopulator;
import name.martingeisse.admin.entity.multi.populator.PopulatorBasedGlobalEntityListPresenter;
import name.martingeisse.admin.entity.property.ExplicitEntityPropertyFilter;
import name.martingeisse.admin.entity.property.SingleEntityPropertyFilter;
import name.martingeisse.admin.entity.schema.AbstractDatabaseDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.entity.schema.MysqlDatabaseDescriptor;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationFolder;
import name.martingeisse.admin.navigation.leaf.GlobalEntityListNavigationLeaf;
import name.martingeisse.admin.navigation.leaf.PanelPageNavigationLeaf;
import name.martingeisse.admin.navigation.leaf.UrlNavigationLeaf;
import name.martingeisse.admin.pages.PagesConfigurationUtil;
import name.martingeisse.admin.readonly.BaselineReadOnlyRendererContributor;
import name.martingeisse.wicket.autoform.AutoformPanel;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformAssociatedValidator;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformValidator;
import name.martingeisse.wicket.autoform.componentfactory.DefaultAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.describe.DefaultAutoformBeanDescriber;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.MountedMapper;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;


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
		ApplicationConfiguration.get().addPlugin(new PrintNameAction());
		ApplicationConfiguration.get().addPlugin(new SingleEntityPropertyFilter(1, null, "modificationTimestamp", false));
		ApplicationConfiguration.get().addPlugin(new SingleEntityPropertyFilter(1, null, "modificationUser_id", false));
		ApplicationConfiguration.get().addPlugin(new SingleEntityPropertyFilter(1, "User", "lastLoginAttemptTimestamp", false));
		ApplicationConfiguration.get().addPlugin(new GlobalEntityListPresenter("ids", "IDs only", IdOnlyGlobalEntityListPanel.class));
		ApplicationConfiguration.get().addPlugin(new GlobalEntityListPresenter("roleList", "Role List", RoleOrderListPanel.class));
		ApplicationConfiguration.get().addPlugin(new PopulatorBasedGlobalEntityListPresenter("pop", "Populator-Based", Arrays.<IEntityCellPopulator> asList(new EntityFieldPopulator("Role Description", "role_description"), new EntityFieldPopulator("Role Order", "role_order"), new MultiCellPopulator("Test", new EntityFieldPopulator(null, "role_description"), new EntityFieldPopulator(null, "role_order")))));
		ApplicationConfiguration.get().addPlugin(new GlobalEntityListPresenter("popdata", "Populator / DataView", PopulatorDataViewPanel.class));
		ApplicationConfiguration.get().addPlugin(new MySchemaStringResourceContributor());

		final ExplicitEntityPropertyFilter userPropertyFilter = new ExplicitEntityPropertyFilter(2, "User");
		userPropertyFilter.getVisiblePropertyNames().add("id");
		userPropertyFilter.getVisiblePropertyNames().add("name");
		ApplicationConfiguration.get().addPlugin(userPropertyFilter);
		
		// parameters
		EntityConfigurationUtil.setEntityNameMappingStrategy(new PrefixEliminatingEntityNameMappingStrategy("phpbb_"));
		PagesConfigurationUtil.setPageBorderFactory(new PageBorderFactory());
		EntityConfigurationUtil.setEntityListFieldOrder(new IEntityListFieldOrder() {
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

		// test
		ApplicationConfiguration.get().addPlugin(new AbstractWebApplicationInitializationContributor() {

			/* (non-Javadoc)
			 * @see name.martingeisse.admin.application.wicket.IWebApplicationInitializationContributor#onInitializeWebApplication(org.apache.wicket.protocol.http.WebApplication)
			 */
			@Override
			public void onInitializeWebApplication(WebApplication webApplication) {
				
				PageParametersEncoder encoder = new PageParametersEncoder() {

					/* (non-Javadoc)
					 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#decodePageParameters(org.apache.wicket.request.Request)
					 */
					@Override
					public PageParameters decodePageParameters(Request request) {
						PageParameters parameters = super.decodePageParameters(request);
						if (parameters == null) {
							parameters = new PageParameters();
						}
						parameters.add("bar", "BARRRARRRARRR");
						return parameters;
					}
					
					/* (non-Javadoc)
					 * @see org.apache.wicket.request.mapper.parameter.PageParametersEncoder#encodePageParameters(org.apache.wicket.request.mapper.parameter.PageParameters)
					 */
					@Override
					public Url encodePageParameters(PageParameters pageParameters) {
						PageParameters copy = new PageParameters(pageParameters);
						copy.remove("bar");
						return super.encodePageParameters(copy);
					}
					
				};
				
				webApplication.mount(new MountedMapper("/mount/${foo}", MountTestPage.class, encoder));
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
		final NavigationFolder root = NavigationConfigurationUtil.getNavigationTree().getRoot();
		root.initChild(new UrlNavigationLeaf("/"), "", "Home");
		final NavigationFolder sub1 = root.addNewSubfolder("sub-one", "Sub One");
		final NavigationFolder sub1sub1 = sub1.addNewSubfolder("s1-sub-one", "s1 Sub One");
		sub1sub1.initChild(new GlobalEntityListNavigationLeaf("phpbb_acl_roles"), "roles", "ACL: Roles");
		sub1.initChild(new GlobalEntityListNavigationLeaf("phpbb_acl_users"), "users", "ACL: Users");
		root.addNewSubfolder("sub-two", "Sub Two");
		root.addNewSubfolder("sub-three", "Sub Three");

		root.initChild(new PanelPageNavigationLeaf(MyAutoformPanel.class, null, true), "test", "Test");
		
//		System.out.println("* " + NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode("/").getPath());
//		System.out.println("* " + NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode("sub-one").getPath());
//		System.out.println("* " + NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode("/bla").getPath());
//		System.out.println("* " + NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode("/sub-one").getPath());
//		System.out.println("* " + NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode("/sub-one/s1-sub-one").getPath());
//		System.out.println("* " + NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode("/sub-two/foo").getPath());
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
