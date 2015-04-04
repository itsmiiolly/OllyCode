package nl.thijsmolendijk.ollycode.parsing;

import static nl.thijsmolendijk.ollycode.lexer.OCTokenType.*;
import static nl.thijsmolendijk.ollycode.parsing.ast.expr.BinaryExpression.*;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

import nl.thijsmolendijk.ollycode.lexer.OCLexer;
import nl.thijsmolendijk.ollycode.lexer.OCToken;
import nl.thijsmolendijk.ollycode.lexer.OCTokenType;
import nl.thijsmolendijk.ollycode.parsing.NodeParser.InfixNodeParser;
import nl.thijsmolendijk.ollycode.parsing.PrecedenceAwareNodeParser.SimplePrecedenceParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Expression;
import nl.thijsmolendijk.ollycode.parsing.ast.Node;
import nl.thijsmolendijk.ollycode.parsing.ast.Statement;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.AssignmentExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.ConcatExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.IdentifierExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.LiteralExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.NewExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.NumericOperationExpressions;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.ValueComparisonExpressions;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.BodyStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.ClassStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.ForStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.FunctionStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.IfStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.ReturnStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.VariableDefinitionStatement;

public class OCParser {
	private HashMap<Predicate<OCToken>, NodeParser> expressionParsers = new HashMap<>();
	private HashMap<Predicate<OCToken>, NodeParser> statementParsers = new HashMap<>();
	private HashMap<Predicate<OCToken>, PrecedenceAwareNodeParser> infixParsers = new HashMap<>();
	
	private OCLexer lexer;
	private OCToken currentToken;
	
	public OCParser(String source) {
		this.lexer = new OCLexer(source);
		
		expression(STRING, LiteralExpression.Text::parse);
		expression(t -> t.isType(TRUE) || t.isType(FALSE), LiteralExpression.Boolean::parse);
		expression(NUMBER, LiteralExpression.Number::parse);
		expression(NULL, LiteralExpression.Null::parse);
		expression(IDENTIFIER, IdentifierExpression::parse); //Also parses function calls
		expression(NEW, NewExpression::parse);
		expression('(', (p) -> {
			p.nextToken(); //consume (
			Expression expr = p.parseExpression();
			if (!p.currentToken.isChar(')')) throw new RuntimeException("Wut? Didn't end with )");
			p.nextToken(); //consume )
			return expr;
		});
		
		infix(ASSIGN, 10, buildParser(AssignmentExpression.class, 10));
		infix('+', ConcatExpression.PRECEDENCE, buildParser(ConcatExpression.class, ConcatExpression.PRECEDENCE));
		infix('-', NumericOperationExpressions.Minus.PRECEDENCE, buildParser(NumericOperationExpressions.Minus.class, NumericOperationExpressions.Minus.PRECEDENCE));
		infix('/', NumericOperationExpressions.Divide.PRECEDENCE, buildParser(NumericOperationExpressions.Divide.class, NumericOperationExpressions.Divide.PRECEDENCE));
		infix('*', NumericOperationExpressions.Multiply.PRECEDENCE, buildParser(NumericOperationExpressions.Multiply.class, NumericOperationExpressions.Multiply.PRECEDENCE));
		infix(AND, ValueComparisonExpressions.And.PRECEDENCE, buildParser(ValueComparisonExpressions.And.class, ValueComparisonExpressions.Or.PRECEDENCE));
		infix(OR, ValueComparisonExpressions.Or.PRECEDENCE, buildParser(ValueComparisonExpressions.Or.class, ValueComparisonExpressions.And.PRECEDENCE));
		infix(EQ, ValueComparisonExpressions.Compare.PRECEDENCE, buildParser(ValueComparisonExpressions.Compare.class, ValueComparisonExpressions.Compare.PRECEDENCE, true));
		infix(N_EQ, ValueComparisonExpressions.Compare.PRECEDENCE, buildParser(ValueComparisonExpressions.Compare.class, ValueComparisonExpressions.Compare.PRECEDENCE, false));
		infix(GT, ValueComparisonExpressions.GreaterThan.PRECEDENCE, buildParser(ValueComparisonExpressions.GreaterThan.class, ValueComparisonExpressions.GreaterThan.PRECEDENCE, false));
		infix(GT_EQ, ValueComparisonExpressions.GreaterThan.PRECEDENCE, buildParser(ValueComparisonExpressions.GreaterThan.class, ValueComparisonExpressions.GreaterThan.PRECEDENCE, true));
		infix(LT, ValueComparisonExpressions.LesserThan.PRECEDENCE, buildParser(ValueComparisonExpressions.LesserThan.class, ValueComparisonExpressions.LesserThan.PRECEDENCE, false));
		infix(LT_EQ, ValueComparisonExpressions.LesserThan.PRECEDENCE, buildParser(ValueComparisonExpressions.LesserThan.class, ValueComparisonExpressions.LesserThan.PRECEDENCE, true));
		
		statement(IF, IfStatement::parse);
		statement(DEF, FunctionStatement::parse);
		statement('{', BodyStatement::parse);
		statement(FOR, ForStatement::parse);
		statement(VAR, VariableDefinitionStatement::parse);
		statement(CLASS, ClassStatement::parse);
		statement(RETURN, ReturnStatement::parse);
		
		nextToken();
	}

