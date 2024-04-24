



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

a839d748a388a972cfcff4a0c13bfea19136d4f9

















a839d748a388a972cfcff4a0c13bfea19136d4f9


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolver.java



7.36 KB









Newer










Older









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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
package heros.solver;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;










more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






18




import java.util.Collections;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






19




import java.util.HashMap;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






20




import java.util.HashSet;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






21




22




import java.util.Map;
import java.util.Map.Entry;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






23




24




25




import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






26














more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






27




28




29




public class BiDiIFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> {

	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> forwardProblem;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






30




	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> backwardProblem;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






31




	private final CountingThreadPoolExecutor sharedExecutor;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






32




33




34




35




36





	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






37




38




39




		this.forwardProblem = new AugmentedTabulationProblem<N,D,M,I>(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem<N,D,M,I>(backwardProblem);
		this.sharedExecutor = new CountingThreadPoolExecutor(1, forwardProblem.numThreads(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






40




41




	}
	









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






42




	public void solve() {		









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






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




		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> fwSolver = new SingleDirectionSolver(forwardProblem);

		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> bwSolver = new SingleDirectionSolver(backwardProblem);
		
		fwSolver.solve();
	}
	
	private class SingleDirectionSolver extends IFDSSolver<N, AbstractionWithSourceStmt<N, D>, M, I> {
		private SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> ifdsProblem) {
			super(ifdsProblem);
		}

		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}
	}










initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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




104




105




106




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




	public static class AbstractionWithSourceStmt<N,D> {

		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}

		private D getAbstraction() {
			return abstraction;
		}
		
		private N getSourceStmt() {
			return source;
		}	
		
		@Override
		public String toString() {
			return "[["+abstraction+" from "+source+"]]";
		}
	}
	
	static class AugmentedTabulationProblem<N,D,M,I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>,M,I> {

		private final IFDSTabulationProblem<N,D,M,I> delegate;
		private final AbstractionWithSourceStmt<N, D> ZERO;
		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();
			ZERO = new AbstractionWithSourceStmt<N, D>(delegate.zeroValue(), null);
		}

		@Override
		public FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M>() {

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






131




132




133




134




135




136




137




				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






138




139




140




141




142




143




144




145




146




147




148




149




150




151




152




153




154




155




156




157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




172




173




174




175




176




177




				}
				
				private Set<AbstractionWithSourceStmt<N, D>> copyOverSourceStmts(AbstractionWithSourceStmt<N, D> source, FlowFunction<D> originalFunction) {
					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					
					Set<AbstractionWithSourceStmt<N, D>> res = new HashSet<AbstractionWithSourceStmt<N,D>>();
					for(D d: origTargets) {
						res.add(new AbstractionWithSourceStmt<N,D>(d,source.getSourceStmt()));
					}
					return res;
				}
			};
		}
		
		//delegate methods follow

		public boolean followReturnsPastSeeds() {
			return delegate.followReturnsPastSeeds();
		}

		public boolean autoAddZero() {
			return delegate.autoAddZero();
		}

		public int numThreads() {
			return delegate.numThreads();
		}

		public boolean computeValues() {
			return delegate.computeValues();
		}

		public I interproceduralCFG() {
			return delegate.interproceduralCFG();
		}










updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






178




179




180




181




182




183




184




185




186




187




188




189




190




191




192




193




		/* attaches the original seed statement to the abstraction
		 */
		public Map<N,Set<AbstractionWithSourceStmt<N, D>>> initialSeeds() {
			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();
			Map<N,Set<AbstractionWithSourceStmt<N, D>>> res = new HashMap<N, Set<AbstractionWithSourceStmt<N,D>>>();
			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();
				Set<AbstractionWithSourceStmt<N, D>> resSet = new HashSet<AbstractionWithSourceStmt<N,D>>();
				for (D d : seeds) {
					//attach source stmt to abstraction
					resSet.add(new AbstractionWithSourceStmt<N,D>(d, stmt));
				}
				res.put(stmt, resSet);
			}			
			return res;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






194




195




196




197




198




199




200




201




202




		}

		public AbstractionWithSourceStmt<N, D> zeroValue() {
			return ZERO;
		}

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

a839d748a388a972cfcff4a0c13bfea19136d4f9

















a839d748a388a972cfcff4a0c13bfea19136d4f9


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolver.java



7.36 KB









Newer










Older









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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
package heros.solver;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;










more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






18




import java.util.Collections;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






19




import java.util.HashMap;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






20




import java.util.HashSet;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






21




22




import java.util.Map;
import java.util.Map.Entry;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






23




24




25




import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






26














more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






27




28




29




public class BiDiIFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> {

	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> forwardProblem;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






30




	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> backwardProblem;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






31




	private final CountingThreadPoolExecutor sharedExecutor;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






32




33




34




35




36





	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






37




38




39




