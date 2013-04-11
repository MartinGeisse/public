/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

/**
 * Various utility instances of {@link IIntPredicate}.
 */
public class IntPredicates {

	/**
	 * Compares the subject value to a constant and returns true only if the
	 * subject value is equal to the constant.
	 */
	public static class Equal extends CompareToConstantPredicate {

		/**
		 * Constructor
		 * @param constant the constant to compare to
		 */
		public Equal(int constant) {
			super(constant);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.common.util.IntPredicates.CompareToConstantPredicate#compare(int, int)
		 */
		@Override
		protected boolean compare(int subjectValue, int constant) {
			return subjectValue == constant;
		}

	}

	/**
	 * Compares the subject value to a constant and returns true only if the
	 * subject value is not equal to the constant.
	 */
	public static class NotEqual extends CompareToConstantPredicate {

		/**
		 * Constructor
		 * @param constant the constant to compare to
		 */
		public NotEqual(int constant) {
			super(constant);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.common.util.IntPredicates.CompareToConstantPredicate#compare(int, int)
		 */
		@Override
		protected boolean compare(int subjectValue, int constant) {
			return subjectValue != constant;
		}

	}

	/**
	 * Compares the subject value to a constant and returns true only if the
	 * subject value is less than the constant.
	 */
	public static class Less extends CompareToConstantPredicate {

		/**
		 * Constructor
		 * @param constant the constant to compare to
		 */
		public Less(int constant) {
			super(constant);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.common.util.IntPredicates.CompareToConstantPredicate#compare(int, int)
		 */
		@Override
		protected boolean compare(int subjectValue, int constant) {
			return subjectValue < constant;
		}

	}

	/**
	 * Compares the subject value to a constant and returns true only if the
	 * subject value is less than or equal to the constant.
	 */
	public static class LessEqual extends CompareToConstantPredicate {

		/**
		 * Constructor
		 * @param constant the constant to compare to
		 */
		public LessEqual(int constant) {
			super(constant);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.common.util.IntPredicates.CompareToConstantPredicate#compare(int, int)
		 */
		@Override
		protected boolean compare(int subjectValue, int constant) {
			return subjectValue <= constant;
		}

	}

	/**
	 * Compares the subject value to a constant and returns true only if the
	 * subject value is greater than the constant.
	 */
	public static class Greater extends CompareToConstantPredicate {

		/**
		 * Constructor
		 * @param constant the constant to compare to
		 */
		public Greater(int constant) {
			super(constant);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.common.util.IntPredicates.CompareToConstantPredicate#compare(int, int)
		 */
		@Override
		protected boolean compare(int subjectValue, int constant) {
			return subjectValue > constant;
		}

	}

	/**
	 * Compares the subject value to a constant and returns true only if the
	 * subject value is greater than or equal to the constant.
	 */
	public static class GreaterEqual extends CompareToConstantPredicate {

		/**
		 * Constructor
		 * @param constant the constant to compare to
		 */
		public GreaterEqual(int constant) {
			super(constant);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.common.util.IntPredicates.CompareToConstantPredicate#compare(int, int)
		 */
		@Override
		protected boolean compare(int subjectValue, int constant) {
			return subjectValue >= constant;
		}

	}

	/**
	 * Abstract predicate implementation for predicates that compare the subject
	 * value to a constant value.
	 */
	public static abstract class CompareToConstantPredicate implements IIntPredicate {

		/**
		 * the constant
		 */
		private int constant;

		/**
		 * Constructor
		 * @param constant the constant to which the subject value is compared
		 */
		public CompareToConstantPredicate(int constant) {
			this.constant = constant;
		}

		/**
		 * Actual implementation of the predicate.
		 * @param subjectValue the subject value
		 * @param constant the constant to which the subject value is compared
		 * @return Returns the result of the predicate.
		 */
		protected abstract boolean compare(int subjectValue, int constant);

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.common.util.IIntPredicate#evaluate(int)
		 */
		@Override
		public boolean evaluate(int value) {
			return compare(value, constant);
		}

	}
}
