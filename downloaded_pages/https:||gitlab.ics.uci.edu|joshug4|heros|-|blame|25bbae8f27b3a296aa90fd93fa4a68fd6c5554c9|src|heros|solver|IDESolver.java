



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

25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9

















25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



30.1 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






3




 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






4




5




6




7




8




9




10




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






11




 *     Marc-Andre Laverdiere-Papineau - Fixed race condition









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






12




 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15















renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.EdgeFunctionCache;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.edgefunc.EdgeIdentity;










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




import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






37




import java.util.concurrent.LinkedBlockingQueue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




39




40




41




42




43




44




45




46




import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






47














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 
 * Note that this solver and its data structures internally use mostly {@link LinkedHashSet}s
 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






58




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






59




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






60




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






61




62




63




64




65




66




67




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









replaced env variable by property


 

 


Eric Bodden
committed
Jan 24, 2013






68




	public static final boolean DEBUG = !System.getProperty("HEROS_DEBUG", "false").equals("false");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




71




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






72




	protected CountingThreadPoolExecutor executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






73




74




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






75




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






76




77




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






78




	protected final JumpFunctions<N,D,V> jumpFn;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






79




80




	
	@SynchronizedBy("thread safe data structure, only modified internally")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






81




	protected final I icfg;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






82




83




84




85




	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






86




	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






87




88




89




90





	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






91




	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






92




93




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






94




	protected final FlowFunctions<N, D, M> flowFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






95




96





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






97




	protected final EdgeFunctions<N,D,M,V> edgeFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






98




99





	@DontSynchronize("only used by single thread")









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






100




	protected final Map<N,Set<D>> initialSeeds;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






101




102





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






103




	protected final JoinLattice<V> valueLattice;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






104




105




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






106




	protected final EdgeFunction<V> allTop;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107














adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






108




	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






109




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




	
	@DontSynchronize("benign races")
	public long flowFunctionApplicationCount;

	@DontSynchronize("benign races")
	public long flowFunctionConstructionCount;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionConstruction;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionApplication;

	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






127




	protected final D zeroValue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






128




129




	
	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






130




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






131




132





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






133




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






134














making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






135




136




	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






137














make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






138




139




140




	@DontSynchronize("readOnly")
	protected final boolean computeValues;










added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






141




142




143




144




	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






145




146




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






147




148




	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






149




150




151




152




153




154




	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






155




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






156




157




158




159




160




		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






161




		this.icfg = tabulationProblem.interproceduralCFG();		









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






162




163




		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




178




179




180




181




182




		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();
		if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}
		if(edgeFunctionCacheBuilder!=null) {
			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);
			edgeFunctions = efCache;
		} else {
			efCache = null;
		}
		this.flowFunctions = flowFunctions;
		this.edgeFunctions = edgeFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.valueLattice = tabulationProblem.joinLattice();
		this.allTop = tabulationProblem.allTopFunction();
		this.jumpFn = new JumpFunctions<N,D,V>(allTop);









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






183




		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






184




		this.numThreads = Math.max(1,tabulationProblem.numThreads());









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






185




		this.computeValues = tabulationProblem.computeValues();









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






186




		this.executor = getExecutor();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






187




188




189




190




191




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






192




	public void solve() {		









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






193




194




195




196




197




198




199




200




		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






201




	 */









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






202




	protected void submitInitialSeeds() {









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






203




204




205




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






206




				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






207




			}









extracted method awaitCompletionComputeValuesAndShutdown()


 

 


Eric Bodden
committed
Jan 30, 2013






208




209




210




211




212




213




214




215




216




217




			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






218




			final long before = System.currentTimeMillis();









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






219




220




			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






221




222




			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






223




		if(computeValues) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






224




225




226




227




228




229




230




			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






231




232




233




		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






234




		executor.shutdown();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






235




236




		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






237




238




239




240




241




242




243




		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






244




		try {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






245




			executor.awaitCompletion();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






246




		} catch (InterruptedException e) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






247




248




249




250




251




			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






252




		}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






253




254




	}










refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






255




256




257




258




    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






259




    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






260




261




    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






262




    }









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






263




	









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






264




265




266




267




268




    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






269




    	executor.execute(vpt);









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






270




271




    }
  









comments


 

 


Eric Bodden
committed
Jan 28, 2013






272




273




274




275




    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






276




277




278




	private void scheduleValueComputationTask(ValueComputationTask task) {
		executor.execute(task);
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






279




280




	
	/**









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






281




282




	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






283




284




	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






285




	 * 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






286




287




	 * @param edge an edge whose target node resembles a method call
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






288




	private void processCall(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






289




290




291




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






292




293




		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






294




		









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






295




		//for each possible callee









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






296




297




		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






298




299




			
			//compute the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






300




301




302




			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






303




304




			
			//for each callee's start point(s)









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






305




			Set<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






306




			for(N sP: startPointsOf) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






307




				//for each result node of the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






308




				for(D d3: res) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






309




					//create initial self-loop









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






310




					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






311




	









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






312




					//register the fact that <sp,d3> has an incoming edge from <n,d2>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






313




314




315




316




317




					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






318




319




320




						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));
						
						assert !jumpFn.reverseLookup(n, d2).isEmpty();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






321




322




323




					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






324




					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






325




					//create new caller-side jump functions to the return sites









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






326




					//because we have observed a potentially new incoming edge into <sP,d3>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






327




328




329




330




					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






331




						//for each return site









Replaced the duplicate call to the icfg by an access to cached structure we have anyway


 

 


Steven Arzt
committed
Mar 11, 2013






332




						for(N retSiteN: returnSiteNs) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






333




							//compute return-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






334




335




							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






336




							//for each target value of the function









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






337




							for(D d5: computeReturnFlowFunction(retFunction, d4, Collections.singleton(d1))) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






338




								//update the caller-side summary function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






339




340




								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






341




								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);							









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






342




								propagate(d1, retSiteN, d5, f.composeWith(fPrime), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






343




344




345




346




347




348




							}
						}
					}
				}		
			}
		}









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






349




350




		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






351




352




353




		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






354




			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






355




				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






356




				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






357




358




359




360




361




			}
		}
	}

	/**









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






362




363




364




365




366




367




368




369




370




371




372




373




374




375




376




	 * Computes the call-to-return flow function for the given call-site
	 * asbtraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the return size
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}

	/**
	 * Lines 21-32 of the algorithm.









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






377




378




379




380




381




382




	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






383




	 */









Merge branch 'develop' into forks/java-fw-bw


 

 


Eric Bodden
committed
Jul 06, 2013






384




	protected void processExit(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






385




386




387




388




389




390




391




		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






392




393




394




395




		//for each of the method's start points, determine incoming calls
		Set<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);
		Set<Entry<N,Set<D>>> inc = new HashSet<Map.Entry<N,Set<D>>>();
		for(N sP: startPointsOf) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






396




397




			//line 21.1 of Naeem/Lhotak/Rodriguez
			









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






398




			//register end-summary









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






399




400




401




			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






402




403




404




405




406




407




408




409




410




411




412




413




414




415




				inc.addAll(incoming(d1, sP));
			}	
		}
		
		//for each incoming call edge already processed
		//(see processCall(..))
		for (Entry<N,Set<D>> entry: inc) {
			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






416




				Set<D> targets = computeReturnFlowFunction(retFunction, d2, entry.getValue());









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






417




418




419




420




421




422




423




424




425




426




427




428




429




430




				//for each incoming-call value
				for(D d4: entry.getValue()) {
					//for each target value at the return site
					//line 23
					for(D d5: targets) {
						//compute composed function
						EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
						EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
						EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);
						//for each jump function coming into the call, propagate to return site using the composed function
						for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
							EdgeFunction<V> f3 = valAndFunc.getValue();
							if(!f3.equalTo(allTop)) {
								D d3 = valAndFunc.getKey();









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






431




								propagate(d3, retSiteC, d5, f3.composeWith(fPrime), c, false);









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






432




433




434




435




							}
						}
					}
				}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






436




			}









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






437




438




		}
		









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






439




		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow









further fix for followReturnPastSeeds:


 

 


Eric Bodden
committed
Jul 08, 2013






440




441




442




		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






443




			// only propagate up if we 









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






444




445




446




447




448




449




450




451




				Set<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;
						Set<D> targets = retFunction.computeTargets(d2);
						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






452




							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






453




						}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






454




455




					}
				}









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






456




457




458




459




460




461




462




463




				//in cases where there are no callers, the return statement would normally not be processed at all;
				//this might be undesirable if the flow function has a side effect such as registering a taint;
				//instead we thus call the return flow function will a null caller
				if(callers.isEmpty()) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
					flowFunctionConstructionCount++;
					retFunction.computeTargets(d2);
				}









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






464




			}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






465




466




		}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






467




468




469




470




471




472




473




474




475




476




477




478




479




	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callerSideD1s The abstractions at the callers' start nodes.
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction
			(FlowFunction<D> retFunction, D d2, Set<D> callerSideD1s) {
		return retFunction.computeTargets(d2);
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






480




481




	/**
	 * Lines 33-37 of the algorithm.









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






482




	 * Simply propagate normal, intra-procedural flows.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






483




484




	 * @param edge
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






485




	private void processNormalFlow(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






486




487




488




489




490




491




492




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






493




			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






494




495




			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






496




				propagate(d1, m, d3, fprime, null, false); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






497




498




499




500




			}
		}
	}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






501




502




503




504




505




506




507




508




509




510




511




512




513




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions-
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */
	protected Set<D> computeNormalFlowFunction
			(FlowFunction<D> flowFunction, D d1, D d2) {
		return flowFunction.computeTargets(d2);
	}










changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






514




515




516




517




518




519




520




521




522




	/**
	 * Propagates the flow further down the exploded super graph, merging any edge function that might
	 * already have been computed for targetVal at target. 
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






523




524




	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






525




	 */









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






526




527




528




	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






529




		EdgeFunction<V> jumpFnE;









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






530




531




		EdgeFunction<V> fPrime;
		boolean newFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






532




533




		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






534




535




536




537




			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






538




539




				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






540




541




542




		}

		if(newFunction) {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






543




			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






544




			scheduleEdgeProcessing(edge);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






545




546




547




548





			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






549




550




					result.append(getDebugName());
					result.append(": ");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






551




552




553




554




555




556




557




558




559




560




561




562




563




564




565




566




					result.append("EDGE:  <");
					result.append(icfg.getMethodOf(target));
					result.append(",");
					result.append(sourceVal);
					result.append("> -> <");
					result.append(target);
					result.append(",");
					result.append(targetVal);
					result.append("> - ");
					result.append(fPrime);
					System.err.println(result.toString());
				}
			}
		}
	}
	









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






567




568




569




570




571




	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






572




573




574




575




576




577




578




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {
				setVal(startPoint, val, valueLattice.bottomElement());
				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); 
				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
			}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






579




		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






580




		









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






581




582




583




584




585




586




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






587




		









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






588




589




590




591




592




593




594




595




596




		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






597




598




		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






599




		for(int t=0;t<numThreads; t++) {









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






600




601




			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






602




		}









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






603




604




605




606




607




608




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






609




610




611




612




613




614




615




616




617




618




619




620




621




622




623




624




625




626




627




628




629




630




631




632




633




634




635




636




637




638




639




640




641




642




643




644




645




646




647




648




649




	}

	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();		
		M p = icfg.getMethodOf(n);
		for(N c: icfg.getCallsFromWithin(p)) {					
			Set<Entry<D, EdgeFunction<V>>> entries; 
			synchronized (jumpFn) {
				entries = jumpFn.forwardLookup(d,c).entrySet();
				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {
					D dPrime = dPAndFP.getKey();
					EdgeFunction<V> fPrime = dPAndFP.getValue();
					N sP = n;
					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValueAtCall(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();
		for(M q: icfg.getCalleesOfCallAt(n)) {
			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);
			flowFunctionConstructionCount++;
			for(D dPrime: callFlowFunction.computeTargets(d)) {
				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);
				for(N startPoint: icfg.getStartPointsOf(q)) {
					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValue(N nHashN, D nHashD, V v) {
		synchronized (val) {
			V valNHash = val(nHashN, nHashD);
			V vPrime = valueLattice.join(valNHash,v);
			if(!vPrime.equals(valNHash)) {
				setVal(nHashN, nHashD, vPrime);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






650




				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






651




652




653




654




655




			}
		}
	}

	private V val(N nHashN, D nHashD){ 









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






656




657




658




659




		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






660




661




662




663




		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






664




665




	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






666




		synchronized (val) {









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






667




668




669




670




			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






671




		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






672




673




674




675




		if(DEBUG)
			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);
	}










removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






676




	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






677




678




679




680




681




682




683




		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






684




685




686




687




688




689




690




691




692




693




694




695




	private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {
		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);
		if(map==null) return Collections.emptySet();
		return map.cellSet();
	}

	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {
		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);
		if(summaries==null) {
			summaries = HashBasedTable.create();
			endSummary.put(sP, d1, summaries);
		}









undoing previous "fix"; as discussed with Steven, it is not required (see comment)


 

 


Eric Bodden
committed
Dec 12, 2012






696




697




698




699




		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






700




701




702




703




704




705




706




707




708




709




710




711




712




713




714




715




716




717




718




719




720




721




722




	}	
	
	private Set<Entry<N, Set<D>>> incoming(D d1, N sP) {
		Map<N, Set<D>> map = incoming.get(sP, d1);
		if(map==null) return Collections.emptySet();
		return map.entrySet();		
	}
	
	private void addIncoming(N sP, D d3, N n, D d2) {
		Map<N, Set<D>> summaries = incoming.get(sP, d3);
		if(summaries==null) {
			summaries = new HashMap<N, Set<D>>();
			incoming.put(sP, d3, summaries);
		}
		Set<D> set = summaries.get(n);
		if(set==null) {
			set = new HashSet<D>();
			summaries.put(n,set);
		}
		set.add(d2);
	}	
	
	/**









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






723




724




	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






725




726




	 */
	public V resultAt(N stmt, D value) {









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






727




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






728




729




730




731




732




		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






733




734




	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






735




736




737




	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






738




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






739




740




741




742




743




744




745




		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






746




747




748




749




750




751




752




	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






753




	









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






754




755




756




757




758




759




760




761




	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "";
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






762




763




764




765




766




767




768




769




770




771




772




773




	public void printStats() {
		if(DEBUG) {
			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {
			System.err.println("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






774




		private final PathEdge<N,D> edge;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






775














removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






776




		public PathEdgeProcessingTask(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






777




778




779




780




781




782




783




784




785




786




787




788




789




790




791




792




793




794




795




796




797




798




799




800




801




802




803




804




			this.edge = edge;
		}

		public void run() {
			if(icfg.isCallStmt(edge.getTarget())) {
				processCall(edge);
			} else {
				//note that some statements, such as "throw" may be
				//both an exit statement and a "normal" statement
				if(icfg.isExitStmt(edge.getTarget())) {
					processExit(edge);
				}
				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {
					processNormalFlow(edge);
				}
			}
		}
	}
	
	private class ValuePropagationTask implements Runnable {
		private final Pair<N, D> nAndD;

		public ValuePropagationTask(Pair<N,D> nAndD) {
			this.nAndD = nAndD;
		}

		public void run() {
			N n = nAndD.getO1();









bug fix for value computation (need to treat initialSeeds just as method start nodes)


 

 


Eric Bodden
committed
Feb 14, 2013






805




			if(icfg.isStartPoint(n) ||









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






806




				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






807




808




809




810




811




812




813




814




815




816




817




818




819




820




821




822




823




824




825




826




827




828




829




830




831




832




833




834




835




836




837




838




839




840




841




842




843




844




845




				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
		}
	}
	
	private class ValueComputationTask implements Runnable {
		private final N[] values;
		final int num;

		public ValueComputationTask(N[] values, int num) {
			this.values = values;
			this.num = num;
		}

		public void run() {
			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;
			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {
				N n = values[i];
				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					
					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;
					lookupByTarget = jumpFn.lookupByTarget(n);
					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {
						D dPrime = sourceValTargetValAndFunction.getRowKey();
						D d = sourceValTargetValAndFunction.getColumnKey();
						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();
						synchronized (val) {
							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));
						}
						flowFunctionApplicationCount++;
					}
				}
			}
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

25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9

















25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



30.1 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






3




 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






4




5




6




7




8




9




10




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






11




 *     Marc-Andre Laverdiere-Papineau - Fixed race condition









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






12




 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15















renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.EdgeFunctionCache;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.edgefunc.EdgeIdentity;










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




import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






37




import java.util.concurrent.LinkedBlockingQueue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




39




40




41




42




43




44




45




46




import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






47














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 
 * Note that this solver and its data structures internally use mostly {@link LinkedHashSet}s
 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






58




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






59




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






60




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






61




62




63




64




65




66




67




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









replaced env variable by property


 

 


Eric Bodden
committed
Jan 24, 2013






68




	public static final boolean DEBUG = !System.getProperty("HEROS_DEBUG", "false").equals("false");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




71




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






72




	protected CountingThreadPoolExecutor executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






73




74




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






75




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






76




77




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






78




	protected final JumpFunctions<N,D,V> jumpFn;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






79




80




	
	@SynchronizedBy("thread safe data structure, only modified internally")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






81




	protected final I icfg;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






82




83




84




85




	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






86




	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






87




88




89




90





	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






91




	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






92




93




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






94




	protected final FlowFunctions<N, D, M> flowFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






95




96





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






97




	protected final EdgeFunctions<N,D,M,V> edgeFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






98




99





	@DontSynchronize("only used by single thread")









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






100




	protected final Map<N,Set<D>> initialSeeds;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






101




102





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






103




	protected final JoinLattice<V> valueLattice;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






104




105




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






106




	protected final EdgeFunction<V> allTop;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107














adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






108




	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






109




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




	
	@DontSynchronize("benign races")
	public long flowFunctionApplicationCount;

	@DontSynchronize("benign races")
	public long flowFunctionConstructionCount;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionConstruction;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionApplication;

	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






127




	protected final D zeroValue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






128




129




	
	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






130




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






131




132





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






133




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






134














making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






135




136




	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






137














make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






138




139




140




	@DontSynchronize("readOnly")
	protected final boolean computeValues;










added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






141




142




143




144




	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






145




146




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






147




148




	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






149




150




151




152




153




154




	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






155




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






156




157




158




159




160




		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






161




		this.icfg = tabulationProblem.interproceduralCFG();		









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






162




163




		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




178




179




180




181




182




		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();
		if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}
		if(edgeFunctionCacheBuilder!=null) {
			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);
			edgeFunctions = efCache;
		} else {
			efCache = null;
		}
		this.flowFunctions = flowFunctions;
		this.edgeFunctions = edgeFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.valueLattice = tabulationProblem.joinLattice();
		this.allTop = tabulationProblem.allTopFunction();
		this.jumpFn = new JumpFunctions<N,D,V>(allTop);









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






183




		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






184




		this.numThreads = Math.max(1,tabulationProblem.numThreads());









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






185




		this.computeValues = tabulationProblem.computeValues();









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






186




		this.executor = getExecutor();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






187




188




189




190




191




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






192




	public void solve() {		









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






193




194




195




196




197




198




199




200




		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






201




	 */









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






202




	protected void submitInitialSeeds() {









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






203




204




205




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






206




				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






207




			}









extracted method awaitCompletionComputeValuesAndShutdown()


 

 


Eric Bodden
committed
Jan 30, 2013






208




209




210




211




212




213




214




215




216




217




			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






218




			final long before = System.currentTimeMillis();









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






219




220




			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






221




222




			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






223




		if(computeValues) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






224




225




226




227




228




229




230




			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






231




232




233




		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






234




		executor.shutdown();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






235




236




		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






237




238




239




240




241




242




243




		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






244




		try {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






245




			executor.awaitCompletion();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






246




		} catch (InterruptedException e) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






247




248




249




250




251




			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






252




		}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






253




254




	}










refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






255




256




257




258




    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






259




    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






260




261




    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






262




    }









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






263




	









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






264




265




266




267




268




    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






269




    	executor.execute(vpt);









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






270




271




    }
  









comments


 

 


Eric Bodden
committed
Jan 28, 2013






272




273




274




275




    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






276




277




278




	private void scheduleValueComputationTask(ValueComputationTask task) {
		executor.execute(task);
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






279




280




	
	/**









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






281




282




	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






283




284




	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






285




	 * 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






286




287




	 * @param edge an edge whose target node resembles a method call
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






288




	private void processCall(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






289




290




291




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






292




293




		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






294




		









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






295




		//for each possible callee









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






296




297




		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






298




299




			
			//compute the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






300




301




302




			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






303




304




			
			//for each callee's start point(s)









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






305




			Set<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






306




			for(N sP: startPointsOf) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






307




				//for each result node of the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






308




				for(D d3: res) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






309




					//create initial self-loop









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






310




					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






311




	









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






312




					//register the fact that <sp,d3> has an incoming edge from <n,d2>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






313




314




315




316




317




					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






318




319




320




						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));
						
						assert !jumpFn.reverseLookup(n, d2).isEmpty();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






321




322




323




					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






324




					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






325




					//create new caller-side jump functions to the return sites









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






326




					//because we have observed a potentially new incoming edge into <sP,d3>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






327




328




329




330




					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






331




						//for each return site









Replaced the duplicate call to the icfg by an access to cached structure we have anyway


 

 


Steven Arzt
committed
Mar 11, 2013






332




						for(N retSiteN: returnSiteNs) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






333




							//compute return-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






334




335




							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






336




							//for each target value of the function









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






337




							for(D d5: computeReturnFlowFunction(retFunction, d4, Collections.singleton(d1))) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






338




								//update the caller-side summary function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






339




340




								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






341




								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);							









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






342




								propagate(d1, retSiteN, d5, f.composeWith(fPrime), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






343




344




345




346




347




348




							}
						}
					}
				}		
			}
		}









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






349




350




		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






351




352




353




		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






354




			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






355




				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






356




				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






357




358




359




360




361




			}
		}
	}

	/**









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






362




363




364




365




366




367




368




369




370




371




372




373




374




375




376




	 * Computes the call-to-return flow function for the given call-site
	 * asbtraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the return size
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}

	/**
	 * Lines 21-32 of the algorithm.









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






377




378




379




380




381




382




	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






383




	 */









Merge branch 'develop' into forks/java-fw-bw


 

 


Eric Bodden
committed
Jul 06, 2013






384




	protected void processExit(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






385




386




387




388




389




390




391




		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






392




393




394




395




		//for each of the method's start points, determine incoming calls
		Set<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);
		Set<Entry<N,Set<D>>> inc = new HashSet<Map.Entry<N,Set<D>>>();
		for(N sP: startPointsOf) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






396




397




			//line 21.1 of Naeem/Lhotak/Rodriguez
			









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






398




			//register end-summary









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






399




400




401




			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






402




403




404




405




406




407




408




409




410




411




412




413




414




415




				inc.addAll(incoming(d1, sP));
			}	
		}
		
		//for each incoming call edge already processed
		//(see processCall(..))
		for (Entry<N,Set<D>> entry: inc) {
			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






416




				Set<D> targets = computeReturnFlowFunction(retFunction, d2, entry.getValue());









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






417




418




419




420




421




422




423




424




425




426




427




428




429




430




				//for each incoming-call value
				for(D d4: entry.getValue()) {
					//for each target value at the return site
					//line 23
					for(D d5: targets) {
						//compute composed function
						EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
						EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
						EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);
						//for each jump function coming into the call, propagate to return site using the composed function
						for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
							EdgeFunction<V> f3 = valAndFunc.getValue();
							if(!f3.equalTo(allTop)) {
								D d3 = valAndFunc.getKey();









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






431




								propagate(d3, retSiteC, d5, f3.composeWith(fPrime), c, false);









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






432




433




434




435




							}
						}
					}
				}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






