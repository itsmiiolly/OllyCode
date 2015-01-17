package nl.thijsmolendijk.ollycode.parsing;

import static nl.thijsmolendijk.ollycode.lexing.OCTokenType.*;

import java.util.ArrayList;
import java.util.List;

import nl.thijsmolendijk.ollycode.ast.ASTElement;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.ast.expression.BooleanExpression;
import nl.thijsmolendijk.ollycode.ast.expression.FunctionCallExpression;
import nl.thijsmolendijk.ollycode.ast.expression.IdentifierExpression;
import nl.thijsmolendijk.ollycode.ast.expression.MemberExpression;
import nl.thijsmolendijk.ollycode.ast.expression.NewInstanceExpression;
import nl.thijsmolendijk.ollycode.ast.expression.NullExpression;
import nl.thijsmolendijk.ollycode.ast.expression.NumberLiteralExpression;
import nl.thijsmolendijk.ollycode.ast.expression.StringLiteralExpression;
import nl.thijsmolendijk.ollycode.ast.expression.VariableAssignmentExpression;
import nl.thijsmolendijk.ollycode.ast.expression.binary.ConcatExpression;
import nl.thijsmolendijk.ollycode.ast.expression.binary.NumericOperationExpressions;
import nl.thijsmolendijk.ollycode.ast.expression.binary.ValueComparisonExpressions;
import nl.thijsmolendijk.ollycode.ast.statement.BodyStatement;
import nl.thijsmolendijk.ollycode.ast.statement.ClassDefinitionStatement;
import nl.thijsmolendijk.ollycode.ast.statement.ForStatement;
import nl.thijsmolendijk.ollycode.ast.statement.FunctionStatement;
import nl.thijsmolendijk.ollycode.ast.statement.IfStatement;
import nl.thijsmolendijk.ollycode.ast.statement.ReturnStatement;
import nl.thijsmolendijk.ollycode.ast.statement.VariableDefinitionStatement;
import nl.thijsmolendijk.ollycode.ast.statement.WhileStatement;
import nl.thijsmolendijk.ollycode.lexing.OCToken;
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
		.identifier("new", NEW)
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
		.substring("==", EQUAL)
		.substring("!=", NOT_EQUAL));

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
			returnValue = parseClassStatement();
		} else if (isType(FOR)) {
			returnValue = parseForStatement();
		} else if (isType(VAR)) {
			returnValue = parseVariableDefinitionStatement();
		} else if (isType(IF)) {
			returnValue = parseIfStatement();
		} else if (isType(DEF)) {
			returnValue = parseFunctionDefinitionStatement();
		} else if (isType(WHILE)) {
			returnValue = parseWhileStatement();
		} else if (isType(BEGIN_BLOCK)) {
			returnValue = parseBlockStatement();
		} else if (shouldThrowUnexpectedTokenErrors) { //again, dirty hack
			showErrorMessage("Unknown token when expecting a statement, received %s", currentToken.toString());
		}
		return returnValue;
	}
	
	/**
	 * Parses a function definition.
	 * Accepts grammar:
	 * def name(args...) {
	 *   [expressions]
	 * }
	 */
	public Statement parseFunctionDefinitionStatement() {
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

		return new FunctionStatement(functionName, argNames, parseBlockStatement());
	}
	
	/**
	 * Parses the initial definition of a variable.
	 * Accepts grammar:
	 * var name [= value]
	 */
	public Statement parseVariableDefinitionStatement() {
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
	 * Parses a for loop.
	 * Accepts grammar:
	 * for (expression, expression, expression) {
	 *   [statements]
	 * }
	 */
	public Statement parseForStatement() {
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

		return new ForStatement(initialization, condition, step, parseBlockStatement());
	}
	
	/**
	 * Parses a while loop.
	 * Accepts grammar:
	 * while (expression) {
	 *   [statements]
	 * }
	 */
	public Statement parseWhileStatement() {
		expectNext('(', "Expected ( after 'while', received %s"); //also consumes while
		nextToken(); //consume (

		Expression condition = parseExpression();
		if (condition == null) showErrorMessage("Expected condition in while");
		expect(')', "Expected ) after while condition, received %s");
		nextToken(); //consume )

		return new WhileStatement(condition, parseBlockStatement());
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
	public Statement parseIfStatement() {
		expectNext('(', "Expected ( after 'for', received %s"); //also consumes for
		nextToken(); //consume (

		//if (cond) { ... } parsing
		Expression ifCondition = parseExpression();
		if (ifCondition == null) showErrorMessage("Expected condition in if");
		expect(')', "Expected ) after if condition, received %s");
		nextToken(); //consume )
		BodyStatement thenBody = parseBlockStatement();

		//else and elseif parsing
		BodyStatement elseBody = null;
		List<Pair<Expression, BodyStatement>> elseifs = new ArrayList<>();
		while (isType(ELSE) || isType(ELSEIF)) {
			if (isType(ELSE)) {
				nextToken(); //consume else
				if (elseBody != null) showErrorMessage("An if statement can only have one else block");
				elseBody = parseBlockStatement();
			} else {
				expectNext('(', "Expected ( after elseif, received %s"); //also consumes elseif
				nextToken(); //consume (

				Expression condition = parseExpression();
				if (condition == null) showErrorMessage("Expected condition in elseif");
				if (!isChar(')')) throw new RuntimeException("Expected ) after elseif condition");
				nextToken(); //consume )

				elseifs.add(Pair.of(condition, parseBlockStatement()));
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
	public Statement parseClassStatement() {
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
	 * Parses a block (statements and expressions between { and })
	 * Accepts grammar:
	 * { [expressions] }
	 */
	public BodyStatement parseBlockStatement() {
		List<ASTElement> contents = new ArrayList<>();

		expect(BEGIN_BLOCK, "Expected '{' at begin of block, received %s");
		nextToken(); //consume {
		while (!isType(END_BLOCK)) {
			ASTElement ast = parseASTElement();
			contents.add(ast instanceof Expression ? new ReturnStatement((Expression) ast) : ast);
			if (isType(EOF)) showErrorMessage("Unexpected EOF. Expected '}'");
		}
		nextToken(); //consume }

		return new BodyStatement(contents);
	}

	/**
	 * Tries to parse the {@code currentToken} into a {@link Expression}. This is achieved by first trying to parse a binary operation, and if this fails the parser tries to match a keyword.
	 */
	public Expression parseExpression() {
		return parseLogicalOrExpression();
	}
	
	/**
	 * Parses logical OR.
	 * Accepts grammar:
	 * statement || statement
	 */
	public Expression parseLogicalOrExpression() {
		Expression left = parseLogicalAndExpression();
		if (left == null) return null;
		
		if (isType(OR)) {
			nextToken(); //consume ||
			Expression right = parseLogicalOrExpression();
			if (right == null) return null;
			
			left = new ValueComparisonExpressions.Or(left, right);
		}
		
		return left;
	}
	
	/**
	 * Parses logical AND.
	 * Accepts grammar:
	 * statement && statement
	 */
	public Expression parseLogicalAndExpression() {
		Expression left = parseValueComparisonExpression();
		if (left == null) return null;
		
		if (isType(AND)) {
			nextToken(); //consume &&
			Expression right = parseLogicalAndExpression();
			if (right == null) return null;
			
			left = new ValueComparisonExpressions.And(left, right);
		}
		
		return left;
	}
	
	/**
	 * Parses is equal and is not equal
	 * Accepts grammar:
	 * statement (==|!=) statement
	 */
	public Expression parseValueComparisonExpression() {
		Expression left = parseLesserThanExpression();
		if (left == null) return null;
		
		if (isType(EQUAL) || isType(NOT_EQUAL)) {
			OCTokenType type = currentToken.getType();
			nextToken(); //consume == or !=
			
			Expression right = parseValueComparisonExpression();
			if (right == null) return null;
			
			left = type == EQUAL ? new ValueComparisonExpressions.Compare(true, left, right) : new ValueComparisonExpressions.Compare(false, left, right);
		}
		
		return left;
	}
	
	/**
	 * Parses lesser than and lesser than or equal
	 * Accepts grammar:
	 * statement (<|<=) statement
	 */
	public Expression parseLesserThanExpression() {
		Expression left = parseGreaterThanExpression();
		if (left == null) return null;
		
		if (isChar('<') || isType(LT_EQ)) {
			OCTokenType type = currentToken.getType();
			nextToken(); //consume < or <=
			
			Expression right = parseLesserThanExpression();
			if (right == null) return null;
			
			left = type == LT_EQ ? new ValueComparisonExpressions.LesserThan(true, left, right) : new ValueComparisonExpressions.LesserThan(false, left, right);
		}
		
		return left;
	}
	
	/**
	 * Parses greater than and greater than or equal
	 * Accepts grammar:
	 * statement (>|>=) statement
	 */
	public Expression parseGreaterThanExpression() {
		Expression left = parseConcatExpression();
		if (left == null) return null;
		
		if (isChar('>') || isType(GT_EQ)) {
			OCTokenType type = currentToken.getType();
			nextToken(); //consume < or <=
			
			Expression right = parseGreaterThanExpression();
			if (right == null) return null;
			
			left = type == GT_EQ ? new ValueComparisonExpressions.GreaterThan(true, left, right) : new ValueComparisonExpressions.GreaterThan(false, left, right);
		}
		
		return left;
	}
	
	/**
	 * Parses concat.
	 * Accepts grammar:
	 * statement - statement
	 */
	public Expression parseConcatExpression() {
		Expression left = parseSubtractionExpression();
		if (left == null) return null;
		
		if (isChar('+')) {
			nextToken(); //consume +
			Expression right = parseConcatExpression();
			if (right == null) return null;
			
			left = new ConcatExpression(left, right);
		}
		
		return left;
	}
	
	/**
	 * Parses subtraction.
	 * Accepts grammar:
	 * statement - statement
	 */
	public Expression parseSubtractionExpression() {
		Expression left = parseMultiplicationExpression();
		if (left == null) return null;
		
		if (isChar('-')) {
			nextToken(); //consume -
			Expression right = parseSubtractionExpression();
			if (right == null) return null;
			
			left = new NumericOperationExpressions.Minus(left, right);
		}
		
		return left;
	}
	
	/**
	 * Parses multiplication.
	 * Accepts grammar:
	 * statement * statement
	 */
	public Expression parseMultiplicationExpression() {
		Expression left = parseDivisionExpression();
		if (left == null) return null;
		
		if (isChar('*')) {
			nextToken(); //consume *
			Expression right = parseMultiplicationExpression();
			if (right == null) return null;
			
			left = new NumericOperationExpressions.Multiply(left, right);
		}
		
		return left;
	}
	
	/**
	 * Parses division.
	 * Accepts grammar:
	 * statement / statement
	 */
	public Expression parseDivisionExpression() {
		Expression left = parseOtherExpression();
		if (left == null) return null;
		
		if (isChar('/')) {
			nextToken(); //consume /
			Expression right = parseDivisionExpression();
			if (right == null) return null;
			
			left = new NumericOperationExpressions.Divide(left, right);
		}
		
		return left;
	}

	/**
	 * Parses identifiers, numbers, strings, null, true, false, parenthesized expressions and member access.
	 */
	private Expression parseOtherExpression() {
		Expression returnValue = null;

		if (isType(NEW)) {
			nextToken(); //consume new
			Expression className = parseExpression();
			if (!(className instanceof IdentifierExpression)) showErrorMessage("Expected class name after 'new', received %s (%s)", className.toString(), className.getClass().getSimpleName());
			List<Expression> params = new ArrayList<>();
			expect('(', "Expected '(' after 'new' classname, received %s");
			nextToken(); //consume (
			while (!isChar(')')) {
				params.add(parseExpression());
				if (!isChar(')') && !isChar(',')) showErrorMessage("Expected ')' or ',' in 'new' param list, received %s", currentToken.toString());
			}
			nextToken(); //consume )
			
			return new NewInstanceExpression(className.toString(), params);
		} else if (isType(IDENTIFIER)) {
			returnValue = parseIdentifier();
		} else if (isType(NUMBER)) {
			returnValue = new NumberLiteralExpression(currentToken.getValue());
			nextToken(); //consume number
		} else if (isType(STRING)) {
			returnValue = new StringLiteralExpression(currentToken.getValue());
			nextToken(); //consume string
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
			nextToken(); //eat (
			returnValue = parseExpression();
			if (!isChar(')')) showErrorMessage("Expected ')' in parenthesized expression, received %s", currentToken.toString());
			nextToken(); // eat )
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
	 * Parses an identifier. An identifier is a string that matches [a-zA-Z][a-zA-Z0-9]*. This parser will also parse variable assignments and method calls because they come directly after an identifier.
	 * Accepts grammar:
	 * identifier [= expression|(args)]
	 */
	public Expression parseIdentifier() {
		OCToken prev = previousToken;
		String id = currentToken.getValue();
		nextToken();  //eat identifier.

		if (!isChar('(') && !isChar('=')) // If the next token isn't a ( or a =, we assume it is a reference to a variable or class
			return new IdentifierExpression(id);

		if (previousToken != null && (isType(DEF, prev) || isType(VAR, prev) || isType(NEW, prev))) //We want to ignore = and ( if we are in a function definition or variable definition
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
}
