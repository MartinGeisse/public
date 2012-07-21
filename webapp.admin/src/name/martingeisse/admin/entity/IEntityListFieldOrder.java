/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.util.Comparator;

import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;

/**
 * This interface defines an order for the fields of an entity.
 */
public interface IEntityListFieldOrder extends Comparator<EntityPropertyDescriptor> {
}