436




			}









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






437




438




		}
		









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






439




		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow









further fix for followReturnPastSeeds:


 

 


Eric Bodden
committed
Jul 08, 2013






440




441




442




		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






443




			// only propagate up if we 









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






444




445




446




447




448




449




450




451




				Set<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;
						Set<D> targets = retFunction.computeTargets(d2);
						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






452




							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






453




						}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






454




455




					}
				}









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






456




457




458




459




460




461




462




463




				//in cases where there are no callers, the return statement would normally not be processed at all;
				//this might be undesirable if the flow function has a side effect such as registering a taint;
				//instead we thus call the return flow function will a null caller
				if(callers.isEmpty()) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
					flowFunctionConstructionCount++;
					retFunction.computeTargets(d2);
				}









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






464




			}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






465




466




		}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






467




468




469




470




471




472




473




474




475




476




477




478




479




	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callerSideD1s The abstractions at the callers' start nodes.
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction
			(FlowFunction<D> retFunction, D d2, Set<D> callerSideD1s) {
		return retFunction.computeTargets(d2);
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






480




481




	/**
	 * Lines 33-37 of the algorithm.









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






482




	 * Simply propagate normal, intra-procedural flows.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






483




484




	 * @param edge
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






485




	private void processNormalFlow(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






486




487




488




489




490




491




492




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






493




			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






494




495




			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






496




				propagate(d1, m, d3, fprime, null, false); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






497




498




499




500




			}
		}
	}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






501




502




503




504




505




506




507




508




509




510




511




512




513




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions-
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */
	protected Set<D> computeNormalFlowFunction
			(FlowFunction<D> flowFunction, D d1, D d2) {
		return flowFunction.computeTargets(d2);
	}










changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






514




515




516




517




518




519




520




521




522




	/**
	 * Propagates the flow further down the exploded super graph, merging any edge function that might
	 * already have been computed for targetVal at target. 
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






523




524




	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






525




	 */









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






526




527




528




	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






529




		EdgeFunction<V> jumpFnE;









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






530




531




		EdgeFunction<V> fPrime;
		boolean newFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






532




533




		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






534




535




536




537




			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






538




539




				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






540




541




542




		}

		if(newFunction) {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






543




			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






544




			scheduleEdgeProcessing(edge);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






545




546




547




548





			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






549




550




					result.append(getDebugName());
					result.append(": ");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






551




552




553




554




555




556




557




558




559




560




561




562




563




564




565




566




					result.append("EDGE:  <");
					result.append(icfg.getMethodOf(target));
					result.append(",");
					result.append(sourceVal);
					result.append("> -> <");
					result.append(target);
					result.append(",");
					result.append(targetVal);
					result.append("> - ");
					result.append(fPrime);
					System.err.println(result.toString());
				}
			}
		}
	}
	









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






567




568




569




570




571




	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






572




573




574




575




576




577




578




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {
				setVal(startPoint, val, valueLattice.bottomElement());
				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); 
				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
			}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






579




		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






580




		









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






581




582




583




584




585




586




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






587




		









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






588




589




590




591




592




593




594




595




596




		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






597




598




		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






599




		for(int t=0;t<numThreads; t++) {









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






600




601




			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






602




		}









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






603




604




605




606




607




608




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






609




610




611




612




613




614




615




616




617




618




619




620




621




622




623




624




625




626




627




628




629




630




631




632




633




634




635




636




637




638




639




640




641




642




643




644




645




646




647




648




649




	}

	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();		
		M p = icfg.getMethodOf(n);
		for(N c: icfg.getCallsFromWithin(p)) {					
			Set<Entry<D, EdgeFunction<V>>> entries; 
			synchronized (jumpFn) {
				entries = jumpFn.forwardLookup(d,c).entrySet();
				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {
					D dPrime = dPAndFP.getKey();
					EdgeFunction<V> fPrime = dPAndFP.getValue();
					N sP = n;
					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValueAtCall(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();
		for(M q: icfg.getCalleesOfCallAt(n)) {
			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);
			flowFunctionConstructionCount++;
			for(D dPrime: callFlowFunction.computeTargets(d)) {
				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);
				for(N startPoint: icfg.getStartPointsOf(q)) {
					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValue(N nHashN, D nHashD, V v) {
		synchronized (val) {
			V valNHash = val(nHashN, nHashD);
			V vPrime = valueLattice.join(valNHash,v);
			if(!vPrime.equals(valNHash)) {
				setVal(nHashN, nHashD, vPrime);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






650




				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






651




652




653




654




655




			}
		}
	}

	private V val(N nHashN, D nHashD){ 









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






656




657




658




659




		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






660




661




662




663




		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






664




665




	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






666




		synchronized (val) {









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






667




668




669




670




			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






671




		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






672




673




674




675




		if(DEBUG)
			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);
	}










removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






676




	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






677




678




679




680




681




682




683




		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






684




685




686




687




688




689




690




691




692




693




694




695




	private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {
		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);
		if(map==null) return Collections.emptySet();
		return map.cellSet();
	}

	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {
		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);
		if(summaries==null) {
			summaries = HashBasedTable.create();
			endSummary.put(sP, d1, summaries);
		}









undoing previous "fix"; as discussed with Steven, it is not required (see comment)


 

 


Eric Bodden
committed
Dec 12, 2012






696




697




698




699




		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






700




701




702




703




704




705




706




707




708




709




710




711




712




713




714




715




716




717




718




719




720




721




722




	}	
	
	private Set<Entry<N, Set<D>>> incoming(D d1, N sP) {
		Map<N, Set<D>> map = incoming.get(sP, d1);
		if(map==null) return Collections.emptySet();
		return map.entrySet();		
	}
	
	private void addIncoming(N sP, D d3, N n, D d2) {
		Map<N, Set<D>> summaries = incoming.get(sP, d3);
		if(summaries==null) {
			summaries = new HashMap<N, Set<D>>();
			incoming.put(sP, d3, summaries);
		}
		Set<D> set = summaries.get(n);
		if(set==null) {
			set = new HashSet<D>();
			summaries.put(n,set);
		}
		set.add(d2);
	}	
	
	/**









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






723




724




	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






725




726




	 */
	public V resultAt(N stmt, D value) {









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






727




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






728




729




730




731




732




		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






733




734




	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






735




736




737




	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






738




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






739




740




741




742




743




744




745




		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






746




747




748




749




750




751




752




	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






753




	









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






754




755




756




757




758




759




760




761




	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "";
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






762




763




764




765




766




767




768




769




770




771




772




773




	public void printStats() {
		if(DEBUG) {
			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {
			System.err.println("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






774




		private final PathEdge<N,D> edge;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






775














removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






776




		public PathEdgeProcessingTask(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






777




778




779




780




781




782




783




784




785




786




787




788




789




790




791




792




793




794




795




796




797




798




799




800




801




802




803




804




			this.edge = edge;
		}

		public void run() {
			if(icfg.isCallStmt(edge.getTarget())) {
				processCall(edge);
			} else {
				//note that some statements, such as "throw" may be
				//both an exit statement and a "normal" statement
				if(icfg.isExitStmt(edge.getTarget())) {
					processExit(edge);
				}
				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {
					processNormalFlow(edge);
				}
			}
		}
	}
	
	private class ValuePropagationTask implements Runnable {
		private final Pair<N, D> nAndD;

		public ValuePropagationTask(Pair<N,D> nAndD) {
			this.nAndD = nAndD;
		}

		public void run() {
			N n = nAndD.getO1();









bug fix for value computation (need to treat initialSeeds just as method start nodes)


 

 


Eric Bodden
committed
Feb 14, 2013






805




			if(icfg.isStartPoint(n) ||









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






806




				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






807




808




809




810




811




812




813




814




815




816




817




818




819




820




821




822




823




824




825




826




827




828




829




830




831




832




833




834




835




836




837




838




839




840




841




842




843




844




845




				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
		}
	}
	
	private class ValueComputationTask implements Runnable {
		private final N[] values;
		final int num;

		public ValueComputationTask(N[] values, int num) {
			this.values = values;
			this.num = num;
		}

		public void run() {
			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;
			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {
				N n = values[i];
				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					
					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;
					lookupByTarget = jumpFn.lookupByTarget(n);
					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {
						D dPrime = sourceValTargetValAndFunction.getRowKey();
						D d = sourceValTargetValAndFunction.getColumnKey();
						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();
						synchronized (val) {
							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));
						}
						flowFunctionApplicationCount++;
					}
				}
			}
		}
	}

}











Open sidebar



Joshua Garcia heros

25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9







Open sidebar



Joshua Garcia heros

25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9




Open sidebar

Joshua Garcia heros

25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Joshua Garciaherosheros
25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9










25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



30.1 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






3




 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






4




5




6




7




8




9




10




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






11




 *     Marc-Andre Laverdiere-Papineau - Fixed race condition









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






12




 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15















renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.EdgeFunctionCache;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.edgefunc.EdgeIdentity;










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




import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






37




import java.util.concurrent.LinkedBlockingQueue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




39




40




41




42




43




44




45




46




import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






47














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 
 * Note that this solver and its data structures internally use mostly {@link LinkedHashSet}s
 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






58




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






59




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






60




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






61




62




63




64




65




66




67




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









replaced env variable by property


 

 


Eric Bodden
committed
Jan 24, 2013






68




	public static final boolean DEBUG = !System.getProperty("HEROS_DEBUG", "false").equals("false");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




71




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






72




	protected CountingThreadPoolExecutor executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






73




74




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






75




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






76




77




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






78




	protected final JumpFunctions<N,D,V> jumpFn;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






79




80




	
	@SynchronizedBy("thread safe data structure, only modified internally")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






81




	protected final I icfg;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






82




83




84




85




	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






86




	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






87




88




89




90





	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






91




	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






92




93




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






94




	protected final FlowFunctions<N, D, M> flowFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






95




96





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






97




	protected final EdgeFunctions<N,D,M,V> edgeFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






98




99





	@DontSynchronize("only used by single thread")









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






100




	protected final Map<N,Set<D>> initialSeeds;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






101




102





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






103




	protected final JoinLattice<V> valueLattice;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






104




105




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






106




	protected final EdgeFunction<V> allTop;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107














adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






108




	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






109




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




	
	@DontSynchronize("benign races")
	public long flowFunctionApplicationCount;

	@DontSynchronize("benign races")
	public long flowFunctionConstructionCount;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionConstruction;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionApplication;

	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






127




	protected final D zeroValue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






128




129




	
	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






130




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






131




132





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






133




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






134














making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






135




136




	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






137














make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






138




139




140




	@DontSynchronize("readOnly")
	protected final boolean computeValues;










added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






141




142




143




144




	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






145




146




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






147




148




	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






149




150




151




152




153




154




	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






155




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






156




157




158




159




160




		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






161




		this.icfg = tabulationProblem.interproceduralCFG();		









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






162




163




		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




178




179




180




181




182




		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();
		if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}
		if(edgeFunctionCacheBuilder!=null) {
			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);
			edgeFunctions = efCache;
		} else {
			efCache = null;
		}
		this.flowFunctions = flowFunctions;
		this.edgeFunctions = edgeFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.valueLattice = tabulationProblem.joinLattice();
		this.allTop = tabulationProblem.allTopFunction();
		this.jumpFn = new JumpFunctions<N,D,V>(allTop);









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






183




		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






184




		this.numThreads = Math.max(1,tabulationProblem.numThreads());









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






185




		this.computeValues = tabulationProblem.computeValues();









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






186




		this.executor = getExecutor();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






187




188




189




190




191




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






192




	public void solve() {		









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






193




194




195




196




197




198




199




200




		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






201




	 */









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






202




	protected void submitInitialSeeds() {









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






203




204




205




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






206




				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






207




			}









extracted method awaitCompletionComputeValuesAndShutdown()


 

 


Eric Bodden
committed
Jan 30, 2013






208




209




210




211




212




213




214




215




216




217




			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






218




			final long before = System.currentTimeMillis();









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






219




220




			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






221




222




			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






223




		if(computeValues) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






224




225




226




227




228




229




230




			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






231




232




233




		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






234




		executor.shutdown();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






235




236




		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






237




238




239




240




241




242




243




		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






244




		try {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






245




			executor.awaitCompletion();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






246




		} catch (InterruptedException e) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






247




248




249




250




251




			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






252




		}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






253




254




	}










refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






255




256




257




258




    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






259




    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






260




261




    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






262




    }









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






263




	









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






264




265




266




267




268




    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






269




    	executor.execute(vpt);









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






270




271




    }
  









comments


 

 


Eric Bodden
committed
Jan 28, 2013






272




273




274




275




    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






276




277




278




	private void scheduleValueComputationTask(ValueComputationTask task) {
		executor.execute(task);
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






279




280




	
	/**









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






281




282




	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






283




284




	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






285




	 * 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






286




287




	 * @param edge an edge whose target node resembles a method call
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






288




	private void processCall(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






289




290




291




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






292




293




		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






294




		









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






295




		//for each possible callee









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






296




297




		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






298




299




			
			//compute the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






300




301




302




			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






303




304




			
			//for each callee's start point(s)









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






305




			Set<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






306




			for(N sP: startPointsOf) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






307




				//for each result node of the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






308




				for(D d3: res) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






309




					//create initial self-loop









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






310




					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






311




	









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






312




					//register the fact that <sp,d3> has an incoming edge from <n,d2>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






313




314




315




316




317




					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






318




319




320




						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));
						
						assert !jumpFn.reverseLookup(n, d2).isEmpty();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






321




322




323




					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






324




					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






325




					//create new caller-side jump functions to the return sites









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






326




					//because we have observed a potentially new incoming edge into <sP,d3>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






327




328




329




330




					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






331




						//for each return site









Replaced the duplicate call to the icfg by an access to cached structure we have anyway


 

 


Steven Arzt
committed
Mar 11, 2013






332




						for(N retSiteN: returnSiteNs) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






333




							//compute return-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






334




335




							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






336




							//for each target value of the function









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






337




							for(D d5: computeReturnFlowFunction(retFunction, d4, Collections.singleton(d1))) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






338




								//update the caller-side summary function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






339




340




								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






341




								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);							









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






342




								propagate(d1, retSiteN, d5, f.composeWith(fPrime), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






343




344




345




346




347




348




							}
						}
					}
				}		
			}
		}









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






349




350




		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






351




352




353




		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






354




			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






355




				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






356




				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






357




358




359




360




361




			}
		}
	}

	/**









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






362




363




364




365




366




367




368




369




370




371




372




373




374




375




376




	 * Computes the call-to-return flow function for the given call-site
	 * asbtraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the return size
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}

	/**
	 * Lines 21-32 of the algorithm.









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






377




378




379




380




381




382




	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






383




	 */









Merge branch 'develop' into forks/java-fw-bw


 

 


Eric Bodden
committed
Jul 06, 2013






384




	protected void processExit(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






385




386




387




388




389




390




391




		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






392




393




394




395




		//for each of the method's start points, determine incoming calls
		Set<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);
		Set<Entry<N,Set<D>>> inc = new HashSet<Map.Entry<N,Set<D>>>();
		for(N sP: startPointsOf) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






396




397




			//line 21.1 of Naeem/Lhotak/Rodriguez
			









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






398




			//register end-summary









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






399




400




401




			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






402




403




404




405




406




407




408




409




410




411




412




413




414




415




				inc.addAll(incoming(d1, sP));
			}	
		}
		
		//for each incoming call edge already processed
		//(see processCall(..))
		for (Entry<N,Set<D>> entry: inc) {
			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






416




				Set<D> targets = computeReturnFlowFunction(retFunction, d2, entry.getValue());









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






417




418




419




420




421




422




423




424




425




426




427




428




429




430




				//for each incoming-call value
				for(D d4: entry.getValue()) {
					//for each target value at the return site
					//line 23
					for(D d5: targets) {
						//compute composed function
						EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
						EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
						EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);
						//for each jump function coming into the call, propagate to return site using the composed function
						for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
							EdgeFunction<V> f3 = valAndFunc.getValue();
							if(!f3.equalTo(allTop)) {
								D d3 = valAndFunc.getKey();









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






431




								propagate(d3, retSiteC, d5, f3.composeWith(fPrime), c, false);









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






432




433




434




435




							}
						}
					}
				}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






436




			}









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






437




438




		}
		









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






439




		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow









further fix for followReturnPastSeeds:


 

 


Eric Bodden
committed
Jul 08, 2013






440




441




442




		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






443




			// only propagate up if we 









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






444




445




446




447




448




449




450




451




				Set<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;
						Set<D> targets = retFunction.computeTargets(d2);
						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






452




							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






453




						}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






454




455




					}
				}









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






456




457




458




459




460




461




462




463




				//in cases where there are no callers, the return statement would normally not be processed at all;
				//this might be undesirable if the flow function has a side effect such as registering a taint;
				//instead we thus call the return flow function will a null caller
				if(callers.isEmpty()) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
					flowFunctionConstructionCount++;
					retFunction.computeTargets(d2);
				}









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






464




			}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






465




466




		}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






467




468




469




470




471




472




473




474




475




476




477




478




