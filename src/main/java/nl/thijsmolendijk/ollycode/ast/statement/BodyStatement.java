package nl.thijsmolendijk.ollycode.ast.statement;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.ASTElement;
import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

/**
 * Represents a body (between { and }) with multiple ASTElements in it
 * @author molenzwiebel
 */
public class BodyStatement implements Statement {
	private List<ASTElement> contents;
	
	public BodyStatement(List<ASTElement> content) {
		this.contents = content;
	}
	
	@Override
	public String toString() {
		return contents.stream().map(Object::toString).collect(Collectors.joining("\n"));
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		OCObject ret = null;
        for (ASTElement statement : contents) {
            ret = statement.eval(interpreter);
        }
        return ret;
	}
}
