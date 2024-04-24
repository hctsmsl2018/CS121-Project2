



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

15b0a59b9024d849291bd3f9ff169d878b08de9d

















15b0a59b9024d849291bd3f9ff169d878b08de9d


Switch branch/tag










heros


src


heros


solver


IFDSSolver.java



Find file
Normal viewHistoryPermalink






IFDSSolver.java



4.45 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import static heros.solver.IFDSSolver.BinaryDomain.BOTTOM;
import static heros.solver.IFDSSolver.BinaryDomain.TOP;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.edgefunc.AllBottom;
import heros.edgefunc.AllTop;
import heros.edgefunc.EdgeIdentity;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






26




27




28





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver}
 * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem.
 * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE
 * problem in which the environments (D to N mappings) represent the set's characteristic function.
 * 
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 * @see IFDSTabulationProblem
 */
public class IFDSSolver<N,D,M,I extends InterproceduralCFG<N, M>> extends IDESolver<N,D,M,IFDSSolver.BinaryDomain,I> {

	static enum BinaryDomain { TOP,BOTTOM } 
	
	private final static EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom<BinaryDomain>(BOTTOM);
	
	/**
	 * Creates a solver for the given problem. The solver must then be started by calling
	 * {@link #solve()}.
	 */
	public IFDSSolver(final IFDSTabulationProblem<N,D,M,I> ifdsProblem) {
		super(new IDETabulationProblem<N,D,M,BinaryDomain,I>() {

			public FlowFunctions<N,D,M> flowFunctions() {
				return ifdsProblem.flowFunctions();
			}

			public I interproceduralCFG() {
				return ifdsProblem.interproceduralCFG();
			}

			public Set<N> initialSeeds() {
				return ifdsProblem.initialSeeds();
			}

			public D zeroValue() {
				return ifdsProblem.zeroValue();
			}

			public EdgeFunctions<N,D,M,BinaryDomain> edgeFunctions() {
				return new IFDSEdgeFunctions();
			}

			public JoinLattice<BinaryDomain> joinLattice() {
				return new JoinLattice<BinaryDomain>() {

					public BinaryDomain topElement() {
						return BinaryDomain.TOP;
					}

					public BinaryDomain bottomElement() {
						return BinaryDomain.BOTTOM;
					}

					public BinaryDomain join(BinaryDomain left, BinaryDomain right) {
						if(left==TOP && right==TOP) {
							return TOP;
						} else {
							return BOTTOM;
						}
					}
				};
			}

			@Override
			public EdgeFunction<BinaryDomain> allTopFunction() {
				return new AllTop<BinaryDomain>(TOP);
			}
			









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






102




103




104




105




106




			@Override
			public boolean followReturnsPastSeeds() {
				return ifdsProblem.followReturnsPastSeeds();
			}
			









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107




108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




133




134




135




136




137




138




139




140




			class IFDSEdgeFunctions implements EdgeFunctions<N,D,M,BinaryDomain> {
		
				public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src,D srcNode,N tgt,D tgtNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt,D srcNode,M destinationMethod,D destNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod,N exitStmt,D exitNode,N returnSite,D retNode) {
					if(exitNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt,D callNode,N returnSite,D returnSideNode) {
					if(callNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
			}

			});
	}
	
	/**
	 * Returns the set of facts that hold at the given statement.
	 */
	public Set<D> ifdsResultsAt(N statement) {
		return resultsAt(statement).keySet();
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

15b0a59b9024d849291bd3f9ff169d878b08de9d

















15b0a59b9024d849291bd3f9ff169d878b08de9d


Switch branch/tag










heros


src


heros


solver


IFDSSolver.java



Find file
Normal viewHistoryPermalink






IFDSSolver.java



4.45 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import static heros.solver.IFDSSolver.BinaryDomain.BOTTOM;
import static heros.solver.IFDSSolver.BinaryDomain.TOP;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.edgefunc.AllBottom;
import heros.edgefunc.AllTop;
import heros.edgefunc.EdgeIdentity;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






26




27




28





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver}
 * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem.
 * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE
 * problem in which the environments (D to N mappings) represent the set's characteristic function.
 * 
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 * @see IFDSTabulationProblem
 */
public class IFDSSolver<N,D,M,I extends InterproceduralCFG<N, M>> extends IDESolver<N,D,M,IFDSSolver.BinaryDomain,I> {

	static enum BinaryDomain { TOP,BOTTOM } 
	
	private final static EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom<BinaryDomain>(BOTTOM);
	
	/**
	 * Creates a solver for the given problem. The solver must then be started by calling
	 * {@link #solve()}.
	 */
	public IFDSSolver(final IFDSTabulationProblem<N,D,M,I> ifdsProblem) {
		super(new IDETabulationProblem<N,D,M,BinaryDomain,I>() {

			public FlowFunctions<N,D,M> flowFunctions() {
				return ifdsProblem.flowFunctions();
			}

			public I interproceduralCFG() {
				return ifdsProblem.interproceduralCFG();
			}

			public Set<N> initialSeeds() {
				return ifdsProblem.initialSeeds();
			}

			public D zeroValue() {
				return ifdsProblem.zeroValue();
			}

			public EdgeFunctions<N,D,M,BinaryDomain> edgeFunctions() {
				return new IFDSEdgeFunctions();
			}

			public JoinLattice<BinaryDomain> joinLattice() {
				return new JoinLattice<BinaryDomain>() {

					public BinaryDomain topElement() {
						return BinaryDomain.TOP;
					}

					public BinaryDomain bottomElement() {
						return BinaryDomain.BOTTOM;
					}

					public BinaryDomain join(BinaryDomain left, BinaryDomain right) {
						if(left==TOP && right==TOP) {
							return TOP;
						} else {
							return BOTTOM;
						}
					}
				};
			}

			@Override
			public EdgeFunction<BinaryDomain> allTopFunction() {
				return new AllTop<BinaryDomain>(TOP);
			}
			









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






102




103




104




105




106




			@Override
			public boolean followReturnsPastSeeds() {
				return ifdsProblem.followReturnsPastSeeds();
			}
			









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107




108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




133




134




135




136




137




138




139




140




			class IFDSEdgeFunctions implements EdgeFunctions<N,D,M,BinaryDomain> {
		
				public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src,D srcNode,N tgt,D tgtNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt,D srcNode,M destinationMethod,D destNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod,N exitStmt,D exitNode,N returnSite,D retNode) {
					if(exitNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt,D callNode,N returnSite,D returnSideNode) {
					if(callNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
			}

			});
	}
	
	/**
	 * Returns the set of facts that hold at the given statement.
	 */
	public Set<D> ifdsResultsAt(N statement) {
		return resultsAt(statement).keySet();
	}

}











Open sidebar



Joshua Garcia heros

15b0a59b9024d849291bd3f9ff169d878b08de9d







Open sidebar



Joshua Garcia heros

15b0a59b9024d849291bd3f9ff169d878b08de9d




Open sidebar

Joshua Garcia heros

15b0a59b9024d849291bd3f9ff169d878b08de9d


Joshua Garciaherosheros
15b0a59b9024d849291bd3f9ff169d878b08de9d










15b0a59b9024d849291bd3f9ff169d878b08de9d


Switch branch/tag










heros


src


heros


solver


IFDSSolver.java



Find file
Normal viewHistoryPermalink






IFDSSolver.java



4.45 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import static heros.solver.IFDSSolver.BinaryDomain.BOTTOM;
import static heros.solver.IFDSSolver.BinaryDomain.TOP;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.edgefunc.AllBottom;
import heros.edgefunc.AllTop;
import heros.edgefunc.EdgeIdentity;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






26




27




28





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver}
 * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem.
 * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE
 * problem in which the environments (D to N mappings) represent the set's characteristic function.
 * 
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 * @see IFDSTabulationProblem
 */
public class IFDSSolver<N,D,M,I extends InterproceduralCFG<N, M>> extends IDESolver<N,D,M,IFDSSolver.BinaryDomain,I> {

	static enum BinaryDomain { TOP,BOTTOM } 
	
	private final static EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom<BinaryDomain>(BOTTOM);
	
	/**
	 * Creates a solver for the given problem. The solver must then be started by calling
	 * {@link #solve()}.
	 */
	public IFDSSolver(final IFDSTabulationProblem<N,D,M,I> ifdsProblem) {
		super(new IDETabulationProblem<N,D,M,BinaryDomain,I>() {

			public FlowFunctions<N,D,M> flowFunctions() {
				return ifdsProblem.flowFunctions();
			}

			public I interproceduralCFG() {
				return ifdsProblem.interproceduralCFG();
			}

			public Set<N> initialSeeds() {
				return ifdsProblem.initialSeeds();
			}

			public D zeroValue() {
				return ifdsProblem.zeroValue();
			}

			public EdgeFunctions<N,D,M,BinaryDomain> edgeFunctions() {
				return new IFDSEdgeFunctions();
			}

			public JoinLattice<BinaryDomain> joinLattice() {
				return new JoinLattice<BinaryDomain>() {

					public BinaryDomain topElement() {
						return BinaryDomain.TOP;
					}

					public BinaryDomain bottomElement() {
						return BinaryDomain.BOTTOM;
					}

					public BinaryDomain join(BinaryDomain left, BinaryDomain right) {
						if(left==TOP && right==TOP) {
							return TOP;
						} else {
							return BOTTOM;
						}
					}
				};
			}

			@Override
			public EdgeFunction<BinaryDomain> allTopFunction() {
				return new AllTop<BinaryDomain>(TOP);
			}
			









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






102




103




104




105




106




			@Override
			public boolean followReturnsPastSeeds() {
				return ifdsProblem.followReturnsPastSeeds();
			}
			









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107




108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




133




134




135




136




137




138




139




140




			class IFDSEdgeFunctions implements EdgeFunctions<N,D,M,BinaryDomain> {
		
				public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src,D srcNode,N tgt,D tgtNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt,D srcNode,M destinationMethod,D destNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod,N exitStmt,D exitNode,N returnSite,D retNode) {
					if(exitNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt,D callNode,N returnSite,D returnSideNode) {
					if(callNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
			}

			});
	}
	
	/**
	 * Returns the set of facts that hold at the given statement.
	 */
	public Set<D> ifdsResultsAt(N statement) {
		return resultsAt(statement).keySet();
	}

}














15b0a59b9024d849291bd3f9ff169d878b08de9d


Switch branch/tag










heros


src


heros


solver


IFDSSolver.java



Find file
Normal viewHistoryPermalink






IFDSSolver.java



4.45 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import static heros.solver.IFDSSolver.BinaryDomain.BOTTOM;
import static heros.solver.IFDSSolver.BinaryDomain.TOP;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.edgefunc.AllBottom;
import heros.edgefunc.AllTop;
import heros.edgefunc.EdgeIdentity;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






26




27




28





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver}
 * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem.
 * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE
 * problem in which the environments (D to N mappings) represent the set's characteristic function.
 * 
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 * @see IFDSTabulationProblem
 */
public class IFDSSolver<N,D,M,I extends InterproceduralCFG<N, M>> extends IDESolver<N,D,M,IFDSSolver.BinaryDomain,I> {

	static enum BinaryDomain { TOP,BOTTOM } 
	
	private final static EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom<BinaryDomain>(BOTTOM);
	
	/**
	 * Creates a solver for the given problem. The solver must then be started by calling
	 * {@link #solve()}.
	 */
	public IFDSSolver(final IFDSTabulationProblem<N,D,M,I> ifdsProblem) {
		super(new IDETabulationProblem<N,D,M,BinaryDomain,I>() {

			public FlowFunctions<N,D,M> flowFunctions() {
				return ifdsProblem.flowFunctions();
			}

			public I interproceduralCFG() {
				return ifdsProblem.interproceduralCFG();
			}

			public Set<N> initialSeeds() {
				return ifdsProblem.initialSeeds();
			}

			public D zeroValue() {
				return ifdsProblem.zeroValue();
			}

			public EdgeFunctions<N,D,M,BinaryDomain> edgeFunctions() {
				return new IFDSEdgeFunctions();
			}

			public JoinLattice<BinaryDomain> joinLattice() {
				return new JoinLattice<BinaryDomain>() {

					public BinaryDomain topElement() {
						return BinaryDomain.TOP;
					}

					public BinaryDomain bottomElement() {
						return BinaryDomain.BOTTOM;
					}

					public BinaryDomain join(BinaryDomain left, BinaryDomain right) {
						if(left==TOP && right==TOP) {
							return TOP;
						} else {
							return BOTTOM;
						}
					}
				};
			}

			@Override
			public EdgeFunction<BinaryDomain> allTopFunction() {
				return new AllTop<BinaryDomain>(TOP);
			}
			









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






102




103




104




105




106




			@Override
			public boolean followReturnsPastSeeds() {
				return ifdsProblem.followReturnsPastSeeds();
			}
			









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107




108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




133




134




135




136




137




138




139




140




			class IFDSEdgeFunctions implements EdgeFunctions<N,D,M,BinaryDomain> {
		
				public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src,D srcNode,N tgt,D tgtNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt,D srcNode,M destinationMethod,D destNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod,N exitStmt,D exitNode,N returnSite,D retNode) {
					if(exitNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt,D callNode,N returnSite,D returnSideNode) {
					if(callNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
			}

			});
	}
	
	/**
	 * Returns the set of facts that hold at the given statement.
	 */
	public Set<D> ifdsResultsAt(N statement) {
		return resultsAt(statement).keySet();
	}

}










15b0a59b9024d849291bd3f9ff169d878b08de9d


Switch branch/tag










heros


src


heros


solver


IFDSSolver.java



Find file
Normal viewHistoryPermalink




15b0a59b9024d849291bd3f9ff169d878b08de9d


Switch branch/tag










heros


src


heros


solver


IFDSSolver.java





15b0a59b9024d849291bd3f9ff169d878b08de9d


Switch branch/tag








15b0a59b9024d849291bd3f9ff169d878b08de9d


Switch branch/tag





15b0a59b9024d849291bd3f9ff169d878b08de9d

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

IFDSSolver.java
Find file
Normal viewHistoryPermalink




IFDSSolver.java



4.45 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import static heros.solver.IFDSSolver.BinaryDomain.BOTTOM;
import static heros.solver.IFDSSolver.BinaryDomain.TOP;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.edgefunc.AllBottom;
import heros.edgefunc.AllTop;
import heros.edgefunc.EdgeIdentity;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






26




27




28





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver}
 * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem.
 * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE
 * problem in which the environments (D to N mappings) represent the set's characteristic function.
 * 
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 * @see IFDSTabulationProblem
 */
public class IFDSSolver<N,D,M,I extends InterproceduralCFG<N, M>> extends IDESolver<N,D,M,IFDSSolver.BinaryDomain,I> {

	static enum BinaryDomain { TOP,BOTTOM } 
	
	private final static EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom<BinaryDomain>(BOTTOM);
	
	/**
	 * Creates a solver for the given problem. The solver must then be started by calling
	 * {@link #solve()}.
	 */
	public IFDSSolver(final IFDSTabulationProblem<N,D,M,I> ifdsProblem) {
		super(new IDETabulationProblem<N,D,M,BinaryDomain,I>() {

			public FlowFunctions<N,D,M> flowFunctions() {
				return ifdsProblem.flowFunctions();
			}

			public I interproceduralCFG() {
				return ifdsProblem.interproceduralCFG();
			}

			public Set<N> initialSeeds() {
				return ifdsProblem.initialSeeds();
			}

			public D zeroValue() {
				return ifdsProblem.zeroValue();
			}

			public EdgeFunctions<N,D,M,BinaryDomain> edgeFunctions() {
				return new IFDSEdgeFunctions();
			}

			public JoinLattice<BinaryDomain> joinLattice() {
				return new JoinLattice<BinaryDomain>() {

					public BinaryDomain topElement() {
						return BinaryDomain.TOP;
					}

					public BinaryDomain bottomElement() {
						return BinaryDomain.BOTTOM;
					}

					public BinaryDomain join(BinaryDomain left, BinaryDomain right) {
						if(left==TOP && right==TOP) {
							return TOP;
						} else {
							return BOTTOM;
						}
					}
				};
			}

			@Override
			public EdgeFunction<BinaryDomain> allTopFunction() {
				return new AllTop<BinaryDomain>(TOP);
			}
			









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






102




103




104




105




106




			@Override
			public boolean followReturnsPastSeeds() {
				return ifdsProblem.followReturnsPastSeeds();
			}
			









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107




108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




133




134




135




136




137




138




139




140




			class IFDSEdgeFunctions implements EdgeFunctions<N,D,M,BinaryDomain> {
		
				public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src,D srcNode,N tgt,D tgtNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt,D srcNode,M destinationMethod,D destNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod,N exitStmt,D exitNode,N returnSite,D retNode) {
					if(exitNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt,D callNode,N returnSite,D returnSideNode) {
					if(callNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
			}

			});
	}
	
	/**
	 * Returns the set of facts that hold at the given statement.
	 */
	public Set<D> ifdsResultsAt(N statement) {
		return resultsAt(statement).keySet();
	}

}








IFDSSolver.java



4.45 KB










IFDSSolver.java



4.45 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import static heros.solver.IFDSSolver.BinaryDomain.BOTTOM;
import static heros.solver.IFDSSolver.BinaryDomain.TOP;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.edgefunc.AllBottom;
import heros.edgefunc.AllTop;
import heros.edgefunc.EdgeIdentity;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






26




27




28





import java.util.Set;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





/**
 * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver}
 * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem.
 * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE
 * problem in which the environments (D to N mappings) represent the set's characteristic function.
 * 
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 * @see IFDSTabulationProblem
 */
public class IFDSSolver<N,D,M,I extends InterproceduralCFG<N, M>> extends IDESolver<N,D,M,IFDSSolver.BinaryDomain,I> {

	static enum BinaryDomain { TOP,BOTTOM } 
	
	private final static EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom<BinaryDomain>(BOTTOM);
	
	/**
	 * Creates a solver for the given problem. The solver must then be started by calling
	 * {@link #solve()}.
	 */
	public IFDSSolver(final IFDSTabulationProblem<N,D,M,I> ifdsProblem) {
		super(new IDETabulationProblem<N,D,M,BinaryDomain,I>() {

			public FlowFunctions<N,D,M> flowFunctions() {
				return ifdsProblem.flowFunctions();
			}

			public I interproceduralCFG() {
				return ifdsProblem.interproceduralCFG();
			}

			public Set<N> initialSeeds() {
				return ifdsProblem.initialSeeds();
			}

			public D zeroValue() {
				return ifdsProblem.zeroValue();
			}

			public EdgeFunctions<N,D,M,BinaryDomain> edgeFunctions() {
				return new IFDSEdgeFunctions();
			}

			public JoinLattice<BinaryDomain> joinLattice() {
				return new JoinLattice<BinaryDomain>() {

					public BinaryDomain topElement() {
						return BinaryDomain.TOP;
					}

					public BinaryDomain bottomElement() {
						return BinaryDomain.BOTTOM;
					}

					public BinaryDomain join(BinaryDomain left, BinaryDomain right) {
						if(left==TOP && right==TOP) {
							return TOP;
						} else {
							return BOTTOM;
						}
					}
				};
			}

			@Override
			public EdgeFunction<BinaryDomain> allTopFunction() {
				return new AllTop<BinaryDomain>(TOP);
			}
			









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






102




103




104




105




106




			@Override
			public boolean followReturnsPastSeeds() {
				return ifdsProblem.followReturnsPastSeeds();
			}
			









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107




108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




133




134




135




136




137




138




139




140




			class IFDSEdgeFunctions implements EdgeFunctions<N,D,M,BinaryDomain> {
		
				public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src,D srcNode,N tgt,D tgtNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt,D srcNode,M destinationMethod,D destNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod,N exitStmt,D exitNode,N returnSite,D retNode) {
					if(exitNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt,D callNode,N returnSite,D returnSideNode) {
					if(callNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
			}

			});
	}
	
	/**
	 * Returns the set of facts that hold at the given statement.
	 */
	public Set<D> ifdsResultsAt(N statement) {
		return resultsAt(statement).keySet();
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
package heros.solver;packageheros.solver;



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




renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

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
import static heros.solver.IFDSSolver.BinaryDomain.BOTTOM;importstaticheros.solver.IFDSSolver.BinaryDomain.BOTTOM;import static heros.solver.IFDSSolver.BinaryDomain.TOP;importstaticheros.solver.IFDSSolver.BinaryDomain.TOP;import heros.EdgeFunction;importheros.EdgeFunction;import heros.EdgeFunctions;importheros.EdgeFunctions;import heros.FlowFunctions;importheros.FlowFunctions;import heros.IDETabulationProblem;importheros.IDETabulationProblem;import heros.IFDSTabulationProblem;importheros.IFDSTabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;import heros.JoinLattice;importheros.JoinLattice;import heros.edgefunc.AllBottom;importheros.edgefunc.AllBottom;import heros.edgefunc.AllTop;importheros.edgefunc.AllTop;import heros.edgefunc.EdgeIdentity;importheros.edgefunc.EdgeIdentity;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

26

27

28
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

29




initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
/**/** * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver} * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver} * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem. * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem. * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE * problem in which the environments (D to N mappings) represent the set's characteristic function. * problem in which the environments (D to N mappings) represent the set's characteristic function. *  *  * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}. * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}. * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}. * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}. * @param <I> The type of inter-procedural control-flow graph being used. * @param <I> The type of inter-procedural control-flow graph being used. * @see IFDSTabulationProblem * @see IFDSTabulationProblem */ */public class IFDSSolver<N,D,M,I extends InterproceduralCFG<N, M>> extends IDESolver<N,D,M,IFDSSolver.BinaryDomain,I> {publicclassIFDSSolver<N,D,M,IextendsInterproceduralCFG<N,M>>extendsIDESolver<N,D,M,IFDSSolver.BinaryDomain,I>{	static enum BinaryDomain { TOP,BOTTOM } staticenumBinaryDomain{TOP,BOTTOM}		private final static EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom<BinaryDomain>(BOTTOM);privatefinalstaticEdgeFunction<BinaryDomain>ALL_BOTTOM=newAllBottom<BinaryDomain>(BOTTOM);		/**/**	 * Creates a solver for the given problem. The solver must then be started by calling	 * Creates a solver for the given problem. The solver must then be started by calling	 * {@link #solve()}.	 * {@link #solve()}.	 */	 */	public IFDSSolver(final IFDSTabulationProblem<N,D,M,I> ifdsProblem) {publicIFDSSolver(finalIFDSTabulationProblem<N,D,M,I>ifdsProblem){		super(new IDETabulationProblem<N,D,M,BinaryDomain,I>() {super(newIDETabulationProblem<N,D,M,BinaryDomain,I>(){			public FlowFunctions<N,D,M> flowFunctions() {publicFlowFunctions<N,D,M>flowFunctions(){				return ifdsProblem.flowFunctions();returnifdsProblem.flowFunctions();			}}			public I interproceduralCFG() {publicIinterproceduralCFG(){				return ifdsProblem.interproceduralCFG();returnifdsProblem.interproceduralCFG();			}}			public Set<N> initialSeeds() {publicSet<N>initialSeeds(){				return ifdsProblem.initialSeeds();returnifdsProblem.initialSeeds();			}}			public D zeroValue() {publicDzeroValue(){				return ifdsProblem.zeroValue();returnifdsProblem.zeroValue();			}}			public EdgeFunctions<N,D,M,BinaryDomain> edgeFunctions() {publicEdgeFunctions<N,D,M,BinaryDomain>edgeFunctions(){				return new IFDSEdgeFunctions();returnnewIFDSEdgeFunctions();			}}			public JoinLattice<BinaryDomain> joinLattice() {publicJoinLattice<BinaryDomain>joinLattice(){				return new JoinLattice<BinaryDomain>() {returnnewJoinLattice<BinaryDomain>(){					public BinaryDomain topElement() {publicBinaryDomaintopElement(){						return BinaryDomain.TOP;returnBinaryDomain.TOP;					}}					public BinaryDomain bottomElement() {publicBinaryDomainbottomElement(){						return BinaryDomain.BOTTOM;returnBinaryDomain.BOTTOM;					}}					public BinaryDomain join(BinaryDomain left, BinaryDomain right) {publicBinaryDomainjoin(BinaryDomainleft,BinaryDomainright){						if(left==TOP && right==TOP) {if(left==TOP&&right==TOP){							return TOP;returnTOP;						} else {}else{							return BOTTOM;returnBOTTOM;						}}					}}				};};			}}			@Override@Override			public EdgeFunction<BinaryDomain> allTopFunction() {publicEdgeFunction<BinaryDomain>allTopFunction(){				return new AllTop<BinaryDomain>(TOP);returnnewAllTop<BinaryDomain>(TOP);			}}			



making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012



making computation of unbalanced edges optional


 

 

making computation of unbalanced edges optional

 

Eric Bodden
committed
Dec 12, 2012

102

103

104

105

106
			@Override@Override			public boolean followReturnsPastSeeds() {publicbooleanfollowReturnsPastSeeds(){				return ifdsProblem.followReturnsPastSeeds();returnifdsProblem.followReturnsPastSeeds();			}}			



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

107

108

109

110

111

112

113

114

115

116

117

118

119

120

121

122

123

124

125

126

127

128

129

130

131

132

133

134

135

136

137

138

139

140
			class IFDSEdgeFunctions implements EdgeFunctions<N,D,M,BinaryDomain> {classIFDSEdgeFunctionsimplementsEdgeFunctions<N,D,M,BinaryDomain>{						public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src,D srcNode,N tgt,D tgtNode) {publicEdgeFunction<BinaryDomain>getNormalEdgeFunction(Nsrc,DsrcNode,Ntgt,DtgtNode){					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;if(srcNode==ifdsProblem.zeroValue())returnALL_BOTTOM;					return EdgeIdentity.v(); returnEdgeIdentity.v();				}}						public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt,D srcNode,M destinationMethod,D destNode) {publicEdgeFunction<BinaryDomain>getCallEdgeFunction(NcallStmt,DsrcNode,MdestinationMethod,DdestNode){					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;if(srcNode==ifdsProblem.zeroValue())returnALL_BOTTOM;					return EdgeIdentity.v(); returnEdgeIdentity.v();				}}						public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod,N exitStmt,D exitNode,N returnSite,D retNode) {publicEdgeFunction<BinaryDomain>getReturnEdgeFunction(NcallSite,McalleeMethod,NexitStmt,DexitNode,NreturnSite,DretNode){					if(exitNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;if(exitNode==ifdsProblem.zeroValue())returnALL_BOTTOM;					return EdgeIdentity.v(); returnEdgeIdentity.v();				}}						public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt,D callNode,N returnSite,D returnSideNode) {publicEdgeFunction<BinaryDomain>getCallToReturnEdgeFunction(NcallStmt,DcallNode,NreturnSite,DreturnSideNode){					if(callNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;if(callNode==ifdsProblem.zeroValue())returnALL_BOTTOM;					return EdgeIdentity.v(); returnEdgeIdentity.v();				}}			}}			});});	}}		/**/**	 * Returns the set of facts that hold at the given statement.	 * Returns the set of facts that hold at the given statement.	 */	 */	public Set<D> ifdsResultsAt(N statement) {publicSet<D>ifdsResultsAt(Nstatement){		return resultsAt(statement).keySet();returnresultsAt(statement).keySet();	}}}}