		this.forwardProblem = new AugmentedTabulationProblem<N,D,M,I>(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem<N,D,M,I>(backwardProblem);
		this.sharedExecutor = new CountingThreadPoolExecutor(1, forwardProblem.numThreads(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






40




41




	}
	









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






42




	public void solve() {		









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






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




		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> fwSolver = new SingleDirectionSolver(forwardProblem);

		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> bwSolver = new SingleDirectionSolver(backwardProblem);
		
		fwSolver.solve();
	}
	
	private class SingleDirectionSolver extends IFDSSolver<N, AbstractionWithSourceStmt<N, D>, M, I> {
		private SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> ifdsProblem) {
			super(ifdsProblem);
		}

		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}
	}










initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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




104




105




106




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




	public static class AbstractionWithSourceStmt<N,D> {

		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}

		private D getAbstraction() {
			return abstraction;
		}
		
		private N getSourceStmt() {
			return source;
		}	
		
		@Override
		public String toString() {
			return "[["+abstraction+" from "+source+"]]";
		}
	}
	
	static class AugmentedTabulationProblem<N,D,M,I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>,M,I> {

		private final IFDSTabulationProblem<N,D,M,I> delegate;
		private final AbstractionWithSourceStmt<N, D> ZERO;
		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();
			ZERO = new AbstractionWithSourceStmt<N, D>(delegate.zeroValue(), null);
		}

		@Override
		public FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M>() {

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






131




132




133




134




135




136




137




				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






138




139




140




141




142




143




144




145




146




147




148




149




150




151




152




153




154




155




156




157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




172




173




174




175




176




177




				}
				
				private Set<AbstractionWithSourceStmt<N, D>> copyOverSourceStmts(AbstractionWithSourceStmt<N, D> source, FlowFunction<D> originalFunction) {
					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					
					Set<AbstractionWithSourceStmt<N, D>> res = new HashSet<AbstractionWithSourceStmt<N,D>>();
					for(D d: origTargets) {
						res.add(new AbstractionWithSourceStmt<N,D>(d,source.getSourceStmt()));
					}
					return res;
				}
			};
		}
		
		//delegate methods follow

		public boolean followReturnsPastSeeds() {
			return delegate.followReturnsPastSeeds();
		}

		public boolean autoAddZero() {
			return delegate.autoAddZero();
		}

		public int numThreads() {
			return delegate.numThreads();
		}

		public boolean computeValues() {
			return delegate.computeValues();
		}

		public I interproceduralCFG() {
			return delegate.interproceduralCFG();
		}










updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






178




179




180




181




182




183




184




185




186




187




188




189




190




191




192




193




		/* attaches the original seed statement to the abstraction
		 */
		public Map<N,Set<AbstractionWithSourceStmt<N, D>>> initialSeeds() {
			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();
			Map<N,Set<AbstractionWithSourceStmt<N, D>>> res = new HashMap<N, Set<AbstractionWithSourceStmt<N,D>>>();
			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();
				Set<AbstractionWithSourceStmt<N, D>> resSet = new HashSet<AbstractionWithSourceStmt<N,D>>();
				for (D d : seeds) {
					//attach source stmt to abstraction
					resSet.add(new AbstractionWithSourceStmt<N,D>(d, stmt));
				}
				res.put(stmt, resSet);
			}			
			return res;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






194




195




196




197




198




199




200




201




202




		}

		public AbstractionWithSourceStmt<N, D> zeroValue() {
			return ZERO;
		}

	}
	
}











Open sidebar



Joshua Garcia heros

a839d748a388a972cfcff4a0c13bfea19136d4f9







Open sidebar



Joshua Garcia heros

a839d748a388a972cfcff4a0c13bfea19136d4f9




Open sidebar

Joshua Garcia heros

a839d748a388a972cfcff4a0c13bfea19136d4f9


Joshua Garciaherosheros
a839d748a388a972cfcff4a0c13bfea19136d4f9










a839d748a388a972cfcff4a0c13bfea19136d4f9


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolver.java



7.36 KB









Newer










Older









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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
package heros.solver;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;










more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






18




import java.util.Collections;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






19




import java.util.HashMap;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






20




import java.util.HashSet;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






21




22




import java.util.Map;
import java.util.Map.Entry;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






23




24




25




import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






26














more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






27




28




29




public class BiDiIFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> {

	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> forwardProblem;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






30




	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> backwardProblem;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






31




	private final CountingThreadPoolExecutor sharedExecutor;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






32




33




34




35




36





	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






37




38




39




		this.forwardProblem = new AugmentedTabulationProblem<N,D,M,I>(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem<N,D,M,I>(backwardProblem);
		this.sharedExecutor = new CountingThreadPoolExecutor(1, forwardProblem.numThreads(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






40




41




	}
	









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






42




	public void solve() {		









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






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




		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> fwSolver = new SingleDirectionSolver(forwardProblem);

		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> bwSolver = new SingleDirectionSolver(backwardProblem);
		
		fwSolver.solve();
	}
	
	private class SingleDirectionSolver extends IFDSSolver<N, AbstractionWithSourceStmt<N, D>, M, I> {
		private SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> ifdsProblem) {
			super(ifdsProblem);
		}

		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}
	}










initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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




104




105




106




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




