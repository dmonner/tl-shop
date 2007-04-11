package JSHOP2;

import java.util.LinkedList;

/**
 * The subclasses of this class can be combined for form any expression in
 * Linear Temporal Logic, to be used as control rules during planning.
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public abstract class LTLExpression extends CompileTimeObject
{
	/**
	 * <code>true</code> iff this logical expression, or one below it, contains
	 * a temporal operator.
	 */
	protected boolean hasTemporalOps;

	/**
	 * This function is used to determine if this logical expression contains any
	 * temporal operators.
	 * 
	 * @return <code>true</code> iff this logical expression, or one below it,
	 *         contains a temporal operator.
	 */
	public boolean hasTemporalOperators()
	{
		return hasTemporalOps;
	}

	/**
	 * Override toString() for pretty printing.
	 */
	public String toString()
	{
		return toCode();
	}

	/**
	 * This method applies the given variable substitution to all predicates in
	 * this expression.
	 * 
	 * @param binding the variable binding to apply.
	 */
	public abstract LTLExpression applySubstitution(Term[] binding);

	/**
   * Reduces an <code>LTLExpression</code> to its simplest form. It does this
   * by:
   * 
   * <ul>
   * <li>Simplifying out all instances of <code>LTLTrue</code> and
   * <code>LTLFalse</code> unless the entire formula simplifies directly to
   * <code>LTLTrue</code> or <code>LTLFalse</code>, in which case these
   * values are returned);</li>
   * <li>Flattening directly-nested conjunctions and disjunctions;</li>
   * <li>Removing nested negations; and,</li>
   * <li>Simplifying <code>p until false</code> to <code>always p</code>;</li>
   * </ul>
   * 
   * This method does not remove duplicate conjuncts or disjuncts.
   * 
   * @param f
   *          the formula to simplify.
   * @return a simplified formula that is logically equivalent to <code>f</code>.
   */
  public static LTLExpression simplify(LTLExpression f)
  {
  	// LTLConjunction case
  	if(f instanceof LTLConjunction)
  	{
  		LTLExpression[] conjuncts = ((LTLConjunction) f).getConjuncts();
  		LinkedList<LTLExpression> newConjuncts = new LinkedList<LTLExpression>();
  
  		// for each conjunct
  		for(int i = 0; i < conjuncts.length; i++)
  		{
  			// simplify it
  			LTLExpression c = simplify(conjuncts[i]);
  
  			// if a conjunct is itself a conjunction
  			if(c instanceof LTLConjunction)
  			{
  				// expand it into the higher-level conjunction
  				LTLExpression[] subconj = ((LTLConjunction) c).getConjuncts();
  
  				for(int j = 0; j < subconj.length; j++)
  					newConjuncts.add(subconj[j]);
  			}
  			// if a conjunct is false, the whole expression is false
  			else if(c instanceof LTLFalse)
  			{
  				return LTLFalse.getInstance();
  			}
  			// if a conjunct is true, omit it; otherwise, add it as before
  			else if(!(c instanceof LTLTrue))
  			{
  				newConjuncts.add(c);
  			}
  		}
  		
  		if(newConjuncts.isEmpty())
  		{
  			return LTLTrue.getInstance();
  		}
  		else if(newConjuncts.size() == 1)
  		{
  			return newConjuncts.get(0);
  		}
  		else 
  		{
  			return new LTLConjunction(newConjuncts.toArray(new LTLExpression[0]));
  		}
  	}
  
  	// LTLDisjunction case
  	else if(f instanceof LTLDisjunction)
  	{
  		LTLExpression[] disjuncts = ((LTLDisjunction) f).getDisjuncts();
  		LinkedList<LTLExpression> newDisjuncts = new LinkedList<LTLExpression>();
  
  		// for each disjunct
  		for(int i = 0; i < disjuncts.length; i++)
  		{
  			// simplify it
  			LTLExpression d = simplify(disjuncts[i]);
  
  			// if a disjunct is itself a disjunction
  			if(d instanceof LTLDisjunction)
  			{
  				// expand it into the higher-level disjunction
  				LTLExpression[] subconj = ((LTLDisjunction) d).getDisjuncts();
  
  				for(int j = 0; j < subconj.length; j++)
  					newDisjuncts.add(subconj[j]);
  			}
  			// if a disjunct is true, the whole expression is true
  			else if(d instanceof LTLTrue)
  			{
  				return LTLTrue.getInstance();
  			}
  			// if a disjunct is false, omit it; otherwise, add it as before
  			else if(!(d instanceof LTLFalse))
  			{
  				newDisjuncts.add(d);
  			}
  		}
  		
  		if(newDisjuncts.isEmpty())
  		{
  			return LTLFalse.getInstance();
  		}
  		else if(newDisjuncts.size() == 1)
  		{
  			return newDisjuncts.get(0);
  		}
  		else 
  		{
  			return new LTLDisjunction(newDisjuncts.toArray(new LTLExpression[0]));
  		}
  	}
  
  	// LTLForAll case
  	else if(f instanceof LTLForAll)
  	{
  		LTLAtom premise = ((LTLForAll) f).getPremise();
  		LTLExpression consequent = simplify(((LTLForAll) f).getConsequent());
  
  		if(consequent instanceof LTLTrue)
  		{
  			return LTLTrue.getInstance();
  		}
  		else if(consequent instanceof LTLFalse)
  		{
  			return LTLFalse.getInstance();
  		}
  		else
  		{
  			return new LTLForAll(premise, consequent);
  		}
  	}
  
  	// LTLExists case
  	else if(f instanceof LTLExists)
  	{
  		LTLAtom premise = ((LTLExists) f).getPremise();
  		LTLExpression consequent = simplify(((LTLExists) f).getConsequent());
  
  		if(consequent instanceof LTLTrue)
  		{
  			return LTLTrue.getInstance();
  		}
  		else if(consequent instanceof LTLFalse)
  		{
  			return LTLFalse.getInstance();
  		}
  		else
  		{
  			return new LTLExists(premise, consequent);
  		}
  	}
  
  	// LTLNegation case
  	else if(f instanceof LTLNegation)
  	{
  		LTLExpression operand = simplify(((LTLNegation) f).getOperand());
  
  		if(operand instanceof LTLTrue)
  		{
  			return LTLFalse.getInstance();
  		}
  		else if(operand instanceof LTLFalse)
  		{
  			return LTLTrue.getInstance();
  		}
  		else if(operand instanceof LTLNegation)
  		{
  			return ((LTLNegation) operand).getOperand();
  		}
  		else
  		{
  			return new LTLNegation(operand);
  		}
  	}
  
  	// LTLNext case
  	else if(f instanceof LTLNext)
  	{
  		LTLExpression operand = simplify(((LTLNext) f).getOperand());
  
  		if(operand instanceof LTLTrue)
  		{
  			return LTLTrue.getInstance();
  		}
  		else if(operand instanceof LTLFalse)
  		{
  			return LTLFalse.getInstance();
  		}
  		else
  		{
  			return new LTLNext(operand);
  		}
  	}
  
  	// LTLAlways case
  	else if(f instanceof LTLAlways)
  	{
  		LTLExpression operand = simplify(((LTLAlways) f).getOperand());
  
  		if(operand instanceof LTLTrue)
  		{
  			return LTLTrue.getInstance();
  		}
  		else if(operand instanceof LTLFalse)
  		{
  			return LTLFalse.getInstance();
  		}
  		else
  		{
  			return new LTLAlways(operand);
  		}
  	}
  
  	// LTLEventually case
  	else if(f instanceof LTLEventually)
  	{
  		LTLExpression operand = simplify(((LTLEventually) f).getOperand());
  
  		if(operand instanceof LTLTrue)
  		{
  			return LTLTrue.getInstance();
  		}
  		else if(operand instanceof LTLFalse)
  		{
  			return LTLFalse.getInstance();
  		}
  		else
  		{
  			return new LTLEventually(operand);
  		}
  	}
  
  	// LTLUntil case
  	else if(f instanceof LTLUntil)
  	{
  		LTLExpression operand1 = simplify(((LTLUntil) f).getFirstOperand());
  		LTLExpression operand2 = simplify(((LTLUntil) f).getSecondOperand());
  
  		if(operand1 instanceof LTLTrue)
  		{
  			// true until p <=> true
  			return LTLTrue.getInstance();
  		}
  		else if(operand1 instanceof LTLFalse)
  		{
  			// false until p <=> p
  			return operand2;
  		}
  		else if(operand2 instanceof LTLTrue)
  		{
  			// p until true <=> true
  			return LTLTrue.getInstance();
  		}
  		else if(operand2 instanceof LTLFalse)
  		{
  			// p until false <=> always p
  			return new LTLAlways(operand1);
  		}
  		else
  		{
  			return new LTLUntil(operand1, operand2);
  		}
  	}
  
  	// LTLAtom, LTLTrue, LTLFalse cases, which are already simplified.
  	else
  	{
  		return f;
  	}
  }

	/**
   * Disjoins two expression in the simplest way possible. If the two
   * expressions were completely simplified beforehand, they are guaranteed to
   * be simplified afteward.
   * 
   * @param a
   *          the first expression.
   * @param b
   *          the second expression.
   * @return the simplified result of disjoining these two expressions.
   */
  public static LTLExpression disjoin(LTLExpression a, LTLExpression b)
  {
  	if(a instanceof LTLTrue || b instanceof LTLTrue)
  	{
  		return LTLTrue.getInstance();
  	}
  	else if(a instanceof LTLFalse)
  	{
  		return b;
  	}
  	else if(b instanceof LTLFalse)
  	{
  		return a;
  	}
  	else if(a instanceof LTLDisjunction && b instanceof LTLDisjunction)
  	{
  		LTLExpression[] disjA = ((LTLDisjunction) a).getDisjuncts();
  		LTLExpression[] disjB = ((LTLDisjunction) b).getDisjuncts();
  		LTLExpression[] disjuncts = new LTLExpression[disjA.length + disjB.length];
  
  		for(int i = 0; i < disjA.length; i++)
  			disjuncts[i] = disjA[i];
  
  		for(int i = 0; i < disjB.length; i++)
  			disjuncts[disjA.length + i] = disjB[i];
  
  		return new LTLDisjunction(disjuncts);
  	}
  	else if(a instanceof LTLDisjunction)
  	{
  		LTLExpression[] disjA = ((LTLDisjunction) a).getDisjuncts();
  		LTLExpression[] disjuncts = new LTLExpression[disjA.length + 1];
  
  		for(int i = 0; i < disjA.length; i++)
  			disjuncts[i] = disjA[i];
  
  		disjuncts[disjA.length] = b;
  
  		return new LTLDisjunction(disjuncts);
  	}
  	else if(b instanceof LTLDisjunction)
  	{
  		LTLExpression[] disjB = ((LTLDisjunction) b).getDisjuncts();
  		LTLExpression[] disjuncts = new LTLExpression[disjB.length + 1];
  
  		disjuncts[0] = a;
  
  		for(int i = 0; i < disjB.length; i++)
  			disjuncts[i + 1] = disjB[i];
  
  		return new LTLDisjunction(disjuncts);
  	}
  	else
  	{
  		return new LTLDisjunction(new LTLExpression[]
  		{
  		  a, b
  		});
  	}
  }

	/**
   * Conjoins two expression in the simplest way possible. If the two
   * expressions were completely simplified beforehand, they are guaranteed to
   * be simplified afteward.
   * 
   * @param a
   *          the first expression.
   * @param b
   *          the second expression.
   * @return the simplified result of conjoining these two expressions.
   */
  public static LTLExpression conjoin(LTLExpression a, LTLExpression b)
  {
  	if(a instanceof LTLFalse || b instanceof LTLFalse)
  	{
  		return LTLFalse.getInstance();
  	}
  	else if(a instanceof LTLTrue)
  	{
  		return b;
  	}
  	else if(b instanceof LTLTrue)
  	{
  		return a;
  	}
  	else if(a instanceof LTLConjunction && b instanceof LTLConjunction)
  	{
  		LTLExpression[] conjA = ((LTLConjunction) a).getConjuncts();
  		LTLExpression[] conjB = ((LTLConjunction) b).getConjuncts();
  		LTLExpression[] conjuncts = new LTLExpression[conjA.length + conjB.length];
  
  		for(int i = 0; i < conjA.length; i++)
  			conjuncts[i] = conjA[i];
  
  		for(int i = 0; i < conjB.length; i++)
  			conjuncts[conjA.length + i] = conjB[i];
  
  		return new LTLConjunction(conjuncts);
  	}
  	else if(a instanceof LTLConjunction)
  	{
  		LTLExpression[] conjA = ((LTLConjunction) a).getConjuncts();
  		LTLExpression[] conjuncts = new LTLExpression[conjA.length + 1];
  
  		for(int i = 0; i < conjA.length; i++)
  			conjuncts[i] = conjA[i];
  
  		conjuncts[conjA.length] = b;
  
  		return new LTLConjunction(conjuncts);
  	}
  	else if(b instanceof LTLConjunction)
  	{
  		LTLExpression[] conjB = ((LTLConjunction) b).getConjuncts();
  		LTLExpression[] conjuncts = new LTLExpression[conjB.length + 1];
  
  		conjuncts[0] = a;
  
  		for(int i = 0; i < conjB.length; i++)
  			conjuncts[i + 1] = conjB[i];
  
  		return new LTLConjunction(conjuncts);
  	}
  	else
  	{
  		return new LTLConjunction(new LTLExpression[]
  		{
  		  a, b
  		});
  	}
  }
}
