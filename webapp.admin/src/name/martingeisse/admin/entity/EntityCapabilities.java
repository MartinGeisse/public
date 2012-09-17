/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.CapabilityKey;
import name.martingeisse.admin.entity.property.IRawEntityListPropertyDisplayFilter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.IEntityNavigationContributor;
import name.martingeisse.admin.entity.schema.annotation.IEntityAnnotatedClassResolver;
import name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector;
import name.martingeisse.admin.entity.schema.search.IEntitySearchContributor;

/**
 * Utilities to access entity configuration in the {@link ApplicationConfiguration}.
 */
public final class EntityCapabilities {

	/**
	 * The capability key for raw entity list property display filters.
	 */
	public static final CapabilityKey<IRawEntityListPropertyDisplayFilter> rawEntityListPropertyDisplayFilterCapability = new CapabilityKey<IRawEntityListPropertyDisplayFilter>();

	/**
	 * The capability key for entity reference detectors.
	 */
	public static final CapabilityKey<IEntityReferenceDetector> entityReferenceDetectorCapability = new CapabilityKey<IEntityReferenceDetector>();

	/**
	 * The capability key for entity instance navigation contributors.
	 */
	public static final CapabilityKey<IEntityNavigationContributor> entityNavigationContributorCapability = new CapabilityKey<IEntityNavigationContributor>();

	/**
	 * The capability key for entity search contributors.
	 */
	public static final CapabilityKey<IEntitySearchContributor> entitySearchContributorCapability = new CapabilityKey<IEntitySearchContributor>();

	/**
	 * The capability key for entity autoform annotated class resolvers.
	 */
	public static final CapabilityKey<IEntityAnnotatedClassResolver> entityAutoformAnnotatedClassResolverCapability = new CapabilityKey<IEntityAnnotatedClassResolver>();

	/**
	 * Prevent instantiation.
	 */
	private EntityCapabilities() {
	}

	/**
	 * Resolves the annotated class for the specified entity.
	 * @param entity the entity
	 * @return the annotated class
	 */
	public static Class<?> resolveAnnotatedClass(EntityDescriptor entity) {
		Class<?> result = null;
		IEntityAnnotatedClassResolver successfulResolver = null;
		for (IEntityAnnotatedClassResolver resolver : entityAutoformAnnotatedClassResolverCapability) {
			Class<?> currentResult = resolver.resolveEntityAnnotatedClass(entity);
			if (currentResult != null) {
				if (result == null || result == currentResult) {
					result = currentResult;
					successfulResolver = resolver;
				} else {
					throw new RuntimeException("Ambiguous autoform-annotated classes found for entity " + entity.getName() +
						": Resolver " + successfulResolver + " returned " + result + ", but resolver " +
						resolver + " returned " + currentResult);
				}
			}
		}
		return result;
	}
	
}
