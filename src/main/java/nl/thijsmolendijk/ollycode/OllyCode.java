package nl.thijsmolendijk.ollycode;

import nl.thijsmolendijk.ollycode.analyzing.PrintingVisitor;
import nl.thijsmolendijk.ollycode.parsing.OCParser;
import nl.thijsmolendijk.ollycode.parsing.ast.Node;

public class OllyCode {
	public static void main(String... args) {
		String source = 
		  "class TestClass :: TestSuper {"
		+ "  def test(a, b, c) {"
		+ "    if (x == 4) {"
		+ "      return a"
		+ "    } else {"
		+ "      for (x = 4, true,) {"
		+ "        return b"
		+ "      }"
		+ "      return"
		+ "    }"
		+ "  }"
		+ ""
		+ "  var myClassVar1 = 10"
		+ "  var myClassVar2 = 10"
		+ "  var myClassVar3 = 10"
		+ "}";
		
		OCParser p = new OCParser(source);
		PrintingVisitor v = new PrintingVisitor();
		Node n;
		while ((n = p.parseNode()) != null) {
			System.out.println(n.getClass().getSimpleName()+":");
			v.accept(n);
			System.out.println();
		}
	}
}
