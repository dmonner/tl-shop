package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "not expr".
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLNegation extends LTLExpression
{
  /**
   * The expression that this operator negates.
   */
  private LTLExpression operand;

  /**
   * @param operandIn the expression for this operator to negate.
   */
  public LTLNegation(LTLExpression operandIn)
  {
    operand = operandIn;
    hasTemporalOps = operand.hasTemporalOperators();
  }
  
  /**
   * @return the expression that this operator negates.
   */
  public LTLExpression getOperand() {
	  return operand;
  }

  /* (non-Javadoc)
   * @see JSHOP2.CompileTimeObject#toCode()
   */
  public String toCode()
  {
    return "new LTLNegation(" + operand.toCode() + ")";
  }

	/* (non-Javadoc)
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
  public LTLExpression applySubstitution(Term[] binding)
  {
	  return new LTLNegation(operand.applySubstitution(binding));
  }
}
