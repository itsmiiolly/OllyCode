package nl.thijsmolendijk.ollycode.ast;

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

public interface ASTVisitor<T> {
	public T visitNode(BooleanExpression expr);
	public T visitNode(FunctionCallExpression expr);
	public T visitNode(IdentifierExpression expr);
	public T visitNode(MemberExpression expr);
	public T visitNode(NewInstanceExpression expr);
	public T visitNode(NullExpression expr);
	public T visitNode(NumberLiteralExpression expr);
	public T visitNode(StringLiteralExpression expr);
	public T visitNode(VariableAssignmentExpression expr);
	
	public T visitNode(ConcatExpression expr);
	public T visitNode(NumericOperationExpressions.Divide expr);
	public T visitNode(NumericOperationExpressions.Minus expr);
	public T visitNode(NumericOperationExpressions.Multiply expr);
	public T visitNode(ValueComparisonExpressions.And expr);
	public T visitNode(ValueComparisonExpressions.Or expr);
	public T visitNode(ValueComparisonExpressions.Compare expr);
	public T visitNode(ValueComparisonExpressions.GreaterThan expr);
	public T visitNode(ValueComparisonExpressions.LesserThan expr);
	
	public T visitNode(BodyStatement stmt);
	public T visitNode(ClassDefinitionStatement stmt);
	public T visitNode(ForStatement stmt);
	public T visitNode(FunctionStatement stmt);
	public T visitNode(IfStatement stmt);
	public T visitNode(ReturnStatement stmt);
	public T visitNode(VariableDefinitionStatement stmt);
	public T visitNode(WhileStatement stmt);
}
