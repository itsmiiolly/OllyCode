package nl.thijsmolendijk.ollycode.parsing;

import static nl.thijsmolendijk.ollycode.lexing.OCTokenType.*;

import java.util.ArrayList;
import java.util.List;

import nl.thijsmolendijk.ollycode.ast.ASTElement;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.ast.expression.BinaryExpression;
import nl.thijsmolendijk.ollycode.ast.expression.BooleanExpression;
import nl.thijsmolendijk.ollycode.ast.expression.FunctionCallExpression;
import nl.thijsmolendijk.ollycode.ast.expression.IdentifierExpression;
import nl.thijsmolendijk.ollycode.ast.expression.MemberExpression;
import nl.thijsmolendijk.ollycode.ast.expression.NullExpression;
import nl.thijsmolendijk.ollycode.ast.expression.NumberLiteralExpression;
import nl.thijsmolendijk.ollycode.ast.expression.ReturnExpression;
import nl.thijsmolendijk.ollycode.ast.expression.StringLiteralExpression;
import nl.thijsmolendijk.ollycode.ast.expression.VariableAssignmentExpression;
import nl.thijsmolendijk.ollycode.ast.statement.BodyStatement;
import nl.thijsmolendijk.ollycode.ast.statement.ClassDefinitionStatement;
import nl.thijsmolendijk.ollycode.ast.statement.ForStatement;
import nl.thijsmolendijk.ollycode.ast.statement.FunctionStatement;
import nl.thijsmolendijk.ollycode.ast.statement.IfStatement;
import nl.thijsmolendijk.ollycode.ast.statement.VariableDefinitionStatement;
import nl.thijsmolendijk.ollycode.ast.statement.WhileStatement;
import nl.thijsmolendijk.ollycode.lexing.OCTokenType;
import nl.thijsmolendijk.ollycode.lexing.OCTokenizingRuleSet;

