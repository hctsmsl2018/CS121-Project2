



GitLab


















Projects
Groups
Topics
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
Topics
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
Topics
Snippets



GitLab








GitLab









Projects
Groups
Topics
Snippets






Projects
Groups
Topics
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


Contributor statistics


Graph


Compare revisions







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


Artifacts


Schedules







Deployments




Deployments




Environments


Releases







Packages and registries




Packages and registries




Model experiments







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












Joshua Garcia heros

9176fc7c7babbf1052d95fe4d27e9e7aa2afc392



















heros


src


heros


alias


AccessPathUtil.java




Find file



Normal view


History


Permalink








AccessPathUtil.java



2.74 KiB









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









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



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


Contributor statistics


Graph


Compare revisions







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


Artifacts


Schedules







Deployments




Deployments




Environments


Releases







Packages and registries




Packages and registries




Model experiments







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


Contributor statistics


Graph


Compare revisions






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

Contributor statistics


Contributor statistics

Graph


Graph

Compare revisions


Compare revisions




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


Artifacts


Schedules






CI/CD


CI/CD




CI/CD


Pipelines


Pipelines

Jobs


Jobs

Artifacts


Artifacts

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




Packages and registries




Packages and registries




Model experiments






Packages and registries


Packages and registries




Packages and registries


Model experiments


Model experiments




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








Joshua Garcia heros

9176fc7c7babbf1052d95fe4d27e9e7aa2afc392



















heros


src


heros


alias


AccessPathUtil.java




Find file



Normal view


History


Permalink








AccessPathUtil.java



2.74 KiB









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









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



}
















Joshua Garcia heros

9176fc7c7babbf1052d95fe4d27e9e7aa2afc392












Joshua Garcia heros

9176fc7c7babbf1052d95fe4d27e9e7aa2afc392










Joshua Garcia heros

9176fc7c7babbf1052d95fe4d27e9e7aa2afc392




Joshua Garciaherosheros
9176fc7c7babbf1052d95fe4d27e9e7aa2afc392












heros


src


heros


alias


AccessPathUtil.java




Find file



Normal view


History


Permalink








AccessPathUtil.java



2.74 KiB









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









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



}

















heros


src


heros


alias


AccessPathUtil.java




Find file



Normal view


History


Permalink








AccessPathUtil.java



2.74 KiB









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









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



}













heros


src


heros


alias


AccessPathUtil.java




Find file



Normal view


History


Permalink








heros


src


heros


alias


AccessPathUtil.java





heros

src

heros

alias

AccessPathUtil.java

Find file



Normal view


History


Permalink


Find file


Normal view

History

Permalink





AccessPathUtil.java



2.74 KiB









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









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



}









AccessPathUtil.java



2.74 KiB










AccessPathUtil.java



2.74 KiB









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









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



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









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



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









annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}








annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



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




annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




13


14



import heros.alias.FieldReference.SpecificFieldReference;







annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)

 

annotated facts (WIP)

Johannes Lerch
committed
Nov 13, 2014


13


14


import heros.alias.FieldReference.SpecificFieldReference;


import heros.alias.FieldReference.SpecificFieldReference;importheros.alias.FieldReference.SpecificFieldReference;




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51



public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
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


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


public class AccessPathUtil {

	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {
		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))
			return false;	
		
		FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();
		FieldReference[] factAccessPath = fact.getAccessPath();
		if(prefixAccessPath.length > factAccessPath.length)
			return false;
		
		for(int i=0; i<prefixAccessPath.length; i++) {
			if(!prefixAccessPath[i].equals(factAccessPath[i]))
				return false;
		}
		
		return true;
	}
	
	public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {
		if(!isPrefixOf(summary.getSourceFact(), sourceFact))
			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));
		
		FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();
		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();
		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();
		
		FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];

		//copy old access path
		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);
		
		//copy delta access path that was omitted while creating the abstracted source fact
		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);
		
		return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);
	}

