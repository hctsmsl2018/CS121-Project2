



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

a81af76a1c153f625068e7a9622a111ae8fb1911

















a81af76a1c153f625068e7a9622a111ae8fb1911


Switch branch/tag










heros


src


heros


InterproceduralCFG.java



Find file
Normal viewHistoryPermalink






InterproceduralCFG.java



2.76 KB









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




package heros;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.List;
import java.util.Set;

/**
 * An interprocedural control-flow graph.
 * 
 * @param <N> Nodes in the CFG, typically {@link Unit} or {@link Block}
 * @param <M> Method representation
 */
public interface InterproceduralCFG<N,M>  {
	
	/**
	 * Returns the method containing a node.
	 * @param n The node for which to get the parent method
	 */
	public M getMethodOf(N n);

	/**
	 * Returns the successor nodes.
	 */
	public List<N> getSuccsOf(N n);










Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )


 

 


Steven Arzt
committed
Feb 25, 2013






35




36




37




38




39




	/**
	 * Returns the predecessor nodes.
	 */
	public List<N> getPredsOf(N n);










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




64




65




66




67




68




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




79




80




81




82




83




84




85




86




87




88




89




90




91




92




93




94




95




96




97




98




99




100




101




102




103




	/**
	 * Returns all callee methods for a given call.
	 */
	public Set<M> getCalleesOfCallAt(N n);

	/**
	 * Returns all caller statements/nodes of a given method.
	 */
	public Set<N> getCallersOf(M m);

	/**
	 * Returns all call sites within a given method.
	 */
	public Set<N> getCallsFromWithin(M m);

	/**
	 * Returns all start points of a given method. There may be
	 * more than one start point in case of a backward analysis.
	 */
	public Set<N> getStartPointsOf(M m);

	/**
	 * Returns all statements to which a call could return.
	 * In the RHS paper, for every call there is just one return site.
	 * We, however, use as return site the successor statements, of which
	 * there can be many in case of exceptional flow.
	 */
	public List<N> getReturnSitesOfCallAt(N n);

	/**
	 * Returns <code>true</code> if the given statement is a call site.
	 */
	public boolean isCallStmt(N stmt);

	/**
	 * Returns <code>true</code> if the given statement leads to a method return
	 * (exceptional or not). For backward analyses may also be start statements.
	 */
	public boolean isExitStmt(N stmt);
	
	/**
	 * Returns true is this is a method's start statement. For backward analyses
	 * those may also be return or throws statements.
	 */
	public boolean isStartPoint(N stmt);
	
	/**
	 * Returns the set of all nodes that are neither call nor start nodes.
	 */
	public Set<N> allNonCallStartNodes();
	
	/**
	 * Returns whether succ is the fall-through successor of stmt,
	 * i.e., the unique successor that is be reached when stmt
	 * does not branch.
	 */
	public boolean isFallThroughSuccessor(N stmt, N succ);
	
	/**
	 * Returns whether succ is a branch target of stmt. 
	 */
	public boolean isBranchTarget(N stmt, N succ);

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

a81af76a1c153f625068e7a9622a111ae8fb1911

















a81af76a1c153f625068e7a9622a111ae8fb1911


Switch branch/tag










heros


src


heros


InterproceduralCFG.java



Find file
Normal viewHistoryPermalink






InterproceduralCFG.java



2.76 KB









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




package heros;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.List;
import java.util.Set;

/**
 * An interprocedural control-flow graph.
 * 
 * @param <N> Nodes in the CFG, typically {@link Unit} or {@link Block}
 * @param <M> Method representation
 */
public interface InterproceduralCFG<N,M>  {
	
	/**
	 * Returns the method containing a node.
	 * @param n The node for which to get the parent method
	 */
	public M getMethodOf(N n);

	/**
	 * Returns the successor nodes.
	 */
	public List<N> getSuccsOf(N n);










Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )


 

 


