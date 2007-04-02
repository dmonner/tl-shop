package JSHOP2;

import java.util.LinkedList;

/**
 * This class provides static convenience methods for working with control rules
 * in Linear Temporal Logic (as <code>LogicalExpression</code> objects).
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class ControlRules
{
	/**
	 * Determines whether a given LTL control rule is true, false, or undetermined
	 * in the given state of the world.
	 * 
	 * @param s
	 *          The current state of the world.
	 * @param f
	 *          The expression to progress.
	 * @return an instance of <code>LTLTrue</code> iff <code>f</code>
	 *         evaluates to true in <code>s</code>; an instance of
	 *         <code>LTLFalse</code> iff <code>f</code> evaluates to false in
	 *         <code>s</code>; or, the progressed formula that must be true in
	 *         subsequent states in order for this formula to not be false in the
	 *         current state.
	 */
	public static LTLExpression progress(State s, LTLExpression f)
	{
		// if there are temporal operators, we cannot evaluate the formula directly
		if(f.hasTemporalOperators())
		{

			// if the top operator is a conjunction
			// then this expression has the form:
			// and { f1, f2, ..., fn }
			// and the formula to return is:
			// and { progress(s, f1), progress(s, f2), ..., progress(s, fn) }
			if(f instanceof LTLConjunction)
			{
				// get the operands of this conjunction
				LTLExpression[] fn = ((LTLConjunction) f).getConjuncts();

				// create a new linked list to hold the progressed subexpressions
				LinkedList<LTLExpression> pfn = new LinkedList<LTLExpression>();

				// progress each of the subexpressions individually
				for(int i = 0; i < fn.length; i++)
				{
					// progress the subexpression
					LTLExpression p = progress(s, fn[i]);

					// if the progression evaluates to FALSE
					if(p instanceof LTLFalse)
					{
						// then our entire conjunction is FALSE.
						return LTLFalse.getInstance();
					}
					// if the progression evaluates to TRUE, then it is irrelevant to our
					// conjunction, and can be removed.
					// otherwise, we must add it to the new conjunction.
					else if(!(p instanceof LTLTrue))
					{
						pfn.add(p);
					}
				}

				// if we have 0 conjuncts left at the end
				if(pfn.size() == 0)
				{
					// this means all our conjuncts were TRUE, so return TRUE
					return LTLTrue.getInstance();
				}
				// if we have exactly 1 conjunct left
				else if(pfn.size() == 1)
				{
					// return it by itself
					return pfn.get(0);
				}
				// otherwise
				else
				{
					// recombine the conjuncts and return them
					return new LTLConjunction(pfn.toArray(new LTLExpression[0]));
				}
			}

			// if the top operator is a disjunction
			// then this expression has the form:
			// or { f1, f2, ..., fn }
			// and the formula to return is:
			// or { progress(s, f1), progress(s, f2), ..., progress(s, fn) }
			else if(f instanceof LTLDisjunction)
			{
				// get the operands of this conjunction
				LTLExpression[] fn = ((LTLDisjunction) f).getDisjuncts();

				// create a new linked list to hold the progressed subexpressions
				LinkedList<LTLExpression> pfn = new LinkedList<LTLExpression>();

				// progress each of the subexpressions individually
				for(int i = 0; i < fn.length; i++)
				{
					// progress the subexpression
					LTLExpression p = progress(s, fn[i]);

					// if the progression evaluates to TRUE
					if(p instanceof LTLTrue)
					{
						// then our entire disjunction is TRUE.
						return LTLTrue.getInstance();
					}
					// if the progression evaluates to FALSE, then it is irrelevant to our
					// disjunction, and can be removed.
					// otherwise, we must add it to the new disjunction.
					else if(!(p instanceof LTLFalse))
					{
						pfn.add(p);
					}
				}

				// if we have 0 disjuncts left at the end
				if(pfn.size() == 0)
				{
					// this means all our disjuncts were FALSE, so return FALSE
					return LTLFalse.getInstance();
				}
				// if we have exactly 1 disjunct left
				else if(pfn.size() == 1)
				{
					// return it by itself
					return pfn.get(0);
				}
				// otherwise
				else
				{
					// recombine the disjuncts and return them
					return new LTLDisjunction(pfn.toArray(new LTLExpression[0]));
				}
			}

			// if the operator is a negation
			// then this expression has the form:
			// not f1
			// and the formula to return is:
			// not progress(s, f1)
			else if(f instanceof LTLNegation)
			{
				// get the operand of this negation
				LTLExpression f1 = ((LTLNegation) f).getOperand();

				LTLExpression pf1 = progress(s, f1);

				// if progress(s, f1) evaluates to TRUE
				if(pf1 instanceof LTLTrue)
				{
					// then the negation of progress(s, f1) is FALSE
					return LTLFalse.getInstance();
				}
				// if progress(s, f1) evaluates to FALSE
				else if(pf1 instanceof LTLFalse)
				{
					// then the negation of progress(s, f1) is TRUE
					return LTLTrue.getInstance();
				}
				// otherwise we return the negated formula
				else
				{
					return new LTLNegation(pf1);
				}
			}

			// if the operator is "next"
			// then this expression has the form:
			// next f1
			// and the formula to return is:
			// f1
			else if(f instanceof LTLNext)
			{
				// return the operand of this "next" operator
				return ((LTLNext) f).getOperand();
			}

			// if the operator is "until"
			// then this expression has the form:
			// f1 until f2
			// and the formula to return is:
			// progress(s, f2) | (progress(s, f1) & f)
			else if(f instanceof LTLUntil)
			{
				// get the operands of this "until" operator
				LTLExpression f1 = ((LTLUntil) f).getFirstOperand();
				LTLExpression f2 = ((LTLUntil) f).getSecondOperand();

				LTLExpression pf2 = progress(s, f2);

				// if progress(s, f2) is TRUE
				if(pf2 instanceof LTLTrue)
				{
					// then our formula reduces to TRUE
					return LTLTrue.getInstance();
				}
				// if progress(s, f2) is FALSE
				else if(pf2 instanceof LTLFalse)
				{
					// then our formula simplifies to progress(s, f1) & f
					LTLExpression pf1 = progress(s, f1);

					// if progress(s, f1) is TRUE
					if(pf1 instanceof LTLTrue)
					{
						// then our formula simplifies to f
						return f;
					}
					// if progress(s, f1) if FALSE
					else if(pf1 instanceof LTLFalse)
					{
						// then our formula simplifies to FALSE
						return LTLFalse.getInstance();
					}
					// otherwise our formula is progress(s, f1) & f
					else
					{
						return new LTLConjunction(new LTLExpression[]
						{
						  pf1, f
						});
					}
				}
				// otherwise we may need to evaluate the whole formula
				else
				{
					LTLExpression pf1 = progress(s, f1);

					// if progress(s, f1) is TRUE
					if(pf1 instanceof LTLTrue)
					{
						// then our formula simplifies to progress(s, f2) | f
						return new LTLDisjunction(new LTLExpression[]
						{
						  pf2, f
						});
					}
					// if progress(s, f1) if FALSE
					else if(pf1 instanceof LTLFalse)
					{
						// then our formula simplifies to progress(s, f2)
						return pf2;
					}
					// otherwise we must evaluate the full formula
					else
					{
						// construct progress(s, f2) | (progress(s, f1) & f)
						return new LTLDisjunction(new LTLExpression[]
						{
						  pf2, new LTLConjunction(new LTLExpression[]
						  {
						    pf1, f
						  })
						});
					}
				}
			}

			// if the operator is "eventually"
			// then this expression has the form:
			// eventually f1
			// and the formula to return is:
			// progress(s, f1) | f
			else if(f instanceof LTLEventually)
			{
				// get the operands of this "eventually" operator
				LTLExpression f1 = ((LTLEventually) f).getOperand();

				LTLExpression pf1 = progress(s, f1);

				// if progress(s, f1) evaluates to TRUE,
				if(pf1 instanceof LTLTrue)
				{
					// then our formula simplifies to TRUE.
					return LTLTrue.getInstance();
				}
				// if progress(s, f1) evaluates to FALSE,
				else if(pf1 instanceof LTLFalse)
				{
					// then our formula simplifies to f
					return f;
				}
				// otherwise our formula does not simplify at all
				else
				{
					// construct the disjunction: progress(s, f1) | f
					return new LTLDisjunction(new LTLExpression[]
					{
					  pf1, f
					});
				}
			}

			// if the operator is "always"
			// then this expression has the form:
			// always f1
			// and the formula to return is:
			// progress(s, f1) & f
			else if(f instanceof LTLAlways)
			{
				// get the operands of this "eventually" operator
				LTLExpression f1 = ((LTLAlways) f).getOperand();

				LTLExpression pf1 = progress(s, f1);

				// if progress(s, f1) evaluates to TRUE,
				if(pf1 instanceof LTLTrue)
				{
					// then our formula simplifies to f.
					return f;
				}
				// if progress(s, f1) evaluates to FALSE,
				else if(pf1 instanceof LTLFalse)
				{
					// then our formula simplifies to FALSE
					return LTLFalse.getInstance();
				}
				// otherwise our formula does not simplify at all
				else
				{
					return new LTLConjunction(new LTLExpression[]
					{
					  pf1, f
					});
				}
			}

			// if the operator is "forall"
			// then this expression has the form:
			// forall x (premise -> consequent)
			// (where "premise" is an atom that is true for only finitely many x)
			// and the formula to return is:
			// and { progress(s, consequent, binding_1),
			// progress(s, consequent, binding_2),
			// ...,
			// progress(s, consequent, binding_n) }
			// for each valid value of binding
			else if(f instanceof LTLForAll)
			{
				LTLAtom premise = ((LTLForAll) f).getPremise();
				LTLExpression consequent = ((LTLForAll) f).getConsequent();

				Predicate atom = premise.getAtom();

				// get the state's iterator, a helper for s.nextBinding, below
				MyIterator me = s.iterator(atom.getHead());

				// to hold the new bindings we encounter
				Term[] newbinding;

				// to hold the new conjuncts
				LinkedList<LTLExpression> conjuncts = new LinkedList<LTLExpression>();

				// for each binding that satisfies the premise
				while((newbinding = s.nextBinding(atom, me)) != null)
				{
					// apply the binding to the consequent
					LTLExpression newConsequent = consequent.applySubstitution(newbinding);

					// progress the expression with the new binding
					LTLExpression p = progress(s, newConsequent);

					// if the progression evaluates to FALSE
					if(p instanceof LTLFalse)
					{
						// then our entire conjunction is FALSE.
						return LTLFalse.getInstance();
					}
					// if the progression evaluates to TRUE, then it is irrelevant to our
					// conjunction, and can be removed.
					// otherwise, we must add it to the new conjunction.
					else if(!(p instanceof LTLTrue))
					{
						conjuncts.add(p);
					}
				}

				// if we have 0 conjuncts left at the end
				if(conjuncts.size() == 0)
				{
					// this means all our conjuncts were TRUE, so return TRUE
					return LTLTrue.getInstance();
				}
				// if we have exactly 1 conjunct left
				else if(conjuncts.size() == 1)
				{
					// return it by itself
					return conjuncts.get(0);
				}
				// otherwise
				else
				{
					// recombine the conjuncts and return them
					return new LTLConjunction(conjuncts.toArray(new LTLExpression[0]));
				}
			}

			// if the operator is "exists"
			// then this expression has the form:
			// forall x (premise -> consequent)
			// (where "premise" is an atom that is true for only finitely many x)
			// and the formula to return is:
			// or { progress(s, consequent, binding_1),
			// progress(s, consequent, binding_2),
			// ...,
			// progress(s, consequent, binding_n) }
			// for each valid value of binding
			else if(f instanceof LTLExists)
			{
				LTLAtom premise = ((LTLExists) f).getPremise();
				LTLExpression consequent = ((LTLExists) f).getConsequent();

				Predicate atom = premise.getAtom();

				// get the state's iterator, a helper for s.nextBinding, below
				MyIterator me = s.iterator(atom.getHead());

				// to hold the new bindings we encounter
				Term[] newbinding;

				// to hold the new conjuncts
				LinkedList<LTLExpression> disjuncts = new LinkedList<LTLExpression>();

				// for each binding that satisfies the premise
				while((newbinding = s.nextBinding(atom, me)) != null)
				{
					// apply the binding to the consequent
					LTLExpression newConsequent = consequent.applySubstitution(newbinding);

					// progress the expression with the new binding
					LTLExpression p = progress(s, newConsequent);

					// if the progression evaluates to TRUE
					if(p instanceof LTLTrue)
					{
						// then our entire disjunction is TRUE.
						return LTLTrue.getInstance();
					}
					// if the progression evaluates to FALSE, then it is irrelevant to our
					// disjunction, and can be removed.
					// otherwise, we must add it to the new disjunction.
					else if(!(p instanceof LTLFalse))
					{
						disjuncts.add(p);
					}
				}

				// if we have 0 disjuncts left at the end
				if(disjuncts.size() == 0)
				{
					// this means all our disjuncts were FALSE, so return FALSE
					return LTLFalse.getInstance();
				}
				// if we have exactly 1 disjunct left
				else if(disjuncts.size() == 1)
				{
					// return it by itself
					return disjuncts.get(0);
				}
				// otherwise
				else
				{
					// recombine the disjuncts and return them
					return new LTLDisjunction(disjuncts.toArray(new LTLExpression[0]));
				}
			}

			// this should never happen
			else
			{
				throw new IllegalArgumentException(
				  "Somehow hasTemporalOperators() returned true but the top operator was not in our list.");
			}
		}

		// otherwise, this formula has no temporal operators
		else
		{
			// if the current state entails this formula
			if(entails(s, f))
				// return TRUE
				return LTLTrue.getInstance();
			else
				// otherwise return FALSE
				return LTLFalse.getInstance();
		}
	}

	/**
	 * Determines the truth value of a logical formula (without temporal
	 * operators) in the given state of the world.
	 * 
	 * @param s
	 *          The current state of the world.
	 * @param f
	 *          The formula to evaluate. <code>f</code> is not allowed to
	 *          contain temporal operators.
	 * @return <code>true</code> iff the current state entails this formula,
	 *         <code>false</code> otherwise.
	 */
	public static boolean entails(State s, LTLExpression f)
	{
		if(f.hasTemporalOperators())
		{
			throw new IllegalArgumentException(
			  "The input formula is not allowed to contain temporal operators.");
		}
		
		// if the top-level operator is a conjunction
		else if(f instanceof LTLConjunction)
		{
			// get the list of conjuncts
			LTLExpression[] fn = ((LTLConjunction) f).getConjuncts();

			// for each conjunct
			for(int i = 0; i < fn.length; i++)
				// if it is not entailed by the current state
				if(!entails(s, fn[i]))
					// the whole conjunction is false
					return false;

			// all conjuncts evaluated to true
			return true;
		}
		
		// if the top-level operator is a disjunction
		else if(f instanceof LTLDisjunction)
		{
			// get the list of conjuncts
			LTLExpression[] fn = ((LTLDisjunction) f).getDisjuncts();

			// for each disjunct
			for(int i = 0; i < fn.length; i++)
				// if it is entailed by the current state
				if(entails(s, fn[i]))
					// the whole conjunction is true
					return true;

			// all disjuncts evaluated to false
			return false;
		}
		
		// if the top-level operator is a negation
		else if(f instanceof LTLNegation)
		{
			// get the operand formula of this negation
			LTLExpression f1 = ((LTLNegation) f).getOperand();

			// return the opposite of it
			return !entails(s, f1);
		}
		
		// if the top-level operator is forall
		else if(f instanceof LTLForAll)
		{
			LTLAtom premise = ((LTLForAll) f).getPremise();
			LTLExpression consequent = ((LTLForAll) f).getConsequent();

			Predicate atom = premise.getAtom();

			// get the state's iterator, a helper for s.nextBinding, below
			MyIterator me = s.iterator(atom.getHead());

			// to hold the new bindings we encounter
			Term[] newbinding;

			// for each binding that satisfies the premise
			while((newbinding = s.nextBinding(atom, me)) != null)
			{
				// apply the binding to the consequent
				LTLExpression newConsequent = consequent.applySubstitution(newbinding);

				// if the consequent is not entailed with the new binding
				if(!entails(s, newConsequent))
					// our forall is not satisfied
					return false;
			}

			// the state entailed the consequent for all valid premise values,
			// so our forall is satisfied
			return true;
		}
		
		// if the top-level operator is exists
		else if(f instanceof LTLExists)
		{
			LTLAtom premise = ((LTLExists) f).getPremise();
			LTLExpression consequent = ((LTLExists) f).getConsequent();

			Predicate atom = premise.getAtom();

			// get the state's iterator, a helper for s.nextBinding, below
			MyIterator me = s.iterator(atom.getHead());

			// to hold the new bindings we encounter
			Term[] newbinding;

			// for each binding that satisfies the premise
			while((newbinding = s.nextBinding(atom, me)) != null)
			{
				// apply the binding to the consequent
				LTLExpression newConsequent = consequent.applySubstitution(newbinding);

				// if the consequent is not entailed with the new binding
				if(entails(s, newConsequent))
					// our exists is satisfied
					return true;
			}

			// the state did not entail the consequent for any valid premise values,
			// so our exists is not satisfied
			return false;
		}
		
		// if the top-level is an atom
		else if(f instanceof LTLAtom)
		{
			// get the actual predicate
			Predicate atom = ((LTLAtom) f).getAtom();

			// ASSERT atom is ground
			if(!atom.isGround())
				throw new IllegalArgumentException("All atoms should be ground here.");

			// get the state's iterator, a helper for s.nextBinding, below
			MyIterator me = s.iterator(atom.getHead());

			// return true iff there the state satisfies this predicate
			return (s.nextBinding(atom, me) != null);
		}

		// this should never happen
		else
		{
			throw new IllegalArgumentException(
			  "Argument has no temporal operators but is not an instance of Conjunction, Disjunction, Negation, Forall, Exists, or Atom.");
		}
	}
}
