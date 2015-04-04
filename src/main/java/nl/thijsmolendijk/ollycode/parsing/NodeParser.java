package nl.thijsmolendijk.ollycode.parsing;

import nl.thijsmolendijk.ollycode.parsing.ast.Expression;
import nl.thijsmolendijk.ollycode.parsing.ast.Node;

/**
 * Interface responsible for parsing a single type of expression or statement.
 * @author molenzwiebel
 */
public interface NodeParser {
	/**
	 * Parses the AST node
	 * @param parser the parser
	 * @return the parsed node
	 */
	public Node parse(OCParser parser);
	
	public interface InfixNodeParser {
		/**
		 * Parses the token.
		 * @param parser the parser
		 * @param left the left part of the expression
		 * @return the parsed node
		 */
		public Node parse(OCParser parser, Expression left);
	}
}
