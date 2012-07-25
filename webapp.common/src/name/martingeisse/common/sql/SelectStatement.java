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
		// TODO ORDER BY
		// TODO GROUP BY
		// TODO OFFSET, LIMIT
	}

	/**
	 * @param builder
	 */
	private void writeTargets(final ISqlBuilder builder) {
		if (targets.isEmpty()) {
			builder.write(" * ");
		} else {
			boolean first = true;
			for (ISelectTarget target : targets) {
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
		for (IExpression condition : conditions) {
			if (first) {
				first = false;
				builder.write(" WHERE ");
			} else {
				builder.write(" AND ");
			}
			condition.writeTo(builder);
		}
	}

}
