package JSHOP2;

public class LTLAtom extends LTLExpression
{
  private Predicate atom;

  public LTLAtom(Predicate logicalAtomIn)
  {
  	atom = logicalAtomIn;
  }

  public String toCode()
  {
    return "new LTLAtom(" + atom.toCode() + ")";
  }
}
