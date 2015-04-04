package nl.thijsmolendijk.ollycode.parsing.ast.stmt;

import nl.thijsmolendijk.ollycode.lexer.OCTokenType;
import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;
import nl.thijsmolendijk.ollycode.parsing.ast.Statement;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.AssignmentExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.IdentifierExpression;

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

	public static Statement parse(OCParser parser) {
		if (!parser.nextToken().isType(OCTokenType.IDENTIFIER)) throw new RuntimeException("Expected identifier after var, received %s");
		Expression next = parser.parseExpression();
		Expression assign = null;
		String name = null;
		if (next instanceof AssignmentExpression) {
			assign = ((AssignmentExpression) next).right;
			name = ((AssignmentExpression) next).left.toString();
		} else {
			name = ((IdentifierExpression) next).toString();
		}
		return new VariableDefinitionStatement(name.toString(), assign);
	}
}
