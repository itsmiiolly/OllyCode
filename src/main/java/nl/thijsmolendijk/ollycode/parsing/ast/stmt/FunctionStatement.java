package nl.thijsmolendijk.ollycode.parsing.ast.stmt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Statement;

public class FunctionStatement implements Statement {
	public String name;
	public List<String> argumentNames;
	public BodyStatement body;
	
	public FunctionStatement(String name, List<String> args, BodyStatement body) {
		this.name = name;
		this.argumentNames = args;
		this.body = body;
	}
	
	@Override
	public String toString() {
		return "def " + name + "(" + argumentNames.stream().collect(Collectors.joining(", ")) + ") " + body;
	}
	
	public static Statement parse(OCParser parser) {
		String functionName = parser.nextToken().getValue();
		if (!parser.nextToken().isChar('(')) throw new RuntimeException("Expected ( after function name, received %s"); //also consumes for
		parser.nextToken(); //consume (

		// Read the list of argument names.
		List<String> argNames = new ArrayList<String>();
		if (!parser.currentToken().isChar(')')) {
			while (true) {
				argNames.add(parser.currentToken().getValue());
				parser.nextToken(); //consume identifier
				if (parser.currentToken().isChar(')')) break;
				if (!parser.currentToken().isChar(',')) throw new RuntimeException("Expected ')' or ',' in argument list, received %s");
				parser.nextToken(); //consume ,
			}
		}
		parser.nextToken(); //consume )

		FunctionStatement func = new FunctionStatement(functionName, argNames, BodyStatement.parse(parser));
		return func;
	}
}
