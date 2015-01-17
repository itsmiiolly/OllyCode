package nl.thijsmolendijk.ollycode.runtime;

/**
 * Represents a string object in the OllyCode runtime
 * @author molenzwiebel
 */
public class OCString extends OCObject {
	private String value;

    public OCString(String value) {
        this.value = value;
    }

    public OCString concat(OCString other) {
        return new OCString(value + other.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(OCObject object) {
    	OCString str = (OCString) object;
        return value.compareTo(str.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return compareTo((OCString) object) == 0;
    }
}
