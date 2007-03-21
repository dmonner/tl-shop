package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * of the form "expr_1 and expr_2 and ... and expr_n".
 * 
 * @author Derek Monner
 * @author <a href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLConjunction extends LTLExpression
{
  /**
   * The individual expressions that are conjoined.
   */
  private LTLExpression[] conjuncts;

	/**
	 * Create a conjunction out of two or more <code>LTLExpression</code> objects.
	 * 
	 * @param conjunctsIn
	 *          the logical expressions to be conjoined.
	 */
  public LTLConjunction(LTLExpression[] conjunctsIn)
  {
  	// must have at least two, otherwise we don't need a conjunction
  	if(conjunctsIn.length < 2)
  		throw new IllegalArgumentException("Must have at least two conjuncts.");
  	
    conjuncts = conjunctsIn;
    hasTemporalOps = false;
    
    // this conjunction has temporal operators if any of its children do
    for(int i = 0; i < conjuncts.length; i++)
    {
    	if(conjuncts[i].hasTemporalOperators()) {
    		hasTemporalOps = true;
    		break;
    	}
    }
  }
  
  /**
   * @return the expressions conjoined by this operator.
   */
  public LTLExpression[] getConjuncts()
  {
  	return conjuncts;
  }

	/* (non-Javadoc)
	 * @see JSHOP2.CompileTimeObject#toCode()
	 */
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
