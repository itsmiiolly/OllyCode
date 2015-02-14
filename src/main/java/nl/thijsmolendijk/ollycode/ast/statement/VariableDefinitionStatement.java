package nl.thijsmolendijk.ollycode.ast.statement;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;

/**
 * Represents the definition of a variable using 'var'. A variable has to be defined before it can be referenced or assigned.
 * @author molenzwiebel
 */
public class VariableDefinitionStatement implements Statement {
	public String name;
	public Expression initialValue;
	
	public VariableDefinitionStatement(String name, Expression initial) {
		this.name = name;
		this.initialValue = initial;
	}
	
	@Override
	public String toString() {
		return initialValue == null ? "var " + name : "var " + name + " = " + initialValue;
	}

	@Override
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}