479




	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callerSideD1s The abstractions at the callers' start nodes.
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction
			(FlowFunction<D> retFunction, D d2, Set<D> callerSideD1s) {
		return retFunction.computeTargets(d2);
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






480




481




	/**
	 * Lines 33-37 of the algorithm.









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






482




	 * Simply propagate normal, intra-procedural flows.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






483




484




	 * @param edge
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






485




	private void processNormalFlow(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






486




487




488




489




490




491




492




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






493




			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






494




495




			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






496




				propagate(d1, m, d3, fprime, null, false); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






497




498




499




500




			}
		}
	}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






501




502




503




504




505




506




507




508




509




510




511




512




513




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions-
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */
	protected Set<D> computeNormalFlowFunction
			(FlowFunction<D> flowFunction, D d1, D d2) {
		return flowFunction.computeTargets(d2);
	}










changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






514




515




516




517




518




519




520




521




522




	/**
	 * Propagates the flow further down the exploded super graph, merging any edge function that might
	 * already have been computed for targetVal at target. 
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






523




524




	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






525




	 */









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






526




527




528




	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






529




		EdgeFunction<V> jumpFnE;









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






530




531




		EdgeFunction<V> fPrime;
		boolean newFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






532




533




		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






534




535




536




537




			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






538




539




				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






540




541




542




		}

		if(newFunction) {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






543




			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






544




			scheduleEdgeProcessing(edge);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






545




546




547




548





			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






549




550




					result.append(getDebugName());
					result.append(": ");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






551




552




553




554




555




556




557




558




559




560




561




562




563




564




565




566




					result.append("EDGE:  <");
					result.append(icfg.getMethodOf(target));
					result.append(",");
					result.append(sourceVal);
					result.append("> -> <");
					result.append(target);
					result.append(",");
					result.append(targetVal);
					result.append("> - ");
					result.append(fPrime);
					System.err.println(result.toString());
				}
			}
		}
	}
	









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






567




568




569




570




571




	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






572




573




574




575




576




577




578




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {
				setVal(startPoint, val, valueLattice.bottomElement());
				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); 
				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
			}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






579




		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






580




		









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






581




582




583




584




585




586




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






587




		









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






588




589




590




591




592




593




594




595




596




		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






597




598




		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






599




		for(int t=0;t<numThreads; t++) {









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






600




601




			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






602




		}









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






603




604




605




606




607




608




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






609




610




611




612




613




614




615




616




617




618




619




620




621




622




623




624




625




626




627




628




629




630




631




632




633




634




635




636




637




638




639




640




641




642




643




644




645




646




647




648




649




	}

	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();		
		M p = icfg.getMethodOf(n);
		for(N c: icfg.getCallsFromWithin(p)) {					
			Set<Entry<D, EdgeFunction<V>>> entries; 
			synchronized (jumpFn) {
				entries = jumpFn.forwardLookup(d,c).entrySet();
				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {
					D dPrime = dPAndFP.getKey();
					EdgeFunction<V> fPrime = dPAndFP.getValue();
					N sP = n;
					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValueAtCall(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();
		for(M q: icfg.getCalleesOfCallAt(n)) {
			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);
			flowFunctionConstructionCount++;
			for(D dPrime: callFlowFunction.computeTargets(d)) {
				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);
				for(N startPoint: icfg.getStartPointsOf(q)) {
					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValue(N nHashN, D nHashD, V v) {
		synchronized (val) {
			V valNHash = val(nHashN, nHashD);
			V vPrime = valueLattice.join(valNHash,v);
			if(!vPrime.equals(valNHash)) {
				setVal(nHashN, nHashD, vPrime);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






650




				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






651




652




653




654




655




			}
		}
	}

	private V val(N nHashN, D nHashD){ 









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






656




657




658




659




		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






660




661




662




663




		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






664




665




	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






666




		synchronized (val) {









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






667




668




669




670




			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






671




		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






672




673




674




675




		if(DEBUG)
			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);
	}










removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






676




	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






677




678




679




680




681




682




683




		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






684




685




686




687




688




689




690




691




692




693




694




695




	private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {
		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);
		if(map==null) return Collections.emptySet();
		return map.cellSet();
	}

	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {
		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);
		if(summaries==null) {
			summaries = HashBasedTable.create();
			endSummary.put(sP, d1, summaries);
		}









undoing previous "fix"; as discussed with Steven, it is not required (see comment)


 

 


Eric Bodden
committed
Dec 12, 2012






696




697




698




699




		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






700




701




702




703




704




705




706




707




708




709




710




711




712




713




714




715




716




717




718




719




720




721




722




	}	
	
	private Set<Entry<N, Set<D>>> incoming(D d1, N sP) {
		Map<N, Set<D>> map = incoming.get(sP, d1);
		if(map==null) return Collections.emptySet();
		return map.entrySet();		
	}
	
	private void addIncoming(N sP, D d3, N n, D d2) {
		Map<N, Set<D>> summaries = incoming.get(sP, d3);
		if(summaries==null) {
			summaries = new HashMap<N, Set<D>>();
			incoming.put(sP, d3, summaries);
		}
		Set<D> set = summaries.get(n);
		if(set==null) {
			set = new HashSet<D>();
			summaries.put(n,set);
		}
		set.add(d2);
	}	
	
	/**









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






723




724




	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






725




726




	 */
	public V resultAt(N stmt, D value) {









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






727




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






728




729




730




731




732




		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






733




734




	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






735




736




737




	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






738




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






739




740




741




742




743




744




745




		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






746




747




748




749




750




751




752




	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






753




	









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






754




755




756




757




758




759




760




761




	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "";
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






762




763




764




765




766




767




768




769




770




771




772




773




	public void printStats() {
		if(DEBUG) {
			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {
			System.err.println("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






774




		private final PathEdge<N,D> edge;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






775














removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






776




		public PathEdgeProcessingTask(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






777




778




779




780




781




782




783




784




785




786




787




788




789




790




791




792




793




794




795




796




797




798




799




800




801




802




803




804




			this.edge = edge;
		}

		public void run() {
			if(icfg.isCallStmt(edge.getTarget())) {
				processCall(edge);
			} else {
				//note that some statements, such as "throw" may be
				//both an exit statement and a "normal" statement
				if(icfg.isExitStmt(edge.getTarget())) {
					processExit(edge);
				}
				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {
					processNormalFlow(edge);
				}
			}
		}
	}
	
	private class ValuePropagationTask implements Runnable {
		private final Pair<N, D> nAndD;

		public ValuePropagationTask(Pair<N,D> nAndD) {
			this.nAndD = nAndD;
		}

		public void run() {
			N n = nAndD.getO1();









bug fix for value computation (need to treat initialSeeds just as method start nodes)


 

 


Eric Bodden
committed
Feb 14, 2013






805




			if(icfg.isStartPoint(n) ||









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






806




				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






807




808




809




810




811




812




813




814




815




816




817




818




819




820




821




822




823




824




825




826




827




828




829




830




831




832




833




834




835




836




837




838




839




840




841




842




843




844




845




				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
		}
	}
	
	private class ValueComputationTask implements Runnable {
		private final N[] values;
		final int num;

		public ValueComputationTask(N[] values, int num) {
			this.values = values;
			this.num = num;
		}

		public void run() {
			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;
			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {
				N n = values[i];
				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					
					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;
					lookupByTarget = jumpFn.lookupByTarget(n);
					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {
						D dPrime = sourceValTargetValAndFunction.getRowKey();
						D d = sourceValTargetValAndFunction.getColumnKey();
						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();
						synchronized (val) {
							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));
						}
						flowFunctionApplicationCount++;
					}
				}
			}
		}
	}

}














25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



30.1 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






3




 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






4




5




6




7




8




9




10




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






11




 *     Marc-Andre Laverdiere-Papineau - Fixed race condition









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






12




 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15















renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.EdgeFunctionCache;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.edgefunc.EdgeIdentity;










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




import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






37




import java.util.concurrent.LinkedBlockingQueue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




39




40




41




42




43




44




45




46




import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






47














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 
 * Note that this solver and its data structures internally use mostly {@link LinkedHashSet}s
 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






58




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






59




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






60




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






61




62




63




64




65




66




67




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









replaced env variable by property


 

 


Eric Bodden
committed
Jan 24, 2013






68




	public static final boolean DEBUG = !System.getProperty("HEROS_DEBUG", "false").equals("false");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




71




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






72




	protected CountingThreadPoolExecutor executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






73




74




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






75




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






76




77




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






78




	protected final JumpFunctions<N,D,V> jumpFn;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






79




80




	
	@SynchronizedBy("thread safe data structure, only modified internally")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






81




	protected final I icfg;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






82




83




84




85




	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






86




	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






87




88




89




90





	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






91




	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






92




93




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






94




	protected final FlowFunctions<N, D, M> flowFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






95




96





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






97




	protected final EdgeFunctions<N,D,M,V> edgeFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






98




99





	@DontSynchronize("only used by single thread")









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






100




	protected final Map<N,Set<D>> initialSeeds;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






101




102





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






103




	protected final JoinLattice<V> valueLattice;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






104




105




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






106




	protected final EdgeFunction<V> allTop;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107














adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






108




	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






109




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




	
	@DontSynchronize("benign races")
	public long flowFunctionApplicationCount;

	@DontSynchronize("benign races")
	public long flowFunctionConstructionCount;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionConstruction;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionApplication;

	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






127




	protected final D zeroValue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






128




129




	
	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






130




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






131




132





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






133




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






134














making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






135




136




	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






137














make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






138




139




140




	@DontSynchronize("readOnly")
	protected final boolean computeValues;










added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






141




142




143




144




	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






145




146




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






147




148




	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






149




150




151




152




153




154




	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






155




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






156




157




158




159




160




		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






161




		this.icfg = tabulationProblem.interproceduralCFG();		









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






162




163




		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




178




179




180




181




182




		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();
		if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}
		if(edgeFunctionCacheBuilder!=null) {
			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);
			edgeFunctions = efCache;
		} else {
			efCache = null;
		}
		this.flowFunctions = flowFunctions;
		this.edgeFunctions = edgeFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.valueLattice = tabulationProblem.joinLattice();
		this.allTop = tabulationProblem.allTopFunction();
		this.jumpFn = new JumpFunctions<N,D,V>(allTop);









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






183




		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






184




		this.numThreads = Math.max(1,tabulationProblem.numThreads());









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






185




		this.computeValues = tabulationProblem.computeValues();









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






186




		this.executor = getExecutor();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






187




188




189




190




191




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






192




	public void solve() {		









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






193




194




195




196




197




198




199




200




		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






201




	 */









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






202




	protected void submitInitialSeeds() {









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






203




204




205




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






206




				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






207




			}









extracted method awaitCompletionComputeValuesAndShutdown()


 

 


Eric Bodden
committed
Jan 30, 2013






208




209




210




211




212




213




214




215




216




217




			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






218




			final long before = System.currentTimeMillis();









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






219




220




			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






221




222




			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






223




		if(computeValues) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






224




225




226




227




228




229




230




			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






231




232




233




		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






234




		executor.shutdown();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






235




236




		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






237




238




239




240




241




242




243




		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






244




		try {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






245




			executor.awaitCompletion();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






246




		} catch (InterruptedException e) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






247




248




249




250




251




			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






252




		}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






253




254




	}










refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






255




256




257




258




    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






259




    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






260




261




    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






262




    }









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






263




	









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






264




265




266




267




268




    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






269




    	executor.execute(vpt);









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






270




271




    }
  









comments


 

 


Eric Bodden
committed
Jan 28, 2013






272




273




274




275




    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






276




277




278




	private void scheduleValueComputationTask(ValueComputationTask task) {
		executor.execute(task);
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






279




280




	
	/**









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






281




282




	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






283




284




	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






285




	 * 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






286




287




	 * @param edge an edge whose target node resembles a method call
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






288




	private void processCall(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






289




290




291




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






292




293




		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






294




		









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






295




		//for each possible callee









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






296




297




		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






298




299




			
			//compute the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






300




301




302




			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






303




304




			
			//for each callee's start point(s)









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






305




			Set<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






306




			for(N sP: startPointsOf) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






307




				//for each result node of the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






308




				for(D d3: res) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






309




					//create initial self-loop









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






310




					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






311




	









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






312




					//register the fact that <sp,d3> has an incoming edge from <n,d2>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






313




314




315




316




317




					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






318




319




320




						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));
						
						assert !jumpFn.reverseLookup(n, d2).isEmpty();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






321




322




323




					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






324




					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






325




					//create new caller-side jump functions to the return sites









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






326




					//because we have observed a potentially new incoming edge into <sP,d3>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






327




328




329




330




					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






331




						//for each return site









Replaced the duplicate call to the icfg by an access to cached structure we have anyway


 

 


Steven Arzt
committed
Mar 11, 2013






332




						for(N retSiteN: returnSiteNs) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






333




							//compute return-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






334




335




							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






336




							//for each target value of the function









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






337




							for(D d5: computeReturnFlowFunction(retFunction, d4, Collections.singleton(d1))) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






338




								//update the caller-side summary function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






339




340




								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






341




								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);							









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






342




								propagate(d1, retSiteN, d5, f.composeWith(fPrime), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






343




344




345




346




347




348




							}
						}
					}
				}		
			}
		}









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






349




350




		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






351




352




353




		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






354




			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






355




				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






356




				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






357




358




359




360




361




			}
		}
	}

	/**









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






362




363




364




365




366




367




368




369




370




371




372




373




374




375




376




	 * Computes the call-to-return flow function for the given call-site
	 * asbtraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the return size
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}

	/**
	 * Lines 21-32 of the algorithm.









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






377




378




379




380




381




382




	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






383




	 */









Merge branch 'develop' into forks/java-fw-bw


 

 


Eric Bodden
committed
Jul 06, 2013






384




	protected void processExit(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






385




386




387




388




389




390




391




		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






392




393




394




395




		//for each of the method's start points, determine incoming calls
		Set<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);
		Set<Entry<N,Set<D>>> inc = new HashSet<Map.Entry<N,Set<D>>>();
		for(N sP: startPointsOf) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






396




397




			//line 21.1 of Naeem/Lhotak/Rodriguez
			









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






398




			//register end-summary









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






399




400




401




			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






402




403




404




405




406




407




408




409




410




411




412




413




414




415




				inc.addAll(incoming(d1, sP));
			}	
		}
		
		//for each incoming call edge already processed
		//(see processCall(..))
		for (Entry<N,Set<D>> entry: inc) {
			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






416




				Set<D> targets = computeReturnFlowFunction(retFunction, d2, entry.getValue());









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






417




418




419




420




421




422




423




424




425




426




427




428




429




430




				//for each incoming-call value
				for(D d4: entry.getValue()) {
					//for each target value at the return site
					//line 23
					for(D d5: targets) {
						//compute composed function
						EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
						EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
						EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);
						//for each jump function coming into the call, propagate to return site using the composed function
						for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
							EdgeFunction<V> f3 = valAndFunc.getValue();
							if(!f3.equalTo(allTop)) {
								D d3 = valAndFunc.getKey();









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






431




								propagate(d3, retSiteC, d5, f3.composeWith(fPrime), c, false);









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






432




433




434




435




							}
						}
					}
				}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






436




			}









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






437




438




		}
		









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






439




		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow









further fix for followReturnPastSeeds:


 

 


Eric Bodden
committed
Jul 08, 2013






440




441




442




		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






443




			// only propagate up if we 









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






444




445




446




447




448




449




450




451




				Set<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;
						Set<D> targets = retFunction.computeTargets(d2);
						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






452




							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






453




						}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






454




455




					}
				}









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






456




457




458




459




460




461




462




463




				//in cases where there are no callers, the return statement would normally not be processed at all;
				//this might be undesirable if the flow function has a side effect such as registering a taint;
				//instead we thus call the return flow function will a null caller
				if(callers.isEmpty()) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
					flowFunctionConstructionCount++;
					retFunction.computeTargets(d2);
				}









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






464




			}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






465




466




		}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






467




468




469




470




471




472




473




474




475




476




477




478




479




	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callerSideD1s The abstractions at the callers' start nodes.
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction
			(FlowFunction<D> retFunction, D d2, Set<D> callerSideD1s) {
		return retFunction.computeTargets(d2);
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






480




481




	/**
	 * Lines 33-37 of the algorithm.









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






482




	 * Simply propagate normal, intra-procedural flows.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






483




484




	 * @param edge
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






485




	private void processNormalFlow(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






486




487




488




489




490




491




492




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






493




			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






494




495




			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






496




				propagate(d1, m, d3, fprime, null, false); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






497




498




499




500




			}
		}
	}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






501




502




503




504




505




506




507




508




509




510




511




512




513




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions-
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */
	protected Set<D> computeNormalFlowFunction
			(FlowFunction<D> flowFunction, D d1, D d2) {
		return flowFunction.computeTargets(d2);
	}










changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






514




515




516




517




518




519




520




521




522




	/**
	 * Propagates the flow further down the exploded super graph, merging any edge function that might
	 * already have been computed for targetVal at target. 
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






523




524




	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






525




	 */









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






526




527




528




	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






529




		EdgeFunction<V> jumpFnE;









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






530




531




		EdgeFunction<V> fPrime;
		boolean newFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






532




533




		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






534




535




536




537




			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






538




539




				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






540




541




542




		}

		if(newFunction) {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






543




			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






544




			scheduleEdgeProcessing(edge);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






545




546




547




548





			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






549




550




					result.append(getDebugName());
					result.append(": ");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






551




552




553




554




555




556




557




558




559




560




561




562




563




564




565




566




					result.append("EDGE:  <");
					result.append(icfg.getMethodOf(target));
					result.append(",");
					result.append(sourceVal);
					result.append("> -> <");
					result.append(target);
					result.append(",");
					result.append(targetVal);
					result.append("> - ");
					result.append(fPrime);
					System.err.println(result.toString());
				}
			}
		}
	}
	









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






567




568




569




570




571




	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






572




573




574




575




576




577




578




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {
				setVal(startPoint, val, valueLattice.bottomElement());
				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); 
				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
			}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






579




		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






580




		









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






581




582




583




584




585




586




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






587




		









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






588




589




590




591




592




593




594




595




596




		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






597




598




		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






599




		for(int t=0;t<numThreads; t++) {









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






600




601




			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






602




		}









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






603




604




605




606




607




608




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






609




610




611




612




613




614




615




616




617




618




619




620




621




622




623




624




625




626




627




628




629




630




631




632




633




634




635




636




637




638




639




640




641




642




643




644




645




646




647




648




