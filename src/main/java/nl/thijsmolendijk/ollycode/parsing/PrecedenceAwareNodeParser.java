package nl.thijsmolendijk.ollycode.parsing;

import nl.thijsmolendijk.ollycode.parsing.NodeParser.InfixNodeParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;
import nl.thijsmolendijk.ollycode.parsing.ast.Node;

/**
 * A {@link NodeParser}, but with a precedence.
 * @author molenzwiebel
 */
public interface PrecedenceAwareNodeParser extends InfixNodeParser {
	/**
	 * @return the precedence of the node this parser parses
	 */
	public int getPrecedence();
	
	public static class SimplePrecedenceParser implements PrecedenceAwareNodeParser {
		private InfixNodeParser parser;
		private int precedence;
		
		public SimplePrecedenceParser(InfixNodeParser parser, int precedence) {
			this.parser = parser;
			this.precedence = precedence;
		}

		@Override
		public Node parse(OCParser parser, Expression left) {
			return this.parser.parse(parser, left);
		}
		
		@Override
		public int getPrecedence() {
			return precedence;
		}
	}
}
