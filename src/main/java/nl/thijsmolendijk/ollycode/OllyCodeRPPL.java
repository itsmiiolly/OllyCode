package nl.thijsmolendijk.ollycode;

import nl.thijsmolendijk.ollycode.parsing.OCParser;

/**
 * A simple OllyCode ReadParsePrintLoop. The class simply reads from stdin and parses it using the {@link OCParser}. If parsed correctly, it will return the exact input.
 * @author molenzwiebel
 */
public class OllyCodeRPPL {
	public static void main(String... args) throws Exception {
		System.out.println("[!] OllyCode RPPL (Read Parse Print Loop) v0.1-SNAPSHOT");
		System.out.println("[!] (c) 2015 by molenzwiebel and ItsMiiOlly");
		
		System.out.println("WIP: At the moment not exactly working (as in, not at all)");
	}
		
		/*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		OCRuntime runtime = new OCRuntime();
		
		System.out.print("> ");
		while ((line = br.readLine()) != null) {
		    OCParser parser = new OCParser(line);
		    OCToken currentToken = parser.getCurrentToken();
		    if (currentToken.getType() == OCTokenType.EOF) break;
		    else if (currentToken.getType() == OCTokenType.CLASS) handleCls(runtime, parser);
		    else if (currentToken.getType() == OCTokenType.DEF) handleDefinition(runtime, parser);
		    else if (currentToken.getType() == OCTokenType.CHARACTER && currentToken.getValue().equals(';')) System.out.println("; detected, doing nothin'");
		    else handleTopLevelExpression(runtime, parser);
		    System.out.print("> ");
		}
	}
	
	private static void handleCls(OCRuntime rt, OCParser parser) {
		Statement result = parser.parseClassStatement();
		if (result != null) {
			rt.defineClass((ClassDefinitionStatement) result);
			System.out.println("\033[32m[+] Parsed class. \n\033[0m");
		} else {
			parser.nextToken(); //Advance to the next token if something is messed up
		}
	}

	private static void handleDefinition(OCRuntime rt, OCParser parser) {
		FunctionStatement result = (FunctionStatement) parser.parseFunctionDefinitionStatement();
		if (result != null) {
			FunctionStatement newFunc = new FunctionStatement("create", new ArrayList<>(), new BodyStatement(new ArrayList<>()));
			ClassDefinitionStatement anonClazz = new ClassDefinitionStatement("AnonClass", new ArrayList<>(), new ArrayList<>(), Arrays.asList(newFunc, result));
			rt.defineClass(anonClazz);
			NewInstanceExpression newExpr = new NewInstanceExpression("AnonClass", new ArrayList<>());
			OCInstance instance = (OCInstance) newExpr.eval(new Interpreter(rt));
			System.out.println("\033[32m[+] Parsed function definition. Result: \n\033[0m"+instance.getInterpreter().invokeFunction(result.getName(), new ArrayList<>()));
		} else {
			parser.nextToken(); //Advance to the next token if something is messed up
		}
	}

	private static void handleTopLevelExpression(OCRuntime rt, OCParser parser) {
		ASTElement result = parser.parseExpression();
		if (result != null) {
			result = new ReturnStatement((Expression) result);
			System.out.println("\033[32m[+] Parsed top level expression (wrapped in function): \n\033[0m"+new FunctionStatement("", new ArrayList<>(), new BodyStatement(Arrays.asList(result))));
		} else {
			parser.nextToken(); //Advance to the next token if something is messed up
		}
	}*/
}
