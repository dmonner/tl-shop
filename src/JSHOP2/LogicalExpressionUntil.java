package JSHOP2;

/**
 * Each "until" term in a logical expression at compile time is represented as
 * an instance of this class.
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LogicalExpressionUntil extends LogicalExpression
{
	/**
	 * The logical expression that must hold until <code>secondExpression</code>.
	 */
	private LogicalExpression firstExpression;

	/**
	 * The logical expression that, when/if it becomes true, will allow
	 * <code>firstExpression</code> to become false.
	 */
	private LogicalExpression secondExpression;

	/**
	 * To initialize this "until" logical expression.
	 * 
	 * @param firstIn
	 *          the logical expression that must hold until
	 *          <code>secondExpression</code>.
	 * @param secondIn
	 *          the logical expression that, when/if it becomes true, will allow
	 *          <code>firstExpression</code> to become false.
	 */
	public LogicalExpressionUntil(LogicalExpression firstIn,
	  LogicalExpression secondIn)
	{
		firstExpression = firstIn;
		secondExpression = secondIn;

		hasTemporalOps = true;
	}
	
	/**
	 * @return the first operand of this "until" operator
	 */
	public LogicalExpression getFirstOperand() {
		return firstExpression;
	}
	
	/**
	 * @return the second operand of this "until" operator
	 */
	public LogicalExpression getSecondOperand() {
		return secondExpression;
	}

	/**
	 * This function produces Java code that implements the classes any object of
	 * which can be used at run time to represent the two logical subexpressions
	 * of the "until" logical expression this object is representing.
	 */
	public String getInitCode()
	{
		return firstExpression.getInitCode() + secondExpression.getInitCode();
	}

	/**
	 * To propagate the variable count to the "until" logical expression
	 * represented by this object.
	 */
	protected void propagateVarCount(int varCount)
	{
		firstExpression.setVarCount(varCount);
		secondExpression.setVarCount(varCount);
	}

	/**
	 * This function produces the Java code to create a
	 * <code>PreconditionForAll</code> object that represents this
	 * <code>ForAll</code> logical expression at run time.
	 */
	public String toCode()
	{
		// TODO: fix toCode()
		return "new PreconditionForAll(" + firstExpression.toCode() + ", "
		  + secondExpression.toCode() + ", " + getVarCount() + ")";
	}
}
