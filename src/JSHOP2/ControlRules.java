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

				// recombine the conjuncts
				return new LTLConjunction(pfn.toArray(new LTLExpression[0]));
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
					else if(!(p instanceof LTLTrue))
					{
						pfn.add(p);
					}
				}

				// recombine the conjuncts
				return new LTLDisjunction(pfn.toArray(new LTLExpression[0]));
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
				LTLExpression f1 = ((LTLEventually) f).getOperand();

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
			// not premise | progress(s, consequent)
			else if(f instanceof LTLForAll)
			{
				// FIXME: Not entirely sure how to proceed here. Experiment.
				return null;
			}

			// if the operator is "exists"
			// then this expression has the form:
			// forall x (premise -> consequent)
			// (where "premise" is an atom that is true for only finitely many x)
			// and the formula to return is:
			// not premise | progress(s, consequent)
			else if(f instanceof LTLExists)
			{
				// FIXME: Not entirely sure how to proceed here. Experiment.
				return null;
			}

			// this should never happen
			else
			{
				// TODO: remove eventually (for debugging purposes)
				System.out.println(f);
				System.out.println(f.getClass());
				System.out.flush();
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
		else if(f instanceof LTLNegation)
		{
			// get the operand formula of this negation
			LTLExpression f1 = ((LTLNegation) f).getOperand();

			// return the opposite of it
			return !entails(s, f1);
		}
		else if(f instanceof LTLForAll)
		{
			LTLExpression premise = ((LTLForAll) f).getPremise();
			LTLExpression consequent = ((LTLForAll) f).getConsequent();

			// assume the premise is atomic
			Predicate atom = ((LTLAtom) premise).getAtom();
			MyIterator me = s.iterator(atom.getHead());
			Term[] binding;

			// for each binding that satisfies the premise
			while((binding = s.nextBinding(atom, me)) != null)
			{
				// TODO: implement entails forall
				// apply the binding to the consequent
				
				// if the state does not entail the new consequent
				
				// return false
			}

			// the state entailed the consequent for all valid premise values
			return true;
		}
		else if(f instanceof LTLExists)
		{
			// TODO: implement entails exists
			return true;
		}
		else if(f instanceof LTLAtom)
		{
			Predicate atom = ((LTLAtom) f).getAtom();

			// ASSERT atom is ground
			if(!atom.isGround())
			{
				System.out.println(atom);
				System.out.flush();
				throw new IllegalArgumentException("All atoms should be ground.");
			}
			return entails(s, f);
		}
		
		// this should never happen
		else
		{
			System.out.println(f);
			System.out.println(f.getClass());
			System.out.flush();
			throw new IllegalArgumentException(
			  "Argument has no temporal operators but is not an instance of Conjunction, Disjunction, Negation, Forall, Exists, or Atom.");
		}
	}

	/**
	 * Computes all possible variable bindings that make a given expression true
	 * in a given state. Used for computing the bindings for the premises of
	 * ForAll and Exists operators.
	 * 
	 * @param s
	 *          The current state of the world.
	 * @param premise
	 *          The formula to get all bindings for. This formula must be a valid
	 *          premise to a ForAll or Exists operator, meaning that it can be
	 *          composed only of "and" and "or" operators, and atoms.
	 * @return a <code>LinkedList</code> of all bindings that individually
	 *         satisfy <code>premise</code> in the given state.
	 */
	private static LinkedList<Term[]> getAllValidBindings(State s, LTLExpression premise)
	{
		// if the top operator is a conjunction
		if(premise instanceof LTLConjunction)
		{
			// "intersect" the bindings from the recursive subcalls
			
			LTLExpression[] conjuncts = ((LTLConjunction) premise).getConjuncts();
			
			// get all valid bindings for the first conjunct
			LinkedList<Term[]> bindings = getAllValidBindings(s, conjuncts[0]);
			
			// for each additional conjunct
			for(int i = 1; i < conjuncts.length; i++)
			{
				// get all valid bindings for it
				LinkedList<Term[]> b = getAllValidBindings(s, conjuncts[i]);
				
				// for each binding in our current set of overall valid bindings
				for(int j = 0; j < bindings.size(); j++)
				{
					Term[] t1 = bindings.get(j);
					boolean found = false;
					
					// for each binding in the newly returned set of valid bindings
					for(int k = 0; k < b.size(); k++)
					{
						Term[] t2 = b.get(k);
						boolean equal = true;
						
						// check if the bindings are equal
						for(int l = 0; j < t2.length; j++)
						{
							if(!t1[l].equals(t2[1]))
							{
								equal = false;
								break;
							}
						}
						
						// if they are, we have found a match;
						// therefore, the binding exists in both sets and should exist
						// in the intersection.
						if(equal)
						{
							found = true;
							break;
						}
					}

					// if we haven't found a match, this binding should not exist
					// in the intersection.
					if(!found)
					{
						bindings.remove(i);
						i--;
					}
				}
			}
			
			return bindings;
		}
		
		// if the top operator is a disjunction
		else if(premise instanceof LTLDisjunction)
		{
			// "union" the bindings from the recursive subcalls
			
			LTLExpression[] disjuncts = ((LTLDisjunction) premise).getDisjuncts();
			
			// get all valid bindings for the first conjunct
			LinkedList<Term[]> bindings = getAllValidBindings(s, disjuncts[0]);
			
			// for each additional conjunct
			for(int i = 1; i < disjuncts.length; i++)
			{
				// get all valid bindings for it
				LinkedList<Term[]> b = getAllValidBindings(s, disjuncts[i]);
				
				// for each binding in the newly returned set of valid bindings
				for(int j = 0; j < b.size(); j++)
				{
					Term[] t1 = b.get(j);
					boolean found = false;
					
					// for each binding in our current set of overall valid bindings
					for(int k = 0; k < bindings.size(); k++)
					{
						Term[] t2 = bindings.get(k);
						boolean equal = true;
						
						// check if the bindings are equal
						for(int l = 0; j < t2.length; j++)
						{
							if(!t1[l].equals(t2[1]))
							{
								equal = false;
								break;
							}
						}
						
						// if they are, we have found a match;
						// therefore, the binding exists in both sets and should exist
						// in the intersection.
						if(equal)
						{
							found = true;
							break;
						}
					}

					// if we haven't found a match, this binding should be added to the
					// union of all bindings
					if(!found)
					{
						bindings.add(t1);
					}
				}
			}
			
			return bindings;
		}
		
		// if the formula is atomic
		else if(premise instanceof LTLAtom)
		{
			Predicate atom = ((LTLAtom) premise).getAtom();
			MyIterator me = s.iterator(atom.getHead());
			LinkedList<Term[]> bindings = new LinkedList<Term[]>();
			Term[] binding;

			while((binding = s.nextBinding(atom, me)) != null)
			{
				bindings.add(binding);
			}

			return bindings;
		}
		
		// the formula is not allowed to have anything but ands/ors/atoms
		else
		{
			System.out.println(premise);
			System.out.println(premise.getClass());
			System.out.flush();
			throw new IllegalArgumentException(
			  "Premise is only allowed to contain \"and\" and \"or\" operators, and atoms.");
		}
	}

}
