package nl.thijsmolendijk.ollycode.ast.statement;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCFunction;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

/**
 * Represents a function in OllyCode with a name, arguments and body
 * @author molenzwiebel
 */
public class FunctionStatement implements Statement {
	private String name;
	private List<String> argumentNames;
	private BodyStatement body;
	
	public FunctionStatement(String n, List<String> aN, BodyStatement b) {
		this.name = n;
		this.argumentNames = aN;
		this.body = b;
	}
	
	public int getArgCount() {
		return argumentNames.size();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "def " + name + "(" + argumentNames.stream().collect(Collectors.joining(", ")) + ") {\n" + body + "\n}";
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		return new OCFunction(name, argumentNames, body);
	}
}
