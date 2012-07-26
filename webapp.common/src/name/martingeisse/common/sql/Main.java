/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

import java.util.Arrays;
import java.util.List;

import name.martingeisse.common.sql.build.SqlBuilderForMySql;
import name.martingeisse.common.sql.expression.BinaryExpression;
import name.martingeisse.common.sql.expression.ColumnReference;
import name.martingeisse.common.sql.expression.IExpression;

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
//		statement.getConditions().add(new InIntegerArrayExpression(new ColumnReference("Forum", "id"), 1, 2));
		
		List<IExpression> expressions = Arrays.<IExpression>asList(
//			new ColumnReference("Forum", "aaa"),
//			new ColumnReference("Forum", "bbb"),
			new ColumnReference("Forum", "ccc"),
			new ColumnReference("Forum", "ddd")
		);
		statement.getConditions().add(BinaryExpression.createAndChain(expressions));
		
		System.out.println(statement.toString(new SqlBuilderForMySql()));
		
	}
}
