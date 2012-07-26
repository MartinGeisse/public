/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import java.util.List;

/**
 * Operators of this type can be used in {@link MultiCondition}s.
 */
public enum MultiConditionOperator {

	/**
	 * AND-combines the sub-expressions, with TRUE being the
	 * default if no expression is registered.
	 */
	AND {
		@Override
		public IExpression createReplacementExpression(List<IExpression> subExpressions) {
			return BinaryExpression.createAndChain(subExpressions);
		}
	},
	
	/**
	 * OR-combines the sub-expressions, with FALSE being the
	 * default if no expression is registered.
	 */
	OR {
		@Override
		public IExpression createReplacementExpression(List<IExpression> subExpressions) {
			return BinaryExpression.createOrChain(subExpressions);
		}
	};
	
	/**
	 * Constructor.
	 */
	private MultiConditionOperator() {
	}

	/**
	 * Creates a replacement expressions for the {@link MultiCondition}.
	 * @param subExpressions the sub-expressions of the {@link MultiCondition}.
	 * @return the replacement expressions
	 */
	public abstract IExpression createReplacementExpression(List<IExpression> subExpressions);
	
}