Steven Arzt
committed
Feb 25, 2013






35




36




37




38




39




	/**
	 * Returns the predecessor nodes.
	 */
	public List<N> getPredsOf(N n);










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




64




65




66




67




68




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




79




80




81




82




83




84




85




86




87




88




89




90




91




92




93




94




95




96




97




98




99




100




101




102




103




	/**
	 * Returns all callee methods for a given call.
	 */
	public Set<M> getCalleesOfCallAt(N n);

	/**
	 * Returns all caller statements/nodes of a given method.
	 */
	public Set<N> getCallersOf(M m);

	/**
	 * Returns all call sites within a given method.
	 */
	public Set<N> getCallsFromWithin(M m);

	/**
	 * Returns all start points of a given method. There may be
	 * more than one start point in case of a backward analysis.
	 */
	public Set<N> getStartPointsOf(M m);

	/**
	 * Returns all statements to which a call could return.
	 * In the RHS paper, for every call there is just one return site.
	 * We, however, use as return site the successor statements, of which
	 * there can be many in case of exceptional flow.
	 */
	public List<N> getReturnSitesOfCallAt(N n);

	/**
	 * Returns <code>true</code> if the given statement is a call site.
	 */
	public boolean isCallStmt(N stmt);

	/**
	 * Returns <code>true</code> if the given statement leads to a method return
	 * (exceptional or not). For backward analyses may also be start statements.
	 */
	public boolean isExitStmt(N stmt);
	
	/**
	 * Returns true is this is a method's start statement. For backward analyses
	 * those may also be return or throws statements.
	 */
	public boolean isStartPoint(N stmt);
	
	/**
	 * Returns the set of all nodes that are neither call nor start nodes.
	 */
	public Set<N> allNonCallStartNodes();
	
	/**
	 * Returns whether succ is the fall-through successor of stmt,
	 * i.e., the unique successor that is be reached when stmt
	 * does not branch.
	 */
	public boolean isFallThroughSuccessor(N stmt, N succ);
	
	/**
	 * Returns whether succ is a branch target of stmt. 
	 */
	public boolean isBranchTarget(N stmt, N succ);

}











Open sidebar



Joshua Garcia heros

a81af76a1c153f625068e7a9622a111ae8fb1911







Open sidebar



Joshua Garcia heros

a81af76a1c153f625068e7a9622a111ae8fb1911




Open sidebar

Joshua Garcia heros

a81af76a1c153f625068e7a9622a111ae8fb1911


Joshua Garciaherosheros
a81af76a1c153f625068e7a9622a111ae8fb1911










a81af76a1c153f625068e7a9622a111ae8fb1911


Switch branch/tag










heros


src


heros


InterproceduralCFG.java



Find file
Normal viewHistoryPermalink






InterproceduralCFG.java



2.76 KB









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




package heros;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.List;
import java.util.Set;

/**
 * An interprocedural control-flow graph.
 * 
 * @param <N> Nodes in the CFG, typically {@link Unit} or {@link Block}
 * @param <M> Method representation
 */
public interface InterproceduralCFG<N,M>  {
	
	/**
	 * Returns the method containing a node.
	 * @param n The node for which to get the parent method
	 */
	public M getMethodOf(N n);

	/**
	 * Returns the successor nodes.
	 */
	public List<N> getSuccsOf(N n);










Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )


 

 


Steven Arzt
committed
Feb 25, 2013






35




36




37




38




39




	/**
	 * Returns the predecessor nodes.
	 */
	public List<N> getPredsOf(N n);










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




64




65




66




67




68




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




79




80




81




82




83




84




85




86




87




88




89




90




91




92




93




94




95




96




97




98




99




100




101




102




