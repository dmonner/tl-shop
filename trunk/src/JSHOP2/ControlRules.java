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
			if(f instanceof LogicalExpressionConjunction)
			{
				// get a list of the conjuncts
				LogicalExpression[] a = ((LogicalExpressionConjunction) f).getConstituents();
				
				// create a new vector to hold the progressed subexpressions
				Vector v = new Vector(a.length);
				
				// progress each of the subexpressions individually
				for(int i = 0; i < a.length; i++) 
				{
					// progress the subexpression
					LogicalExpression p = progress(s, a[i]);
					
					// if the progression evaluates to FALSE, then our entire conjunction
					// is FALSE, so return FALSE.
					if(p instanceof LogicalExpressionFalse)
						return p;
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
			else if(f instanceof LogicalExpressionDisjunction)
			{
				// get a list of the disjuncts
				LogicalExpression[] a = ((LogicalExpressionDisjunction) f).getConstituents();

				// create a new vector to hold the progressed subexpressions
				Vector v = new Vector(a.length);
				
				// progress each of the subexpressions individually
				for(int i = 0; i < a.length; i++) 
				{
					// progress the subexpression
					LogicalExpression p = progress(s, a[i]);
					
					// if the progression evaluates to TRUE, then our entire disjunction
					// is TRUE, so return TRUE.
					if(p instanceof LogicalExpressionTrue)
						return p;
					// if the progression evaluates to FALSE, then it is irrelevant to our
					// disjunction, and can be removed.
					// otherwise, we must add it to the new disjunction.
					else if(!(p instanceof LogicalExpressionFalse))
						v.add(p);
				}
				
				// recombine the disjuncts
				return new LogicalExpressionDisjunction(v);
			}
			else if(f instanceof LogicalExpressionNegation)
			{
				// get the operand of this negation
				LogicalExpression op = ((LogicalExpressionNegation) f).getOperand();
				
				// return the negation of its progression
				return new LogicalExpressionNegation(progress(s, op));
			}
			else if(f instanceof LogicalExpressionNext)
			{
				// return the operand of this "next" operator
				return ((LogicalExpressionNext) f).getOperand();
			}
			else if(f instanceof LogicalExpressionUntil)
			{
				// get the operands of this "until" operator
				LogicalExpression f1 = ((LogicalExpressionUntil) f).getFirstOperand();
				LogicalExpression f2 = ((LogicalExpressionUntil) f).getSecondOperand();
				
				//TODO: add in boolean simplifications
				
				// construct the conjunction: progress(s, f1) & f)
				Vector conjuncts = new Vector();
				conjuncts.add(progress(s, f1));
				conjuncts.add(f);
				LogicalExpression conjunction = new LogicalExpressionConjunction(conjuncts);
				
				// construct the disjunction: progress(s, f2) | (progress(s, f1) & f)
				Vector disjuncts = new Vector();
				disjuncts.add(progress(s, f2));
				disjuncts.add(conjunction);
				return new LogicalExpressionDisjunction(disjuncts);
			}
			//TODO: Eventually, Always, ForAll, others?
			else 
			{
				//TODO: remove eventually (for debugging purposes)
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
