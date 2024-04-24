



GitLab


















Projects
Groups
Snippets


















/












Help









Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab






Sign in




Toggle navigation

Menu








GitLab


















Projects
Groups
Snippets


















/












Help









Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab






Sign in




Toggle navigation

Menu






GitLab


















Projects
Groups
Snippets



GitLab






GitLab









Projects
Groups
Snippets






Projects
Groups
Snippets















/




















/














/










Help









Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab






Sign in




Help









Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab





Help





Help


Support


Community forum



Keyboard shortcuts
?




Submit feedback


Contribute to GitLab





Help

Support

Community forum


Keyboard shortcuts
?


Submit feedback

Contribute to GitLab


Sign in
Toggle navigation
Menu

Menu




H


heros






Project information




Project information




Activity


Labels


Members







Repository




Repository




Files


Commits


Branches


Tags


Contributors


Graph


Compare







Issues

0



Issues

0



List


Boards


Service Desk


Milestones







Merge requests

0



Merge requests

0






CI/CD




CI/CD




Pipelines


Jobs


Schedules







Deployments




Deployments




Environments


Releases







Monitor




Monitor




Incidents







Analytics




Analytics




Value stream


CI/CD


Repository







Wiki




Wiki





Activity


Graph


Create a new issue


Jobs


Commits


Issue Boards




Collapse sidebar


Close sidebar








Open sidebar



Joshua Garcia heros

9cd88f45a6eeb6efb61fbd70ef0243b236ac919a

















9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Switch branch/tag










heros


src


heros


alias


FlowFunction.java



Find file
Normal viewHistoryPermalink






FlowFunction.java



5.55 KB









Newer










Older









annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}













H


heros






Project information




Project information




Activity


Labels


Members







Repository




Repository




Files


Commits


Branches


Tags


Contributors


Graph


Compare







Issues

0



Issues

0



List


Boards


Service Desk


Milestones







Merge requests

0



Merge requests

0






CI/CD




CI/CD




Pipelines


Jobs


Schedules







Deployments




Deployments




Environments


Releases







Monitor




Monitor




Incidents







Analytics




Analytics




Value stream


CI/CD


Repository







Wiki




Wiki





Activity


Graph


Create a new issue


Jobs


Commits


Issue Boards




Collapse sidebar


Close sidebar


H


heros


H
H
heros




Project information




Project information




Activity


Labels


Members






Project information


Project information




Project information


Activity


Activity

Labels


Labels

Members


Members




Repository




Repository




Files


Commits


Branches


Tags


Contributors


Graph


Compare






Repository


Repository




Repository


Files


Files

Commits


Commits

Branches


Branches

Tags


Tags

Contributors


Contributors

Graph


Graph

Compare


Compare




Issues

0



Issues

0



List


Boards


Service Desk


Milestones






Issues
0


Issues

0



Issues

0
0

List


List

Boards


Boards

Service Desk


Service Desk

Milestones


Milestones




Merge requests

0



Merge requests

0





Merge requests
0


Merge requests

0



Merge requests

0
0




CI/CD




CI/CD




Pipelines


Jobs


Schedules






CI/CD


CI/CD




CI/CD


Pipelines


Pipelines

Jobs


Jobs

Schedules


Schedules




Deployments




Deployments




Environments


Releases






Deployments


Deployments




Deployments


Environments


Environments

Releases


Releases




Monitor




Monitor




Incidents






Monitor


Monitor




Monitor


Incidents


Incidents




Analytics




Analytics




Value stream


CI/CD


Repository






Analytics


Analytics




Analytics


Value stream


Value stream

CI/CD


CI/CD

Repository


Repository




Wiki




Wiki






Wiki


Wiki




Wiki


Activity

Graph

Create a new issue

Jobs

Commits

Issue Boards
Collapse sidebarClose sidebar




Open sidebar



Joshua Garcia heros

9cd88f45a6eeb6efb61fbd70ef0243b236ac919a

















9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Switch branch/tag










heros


src


heros


alias


FlowFunction.java



Find file
Normal viewHistoryPermalink






FlowFunction.java



5.55 KB









Newer










Older









annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}












Open sidebar



Joshua Garcia heros

9cd88f45a6eeb6efb61fbd70ef0243b236ac919a







Open sidebar



Joshua Garcia heros

9cd88f45a6eeb6efb61fbd70ef0243b236ac919a




Open sidebar

Joshua Garcia heros

9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Joshua Garciaherosheros
9cd88f45a6eeb6efb61fbd70ef0243b236ac919a










9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Switch branch/tag










heros


src


heros


alias


FlowFunction.java



Find file
Normal viewHistoryPermalink






FlowFunction.java



5.55 KB









Newer










Older









annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}















9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Switch branch/tag










