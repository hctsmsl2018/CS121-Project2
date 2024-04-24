



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

b9cdabaae9588fd8e406e2141a065226bbbb0d06

















b9cdabaae9588fd8e406e2141a065226bbbb0d06


Switch branch/tag










heros


src


heros


fieldsens


FlowFunctions.java



Find file
Normal viewHistoryPermalink






FlowFunctions.java



4.58 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
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

b9cdabaae9588fd8e406e2141a065226bbbb0d06

















b9cdabaae9588fd8e406e2141a065226bbbb0d06


Switch branch/tag










heros


src


heros


fieldsens


FlowFunctions.java



Find file
Normal viewHistoryPermalink






FlowFunctions.java



4.58 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
}












Open sidebar



Joshua Garcia heros

b9cdabaae9588fd8e406e2141a065226bbbb0d06







Open sidebar



Joshua Garcia heros

b9cdabaae9588fd8e406e2141a065226bbbb0d06




Open sidebar

Joshua Garcia heros

b9cdabaae9588fd8e406e2141a065226bbbb0d06


Joshua Garciaherosheros
b9cdabaae9588fd8e406e2141a065226bbbb0d06










b9cdabaae9588fd8e406e2141a065226bbbb0d06


Switch branch/tag










heros


src


heros


fieldsens


FlowFunctions.java



Find file
Normal viewHistoryPermalink






FlowFunctions.java



4.58 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
}















b9cdabaae9588fd8e406e2141a065226bbbb0d06


Switch branch/tag










heros


src


heros


fieldsens


FlowFunctions.java



Find file
Normal viewHistoryPermalink






FlowFunctions.java



4.58 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
}











b9cdabaae9588fd8e406e2141a065226bbbb0d06


Switch branch/tag










heros


src


heros


fieldsens


FlowFunctions.java



Find file
Normal viewHistoryPermalink




b9cdabaae9588fd8e406e2141a065226bbbb0d06


Switch branch/tag










heros


src


heros


fieldsens


FlowFunctions.java





b9cdabaae9588fd8e406e2141a065226bbbb0d06


Switch branch/tag








b9cdabaae9588fd8e406e2141a065226bbbb0d06


Switch branch/tag





b9cdabaae9588fd8e406e2141a065226bbbb0d06

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

fieldsens

FlowFunctions.java
Find file
Normal viewHistoryPermalink




FlowFunctions.java



4.58 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
}









FlowFunctions.java



4.58 KB










FlowFunctions.java



4.58 KB









Newer










Older
NewerOlder







fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
}











fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
}









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
}







fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************






fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015



fixed characters that made the build server fail

 

fixed characters that made the build server fail

Steven Arzt
committed
Jun 03, 2015


1


﻿/*******************************************************************************

﻿/*******************************************************************************﻿/*******************************************************************************




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




2


3


4


5


6


7


8


9



 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


2


3


4


5


6


7


8


9


 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation

 * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation




fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




10



 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity






fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015



fixed characters that made the build server fail

 

fixed characters that made the build server fail

Steven Arzt
committed
Jun 03, 2015


10


 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity

 *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity *     Johannes Lerch, Johannes Spaeth - extension for field sensitivity




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




11



 ******************************************************************************/






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


11


 ******************************************************************************/

 ******************************************************************************/ ******************************************************************************/




renaming package

 


Johannes Lerch
committed
Jun 01, 2015




12



package heros.fieldsens;






renaming package

 


Johannes Lerch
committed
Jun 01, 2015



renaming package

 

renaming package

Johannes Lerch
committed
Jun 01, 2015


12


package heros.fieldsens;

package heros.fieldsens;packageheros.fieldsens;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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





/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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




/**
 * Classes implementing this interface provide a factory for a 
 * range of flow functions used to compute which D-type values
 * are reachable along the program's control flow.
 * 
 * @param <Stmt>
 *            The type of nodes in the interprocedural control-flow graph.
 *            Typically {@link Unit}.
 * @param <F>
 *            The type of data-flow facts to be computed by the tabulation
 *            problem.
 * @param <Method>
 *            The type of objects used to represent methods. Typically
 *            {@link SootMethod}.
 */

