package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

/**
 * Container class for all numeric binary operations in OllyCode.
 * @author molenzwiebel
 */
public class ValueComparisonExpressions {
	/**
	 * Represents the {@code &&} (AND) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class And extends BinaryExpression {
		public static final int PRECEDENCE = 20;

		public And(Expression l, Expression r) {
			super(l, "&&", r);
		}
	}
	
	/**
	 * Represents the {@code ||} (OR) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Or extends BinaryExpression {
		public static final int PRECEDENCE = 20;

		public Or(Expression l, Expression r) {
			super(l, "||", r);
		}
	}
	
	/**
	 * Represents the {@code ==} (EQUAL) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Compare extends BinaryExpression {
		public static final int PRECEDENCE = 20;
		public boolean returnTrueIfEqual;
		
		public Compare(Boolean returnTrueIfEqual, Expression l, Expression r) {
			super(l, returnTrueIfEqual ? "==" : "!=", r);
			this.returnTrueIfEqual = returnTrueIfEqual;
		}
	}
	
	/**
	 * Represents the {@code >} and {@code >=} (GT, GT_EQ) operators in OllyCode.
	 * @author molenzwiebel
	 */
	public static class GreaterThan extends BinaryExpression {
		public static final int PRECEDENCE = 20;
		public boolean allowsEqual;
		
		public GreaterThan(Boolean eq, Expression l, Expression r) {
			super(l, eq ? ">=" : ">", r);
			this.allowsEqual = eq;
		}
	}
	
	/**
	 * Represents the {@code <} and {@code <=} (LT, LT_EQ) operators in OllyCode.
	 * @author molenzwiebel
	 */
	public static class LesserThan extends BinaryExpression {
		public static final int PRECEDENCE = 20;
		public boolean allowsEqual;
		
		public LesserThan(Boolean eq, Expression l, Expression r) {
			super(l, eq ? "<=" : "<", r);
			this.allowsEqual = eq;
		}
	}
}
