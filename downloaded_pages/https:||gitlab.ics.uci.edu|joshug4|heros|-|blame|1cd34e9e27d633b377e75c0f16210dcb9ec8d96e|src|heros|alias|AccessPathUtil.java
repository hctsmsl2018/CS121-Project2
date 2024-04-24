



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

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e

















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink






AccessPathUtil.java



3.64 KB









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









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



			return false;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








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



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44












cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



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

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e

















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink






AccessPathUtil.java



3.64 KB









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









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



			return false;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








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



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44












cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



}












Open sidebar



Joshua Garcia heros

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e







Open sidebar



Joshua Garcia heros

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e




Open sidebar

Joshua Garcia heros

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Joshua Garciaherosheros
1cd34e9e27d633b377e75c0f16210dcb9ec8d96e










1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink






AccessPathUtil.java



3.64 KB









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









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



			return false;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








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



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44












cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



}















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink






AccessPathUtil.java



3.64 KB









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









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



			return false;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








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



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44












cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



}











1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java



Find file
Normal viewHistoryPermalink




1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


alias


AccessPathUtil.java





1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag








1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag





1cd34e9e27d633b377e75c0f16210dcb9ec8d96e

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



3.64 KB









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









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



			return false;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








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



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44












cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



}









AccessPathUtil.java



3.64 KB










AccessPathUtil.java



3.64 KB









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









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



			return false;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








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



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44












cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



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









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



			return false;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








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



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44












cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



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









handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



			return false;








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);








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



		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44












cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



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




handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




13


14


15


16



import java.util.ArrayList;

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


13


14


15


16


import java.util.ArrayList;

import com.google.common.base.Optional;


import java.util.ArrayList;importjava.util.ArrayList;import com.google.common.base.Optional;importcom.google.common.base.Optional;




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




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




20



	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


20


	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {

	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef,  D>> boolean isPrefixOf(D prefixCandidate, D fact) {publicstatic<FieldRef,DextendsFieldSensitiveFact<?,FieldRef,D>>booleanisPrefixOf(DprefixCandidate,Dfact){




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




21



		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


21


		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))

		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




22



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


22


			return false;

			return false;returnfalse;




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




23



		






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


23


		

		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




24



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


24


		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());

		return prefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());returnprefixCandidate.getAccessPath().isPrefixOf(fact.getAccessPath());




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




25


26



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


25


26


	}
	

	}}	




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




27



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


27


	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {

	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> Optional<D> applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {publicstatic<FieldRef,DextendsFieldSensitiveFact<?,FieldRef,D>>Optional<D>applyAbstractedSummary(DsourceFact,SummaryEdge<D,?>summary){




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




28


29


30



		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
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


28


29


30


		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		

		if(!isPrefixOf(summary.getSourceFact(), sourceFact))if(!isPrefixOf(summary.getSourceFact(),sourceFact))			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));thrownewIllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'",summary,sourceFact));		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




31


32


33



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


31


32


33


		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();
		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();
		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();

		AccessPath<FieldRef> concreteAccessPath = sourceFact.getAccessPath();AccessPath<FieldRef>concreteAccessPath=sourceFact.getAccessPath();		AccessPath<FieldRef> abstractAccessPath = summary.getSourceFact().getAccessPath();AccessPath<FieldRef>abstractAccessPath=summary.getSourceFact().getAccessPath();		AccessPath<FieldRef> targetAccessPath = summary.getTargetFact().getAccessPath();AccessPath<FieldRef>targetAccessPath=summary.getTargetFact().getAccessPath();




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




34



		






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


34


		

		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




35


36


37



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


35


36


37


		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);
		if(targetAccessPath.isAccessInExclusions(delta))
			return Optional.absent();

		FieldRef[] delta = abstractAccessPath.getDeltaTo(concreteAccessPath);FieldRef[]delta=abstractAccessPath.getDeltaTo(concreteAccessPath);		if(targetAccessPath.isAccessInExclusions(delta))if(targetAccessPath.isAccessInExclusions(delta))			return Optional.absent();returnOptional.absent();




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




38



		






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


38


		

		




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




39


40



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


39


40


		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);
		result = result.mergeExcludedFieldReferences(concreteAccessPath);

		AccessPath<FieldRef> result = targetAccessPath.addFieldReference(delta);AccessPath<FieldRef>result=targetAccessPath.addFieldReference(delta);		result = result.mergeExcludedFieldReferences(concreteAccessPath);result=result.mergeExcludedFieldReferences(concreteAccessPath);




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


42


		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));

		return Optional.of(summary.getTargetFact().cloneWithAccessPath(result));returnOptional.of(summary.getTargetFact().cloneWithAccessPath(result));




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




43



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


43


	}

	}}




annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




44










annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)

 

annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


44









cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




45


46


47


48


49


50



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


45


46


47


48


49


50


//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
//		return fact.cloneWithAccessPath(accessPath);
//	}

//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];//		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);//		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);//		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);//		return fact.cloneWithAccessPath(accessPath);//		return fact.cloneWithAccessPath(accessPath);//	}//	}




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




51



	






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


51


	

	




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69



//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {
//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))
//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));
//		
//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();
//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();
//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();
//		
//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];
//
//		//copy old access path
//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
//		
//		//copy delta access path that was omitted while creating the abstracted source fact
//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
//		
//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);
//	}

//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {//	public static <FieldRef, D extends FieldSensitiveFact<?, FieldRef, D>> D concretizeCallerSourceFact(IncomingEdge<D, ?> incomingEdge, D calleeSourceFact) {//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))//		if(!isPrefixOf(incomingEdge.getCalleeSourceFact(), calleeSourceFact))//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));//			throw new IllegalArgumentException(String.format("Callee Source Fact in IncomingEdge '%s' is not a prefix of the given fact '%s'.", incomingEdge, calleeSourceFact));//		//		//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();//		FieldReference[] abstractAccessPath = incomingEdge.getCalleeSourceFact().getAccessPath();//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();//		FieldReference[] concreteAccessPath = calleeSourceFact.getAccessPath();//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();//		FieldReference[] targetAccessPath = incomingEdge.getCallerSourceFact().getAccessPath();//		//		//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];//		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];//////		//copy old access path//		//copy old access path//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);//		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);//		//		//		//copy delta access path that was omitted while creating the abstracted source fact//		//copy delta access path that was omitted while creating the abstracted source fact//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);//		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);//		//		//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);//		return incomingEdge.getCallerSourceFact().cloneWithAccessPath(resultAccessPath);//	}//	}




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




70



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


70


}
}}





