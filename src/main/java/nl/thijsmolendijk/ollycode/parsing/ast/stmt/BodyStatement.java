package nl.thijsmolendijk.ollycode.parsing.ast.stmt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.lexer.OCTokenType;
import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Node;
import nl.thijsmolendijk.ollycode.parsing.ast.Statement;

public class BodyStatement implements Statement {
	public List<Node> body;
	
	public BodyStatement(List<Node> body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		return "{\n"+body.stream().map(x -> "  "+x.toString()).collect(Collectors.joining("\n"))+"\n}";
	}
	
	public static BodyStatement parse(OCParser parser) {
		List<Node> contents = new ArrayList<>();
		if (!parser.currentToken().isChar('{')) throw new RuntimeException("Expected '{' at begin of block, received %s");
		parser.nextToken(); //consume {
		while (!parser.currentToken().isChar('}')) {
			Node ast = parser.parseNode();
			contents.add(ast);
			if (parser.currentToken().isType(OCTokenType.EOF)) throw new RuntimeException("Unexpected EOF. Expected '}'");
		}
		parser.nextToken(); //consume }

		return new BodyStatement(contents);
	}
}
