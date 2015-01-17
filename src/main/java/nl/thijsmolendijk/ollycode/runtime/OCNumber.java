package nl.thijsmolendijk.ollycode.runtime;

/**
 * Represents a numeric object in the OllyCode runtime
 * @author molenzwiebel
 */
public class OCNumber extends OCObject {
	private Double value;
	
	public OCNumber(double val) {
		this.value = val;
	}
	
	/**
	 * @return the double value of this number
	 */
	public double doubleValue() {
		return value.doubleValue();
	}
	
	@Override
	public int compareTo(OCObject object) {
		OCNumber number = (OCNumber) object;
        return value.compareTo(number.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object object) {
        return compareTo((OCNumber) object) == 0;
    }
}