heros


src


heros


alias


FlowFunction.java



Find file
Normal viewHistoryPermalink






FlowFunction.java



5.55 KB









Newer










Older









annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}











9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Switch branch/tag










heros


src


heros


alias


FlowFunction.java



Find file
Normal viewHistoryPermalink




9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Switch branch/tag










heros


src


heros


alias


FlowFunction.java





9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Switch branch/tag








9cd88f45a6eeb6efb61fbd70ef0243b236ac919a


Switch branch/tag





9cd88f45a6eeb6efb61fbd70ef0243b236ac919a

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

alias

FlowFunction.java
Find file
Normal viewHistoryPermalink




FlowFunction.java



5.55 KB









Newer










Older









annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}









FlowFunction.java



5.55 KB










FlowFunction.java



5.55 KB









Newer










Older
NewerOlder







annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}











annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}









annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}








Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}








annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}







annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


1


2


3


4


5


6


7


8


9


10


11


12


13


14


15


16


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A flow function computes which of the finitely many D-type values are reachable
 * from the current source values. Typically there will be one such function
 * associated with every possible control flow. 
 * 
 * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that
 * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is
 * because the duration of IDE's fixed point iteration may depend on the iteration order.
 * Within the solver, we have tried to fix this order as much as possible, but the
 * order, in general, does also depend on the order in which the result set
 * of {@link #computeTargets(Object)} is traversed.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */

/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import java.util.LinkedHashSet;importjava.util.LinkedHashSet;import java.util.Set;importjava.util.Set;/**/** * A flow function computes which of the finitely many D-type values are reachable * A flow function computes which of the finitely many D-type values are reachable * from the current source values. Typically there will be one such function * from the current source values. Typically there will be one such function * associated with every possible control flow.  * associated with every possible control flow.  *  *  * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that * <b>NOTE:</b> To be able to produce <b>deterministic benchmarking results</b>, we have found that * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is * it helps to return {@link LinkedHashSet}s from {@link #computeTargets(Object)}. This is * because the duration of IDE's fixed point iteration may depend on the iteration order. * because the duration of IDE's fixed point iteration may depend on the iteration order. * Within the solver, we have tried to fix this order as much as possible, but the * Within the solver, we have tried to fix this order as much as possible, but the * order, in general, does also depend on the order in which the result set * order, in general, does also depend on the order in which the result set * of {@link #computeTargets(Object)} is traversed. * of {@link #computeTargets(Object)} is traversed. *  *  * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads. * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads. * Hence, classes implementing this interface should synchronize accesses to * Hence, classes implementing this interface should synchronize accesses to * any mutable shared state. * any mutable shared state. *  *  * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem. */ */




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




34



public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


34


public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {

public interface FlowFunction<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {publicinterfaceFlowFunction<FieldRef,DextendsFieldSensitiveFact<?,FieldRef,D>>{




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




35


36


37


38




	/**
	 * Returns the target values reachable from the source.
	 */






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


35


36


37


38



	/**
	 * Returns the target values reachable from the source.
	 */

	/**/**	 * Returns the target values reachable from the source.	 * Returns the target values reachable from the source.	 */	 */




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




39



	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


39


	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);

	Set<ConstrainedFact<FieldRef, D>> computeTargets(D source);Set<ConstrainedFact<FieldRef,D>>computeTargets(Dsource);




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




40



	






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


40


	

	




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




41



	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


41


	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {

	public static class ConstrainedFact<FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> {publicstaticclassConstrainedFact<FieldRef,DextendsFieldSensitiveFact<?,FieldRef,D>>{




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




42


43



		
		private D fact;






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


42


43


		
		private D fact;

				private D fact;privateDfact;




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




44



		private Constraint<FieldRef> constraint;






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


44


		private Constraint<FieldRef> constraint;

		private Constraint<FieldRef> constraint;privateConstraint<FieldRef>constraint;




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




45



		






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


45


		

		




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




46


47


48


49


50


51



		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


46


47


48


49


50


51


		public ConstrainedFact(D fact) {
			this.fact = fact;
			this.constraint = null;
		}
		
		public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {

		public ConstrainedFact(D fact) {publicConstrainedFact(Dfact){			this.fact = fact;this.fact=fact;			this.constraint = null;this.constraint=null;		}}				public ConstrainedFact(D fact, Constraint<FieldRef> constraint) {publicConstrainedFact(Dfact,Constraint<FieldRef>constraint){




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




52



			this.fact = fact;






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


52


			this.fact = fact;

			this.fact = fact;this.fact=fact;




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




53



			this.constraint = constraint;






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


53


			this.constraint = constraint;

			this.constraint = constraint;this.constraint=constraint;




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




54


55


56


57


58


59



		}
		
		public D getFact() {
			return fact;
		}
		






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


54


55


56


57


58


59


		}
		
		public D getFact() {
			return fact;
		}
		

		}}				public D getFact() {publicDgetFact(){			return fact;returnfact;		}}		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




60


61



		public Constraint<FieldRef> getConstraint() {
			return constraint;






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


60


61


		public Constraint<FieldRef> getConstraint() {
			return constraint;

		public Constraint<FieldRef> getConstraint() {publicConstraint<FieldRef>getConstraint(){			return constraint;returnconstraint;




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




62


63


64


65


66


67



		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


62


63


64


65


66


67


		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;

		}}		@Override@Override		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = 1;intresult=1;




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




68



			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


68


			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());

			result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());result=prime*result+((constraint==null)?0:constraint.hashCode());




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




69


70


71


72


73


74


75


76


77


78



			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


69


70


71


72


73


74


75


76


77


78


			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;

			result = prime * result + ((fact == null) ? 0 : fact.hashCode());result=prime*result+((fact==null)?0:fact.hashCode());			return result;returnresult;		}}		@Override@Override		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (obj == null)if(obj==null)				return false;returnfalse;




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




79



			if (!(obj instanceof ConstrainedFact))






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


79


			if (!(obj instanceof ConstrainedFact))

			if (!(obj instanceof ConstrainedFact))if(!(objinstanceofConstrainedFact))




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




80



				return false;






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


80


				return false;

				return false;returnfalse;




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




81



			ConstrainedFact other = (ConstrainedFact) obj;






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


81


			ConstrainedFact other = (ConstrainedFact) obj;

			ConstrainedFact other = (ConstrainedFact) obj;ConstrainedFactother=(ConstrainedFact)obj;




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




82


83


84


85


86



			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


82


83


84


85


86


			if (constraint == null) {
				if (other.constraint != null)
					return false;
			} else if (!constraint.equals(other.constraint))
				return false;

			if (constraint == null) {if(constraint==null){				if (other.constraint != null)if(other.constraint!=null)					return false;returnfalse;			} else if (!constraint.equals(other.constraint))}elseif(!constraint.equals(other.constraint))				return false;returnfalse;




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




87


88


89


90


91


92


93



			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}






annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


87


88


89


90


91


92


93


			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			return true;
		}

			if (fact == null) {if(fact==null){				if (other.fact != null)if(other.fact!=null)					return false;returnfalse;			} else if (!fact.equals(other.fact))}elseif(!fact.equals(other.fact))				return false;returnfalse;			return true;returntrue;		}}




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




