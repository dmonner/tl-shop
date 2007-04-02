package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "next expr".
 * 
 * @author Derek Monner
 * @author <a href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
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
	 * @param operandIn
	 *          the logical expression that must hold in the next state.
	 */
	public LTLNext(LTLExpression operandIn)
	{
		operand = operandIn;
		hasTemporalOps = true;
	}
	
	/**
	 * @return the logical expression that must hold in the next state.
	 */
	public LTLExpression getOperand()
	{
		return operand;
	}

	/* (non-Javadoc)
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
	public String toCode()
	{
		return "new LTLNext(" + operand.toCode() + ")";
	}
	
	/* (non-Javadoc)
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
  public LTLExpression applySubstitution(Term[] binding)
  {
	  return new LTLNext(operand.applySubstitution(binding));
  }
}
