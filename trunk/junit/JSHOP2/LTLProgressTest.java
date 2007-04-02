package JSHOP2;

import junit.framework.TestCase;

/**
 * @author Derek Monner
 */
public class LTLProgressTest extends TestCase {

	private LTLAtom p1;
	private LTLAtom p2;
	private LTLAtom p3;
	private LTLAtom p4;
	
	private LTLAtom p5x;
	private LTLAtom p5a;
	private LTLAtom p5b;
	private LTLAtom p5c;
	
	private LTLAtom p6x;
	private LTLAtom p6a;
	private LTLAtom p6b;
	private LTLAtom p6c;
	private LTLAtom p6d;
	
	private LTLAtom p7x;
	private LTLAtom p7d;
	private LTLAtom p7e;
	
	private State s;
	
	public void setUp() {
		
		TermConstant.initialize(5);
		TermVariable.initialize(1);
		
		Predicate pr1 = new Predicate(1, 0, TermList.NIL);
		Predicate pr2 = new Predicate(2, 0, TermList.NIL);
		Predicate pr3 = new Predicate(3, 0, TermList.NIL);
		Predicate pr4 = new Predicate(4, 0, TermList.NIL);
		
		Predicate pr5x = new Predicate(5, 1, TermVariable.getVariable(0));
		Predicate pr5a = new Predicate(5, 1, TermConstant.getConstant(0));
		Predicate pr5b = new Predicate(5, 1, TermConstant.getConstant(1));
		Predicate pr5c = new Predicate(5, 1, TermConstant.getConstant(2));
		
		Predicate pr6x = new Predicate(6, 1, TermVariable.getVariable(0));
		Predicate pr6a = new Predicate(6, 1, TermConstant.getConstant(0));
		Predicate pr6b = new Predicate(6, 1, TermConstant.getConstant(1));
		Predicate pr6c = new Predicate(6, 1, TermConstant.getConstant(2));
		Predicate pr6d = new Predicate(6, 1, TermConstant.getConstant(3));
		
		Predicate pr7x = new Predicate(7, 1, TermVariable.getVariable(0));
		Predicate pr7d = new Predicate(7, 1, TermConstant.getConstant(3));
		Predicate pr7e = new Predicate(7, 1, TermConstant.getConstant(4));
		
		p1 = new LTLAtom(pr1);
		p2 = new LTLAtom(pr2);
		p3 = new LTLAtom(pr3);
		p4 = new LTLAtom(pr4);
		
		p5x = new LTLAtom(pr5x);
		p5a = new LTLAtom(pr5a);
		p5b = new LTLAtom(pr5b);
		p5c = new LTLAtom(pr5c);
		
		p6x = new LTLAtom(pr6x);
		p6a = new LTLAtom(pr6a);
		p6b = new LTLAtom(pr6b);
		p6c = new LTLAtom(pr6c);
		p6d = new LTLAtom(pr6d);
		
		p7x = new LTLAtom(pr7x);
		p7d = new LTLAtom(pr7d);
		p7e = new LTLAtom(pr7e);
		
		s = new State(8, new Axiom[8][0]);
		s.add(pr1);
		s.add(pr2);
		s.add(pr5a);
		s.add(pr5b);
		s.add(pr5c);
		s.add(pr6b);
		s.add(pr6c);
		s.add(pr7d);
	}
	
	public void testProgressConjunction() {
		LTLExpression e;
		
		// 2 conjuncts left
		e = new LTLConjunction(new LTLExpression[] {
			p1,
			new LTLEventually(p3),
			p2,
			new LTLNext(p4)
		});
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLConjunction(new LTLExpression[] {new LTLEventually(" + p3 +"), " + p4 + "})"));
		
		// 1 conjunct left
		e = new LTLConjunction(new LTLExpression[] {
			p1,
			new LTLEventually(p1),
			p2,
			new LTLNext(p4)
		});
		assertTrue(ControlRules.progress(s, e).toString().equals(p4.toString()));
		
		// no conjuncts left
		e = new LTLConjunction(new LTLExpression[] {
			new LTLUntil(p3, p1),
			new LTLUntil(p4, p2)
		});
		assertTrue(ControlRules.progress(s, e) instanceof LTLTrue);

		// a conjunct evaluates to false
		e = new LTLConjunction(new LTLExpression[] {
			new LTLNext(p2),
			p3,
			new LTLUntil(p3, p1)
		});
		assertTrue(ControlRules.progress(s, e) instanceof LTLFalse);
	}
	
