



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

5bea6302bd6044e8d8258dbf668c174214c1b18d

















5bea6302bd6044e8d8258dbf668c174214c1b18d


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolver.java



14.3 KB









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









changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19












more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28












comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  








comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.








comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;








comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56












comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);








improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



	}
	








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {








comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();








comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {








comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {








comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}








don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}









comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}









made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
		}
		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {








toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



		}








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}









initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



				}
				








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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




348


349



		/* attaches the original seed statement to the abstraction
		 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



		}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
		}

	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



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

5bea6302bd6044e8d8258dbf668c174214c1b18d

















5bea6302bd6044e8d8258dbf668c174214c1b18d


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolver.java



14.3 KB









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









changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19












more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28












comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  








comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.








comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;








comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56












comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);








improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



	}
	








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {








comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();








comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {








comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {








comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}








don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}









comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}









made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
		}
		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {








toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



		}








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}









initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



				}
				








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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




348


349



		/* attaches the original seed statement to the abstraction
		 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



		}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
		}

	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



}












Open sidebar



Joshua Garcia heros

5bea6302bd6044e8d8258dbf668c174214c1b18d







Open sidebar



Joshua Garcia heros

5bea6302bd6044e8d8258dbf668c174214c1b18d




Open sidebar

Joshua Garcia heros

5bea6302bd6044e8d8258dbf668c174214c1b18d


Joshua Garciaherosheros
5bea6302bd6044e8d8258dbf668c174214c1b18d










5bea6302bd6044e8d8258dbf668c174214c1b18d


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolver.java



14.3 KB









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









changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19












more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28












comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  








comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.








comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;








comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56












comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);








improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



	}
	








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {








comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();








comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {








comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {








comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}








don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}









comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}









made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
		}
		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {








toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



		}








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}









initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



				}
				








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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




348


349



		/* attaches the original seed statement to the abstraction
		 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



		}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
		}

	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



}















5bea6302bd6044e8d8258dbf668c174214c1b18d


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolver.java



14.3 KB









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









changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19












more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28












comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  








comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.








comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;








comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56












comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);








improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



	}
	








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {








comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();








comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {








comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {








comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}








don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}









comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}









made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
		}
		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {








toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



		}








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}









initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



				}
				








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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




348


349



		/* attaches the original seed statement to the abstraction
		 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



		}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
		}

	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



}











5bea6302bd6044e8d8258dbf668c174214c1b18d


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java



Find file
Normal viewHistoryPermalink




5bea6302bd6044e8d8258dbf668c174214c1b18d


Switch branch/tag










heros


src


heros


solver


BiDiIFDSSolver.java





5bea6302bd6044e8d8258dbf668c174214c1b18d


Switch branch/tag








5bea6302bd6044e8d8258dbf668c174214c1b18d


Switch branch/tag





5bea6302bd6044e8d8258dbf668c174214c1b18d

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



14.3 KB









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









changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19












more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28












comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  








comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.








comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;








comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56












comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);








improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



	}
	








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {








comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();








comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {








comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {








comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}








don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}









comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}









made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
		}
		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {








toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



		}








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}









initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



				}
				








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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




348


349



		/* attaches the original seed statement to the abstraction
		 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



		}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
		}

	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



}









BiDiIFDSSolver.java



14.3 KB










BiDiIFDSSolver.java



14.3 KB









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









changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19












more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28












comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  








comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.








comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;








comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56












comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);








improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



	}
	








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {








comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();








comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {








comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {








comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}








don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}









comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}









made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
		}
		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {








toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



		}








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}









initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



				}
				








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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




348


349



		/* attaches the original seed statement to the abstraction
		 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



		}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
		}

	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



}











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









changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19












more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28












comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  








comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.








comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;








comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56












comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);








improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



	}
	








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {








comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();








comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {








comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {








comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}








don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}









comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}









made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
		}
		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {








toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



		}








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}









initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



				}
				








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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




348


349



		/* attaches the original seed statement to the abstraction
		 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



		}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
		}

	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



}









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









changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19












more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28












comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  








comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.








comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;








comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56












comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);








improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



	}
	








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100












added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {








comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();








comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {








comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {








comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case








don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {








comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		








comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)








Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}








don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);








finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}








BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}









comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}









made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
		}
		








made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {








toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



		}








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")








bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());








bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}









initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {








more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



				}
				








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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




348


349



		/* attaches the original seed statement to the abstraction
		 */








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));








updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



		}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
		}

	}
	








comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		








added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {








comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	








initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



}







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


/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.solver;packageheros.solver;




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




13



import heros.EdgeFunction;






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


13


import heros.EdgeFunction;

import heros.EdgeFunction;importheros.EdgeFunction;




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




14


15


16


17



import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


14


15


16


17


import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

import heros.FlowFunction;importheros.FlowFunction;import heros.FlowFunctions;importheros.FlowFunctions;import heros.IFDSTabulationProblem;importheros.IFDSTabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




18



import heros.solver.PathTrackingIFDSSolver.LinkedNode;






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


18


import heros.solver.PathTrackingIFDSSolver.LinkedNode;

import heros.solver.PathTrackingIFDSSolver.LinkedNode;importheros.solver.PathTrackingIFDSSolver.LinkedNode;




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




19










initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


19









more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




20



import java.util.Collections;






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


import java.util.Collections;

import java.util.Collections;importjava.util.Collections;




updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




21



import java.util.HashMap;






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


import java.util.HashMap;

import java.util.HashMap;importjava.util.HashMap;




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




22



import java.util.HashSet;






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


22


import java.util.HashSet;

import java.util.HashSet;importjava.util.HashSet;




updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




23


24



import java.util.Map;
import java.util.Map.Entry;






updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement

 

updated initialSeeds to include source statement

Eric Bodden
committed
Jul 06, 2013


23


24


import java.util.Map;
import java.util.Map.Entry;

import java.util.Map;importjava.util.Map;import java.util.Map.Entry;importjava.util.Map.Entry;




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




25


26


27



import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


25


26


27


import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import java.util.Set;importjava.util.Set;import java.util.concurrent.LinkedBlockingQueue;importjava.util.concurrent.LinkedBlockingQueue;import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




28










initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


28









comments

 


Eric Bodden
committed
Jul 09, 2013




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



/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


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


