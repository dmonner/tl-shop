package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "expr1 until expr2".
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLUntil extends LTLExpression
{
	/**
	 * The logical expression that must hold until <code>secondOperator</code>.
	 */
	private LTLExpression firstOperand;

	/**
	 * The logical expression that, when/if it becomes true, will allow
	 * <code>firstExpression</code> to become false.
	 */
	private LTLExpression secondOperand;

	/**
	 * To initialize this "until" logical expression.
	 * 
	 * @param firstIn
	 *          the logical expression that must hold until
	 *          <code>secondOperator</code>.
	 * @param secondIn
	 *          the logical expression that, when/if it becomes true, will allow
	 *          <code>firstExpression</code> to become false.
	 */
	public LTLUntil(LTLExpression first, LTLExpression second)
	{
		firstOperand = first;
		secondOperand = second;
		hasTemporalOps = true;
	}

	/**
	 * @return the first operand of this "until" operator
	 */
	public LTLExpression getFirstOperand()
	{
		return firstOperand;
	}

	/**
	 * @return the second operand of this "until" operator
	 */
	public LTLExpression getSecondOperand()
	{
		return secondOperand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
	public String toCode()
	{
		return "new LTLUntil(" + firstOperand.toCode() + ", "
		  + secondOperand.toCode() + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
	public LTLExpression applySubstitution(Term[] binding)
	{
		return new LTLUntil(firstOperand.applySubstitution(binding), secondOperand
		  .applySubstitution(binding));
	}
}
