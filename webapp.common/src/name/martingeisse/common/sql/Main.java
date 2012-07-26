/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

import name.martingeisse.common.sql.build.SqlBuilderForMySql;
import name.martingeisse.common.sql.expression.ColumnReference;
import name.martingeisse.common.sql.expression.InIntegerArrayExpression;

/**
 * TODO: remove me
 */
public class Main {

	/**
	 * @param args ...
	 */
	public static void main(String[] args) {

		SelectStatement statement = new SelectStatement();
		statement.getTargets().add(new CountAllTarget());
		statement.getTargets().add(new CountAllTarget());
		statement.setPrimaryTableFetchSpecifier(new PrimaryTableFetchSpecifier("phpbb_forums", "Forum"));
//		statement.getConditions().add(new BinaryExpression(new ColumnReference("Forum", "id"), BinaryOperator.GREATER_EQUAL, new IntegerLiteral(50)));
		statement.getConditions().add(new InIntegerArrayExpression(new ColumnReference("Forum", "id"), 1, 2));
		System.out.println(statement.toString(new SqlBuilderForMySql()));
		
	}
}
