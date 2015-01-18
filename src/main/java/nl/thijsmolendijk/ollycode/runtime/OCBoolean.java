package nl.thijsmolendijk.ollycode.runtime;

/**
 * Represents a boolean object in the OllyCode runtime
 * @author molenzwiebel
 */
public class OCBoolean extends OCObject {
	public static final OCBoolean TRUE = new OCBoolean(true);
	public static final OCBoolean FALSE = new OCBoolean(false);
	private boolean value;

	private OCBoolean(boolean value) {
		this.value = value;
	}

	/**
	 * @return the value of this boolean
	 */
	public boolean booleanValue() {
		return this.value;
	}

	/**
	 * Inverts the boolean and returns a new value
	 * @return the inverted boolean
	 */
	public OCBoolean not() {
		return valueOf(!this.value);
	}

	@Override
    public String toString() {
        return this == TRUE ? "true" : "false";
    }

    @Override
    public int compareTo(OCObject o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }

    /**
     * Fetches the static OCBoolean instance for the provided boolean value
     * @param value the value to initialize the boolean with
     * @return the new OCBoolean
     */
	public static OCBoolean valueOf(boolean value) {
		return value ? TRUE : FALSE;
	}
}
