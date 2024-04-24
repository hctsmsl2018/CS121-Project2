



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

19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0

















19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink






Kill.java



1.23 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




package heros.flowfunc;










cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






13




14




import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






15




import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






19














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * Function that kills a specific value (i.e. returns an empty set for when given this
 * value as an argument), but behaves like the identity function for all other values.
 *
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public class Kill<D> implements FlowFunction<D> {
	
	private final D killValue;
	
	public Kill(D killValue){
		this.killValue = killValue;
	} 

	public Set<D> computeTargets(D source) {









fix to Kill function: should compare using equals, not ==


 

 


Eric Bodden
committed
Dec 18, 2014






36




		if(source.equals(killValue)) {









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






37




			return emptySet();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




		} else









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






39




			return singleton(source);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






40




41




42




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

19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0

















19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink






Kill.java



1.23 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




package heros.flowfunc;










cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






13




14




import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






15




import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






19














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * Function that kills a specific value (i.e. returns an empty set for when given this
 * value as an argument), but behaves like the identity function for all other values.
 *
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public class Kill<D> implements FlowFunction<D> {
	
	private final D killValue;
	
	public Kill(D killValue){
		this.killValue = killValue;
	} 

	public Set<D> computeTargets(D source) {









fix to Kill function: should compare using equals, not ==


 

 


Eric Bodden
committed
Dec 18, 2014






36




		if(source.equals(killValue)) {









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






37




			return emptySet();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




		} else









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






39




			return singleton(source);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






40




41




42




	}
	
}











Open sidebar



Joshua Garcia heros

19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0







Open sidebar



Joshua Garcia heros

19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0




Open sidebar

Joshua Garcia heros

19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Joshua Garciaherosheros
19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0










19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink






Kill.java



1.23 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




package heros.flowfunc;










cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






13




14




import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






15




import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






19














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * Function that kills a specific value (i.e. returns an empty set for when given this
 * value as an argument), but behaves like the identity function for all other values.
 *
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public class Kill<D> implements FlowFunction<D> {
	
	private final D killValue;
	
	public Kill(D killValue){
		this.killValue = killValue;
	} 

	public Set<D> computeTargets(D source) {









fix to Kill function: should compare using equals, not ==


 

 


Eric Bodden
committed
Dec 18, 2014






36




		if(source.equals(killValue)) {









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






37




			return emptySet();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




		} else









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






39




			return singleton(source);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






40




41




42




	}
	
}














19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink






Kill.java



1.23 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




package heros.flowfunc;










cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






13




14




import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






15




import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






19














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * Function that kills a specific value (i.e. returns an empty set for when given this
 * value as an argument), but behaves like the identity function for all other values.
 *
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public class Kill<D> implements FlowFunction<D> {
	
	private final D killValue;
	
	public Kill(D killValue){
		this.killValue = killValue;
	} 

	public Set<D> computeTargets(D source) {









fix to Kill function: should compare using equals, not ==


 

 


Eric Bodden
committed
Dec 18, 2014






36




		if(source.equals(killValue)) {









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






37




			return emptySet();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




		} else









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






39




			return singleton(source);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






40




41




42




	}
	
}










19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink




19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Switch branch/tag










heros


src


heros


flowfunc


Kill.java





19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Switch branch/tag








19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0


Switch branch/tag





19cb6fbd0be3eed029a9b4ca706885ee23d7e6e0

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

flowfunc

Kill.java
Find file
Normal viewHistoryPermalink




Kill.java



1.23 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




package heros.flowfunc;










cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






13




14




import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






15




import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






19














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * Function that kills a specific value (i.e. returns an empty set for when given this
 * value as an argument), but behaves like the identity function for all other values.
 *
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public class Kill<D> implements FlowFunction<D> {
	
	private final D killValue;
	
	public Kill(D killValue){
		this.killValue = killValue;
	} 

	public Set<D> computeTargets(D source) {









fix to Kill function: should compare using equals, not ==


 

 


Eric Bodden
committed
Dec 18, 2014






36




		if(source.equals(killValue)) {









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






37




			return emptySet();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




		} else









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






39




			return singleton(source);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






40




41




42




	}
	
}








Kill.java



1.23 KB










Kill.java



1.23 KB









Newer










Older
NewerOlder







license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




package heros.flowfunc;










cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






13




14




import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






15




import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






19














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * Function that kills a specific value (i.e. returns an empty set for when given this
 * value as an argument), but behaves like the identity function for all other values.
 *
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public class Kill<D> implements FlowFunction<D> {
	
	private final D killValue;
	
	public Kill(D killValue){
		this.killValue = killValue;
	} 

	public Set<D> computeTargets(D source) {









fix to Kill function: should compare using equals, not ==


 

 


Eric Bodden
committed
Dec 18, 2014






36




		if(source.equals(killValue)) {









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






37




			return emptySet();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




		} else









cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013






39




			return singleton(source);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






40




41




42




	}
	
}







license headers


 

 


Eric Bodden
committed
Nov 29, 2012



license headers


 

 

license headers

 

Eric Bodden
committed
Nov 29, 2012

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/



renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

11

12
package heros.flowfunc;packageheros.flowfunc;



cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013



cleaned up code


 

 

cleaned up code

 

Eric Bodden
committed
Dec 24, 2013

13

14
import static java.util.Collections.emptySet;importstaticjava.util.Collections.emptySet;import static java.util.Collections.singleton;importstaticjava.util.Collections.singleton;



renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

15
import heros.FlowFunction;importheros.FlowFunction;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

16

17

18
import java.util.Set;importjava.util.Set;



renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

19




initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
/**/** * Function that kills a specific value (i.e. returns an empty set for when given this * Function that kills a specific value (i.e. returns an empty set for when given this * value as an argument), but behaves like the identity function for all other values. * value as an argument), but behaves like the identity function for all other values. * * * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem. */ */public class Kill<D> implements FlowFunction<D> {publicclassKill<D>implementsFlowFunction<D>{		private final D killValue;privatefinalDkillValue;		public Kill(D killValue){publicKill(DkillValue){		this.killValue = killValue;this.killValue=killValue;	} }	public Set<D> computeTargets(D source) {publicSet<D>computeTargets(Dsource){



fix to Kill function: should compare using equals, not ==


 

 


Eric Bodden
committed
Dec 18, 2014



fix to Kill function: should compare using equals, not ==


 

 

fix to Kill function: should compare using equals, not ==

 

Eric Bodden
committed
Dec 18, 2014

36
		if(source.equals(killValue)) {if(source.equals(killValue)){



cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013



cleaned up code


 

 

cleaned up code

 

Eric Bodden
committed
Dec 24, 2013

37
			return emptySet();returnemptySet();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

38
		} else}else



cleaned up code


 

 


Eric Bodden
committed
Dec 24, 2013



cleaned up code


 

 

cleaned up code

 

Eric Bodden
committed
Dec 24, 2013

39
			return singleton(source);returnsingleton(source);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

40

41

42
	}}	}}





