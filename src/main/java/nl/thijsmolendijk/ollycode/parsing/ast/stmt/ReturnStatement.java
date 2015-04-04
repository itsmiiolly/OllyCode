package nl.thijsmolendijk.ollycode.parsing.ast.stmt;

import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;
import nl.thijsmolendijk.ollycode.parsing.ast.Statement;

public class ReturnStatement implements Statement {
	public Expression value;
	
	public ReturnStatement(Expression expr) {
		this.value = expr;
	}
	
	@Override
	public String toString() {
		return "return" + (value == null ? "" : " "+value);
	}
	
	public static Statement parse(OCParser p) {
		p.nextToken(); //Consume return
		
		Expression val = p.parseExpression();
		return new ReturnStatement(val);
	}
}
