package JSHOP2;

/**
 * An expression in Linear Temporal Logic that represents the value "true".
 * 
 * There is only ever one instance of this class, since all "true" values are
 * equivalent. It is accessed with the getInstance() method.
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLTrue extends LTLExpression {

	private static LTLTrue INSTANCE = new LTLTrue();

	private LTLTrue() {}

	/**
	 * @return the singleton instance of this class.
	 */
	public static LTLTrue getInstance() 
	{
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
	public String toCode() 
	{
		return "LTLTrue.getInstance()";
	}

}
