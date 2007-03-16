package JSHOP2;

import java.util.Vector;

/**
 * This class provides static convenience methods for working with control
 * rules in Linear Temporal Logic (as <code>LogicalExpression</code> objects).
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class ControlRules
{
	/**
	 * @param s
	 * @param f
	 * @return
	 */
	public static LogicalExpression progress(State s, LogicalExpression f)
	{
		// if there are temporal operators, we cannot evaluate the formula directly
		if(f.hasTemporalOperators())
		{
			
			// if the top operator is a conjunction
			//   then this expression has the form: 
			//     and{ f1, f2, ..., fn }
			//   and the formula to return is: 
			//     and{ progress(s, f1), progress(s, f2), ..., progress(s, fn) }
			if(f instanceof LogicalExpressionConjunction)
			{
				// get a list of the conjuncts
				LogicalExpression[] fn = ((LogicalExpressionConjunction) f).getConstituents();
				
				// create a new vector to hold the progressed subexpressions
				Vector v = new Vector(fn.length);
				
				// progress each of the subexpressions individually
				for(int i = 0; i < fn.length; i++) 
				{
					// progress the subexpression
					LogicalExpression p = progress(s, fn[i]);
					
					// if the progression evaluates to FALSE, then our entire conjunction
					// is FALSE, so return FALSE.
					if(p instanceof LogicalExpressionFalse)
						return LogicalExpressionFalse.getInstance();
					// if the progression evaluates to TRUE, then it is irrelevant to our
					// conjunction, and can be removed.
					// otherwise, we must add it to the new conjunction.
					else if(!(p instanceof LogicalExpressionTrue))
						v.add(p);
				}
				
				// recombine the conjuncts
				return new LogicalExpressionConjunction(v);
			}
			
			// if the top operator is a disjunction
			//   then this expression has the form: 
			//     or{ f1, f2, ..., fn }
			//   and the formula to return is: 
			//     or{ progress(s, f1), progress(s, f2), ..., progress(s, fn) }
			else if(f instanceof LogicalExpressionDisjunction)
			{
				// get a list of the disjuncts
				LogicalExpression[] fn = ((LogicalExpressionDisjunction) f).getConstituents();

				// create a new vector to hold the progressed subexpressions
				Vector v = new Vector(fn.length);
				
				// progress each of the subexpressions individually
				for(int i = 0; i < fn.length; i++) 
				{
					// progress the subexpression
					LogicalExpression p = progress(s, fn[i]);
					
					// if the progression evaluates to TRUE, then our entire disjunction
					// is TRUE, so return TRUE.
					if(p instanceof LogicalExpressionTrue)
						return LogicalExpressionTrue.getInstance();
					// if the progression evaluates to FALSE, then it is irrelevant to our
					// disjunction, and can be removed.
					// otherwise, we must add it to the new disjunction.
					else if(!(p instanceof LogicalExpressionFalse))
						v.add(p);
				}
				
				// recombine the disjuncts
				return new LogicalExpressionDisjunction(v);
			}
			
			// if the operator is a negation
			//   then this expression has the form: 
			//     not f1
			//   and the formula to return is: 
			//     not progress(s, f1)
			else if(f instanceof LogicalExpressionNegation)
			{
				// get the operand of this negation
				LogicalExpression f1 = ((LogicalExpressionNegation) f).getOperand();
				
				// calculates progress(s, f1)
				LogicalExpression pf1 = progress(s, f1);
				
				// if progress(s, f1) evaluates to TRUE
				if(pf1 instanceof LogicalExpressionTrue)
				{
					// then the negation of progress(s, f1) is FALSE
					return LogicalExpressionFalse.getInstance();
				}
				// if progress(s, f1) evaluates to FALSE
				else if(pf1 instanceof LogicalExpressionFalse)
				{
					// then the negation of progress(s, f1) is TRUE
					return LogicalExpressionTrue.getInstance();
				}
				// otherwise we return the negated formula
				else
				{
					return new LogicalExpressionNegation(pf1);
				}
			}
			
			// if the operator is "next"
			//   then this expression has the form: 
			//     next f1
			//   and the formula to return is: 
			//     f1
			else if(f instanceof LogicalExpressionNext)
			{
				// return the operand of this "next" operator
				return ((LogicalExpressionNext) f).getOperand();
			}
			
			// if the operator is "until"
			//   then this expression has the form: 
			//     f1 until f2
			//   and the formula to return is: 
			//     progress(s, f2) | (progress(s, f1) & f)
			else if(f instanceof LogicalExpressionUntil)
			{
				// get the operands of this "until" operator
				LogicalExpression f1 = ((LogicalExpressionUntil) f).getFirstOperand();
				LogicalExpression f2 = ((LogicalExpressionUntil) f).getSecondOperand();
				
				// calculate progress(s, f2)
				LogicalExpression pf2 = progress(s, f2);
				
				// if progress(s, f2) is TRUE
				if(pf2 instanceof LogicalExpressionTrue)
				{
					// then our formula reduces to TRUE
					return LogicalExpressionTrue.getInstance();
				}
				// if progress(s, f2) is FALSE
				else if(pf2 instanceof LogicalExpressionFalse)
				{
					// then our formula simplifies to progress(s, f1) & f
					LogicalExpression pf1 = progress(s, f1);
					
					// if progress(s, f1) is TRUE
					if(pf1 instanceof LogicalExpressionTrue)
					{
						// then our formula simplifies to f
						return f;
					}
					// if progress(s, f1) if FALSE
					else if(pf1 instanceof LogicalExpressionFalse)
					{
						// then our formula simplifies to FALSE
						return LogicalExpressionFalse.getInstance();
					}
					// otherwise our formula is progress(s, f1) & f
					else
					{
						Vector conjuncts = new Vector();
						conjuncts.add(pf1);
						conjuncts.add(f);
						return new LogicalExpressionConjunction(conjuncts);
					}
				}
				// otherwise we may need to evaluate the whole formula
				else
				{
					LogicalExpression pf1 = progress(s, f1);
					
					// if progress(s, f1) is TRUE
					if(pf1 instanceof LogicalExpressionTrue)
					{
						// then our formula simplifies to progress(s, f2) | f
						Vector disjuncts = new Vector();
						disjuncts.add(pf2);
						disjuncts.add(f);
						return new LogicalExpressionDisjunction(disjuncts);						
					}
					// if progress(s, f1) if FALSE
					else if(pf1 instanceof LogicalExpressionFalse)
					{
						// then our formula simplifies to progress(s, f2)
						return pf2;
					}
					// otherwise we must evaluate the full formula, progress(s, f1) & f
					else
					{
						// construct the conjunction: progress(s, f1) & f
						Vector conjuncts = new Vector();
						conjuncts.add(pf1);
						conjuncts.add(f);
						LogicalExpression conjunction = new LogicalExpressionConjunction(conjuncts);
						
						// construct the disjunction: progress(s, f2) | (progress(s, f1) & f)
						Vector disjuncts = new Vector();
						disjuncts.add(pf2);
						disjuncts.add(conjunction);
						return new LogicalExpressionDisjunction(disjuncts);
					}
				}
			}
			
			// if the operator is "eventually"
			//   then this expression has the form: 
			//     eventually f1
			//   and the formula to return is: 
			//     progress(s, f1) | f
			else if(f instanceof LogicalExpressionEventually)
			{
				// get the operands of this "eventually" operator
				LogicalExpression f1 = ((LogicalExpressionEventually) f).getOperand();
				
				// calculate progress(s, f1)
				LogicalExpression pf1 = progress(s, f1);
				
				// if progress(s, f1) evaluates to TRUE,
				if(pf1 instanceof LogicalExpressionTrue)
				{
					// then our formula simplifies to TRUE.
					return LogicalExpressionTrue.getInstance();
				}
				// if progress(s, f1) evaluates to FALSE,
				else if(pf1 instanceof LogicalExpressionFalse)
				{
					// then our formula simplifies to f
					return f;
				}
				// otherwise our formula does not simplify at all
				else
				{
					// construct the disjunction: progress(s, f1) | f
					Vector disjuncts = new Vector();
					disjuncts.add(pf1);
					disjuncts.add(f);
					return new LogicalExpressionDisjunction(disjuncts);					
				}
			}
			
			// if the operator is "always"
			//   then this expression has the form: 
			//     always f1
			//   and the formula to return is: 
			//     progress(s, f1) & f
			else if(f instanceof LogicalExpressionAlways)
			{
				// get the operands of this "eventually" operator
				LogicalExpression f1 = ((LogicalExpressionEventually) f).getOperand();
				
				// calculate progress(s, f1)
				LogicalExpression pf1 = progress(s, f1);
				
				// if progress(s, f1) evaluates to TRUE,
				if(pf1 instanceof LogicalExpressionTrue)
				{
					// then our formula simplifies to f.
					return f;
				}
				// if progress(s, f1) evaluates to FALSE,
				else if(pf1 instanceof LogicalExpressionFalse)
				{
					// then our formula simplifies to FALSE
					return LogicalExpressionFalse.getInstance();
				}
				// otherwise our formula does not simplify at all
				else
				{
					// construct the conjunction: progress(s, f1) & f
					Vector conjuncts = new Vector();
					conjuncts.add(pf1);
					conjuncts.add(f);
					return new LogicalExpressionConjunction(conjuncts);					
				}
			}
			
			// if the operator is "forall"
			//   then this expression has the form: 
			//     forall x (premise -> consequence)
			//   (where "premise" is an atom that is true for only finitely many x)
			//   and the formula to return is: 
			//     not premise | progress(s, consequence) 
			else if(f instanceof LogicalExpressionForAll)
			{
				//FIXME: Not entirely sure how to proceed here. Experiment.
				
				
				return null;
			}
			
			// The following classes will never have hasTemporalOperators() true, so
			// we need not deal with them here:
			//   LogicalExpressionNil
			//   LogicalExpressionAssign
			//   LogicalExpressionAtomic
			//   LogicalExpressionCall
			
			// this should never happen
			else 
			{
				//TODO: remove eventually (for debugging purposes)
				//TODO: implement a "toString" for each LogicalExpression class to provide pretty prints.
				System.out.println(f);
				System.out.println(f.getClass());
				System.out.flush();
				throw new IllegalArgumentException("Somehow hasTemporalOperators() returned true but the top operator was not in our list.");
			}
		}
		// this formula has no temporal operators
		else
		{
			// if the current state entails this formula
			if(entails(s, f))
				// return "true"
				return LogicalExpressionTrue.getInstance();
			else 
				// otherwise return "false"
				return LogicalExpressionFalse.getInstance();
		}
	}
	
	/**
	 * @param s
	 * @param expr
	 * @return
	 */
	public static boolean entails(State s, LogicalExpression expr)
	{
		//TODO: implement entails()
		return true;
	}
}
