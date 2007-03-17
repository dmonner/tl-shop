package JSHOP2;

public class LTLDisjunction extends LTLExpression
{
  private LTLExpression firstOperand;
  private LTLExpression secondOperand;

  public LTLDisjunction(LTLExpression first, LTLExpression second)
  {
    firstOperand = first;
    secondOperand = second;
    hasTemporalOps = first.hasTemporalOperators() || second.hasTemporalOperators();
  }
  
  public LTLExpression getFirstOperand()
  {
  	return firstOperand;
  }

	public LTLExpression getSecondOperand()
  {
  	return secondOperand;
  }

	public String toCode()
  {
    return "new LTLDisjunction(" + firstOperand.toCode() + ", " + secondOperand.toCode() +")";
  }
}
