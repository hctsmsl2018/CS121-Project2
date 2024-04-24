



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

600527394453cccf4607d618130727017360f8ca

















600527394453cccf4607d618130727017360f8ca


Switch branch/tag










heros


src


heros


IFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






IFDSTabulationProblem.java



2.84 KB









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




package heros;










improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






13




import heros.solver.IDESolver;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






14




import heros.solver.IFDSSolver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






15




16




17





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






18














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A tabulation problem for solving in an {@link IFDSSolver} as described
 * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public interface IFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> {

	/**
	 * Returns a set of flow functions. Those functions are used to compute data-flow facts
	 * along the various kinds of control flows.
     *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	FlowFunctions<N,D,M> flowFunctions();
	
	/**
	 * Returns the interprocedural control-flow graph which this problem is computed over.
	 * 
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	I interproceduralCFG();
	
	/**
	 * Returns initial seeds to be used for the analysis. (a set of statements)
	 */
	Set<N> initialSeeds();
	
	/**
	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>
	 * be part of the domain of data-flow facts. Typically this will be a
	 * singleton object of type {@link D} that is used for nothing else.
	 * It must holds that this object does not equals any object 
	 * within the domain.
	 *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	D zeroValue();









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






64




65




66




67




68




	
	/**
	 * If true, the analysis will compute a partially unbalanced analysis problem in which
	 * function returns are followed also further up the call stack than where the initial seeds
	 * started.









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






69




70




71




72




	 * 
	 * If this is enabled, when reaching the exit of a method that is <i>nowhere</i> called, in order
	 * to avoid not at all processing the exit statement, the {@link IDESolver} will call
	 * the <i>normal</i> flow function with both <i>curr</i> and <i>succ</i> set to the exit node.









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






73




74




	 */
	boolean followReturnsPastSeeds();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






75




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

600527394453cccf4607d618130727017360f8ca

















600527394453cccf4607d618130727017360f8ca


Switch branch/tag










heros


src


heros


IFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






IFDSTabulationProblem.java



2.84 KB









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




package heros;










improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






13




import heros.solver.IDESolver;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






14




import heros.solver.IFDSSolver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






15




16




17





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






18














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A tabulation problem for solving in an {@link IFDSSolver} as described
 * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public interface IFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> {

	/**
	 * Returns a set of flow functions. Those functions are used to compute data-flow facts
	 * along the various kinds of control flows.
     *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	FlowFunctions<N,D,M> flowFunctions();
	
	/**
	 * Returns the interprocedural control-flow graph which this problem is computed over.
	 * 
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	I interproceduralCFG();
	
	/**
	 * Returns initial seeds to be used for the analysis. (a set of statements)
	 */
	Set<N> initialSeeds();
	
	/**
	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>
	 * be part of the domain of data-flow facts. Typically this will be a
	 * singleton object of type {@link D} that is used for nothing else.
	 * It must holds that this object does not equals any object 
	 * within the domain.
	 *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	D zeroValue();









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






64




65




66




67




68




	
	/**
	 * If true, the analysis will compute a partially unbalanced analysis problem in which
	 * function returns are followed also further up the call stack than where the initial seeds
	 * started.









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






69




70




71




72




	 * 
	 * If this is enabled, when reaching the exit of a method that is <i>nowhere</i> called, in order
	 * to avoid not at all processing the exit statement, the {@link IDESolver} will call
	 * the <i>normal</i> flow function with both <i>curr</i> and <i>succ</i> set to the exit node.









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






73




74




	 */
	boolean followReturnsPastSeeds();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






75




}











Open sidebar



Joshua Garcia heros

600527394453cccf4607d618130727017360f8ca







Open sidebar



Joshua Garcia heros

600527394453cccf4607d618130727017360f8ca




Open sidebar

Joshua Garcia heros

600527394453cccf4607d618130727017360f8ca


Joshua Garciaherosheros
600527394453cccf4607d618130727017360f8ca










600527394453cccf4607d618130727017360f8ca


Switch branch/tag










heros


src


heros


IFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






IFDSTabulationProblem.java



2.84 KB









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




package heros;










improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






13




import heros.solver.IDESolver;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






14




import heros.solver.IFDSSolver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






15




16




17





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






18














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A tabulation problem for solving in an {@link IFDSSolver} as described
 * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public interface IFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> {

	/**
	 * Returns a set of flow functions. Those functions are used to compute data-flow facts
	 * along the various kinds of control flows.
     *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	FlowFunctions<N,D,M> flowFunctions();
	
	/**
	 * Returns the interprocedural control-flow graph which this problem is computed over.
	 * 
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	I interproceduralCFG();
	
	/**
	 * Returns initial seeds to be used for the analysis. (a set of statements)
	 */
	Set<N> initialSeeds();
	
	/**
	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>
	 * be part of the domain of data-flow facts. Typically this will be a
	 * singleton object of type {@link D} that is used for nothing else.
	 * It must holds that this object does not equals any object 
	 * within the domain.
	 *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	D zeroValue();









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






64




65




66




67




68




	
	/**
	 * If true, the analysis will compute a partially unbalanced analysis problem in which
	 * function returns are followed also further up the call stack than where the initial seeds
	 * started.









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






69




70




71




72




	 * 
	 * If this is enabled, when reaching the exit of a method that is <i>nowhere</i> called, in order
	 * to avoid not at all processing the exit statement, the {@link IDESolver} will call
	 * the <i>normal</i> flow function with both <i>curr</i> and <i>succ</i> set to the exit node.









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






73




74




	 */
	boolean followReturnsPastSeeds();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






75




}














600527394453cccf4607d618130727017360f8ca


Switch branch/tag










heros


src


heros


IFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






IFDSTabulationProblem.java



2.84 KB









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




package heros;










improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






13




import heros.solver.IDESolver;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






14




import heros.solver.IFDSSolver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






15




16




17





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






18














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A tabulation problem for solving in an {@link IFDSSolver} as described
 * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public interface IFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> {

	/**
	 * Returns a set of flow functions. Those functions are used to compute data-flow facts
	 * along the various kinds of control flows.
     *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	FlowFunctions<N,D,M> flowFunctions();
	
	/**
	 * Returns the interprocedural control-flow graph which this problem is computed over.
	 * 
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	I interproceduralCFG();
	
	/**
	 * Returns initial seeds to be used for the analysis. (a set of statements)
	 */
	Set<N> initialSeeds();
	
	/**
	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>
	 * be part of the domain of data-flow facts. Typically this will be a
	 * singleton object of type {@link D} that is used for nothing else.
	 * It must holds that this object does not equals any object 
	 * within the domain.
	 *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	D zeroValue();









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






64




65




66




67




68




	
	/**
	 * If true, the analysis will compute a partially unbalanced analysis problem in which
	 * function returns are followed also further up the call stack than where the initial seeds
	 * started.









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






69




70




71




72




	 * 
	 * If this is enabled, when reaching the exit of a method that is <i>nowhere</i> called, in order
	 * to avoid not at all processing the exit statement, the {@link IDESolver} will call
	 * the <i>normal</i> flow function with both <i>curr</i> and <i>succ</i> set to the exit node.









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






73




74




	 */
	boolean followReturnsPastSeeds();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






75




}










600527394453cccf4607d618130727017360f8ca


Switch branch/tag










heros


src


heros


IFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink




600527394453cccf4607d618130727017360f8ca


Switch branch/tag










heros


src


heros


IFDSTabulationProblem.java





600527394453cccf4607d618130727017360f8ca


Switch branch/tag








600527394453cccf4607d618130727017360f8ca


Switch branch/tag





600527394453cccf4607d618130727017360f8ca

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

IFDSTabulationProblem.java
Find file
Normal viewHistoryPermalink




IFDSTabulationProblem.java



2.84 KB









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




package heros;










improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






13




import heros.solver.IDESolver;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






14




import heros.solver.IFDSSolver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






15




16




17





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






18














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A tabulation problem for solving in an {@link IFDSSolver} as described
 * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public interface IFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> {

	/**
	 * Returns a set of flow functions. Those functions are used to compute data-flow facts
	 * along the various kinds of control flows.
     *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	FlowFunctions<N,D,M> flowFunctions();
	
	/**
	 * Returns the interprocedural control-flow graph which this problem is computed over.
	 * 
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	I interproceduralCFG();
	
	/**
	 * Returns initial seeds to be used for the analysis. (a set of statements)
	 */
	Set<N> initialSeeds();
	
	/**
	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>
	 * be part of the domain of data-flow facts. Typically this will be a
	 * singleton object of type {@link D} that is used for nothing else.
	 * It must holds that this object does not equals any object 
	 * within the domain.
	 *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	D zeroValue();









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






64




65




66




67




68




	
	/**
	 * If true, the analysis will compute a partially unbalanced analysis problem in which
	 * function returns are followed also further up the call stack than where the initial seeds
	 * started.









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






69




70




71




72




	 * 
	 * If this is enabled, when reaching the exit of a method that is <i>nowhere</i> called, in order
	 * to avoid not at all processing the exit statement, the {@link IDESolver} will call
	 * the <i>normal</i> flow function with both <i>curr</i> and <i>succ</i> set to the exit node.









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






73




74




	 */
	boolean followReturnsPastSeeds();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






75




}








IFDSTabulationProblem.java



2.84 KB










IFDSTabulationProblem.java



2.84 KB









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




package heros;










improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






13




import heros.solver.IDESolver;









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






14




import heros.solver.IFDSSolver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






15




16




17





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






18














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A tabulation problem for solving in an {@link IFDSSolver} as described
 * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public interface IFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> {

	/**
	 * Returns a set of flow functions. Those functions are used to compute data-flow facts
	 * along the various kinds of control flows.
     *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	FlowFunctions<N,D,M> flowFunctions();
	
	/**
	 * Returns the interprocedural control-flow graph which this problem is computed over.
	 * 
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	I interproceduralCFG();
	
	/**
	 * Returns initial seeds to be used for the analysis. (a set of statements)
	 */
	Set<N> initialSeeds();
	
	/**
	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>
	 * be part of the domain of data-flow facts. Typically this will be a
	 * singleton object of type {@link D} that is used for nothing else.
	 * It must holds that this object does not equals any object 
	 * within the domain.
	 *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	D zeroValue();









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






64




65




66




67




68




	
	/**
	 * If true, the analysis will compute a partially unbalanced analysis problem in which
	 * function returns are followed also further up the call stack than where the initial seeds
	 * started.









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






69




70




71




72




	 * 
	 * If this is enabled, when reaching the exit of a method that is <i>nowhere</i> called, in order
	 * to avoid not at all processing the exit statement, the {@link IDESolver} will call
	 * the <i>normal</i> flow function with both <i>curr</i> and <i>succ</i> set to the exit node.









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






73




74




	 */
	boolean followReturnsPastSeeds();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






75




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
package heros;packageheros;



improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012



improved handling of unbalanced problems


 

 

improved handling of unbalanced problems

 

Eric Bodden
committed
Dec 17, 2012

13
import heros.solver.IDESolver;importheros.solver.IDESolver;



renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

14
import heros.solver.IFDSSolver;importheros.solver.IFDSSolver;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

15

16

17
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

18




initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
/**/** * A tabulation problem for solving in an {@link IFDSSolver} as described * A tabulation problem for solving in an {@link IFDSSolver} as described * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper. * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper. * * * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}. * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}. * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}. * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}. * @param <I> The type of inter-procedural control-flow graph being used. * @param <I> The type of inter-procedural control-flow graph being used. */ */public interface IFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> {publicinterfaceIFDSTabulationProblem<N,D,M,IextendsInterproceduralCFG<N,M>>{	/**/**	 * Returns a set of flow functions. Those functions are used to compute data-flow facts	 * Returns a set of flow functions. Those functions are used to compute data-flow facts	 * along the various kinds of control flows.	 * along the various kinds of control flows.     *     *	 * <b>NOTE:</b> this method could be called many times. Implementations of this	 * <b>NOTE:</b> this method could be called many times. Implementations of this	 * interface should therefore cache the return value! 	 * interface should therefore cache the return value! 	 */	 */	FlowFunctions<N,D,M> flowFunctions();FlowFunctions<N,D,M>flowFunctions();		/**/**	 * Returns the interprocedural control-flow graph which this problem is computed over.	 * Returns the interprocedural control-flow graph which this problem is computed over.	 * 	 * 	 * <b>NOTE:</b> this method could be called many times. Implementations of this	 * <b>NOTE:</b> this method could be called many times. Implementations of this	 * interface should therefore cache the return value! 	 * interface should therefore cache the return value! 	 */	 */	I interproceduralCFG();IinterproceduralCFG();		/**/**	 * Returns initial seeds to be used for the analysis. (a set of statements)	 * Returns initial seeds to be used for the analysis. (a set of statements)	 */	 */	Set<N> initialSeeds();Set<N>initialSeeds();		/**/**	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>	 * be part of the domain of data-flow facts. Typically this will be a	 * be part of the domain of data-flow facts. Typically this will be a	 * singleton object of type {@link D} that is used for nothing else.	 * singleton object of type {@link D} that is used for nothing else.	 * It must holds that this object does not equals any object 	 * It must holds that this object does not equals any object 	 * within the domain.	 * within the domain.	 *	 *	 * <b>NOTE:</b> this method could be called many times. Implementations of this	 * <b>NOTE:</b> this method could be called many times. Implementations of this	 * interface should therefore cache the return value! 	 * interface should therefore cache the return value! 	 */	 */	D zeroValue();DzeroValue();



making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012



making computation of unbalanced edges optional


 

 

making computation of unbalanced edges optional

 

Eric Bodden
committed
Dec 12, 2012

64

65

66

67

68
		/**/**	 * If true, the analysis will compute a partially unbalanced analysis problem in which	 * If true, the analysis will compute a partially unbalanced analysis problem in which	 * function returns are followed also further up the call stack than where the initial seeds	 * function returns are followed also further up the call stack than where the initial seeds	 * started.	 * started.



improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012



improved handling of unbalanced problems


 

 

improved handling of unbalanced problems

 

Eric Bodden
committed
Dec 17, 2012

69

70

71

72
	 * 	 * 	 * If this is enabled, when reaching the exit of a method that is <i>nowhere</i> called, in order	 * If this is enabled, when reaching the exit of a method that is <i>nowhere</i> called, in order	 * to avoid not at all processing the exit statement, the {@link IDESolver} will call	 * to avoid not at all processing the exit statement, the {@link IDESolver} will call	 * the <i>normal</i> flow function with both <i>curr</i> and <i>succ</i> set to the exit node.	 * the <i>normal</i> flow function with both <i>curr</i> and <i>succ</i> set to the exit node.



making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012



making computation of unbalanced edges optional


 

 

making computation of unbalanced edges optional

 

Eric Bodden
committed
Dec 12, 2012

73

74
	 */	 */	boolean followReturnsPastSeeds();booleanfollowReturnsPastSeeds();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

75
}}