	/**
	 * Registers a new parser with the provided predicate
	 * @param pred the predicate
	 * @param parser the parser
	 */
	protected void expression(Predicate<OCToken> pred, NodeParser parser) {
		expressionParsers.put(pred, parser);
	}
	
	/**
	 * @see #expression(Predicate, NodeParser)
	 */
	protected void expression(OCTokenType type, NodeParser parser) {
		expression(x -> x.isType(type), parser);
	}
	
	/**
	 * @see #expression(Predicate, NodeParser)
	 */
	protected void expression(char c, NodeParser parser) {
		expression(x -> x.isChar(c), parser);
	}
	
	/**
	 * Registers a new parser with the provided predicate
	 * @param pred the predicate
	 * @param parser the parser
	 */
	protected void statement(Predicate<OCToken> pred, NodeParser parser) {
		statementParsers.put(pred, parser);
	}
	
	/**
	 * @see #statement(Predicate, NodeParser)
	 */
	protected void statement(OCTokenType type, NodeParser parser) {
		statement(x -> x.isType(type), parser);
	}
	
	/**
	 * @see #statement(Predicate, NodeParser)
	 */
	protected void statement(char c, NodeParser parser) {
		statement(x -> x.isChar(c), parser);
	}
	
	/**
	 * Registers a new infix parser with the provided predicate
	 * @param pred the predicate
	 * @param precedence the precedence of the infix
	 * @param parser the parser
	 */
	protected void infix(Predicate<OCToken> pred, int precedence, InfixNodeParser parser) {
		infixParsers.put(pred, new SimplePrecedenceParser(parser, precedence));
	}
	
	/**
	 * @see #infix(Predicate, int, NodeParser)
	 */
	protected void infix(OCTokenType type, int precedence, InfixNodeParser parser) {
		infix(x -> x.isType(type), precedence, parser);
	}
	
	/**
	 * @see #infix(Predicate, int, NodeParser)
	 */
	protected void infix(char c, int precedence, InfixNodeParser parser) {
		infix(x -> x.isChar(c), precedence, parser);
	}
	
	public OCToken nextToken() {
		return currentToken = lexer.nextToken();
	}
	
	public OCToken currentToken() {
		return currentToken;
	}

	public OCToken consume() {
		OCToken current = currentToken;
		nextToken();
		return current;
	}
	
	public Node parseNode() {		
		Expression expr = parseExpression(0);
		if (expr != null) return expr;
				
		Statement stmt = parseStatement();
		if (stmt != null) return stmt;
		
		return null;
	}
	
	/**
	 * Parses a statement
	 */
	public Statement parseStatement() {
		Optional<Entry<Predicate<OCToken>, NodeParser>> expr = statementParsers.entrySet().stream().filter(x -> x.getKey().test(currentToken)).findFirst();
		if (!expr.isPresent()) return null;
		
		Node value = expr.get().getValue().parse(this);
		if (value instanceof Expression) return null;
		return (Statement) value;
	}
	
	/**
	 * Parses an expression
	 */
	public Expression parseExpression() {
		return parseExpression(0);
	}

	/**
	 * Parses an expression
	 * @param precedence the precedence (used as a part of the Pratt parser)
	 */
	public Expression parseExpression(int precedence) {
		Optional<Entry<Predicate<OCToken>, NodeParser>> expr = expressionParsers.entrySet().stream().filter(x -> x.getKey().test(currentToken)).findFirst();
		if (!expr.isPresent()) return null;

		Node value = expr.get().getValue().parse(this);
		if (value instanceof Statement) return null;
		Expression left = (Expression) value;
		while (precedence < getPrecedence()) {
			Optional<Entry<Predicate<OCToken>, PrecedenceAwareNodeParser>> parser = infixParsers.entrySet().stream().filter(x -> x.getKey().test(currentToken)).findFirst();
			left = (Expression) parser.get().getValue().parse(this, left);
		}
		return left;
	}
	
	/**
	 * @return the precedence of the next token
	 */
	private int getPrecedence() {
		Optional<Entry<Predicate<OCToken>, PrecedenceAwareNodeParser>> parser = infixParsers.entrySet().stream().filter(x -> x.getKey().test(currentToken)).findFirst();
		if (parser.isPresent()) return parser.get().getValue().getPrecedence();
		return 0;
	}
}
