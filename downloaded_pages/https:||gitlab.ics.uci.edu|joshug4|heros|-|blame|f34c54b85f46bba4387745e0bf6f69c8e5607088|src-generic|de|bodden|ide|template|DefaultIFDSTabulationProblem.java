



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


src-generic


de


bodden


ide


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultIFDSTabulationProblem.java



1.38 KB









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




package de.bodden.ide.template;

import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;

/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override
	public final I interproceduralCFG() {
		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
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


src-generic


de


bodden


ide


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultIFDSTabulationProblem.java



1.38 KB









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




package de.bodden.ide.template;

import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;

/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override
	public final I interproceduralCFG() {
		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
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


src-generic


de


bodden


ide


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultIFDSTabulationProblem.java



1.38 KB









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




package de.bodden.ide.template;

import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;

/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override
	public final I interproceduralCFG() {
		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}

}














f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-generic


de


bodden


ide


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultIFDSTabulationProblem.java



1.38 KB









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




package de.bodden.ide.template;

import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;

/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override
	public final I interproceduralCFG() {
		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}

}










f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-generic


de


bodden


ide


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink




f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag










heros


src-generic


de


bodden


ide


template


DefaultIFDSTabulationProblem.java





f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag








f34c54b85f46bba4387745e0bf6f69c8e5607088


Switch branch/tag





f34c54b85f46bba4387745e0bf6f69c8e5607088

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src-generic

de

bodden

ide

template

DefaultIFDSTabulationProblem.java
Find file
Normal viewHistoryPermalink




DefaultIFDSTabulationProblem.java



1.38 KB









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




package de.bodden.ide.template;

import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;

/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override
	public final I interproceduralCFG() {
		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}

}








DefaultIFDSTabulationProblem.java



1.38 KB










DefaultIFDSTabulationProblem.java



1.38 KB









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




package de.bodden.ide.template;

import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;

/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override
	public final I interproceduralCFG() {
		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
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
package de.bodden.ide.template;packagede.bodden.ide.template;import de.bodden.ide.FlowFunctions;importde.bodden.ide.FlowFunctions;import de.bodden.ide.IFDSTabulationProblem;importde.bodden.ide.IFDSTabulationProblem;import de.bodden.ide.InterproceduralCFG;importde.bodden.ide.InterproceduralCFG;/**/** * This is a template for {@link IFDSTabulationProblem}s that automatically caches values * This is a template for {@link IFDSTabulationProblem}s that automatically caches values * that ought to be cached. This class uses the Factory Method design pattern. * that ought to be cached. This class uses the Factory Method design pattern. * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently * reused for solving multiple different {@link IFDSTabulationProblem}s. * reused for solving multiple different {@link IFDSTabulationProblem}s. * This class is specific to Soot.  * This class is specific to Soot.  *  *  * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem. */ */public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {publicabstractclassDefaultIFDSTabulationProblem<N,D,M,IextendsInterproceduralCFG<N,M>>implementsIFDSTabulationProblem<N,D,M,I>{	private final I icfg;privatefinalIicfg;	private FlowFunctions<N,D,M> flowFunctions;privateFlowFunctions<N,D,M>flowFunctions;	private D zeroValue;privateDzeroValue;		public DefaultIFDSTabulationProblem(I icfg) {publicDefaultIFDSTabulationProblem(Iicfg){		this.icfg = icfg;this.icfg=icfg;	}}		protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();protectedabstractFlowFunctions<N,D,M>createFlowFunctionsFactory();	protected abstract D createZeroValue();protectedabstractDcreateZeroValue();	@Override@Override	public final FlowFunctions<N,D,M> flowFunctions() {publicfinalFlowFunctions<N,D,M>flowFunctions(){		if(flowFunctions==null) {if(flowFunctions==null){			flowFunctions = createFlowFunctionsFactory();flowFunctions=createFlowFunctionsFactory();		}}		return flowFunctions;returnflowFunctions;	}}	@Override@Override	public final I interproceduralCFG() {publicfinalIinterproceduralCFG(){		return icfg;returnicfg;	}}	@Override@Override	public final D zeroValue() {publicfinalDzeroValue(){		if(zeroValue==null) {if(zeroValue==null){			zeroValue = createZeroValue();zeroValue=createZeroValue();		}}		return zeroValue;returnzeroValue;	}}}}