import org.apache.commons.lang3.tuple.Pair;

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
		.identifier("while", WHILE)
		.substring(">=", GT_EQ)
		.substring("<=", LT_EQ)
		.substring("&&", AND)
		.substring("||", OR)
		.substring("==", EQUAL));

		nextToken(); //advance to the first token.
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
			showErrorMessage("Unknown token when expecting an expression, received %s", currentToken.toString());
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
	 * Parses a binary operation with regards to precedence.
	 * Accepts grammar:
	 * expression operator expression
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
	 * Parses a simple string into its AST equivalent. This just extracts the token value and returns an expression for it.
	 * Accepts grammar:
	 * "value"
	 */
	public Expression parseString() {
		String value = currentToken.getValue();
		nextToken(); //consume the string
		return new StringLiteralExpression(value);
	}

	/**
	 * Parses a simple number into its AST equivalent. This just extracts the token value and returns an expression for it.
	 * Accepts grammar:
	 * ##[.##]
	 */
	public Expression parseNumber() {
		Double value = currentToken.getValue();
		nextToken(); //consume the string
		return new NumberLiteralExpression(value);
	}

	/**
	 * Parses any expression between parentheses:
	 * Accepts grammar:
	 * ( expression )
	 */
	public Expression parseParen() {
		nextToken(); // eat (.

		Expression expr = parseExpression();
		if (expr == null) return null;

		if (!isChar(')')) showErrorMessage("Expected ')' in parenthesized expression, received %s", currentToken.toString());
		nextToken(); // eat ).

		return expr;
	}

	/**
	 * Parses the initial definition of a variable.
	 * Accepts grammar:
	 * var name [= value]
	 */
	public Statement parseVariableDefinition() {
		expectNext(IDENTIFIER, "Expected variable name after 'var', received %s");

		String varName = currentToken.getValue();
		nextToken(); //eat var name
		if (!isChar('=')) return new VariableDefinitionStatement(varName, null);

		nextToken(); //eat =
		Expression value = parseExpression();
		if (value == null) showErrorMessage("Expected expression after 'var "+varName+" = '");
		return new VariableDefinitionStatement(varName, value);
	}

	/**
	 * Parses a function definition.
	 * Accepts grammar:
	 * def name(args...) {
	 *   [expressions]
	 * }
	 */
	public Statement parseFunctionDefinition() {
		expectNext(IDENTIFIER, "Expected function name after 'def', received %s"); //also consumes def
		String functionName = currentToken.getValue();
		expectNext('(', "Expected ( after function name, received %s"); //also consumes name
		nextToken(); //consume (

		// Read the list of argument names.
		List<String> argNames = new ArrayList<String>();
		if (!isChar(')')) {
			while (true) {
				expect(IDENTIFIER, "Expected identifier in function prototype, received %s");
				argNames.add(currentToken.getValue());
				nextToken(); //consume identifier
				if (isChar(')')) break;
				if (!isChar(',')) throw new RuntimeException("Expected ')' or ',' in argument list");
				nextToken(); //consume ,
			}
		}
		expect(')', "Expected ) after function parameter names, received %s");
		nextToken(); //consume )

		return new FunctionStatement(functionName, argNames, parseBlock());
	}

	/**
	 * Parses a for loop.
	 * Accepts grammar:
	 * for (expression, expression, expression) {
	 *   [statements]
	 * }
	 */
	public Statement parseFor() {
		expectNext('(', "Expected ( after 'for', received %s"); //also consumes for
		nextToken(); //consume (

		ASTElement initialization = parseASTElement();
		if (initialization == null || (!(initialization instanceof Expression) && !(initialization instanceof VariableDefinitionStatement))) showErrorMessage("Expected expression or variable initialization in for, received %s", currentToken.toString());
		expect(',', "Expected , in for definition, received %s");
		nextToken(); //consume ,

		Expression condition = parseExpression();
		if (condition == null) showErrorMessage("Expected condition in for definition");
		expect(',', "Expected , in for definition, received %s");
		nextToken(); //consume ,

		Expression step = parseExpression();
		if (step == null) showErrorMessage("Expected step in for definition");
		expect(')', "Expected ) at the end of for definition, received %s");
		nextToken(); //consume )

		return new ForStatement(initialization, condition, step, parseBlock());
	}

	/**
	 * Parses a while loop.
	 * Accepts grammar:
	 * while (expression) {
	 *   [statements]
	 * }
	 */
	public Statement parseWhile() {
		expectNext('(', "Expected ( after 'while', received %s"); //also consumes while
		nextToken(); //consume (

		Expression condition = parseExpression();
		if (condition == null) showErrorMessage("Expected condition in while");
		expect(')', "Expected ) after while condition, received %s");
		nextToken(); //consume )

		return new WhileStatement(condition, parseBlock());
	}

	/**
	 * Parses an if statement.
	 * Accepts grammar:
	 * if (condition) {
	 *   [statements]
	 * } [elseif (condition) {
	 *   [statements]
	 * }] [else {
	 *   [statements]
	 * }]
	 */
	public Statement parseIf() {
		expectNext('(', "Expected ( after 'for', received %s"); //also consumes for
		nextToken(); //consume (

		//if (cond) { ... } parsing
		Expression ifCondition = parseExpression();
		if (ifCondition == null) showErrorMessage("Expected condition in if");
		expect(')', "Expected ) after if condition, received %s");
		nextToken(); //consume )
		BodyStatement thenBody = parseBlock();

		//else and elseif parsing
		BodyStatement elseBody = null;
		List<Pair<Expression, BodyStatement>> elseifs = new ArrayList<>();
		while (isType(ELSE) || isType(ELSEIF)) {
			if (isType(ELSE)) {
				nextToken(); //consume else
				if (elseBody != null) showErrorMessage("An if statement can only have one else block");
				elseBody = parseBlock();
			} else {
				expectNext('(', "Expected ( after elseif, received %s"); //also consumes elseif
				nextToken(); //consume (

				Expression condition = parseExpression();
				if (condition == null) showErrorMessage("Expected condition in elseif");
				if (!isChar(')')) throw new RuntimeException("Expected ) after elseif condition");
				nextToken(); //consume )

				elseifs.add(Pair.of(condition, parseBlock()));
			}
		}

		return new IfStatement(ifCondition, thenBody, elseifs, elseBody);
	}

	/**
	 * Parses a class definition.
	 * Accepts grammar:
	 * class IDEN [:: IDEN, IDEN] {
	 *   VARDEFS
	 *   DEFS
	 * }
	 */
	public Statement parseClass() {
		expectNext(IDENTIFIER, "Expected class name after 'class', received %s");
		String name = currentToken.getValue();
		nextToken(); //consume class name

		List<String> parents = new ArrayList<>();
		if (isChar(':')) {
			nextToken(); //consume :
			if (!isChar(':')) showErrorMessage("Expected TWO colons (:) or { after class name, received %s", currentToken.toString());

			while (true) {
				expectNext(IDENTIFIER, "Expected identifier in class parent list, received %s");
				parents.add(currentToken.getValue());
				nextToken(); //consume name
				if (!isChar(',')) break; //check for ,
			}
		}
		expect(BEGIN_BLOCK, "Expected { after class definition, received %s");
		nextToken(); //consume {

		List<VariableDefinitionStatement> vars = new ArrayList<>();
		List<FunctionStatement> funcs = new ArrayList<>();
		boolean hasNewMethod = false;
		while (!isType(END_BLOCK)) {
			if (isType(EOF)) showErrorMessage("Unexpected EOF, expecting }");

			Statement expr = parseStatement();
			if (!(expr instanceof VariableDefinitionStatement) && !(expr instanceof FunctionStatement)) showErrorMessage("Unexpected statement "+expr+" ("+expr.getClass().getSimpleName()+"). Can only have variable definitions and functions in class body");
			if (expr instanceof VariableDefinitionStatement) {
				VariableDefinitionStatement st = (VariableDefinitionStatement) expr;
				if (vars.stream().anyMatch(x -> x.name.equalsIgnoreCase(st.name))) throw new RuntimeException("Duplicate class variable "+st.name);
				vars.add(st);
			}

			if (expr instanceof FunctionStatement) {
				FunctionStatement fExpr = (FunctionStatement) expr;
				if (funcs.stream().anyMatch(x -> x.getName().equalsIgnoreCase(fExpr.getName()) && x.getArgCount() == fExpr.getArgCount()))
					showErrorMessage("Duplicate function "+fExpr.getName()+" with same amount of parameters");

				if (!hasNewMethod && fExpr.getName().equals("new")) hasNewMethod = true;
				funcs.add(fExpr);
			}
		}
		nextToken(); //consume }

		if (!hasNewMethod) {
			funcs.add(new FunctionStatement("new", new ArrayList<>(), new BodyStatement(new ArrayList<>())));
		}

		ClassDefinitionStatement retval = new ClassDefinitionStatement(name, parents, vars, funcs);
		return retval;
	}

	/**
	 * Parses an identifier. An identifier is a string that matches [a-zA-Z][a-zA-Z0-9]*. This parser will also parse variable assignments and method calls because they come directly after an identifier.
	 * Accepts grammar:
	 * identifier [= expression|(args)]
	 */
	public Expression parseIdentifier() {
		String id = currentToken.getValue();
		nextToken();  //eat identifier.

		if (!isChar('(') && !isChar('=')) // If the next token isn't a ( or a =, we assume it is a reference to a variable or class
			return new IdentifierExpression(id);

		if (previousToken != null && (isType(DEF, previousToken) || isType(VAR, previousToken))) //We want to ignore = and ( if we are in a function definition or variable definition
			return new IdentifierExpression(id);

		if (isChar('(')) { // Assume it's a method call
			nextToken();  // consume (
			List<Expression> args = new ArrayList<>();
			if (!isChar(')')) { //if there are arguments in this method call...
				while (true) {
					Expression arg = parseExpression();
					if (arg == null) return null;
					args.add(arg);

					if (isChar(')')) break;
					if (!isChar(',')) showErrorMessage("Expected , or ) in method call argument list, received %s", currentToken.toString());
					nextToken(); //consume ,
				}
			}
			nextToken(); //consume )

			return new FunctionCallExpression(id, args);
		} else if (isChar('=')) { //if it is a variable assignment
			nextToken(); //consume =
			return new VariableAssignmentExpression(id, parseExpression());
		}

		return null;
	}

	/**
	 * Parses a list of expressions delimited by ,.
	 * Accepts grammar:
	 * expression[, expression][, expression]
	 */
	public List<Expression> parseExpressionList() {
		List<Expression> contents = new ArrayList<>();
		while (true) {
			Expression expr = parseExpression();
			if (expr == null) showErrorMessage("Expected expression");
			nextToken(); //consume identifier
			if (!isChar(',')) break;
			nextToken(); //consume ,
		}
		return contents;
	}

	/**
	 * Parses a block (statements and expressions between { and })
	 * Accepts grammar:
	 * { [expressions] }
	 */
	public BodyStatement parseBlock() {
		List<ASTElement> contents = new ArrayList<>();

		expect(BEGIN_BLOCK, "Expected '{' at begin of block, received %s");
		nextToken(); //consume {
		while (!isType(END_BLOCK)) {
			ASTElement ast = parseASTElement();
			contents.add(ast instanceof Expression ? new ReturnExpression((Expression) ast) : ast);
			if (isType(EOF)) showErrorMessage("Unexpected EOF. Expected '}'");
		}
		nextToken(); //consume }

		return new BodyStatement(contents);
	}
}
