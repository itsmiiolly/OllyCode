package nl.thijsmolendijk.ollycode.ast.expression.binary;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.expression.BinaryExpression;

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
		public And(Expression l, Expression r) {
			super("&&", l, r);
		}
	}
	
	/**
	 * Represents the {@code ||} (OR) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Or extends BinaryExpression {
		public Or(Expression l, Expression r) {
			super("||", l, r);
		}
	}
	
	/**
	 * Represents the {@code ==} (EQUAL) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Compare extends BinaryExpression {
		private boolean returnTrueIfEqual;
		
		public Compare(boolean returnTrueIfEqual, Expression l, Expression r) {
			super("==", l, r);
			this.returnTrueIfEqual = returnTrueIfEqual;
		}
	}
	
	/**
	 * Represents the {@code >} and {@code >=} (GT, GT_EQ) operators in OllyCode.
	 * @author molenzwiebel
	 */
	public static class GreaterThan extends BinaryExpression {
		private boolean allowsEqual;
		
		public GreaterThan(boolean eq, Expression l, Expression r) {
			super(">", l, r);
			this.allowsEqual = eq;
		}
	}
	
	/**
	 * Represents the {@code <} and {@code <=} (LT, LT_EQ) operators in OllyCode.
	 * @author molenzwiebel
	 */
	public static class LesserThan extends BinaryExpression {
		private boolean allowsEqual;
		
		public LesserThan(boolean eq, Expression l, Expression r) {
			super(">", l, r);
			this.allowsEqual = eq;
		}
	}
}
