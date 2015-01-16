package nl.thijsmolendijk.ollycode.lexing;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Class that stores which identifiers or characters return which {@link OCTokenType}s
 * @author molenzwiebel
 */
public class OCTokenizingRuleSet {
	private HashMap<String, OCTokenType> identifiers;
	private HashMap<Character, OCTokenType> characters;
	private HashMap<String, OCTokenType> substrings;
	private int storedLongestSubstring = -1;
	
	public OCTokenizingRuleSet() {
		this.identifiers = new HashMap<>();
		this.characters = new HashMap<>();
		this.substrings = new HashMap<>();
	}
	
	/**
	 * @return the longest key in {@code substrings} so we only substring what we need to
	 */
	public int longestSubstring() {
		if (storedLongestSubstring != -1) return storedLongestSubstring;
		for (String key : substrings.keySet()) {
			if (key.length() > storedLongestSubstring) storedLongestSubstring = key.length();
		}
		return storedLongestSubstring;
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
	 * Adds a new substring matcher to {@link OCTokenType} mapping
	 * @param substr the substring
	 * @param type the type
	 */
	public OCTokenizingRuleSet substring(String substr, OCTokenType type) {
		substrings.put(substr, type);
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
	 * Gets the registered {@link OCTokenType} for the specified substring, or null if not registered
	 * @param token the operator
	 * @param nextToken the next token
	 * @return the type and the size of the substring
	 */
	public Pair<OCTokenType, Integer> forSubstring(String str) {
		if (str.isEmpty()) return null;
		Entry<String, OCTokenType> entry = substrings.entrySet().stream().filter(x -> x.getKey().startsWith(str)).findFirst().orElse(null);
		if (entry == null) return null;
		return Pair.of(entry.getValue(), entry.getKey().length());
	}
	
	/**
	 * Gets the registered {@link OCTokenType} for the specified identifier, or null if not registered
	 * @param id the identifier
	 * @return the type
	 */
	public OCTokenType forChar(char id) {
		return characters.get(id);
	}

	@Override
	public String toString() {
		return String.format("OCTokenizingRuleSet{identifiers=%s, characters=%s}", identifiers, characters);
	}
}
