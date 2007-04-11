package JSHOP2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * This class is used to represent the current state of the world.
 * 
 * @author Okhtay Ilghami
 * @author <a href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.3
 */
public class State
{
	/**
	 * The atoms in the current state of the world as an array of
	 * <code>Vector</code>s. The array is indexed by the possible heads (i.e.,
	 * the constant symbol that comes first) of the possible predicates.
	 */
	private Vector[] atoms;

	/**
	 * The axioms in the domain description as a two-dimensional array. The array
	 * is indexed first by the head of the predicates each axiom can prove and
	 * second by the axioms themselves.
	 */
	private Axiom[][] axioms;

	/**
	 * The protections in the current state of the world as an array of
	 * <code>Vector</code>s. The array is indexed by the heads of protected
	 * predicates.
	 */
	private Vector[] protections;

	/**
	 * The control rules that apply to the current state.
	 */
	private LTLExpression controlRules;

	/**
	 * To initialize the state of the world.
	 * 
	 * @param size
	 *          the number of possible heads of predicates (i.e., the number of
	 *          constant symbols that can come first in a predicate).
	 * @param axiomsIn
	 *          the axioms in the domain description as a two-dimensional array.
	 *          The array is indexed first by the head of the predicates each
	 *          axiom can prove and second by the axioms themselves.
	 */
	public State(int size, Axiom[][] axiomsIn)
	{
		// -- Initialize the arrays that represent the atoms and protections in the
		// -- current state of the world.
		atoms = new Vector[size];

		protections = new Vector[size];

		for(int i = 0; i < size; i++)
		{
			atoms[i] = new Vector();
			protections[i] = new Vector();
		}

		axioms = axiomsIn;

		// intialize the control rules to "true" - we add actual rules by conjoining
		// this with parameters to addControlRules(...)
		controlRules = LTLTrue.getInstance();
	}

	/**
	 * To add a predicate to the current state of the world.
	 * 
	 * @param p
	 *          the predicate to be added.
	 * @return <code>true</code> if the predicate was added (i.e., it was not
	 *         already in the current state of the world), <code>false</code>
	 *         otherwise.
	 */
	public boolean add(Predicate p)
	{
		Term t;

		// -- Find the right Vector to add this predicate to.
		Iterator e = atoms[p.getHead()].iterator();

		// -- First look for the predicate in the Vector. If it is already there,
		// -- do nothing and return false.
		while(e.hasNext())
		{
			t = (Term) e.next();

			if(p.equals(t))
				return false;
		}

		// -- Add the predicate and return true.
		atoms[p.getHead()].add(p.getParam());

		return true;
	}
	
	/**
	 * @param rule the new control rule to conjoin to this state's rules.
	 */
	public void addControlRule(LTLExpression rule)
	{
		controlRules = LTLExpression.conjoin(controlRules, rule);
	}

	/**
	 * To protect a given predicate in the current state of the world.
	 * 
	 * @param p
	 *          the predicate to be protected.
	 * @return this function always returns <code>true</code>.
	 */
	public boolean addProtection(Predicate p)
	{
		NumberedPredicate np;

		// -- First, find the appropriate Vector to add the protection to.
		Iterator e = protections[p.getHead()].iterator();

		// -- If the predicate is already protected, just increase the protection
		// -- counter.
		while(e.hasNext())
		{
			np = (NumberedPredicate) e.next();

			if(p.equals(np.getParam()))
			{
				np.inc();
				return true;
			}
		}

		// -- If this is the first time this predicate is being protected, add it
		// -- to the Vector.
		protections[p.getHead()].add(new NumberedPredicate(p));
		return true;
	}

	/**
	 * To empty the world state.
	 */
	public void clear()
	{
		for(int i = 0; i < atoms.length; i++)
		{
			atoms[i].clear();
			protections[i].clear();
		}
	}
	
	/**
	 * Remove all control rules; for testing purposes.
	 */
	public void clearControlRules()
	{
		controlRules = LTLTrue.getInstance();
	}

	/**
	 * To delete a predicate from the current state of the world.
	 * 
	 * @param p
	 *          the predicate to be deleted.
	 * @return the index of the predicate that was deleted in the
	 *         <code>Vector</code> if the predicate was deleted (i.e., it
	 *         existed in the current state of the world), -1 otherwise. This
	 *         index is used in case of a backtrack to undo this deletion by
	 *         inserting the deleted predicate right back where it used to be.
	 */
	public int del(Predicate p)
	{
		Term t;

		// -- Find the right Vector to delete this predicate from.
		Vector vec = atoms[p.getHead()];

		// -- If predicate is found, delete it and return its index.
		for(int i = 0; i < vec.size(); i++)
		{
			t = (Term) vec.get(i);

			if(p.equals(t))
			{
				vec.remove(i);
				return i;
			}
		}

		// -- There was nothing to delete, so return -1.
		return -1;
	}

