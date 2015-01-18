package nl.thijsmolendijk.ollycode.ast.expression.binary;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.expression.BinaryExpression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCNumber;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

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

		@Override
		public OCObject eval(Interpreter interpreter) {
			OCObject leftResult = left.eval(interpreter);
			OCObject rightResult = right.eval(interpreter);
			
			if (!leftResult.isNumber() || !rightResult.isNumber()) throw new RuntimeException("Expected two numbers for -");
			return new OCNumber(leftResult.toNumber().doubleValue() - rightResult.toNumber().doubleValue());
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

		@Override
		public OCObject eval(Interpreter interpreter) {
			OCObject leftResult = left.eval(interpreter);
			OCObject rightResult = right.eval(interpreter);
			
			if (!leftResult.isNumber() || !rightResult.isNumber()) throw new RuntimeException("Expected two numbers for *");
			return new OCNumber(leftResult.toNumber().doubleValue() * rightResult.toNumber().doubleValue());
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

		@Override
		public OCObject eval(Interpreter interpreter) {
			OCObject leftResult = left.eval(interpreter);
			OCObject rightResult = right.eval(interpreter);
			
			if (!leftResult.isNumber() || !rightResult.isNumber()) throw new RuntimeException("Expected two numbers for /");
			return new OCNumber(leftResult.toNumber().doubleValue() / rightResult.toNumber().doubleValue());
		}
	}
}
