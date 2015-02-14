package nl.thijsmolendijk.ollycode.ast.statement;

import java.util.List;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Represents an if statement in ollycode. The statement has one if condition, optionally one else condition and unlimited elseif conditions.
 * @author molenzwiebel
 */
public class IfStatement implements Statement {
	private Expression condition;
	private BodyStatement ifThen;
	private List<Pair<Expression, BodyStatement>> elseifs;
	private BodyStatement ifElse;

	public IfStatement(Expression cond, BodyStatement th, List<Pair<Expression, BodyStatement>> elseifs, BodyStatement el) {
		this.condition = cond;
		this.ifThen = th;
		this.ifElse = el;
		this.elseifs = elseifs;
	}

	@Override
	public String toString() {
		if (elseifs.size() == 0) {
			return ifElse == null ? "if (" + condition + ") {\n" + ifThen + "\n}" : "if (" + condition + ") {\n" + ifThen + "\n} else {\n" + ifElse + "\n}";
		} else {
			StringBuilder builder = new StringBuilder("if (").append(condition).append(") {\n").append(ifThen).append("\n}");
			for (Pair<Expression, BodyStatement> elseif : elseifs) {
				builder = builder.append(" elseif (").append(elseif.getKey()).append(") {\n").append(elseif.getValue()).append("\n}");
			}
			if (ifElse != null) {
				builder = builder.append(" else {\n").append(ifElse).append("\n}");
			}
			return builder.toString();
		}
	}

	@Override
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}