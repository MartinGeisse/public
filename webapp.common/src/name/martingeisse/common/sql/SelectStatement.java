/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.sql.build.ISqlBuilder;
import name.martingeisse.common.sql.expression.IExpression;

/**
 * Represents an SQL SELECT statement.
 */
public final class SelectStatement {

	/**
	 * the targets
	 */
	private List<ISelectTarget> targets = new ArrayList<ISelectTarget>();

	/**
	 * the primaryTableFetchSpecifier
	 */
	private IPrimaryTableFetchSpecifier primaryTableFetchSpecifier;

	/**
	 * the joinSpecifiers
	 */
	private List<IJoinSpecifier> joinSpecifiers = new ArrayList<IJoinSpecifier>();

	/**
	 * the conditions
	 */
	private List<IExpression> conditions = new ArrayList<IExpression>();

	/**
	 * the orderSteps
	 */
	private List<IOrderStep> orderSteps = new ArrayList<IOrderStep>();

	/**
	 * the offset
	 */
	private Integer offset;

	/**
	 * the limit
	 */
	private Integer limit;

	/**
	 * Constructor.
	 */
	public SelectStatement() {
	}

	/**
	 * Getter method for the targets.
	 * @return the targets
	 */
	public List<ISelectTarget> getTargets() {
		return targets;
	}

	/**
	 * Setter method for the targets.
	 * @param targets the targets to set
	 */
	public void setTargets(final List<ISelectTarget> targets) {
		this.targets = targets;
	}

	/**
	 * Getter method for the primaryTableFetchSpecifier.
	 * @return the primaryTableFetchSpecifier
	 */
	public IPrimaryTableFetchSpecifier getPrimaryTableFetchSpecifier() {
		return primaryTableFetchSpecifier;
	}

	/**
	 * Setter method for the primaryTableFetchSpecifier.
	 * @param primaryTableFetchSpecifier the primaryTableFetchSpecifier to set
	 */
	public void setPrimaryTableFetchSpecifier(final IPrimaryTableFetchSpecifier primaryTableFetchSpecifier) {
		this.primaryTableFetchSpecifier = primaryTableFetchSpecifier;
	}

	/**
	 * Getter method for the joinSpecifiers.
	 * @return the joinSpecifiers
	 */
	public List<IJoinSpecifier> getJoinSpecifiers() {
		return joinSpecifiers;
	}

	/**
	 * Setter method for the joinSpecifiers.
	 * @param joinSpecifiers the joinSpecifiers to set
	 */
	public void setJoinSpecifiers(final List<IJoinSpecifier> joinSpecifiers) {
		this.joinSpecifiers = joinSpecifiers;
	}

	/**
	 * Getter method for the conditions.
	 * @return the conditions
	 */
	public List<IExpression> getConditions() {
		return conditions;
	}

	/**
	 * Setter method for the conditions.
	 * @param conditions the conditions to set
	 */
	public void setConditions(final List<IExpression> conditions) {
		this.conditions = conditions;
	}

	/**
	 * Getter method for the orderSteps.
	 * @return the orderSteps
	 */
	public List<IOrderStep> getOrderSteps() {
		return orderSteps;
	}

	/**
	 * Setter method for the orderSteps.
	 * @param orderSteps the orderSteps to set
	 */
	public void setOrderSteps(final List<IOrderStep> orderSteps) {
		this.orderSteps = orderSteps;
	}

	/**
	 * Getter method for the offset.
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * Setter method for the offset.
	 * @param offset the offset to set
	 */
	public void setOffset(final Integer offset) {
		this.offset = offset;
	}

	/**
	 * Getter method for the limit.
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * Setter method for the limit.
	 * @param limit the limit to set
	 */
	public void setLimit(final Integer limit) {
		this.limit = limit;
	}

	/**
	 * Turns this select statement to an SQL string using the specified builder.
	 * 
	 * The builder should be empty, otherwise its contents appear at the
	 * beginning of the return value. The builder is left with the return
	 * value as its contents.
	 * 
	 * @param builder the builder to use
	 * @return the SQL string
	 */
	public String toString(final ISqlBuilder builder) {
		writeTo(builder);
		return builder.toString();
	}

	/**
	 * Turns this select statement to an SQL string using the specified builder.
	 * @param builder the builder to use
	 */
	public void writeTo(final ISqlBuilder builder) {
		builder.write("SELECT ");
		writeTargets(builder);
		writeFrom(primaryTableFetchSpecifier, builder);
		// TODO joins
		writeWhereClause(builder);
		writeOrderByClause(builder);
		writeOptionalIntegerClause(builder, "LIMIT", limit);
		writeOptionalIntegerClause(builder, "OFFSET", offset);
	}

	/**
	 * @param builder
	 */
	private void writeTargets(final ISqlBuilder builder) {
		if (targets.isEmpty()) {
			builder.write(" * ");
		} else {
			boolean first = true;
			for (final ISelectTarget target : targets) {
				if (first) {
					first = false;
				} else {
					builder.write(", ");
				}
				target.writeTo(builder);
			}
		}
	}

	/**
	 * @param builder
	 */
	private void writeFrom(final ITableFetchSpecifier table, final ISqlBuilder builder) {
		builder.write(" FROM ");
		builder.writeTableReference(table.getTableName());
		final String alias = table.getAlias();
		if (alias != null) {
			builder.write(" AS ");
			builder.writeTableAliasDeclaration(alias);
		}
	}

	/**
	 * @param builder
	 */
	private void writeWhereClause(final ISqlBuilder builder) {
		boolean first = true;
		for (final IExpression condition : conditions) {
			if (first) {
				first = false;
				builder.write(" WHERE ");
			} else {
				builder.write(" AND ");
			}
			condition.writeTo(builder);
		}
	}

	/**
	 * @param builder
	 */
	private void writeOrderByClause(final ISqlBuilder builder) {
		boolean first = true;
		for (final IOrderStep orderStep : orderSteps) {
			if (first) {
				first = false;
				builder.write(" ORDER BY ");
			} else {
				builder.write(", ");
			}
			orderStep.writeTo(builder);
		}
	}
	
	/**
	 * @param prefix
	 * @param value
	 */
	private void writeOptionalIntegerClause(final ISqlBuilder builder, String prefix, Integer value) {
		if (value != null) {
			builder.write(" ");
			builder.write(prefix);
			builder.write(" ");
			builder.write(value.toString());
			builder.write(" ");
		}
	}

}
