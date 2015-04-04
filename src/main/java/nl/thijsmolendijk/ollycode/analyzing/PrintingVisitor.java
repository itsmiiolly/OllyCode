package nl.thijsmolendijk.ollycode.analyzing;

import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.parsing.ast.Node;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.BinaryExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.CallExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.IdentifierExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.LiteralExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.MemberExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.expr.NewExpression;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.BodyStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.ClassStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.ForStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.FunctionStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.IfStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.ReturnStatement;
import nl.thijsmolendijk.ollycode.parsing.ast.stmt.VariableDefinitionStatement;

import org.apache.commons.lang3.StringUtils;

public class PrintingVisitor extends GenericVisitor<Void> {
	private int indentation = 0;
	
	private void indent() {
		indentation += 4;
	}
	
	private void outdent() {
		indentation -= 4;
	}
	
	private void print(String text) {
		System.out.println(StringUtils.repeat(' ', indentation)+text);
	}
	
	private void visit(BinaryExpression expr) {
		print(expr.left + " " + expr.operation + " " + expr.right);
	}
	
	private void visit(CallExpression expr) {
		print(expr.methodName + "(" + expr.callArgs.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")");
	}
	
	private void visit(IdentifierExpression expr) {
		print(expr.name);
	}
	
	private void visit(LiteralExpression.Boolean expr) {
		print(expr.value + "");
	}
	
	private void visit(LiteralExpression.Null expr) {
		print("null");
	}
	
	private void visit(LiteralExpression.Number expr) {
		print(expr + "");
	}
	
	private void visit(LiteralExpression.Text expr) {
		print("\"" + expr.value + "\"");
	}
	
	private void visit(MemberExpression expr) {
		print(expr.parent + "." + expr.child);
	}
	
	private void visit(NewExpression expr) {
		print("new "+expr.name+"("+expr.args.stream().map(Object::toString).collect(Collectors.joining(", "))+")");
	}
	
	private void visit(BodyStatement expr) {
		for (Node n : expr.body) {
			accept(n);
		}
	}
	
	private void visit(ClassStatement expr) {
		print("class "+expr.name+(expr.superclass == null ? "" : " :: "+expr.superclass)+" {");
		indent();
		for (VariableDefinitionStatement stmt : expr.classVariables) {
			accept(stmt);
		}
		if (expr.classVariables.size() > 0) print("");
		for (FunctionStatement stmt : expr.functions) {
			accept(stmt);
			print(""); //Newline between functions
		}
		outdent();
		print("}");
	}
	
	private void visit(ForStatement stmt) {
		print("for ("+stmt.initialization+", "+stmt.condition+", "+stmt.step+") {");
		indent();
		accept(stmt.body);
		outdent();
		print("}");
	}
	
	private void visit(FunctionStatement stmt) {
		print("def "+stmt.name+"("+stmt.argumentNames.stream().collect(Collectors.joining(", "))+") {");
		indent();
		accept(stmt.body);
		outdent();
		print("}");
	}
	
	private void visit(IfStatement stmt) {
		print("if ("+stmt.condition+") {");
		indent();
		accept(stmt.ifThen);
		outdent();
		if (stmt.ifElse != null) {
			print("} else {");
			indent();
			accept(stmt.ifElse);
			outdent();
		}
		print("}");
	}
	
	private void visit(ReturnStatement stmt) {
		print("return"+(stmt.value != null ? " "+stmt.value : ""));
	}
	
	private void visit(VariableDefinitionStatement stmt) {
		print("var "+stmt.name+(stmt.initialValue != null ? " = "+stmt.initialValue : ""));
	}
}
