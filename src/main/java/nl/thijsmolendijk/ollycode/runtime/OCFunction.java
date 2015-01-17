package nl.thijsmolendijk.ollycode.runtime;

import java.util.List;

import nl.thijsmolendijk.ollycode.ast.statement.BodyStatement;

/**
 * Represents a function in the OllyCode runtime
 * @author molenzwiebel
 */
public class OCFunction extends OCObject {
	private List<String> parameters;
	private BodyStatement body;
	
	public OCFunction(List<String> params, BodyStatement body) {
		this.parameters = params;
		this.body = body;
	}
	
	/**
	 * @return the body of this function
	 */
	public BodyStatement getBody() {
        return body;
    }

	/**
	 * @return the amount of parameters this function takes
	 */
    public int getParameterCount() {
        return parameters.size();
    }

    /**
     * @param index
     * @return the name of the param at the provided index
     */
    public String getParameterName(int index) {
        return parameters.get(index);
    }

	@Override
    public int compareTo(OCObject o) {
        throw new UnsupportedOperationException();
    }
}
