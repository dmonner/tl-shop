package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "always expr".
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLAlways extends LTLExpression
{
	/**
	 * The expression that must always hold.
	 */
	private LTLExpression operand;

	/**
	 * @param operandIn
	 *          the expression that must always hold.
	 */
	public LTLAlways(LTLExpression operandIn)
	{
		operand = operandIn;
		hasTemporalOps = true;
	}

	/**
	 * @return the expression that must always hold.
	 */
	public LTLExpression getOperand()
	{
		return operand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
	public String toCode()
	{
		return "new LTLAlways(" + operand.toCode() + ")";
	}

	/* (non-Javadoc)
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
  public LTLExpression applySubstitution(Term[] binding)
  {
	  return new LTLAlways(operand.applySubstitution(binding));
  }
}
