/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import java.util.Arrays;
import java.util.List;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.util.ParameterUtil;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;

/**
 * Utility methods used by entity list filters and their clients.
 * 
 * All methods that do not take an explicit entity alias use {@link EntityDescriptor#ALIAS}.
 * An alias must be present since QueryDSL demands it, i.e. queries cannot refer to
 * fields without a table or alias prefix.
 */
public class EntityExpressionUtil {

	/**
	 * Prevent instantiation.
	 */
	private EntityExpressionUtil() {
	}

	/**
	 * Creates and returns a {@link Path} for an entity, using the
	 * default alias {@link EntityDescriptor#ALIAS}.
	 * 
	 * @return the path for the entity
	 */
	public static Path<Object> entityPath() {
		return entityPath(EntityDescriptor.ALIAS);
	}

	/**
	 * Creates and returns a {@link Path} for an entity, using the
	 * specified entity alias.
	 * 
	 * @param entityAlias the alias for the entity
	 * @return the path for the entity
	 */
	public static Path<Object> entityPath(String entityAlias) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		return Expressions.path(Object.class, entityAlias);
	}

	/**
	 * Creates and returns an untyped {@link Path} for the specified field of an
	 * entity, using the default entity alias {@link EntityDescriptor#ALIAS}.
	 * 
	 * @param fieldName the name of the field
	 * @return the path for the field
	 */
	public static Path<Object> fieldPath(final String fieldName) {
		return fieldPath(EntityDescriptor.ALIAS, fieldName);
	}

	/**
	 * Creates and returns an untyped {@link Path} for the specified field of an
	 * entity, using the specified entity alias.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldName the name of the field
	 * @return the path for the field
	 */
	public static Path<Object> fieldPath(final String entityAlias, final String fieldName) {
		return fieldPath(entityAlias, Object.class, fieldName);
	}

	/**
	 * Creates and returns a typed {@link Path} for the specified field of
	 * an entity, using the default entity alias {@link EntityDescriptor#ALIAS}.
	 * 
	 * @param fieldType the class object for the field type
	 * @param fieldName the name of the field
	 * @param <T> the static field type
	 * @return the path for the field
	 */
	public static <T> Path<T> fieldPath(final Class<T> fieldType, final String fieldName) {
		return fieldPath(EntityDescriptor.ALIAS, fieldType, fieldName);
	}

	/**
	 * Creates and returns a typed {@link Path} for the specified field of
	 * an entity, using the specified entity alias.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldType the class object for the field type
	 * @param fieldName the name of the field
	 * @param <T> the static field type
	 * @return the path for the field
	 */
	public static <T> Path<T> fieldPath(final String entityAlias, final Class<T> fieldType, final String fieldName) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		ParameterUtil.ensureNotNull(fieldType, "fieldType");
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		if (entityAlias == null) {
			return Expressions.path(fieldType, fieldName);
		} else {
			return Expressions.path(fieldType, Expressions.path(Object.class, entityAlias), fieldName);
		}
	}

	/**
	 * Creates and returns a new (entity.fieldName IS NULL) predicate.
	 * 
	 * @param fieldName the name of the field to compare with NULL
	 * @return the predicate
	 */
	public static final Predicate fieldIsNull(final String fieldName) {
		return fieldIsNull(EntityDescriptor.ALIAS, fieldName);
	}

	/**
	 * Creates and returns a new (entity.fieldName IS NULL) predicate.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldName the name of the field to compare with NULL
	 * @return the predicate
	 */
	public static final Predicate fieldIsNull(final String entityAlias, final String fieldName) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		return Expressions.predicate(Ops.IS_NULL, fieldPath(entityAlias, fieldName));
	}
	
	/**
	 * Creates and returns a new (entity.fieldName IS NOT NULL) predicate.
	 * 
	 * @param fieldName the name of the field to compare with NULL
	 * @return the predicate
	 */
	public static final Predicate fieldIsNotNull(final String fieldName) {
		return fieldIsNotNull(EntityDescriptor.ALIAS, fieldName);
	}
	
	/**
	 * Creates and returns a new (entity.fieldName IS NOT NULL) predicate.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldName the name of the field to compare with NULL
	 * @return the predicate
	 */
	public static final Predicate fieldIsNotNull(final String entityAlias, final String fieldName) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		return Expressions.predicate(Ops.IS_NOT_NULL, fieldPath(entityAlias, fieldName));
	}

	/**
	 * Creates and returns a new (entity.fieldName (operator) expectedValue) predicate.
	 * The right-hand value is a literal (constant) -- create and add a custom predicate
	 * to compare with arbitrary expressions.
	 * 
	 * @param fieldName the name of the field to compare
	 * @param operator the comparison operator
	 * @param rightValue the right-hand value to compare with
	 * @return the predicate
	 */
	public static final Predicate fieldComparison(final String fieldName, final Operator<Boolean> operator, final Object rightValue) {
		return fieldComparison(EntityDescriptor.ALIAS, fieldName, operator, rightValue);
	}

	/**
	 * Creates and returns a new (entity.fieldName (operator) expectedValue) predicate.
	 * The right-hand value is a literal (constant) -- create and add a custom predicate
	 * to compare with arbitrary expressions.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldName the name of the field to compare
	 * @param operator the comparison operator
	 * @param rightValue the right-hand value to compare with
	 * @return the predicate
	 */
	public static final Predicate fieldComparison(final String entityAlias, final String fieldName, final Operator<Boolean> operator, final Object rightValue) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		ParameterUtil.ensureNotNull(operator, "operator");
		ParameterUtil.ensureNotNull(rightValue, "rightValue");
		return Expressions.predicate(operator, fieldPath(entityAlias, fieldName), Expressions.constant(rightValue));
	}

	/**
	 * Creates and returns a new (entity.fieldName = expectedValue) predicate.
	 * The expected value is a literal (constant) -- create and add a custom predicate
	 * to compare with arbitrary expressions.
	 * 
	 * This method supports comparing with NULL as the right-hand expression and
	 * will turn it into an "IS NULL" expression.
	 * 
	 * @param fieldName the name of the field to compare
	 * @param expectedValue the value to compare with (can be null to test for NULL)
	 * @return the predicate
	 */
	public static final Predicate fieldEquals(final String fieldName, final Object expectedValue) {
		return fieldEquals(EntityDescriptor.ALIAS, fieldName, expectedValue);
	}

	/**
	 * Creates and returns a new (entity.fieldName = expectedValue) predicate.
	 * The expected value is a literal (constant) -- create and add a custom predicate
	 * to compare with arbitrary expressions.
	 * 
	 * This method supports comparing with NULL as the right-hand expression and
	 * will turn it into an "IS NULL" expression.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldName the name of the field to compare
	 * @param expectedValue the value to compare with (can be null to test for NULL)
	 * @return the predicate
	 */
	public static final Predicate fieldEquals(final String entityAlias, final String fieldName, final Object expectedValue) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		if (expectedValue == null) {
			return fieldIsNull(entityAlias, fieldName);
		} else {
			return fieldComparison(entityAlias, fieldName, Ops.EQ, expectedValue);
		}
	}

	/**
	 * Creates and returns a new (entity.fieldName != notExpectedValue) predicate.
	 * The not-expected value is a literal (constant) -- create and add a custom predicate
	 * to compare with arbitrary expressions.
	 * 
	 * This method supports comparing with NULL as the right-hand expression and
	 * will turn it into an "IS NOT NULL" expression.
	 * 
	 * @param fieldName the name of the field to compare
	 * @param notExpectedValue the value to compare with (can be null to test for NULL)
	 * @return the predicate
	 */
	public static final Predicate fieldNotEquals(final String fieldName, final Object notExpectedValue) {
		return fieldNotEquals(EntityDescriptor.ALIAS, fieldName, notExpectedValue);
	}

	/**
	 * Creates and returns a new (entity.fieldName != notExpectedValue) predicate.
	 * The not-expected value is a literal (constant) -- create and add a custom predicate
	 * to compare with arbitrary expressions.
	 * 
	 * This method supports comparing with NULL as the right-hand expression and
	 * will turn it into an "IS NOT NULL" expression.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldName the name of the field to compare
	 * @param notExpectedValue the value to compare with (can be null to test for NULL)
	 * @return the predicate
	 */
	public static final Predicate fieldNotEquals(final String entityAlias, final String fieldName, final Object notExpectedValue) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		if (notExpectedValue == null) {
			return fieldIsNotNull(entityAlias, fieldName);
		} else {
			return fieldComparison(entityAlias, fieldName, Ops.NE, notExpectedValue);
		}
	}

	/**
	 * Creates and returns a new (entity.fieldName IN (expectedValues)) predicate.
	 * The expected values are literals (constants). Note that null is not allowed
	 * in the expectedValues.
	 * 
	 * @param fieldName the name of the field to compare
	 * @param expectedValues the values to compare with
	 * @return the predicate
	 */
	public static final Predicate fieldIn(final String fieldName, final Object... expectedValues) {
		return fieldIn(EntityDescriptor.ALIAS, fieldName, expectedValues);
	}

	/**
	 * Creates and returns a new (entity.fieldName IN (expectedValues)) predicate.
	 * The expected values are literals (constants). Note that null is not allowed
	 * in the expectedValues.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldName the name of the field to compare
	 * @param expectedValues the values to compare with
	 * @return the predicate
	 */
	public static final Predicate fieldIn(final String entityAlias, final String fieldName, final Object... expectedValues) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		ParameterUtil.ensureNotNull(expectedValues, "expectedValues");
		ParameterUtil.ensureNoNullElement(expectedValues, "expectedValues");
		return BooleanOperation.create(Ops.IN, fieldPath(entityAlias, fieldName), new ConstantImpl<List<?>>(Arrays.asList(expectedValues)));
	}

	/**
	 * Creates and returns a new (entity.fieldName NOT IN (notExpectedValues)) predicate.
	 * The not-expected values are literals (constants). Note that null is not allowed
	 * in the expectedValues.
	 * 
	 * @param fieldName the name of the field to compare
	 * @param notExpectedValues the values to compare with
	 * @return the predicate
	 */
	public static final Predicate fieldNotIn(final String fieldName, final Object... notExpectedValues) {
		return fieldNotIn(EntityDescriptor.ALIAS, fieldName, notExpectedValues);
	}

	/**
	 * Creates and returns a new (entity.fieldName NOT IN (notExpectedValues)) predicate.
	 * The not-expected values are literals (constants). Note that null is not allowed
	 * in the expectedValues.
	 * 
	 * @param entityAlias the alias for the entity
	 * @param fieldName the name of the field to compare
	 * @param notExpectedValues the values to compare with
	 * @return the predicate
	 */
	public static final Predicate fieldNotIn(final String entityAlias, final String fieldName, final Object... notExpectedValues) {
		ParameterUtil.ensureNotNull(entityAlias, "entityAlias");
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		ParameterUtil.ensureNotNull(notExpectedValues, "notExpectedValues");
		ParameterUtil.ensureNoNullElement(notExpectedValues, "notExpectedValues");
		return BooleanOperation.create(Ops.NOT, BooleanOperation.create(Ops.IN, fieldPath(entityAlias, fieldName), new ConstantImpl<List<?>>(Arrays.asList(notExpectedValues))));
	}
	
}