103




	/**
	 * Returns all callee methods for a given call.
	 */
	public Set<M> getCalleesOfCallAt(N n);

	/**
	 * Returns all caller statements/nodes of a given method.
	 */
	public Set<N> getCallersOf(M m);

	/**
	 * Returns all call sites within a given method.
	 */
	public Set<N> getCallsFromWithin(M m);

	/**
	 * Returns all start points of a given method. There may be
	 * more than one start point in case of a backward analysis.
	 */
	public Set<N> getStartPointsOf(M m);

	/**
	 * Returns all statements to which a call could return.
	 * In the RHS paper, for every call there is just one return site.
	 * We, however, use as return site the successor statements, of which
	 * there can be many in case of exceptional flow.
	 */
	public List<N> getReturnSitesOfCallAt(N n);

	/**
	 * Returns <code>true</code> if the given statement is a call site.
	 */
	public boolean isCallStmt(N stmt);

	/**
	 * Returns <code>true</code> if the given statement leads to a method return
	 * (exceptional or not). For backward analyses may also be start statements.
	 */
	public boolean isExitStmt(N stmt);
	
	/**
	 * Returns true is this is a method's start statement. For backward analyses
	 * those may also be return or throws statements.
	 */
	public boolean isStartPoint(N stmt);
	
	/**
	 * Returns the set of all nodes that are neither call nor start nodes.
	 */
	public Set<N> allNonCallStartNodes();
	
	/**
	 * Returns whether succ is the fall-through successor of stmt,
	 * i.e., the unique successor that is be reached when stmt
	 * does not branch.
	 */
	public boolean isFallThroughSuccessor(N stmt, N succ);
	
	/**
	 * Returns whether succ is a branch target of stmt. 
	 */
	public boolean isBranchTarget(N stmt, N succ);

}














a81af76a1c153f625068e7a9622a111ae8fb1911


Switch branch/tag










heros


src


heros


InterproceduralCFG.java



Find file
Normal viewHistoryPermalink






InterproceduralCFG.java



2.76 KB









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




package heros;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.List;
import java.util.Set;

/**
 * An interprocedural control-flow graph.
 * 
 * @param <N> Nodes in the CFG, typically {@link Unit} or {@link Block}
 * @param <M> Method representation
 */
public interface InterproceduralCFG<N,M>  {
	
	/**
	 * Returns the method containing a node.
	 * @param n The node for which to get the parent method
	 */
	public M getMethodOf(N n);

	/**
	 * Returns the successor nodes.
	 */
	public List<N> getSuccsOf(N n);










Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )


 

 


Steven Arzt
committed
Feb 25, 2013






35




36




37




38




39




	/**
	 * Returns the predecessor nodes.
	 */
	public List<N> getPredsOf(N n);










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




64




65




66




67




68




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




79




80




81




82




83




84




85




86




87




88




89




90




91




92




93




94




95




96




97




98




99




100




101




102




103




	/**
	 * Returns all callee methods for a given call.
	 */
	public Set<M> getCalleesOfCallAt(N n);

	/**
	 * Returns all caller statements/nodes of a given method.
	 */
	public Set<N> getCallersOf(M m);

	/**
	 * Returns all call sites within a given method.
	 */
	public Set<N> getCallsFromWithin(M m);

	/**
	 * Returns all start points of a given method. There may be
	 * more than one start point in case of a backward analysis.
	 */
	public Set<N> getStartPointsOf(M m);

	/**
	 * Returns all statements to which a call could return.
	 * In the RHS paper, for every call there is just one return site.
	 * We, however, use as return site the successor statements, of which
	 * there can be many in case of exceptional flow.
	 */
	public List<N> getReturnSitesOfCallAt(N n);

	/**
	 * Returns <code>true</code> if the given statement is a call site.
	 */
	public boolean isCallStmt(N stmt);

	/**
	 * Returns <code>true</code> if the given statement leads to a method return
	 * (exceptional or not). For backward analyses may also be start statements.
	 */
	public boolean isExitStmt(N stmt);
	
	/**
	 * Returns true is this is a method's start statement. For backward analyses
	 * those may also be return or throws statements.
	 */
	public boolean isStartPoint(N stmt);
	
	/**
	 * Returns the set of all nodes that are neither call nor start nodes.
	 */
	public Set<N> allNonCallStartNodes();
	
	/**
	 * Returns whether succ is the fall-through successor of stmt,
	 * i.e., the unique successor that is be reached when stmt
	 * does not branch.
	 */
	public boolean isFallThroughSuccessor(N stmt, N succ);
	
	/**
	 * Returns whether succ is a branch target of stmt. 
	 */
	public boolean isBranchTarget(N stmt, N succ);

}










