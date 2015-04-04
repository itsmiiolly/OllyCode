package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

/**
 * Container class for all numeric binary operations in OllyCode.
 * @author molenzwiebel
 */
public class NumericOperationExpressions {
	/**
	 * Represents the {@code -} (MINUS) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Minus extends BinaryExpression {
		public static final int PRECEDENCE = 15;
		
		public Minus(Expression l, Expression r) {
			super(l, "-", r);
		}
	}
	
	/**
	 * Represents the {@code *} (MULT) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Multiply extends BinaryExpression {
		public static final int PRECEDENCE = 20;
		
		public Multiply(Expression l, Expression r) {
			super(l, "*", r);
		}
	}
	
	/**
	 * Represents the {@code /} (DIVIDE) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Divide extends BinaryExpression {
		public static final int PRECEDENCE = 20;

		public Divide(Expression l, Expression r) {
			super(l, "/", r);
		}
	}
}