/**/** * Classes implementing this interface provide a factory for a  * Classes implementing this interface provide a factory for a  * range of flow functions used to compute which D-type values * range of flow functions used to compute which D-type values * are reachable along the program's control flow. * are reachable along the program's control flow. *  *  * @param <Stmt> * @param <Stmt> *            The type of nodes in the interprocedural control-flow graph. *            The type of nodes in the interprocedural control-flow graph. *            Typically {@link Unit}. *            Typically {@link Unit}. * @param <F> * @param <F> *            The type of data-flow facts to be computed by the tabulation *            The type of data-flow facts to be computed by the tabulation *            problem. *            problem. * @param <Method> * @param <Method> *            The type of objects used to represent methods. Typically *            The type of objects used to represent methods. Typically *            {@link SootMethod}. *            {@link SootMethod}. */ */




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




30



public interface FlowFunctions<Stmt, FieldRef, F, Method> {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


30


public interface FlowFunctions<Stmt, FieldRef, F, Method> {

public interface FlowFunctions<Stmt, FieldRef, F, Method> {publicinterfaceFlowFunctions<Stmt,FieldRef,F,Method>{




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31


32


33


34


35


36


37


38




	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


31


32


33


34


35


36


37


38



	/**
	 * Returns the flow function that computes the flow for a normal statement,
	 * i.e., a statement that is neither a call nor an exit statement.
	 * 
	 * @param curr
	 *            The current statement.
	 */

	/**/**	 * Returns the flow function that computes the flow for a normal statement,	 * Returns the flow function that computes the flow for a normal statement,	 * i.e., a statement that is neither a call nor an exit statement.	 * i.e., a statement that is neither a call nor an exit statement.	 * 	 * 	 * @param curr	 * @param curr	 *            The current statement.	 *            The current statement.	 */	 */




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


39


	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);

	public FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt curr);publicFlowFunction<FieldRef,F,Stmt,Method>getNormalFlowFunction(Stmtcurr);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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



	
	/**
	 * Returns the flow function that computes the flow for a call statement.
	 * 
	 * @param callStmt
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param destinationMethod
	 *            The concrete target method for which the flow is computed.
	 */

		/**/**	 * Returns the flow function that computes the flow for a call statement.	 * Returns the flow function that computes the flow for a call statement.	 * 	 * 	 * @param callStmt	 * @param callStmt	 *            The statement containing the invoke expression giving rise to	 *            The statement containing the invoke expression giving rise to	 *            this call.	 *            this call.	 * @param destinationMethod	 * @param destinationMethod	 *            The concrete target method for which the flow is computed.	 *            The concrete target method for which the flow is computed.	 */	 */




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




51



	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


51


	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);

