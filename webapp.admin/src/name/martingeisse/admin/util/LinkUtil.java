/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * This class contains utility methods to deal with HTML links.
 */
public class LinkUtil {

	/**
	 * Prevent instantiation.
	 */
	private LinkUtil() {
	}

	/**
	 * Creates a link that is disabled and has "#" as its href.
	 * @param wicketId the wicket id of the link to create
	 * @return the link
	 */
	public static AbstractLink createDisabledLink(final String wicketId) {
		final ExternalLink link = new ExternalLink(wicketId, "#");
		link.setEnabled(false);
		return link;
	}

	/**
	 * Creates a link to the single-instance entity presentation page of the specified entity instance. 
	 * @param wicketId the wicket id of the link to create
	 * @param entity the linked entity class
	 * @param entityId the id of the linked entity instance, or null to use the current ID from the page that contains the link
	 * @param subpathSegments the subpath segments to walk from the entity instance navigation root
	 * to reach the node to link. The specified node must exist, otherwise this method throws an
	 * {@link IllegalArgumentException}.
	 * @return the link
	 */
	public static BookmarkablePageLink<?> createSingleEntityLink(final String wicketId, final EntityDescriptor entity, final Object entityId, final String... subpathSegments) {
		final BookmarkablePageLink<?> link = (BookmarkablePageLink<?>)entity.getInstanceNavigationNode(subpathSegments).createLink(wicketId);
		if (entityId != null) {
			link.getPageParameters().add("id", entityId);
		}
		return link;
	}

	/**
	 * Returns the URL for the single-instance entity presentation page of the specified entity instance. 
	 * @param entity the linked entity class
	 * @param entityId the id of the linked entity instance, or null to use the current ID from the page that contains the link
	 * @param subpathSegments the subpath segments to walk from the entity instance navigation root
	 * to reach the node to link. The specified node must exist, otherwise this method throws an
	 * {@link IllegalArgumentException}.
	 * @return the link
	 */
	public static String getSingleEntityLinkUrl(final EntityDescriptor entity, final Object entityId, final String... subpathSegments) {
		final BookmarkablePageLink<?> link = createSingleEntityLink("dummy", entity, entityId, subpathSegments);
		return RequestCycle.get().urlFor(link.getPageClass(), link.getPageParameters()).toString();
	}

	/**
	 * Creates a link to the single-instance entity presentation page of the specified entity instance. 
	 * @param wicketId the wicket id of the link to create
	 * @param entityName the name of the linked entity class
	 * @param entityId the id of the linked entity instance, or null to use the current ID from the page that contains the link
	 * @param subpathSegments the subpath segments to walk from the entity instance navigation root
	 * to reach the node to link. The specified node must exist, otherwise this method throws an
	 * {@link IllegalArgumentException}.
	 * @return the link
	 */
	public static AbstractLink createSingleEntityLink(final String wicketId, final String entityName, final Object entityId, final String... subpathSegments) {
		ParameterUtil.ensureNotNull(wicketId, "wicketId");
		ParameterUtil.ensureNotNull(entityName, "entityName");
		EntityDescriptor entity = ApplicationSchema.instance.findRequiredEntity(entityName);
		return createSingleEntityLink(wicketId, entity, entityId, subpathSegments);
	}

}
