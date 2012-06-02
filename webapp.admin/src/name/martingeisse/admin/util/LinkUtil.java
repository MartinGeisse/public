/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

import name.martingeisse.admin.pages.EntityPresentationPage;
import name.martingeisse.admin.schema.EntityDescriptor;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * TODO: document me
 *
 */
public class LinkUtil {

	/**
	 * Creates a link to the single-instance entity presentation page of the specified entity instance. 
	 * @param wicketId the wicket id of the link to create
	 * @param entity the linked entity class
	 * @param entityId the id of the linked entity instance
	 * @return the link
	 */
	public static AbstractLink createSingleEntityLink(String wicketId, EntityDescriptor entity, Object entityId) {
		return createSingleEntityLink(wicketId, entity.getTableName(), entityId);
	}
	
	/**
	 * Creates a link to the single-instance entity presentation page of the specified entity instance. 
	 * @param wicketId the wicket id of the link to create
	 * @param entityName the name of the linked entity class
	 * @param entityId the id of the linked entity instance
	 * @return the link
	 */
	public static AbstractLink createSingleEntityLink(String wicketId, String entityName, Object entityId) {
		PageParameters parameters = new PageParameters();
		parameters.add("entity", entityName);
		parameters.add("id", entityId);
		return new BookmarkablePageLink<Void>(wicketId, EntityPresentationPage.class, parameters);
	}
	
}
