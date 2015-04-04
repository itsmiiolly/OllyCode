package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import java.util.ArrayList;
import java.util.Arrays;

import nl.thijsmolendijk.ollycode.parsing.NodeParser.InfixNodeParser;
import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

public class BinaryExpression implements Expression {
	public Expression left;
	public Expression right;
	public String operation;
	
	public BinaryExpression(Expression left, String operation, Expression right) {
		this.left = left;
		this.right = right;
		this.operation = operation;
	}
	
	@Override
	public String toString() {
		return left + " " + operation + " " + right;
	}
	
	private static Expression parseGenericBinary(OCParser parser, Expression left, Class<? extends BinaryExpression> type, int precedence, Object... otherConstructorArgs) {
		parser.nextToken(); //consume the operator
		Expression right = parser.parseExpression(precedence);
		try {
			ArrayList<Class<?>> types = new ArrayList<>();
			for (Object obj : otherConstructorArgs) types.add(obj.getClass());
			types.add(Expression.class); //left
			types.add(Expression.class); //right
			
			ArrayList<Object> args = new ArrayList<>();
			args.addAll(Arrays.asList(otherConstructorArgs));
			args.add(left);
			args.add(right);
			
			return type.getConstructor(types.toArray(new Class<?>[2])).newInstance(args.toArray(new Object[2]));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static InfixNodeParser buildParser(Class<? extends BinaryExpression> type, int precedence, Object... otherConstructorArgs) {
		return (p, left) -> parseGenericBinary(p, left, type, precedence, otherConstructorArgs);
	}
}
