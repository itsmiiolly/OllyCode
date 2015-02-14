package nl.thijsmolendijk.ollycode.ast.statement;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Statement;

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
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}