649




	}

	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();		
		M p = icfg.getMethodOf(n);
		for(N c: icfg.getCallsFromWithin(p)) {					
			Set<Entry<D, EdgeFunction<V>>> entries; 
			synchronized (jumpFn) {
				entries = jumpFn.forwardLookup(d,c).entrySet();
				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {
					D dPrime = dPAndFP.getKey();
					EdgeFunction<V> fPrime = dPAndFP.getValue();
					N sP = n;
					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValueAtCall(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();
		for(M q: icfg.getCalleesOfCallAt(n)) {
			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);
			flowFunctionConstructionCount++;
			for(D dPrime: callFlowFunction.computeTargets(d)) {
				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);
				for(N startPoint: icfg.getStartPointsOf(q)) {
					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValue(N nHashN, D nHashD, V v) {
		synchronized (val) {
			V valNHash = val(nHashN, nHashD);
			V vPrime = valueLattice.join(valNHash,v);
			if(!vPrime.equals(valNHash)) {
				setVal(nHashN, nHashD, vPrime);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






650




				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






651




652




653




654




655




			}
		}
	}

	private V val(N nHashN, D nHashD){ 









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






656




657




658




659




		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






660




661




662




663




		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






664




665




	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






666




		synchronized (val) {









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






667




668




669




670




			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






671




		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






672




673




674




675




		if(DEBUG)
			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);
	}










removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






676




	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






677




678




679




680




681




682




683




		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






684




685




686




687




688




689




690




691




692




693




694




695




	private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {
		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);
		if(map==null) return Collections.emptySet();
		return map.cellSet();
	}

	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {
		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);
		if(summaries==null) {
			summaries = HashBasedTable.create();
			endSummary.put(sP, d1, summaries);
		}









undoing previous "fix"; as discussed with Steven, it is not required (see comment)


 

 


Eric Bodden
committed
Dec 12, 2012






696




697




698




699




		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






700




701




702




703




704




705




706




707




708




709




710




711




712




713




714




715




716




717




718




719




720




721




722




	}	
	
	private Set<Entry<N, Set<D>>> incoming(D d1, N sP) {
		Map<N, Set<D>> map = incoming.get(sP, d1);
		if(map==null) return Collections.emptySet();
		return map.entrySet();		
	}
	
	private void addIncoming(N sP, D d3, N n, D d2) {
		Map<N, Set<D>> summaries = incoming.get(sP, d3);
		if(summaries==null) {
			summaries = new HashMap<N, Set<D>>();
			incoming.put(sP, d3, summaries);
		}
		Set<D> set = summaries.get(n);
		if(set==null) {
			set = new HashSet<D>();
			summaries.put(n,set);
		}
		set.add(d2);
	}	
	
	/**









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






723




724




	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






725




726




	 */
	public V resultAt(N stmt, D value) {









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






727




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






728




729




730




731




732




		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






733




734




	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






735




736




737




	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






738




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






739




740




741




742




743




744




745




		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






746




747




748




749




750




751




752




	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






753




	









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






754




755




756




757




758




759




760




761




	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "";
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






762




763




764




765




766




767




768




769




770




771




772




773




	public void printStats() {
		if(DEBUG) {
			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {
			System.err.println("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






774




		private final PathEdge<N,D> edge;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






775














removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






776




		public PathEdgeProcessingTask(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






777




778




779




780




781




782




783




784




785




786




787




788




789




790




791




792




793




794




795




796




797




798




799




800




801




802




803




804




			this.edge = edge;
		}

		public void run() {
			if(icfg.isCallStmt(edge.getTarget())) {
				processCall(edge);
			} else {
				//note that some statements, such as "throw" may be
				//both an exit statement and a "normal" statement
				if(icfg.isExitStmt(edge.getTarget())) {
					processExit(edge);
				}
				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {
					processNormalFlow(edge);
				}
			}
		}
	}
	
	private class ValuePropagationTask implements Runnable {
		private final Pair<N, D> nAndD;

		public ValuePropagationTask(Pair<N,D> nAndD) {
			this.nAndD = nAndD;
		}

		public void run() {
			N n = nAndD.getO1();









bug fix for value computation (need to treat initialSeeds just as method start nodes)


 

 


Eric Bodden
committed
Feb 14, 2013






805




			if(icfg.isStartPoint(n) ||









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






806




				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






807




808




809




810




811




812




813




814




815




816




817




818




819




820




821




822




823




824




825




826




827




828




829




830




831




832




833




834




835




836




837




838




839




840




841




842




843




844




845




				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
		}
	}
	
	private class ValueComputationTask implements Runnable {
		private final N[] values;
		final int num;

		public ValueComputationTask(N[] values, int num) {
			this.values = values;
			this.num = num;
		}

		public void run() {
			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;
			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {
				N n = values[i];
				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					
					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;
					lookupByTarget = jumpFn.lookupByTarget(n);
					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {
						D dPrime = sourceValTargetValAndFunction.getRowKey();
						D d = sourceValTargetValAndFunction.getColumnKey();
						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();
						synchronized (val) {
							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));
						}
						flowFunctionApplicationCount++;
					}
				}
			}
		}
	}

}










25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink




25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java





25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag








25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag





25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

IDESolver.java
Find file
Normal viewHistoryPermalink




IDESolver.java



30.1 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






3




 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






4




5




6




7




8




9




10




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






11




 *     Marc-Andre Laverdiere-Papineau - Fixed race condition









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






12




 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15















renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.EdgeFunctionCache;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.edgefunc.EdgeIdentity;










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




import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






37




import java.util.concurrent.LinkedBlockingQueue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




39




40




41




42




43




44




45




46




import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






47














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 
 * Note that this solver and its data structures internally use mostly {@link LinkedHashSet}s
 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






58




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






59




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






60




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






61




62




63




64




65




66




67




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









replaced env variable by property


 

 


Eric Bodden
committed
Jan 24, 2013






68




	public static final boolean DEBUG = !System.getProperty("HEROS_DEBUG", "false").equals("false");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




71




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






72




	protected CountingThreadPoolExecutor executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






73




74




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






75




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






76




77




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






78




	protected final JumpFunctions<N,D,V> jumpFn;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






79




80




	
	@SynchronizedBy("thread safe data structure, only modified internally")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






81




	protected final I icfg;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






82




83




84




85




	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






86




	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






87




88




89




90





	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






91




	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






92




93




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






94




	protected final FlowFunctions<N, D, M> flowFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






95




96





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






97




	protected final EdgeFunctions<N,D,M,V> edgeFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






98




99





	@DontSynchronize("only used by single thread")









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






100




	protected final Map<N,Set<D>> initialSeeds;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






101




102





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






103




	protected final JoinLattice<V> valueLattice;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






104




105




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






106




	protected final EdgeFunction<V> allTop;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107














adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






108




	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






109




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




	
	@DontSynchronize("benign races")
	public long flowFunctionApplicationCount;

	@DontSynchronize("benign races")
	public long flowFunctionConstructionCount;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionConstruction;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionApplication;

	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






127




	protected final D zeroValue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






128




129




	
	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






130




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






131




132





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






133




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






134














making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






135




136




	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






137














make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






138




139




140




	@DontSynchronize("readOnly")
	protected final boolean computeValues;










added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






141




142




143




144




	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






145




146




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






147




148




	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






149




150




151




152




153




154




	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






155




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






156




157




158




159




160




		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






161




		this.icfg = tabulationProblem.interproceduralCFG();		









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






162




163




		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




178




179




180




181




182




		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();
		if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}
		if(edgeFunctionCacheBuilder!=null) {
			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);
			edgeFunctions = efCache;
		} else {
			efCache = null;
		}
		this.flowFunctions = flowFunctions;
		this.edgeFunctions = edgeFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.valueLattice = tabulationProblem.joinLattice();
		this.allTop = tabulationProblem.allTopFunction();
		this.jumpFn = new JumpFunctions<N,D,V>(allTop);









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






183




		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






184




		this.numThreads = Math.max(1,tabulationProblem.numThreads());









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






185




		this.computeValues = tabulationProblem.computeValues();









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






186




		this.executor = getExecutor();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






187




188




189




190




191




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






192




	public void solve() {		









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






193




194




195




196




197




198




199




200




		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






201




	 */









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






202




	protected void submitInitialSeeds() {









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






203




204




205




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






206




				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






207




			}









extracted method awaitCompletionComputeValuesAndShutdown()


 

 


Eric Bodden
committed
Jan 30, 2013






208




209




210




211




212




213




214




215




216




217




			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






218




			final long before = System.currentTimeMillis();









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






219




220




			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






221




222




			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






223




		if(computeValues) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






224




225




226




227




228




229




230




			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






231




232




233




		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






234




		executor.shutdown();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






235




236




		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






237




238




239




240




241




242




243




		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






244




		try {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






245




			executor.awaitCompletion();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






246




		} catch (InterruptedException e) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






247




248




249




250




251




			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






252




		}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






253




254




	}










refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






255




256




257




258




    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






259




    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






260




261




    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






262




    }









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






263




	









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






264




265




266




267




268




    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






269




    	executor.execute(vpt);









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






270




271




    }
  









comments


 

 


Eric Bodden
committed
Jan 28, 2013






272




273




274




275




    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






276




277




278




	private void scheduleValueComputationTask(ValueComputationTask task) {
		executor.execute(task);
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






279




280




	
	/**









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






281




282




	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






283




284




	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






285




	 * 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






286




287




	 * @param edge an edge whose target node resembles a method call
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






288




	private void processCall(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






289




290




291




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






292




293




		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






294




		









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






295




		//for each possible callee









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






296




297




		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






298




299




			
			//compute the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






300




301




302




			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






303




304




			
			//for each callee's start point(s)









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






305




			Set<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






306




			for(N sP: startPointsOf) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






307




				//for each result node of the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






308




				for(D d3: res) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






309




					//create initial self-loop









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






310




					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






311




	









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






312




					//register the fact that <sp,d3> has an incoming edge from <n,d2>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






313




314




315




316




317




					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






318




319




320




						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));
						
						assert !jumpFn.reverseLookup(n, d2).isEmpty();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






321




322




323




					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






324




					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






325




					//create new caller-side jump functions to the return sites









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






326




					//because we have observed a potentially new incoming edge into <sP,d3>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






327




328




329




330




					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






331




						//for each return site









Replaced the duplicate call to the icfg by an access to cached structure we have anyway


 

 


Steven Arzt
committed
Mar 11, 2013






332




						for(N retSiteN: returnSiteNs) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






333




							//compute return-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






334




335




							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






336




							//for each target value of the function









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






337




							for(D d5: computeReturnFlowFunction(retFunction, d4, Collections.singleton(d1))) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






338




								//update the caller-side summary function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






339




340




								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






341




								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);							









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






342




								propagate(d1, retSiteN, d5, f.composeWith(fPrime), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






343




344




345




346




347




348




							}
						}
					}
				}		
			}
		}









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






349




350




		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






351




352




353




		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






354




			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






355




				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






356




				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






357




358




359




360




361




			}
		}
	}

	/**









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






362




363




364




365




366




367




368




369




370




371




372




373




374




375




376




	 * Computes the call-to-return flow function for the given call-site
	 * asbtraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the return size
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}

	/**
	 * Lines 21-32 of the algorithm.









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






377




378




379




380




381




382




	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






383




	 */









Merge branch 'develop' into forks/java-fw-bw


 

 


Eric Bodden
committed
Jul 06, 2013






384




	protected void processExit(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






385




386




387




388




389




390




391




		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






392




393




394




395




		//for each of the method's start points, determine incoming calls
		Set<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);
		Set<Entry<N,Set<D>>> inc = new HashSet<Map.Entry<N,Set<D>>>();
		for(N sP: startPointsOf) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






396




397




			//line 21.1 of Naeem/Lhotak/Rodriguez
			









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






398




			//register end-summary









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






399




400




401




			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






402




403




404




405




406




407




408




409




410




411




412




413




414




415




				inc.addAll(incoming(d1, sP));
			}	
		}
		
		//for each incoming call edge already processed
		//(see processCall(..))
		for (Entry<N,Set<D>> entry: inc) {
			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






416




				Set<D> targets = computeReturnFlowFunction(retFunction, d2, entry.getValue());









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






417




418




419




420




421




422




423




424




425




426




427




428




429




430




				//for each incoming-call value
				for(D d4: entry.getValue()) {
					//for each target value at the return site
					//line 23
					for(D d5: targets) {
						//compute composed function
						EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
						EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
						EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);
						//for each jump function coming into the call, propagate to return site using the composed function
						for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
							EdgeFunction<V> f3 = valAndFunc.getValue();
							if(!f3.equalTo(allTop)) {
								D d3 = valAndFunc.getKey();









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






431




								propagate(d3, retSiteC, d5, f3.composeWith(fPrime), c, false);









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






432




433




434




435




							}
						}
					}
				}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






436




			}









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






437




438




		}
		









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






439




		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow









further fix for followReturnPastSeeds:


 

 


Eric Bodden
committed
Jul 08, 2013






440




441




442




		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






443




			// only propagate up if we 









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






444




445




446




447




448




449




450




451




				Set<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;
						Set<D> targets = retFunction.computeTargets(d2);
						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






452




							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






453




						}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






454




455




					}
				}









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






456




457




458




459




460




461




462




463




				//in cases where there are no callers, the return statement would normally not be processed at all;
				//this might be undesirable if the flow function has a side effect such as registering a taint;
				//instead we thus call the return flow function will a null caller
				if(callers.isEmpty()) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
					flowFunctionConstructionCount++;
					retFunction.computeTargets(d2);
				}









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






464




			}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






465




466




		}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






467




468




469




470




471




472




473




474




475




476




477




478




479




	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callerSideD1s The abstractions at the callers' start nodes.
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction
			(FlowFunction<D> retFunction, D d2, Set<D> callerSideD1s) {
		return retFunction.computeTargets(d2);
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






480




481




	/**
	 * Lines 33-37 of the algorithm.









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






482




	 * Simply propagate normal, intra-procedural flows.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






483




484




	 * @param edge
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






485




	private void processNormalFlow(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






486




487




488




489




490




491




492




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






493




			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






494




495




			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






496




				propagate(d1, m, d3, fprime, null, false); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






497




498




499




500




			}
		}
	}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






501




502




503




504




505




506




507




508




509




510




511




512




513




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions-
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */
	protected Set<D> computeNormalFlowFunction
			(FlowFunction<D> flowFunction, D d1, D d2) {
		return flowFunction.computeTargets(d2);
	}










changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






514




515




516




517




518




519




520




521




522




	/**
	 * Propagates the flow further down the exploded super graph, merging any edge function that might
	 * already have been computed for targetVal at target. 
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






523




524




	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






525




	 */









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






526




527




528




	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






529




		EdgeFunction<V> jumpFnE;









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






530




531




		EdgeFunction<V> fPrime;
		boolean newFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






532




533




		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






534




535




536




537




			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






538




539




				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






540




541




542




		}

		if(newFunction) {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






543




			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






544




			scheduleEdgeProcessing(edge);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






545




546




547




548





			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






549




550




					result.append(getDebugName());
					result.append(": ");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






551




552




553




554




555




556




557




558




559




560




561




562




563




564




565




566




					result.append("EDGE:  <");
					result.append(icfg.getMethodOf(target));
					result.append(",");
					result.append(sourceVal);
					result.append("> -> <");
					result.append(target);
					result.append(",");
					result.append(targetVal);
					result.append("> - ");
					result.append(fPrime);
					System.err.println(result.toString());
				}
			}
		}
	}
	









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






567




568




569




570




571




	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






572




573




574




575




576




577




578




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {
				setVal(startPoint, val, valueLattice.bottomElement());
				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); 
				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
			}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






579




		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






580




		









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






581




582




583




584




585




586




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






587




		









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






588




589




590




591




592




593




594




595




596




		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






597




598




		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






599




		for(int t=0;t<numThreads; t++) {









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






600




601




			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






602




		}









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






603




604




605




606




607




608




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






609




610




611




612




613




614




615




616




617




618




619




620




621




622




623




624




625




626




627




628




629




630




631




632




633




634




635




636




637




638




639




640




641




642




643




644




645




646




647




648




649




	}

	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();		
		M p = icfg.getMethodOf(n);
		for(N c: icfg.getCallsFromWithin(p)) {					
			Set<Entry<D, EdgeFunction<V>>> entries; 
			synchronized (jumpFn) {
				entries = jumpFn.forwardLookup(d,c).entrySet();
				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {
					D dPrime = dPAndFP.getKey();
					EdgeFunction<V> fPrime = dPAndFP.getValue();
					N sP = n;
					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValueAtCall(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();
		for(M q: icfg.getCalleesOfCallAt(n)) {
			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);
			flowFunctionConstructionCount++;
			for(D dPrime: callFlowFunction.computeTargets(d)) {
				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);
				for(N startPoint: icfg.getStartPointsOf(q)) {
					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValue(N nHashN, D nHashD, V v) {
		synchronized (val) {
			V valNHash = val(nHashN, nHashD);
			V vPrime = valueLattice.join(valNHash,v);
			if(!vPrime.equals(valNHash)) {
				setVal(nHashN, nHashD, vPrime);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






650




				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






651




652




653




654




655




			}
		}
	}

	private V val(N nHashN, D nHashD){ 









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






656




657




658




659




		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






660




661




662




663




		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






664




665




	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






666




		synchronized (val) {









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






667




668




669




670




			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






671




		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






672




673




674




675




		if(DEBUG)
			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);
	}










removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






676




	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






677




678




679




680




681




682




683




		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






684




685




686




687




688




689




690




691




692




693




694




695




	private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {
		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);
		if(map==null) return Collections.emptySet();
		return map.cellSet();
	}

	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {
		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);
		if(summaries==null) {
			summaries = HashBasedTable.create();
			endSummary.put(sP, d1, summaries);
		}









undoing previous "fix"; as discussed with Steven, it is not required (see comment)


 

 


Eric Bodden
committed
Dec 12, 2012






696




697




698




699




		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






700




701




702




703




704




705




706




707




708




709




710




711




712




713




714




715




716




717




718




719




720




721




722




	}	
	
	private Set<Entry<N, Set<D>>> incoming(D d1, N sP) {
		Map<N, Set<D>> map = incoming.get(sP, d1);
		if(map==null) return Collections.emptySet();
		return map.entrySet();		
	}
	
	private void addIncoming(N sP, D d3, N n, D d2) {
		Map<N, Set<D>> summaries = incoming.get(sP, d3);
		if(summaries==null) {
			summaries = new HashMap<N, Set<D>>();
			incoming.put(sP, d3, summaries);
		}
		Set<D> set = summaries.get(n);
		if(set==null) {
			set = new HashSet<D>();
			summaries.put(n,set);
		}
		set.add(d2);
	}	
	
	/**









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






723




724




	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






725




726




	 */
	public V resultAt(N stmt, D value) {









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






727




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






728




729




730




731




732




		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






733




734




	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






735




736




737




	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






738




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






739




740




741




742




743




744




745




		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






746




747




748




749




750




751




752




	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






753




	









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






754




755




756




757




758




759




760




761




	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "";
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






762




763




764




765




766




767




768




769




770




771




772




773




	public void printStats() {
		if(DEBUG) {
			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {
			System.err.println("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






774




		private final PathEdge<N,D> edge;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






775














removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






776




		public PathEdgeProcessingTask(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






777




778




779




780




781




782




783




784




785




786




787




788




789




790




791




792




793




794




795




796




797




798




799




800




801




802




803




804




			this.edge = edge;
		}

		public void run() {
			if(icfg.isCallStmt(edge.getTarget())) {
				processCall(edge);
			} else {
				//note that some statements, such as "throw" may be
				//both an exit statement and a "normal" statement
				if(icfg.isExitStmt(edge.getTarget())) {
					processExit(edge);
				}
				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {
					processNormalFlow(edge);
				}
			}
		}
	}
	
	private class ValuePropagationTask implements Runnable {
		private final Pair<N, D> nAndD;

		public ValuePropagationTask(Pair<N,D> nAndD) {
			this.nAndD = nAndD;
		}

		public void run() {
			N n = nAndD.getO1();









bug fix for value computation (need to treat initialSeeds just as method start nodes)


 

 


Eric Bodden
committed
Feb 14, 2013






805




			if(icfg.isStartPoint(n) ||









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






806




				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






807




808




809




810




811




812




813




814




815




816




817




818




819




820




821




822




823




824




825




826




827




828




829




830




831




832




833




834




835




836




837




838




839




840




841




842




843




844




845




				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
		}
	}
	
	private class ValueComputationTask implements Runnable {
		private final N[] values;
		final int num;

		public ValueComputationTask(N[] values, int num) {
			this.values = values;
			this.num = num;
		}

		public void run() {
			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;
			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {
				N n = values[i];
				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					
					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;
					lookupByTarget = jumpFn.lookupByTarget(n);
					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {
						D dPrime = sourceValTargetValAndFunction.getRowKey();
						D d = sourceValTargetValAndFunction.getColumnKey();
						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();
						synchronized (val) {
							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));
						}
						flowFunctionApplicationCount++;
					}
				}
			}
		}
	}

}








IDESolver.java



30.1 KB










IDESolver.java



30.1 KB









Newer










Older
NewerOlder







license headers


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






3




 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






4




5




6




7




8




9




10




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






11




 *     Marc-Andre Laverdiere-Papineau - Fixed race condition









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






12




 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15















renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






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




import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.EdgeFunctionCache;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.edgefunc.EdgeIdentity;










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




import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






37




import java.util.concurrent.LinkedBlockingQueue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






38




39




40




41




42




43




44




45




46




import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






47














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 
 * Note that this solver and its data structures internally use mostly {@link LinkedHashSet}s
 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






58




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






59




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






60




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






61




62




63




64




65




66




67




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









replaced env variable by property


 

 


Eric Bodden
committed
Jan 24, 2013






68




	public static final boolean DEBUG = !System.getProperty("HEROS_DEBUG", "false").equals("false");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




71




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






72




	protected CountingThreadPoolExecutor executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






73




74




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






75




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






76




77




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






78




	protected final JumpFunctions<N,D,V> jumpFn;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






79




80




	
	@SynchronizedBy("thread safe data structure, only modified internally")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






81




	protected final I icfg;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






82




83




84




85




	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






86




	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






87




88




89




90





	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






91




	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






92




93




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






94




	protected final FlowFunctions<N, D, M> flowFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






95




96





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






97




	protected final EdgeFunctions<N,D,M,V> edgeFunctions;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






98




99





	@DontSynchronize("only used by single thread")









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






100




	protected final Map<N,Set<D>> initialSeeds;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






101




102





	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






103




	protected final JoinLattice<V> valueLattice;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






104




105




	
	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






106




	protected final EdgeFunction<V> allTop;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






107














adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






108




	@SynchronizedBy("consistent lock on field")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






109




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




	
	@DontSynchronize("benign races")
	public long flowFunctionApplicationCount;

	@DontSynchronize("benign races")
	public long flowFunctionConstructionCount;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionConstruction;
	
	@DontSynchronize("benign races")
	public long durationFlowFunctionApplication;

	@DontSynchronize("stateless")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






127




	protected final D zeroValue;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






128




129




	
	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






130




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






131




132





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






133




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






134














making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






135




136




	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






137














make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






138




139




140




	@DontSynchronize("readOnly")
	protected final boolean computeValues;










added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






141




142




143




144




	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






145




146




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






147




148




	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






149




150




151




152




153




154




	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






155




	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






156




157




158




159




160




		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();









added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013






161




		this.icfg = tabulationProblem.interproceduralCFG();		









refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






162




163




		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




178




179




180




181




182




		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();
		if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}
		if(edgeFunctionCacheBuilder!=null) {
			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);
			edgeFunctions = efCache;
		} else {
			efCache = null;
		}
		this.flowFunctions = flowFunctions;
		this.edgeFunctions = edgeFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.valueLattice = tabulationProblem.joinLattice();
		this.allTop = tabulationProblem.allTopFunction();
		this.jumpFn = new JumpFunctions<N,D,V>(allTop);









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






183




		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






184




		this.numThreads = Math.max(1,tabulationProblem.numThreads());









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






185




		this.computeValues = tabulationProblem.computeValues();









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






186




		this.executor = getExecutor();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






187




188




189




190




191




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






192




	public void solve() {		









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






193




194




195




196




197




198




199




200




		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






201




	 */









extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013






202




	protected void submitInitialSeeds() {









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






203




204




205




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






206




				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






207




			}









extracted method awaitCompletionComputeValuesAndShutdown()


 

 


Eric Bodden
committed
Jan 30, 2013






208




209




210




211




212




213




214




215




216




217




			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






218




			final long before = System.currentTimeMillis();









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






219




220




			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






221




222




			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






223




		if(computeValues) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






224




225




226




227




228




229




230




			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






231




232




233




		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






234




		executor.shutdown();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






235




236




		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






237




238




239




240




241




242




243




		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






244




		try {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






245




			executor.awaitCompletion();









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






246




		} catch (InterruptedException e) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






247




248




249




250




251




			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);









fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013






252




		}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






253




254




	}










refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






255




256




257




258




    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






259




    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






260




261




    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






262




    }









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






263




	









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






264




265




266




267




268




    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){









indentation


 

 


Eric Bodden
committed
Jan 29, 2013






269




    	executor.execute(vpt);









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






270




271




    }
  









comments


 

 


Eric Bodden
committed
Jan 28, 2013






272




273




274




275




    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






276




277




278




	private void scheduleValueComputationTask(ValueComputationTask task) {
		executor.execute(task);
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






279




280




	
	/**









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






281




282




	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






283




284




	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






285




	 * 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






286




287




	 * @param edge an edge whose target node resembles a method call
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






288




	private void processCall(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






289




290




291




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






292




293




		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






294




		









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






295




		//for each possible callee









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






296




297




		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






298




299




			
			//compute the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






300




301




302




			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






303




304




			
			//for each callee's start point(s)









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






305




			Set<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






306




			for(N sP: startPointsOf) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






307




				//for each result node of the call-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






308




				for(D d3: res) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






309




					//create initial self-loop









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






310




					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






311




	









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






312




					//register the fact that <sp,d3> has an incoming edge from <n,d2>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






313




314




315




316




317




					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






318




319




320




						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));
						
						assert !jumpFn.reverseLookup(n, d2).isEmpty();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






321




322




323




					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






324




					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






325




					//create new caller-side jump functions to the return sites









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






326




					//because we have observed a potentially new incoming edge into <sP,d3>









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






327




328




329




330




					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






331




						//for each return site









Replaced the duplicate call to the icfg by an access to cached structure we have anyway


 

 


Steven Arzt
committed
Mar 11, 2013






332




						for(N retSiteN: returnSiteNs) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






333




							//compute return-flow function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






334




335




							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






336




							//for each target value of the function









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






337




							for(D d5: computeReturnFlowFunction(retFunction, d4, Collections.singleton(d1))) {









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






338




								//update the caller-side summary function









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






339




340




								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);









removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012






341




								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);							









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






342




								propagate(d1, retSiteN, d5, f.composeWith(fPrime), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






343




344




345




346




347




348




							}
						}
					}
				}		
			}
		}









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






349




350




		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






351




352




353




		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






354




			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






355




				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






356




				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






357




358




359




360




361




			}
		}
	}

	/**









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






362




363




364




365




366




367




368




369




370




371




372




373




374




375




376




	 * Computes the call-to-return flow function for the given call-site
	 * asbtraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the return size
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}

	/**
	 * Lines 21-32 of the algorithm.









comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012






377




378




379




380




381




382




	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






383




	 */









Merge branch 'develop' into forks/java-fw-bw


 

 


Eric Bodden
committed
Jul 06, 2013






384




	protected void processExit(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






385




386




387




388




389




390




391




		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






392




393




394




395




		//for each of the method's start points, determine incoming calls
		Set<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);
		Set<Entry<N,Set<D>>> inc = new HashSet<Map.Entry<N,Set<D>>>();
		for(N sP: startPointsOf) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






396




397




			//line 21.1 of Naeem/Lhotak/Rodriguez
			









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






398




			//register end-summary









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






399




400




401




			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






402




403




404




405




406




407




408




409




410




411




412




413




414




415




				inc.addAll(incoming(d1, sP));
			}	
		}
		
		//for each incoming call edge already processed
		//(see processCall(..))
		for (Entry<N,Set<D>> entry: inc) {
			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






416




				Set<D> targets = computeReturnFlowFunction(retFunction, d2, entry.getValue());









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






417




418




419




420




421




422




423




424




425




426




427




428




429




430




				//for each incoming-call value
				for(D d4: entry.getValue()) {
					//for each target value at the return site
					//line 23
					for(D d5: targets) {
						//compute composed function
						EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
						EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
						EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);
						//for each jump function coming into the call, propagate to return site using the composed function
						for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
							EdgeFunction<V> f3 = valAndFunc.getValue();
							if(!f3.equalTo(allTop)) {
								D d3 = valAndFunc.getKey();









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






431




								propagate(d3, retSiteC, d5, f3.composeWith(fPrime), c, false);









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






432




433




434




435




							}
						}
					}
				}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






436




			}









performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013






437




438




		}
		









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






439




		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow









further fix for followReturnPastSeeds:


 

 


Eric Bodden
committed
Jul 08, 2013






440




441




442




		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {









improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013






443




			// only propagate up if we 









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






444




445




446




447




448




449




450




451




				Set<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;
						Set<D> targets = retFunction.computeTargets(d2);
						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






452




							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






453




						}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






454




455




					}
				}









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013






456




457




458




459




460




461




462




463




				//in cases where there are no callers, the return statement would normally not be processed at all;
				//this might be undesirable if the flow function has a side effect such as registering a taint;
				//instead we thus call the return flow function will a null caller
				if(callers.isEmpty()) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
					flowFunctionConstructionCount++;
					retFunction.computeTargets(d2);
				}









improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012






464




			}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






465




466




		}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






467




468




469




470




471




472




473




474




475




476




477




478




479




	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callerSideD1s The abstractions at the callers' start nodes.
	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction
			(FlowFunction<D> retFunction, D d2, Set<D> callerSideD1s) {
		return retFunction.computeTargets(d2);
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






480




481




	/**
	 * Lines 33-37 of the algorithm.









implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012






482




	 * Simply propagate normal, intra-procedural flows.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






483




484




	 * @param edge
	 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






485




	private void processNormalFlow(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






486




487




488




489




490




491




492




		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






493




			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






494




495




			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






496




				propagate(d1, m, d3, fprime, null, false); 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






497




498




499




500




			}
		}
	}
	









Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013






501




502




503




504




505




506




507




508




509




510




511




512




513




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions-
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */
	protected Set<D> computeNormalFlowFunction
			(FlowFunction<D> flowFunction, D d1, D d2) {
		return flowFunction.computeTargets(d2);
	}










changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






514




515




516




517




518




519




520




521




522




	/**
	 * Propagates the flow further down the exploded super graph, merging any edge function that might
	 * already have been computed for targetVal at target. 
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






523




524




	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






525




	 */









first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013






526




527




528




	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






529




		EdgeFunction<V> jumpFnE;









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






530




531




		EdgeFunction<V> fPrime;
		boolean newFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






532




533




		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






534




535




536




537




			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






538




539




				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}









fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013






540




541




542




		}

		if(newFunction) {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






543




			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






544




			scheduleEdgeProcessing(edge);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






545




546




547




548





			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






549




550




					result.append(getDebugName());
					result.append(": ");









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






551




552




553




554




555




556




557




558




559




560




561




562




563




564




565




566




					result.append("EDGE:  <");
					result.append(icfg.getMethodOf(target));
					result.append(",");
					result.append(sourceVal);
					result.append("> -> <");
					result.append(target);
					result.append(",");
					result.append(targetVal);
					result.append("> - ");
					result.append(fPrime);
					System.err.println(result.toString());
				}
			}
		}
	}
	









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






567




568




569




570




571




	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






572




573




574




575




576




577




578




		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {
				setVal(startPoint, val, valueLattice.bottomElement());
				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); 
				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
			}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






579




		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






580




		









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






581




582




583




584




585




586




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






587




		









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






588




589




590




591




592




593




594




595




596




		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;









Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013






597




598




		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






599




		for(int t=0;t<numThreads; t++) {









refactoring


 

 


Eric Bodden
committed
Jan 28, 2013






600




601




			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






602




		}









Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






603




604




605




606




607




608




		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






609




610




611




612




613




614




615




616




617




618




619




620




621




622




623




624




625




626




627




628




629




630




631




632




633




634




635




636




637




638




639




640




641




642




643




644




645




646




647




648




649




	}

	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();		
		M p = icfg.getMethodOf(n);
		for(N c: icfg.getCallsFromWithin(p)) {					
			Set<Entry<D, EdgeFunction<V>>> entries; 
			synchronized (jumpFn) {
				entries = jumpFn.forwardLookup(d,c).entrySet();
				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {
					D dPrime = dPAndFP.getKey();
					EdgeFunction<V> fPrime = dPAndFP.getValue();
					N sP = n;
					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValueAtCall(Pair<N, D> nAndD, N n) {
		D d = nAndD.getO2();
		for(M q: icfg.getCalleesOfCallAt(n)) {
			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);
			flowFunctionConstructionCount++;
			for(D dPrime: callFlowFunction.computeTargets(d)) {
				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);
				for(N startPoint: icfg.getStartPointsOf(q)) {
					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));
					flowFunctionApplicationCount++;
				}
			}
		}
	}
	
	private void propagateValue(N nHashN, D nHashD, V v) {
		synchronized (val) {
			V valNHash = val(nHashN, nHashD);
			V vPrime = valueLattice.join(valNHash,v);
			if(!vPrime.equals(valNHash)) {
				setVal(nHashN, nHashD, vPrime);









minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013






650




				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






651




652




653




654




655




			}
		}
	}

	private V val(N nHashN, D nHashD){ 









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






656




657




658




659




		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






660




661




662




663




		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






664




665




	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






666




		synchronized (val) {









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






667




668




669




670




			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






671




		}









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






672




673




674




675




		if(DEBUG)
			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);
	}










removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






676




	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {









reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012






677




678




679




680




681




682




683




		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






684




685




686




687




688




689




690




691




692




693




694




695




	private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {
		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);
		if(map==null) return Collections.emptySet();
		return map.cellSet();
	}

	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {
		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);
		if(summaries==null) {
			summaries = HashBasedTable.create();
			endSummary.put(sP, d1, summaries);
		}









undoing previous "fix"; as discussed with Steven, it is not required (see comment)


 

 


Eric Bodden
committed
Dec 12, 2012






696




697




698




699




		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






700




701




702




703




704




705




706




707




708




709




710




711




712




713




714




715




716




717




718




719




720




721




722




	}	
	
	private Set<Entry<N, Set<D>>> incoming(D d1, N sP) {
		Map<N, Set<D>> map = incoming.get(sP, d1);
		if(map==null) return Collections.emptySet();
		return map.entrySet();		
	}
	
	private void addIncoming(N sP, D d3, N n, D d2) {
		Map<N, Set<D>> summaries = incoming.get(sP, d3);
		if(summaries==null) {
			summaries = new HashMap<N, Set<D>>();
			incoming.put(sP, d3, summaries);
		}
		Set<D> set = summaries.get(n);
		if(set==null) {
			set = new HashSet<D>();
			summaries.put(n,set);
		}
		set.add(d2);
	}	
	
	/**









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






723




724




	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






725




726




	 */
	public V resultAt(N stmt, D value) {









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






727




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






728




729




730




731




732




		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.









memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013






733




734




	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






735




736




737




	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value









adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013






738




		//no need to synchronize here as all threads are known to have terminated









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






739




740




741




742




743




744




745




		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}









making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013






746




747




748




749




750




751




752




	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}









changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013






753




	









added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013






754




755




756




757




758




759




760




761




	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "";
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






762




763




764




765




766




767




768




769




770




771




772




773




	public void printStats() {
		if(DEBUG) {
			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {
			System.err.println("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






774




		private final PathEdge<N,D> edge;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






775














removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






776




		public PathEdgeProcessingTask(PathEdge<N,D> edge) {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






777




778




779




780




781




782




783




784




785




786




787




788




789




790




791




792




793




794




795




796




797




798




799




800




801




802




803




804




			this.edge = edge;
		}

		public void run() {
			if(icfg.isCallStmt(edge.getTarget())) {
				processCall(edge);
			} else {
				//note that some statements, such as "throw" may be
				//both an exit statement and a "normal" statement
				if(icfg.isExitStmt(edge.getTarget())) {
					processExit(edge);
				}
				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {
					processNormalFlow(edge);
				}
			}
		}
	}
	
	private class ValuePropagationTask implements Runnable {
		private final Pair<N, D> nAndD;

		public ValuePropagationTask(Pair<N,D> nAndD) {
			this.nAndD = nAndD;
		}

		public void run() {
			N n = nAndD.getO1();









bug fix for value computation (need to treat initialSeeds just as method start nodes)


 

 


Eric Bodden
committed
Feb 14, 2013






805




			if(icfg.isStartPoint(n) ||









changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013






806




				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






807




808




809




810




811




812




813




814




815




816




817




818




819




820




821




822




823




824




825




826




827




828




829




830




831




832




833




834




835




836




837




838




839




840




841




842




843




844




845




				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
		}
	}
	
	private class ValueComputationTask implements Runnable {
		private final N[] values;
		final int num;

		public ValueComputationTask(N[] values, int num) {
			this.values = values;
			this.num = num;
		}

		public void run() {
			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;
			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {
				N n = values[i];
				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					
					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;
					lookupByTarget = jumpFn.lookupByTarget(n);
					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {
						D dPrime = sourceValTargetValAndFunction.getRowKey();
						D d = sourceValTargetValAndFunction.getColumnKey();
						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();
						synchronized (val) {
							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));
						}
						flowFunctionApplicationCount++;
					}
				}
			}
		}
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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden.



minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013



minor cleanups


 

 

minor cleanups

 

Eric Bodden
committed
Jan 26, 2013

3
 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal



license headers


 

 


Eric Bodden
committed
Nov 29, 2012



license headers


 

 

license headers

 

Eric Bodden
committed
Nov 29, 2012

4

5

6

7

8

9

10
 * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation



minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013



minor cleanups


 

 

minor cleanups

 

Eric Bodden
committed
Jan 26, 2013

11
 *     Marc-Andre Laverdiere-Papineau - Fixed race condition *     Marc-Andre Laverdiere-Papineau - Fixed race condition



license headers


 

 


Eric Bodden
committed
Nov 29, 2012



license headers


 

 

license headers

 

Eric Bodden
committed
Nov 29, 2012

12
 ******************************************************************************/ ******************************************************************************/



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

14

15




renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

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
import heros.DontSynchronize;importheros.DontSynchronize;import heros.EdgeFunction;importheros.EdgeFunction;import heros.EdgeFunctionCache;importheros.EdgeFunctionCache;import heros.EdgeFunctions;importheros.EdgeFunctions;import heros.FlowFunction;importheros.FlowFunction;import heros.FlowFunctionCache;importheros.FlowFunctionCache;import heros.FlowFunctions;importheros.FlowFunctions;import heros.IDETabulationProblem;importheros.IDETabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;import heros.JoinLattice;importheros.JoinLattice;import heros.SynchronizedBy;importheros.SynchronizedBy;import heros.ZeroedFlowFunctions;importheros.ZeroedFlowFunctions;import heros.edgefunc.EdgeIdentity;importheros.edgefunc.EdgeIdentity;



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
import java.util.Collections;importjava.util.Collections;import java.util.HashMap;importjava.util.HashMap;import java.util.HashSet;importjava.util.HashSet;import java.util.List;importjava.util.List;import java.util.Map;importjava.util.Map;import java.util.Map.Entry;importjava.util.Map.Entry;import java.util.Set;importjava.util.Set;



Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013



Fixed race condition in IDESolver and simplified the code


 

 

Fixed race condition in IDESolver and simplified the code

 

Marc-André Laverdière
committed
Jan 25, 2013

37
import java.util.concurrent.LinkedBlockingQueue;importjava.util.concurrent.LinkedBlockingQueue;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

38

39

40

41

42

43

44

45

46
import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;import com.google.common.base.Predicate;importcom.google.common.base.Predicate;import com.google.common.cache.CacheBuilder;importcom.google.common.cache.CacheBuilder;import com.google.common.collect.HashBasedTable;importcom.google.common.collect.HashBasedTable;import com.google.common.collect.Maps;importcom.google.common.collect.Maps;import com.google.common.collect.Table;importcom.google.common.collect.Table;import com.google.common.collect.Table.Cell;importcom.google.common.collect.Table.Cell;



renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

47




initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
/**/** * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv, * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv, * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}. * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}. *  *  * Note that this solver and its data structures internally use mostly {@link LinkedHashSet}s * Note that this solver and its data structures internally use mostly {@link LinkedHashSet}s * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This * is to produce, as much as possible, reproducible benchmarking results. We have found * is to produce, as much as possible, reproducible benchmarking results. We have found * that the iteration order can matter a lot in terms of speed. * that the iteration order can matter a lot in terms of speed. * *



moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012



moved dependencies on soot into separate package


 

 

moved dependencies on soot into separate package

 

Eric Bodden
committed
Nov 28, 2012

58
 * @param <N> The type of nodes in the interprocedural control-flow graph.  * @param <N> The type of nodes in the interprocedural control-flow graph. 



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

59
 * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem.



moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012



moved dependencies on soot into separate package


 

 

moved dependencies on soot into separate package

 

Eric Bodden
committed
Nov 28, 2012

60
 * @param <M> The type of objects used to represent methods. * @param <M> The type of objects used to represent methods.



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

61

62

63

64

65

66

