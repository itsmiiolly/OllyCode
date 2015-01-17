package nl.thijsmolendijk.ollycode.runtime;

/**
 * Superclass of any variable type available at runtime.
 * @author molenzwiebel
 */
public abstract class OCObject implements Comparable<OCObject> {
	public boolean isNumber() {
		return this instanceof OCNumber;
	}
	
	public OCNumber toNumber() {
		if (isNumber()) {
			return (OCNumber) this;
		}
		throw new RuntimeException("Expected number, received" + this);
	}
	
	public boolean isBoolean() {
		return this instanceof OCBoolean;
	}

	public OCBoolean toBoolean() {
		if (isBoolean()) {
			return (OCBoolean) this;
		}
		throw new RuntimeException("Expected boolean, received" + this);
	}
	
	public boolean isString() {
		return this instanceof OCString;
	}

	public OCString toOCString() {
		if (isString())
			return (OCString) this;
		// Implicit converting of types to string
		return new OCString(this.toString());
	}
}