	public void testEntailsConjunction() {
		LTLExpression e;
		
		// all conjuncts true
		e = new LTLConjunction(new LTLExpression[] {
			p1,
			p2
		});
		assertTrue(ControlRules.entails(s, e));

		// a conjunct evaluates to false
		e = new LTLConjunction(new LTLExpression[] {
			p1,
			p3,
			p2,
		});
		assertFalse(ControlRules.entails(s, e));
	}
	
	public void testProgressDisjunction() {
		LTLExpression e;
		
		// 2 disjuncts left
		e = new LTLDisjunction(new LTLExpression[] {
			p3,
			new LTLEventually(p3),
			p4,
			new LTLNext(p4)
		});
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLDisjunction(new LTLExpression[] {new LTLEventually(" + p3 +"), " + p4 + "})"));
		
		// 1 disjunct left
		e = new LTLDisjunction(new LTLExpression[] {
			p3,
			p4,
			new LTLNext(p4),
			p3
		});
		assertTrue(ControlRules.progress(s, e).toString().equals(p4.toString()));

		// a disjunct evaluates to true
		e = new LTLDisjunction(new LTLExpression[] {
			p3,
			new LTLUntil(p4, p1)
		});
		assertTrue(ControlRules.progress(s, e) instanceof LTLTrue);

		// no disjuncts left
		e = new LTLDisjunction(new LTLExpression[] {
			new LTLUntil(p3, p4),
			new LTLUntil(p4, p3)
		});
		assertTrue(ControlRules.progress(s, e) instanceof LTLFalse);
	}
	
	public void testEntailsDisjunction() {
		LTLExpression e;
		
		// a disjunct evaluates to true
		e = new LTLDisjunction(new LTLExpression[] {
			p3,
			p1,
			p4
		});
		assertTrue(ControlRules.entails(s, e));

		// all disjuncts false
		e = new LTLDisjunction(new LTLExpression[] {
			p3,
			p4,
			p6a
		});
		assertFalse(ControlRules.entails(s, e));		
	}
	
	public void testProgressNegation() {
		LTLExpression e;
		
		// operand not evaluable
		e = new LTLNegation(new LTLEventually(p3));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLNegation(new LTLEventually(" + p3 + "))"));
		
		// operand is true
		e = new LTLNegation(new LTLEventually(p1));
		assertTrue(ControlRules.progress(s, e) instanceof LTLFalse);

		// operand is false
		e = new LTLNegation(new LTLUntil(p3, p4));
		assertTrue(ControlRules.progress(s, e) instanceof LTLTrue);
	}
	
	public void testEntailsNegation() {
		LTLExpression e;

		// operand is true
		e = new LTLNegation(p1);
		assertFalse(ControlRules.entails(s, e));

		// operand is false
		e = new LTLNegation(p3);
		assertTrue(ControlRules.entails(s, e));
	}
	
	public void testProgressForAll() {
		LTLExpression e;
		
		// 2 or more bindings not evaluable
		e = new LTLForAll(p5x, new LTLNext(p6x));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLConjunction(new LTLExpression[] {" + p6a + ", " + p6b + ", " + p6c + "})"));
		
		// all bindings are true
		e = new LTLForAll(p6x, new LTLEventually(p5x));
		assertTrue(ControlRules.progress(s, e) instanceof LTLTrue);
		
		// 1 binding not evaluable
		e = new LTLForAll(p5x, new LTLEventually(p6x));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLEventually(" + p6a + ")"));

		// 1 binding is false
		e = new LTLForAll(p5x, new LTLUntil(p6x, p4));
		assertTrue(ControlRules.progress(s, e) instanceof LTLFalse);
	}
	
