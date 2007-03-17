package JSHOP2;

public class LTLNegation extends LTLExpression
{
  private LTLExpression operand;

  public LTLNegation(LTLExpression op)
  {
    operand = op;
    hasTemporalOps = operand.hasTemporalOperators();
  }
  
  public LTLExpression getOperand() {
	  return operand;
  }

  public String toCode()
  {
    return "new LTLNegation(" + operand.toCode() + ")";
  }
}
