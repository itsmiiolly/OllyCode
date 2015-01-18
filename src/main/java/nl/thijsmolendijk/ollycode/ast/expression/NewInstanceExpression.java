package nl.thijsmolendijk.ollycode.ast.expression;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCInstance;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

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
	public OCObject eval(Interpreter interpreter) {
		OCInstance instance = new OCInstance(interpreter.getRuntime(), interpreter.getRuntime().getClass(className));
		List<OCObject> args = params.stream().map(x -> x.eval(interpreter)).collect(Collectors.toList());
		instance.getInterpreter().invokeFunction("create", args);
		return instance;
	}
}
