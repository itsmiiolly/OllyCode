package nl.thijsmolendijk.ollycode.runtime;

import nl.thijsmolendijk.ollycode.ast.statement.ClassDefinitionStatement;
import nl.thijsmolendijk.ollycode.ast.statement.FunctionStatement;
import nl.thijsmolendijk.ollycode.ast.statement.VariableDefinitionStatement;

/**
 * Represents an instance of a class defined in OllyCode.
 * @author molenzwiebel
 */
public class OCInstance extends OCObject {
	private String className;
	/**
	 * The interpreter for this instance
	 */
	private Interpreter interpreter;
	
	public OCInstance(OCRuntime runtime, ClassDefinitionStatement clazz) {
		className = clazz.getName();
		interpreter = new Interpreter(runtime);
		for (FunctionStatement statement : clazz.getDefinedFunctions()) {
			interpreter.addFunction((OCFunction) statement.eval(interpreter));
		}
		for (VariableDefinitionStatement st : clazz.getInstanceVariableDefinitions()) {
			st.eval(interpreter);
		}
		
		for (String superClass : clazz.getSuperclasses()) {
			ClassDefinitionStatement superclazz = runtime.getClass(superClass);
			for (FunctionStatement statement : superclazz.getDefinedFunctions()) {
				if (interpreter.getFunction(statement.getName(), statement.getArgCount()) != OCNull.INSTANCE && !statement.getName().equals("create")) throw new RuntimeException("Duplicate method "+statement.getName()+" in class "+className+" or superclasses");
				interpreter.addFunction((OCFunction) statement.eval(interpreter));
			}
			
			for (VariableDefinitionStatement st : superclazz.getInstanceVariableDefinitions()) {
				interpreter.setVariable(st.name, st.eval(interpreter));
			}
		}
	}
	
	/**
	 * @return the interpreter for this instance
	 */
	public Interpreter getInterpreter() {
		return interpreter;
	}

	@Override
	public int compareTo(OCObject object) {
		throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return interpreter.hashCode() * 2;
    }

    @Override
    public String toString() {
        return className + "@" + hashCode();
    }
}
