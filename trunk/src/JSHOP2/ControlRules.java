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
	public static LTLExpression progress(State s, LTLExpression f)
	{
		// if there are temporal operators, we cannot evaluate the formula directly
		if(f.hasTemporalOperators())
		{
			
			// if the top operator is a conjunction
			//   then this expression has the form: 
			//     f1 and f2
			//   and the formula to return is: 
			//     progress(s, f1) and progress(s, f2)
			if(f instanceof LTLConjunction)
			{
				// get the operands of this conjunction
				LTLExpression f1 = ((LTLConjunction) f).getFirstOperand();
				LTLExpression f2 = ((LTLConjunction) f).getSecondOperand();

				LTLExpression pf1 = progress(s, f1);
				
				// if progress(s, f1) evaluates to TRUE
				if(pf1 instanceof LTLTrue)
				{
					// then our formula simplifies to progress(s, f2)
					return progress(s, f2);
				}
				// if progress(s, f1) evaluates to FALSE
				else if(pf1 instanceof LTLFalse)
				{
					// then the entire conjunction is FALSE
					return LTLFalse.getInstance();
				}
				// otherwise, see if we can simplify our formula by evaluating progress(s, f2)
				else
				{
					LTLExpression pf2 = progress(s, f2);
					
					// if progress(s, f2) evaluation to TRUE
					if(pf2 instanceof LTLTrue)
					{
						// then our formula simplifies to progress(s, f1)
						return pf1;
					}
					// if progress(s, f2) evaluates to FALSE
					else if(pf2 instanceof LTLFalse)
					{
						// then the entire conjunction is FALSE
						return LTLFalse.getInstance();
					}
					// otherwise, our formula remains progress(s, f1) and progress(s, f2)
					else
					{
						return new LTLConjunction(pf1, pf2);
					}
				}
			}
			
			// if the top operator is a disjunction
			//   then this expression has the form: 
			//     f1 or f2
			//   and the formula to return is: 
			//     progress(s, f1) or progress(s, f2)
			else if(f instanceof LTLDisjunction)
			{
				// get the operands of this disjunction
				LTLExpression f1 = ((LTLDisjunction) f).getFirstOperand();
				LTLExpression f2 = ((LTLDisjunction) f).getSecondOperand();

				LTLExpression pf1 = progress(s, f1);
				
				// if progress(s, f1) evaluates to TRUE
				if(pf1 instanceof LTLTrue)
				{
					// then the entire disjunction is TRUE
					return LTLTrue.getInstance();
				}
				// if progress(s, f1) evaluates to FALSE
				else if(pf1 instanceof LTLFalse)
				{
					// then our formula simplifies to progress(s, f2)
					return progress(s, f2);
				}
				// otherwise, see if we can simplify our formula by evaluating progress(s, f2)
				else
				{
					LTLExpression pf2 = progress(s, f2);
					
					// if progress(s, f2) evaluates to TRUE
					if(pf2 instanceof LTLTrue)
					{
						// then the entire disjunction is TRUE
						return LTLTrue.getInstance();
					}
					// if progress(s, f2) evaluates to FALSE
					else if(pf2 instanceof LTLFalse)
					{
						// then our formula simplifies to progress(s, f1)
						return pf1;
					}
					// otherwise, our formula remains progress(s, f1) or progress(s, f2)
					else
					{
						return new LTLDisjunction(pf1, pf2);
					}
				}
			}
			
			// if the operator is a negation
			//   then this expression has the form: 
			//     not f1
			//   and the formula to return is: 
			//     not progress(s, f1)
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
			//   then this expression has the form: 
			//     next f1
			//   and the formula to return is: 
			//     f1
			else if(f instanceof LTLNext)
			{
				// return the operand of this "next" operator
				return ((LTLNext) f).getOperand();
			}
			
			// if the operator is "until"
			//   then this expression has the form: 
			//     f1 until f2
			//   and the formula to return is: 
			//     progress(s, f2) | (progress(s, f1) & f)
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
						return new LTLConjunction(pf1, f);
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
						return new LTLDisjunction(pf2, f);						
					}
					// if progress(s, f1) if FALSE
					else if(pf1 instanceof LTLFalse)
					{
						// then our formula simplifies to progress(s, f2)
						return pf2;
					}
					// otherwise we must evaluate the full formula, progress(s, f1) & f
					else
					{
						return new LTLDisjunction(pf2, new LTLConjunction(pf1, f));
					}
				}
			}
			
			// if the operator is "eventually"
			//   then this expression has the form: 
			//     eventually f1
			//   and the formula to return is: 
			//     progress(s, f1) | f
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
					return new LTLDisjunction(pf1, f);					
				}
			}
			
			// if the operator is "always"
			//   then this expression has the form: 
			//     always f1
			//   and the formula to return is: 
			//     progress(s, f1) & f
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
					return new LTLConjunction(pf1, f);					
				}
			}
			
			// if the operator is "forall"
			//   then this expression has the form: 
			//     forall x (premise -> consequent)
			//   (where "premise" is an atom that is true for only finitely many x)
			//   and the formula to return is: 
			//     not premise | progress(s, consequent) 
			else if(f instanceof LTLForAll)
			{
				//FIXME: Not entirely sure how to proceed here. Experiment.
				return null;
			}
		
			// if the operator is "exists"
			//   then this expression has the form: 
			//     forall x (premise -> consequent)
			//   (where "premise" is an atom that is true for only finitely many x)
			//   and the formula to return is: 
			//     not premise | progress(s, consequent) 
			else if(f instanceof LTLForAll)
			{
				//FIXME: Not entirely sure how to proceed here. Experiment.
				return null;
			}
		
			// this should never happen
			else 
			{
				//TODO: remove eventually (for debugging purposes)
				//TODO: implement a "toString" for each LTLExpression class to provide pretty prints.
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
				return LTLTrue.getInstance();
			else 
				// otherwise return "false"
				return LTLFalse.getInstance();
		}
	}
	
	/**
	 * @param s
	 * @param expr
	 * @return
	 */
	public static boolean entails(State s, LTLExpression expr)
	{
		//TODO: implement entails()
		return true;
	}
}
