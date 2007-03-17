package JSHOP2;

public class LTLConjunction extends LTLExpression
{
  private LTLExpression firstOperand;
  private LTLExpression secondOperand;

  public LTLConjunction(LTLExpression first, LTLExpression second)
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
    return "new LTLConjunction(" + firstOperand.toCode() + ", " + secondOperand.toCode() +")";
  }
}