94


95


96



		
		@Override
		public String toString() {






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


94


95


96


		
		@Override
		public String toString() {

				@Override@Override		public String toString() {publicStringtoString(){




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




97


98


99


100


101


102



			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


97


98


99


100


101


102


			return fact.toString()+"<"+constraint+">";
		}
	}
	
	public interface Constraint<FieldRef> {
		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);

			return fact.toString()+"<"+constraint+">";returnfact.toString()+"<"+constraint+">";		}}	}}		public interface Constraint<FieldRef> {publicinterfaceConstraint<FieldRef>{		AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath);AccessPath<FieldRef>applyToAccessPath(AccessPath<FieldRef>accPath);




Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




103


104



		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);






Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015



Bug fix: check for possibility before applying DeltaConstraint

 

Bug fix: check for possibility before applying DeltaConstraint

Johannes Lerch
committed
Jan 30, 2015


103


104


		
		boolean canBeAppliedTo(AccessPath<FieldRef> accPath);

				boolean canBeAppliedTo(AccessPath<FieldRef> accPath);booleancanBeAppliedTo(AccessPath<FieldRef>accPath);




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




105


106


107


108


109


110


111


112


113


114


115



	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


105


106


107


108


109


110


111


112


113


114


115


	}
	
	public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
		private FieldRef fieldRef;

		public WriteFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}

		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {

	}}		public class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {publicclassWriteFieldConstraint<FieldRef>implementsConstraint<FieldRef>{		private FieldRef fieldRef;privateFieldReffieldRef;		public WriteFieldConstraint(FieldRef fieldRef) {publicWriteFieldConstraint(FieldReffieldRef){			this.fieldRef = fieldRef;this.fieldRef=fieldRef;		}}		@Override@Override		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {publicAccessPath<FieldRef>applyToAccessPath(AccessPath<FieldRef>accPath){




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




116



			return accPath.mergeExcludedFieldReference(fieldRef);






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


116


			return accPath.mergeExcludedFieldReference(fieldRef);

			return accPath.mergeExcludedFieldReference(fieldRef);returnaccPath.mergeExcludedFieldReference(fieldRef);




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




117


118


119


120


121


122



		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


117


118


119


120


121


122


		}
		
		@Override
		public String toString() {
			return "^"+fieldRef.toString();
		}

		}}				@Override@Override		public String toString() {publicStringtoString(){			return "^"+fieldRef.toString();return"^"+fieldRef.toString();		}}




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WriteFieldConstraint))
				return false;
			WriteFieldConstraint other = (WriteFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}

		@Override@Override		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = 1;intresult=1;			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());result=prime*result+((fieldRef==null)?0:fieldRef.hashCode());			return result;returnresult;		}}		@Override@Override		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (obj == null)if(obj==null)				return false;returnfalse;			if (!(obj instanceof WriteFieldConstraint))if(!(objinstanceofWriteFieldConstraint))				return false;returnfalse;			WriteFieldConstraint other = (WriteFieldConstraint) obj;WriteFieldConstraintother=(WriteFieldConstraint)obj;			if (fieldRef == null) {if(fieldRef==null){				if (other.fieldRef != null)if(other.fieldRef!=null)					return false;returnfalse;			} else if (!fieldRef.equals(other.fieldRef))}elseif(!fieldRef.equals(other.fieldRef))				return false;returnfalse;			return true;returntrue;		}}




Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




