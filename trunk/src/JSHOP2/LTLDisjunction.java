package JSHOP2;

public class LTLDisjunction extends LTLExpression
{
  private LTLExpression[] disjuncts;

  public LTLDisjunction(LTLExpression[] conjunctsIn)
  {
    disjuncts = conjunctsIn;
    hasTemporalOps = false;
    
    for(int i = 0; i < disjuncts.length; i++)
    {
    	if(disjuncts[i].hasTemporalOperators()) {
    		hasTemporalOps = true;
    		break;
    	}
    }
  }
  
  public LTLExpression[] getDisjuncts()
  {
  	return disjuncts;
  }

	public String toCode()
  {
    String s = "new LTLDisjunction(new LTLExpression[] {";
    
    for(int i = 0; i < disjuncts.length; i++) 
    {
    	s += disjuncts[i].toCode();
    	if(i < disjuncts.length - 1)
    		s += ", ";
    }
    
    s += "})";
    
    return s;
  }
}
