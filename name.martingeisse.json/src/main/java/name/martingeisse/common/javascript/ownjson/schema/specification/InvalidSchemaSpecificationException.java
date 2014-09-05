/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.schema.specification;

/**
 * Signals an error that was found in the schema specification while
 * building a schema object from it. This exception does not describe
 * the error -- use the meta-schema for that. 
 */
public class InvalidSchemaSpecificationException extends Exception {
}