package nl.thijsmolendijk.ollycode.ast;

import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

/**
 * Represents part of the Abstract Syntax Tree that the OCParser creates from the source.
 * ASTElements can either be Expressions or Statements. The difference between those is that Expressions <i>always</i> return a value, whereas Statements are not required to.
 * For example, a number literal is an expression, because it will always return a number.
 * If statements, function statements, while statements and others are classified as statements because they do not guaranteeed return a value. This is also why they cannot be used in a binary expression.
 * @author molenzwiebel
 */
public interface ASTElement {
	/**
	 * Require any ASTElement to override the toString function
	 * @return
	 */
	@Override
	public String toString();
	
	/**
	 * Evaluates the AST node and returns the result
	 * @param interpreter the interpreter storing all variables
	 * @return the result of the evaluation, or null if not applicable (functions without return values for example)
	 */
	public OCObject eval(Interpreter interpreter);
}
