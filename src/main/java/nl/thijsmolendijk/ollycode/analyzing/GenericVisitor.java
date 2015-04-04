package nl.thijsmolendijk.ollycode.analyzing;

import java.lang.reflect.Method;

public abstract class GenericVisitor<ReturnType> {
	
	@SuppressWarnings("unchecked")
	public ReturnType accept(Object... args) {
		if (args.length == 0) throw new IllegalArgumentException("Expected object to accept");
		try {
			Method[] methods = this.getClass().getDeclaredMethods();
			for (Method m : methods) {
				if (!m.getName().equalsIgnoreCase("visit")) continue;
				Class<?>[] params = m.getParameterTypes();
				if (params.length != args.length) continue;
				if (!params[0].isAssignableFrom(args[0].getClass())) continue;
				m.setAccessible(true);
				return (ReturnType) m.invoke(this, args);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error while attempting to visit "+args[0], ex);
		}
		throw new RuntimeException("No visit method for "+args[0].getClass()+" in "+this.getClass().getSimpleName());
	}
	
}