	public FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt callStmt, Method destinationMethod);publicFlowFunction<FieldRef,F,Stmt,Method>getCallFlowFunction(StmtcallStmt,MethoddestinationMethod);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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



	/**
	 * Returns the flow function that computes the flow for a an exit from a
	 * method. An exit can be a return or an exceptional exit.
	 * 
	 * @param callSite
	 *            One of all the call sites in the program that called the
	 *            method from which the exitStmt is actually returning. This
	 *            information can be exploited to compute a value that depends on
	 *            information from before the call.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @param calleeMethod
	 *            The method from which exitStmt returns.
	 * @param exitStmt
	 *            The statement exiting the method, typically a return or throw
	 *            statement.
	 * @param returnSite
	 *            One of the successor statements of the callSite. There may be
	 *            multiple successors in case of possible exceptional flow. This
	 *            method will be called for each such successor.
	 *            <b>Note:</b> This value might be <code>null</code> if
	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}
	 *            returning <code>true</code> in a situation where the call graph
	 *            does not contain a caller for the method that is returned from.
	 * @return
	 */

	/**/**	 * Returns the flow function that computes the flow for a an exit from a	 * Returns the flow function that computes the flow for a an exit from a	 * method. An exit can be a return or an exceptional exit.	 * method. An exit can be a return or an exceptional exit.	 * 	 * 	 * @param callSite	 * @param callSite	 *            One of all the call sites in the program that called the	 *            One of all the call sites in the program that called the	 *            method from which the exitStmt is actually returning. This	 *            method from which the exitStmt is actually returning. This	 *            information can be exploited to compute a value that depends on	 *            information can be exploited to compute a value that depends on	 *            information from before the call.	 *            information from before the call.	 *            <b>Note:</b> This value might be <code>null</code> if	 *            <b>Note:</b> This value might be <code>null</code> if	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}	 *            returning <code>true</code> in a situation where the call graph	 *            returning <code>true</code> in a situation where the call graph	 *            does not contain a caller for the method that is returned from.	 *            does not contain a caller for the method that is returned from.	 * @param calleeMethod	 * @param calleeMethod	 *            The method from which exitStmt returns.	 *            The method from which exitStmt returns.	 * @param exitStmt	 * @param exitStmt	 *            The statement exiting the method, typically a return or throw	 *            The statement exiting the method, typically a return or throw	 *            statement.	 *            statement.	 * @param returnSite	 * @param returnSite	 *            One of the successor statements of the callSite. There may be	 *            One of the successor statements of the callSite. There may be	 *            multiple successors in case of possible exceptional flow. This	 *            multiple successors in case of possible exceptional flow. This	 *            method will be called for each such successor.	 *            method will be called for each such successor.	 *            <b>Note:</b> This value might be <code>null</code> if	 *            <b>Note:</b> This value might be <code>null</code> if	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}	 *            using a tabulation problem with {@link IFDSTabulationProblem#followReturnsPastSeeds()}	 *            returning <code>true</code> in a situation where the call graph	 *            returning <code>true</code> in a situation where the call graph	 *            does not contain a caller for the method that is returned from.	 *            does not contain a caller for the method that is returned from.	 * @return	 * @return	 */	 */




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81



	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


81


	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);

	public FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt callSite, Method calleeMethod, Stmt exitStmt, Stmt returnSite);publicFlowFunction<FieldRef,F,Stmt,Method>getReturnFlowFunction(StmtcallSite,MethodcalleeMethod,StmtexitStmt,StmtreturnSite);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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




	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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



	/**
	 * Returns the flow function that computes the flow from a call site to a
	 * successor statement just after the call. There may be multiple successors
	 * in case of exceptional control flow. In this case this method will be
	 * called for every such successor. Typically, one will propagate into a
	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only
	 * such information that actually concerns the callee method. All other
	 * information, e.g. information that cannot be modified by the call, is
	 * passed along this call-return edge.
	 * 
	 * @param callSite
	 *            The statement containing the invoke expression giving rise to
	 *            this call.
	 * @param returnSite
	 *            The return site to which the information is propagated. For
	 *            exceptional flow, this may actually be the start of an
	 *            exception handler.
	 */

	/**/**	 * Returns the flow function that computes the flow from a call site to a	 * Returns the flow function that computes the flow from a call site to a	 * successor statement just after the call. There may be multiple successors	 * successor statement just after the call. There may be multiple successors	 * in case of exceptional control flow. In this case this method will be	 * in case of exceptional control flow. In this case this method will be	 * called for every such successor. Typically, one will propagate into a	 * called for every such successor. Typically, one will propagate into a	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only	 * method call, using {@link #getCallFlowFunction(Object, Object)}, only	 * such information that actually concerns the callee method. All other	 * such information that actually concerns the callee method. All other	 * information, e.g. information that cannot be modified by the call, is	 * information, e.g. information that cannot be modified by the call, is	 * passed along this call-return edge.	 * passed along this call-return edge.	 * 	 * 	 * @param callSite	 * @param callSite	 *            The statement containing the invoke expression giving rise to	 *            The statement containing the invoke expression giving rise to	 *            this call.	 *            this call.	 * @param returnSite	 * @param returnSite	 *            The return site to which the information is propagated. For	 *            The return site to which the information is propagated. For	 *            exceptional flow, this may actually be the start of an	 *            exceptional flow, this may actually be the start of an	 *            exception handler.	 *            exception handler.	 */	 */




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101



	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


101


	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);

	public FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt callSite, Stmt returnSite);publicFlowFunction<FieldRef,F,Stmt,Method>getCallToReturnFlowFunction(StmtcallSite,StmtreturnSite);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




102


103


104


105




	
	
}





FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


102


103


104


105



	
	
}
		}}