	/**
	 * To unprotect a given predicate.
	 * 
	 * @param p
	 *          the predicate to be unprotected.
	 * @return <code>true</code> if the protected is unprotected successfully,
	 *         <code>false</code> otherwise (i.e., when the predicate was not
	 *         protected before).
	 */
	public boolean delProtection(Predicate p)
	{
		NumberedPredicate np;

		// -- First, find the appropriate Vector to delete the protection from.
		Iterator e = protections[p.getHead()].iterator();

		// -- Look for the protection.
		while(e.hasNext())
		{
			np = (NumberedPredicate) e.next();

			// -- If it is found,
			if(p.equals(np.getParam()))
			{
				// -- Decrease the protection counter for this predicate.
				if(!np.dec())
					// -- If the counter drops to zero, remove the protection completely.
					e.remove();

				return true;
			}
		}

		// -- Nothing was there to delete, so return false.
		return false;
	}

	public LTLExpression getControlRules()
	{
		return controlRules;
	}
	
	/**
	 * To check if a predicate is protected.
	 * 
	 * @param p
	 *          the predicate to be checked.
	 * @return <code>true</code> if the predicate is protected,
	 *         <code>false</code> otherwise.
	 */
	public boolean isProtected(Predicate p)
	{
		NumberedPredicate np;

		// -- First, find the appropriate Vector to look for the protection.
		Iterator e = protections[p.getHead()].iterator();

		// -- Iterate over the Vector to find the protection.
		while(e.hasNext())
		{
			np = (NumberedPredicate) e.next();

			if(p.equals(np.getParam()))
				return true;
		}

		return false;
	}

	/**
	 * To initialize and return the appropriate iterator when looking for ways to
	 * satisfy a given predicate.
	 * 
	 * @param head
	 *          the index of the constant symbol that is the head of the predicate
	 *          (i.e., that comes first in the predicate).
	 * @return the iterator to be used to find the satisfiers for this predicate.
	 */
	public MyIterator iterator(int head)
	{
		return new MyIterator(atoms[head]);
	}

	/**
	 * This function returns the bindings that can satisfy a given precondition
	 * one-by-one.
	 * 
	 * @param p
	 *          the predicate to be satisfied.
	 * @param me
	 *          the iterator that keeps track of where we are with the satisfiers
	 *          so that the next time this function is called, we can take off
	 *          where we stopped last time.
	 * @return the next binding as an array of terms indexed by the indeices of
	 *         the variable symbols in the given predicate.
	 */
	public Term[] nextBinding(Predicate p, MyIterator me)
	{
		Term[] nextB;

		Term[] retVal;

		Term t;

		// -- If we are still looking into the atoms to prove the predicate (i.e.,
		// -- we have not started looking into the axioms),
		if(me.whichAxiom == -1)
		{
			// -- Iterate over the appropriate Vector to find atoms that can satisfy
			// -- the given predicate.
			while(me.index < me.vec.size())
			{
				t = (Term) me.vec.get(me.index++);
				retVal = p.findUnifier(t);

				// -- If this atom can satisfy the given predicate, return the binding
				// -- that unifies the two.
				if(retVal != null)
					return retVal;
			}

			// -- We have already looked at all the atoms that could possibly satisfy
			// -- the predicate. From now on, we will look at the axioms only.
			me.whichAxiom = 0;
		}

		while(true)
		{
			// -- If we need to look at a new axiom,
			while(me.ax == null)
			{
				// -- If there are no more axioms to be looked at, return null.
				if(me.whichAxiom == axioms[p.getHead()].length)
					return null;

				// -- Try the next axiom whose head matches the head of the given
				// -- predicate.
				me.ax = axioms[p.getHead()][me.whichAxiom++];

				// -- Try to unify the axiom's head with the predicate.
				me.binding = me.ax.unify(p);

				// -- If the two can not be unified,
				if(me.binding == null)
					// -- Try to look for the next axiom.
					me.ax = null;
				else
				{
					// -- Start with the first branch of this axiom.
					me.index = 0;
					// -- No branch has been satisfied yet, so set this variable to false.
					me.found = false;
				}
			}

			// -- Iterate on all the branches of this axiom.
			for(; me.index < me.ax.getBranchSize(); me.index++)
			{
				// -- If this is the first time this branch is considered, get the
				// -- iterator for the precondition of this branch.
				if(me.pre == null)
					me.pre = me.ax.getIterator(me.binding, me.index);

				// -- Try the next satisfier for the precondition of this branch of this
				// -- axiom. If there is a next satisfier,
				while((nextB = me.pre.nextBinding()) != null)
				{
					// -- Merge the two bindings.
					Term.merge(nextB, me.binding);

					// -- Calculate the instance of the axiom we are using.
					Predicate groundAxiomHead = me.ax.getHead().applySubstitution(nextB);

					// -- Try to unify the axiom and the predicate.
					retVal = p.findUnifier(groundAxiomHead.getParam());

					// -- If there is such unifier, return it.
					if(retVal != null)
					{
						// -- The further branches of this axiom must NOT be considered even
						// -- if this branch fails because there has been at least one
						// -- satisfier for this branch of the axiom. Set this variable to
						// -- true to prevent the further branches of this axiom from being
						// -- considered.
						me.found = true;

						return retVal;
					}
				}

				// -- Try the next branch of this axiom.
				me.pre = null;

				// -- According to the semantics of the axiom branches in JSHOP2, second
				// -- branch is considered only when there is no binding for the first
				// -- branch, the third branch is considered only when there is no
				// -- binding for the first and second branches, etc. Therefore, if one
				// -- of the branches of this axiom has already returned a satisfier,
				// -- the other branches should be ignored.
				if(me.found)
					break;
			}

			// -- Try the next axiom.
			me.ax = null;
		}
	}

