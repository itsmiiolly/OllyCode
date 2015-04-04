package nl.thijsmolendijk.ollycode.parsing.ast.stmt;

import nl.thijsmolendijk.ollycode.lexer.OCTokenType;
import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;
import nl.thijsmolendijk.ollycode.parsing.ast.Statement;

public class IfStatement implements Statement {
	public Expression condition;
	public BodyStatement ifThen;
	public BodyStatement ifElse;
	
	public IfStatement(Expression cond, BodyStatement then, BodyStatement _else) {
		this.condition = cond;
		this.ifThen = then;
		this.ifElse = _else;
	}
	
	@Override
	public String toString() {
		return "if ("+condition+") "+ifThen+(ifElse == null ? "" : " else "+ifElse);
	}
	
	public static Statement parse(OCParser p) {
		if (!p.nextToken().isChar('(')) throw new RuntimeException("Expected ( after if");
		p.nextToken(); //Consume (
		System.out.println(p.currentToken());
		
		Expression cond = p.parseExpression();
		if (!p.consume().isChar(')')) throw new RuntimeException("Expected ) after if condition");
		
		BodyStatement body = BodyStatement.parse(p);
		if (p.currentToken().isType(OCTokenType.ELSE)) {
			p.nextToken(); //Consume else
			return new IfStatement(cond, body, BodyStatement.parse(p));
		}
		return new IfStatement(cond, body, null);
	}
}
