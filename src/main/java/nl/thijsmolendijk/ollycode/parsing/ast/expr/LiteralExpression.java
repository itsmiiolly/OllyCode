package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import nl.thijsmolendijk.ollycode.lexer.OCTokenType;
import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

public class LiteralExpression {
	public static class Boolean implements Expression {
		public boolean value;

		public Boolean(boolean value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "" + value;
		}
		
		public static Expression parse(OCParser p) {
			return new Boolean(p.consume().isType(OCTokenType.TRUE));
		}
	}

	public static class Number implements Expression {
		public double value;

		public Number(double value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "" + value;
		}
		
		public static Expression parse(OCParser p) {
			return new Number(p.consume().getValue());
		}
	}

	public static class Text implements Expression {
		public String value;

		public Text(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
		
		public static Expression parse(OCParser p) {
			return new Text(p.consume().getValue());
		}
	}
	
	public static class Null implements Expression {
		@Override
		public String toString() {
			return "null";
		}
		
		public static Expression parse(OCParser p) {
			p.nextToken(); //Consume null keyword
			return new Null();
		}
	}
}