a81af76a1c153f625068e7a9622a111ae8fb1911


Switch branch/tag










heros


src


heros


InterproceduralCFG.java



Find file
Normal viewHistoryPermalink




a81af76a1c153f625068e7a9622a111ae8fb1911


Switch branch/tag










heros


src


heros


InterproceduralCFG.java





a81af76a1c153f625068e7a9622a111ae8fb1911


Switch branch/tag








a81af76a1c153f625068e7a9622a111ae8fb1911


Switch branch/tag





a81af76a1c153f625068e7a9622a111ae8fb1911

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

InterproceduralCFG.java
Find file
Normal viewHistoryPermalink




InterproceduralCFG.java



2.76 KB









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




package heros;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.List;
import java.util.Set;

/**
 * An interprocedural control-flow graph.
 * 
 * @param <N> Nodes in the CFG, typically {@link Unit} or {@link Block}
 * @param <M> Method representation
 */
public interface InterproceduralCFG<N,M>  {
	
	/**
	 * Returns the method containing a node.
	 * @param n The node for which to get the parent method
	 */
	public M getMethodOf(N n);

	/**
	 * Returns the successor nodes.
	 */
	public List<N> getSuccsOf(N n);










Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )


 

 


Steven Arzt
committed
Feb 25, 2013






35




36




37




38




39




	/**
	 * Returns the predecessor nodes.
	 */
	public List<N> getPredsOf(N n);










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




64




65




66




67




68




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




79




80




81




82




83




84




85




86




87




88




89




90




91




92




93




94




95




96




97




98




99




100




101




102




103




	/**
	 * Returns all callee methods for a given call.
	 */
	public Set<M> getCalleesOfCallAt(N n);

	/**
	 * Returns all caller statements/nodes of a given method.
	 */
	public Set<N> getCallersOf(M m);

	/**
	 * Returns all call sites within a given method.
	 */
	public Set<N> getCallsFromWithin(M m);

	/**
	 * Returns all start points of a given method. There may be
	 * more than one start point in case of a backward analysis.
	 */
	public Set<N> getStartPointsOf(M m);

	/**
	 * Returns all statements to which a call could return.
	 * In the RHS paper, for every call there is just one return site.
	 * We, however, use as return site the successor statements, of which
	 * there can be many in case of exceptional flow.
	 */
	public List<N> getReturnSitesOfCallAt(N n);

	/**
	 * Returns <code>true</code> if the given statement is a call site.
	 */
	public boolean isCallStmt(N stmt);

	/**
	 * Returns <code>true</code> if the given statement leads to a method return
	 * (exceptional or not). For backward analyses may also be start statements.
	 */
	public boolean isExitStmt(N stmt);
	
	/**
	 * Returns true is this is a method's start statement. For backward analyses
	 * those may also be return or throws statements.
	 */
	public boolean isStartPoint(N stmt);
	
	/**
	 * Returns the set of all nodes that are neither call nor start nodes.
	 */
	public Set<N> allNonCallStartNodes();
	
	/**
	 * Returns whether succ is the fall-through successor of stmt,
	 * i.e., the unique successor that is be reached when stmt
	 * does not branch.
	 */
	public boolean isFallThroughSuccessor(N stmt, N succ);
	
	/**
	 * Returns whether succ is a branch target of stmt. 
	 */
	public boolean isBranchTarget(N stmt, N succ);

}