/**
 * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to
 * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions.
 * 
 * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems
 * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}.
 * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified
 * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever).
 * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their
 * computed paths are realizable at the same time.

/**/** * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to * This is a special IFDS solver that solves the analysis problem inside out, i.e., from further down the call stack to * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions. * further up the call stack. This can be useful, for instance, for taint analysis problems that track flows in two directions. *  *  * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems * The solver is instantiated with two analyses, one to be computed forward and one to be computed backward. Both analysis problems * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}. * must be unbalanced, i.e., must return <code>true</code> for {@link IFDSTabulationProblem#followReturnsPastSeeds()}. * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified * The solver then executes both analyses in lockstep, i.e., when one of the analyses reaches an unbalanced return edge (signified * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever). * by a ZERO source value) then the solver pauses this analysis until the other analysis reaches the same unbalanced return (if ever). * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their * The result is that the analyses will never diverge, i.e., will ultimately always only propagate into contexts in which both their * computed paths are realizable at the same time. * computed paths are realizable at the same time.




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




39


40


41



 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


39


40


41


 * 
 * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * reportable paths.  

 *  *  * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form * This solver requires data-flow abstractions that implement the {@link LinkedNode} interface such that data-flow values can be linked to form * reportable paths.   * reportable paths.  




comments

 


Eric Bodden
committed
Jul 09, 2013




42


43



 *
 * @param <N> see {@link IFDSSolver}






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


42


43


 *
 * @param <N> see {@link IFDSSolver}

 * * * @param <N> see {@link IFDSSolver} * @param <N> see {@link IFDSSolver}




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




44


45



 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


44


45


 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form
 * 				reportable paths.

 * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form * @param <D> A data-flow abstraction that must implement the {@link LinkedNode} interface such that data-flow values can be linked to form * 				reportable paths. * 				reportable paths.




comments

 


Eric Bodden
committed
Jul 09, 2013




46


47


48



 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


46


47


48


 * @param <M> see {@link IFDSSolver}
 * @param <I> see {@link IFDSSolver}
 */

 * @param <M> see {@link IFDSSolver} * @param <M> see {@link IFDSSolver} * @param <I> see {@link IFDSSolver} * @param <I> see {@link IFDSSolver} */ */




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




49



public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


49


public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {

public class BiDiIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> {publicclassBiDiIFDSSolver<N,DextendsPathTrackingIFDSSolver.LinkedNode<D>,M,IextendsInterproceduralCFG<N,M>>{




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




50










more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


50









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




51


52



	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


51


52


	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;
	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;

	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> forwardProblem;privatefinalIFDSTabulationProblem<N,AbstractionWithSourceStmt,M,I>forwardProblem;	private final IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> backwardProblem;privatefinalIFDSTabulationProblem<N,AbstractionWithSourceStmt,M,I>backwardProblem;




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




53



	private final CountingThreadPoolExecutor sharedExecutor;






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


53


	private final CountingThreadPoolExecutor sharedExecutor;

	private final CountingThreadPoolExecutor sharedExecutor;privatefinalCountingThreadPoolExecutorsharedExecutor;




comments

 


Eric Bodden
committed
Jul 09, 2013




54


55



	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


54


55


	private SingleDirectionSolver fwSolver;
	private SingleDirectionSolver bwSolver;

	private SingleDirectionSolver fwSolver;privateSingleDirectionSolverfwSolver;	private SingleDirectionSolver bwSolver;privateSingleDirectionSolverbwSolver;




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




56










initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


56









comments

 


Eric Bodden
committed
Jul 09, 2013




57


58


59



	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


57


58


59


	/**
	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.
	 */

	/**/**	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.	 * Instantiates a {@link BiDiIFDSSolver} with the associated forward and backward problem.	 */	 */




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




60


61


62


63



	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
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


60


61


62


63


	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {
		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); 
		}

	public BiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I> forwardProblem, IFDSTabulationProblem<N,D,M,I> backwardProblem) {publicBiDiIFDSSolver(IFDSTabulationProblem<N,D,M,I>forwardProblem,IFDSTabulationProblem<N,D,M,I>backwardProblem){		if(!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {if(!forwardProblem.followReturnsPastSeeds()||!backwardProblem.followReturnsPastSeeds()){			throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true."); thrownewIllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true.");		}}




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




64


65



		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


64


65


		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);

		this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);this.forwardProblem=newAugmentedTabulationProblem(forwardProblem);		this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);this.backwardProblem=newAugmentedTabulationProblem(backwardProblem);




improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013




66



		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());






improved handling of number of threads (thanks Johannes!)

 


Eric Bodden
committed
Jul 11, 2013



improved handling of number of threads (thanks Johannes!)

 

improved handling of number of threads (thanks Johannes!)

Eric Bodden
committed
Jul 11, 2013


66


		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

		this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1,forwardProblem.numThreads()), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());this.sharedExecutor=newCountingThreadPoolExecutor(1,Math.max(1,forwardProblem.numThreads()),30,TimeUnit.SECONDS,newLinkedBlockingQueue<Runnable>());




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




67


68



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


67


68


	}
	

	}}	




updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




69



	public void solve() {		






updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement

 

updated initialSeeds to include source statement

Eric Bodden
committed
Jul 06, 2013


69


	public void solve() {		

	public void solve() {		publicvoidsolve(){




made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




70


71



		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");






made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013



made bidi solver extensible

 

made bidi solver extensible

Eric Bodden
committed
Jul 23, 2013


70


71


		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");
		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");

		fwSolver = createSingleDirectionSolver(forwardProblem, "FW");fwSolver=createSingleDirectionSolver(forwardProblem,"FW");		bwSolver = createSingleDirectionSolver(backwardProblem, "BW");bwSolver=createSingleDirectionSolver(backwardProblem,"BW");




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




72


73



		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


72


73


		fwSolver.otherSolver = bwSolver;
		bwSolver.otherSolver = fwSolver;

		fwSolver.otherSolver = bwSolver;fwSolver.otherSolver=bwSolver;		bwSolver.otherSolver = fwSolver;bwSolver.otherSolver=fwSolver;




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




74



		






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


74


		

		




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




75



		//start the bw solver






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


75


		//start the bw solver

		//start the bw solver//start the bw solver




BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




76



		bwSolver.submitInitialSeeds();






BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013



BiDiSolver: support for debug name and simultaneous submission of both analyses

 

BiDiSolver: support for debug name and simultaneous submission of both analyses

Eric Bodden
committed
Jul 06, 2013


76


		bwSolver.submitInitialSeeds();

		bwSolver.submitInitialSeeds();bwSolver.submitInitialSeeds();




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




77


78


79


80



		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


77


78


79


80


		
		//start the fw solver and block until both solvers have completed
		//(note that they both share the same executor, see below)
		//note to self: the order of the two should not matter

				//start the fw solver and block until both solvers have completed//start the fw solver and block until both solvers have completed		//(note that they both share the same executor, see below)//(note that they both share the same executor, see below)		//note to self: the order of the two should not matter//note to self: the order of the two should not matter




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




81


82


83



		fwSolver.solve();
	}
	






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


81


82


83


		fwSolver.solve();
	}
	

		fwSolver.solve();fwSolver.solve();	}}	




