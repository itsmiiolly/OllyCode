package nl.thijsmolendijk.ollycode.parsing.ast.stmt;

import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;
import nl.thijsmolendijk.ollycode.parsing.ast.Node;
import nl.thijsmolendijk.ollycode.parsing.ast.Statement;

public class ForStatement implements Statement {
	public Node initialization;
	public Expression condition;
	public Node step;
	public BodyStatement body;

	public ForStatement(Node initialization, Expression condition, Node step, BodyStatement body) {
		this.initialization = initialization;
		this.condition = condition;
		this.step = step;
		this.body = body;
	}

	@Override
	public String toString() {
		return "for ("+initialization+", "+condition+", "+step+") "+body;
	}

	public static Statement parse(OCParser parser) {
		if (!parser.nextToken().isChar('(')) throw new RuntimeException("Expected ( after 'for', received %s"); //also consumes for
		parser.nextToken(); //consume (

		Node initialization = parser.currentToken().isChar(',') ? null : parser.parseNode();
		if (!parser.currentToken().isChar(',')) throw new RuntimeException("Expected , in for definition, received %s");
		parser.nextToken(); //consume ,

		Expression condition = parser.parseExpression();
		if (condition == null) throw new RuntimeException("Expected condition in for definition");
		if (!parser.currentToken().isChar(',')) throw new RuntimeException("Expected , in for definition, received %s");
		parser.nextToken(); //consume ,

		Node step = parser.currentToken().isChar(')') ? null : parser.parseNode();
		if (!parser.currentToken().isChar(')')) throw new RuntimeException("Expected ) after for definition, received %s");
		parser.nextToken(); //consume )

		return new ForStatement(initialization, condition, step, BodyStatement.parse(parser));
	}
}
