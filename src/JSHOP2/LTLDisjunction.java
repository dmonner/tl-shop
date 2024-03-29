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
	 * Create a disjunction out of two or more <code>LTLExpression</code> objects.
	 * 
	 * @param disjunctsIn
	 *          the logical expressions to be disjoined.
	 */
  public LTLDisjunction(LTLExpression[] disjunctsIn)
  {
  	// must have at least two, otherwise we don't need a disjunction
  	if(disjunctsIn.length < 2)
  		throw new IllegalArgumentException("Must have at least two disjuncts.");

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
	public LTLExpression applySubstitution(Term[] binding)
	{
		LTLExpression[] newdisj = new LTLExpression[disjuncts.length];
		
		for(int i = 0; i < disjuncts.length; i++)
			newdisj[i] = disjuncts[i].applySubstitution(binding);
		
		return new LTLConjunction(newdisj);
	}
}