	public void testEntailsForAll() {
		LTLExpression e;
		
		// all bindings are true
		e = new LTLForAll(p6x, p5x);
		assertTrue(ControlRules.entails(s, e));
		
		// 1 binding is false
		e = new LTLForAll(p5x, p6x);
		assertFalse(ControlRules.entails(s, e));		
	}
	
	public void testProgressExists() {
		LTLExpression e;
		
		// 2 or more bindings not evaluable
		e = new LTLExists(p5x, new LTLNext(p6x));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLDisjunction(new LTLExpression[] {" + p6a + ", " + p6b + ", " + p6c + "})"));
		
		// all bindings are false
		e = new LTLExists(p5x, new LTLUntil(p4, p7x));
		assertTrue(ControlRules.progress(s, e) instanceof LTLFalse);
		
		// 1 binding not evaluable
		e = new LTLExists(p7x, new LTLEventually(p6x));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLEventually(" + p6d + ")"));

		// 1 binding is true
		e = new LTLExists(p5x, new LTLEventually(p6x));
		assertTrue(ControlRules.progress(s, e) instanceof LTLTrue);
	}
	
	public void testEntailsExists() {
		LTLExpression e;
		
		// all bindings are false
		e = new LTLExists(p5x, p7x);
		assertFalse(ControlRules.entails(s, e));
		
		// 1 binding is true
		e = new LTLExists(p5x, p6x);
		assertTrue(ControlRules.entails(s, e));
	}
	
	public void testProgressNext() {
		LTLExpression e;
		
		// test next
		e = new LTLNext(p5a);
		assertTrue(ControlRules.progress(s, e).toString().equals(p5a.toString()));
	}
	
	public void testProgressEventually() {
		LTLExpression e;
		
		// operand not evaluable
		e = new LTLEventually(new LTLNext(p3));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLDisjunction(new LTLExpression[] {" + p3 + ", " + e + "})"));
		
		// operand is true
		e = new LTLEventually(p1);
		assertTrue(ControlRules.progress(s, e) instanceof LTLTrue);

		// operand is false
		e = new LTLEventually(p3);
		assertTrue(ControlRules.progress(s, e).toString().equals(e.toString()));
	}
	
	public void testProgressAlways() {
		LTLExpression e;
		
		// operand not evaluable
		e = new LTLAlways(new LTLNext(p3));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLConjunction(new LTLExpression[] {" + p3 + ", " + e + "})"));
		
		// operand is true
		e = new LTLAlways(p1);
		assertTrue(ControlRules.progress(s, e).toString().equals(e.toString()));

		// operand is false
		e = new LTLAlways(p3);
		assertTrue(ControlRules.progress(s, e) instanceof LTLFalse);
	}
	
	public void testProgressUntil() {
		LTLExpression e;
		
		// second operand is true
		e = new LTLUntil(p3, p1);
		assertTrue(ControlRules.progress(s, e) instanceof LTLTrue);
		
		// second operand false, first operand true
		e = new LTLUntil(p1, p3);
		assertTrue(ControlRules.progress(s, e).toString().equals(e.toString()));

		// second operand false, first operand false
		e = new LTLUntil(p3, p4);
		assertTrue(ControlRules.progress(s, e) instanceof LTLFalse);
		
		// second operand false, first operand unevaluable
		e = new LTLUntil(new LTLNext(p3), p4);
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLConjunction(new LTLExpression[] {" + p3 + ", " + e + "})"));
		
		// second operand unevaluable, first operand true
		e = new LTLUntil(p1, new LTLNext(p3));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLDisjunction(new LTLExpression[] {" + p3 + ", " + e + "})"));

		// second operand unevaluable, first operand false
		e = new LTLUntil(p3, new LTLNext(p3));
		assertTrue(ControlRules.progress(s, e).toString().equals(p3.toString()));
		
		// second operand unevaluable, first operand unevaluable
		e = new LTLUntil(new LTLNext(p4), new LTLNext(p3));
		assertTrue(ControlRules.progress(s, e).toString().equals("new LTLDisjunction(new LTLExpression[] {" + p3 + ", new LTLConjunction(new LTLExpression[] {" + p4 + ", " + e + "})})"));
	}
	
}
