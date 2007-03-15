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
	 * @param expr
	 * @return
	 */
	public static LogicalExpression progress(State s, LogicalExpression expr)
	{
		// if there are temporal operators, we cannot evaluate the formula directly
		if(expr.hasTemporalOperators())
		{
			// if the top operator is a conjunction
			if(expr instanceof LogicalExpressionConjunction)
			{
				// get a list of the conjuncts
				LogicalExpression[] a = ((LogicalExpressionConjunction) expr).getConstituents();
				
				// create a new vector to hold the progressed subexpressions
				Vector v = new Vector(a.length);
				
				// progress each of the subexpressions individually
				for(int i = 0; i < a.length; i++) 
				{
					// progress the subexpression
					LogicalExpression p = progress(s, a[i]);
					
					// if the progression evaluates to FALSE, then our entire conjunction
					// is FALSE, so return FALSE.
//					if(p instanceof LogicalExpressionFalse)
//						return p;
					// if the progression evaluates to TRUE, then it is irrelevant to our
					// conjunction, and can be removed.
					// otherwise, we must add it to the new conjunction.
//					else if(!(p instanceof LogicalExpressionTrue))
//						v.add(p);
				}
				
				// recombine the conjuncts
				return new LogicalExpressionConjunction(v);
			}
			// if the top operator is a disjunction
			else if(expr instanceof LogicalExpressionDisjunction)
			{
				// get a list of the disjuncts
				LogicalExpression[] a = ((LogicalExpressionDisjunction) expr).getConstituents();

				// create a new vector to hold the progressed subexpressions
				Vector v = new Vector(a.length);
				
				// progress each of the subexpressions individually
				for(int i = 0; i < a.length; i++) 
				{
					// progress the subexpression
					LogicalExpression p = progress(s, a[i]);
					
					// if the progression evaluates to TRUE, then our entire disjunction
					// is TRUE, so return TRUE.
//					if(p instanceof LogicalExpressionTrue)
//						return p;
					// if the progression evaluates to FALSE, then it is irrelevant to our
					// disjunction, and can be removed.
					// otherwise, we must add it to the new disjunction.
//					else if(!(p instanceof LogicalExpressionFalse))
//						v.add(p);
				}
				
				// recombine the disjuncts
				return new LogicalExpressionDisjunction(v);
			}
		}
		else
		{
//			if(entails(s, expr))
//				return ; // a static "true" LE
//			else 
//				return ; // a static "false" LE
		}
		
		//TODO: remove this
		return null;
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
