package nl.thijsmolendijk.ollycode.ast.expression.binary;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.expression.BinaryExpression;

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
		public Minus(Expression l, Expression r) {
			super("-", l, r);
		}
	}
	
	/**
	 * Represents the {@code *} (MULT) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Multiply extends BinaryExpression {
		public Multiply(Expression l, Expression r) {
			super("*", l, r);
		}
	}
	
	/**
	 * Represents the {@code /} (DIVIDE) operator in OllyCode.
	 * @author molenzwiebel
	 */
	public static class Divide extends BinaryExpression {
		public Divide(Expression l, Expression r) {
			super("/", l, r);
		}
	}
}