made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




84


85


86



	/**
	 * Creates a solver to be used for each single analysis direction.
	 */






made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013



made bidi solver extensible

 

made bidi solver extensible

Eric Bodden
committed
Jul 23, 2013


84


85


86


	/**
	 * Creates a solver to be used for each single analysis direction.
	 */

	/**/**	 * Creates a solver to be used for each single analysis direction.	 * Creates a solver to be used for each single analysis direction.	 */	 */




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




87



	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


87


	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {

	protected SingleDirectionSolver createSingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> problem, String debugName) {protectedSingleDirectionSolvercreateSingleDirectionSolver(IFDSTabulationProblem<N,AbstractionWithSourceStmt,M,I>problem,StringdebugName){




made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




88


89


90



		return new SingleDirectionSolver(problem, debugName);
	}
	






made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013



made bidi solver extensible

 

made bidi solver extensible

Eric Bodden
committed
Jul 23, 2013


88


89


90


		return new SingleDirectionSolver(problem, debugName);
	}
	

		return new SingleDirectionSolver(problem, debugName);returnnewSingleDirectionSolver(problem,debugName);	}}	




comments

 


Eric Bodden
committed
Jul 09, 2013




91


92


93



	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


91


92


93


	/**
	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.
	 */

	/**/**	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.	 * This is a modified IFDS solver that is capable of pausing and unpausing return-flow edges.	 */	 */




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




94



	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


94


	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {

	protected class SingleDirectionSolver extends PathTrackingIFDSSolver<N, AbstractionWithSourceStmt, M, I> {protectedclassSingleDirectionSolverextendsPathTrackingIFDSSolver<N,AbstractionWithSourceStmt,M,I>{




BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




95



		private final String debugName;






BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013



BiDiSolver: support for debug name and simultaneous submission of both analyses

 

BiDiSolver: support for debug name and simultaneous submission of both analyses

Eric Bodden
committed
Jul 06, 2013


95


		private final String debugName;

		private final String debugName;privatefinalStringdebugName;




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




96


97



		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


96


97


		private SingleDirectionSolver otherSolver;
		private Set<N> leakedSources = new HashSet<N>();

		private SingleDirectionSolver otherSolver;privateSingleDirectionSolverotherSolver;		private Set<N> leakedSources = new HashSet<N>();privateSet<N>leakedSources=newHashSet<N>();




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




98


99



		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


98


99


		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =
				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();

		private Map<N,Set<PathEdge<N,AbstractionWithSourceStmt>>> pausedPathEdges =privateMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>pausedPathEdges=				new HashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();newHashMap<N,Set<PathEdge<N,AbstractionWithSourceStmt>>>();




BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




100










BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013



BiDiSolver: support for debug name and simultaneous submission of both analyses

 

BiDiSolver: support for debug name and simultaneous submission of both analyses

Eric Bodden
committed
Jul 06, 2013


100









added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




101



		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


101


		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {

		public SingleDirectionSolver(IFDSTabulationProblem<N, AbstractionWithSourceStmt, M, I> ifdsProblem, String debugName) {publicSingleDirectionSolver(IFDSTabulationProblem<N,AbstractionWithSourceStmt,M,I>ifdsProblem,StringdebugName){




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




102



			super(ifdsProblem);






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


102


			super(ifdsProblem);

			super(ifdsProblem);super(ifdsProblem);




BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




103



			this.debugName = debugName;






BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013



BiDiSolver: support for debug name and simultaneous submission of both analyses

 

BiDiSolver: support for debug name and simultaneous submission of both analyses

Eric Bodden
committed
Jul 06, 2013


103


			this.debugName = debugName;

			this.debugName = debugName;this.debugName=debugName;




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




104



		}






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


104


		}

		}}




BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




105



		






BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013



BiDiSolver: support for debug name and simultaneous submission of both analyses

 

BiDiSolver: support for debug name and simultaneous submission of both analyses

Eric Bodden
committed
Jul 06, 2013


105


		

		




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




106



		@Override






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


106


		@Override

		@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




107



		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


107


		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {

		protected void processExit(PathEdge<N,AbstractionWithSourceStmt> edge) {protectedvoidprocessExit(PathEdge<N,AbstractionWithSourceStmt>edge){




comments

 


Eric Bodden
committed
Jul 09, 2013




108



			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


108


			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge

			//if an edge is originating from ZERO then to us this signifies an unbalanced return edge//if an edge is originating from ZERO then to us this signifies an unbalanced return edge




don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




109


110



			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();






don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013



don't pause conditional edges

 

don't pause conditional edges

Eric Bodden
committed
Jul 08, 2013


109


110


			if(edge.factAtSource().equals(zeroValue)) {
				N sourceStmt = edge.factAtTarget().getSourceStmt();

			if(edge.factAtSource().equals(zeroValue)) {if(edge.factAtSource().equals(zeroValue)){				N sourceStmt = edge.factAtTarget().getSourceStmt();NsourceStmt=edge.factAtTarget().getSourceStmt();




comments

 


Eric Bodden
committed
Jul 09, 2013




111



				//we mark the fact that this solver would like to "leak" this edge to the caller






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


111


				//we mark the fact that this solver would like to "leak" this edge to the caller

				//we mark the fact that this solver would like to "leak" this edge to the caller//we mark the fact that this solver would like to "leak" this edge to the caller




don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




112


113



				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {






don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013



don't pause conditional edges

 

don't pause conditional edges

Eric Bodden
committed
Jul 08, 2013


112


113


				leakedSources.add(sourceStmt);
				if(otherSolver.hasLeaked(sourceStmt)) {

				leakedSources.add(sourceStmt);leakedSources.add(sourceStmt);				if(otherSolver.hasLeaked(sourceStmt)) {if(otherSolver.hasLeaked(sourceStmt)){




comments

 


Eric Bodden
committed
Jul 09, 2013




114



					//if the other solver has leaked already then unpause its edges and continue






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


114


					//if the other solver has leaked already then unpause its edges and continue

					//if the other solver has leaked already then unpause its edges and continue//if the other solver has leaked already then unpause its edges and continue




don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




115


116


117



					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {






don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013



don't pause conditional edges

 

don't pause conditional edges

Eric Bodden
committed
Jul 08, 2013


115


116


117


					otherSolver.unpausePathEdgesForSource(sourceStmt);
					super.processExit(edge);
				} else {

					otherSolver.unpausePathEdgesForSource(sourceStmt);otherSolver.unpausePathEdgesForSource(sourceStmt);					super.processExit(edge);super.processExit(edge);				} else {}else{




comments

 


Eric Bodden
committed
Jul 09, 2013




118



					//otherwise we pause this solver's edge and don't continue






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


118


					//otherwise we pause this solver's edge and don't continue

					//otherwise we pause this solver's edge and don't continue//otherwise we pause this solver's edge and don't continue




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




119



					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


119


					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);

					Set<PathEdge<N,AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);Set<PathEdge<N,AbstractionWithSourceStmt>>pausedEdges=pausedPathEdges.get(sourceStmt);




don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




120



					if(pausedEdges==null) {






don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013



don't pause conditional edges

 

don't pause conditional edges

Eric Bodden
committed
Jul 08, 2013


120


					if(pausedEdges==null) {

					if(pausedEdges==null) {if(pausedEdges==null){




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




121



						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


121


						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();

						pausedEdges = new HashSet<PathEdge<N,AbstractionWithSourceStmt>>();pausedEdges=newHashSet<PathEdge<N,AbstractionWithSourceStmt>>();




don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




122


123


124



						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);






don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013



don't pause conditional edges

 

don't pause conditional edges

Eric Bodden
committed
Jul 08, 2013


122


123


124


						pausedPathEdges.put(sourceStmt,pausedEdges);
					}				
					pausedEdges.add(edge);

						pausedPathEdges.put(sourceStmt,pausedEdges);pausedPathEdges.put(sourceStmt,pausedEdges);					}				}					pausedEdges.add(edge);pausedEdges.add(edge);




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




125



                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


125


                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);

                    logger.debug(" ++ PAUSE {}: {}", debugName, edge);logger.debug(" ++ PAUSE {}: {}",debugName,edge);




don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




126



				}






don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013



don't pause conditional edges

 

don't pause conditional edges

Eric Bodden
committed
Jul 08, 2013


126


				}

				}}




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




127



			} else {






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


127


			} else {

			} else {}else{




comments

 


Eric Bodden
committed
Jul 09, 2013




128



				//the default case






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


128


				//the default case

				//the default case//the default case




don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013




129


130



				super.processExit(edge);
			}






don't pause conditional edges

 


Eric Bodden
committed
Jul 08, 2013



don't pause conditional edges

 

don't pause conditional edges

Eric Bodden
committed
Jul 08, 2013


129


130


				super.processExit(edge);
			}

				super.processExit(edge);super.processExit(edge);			}}




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




131


132



		}
		






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


131


132


		}
		

		}}		




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




