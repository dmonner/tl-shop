package JSHOP2;

/**
 * Each "always" term in a logical expression at compile time is represented as
 * an instance of this class.
 * 
 * @author Derek Monner
 * @author <a href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLAlways extends LTLExpression
{
	/**
	 * The logical expression that must always hold.
	 */
	private LTLExpression operand;

	/**
	 * To initialize this always logical expression.
	 * 
	 * @param op
	 *          the logical expression that must always hold.
	 */
	public LTLAlways(LTLExpression op)
	{
		operand = op;
		hasTemporalOps = true;
	}

	/**
	 * @return the logical expression which this "always" operator affects
	 */
	public LTLExpression getOperand()
	{
		return operand;
	}

	public String toCode()
	{
		return "new LTLAlways(" + operand.toCode() + ")";
	}
}
