package nl.thijsmolendijk.ollycode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.ast.expression.ReturnExpression;
import nl.thijsmolendijk.ollycode.ast.statement.BodyStatement;
import nl.thijsmolendijk.ollycode.ast.statement.FunctionStatement;
import nl.thijsmolendijk.ollycode.lexing.OCToken;
import nl.thijsmolendijk.ollycode.lexing.OCTokenType;
import nl.thijsmolendijk.ollycode.parsing.OCParser;

/**
 * A simple OllyCode ReadParsePrintLoop. The class simply reads from stdin and parses it using the {@link OCParser}. If parsed correctly, it will return the exact input.
 * @author molenzwiebel
 */
public class OllyCodeRPPL {
	public static void main(String... args) throws Exception {
		System.out.println("[!] OllyCode RPPL (Read Parse Print Loop) v0.1-SNAPSHOT");
		System.out.println("[!] (c) 2015 by molenzwiebel and ItsMiiOlly");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		
		System.out.print("> ");
		while ((line = br.readLine()) != null) {
		    OCParser parser = new OCParser(line);
		    OCToken currentToken = parser.getCurrentToken();
		    if (currentToken.getType() == OCTokenType.EOF) break;
		    else if (currentToken.getType() == OCTokenType.CLASS) handleCls(parser);
		    else if (currentToken.getType() == OCTokenType.DEF) handleDefinition(parser);
		    else if (currentToken.getType() == OCTokenType.CHARACTER && currentToken.getValue().equals(';')) System.out.println("; detected, doing nothin'");
		    else handleTopLevelExpression(parser);
		    System.out.print("> ");
		}
	}
	
	private static void handleCls(OCParser parser) {
		Statement result = parser.parseClass();
		if (result != null) {
			System.out.println("\033[32m[+] Parsed class: \n\033[0m"+result);
		} else {
			parser.nextToken(); //Advance to the next token if something is messed up
		}
	}

	private static void handleDefinition(OCParser parser) {
		Statement result = parser.parseFunctionDefinition();
		if (result != null) {
			System.out.println("\033[32m[+] Parsed function definition: \n\033[0m"+result);
		} else {
			parser.nextToken(); //Advance to the next token if something is messed up
		}
	}

	private static void handleTopLevelExpression(OCParser parser) {
		Expression result = parser.parseExpression();
		if (result != null) {
			result = new ReturnExpression(result);
			System.out.println("\033[32m[+] Parsed top level expression (wrapped in function): \n\033[0m"+new FunctionStatement("", new ArrayList<>(), new BodyStatement(Arrays.asList(result))));
		} else {
			parser.nextToken(); //Advance to the next token if something is messed up
		}
	}
}
