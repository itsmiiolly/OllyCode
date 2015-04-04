package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

public class CallExpression implements Expression {
	public String methodName;
	public List<Expression> callArgs;
	
	public CallExpression(String name, List<Expression> args) {
		this.methodName = name;
		this.callArgs = args;
	}
	
	@Override
	public String toString() {
		return methodName + "(" + callArgs.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
	}
}
