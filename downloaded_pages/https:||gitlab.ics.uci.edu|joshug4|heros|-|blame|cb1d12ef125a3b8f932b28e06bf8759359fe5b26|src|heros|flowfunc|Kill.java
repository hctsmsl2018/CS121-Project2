



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

cb1d12ef125a3b8f932b28e06bf8759359fe5b26

















cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink






Kill.java



680 Bytes









Newer










Older









renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




8












initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
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

cb1d12ef125a3b8f932b28e06bf8759359fe5b26

















cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink






Kill.java



680 Bytes









Newer










Older









renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




8












initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}












Open sidebar



Joshua Garcia heros

cb1d12ef125a3b8f932b28e06bf8759359fe5b26







Open sidebar



Joshua Garcia heros

cb1d12ef125a3b8f932b28e06bf8759359fe5b26




Open sidebar

Joshua Garcia heros

cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Joshua Garciaherosheros
cb1d12ef125a3b8f932b28e06bf8759359fe5b26










cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink






Kill.java



680 Bytes









Newer










Older









renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




8












initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}















cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink






Kill.java



680 Bytes









Newer










Older









renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




8












initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}











cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Switch branch/tag










heros


src


heros


flowfunc


Kill.java



Find file
Normal viewHistoryPermalink




cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Switch branch/tag










heros


src


heros


flowfunc


Kill.java





cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Switch branch/tag








cb1d12ef125a3b8f932b28e06bf8759359fe5b26


Switch branch/tag





cb1d12ef125a3b8f932b28e06bf8759359fe5b26

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



680 Bytes









Newer










Older









renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




8












initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}









Kill.java



680 Bytes










Kill.java



680 Bytes









Newer










Older
NewerOlder







renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




8












initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}











renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




8












initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}









renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




8












initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}







renamed package

 


Eric Bodden
committed
Nov 29, 2012




1


2


3



package heros.flowfunc;

import heros.FlowFunction;






renamed package

 


Eric Bodden
committed
Nov 29, 2012



renamed package

 

renamed package

Eric Bodden
committed
Nov 29, 2012


1


2


3


package heros.flowfunc;

import heros.FlowFunction;

package heros.flowfunc;packageheros.flowfunc;import heros.FlowFunction;importheros.FlowFunction;




initial checkin



Eric Bodden
committed
Nov 14, 2012




4


5


6


7




import java.util.Collections;
import java.util.Set;







initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


4


5


6


7



import java.util.Collections;
import java.util.Set;


import java.util.Collections;importjava.util.Collections;import java.util.Set;importjava.util.Set;




renamed package

 


Eric Bodden
committed
Nov 28, 2012




8










renamed package

 


Eric Bodden
committed
Nov 28, 2012



renamed package

 

renamed package

Eric Bodden
committed
Nov 28, 2012


8









initial checkin



Eric Bodden
committed
Nov 14, 2012




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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}





initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


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
		if(source==killValue) {
			return Collections.emptySet();
		} else
			return Collections.singleton(source);
	}
	
}
/**/** * Function that kills a specific value (i.e. returns an empty set for when given this * Function that kills a specific value (i.e. returns an empty set for when given this * value as an argument), but behaves like the identity function for all other values. * value as an argument), but behaves like the identity function for all other values. * * * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem. */ */public class Kill<D> implements FlowFunction<D> {publicclassKill<D>implementsFlowFunction<D>{		private final D killValue;privatefinalDkillValue;		public Kill(D killValue){publicKill(DkillValue){		this.killValue = killValue;this.killValue=killValue;	} }	public Set<D> computeTargets(D source) {publicSet<D>computeTargets(Dsource){		if(source==killValue) {if(source==killValue){			return Collections.emptySet();returnCollections.emptySet();		} else}else			return Collections.singleton(source);returnCollections.singleton(source);	}}	}}





