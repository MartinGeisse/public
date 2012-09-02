/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.template.BooleanTemplate;

/**
 * Utility class to build a condition predicate for an
 * entity to fetch.
 */
public class EntityConditions implements Predicate, Cloneable, Operation<Boolean> {

	/**
	 * the entityPath
	 */
	private final Path<?> entityPath;

	/**
	 * the buildOperator
	 */
	private final Operator<Boolean> buildOperator;

	/**
	 * the predicate
	 */
	private Predicate predicate;

	/**
	 * Constructor using the default entity filter alias ({@link EntityExpressionUtil#ALIAS}).
	 */
	public EntityConditions() {
		this(Expressions.path(Object.class, EntityExpressionUtil.ALIAS), Ops.AND);
	}

	/**
	 * Constructor.
	 * @param entityPath the path to the entity to build a predicate for
	 */
	public EntityConditions(final Path<?> entityPath) {
		this(entityPath, Ops.AND);
	}

	/**
	 * Constructor using the default entity filter alias ({@link EntityExpressionUtil#ALIAS}).
	 * @param buildOperator the operator used to combine expressions
	 */
	public EntityConditions(final Operator<Boolean> buildOperator) {
		this(Expressions.path(Object.class, EntityExpressionUtil.ALIAS), buildOperator);
	}

	/**
	 * Constructor.
	 * @param entityPath the path to the entity to build a predicate for
	 * @param buildOperator the operator used to combine expressions
	 */
	public EntityConditions(final Path<?> entityPath, final Operator<Boolean> buildOperator) {
		if (entityPath == null) {
			throw new IllegalArgumentException("entityPath is null");
		}
		if (buildOperator != Ops.AND && buildOperator != Ops.OR && buildOperator != Ops.XOR) {
			throw new IllegalArgumentException("invalid operator for EntityCondition: " + buildOperator);
		}
		this.entityPath = entityPath;
		this.buildOperator = buildOperator;
		this.predicate = null;
	}

	/**
	 * Getter method for the entityPath.
	 * @return the entityPath
	 */
	public final Path<?> getEntityPath() {
		return entityPath;
	}

	/**
	 * Getter method for the buildOperator.
	 * @return the buildOperator
	 */
	public final Operator<Boolean> getBuildOperator() {
		return buildOperator;
	}

