package nl.thijsmolendijk.ollycode.ast.expression;

import java.util.stream.Collectors;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCInstance;
import nl.thijsmolendijk.ollycode.runtime.OCNull;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

/**
 * Represents the accessing or calling of a member.
 * For example: in myPlayer.name, myPlayer will be the parent and name will be the member
 * The parser will automatically parse chained member calls into a series of MemberExpressions:
 * Bukkit.getPluginManager().registerEvents() creates 2 member expressions in the following configuration:
 * MemberExpression{parent=MemberExpression{parent=Bukkit,member=getPluginManager()},member=registerEvents()}
 * @author molenzwiebel
 */
public class MemberExpression implements Expression {
	private Expression parent;
	private Expression member;
	
	public MemberExpression(Expression p, Expression m) {
		this.parent = p;
		this.member = m;
	}
	
	@Override
	public String toString() {
		return parent + "." + member;
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		OCObject actor = parent.eval(interpreter);
		if (!(actor instanceof OCInstance)) throw new RuntimeException("Expected member expression to call method or get variable on an object");
		OCInstance instance = (OCInstance) actor;
		if (member instanceof FunctionCallExpression) {
			FunctionCallExpression fc = (FunctionCallExpression) member;
			return instance.getInterpreter().invokeFunction(fc.functionName, fc.arguments.stream().map(x -> x.eval(interpreter)).collect(Collectors.toList()));
		}
		if (member instanceof IdentifierExpression) {
			return instance.getInterpreter().getVariable(member.toString());
		}
		return OCNull.INSTANCE;
	}
}
