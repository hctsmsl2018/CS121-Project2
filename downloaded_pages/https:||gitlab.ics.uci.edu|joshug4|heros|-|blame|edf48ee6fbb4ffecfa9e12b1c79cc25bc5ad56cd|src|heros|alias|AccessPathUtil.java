



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

edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd

















edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink






AccessPathUtil.java



2.46 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17












use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51












avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



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

edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd

















edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink






AccessPathUtil.java



2.46 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17












use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51












avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



}












Open sidebar



Joshua Garcia heros

edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd







Open sidebar



Joshua Garcia heros

edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd




Open sidebar

Joshua Garcia heros

edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Joshua Garciaherosheros
edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd










edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink






AccessPathUtil.java



2.46 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17












use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51












avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



}















edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink






AccessPathUtil.java



2.46 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17












use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51












avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



}











edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink




edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java





edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Switch branch/tag








edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd


Switch branch/tag





edf48ee6fbb4ffecfa9e12b1c79cc25bc5ad56cd

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

alias

AccessPathUtil.java
Find file
Normal viewHistoryPermalink




AccessPathUtil.java



2.46 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17












use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51












avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



}









AccessPathUtil.java



2.46 KB










AccessPathUtil.java



2.46 KB









Newer










Older
NewerOlder







use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17












use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51












avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



}











use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17












use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51












avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



}









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17












use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {









Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51












avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



}







use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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



/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;







use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


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


/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;


/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




13


14



import heros.alias.AccessPath.PrefixTestResult;







Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


13


14


import heros.alias.AccessPath.PrefixTestResult;


import heros.alias.AccessPath.PrefixTestResult;importheros.alias.AccessPath.PrefixTestResult;




handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




15


16



import com.google.common.base.Optional;







handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths

 

handling for writing fields / excluding access paths

Johannes Lerch
committed
Jan 05, 2015


15


16


import com.google.common.base.Optional;


import com.google.common.base.Optional;importcom.google.common.base.Optional;




annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




17










annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)

 

annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


17









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




18


19



public class AccessPathUtil {







use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


18


19


public class AccessPathUtil {


public class AccessPathUtil {publicclassAccessPathUtil{




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


20


	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {

	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> PrefixTestResult isPrefixOf(D prefixCandidate, D fact) {publicstatic<FieldRef,DextendsFieldSensitiveFact<?,FieldRef,D>>PrefixTestResultisPrefixOf(DprefixCandidate,Dfact){




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




21


22



		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


21


22


		if(prefixCandidate.getBaseValue() == null) {
			if(fact.getBaseValue() != null)

		if(prefixCandidate.getBaseValue() == null) {if(prefixCandidate.getBaseValue()==null){			if(fact.getBaseValue() != null)if(fact.getBaseValue()!=null)




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




23



				return PrefixTestResult.NO_PREFIX;






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


23


				return PrefixTestResult.NO_PREFIX;

				return PrefixTestResult.NO_PREFIX;returnPrefixTestResult.NO_PREFIX;




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




24



		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


24


		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))

		} else if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))}elseif(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




25



			return PrefixTestResult.NO_PREFIX;






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


25


			return PrefixTestResult.NO_PREFIX;

			return PrefixTestResult.NO_PREFIX;returnPrefixTestResult.NO_PREFIX;




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




26



		






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


26


		

		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


27


		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());

		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());returnprefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29



	}
	






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


28


29


	}
	

	}}	




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




30



	






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


30


	

	




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


31


	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {

	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {publicstatic<FieldRef,DextendsFieldSensitiveFact<?,FieldRef,D>>Optional<D>applyAbstractedSummary(DsourceFact,SummaryEdge<D,?>summary){




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




32



		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


32


		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))

		if(!isPrefixOf(summary.getSourceFact(), sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))if(!isPrefixOf(summary.getSourceFact(),sourceFact).atLeast(PrefixTestResult.GUARANTEED_PREFIX))




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




33


34



			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


33


34


			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		

			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));thrownewIllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'",summary,sourceFact));		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


35


36


37


		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();

		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();AccessPath<FieldRef>concreteAccessPath=sourceFact.getAccessPath();		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();AccessPath<FieldRef>abstractAccessPath=summary.getSourceFact().getAccessPath();		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();AccessPath<FieldRef>targetAccessPath=summary.getTargetFact().getAccessPath();




avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




38


39


40




		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());






avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015



avoiding unnecessary calls to clone a fact

 

avoiding unnecessary calls to clone a fact

Johannes Lerch
committed
Jan 08, 2015


38


39


40



		if(abstractAccessPath.equals(concreteAccessPath))
			return Optional.of(summary.getTargetFact());

		if(abstractAccessPath.equals(concreteAccessPath))if(abstractAccessPath.equals(concreteAccessPath))			return Optional.of(summary.getTargetFact());returnOptional.of(summary.getTargetFact());




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




41



		






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


41


		

		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




42


43


44



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


42


43


44


		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();

		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);FieldRef[]delta=abstractAccessPath.getDeltaTo(concreteAccessPath);		if(targetAccessPath.isAccessInExclusions(delta))if(targetAccessPath.isAccessInExclusions(delta))			return Optional.absent();returnOptional.absent();




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




45



		






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


45


		

		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




46


47



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


46


47


		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);

		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);AccessPath<FieldRef>result=targetAccessPath.addFieldReference(delta);		result = result.mergeExcludedFieldReferences(concreteAccessPath);result=result.mergeExcludedFieldReferences(concreteAccessPath);




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




48



		






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


48


		

		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




49



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


49


		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));

		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));returnOptional.of(summary.getTargetFact().cloneWithAccessPath(result));




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




50



	}






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


50


	}

	}}




annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




51










annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)

 

annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


51









avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015




52


53


54


55


56


57



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}






avoiding unnecessary calls to clone a fact

 


Johannes Lerch
committed
Jan 08, 2015



avoiding unnecessary calls to clone a fact

 

avoiding unnecessary calls to clone a fact

Johannes Lerch
committed
Jan 08, 2015


52


53


54


55


56


57


	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {
		if(fact.getAccessPath().equals(accPath))
			return fact;
		else
			return fact.cloneWithAccessPath(accPath);
	}

	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> D cloneWithAccessPath(D fact, AccessPath<FieldRef> accPath) {publicstatic<FieldRef,DextendsFieldSensitiveFact<?,FieldRef,D>>DcloneWithAccessPath(Dfact,AccessPath<FieldRef>accPath){		if(fact.getAccessPath().equals(accPath))if(fact.getAccessPath().equals(accPath))			return fact;returnfact;		elseelse			return fact.cloneWithAccessPath(accPath);returnfact.cloneWithAccessPath(accPath);	}}




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




58



}





use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


58


}
}}