67
 * @param <V> The type of values to be computed along flow edges. * @param <V> The type of values to be computed along flow edges. * @param <I> The type of inter-procedural control-flow graph being used. * @param <I> The type of inter-procedural control-flow graph being used. */ */public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {publicclassIDESolver<N,D,M,V,IextendsInterproceduralCFG<N,M>>{		public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();publicstaticCacheBuilder<Object,Object>DEFAULT_CACHE_BUILDER=CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();	



replaced env variable by property


 

 


Eric Bodden
committed
Jan 24, 2013



replaced env variable by property


 

 

replaced env variable by property

 

Eric Bodden
committed
Jan 24, 2013

68
	public static final boolean DEBUG = !System.getProperty("HEROS_DEBUG", "false").equals("false");publicstaticfinalbooleanDEBUG=!System.getProperty("HEROS_DEBUG","false").equals("false");



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

69

70

71
		//executor for dispatching individual compute jobs (may be multi-threaded)//executor for dispatching individual compute jobs (may be multi-threaded)	@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")



Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013



Revert "adding CountLatch"


 

 

Revert "adding CountLatch"

 

Eric Bodden
committed
Jan 28, 2013

72
	protected CountingThreadPoolExecutor executor;protectedCountingThreadPoolExecutorexecutor;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

73

74
		@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

75
	protected int numThreads;protectedintnumThreads;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

76

77
		@SynchronizedBy("thread safe data structure, consistent locking when used")@SynchronizedBy("thread safe data structure, consistent locking when used")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

78
	protected final JumpFunctions<N,D,V> jumpFn;protectedfinalJumpFunctions<N,D,V>jumpFn;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

79

80
		@SynchronizedBy("thread safe data structure, only modified internally")@SynchronizedBy("thread safe data structure, only modified internally")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

81
	protected final I icfg;protectedfinalIicfg;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

82

83

84

85
		//stores summaries that were queried before they were computed//stores summaries that were queried before they were computed	//see CC 2010 paper by Naeem, Lhotak and Rodriguez//see CC 2010 paper by Naeem, Lhotak and Rodriguez	@SynchronizedBy("consistent lock on 'incoming'")@SynchronizedBy("consistent lock on 'incoming'")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

86
	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();protectedfinalTable<N,D,Table<N,D,EdgeFunction<V>>>endSummary=HashBasedTable.create();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

87

88

89

90
	//edges going along calls//edges going along calls	//see CC 2010 paper by Naeem, Lhotak and Rodriguez//see CC 2010 paper by Naeem, Lhotak and Rodriguez	@SynchronizedBy("consistent lock on field")@SynchronizedBy("consistent lock on field")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

91
	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();protectedfinalTable<N,D,Map<N,Set<D>>>incoming=HashBasedTable.create();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

92

93
		@DontSynchronize("stateless")@DontSynchronize("stateless")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

94
	protected final FlowFunctions<N, D, M> flowFunctions;protectedfinalFlowFunctions<N,D,M>flowFunctions;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

95

96
	@DontSynchronize("stateless")@DontSynchronize("stateless")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

97
	protected final EdgeFunctions<N,D,M,V> edgeFunctions;protectedfinalEdgeFunctions<N,D,M,V>edgeFunctions;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

98

99
	@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")



changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...


 

 

changing initialization of analysis such that initialSeeds not is a mapping...

 

Eric Bodden
committed
Jul 05, 2013

100
	protected final Map<N,Set<D>> initialSeeds;protectedfinalMap<N,Set<D>>initialSeeds;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

101

102
	@DontSynchronize("stateless")@DontSynchronize("stateless")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

103
	protected final JoinLattice<V> valueLattice;protectedfinalJoinLattice<V>valueLattice;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

104

105
		@DontSynchronize("stateless")@DontSynchronize("stateless")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

106
	protected final EdgeFunction<V> allTop;protectedfinalEdgeFunction<V>allTop;



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




adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...


 

 

adding synchronization on "val" due to possible race conditions (thanks to...

 

Eric Bodden
committed
May 29, 2013

108
	@SynchronizedBy("consistent lock on field")@SynchronizedBy("consistent lock on field")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

109
	protected final Table<N,D,V> val = HashBasedTable.create();	protectedfinalTable<N,D,V>val=HashBasedTable.create();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long flowFunctionApplicationCount;publiclongflowFunctionApplicationCount;	@DontSynchronize("benign races")@DontSynchronize("benign races")	public long flowFunctionConstructionCount;publiclongflowFunctionConstructionCount;		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long propagationCount;publiclongpropagationCount;		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long durationFlowFunctionConstruction;publiclongdurationFlowFunctionConstruction;		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long durationFlowFunctionApplication;publiclongdurationFlowFunctionApplication;	@DontSynchronize("stateless")@DontSynchronize("stateless")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

127
	protected final D zeroValue;protectedfinalDzeroValue;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

128

129
		@DontSynchronize("readOnly")@DontSynchronize("readOnly")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

130
	protected final FlowFunctionCache<N,D,M> ffCache; protectedfinalFlowFunctionCache<N,D,M>ffCache;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

131

132
	@DontSynchronize("readOnly")@DontSynchronize("readOnly")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

133
	protected final EdgeFunctionCache<N,D,M,V> efCache;protectedfinalEdgeFunctionCache<N,D,M,V>efCache;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

134




making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012



making computation of unbalanced edges optional


 

 

making computation of unbalanced edges optional

 

Eric Bodden
committed
Dec 12, 2012

135

136
	@DontSynchronize("readOnly")@DontSynchronize("readOnly")	protected final boolean followReturnsPastSeeds;protectedfinalbooleanfollowReturnsPastSeeds;



added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013



added possibility to switch off auto-zeroing of flow functions


 

 

added possibility to switch off auto-zeroing of flow functions

 

Eric Bodden
committed
Jan 22, 2013

137




make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013



make computation of values optional


 

 

make computation of values optional

 

Eric Bodden
committed
Jan 29, 2013

138

139

140
	@DontSynchronize("readOnly")@DontSynchronize("readOnly")	protected final boolean computeValues;protectedfinalbooleancomputeValues;



added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013



added possibility to switch off auto-zeroing of flow functions


 

 

added possibility to switch off auto-zeroing of flow functions

 

Eric Bodden
committed
Jan 22, 2013

141

142

143

144
	/**/**	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * The solver must then be started by calling {@link #solve()}.	 * The solver must then be started by calling {@link #solve()}.	 */	 */



refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 

refactoring: autoAddZero is now set in IFDSTabulationProblem

 

Eric Bodden
committed
Jan 28, 2013

145

146
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {publicIDESolver(IDETabulationProblem<N,D,M,V,I>tabulationProblem){		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);this(tabulationProblem,DEFAULT_CACHE_BUILDER,DEFAULT_CACHE_BUILDER);



added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013



added possibility to switch off auto-zeroing of flow functions


 

 

added possibility to switch off auto-zeroing of flow functions

 

Eric Bodden
committed
Jan 22, 2013

147

148
	}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

149

150

151

152

153

154
	/**/**	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * {@link #solve()}.	 * {@link #solve()}.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 */	 */



refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 

refactoring: autoAddZero is now set in IFDSTabulationProblem

 

Eric Bodden
committed
Jan 28, 2013

155
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {publicIDESolver(IDETabulationProblem<N,D,M,V,I>tabulationProblem,@SuppressWarnings("rawtypes")CacheBuilderflowFunctionCacheBuilder,@SuppressWarnings("rawtypes")CacheBuilderedgeFunctionCacheBuilder){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

156

157

158

159

160
		if(DEBUG) {if(DEBUG){			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();flowFunctionCacheBuilder=flowFunctionCacheBuilder.recordStats();			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();edgeFunctionCacheBuilder=edgeFunctionCacheBuilder.recordStats();		}}		this.zeroValue = tabulationProblem.zeroValue();this.zeroValue=tabulationProblem.zeroValue();



added possibility to switch off auto-zeroing of flow functions


 

 


Eric Bodden
committed
Jan 22, 2013



added possibility to switch off auto-zeroing of flow functions


 

 

added possibility to switch off auto-zeroing of flow functions

 

Eric Bodden
committed
Jan 22, 2013

161
		this.icfg = tabulationProblem.interproceduralCFG();		this.icfg=tabulationProblem.interproceduralCFG();



refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 

refactoring: autoAddZero is now set in IFDSTabulationProblem

 

Eric Bodden
committed
Jan 28, 2013

162

163
		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?FlowFunctions<N,D,M>flowFunctions=tabulationProblem.autoAddZero()?				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); newZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(),tabulationProblem.zeroValue()):tabulationProblem.flowFunctions();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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

178

179

180

181

182
		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();EdgeFunctions<N,D,M,V>edgeFunctions=tabulationProblem.edgeFunctions();		if(flowFunctionCacheBuilder!=null) {if(flowFunctionCacheBuilder!=null){			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);ffCache=newFlowFunctionCache<N,D,M>(flowFunctions,flowFunctionCacheBuilder);			flowFunctions = ffCache;flowFunctions=ffCache;		} else {}else{			ffCache = null;ffCache=null;		}}		if(edgeFunctionCacheBuilder!=null) {if(edgeFunctionCacheBuilder!=null){			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);efCache=newEdgeFunctionCache<N,D,M,V>(edgeFunctions,edgeFunctionCacheBuilder);			edgeFunctions = efCache;edgeFunctions=efCache;		} else {}else{			efCache = null;efCache=null;		}}		this.flowFunctions = flowFunctions;this.flowFunctions=flowFunctions;		this.edgeFunctions = edgeFunctions;this.edgeFunctions=edgeFunctions;		this.initialSeeds = tabulationProblem.initialSeeds();this.initialSeeds=tabulationProblem.initialSeeds();		this.valueLattice = tabulationProblem.joinLattice();this.valueLattice=tabulationProblem.joinLattice();		this.allTop = tabulationProblem.allTopFunction();this.allTop=tabulationProblem.allTopFunction();		this.jumpFn = new JumpFunctions<N,D,V>(allTop);this.jumpFn=newJumpFunctions<N,D,V>(allTop);



making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012



making computation of unbalanced edges optional


 

 

making computation of unbalanced edges optional

 

Eric Bodden
committed
Dec 12, 2012

183
		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();this.followReturnsPastSeeds=tabulationProblem.followReturnsPastSeeds();



number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013



number of threads is now configured through SolverConfiguration, a new super...


 

 

number of threads is now configured through SolverConfiguration, a new super...

 

Eric Bodden
committed
Jan 29, 2013

184
		this.numThreads = Math.max(1,tabulationProblem.numThreads());this.numThreads=Math.max(1,tabulationProblem.numThreads());



make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013



make computation of values optional


 

 

make computation of values optional

 

Eric Bodden
committed
Jan 29, 2013

185
		this.computeValues = tabulationProblem.computeValues();this.computeValues=tabulationProblem.computeValues();



making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013



making executor exchangeable


 

 

making executor exchangeable

 

Eric Bodden
committed
Jan 29, 2013

186
		this.executor = getExecutor();this.executor=getExecutor();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

187

188

189

190

191
	}}	/**/**	 * Runs the solver on the configured problem. This can take some time.	 * Runs the solver on the configured problem. This can take some time.	 */	 */



number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013



number of threads is now configured through SolverConfiguration, a new super...


 

 

number of threads is now configured through SolverConfiguration, a new super...

 

Eric Bodden
committed
Jan 29, 2013

192
	public void solve() {		publicvoidsolve(){



extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013



extracting method submitInitialSeeds to allow submission without having to wait


 

 

extracting method submitInitialSeeds to allow submission without having to wait

 

Eric Bodden
committed
Jul 06, 2013

193

194

195

196

197

198

199

200
		submitInitialSeeds();submitInitialSeeds();		awaitCompletionComputeValuesAndShutdown();awaitCompletionComputeValuesAndShutdown();	}}	/**/**	 * Schedules the processing of initial seeds, initiating the analysis.	 * Schedules the processing of initial seeds, initiating the analysis.	 * Clients should only call this methods if performing synchronization on	 * Clients should only call this methods if performing synchronization on	 * their own. Normally, {@link #solve()} should be called instead.	 * their own. Normally, {@link #solve()} should be called instead.



changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows


 

 

changed signature of "propagate" to include original call site for return and call flows

 

Eric Bodden
committed
Jul 06, 2013

201
	 */	 */



extracting method submitInitialSeeds to allow submission without having to wait


 

 


Eric Bodden
committed
Jul 06, 2013



extracting method submitInitialSeeds to allow submission without having to wait


 

 

extracting method submitInitialSeeds to allow submission without having to wait

 

Eric Bodden
committed
Jul 06, 2013

202
	protected void submitInitialSeeds() {protectedvoidsubmitInitialSeeds(){



changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...


 

 

changing initialization of analysis such that initialSeeds not is a mapping...

 

Eric Bodden
committed
Jul 05, 2013

203

204

205
		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {for(Entry<N,Set<D>>seed:initialSeeds.entrySet()){			N startPoint = seed.getKey();NstartPoint=seed.getKey();			for(D val: seed.getValue()) {for(Dval:seed.getValue()){



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

206
				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);propagate(zeroValue,startPoint,val,EdgeIdentity.<V>v(),null,false);



changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...


 

 

changing initialization of analysis such that initialSeeds not is a mapping...

 

Eric Bodden
committed
Jul 05, 2013

207
			}}



extracted method awaitCompletionComputeValuesAndShutdown()


 

 


Eric Bodden
committed
Jan 30, 2013



extracted method awaitCompletionComputeValuesAndShutdown()


 

 

extracted method awaitCompletionComputeValuesAndShutdown()

 

Eric Bodden
committed
Jan 30, 2013

208

209

210

211

212

213

214

215

216

217
			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());jumpFn.addFunction(zeroValue,startPoint,zeroValue,EdgeIdentity.<V>v());		}}	}}	/**/**	 * Awaits the completion of the exploded super graph. When complete, computes result values,	 * Awaits the completion of the exploded super graph. When complete, computes result values,	 * shuts down the executor and returns.	 * shuts down the executor and returns.	 */	 */	protected void awaitCompletionComputeValuesAndShutdown() {protectedvoidawaitCompletionComputeValuesAndShutdown(){		{{



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

218
			final long before = System.currentTimeMillis();finallongbefore=System.currentTimeMillis();



simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling


 

 

simplified exception handling

 

Eric Bodden
committed
Jul 11, 2013

219

220
			//run executor and await termination of tasks//run executor and await termination of tasks			runExecutorAndAwaitCompletion();runExecutorAndAwaitCompletion();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

221

222
			durationFlowFunctionConstruction = System.currentTimeMillis() - before;durationFlowFunctionConstruction=System.currentTimeMillis()-before;		}}



make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013



make computation of values optional


 

 

make computation of values optional

 

Eric Bodden
committed
Jan 29, 2013

223
		if(computeValues) {if(computeValues){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

224

225

226

227

228

229

230
			final long before = System.currentTimeMillis();finallongbefore=System.currentTimeMillis();			computeValues();computeValues();			durationFlowFunctionApplication = System.currentTimeMillis() - before;durationFlowFunctionApplication=System.currentTimeMillis()-before;		}}		if(DEBUG) if(DEBUG)			printStats();printStats();		



fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence


 

 

fixing shutdown sequence

 

Eric Bodden
committed
Jan 29, 2013

231

232

233
		//ask executor to shut down;//ask executor to shut down;		//this will cause new submissions to the executor to be rejected,//this will cause new submissions to the executor to be rejected,		//but at this point all tasks should have completed anyway//but at this point all tasks should have completed anyway



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

234
		executor.shutdown();executor.shutdown();



fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence


 

 

fixing shutdown sequence

 

Eric Bodden
committed
Jan 29, 2013

235

236
		//similarly here: we await termination, but this should happen instantaneously,//similarly here: we await termination, but this should happen instantaneously,		//as all tasks should have completed//as all tasks should have completed



simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling


 

 

simplified exception handling

 

Eric Bodden
committed
Jul 11, 2013

237

238

239

240

241

242

243
		runExecutorAndAwaitCompletion();runExecutorAndAwaitCompletion();	}}	/**/**	 * Runs execution, re-throwing exceptions that might be thrown during its execution.	 * Runs execution, re-throwing exceptions that might be thrown during its execution.	 */	 */	private void runExecutorAndAwaitCompletion() {privatevoidrunExecutorAndAwaitCompletion(){



fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence


 

 

fixing shutdown sequence

 

Eric Bodden
committed
Jan 29, 2013

244
		try {try{



simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling


 

 

simplified exception handling

 

Eric Bodden
committed
Jul 11, 2013

245
			executor.awaitCompletion();executor.awaitCompletion();



fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence


 

 

fixing shutdown sequence

 

Eric Bodden
committed
Jan 29, 2013

246
		} catch (InterruptedException e) {}catch(InterruptedExceptione){



simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling


 

 

simplified exception handling

 

Eric Bodden
committed
Jul 11, 2013

247

248

249

250

251
			e.printStackTrace();e.printStackTrace();		}}		Throwable exception = executor.getException();Throwableexception=executor.getException();		if(exception!=null) {if(exception!=null){			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);thrownewRuntimeException("There were exceptions during IDE analysis. Exiting.",exception);



fixing shutdown sequence


 

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence


 

 

fixing shutdown sequence

 

Eric Bodden
committed
Jan 29, 2013

252
		}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

253

254
	}}



refactoring


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring


 

 

refactoring

 

Eric Bodden
committed
Jan 28, 2013

255

256

257

258
    /**/**     * Dispatch the processing of a given edge. It may be executed in a different thread.     * Dispatch the processing of a given edge. It may be executed in a different thread.     * @param edge the edge to process     * @param edge the edge to process     */     */



removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge


 

 

removed superfluous type parameter from PathEdge

 

Eric Bodden
committed
Jul 06, 2013

259
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){protectedvoidscheduleEdgeProcessing(PathEdge<N,D>edge){



indentation


 

 


Eric Bodden
committed
Jan 29, 2013



indentation


 

 

indentation

 

Eric Bodden
committed
Jan 29, 2013

260

261
    	executor.execute(new PathEdgeProcessingTask(edge));executor.execute(newPathEdgeProcessingTask(edge));    	propagationCount++;propagationCount++;



refactoring


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring


 

 

refactoring

 

Eric Bodden
committed
Jan 28, 2013

262
    }}



Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013



Fixed race condition in IDESolver and simplified the code


 

 

Fixed race condition in IDESolver and simplified the code

 

Marc-André Laverdière
committed
Jan 25, 2013

263
	



refactoring


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring


 

 

refactoring

 

Eric Bodden
committed
Jan 28, 2013

264

265

266

267

268
    /**/**     * Dispatch the processing of a given value. It may be executed in a different thread.     * Dispatch the processing of a given value. It may be executed in a different thread.     * @param vpt     * @param vpt     */     */    private void scheduleValueProcessing(ValuePropagationTask vpt){privatevoidscheduleValueProcessing(ValuePropagationTaskvpt){



indentation


 

 


Eric Bodden
committed
Jan 29, 2013



indentation


 

 

indentation

 

Eric Bodden
committed
Jan 29, 2013

269
    	executor.execute(vpt);executor.execute(vpt);



refactoring


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring


 

 

refactoring

 

Eric Bodden
committed
Jan 28, 2013

270

271
    }}  



comments


 

 


Eric Bodden
committed
Jan 28, 2013



comments


 

 

comments

 

Eric Bodden
committed
Jan 28, 2013

272

273

274

275
    /**/**     * Dispatch the computation of a given value. It may be executed in a different thread.     * Dispatch the computation of a given value. It may be executed in a different thread.     * @param task     * @param task     */     */



refactoring


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring


 

 

refactoring

 

Eric Bodden
committed
Jan 28, 2013

276

277

278
	private void scheduleValueComputationTask(ValueComputationTask task) {privatevoidscheduleValueComputationTask(ValueComputationTasktask){		executor.execute(task);executor.execute(task);	}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

279

280
		/**/**



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

281

282
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.	 * 	 * 



comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012



comments and minor optimizations


 

 

comments and minor optimizations

 

Eric Bodden
committed
Dec 12, 2012

283

284
	 * For each possible callee, registers incoming call edges.	 * For each possible callee, registers incoming call edges.	 * Also propagates call-to-return flows and summarized callee flows within the caller. 	 * Also propagates call-to-return flows and summarized callee flows within the caller. 



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

285
	 * 	 * 



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

286

287
	 * @param edge an edge whose target node resembles a method call	 * @param edge an edge whose target node resembles a method call	 */	 */



removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge


 

 

removed superfluous type parameter from PathEdge

 

Eric Bodden
committed
Jul 06, 2013

288
	private void processCall(PathEdge<N,D> edge) {privatevoidprocessCall(PathEdge<N,D>edge){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

289

290

291
		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); // a call node; line 14...finalNn=edge.getTarget();// a call node; line 14...		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();



comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012



comments and minor optimizations


 

 

comments and minor optimizations

 

Eric Bodden
committed
Dec 12, 2012

292

293
		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);List<N>returnSiteNs=icfg.getReturnSitesOfCallAt(n);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

294
		



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

295
		//for each possible callee//for each possible callee



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

296

297
		Set<M> callees = icfg.getCalleesOfCallAt(n);Set<M>callees=icfg.getCalleesOfCallAt(n);		for(M sCalledProcN: callees) { //still line 14for(MsCalledProcN:callees){//still line 14



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

298

299
						//compute the call-flow function//compute the call-flow function



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

300

301

302
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);FlowFunction<D>function=flowFunctions.getCallFlowFunction(n,sCalledProcN);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;			Set<D> res = function.computeTargets(d2);Set<D>res=function.computeTargets(d2);



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

303

304
						//for each callee's start point(s)//for each callee's start point(s)



performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...


 

 

performance fix for return edges; if there were N start points (e.g. in a...

 

Eric Bodden
committed
Apr 25, 2013

305
			Set<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);Set<N>startPointsOf=icfg.getStartPointsOf(sCalledProcN);



memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values


 

 

memory improvement: do not store implicit TOP values

 

Steven Arzt
committed
Jul 04, 2013

306
			for(N sP: startPointsOf) {for(NsP:startPointsOf){



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

307
				//for each result node of the call-flow function//for each result node of the call-flow function



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

308
				for(D d3: res) {for(Dd3:res){



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

309
					//create initial self-loop//create initial self-loop



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

310
					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15propagate(d3,sP,d3,EdgeIdentity.<V>v(),n,false);//line 15



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

311
	



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

312
					//register the fact that <sp,d3> has an incoming edge from <n,d2>//register the fact that <sp,d3> has an incoming edge from <n,d2>



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

313

314

315

316

317
					Set<Cell<N, D, EdgeFunction<V>>> endSumm;Set<Cell<N,D,EdgeFunction<V>>>endSumm;					synchronized (incoming) {synchronized(incoming){						//line 15.1 of Naeem/Lhotak/Rodriguez//line 15.1 of Naeem/Lhotak/Rodriguez						addIncoming(sP,d3,n,d2);addIncoming(sP,d3,n,d2);						//line 15.2, copy to avoid concurrent modification exceptions by other threads//line 15.2, copy to avoid concurrent modification exceptions by other threads



memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values


 

 

memory improvement: do not store implicit TOP values

 

Steven Arzt
committed
Jul 04, 2013

318

319

320
						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));endSumm=newHashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP,d3));												assert !jumpFn.reverseLookup(n, d2).isEmpty();assert!jumpFn.reverseLookup(n,d2).isEmpty();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

321

322

323
					}}										//still line 15.2 of Naeem/Lhotak/Rodriguez//still line 15.2 of Naeem/Lhotak/Rodriguez



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

324
					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,//for each already-queried exit value <eP,d4> reachable from <sP,d3>,



removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012



removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 

removed caller-side summary functions; instead now just use callee-side "endSummaries"

 

Eric Bodden
committed
Dec 12, 2012

325
					//create new caller-side jump functions to the return sites//create new caller-side jump functions to the return sites



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

326
					//because we have observed a potentially new incoming edge into <sP,d3>//because we have observed a potentially new incoming edge into <sP,d3>



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

327

328

329

330
					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {for(Cell<N,D,EdgeFunction<V>>entry:endSumm){						N eP = entry.getRowKey();NeP=entry.getRowKey();						D d4 = entry.getColumnKey();Dd4=entry.getColumnKey();						EdgeFunction<V> fCalleeSummary = entry.getValue();EdgeFunction<V>fCalleeSummary=entry.getValue();



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

331
						//for each return site//for each return site



Replaced the duplicate call to the icfg by an access to cached structure we have anyway


 

 


Steven Arzt
committed
Mar 11, 2013



Replaced the duplicate call to the icfg by an access to cached structure we have anyway


 

 

Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 

Steven Arzt
committed
Mar 11, 2013

332
						for(N retSiteN: returnSiteNs) {for(NretSiteN:returnSiteNs){



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

333
							//compute return-flow function//compute return-flow function



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

334

335
							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(n,sCalledProcN,eP,retSiteN);							flowFunctionConstructionCount++;flowFunctionConstructionCount++;



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

336
							//for each target value of the function//for each target value of the function



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Steven Arzt
committed
Sep 18, 2013

337
							for(D d5: computeReturnFlowFunction(retFunction, d4, Collections.singleton(d1))) {for(Dd5:computeReturnFlowFunction(retFunction,d4,Collections.singleton(d1))){



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

338
								//update the caller-side summary function//update the caller-side summary function



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

339

340
								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);EdgeFunction<V>f4=edgeFunctions.getCallEdgeFunction(n,d2,sCalledProcN,d3);								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);EdgeFunction<V>f5=edgeFunctions.getReturnEdgeFunction(n,sCalledProcN,eP,d4,retSiteN,d5);



removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 


Eric Bodden
committed
Dec 12, 2012



removed caller-side summary functions; instead now just use callee-side "endSummaries"


 

 

removed caller-side summary functions; instead now just use callee-side "endSummaries"

 

Eric Bodden
committed
Dec 12, 2012

341
								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);							EdgeFunction<V>fPrime=f4.composeWith(fCalleeSummary).composeWith(f5);



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

342
								propagate(d1, retSiteN, d5, f.composeWith(fPrime), n, false);propagate(d1,retSiteN,d5,f.composeWith(fPrime),n,false);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

343

344

345

346

347

348
							}}						}}					}}				}		}			}}		}}



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

