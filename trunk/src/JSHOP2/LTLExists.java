package JSHOP2;

public class LTLExists extends LTLExpression
{
  private LTLExpression premise;
  private LTLExpression consequent;

  public LTLExists(LTLExpression premiseIn, LTLExpression consequentIn)
  {
    premise = premiseIn;
    consequent = consequentIn;
    
    // The premise of a ForAll is not allowed to have temporal operators
    if(premise.hasTemporalOperators())
    	throw new IllegalArgumentException("Premise is not allowed to have temporal operators.");
    
    hasTemporalOps = consequent.hasTemporalOperators();
  }
  
  public LTLExpression getPremise()
  {
  	return premise;
  }
  
  public LTLExpression getConsequence()
  {
  	return consequent;
  }

  public String toCode()
  {
    return "new LTLExists(" + premise.toCode() + ", " + consequent.toCode() + ")";
  }
}
