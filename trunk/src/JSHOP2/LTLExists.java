package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "exists(x : g(x)) expr". "g(x)" is called premise and must be an
 * atom involving "x". The "expr" is called the consequent and may be any LTL
 * expression. This expression is equivalent to "exists(x), g(x) and expr".
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLExists extends LTLExpression
{
	/**
	 * The premise of this exists expression; an atom involving x.
	 */
	private LTLAtom premise;

	/**
	 * The consequent of this exists expression; it must be true for at least one value
	 * of x that makes the premise true.
	 */
	private LTLExpression consequent;

	/**
	 * @param premiseIn the premise to use in this exists expression.
	 * @param consequentIn the consequent to use in this exists expression.
	 */
	public LTLExists(LTLAtom premiseIn, LTLExpression consequentIn)
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
	 * @return the premise of this forall expression.
	 */
	public LTLExpression getConsequent()
	{
		return consequent;
	}

	/* (non-Javadoc)
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
	public String toCode()
	{
		return "new LTLExists(" + premise.toCode() + ", " + consequent.toCode()
		  + ")";
	}
}
