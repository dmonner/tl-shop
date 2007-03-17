package JSHOP2;

/**
 * Each "eventually" term in a logical expression at compile time is represented
 * as an instance of this class.
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLEventually extends LTLExpression
{
	/**
	 * The logical expression that must eventually hold.
	 */
	private LTLExpression operand;

	/**
	 * To initialize this eventually logical expression.
	 * 
	 * @param op
	 *          the logical expression that must eventually hold.
	 */
	public LTLEventually(LTLExpression op)
	{
		operand = op;
		hasTemporalOps = true;
	}

	/**
	 * @return the logical expression which this "eventually" operator affects
	 */
	public LTLExpression getOperand()
	{
		return operand;
	}

	public String toCode()
	{
		return "new LTLEventually(" + operand.toCode() + ")";
	}
}
