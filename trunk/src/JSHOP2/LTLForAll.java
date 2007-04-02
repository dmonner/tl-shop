package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "forall(x : g(x)) expr". "g(x)" is called premise and must be an
 * atom involving "x". The "expr" is called the consequent and may be any LTL
 * expression. This expression is equivalent to "forall x, g(x) => expr".
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLForAll extends LTLExpression
{
	/**
	 * The premise of this forall expression; an atom involving x.
	 */
	private LTLAtom premise;

	/**
	 * The consequent of this forall expression; it must be true for each value of
	 * x that makes the premise true.
	 */
	private LTLExpression consequent;

	/**
	 * @param premiseIn
	 *          the premise to use in this forall expression.
	 * @param consequentIn
	 *          the consequent to use in this forall expression.
	 */
	public LTLForAll(LTLAtom premiseIn, LTLExpression consequentIn)
	{
		premise = premiseIn;
		consequent = consequentIn;
		hasTemporalOps = consequent.hasTemporalOperators();
	}

	/**
	 * @return the premise of this forall expression.
	 */
	public LTLAtom getPremise()
	{
		return premise;
	}

	/**
	 * @return the consequent of this forall expression.
	 */
	public LTLExpression getConsequent()
	{
		return consequent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
	public String toCode()
	{
		return "new LTLForAll(" + premise.toCode() + ", " + consequent.toCode()
		  + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
	public LTLExpression applySubstitution(Term[] binding)
	{
		return new LTLForAll(premise.applySubstitution(binding), consequent
		  .applySubstitution(binding));
	}
}
