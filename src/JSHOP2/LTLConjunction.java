package JSHOP2;

public class LTLConjunction extends LTLExpression
{
  private LTLExpression[] conjuncts;

  public LTLConjunction(LTLExpression[] conjunctsIn)
  {
    conjuncts = conjunctsIn;
    hasTemporalOps = false;
    
    for(int i = 0; i < conjuncts.length; i++)
    {
    	if(conjuncts[i].hasTemporalOperators()) {
    		hasTemporalOps = true;
    		break;
    	}
    }
  }
  
  public LTLExpression[] getConjuncts()
  {
  	return conjuncts;
  }

	public String toCode()
  {
    String s = "new LTLConjunction(new LTLExpression[] {";
    
    for(int i = 0; i < conjuncts.length; i++) 
    {
    	s += conjuncts[i].toCode();
    	if(i < conjuncts.length - 1)
    		s += ", ";
    }
    
    s += "})";
    
    return s;
  }
}
