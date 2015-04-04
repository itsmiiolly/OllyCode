package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import java.util.ArrayList;
import java.util.List;

import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

public class IdentifierExpression implements Expression {
	public String name;
	
	public IdentifierExpression(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static List<Expression> parseList(OCParser parser) {
		List<Expression> args = new ArrayList<>();
		if (!parser.currentToken().isChar(')')) { //if there are arguments in this method call...
			while (true) {
				Expression arg = parser.parseExpression();
				if (arg == null) return null;
				args.add(arg);
				if (parser.currentToken().isChar(')')) break;
				if (!parser.currentToken().isChar(',')) throw new RuntimeException("Expected , or (");
				parser.nextToken(); //consume ,
			}
		}
		parser.nextToken(); //consume )
		return args;
	}
	
	public static Expression parse(OCParser parser) {
		String expr = parser.currentToken().getValue();
		Expression retval = null;

		if (parser.nextToken().isChar('(')) {
			parser.nextToken();  // consume (
			retval = new CallExpression(expr, parseList(parser));
		}

		if (retval == null) {
			retval = new IdentifierExpression(expr);
		}
		
		while (parser.currentToken().isChar('.')) {
			String name = parser.nextToken().getValue();
			if (parser.nextToken().isChar('(')) {
				parser.nextToken(); //consume (
				List<Expression> parameters = parseList(parser);
				retval = new MemberExpression(retval, new CallExpression(name, parameters));
			} else {
				retval = new MemberExpression(retval, new IdentifierExpression(name));
			}
		}
		
		return retval;
	}
}
