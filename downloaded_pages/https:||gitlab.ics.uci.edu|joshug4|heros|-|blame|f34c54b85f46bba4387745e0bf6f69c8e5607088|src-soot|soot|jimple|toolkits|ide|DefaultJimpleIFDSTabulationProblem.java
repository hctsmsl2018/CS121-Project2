



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

f34c54b85f46bba4387745e0bf6f69c8e5607088

















f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


DefaultJimpleIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultJimpleIFDSTabulationProblem.java



596 Bytes









Newer










Older









adding missing files



 


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




11




12




13




14




15




16




17




18




19




package soot.jimple.toolkits.ide;

import soot.SootMethod;
import soot.Unit;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.template.DefaultIDETabulationProblem;
import de.bodden.ide.template.DefaultIFDSTabulationProblem;

/**
 *  A {@link DefaultIDETabulationProblem} with {@link Unit}s as nodes and {@link SootMethod}s as methods.
 */
public abstract class DefaultJimpleIFDSTabulationProblem<D,I extends InterproceduralCFG<Unit,SootMethod>>
  extends DefaultIFDSTabulationProblem<Unit,D,SootMethod,I> {

	public DefaultJimpleIFDSTabulationProblem(I icfg) {
		super(icfg);
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

f34c54b85f46bba4387745e0bf6f69c8e5607088

















f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


DefaultJimpleIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultJimpleIFDSTabulationProblem.java



596 Bytes









Newer










Older









adding missing files



 


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




11




12




13




14




15




16




17




18




19




package soot.jimple.toolkits.ide;

import soot.SootMethod;
import soot.Unit;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.template.DefaultIDETabulationProblem;
import de.bodden.ide.template.DefaultIFDSTabulationProblem;

/**
 *  A {@link DefaultIDETabulationProblem} with {@link Unit}s as nodes and {@link SootMethod}s as methods.
 */
public abstract class DefaultJimpleIFDSTabulationProblem<D,I extends InterproceduralCFG<Unit,SootMethod>>
  extends DefaultIFDSTabulationProblem<Unit,D,SootMethod,I> {

	public DefaultJimpleIFDSTabulationProblem(I icfg) {
		super(icfg);
	}
	
}











Open sidebar



Joshua Garcia heros

f34c54b85f46bba4387745e0bf6f69c8e5607088







Open sidebar



Joshua Garcia heros

f34c54b85f46bba4387745e0bf6f69c8e5607088




Open sidebar

Joshua Garcia heros

f34c54b85f46bba4387745e0bf6f69c8e5607088


Joshua Garciaherosheros
f34c54b85f46bba4387745e0bf6f69c8e5607088










f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


DefaultJimpleIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultJimpleIFDSTabulationProblem.java



596 Bytes









Newer










Older









adding missing files



 


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




11




12




13




14




15




16




17




18




19




package soot.jimple.toolkits.ide;

import soot.SootMethod;
import soot.Unit;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.template.DefaultIDETabulationProblem;
import de.bodden.ide.template.DefaultIFDSTabulationProblem;

/**
 *  A {@link DefaultIDETabulationProblem} with {@link Unit}s as nodes and {@link SootMethod}s as methods.
 */
public abstract class DefaultJimpleIFDSTabulationProblem<D,I extends InterproceduralCFG<Unit,SootMethod>>
  extends DefaultIFDSTabulationProblem<Unit,D,SootMethod,I> {

	public DefaultJimpleIFDSTabulationProblem(I icfg) {
		super(icfg);
	}
	
}














f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


DefaultJimpleIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultJimpleIFDSTabulationProblem.java



596 Bytes









Newer










Older









adding missing files



 


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




11




12




13




14




15




16




17




18




19




package soot.jimple.toolkits.ide;

import soot.SootMethod;
import soot.Unit;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.template.DefaultIDETabulationProblem;
import de.bodden.ide.template.DefaultIFDSTabulationProblem;

/**
 *  A {@link DefaultIDETabulationProblem} with {@link Unit}s as nodes and {@link SootMethod}s as methods.
 */
public abstract class DefaultJimpleIFDSTabulationProblem<D,I extends InterproceduralCFG<Unit,SootMethod>>
  extends DefaultIFDSTabulationProblem<Unit,D,SootMethod,I> {

	public DefaultJimpleIFDSTabulationProblem(I icfg) {
		super(icfg);
	}
	
}










f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


DefaultJimpleIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink




f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


DefaultJimpleIFDSTabulationProblem.java





f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag








f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag





f34c54b85f46bba4387745e0bf6f69c8e5607088

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src-soot

soot

jimple

toolkits

ide

DefaultJimpleIFDSTabulationProblem.java
Find file
Normal viewHistoryPermalink




DefaultJimpleIFDSTabulationProblem.java



596 Bytes









Newer










Older









adding missing files



 


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




11




12




13




14




15




16




17




18




19




package soot.jimple.toolkits.ide;

import soot.SootMethod;
import soot.Unit;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.template.DefaultIDETabulationProblem;
import de.bodden.ide.template.DefaultIFDSTabulationProblem;

/**
 *  A {@link DefaultIDETabulationProblem} with {@link Unit}s as nodes and {@link SootMethod}s as methods.
 */
public abstract class DefaultJimpleIFDSTabulationProblem<D,I extends InterproceduralCFG<Unit,SootMethod>>
  extends DefaultIFDSTabulationProblem<Unit,D,SootMethod,I> {

	public DefaultJimpleIFDSTabulationProblem(I icfg) {
		super(icfg);
	}
	
}








DefaultJimpleIFDSTabulationProblem.java



596 Bytes










DefaultJimpleIFDSTabulationProblem.java



596 Bytes









Newer










Older
NewerOlder







adding missing files



 


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




11




12




13




14




15




16




17




18




19




package soot.jimple.toolkits.ide;

import soot.SootMethod;
import soot.Unit;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.template.DefaultIDETabulationProblem;
import de.bodden.ide.template.DefaultIFDSTabulationProblem;

/**
 *  A {@link DefaultIDETabulationProblem} with {@link Unit}s as nodes and {@link SootMethod}s as methods.
 */
public abstract class DefaultJimpleIFDSTabulationProblem<D,I extends InterproceduralCFG<Unit,SootMethod>>
  extends DefaultIFDSTabulationProblem<Unit,D,SootMethod,I> {

	public DefaultJimpleIFDSTabulationProblem(I icfg) {
		super(icfg);
	}
	
}







adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


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

11

12

13

14

15

16

17

18

19
package soot.jimple.toolkits.ide;packagesoot.jimple.toolkits.ide;import soot.SootMethod;importsoot.SootMethod;import soot.Unit;importsoot.Unit;import de.bodden.ide.InterproceduralCFG;importde.bodden.ide.InterproceduralCFG;import de.bodden.ide.template.DefaultIDETabulationProblem;importde.bodden.ide.template.DefaultIDETabulationProblem;import de.bodden.ide.template.DefaultIFDSTabulationProblem;importde.bodden.ide.template.DefaultIFDSTabulationProblem;/**/** *  A {@link DefaultIDETabulationProblem} with {@link Unit}s as nodes and {@link SootMethod}s as methods. *  A {@link DefaultIDETabulationProblem} with {@link Unit}s as nodes and {@link SootMethod}s as methods. */ */public abstract class DefaultJimpleIFDSTabulationProblem<D,I extends InterproceduralCFG<Unit,SootMethod>>publicabstractclassDefaultJimpleIFDSTabulationProblem<D,IextendsInterproceduralCFG<Unit,SootMethod>>  extends DefaultIFDSTabulationProblem<Unit,D,SootMethod,I> {extendsDefaultIFDSTabulationProblem<Unit,D,SootMethod,I>{	public DefaultJimpleIFDSTabulationProblem(I icfg) {publicDefaultJimpleIFDSTabulationProblem(Iicfg){		super(icfg);super(icfg);	}}	}}