148


149


150


151


152




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}






Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015



Bug fix: check for possibility before applying DeltaConstraint

 

Bug fix: check for possibility before applying DeltaConstraint

Johannes Lerch
committed
Jan 30, 2015


148


149


150


151


152



		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return true;
		}

		@Override@Override		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {publicbooleancanBeAppliedTo(AccessPath<FieldRef>accPath){			return true;returntrue;		}}




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170



	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


	}
	
	public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {

		private FieldRef fieldRef;

		public ReadFieldConstraint(FieldRef fieldRef) {
			this.fieldRef = fieldRef;
		}
		
		@Override
		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
			return accPath.addFieldReference(fieldRef);
		}
		
		@Override
		public String toString() {
			return fieldRef.toString();

	}}		public class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {publicclassReadFieldConstraint<FieldRef>implementsConstraint<FieldRef>{		private FieldRef fieldRef;privateFieldReffieldRef;		public ReadFieldConstraint(FieldRef fieldRef) {publicReadFieldConstraint(FieldReffieldRef){			this.fieldRef = fieldRef;this.fieldRef=fieldRef;		}}				@Override@Override		public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {publicAccessPath<FieldRef>applyToAccessPath(AccessPath<FieldRef>accPath){			return accPath.addFieldReference(fieldRef);returnaccPath.addFieldReference(fieldRef);		}}				@Override@Override		public String toString() {publicStringtoString(){			return fieldRef.toString();returnfieldRef.toString();




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




171



		}






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


171


		}

		}}




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


172


173


174


175


176


177


178


179


180


181


182


183


184


185


186


187


188


189


190


191


192


193


194


195


196



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ReadFieldConstraint))
				return false;
			ReadFieldConstraint other = (ReadFieldConstraint) obj;
			if (fieldRef == null) {
				if (other.fieldRef != null)
					return false;
			} else if (!fieldRef.equals(other.fieldRef))
				return false;
			return true;
		}

		@Override@Override		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = 1;intresult=1;			result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());result=prime*result+((fieldRef==null)?0:fieldRef.hashCode());			return result;returnresult;		}}		@Override@Override		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (obj == null)if(obj==null)				return false;returnfalse;			if (!(obj instanceof ReadFieldConstraint))if(!(objinstanceofReadFieldConstraint))				return false;returnfalse;			ReadFieldConstraint other = (ReadFieldConstraint) obj;ReadFieldConstraintother=(ReadFieldConstraint)obj;			if (fieldRef == null) {if(fieldRef==null){				if (other.fieldRef != null)if(other.fieldRef!=null)					return false;returnfalse;			} else if (!fieldRef.equals(other.fieldRef))}elseif(!fieldRef.equals(other.fieldRef))				return false;returnfalse;			return true;returntrue;		}}




Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015




197


198


199


200


201




		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}






Bug fix: check for possibility before applying DeltaConstraint

 


Johannes Lerch
committed
Jan 30, 2015



Bug fix: check for possibility before applying DeltaConstraint

 

Bug fix: check for possibility before applying DeltaConstraint

Johannes Lerch
committed
Jan 30, 2015


197


198


199


200


201



		@Override
		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
			return !accPath.isAccessInExclusions(fieldRef);
		}

		@Override@Override		public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {publicbooleancanBeAppliedTo(AccessPath<FieldRef>accPath){			return !accPath.isAccessInExclusions(fieldRef);return!accPath.isAccessInExclusions(fieldRef);		}}




annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014




202


203



	}
}





annotated facts (WIP)



Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


202


203


	}
}
	}}}}