	/**
	 * This function is used to print the current state of the world.
	 */
	public void print()
	{
		for(int i = 0; i < atoms.length; i++)
		{
			Iterator e = atoms[i].iterator();

			while(e.hasNext())
			{
				Term t = (Term) e.next();
				(new Predicate(i, 0, t)).print();
			}

			System.out.println();
		}

		System.out.println("------");
	}

	/**
	 * This function is used, in case of a backtrack, to undo the changes that
	 * were made to the control rules because of the backtracked decision.
	 * 
	 * @param oldRules the control rules from before the backtracked decision.
	 */
	public void undo(LTLExpression oldRules)
	{
		controlRules = oldRules;
	}
	
	/**
	 * This function is used, in case of a backtrack, to undo the changes that
	 * were made to the current state of the world because of the backtracked
	 * decision.
	 * 
	 * @param delAdd
	 *          a 4-member array of type <code>Vector</code>. These four
	 *          members are the deleted atoms, the added atoms, the deleted
	 *          protections and the added protections respectively.
	 */
	public void undo(Vector[] delAdd)
	{
		Iterator e;

		NumberedPredicate np;

		// -- Since when an operator is applied, first the predicates in its delete
		// -- list are deleted and then the predicates in its add list are added,
		// -- when that application is undone, first the added predicates should be
		// -- deleted and then the deleted predicates should be added.

		// -- Deleting the added predicates.
		e = delAdd[1].iterator();
		while(e.hasNext())
			del((Predicate) e.next());

		// -- Adding the deleted predicates, exactly where they were deleted from.
		for(int i = delAdd[0].size() - 1; i >= 0; i--)
		{
			np = (NumberedPredicate) delAdd[0].get(i);
			atoms[np.getHead()].add(np.getNumber(), np.getParam());
		}

		// -- Deleting the added protections.
		e = delAdd[3].iterator();
		while(e.hasNext())
			delProtection((Predicate) e.next());

		// -- Adding the deleted protections.
		e = delAdd[2].iterator();
		while(e.hasNext())
			addProtection((Predicate) e.next());
	}

	public boolean isValidFinalState()
	{
		return isValidFinalState(controlRules);
	}
	
