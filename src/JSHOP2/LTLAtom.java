package JSHOP2;

/**
 * An instance of this class represents an expression in Linear Temporal Logic
 * that is atomic, meaning that it consists of a single predicate.
 * 
 * @author Derek Monner
 * @author <a
 *         href="http://www.cs.umd.edu/~dmonner">http://www.cs.umd.edu/~dmonner</a>
 * @version 1.0.3
 */
public class LTLAtom extends LTLExpression
{
  /**
   * The predicate this object represents.
   */
  private Predicate atom;

  /**
   * @param atomIn the predicate this object will represent.
   */
  public LTLAtom(Predicate atomIn)
  {
  	atom = atomIn;
  }
  
  /**
   * @return the predicate this object represents.
   */
  public Predicate getAtom()
  {
  	return atom;
  }

  /* (non-Javadoc)
   * @see JSHOP2.CompileTimeObject#toCode()
   */
  public String toCode()
  {
    return "new LTLAtom(" + atom.toCode() + ")";
  }

	/* (non-Javadoc)
	 * @see JSHOP2.LTLExpression#applySubstitution(JSHOP2.Term[])
	 */
  public LTLAtom applySubstitution(Term[] binding)
  {
	  return new LTLAtom(atom.applySubstitution(binding));
  }
}
