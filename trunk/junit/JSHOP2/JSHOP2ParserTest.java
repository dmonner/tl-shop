/**
 * 
 */
package JSHOP2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import junit.framework.TestCase;

/**
 * Basic Tests for JSHOPParser
 * 
 * @author Nathaniel Ayewah
 *
 */
public class JSHOP2ParserTest extends TestCase {
	
	/**
	 * Get an Internal domain primed for parsing the parseString
	 * @param parseString
	 */
	public static InternalDomain getInternalDomain(String parseString) {
		try {
			return new InternalDomain(parseString, -1);
		} catch (IOException e) {
			fail(e.getMessage());
			return null;
		}		
	}
	
	public void testLogicalAtom() throws RecognitionException, TokenStreamException {		
		String parseString = "(ishot ?x)";
		InternalDomain idm = getInternalDomain(parseString);
		
		Predicate p = idm.getParser().la();
		assertEquals(p.getVarCount(), 1);
		
		Term t = p.getParam();
		assertFalse(t.isGround());
		assertFalse(t.isNil());
				
		System.out.println( t.toCode() );
	}
	
	public void testOperator() throws RecognitionException, TokenStreamException {
		String parseString = "(:operator (!pickup ?a) () () ((have ?a)))";
		InternalDomain idm = getInternalDomain(parseString);
		
		idm.getParser().op();
		for (Iterator iter = idm.getOperators().iterator(); iter.hasNext(); ) {
			Object o = iter.next();
			assertTrue(o instanceof InternalOperator);
			InternalOperator iop = (InternalOperator) o;
			Predicate h = iop.getHead();
			
			//System.out.println(h.getVarCount());
			//iop.getHead();
			//System.out.println( iop.toCode() );		
		}
	}

	public void testLogicalExpressionConjunction() throws RecognitionException, TokenStreamException {		
		String parseString = "(AND (ishot ?x) (iscold ?y) (OR (iswarm ?z) (ishot ?a)))";
		InternalDomain idm = getInternalDomain(parseString);
		
		LogicalExpression e = idm.getParser().le();
		assertTrue(e instanceof LogicalExpressionConjunction);
		
		LogicalExpressionConjunction lc = (LogicalExpressionConjunction) e;
		
		assertEquals(lc.getLe().length, 3);
		assertTrue(lc.getLe()[0] instanceof LogicalExpressionAtomic);
		assertTrue(lc.getLe()[1] instanceof LogicalExpressionAtomic);
		assertTrue(lc.getLe()[2] instanceof LogicalExpressionDisjunction);
	}
}