InterproceduralCFG.java



2.76 KB










InterproceduralCFG.java



2.76 KB









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




package heros;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.List;
import java.util.Set;

/**
 * An interprocedural control-flow graph.
 * 
 * @param <N> Nodes in the CFG, typically {@link Unit} or {@link Block}
 * @param <M> Method representation
 */
public interface InterproceduralCFG<N,M>  {
	
	/**
	 * Returns the method containing a node.
	 * @param n The node for which to get the parent method
	 */
	public M getMethodOf(N n);

	/**
	 * Returns the successor nodes.
	 */
	public List<N> getSuccsOf(N n);










Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )


 

 


Steven Arzt
committed
Feb 25, 2013






35




36




37




38




39




	/**
	 * Returns the predecessor nodes.
	 */
	public List<N> getPredsOf(N n);










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




64




65




66




67




68




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




79




80




81




82




83




84




85




86




87




88




89




90




91




92




93




94




95




96




97




98




99




100




101




102




103




	/**
	 * Returns all callee methods for a given call.
	 */
	public Set<M> getCalleesOfCallAt(N n);

	/**
	 * Returns all caller statements/nodes of a given method.
	 */
	public Set<N> getCallersOf(M m);

	/**
	 * Returns all call sites within a given method.
	 */
	public Set<N> getCallsFromWithin(M m);

	/**
	 * Returns all start points of a given method. There may be
	 * more than one start point in case of a backward analysis.
	 */
	public Set<N> getStartPointsOf(M m);

	/**
	 * Returns all statements to which a call could return.
	 * In the RHS paper, for every call there is just one return site.
	 * We, however, use as return site the successor statements, of which
	 * there can be many in case of exceptional flow.
	 */
	public List<N> getReturnSitesOfCallAt(N n);

	/**
	 * Returns <code>true</code> if the given statement is a call site.
	 */
	public boolean isCallStmt(N stmt);

	/**
	 * Returns <code>true</code> if the given statement leads to a method return
	 * (exceptional or not). For backward analyses may also be start statements.
	 */
	public boolean isExitStmt(N stmt);
	
	/**
	 * Returns true is this is a method's start statement. For backward analyses
	 * those may also be return or throws statements.
	 */
	public boolean isStartPoint(N stmt);
	
	/**
	 * Returns the set of all nodes that are neither call nor start nodes.
	 */
	public Set<N> allNonCallStartNodes();
	
	/**
	 * Returns whether succ is the fall-through successor of stmt,
	 * i.e., the unique successor that is be reached when stmt
	 * does not branch.
	 */
	public boolean isFallThroughSuccessor(N stmt, N succ);
	
	/**
	 * Returns whether succ is a branch target of stmt. 
	 */
	public boolean isBranchTarget(N stmt, N succ);

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
package heros;packageheros;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
import java.util.List;importjava.util.List;import java.util.Set;importjava.util.Set;/**/** * An interprocedural control-flow graph. * An interprocedural control-flow graph. *  *  * @param <N> Nodes in the CFG, typically {@link Unit} or {@link Block} * @param <N> Nodes in the CFG, typically {@link Unit} or {@link Block} * @param <M> Method representation * @param <M> Method representation */ */public interface InterproceduralCFG<N,M>  {publicinterfaceInterproceduralCFG<N,M>{		/**/**	 * Returns the method containing a node.	 * Returns the method containing a node.	 * @param n The node for which to get the parent method	 * @param n The node for which to get the parent method	 */	 */	public M getMethodOf(N n);publicMgetMethodOf(Nn);	/**/**	 * Returns the successor nodes.	 * Returns the successor nodes.	 */	 */	public List<N> getSuccsOf(N n);publicList<N>getSuccsOf(Nn);



Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )


 

 


Steven Arzt
committed
Feb 25, 2013



Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )


 

 