133



		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


133


		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {

		protected void propagate(AbstractionWithSourceStmt sourceVal, N target, AbstractionWithSourceStmt targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {protectedvoidpropagate(AbstractionWithSourceStmtsourceVal,Ntarget,AbstractionWithSourceStmttargetVal,EdgeFunction<IFDSSolver.BinaryDomain>f,NrelatedCallSite,booleanisUnbalancedReturn){




comments

 


Eric Bodden
committed
Jul 09, 2013




134



			//the follwing branch will be taken only on an unbalanced return






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


134


			//the follwing branch will be taken only on an unbalanced return

			//the follwing branch will be taken only on an unbalanced return//the follwing branch will be taken only on an unbalanced return




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




135



			if(isUnbalancedReturn) {






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


135


			if(isUnbalancedReturn) {

			if(isUnbalancedReturn) {if(isUnbalancedReturn){




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




136


137


138



				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


136


137


138


				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";
				
				//attach target statement as new "source" statement to track

				assert sourceVal.getSourceStmt()==null : "source value should have no statement attached";assertsourceVal.getSourceStmt()==null:"source value should have no statement attached";								//attach target statement as new "source" statement to track//attach target statement as new "source" statement to track




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




139



				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


139


				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);

				targetVal = new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);targetVal=newAbstractionWithSourceStmt(targetVal.getAbstraction(),relatedCallSite);




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




140



				






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


140


				

				




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




141



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


141


				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);

				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);super.propagate(sourceVal,target,targetVal,f,relatedCallSite,isUnbalancedReturn);




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




142



			} else { 






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


142


			} else { 

			} else { }else{




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




143



				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


143


				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);

				super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);super.propagate(sourceVal,target,targetVal,f,relatedCallSite,isUnbalancedReturn);




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




144


145


146



			}
		}
		






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


144


145


146


			}
		}
		

			}}		}}		




comments

 


Eric Bodden
committed
Jul 09, 2013




147


148


149


150



		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


147


148


149


150


		/**
		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source
		 * to its caller.
		 */

		/**/**		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source		 * Returns <code>true</code> if this solver has tried to leak an edge originating from the given source		 * to its caller.		 * to its caller.		 */		 */




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




151


152


153


154



		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


151


152


153


154


		private boolean hasLeaked(N sourceStmt) {
			return leakedSources.contains(sourceStmt);
		}
		

		private boolean hasLeaked(N sourceStmt) {privatebooleanhasLeaked(NsourceStmt){			return leakedSources.contains(sourceStmt);returnleakedSources.contains(sourceStmt);		}}		




comments

 


Eric Bodden
committed
Jul 09, 2013




155


156


157



		/**
		 * Unpauses all edges associated with the given source statement.
		 */






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


155


156