349

350
		//line 17-19 of Naeem/Lhotak/Rodriguez		//line 17-19 of Naeem/Lhotak/Rodriguez				//process intra-procedural flows along call-to-return flow functions//process intra-procedural flows along call-to-return flow functions



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

351

352

353
		for (N returnSiteN : returnSiteNs) {for(NreturnSiteN:returnSiteNs){			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);FlowFunction<D>callToReturnFlowFunction=flowFunctions.getCallToReturnFlowFunction(n,returnSiteN);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Steven Arzt
committed
Sep 18, 2013

354
			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {for(Dd3:computeCallToReturnFlowFunction(callToReturnFlowFunction,d1,d2)){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

355
				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);EdgeFunction<V>edgeFnE=edgeFunctions.getCallToReturnEdgeFunction(n,d2,returnSiteN,d3);



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

356
				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);propagate(d1,returnSiteN,d3,f.composeWith(edgeFnE),n,false);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

357

358

359

360

361
			}}		}}	}}	/**/**



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Steven Arzt
committed
Sep 18, 2013

362

363

364

365

366

367

368

369

370

371

372

373

374

375

376
	 * Computes the call-to-return flow function for the given call-site	 * Computes the call-to-return flow function for the given call-site	 * asbtraction	 * asbtraction	 * @param callToReturnFlowFunction The call-to-return flow function to	 * @param callToReturnFlowFunction The call-to-return flow function to	 * compute	 * compute	 * @param d1 The abstraction at the current method's start node.	 * @param d1 The abstraction at the current method's start node.	 * @param d2 The abstraction at the return size	 * @param d2 The abstraction at the return size	 * @return The set of caller-side abstractions at the return site	 * @return The set of caller-side abstractions at the return site	 */	 */	protected Set<D> computeCallToReturnFlowFunctionprotectedSet<D>computeCallToReturnFlowFunction			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {(FlowFunction<D>callToReturnFlowFunction,Dd1,Dd2){		return callToReturnFlowFunction.computeTargets(d2);returncallToReturnFlowFunction.computeTargets(d2);	}}	/**/**	 * Lines 21-32 of the algorithm.	 * Lines 21-32 of the algorithm.



comments and minor optimizations


 

 


Eric Bodden
committed
Dec 12, 2012



comments and minor optimizations


 

 

comments and minor optimizations

 

Eric Bodden
committed
Dec 12, 2012

377

378

379

380

381

382
	 * 	 * 	 * Stores callee-side summaries.	 * Stores callee-side summaries.	 * Also, at the side of the caller, propagates intra-procedural flows to return sites	 * Also, at the side of the caller, propagates intra-procedural flows to return sites	 * using those newly computed summaries.	 * using those newly computed summaries.	 * 	 * 	 * @param edge an edge whose target node resembles a method exits	 * @param edge an edge whose target node resembles a method exits



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

383
	 */	 */



Merge branch 'develop' into forks/java-fw-bw


 

 


Eric Bodden
committed
Jul 06, 2013



Merge branch 'develop' into forks/java-fw-bw


 

 

Merge branch 'develop' into forks/java-fw-bw

 

Eric Bodden
committed
Jul 06, 2013

384
	protected void processExit(PathEdge<N,D> edge) {protectedvoidprocessExit(PathEdge<N,D>edge){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

385

386

387

388

389

390

391
		final N n = edge.getTarget(); // an exit node; line 21...finalNn=edge.getTarget();// an exit node; line 21...		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);		M methodThatNeedsSummary = icfg.getMethodOf(n);MmethodThatNeedsSummary=icfg.getMethodOf(n);				final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();		



performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...


 

 

performance fix for return edges; if there were N start points (e.g. in a...

 

Eric Bodden
committed
Apr 25, 2013

392

393

394

395
		//for each of the method's start points, determine incoming calls//for each of the method's start points, determine incoming calls		Set<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);Set<N>startPointsOf=icfg.getStartPointsOf(methodThatNeedsSummary);		Set<Entry<N,Set<D>>> inc = new HashSet<Map.Entry<N,Set<D>>>();Set<Entry<N,Set<D>>>inc=newHashSet<Map.Entry<N,Set<D>>>();		for(N sP: startPointsOf) {for(NsP:startPointsOf){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

396

397
			//line 21.1 of Naeem/Lhotak/Rodriguez//line 21.1 of Naeem/Lhotak/Rodriguez			



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

398
			//register end-summary//register end-summary



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

399

400

401
			synchronized (incoming) {synchronized(incoming){				addEndSummary(sP, d1, n, d2, f);addEndSummary(sP,d1,n,d2,f);				//copy to avoid concurrent modification exceptions by other threads//copy to avoid concurrent modification exceptions by other threads



performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...


 

 

performance fix for return edges; if there were N start points (e.g. in a...

 

Eric Bodden
committed
Apr 25, 2013

402

403

404

405

406

407

408

409

410

411

412

413

414

415
				inc.addAll(incoming(d1, sP));inc.addAll(incoming(d1,sP));			}	}		}}				//for each incoming call edge already processed//for each incoming call edge already processed		//(see processCall(..))//(see processCall(..))		for (Entry<N,Set<D>> entry: inc) {for(Entry<N,Set<D>>entry:inc){			//line 22//line 22			N c = entry.getKey();Nc=entry.getKey();			//for each return site//for each return site			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(c)){				//compute return-flow function//compute return-flow function				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(c,methodThatNeedsSummary,n,retSiteC);				flowFunctionConstructionCount++;flowFunctionConstructionCount++;



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Steven Arzt
committed
Sep 18, 2013

416
				Set<D> targets = computeReturnFlowFunction(retFunction, d2, entry.getValue());Set<D>targets=computeReturnFlowFunction(retFunction,d2,entry.getValue());



performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...


 

 

performance fix for return edges; if there were N start points (e.g. in a...

 

Eric Bodden
committed
Apr 25, 2013

417

418

419

420

421

422

423

424

425

426

427

428

429

430
				//for each incoming-call value//for each incoming-call value				for(D d4: entry.getValue()) {for(Dd4:entry.getValue()){					//for each target value at the return site//for each target value at the return site					//line 23//line 23					for(D d5: targets) {for(Dd5:targets){						//compute composed function//compute composed function						EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);EdgeFunction<V>f4=edgeFunctions.getCallEdgeFunction(c,d4,icfg.getMethodOf(n),d1);						EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);EdgeFunction<V>f5=edgeFunctions.getReturnEdgeFunction(c,icfg.getMethodOf(n),n,d2,retSiteC,d5);						EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);EdgeFunction<V>fPrime=f4.composeWith(f).composeWith(f5);						//for each jump function coming into the call, propagate to return site using the composed function//for each jump function coming into the call, propagate to return site using the composed function						for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {for(Map.Entry<D,EdgeFunction<V>>valAndFunc:jumpFn.reverseLookup(c,d4).entrySet()){							EdgeFunction<V> f3 = valAndFunc.getValue();EdgeFunction<V>f3=valAndFunc.getValue();							if(!f3.equalTo(allTop)) {if(!f3.equalTo(allTop)){								D d3 = valAndFunc.getKey();Dd3=valAndFunc.getKey();



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

431
								propagate(d3, retSiteC, d5, f3.composeWith(fPrime), c, false);propagate(d3,retSiteC,d5,f3.composeWith(fPrime),c,false);



performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...


 

 

performance fix for return edges; if there were N start points (e.g. in a...

 

Eric Bodden
committed
Apr 25, 2013

432

433

434

435
							}}						}}					}}				}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

436
			}}



performance fix for return edges; if there were N start points (e.g. in a...


 

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...


 

 

performance fix for return edges; if there were N start points (e.g. in a...

 

Eric Bodden
committed
Apr 25, 2013

437

438
		}}		



improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013



improved and simplified handling of unbalanced problems:


 

 

improved and simplified handling of unbalanced problems:

 

Eric Bodden
committed
Jul 08, 2013

439
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow



further fix for followReturnPastSeeds:


 

 


Eric Bodden
committed
Jul 08, 2013



further fix for followReturnPastSeeds:


 

 

further fix for followReturnPastSeeds:

 

Eric Bodden
committed
Jul 08, 2013

440

441

442
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only		//be propagated into callers that have an incoming edge for this condition//be propagated into callers that have an incoming edge for this condition		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {if(followReturnsPastSeeds&&inc.isEmpty()&&d1.equals(zeroValue)){



improved and simplified handling of unbalanced problems:


 

 


Eric Bodden
committed
Jul 08, 2013



improved and simplified handling of unbalanced problems:


 

 

improved and simplified handling of unbalanced problems:

 

Eric Bodden
committed
Jul 08, 2013

443
			// only propagate up if we // only propagate up if we 



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 

Steven Arzt
committed
Jun 16, 2013

444

445

446

447

448

449

450

451
				Set<N> callers = icfg.getCallersOf(methodThatNeedsSummary);Set<N>callers=icfg.getCallersOf(methodThatNeedsSummary);				for(N c: callers) {for(Nc:callers){					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(c)){						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(c,methodThatNeedsSummary,n,retSiteC);						flowFunctionConstructionCount++;flowFunctionConstructionCount++;						Set<D> targets = retFunction.computeTargets(d2);Set<D>targets=retFunction.computeTargets(d2);						for(D d5: targets) {for(Dd5:targets){							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);EdgeFunction<V>f5=edgeFunctions.getReturnEdgeFunction(c,icfg.getMethodOf(n),n,d2,retSiteC,d5);



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

452
							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);propagate(zeroValue,retSiteC,d5,f.composeWith(f5),c,true);



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 

Steven Arzt
committed
Jun 16, 2013

453
						}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

454

455
					}}				}}



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 


Steven Arzt
committed
Jun 16, 2013



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...


 

 

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 

Steven Arzt
committed
Jun 16, 2013

456

457

458

459

460

461

462

463
				//in cases where there are no callers, the return statement would normally not be processed at all;//in cases where there are no callers, the return statement would normally not be processed at all;				//this might be undesirable if the flow function has a side effect such as registering a taint;//this might be undesirable if the flow function has a side effect such as registering a taint;				//instead we thus call the return flow function will a null caller//instead we thus call the return flow function will a null caller				if(callers.isEmpty()) {if(callers.isEmpty()){					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(null,methodThatNeedsSummary,n,null);					flowFunctionConstructionCount++;flowFunctionConstructionCount++;					retFunction.computeTargets(d2);retFunction.computeTargets(d2);				}}



improved handling of unbalanced problems


 

 


Eric Bodden
committed
Dec 17, 2012



improved handling of unbalanced problems


 

 

improved handling of unbalanced problems

 

Eric Bodden
committed
Dec 17, 2012

464
			}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

465

466
		}}	



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Steven Arzt
committed
Sep 18, 2013

467

468

469

470

471

472

473

474

475

476

477

478

479
	/**/**	 * Computes the return flow function for the given set of caller-side	 * Computes the return flow function for the given set of caller-side	 * abstractions.	 * abstractions.	 * @param retFunction The return flow function to compute	 * @param retFunction The return flow function to compute	 * @param d2 The abstraction at the exit node in the callee	 * @param d2 The abstraction at the exit node in the callee	 * @param callerSideD1s The abstractions at the callers' start nodes.	 * @param callerSideD1s The abstractions at the callers' start nodes.	 * @return The set of caller-side abstractions at the return site	 * @return The set of caller-side abstractions at the return site	 */	 */	protected Set<D> computeReturnFlowFunctionprotectedSet<D>computeReturnFlowFunction			(FlowFunction<D> retFunction, D d2, Set<D> callerSideD1s) {(FlowFunction<D>retFunction,Dd2,Set<D>callerSideD1s){		return retFunction.computeTargets(d2);returnretFunction.computeTargets(d2);	}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

480

481
	/**/**	 * Lines 33-37 of the algorithm.	 * Lines 33-37 of the algorithm.



implemented a small optimization in processExit: propagate intra-procedural...


 

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...


 

 

implemented a small optimization in processExit: propagate intra-procedural...

 

Eric Bodden
committed
Dec 12, 2012

482
	 * Simply propagate normal, intra-procedural flows.	 * Simply propagate normal, intra-procedural flows.



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

483

484
	 * @param edge	 * @param edge	 */	 */



removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge


 

 

removed superfluous type parameter from PathEdge

 

Eric Bodden
committed
Jul 06, 2013

485
	private void processNormalFlow(PathEdge<N,D> edge) {privatevoidprocessNormalFlow(PathEdge<N,D>edge){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

486

487

488

489

490

491

492
		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); finalNn=edge.getTarget();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);		for (N m : icfg.getSuccsOf(n)) {for(Nm:icfg.getSuccsOf(n)){			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);FlowFunction<D>flowFunction=flowFunctions.getNormalFlowFunction(n,m);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Steven Arzt
committed
Sep 18, 2013

493
			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);Set<D>res=computeNormalFlowFunction(flowFunction,d1,d2);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

494

495
			for (D d3 : res) {for(Dd3:res){				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));EdgeFunction<V>fprime=f.composeWith(edgeFunctions.getNormalEdgeFunction(n,d2,m,d3));



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

496
				propagate(d1, m, d3, fprime, null, false); propagate(d1,m,d3,fprime,null,false);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

497

498

499

500
			}}		}}	}}	



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...


 

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Steven Arzt
committed
Sep 18, 2013

501

502

503

504

505

506

507

508

509

510

511

512

513
	/**/**	 * Computes the normal flow function for the given set of start and end	 * Computes the normal flow function for the given set of start and end	 * abstractions-	 * abstractions-	 * @param flowFunction The normal flow function to compute	 * @param flowFunction The normal flow function to compute	 * @param d1 The abstraction at the method's start node	 * @param d1 The abstraction at the method's start node	 * @param d1 The abstraction at the current node	 * @param d1 The abstraction at the current node	 * @return The set of abstractions at the successor node	 * @return The set of abstractions at the successor node	 */	 */	protected Set<D> computeNormalFlowFunctionprotectedSet<D>computeNormalFlowFunction			(FlowFunction<D> flowFunction, D d1, D d2) {(FlowFunction<D>flowFunction,Dd1,Dd2){		return flowFunction.computeTargets(d2);returnflowFunction.computeTargets(d2);	}}



changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows


 

 

changed signature of "propagate" to include original call site for return and call flows

 

Eric Bodden
committed
Jul 06, 2013

514

515

516

517

518

519

520

521

522
	/**/**	 * Propagates the flow further down the exploded super graph, merging any edge function that might	 * Propagates the flow further down the exploded super graph, merging any edge function that might	 * already have been computed for targetVal at target. 	 * already have been computed for targetVal at target. 	 * @param sourceVal the source value of the propagated summary edge	 * @param sourceVal the source value of the propagated summary edge	 * @param target the target statement	 * @param target the target statement	 * @param targetVal the target value at the target statement	 * @param targetVal the target value at the target statement	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

523

524
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 



changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows


 

 

changed signature of "propagate" to include original call site for return and call flows

 

Eric Bodden
committed
Jul 06, 2013

525
	 */	 */



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...


 

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

 

Eric Bodden
committed
Jul 09, 2013

526

527

528
	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,protectedvoidpropagate(DsourceVal,Ntarget,DtargetVal,EdgeFunction<V>f,		/* deliberately exposed to clients */ N relatedCallSite,/* deliberately exposed to clients */NrelatedCallSite,		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {/* deliberately exposed to clients */booleanisUnbalancedReturn){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

529
		EdgeFunction<V> jumpFnE;EdgeFunction<V>jumpFnE;



fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013



fixing race condition found by Steven Arzt


 

 

fixing race condition found by Steven Arzt

 

Eric Bodden
committed
Jan 08, 2013

530

531
		EdgeFunction<V> fPrime;EdgeFunction<V>fPrime;		boolean newFunction;booleannewFunction;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

532

533
		synchronized (jumpFn) {synchronized(jumpFn){			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);jumpFnE=jumpFn.reverseLookup(target,targetVal).get(sourceVal);



fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013



fixing race condition found by Steven Arzt


 

 

fixing race condition found by Steven Arzt

 

Eric Bodden
committed
Jan 08, 2013

534

535

536

537
			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)if(jumpFnE==null)jumpFnE=allTop;//JumpFn is initialized to all-top (see line [2] in SRH96 paper)			fPrime = jumpFnE.joinWith(f);fPrime=jumpFnE.joinWith(f);			newFunction = !fPrime.equalTo(jumpFnE);newFunction=!fPrime.equalTo(jumpFnE);			if(newFunction) {if(newFunction){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

538

539
				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);jumpFn.addFunction(sourceVal,target,targetVal,fPrime);			}}



fixing race condition found by Steven Arzt


 

 


Eric Bodden
committed
Jan 08, 2013



fixing race condition found by Steven Arzt


 

 

fixing race condition found by Steven Arzt

 

Eric Bodden
committed
Jan 08, 2013

540

541

542
		}}		if(newFunction) {if(newFunction){



removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge


 

 

removed superfluous type parameter from PathEdge

 

Eric Bodden
committed
Jul 06, 2013

543
			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);PathEdge<N,D>edge=newPathEdge<N,D>(sourceVal,target,targetVal);



minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013



minor cleanups


 

 

minor cleanups

 

Eric Bodden
committed
Jan 26, 2013

544
			scheduleEdgeProcessing(edge);scheduleEdgeProcessing(edge);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

545

546

547

548
			if(DEBUG) {if(DEBUG){				if(targetVal!=zeroValue) {			if(targetVal!=zeroValue){					StringBuilder result = new StringBuilder();StringBuilderresult=newStringBuilder();



added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013



added support for debug name


 

 

added support for debug name

 

Eric Bodden
committed
Jul 06, 2013

549

550
					result.append(getDebugName());result.append(getDebugName());					result.append(": ");result.append(": ");



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

551

552

553

554

555

556

557

558

559

560

561

562

563

564

565

566
					result.append("EDGE:  <");result.append("EDGE:  <");					result.append(icfg.getMethodOf(target));result.append(icfg.getMethodOf(target));					result.append(",");result.append(",");					result.append(sourceVal);result.append(sourceVal);					result.append("> -> <");result.append("> -> <");					result.append(target);result.append(target);					result.append(",");result.append(",");					result.append(targetVal);result.append(targetVal);					result.append("> - ");result.append("> - ");					result.append(fPrime);result.append(fPrime);					System.err.println(result.toString());System.err.println(result.toString());				}}			}}		}}	}}	



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

