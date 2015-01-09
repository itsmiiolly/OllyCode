package nl.thijsmolendijk.ollycode.parsing;

import static nl.thijsmolendijk.ollycode.lexing.OCTokenType.*;

import java.util.ArrayList;
import java.util.List;

import nl.thijsmolendijk.ollycode.ast.ASTElement;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.ast.expression.BinaryExpression;
import nl.thijsmolendijk.ollycode.ast.expression.BooleanExpression;
import nl.thijsmolendijk.ollycode.ast.expression.NullExpression;
import nl.thijsmolendijk.ollycode.ast.expression.ReturnExpression;
import nl.thijsmolendijk.ollycode.ast.statement.BodyStatement;
import nl.thijsmolendijk.ollycode.lexing.OCTokenType;
import nl.thijsmolendijk.ollycode.lexing.OCTokenizingRuleSet;

/**
 * The main parser for OllyCode that transforms text into {@link ASTElement}s
 * @author molenzwiebel
 */
public class OCParser extends BasicParser {
	//Dirty hack to let parseExpression and parseStatement not error if they are called by parseASTElement
	private boolean shouldThrowUnexpectedTokenErrors = true;
	
	public OCParser(String input) {
		super(input, new OCTokenizingRuleSet()
				.character('{', BEGIN_BLOCK)
				.character('}', END_BLOCK)
				.identifier("def", DEF)
				.identifier("null", NULL)
				.identifier("true", TRUE)
				.identifier("false", FALSE)
				.identifier("var", VAR)
				.identifier("if", IF)
				.identifier("else", ELSE)
				.identifier("elseif", ELSEIF)
				.identifier("return", RETURN)
				.identifier("for", FOR)
				.identifier("class", CLASS)
				.identifier("while", WHILE));
	}
	
	/**
	 * Combination of {@link #parseStatement()} and {@link #parseExpression()}
	 */
	public ASTElement parseASTElement() {
		shouldThrowUnexpectedTokenErrors = false;
		ASTElement returnValue = parseStatement();
		if (returnValue == null) returnValue = parseExpression();
		shouldThrowUnexpectedTokenErrors = true;
		return returnValue;
	}
	
	/**
	 * Tries to parse the {@code currentToken} into a {@link Statement}. This method will throw an error when an expression or other token is received instead.
	 */
	public Statement parseStatement() {
		Statement returnValue = null;		
		if (isType(CLASS)) {
			returnValue = parseClass();
		} else if (isType(FOR)) {
			returnValue = parseFor();
		} else if (isType(VAR)) {
			returnValue = parseVariableDefinition();
		} else if (isType(IF)) {
			returnValue = parseIf();
		} else if (isType(DEF)) {
			returnValue = parseFunctionDefinition();
		} else if (isType(WHILE)) {
			returnValue = parseWhile();
		} else if (isType(BEGIN_BLOCK)) {
			returnValue = parseBlock();
		} else if (shouldThrowUnexpectedTokenErrors) { //again, dirty hack
			showErrorMessage("Unknown token when expecting a statement, received %s", currentToken.toString());
		}
		return returnValue;
	}
	
	/**
	 * Tries to parse the {@code currentToken} into a {@link Expression}. This is achieved by first trying to parse a binary operation, and if this fails the parser tries to match a keyword.
	 */
	public Expression parseExpression() {
		Expression left = _parseExpression();
		if (left == null) return null;
		
		return parseBinaryOperationRight(0, left);
	}
	
	/**
	 * Does the same as {@link #parseExpression()} but does not try to match a binary expression. This method should <i>not</i> be called by anything other than {@link #parseExpression()} and {@link #parseBinaryOperationRight(int, Expression)}.
	 */
	private Expression _parseExpression() {
		Expression returnValue = null;
		
		if (isType(IDENTIFIER)) {
			returnValue = parseIdentifier();
		} else if (isType(NUMBER)) {
			returnValue = parseNumber();
		} else if (isType(STRING)) {
			returnValue = parseString();
		} else if (isType(NULL)) {
			nextToken(); //consume null
			returnValue = new NullExpression();
		} else if (isType(TRUE)) {
			nextToken(); //consume true
			returnValue = new BooleanExpression(true);
		} else if (isType(FALSE)) {
			nextToken(); //consume false
			returnValue = new BooleanExpression(false);
		} else if (isChar('(')) {
			returnValue = parseParen();
		} else if (shouldThrowUnexpectedTokenErrors) { //dirty dirty
			showErrorMessage("Unknown token when expecting a statement, received %s", currentToken.toString());
		}

		//Parse member access
		while (isChar('.')) {
			nextToken(); //consume .
			expect(IDENTIFIER, "Expected identifier or function call when accessing members using '.'. Received %s");
			returnValue = new MemberExpression(returnValue, parseIdentifier());
		}

		return returnValue;
	}
	
	/**
	 * Parses:
	 * (EXPRESSION) OPERATOR (EXPRESSION)
	 */
	public Expression parseBinaryOperationRight(int expressionPrecedence, Expression left) {
		// If this is a binop, find its precedence.
		while (true) {
			int prec = currentToken.getValue() instanceof Character ? OCTokenType.getTokenPrecedence(currentToken.<Character>getValue()) : -1;
			if (prec < expressionPrecedence)
				return left;

			char operation = currentToken.getValue();
			nextToken();  // eat operation

			Expression right = _parseExpression();
			if (right == null) return null;

			int nextPrec = currentToken.getValue() instanceof Character ? OCTokenType.getTokenPrecedence(currentToken.<Character>getValue()) : -1;
			if (prec < nextPrec) {
				right = parseBinaryOperationRight(prec + 1, right);
				if (right == null) return null;
			}

			left = new BinaryExpression(String.valueOf(operation), left, right);
		}
	}
	
	/**
	 * Parses a block (statements and expressions between { and })
	 */
	public BodyStatement parseBlock() {
		List<ASTElement> contents = new ArrayList<>();
		
		expect(BEGIN_BLOCK, "Expected '{' at begin of block, received %s");
		nextToken(); //consume {
		while (!isType(END_BLOCK)) {
			if (isType(EOF)) showErrorMessage("Unexpected EOF. Expected '}'");
			ASTElement ast = parseASTElement();
			contents.add(ast instanceof Expression ? new ReturnExpression((Expression) ast) : ast);
		}
		nextToken(); //consume }
		
		return new BodyStatement(contents);
	}
}
