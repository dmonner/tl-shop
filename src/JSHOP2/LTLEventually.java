package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "eventually expr".
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
	public String toCode()
	{
		return "new LTLEventually(" + operand.toCode() + ")";
	}

	/* (non-Javadoc)
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
  public LTLExpression applySubstitution(Term[] binding)
  {
	  return new LTLEventually(operand.applySubstitution(binding));
  }
}
