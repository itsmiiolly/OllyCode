package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

/**
 * Represents the assigning of a value to a variable. Note that this is classified as an expression because it will return its new value so that some tricky if statements are possible.
 * The assignment of variables being an expression allows ollycode to support statements like this: <code>if (myVar = getVar() == "myVar") {</code>, where getVar() will be compares to "myVar"
 * @author molenzwiebel
 */
public class VariableAssignmentExpression implements Expression {
	private String variableName;
	private Expression value;
	
	public VariableAssignmentExpression(String name, Expression val) {
		this.variableName = name;
		this.value = val;
	}
	
	@Override
	public String toString() {
		return variableName + " = " + value;
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		OCObject result = value.eval(interpreter);
		interpreter.setVariable(variableName, result);
		return result;
	}
}