	/**
   * This method is to be called when the planning process is finished. It 
   * checks that all the implied conditions in the control rules are satisfied
   * by the final state. In particular, this will filter out plans where an
   * <code>LTLEventually</code> condition never comes to pass.
   * 
   * @return <code>true</code> if the control rules are satisfied by the final
   * state; <code>false</code> otherwise.
   */
  private boolean isValidFinalState(LTLExpression f)
  {
  	if(!f.hasTemporalOperators())
  	{
  		return entails(f);
  	}
  	else if(f instanceof LTLConjunction)
  	{
  		LTLExpression[] conj = ((LTLConjunction) f).getConjuncts();
  		
  		for(int i = 0; i < conj.length; i++)
  			if(!isValidFinalState(conj[i]))
  				return false;
  		
  		return true;
  	}
  	else if(f instanceof LTLDisjunction)
  	{
  		LTLExpression[] disj = ((LTLDisjunction) f).getDisjuncts();
  		
  		for(int i = 0; i < disj.length; i++)
  			if(isValidFinalState(disj[i]))
  				return true;
  		
  		return false;
  	}
  	else if(f instanceof LTLNegation)
  	{
  		return !isValidFinalState(((LTLNegation) f).getOperand());
  	}
  	else if(f instanceof LTLForAll)
  	{
  		LTLAtom premise = ((LTLForAll) f).getPremise();
  		LTLExpression consequent = ((LTLForAll) f).getConsequent();
  
  		Predicate atom = premise.getAtom();
  
  		// get the state's iterator, a helper for s.nextBinding, below
  		MyIterator me = iterator(atom.getHead());
  
  		// to hold the new bindings we encounter
  		Term[] newbinding;
  
  		// for each binding that satisfies the premise
  		while((newbinding = nextBinding(atom, me)) != null)
  		{
  			// apply the binding to the consequent
  			LTLExpression newConsequent = consequent.applySubstitution(newbinding);
  
  			// if the consequent is not entailed with the new binding
  			if(!isValidFinalState(newConsequent))
  				// our forall is not satisfied
  				return false;
  		}
  
  		// the state entailed the consequent for all valid premise values,
  		// so our forall is satisfied
  		return true;			
  	}
  	else if(f instanceof LTLExists)
  	{
  		LTLAtom premise = ((LTLExists) f).getPremise();
  		LTLExpression consequent = ((LTLExists) f).getConsequent();
  
  		Predicate atom = premise.getAtom();
  
  		// get the state's iterator, a helper for s.nextBinding, below
  		MyIterator me = iterator(atom.getHead());
  
  		// to hold the new bindings we encounter
  		Term[] newbinding;
  
  		// for each binding that satisfies the premise
  		while((newbinding = nextBinding(atom, me)) != null)
  		{
  			// apply the binding to the consequent
  			LTLExpression newConsequent = consequent.applySubstitution(newbinding);
  
  			// if the consequent is not entailed with the new binding
  			if(isValidFinalState(newConsequent))
  				// our exists is satisfied
  				return true;
  		}
  
  		// the state did not entail the consequent for any valid premise values,
  		// so our exists is not satisfied
  		return false;
  	}
  	else if(f instanceof LTLAlways)
  	{
  		// if an Always hasn't been made false yet, it was true all the way
  		// through; this fulfills the Always condition.
  		return true;
  	}
  	else if(f instanceof LTLEventually)
  	{
  		// if an Eventually hasn't been removed yet, that means the condition
  		// never became true; this does not fulfill the Eventually condition.
  		return false;
  	}
  	else if(f instanceof LTLNext)
  	{
  		// any constraints on the Next state are irrelevant, as we are at the
  		// final state.
  		return true;
  	}
  	else if(f instanceof LTLUntil)
  	{
  		// if an Until hasn't been removed yet, that means the first operand has
  		// held the entire time; this fulfills the Until condition.
  		return true;
  	}
  	else
  	{
  		// This should never happen
  		throw new IllegalArgumentException("Formula is malformed.");
  	}
  }

