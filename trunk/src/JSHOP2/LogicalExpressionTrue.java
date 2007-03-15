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
public class LogicalExpressionTrue extends LogicalExpression {

	private static LogicalExpressionTrue INSTANCE = new LogicalExpressionTrue();

	private LogicalExpressionTrue() {}

	public static LogicalExpressionTrue getInstance() 
	{
		return INSTANCE;
	}

	@Override
	public String getInitCode() 
	{
		return "";
	}

	@Override
	protected void propagateVarCount(int varCountIn) {}

	@Override
	public String toCode() 
	{
		// TODO fix toCode()
		return "";
	}

}
