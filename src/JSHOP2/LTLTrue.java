package JSHOP2;

/**
 * A logical expression that represents the value "true". Only useful during
 * the ControlRules.progress and ControlRules.entails methods.
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

	public static LTLTrue getInstance() 
	{
		return INSTANCE;
	}

	public String toCode() 
	{
		return "LTLTrue.getInstance()";
	}

}