	public static class AbstractionWithSourceStmt<N,D> {

		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}

		private D getAbstraction() {
			return abstraction;
		}
		
		private N getSourceStmt() {
			return source;
		}	
		
		@Override
		public String toString() {
			return "[["+abstraction+" from "+source+"]]";
		}
	}
	
	static class AugmentedTabulationProblem<N,D,M,I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>,M,I> {

		private final IFDSTabulationProblem<N,D,M,I> delegate;
		private final AbstractionWithSourceStmt<N, D> ZERO;
		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();
			ZERO = new AbstractionWithSourceStmt<N, D>(delegate.zeroValue(), null);
		}

		@Override
		public FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M>() {

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






131




132




133




134




135




136




137




				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






138




139




140




141




142




143




144




145




146




147




148




149




150




151




152




153




154




155




156




157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




172




173




174




175




176




177




				}
				
				private Set<AbstractionWithSourceStmt<N, D>> copyOverSourceStmts(AbstractionWithSourceStmt<N, D> source, FlowFunction<D> originalFunction) {
					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					
					Set<AbstractionWithSourceStmt<N, D>> res = new HashSet<AbstractionWithSourceStmt<N,D>>();
					for(D d: origTargets) {
						res.add(new AbstractionWithSourceStmt<N,D>(d,source.getSourceStmt()));
					}
					return res;
				}
			};
		}
		
		//delegate methods follow

		public boolean followReturnsPastSeeds() {
			return delegate.followReturnsPastSeeds();
		}

		public boolean autoAddZero() {
			return delegate.autoAddZero();
		}

		public int numThreads() {
			return delegate.numThreads();
		}

		public boolean computeValues() {
			return delegate.computeValues();
		}

		public I interproceduralCFG() {
			return delegate.interproceduralCFG();
		}










updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






178




179




180




181




182




183




184




185




186




187




188




189




190




191




192




193




		/* attaches the original seed statement to the abstraction
		 */
		public Map<N,Set<AbstractionWithSourceStmt<N, D>>> initialSeeds() {
			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();
			Map<N,Set<AbstractionWithSourceStmt<N, D>>> res = new HashMap<N, Set<AbstractionWithSourceStmt<N,D>>>();
			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();
				Set<AbstractionWithSourceStmt<N, D>> resSet = new HashSet<AbstractionWithSourceStmt<N,D>>();
				for (D d : seeds) {
					//attach source stmt to abstraction
					resSet.add(new AbstractionWithSourceStmt<N,D>(d, stmt));
				}
				res.put(stmt, resSet);
			}			
			return res;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






194




195




196




197




198




199




200




201




202




		}

		public AbstractionWithSourceStmt<N, D> zeroValue() {
			return ZERO;
		}

	}
	
}














a839d748a388a972cfcff4a0c13bfea19136d4f9


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolver.java



7.36 KB









Newer










Older









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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
package heros.solver;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;










more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






18




import java.util.Collections;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






19




import java.util.HashMap;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






20




import java.util.HashSet;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






21




22




import java.util.Map;
import java.util.Map.Entry;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






23




24




25




import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






26














more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






27




28




29




public class BiDiIFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> {

	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> forwardProblem;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






30




	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> backwardProblem;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






31




	private final CountingThreadPoolExecutor sharedExecutor;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






32




33




34




35




36





	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






37




38




39




		this.forwardProblem = new AugmentedTabulationProblem<N,D,M,I>(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem<N,D,M,I>(backwardProblem);
		this.sharedExecutor = new CountingThreadPoolExecutor(1, forwardProblem.numThreads(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






40




41




	}
	









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






42




	public void solve() {		









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






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




		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> fwSolver = new SingleDirectionSolver(forwardProblem);

		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> bwSolver = new SingleDirectionSolver(backwardProblem);
		
		fwSolver.solve();
	}
	
	private class SingleDirectionSolver extends IFDSSolver<N, AbstractionWithSourceStmt<N, D>, M, I> {
		private SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> ifdsProblem) {
			super(ifdsProblem);
		}

		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}
	}










initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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




104




105




106




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




	public static class AbstractionWithSourceStmt<N,D> {

		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}

		private D getAbstraction() {
			return abstraction;
		}
		
		private N getSourceStmt() {
			return source;
		}	
		
