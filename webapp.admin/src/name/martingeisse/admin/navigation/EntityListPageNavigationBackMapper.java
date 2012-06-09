/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.admin.pages.EntityTablePage;

import org.apache.wicket.Page;

/**
 * Maps {@link EntityTablePage} instances to {@link GlobalEntityListNavigationLeaf}
 * nodes.
 */
public class EntityListPageNavigationBackMapper extends AbstractNavigationBackMapper {

	/**
	 * the map
	 */
	private Map<String, GlobalEntityListNavigationLeaf> map;

	/**
	 * Constructor.
	 */
	public EntityListPageNavigationBackMapper() {
		map = new HashMap<String, GlobalEntityListNavigationLeaf>();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.INavigationBackMapper#initialize(name.martingeisse.admin.navigation.NavigationTree)
	 */
	@Override
	public void initialize(final NavigationTree tree) {
		tree.getRoot().visitLeafNodes(new INavigationLeafVisitor() {
			@Override
			public void visit(AbstractNavigationLeaf leaf) {
				if (leaf instanceof GlobalEntityListNavigationLeaf) {
					GlobalEntityListNavigationLeaf entityLeaf = (GlobalEntityListNavigationLeaf)leaf;
					map.put(entityLeaf.getEntityName(), entityLeaf);
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.INavigationBackMapper#mapPageToNavigationNode(org.apache.wicket.Page)
	 */
	@Override
	public AbstractNavigationNode mapPageToNavigationNode(final Page page) {
		if (page instanceof EntityTablePage) {
			EntityTablePage entityTablePage = (EntityTablePage)page;
			return map.get(entityTablePage.getEntity().getTableName());
		} else {
			return null;
		}
	}

}
