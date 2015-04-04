package nl.thijsmolendijk.ollycode.parsing.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import nl.thijsmolendijk.ollycode.lexer.OCTokenType;
import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Statement;

public class ClassStatement implements Statement {
	public String name;
	public String superclass;
	public List<VariableDefinitionStatement> classVariables;
	public List<FunctionStatement> functions;
	
	public ClassStatement(String name, String superclass, List<VariableDefinitionStatement> classVariables, List<FunctionStatement> functions) {
		this.name = name;
		this.superclass = superclass;
		this.classVariables = classVariables;
		this.functions = functions;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder().append("class ").append(name);
		if (superclass != null) b.append(" :: ").append(superclass);
		b.append(" {\n");
		for (VariableDefinitionStatement stmt : classVariables) {
			b.append("  ").append(stmt.toString()).append("\n");
		}
		if (classVariables.size() > 0) b.append("\n");
		for (FunctionStatement stmt : functions) {
			b.append("  ").append(stmt.toString()).append("\n");
		}
		b.append("\n}");
		return b.toString();
	}
	
	public static Statement parse(OCParser p) {
		String name = p.nextToken().getValue();
		String superclass = null;
		
		if (p.nextToken().isType(OCTokenType.CLASS_EXTEND)) {
			superclass = p.nextToken().getValue();
			p.nextToken(); //Consume superclass name
		}
		
		if (!p.currentToken().isChar('{')) throw new RuntimeException("Expected { after class header.");
		p.nextToken(); //Consume {
		
		List<VariableDefinitionStatement> vars = new ArrayList<>();
		List<FunctionStatement> funcs = new ArrayList<>();
		
		boolean hasNewMethod = false;
		while (!p.currentToken().isChar('}')) {
			if (p.currentToken().isType(OCTokenType.EOF)) throw new RuntimeException("Unexpected EOF, expecting }");

			Statement expr = p.parseStatement();
			if (!(expr instanceof VariableDefinitionStatement) && !(expr instanceof FunctionStatement)) throw new RuntimeException("Unexpected statement "+expr+" ("+expr.getClass().getSimpleName()+"). Can only have variable definitions and functions in class body");
			if (expr instanceof VariableDefinitionStatement) {
				VariableDefinitionStatement st = (VariableDefinitionStatement) expr;
				if (vars.stream().anyMatch(x -> x.name.equalsIgnoreCase(st.name))) throw new RuntimeException("Duplicate class variable "+st.name);
				vars.add(st);
			}

			if (expr instanceof FunctionStatement) {
				FunctionStatement fExpr = (FunctionStatement) expr;
				if (funcs.stream().anyMatch(x -> x.name.equalsIgnoreCase(fExpr.name)))
					throw new RuntimeException("Duplicate function "+fExpr.name+" in class "+name);

				if (!hasNewMethod && fExpr.name.equals("create")) hasNewMethod = true;
				funcs.add(fExpr);
			}
		}
		p.nextToken(); //consume }
		
		if (!hasNewMethod) {
			funcs.add(new FunctionStatement("create", new ArrayList<>(), new BodyStatement(new ArrayList<>())));
		}
		
		return new ClassStatement(name, superclass, vars, funcs);
	}
}
