package nl.thijsmolendijk.ollycode.ast.expression;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

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
	public OCObject eval(Interpreter interpreter) {
		return interpreter.invokeFunction(functionName, arguments.stream().map(x -> x.eval(interpreter)).collect(Collectors.toList()));
	}
}