157


		/**
		 * Unpauses all edges associated with the given source statement.
		 */

		/**/**		 * Unpauses all edges associated with the given source statement.		 * Unpauses all edges associated with the given source statement.		 */		 */




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




158



		private void unpausePathEdgesForSource(N sourceStmt) {






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


158


		private void unpausePathEdgesForSource(N sourceStmt) {

		private void unpausePathEdgesForSource(N sourceStmt) {privatevoidunpausePathEdgesForSource(NsourceStmt){




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




159



			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


159


			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);

			Set<PathEdge<N, AbstractionWithSourceStmt>> pausedEdges = pausedPathEdges.get(sourceStmt);Set<PathEdge<N,AbstractionWithSourceStmt>>pausedEdges=pausedPathEdges.get(sourceStmt);




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




160



			if(pausedEdges!=null) {






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


160


			if(pausedEdges!=null) {

			if(pausedEdges!=null) {if(pausedEdges!=null){




Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




161



			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {






Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013



Merge branch 'feature/reduced-summaries-bidi' into develop

 

Merge branch 'feature/reduced-summaries-bidi' into develop

Eric Bodden
committed
Oct 18, 2013


161


			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {

			for(PathEdge<N, AbstractionWithSourceStmt> pausedEdge: pausedEdges) {for(PathEdge<N,AbstractionWithSourceStmt>pausedEdge:pausedEdges){




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




162



					if(DEBUG)






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


162


					if(DEBUG)

					if(DEBUG)if(DEBUG)




Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013




163



						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);






Merge branch 'feature/reduced-summaries-bidi' into develop

 


Eric Bodden
committed
Oct 18, 2013



Merge branch 'feature/reduced-summaries-bidi' into develop

 

Merge branch 'feature/reduced-summaries-bidi' into develop

Eric Bodden
committed
Oct 18, 2013


163


						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);

						logger.debug("-- UNPAUSE {}: {}",debugName, pausedEdge);logger.debug("-- UNPAUSE {}: {}",debugName,pausedEdge);




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




164


165



					super.processExit(pausedEdge);
				}






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


164


165


					super.processExit(pausedEdge);
				}

					super.processExit(pausedEdge);super.processExit(pausedEdge);				}}




don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013




166



				pausedPathEdges.remove(sourceStmt);






don't forget to clear pause list un unpause

 


Eric Bodden
committed
Jul 06, 2013



don't forget to clear pause list un unpause

 

don't forget to clear pause list un unpause

Eric Bodden
committed
Jul 06, 2013


166


				pausedPathEdges.remove(sourceStmt);

				pausedPathEdges.remove(sourceStmt);pausedPathEdges.remove(sourceStmt);




finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013




167


168


169



			}
		}
		






finalized (?) logic for bidi solver

 


Eric Bodden
committed
Jul 06, 2013



finalized (?) logic for bidi solver

 

finalized (?) logic for bidi solver

Eric Bodden
committed
Jul 06, 2013


167


168


169


			}
		}
		

			}}		}}		




BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




170


171


172



		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 






BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013



BiDiSolver: support for debug name and simultaneous submission of both analyses

 

BiDiSolver: support for debug name and simultaneous submission of both analyses

Eric Bodden
committed
Jul 06, 2013


170


171


172


		/* we share the same executor; this will cause the call to solve() above to block
		 * until both solvers have finished
		 */ 

		/* we share the same executor; this will cause the call to solve() above to block/* we share the same executor; this will cause the call to solve() above to block		 * until both solvers have finished		 * until both solvers have finished		 */ 		 */




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




173


174


175



		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


173


174


175


		protected CountingThreadPoolExecutor getExecutor() {
			return sharedExecutor;
		}

		protected CountingThreadPoolExecutor getExecutor() {protectedCountingThreadPoolExecutorgetExecutor(){			return sharedExecutor;returnsharedExecutor;		}}




BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013




176


177


178


179



		
		protected String getDebugName() {
			return debugName;
		}






BiDiSolver: support for debug name and simultaneous submission of both analyses

 


Eric Bodden
committed
Jul 06, 2013



BiDiSolver: support for debug name and simultaneous submission of both analyses

 

BiDiSolver: support for debug name and simultaneous submission of both analyses

Eric Bodden
committed
Jul 06, 2013


176


177


178


179


		
		protected String getDebugName() {
			return debugName;
		}

				protected String getDebugName() {protectedStringgetDebugName(){			return debugName;returndebugName;		}}




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




180


181



	}







more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


180


181


	}


	}}




comments

 


Eric Bodden
committed
Jul 09, 2013




182


183


184


185



	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


182


183


184


185


	/**
	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the
	 * abstraction the source statement from which this fact originated. 
	 */

	/**/**	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the	 * This is an augmented abstraction propagated by the {@link SingleDirectionSolver}. It associates with the	 * abstraction the source statement from which this fact originated. 	 * abstraction the source statement from which this fact originated. 	 */	 */




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




186



	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


