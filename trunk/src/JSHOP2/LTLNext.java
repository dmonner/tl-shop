package JSHOP2;

/**
 * Each "next" term in a logical expression at compile time is represented
 * as an instance of this class.
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLNext extends LTLExpression
{
	/**
	 * The logical expression that must hold in the next state.
	 */
	private LTLExpression operand;

	/**
	 * To initialize this next logical expression.
	 * 
	 * @param op
	 *          the logical expression that must hold in the next state.
	 */
	public LTLNext(LTLExpression op)
	{
		operand = op;
		hasTemporalOps = true;
	}
	
	/**
	 * @return the logical expression which this "next" operator affects
	 */
	public LTLExpression getOperand()
	{
		return operand;
	}

	public String toCode()
	{
		return "new LTLNext(" + operand.toCode() + ")";
	}
}
