package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;

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
}
