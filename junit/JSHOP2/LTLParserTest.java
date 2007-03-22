/**
 * 
 */
package JSHOP2;

import java.io.IOException;
import java.util.ArrayList;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import junit.framework.TestCase;

/**
 * @author Nathaniel Ayewah
 *
 */
public class LTLParserTest extends TestCase {

	InternalDomain idm = null;
	JSHOP2Parser parser = null;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		idm = null;
		parser = null;
	}
	
	/**
	 * Get an Internal domain primed for parsing the parseString
	 * @param parseString
	 */
	public JSHOP2Parser getParser(String parseString) {
		try {
			idm = new InternalDomain(parseString, -1);
			parser = idm.getParser();
			return parser;
		} catch (IOException e) {
			fail(e.getMessage());
			return null;
		}		
	}
	
	/**
	 * parse a string as an LTL constraint and return the resulting LTLExpression
	 */
	public LTLExpression parseConstraint(String parseString) {
		try {
			return getParser(parseString).constraint();			
		} 
		catch (RecognitionException e) { fail(e.getMessage()); } 
		catch (TokenStreamException e) { fail(e.getMessage()); }
		return null;
	}
	
	/**
	 * parse an invalid string as an LTL constraint and check that an exception is thrown
	 */
	public void parseConstraintWithException(String parseString) {
		try {
			getParser(parseString).constraint();	
			fail("Input String should throw exception: " + parseString);
		} 
		catch (RecognitionException e) { } // success 
		catch (TokenStreamException e) { } // success
	}
	
	public void checkConstraint(String parseString, String checkString) {
		LTLExpression lexp = parseConstraint(parseString);
		String parsed = lexp.toString();
		
		if (false) System.out.println(parsed);
		assertEquals(parsed, checkString);
	}
	
	// test that all the given strings can be parsed
	public void testGeneric() throws RecognitionException, TokenStreamException {
		// parenthesis not required around ltl expression...
		String parseString = "(:constraint (TRUE))";
		checkConstraint(parseString, "LTLTrue.getInstance()");
		
		parseString = "(:constraint ((TRUE)))";
		checkConstraint(parseString, "LTLTrue.getInstance()");

		// ... except when you want to put two ltlexpr next to each other 
		parseString = "(:constraint (AND (TRUE) (FALSE)))";
		checkConstraint(parseString, "new LTLConjunction(new LTLExpression[] {LTLTrue.getInstance(), LTLFalse.getInstance()})");
		
		parseString = "(:constraint (AND TRUE FALSE))"; // bad syntax: two ltls next to each other
		parseConstraintWithException(parseString);

		parseString = "(:constraint ((TRUE) (FALSE)))";
		checkConstraint(parseString, "new LTLConjunction(new LTLExpression[] {LTLTrue.getInstance(), LTLFalse.getInstance()})");
		
		parseString = "(:constraint (TRUE FALSE))"; // bad syntax: two ltls next to each other
		parseConstraintWithException(parseString);

		parseString = "(:constraint (TRUE) (FALSE))"; // ok
		checkConstraint(parseString, "new LTLConjunction(new LTLExpression[] {LTLTrue.getInstance(), LTLFalse.getInstance()})");

		parseString = "(:constraint TRUE FALSE)"; // bad syntax: two ltls next to each other
		parseConstraintWithException(parseString);

		parseString = "(:constraint (OR (TRUE) (FALSE)))";
		checkConstraint(parseString, "new LTLDisjunction(new LTLExpression[] {LTLTrue.getInstance(), LTLFalse.getInstance()})");

		parseString = "(:constraint (OR TRUE FALSE))"; // bad syntax: two ltls next to each other
		parseConstraintWithException(parseString);
		
		parseString = "(:constraint OR (TRUE) (FALSE))"; // ok
		checkConstraint(parseString, "new LTLDisjunction(new LTLExpression[] {LTLTrue.getInstance(), LTLFalse.getInstance()})");

		parseString = "(:constraint (:until (TRUE) (FALSE)))";
		checkConstraint(parseString, "new LTLUntil(LTLTrue.getInstance(), LTLFalse.getInstance())");

		parseString = "(:constraint :until (TRUE) (FALSE))"; // ok
		checkConstraint(parseString, "new LTLUntil(LTLTrue.getInstance(), LTLFalse.getInstance())");

		parseString = "(:constraint (:until TRUE FALSE))"; // bad syntax: two ltls next to each other
		parseConstraintWithException(parseString);

		parseString = "(:constraint (:until ((TRUE) (TRUE)) (FALSE)))";
		checkConstraint(parseString, "new LTLUntil(new LTLConjunction(new LTLExpression[] {LTLTrue.getInstance(), LTLTrue.getInstance()}), LTLFalse.getInstance())");

		parseString = "(:constraint (:until (TRUE) (TRUE) (FALSE)))"; // bad syntax: expect exactly two ltls
		parseConstraintWithException(parseString);

		// LTLAtom
		parseString = "(:constraint (isHot ?x))";
		checkConstraint(parseString, "new LTLAtom(new Predicate(0, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)))");

		parseString = "(:constraint (?x))"; // meaningless, variable is not bound to anything
		checkConstraint(parseString, "new LTLAtom(new Predicate(-1, 0, TermList.NIL))");

		parseString = "(:constraint ?x)"; // meaningless, variable is not bound to anything
		checkConstraint(parseString, "new LTLAtom(new Predicate(-1, 0, TermList.NIL))");

		parseString = "(:constraint (:next (TRUE) (TRUE)))";
		checkConstraint(parseString, "new LTLNext(new LTLConjunction(new LTLExpression[] {LTLTrue.getInstance(), LTLTrue.getInstance()}))");
	}
	
	public void testUntil() {
		String parseString = "(:constraint (:until ((TRUE) (TRUE)) (FALSE)))";
		LTLExpression lexp = parseConstraint(parseString);
		assertTrue(lexp instanceof LTLUntil);
		assertTrue(lexp.hasTemporalOperators());
		
		LTLUntil luntil = (LTLUntil) lexp;
		
		lexp = luntil.getFirstOperand();
		assertTrue(lexp instanceof LTLConjunction);
		assertFalse(lexp.hasTemporalOperators());

		lexp = luntil.getSecondOperand();
		assertTrue(lexp instanceof LTLFalse);
		assertFalse(lexp.hasTemporalOperators());
	}
	
	public void testLTLAtom() {
		String parseString = "(:constraint (ishot ?s b 5))";
		LTLExpression lexp = parseConstraint(parseString);
		assertTrue(lexp instanceof LTLAtom);
		assertFalse(lexp.hasTemporalOperators());		
		System.out.println(((LTLAtom) lexp).getAtom().toCode());
		System.out.println(((LTLAtom) lexp).getAtom().getVarCount());
		System.out.println(((LTLAtom) lexp).getAtom().getHead());
		System.out.println(((LTLAtom) lexp).getAtom().isVar());
		System.out.println(((LTLAtom) lexp).getAtom().isGround());

		int head = ((LTLAtom) lexp).getAtom().getHead();
		if (head >= 0) System.out.println(idm.getConstants().get(head));

		System.out.println(((LTLAtom) lexp).getAtom().getParam().getClass());
		TermList tl = (TermList) ((LTLAtom) lexp).getAtom().getParam();
		System.out.println(tl.getList().getHead().toCode());
	}
	
	public void testLTLForAll() {
		String parseString = "(:constraint (forall nil ?x true))";
		LTLExpression lexp = parseConstraint(parseString);
		assertTrue(lexp instanceof LTLForAll);
		assertFalse(lexp.hasTemporalOperators());		
		checkConstraint(parseString, "new LTLForAll(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");
		
		parseString = "(:constraint (forall (?x) ?x true))";
		checkConstraint(parseString, "new LTLForAll(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");

		parseString = "(:constraint (forall () ?x true))";
		checkConstraint(parseString, "new LTLForAll(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");

		parseString = "(:constraint (forall () (?x) true))";
		checkConstraint(parseString, "new LTLForAll(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");

		parseString = "(:constraint (forall () (?x) (true)))";
		checkConstraint(parseString, "new LTLForAll(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");
	}
	
	public void testLTLExists() {
		String parseString = "(:constraint (Exists nil ?x true))";
		LTLExpression lexp = parseConstraint(parseString);
		assertTrue(lexp instanceof LTLExists);
		assertFalse(lexp.hasTemporalOperators());		
		checkConstraint(parseString, "new LTLExists(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");
		
		parseString = "(:constraint (Exists (?x) ?x true))";
		checkConstraint(parseString, "new LTLExists(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");

		parseString = "(:constraint (EXISTS () ?x true))";
		checkConstraint(parseString, "new LTLExists(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");

		parseString = "(:constraint (exists () (?x) true))";
		checkConstraint(parseString, "new LTLExists(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");

		parseString = "(:constraint (exists () (?x) (true)))";
		checkConstraint(parseString, "new LTLExists(new LTLAtom(new Predicate(-1, 0, TermList.NIL)), LTLTrue.getInstance())");
	}
}