public class AccessPathUtil {publicclassAccessPathUtil{	public static <D extends FieldSensitiveFact<?, D>> boolean isPrefixOf(D prefixCandidate, D fact) {publicstatic<DextendsFieldSensitiveFact<?,D>>booleanisPrefixOf(DprefixCandidate,Dfact){		if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))if(!prefixCandidate.getBaseValue().equals(fact.getBaseValue()))			return false;	returnfalse;				FieldReference[] prefixAccessPath = prefixCandidate.getAccessPath();FieldReference[]prefixAccessPath=prefixCandidate.getAccessPath();		FieldReference[] factAccessPath = fact.getAccessPath();FieldReference[]factAccessPath=fact.getAccessPath();		if(prefixAccessPath.length > factAccessPath.length)if(prefixAccessPath.length>factAccessPath.length)			return false;returnfalse;				for(int i=0; i<prefixAccessPath.length; i++) {for(inti=0;i<prefixAccessPath.length;i++){			if(!prefixAccessPath[i].equals(factAccessPath[i]))if(!prefixAccessPath[i].equals(factAccessPath[i]))				return false;returnfalse;		}}				return true;returntrue;	}}		public static <D extends FieldSensitiveFact<?, D>> D applyAbstractedSummary(D sourceFact, SummaryEdge<D, ?> summary) {publicstatic<DextendsFieldSensitiveFact<?,D>>DapplyAbstractedSummary(DsourceFact,SummaryEdge<D,?>summary){		if(!isPrefixOf(summary.getSourceFact(), sourceFact))if(!isPrefixOf(summary.getSourceFact(),sourceFact))			throw new IllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'", summary, sourceFact));thrownewIllegalArgumentException(String.format("Source fact in given summary edge '%s' is not a prefix of the given source fact '%s'",summary,sourceFact));				FieldReference[] abstractAccessPath = summary.getSourceFact().getAccessPath();FieldReference[]abstractAccessPath=summary.getSourceFact().getAccessPath();		FieldReference[] concreteAccessPath = sourceFact.getAccessPath();FieldReference[]concreteAccessPath=sourceFact.getAccessPath();		FieldReference[] targetAccessPath = summary.getTargetFact().getAccessPath();FieldReference[]targetAccessPath=summary.getTargetFact().getAccessPath();				FieldReference[] resultAccessPath = new FieldReference[targetAccessPath.length + concreteAccessPath.length - abstractAccessPath.length];FieldReference[]resultAccessPath=newFieldReference[targetAccessPath.length+concreteAccessPath.length-abstractAccessPath.length];		//copy old access path//copy old access path		System.arraycopy(targetAccessPath, 0, resultAccessPath, 0, targetAccessPath.length);System.arraycopy(targetAccessPath,0,resultAccessPath,0,targetAccessPath.length);				//copy delta access path that was omitted while creating the abstracted source fact//copy delta access path that was omitted while creating the abstracted source fact		System.arraycopy(concreteAccessPath, abstractAccessPath.length, resultAccessPath, targetAccessPath.length, concreteAccessPath.length - abstractAccessPath.length);System.arraycopy(concreteAccessPath,abstractAccessPath.length,resultAccessPath,targetAccessPath.length,concreteAccessPath.length-abstractAccessPath.length);				return summary.getTargetFact().cloneWithAccessPath(resultAccessPath);returnsummary.getTargetFact().cloneWithAccessPath(resultAccessPath);	}}




annotated facts (WIP)

 


Johannes Lerch
committed
Nov 13, 2014




52


53


54


55


56


57


58




	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
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


52


53


54


55


56


57


58



	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {
		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];
		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);
		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);
		return fact.cloneWithAccessPath(accessPath);
	}

	public static <D extends FieldSensitiveFact<?, D>> D cloneWithConcatenatedAccessPath(D fact, FieldReference... fieldRefs) {publicstatic<DextendsFieldSensitiveFact<?,D>>DcloneWithConcatenatedAccessPath(Dfact,FieldReference...fieldRefs){		FieldReference[] accessPath = new FieldReference[fact.getAccessPath().length+fieldRefs.length];FieldReference[]accessPath=newFieldReference[fact.getAccessPath().length+fieldRefs.length];		System.arraycopy(fact.getAccessPath(), 0, accessPath, 0, fact.getAccessPath().length);System.arraycopy(fact.getAccessPath(),0,accessPath,0,fact.getAccessPath().length);		System.arraycopy(fieldRefs, 0, accessPath, fact.getAccessPath().length, fieldRefs.length);System.arraycopy(fieldRefs,0,accessPath,fact.getAccessPath().length,fieldRefs.length);		return fact.cloneWithAccessPath(accessPath);returnfact.cloneWithAccessPath(accessPath);	}}




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




59



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


59


}
}}





