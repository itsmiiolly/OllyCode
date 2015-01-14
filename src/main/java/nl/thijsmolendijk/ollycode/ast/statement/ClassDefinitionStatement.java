package nl.thijsmolendijk.ollycode.ast.statement;

import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.Statement;

/**
 * Represents the definition of a class in ollycode. A class can have multiple superclasses, can implement java interfaces and can only declare variable definitions or functions on the top level.
 * @author molenzwiebel
 */
public class ClassDefinitionStatement implements Statement {
	private String name;
	private List<String> parentClasses;
	private List<VariableDefinitionStatement> variables;
	private List<FunctionStatement> functions;
	
	public ClassDefinitionStatement(String name, List<String> parents, List<VariableDefinitionStatement> vars, List<FunctionStatement> funcs) {
		this.name = name;
		this.parentClasses = parents;
		this.variables = vars;
		this.functions = funcs;
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder("class ").append(name);
		if (parentClasses.size() > 0) {
			ret.append(" :: ");
			ret.append(parentClasses.stream().collect(Collectors.joining(", ")));
		}
		ret.append(" {\n");
		for (VariableDefinitionStatement def : variables) {
			ret.append("  ").append(def).append("\n");
		}
		ret.append("\n");
		for (FunctionStatement func : functions) {
			ret.append("  ").append(func.toString().replace("\n", "\n    ")).append("\n\n");
		}
		ret.append("}");
		return ret.toString();
	}
}
