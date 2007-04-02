package JSHOP2;

/**
 * An expression in Linear Temporal Logic that represents the value "false".
 * 
 * There is only ever one instance of this class, since all "false" values are
 * equivalent. It is accessed with the getInstance() method.
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLFalse extends LTLExpression {

	private static LTLFalse INSTANCE = new LTLFalse();

	private LTLFalse() {}

	/**
	 * @return the singleton instance of this class.
	 */
	public static LTLFalse getInstance() 
	{
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
	public String toCode() 
	{
		return "LTLFalse.getInstance()";
	}


	/* (non-Javadoc)
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
  public LTLExpression applySubstitution(Term[] binding)
  {
	  return this;
  }
}