	/**
   * Determines the truth value of a logical formula (without temporal
   * operators) in the given state of the world.
   * 
   * @param f
   *          The formula to evaluate. <code>f</code> is not allowed to
   *          contain temporal operators.
   * @return <code>true</code> iff the current state entails this formula,
   *         <code>false</code> otherwise.
   */
  public boolean entails(LTLExpression f)
  {
  	if(f.hasTemporalOperators())
  	{
  		throw new IllegalArgumentException(
  		  "The input formula is not allowed to contain temporal operators.");
  	}
  
  	else if(f instanceof LTLTrue)
  	{
  		return true;
  	}
  
  	else if(f instanceof LTLFalse)
  	{
  		return false;
  	}
  
  	// if the top-level operator is a conjunction
  	else if(f instanceof LTLConjunction)
  	{
  		// get the list of conjuncts
  		LTLExpression[] fn = ((LTLConjunction) f).getConjuncts();
  
  		// for each conjunct
  		for(int i = 0; i < fn.length; i++)
  			// if it is not entailed by the current state
  			if(!entails(fn[i]))
  				// the whole conjunction is false
  				return false;
  
  		// all conjuncts evaluated to true
  		return true;
  	}
  
  	// if the top-level operator is a disjunction
  	else if(f instanceof LTLDisjunction)
  	{
  		// get the list of conjuncts
  		LTLExpression[] fn = ((LTLDisjunction) f).getDisjuncts();
  
  		// for each disjunct
  		for(int i = 0; i < fn.length; i++)
  			// if it is entailed by the current state
  			if(entails(fn[i]))
  				// the whole conjunction is true
  				return true;
  
  		// all disjuncts evaluated to false
  		return false;
  	}
  
  	// if the top-level operator is a negation
  	else if(f instanceof LTLNegation)
  	{
  		// get the operand formula of this negation
  		LTLExpression f1 = ((LTLNegation) f).getOperand();
  
  		// return the opposite of it
  		return !entails(f1);
  	}
  
  	// if the top-level operator is forall
  	else if(f instanceof LTLForAll)
  	{
  		LTLAtom premise = ((LTLForAll) f).getPremise();
  		LTLExpression consequent = ((LTLForAll) f).getConsequent();
  
  		Predicate atom = premise.getAtom();
  
  		// get the state's iterator, a helper for s.nextBinding, below
  		MyIterator me = iterator(atom.getHead());
  
  		// to hold the new bindings we encounter
  		Term[] newbinding;
  
  		// for each binding that satisfies the premise
  		while((newbinding = nextBinding(atom, me)) != null)
  		{
  			// apply the binding to the consequent
  			LTLExpression newConsequent = consequent.applySubstitution(newbinding);
  
  			// if the consequent is not entailed with the new binding
  			if(!entails(newConsequent))
  				// our forall is not satisfied
  				return false;
  		}
  
  		// the state entailed the consequent for all valid premise values,
  		// so our forall is satisfied
  		return true;
  	}
  
  	// if the top-level operator is exists
  	else if(f instanceof LTLExists)
  	{
  		LTLAtom premise = ((LTLExists) f).getPremise();
  		LTLExpression consequent = ((LTLExists) f).getConsequent();
  
  		Predicate atom = premise.getAtom();
  
  		// get the state's iterator, a helper for s.nextBinding, below
  		MyIterator me = iterator(atom.getHead());
  
  		// to hold the new bindings we encounter
  		Term[] newbinding;
  
  		// for each binding that satisfies the premise
  		while((newbinding = nextBinding(atom, me)) != null)
  		{
  			// apply the binding to the consequent
  			LTLExpression newConsequent = consequent.applySubstitution(newbinding);
  
  			// if the consequent is not entailed with the new binding
  			if(entails(newConsequent))
  				// our exists is satisfied
  				return true;
  		}
  
  		// the state did not entail the consequent for any valid premise values,
  		// so our exists is not satisfied
  		return false;
  	}
  
  	// if the top-level is an atom
  	else if(f instanceof LTLAtom)
  	{
  		// get the actual predicate
  		Predicate atom = ((LTLAtom) f).getAtom();
  
  		// ASSERT atom is ground
  		if(!atom.isGround())
  			throw new IllegalArgumentException("All atoms should be ground here.");
  
  		// get the state's iterator, a helper for s.nextBinding, below
  		MyIterator me = iterator(atom.getHead());
  
  		// return true iff there the state satisfies this predicate
  		return(nextBinding(atom, me) != null);
  	}
  
  	// this should never happen
  	else
  	{
  		throw new IllegalArgumentException(
  		  "Argument has no temporal operators but is not an instance of Conjunction, Disjunction, Negation, Forall, Exists, or Atom.");
  	}
  }

  public boolean progress()
  {
  	LTLExpression pf = progress(controlRules);
  	
  	if(pf instanceof LTLFalse)
  	{
  		return false;
  	}
  	else 
  	{
  		controlRules = pf;
  		return true;
  	}
  }
  
