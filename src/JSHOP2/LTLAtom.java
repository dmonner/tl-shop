package JSHOP2;

public class LTLAtom extends LTLExpression
{
  private Predicate atom;

  public LTLAtom(Predicate logicalAtomIn)
  {
  	atom = logicalAtomIn;
  }
  
  public Predicate getAtom()
  {
  	return atom;
  }

  public String toCode()
  {
    return "new LTLAtom(" + atom.toCode() + ")";
  }
}
