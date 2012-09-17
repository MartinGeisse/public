/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.CapabilityKey;
import name.martingeisse.admin.entity.property.IRawEntityListPropertyDisplayFilter;
import name.martingeisse.admin.entity.schema.IEntityNavigationContributor;
import name.martingeisse.admin.entity.schema.annotation.IEntityAnnotationContributor;
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
	 * The capability key for entity annotation contributors.
	 */
	public static final CapabilityKey<IEntityAnnotationContributor> entityAnnotationContributorCapability = new CapabilityKey<IEntityAnnotationContributor>();

	/**
	 * Prevent instantiation.
	 */
	private EntityCapabilities() {
	}

}