	/**
   * Determines whether a given LTL control rule is true, false, or undetermined
   * in the given state of the world.
   * 
   * @param s
   *          The current state of the world.
   * @param f
   *          The expression to progress.
   * @return an instance of <code>LTLTrue</code> iff <code>f</code>
   *         evaluates to true in <code>s</code>; an instance of
   *         <code>LTLFalse</code> iff <code>f</code> evaluates to false in
   *         <code>s</code>; or, the progressed formula that must be true in
   *         subsequent states in order for this formula to not be false in the
   *         current state.
   */
  private LTLExpression progress(LTLExpression f)
  {
  	// if there are temporal operators, we cannot evaluate the formula directly
  	if(f.hasTemporalOperators())
  	{
  
  		// if the top operator is a conjunction
  		// then this expression has the form:
  		// and { f1, f2, ..., fn }
  		// and the formula to return is:
  		// and { progress(s, f1), progress(s, f2), ..., progress(s, fn) }
  		if(f instanceof LTLConjunction)
  		{
  			// get the operands of this conjunction
  			LTLExpression[] fn = ((LTLConjunction) f).getConjuncts();
  
  			// create a new linked list to hold the progressed subexpressions
  			LinkedList<LTLExpression> pfn = new LinkedList<LTLExpression>();
  
  			// progress each of the subexpressions individually
  			for(int i = 0; i < fn.length; i++)
  			{
  				// progress the subexpression
  				LTLExpression p = progress(fn[i]);
  
  				// if the progression evaluates to FALSE
  				if(p instanceof LTLFalse)
  				{
  					// then our entire conjunction is FALSE.
  					return LTLFalse.getInstance();
  				}
  				// if the progression evaluates to TRUE, then it is irrelevant to our
  				// conjunction, and can be removed.
  				// otherwise, we must add it to the new conjunction.
  				else if(!(p instanceof LTLTrue))
  				{
  					pfn.add(p);
  				}
  			}
  
  			// if we have 0 conjuncts left at the end
  			if(pfn.size() == 0)
  			{
  				// this means all our conjuncts were TRUE, so return TRUE
  				return LTLTrue.getInstance();
  			}
  			// if we have exactly 1 conjunct left
  			else if(pfn.size() == 1)
  			{
  				// return it by itself
  				return pfn.get(0);
  			}
  			// otherwise
  			else
  			{
  				// recombine the conjuncts and return them
  				return new LTLConjunction(pfn.toArray(new LTLExpression[0]));
  			}
  		}
  
  		// if the top operator is a disjunction
  		// then this expression has the form:
  		// or { f1, f2, ..., fn }
  		// and the formula to return is:
  		// or { progress(s, f1), progress(s, f2), ..., progress(s, fn) }
  		else if(f instanceof LTLDisjunction)
  		{
  			// get the operands of this conjunction
  			LTLExpression[] fn = ((LTLDisjunction) f).getDisjuncts();
  
  			// create a new linked list to hold the progressed subexpressions
  			LinkedList<LTLExpression> pfn = new LinkedList<LTLExpression>();
  
  			// progress each of the subexpressions individually
  			for(int i = 0; i < fn.length; i++)
  			{
  				// progress the subexpression
  				LTLExpression p = progress(fn[i]);
  
  				// if the progression evaluates to TRUE
  				if(p instanceof LTLTrue)
  				{
  					// then our entire disjunction is TRUE.
  					return LTLTrue.getInstance();
  				}
  				// if the progression evaluates to FALSE, then it is irrelevant to our
  				// disjunction, and can be removed.
  				// otherwise, we must add it to the new disjunction.
  				else if(!(p instanceof LTLFalse))
  				{
  					pfn.add(p);
  				}
  			}
  
  			// if we have 0 disjuncts left at the end
  			if(pfn.size() == 0)
  			{
  				// this means all our disjuncts were FALSE, so return FALSE
  				return LTLFalse.getInstance();
  			}
  			// if we have exactly 1 disjunct left
  			else if(pfn.size() == 1)
  			{
  				// return it by itself
  				return pfn.get(0);
  			}
  			// otherwise
  			else
  			{
  				// recombine the disjuncts and return them
  				return new LTLDisjunction(pfn.toArray(new LTLExpression[0]));
  			}
  		}
  
  		// if the operator is a negation
  		// then this expression has the form:
  		// not f1
  		// and the formula to return is:
  		// not progress(s, f1)
  		else if(f instanceof LTLNegation)
  		{
  			// get the operand of this negation
  			LTLExpression f1 = ((LTLNegation) f).getOperand();
  
  			LTLExpression pf1 = progress(f1);
  
  			// if progress(s, f1) evaluates to TRUE
  			if(pf1 instanceof LTLTrue)
  			{
  				// then the negation of progress(s, f1) is FALSE
  				return LTLFalse.getInstance();
  			}
  			// if progress(s, f1) evaluates to FALSE
  			else if(pf1 instanceof LTLFalse)
  			{
  				// then the negation of progress(s, f1) is TRUE
  				return LTLTrue.getInstance();
  			}
  			// otherwise we return the negated formula
  			else
  			{
  				return new LTLNegation(pf1);
  			}
  		}
  
  		// if the operator is "next"
  		// then this expression has the form:
  		// next f1
  		// and the formula to return is:
  		// f1
  		else if(f instanceof LTLNext)
  		{
  			// return the operand of this "next" operator
  			return ((LTLNext) f).getOperand();
  		}
  
  		// if the operator is "until"
  		// then this expression has the form:
  		// f1 until f2
  		// and the formula to return is:
  		// progress(s, f2) | (progress(s, f1) & f)
  		else if(f instanceof LTLUntil)
  		{
  			// get the operands of this "until" operator
  			LTLExpression f1 = ((LTLUntil) f).getFirstOperand();
  			LTLExpression f2 = ((LTLUntil) f).getSecondOperand();
  
  			LTLExpression pf2 = progress(f2);
  
  			// if progress(s, f2) is TRUE
  			if(pf2 instanceof LTLTrue)
  			{
  				// then our formula reduces to TRUE
  				return LTLTrue.getInstance();
  			}
  			// if progress(s, f2) is FALSE
  			else if(pf2 instanceof LTLFalse)
  			{
  				// then our formula simplifies to progress(s, f1) & f
  				LTLExpression pf1 = progress(f1);
  
  				// if progress(s, f1) is TRUE
  				if(pf1 instanceof LTLTrue)
  				{
  					// then our formula simplifies to f
  					return f;
  				}
  				// if progress(s, f1) if FALSE
  				else if(pf1 instanceof LTLFalse)
  				{
  					// then our formula simplifies to FALSE
  					return LTLFalse.getInstance();
  				}
  				// otherwise our formula is progress(s, f1) & f
  				else
  				{
  					return new LTLConjunction(new LTLExpression[]
  					{
  					  pf1, f
  					});
  				}
  			}
  			// otherwise we may need to evaluate the whole formula
  			else
  			{
  				LTLExpression pf1 = progress(f1);
  
  				// if progress(s, f1) is TRUE
  				if(pf1 instanceof LTLTrue)
  				{
  					// then our formula simplifies to progress(s, f2) | f
  					return new LTLDisjunction(new LTLExpression[]
  					{
  					  pf2, f
  					});
  				}
  				// if progress(s, f1) if FALSE
  				else if(pf1 instanceof LTLFalse)
  				{
  					// then our formula simplifies to progress(s, f2)
  					return pf2;
  				}
  				// otherwise we must evaluate the full formula
  				else
  				{
  					// construct progress(s, f2) | (progress(s, f1) & f)
  					return new LTLDisjunction(new LTLExpression[]
  					{
  					  pf2, new LTLConjunction(new LTLExpression[]
  					  {
  					    pf1, f
  					  })
  					});
  				}
  			}
  		}
  
  		// if the operator is "eventually"
  		// then this expression has the form:
  		// eventually f1
  		// and the formula to return is:
  		// progress(s, f1) | f
  		else if(f instanceof LTLEventually)
  		{
  			// get the operands of this "eventually" operator
  			LTLExpression f1 = ((LTLEventually) f).getOperand();
  
  			LTLExpression pf1 = progress(f1);
  
  			// if progress(s, f1) evaluates to TRUE,
  			if(pf1 instanceof LTLTrue)
  			{
  				// then our formula simplifies to TRUE.
  				return LTLTrue.getInstance();
  			}
  			// if progress(s, f1) evaluates to FALSE,
  			else if(pf1 instanceof LTLFalse)
  			{
  				// then our formula simplifies to f
  				return f;
  			}
  			// otherwise our formula does not simplify at all
  			else
  			{
  				// construct the disjunction: progress(s, f1) | f
  				return new LTLDisjunction(new LTLExpression[]
  				{
  				  pf1, f
  				});
  			}
  		}
  
  		// if the operator is "always"
  		// then this expression has the form:
  		// always f1
  		// and the formula to return is:
  		// progress(s, f1) & f
  		else if(f instanceof LTLAlways)
  		{
  			// get the operands of this "eventually" operator
  			LTLExpression f1 = ((LTLAlways) f).getOperand();
  
  			LTLExpression pf1 = progress(f1);
  
  			// if progress(s, f1) evaluates to TRUE,
  			if(pf1 instanceof LTLTrue)
  			{
  				// then our formula simplifies to f.
  				return f;
  			}
  			// if progress(s, f1) evaluates to FALSE,
  			else if(pf1 instanceof LTLFalse)
  			{
  				// then our formula simplifies to FALSE
  				return LTLFalse.getInstance();
  			}
  			// otherwise our formula does not simplify at all
  			else
  			{
  				return new LTLConjunction(new LTLExpression[]
  				{
  				  pf1, f
  				});
  			}
  		}
  
  		// if the operator is "forall"
  		// then this expression has the form:
  		// forall x (premise -> consequent)
  		// (where "premise" is an atom that is true for only finitely many x)
  		// and the formula to return is:
  		// and { progress(s, consequent, binding_1),
  		// progress(s, consequent, binding_2),
  		// ...,
  		// progress(s, consequent, binding_n) }
  		// for each valid value of binding
  		else if(f instanceof LTLForAll)
  		{
  			LTLAtom premise = ((LTLForAll) f).getPremise();
  			LTLExpression consequent = ((LTLForAll) f).getConsequent();
  
  			Predicate atom = premise.getAtom();
  
  			// get the state's iterator, a helper for s.nextBinding, below
  			MyIterator me = iterator(atom.getHead());
  
  			// to hold the new bindings we encounter
  			Term[] newbinding;
  
  			// to hold the new conjuncts
  			LinkedList<LTLExpression> conjuncts = new LinkedList<LTLExpression>();
  
  			// for each binding that satisfies the premise
  			while((newbinding = nextBinding(atom, me)) != null)
  			{
  				// apply the binding to the consequent
  				LTLExpression newConsequent = consequent
  				  .applySubstitution(newbinding);
  
  				// progress the expression with the new binding
  				LTLExpression p = progress(newConsequent);
  
  				// if the progression evaluates to FALSE
  				if(p instanceof LTLFalse)
  				{
  					// then our entire conjunction is FALSE.
  					return LTLFalse.getInstance();
  				}
  				// if the progression evaluates to TRUE, then it is irrelevant to our
  				// conjunction, and can be removed.
  				// otherwise, we must add it to the new conjunction.
  				else if(!(p instanceof LTLTrue))
  				{
  					conjuncts.add(p);
  				}
  			}
  
  			// if we have 0 conjuncts left at the end
  			if(conjuncts.size() == 0)
  			{
  				// this means all our conjuncts were TRUE, so return TRUE
  				return LTLTrue.getInstance();
  			}
  			// if we have exactly 1 conjunct left
  			else if(conjuncts.size() == 1)
  			{
  				// return it by itself
  				return conjuncts.get(0);
  			}
  			// otherwise
  			else
  			{
  				// recombine the conjuncts and return them
  				return new LTLConjunction(conjuncts.toArray(new LTLExpression[0]));
  			}
  		}
  
  		// if the operator is "exists"
  		// then this expression has the form:
  		// forall x (premise -> consequent)
  		// (where "premise" is an atom that is true for only finitely many x)
  		// and the formula to return is:
  		// or { progress(s, consequent, binding_1),
  		// progress(s, consequent, binding_2),
  		// ...,
  		// progress(s, consequent, binding_n) }
  		// for each valid value of binding
  		else if(f instanceof LTLExists)
  		{
  			LTLAtom premise = ((LTLExists) f).getPremise();
  			LTLExpression consequent = ((LTLExists) f).getConsequent();
  
  			Predicate atom = premise.getAtom();
  
  			// get the state's iterator, a helper for s.nextBinding, below
  			MyIterator me = iterator(atom.getHead());
  
  			// to hold the new bindings we encounter
  			Term[] newbinding;
  
  			// to hold the new conjuncts
  			LinkedList<LTLExpression> disjuncts = new LinkedList<LTLExpression>();
  
  			// for each binding that satisfies the premise
  			while((newbinding = nextBinding(atom, me)) != null)
  			{
  				// apply the binding to the consequent
  				LTLExpression newConsequent = consequent
  				  .applySubstitution(newbinding);
  
  				// progress the expression with the new binding
  				LTLExpression p = progress(newConsequent);
  
  				// if the progression evaluates to TRUE
  				if(p instanceof LTLTrue)
  				{
  					// then our entire disjunction is TRUE.
  					return LTLTrue.getInstance();
  				}
  				// if the progression evaluates to FALSE, then it is irrelevant to our
  				// disjunction, and can be removed.
  				// otherwise, we must add it to the new disjunction.
  				else if(!(p instanceof LTLFalse))
  				{
  					disjuncts.add(p);
  				}
  			}
  
  			// if we have 0 disjuncts left at the end
  			if(disjuncts.size() == 0)
  			{
  				// this means all our disjuncts were FALSE, so return FALSE
  				return LTLFalse.getInstance();
  			}
  			// if we have exactly 1 disjunct left
  			else if(disjuncts.size() == 1)
  			{
  				// return it by itself
  				return disjuncts.get(0);
  			}
  			// otherwise
  			else
  			{
  				// recombine the disjuncts and return them
  				return new LTLDisjunction(disjuncts.toArray(new LTLExpression[0]));
  			}
  		}
  
  		// this should never happen
  		else
  		{
  			throw new IllegalArgumentException(
  			  "Somehow hasTemporalOperators() returned true but the top operator was not in our list.");
  		}
  	}
  
  	// otherwise, this formula has no temporal operators
  	else
  	{
  		// if the current state entails this formula
  		if(entails(f))
  			// return TRUE
  			return LTLTrue.getInstance();
  		else
  			// otherwise return FALSE
  			return LTLFalse.getInstance();
  	}
  }
}
