package nl.thijsmolendijk.ollycode.ast.expression.binary;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.expression.BinaryExpression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCBoolean;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

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

		@Override
		public OCObject eval(Interpreter interpreter) {
			OCObject leftResult = left.eval(interpreter);
			OCObject rightResult = right.eval(interpreter);
			
			if (!leftResult.isBoolean() || !rightResult.isBoolean()) throw new RuntimeException("Expected two booleans for &&");
			return OCBoolean.valueOf(leftResult.toBoolean().booleanValue() && rightResult.toBoolean().booleanValue());
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

		@Override
		public OCObject eval(Interpreter interpreter) {
			OCObject leftResult = left.eval(interpreter);
			OCObject rightResult = right.eval(interpreter);
			
			if (!leftResult.isBoolean() || !rightResult.isBoolean()) throw new RuntimeException("Expected two booleans for &&");
			return OCBoolean.valueOf(leftResult.toBoolean().booleanValue() || rightResult.toBoolean().booleanValue());
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

		@Override
		public OCObject eval(Interpreter interpreter) {
			OCObject leftResult = left.eval(interpreter);
			OCObject rightResult = right.eval(interpreter);
			OCBoolean res = OCBoolean.valueOf(leftResult.equals(rightResult));
			return returnTrueIfEqual ? res : res.not();
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

		@Override
		public OCObject eval(Interpreter interpreter) {
			OCObject leftResult = left.eval(interpreter);
			OCObject rightResult = right.eval(interpreter);
			
			if (!leftResult.isNumber() || !rightResult.isNumber()) throw new RuntimeException("Expected two numbers for > and >=");
			return allowsEqual ? OCBoolean.valueOf(leftResult.toNumber().doubleValue() >= rightResult.toNumber().doubleValue()) : OCBoolean.valueOf(leftResult.toNumber().doubleValue() > rightResult.toNumber().doubleValue());
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
		
		@Override
		public OCObject eval(Interpreter interpreter) {
			OCObject leftResult = left.eval(interpreter);
			OCObject rightResult = right.eval(interpreter);
			
			if (!leftResult.isNumber() || !rightResult.isNumber()) throw new RuntimeException("Expected two numbers for < and <=");
			return allowsEqual ? OCBoolean.valueOf(leftResult.toNumber().doubleValue() <= rightResult.toNumber().doubleValue()) : OCBoolean.valueOf(leftResult.toNumber().doubleValue() < rightResult.toNumber().doubleValue());
		}
	}
}