	/**
	 * Getter method for the predicate.
	 * @return the predicate
	 */
	public final Predicate getPredicate() {
		return predicate;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.types.Expression#accept(com.mysema.query.types.Visitor, java.lang.Object)
	 */
	@Override
	public final <R, C> R accept(final Visitor<R, C> v, final C context) {
		return v.visit(this, context);
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.types.Expression#getType()
	 */
	@Override
	public final Class<? extends Boolean> getType() {
		return Boolean.class;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.types.Operation#getOperator()
	 */
	@Override
	public final Operator<? super Boolean> getOperator() {
		return Ops.WRAPPED;
	}

	/**
	 * @return the predicate that has the same effect as this condition object
	 * in its current state,
	 */
	public final Predicate getEffectivePredicate() {
		if (predicate != null) {
			return predicate;
		}
		if (buildOperator == Ops.AND) {
			return BooleanTemplate.TRUE;
		} else {
			return BooleanTemplate.FALSE;
		}
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.types.Operation#getArg(int)
	 */
	@Override
	public final Expression<?> getArg(final int index) {
		if (index != 0) {
			throw new IndexOutOfBoundsException();
		}
		return getEffectivePredicate();
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.types.Operation#getArgs()
	 */
	@Override
	public final List<Expression<?>> getArgs() {
		return Arrays.<Expression<?>> asList(getEffectivePredicate());
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.types.Predicate#not()
	 */
	@Override
	public final Predicate not() {
		return getEffectivePredicate().not();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected final EntityConditions clone() throws CloneNotSupportedException {
		return (EntityConditions)super.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof EntityConditions) {
			final EntityConditions other = (EntityConditions)obj;
			return Objects.equal(other.getEntityPath(), entityPath) && Objects.equal(other.getBuildOperator(), buildOperator) && Objects.equal(other.getPredicate(), predicate);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return entityPath.hashCode() + buildOperator.hashCode() + (predicate == null ? 0 : predicate.hashCode());
	}

	/**
	 * Adds a new predicate and combines it with the existing predicates using the
	 * operator specified at construction.
	 * @param predicate the predicate to add
	 */
	public final void add(final Predicate predicate) {
		if (predicate != null) {
			if (this.predicate == null) {
				this.predicate = predicate;
			} else {
				this.predicate = BooleanOperation.create(buildOperator, this.predicate, predicate);
			}
		}
	}

	/**
	 * Creates and returns an untyped path to a field of the entity.
	 * @param fieldName the name of the field
	 * @return the path
	 */
	public final Path<?> createFieldPath(final String fieldName) {
		return Expressions.path(Object.class, entityPath, fieldName);
	}

	/**
	 * Creates and returns a typed path to a field of the entity.
	 * @param <T> the static field type
	 * @param type the class object for the field type
	 * @param fieldName the name of the field
	 * @return the path
	 */
	public final <T> Path<T> createFieldPath(final Class<T> type, final String fieldName) {
		return Expressions.path(type, entityPath, fieldName);
	}

	/**
	 * Adds a new (entity.fieldName IS NULL) predicate and combines it with the existing
	 * predicates using the operator specified at construction.
	 * @param fieldName the name of the field to compare with NULL
	 */
	public final void addFieldIsNull(final String fieldName) {
		add(Expressions.predicate(Ops.IS_NULL, createFieldPath(fieldName)));
	}

	/**
	 * Adds a new (entity.fieldName IS NOT NULL) predicate and combines it with the existing
	 * predicates using the operator specified at construction.
	 * @param fieldName the name of the field to compare with NULL
	 */
	public final void addFieldIsNotNull(final String fieldName) {
		add(Expressions.predicate(Ops.IS_NOT_NULL, createFieldPath(fieldName)));
	}
	
	/**
	 * Adds a new (entity.fieldName (operator) expectedValue) predicate and combines it with the existing
	 * predicates using the operator specified at construction. The right-hand value is a literal
	 * (constant) -- create and add a custom predicate to compare with arbitrary expressions.
	 * @param fieldName the name of the field to compare
	 * @param operator the comparison operator
	 * @param rightValue the right-hand value to compare with
	 */
	public final void addFieldComparison(final String fieldName, final Operator<Boolean> operator, final Object rightValue) {
		add(Expressions.predicate(operator, createFieldPath(fieldName), Expressions.constant(rightValue)));
	}

	/**
	 * Adds a new (entity.fieldName = expectedValue) predicate and combines it with the existing
	 * predicates using the operator specified at construction. The expected value is a literal
	 * (constant) -- create and add a custom predicate to compare with arbitrary expressions.
	 * 
	 * @param fieldName the name of the field to compare
	 * @param expectedValue the value to compare with
	 */
	public final void addFieldEquals(final String fieldName, final Object expectedValue) {
		if (expectedValue == null) {
			addFieldIsNull(fieldName);
		} else {
			addFieldComparison(fieldName, Ops.EQ, expectedValue);
		}
	}

	/**
	 * Adds a new (entity.fieldName != notExpectedValue) predicate and combines it with the existing
	 * predicates using the operator specified at construction. The not-expected value is a literal
	 * (constant) -- create and add a custom predicate to compare with arbitrary expressions.
	 * 
	 * @param fieldName the name of the field to compare
	 * @param notExpectedValue the value to compare with
	 */
	public final void addFieldNotEquals(final String fieldName, final Object notExpectedValue) {
		if (notExpectedValue == null) {
			addFieldIsNotNull(fieldName);
		} else {
			addFieldComparison(fieldName, Ops.NE, notExpectedValue);
		}
	}

	/**
	 * Adds a new (entity.fieldName IN (expectedValues)) predicate and combines it with the existing
	 * predicates using the operator specified at construction. The expected values are literals
	 * (constants).
	 * 
	 * @param fieldName the name of the field to compare
	 * @param expectedValues the values to compare with
	 */
	public final void addFieldIn(final String fieldName, final Object... expectedValues) {
		add(BooleanOperation.create(Ops.IN, createFieldPath(fieldName), new ConstantImpl<List<?>>(Arrays.asList(expectedValues))));
	}

	/**
	 * Adds a new (entity.fieldName NOT IN (notExpectedValues)) predicate and combines it with the existing
	 * predicates using the operator specified at construction. The not-expected values are literals
	 * (constants).
	 * 
	 * @param fieldName the name of the field to compare
	 * @param notExpectedValues the values to compare with
	 */
	public final void addFieldNotIn(final String fieldName, final Object... notExpectedValues) {
		add(BooleanOperation.create(Ops.NOT, BooleanOperation.create(Ops.IN, createFieldPath(fieldName), new ConstantImpl<List<?>>(Arrays.asList(notExpectedValues)))));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getEffectivePredicate().toString();
	}
	
}
