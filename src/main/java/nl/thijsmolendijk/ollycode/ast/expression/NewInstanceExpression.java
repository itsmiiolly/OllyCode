package nl.thijsmolendijk.ollycode.ast.expression;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;

/**
 * Expression that represents the creation of a new object.
 * @author molenzwiebel
 */
public class NewInstanceExpression implements Expression {
	private String className;
	private List<Expression> params;
	
	public NewInstanceExpression(String name, List<Expression> params) {
		this.className = name;
		this.params = params;
	}
	
	@Override
	public String toString() {
		return "create " + className + "(" + params.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
	}

	@Override
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}
