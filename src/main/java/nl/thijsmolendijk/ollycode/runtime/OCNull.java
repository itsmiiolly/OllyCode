package nl.thijsmolendijk.ollycode.runtime;

/**
 * Represents null in the OllyCode runtime
 * @author molenzwiebel
 */
public class OCNull extends OCObject {	
	public static final OCNull INSTANCE = new OCNull();
	private OCNull() {}
	
	@Override
	public int compareTo(OCObject object) {
		return 0;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }
}
