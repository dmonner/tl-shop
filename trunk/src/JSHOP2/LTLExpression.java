package JSHOP2;

/**
 * The subclasses of this class can be combined for form any expression in
 * Linear Temporal Logic, to be used as control rules during planning.
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public abstract class LTLExpression extends CompileTimeObject
{
	/**
	 * <code>true</code> iff this logical expression, or one below it, contains
	 * a temporal operator.
	 */
	protected boolean hasTemporalOps;

	/**
	 * This function is used to determine if this logical expression contains any
	 * temporal operators.
	 * 
	 * @return <code>true</code> iff this logical expression, or one below it,
	 *         contains a temporal operator.
	 */
	public boolean hasTemporalOperators()
	{
		return hasTemporalOps;
	}

	/**
	 * Override toString() for pretty printing.
	 */
	public String toString()
	{
		return toCode();
	}

	/**
	 * This method applies the given variable substitution to all predicates in
	 * this expression.
	 * 
	 * @param binding the variable binding to apply.
	 */
	public abstract LTLExpression applySubstitution(Term[] binding);
}
