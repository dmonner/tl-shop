package JSHOP2;

public abstract class LTLExpression extends CompileTimeObject
{
  /** <code>true</code> iff this logical expression, or one below it, contains
   *  a temporal operator.
  */
  protected boolean hasTemporalOps;
  
  /** This function is used to determine if this logical expression contains
   *  any temporal operators.
   *
   *  @return <code>true</code> iff this logical expression, or one below it, 
   *           contains a temporal operator.
  */
  public boolean hasTemporalOperators() 
  {
    return hasTemporalOps;
  }
  
  /** Override toString() for pretty printing.
   */
  public String toString() {
  	return toCode();
  }
}