Added a getPredsOf() to the cfg ( similarly to the existing getSuccsOf() )

 

Steven Arzt
committed
Feb 25, 2013

35

36

37

38

39
	/**/**	 * Returns the predecessor nodes.	 * Returns the predecessor nodes.	 */	 */	public List<N> getPredsOf(N n);publicList<N>getPredsOf(Nn);



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

64

65

66

67

68

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

79

80

81

82

83

84

85

86

87

88

89

90

91

92

93

94

95

96

97

98

99

100

101

102

103
	/**/**	 * Returns all callee methods for a given call.	 * Returns all callee methods for a given call.	 */	 */	public Set<M> getCalleesOfCallAt(N n);publicSet<M>getCalleesOfCallAt(Nn);	/**/**	 * Returns all caller statements/nodes of a given method.	 * Returns all caller statements/nodes of a given method.	 */	 */	public Set<N> getCallersOf(M m);publicSet<N>getCallersOf(Mm);	/**/**	 * Returns all call sites within a given method.	 * Returns all call sites within a given method.	 */	 */	public Set<N> getCallsFromWithin(M m);publicSet<N>getCallsFromWithin(Mm);	/**/**	 * Returns all start points of a given method. There may be	 * Returns all start points of a given method. There may be	 * more than one start point in case of a backward analysis.	 * more than one start point in case of a backward analysis.	 */	 */	public Set<N> getStartPointsOf(M m);publicSet<N>getStartPointsOf(Mm);	/**/**	 * Returns all statements to which a call could return.	 * Returns all statements to which a call could return.	 * In the RHS paper, for every call there is just one return site.	 * In the RHS paper, for every call there is just one return site.	 * We, however, use as return site the successor statements, of which	 * We, however, use as return site the successor statements, of which	 * there can be many in case of exceptional flow.	 * there can be many in case of exceptional flow.	 */	 */	public List<N> getReturnSitesOfCallAt(N n);publicList<N>getReturnSitesOfCallAt(Nn);	/**/**	 * Returns <code>true</code> if the given statement is a call site.	 * Returns <code>true</code> if the given statement is a call site.	 */	 */	public boolean isCallStmt(N stmt);publicbooleanisCallStmt(Nstmt);	/**/**	 * Returns <code>true</code> if the given statement leads to a method return	 * Returns <code>true</code> if the given statement leads to a method return	 * (exceptional or not). For backward analyses may also be start statements.	 * (exceptional or not). For backward analyses may also be start statements.	 */	 */	public boolean isExitStmt(N stmt);publicbooleanisExitStmt(Nstmt);		/**/**	 * Returns true is this is a method's start statement. For backward analyses	 * Returns true is this is a method's start statement. For backward analyses	 * those may also be return or throws statements.	 * those may also be return or throws statements.	 */	 */	public boolean isStartPoint(N stmt);publicbooleanisStartPoint(Nstmt);		/**/**	 * Returns the set of all nodes that are neither call nor start nodes.	 * Returns the set of all nodes that are neither call nor start nodes.	 */	 */	public Set<N> allNonCallStartNodes();publicSet<N>allNonCallStartNodes();		/**/**	 * Returns whether succ is the fall-through successor of stmt,	 * Returns whether succ is the fall-through successor of stmt,	 * i.e., the unique successor that is be reached when stmt	 * i.e., the unique successor that is be reached when stmt	 * does not branch.	 * does not branch.	 */	 */	public boolean isFallThroughSuccessor(N stmt, N succ);publicbooleanisFallThroughSuccessor(Nstmt,Nsucc);		/**/**	 * Returns whether succ is a branch target of stmt. 	 * Returns whether succ is a branch target of stmt. 	 */	 */	public boolean isBranchTarget(N stmt, N succ);publicbooleanisBranchTarget(Nstmt,Nsucc);}}