567

568

569

570

571
	/**/**	 * Computes the final values for edge functions.	 * Computes the final values for edge functions.	 */	 */	private void computeValues() {	privatevoidcomputeValues(){		//Phase II(i)//Phase II(i)



changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...


 

 

changing initialization of analysis such that initialSeeds not is a mapping...

 

Eric Bodden
committed
Jul 05, 2013

572

573

574

575

576

577

578
		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {for(Entry<N,Set<D>>seed:initialSeeds.entrySet()){			N startPoint = seed.getKey();NstartPoint=seed.getKey();			for(D val: seed.getValue()) {for(Dval:seed.getValue()){				setVal(startPoint, val, valueLattice.bottomElement());setVal(startPoint,val,valueLattice.bottomElement());				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); Pair<N,D>superGraphNode=newPair<N,D>(startPoint,val);				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));scheduleValueProcessing(newValuePropagationTask(superGraphNode));			}}



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

579
		}}



Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013



Fixed race condition in IDESolver and simplified the code


 

 

Fixed race condition in IDESolver and simplified the code

 

Marc-André Laverdière
committed
Jan 25, 2013

580
		



Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013



Revert "adding CountLatch"


 

 

Revert "adding CountLatch"

 

Eric Bodden
committed
Jan 28, 2013

581

582

583

584

585

586
		//await termination of tasks//await termination of tasks		try {try{			executor.awaitCompletion();executor.awaitCompletion();		} catch (InterruptedException e) {}catch(InterruptedExceptione){			e.printStackTrace();e.printStackTrace();		}}



Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013



Fixed race condition in IDESolver and simplified the code


 

 

Fixed race condition in IDESolver and simplified the code

 

Marc-André Laverdière
committed
Jan 25, 2013

587
		



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

588

589

590

591

592

593

594

595

596
		//Phase II(ii)//Phase II(ii)		//we create an array of all nodes and then dispatch fractions of this array to multiple threads//we create an array of all nodes and then dispatch fractions of this array to multiple threads		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();Set<N>allNonCallStartNodes=icfg.allNonCallStartNodes();		@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];N[]nonCallStartNodesArray=(N[])newObject[allNonCallStartNodes.size()];		int i=0;inti=0;		for (N n : allNonCallStartNodes) {for(Nn:allNonCallStartNodes){			nonCallStartNodesArray[i] = n;nonCallStartNodesArray[i]=n;			i++;i++;



Fixed race condition in IDESolver and simplified the code


 

 


Marc-André Laverdière
committed
Jan 25, 2013



Fixed race condition in IDESolver and simplified the code


 

 

Fixed race condition in IDESolver and simplified the code

 

Marc-André Laverdière
committed
Jan 25, 2013

597

598
		}}		//No need to keep track of the number of tasks scheduled here, since we call shutdown//No need to keep track of the number of tasks scheduled here, since we call shutdown



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

599
		for(int t=0;t<numThreads; t++) {for(intt=0;t<numThreads;t++){



refactoring


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring


 

 

refactoring

 

Eric Bodden
committed
Jan 28, 2013

600

601
			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);ValueComputationTasktask=newValueComputationTask(nonCallStartNodesArray,t);			scheduleValueComputationTask(task);scheduleValueComputationTask(task);



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

602
		}}



Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013



Revert "adding CountLatch"


 

 

Revert "adding CountLatch"

 

Eric Bodden
committed
Jan 28, 2013

603

604

605

606

607

608
		//await termination of tasks//await termination of tasks		try {try{			executor.awaitCompletion();executor.awaitCompletion();		} catch (InterruptedException e) {}catch(InterruptedExceptione){			e.printStackTrace();e.printStackTrace();		}}



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

609

610

611

612

613

614

615

616

617

618

619

620

621

622

623

624

625

626

627

628

629

630

631

632

633

634

635

636

637

638

639

640

641

642

643

644

645

646

647

648

649
	}}	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {privatevoidpropagateValueAtStart(Pair<N,D>nAndD,Nn){		D d = nAndD.getO2();		Dd=nAndD.getO2();		M p = icfg.getMethodOf(n);Mp=icfg.getMethodOf(n);		for(N c: icfg.getCallsFromWithin(p)) {					for(Nc:icfg.getCallsFromWithin(p)){			Set<Entry<D, EdgeFunction<V>>> entries; Set<Entry<D,EdgeFunction<V>>>entries;			synchronized (jumpFn) {synchronized(jumpFn){				entries = jumpFn.forwardLookup(d,c).entrySet();entries=jumpFn.forwardLookup(d,c).entrySet();				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {for(Map.Entry<D,EdgeFunction<V>>dPAndFP:entries){					D dPrime = dPAndFP.getKey();DdPrime=dPAndFP.getKey();					EdgeFunction<V> fPrime = dPAndFP.getValue();EdgeFunction<V>fPrime=dPAndFP.getValue();					N sP = n;NsP=n;					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));					flowFunctionApplicationCount++;flowFunctionApplicationCount++;				}}			}}		}}	}}		private void propagateValueAtCall(Pair<N, D> nAndD, N n) {privatevoidpropagateValueAtCall(Pair<N,D>nAndD,Nn){		D d = nAndD.getO2();Dd=nAndD.getO2();		for(M q: icfg.getCalleesOfCallAt(n)) {for(Mq:icfg.getCalleesOfCallAt(n)){			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);FlowFunction<D>callFlowFunction=flowFunctions.getCallFlowFunction(n,q);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;			for(D dPrime: callFlowFunction.computeTargets(d)) {for(DdPrime:callFlowFunction.computeTargets(d)){				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);EdgeFunction<V>edgeFn=edgeFunctions.getCallEdgeFunction(n,d,q,dPrime);				for(N startPoint: icfg.getStartPointsOf(q)) {for(NstartPoint:icfg.getStartPointsOf(q)){					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));propagateValue(startPoint,dPrime,edgeFn.computeTarget(val(n,d)));					flowFunctionApplicationCount++;flowFunctionApplicationCount++;				}}			}}		}}	}}		private void propagateValue(N nHashN, D nHashD, V v) {privatevoidpropagateValue(NnHashN,DnHashD,Vv){		synchronized (val) {synchronized(val){			V valNHash = val(nHashN, nHashD);VvalNHash=val(nHashN,nHashD);			V vPrime = valueLattice.join(valNHash,v);VvPrime=valueLattice.join(valNHash,v);			if(!vPrime.equals(valNHash)) {if(!vPrime.equals(valNHash)){				setVal(nHashN, nHashD, vPrime);setVal(nHashN,nHashD,vPrime);



minor cleanups


 

 


Eric Bodden
committed
Jan 26, 2013



minor cleanups


 

 

minor cleanups

 

Eric Bodden
committed
Jan 26, 2013

650
				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));scheduleValueProcessing(newValuePropagationTask(newPair<N,D>(nHashN,nHashD)));



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

651

652

653

654

655
			}}		}}	}}	private V val(N nHashN, D nHashD){ privateVval(NnHashN,DnHashD){



adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...


 

 

adding synchronization on "val" due to possible race conditions (thanks to...

 

Eric Bodden
committed
May 29, 2013

656

657

658

659
		V l;Vl;		synchronized (val) {synchronized(val){			l = val.get(nHashN, nHashD);l=val.get(nHashN,nHashD);		}}



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

660

661

662

663
		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paperif(l==null)returnvalueLattice.topElement();//implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper		else return l;elsereturnl;	}}	



memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values


 

 

memory improvement: do not store implicit TOP values

 

Steven Arzt
committed
Jul 04, 2013

664

665
	private void setVal(N nHashN, D nHashD,V l){privatevoidsetVal(NnHashN,DnHashD,Vl){		// TOP is the implicit default value which we do not need to store.// TOP is the implicit default value which we do not need to store.



adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...


 

 

adding synchronization on "val" due to possible race conditions (thanks to...

 

Eric Bodden
committed
May 29, 2013

666
		synchronized (val) {synchronized(val){



memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values


 

 

memory improvement: do not store implicit TOP values

 

Steven Arzt
committed
Jul 04, 2013

667

668

669

670
			if (l == valueLattice.topElement())     // do not store top valuesif(l==valueLattice.topElement())// do not store top values				val.remove(nHashN, nHashD);val.remove(nHashN,nHashD);			elseelse				val.put(nHashN, nHashD,l);val.put(nHashN,nHashD,l);



adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...


 

 

adding synchronization on "val" due to possible race conditions (thanks to...

 

Eric Bodden
committed
May 29, 2013

671
		}}



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

672

673

674

675
		if(DEBUG)if(DEBUG)			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+" "+l);	}}



removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge


 

 

removed superfluous type parameter from PathEdge

 

Eric Bodden
committed
Jul 06, 2013

676
	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {privateEdgeFunction<V>jumpFunction(PathEdge<N,D>edge){



reordered some methods


 

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods


 

 

reordered some methods

 

Eric Bodden
committed
Dec 12, 2012

677

678

679

680

681

682

683
		synchronized (jumpFn) {synchronized(jumpFn){			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());EdgeFunction<V>function=jumpFn.forwardLookup(edge.factAtSource(),edge.getTarget()).get(edge.factAtTarget());			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paperif(function==null)returnallTop;//JumpFn initialized to all-top, see line [2] in SRH96 paper			return function;returnfunction;		}}	}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

684

685

686

687

688

689

690

691

692

693

694

695
	private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {privateSet<Cell<N,D,EdgeFunction<V>>>endSummary(NsP,Dd3){		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);Table<N,D,EdgeFunction<V>>map=endSummary.get(sP,d3);		if(map==null) return Collections.emptySet();if(map==null)returnCollections.emptySet();		return map.cellSet();returnmap.cellSet();	}}	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {privatevoidaddEndSummary(NsP,Dd1,NeP,Dd2,EdgeFunction<V>f){		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);Table<N,D,EdgeFunction<V>>summaries=endSummary.get(sP,d1);		if(summaries==null) {if(summaries==null){			summaries = HashBasedTable.create();summaries=HashBasedTable.create();			endSummary.put(sP, d1, summaries);endSummary.put(sP,d1,summaries);		}}



undoing previous "fix"; as discussed with Steven, it is not required (see comment)


 

 


Eric Bodden
committed
Dec 12, 2012



undoing previous "fix"; as discussed with Steven, it is not required (see comment)


 

 

undoing previous "fix"; as discussed with Steven, it is not required (see comment)

 

Eric Bodden
committed
Dec 12, 2012

696

697

698

699
		//note: at this point we don't need to join with a potential previous f//note: at this point we don't need to join with a potential previous f		//because f is a jump function, which is already properly joined//because f is a jump function, which is already properly joined		//within propagate(..)//within propagate(..)		summaries.put(eP,d2,f);summaries.put(eP,d2,f);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

700

701

702

703

704

705

706

707

708

709

710

711

712

713

714

715

716

717

718

719

720

721

722
	}	}		private Set<Entry<N, Set<D>>> incoming(D d1, N sP) {privateSet<Entry<N,Set<D>>>incoming(Dd1,NsP){		Map<N, Set<D>> map = incoming.get(sP, d1);Map<N,Set<D>>map=incoming.get(sP,d1);		if(map==null) return Collections.emptySet();if(map==null)returnCollections.emptySet();		return map.entrySet();		returnmap.entrySet();	}}		private void addIncoming(N sP, D d3, N n, D d2) {privatevoidaddIncoming(NsP,Dd3,Nn,Dd2){		Map<N, Set<D>> summaries = incoming.get(sP, d3);Map<N,Set<D>>summaries=incoming.get(sP,d3);		if(summaries==null) {if(summaries==null){			summaries = new HashMap<N, Set<D>>();summaries=newHashMap<N,Set<D>>();			incoming.put(sP, d3, summaries);incoming.put(sP,d3,summaries);		}}		Set<D> set = summaries.get(n);Set<D>set=summaries.get(n);		if(set==null) {if(set==null){			set = new HashSet<D>();set=newHashSet<D>();			summaries.put(n,set);summaries.put(n,set);		}}		set.add(d2);set.add(d2);	}	}		/**/**



memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values


 

 

memory improvement: do not store implicit TOP values

 

Steven Arzt
committed
Jul 04, 2013

723

724
	 * Returns the V-type result for the given value at the given statement.	 * Returns the V-type result for the given value at the given statement.	 * TOP values are never returned.	 * TOP values are never returned.



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

725

726
	 */	 */	public V resultAt(N stmt, D value) {publicVresultAt(Nstmt,Dvalue){



adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...


 

 

adding synchronization on "val" due to possible race conditions (thanks to...

 

Eric Bodden
committed
May 29, 2013

727
		//no need to synchronize here as all threads are known to have terminated//no need to synchronize here as all threads are known to have terminated



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

728

729

730

731

732
		return val.get(stmt, value);returnval.get(stmt,value);	}}		/**/**	 * Returns the resulting environment for the given statement.	 * Returns the resulting environment for the given statement.



memory improvement: do not store implicit TOP values


 

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values


 

 

memory improvement: do not store implicit TOP values

 

Steven Arzt
committed
Jul 04, 2013

733

734
	 * The artificial zero value is automatically stripped. TOP values are	 * The artificial zero value is automatically stripped. TOP values are	 * never returned.	 * never returned.



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

735

736

737
	 */	 */	public Map<D,V> resultsAt(N stmt) {publicMap<D,V>resultsAt(Nstmt){		//filter out the artificial zero-value//filter out the artificial zero-value



adding synchronization on "val" due to possible race conditions (thanks to...


 

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...


 

 

adding synchronization on "val" due to possible race conditions (thanks to...

 

Eric Bodden
committed
May 29, 2013

738
		//no need to synchronize here as all threads are known to have terminated//no need to synchronize here as all threads are known to have terminated



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

739

740

741

742

743

744

745
		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {returnMaps.filterKeys(val.row(stmt),newPredicate<D>(){			public boolean apply(D val) {publicbooleanapply(Dval){				return val!=zeroValue;returnval!=zeroValue;			}}		});});	}}



making executor exchangeable


 

 


Eric Bodden
committed
Jan 29, 2013



making executor exchangeable


 

 

making executor exchangeable

 

Eric Bodden
committed
Jan 29, 2013

746

747

748

749

750

751

752
		/**/**	 * Factory method for this solver's thread-pool executor.	 * Factory method for this solver's thread-pool executor.	 */	 */	protected CountingThreadPoolExecutor getExecutor() {protectedCountingThreadPoolExecutorgetExecutor(){		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());returnnewCountingThreadPoolExecutor(1,this.numThreads,30,TimeUnit.SECONDS,newLinkedBlockingQueue<Runnable>());	}}



changed signature of "propagate" to include original call site for return and call flows


 

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows


 

 

changed signature of "propagate" to include original call site for return and call flows

 

Eric Bodden
committed
Jul 06, 2013

753
	



added support for debug name


 

 


Eric Bodden
committed
Jul 06, 2013



added support for debug name


 

 

added support for debug name

 

Eric Bodden
committed
Jul 06, 2013

754

755

756

757

758

759

760

761
	/**/**	 * Returns a String used to identify the output of this solver in debug mode.	 * Returns a String used to identify the output of this solver in debug mode.	 * Subclasses can overwrite this string to distinguish the output from different solvers.	 * Subclasses can overwrite this string to distinguish the output from different solvers.	 */	 */	protected String getDebugName() {protectedStringgetDebugName(){		return "";return"";	}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

762

763

764

765

766

767

768

769

770

771

772

773
	public void printStats() {publicvoidprintStats(){		if(DEBUG) {if(DEBUG){			if(ffCache!=null)if(ffCache!=null)				ffCache.printStats();ffCache.printStats();			if(efCache!=null)if(efCache!=null)				efCache.printStats();efCache.printStats();		} else {}else{			System.err.println("No statistics were collected, as DEBUG is disabled.");System.err.println("No statistics were collected, as DEBUG is disabled.");		}}	}}		private class PathEdgeProcessingTask implements Runnable {privateclassPathEdgeProcessingTaskimplementsRunnable{



removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge


 

 

removed superfluous type parameter from PathEdge

 

Eric Bodden
committed
Jul 06, 2013

774
		private final PathEdge<N,D> edge;privatefinalPathEdge<N,D>edge;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

775




removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge


 

 

removed superfluous type parameter from PathEdge

 

Eric Bodden
committed
Jul 06, 2013

776
		public PathEdgeProcessingTask(PathEdge<N,D> edge) {publicPathEdgeProcessingTask(PathEdge<N,D>edge){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

777

778

779

780

781

782

783

784

785

786

787

788

789

790

791

792

793

794

795

796

797

798

799

800

801

802

803

804
			this.edge = edge;this.edge=edge;		}}		public void run() {publicvoidrun(){			if(icfg.isCallStmt(edge.getTarget())) {if(icfg.isCallStmt(edge.getTarget())){				processCall(edge);processCall(edge);			} else {}else{				//note that some statements, such as "throw" may be//note that some statements, such as "throw" may be				//both an exit statement and a "normal" statement//both an exit statement and a "normal" statement				if(icfg.isExitStmt(edge.getTarget())) {if(icfg.isExitStmt(edge.getTarget())){					processExit(edge);processExit(edge);				}}				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()){					processNormalFlow(edge);processNormalFlow(edge);				}}			}}		}}	}}		private class ValuePropagationTask implements Runnable {privateclassValuePropagationTaskimplementsRunnable{		private final Pair<N, D> nAndD;privatefinalPair<N,D>nAndD;		public ValuePropagationTask(Pair<N,D> nAndD) {publicValuePropagationTask(Pair<N,D>nAndD){			this.nAndD = nAndD;this.nAndD=nAndD;		}}		public void run() {publicvoidrun(){			N n = nAndD.getO1();Nn=nAndD.getO1();



bug fix for value computation (need to treat initialSeeds just as method start nodes)


 

 


Eric Bodden
committed
Feb 14, 2013



bug fix for value computation (need to treat initialSeeds just as method start nodes)


 

 

bug fix for value computation (need to treat initialSeeds just as method start nodes)

 

Eric Bodden
committed
Feb 14, 2013

805
			if(icfg.isStartPoint(n) ||if(icfg.isStartPoint(n)||



changing initialization of analysis such that initialSeeds not is a mapping...


 

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...


 

 

changing initialization of analysis such that initialSeeds not is a mapping...

 

Eric Bodden
committed
Jul 05, 2013

806
				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as suchinitialSeeds.containsKey(n)){//our initial seeds are not necessarily method-start points but here they should be treated as such



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

807

808

809

810

811

812

813

814

815

816

817

818

819

820

821

822

823

824

825

826

827

828

829

830

831

832

833

834

835

836

837

838

839

840

841

842

843

844

845
				propagateValueAtStart(nAndD, n);propagateValueAtStart(nAndD,n);			}}			if(icfg.isCallStmt(n)) {if(icfg.isCallStmt(n)){				propagateValueAtCall(nAndD, n);propagateValueAtCall(nAndD,n);			}}		}}	}}		private class ValueComputationTask implements Runnable {privateclassValueComputationTaskimplementsRunnable{		private final N[] values;privatefinalN[]values;		final int num;finalintnum;		public ValueComputationTask(N[] values, int num) {publicValueComputationTask(N[]values,intnum){			this.values = values;this.values=values;			this.num = num;this.num=num;		}}		public void run() {publicvoidrun(){			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;intsectionSize=(int)Math.floor(values.length/numThreads)+numThreads;			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {for(inti=sectionSize*num;i<Math.min(sectionSize*(num+1),values.length);i++){				N n = values[i];Nn=values[i];				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					for(NsP:icfg.getStartPointsOf(icfg.getMethodOf(n))){					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;Set<Cell<D,D,EdgeFunction<V>>>lookupByTarget;					lookupByTarget = jumpFn.lookupByTarget(n);lookupByTarget=jumpFn.lookupByTarget(n);					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {for(Cell<D,D,EdgeFunction<V>>sourceValTargetValAndFunction:lookupByTarget){						D dPrime = sourceValTargetValAndFunction.getRowKey();DdPrime=sourceValTargetValAndFunction.getRowKey();						D d = sourceValTargetValAndFunction.getColumnKey();Dd=sourceValTargetValAndFunction.getColumnKey();						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();EdgeFunction<V>fPrime=sourceValTargetValAndFunction.getValue();						synchronized (val) {synchronized(val){							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));						}}						flowFunctionApplicationCount++;flowFunctionApplicationCount++;					}}				}}			}}		}}	}}}}





