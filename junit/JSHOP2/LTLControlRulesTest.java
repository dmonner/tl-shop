package JSHOP2;

import junit.framework.TestCase;

/**
 * @author Derek Monner
 */
public class LTLControlRulesTest extends TestCase
{

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

	public void setUp()
	{

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

	public void testConjoin()
	{
		LTLExpression a, b;

		// first expression false
		a = LTLFalse.getInstance();
		b = p1;
		assertTrue(LTLExpression.conjoin(a, b).toString().equals(
		  LTLFalse.getInstance().toString()));

		// second expression false
		assertTrue(LTLExpression.conjoin(b, a).toString().equals(
		  LTLFalse.getInstance().toString()));

		// first expression true
		a = LTLTrue.getInstance();
		assertTrue(LTLExpression.conjoin(a, b).toString().equals(p1.toString()));

		// second expression true
		assertTrue(LTLExpression.conjoin(b, a).toString().equals(p1.toString()));

		// first expression is a conjunction
		a = new LTLConjunction(new LTLExpression[]
		{
		  p2, p3
		});
		assertTrue(LTLExpression.conjoin(a, b).toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p2 + ", " + p3 + ", " + p1
		    + "})"));

		// second expression is a conjunction
		assertTrue(LTLExpression.conjoin(b, a).toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p1 + ", " + p2 + ", " + p3
		    + "})"));