		@Override
		public String toString() {
			return "[["+abstraction+" from "+source+"]]";
		}
	}
	
	static class AugmentedTabulationProblem<N,D,M,I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>,M,I> {

		private final IFDSTabulationProblem<N,D,M,I> delegate;
		private final AbstractionWithSourceStmt<N, D> ZERO;
		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();
			ZERO = new AbstractionWithSourceStmt<N, D>(delegate.zeroValue(), null);
		}

		@Override
		public FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M>() {

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






131




132




133




134




135




136




137




				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






138




139




140




141




142




143




144




145




146




147




148




149




150




151




152




153




154




155




156




157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




172




173




174




175




176




177




				}
				
				private Set<AbstractionWithSourceStmt<N, D>> copyOverSourceStmts(AbstractionWithSourceStmt<N, D> source, FlowFunction<D> originalFunction) {
					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					
					Set<AbstractionWithSourceStmt<N, D>> res = new HashSet<AbstractionWithSourceStmt<N,D>>();
					for(D d: origTargets) {
						res.add(new AbstractionWithSourceStmt<N,D>(d,source.getSourceStmt()));
					}
					return res;
				}
			};
		}
		
		//delegate methods follow

		public boolean followReturnsPastSeeds() {
			return delegate.followReturnsPastSeeds();
		}

		public boolean autoAddZero() {
			return delegate.autoAddZero();
		}

		public int numThreads() {
			return delegate.numThreads();
		}

		public boolean computeValues() {
			return delegate.computeValues();
		}

		public I interproceduralCFG() {
			return delegate.interproceduralCFG();
		}










updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






178




179




180




181




182




183




184




185




186




187




188




189




190




191




192




193




		/* attaches the original seed statement to the abstraction
		 */
		public Map<N,Set<AbstractionWithSourceStmt<N, D>>> initialSeeds() {
			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();
			Map<N,Set<AbstractionWithSourceStmt<N, D>>> res = new HashMap<N, Set<AbstractionWithSourceStmt<N,D>>>();
			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();
				Set<AbstractionWithSourceStmt<N, D>> resSet = new HashSet<AbstractionWithSourceStmt<N,D>>();
				for (D d : seeds) {
					//attach source stmt to abstraction
					resSet.add(new AbstractionWithSourceStmt<N,D>(d, stmt));
				}
				res.put(stmt, resSet);
			}			
			return res;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






194




195




196




197




198




199




200




201




202




		}

		public AbstractionWithSourceStmt<N, D> zeroValue() {
			return ZERO;
		}

	}
	
}










a839d748a388a972cfcff4a0c13bfea19136d4f9


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink




a839d748a388a972cfcff4a0c13bfea19136d4f9


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java





a839d748a388a972cfcff4a0c13bfea19136d4f9


Switch branch/tag








a839d748a388a972cfcff4a0c13bfea19136d4f9


Switch branch/tag





a839d748a388a972cfcff4a0c13bfea19136d4f9

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

BiDiIFDSSolver.java
Find file
Normal viewHistoryPermalink




BiDiIFDSSolver.java



7.36 KB









Newer










Older









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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
package heros.solver;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;










more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






18




import java.util.Collections;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






19




import java.util.HashMap;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






20




import java.util.HashSet;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






21




22




import java.util.Map;
import java.util.Map.Entry;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






23




24




25




import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






26














more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






27




28




29




public class BiDiIFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> {

	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> forwardProblem;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






30




	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> backwardProblem;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






31




	private final CountingThreadPoolExecutor sharedExecutor;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






32




33




34




35




36





	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






37




38




39




		this.forwardProblem = new AugmentedTabulationProblem<N,D,M,I>(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem<N,D,M,I>(backwardProblem);
		this.sharedExecutor = new CountingThreadPoolExecutor(1, forwardProblem.numThreads(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






40




41




	}
	









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






42




	public void solve() {		









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






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




		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> fwSolver = new SingleDirectionSolver(forwardProblem);

		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> bwSolver = new SingleDirectionSolver(backwardProblem);
		
		fwSolver.solve();
	}
	
	private class SingleDirectionSolver extends IFDSSolver<N, AbstractionWithSourceStmt<N, D>, M, I> {
		private SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> ifdsProblem) {
			super(ifdsProblem);
		}

		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}
	}










initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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




104




105




106




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




	public static class AbstractionWithSourceStmt<N,D> {

		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}

		private D getAbstraction() {
			return abstraction;
		}
		
		private N getSourceStmt() {
			return source;
		}	
		
		@Override
		public String toString() {
			return "[["+abstraction+" from "+source+"]]";
		}
	}
	
	static class AugmentedTabulationProblem<N,D,M,I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>,M,I> {

		private final IFDSTabulationProblem<N,D,M,I> delegate;
		private final AbstractionWithSourceStmt<N, D> ZERO;
		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();
			ZERO = new AbstractionWithSourceStmt<N, D>(delegate.zeroValue(), null);
		}

		@Override
		public FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M>() {

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






131




132




133




134




135




136




137




				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






138




139




140




141




142




143




144




145




146




147




148




149




150




151




152




153




154




155




156




157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




172




173




174




175




176




177




				}
				
				private Set<AbstractionWithSourceStmt<N, D>> copyOverSourceStmts(AbstractionWithSourceStmt<N, D> source, FlowFunction<D> originalFunction) {
					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					
					Set<AbstractionWithSourceStmt<N, D>> res = new HashSet<AbstractionWithSourceStmt<N,D>>();
					for(D d: origTargets) {
						res.add(new AbstractionWithSourceStmt<N,D>(d,source.getSourceStmt()));
					}
					return res;
				}
			};
		}
		
		//delegate methods follow

		public boolean followReturnsPastSeeds() {
			return delegate.followReturnsPastSeeds();
		}

		public boolean autoAddZero() {
			return delegate.autoAddZero();
		}

		public int numThreads() {
			return delegate.numThreads();
		}

		public boolean computeValues() {
			return delegate.computeValues();
		}

		public I interproceduralCFG() {
			return delegate.interproceduralCFG();
		}










updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






178




179




180




181




182




183




184




185




186




187




188




189




190




191




192




193




		/* attaches the original seed statement to the abstraction
		 */
		public Map<N,Set<AbstractionWithSourceStmt<N, D>>> initialSeeds() {
			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();
			Map<N,Set<AbstractionWithSourceStmt<N, D>>> res = new HashMap<N, Set<AbstractionWithSourceStmt<N,D>>>();
			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();
				Set<AbstractionWithSourceStmt<N, D>> resSet = new HashSet<AbstractionWithSourceStmt<N,D>>();
				for (D d : seeds) {
					//attach source stmt to abstraction
					resSet.add(new AbstractionWithSourceStmt<N,D>(d, stmt));
				}
				res.put(stmt, resSet);
			}			
			return res;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






194




195




196




197




198




199




200




201




202




		}

		public AbstractionWithSourceStmt<N, D> zeroValue() {
			return ZERO;
		}

	}
	
}








BiDiIFDSSolver.java



7.36 KB










BiDiIFDSSolver.java



7.36 KB









Newer










Older
NewerOlder







initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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
package heros.solver;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;










more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






18




import java.util.Collections;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






19




import java.util.HashMap;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






20




import java.util.HashSet;









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






21




22




import java.util.Map;
import java.util.Map.Entry;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






23




24




25




import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






26














more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






27




28




29




public class BiDiIFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> {

	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> forwardProblem;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






30




	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> backwardProblem;









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






31




	private final CountingThreadPoolExecutor sharedExecutor;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






32




33




34




35




36





	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






37




38




39




		this.forwardProblem = new AugmentedTabulationProblem<N,D,M,I>(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem<N,D,M,I>(backwardProblem);
		this.sharedExecutor = new CountingThreadPoolExecutor(1, forwardProblem.numThreads(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






40




41




	}
	









updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






42




	public void solve() {		









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






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




		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> fwSolver = new SingleDirectionSolver(forwardProblem);

		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> bwSolver = new SingleDirectionSolver(backwardProblem);
		
		fwSolver.solve();
	}
	
	private class SingleDirectionSolver extends IFDSSolver<N, AbstractionWithSourceStmt<N, D>, M, I> {
		private SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> ifdsProblem) {
			super(ifdsProblem);
		}

		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}
	}










initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






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




104




105




106




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




	public static class AbstractionWithSourceStmt<N,D> {

		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}

		private D getAbstraction() {
			return abstraction;
		}
		
		private N getSourceStmt() {
			return source;
		}	
		
		@Override
		public String toString() {
			return "[["+abstraction+" from "+source+"]]";
		}
	}
	
	static class AugmentedTabulationProblem<N,D,M,I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>,M,I> {

		private final IFDSTabulationProblem<N,D,M,I> delegate;
		private final AbstractionWithSourceStmt<N, D> ZERO;
		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();
			ZERO = new AbstractionWithSourceStmt<N, D>(delegate.zeroValue(), null);
		}

		@Override
		public FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M>() {

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override









more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013






131




132




133




134




135




136




137




				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {
						@Override
						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {
							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






138




139




140




141




142




143




144




145




146




147




148




149




150




151




152




153




154




155




156




157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




172




173




174




175




176




177




				}
				
				private Set<AbstractionWithSourceStmt<N, D>> copyOverSourceStmts(AbstractionWithSourceStmt<N, D> source, FlowFunction<D> originalFunction) {
					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					
					Set<AbstractionWithSourceStmt<N, D>> res = new HashSet<AbstractionWithSourceStmt<N,D>>();
					for(D d: origTargets) {
						res.add(new AbstractionWithSourceStmt<N,D>(d,source.getSourceStmt()));
					}
					return res;
				}
			};
		}
		
		//delegate methods follow

		public boolean followReturnsPastSeeds() {
			return delegate.followReturnsPastSeeds();
		}

		public boolean autoAddZero() {
			return delegate.autoAddZero();
		}

		public int numThreads() {
			return delegate.numThreads();
		}

		public boolean computeValues() {
			return delegate.computeValues();
		}

		public I interproceduralCFG() {
			return delegate.interproceduralCFG();
		}










updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013






178




179




180




181




182




183




184




185




186




187




188




189




190




191




192




193




		/* attaches the original seed statement to the abstraction
		 */
		public Map<N,Set<AbstractionWithSourceStmt<N, D>>> initialSeeds() {
			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();
			Map<N,Set<AbstractionWithSourceStmt<N, D>>> res = new HashMap<N, Set<AbstractionWithSourceStmt<N,D>>>();
			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();
				Set<AbstractionWithSourceStmt<N, D>> resSet = new HashSet<AbstractionWithSourceStmt<N,D>>();
				for (D d : seeds) {
					//attach source stmt to abstraction
					resSet.add(new AbstractionWithSourceStmt<N,D>(d, stmt));
				}
				res.put(stmt, resSet);
			}			
			return res;









initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013






194




195




196




197




198




199




200




201




202




		}

		public AbstractionWithSourceStmt<N, D> zeroValue() {
			return ZERO;
		}

	}
	
}







initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver



 

initial draft of BiDi Solver


Eric Bodden
committed
Jul 05, 2013

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.solver;packageheros.solver;import heros.FlowFunction;importheros.FlowFunction;import heros.FlowFunctions;importheros.FlowFunctions;import heros.IFDSTabulationProblem;importheros.IFDSTabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;



more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver


 

 

more advances on BiDi Solver

 

Eric Bodden
committed
Jul 05, 2013

18
import java.util.Collections;importjava.util.Collections;



updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement


 

 

updated initialSeeds to include source statement

 

Eric Bodden
committed
Jul 06, 2013

19
import java.util.HashMap;importjava.util.HashMap;



more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver


 

 

more advances on BiDi Solver

 

Eric Bodden
committed
Jul 05, 2013

20
import java.util.HashSet;importjava.util.HashSet;



updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement


 

 

updated initialSeeds to include source statement

 

Eric Bodden
committed
Jul 06, 2013

21

22
import java.util.Map;importjava.util.Map;import java.util.Map.Entry;importjava.util.Map.Entry;



more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver


 

 

more advances on BiDi Solver

 

Eric Bodden
committed
Jul 05, 2013

23

24

25
import java.util.Set;importjava.util.Set;import java.util.concurrent.LinkedBlockingQueue;importjava.util.concurrent.LinkedBlockingQueue;import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;



initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver



 

initial draft of BiDi Solver


Eric Bodden
committed
Jul 05, 2013

26




more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver


 

 

more advances on BiDi Solver

 

Eric Bodden
committed
Jul 05, 2013

27

28

