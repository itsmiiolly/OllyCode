package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

public class NewExpression implements Expression {
	public String name;
	public List<Expression> args;
	
	public NewExpression(String name, List<Expression> args) {
		this.name = name;
		this.args = args;
	}
	
	@Override
	public String toString() {
		return "new "+name+"("+args.stream().map(Object::toString).collect(Collectors.joining(", "))+")";
	}
	
	public static Expression parse(OCParser parser) {
		parser.nextToken();
		String name = parser.consume().getValue();
		List<Expression> args = new ArrayList<>();
		if (parser.currentToken().isChar('(')) {
			parser.nextToken();
			args = IdentifierExpression.parseList(parser);
		}
		return new NewExpression(name, args);
	}
}
