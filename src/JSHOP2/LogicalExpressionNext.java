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
public class LogicalExpressionNext extends LogicalExpression
{
	/**
	 * The logical expression that must hold in the next state.
	 */
	private LogicalExpression le;

	/**
	 * To initialize this next logical expression.
	 * 
	 * @param leIn
	 *          the logical expression that must hold in the next state.
	 */
	public LogicalExpressionNext(LogicalExpression leIn)
	{
		le = leIn;
		hasTemporalOps = true;
	}

	/**
	 * This function produces Java code that implements the class any object of
	 * which can be used at run time to represent the logical expression that must
	 * hold in the next state.
	 */
	public String getInitCode()
	{
		return le.getInitCode();
	}

	/**
	 * To propagate the variable count to the logical expression that must
	 * hold in the next state which this object represents.
	 */
	protected void propagateVarCount(int varCount)
	{
		le.setVarCount(varCount);
	}

	/**
	 * This function produces the Java code to create a
	 * <code>PreconditionNegation</code> object that represents this negative
	 * logical expression at run time.
	 */
	public String toCode()
	{
		// TODO: fix toCode()
		return "new PreconditionNegation(" + le.toCode() + ", " + getVarCount()
		  + ")";
	}
}
