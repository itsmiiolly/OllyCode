package nl.thijsmolendijk.ollycode.ast.expression;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;

public class FunctionCallExpression implements Expression {
	public String functionName;
	public List<Expression> arguments;
	
	public FunctionCallExpression(String name, List<Expression> args) {
		this.functionName = name;
		this.arguments = args;
	}
	
	@Override
	public String toString() {
		return functionName + "(" + arguments.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
	}

	@Override
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}
