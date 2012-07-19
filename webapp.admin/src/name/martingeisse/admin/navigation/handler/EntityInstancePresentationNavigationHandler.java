/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.IEntityNameAware;
import name.martingeisse.admin.pages.EntityPresentationPage;

import org.apache.wicket.util.string.StringValue;

/**
 * This node handler represents the presentation page for
 * an entity instance. Both the entity type and the presenter
 * name can be either fixed or determined from a parameter.
 * The values for both must be specified in this handler
 * if and only if the navigation path to this handler does
 * not contain variable declarations for them.
 */
public class EntityInstancePresentationNavigationHandler extends BookmarkablePageNavigationHandler implements IEntityNameAware {

	/**
	 * Constructor.
	 * @param fixedEntity the entity name, or null to use a parameter named "entity"
	 * @param fixedPresenter the presenter name, or null to use a parameter named "presenter".
	 * Note: To use the default presenter, pass its name, "default".
	 */
	public EntityInstancePresentationNavigationHandler(String fixedEntity, String fixedPresenter) {
		super(EntityPresentationPage.class);
		if (fixedEntity != null) {
			getImplicitPageParameters().add("entity", fixedEntity);
		}
		if (fixedPresenter != null) {
			getImplicitPageParameters().add("presenter", fixedPresenter);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameAware#getEntityName()
	 */
	@Override
	public String getEntityName() {
		StringValue value = getImplicitPageParameters().get("entity");
		return (value == null ? null : value.toString());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameAware#setEntityName(java.lang.String)
	 */
	@Override
	public void setEntityName(String entityName) {
		getImplicitPageParameters().remove("entity");
		getImplicitPageParameters().add("entity", entityName);
	}

}
