package nl.thijsmolendijk.ollycode.runtime;

import java.util.HashMap;

import nl.thijsmolendijk.ollycode.ast.statement.ClassDefinitionStatement;

/**
 * Represents the OllyCode runtime.
 * @author molenzwiebel
 */
public class OCRuntime {
	/**
	 * A list of all the defined classes in the current runtime. Used for looking up parent classes.
	 */
	private HashMap<String, ClassDefinitionStatement> definedClasses;
	
	public OCRuntime() {
		this.definedClasses = new HashMap<>();
	}
	
	/**
	 * Adds a new class to the runtime
	 * @param stm the class to add
	 */
	public void defineClass(ClassDefinitionStatement stm) {
		if (definedClasses.containsKey(stm.getName())) throw new RuntimeException("Duplicate class "+stm.getName());
		definedClasses.put(stm.getName(), stm);
		System.out.println("[!] Defined class with name "+stm.getName());
	}
	
	/**
	 * Gets the class with the specified name from the defined class set. Throws exception if the class was not defined
	 * @param name the name of the class
	 * @return the class
	 */
	public ClassDefinitionStatement getClass(String name) {
		if (!definedClasses.containsKey(name)) throw new RuntimeException("Unknown class "+name);
		return definedClasses.get(name);
	}
}
