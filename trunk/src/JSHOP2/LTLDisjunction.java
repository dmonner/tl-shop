package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "expr_1 or expr_2 or ... or expr_n".
 * 
 * @author Derek Monner
 * @author <a href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLDisjunction extends LTLExpression
{
  /**
   * The individual expressions that are disjoined.
   */
  private LTLExpression[] disjuncts;

	/**
	 * @param disjunctsIn
	 *          the logical expressions to be disjoined.
	 */
  public LTLDisjunction(LTLExpression[] disjunctsIn)
  {
    disjuncts = disjunctsIn;
    hasTemporalOps = false;
    
    // this disjunction has temporal operators if any of its children do
    for(int i = 0; i < disjuncts.length; i++)
    {
    	if(disjuncts[i].hasTemporalOperators()) {
    		hasTemporalOps = true;
    		break;
    	}
    }
  }
  
  /**
   * @return the expressions disjoined by this operator.
   */
  public LTLExpression[] getDisjuncts()
  {
  	return disjuncts;
  }

	/* (non-Javadoc)
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
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