186


	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {

	public class AbstractionWithSourceStmt implements PathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt> {publicclassAbstractionWithSourceStmtimplementsPathTrackingIFDSSolver.LinkedNode<AbstractionWithSourceStmt>{




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




187


188


189


190


191


192


193


194


195




		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
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


187


188


189


190


191


192


193


194


195



		protected final D abstraction;
		protected final N source;
		
		private AbstractionWithSourceStmt(D abstraction, N source) {
			this.abstraction = abstraction;
			this.source = source;
		}


		protected final D abstraction;protectedfinalDabstraction;		protected final N source;protectedfinalNsource;				private AbstractionWithSourceStmt(D abstraction, N source) {privateAbstractionWithSourceStmt(Dabstraction,Nsource){			this.abstraction = abstraction;this.abstraction=abstraction;			this.source = source;this.source=source;		}}




made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




196



		public D getAbstraction() {






made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013



made bidi solver extensible

 

made bidi solver extensible

Eric Bodden
committed
Jul 23, 2013


196


		public D getAbstraction() {

		public D getAbstraction() {publicDgetAbstraction(){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




197


198


199



			return abstraction;
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


197


198


199


			return abstraction;
		}
		

			return abstraction;returnabstraction;		}}		




made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013




200



		public N getSourceStmt() {






made bidi solver extensible

 


Eric Bodden
committed
Jul 23, 2013



made bidi solver extensible

 

made bidi solver extensible

Eric Bodden
committed
Jul 23, 2013


200


		public N getSourceStmt() {

		public N getSourceStmt() {publicNgetSourceStmt(){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




201


202


203


204


205



			return source;
		}	
		
		@Override
		public String toString() {






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


201


202


203


204


205


			return source;
		}	
		
		@Override
		public String toString() {

			return source;returnsource;		}	}				@Override@Override		public String toString() {publicStringtoString(){




toString

 


Eric Bodden
committed
Jul 06, 2013




206


207


208


209



			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();






toString

 


Eric Bodden
committed
Jul 06, 2013



toString

 

toString

Eric Bodden
committed
Jul 06, 2013


206


207


208


209


			if(source!=null)
				return ""+abstraction+"-@-"+source+"";
			else
				return abstraction.toString();

			if(source!=null)if(source!=null)				return ""+abstraction+"-@-"+source+"";return""+abstraction+"-@-"+source+"";			elseelse				return abstraction.toString();returnabstraction.toString();




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




210



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


210


		}

		}}




bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;






bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013



bugfix: must implement equals/hashCode for abstraction!

 

bugfix: must implement equals/hashCode for abstraction!

Eric Bodden
committed
Jul 08, 2013


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


228



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;

		@Override@Override		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = 1;intresult=1;			result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());result=prime*result+((abstraction==null)?0:abstraction.hashCode());			result = prime * result + ((source == null) ? 0 : source.hashCode());result=prime*result+((source==null)?0:source.hashCode());			return result;returnresult;		}}		@Override@Override		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (obj == null)if(obj==null)				return false;returnfalse;			if (getClass() != obj.getClass())if(getClass()!=obj.getClass())				return false;returnfalse;




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




229



			@SuppressWarnings("unchecked")






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


229


			@SuppressWarnings("unchecked")

			@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")




bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013




230


231


232


233


234


235


236


237


238


239


240


241


242



			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}






bugfix: must implement equals/hashCode for abstraction!

 


Eric Bodden
committed
Jul 08, 2013



bugfix: must implement equals/hashCode for abstraction!

 

bugfix: must implement equals/hashCode for abstraction!

Eric Bodden
committed
Jul 08, 2013


230


231


232


233


234


235


236


237


238


239


240


241


242


			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
			if (abstraction == null) {
				if (other.abstraction != null)
					return false;
			} else if (!abstraction.equals(other.abstraction))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}

			AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;AbstractionWithSourceStmtother=(AbstractionWithSourceStmt)obj;			if (abstraction == null) {if(abstraction==null){				if (other.abstraction != null)if(other.abstraction!=null)					return false;returnfalse;			} else if (!abstraction.equals(other.abstraction))}elseif(!abstraction.equals(other.abstraction))				return false;returnfalse;			if (source == null) {if(source==null){				if (other.source != null)if(other.source!=null)					return false;returnfalse;			} else if (!source.equals(other.source))}elseif(!source.equals(other.source))				return false;returnfalse;			return true;returntrue;		}}




bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




243


244




		@Override






bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013



bidisolver now supports summarizing abstractions

 

bidisolver now supports summarizing abstractions

Eric Bodden
committed
Jul 23, 2013


243


244



		@Override

		@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




245


246



		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


245


246


		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {
			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());

		public void addNeighbor(AbstractionWithSourceStmt originalAbstraction) {publicvoidaddNeighbor(AbstractionWithSourceStmtoriginalAbstraction){			getAbstraction().addNeighbor(originalAbstraction.getAbstraction());getAbstraction().addNeighbor(originalAbstraction.getAbstraction());




bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013




247


248



		}







bidisolver now supports summarizing abstractions

 


Eric Bodden
committed
Jul 23, 2013



bidisolver now supports summarizing abstractions

 

bidisolver now supports summarizing abstractions

Eric Bodden
committed
Jul 23, 2013


247


248


		}


		}}




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




249


250



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


249


250


	}
	

	}}	




comments

 


Eric Bodden
committed
Jul 09, 2013




251


252


253



	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


251


252


253


	/**
	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.
	 */

	/**/**	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.	 * This tabulation problem simply propagates augmented abstractions where the normal problem would propagate normal abstractions.	 */	 */




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




254



	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


254


	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {

	private class AugmentedTabulationProblem implements IFDSTabulationProblem<N, AbstractionWithSourceStmt,M,I> {privateclassAugmentedTabulationProblemimplementsIFDSTabulationProblem<N,AbstractionWithSourceStmt,M,I>{




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




255


256




		private final IFDSTabulationProblem<N,D,M,I> delegate;






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


255


256



		private final IFDSTabulationProblem<N,D,M,I> delegate;

		private final IFDSTabulationProblem<N,D,M,I> delegate;privatefinalIFDSTabulationProblem<N,D,M,I>delegate;




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




257



		private final AbstractionWithSourceStmt ZERO;






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


257


		private final AbstractionWithSourceStmt ZERO;

		private final AbstractionWithSourceStmt ZERO;privatefinalAbstractionWithSourceStmtZERO;




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




258


259


260


261


262



		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


258


259


260


261


262


		private final FlowFunctions<N, D, M> originalFunctions;
		
		public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {
			this.delegate = delegate;
			originalFunctions = this.delegate.flowFunctions();

		private final FlowFunctions<N, D, M> originalFunctions;privatefinalFlowFunctions<N,D,M>originalFunctions;				public AugmentedTabulationProblem(IFDSTabulationProblem<N, D, M, I> delegate) {publicAugmentedTabulationProblem(IFDSTabulationProblem<N,D,M,I>delegate){			this.delegate = delegate;this.delegate=delegate;			originalFunctions = this.delegate.flowFunctions();originalFunctions=this.delegate.flowFunctions();




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




263



			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


263


			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);

			ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);ZERO=newAbstractionWithSourceStmt(delegate.zeroValue(),null);




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




264


265


266



		}

		@Override






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


264


265


266


		}

		@Override

		}}		@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




267


268



		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


267


