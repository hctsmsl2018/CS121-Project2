



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

4a2f975587c05da46846bc13b03ed98888c05ea4

















4a2f975587c05da46846bc13b03ed98888c05ea4


Switch branch/tag










heros


..


template


BackwardsInterproceduralCFG.java



Find file
Normal viewHistoryPermalink






BackwardsInterproceduralCFG.java



514 Bytes









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.template;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;

/**
 * Same as {@link JimpleBasedInterproceduralCFG} but based on inverted unit graphs.
 * This should be used for backward analyses.
 */
public class BackwardsInterproceduralCFG extends JimpleBasedInterproceduralCFG {

	@Override
	protected DirectedGraph<Unit> makeGraph(Body body) {
		return new InverseGraph<Unit>(super.makeGraph(body));
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

4a2f975587c05da46846bc13b03ed98888c05ea4

















4a2f975587c05da46846bc13b03ed98888c05ea4


Switch branch/tag










heros


..


template


BackwardsInterproceduralCFG.java



Find file
Normal viewHistoryPermalink






BackwardsInterproceduralCFG.java



514 Bytes









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.template;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;

/**
 * Same as {@link JimpleBasedInterproceduralCFG} but based on inverted unit graphs.
 * This should be used for backward analyses.
 */
public class BackwardsInterproceduralCFG extends JimpleBasedInterproceduralCFG {

	@Override
	protected DirectedGraph<Unit> makeGraph(Body body) {
		return new InverseGraph<Unit>(super.makeGraph(body));
	}
	
}











Open sidebar



Joshua Garcia heros

4a2f975587c05da46846bc13b03ed98888c05ea4







Open sidebar



Joshua Garcia heros

4a2f975587c05da46846bc13b03ed98888c05ea4




Open sidebar

Joshua Garcia heros

4a2f975587c05da46846bc13b03ed98888c05ea4


Joshua Garciaherosheros
4a2f975587c05da46846bc13b03ed98888c05ea4










4a2f975587c05da46846bc13b03ed98888c05ea4


Switch branch/tag










heros


..


template


BackwardsInterproceduralCFG.java



Find file
Normal viewHistoryPermalink






BackwardsInterproceduralCFG.java



514 Bytes









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.template;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;

/**
 * Same as {@link JimpleBasedInterproceduralCFG} but based on inverted unit graphs.
 * This should be used for backward analyses.
 */
public class BackwardsInterproceduralCFG extends JimpleBasedInterproceduralCFG {

	@Override
	protected DirectedGraph<Unit> makeGraph(Body body) {
		return new InverseGraph<Unit>(super.makeGraph(body));
	}
	
}














4a2f975587c05da46846bc13b03ed98888c05ea4


Switch branch/tag










heros


..


template


BackwardsInterproceduralCFG.java



Find file
Normal viewHistoryPermalink






BackwardsInterproceduralCFG.java



514 Bytes









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.template;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;

/**
 * Same as {@link JimpleBasedInterproceduralCFG} but based on inverted unit graphs.
 * This should be used for backward analyses.
 */
public class BackwardsInterproceduralCFG extends JimpleBasedInterproceduralCFG {

	@Override
	protected DirectedGraph<Unit> makeGraph(Body body) {
		return new InverseGraph<Unit>(super.makeGraph(body));
	}
	
}










4a2f975587c05da46846bc13b03ed98888c05ea4


Switch branch/tag










heros


..


template


BackwardsInterproceduralCFG.java



Find file
Normal viewHistoryPermalink




4a2f975587c05da46846bc13b03ed98888c05ea4


Switch branch/tag










heros


..


template


BackwardsInterproceduralCFG.java





4a2f975587c05da46846bc13b03ed98888c05ea4


Switch branch/tag








4a2f975587c05da46846bc13b03ed98888c05ea4


Switch branch/tag





4a2f975587c05da46846bc13b03ed98888c05ea4

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

..

template

BackwardsInterproceduralCFG.java
Find file
Normal viewHistoryPermalink




BackwardsInterproceduralCFG.java



514 Bytes









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.template;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;

/**
 * Same as {@link JimpleBasedInterproceduralCFG} but based on inverted unit graphs.
 * This should be used for backward analyses.
 */
public class BackwardsInterproceduralCFG extends JimpleBasedInterproceduralCFG {

	@Override
	protected DirectedGraph<Unit> makeGraph(Body body) {
		return new InverseGraph<Unit>(super.makeGraph(body));
	}
	
}








BackwardsInterproceduralCFG.java



514 Bytes










BackwardsInterproceduralCFG.java



514 Bytes









Newer










Older
NewerOlder







initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.template;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;

/**
 * Same as {@link JimpleBasedInterproceduralCFG} but based on inverted unit graphs.
 * This should be used for backward analyses.
 */
public class BackwardsInterproceduralCFG extends JimpleBasedInterproceduralCFG {

	@Override
	protected DirectedGraph<Unit> makeGraph(Body body) {
		return new InverseGraph<Unit>(super.makeGraph(body));
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
package soot.jimple.interproc.ifds.template;packagesoot.jimple.interproc.ifds.template;import soot.Body;importsoot.Body;import soot.Unit;importsoot.Unit;import soot.toolkits.graph.DirectedGraph;importsoot.toolkits.graph.DirectedGraph;import soot.toolkits.graph.InverseGraph;importsoot.toolkits.graph.InverseGraph;/**/** * Same as {@link JimpleBasedInterproceduralCFG} but based on inverted unit graphs. * Same as {@link JimpleBasedInterproceduralCFG} but based on inverted unit graphs. * This should be used for backward analyses. * This should be used for backward analyses. */ */public class BackwardsInterproceduralCFG extends JimpleBasedInterproceduralCFG {publicclassBackwardsInterproceduralCFGextendsJimpleBasedInterproceduralCFG{	@Override@Override	protected DirectedGraph<Unit> makeGraph(Body body) {protectedDirectedGraph<Unit>makeGraph(Bodybody){		return new InverseGraph<Unit>(super.makeGraph(body));returnnewInverseGraph<Unit>(super.makeGraph(body));	}}	}}