29
public class BiDiIFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> {publicclassBiDiIFDSSolver<N,D,M,IextendsInterproceduralCFG<N,M>>{	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> forwardProblem;privatefinalIFDSTabulationProblem<N,AbstractionWithSourceStmt<N,D>,M,I>forwardProblem;



initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver



 

initial draft of BiDi Solver


Eric Bodden
committed
Jul 05, 2013

30
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> backwardProblem;privatefinalIFDSTabulationProblem<N,AbstractionWithSourceStmt<N,D>,M,I>backwardProblem;



more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver


 

 

more advances on BiDi Solver

 

Eric Bodden
committed
Jul 05, 2013

31
	private final CountingThreadPoolExecutor sharedExecutor;privatefinalCountingThreadPoolExecutorsharedExecutor;



initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver



 

initial draft of BiDi Solver


Eric Bodden
committed
Jul 05, 2013

32

33

34

35

36
	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {publicBiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I>forwardProblem,IFDSTabulationProblem<N,D,M,I>backwardProblem){		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {if(!forwardProblem.followReturnsPastSeeds()||!backwardProblem.followReturnsPastSeeds()){			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); thrownewIllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true.");		}}



more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver


 

 

more advances on BiDi Solver

 

Eric Bodden
committed
Jul 05, 2013

37

38

39
		this.forwardProblem = new AugmentedTabulationProblem<N,D,M,I>(forwardProblem);this.forwardProblem=newAugmentedTabulationProblem<N,D,M,I>(forwardProblem);		this.backwardProblem = new AugmentedTabulationProblem<N,D,M,I>(backwardProblem);this.backwardProblem=newAugmentedTabulationProblem<N,D,M,I>(backwardProblem);		this.sharedExecutor = new CountingThreadPoolExecutor(1, forwardProblem.numThreads(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());this.sharedExecutor=newCountingThreadPoolExecutor(1,forwardProblem.numThreads(),30,TimeUnit.SECONDS,newLinkedBlockingQueue<Runnable>());



initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver



 

initial draft of BiDi Solver


Eric Bodden
committed
Jul 05, 2013

40

41
	}}	



updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement


 

 

updated initialSeeds to include source statement

 

Eric Bodden
committed
Jul 06, 2013

42
	public void solve() {		publicvoidsolve(){



more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver


 

 

more advances on BiDi Solver

 

Eric Bodden
committed
Jul 05, 2013

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
		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> fwSolver = new SingleDirectionSolver(forwardProblem);IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I>fwSolver=newSingleDirectionSolver(forwardProblem);		IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I> bwSolver = new SingleDirectionSolver(backwardProblem);IFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I>bwSolver=newSingleDirectionSolver(backwardProblem);				fwSolver.solve();fwSolver.solve();	}}		private class SingleDirectionSolver extends IFDSSolver<N, AbstractionWithSourceStmt<N, D>, M, I> {privateclassSingleDirectionSolverextendsIFDSSolver<N,AbstractionWithSourceStmt<N,D>,M,I>{		private SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt<N, D>, M, I> ifdsProblem) {privateSingleDirectionSolver(IFDSTabulationProblem<N,AbstractionWithSourceStmt<N,D>,M,I>ifdsProblem){			super(ifdsProblem);super(ifdsProblem);		}}		protected CountingThreadPoolExecutor getExecutor() {protectedCountingThreadPoolExecutorgetExecutor(){			return sharedExecutor;returnsharedExecutor;		}}	}}



initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver



 

initial draft of BiDi Solver


Eric Bodden
committed
Jul 05, 2013

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

104

105

106

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
	public static class AbstractionWithSourceStmt<N,D> {publicstaticclassAbstractionWithSourceStmt<N,D>{		protected final D abstraction;protectedfinalDabstraction;		protected final N source;protectedfinalNsource;				private AbstractionWithSourceStmt(D abstraction, N source) {privateAbstractionWithSourceStmt(Dabstraction,Nsource){			this.abstraction = abstraction;this.abstraction=abstraction;			this.source = source;this.source=source;		}}		private D getAbstraction() {privateDgetAbstraction(){			return abstraction;returnabstraction;		}}				private N getSourceStmt() {privateNgetSourceStmt(){			return source;returnsource;		}	}				@Override@Override		public String toString() {publicStringtoString(){			return "[["+abstraction+" from "+source+"]]";return"[["+abstraction+" from "+source+"]]";		}}	}}		static class AugmentedTabulationProblem<N,D,M,I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>,M,I> {staticclassAugmentedTabulationProblem<N,D,M,IextendsInterproceduralCFG<N,M>>implementsIFDSTabulationProblem<N,BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>,M,I>{		private final IFDSTabulationProblem<N,D,M,I> delegate;privatefinalIFDSTabulationProblem<N,D,M,I>delegate;		private final AbstractionWithSourceStmt<N, D> ZERO;privatefinalAbstractionWithSourceStmt<N,D>ZERO;		private final FlowFunctions<N, D, M> originalFunctions;privatefinalFlowFunctions<N,D,M>originalFunctions;				public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {publicAugmentedTabulationProblem(IFDSTabulationProblem<N,D,M,I>delegate){			this.delegate = delegate;this.delegate=delegate;			originalFunctions = this.delegate.flowFunctions();originalFunctions=this.delegate.flowFunctions();			ZERO = new AbstractionWithSourceStmt<N, D>(delegate.zeroValue(), null);ZERO=newAbstractionWithSourceStmt<N,D>(delegate.zeroValue(),null);		}}		@Override@Override		public FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M> flowFunctions() {publicFlowFunctions<N,AbstractionWithSourceStmt<N,D>,M>flowFunctions(){			return new FlowFunctions<N, AbstractionWithSourceStmt<N, D>, M>() {returnnewFlowFunctions<N,AbstractionWithSourceStmt<N,D>,M>(){				@Override@Override				public FlowFunction<AbstractionWithSourceStmt<N, D>> getNormalFlowFunction(final N curr, final N succ) {publicFlowFunction<AbstractionWithSourceStmt<N,D>>getNormalFlowFunction(finalNcurr,finalNsucc){					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {returnnewFlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>(){						@Override@Override						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {publicSet<AbstractionWithSourceStmt<N,D>>computeTargets(AbstractionWithSourceStmt<N,D>source){							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));returncopyOverSourceStmts(source,originalFunctions.getNormalFlowFunction(curr,succ));						}}					};};				}}				@Override@Override				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallFlowFunction(final N callStmt, final M destinationMethod) {publicFlowFunction<AbstractionWithSourceStmt<N,D>>getCallFlowFunction(finalNcallStmt,finalMdestinationMethod){					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {returnnewFlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>(){						@Override@Override						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {publicSet<AbstractionWithSourceStmt<N,D>>computeTargets(AbstractionWithSourceStmt<N,D>source){							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));returncopyOverSourceStmts(source,originalFunctions.getCallFlowFunction(callStmt,destinationMethod));						}}					};};				}}				@Override@Override				public FlowFunction<AbstractionWithSourceStmt<N, D>> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {publicFlowFunction<AbstractionWithSourceStmt<N,D>>getReturnFlowFunction(finalNcallSite,finalMcalleeMethod,finalNexitStmt,finalNreturnSite){					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {returnnewFlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>(){						@Override@Override						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {publicSet<AbstractionWithSourceStmt<N,D>>computeTargets(AbstractionWithSourceStmt<N,D>source){							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));returncopyOverSourceStmts(source,originalFunctions.getReturnFlowFunction(callSite,calleeMethod,exitStmt,returnSite));						}}					};};				}}				@Override@Override



more advances on BiDi Solver


 

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver


 

 

more advances on BiDi Solver

 

Eric Bodden
committed
Jul 05, 2013

131

132

133

134

135

136

137
				public FlowFunction<AbstractionWithSourceStmt<N, D>> getCallToReturnFlowFunction(final N callSite, final N returnSite) {publicFlowFunction<AbstractionWithSourceStmt<N,D>>getCallToReturnFlowFunction(finalNcallSite,finalNreturnSite){					return new FlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>() {returnnewFlowFunction<BiDiIFDSSolver.AbstractionWithSourceStmt<N,D>>(){						@Override@Override						public Set<AbstractionWithSourceStmt<N, D>> computeTargets(AbstractionWithSourceStmt<N, D> source) {publicSet<AbstractionWithSourceStmt<N,D>>computeTargets(AbstractionWithSourceStmt<N,D>source){							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));returncopyOverSourceStmts(source,originalFunctions.getCallToReturnFlowFunction(callSite,returnSite));						}}					};};



initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver



 

initial draft of BiDi Solver


Eric Bodden
committed
Jul 05, 2013

138

139

140

141

142

143

144

145

146

147

148

149

150

151

152

153

154

155

156

157

158

159

160

161

162

163

164

165

166

167

168

169

170

171

172

173

174

175

176

177
				}}								private Set<AbstractionWithSourceStmt<N, D>> copyOverSourceStmts(AbstractionWithSourceStmt<N, D> source, FlowFunction<D> originalFunction) {privateSet<AbstractionWithSourceStmt<N,D>>copyOverSourceStmts(AbstractionWithSourceStmt<N,D>source,FlowFunction<D>originalFunction){					D originalAbstraction = source.getAbstraction();DoriginalAbstraction=source.getAbstraction();					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);Set<D>origTargets=originalFunction.computeTargets(originalAbstraction);					//optimization//optimization					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); if(origTargets.equals(Collections.singleton(originalAbstraction)))returnCollections.singleton(source);										Set<AbstractionWithSourceStmt<N, D>> res = new HashSet<AbstractionWithSourceStmt<N,D>>();Set<AbstractionWithSourceStmt<N,D>>res=newHashSet<AbstractionWithSourceStmt<N,D>>();					for(D d: origTargets) {for(Dd:origTargets){						res.add(new AbstractionWithSourceStmt<N,D>(d,source.getSourceStmt()));res.add(newAbstractionWithSourceStmt<N,D>(d,source.getSourceStmt()));					}}					return res;returnres;				}}			};};		}}				//delegate methods follow//delegate methods follow		public boolean followReturnsPastSeeds() {publicbooleanfollowReturnsPastSeeds(){			return delegate.followReturnsPastSeeds();returndelegate.followReturnsPastSeeds();		}}		public boolean autoAddZero() {publicbooleanautoAddZero(){			return delegate.autoAddZero();returndelegate.autoAddZero();		}}		public int numThreads() {publicintnumThreads(){			return delegate.numThreads();returndelegate.numThreads();		}}		public boolean computeValues() {publicbooleancomputeValues(){			return delegate.computeValues();returndelegate.computeValues();		}}		public I interproceduralCFG() {publicIinterproceduralCFG(){			return delegate.interproceduralCFG();returndelegate.interproceduralCFG();		}}



updated initialSeeds to include source statement


 

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement


 

 

updated initialSeeds to include source statement

 

Eric Bodden
committed
Jul 06, 2013

178

179

180

181

182

183

184

185

186

187

188

189

190

191

192

193
		/* attaches the original seed statement to the abstraction/* attaches the original seed statement to the abstraction		 */		 */		public Map<N,Set<AbstractionWithSourceStmt<N, D>>> initialSeeds() {publicMap<N,Set<AbstractionWithSourceStmt<N,D>>>initialSeeds(){			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();Map<N,Set<D>>originalSeeds=delegate.initialSeeds();			Map<N,Set<AbstractionWithSourceStmt<N, D>>> res = new HashMap<N, Set<AbstractionWithSourceStmt<N,D>>>();Map<N,Set<AbstractionWithSourceStmt<N,D>>>res=newHashMap<N,Set<AbstractionWithSourceStmt<N,D>>>();			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {for(Entry<N,Set<D>>entry:originalSeeds.entrySet()){				N stmt = entry.getKey();Nstmt=entry.getKey();				Set<D> seeds = entry.getValue();Set<D>seeds=entry.getValue();				Set<AbstractionWithSourceStmt<N, D>> resSet = new HashSet<AbstractionWithSourceStmt<N,D>>();Set<AbstractionWithSourceStmt<N,D>>resSet=newHashSet<AbstractionWithSourceStmt<N,D>>();				for (D d : seeds) {for(Dd:seeds){					//attach source stmt to abstraction//attach source stmt to abstraction					resSet.add(new AbstractionWithSourceStmt<N,D>(d, stmt));resSet.add(newAbstractionWithSourceStmt<N,D>(d,stmt));				}}				res.put(stmt, resSet);res.put(stmt,resSet);			}			}			return res;returnres;



initial draft of BiDi Solver



 


Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver



 

initial draft of BiDi Solver


Eric Bodden
committed
Jul 05, 2013

194

195

196

197

198

199

200

201

202
		}}		public AbstractionWithSourceStmt<N, D> zeroValue() {publicAbstractionWithSourceStmt<N,D>zeroValue(){			return ZERO;returnZERO;		}}	}}	}}





