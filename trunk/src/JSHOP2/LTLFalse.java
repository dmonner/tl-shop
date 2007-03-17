package JSHOP2;

/**
 * A logical expression that represents the value "false". Only useful during
 * the ControlRules.progress and ControlRules.entails methods.
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

	public static LTLFalse getInstance() 
	{
		return INSTANCE;
	}

	public String toCode() 
	{
		return "LTLFalse.getInstance()";
	}

}