268


		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {
			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {

		public FlowFunctions<N, AbstractionWithSourceStmt, M> flowFunctions() {publicFlowFunctions<N,AbstractionWithSourceStmt,M>flowFunctions(){			return new FlowFunctions<N, AbstractionWithSourceStmt, M>() {returnnewFlowFunctions<N,AbstractionWithSourceStmt,M>(){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




269


270




				@Override






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


269


270



				@Override

				@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




271


272



				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


271


272


				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
					return new FlowFunction<AbstractionWithSourceStmt>() {

				public FlowFunction<AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {publicFlowFunction<AbstractionWithSourceStmt>getNormalFlowFunction(finalNcurr,finalNsucc){					return new FlowFunction<AbstractionWithSourceStmt>() {returnnewFlowFunction<AbstractionWithSourceStmt>(){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




273



						@Override






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


273


						@Override

						@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




274



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


274


						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {

						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {publicSet<AbstractionWithSourceStmt>computeTargets(AbstractionWithSourceStmtsource){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




275


276


277


278


279


280



							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


275


276


277


278


279


280


							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));
						}
					};
				}

				@Override

							return copyOverSourceStmts(source, originalFunctions.getNormalFlowFunction(curr, succ));returncopyOverSourceStmts(source,originalFunctions.getNormalFlowFunction(curr,succ));						}}					};};				}}				@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




281


282



				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


281


282


				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
					return new FlowFunction<AbstractionWithSourceStmt>() {

				public FlowFunction<AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {publicFlowFunction<AbstractionWithSourceStmt>getCallFlowFunction(finalNcallStmt,finalMdestinationMethod){					return new FlowFunction<AbstractionWithSourceStmt>() {returnnewFlowFunction<AbstractionWithSourceStmt>(){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




283



						@Override






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


283


						@Override

						@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




284



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


284


						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {

						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {publicSet<AbstractionWithSourceStmt>computeTargets(AbstractionWithSourceStmtsource){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




285


286


287


288


289


290



							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


285


286


287


288


289


290


							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));
						}
					};
				}

				@Override

							return copyOverSourceStmts(source, originalFunctions.getCallFlowFunction(callStmt, destinationMethod));returncopyOverSourceStmts(source,originalFunctions.getCallFlowFunction(callStmt,destinationMethod));						}}					};};				}}				@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




291


292



				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


291


292


				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {

				public FlowFunction<AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {publicFlowFunction<AbstractionWithSourceStmt>getReturnFlowFunction(finalNcallSite,finalMcalleeMethod,finalNexitStmt,finalNreturnSite){					return new FlowFunction<AbstractionWithSourceStmt>() {returnnewFlowFunction<AbstractionWithSourceStmt>(){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




293



						@Override






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


293


						@Override

						@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




294



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


294


						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {

						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {publicSet<AbstractionWithSourceStmt>computeTargets(AbstractionWithSourceStmtsource){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




295


296


297


298


299


300



							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


295


296


297


298


299


300


							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
						}
					};
				}

				@Override

							return copyOverSourceStmts(source, originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));returncopyOverSourceStmts(source,originalFunctions.getReturnFlowFunction(callSite,calleeMethod,exitStmt,returnSite));						}}					};};				}}				@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




301


302



				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


301


302


				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
					return new FlowFunction<AbstractionWithSourceStmt>() {

				public FlowFunction<AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {publicFlowFunction<AbstractionWithSourceStmt>getCallToReturnFlowFunction(finalNcallSite,finalNreturnSite){					return new FlowFunction<AbstractionWithSourceStmt>() {returnnewFlowFunction<AbstractionWithSourceStmt>(){




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




303



						@Override






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


303


						@Override

						@Override@Override




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




304



						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


304


						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {

						public Set<AbstractionWithSourceStmt> computeTargets(AbstractionWithSourceStmt source) {publicSet<AbstractionWithSourceStmt>computeTargets(AbstractionWithSourceStmtsource){




more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013




305


306


307



							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};






more advances on BiDi Solver

 


Eric Bodden
committed
Jul 05, 2013



more advances on BiDi Solver

 

more advances on BiDi Solver

Eric Bodden
committed
Jul 05, 2013


305


306


307


							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
						}
					};

							return copyOverSourceStmts(source, originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));returncopyOverSourceStmts(source,originalFunctions.getCallToReturnFlowFunction(callSite,returnSite));						}}					};};




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




308


309



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


308


309


				}
				

				}}				




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




310



				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


310


				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {

				private Set<AbstractionWithSourceStmt> copyOverSourceStmts(AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {privateSet<AbstractionWithSourceStmt>copyOverSourceStmts(AbstractionWithSourceStmtsource,FlowFunction<D>originalFunction){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




311


312


313


314


315


316



					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


311


312


313


314


315


316


					D originalAbstraction = source.getAbstraction();
					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);

					//optimization
					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); 
					

					D originalAbstraction = source.getAbstraction();DoriginalAbstraction=source.getAbstraction();					Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);Set<D>origTargets=originalFunction.computeTargets(originalAbstraction);					//optimization//optimization					if(origTargets.equals(Collections.singleton(originalAbstraction))) return Collections.singleton(source); if(origTargets.equals(Collections.singleton(originalAbstraction)))returnCollections.singleton(source);					




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




317



					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


317


					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();

					Set<AbstractionWithSourceStmt> res = new HashSet<AbstractionWithSourceStmt>();Set<AbstractionWithSourceStmt>res=newHashSet<AbstractionWithSourceStmt>();




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




318



					for(D d: origTargets) {






initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


318


					for(D d: origTargets) {

					for(D d: origTargets) {for(Dd:origTargets){




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




319



						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


319


						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));

						res.add(new AbstractionWithSourceStmt(d,source.getSourceStmt()));res.add(newAbstractionWithSourceStmt(d,source.getSourceStmt()));




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347



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







initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013



initial draft of BiDi Solver


initial draft of BiDi Solver

Eric Bodden
committed
Jul 05, 2013


320


321


322


323


324


325


326


327


328


329


330


331


332


333


334


335


336


337


338


339


340


341


342


343


344


345


346


347


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


					}}					return res;returnres;				}}			};};		}}				//delegate methods follow//delegate methods follow		public boolean followReturnsPastSeeds() {publicbooleanfollowReturnsPastSeeds(){			return delegate.followReturnsPastSeeds();returndelegate.followReturnsPastSeeds();		}}		public boolean autoAddZero() {publicbooleanautoAddZero(){			return delegate.autoAddZero();returndelegate.autoAddZero();		}}		public int numThreads() {publicintnumThreads(){			return delegate.numThreads();returndelegate.numThreads();		}}		public boolean computeValues() {publicbooleancomputeValues(){			return delegate.computeValues();returndelegate.computeValues();		}}		public I interproceduralCFG() {publicIinterproceduralCFG(){			return delegate.interproceduralCFG();returndelegate.interproceduralCFG();		}}




updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




348


349



		/* attaches the original seed statement to the abstraction
		 */






updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement

 

updated initialSeeds to include source statement

Eric Bodden
committed
Jul 06, 2013


348


349


		/* attaches the original seed statement to the abstraction
		 */

		/* attaches the original seed statement to the abstraction/* attaches the original seed statement to the abstraction		 */		 */




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




350



		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


350


		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {

		public Map<N,Set<AbstractionWithSourceStmt>> initialSeeds() {publicMap<N,Set<AbstractionWithSourceStmt>>initialSeeds(){




updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




351



			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();






updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement

 

updated initialSeeds to include source statement

Eric Bodden
committed
Jul 06, 2013


351


			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();

			Map<N, Set<D>> originalSeeds = delegate.initialSeeds();Map<N,Set<D>>originalSeeds=delegate.initialSeeds();




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




352



			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


352


			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();

			Map<N,Set<AbstractionWithSourceStmt>> res = new HashMap<N, Set<AbstractionWithSourceStmt>>();Map<N,Set<AbstractionWithSourceStmt>>res=newHashMap<N,Set<AbstractionWithSourceStmt>>();




updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




353


354


355



			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();






updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement

 

updated initialSeeds to include source statement

Eric Bodden
committed
Jul 06, 2013


353


354


355


			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {
				N stmt = entry.getKey();
				Set<D> seeds = entry.getValue();

			for(Entry<N, Set<D>> entry: originalSeeds.entrySet()) {for(Entry<N,Set<D>>entry:originalSeeds.entrySet()){				N stmt = entry.getKey();Nstmt=entry.getKey();				Set<D> seeds = entry.getValue();Set<D>seeds=entry.getValue();




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




356



				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


356


				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();

				Set<AbstractionWithSourceStmt> resSet = new HashSet<AbstractionWithSourceStmt>();Set<AbstractionWithSourceStmt>resSet=newHashSet<AbstractionWithSourceStmt>();




updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




357


358



				for (D d : seeds) {
					//attach source stmt to abstraction






updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement

 

updated initialSeeds to include source statement

Eric Bodden
committed
Jul 06, 2013


357


358


				for (D d : seeds) {
					//attach source stmt to abstraction

				for (D d : seeds) {for(Dd:seeds){					//attach source stmt to abstraction//attach source stmt to abstraction




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




359



					resSet.add(new AbstractionWithSourceStmt(d, stmt));






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


359


					resSet.add(new AbstractionWithSourceStmt(d, stmt));

					resSet.add(new AbstractionWithSourceStmt(d, stmt));resSet.add(newAbstractionWithSourceStmt(d,stmt));




updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013




360


361


362


363



				}
				res.put(stmt, resSet);
			}			
			return res;






updated initialSeeds to include source statement

 


Eric Bodden
committed
Jul 06, 2013



updated initialSeeds to include source statement

 

updated initialSeeds to include source statement

Eric Bodden
committed
Jul 06, 2013


360


361


362


363


				}
				res.put(stmt, resSet);
			}			
			return res;

				}}				res.put(stmt, resSet);res.put(stmt,resSet);			}			}			return res;returnres;




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




364


365



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


364


365


		}


		}}




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




366



		public AbstractionWithSourceStmt zeroValue() {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


366


		public AbstractionWithSourceStmt zeroValue() {

		public AbstractionWithSourceStmt zeroValue() {publicAbstractionWithSourceStmtzeroValue(){




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




367


368


369


370


371



			return ZERO;
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


367


368


369


370


371


			return ZERO;
		}

	}
	

			return ZERO;returnZERO;		}}	}}	




comments

 


Eric Bodden
committed
Jul 09, 2013




372


373


374


375


376


377


378


379


380



	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}







comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


372


373


374


375


376


377


378


379


380


	public Set<D> fwIFDSResultAt(N stmt) {
		return extractResults(fwSolver.ifdsResultsAt(stmt));
	}

	
	public Set<D> bwIFDSResultAt(N stmt) {
		return extractResults(bwSolver.ifdsResultsAt(stmt));
	}


	public Set<D> fwIFDSResultAt(N stmt) {publicSet<D>fwIFDSResultAt(Nstmt){		return extractResults(fwSolver.ifdsResultsAt(stmt));returnextractResults(fwSolver.ifdsResultsAt(stmt));	}}		public Set<D> bwIFDSResultAt(N stmt) {publicSet<D>bwIFDSResultAt(Nstmt){		return extractResults(bwSolver.ifdsResultsAt(stmt));returnextractResults(bwSolver.ifdsResultsAt(stmt));	}}




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




381



	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


381


	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {

	private Set<D> extractResults(Set<AbstractionWithSourceStmt> annotatedResults) {privateSet<D>extractResults(Set<AbstractionWithSourceStmt>annotatedResults){




comments

 


Eric Bodden
committed
Jul 09, 2013




382



		Set<D> res = new HashSet<D>();		






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


382


		Set<D> res = new HashSet<D>();		

		Set<D> res = new HashSet<D>();		Set<D>res=newHashSet<D>();




added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013




383



		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {






added a PathTrackingIFDSSolver which implements the keeping track of paths;

 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;

 

added a PathTrackingIFDSSolver which implements the keeping track of paths;

Eric Bodden
committed
Oct 18, 2013


383


		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {

		for (AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {for(AbstractionWithSourceStmtabstractionWithSourceStmt:annotatedResults){




comments

 


Eric Bodden
committed
Jul 09, 2013




384


385


386


387


388



			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	






comments

 


Eric Bodden
committed
Jul 09, 2013



comments

 

comments

Eric Bodden
committed
Jul 09, 2013


384


385


386


387


388


			res.add(abstractionWithSourceStmt.getAbstraction());
		}
		return res;
	}
	

			res.add(abstractionWithSourceStmt.getAbstraction());res.add(abstractionWithSourceStmt.getAbstraction());		}}		return res;returnres;	}}	




initial draft of BiDi Solver



Eric Bodden
committed
Jul 05, 2013




389



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


389


}
}}





