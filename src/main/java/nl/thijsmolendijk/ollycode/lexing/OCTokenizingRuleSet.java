package nl.thijsmolendijk.ollycode.lexing;

import java.util.HashMap;

/**
 * Class that stores which identifiers or characters return which {@link OCTokenType}s
 * @author molenzwiebel
 */
public class OCTokenizingRuleSet {
	private HashMap<String, OCTokenType> identifiers;
	private HashMap<Character, OCTokenType> characters;
	
	public OCTokenizingRuleSet() {
		this.identifiers = new HashMap<>();
		this.characters = new HashMap<>();
	}
	
	/**
	 * Adds a new identifier to {@link OCTokenType} mapping
	 * @param id the identifier
	 * @param type the type
	 */
	public OCTokenizingRuleSet identifier(String id, OCTokenType type) {
		if (identifiers.containsKey(id)) throw new IllegalArgumentException("OCTokenizingRuleSet already contains identifier -> OCTokenType mapping for identifier "+id);
		identifiers.put(id, type);
		return this;
	}
	
	/**
	 * Adds a new character to {@link OCTokenType} mapping
	 * @param id the identifier
	 * @param type the type
	 */
	public OCTokenizingRuleSet character(char id, OCTokenType type) {
		if (characters.containsKey(id)) throw new IllegalArgumentException("OCTokenizingRuleSet already contains character -> OCTokenType mapping for character "+id);
		characters.put(id, type);
		return this;
	}
	
	/**
	 * Gets the registered {@link OCTokenType} for the specified identifier, or null if not registered
	 * @param id the identifier
	 * @return the type
	 */
	public OCTokenType forIdentifier(String id) {
		return identifiers.get(id);
	}
	
	/**
	 * Gets the registered {@link OCTokenType} for the specified identifier, or null if not registered
	 * @param id the identifier
	 * @return the type
	 */
	public OCTokenType forChar(char id) {
		return characters.get(id);
	}
}