		// both are conjunctions, and order is preserved
		b = new LTLConjunction(new LTLExpression[]
		{
		  p1, p4
		});
		assertTrue(LTLExpression.conjoin(a, b).toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p2 + ", " + p3 + ", " + p1
		    + ", " + p4 + "})"));
		assertTrue(LTLExpression.conjoin(b, a).toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p1 + ", " + p4 + ", " + p2
		    + ", " + p3 + "})"));

		// normal case
		a = p1;
		b = p2;
		assertTrue(LTLExpression.conjoin(a, b).toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p1 + ", " + p2 + "})"));
		assertTrue(LTLExpression.conjoin(b, a).toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p2 + ", " + p1 + "})"));
	}

	public void testDisjoin()
	{
		LTLExpression a, b;

		// first expression true
		a = LTLTrue.getInstance();
		b = p1;
		assertTrue(LTLExpression.disjoin(a, b).toString().equals(
		  LTLTrue.getInstance().toString()));

		// second expression true
		assertTrue(LTLExpression.disjoin(b, a).toString().equals(
		  LTLTrue.getInstance().toString()));

		// first expression false
		a = LTLFalse.getInstance();
		assertTrue(LTLExpression.disjoin(a, b).toString().equals(p1.toString()));

		// second expression false
		assertTrue(LTLExpression.disjoin(b, a).toString().equals(p1.toString()));

		// first expression is a disjunction
		a = new LTLDisjunction(new LTLExpression[]
		{
		  p2, p3
		});
		assertTrue(LTLExpression.disjoin(a, b).toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p2 + ", " + p3 + ", " + p1
		    + "})"));

		// second expression is a disjunction
		assertTrue(LTLExpression.disjoin(b, a).toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p1 + ", " + p2 + ", " + p3
		    + "})"));

		// both are disjunctions, and order is preserved
		b = new LTLDisjunction(new LTLExpression[]
		{
		  p1, p4
		});
		assertTrue(LTLExpression.disjoin(a, b).toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p2 + ", " + p3 + ", " + p1
		    + ", " + p4 + "})"));
		assertTrue(LTLExpression.disjoin(b, a).toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p1 + ", " + p4 + ", " + p2
		    + ", " + p3 + "})"));

		// normal case
		a = p1;
		b = p2;
		assertTrue(LTLExpression.disjoin(a, b).toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p1 + ", " + p2 + "})"));
		assertTrue(LTLExpression.disjoin(b, a).toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p2 + ", " + p1 + "})"));
	}

	public void testSimplify()
	{
		LTLExpression e;
		String s;

		// recursive deep conjunction test
		e = new LTLConjunction(new LTLExpression[]
		{
		  new LTLConjunction(new LTLExpression[]
		  {
		    p2, new LTLConjunction(new LTLExpression[]
		    {
		      p1, new LTLConjunction(new LTLExpression[]
		      {
		        p5a, p5b
		      }), p4
		    }), p3
		  }), new LTLConjunction(new LTLExpression[]
		  {
		    new LTLConjunction(new LTLExpression[]
		    {
		      p5a, p5b
		    }), new LTLConjunction(new LTLExpression[]
		    {
		      p1, new LTLConjunction(new LTLExpression[]
		      {
		        p5a, p5b
		      })
		    })
		  })
		});
		assertTrue(LTLExpression.simplify(e).toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p2 + ", " + p1 + ", " + p5a
		    + ", " + p5b + ", " + p4 + ", " + p3 + ", " + p5a + ", " + p5b + ", "
		    + p1 + ", " + p5a + ", " + p5b + "})"));

		// recursive deep disjunction test
		e = new LTLDisjunction(new LTLExpression[]
		{
		  new LTLDisjunction(new LTLExpression[]
		  {
		    p2, new LTLDisjunction(new LTLExpression[]
		    {
		      p1, new LTLDisjunction(new LTLExpression[]
		      {
		        p5a, p5b
		      }), p4
		    }), p3
		  }), new LTLDisjunction(new LTLExpression[]
		  {
		    new LTLDisjunction(new LTLExpression[]
		    {
		      p5a, p5b
		    }), new LTLDisjunction(new LTLExpression[]
		    {
		      p1, new LTLDisjunction(new LTLExpression[]
		      {
		        p5a, p5b
		      })
		    })
		  })
		});
		assertTrue(LTLExpression.simplify(e).toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p2 + ", " + p1 + ", " + p5a
		    + ", " + p5b + ", " + p4 + ", " + p3 + ", " + p5a + ", " + p5b + ", "
		    + p1 + ", " + p5a + ", " + p5b + "})"));

		// recursive deep negation test
		e = new LTLNegation(
		  new LTLNegation(new LTLNegation(new LTLNegation(new LTLUntil(p1,
		    new LTLNegation(new LTLNegation(new LTLNegation(p2))))))));
		assertTrue(LTLExpression.simplify(e).toString().equals(
		  "new LTLUntil(" + p1 + ", new LTLNegation(" + p2 + "))"));

		// forall tests
		e = new LTLForAll(p5x, LTLTrue.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLForAll(p5x, new LTLDisjunction(new LTLExpression[]
		{
		  p1, LTLTrue.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLForAll(p5x, LTLFalse.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLForAll(p5x, new LTLConjunction(new LTLExpression[]
		{
		  p1, LTLFalse.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLForAll(p5x, p6x);
		s = e.toString();
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		e = new LTLForAll(p5x, new LTLNegation(new LTLNegation(p6x)));
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		// exists tests
		e = new LTLExists(p5x, LTLTrue.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLExists(p5x, new LTLDisjunction(new LTLExpression[]
		{
		  p1, LTLTrue.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLExists(p5x, LTLFalse.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLExists(p5x, new LTLConjunction(new LTLExpression[]
		{
		  p1, LTLFalse.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLExists(p5x, p6x);
		s = e.toString();
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		e = new LTLExists(p5x, new LTLNegation(new LTLNegation(p6x)));
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		// next tests
		e = new LTLNext(LTLTrue.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLNext(new LTLDisjunction(new LTLExpression[]
		{
		  p1, LTLTrue.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLNext(LTLFalse.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLNext(new LTLConjunction(new LTLExpression[]
		{
		  p1, LTLFalse.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLNext(p6x);
		s = e.toString();
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		e = new LTLNext(new LTLNegation(new LTLNegation(p6x)));
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		// always tests
		e = new LTLAlways(LTLTrue.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLAlways(new LTLDisjunction(new LTLExpression[]
		{
		  p1, LTLTrue.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLAlways(LTLFalse.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLAlways(new LTLConjunction(new LTLExpression[]
		{
		  p1, LTLFalse.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLAlways(p6x);
		s = e.toString();
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		e = new LTLAlways(new LTLNegation(new LTLNegation(p6x)));
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		// eventually tests
		e = new LTLEventually(LTLTrue.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLEventually(new LTLDisjunction(new LTLExpression[]
		{
		  p1, LTLTrue.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLEventually(LTLFalse.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLEventually(new LTLConjunction(new LTLExpression[]
		{
		  p1, LTLFalse.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);

		e = new LTLEventually(p6x);
		s = e.toString();
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		e = new LTLEventually(new LTLNegation(new LTLNegation(p6x)));
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		// until tests
		e = new LTLUntil(LTLTrue.getInstance(), p6x);
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLUntil(new LTLDisjunction(new LTLExpression[]
		{
		  p1, LTLTrue.getInstance()
		}), p6x);
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLUntil(LTLFalse.getInstance(), p6x);
		assertTrue(LTLExpression.simplify(e).toString().equals(p6x.toString()));

		e = new LTLUntil(new LTLConjunction(new LTLExpression[]
		{
		  p1, LTLFalse.getInstance()
		}), p6x);
		assertTrue(LTLExpression.simplify(e).toString().equals(p6x.toString()));

		e = new LTLUntil(p6x, LTLTrue.getInstance());
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLUntil(p6x, new LTLDisjunction(new LTLExpression[]
		{
		  p1, LTLTrue.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = new LTLUntil(p6x, LTLFalse.getInstance());
		assertTrue(LTLExpression.simplify(e).toString().equals(
		  "new LTLAlways(" + p6x + ")"));

		e = new LTLUntil(p6x, new LTLConjunction(new LTLExpression[]
		{
		  p1, LTLFalse.getInstance()
		}));
		assertTrue(LTLExpression.simplify(e).toString().equals(
		  "new LTLAlways(" + p6x + ")"));

		e = new LTLUntil(p5x, p6x);
		s = e.toString();
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		e = new LTLUntil(new LTLNegation(new LTLNegation(p5x)), new LTLNegation(
		  new LTLNegation(p6x)));
		assertTrue(LTLExpression.simplify(e).toString().equals(s));

		// atom/true/false tests
		e = p1;
		assertTrue(LTLExpression.simplify(e).toString().equals(p1.toString()));

		e = LTLTrue.getInstance();
		assertTrue(LTLExpression.simplify(e) instanceof LTLTrue);

		e = LTLFalse.getInstance();
		assertTrue(LTLExpression.simplify(e) instanceof LTLFalse);
	}

	public void testProgressConjunction()
	{
		LTLExpression e;

		// 2 conjuncts left
		e = new LTLConjunction(new LTLExpression[]
		{
		  p1, new LTLEventually(p3), p2, new LTLNext(p4)
		});
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLConjunction(new LTLExpression[] {new LTLEventually(" + p3 + "), "
		    + p4 + "})"));

		// 1 conjunct left
		e = new LTLConjunction(new LTLExpression[]
		{
		  p1, new LTLEventually(p1), p2, new LTLNext(p4)
		});
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(p4.toString()));

		// no conjuncts left
		e = new LTLConjunction(new LTLExpression[]
		{
		  new LTLUntil(p3, p1), new LTLUntil(p4, p2)
		});
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules() instanceof LTLTrue);

		// a conjunct evaluates to false
		e = new LTLConjunction(new LTLExpression[]
		{
		  new LTLNext(p2), p3, new LTLUntil(p3, p1)
		});
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertFalse(s.progress());
	}

	public void testEntailsConjunction()
	{
		LTLExpression e;

		// all conjuncts true
		e = new LTLConjunction(new LTLExpression[]
		{
		  p1, p2
		});
		assertTrue(s.entails(e));

		// a conjunct evaluates to false
		e = new LTLConjunction(new LTLExpression[]
		{
		  p1, p3, p2,
		});
		assertFalse(s.entails(e));
	}

	public void testProgressDisjunction()
	{
		LTLExpression e;

		// 2 disjuncts left
		e = new LTLDisjunction(new LTLExpression[]
		{
		  p3, new LTLEventually(p3), p4, new LTLNext(p4)
		});
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {new LTLEventually(" + p3 + "), "
		    + p4 + "})"));

		// 1 disjunct left
		e = new LTLDisjunction(new LTLExpression[]
		{
		  p3, p4, new LTLNext(p4), p3
		});
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(p4.toString()));

		// a disjunct evaluates to true
		e = new LTLDisjunction(new LTLExpression[]
		{
		  p3, new LTLUntil(p4, p1)
		});
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules() instanceof LTLTrue);

		// no disjuncts left
		e = new LTLDisjunction(new LTLExpression[]
		{
		  new LTLUntil(p3, p4), new LTLUntil(p4, p3)
		});
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertFalse(s.progress());
	}

	public void testEntailsDisjunction()
	{
		LTLExpression e;

		// a disjunct evaluates to true
		e = new LTLDisjunction(new LTLExpression[]
		{
		  p3, p1, p4
		});
		assertTrue(s.entails(e));

		// all disjuncts false
		e = new LTLDisjunction(new LTLExpression[]
		{
		  p3, p4, p6a
		});
		assertFalse(s.entails(e));
	}

	public void testProgressNegation()
	{
		LTLExpression e;

		// operand not evaluable
		e = new LTLNegation(new LTLEventually(p3));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLNegation(new LTLEventually(" + p3 + "))"));

		// operand is true
		e = new LTLNegation(new LTLEventually(p1));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertFalse(s.progress());

		// operand is false
		e = new LTLNegation(new LTLUntil(p3, p4));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules() instanceof LTLTrue);
	}

	public void testEntailsNegation()
	{
		LTLExpression e;

		// operand is true
		e = new LTLNegation(p1);
		assertFalse(s.entails(e));

		// operand is false
		e = new LTLNegation(p3);
		assertTrue(s.entails(e));
	}

	public void testProgressForAll()
	{
		LTLExpression e;

		// 2 or more bindings not evaluable
		e = new LTLForAll(p5x, new LTLNext(p6x));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p6a + ", " + p6b + ", "
		    + p6c + "})"));

		// all bindings are true
		e = new LTLForAll(p6x, new LTLEventually(p5x));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules() instanceof LTLTrue);

		// 1 binding not evaluable
		e = new LTLForAll(p5x, new LTLEventually(p6x));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLEventually(" + p6a + ")"));

		// 1 binding is false
		e = new LTLForAll(p5x, new LTLUntil(p6x, p4));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertFalse(s.progress());
	}

	public void testEntailsForAll()
	{
		LTLExpression e;

		// all bindings are true
		e = new LTLForAll(p6x, p5x);
		assertTrue(s.entails(e));

		// 1 binding is false
		e = new LTLForAll(p5x, p6x);
		assertFalse(s.entails(e));
	}

	public void testProgressExists()
	{
		LTLExpression e;

		// 2 or more bindings not evaluable
		e = new LTLExists(p5x, new LTLNext(p6x));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p6a + ", " + p6b + ", "
		    + p6c + "})"));

		// all bindings are false
		e = new LTLExists(p5x, new LTLUntil(p4, p7x));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertFalse(s.progress());

		// 1 binding not evaluable
		e = new LTLExists(p7x, new LTLEventually(p6x));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLEventually(" + p6d + ")"));

		// 1 binding is true
		e = new LTLExists(p5x, new LTLEventually(p6x));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules() instanceof LTLTrue);
	}

	public void testEntailsExists()
	{
		LTLExpression e;

		// all bindings are false
		e = new LTLExists(p5x, p7x);
		assertFalse(s.entails(e));

		// 1 binding is true
		e = new LTLExists(p5x, p6x);
		assertTrue(s.entails(e));
	}

	public void testProgressNext()
	{
		LTLExpression e;

		// test next
		e = new LTLNext(p5a);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(p5a.toString()));
	}

	public void testProgressEventually()
	{
		LTLExpression e;

		// operand not evaluable
		e = new LTLEventually(new LTLNext(p3));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p3 + ", " + e + "})"));

		// operand is true
		e = new LTLEventually(p1);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules() instanceof LTLTrue);

		// operand is false
		e = new LTLEventually(p3);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(e.toString()));
	}

	public void testProgressAlways()
	{
		LTLExpression e;

		// operand not evaluable
		e = new LTLAlways(new LTLNext(p3));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p3 + ", " + e + "})"));

		// operand is true
		e = new LTLAlways(p1);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(e.toString()));

		// operand is false
		e = new LTLAlways(p3);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertFalse(s.progress());
	}

	public void testProgressUntil()
	{
		LTLExpression e;

		// second operand is true
		e = new LTLUntil(p3, p1);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules() instanceof LTLTrue);

		// second operand false, first operand true
		e = new LTLUntil(p1, p3);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(e.toString()));

		// second operand false, first operand false
		e = new LTLUntil(p3, p4);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertFalse(s.progress());

		// second operand false, first operand unevaluable
		e = new LTLUntil(new LTLNext(p3), p4);
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLConjunction(new LTLExpression[] {" + p3 + ", " + e + "})"));

		// second operand unevaluable, first operand true
		e = new LTLUntil(p1, new LTLNext(p3));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(
		  "new LTLDisjunction(new LTLExpression[] {" + p3 + ", " + e + "})"));

		// second operand unevaluable, first operand false
		e = new LTLUntil(p3, new LTLNext(p3));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString().equals(p3.toString()));

		// second operand unevaluable, first operand unevaluable
		e = new LTLUntil(new LTLNext(p4), new LTLNext(p3));
		s.clearControlRules();
		s.addControlRule(e);
		s.progress();
		assertTrue(s.getControlRules().toString()
		  .equals(
		    "new LTLDisjunction(new LTLExpression[] {" + p3
		      + ", new LTLConjunction(new LTLExpression[] {" + p4 + ", " + e
		      + "})})"));
	}

}
