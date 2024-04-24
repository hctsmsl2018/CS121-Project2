



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

3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c

















3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



32 KB









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









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;








initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



import java.util.concurrent.TimeUnit;









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



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




50












initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s








initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 








initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.








initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);









comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();









Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;








initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;








initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;








initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;








initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;








initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;








initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;








initial checkin



Eric Bodden
committed
Nov 14, 2012




111












adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




131



	protected final D zeroValue;








initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 








initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;








initial checkin



Eric Bodden
committed
Nov 14, 2012




138












making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141












make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;









added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



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




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();








making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();








initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



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




205



	 */








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}








extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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




222



			final long before = System.currentTimeMillis();








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();








initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())








initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway








initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



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




248



		try {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



	}









refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }








Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	








refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);








refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  








comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */








refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}








initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 








initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);









initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15








initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>








initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));








initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,








removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>








initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site








Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function








initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



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




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions








initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
		}
	}









Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



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




413



	 */








Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary








initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}








initial checkin



Eric Bodden
committed
Nov 14, 2012




470



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow








further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}








initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



					}
				}








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



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




498



			}








initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



		}
	








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.








initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		








initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);








initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
		}
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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




566


567


568


569


570


571


572


573


574



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




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);








minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);








initial checkin



Eric Bodden
committed
Nov 14, 2012




597












Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }








initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



		}
	}
	








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)








Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



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




617



		}








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



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




625



		








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



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




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {








refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



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




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



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




721


722


723


724


725


726


727


728


729


730


731


732



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




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);








initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



	}	
	








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



	}
	








made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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








initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



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




787


788


789


790


791


792


793



	
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




794



	








added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



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




803



	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");








initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;








initial checkin



Eric Bodden
committed
Nov 14, 2012




816












removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




846



			if(icfg.isStartPoint(n) ||








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such








initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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

3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c

















3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



32 KB









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









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;








initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



import java.util.concurrent.TimeUnit;









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



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




50












initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s








initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 








initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.








initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);









comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();









Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;








initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;








initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;








initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;








initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;








initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;








initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;








initial checkin



Eric Bodden
committed
Nov 14, 2012




111












adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




131



	protected final D zeroValue;








initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 








initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;








initial checkin



Eric Bodden
committed
Nov 14, 2012




138












making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141












make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;









added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



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




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();








making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();








initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



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




205



	 */








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}








extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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




222



			final long before = System.currentTimeMillis();








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();








initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())








initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway








initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



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




248



		try {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



	}









refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }








Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	








refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);








refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  








comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */








refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}








initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 








initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);









initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15








initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>








initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));








initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,








removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>








initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site








Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function








initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



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




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions








initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
		}
	}









Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



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




413



	 */








Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary








initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}








initial checkin



Eric Bodden
committed
Nov 14, 2012




470



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow








further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}








initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



					}
				}








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



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




498



			}








initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



		}
	








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.








initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		








initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);








initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
		}
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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




566


567


568


569


570


571


572


573


574



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




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);








minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);








initial checkin



Eric Bodden
committed
Nov 14, 2012




597












Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }








initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



		}
	}
	








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)








Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



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




617



		}








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



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




625



		








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



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




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {








refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



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




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



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




721


722


723


724


725


726


727


728


729


730


731


732



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




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);








initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



	}	
	








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



	}
	








made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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








initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



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




787


788


789


790


791


792


793



	
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




794



	








added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



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




803



	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");








initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;








initial checkin



Eric Bodden
committed
Nov 14, 2012




816












removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




846



			if(icfg.isStartPoint(n) ||








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such








initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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

3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c







Open sidebar



Joshua Garcia heros

3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c




Open sidebar

Joshua Garcia heros

3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Joshua Garciaherosheros
3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c










3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



32 KB









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









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;








initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



import java.util.concurrent.TimeUnit;









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



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




50












initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s








initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 








initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.








initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);









comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();









Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;








initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;








initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;








initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;








initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;








initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;








initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;








initial checkin



Eric Bodden
committed
Nov 14, 2012




111












adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




131



	protected final D zeroValue;








initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 








initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;








initial checkin



Eric Bodden
committed
Nov 14, 2012




138












making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141












make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;









added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



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




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();








making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();








initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



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




205



	 */








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}








extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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




222



			final long before = System.currentTimeMillis();








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();








initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())








initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway








initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



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




248



		try {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



	}









refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }








Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	








refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);








refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  








comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */








refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}








initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 








initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);









initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15








initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>








initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));








initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,








removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>








initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site








Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function








initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



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




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions








initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
		}
	}









Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



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




413



	 */








Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary








initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}








initial checkin



Eric Bodden
committed
Nov 14, 2012




470



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow








further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}








initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



					}
				}








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



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




498



			}








initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



		}
	








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.








initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		








initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);








initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
		}
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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




566


567


568


569


570


571


572


573


574



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




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);








minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);








initial checkin



Eric Bodden
committed
Nov 14, 2012




597












Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }








initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



		}
	}
	








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)








Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



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




617



		}








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



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




625



		








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



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




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {








refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



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




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



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




721


722


723


724


725


726


727


728


729


730


731


732



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




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);








initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



	}	
	








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



	}
	








made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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








initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



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




787


788


789


790


791


792


793



	
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




794



	








added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



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




803



	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");








initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;








initial checkin



Eric Bodden
committed
Nov 14, 2012




816












removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




846



			if(icfg.isStartPoint(n) ||








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such








initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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















3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



32 KB









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









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;








initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



import java.util.concurrent.TimeUnit;









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



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




50












initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s








initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 








initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.








initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);









comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();









Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;








initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;








initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;








initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;








initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;








initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;








initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;








initial checkin



Eric Bodden
committed
Nov 14, 2012




111












adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




131



	protected final D zeroValue;








initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 








initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;








initial checkin



Eric Bodden
committed
Nov 14, 2012




138












making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141












make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;









added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



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




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();








making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();








initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



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




205



	 */








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}








extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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




222



			final long before = System.currentTimeMillis();








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();








initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())








initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway








initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



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




248



		try {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



	}









refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }








Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	








refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);








refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  








comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */








refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}








initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 








initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);









initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15








initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>








initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));








initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,








removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>








initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site








Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function








initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



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




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions








initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
		}
	}









Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



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




413



	 */








Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary








initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}








initial checkin



Eric Bodden
committed
Nov 14, 2012




470



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow








further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}








initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



					}
				}








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



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




498



			}








initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



		}
	








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.








initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		








initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);








initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
		}
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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




566


567


568


569


570


571


572


573


574



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




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);








minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);








initial checkin



Eric Bodden
committed
Nov 14, 2012




597












Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }








initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



		}
	}
	








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)








Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



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




617



		}








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



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




625



		








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



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




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {








refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



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




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



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




721


722


723


724


725


726


727


728


729


730


731


732



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




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);








initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



	}	
	








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



	}
	








made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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








initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



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




787


788


789


790


791


792


793



	
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




794



	








added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



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




803



	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");








initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;








initial checkin



Eric Bodden
committed
Nov 14, 2012




816












removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




846



			if(icfg.isStartPoint(n) ||








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such








initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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











3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
Normal viewHistoryPermalink




3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Switch branch/tag










heros


src


heros


solver


IDESolver.java





3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Switch branch/tag








3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c


Switch branch/tag





3d7cf97783ed91e3da7130642c6a3b0bc64f9b0c

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



32 KB









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









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;








initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



import java.util.concurrent.TimeUnit;









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



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




50












initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s








initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 








initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.








initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);









comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();









Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;








initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;








initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;








initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;








initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;








initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;








initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;








initial checkin



Eric Bodden
committed
Nov 14, 2012




111












adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




131



	protected final D zeroValue;








initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 








initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;








initial checkin



Eric Bodden
committed
Nov 14, 2012




138












making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141












make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;









added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



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




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();








making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();








initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



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




205



	 */








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}








extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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




222



			final long before = System.currentTimeMillis();








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();








initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())








initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway








initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



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




248



		try {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



	}









refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }








Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	








refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);








refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  








comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */








refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}








initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 








initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);









initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15








initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>








initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));








initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,








removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>








initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site








Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function








initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



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




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions








initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
		}
	}









Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



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




413



	 */








Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary








initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}








initial checkin



Eric Bodden
committed
Nov 14, 2012




470



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow








further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}








initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



					}
				}








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



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




498



			}








initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



		}
	








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.








initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		








initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);








initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
		}
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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




566


567


568


569


570


571


572


573


574



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




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);








minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);








initial checkin



Eric Bodden
committed
Nov 14, 2012




597












Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }








initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



		}
	}
	








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)








Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



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




617



		}








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



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




625



		








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



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




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {








refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



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




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



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




721


722


723


724


725


726


727


728


729


730


731


732



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




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);








initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



	}	
	








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



	}
	








made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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








initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



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




787


788


789


790


791


792


793



	
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




794



	








added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



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




803



	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");








initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;








initial checkin



Eric Bodden
committed
Nov 14, 2012




816












removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




846



			if(icfg.isStartPoint(n) ||








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such








initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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



32 KB










IDESolver.java



32 KB









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









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;








initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



import java.util.concurrent.TimeUnit;









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



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




50












initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s








initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 








initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.








initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);









comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();









Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;








initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;








initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;








initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;








initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;








initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;








initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;








initial checkin



Eric Bodden
committed
Nov 14, 2012




111












adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




131



	protected final D zeroValue;








initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 








initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;








initial checkin



Eric Bodden
committed
Nov 14, 2012




138












making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141












make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;









added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



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




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();








making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();








initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



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




205



	 */








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}








extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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




222



			final long before = System.currentTimeMillis();








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();








initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())








initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway








initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



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




248



		try {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



	}









refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }








Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	








refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);








refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  








comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */








refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}








initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 








initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);









initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15








initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>








initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));








initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,








removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>








initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site








Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function








initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



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




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions








initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
		}
	}









Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



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




413



	 */








Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary








initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}








initial checkin



Eric Bodden
committed
Nov 14, 2012




470



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow








further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}








initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



					}
				}








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



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




498



			}








initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



		}
	








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.








initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		








initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);








initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
		}
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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




566


567


568


569


570


571


572


573


574



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




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);








minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);








initial checkin



Eric Bodden
committed
Nov 14, 2012




597












Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }








initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



		}
	}
	








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)








Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



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




617



		}








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



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




625



		








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



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




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {








refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



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




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



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




721


722


723


724


725


726


727


728


729


730


731


732



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




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);








initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



	}	
	








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



	}
	








made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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








initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



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




787


788


789


790


791


792


793



	
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




794



	








added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



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




803



	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");








initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;








initial checkin



Eric Bodden
committed
Nov 14, 2012




816












removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




846



			if(icfg.isStartPoint(n) ||








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such








initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;








initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



import java.util.concurrent.TimeUnit;









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



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




50












initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s








initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 








initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.








initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);









comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();









Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;








initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;








initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;








initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;








initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;








initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;








initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;








initial checkin



Eric Bodden
committed
Nov 14, 2012




111












adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




131



	protected final D zeroValue;








initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 








initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;








initial checkin



Eric Bodden
committed
Nov 14, 2012




138












making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141












make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;









added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



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




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();








making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();








initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



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




205



	 */








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}








extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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




222



			final long before = System.currentTimeMillis();








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();








initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())








initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway








initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



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




248



		try {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



	}









refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }








Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	








refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);








refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  








comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */








refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}








initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 








initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);









initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15








initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>








initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));








initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,








removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>








initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site








Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function








initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



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




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions








initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
		}
	}









Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



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




413



	 */








Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary








initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}








initial checkin



Eric Bodden
committed
Nov 14, 2012




470



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow








further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}








initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



					}
				}








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



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




498



			}








initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



		}
	








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.








initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		








initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);








initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
		}
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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




566


567


568


569


570


571


572


573


574



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




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);








minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);








initial checkin



Eric Bodden
committed
Nov 14, 2012




597












Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }








initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



		}
	}
	








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)








Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



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




617



		}








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



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




625



		








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



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




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {








refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



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




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



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




721


722


723


724


725


726


727


728


729


730


731


732



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




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);








initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



	}	
	








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



	}
	








made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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








initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



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




787


788


789


790


791


792


793



	
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




794



	








added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



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




803



	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");








initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;








initial checkin



Eric Bodden
committed
Nov 14, 2012




816












removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




846



			if(icfg.isStartPoint(n) ||








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such








initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;








initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



import java.util.concurrent.TimeUnit;









generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



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




50












initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s








initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 








initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.








moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.








initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);









comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();









Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;








initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;








initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;








initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;








initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();








initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;








initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;








initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;








initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;








initial checkin



Eric Bodden
committed
Nov 14, 2012




111












adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




131



	protected final D zeroValue;








initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 








initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")








added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;








initial checkin



Eric Bodden
committed
Nov 14, 2012




138












making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141












make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;









added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



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




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();








added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		








refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();








making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();








initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */








number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



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




205



	 */








extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}








extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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




222



			final long before = System.currentTimeMillis();








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();








initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}








make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())








initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway








initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



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




248



		try {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);








fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



	}









refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }








Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	








refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);








refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  








comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */








refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;








refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}








initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 








initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);









initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);








initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15








initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>








initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));








initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,








removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>








initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site








Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function








initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function








initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



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




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions








initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);








initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
		}
	}









Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site








Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.








comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



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




413



	 */








Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls








generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary








initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}








initial checkin



Eric Bodden
committed
Nov 14, 2012




470



			}








performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow








further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {








improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 








generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}








initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



					}
				}








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



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




498



			}








initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



		}
	








Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction








added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}









initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.








implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.








initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();








1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		








initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);








initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 








initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
		}
	}
	








Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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




566


567


568


569


570


571


572


573


574



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




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 








changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */








first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}








fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);








minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);








initial checkin



Eric Bodden
committed
Nov 14, 2012




597












Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {








Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }








initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



		}
	}
	








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)








Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



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




617



		}








Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



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




625



		








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



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




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {








refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}








Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



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




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {








reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



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




721


722


723


724


725


726


727


728


729


730


731


732



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




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);








initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



	}	
	








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);








1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}








initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



	}
	








made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {








added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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








initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.








memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.








initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value








adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated








initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



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




787


788


789


790


791


792


793



	
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




794



	








added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



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




803



	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");








initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {








removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;








initial checkin



Eric Bodden
committed
Nov 14, 2012




816












removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




846



			if(icfg.isStartPoint(n) ||








changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such








initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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




1


2



/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.






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


/*******************************************************************************
 * Copyright (c) 2012 Eric Bodden.

/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden.




minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




3



 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal






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


 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal

 * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal * Copyright (c) 2013 Tata Consultancy Services & Ecole Polytechnique de Montreal




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


 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation

 * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation




minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




11



 *     Marc-Andre Laverdiere-Papineau - Fixed race condition






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


 *     Marc-Andre Laverdiere-Papineau - Fixed race condition

 *     Marc-Andre Laverdiere-Papineau - Fixed race condition *     Marc-Andre Laverdiere-Papineau - Fixed race condition




license headers

 


Eric Bodden
committed
Nov 29, 2012




12



 ******************************************************************************/






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


 ******************************************************************************/

 ******************************************************************************/ ******************************************************************************/




renamed package

 


Eric Bodden
committed
Nov 29, 2012




13



package heros.solver;






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


package heros.solver;

package heros.solver;packageheros.solver;




initial checkin



Eric Bodden
committed
Nov 14, 2012




14


15











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


import heros.DontSynchronize;importheros.DontSynchronize;import heros.EdgeFunction;importheros.EdgeFunction;import heros.EdgeFunctionCache;importheros.EdgeFunctionCache;import heros.EdgeFunctions;importheros.EdgeFunctions;import heros.FlowFunction;importheros.FlowFunction;import heros.FlowFunctionCache;importheros.FlowFunctionCache;import heros.FlowFunctions;importheros.FlowFunctions;import heros.IDETabulationProblem;importheros.IDETabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;import heros.JoinLattice;importheros.JoinLattice;import heros.SynchronizedBy;importheros.SynchronizedBy;import heros.ZeroedFlowFunctions;importheros.ZeroedFlowFunctions;import heros.edgefunc.EdgeIdentity;importheros.edgefunc.EdgeIdentity;




generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




30



import java.util.Collection;






generalized some return types

 


Steven Arzt
committed
Jan 17, 2014



generalized some return types

 

generalized some return types

Steven Arzt
committed
Jan 17, 2014


30


import java.util.Collection;

import java.util.Collection;importjava.util.Collection;




initial checkin



Eric Bodden
committed
Nov 14, 2012




31


32


33


34


35


36



import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


31


32


33


34


35


36


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import java.util.Collections;importjava.util.Collections;import java.util.HashMap;importjava.util.HashMap;import java.util.HashSet;importjava.util.HashSet;import java.util.Map;importjava.util.Map;import java.util.Map.Entry;importjava.util.Map.Entry;import java.util.Set;importjava.util.Set;




Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




37



import java.util.concurrent.LinkedBlockingQueue;






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


import java.util.concurrent.LinkedBlockingQueue;

import java.util.concurrent.LinkedBlockingQueue;importjava.util.concurrent.LinkedBlockingQueue;




initial checkin



Eric Bodden
committed
Nov 14, 2012




38


39



import java.util.concurrent.TimeUnit;







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


import java.util.concurrent.TimeUnit;


import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;




generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




40


41


42



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;







generalized some return types

 


Steven Arzt
committed
Jan 17, 2014



generalized some return types

 

generalized some return types

Steven Arzt
committed
Jan 17, 2014


40


41


42


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.slf4j.Logger;importorg.slf4j.Logger;import org.slf4j.LoggerFactory;importorg.slf4j.LoggerFactory;




initial checkin



Eric Bodden
committed
Nov 14, 2012




43


44


45


46


47


48


49



import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;







initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


43


44


45


46


47


48


49


import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;


import com.google.common.base.Predicate;importcom.google.common.base.Predicate;import com.google.common.cache.CacheBuilder;importcom.google.common.cache.CacheBuilder;import com.google.common.collect.HashBasedTable;importcom.google.common.collect.HashBasedTable;import com.google.common.collect.Maps;importcom.google.common.collect.Maps;import com.google.common.collect.Table;importcom.google.common.collect.Table;import com.google.common.collect.Table.Cell;importcom.google.common.collect.Table.Cell;




renamed package

 


Eric Bodden
committed
Nov 28, 2012




50










renamed package

 


Eric Bodden
committed
Nov 28, 2012



renamed package

 

renamed package

Eric Bodden
committed
Nov 28, 2012


50









initial checkin



Eric Bodden
committed
Nov 14, 2012




51


52


53


54


55



/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


51


52


53


54


55


/**
 * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv,
 * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be
 * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}.
 * 

/**/** * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv, * Solves the given {@link IDETabulationProblem} as described in the 1996 paper by Sagiv, * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be * Horwitz and Reps. To solve the problem, call {@link #solve()}. Results can then be * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}. * queried by using {@link #resultAt(Object, Object)} and {@link #resultsAt(Object)}. *  * 




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




56



 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


56


 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s

 * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s * Note that this solver and its data structures internally use mostly {@link java.util.LinkedHashSet}s




initial checkin



Eric Bodden
committed
Nov 14, 2012




57


58


59


60



 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


57


58


59


60


 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This
 * is to produce, as much as possible, reproducible benchmarking results. We have found
 * that the iteration order can matter a lot in terms of speed.
 *

 * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This * instead of normal {@link HashSet}s to fix the iteration order as much as possible. This * is to produce, as much as possible, reproducible benchmarking results. We have found * is to produce, as much as possible, reproducible benchmarking results. We have found * that the iteration order can matter a lot in terms of speed. * that the iteration order can matter a lot in terms of speed. * *




moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




61



 * @param <N> The type of nodes in the interprocedural control-flow graph. 






moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012



moved dependencies on soot into separate package

 

moved dependencies on soot into separate package

Eric Bodden
committed
Nov 28, 2012


61


 * @param <N> The type of nodes in the interprocedural control-flow graph. 

 * @param <N> The type of nodes in the interprocedural control-flow graph.  * @param <N> The type of nodes in the interprocedural control-flow graph. 




initial checkin



Eric Bodden
committed
Nov 14, 2012




62



 * @param <D> The type of data-flow facts to be computed by the tabulation problem.






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


62


 * @param <D> The type of data-flow facts to be computed by the tabulation problem.

 * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem.




moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012




63



 * @param <M> The type of objects used to represent methods.






moved dependencies on soot into separate package

 


Eric Bodden
committed
Nov 28, 2012



moved dependencies on soot into separate package

 

moved dependencies on soot into separate package

Eric Bodden
committed
Nov 28, 2012


63


 * @param <M> The type of objects used to represent methods.

 * @param <M> The type of objects used to represent methods. * @param <M> The type of objects used to represent methods.




initial checkin



Eric Bodden
committed
Nov 14, 2012




64


65


66


67


68


69


70



 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


64


65


66


67


68


69


70


 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	

 * @param <V> The type of values to be computed along flow edges. * @param <V> The type of values to be computed along flow edges. * @param <I> The type of inter-procedural control-flow graph being used. * @param <I> The type of inter-procedural control-flow graph being used. */ */public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {publicclassIDESolver<N,D,M,V,IextendsInterproceduralCFG<N,M>>{		public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();publicstaticCacheBuilder<Object,Object>DEFAULT_CACHE_BUILDER=CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();	




Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




71


72



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);







Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013



Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 

Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

Marc-André Laverdière
committed
Oct 10, 2013


71


72


    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);


    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);protectedstaticfinalLoggerlogger=LoggerFactory.getLogger(IDESolver.class);




comments

 


Eric Bodden
committed
Oct 28, 2013




73



    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace






comments

 


Eric Bodden
committed
Oct 28, 2013



comments

 

comments

Eric Bodden
committed
Oct 28, 2013


73


    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace

    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace//enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace




Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




74


75



    public static final boolean DEBUG = logger.isDebugEnabled();







Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013



Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 

Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

Marc-André Laverdière
committed
Oct 10, 2013


74


75


    public static final boolean DEBUG = logger.isDebugEnabled();


    public static final boolean DEBUG = logger.isDebugEnabled();publicstaticfinalbooleanDEBUG=logger.isDebugEnabled();




Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




76



	protected CountingThreadPoolExecutor executor;






Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013



Revert "adding CountLatch"

 

Revert "adding CountLatch"

Eric Bodden
committed
Jan 28, 2013


76


	protected CountingThreadPoolExecutor executor;

	protected CountingThreadPoolExecutor executor;protectedCountingThreadPoolExecutorexecutor;




initial checkin



Eric Bodden
committed
Nov 14, 2012




77


78



	
	@DontSynchronize("only used by single thread")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


77


78


	
	@DontSynchronize("only used by single thread")

		@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




79



	protected int numThreads;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


79


	protected int numThreads;

	protected int numThreads;protectedintnumThreads;




initial checkin



Eric Bodden
committed
Nov 14, 2012




80


81



	
	@SynchronizedBy("thread safe data structure, consistent locking when used")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


80


81


	
	@SynchronizedBy("thread safe data structure, consistent locking when used")

		@SynchronizedBy("thread safe data structure, consistent locking when used")@SynchronizedBy("thread safe data structure, consistent locking when used")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




82



	protected final JumpFunctions<N,D,V> jumpFn;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


82


	protected final JumpFunctions<N,D,V> jumpFn;

	protected final JumpFunctions<N,D,V> jumpFn;protectedfinalJumpFunctions<N,D,V>jumpFn;




initial checkin



Eric Bodden
committed
Nov 14, 2012




83


84



	
	@SynchronizedBy("thread safe data structure, only modified internally")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


83


84


	
	@SynchronizedBy("thread safe data structure, only modified internally")

		@SynchronizedBy("thread safe data structure, only modified internally")@SynchronizedBy("thread safe data structure, only modified internally")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




85



	protected final I icfg;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


85


	protected final I icfg;

	protected final I icfg;protectedfinalIicfg;




initial checkin



Eric Bodden
committed
Nov 14, 2012




86


87


88


89



	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


86


87


88


89


	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")

		//stores summaries that were queried before they were computed//stores summaries that were queried before they were computed	//see CC 2010 paper by Naeem, Lhotak and Rodriguez//see CC 2010 paper by Naeem, Lhotak and Rodriguez	@SynchronizedBy("consistent lock on 'incoming'")@SynchronizedBy("consistent lock on 'incoming'")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




90



	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


90


	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();

	protected final Table<N,D,Table<N,D,EdgeFunction<V>>> endSummary = HashBasedTable.create();protectedfinalTable<N,D,Table<N,D,EdgeFunction<V>>>endSummary=HashBasedTable.create();




initial checkin



Eric Bodden
committed
Nov 14, 2012




91


92


93


94




	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


91


92


93


94



	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")

	//edges going along calls//edges going along calls	//see CC 2010 paper by Naeem, Lhotak and Rodriguez//see CC 2010 paper by Naeem, Lhotak and Rodriguez	@SynchronizedBy("consistent lock on field")@SynchronizedBy("consistent lock on field")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




95



	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


95


	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();

	protected final Table<N,D,Map<N,Set<D>>> incoming = HashBasedTable.create();protectedfinalTable<N,D,Map<N,Set<D>>>incoming=HashBasedTable.create();




initial checkin



Eric Bodden
committed
Nov 14, 2012




96


97



	
	@DontSynchronize("stateless")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


96


97


	
	@DontSynchronize("stateless")

		@DontSynchronize("stateless")@DontSynchronize("stateless")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




98



	protected final FlowFunctions<N, D, M> flowFunctions;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


98


	protected final FlowFunctions<N, D, M> flowFunctions;

	protected final FlowFunctions<N, D, M> flowFunctions;protectedfinalFlowFunctions<N,D,M>flowFunctions;




initial checkin



Eric Bodden
committed
Nov 14, 2012




99


100




	@DontSynchronize("stateless")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


99


100



	@DontSynchronize("stateless")

	@DontSynchronize("stateless")@DontSynchronize("stateless")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




101



	protected final EdgeFunctions<N,D,M,V> edgeFunctions;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


101


	protected final EdgeFunctions<N,D,M,V> edgeFunctions;

	protected final EdgeFunctions<N,D,M,V> edgeFunctions;protectedfinalEdgeFunctions<N,D,M,V>edgeFunctions;




initial checkin



Eric Bodden
committed
Nov 14, 2012




102


103




	@DontSynchronize("only used by single thread")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


102


103



	@DontSynchronize("only used by single thread")

	@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")




changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




104



	protected final Map<N,Set<D>> initialSeeds;






changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...

 

changing initialization of analysis such that initialSeeds not is a mapping...

Eric Bodden
committed
Jul 05, 2013


104


	protected final Map<N,Set<D>> initialSeeds;

	protected final Map<N,Set<D>> initialSeeds;protectedfinalMap<N,Set<D>>initialSeeds;




initial checkin



Eric Bodden
committed
Nov 14, 2012




105


106




	@DontSynchronize("stateless")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


105


106



	@DontSynchronize("stateless")

	@DontSynchronize("stateless")@DontSynchronize("stateless")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




107



	protected final JoinLattice<V> valueLattice;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


107


	protected final JoinLattice<V> valueLattice;

	protected final JoinLattice<V> valueLattice;protectedfinalJoinLattice<V>valueLattice;




initial checkin



Eric Bodden
committed
Nov 14, 2012




108


109



	
	@DontSynchronize("stateless")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


108


109


	
	@DontSynchronize("stateless")

		@DontSynchronize("stateless")@DontSynchronize("stateless")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




110



	protected final EdgeFunction<V> allTop;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


110


	protected final EdgeFunction<V> allTop;

	protected final EdgeFunction<V> allTop;protectedfinalEdgeFunction<V>allTop;




initial checkin



Eric Bodden
committed
Nov 14, 2012




111










initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


111









adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




112



	@SynchronizedBy("consistent lock on field")






adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...

 

adding synchronization on "val" due to possible race conditions (thanks to...

Eric Bodden
committed
May 29, 2013


112


	@SynchronizedBy("consistent lock on field")

	@SynchronizedBy("consistent lock on field")@SynchronizedBy("consistent lock on field")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




113



	protected final Table<N,D,V> val = HashBasedTable.create();	






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


113


	protected final Table<N,D,V> val = HashBasedTable.create();	

	protected final Table<N,D,V> val = HashBasedTable.create();	protectedfinalTable<N,D,V>val=HashBasedTable.create();




initial checkin



Eric Bodden
committed
Nov 14, 2012




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






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


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

		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long flowFunctionApplicationCount;publiclongflowFunctionApplicationCount;	@DontSynchronize("benign races")@DontSynchronize("benign races")	public long flowFunctionConstructionCount;publiclongflowFunctionConstructionCount;		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long propagationCount;publiclongpropagationCount;		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long durationFlowFunctionConstruction;publiclongdurationFlowFunctionConstruction;		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long durationFlowFunctionApplication;publiclongdurationFlowFunctionApplication;	@DontSynchronize("stateless")@DontSynchronize("stateless")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




131



	protected final D zeroValue;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


131


	protected final D zeroValue;

	protected final D zeroValue;protectedfinalDzeroValue;




initial checkin



Eric Bodden
committed
Nov 14, 2012




132


133



	
	@DontSynchronize("readOnly")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


132


133


	
	@DontSynchronize("readOnly")

		@DontSynchronize("readOnly")@DontSynchronize("readOnly")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




134



	protected final FlowFunctionCache<N,D,M> ffCache; 






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


134


	protected final FlowFunctionCache<N,D,M> ffCache; 

	protected final FlowFunctionCache<N,D,M> ffCache; protectedfinalFlowFunctionCache<N,D,M>ffCache;




initial checkin



Eric Bodden
committed
Nov 14, 2012




135


136




	@DontSynchronize("readOnly")






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


135


136



	@DontSynchronize("readOnly")

	@DontSynchronize("readOnly")@DontSynchronize("readOnly")




added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012




137



	protected final EdgeFunctionCache<N,D,M,V> efCache;






added dumping code again for Soot/Jimple versions

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions

 

added dumping code again for Soot/Jimple versions

Eric Bodden
committed
Nov 29, 2012


137


	protected final EdgeFunctionCache<N,D,M,V> efCache;

	protected final EdgeFunctionCache<N,D,M,V> efCache;protectedfinalEdgeFunctionCache<N,D,M,V>efCache;




initial checkin



Eric Bodden
committed
Nov 14, 2012




138










initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


138









making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




139


140



	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;






making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012



making computation of unbalanced edges optional

 

making computation of unbalanced edges optional

Eric Bodden
committed
Dec 12, 2012


139


140


	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;

	@DontSynchronize("readOnly")@DontSynchronize("readOnly")	protected final boolean followReturnsPastSeeds;protectedfinalbooleanfollowReturnsPastSeeds;




added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




141










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









make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




142


143


144



	@DontSynchronize("readOnly")
	protected final boolean computeValues;







make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013



make computation of values optional

 

make computation of values optional

Eric Bodden
committed
Jan 29, 2013


142


143


144


	@DontSynchronize("readOnly")
	protected final boolean computeValues;


	@DontSynchronize("readOnly")@DontSynchronize("readOnly")	protected final boolean computeValues;protectedfinalbooleancomputeValues;




added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




145


146


147


148



	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */






added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013



added possibility to switch off auto-zeroing of flow functions

 

added possibility to switch off auto-zeroing of flow functions

Eric Bodden
committed
Jan 22, 2013


145


146


147


148


	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */

	/**/**	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * The solver must then be started by calling {@link #solve()}.	 * The solver must then be started by calling {@link #solve()}.	 */	 */




refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




149


150



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);






refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013



refactoring: autoAddZero is now set in IFDSTabulationProblem

 

refactoring: autoAddZero is now set in IFDSTabulationProblem

Eric Bodden
committed
Jan 28, 2013


149


150


	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);

	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {publicIDESolver(IDETabulationProblem<N,D,M,V,I>tabulationProblem){		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);this(tabulationProblem,DEFAULT_CACHE_BUILDER,DEFAULT_CACHE_BUILDER);




added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




151


152



	}







added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013



added possibility to switch off auto-zeroing of flow functions

 

added possibility to switch off auto-zeroing of flow functions

Eric Bodden
committed
Jan 22, 2013


151


152


	}


	}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




153


154


155


156


157


158



	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


153


154


155


156


157


158


	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */

	/**/**	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * {@link #solve()}.	 * {@link #solve()}.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 */	 */




refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




159



	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {






refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013



refactoring: autoAddZero is now set in IFDSTabulationProblem

 

refactoring: autoAddZero is now set in IFDSTabulationProblem

Eric Bodden
committed
Jan 28, 2013


159


	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {

	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {publicIDESolver(IDETabulationProblem<N,D,M,V,I>tabulationProblem,@SuppressWarnings("rawtypes")CacheBuilderflowFunctionCacheBuilder,@SuppressWarnings("rawtypes")CacheBuilderedgeFunctionCacheBuilder){




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




160



		if(logger.isDebugEnabled()) {






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


160


		if(logger.isDebugEnabled()) {

		if(logger.isDebugEnabled()) {if(logger.isDebugEnabled()){




initial checkin



Eric Bodden
committed
Nov 14, 2012




161


162


163


164



			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


161


162


163


164


			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();

			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();flowFunctionCacheBuilder=flowFunctionCacheBuilder.recordStats();			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();edgeFunctionCacheBuilder=edgeFunctionCacheBuilder.recordStats();		}}		this.zeroValue = tabulationProblem.zeroValue();this.zeroValue=tabulationProblem.zeroValue();




added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013




165



		this.icfg = tabulationProblem.interproceduralCFG();		






added possibility to switch off auto-zeroing of flow functions

 


Eric Bodden
committed
Jan 22, 2013



added possibility to switch off auto-zeroing of flow functions

 

added possibility to switch off auto-zeroing of flow functions

Eric Bodden
committed
Jan 22, 2013


165


		this.icfg = tabulationProblem.interproceduralCFG();		

		this.icfg = tabulationProblem.interproceduralCFG();		this.icfg=tabulationProblem.interproceduralCFG();




refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013




166


167



		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 






refactoring: autoAddZero is now set in IFDSTabulationProblem

 


Eric Bodden
committed
Jan 28, 2013



refactoring: autoAddZero is now set in IFDSTabulationProblem

 

refactoring: autoAddZero is now set in IFDSTabulationProblem

Eric Bodden
committed
Jan 28, 2013


166


167


		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); 

		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?FlowFunctions<N,D,M>flowFunctions=tabulationProblem.autoAddZero()?				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions(); newZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(),tabulationProblem.zeroValue()):tabulationProblem.flowFunctions();




initial checkin



Eric Bodden
committed
Nov 14, 2012




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


183


184


185


186



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






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


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


183


184


185


186


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

		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();EdgeFunctions<N,D,M,V>edgeFunctions=tabulationProblem.edgeFunctions();		if(flowFunctionCacheBuilder!=null) {if(flowFunctionCacheBuilder!=null){			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);ffCache=newFlowFunctionCache<N,D,M>(flowFunctions,flowFunctionCacheBuilder);			flowFunctions = ffCache;flowFunctions=ffCache;		} else {}else{			ffCache = null;ffCache=null;		}}		if(edgeFunctionCacheBuilder!=null) {if(edgeFunctionCacheBuilder!=null){			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);efCache=newEdgeFunctionCache<N,D,M,V>(edgeFunctions,edgeFunctionCacheBuilder);			edgeFunctions = efCache;edgeFunctions=efCache;		} else {}else{			efCache = null;efCache=null;		}}		this.flowFunctions = flowFunctions;this.flowFunctions=flowFunctions;		this.edgeFunctions = edgeFunctions;this.edgeFunctions=edgeFunctions;		this.initialSeeds = tabulationProblem.initialSeeds();this.initialSeeds=tabulationProblem.initialSeeds();		this.valueLattice = tabulationProblem.joinLattice();this.valueLattice=tabulationProblem.joinLattice();		this.allTop = tabulationProblem.allTopFunction();this.allTop=tabulationProblem.allTopFunction();		this.jumpFn = new JumpFunctions<N,D,V>(allTop);this.jumpFn=newJumpFunctions<N,D,V>(allTop);




making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012




187



		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();






making computation of unbalanced edges optional

 


Eric Bodden
committed
Dec 12, 2012



making computation of unbalanced edges optional

 

making computation of unbalanced edges optional

Eric Bodden
committed
Dec 12, 2012


187


		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();

		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();this.followReturnsPastSeeds=tabulationProblem.followReturnsPastSeeds();




number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




188



		this.numThreads = Math.max(1,tabulationProblem.numThreads());






number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013



number of threads is now configured through SolverConfiguration, a new super...

 

number of threads is now configured through SolverConfiguration, a new super...

Eric Bodden
committed
Jan 29, 2013


188


		this.numThreads = Math.max(1,tabulationProblem.numThreads());

		this.numThreads = Math.max(1,tabulationProblem.numThreads());this.numThreads=Math.max(1,tabulationProblem.numThreads());




make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




189



		this.computeValues = tabulationProblem.computeValues();






make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013



make computation of values optional

 

make computation of values optional

Eric Bodden
committed
Jan 29, 2013


189


		this.computeValues = tabulationProblem.computeValues();

		this.computeValues = tabulationProblem.computeValues();this.computeValues=tabulationProblem.computeValues();




making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




190



		this.executor = getExecutor();






making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013



making executor exchangeable

 

making executor exchangeable

Eric Bodden
committed
Jan 29, 2013


190


		this.executor = getExecutor();

		this.executor = getExecutor();this.executor=getExecutor();




initial checkin



Eric Bodden
committed
Nov 14, 2012




191


192


193


194


195



	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


191


192


193


194


195


	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */

	}}	/**/**	 * Runs the solver on the configured problem. This can take some time.	 * Runs the solver on the configured problem. This can take some time.	 */	 */




number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013




196



	public void solve() {		






number of threads is now configured through SolverConfiguration, a new super...

 


Eric Bodden
committed
Jan 29, 2013



number of threads is now configured through SolverConfiguration, a new super...

 

number of threads is now configured through SolverConfiguration, a new super...

Eric Bodden
committed
Jan 29, 2013


196


	public void solve() {		

	public void solve() {		publicvoidsolve(){




extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




197


198


199


200


201


202


203


204



		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.






extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013



extracting method submitInitialSeeds to allow submission without having to wait

 

extracting method submitInitialSeeds to allow submission without having to wait

Eric Bodden
committed
Jul 06, 2013


197


198


199


200


201


202


203


204


		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.

		submitInitialSeeds();submitInitialSeeds();		awaitCompletionComputeValuesAndShutdown();awaitCompletionComputeValuesAndShutdown();	}}	/**/**	 * Schedules the processing of initial seeds, initiating the analysis.	 * Schedules the processing of initial seeds, initiating the analysis.	 * Clients should only call this methods if performing synchronization on	 * Clients should only call this methods if performing synchronization on	 * their own. Normally, {@link #solve()} should be called instead.	 * their own. Normally, {@link #solve()} should be called instead.




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




205



	 */






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


205


	 */

	 */	 */




extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013




206



	protected void submitInitialSeeds() {






extracting method submitInitialSeeds to allow submission without having to wait

 


Eric Bodden
committed
Jul 06, 2013



extracting method submitInitialSeeds to allow submission without having to wait

 

extracting method submitInitialSeeds to allow submission without having to wait

Eric Bodden
committed
Jul 06, 2013


206


	protected void submitInitialSeeds() {

	protected void submitInitialSeeds() {protectedvoidsubmitInitialSeeds(){




changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




207


208


209



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {






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


208


209


		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {

		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {for(Entry<N,Set<D>>seed:initialSeeds.entrySet()){			N startPoint = seed.getKey();NstartPoint=seed.getKey();			for(D val: seed.getValue()) {for(Dval:seed.getValue()){




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




210



				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


210


				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);

				propagate(zeroValue, startPoint, val, EdgeIdentity.<V>v(), null, false);propagate(zeroValue,startPoint,val,EdgeIdentity.<V>v(),null,false);




changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




211



			}






changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...

 

changing initialization of analysis such that initialSeeds not is a mapping...

Eric Bodden
committed
Jul 05, 2013


211


			}

			}}




extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013




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



			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{






extracted method awaitCompletionComputeValuesAndShutdown()

 


Eric Bodden
committed
Jan 30, 2013



extracted method awaitCompletionComputeValuesAndShutdown()

 

extracted method awaitCompletionComputeValuesAndShutdown()

Eric Bodden
committed
Jan 30, 2013


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


			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{

			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());jumpFn.addFunction(zeroValue,startPoint,zeroValue,EdgeIdentity.<V>v());		}}	}}	/**/**	 * Awaits the completion of the exploded super graph. When complete, computes result values,	 * Awaits the completion of the exploded super graph. When complete, computes result values,	 * shuts down the executor and returns.	 * shuts down the executor and returns.	 */	 */	protected void awaitCompletionComputeValuesAndShutdown() {protectedvoidawaitCompletionComputeValuesAndShutdown(){		{{




initial checkin



Eric Bodden
committed
Nov 14, 2012




222



			final long before = System.currentTimeMillis();






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


222


			final long before = System.currentTimeMillis();

			final long before = System.currentTimeMillis();finallongbefore=System.currentTimeMillis();




simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




223


224



			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();






simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling

 

simplified exception handling

Eric Bodden
committed
Jul 11, 2013


223


224


			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();

			//run executor and await termination of tasks//run executor and await termination of tasks			runExecutorAndAwaitCompletion();runExecutorAndAwaitCompletion();




initial checkin



Eric Bodden
committed
Nov 14, 2012




225


226



			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
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


225


226


			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}

			durationFlowFunctionConstruction = System.currentTimeMillis() - before;durationFlowFunctionConstruction=System.currentTimeMillis()-before;		}}




make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013




227



		if(computeValues) {






make computation of values optional

 


Eric Bodden
committed
Jan 29, 2013



make computation of values optional

 

make computation of values optional

Eric Bodden
committed
Jan 29, 2013


227


		if(computeValues) {

		if(computeValues) {if(computeValues){




initial checkin



Eric Bodden
committed
Nov 14, 2012




228


229


230


231



			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
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


228


229


230


231


			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}

			final long before = System.currentTimeMillis();finallongbefore=System.currentTimeMillis();			computeValues();computeValues();			durationFlowFunctionApplication = System.currentTimeMillis() - before;durationFlowFunctionApplication=System.currentTimeMillis()-before;		}}




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




232



		if(logger.isDebugEnabled())






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


232


		if(logger.isDebugEnabled())

		if(logger.isDebugEnabled())if(logger.isDebugEnabled())




initial checkin



Eric Bodden
committed
Nov 14, 2012




233


234



			printStats();
		






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


233


234


			printStats();
		

			printStats();printStats();		




fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




235


236


237



		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway






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


237


		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway

		//ask executor to shut down;//ask executor to shut down;		//this will cause new submissions to the executor to be rejected,//this will cause new submissions to the executor to be rejected,		//but at this point all tasks should have completed anyway//but at this point all tasks should have completed anyway




initial checkin



Eric Bodden
committed
Nov 14, 2012




238



		executor.shutdown();






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


238


		executor.shutdown();

		executor.shutdown();executor.shutdown();




fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




239


240



		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed






fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence

 

fixing shutdown sequence

Eric Bodden
committed
Jan 29, 2013


239


240


		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed

		//similarly here: we await termination, but this should happen instantaneously,//similarly here: we await termination, but this should happen instantaneously,		//as all tasks should have completed//as all tasks should have completed




simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




241


242


243


244


245


246


247



		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {






simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling

 

simplified exception handling

Eric Bodden
committed
Jul 11, 2013


241


242


243


244


245


246


247


		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {

		runExecutorAndAwaitCompletion();runExecutorAndAwaitCompletion();	}}	/**/**	 * Runs execution, re-throwing exceptions that might be thrown during its execution.	 * Runs execution, re-throwing exceptions that might be thrown during its execution.	 */	 */	private void runExecutorAndAwaitCompletion() {privatevoidrunExecutorAndAwaitCompletion(){




fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




248



		try {






fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence

 

fixing shutdown sequence

Eric Bodden
committed
Jan 29, 2013


248


		try {

		try {try{




simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




249



			executor.awaitCompletion();






simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling

 

simplified exception handling

Eric Bodden
committed
Jul 11, 2013


249


			executor.awaitCompletion();

			executor.awaitCompletion();executor.awaitCompletion();




fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




250



		} catch (InterruptedException e) {






fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence

 

fixing shutdown sequence

Eric Bodden
committed
Jan 29, 2013


250


		} catch (InterruptedException e) {

		} catch (InterruptedException e) {}catch(InterruptedExceptione){




simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




251


252


253


254


255



			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);






simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling

 

simplified exception handling

Eric Bodden
committed
Jul 11, 2013


251


252


253


254


255


			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);

			e.printStackTrace();e.printStackTrace();		}}		Throwable exception = executor.getException();Throwableexception=executor.getException();		if(exception!=null) {if(exception!=null){			throw new RuntimeException("There were exceptions during IDE analysis. Exiting.",exception);thrownewRuntimeException("There were exceptions during IDE analysis. Exiting.",exception);




fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013




256



		}






fixing shutdown sequence

 


Eric Bodden
committed
Jan 29, 2013



fixing shutdown sequence

 

fixing shutdown sequence

Eric Bodden
committed
Jan 29, 2013


256


		}

		}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




257


258



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


257


258


	}


	}}




refactoring

 


Eric Bodden
committed
Jan 28, 2013




259


260


261


262



    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */






refactoring

 


Eric Bodden
committed
Jan 28, 2013



refactoring

 

refactoring

Eric Bodden
committed
Jan 28, 2013


259


260


261


262


    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */

    /**/**     * Dispatch the processing of a given edge. It may be executed in a different thread.     * Dispatch the processing of a given edge. It may be executed in a different thread.     * @param edge the edge to process     * @param edge the edge to process     */     */




removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




263



    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){






removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge

 

removed superfluous type parameter from PathEdge

Eric Bodden
committed
Jul 06, 2013


263


    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){

    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){protectedvoidscheduleEdgeProcessing(PathEdge<N,D>edge){




Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




264


265


266


267



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;






Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013



Better be careful with executors: If they are shutting down, no new tasks may...

 

Better be careful with executors: If they are shutting down, no new tasks may...

Steven Arzt
committed
Oct 26, 2013


264


265


266


267


    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;

    	// If the executor has been killed, there is little point// If the executor has been killed, there is little point    	// in submitting new tasks// in submitting new tasks    	if (executor.isTerminating())if(executor.isTerminating())    		return;return;




indentation

 


Eric Bodden
committed
Jan 29, 2013




268


269



    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;






indentation

 


Eric Bodden
committed
Jan 29, 2013



indentation

 

indentation

Eric Bodden
committed
Jan 29, 2013


268


269


    	executor.execute(new PathEdgeProcessingTask(edge));
    	propagationCount++;

    	executor.execute(new PathEdgeProcessingTask(edge));executor.execute(newPathEdgeProcessingTask(edge));    	propagationCount++;propagationCount++;




refactoring

 


Eric Bodden
committed
Jan 28, 2013




270



    }






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


    }

    }}




Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




271



	






Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013



Fixed race condition in IDESolver and simplified the code

 

Fixed race condition in IDESolver and simplified the code

Marc-André Laverdière
committed
Jan 25, 2013


271


	

	




refactoring

 


Eric Bodden
committed
Jan 28, 2013




272


273


274


275


276



    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){






refactoring

 


Eric Bodden
committed
Jan 28, 2013



refactoring

 

refactoring

Eric Bodden
committed
Jan 28, 2013


272


273


274


275


276


    /**
     * Dispatch the processing of a given value. It may be executed in a different thread.
     * @param vpt
     */
    private void scheduleValueProcessing(ValuePropagationTask vpt){

    /**/**     * Dispatch the processing of a given value. It may be executed in a different thread.     * Dispatch the processing of a given value. It may be executed in a different thread.     * @param vpt     * @param vpt     */     */    private void scheduleValueProcessing(ValuePropagationTask vpt){privatevoidscheduleValueProcessing(ValuePropagationTaskvpt){




Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




277


278


279


280



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;






Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013



Better be careful with executors: If they are shutting down, no new tasks may...

 

Better be careful with executors: If they are shutting down, no new tasks may...

Steven Arzt
committed
Oct 26, 2013


277


278


279


280


    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;

    	// If the executor has been killed, there is little point// If the executor has been killed, there is little point    	// in submitting new tasks// in submitting new tasks    	if (executor.isTerminating())if(executor.isTerminating())    		return;return;




indentation

 


Eric Bodden
committed
Jan 29, 2013




281



    	executor.execute(vpt);






indentation

 


Eric Bodden
committed
Jan 29, 2013



indentation

 

indentation

Eric Bodden
committed
Jan 29, 2013


281


    	executor.execute(vpt);

    	executor.execute(vpt);executor.execute(vpt);




refactoring

 


Eric Bodden
committed
Jan 28, 2013




282


283



    }
  






refactoring

 


Eric Bodden
committed
Jan 28, 2013



refactoring

 

refactoring

Eric Bodden
committed
Jan 28, 2013


282


283


    }
  

    }}  




comments

 


Eric Bodden
committed
Jan 28, 2013




284


285


286


287



    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */






comments

 


Eric Bodden
committed
Jan 28, 2013



comments

 

comments

Eric Bodden
committed
Jan 28, 2013


284


285


286


287


    /**
     * Dispatch the computation of a given value. It may be executed in a different thread.
     * @param task
     */

    /**/**     * Dispatch the computation of a given value. It may be executed in a different thread.     * Dispatch the computation of a given value. It may be executed in a different thread.     * @param task     * @param task     */     */




refactoring

 


Eric Bodden
committed
Jan 28, 2013




288



	private void scheduleValueComputationTask(ValueComputationTask task) {






refactoring

 


Eric Bodden
committed
Jan 28, 2013



refactoring

 

refactoring

Eric Bodden
committed
Jan 28, 2013


288


	private void scheduleValueComputationTask(ValueComputationTask task) {

	private void scheduleValueComputationTask(ValueComputationTask task) {privatevoidscheduleValueComputationTask(ValueComputationTasktask){




Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




289


290


291


292



    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;






Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013



Better be careful with executors: If they are shutting down, no new tasks may...

 

Better be careful with executors: If they are shutting down, no new tasks may...

Steven Arzt
committed
Oct 26, 2013


289


290


291


292


    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;

    	// If the executor has been killed, there is little point// If the executor has been killed, there is little point    	// in submitting new tasks// in submitting new tasks    	if (executor.isTerminating())if(executor.isTerminating())    		return;return;




refactoring

 


Eric Bodden
committed
Jan 28, 2013




293


294



		executor.execute(task);
	}






refactoring

 


Eric Bodden
committed
Jan 28, 2013



refactoring

 

refactoring

Eric Bodden
committed
Jan 28, 2013


293


294


		executor.execute(task);
	}

		executor.execute(task);executor.execute(task);	}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




295


296



	
	/**






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


295


296


	
	/**

		/**/**




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




297


298



	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


297


298


	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 

	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.	 * 	 * 




comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




299


300



	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 






comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012



comments and minor optimizations

 

comments and minor optimizations

Eric Bodden
committed
Dec 12, 2012


299


300


	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 

	 * For each possible callee, registers incoming call edges.	 * For each possible callee, registers incoming call edges.	 * Also propagates call-to-return flows and summarized callee flows within the caller. 	 * Also propagates call-to-return flows and summarized callee flows within the caller. 




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




301



	 * 






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


301


	 * 

	 * 	 * 




initial checkin



Eric Bodden
committed
Nov 14, 2012




302


303



	 * @param edge an edge whose target node resembles a method call
	 */






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


302


303


	 * @param edge an edge whose target node resembles a method call
	 */

	 * @param edge an edge whose target node resembles a method call	 * @param edge an edge whose target node resembles a method call	 */	 */




removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




304



	private void processCall(PathEdge<N,D> edge) {






removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge

 

removed superfluous type parameter from PathEdge

Eric Bodden
committed
Jul 06, 2013


304


	private void processCall(PathEdge<N,D> edge) {

	private void processCall(PathEdge<N,D> edge) {privatevoidprocessCall(PathEdge<N,D>edge){




initial checkin



Eric Bodden
committed
Nov 14, 2012




305


306



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


305


306


		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...

		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); // a call node; line 14...finalNn=edge.getTarget();// a call node; line 14...




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




307


308


309




        logger.trace("Processing call to {}", n);







Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


307


308


309



        logger.trace("Processing call to {}", n);


        logger.trace("Processing call to {}", n);logger.trace("Processing call to {}",n);




initial checkin



Eric Bodden
committed
Nov 14, 2012




310



		final D d2 = edge.factAtTarget();






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


310


		final D d2 = edge.factAtTarget();

		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();




comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




311



		EdgeFunction<V> f = jumpFunction(edge);






comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012



comments and minor optimizations

 

comments and minor optimizations

Eric Bodden
committed
Dec 12, 2012


311


		EdgeFunction<V> f = jumpFunction(edge);

		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);




generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




312



		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);






generalized some return types

 


Steven Arzt
committed
Jan 17, 2014



generalized some return types

 

generalized some return types

Steven Arzt
committed
Jan 17, 2014


312


		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);

		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);Collection<N>returnSiteNs=icfg.getReturnSitesOfCallAt(n);




initial checkin



Eric Bodden
committed
Nov 14, 2012




313



		






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


		

		




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




314



		//for each possible callee






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


314


		//for each possible callee

		//for each possible callee//for each possible callee




generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




315



		Collection<M> callees = icfg.getCalleesOfCallAt(n);






generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014



generalized ICFG types

 

generalized ICFG types

Steven Arzt
committed
Apr 02, 2014


315


		Collection<M> callees = icfg.getCalleesOfCallAt(n);

		Collection<M> callees = icfg.getCalleesOfCallAt(n);Collection<M>callees=icfg.getCalleesOfCallAt(n);




initial checkin



Eric Bodden
committed
Nov 14, 2012




316



		for(M sCalledProcN: callees) { //still line 14






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


316


		for(M sCalledProcN: callees) { //still line 14

		for(M sCalledProcN: callees) { //still line 14for(MsCalledProcN:callees){//still line 14




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




317


318



			
			//compute the call-flow function






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


317


318


			
			//compute the call-flow function

						//compute the call-flow function//compute the call-flow function




initial checkin



Eric Bodden
committed
Nov 14, 2012




319


320



			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


319


320


			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;

			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);FlowFunction<D>function=flowFunctions.getCallFlowFunction(n,sCalledProcN);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;




Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




321



			Set<D> res = computeCallFlowFunction(function, d1, d2);






Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013



Refactoring: Call flow function is now also computed in a protected method to...

 

Refactoring: Call flow function is now also computed in a protected method to...

Steven Arzt
committed
Oct 28, 2013


321


			Set<D> res = computeCallFlowFunction(function, d1, d2);

			Set<D> res = computeCallFlowFunction(function, d1, d2);Set<D>res=computeCallFlowFunction(function,d1,d2);




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




322


323



			
			//for each callee's start point(s)






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


322


323


			
			//for each callee's start point(s)

						//for each callee's start point(s)//for each callee's start point(s)




generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




324



			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);






generalized some return types

 


Steven Arzt
committed
Jan 17, 2014



generalized some return types

 

generalized some return types

Steven Arzt
committed
Jan 17, 2014


324


			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);

			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);Collection<N>startPointsOf=icfg.getStartPointsOf(sCalledProcN);




memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




325



			for(N sP: startPointsOf) {






memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values

 

memory improvement: do not store implicit TOP values

Steven Arzt
committed
Jul 04, 2013


325


			for(N sP: startPointsOf) {

			for(N sP: startPointsOf) {for(NsP:startPointsOf){




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




326



				//for each result node of the call-flow function






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


				//for each result node of the call-flow function

				//for each result node of the call-flow function//for each result node of the call-flow function




initial checkin



Eric Bodden
committed
Nov 14, 2012




327



				for(D d3: res) {






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


				for(D d3: res) {

				for(D d3: res) {for(Dd3:res){




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




328



					//create initial self-loop






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


328


					//create initial self-loop

					//create initial self-loop//create initial self-loop




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




329



					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


329


					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15

					propagate(d3, sP, d3, EdgeIdentity.<V>v(), n, false); //line 15propagate(d3,sP,d3,EdgeIdentity.<V>v(),n,false);//line 15




initial checkin



Eric Bodden
committed
Nov 14, 2012




330



	






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


330


	

	




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




331



					//register the fact that <sp,d3> has an incoming edge from <n,d2>






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


					//register the fact that <sp,d3> has an incoming edge from <n,d2>

					//register the fact that <sp,d3> has an incoming edge from <n,d2>//register the fact that <sp,d3> has an incoming edge from <n,d2>




initial checkin



Eric Bodden
committed
Nov 14, 2012




332


333


334


335


336



					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


332


333


334


335


336


					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads

					Set<Cell<N, D, EdgeFunction<V>>> endSumm;Set<Cell<N,D,EdgeFunction<V>>>endSumm;					synchronized (incoming) {synchronized(incoming){						//line 15.1 of Naeem/Lhotak/Rodriguez//line 15.1 of Naeem/Lhotak/Rodriguez						addIncoming(sP,d3,n,d2);addIncoming(sP,d3,n,d2);						//line 15.2, copy to avoid concurrent modification exceptions by other threads//line 15.2, copy to avoid concurrent modification exceptions by other threads




memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




337



						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));






memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values

 

memory improvement: do not store implicit TOP values

Steven Arzt
committed
Jul 04, 2013


337


						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));

						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));endSumm=newHashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP,d3));




initial checkin



Eric Bodden
committed
Nov 14, 2012




338


339


340



					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


338


339


340


					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez

					}}										//still line 15.2 of Naeem/Lhotak/Rodriguez//still line 15.2 of Naeem/Lhotak/Rodriguez




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




341



					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


341


					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,

					//for each already-queried exit value <eP,d4> reachable from <sP,d3>,//for each already-queried exit value <eP,d4> reachable from <sP,d3>,




removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012




342



					//create new caller-side jump functions to the return sites






removed caller-side summary functions; instead now just use callee-side "endSummaries"

 


Eric Bodden
committed
Dec 12, 2012



removed caller-side summary functions; instead now just use callee-side "endSummaries"

 

removed caller-side summary functions; instead now just use callee-side "endSummaries"

Eric Bodden
committed
Dec 12, 2012


342


					//create new caller-side jump functions to the return sites

					//create new caller-side jump functions to the return sites//create new caller-side jump functions to the return sites




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




343



					//because we have observed a potentially new incoming edge into <sP,d3>






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


343


					//because we have observed a potentially new incoming edge into <sP,d3>

					//because we have observed a potentially new incoming edge into <sP,d3>//because we have observed a potentially new incoming edge into <sP,d3>




initial checkin



Eric Bodden
committed
Nov 14, 2012




344


345


346


347



					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


344


345


346


347


					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();

					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {for(Cell<N,D,EdgeFunction<V>>entry:endSumm){						N eP = entry.getRowKey();NeP=entry.getRowKey();						D d4 = entry.getColumnKey();Dd4=entry.getColumnKey();						EdgeFunction<V> fCalleeSummary = entry.getValue();EdgeFunction<V>fCalleeSummary=entry.getValue();




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




348



						//for each return site






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


348


						//for each return site

						//for each return site//for each return site




Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013




349



						for(N retSiteN: returnSiteNs) {






Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 


Steven Arzt
committed
Mar 11, 2013



Replaced the duplicate call to the icfg by an access to cached structure we have anyway

 

Replaced the duplicate call to the icfg by an access to cached structure we have anyway

Steven Arzt
committed
Mar 11, 2013


349


						for(N retSiteN: returnSiteNs) {

						for(N retSiteN: returnSiteNs) {for(NretSiteN:returnSiteNs){




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




350



							//compute return-flow function






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


350


							//compute return-flow function

							//compute return-flow function//compute return-flow function




initial checkin



Eric Bodden
committed
Nov 14, 2012




351


352



							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;






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


							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;

							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(n,sCalledProcN,eP,retSiteN);							flowFunctionConstructionCount++;flowFunctionConstructionCount++;




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




353



							//for each target value of the function






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


353


							//for each target value of the function

							//for each target value of the function//for each target value of the function




added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




354



							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {






added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013



added a parameter in the internal protected method and fixed some JavaDoc comments

 

added a parameter in the internal protected method and fixed some JavaDoc comments

Steven Arzt
committed
Sep 19, 2013


354


							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {

							for(D d5: computeReturnFlowFunction(retFunction, d4, n, Collections.singleton(d2))) {for(Dd5:computeReturnFlowFunction(retFunction,d4,n,Collections.singleton(d2))){




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




355



								//update the caller-side summary function






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


355


								//update the caller-side summary function

								//update the caller-side summary function//update the caller-side summary function




initial checkin



Eric Bodden
committed
Nov 14, 2012




356


357



								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


356


357


								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);

								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);EdgeFunction<V>f4=edgeFunctions.getCallEdgeFunction(n,d2,sCalledProcN,d3);								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);EdgeFunction<V>f5=edgeFunctions.getReturnEdgeFunction(n,sCalledProcN,eP,d4,retSiteN,d5);




Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




358


359


360



								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);






Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014



Added missing restoreContext call in processCall

 

Added missing restoreContext call in processCall

Johannes Lerch
committed
Mar 03, 2014


358


359


360


								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					
								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);
								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);

								EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);					EdgeFunction<V>fPrime=f4.composeWith(fCalleeSummary).composeWith(f5);								D d5_restoredCtx = restoreContextOnReturnedFact(d2, d5);Dd5_restoredCtx=restoreContextOnReturnedFact(d2,d5);								propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);propagate(d1,retSiteN,d5_restoredCtx,f.composeWith(fPrime),n,false);




initial checkin



Eric Bodden
committed
Nov 14, 2012




361


362


363


364


365


366



							}
						}
					}
				}		
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


361


362


363


364


365


366


							}
						}
					}
				}		
			}
		}

							}}						}}					}}				}		}			}}		}}




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




367


368



		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


367


368


		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions

		//line 17-19 of Naeem/Lhotak/Rodriguez		//line 17-19 of Naeem/Lhotak/Rodriguez				//process intra-procedural flows along call-to-return flow functions//process intra-procedural flows along call-to-return flow functions




initial checkin



Eric Bodden
committed
Nov 14, 2012




369


370


371



		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


369


370


371


		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;

		for (N returnSiteN : returnSiteNs) {for(NreturnSiteN:returnSiteNs){			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);FlowFunction<D>callToReturnFlowFunction=flowFunctions.getCallToReturnFlowFunction(n,returnSiteN);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




372



			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {






Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


372


			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {

			for(D d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2)) {for(Dd3:computeCallToReturnFlowFunction(callToReturnFlowFunction,d1,d2)){




initial checkin



Eric Bodden
committed
Nov 14, 2012




373



				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


373


				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);

				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);EdgeFunction<V>edgeFnE=edgeFunctions.getCallToReturnEdgeFunction(n,d2,returnSiteN,d3);




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




374



				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


374


				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);

				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE), n, false);propagate(d1,returnSiteN,d3,f.composeWith(edgeFnE),n,false);




initial checkin



Eric Bodden
committed
Nov 14, 2012




375


376


377


378



			}
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


375


376


377


378


			}
		}
	}


			}}		}}	}}




Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




379


380


381


382


383


384


385


386


387


388


389


390



	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}







Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013



Refactoring: Call flow function is now also computed in a protected method to...

 

Refactoring: Call flow function is now also computed in a protected method to...

Steven Arzt
committed
Oct 28, 2013


379


380


381


382


383


384


385


386


387


388


389


390


	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */
	protected Set<D> computeCallFlowFunction
			(FlowFunction<D> callFlowFunction, D d1, D d2) {
		return callFlowFunction.computeTargets(d2);
	}


	/**/**	 * Computes the call flow function for the given call-site abstraction	 * Computes the call flow function for the given call-site abstraction	 * @param callFlowFunction The call flow function to compute	 * @param callFlowFunction The call flow function to compute	 * @param d1 The abstraction at the current method's start node.	 * @param d1 The abstraction at the current method's start node.	 * @param d2 The abstraction at the call site	 * @param d2 The abstraction at the call site	 * @return The set of caller-side abstractions at the callee's start node	 * @return The set of caller-side abstractions at the callee's start node	 */	 */	protected Set<D> computeCallFlowFunctionprotectedSet<D>computeCallFlowFunction			(FlowFunction<D> callFlowFunction, D d1, D d2) {(FlowFunction<D>callFlowFunction,Dd1,Dd2){		return callFlowFunction.computeTargets(d2);returncallFlowFunction.computeTargets(d2);	}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




391



	/**






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


391


	/**

	/**/**




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




392



	 * Computes the call-to-return flow function for the given call-site






Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


392


	 * Computes the call-to-return flow function for the given call-site

	 * Computes the call-to-return flow function for the given call-site	 * Computes the call-to-return flow function for the given call-site




Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013




393



	 * abstraction






Refactoring: Call flow function is now also computed in a protected method to...

 


Steven Arzt
committed
Oct 28, 2013



Refactoring: Call flow function is now also computed in a protected method to...

 

Refactoring: Call flow function is now also computed in a protected method to...

Steven Arzt
committed
Oct 28, 2013


393


	 * abstraction

	 * abstraction	 * abstraction




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




394


395


396



	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.






Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


394


395


396


	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.

	 * @param callToReturnFlowFunction The call-to-return flow function to	 * @param callToReturnFlowFunction The call-to-return flow function to	 * compute	 * compute	 * @param d1 The abstraction at the current method's start node.	 * @param d1 The abstraction at the current method's start node.




added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




397



	 * @param d2 The abstraction at the call site






added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013



added a parameter in the internal protected method and fixed some JavaDoc comments

 

added a parameter in the internal protected method and fixed some JavaDoc comments

Steven Arzt
committed
Sep 19, 2013


397


	 * @param d2 The abstraction at the call site

	 * @param d2 The abstraction at the call site	 * @param d2 The abstraction at the call site




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




398


399


400


401


402


403



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}






Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


398


399


400


401


402


403


	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeCallToReturnFlowFunction
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
		return callToReturnFlowFunction.computeTargets(d2);
	}

	 * @return The set of caller-side abstractions at the return site	 * @return The set of caller-side abstractions at the return site	 */	 */	protected Set<D> computeCallToReturnFlowFunctionprotectedSet<D>computeCallToReturnFlowFunction			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {(FlowFunction<D>callToReturnFlowFunction,Dd1,Dd2){		return callToReturnFlowFunction.computeTargets(d2);returncallToReturnFlowFunction.computeTargets(d2);	}}




1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




404



	






1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013



1) semantic fix: unbalanced returns are associated with a caller-side zero...

 

1) semantic fix: unbalanced returns are associated with a caller-side zero...

Steven Arzt
committed
Oct 10, 2013


404


	

	




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




405


406



	/**
	 * Lines 21-32 of the algorithm.






Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


405


406


	/**
	 * Lines 21-32 of the algorithm.

	/**/**	 * Lines 21-32 of the algorithm.	 * Lines 21-32 of the algorithm.




comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012




407


408


409


410


411


412



	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits






comments and minor optimizations

 


Eric Bodden
committed
Dec 12, 2012



comments and minor optimizations

 

comments and minor optimizations

Eric Bodden
committed
Dec 12, 2012


407


408


409


410


411


412


	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits

	 * 	 * 	 * Stores callee-side summaries.	 * Stores callee-side summaries.	 * Also, at the side of the caller, propagates intra-procedural flows to return sites	 * Also, at the side of the caller, propagates intra-procedural flows to return sites	 * using those newly computed summaries.	 * using those newly computed summaries.	 * 	 * 	 * @param edge an edge whose target node resembles a method exits	 * @param edge an edge whose target node resembles a method exits




initial checkin



Eric Bodden
committed
Nov 14, 2012




413



	 */






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


413


	 */

	 */	 */




Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013




414



	protected void processExit(PathEdge<N,D> edge) {






Merge branch 'develop' into forks/java-fw-bw

 


Eric Bodden
committed
Jul 06, 2013



Merge branch 'develop' into forks/java-fw-bw

 

Merge branch 'develop' into forks/java-fw-bw

Eric Bodden
committed
Jul 06, 2013


414


	protected void processExit(PathEdge<N,D> edge) {

	protected void processExit(PathEdge<N,D> edge) {protectedvoidprocessExit(PathEdge<N,D>edge){




initial checkin



Eric Bodden
committed
Nov 14, 2012




415


416


417


418


419


420


421



		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


415


416


417


418


419


420


421


		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		

		final N n = edge.getTarget(); // an exit node; line 21...finalNn=edge.getTarget();// an exit node; line 21...		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);		M methodThatNeedsSummary = icfg.getMethodOf(n);MmethodThatNeedsSummary=icfg.getMethodOf(n);				final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();		




performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




422



		//for each of the method's start points, determine incoming calls






performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...

 

performance fix for return edges; if there were N start points (e.g. in a...

Eric Bodden
committed
Apr 25, 2013


422


		//for each of the method's start points, determine incoming calls

		//for each of the method's start points, determine incoming calls//for each of the method's start points, determine incoming calls




generalized some return types

 


Steven Arzt
committed
Jan 17, 2014




423



		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);






generalized some return types

 


Steven Arzt
committed
Jan 17, 2014



generalized some return types

 

generalized some return types

Steven Arzt
committed
Jan 17, 2014


423


		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);

		Collection<N> startPointsOf = icfg.getStartPointsOf(methodThatNeedsSummary);Collection<N>startPointsOf=icfg.getStartPointsOf(methodThatNeedsSummary);




1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




424



		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();






1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013



1) added some override annotations

 

1) added some override annotations

Steven Arzt
committed
Oct 25, 2013


424


		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();

		Map<N,Set<D>> inc = new HashMap<N,Set<D>>();Map<N,Set<D>>inc=newHashMap<N,Set<D>>();




performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




425



		for(N sP: startPointsOf) {






performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...

 

performance fix for return edges; if there were N start points (e.g. in a...

Eric Bodden
committed
Apr 25, 2013


425


		for(N sP: startPointsOf) {

		for(N sP: startPointsOf) {for(NsP:startPointsOf){




initial checkin



Eric Bodden
committed
Nov 14, 2012




426


427



			//line 21.1 of Naeem/Lhotak/Rodriguez
			






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


426


427


			//line 21.1 of Naeem/Lhotak/Rodriguez
			

			//line 21.1 of Naeem/Lhotak/Rodriguez//line 21.1 of Naeem/Lhotak/Rodriguez			




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




428



			//register end-summary






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


428


			//register end-summary

			//register end-summary//register end-summary




initial checkin



Eric Bodden
committed
Nov 14, 2012




429


430


431



			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


429


430


431


			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads

			synchronized (incoming) {synchronized(incoming){				addEndSummary(sP, d1, n, d2, f);addEndSummary(sP,d1,n,d2,f);				//copy to avoid concurrent modification exceptions by other threads//copy to avoid concurrent modification exceptions by other threads




1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




432


433



				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));






1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013



1) added some override annotations

 

1) added some override annotations

Steven Arzt
committed
Oct 25, 2013


432


433


				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())
					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));

				for (Entry<N, Set<D>> entry : incoming(d1, sP).entrySet())for(Entry<N,Set<D>>entry:incoming(d1,sP).entrySet())					inc.put(entry.getKey(), new HashSet<D>(entry.getValue()));inc.put(entry.getKey(),newHashSet<D>(entry.getValue()));




1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




434



			}






1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013



1) semantic fix: unbalanced returns are associated with a caller-side zero...

 

1) semantic fix: unbalanced returns are associated with a caller-side zero...

Steven Arzt
committed
Oct 10, 2013


434


			}

			}}




performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




435


436


437


438



		}
		
		//for each incoming call edge already processed
		//(see processCall(..))






performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...

 

performance fix for return edges; if there were N start points (e.g. in a...

Eric Bodden
committed
Apr 25, 2013


435


436


437


438


		}
		
		//for each incoming call edge already processed
		//(see processCall(..))

		}}				//for each incoming call edge already processed//for each incoming call edge already processed		//(see processCall(..))//(see processCall(..))




1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




439



		for (Entry<N,Set<D>> entry: inc.entrySet()) {






1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013



1) added some override annotations

 

1) added some override annotations

Steven Arzt
committed
Oct 25, 2013


439


		for (Entry<N,Set<D>> entry: inc.entrySet()) {

		for (Entry<N,Set<D>> entry: inc.entrySet()) {for(Entry<N,Set<D>>entry:inc.entrySet()){




performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




440


441


442


443


444


445


446



			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;






performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...

 

performance fix for return edges; if there were N start points (e.g. in a...

Eric Bodden
committed
Apr 25, 2013


440


441


442


443


444


445


446


			//line 22
			N c = entry.getKey();
			//for each return site
			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
				//compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
				flowFunctionConstructionCount++;

			//line 22//line 22			N c = entry.getKey();Nc=entry.getKey();			//for each return site//for each return site			for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(c)){				//compute return-flow function//compute return-flow function				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(c,methodThatNeedsSummary,n,retSiteC);				flowFunctionConstructionCount++;flowFunctionConstructionCount++;




added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




447



				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());






added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013



added a parameter in the internal protected method and fixed some JavaDoc comments

 

added a parameter in the internal protected method and fixed some JavaDoc comments

Steven Arzt
committed
Sep 19, 2013


447


				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());

				Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, entry.getValue());Set<D>targets=computeReturnFlowFunction(retFunction,d2,c,entry.getValue());




performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




448


449


450


451


452


453


454


455


456


457



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






performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...

 

performance fix for return edges; if there were N start points (e.g. in a...

Eric Bodden
committed
Apr 25, 2013


448


449


450


451


452


453


454


455


456


457


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

				//for each incoming-call value//for each incoming-call value				for(D d4: entry.getValue()) {for(Dd4:entry.getValue()){					//for each target value at the return site//for each target value at the return site					//line 23//line 23					for(D d5: targets) {for(Dd5:targets){						//compute composed function//compute composed function						EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);EdgeFunction<V>f4=edgeFunctions.getCallEdgeFunction(c,d4,icfg.getMethodOf(n),d1);						EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);EdgeFunction<V>f5=edgeFunctions.getReturnEdgeFunction(c,icfg.getMethodOf(n),n,d2,retSiteC,d5);						EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);EdgeFunction<V>fPrime=f4.composeWith(f).composeWith(f5);						//for each jump function coming into the call, propagate to return site using the composed function//for each jump function coming into the call, propagate to return site using the composed function




added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




458


459


460


461


462



						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();






added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013



added some synchronization statements

 

added some synchronization statements

Steven Arzt
committed
Oct 23, 2013


458


459


460


461


462


						synchronized (jumpFn) { // some other thread might change jumpFn on the way
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)) {
									D d3 = valAndFunc.getKey();

						synchronized (jumpFn) { // some other thread might change jumpFn on the waysynchronized(jumpFn){// some other thread might change jumpFn on the way							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {for(Map.Entry<D,EdgeFunction<V>>valAndFunc:jumpFn.reverseLookup(c,d4).entrySet()){								EdgeFunction<V> f3 = valAndFunc.getValue();EdgeFunction<V>f3=valAndFunc.getValue();								if(!f3.equalTo(allTop)) {if(!f3.equalTo(allTop)){									D d3 = valAndFunc.getKey();Dd3=valAndFunc.getKey();




Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




463


464



									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);






Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014



Enabling possibility to reuse summaries in callees by setting source

 

Enabling possibility to reuse summaries in callees by setting source

Johannes Lerch
committed
Feb 28, 2014


463


464


									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);
									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);

									D d5_restoredCtx = restoreContextOnReturnedFact(d4, d5);Dd5_restoredCtx=restoreContextOnReturnedFact(d4,d5);									propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);propagate(d3,retSiteC,d5_restoredCtx,f3.composeWith(fPrime),c,false);




added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




465



								}






added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013



added some synchronization statements

 

added some synchronization statements

Steven Arzt
committed
Oct 23, 2013


465


								}

								}}




performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




466


467


468


469



							}
						}
					}
				}






performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...

 

performance fix for return edges; if there were N start points (e.g. in a...

Eric Bodden
committed
Apr 25, 2013


466


467


468


469


							}
						}
					}
				}

							}}						}}					}}				}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




470



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


470


			}

			}}




performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013




471


472



		}
		






performance fix for return edges; if there were N start points (e.g. in a...

 


Eric Bodden
committed
Apr 25, 2013



performance fix for return edges; if there were N start points (e.g. in a...

 

performance fix for return edges; if there were N start points (e.g. in a...

Eric Bodden
committed
Apr 25, 2013


471


472


		}
		

		}}		




improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




473



		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow






improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013



improved and simplified handling of unbalanced problems:

 

improved and simplified handling of unbalanced problems:

Eric Bodden
committed
Jul 08, 2013


473


		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow

		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow




further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013




474


475


476



		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {






further fix for followReturnPastSeeds:

 


Eric Bodden
committed
Jul 08, 2013



further fix for followReturnPastSeeds:

 

further fix for followReturnPastSeeds:

Eric Bodden
committed
Jul 08, 2013


474


475


476


		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition
		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {

		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only		//be propagated into callers that have an incoming edge for this condition//be propagated into callers that have an incoming edge for this condition		if(followReturnsPastSeeds && inc.isEmpty() && d1.equals(zeroValue)) {if(followReturnsPastSeeds&&inc.isEmpty()&&d1.equals(zeroValue)){




improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013




477



			// only propagate up if we 






improved and simplified handling of unbalanced problems:

 


Eric Bodden
committed
Jul 08, 2013



improved and simplified handling of unbalanced problems:

 

improved and simplified handling of unbalanced problems:

Eric Bodden
committed
Jul 08, 2013


477


			// only propagate up if we 

			// only propagate up if we // only propagate up if we 




generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014




478



				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);






generalized ICFG types

 


Steven Arzt
committed
Apr 02, 2014



generalized ICFG types

 

generalized ICFG types

Steven Arzt
committed
Apr 02, 2014


478


				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);

				Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);Collection<N>callers=icfg.getCallersOf(methodThatNeedsSummary);




fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




479


480


481


482



				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;






fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

Steven Arzt
committed
Jun 16, 2013


479


480


481


482


				for(N c: callers) {
					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
						flowFunctionConstructionCount++;

				for(N c: callers) {for(Nc:callers){					for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(c)){						FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(c,methodThatNeedsSummary,n,retSiteC);						flowFunctionConstructionCount++;flowFunctionConstructionCount++;




1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




483



						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));






1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013



1) semantic fix: unbalanced returns are associated with a caller-side zero...

 

1) semantic fix: unbalanced returns are associated with a caller-side zero...

Steven Arzt
committed
Oct 10, 2013


483


						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));

						Set<D> targets = computeReturnFlowFunction(retFunction, d2, c, Collections.singleton(zeroValue));Set<D>targets=computeReturnFlowFunction(retFunction,d2,c,Collections.singleton(zeroValue));




fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




484


485



						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);






fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

Steven Arzt
committed
Jun 16, 2013


484


485


						for(D d5: targets) {
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);

						for(D d5: targets) {for(Dd5:targets){							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);EdgeFunction<V>f5=edgeFunctions.getReturnEdgeFunction(c,icfg.getMethodOf(n),n,d2,retSiteC,d5);




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




486



							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


486


							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);

							propagate(zeroValue, retSiteC, d5, f.composeWith(f5), c, true);propagate(zeroValue,retSiteC,d5,f.composeWith(f5),c,true);




fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




487



						}






fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

Steven Arzt
committed
Jun 16, 2013


487


						}

						}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




488


489



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


488


489


					}
				}

					}}				}}




fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013




490


491


492


493


494


495


496


497



				//in cases where there are no callers, the return statement would normally not be processed at all;
				//this might be undesirable if the flow function has a side effect such as registering a taint;
				//instead we thus call the return flow function will a null caller
				if(callers.isEmpty()) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
					flowFunctionConstructionCount++;
					retFunction.computeTargets(d2);
				}






fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 


Steven Arzt
committed
Jun 16, 2013



fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

 

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

Steven Arzt
committed
Jun 16, 2013


490


491


492


493


494


495


496


497


				//in cases where there are no callers, the return statement would normally not be processed at all;
				//this might be undesirable if the flow function has a side effect such as registering a taint;
				//instead we thus call the return flow function will a null caller
				if(callers.isEmpty()) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
					flowFunctionConstructionCount++;
					retFunction.computeTargets(d2);
				}

				//in cases where there are no callers, the return statement would normally not be processed at all;//in cases where there are no callers, the return statement would normally not be processed at all;				//this might be undesirable if the flow function has a side effect such as registering a taint;//this might be undesirable if the flow function has a side effect such as registering a taint;				//instead we thus call the return flow function will a null caller//instead we thus call the return flow function will a null caller				if(callers.isEmpty()) {if(callers.isEmpty()){					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(null,methodThatNeedsSummary,n,null);					flowFunctionConstructionCount++;flowFunctionConstructionCount++;					retFunction.computeTargets(d2);retFunction.computeTargets(d2);				}}




improved handling of unbalanced problems

 


Eric Bodden
committed
Dec 17, 2012




498



			}






improved handling of unbalanced problems

 


Eric Bodden
committed
Dec 17, 2012



improved handling of unbalanced problems

 

improved handling of unbalanced problems

Eric Bodden
committed
Dec 17, 2012


498


			}

			}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




499


500



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


499


500


		}
	

		}}	




Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014




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


514


515


516



	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	






Enabling possibility to reuse summaries in callees by setting source

 


Johannes Lerch
committed
Feb 28, 2014



Enabling possibility to reuse summaries in callees by setting source

 

Enabling possibility to reuse summaries in callees by setting source

Johannes Lerch
committed
Feb 28, 2014


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


514


515


516


	/**
	 * This method will be called for each incoming edge and can be used to
	 * transfer knowledge from the calling edge to the returning edge, without
	 * affecting the summary edges at the callee.
	 * 
	 * @param d4
	 *            Fact stored with the incoming edge, i.e., present at the
	 *            caller side
	 * @param d5
	 *            Fact that originally should be propagated to the caller.
	 * @return Fact that will be propagated to the caller.
	 */
	protected D restoreContextOnReturnedFact(D d4, D d5) {
		return d5;
	}
	

	/**/**	 * This method will be called for each incoming edge and can be used to	 * This method will be called for each incoming edge and can be used to	 * transfer knowledge from the calling edge to the returning edge, without	 * transfer knowledge from the calling edge to the returning edge, without	 * affecting the summary edges at the callee.	 * affecting the summary edges at the callee.	 * 	 * 	 * @param d4	 * @param d4	 *            Fact stored with the incoming edge, i.e., present at the	 *            Fact stored with the incoming edge, i.e., present at the	 *            caller side	 *            caller side	 * @param d5	 * @param d5	 *            Fact that originally should be propagated to the caller.	 *            Fact that originally should be propagated to the caller.	 * @return Fact that will be propagated to the caller.	 * @return Fact that will be propagated to the caller.	 */	 */	protected D restoreContextOnReturnedFact(D d4, D d5) {protectedDrestoreContextOnReturnedFact(Dd4,Dd5){		return d5;returnd5;	}}	




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




517


518


519


520


521



	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee






Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


517


518


519


520


521


	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee

	/**/**	 * Computes the return flow function for the given set of caller-side	 * Computes the return flow function for the given set of caller-side	 * abstractions.	 * abstractions.	 * @param retFunction The return flow function to compute	 * @param retFunction The return flow function to compute	 * @param d2 The abstraction at the exit node in the callee	 * @param d2 The abstraction at the exit node in the callee




added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




522


523



	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site






added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013



added a parameter in the internal protected method and fixed some JavaDoc comments

 

added a parameter in the internal protected method and fixed some JavaDoc comments

Steven Arzt
committed
Sep 19, 2013


522


523


	 * @param callSite The call site
	 * @param callerSideDs The abstractions at the call site

	 * @param callSite The call site	 * @param callSite The call site	 * @param callerSideDs The abstractions at the call site	 * @param callerSideDs The abstractions at the call site




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




524


525


526



	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction






Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


524


525


526


	 * @return The set of caller-side abstractions at the return site
	 */
	protected Set<D> computeReturnFlowFunction

	 * @return The set of caller-side abstractions at the return site	 * @return The set of caller-side abstractions at the return site	 */	 */	protected Set<D> computeReturnFlowFunctionprotectedSet<D>computeReturnFlowFunction




added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013




527



			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {






added a parameter in the internal protected method and fixed some JavaDoc comments

 


Steven Arzt
committed
Sep 19, 2013



added a parameter in the internal protected method and fixed some JavaDoc comments

 

added a parameter in the internal protected method and fixed some JavaDoc comments

Steven Arzt
committed
Sep 19, 2013


527


			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {

			(FlowFunction<D> retFunction, D d2, N callSite, Set<D> callerSideDs) {(FlowFunction<D>retFunction,Dd2,NcallSite,Set<D>callerSideDs){




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




528


529


530



		return retFunction.computeTargets(d2);
	}







Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


528


529


530


		return retFunction.computeTargets(d2);
	}


		return retFunction.computeTargets(d2);returnretFunction.computeTargets(d2);	}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




531


532



	/**
	 * Lines 33-37 of the algorithm.






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


531


532


	/**
	 * Lines 33-37 of the algorithm.

	/**/**	 * Lines 33-37 of the algorithm.	 * Lines 33-37 of the algorithm.




implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012




533



	 * Simply propagate normal, intra-procedural flows.






implemented a small optimization in processExit: propagate intra-procedural...

 


Eric Bodden
committed
Dec 12, 2012



implemented a small optimization in processExit: propagate intra-procedural...

 

implemented a small optimization in processExit: propagate intra-procedural...

Eric Bodden
committed
Dec 12, 2012


533


	 * Simply propagate normal, intra-procedural flows.

	 * Simply propagate normal, intra-procedural flows.	 * Simply propagate normal, intra-procedural flows.




initial checkin



Eric Bodden
committed
Nov 14, 2012




534


535



	 * @param edge
	 */






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


534


535


	 * @param edge
	 */

	 * @param edge	 * @param edge	 */	 */




removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




536



	private void processNormalFlow(PathEdge<N,D> edge) {






removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge

 

removed superfluous type parameter from PathEdge

Eric Bodden
committed
Jul 06, 2013


536


	private void processNormalFlow(PathEdge<N,D> edge) {

	private void processNormalFlow(PathEdge<N,D> edge) {privatevoidprocessNormalFlow(PathEdge<N,D>edge){




initial checkin



Eric Bodden
committed
Nov 14, 2012




537


538


539



		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


537


538


539


		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();

		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); finalNn=edge.getTarget();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();




1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013




540



		






1) semantic fix: unbalanced returns are associated with a caller-side zero...

 


Steven Arzt
committed
Oct 10, 2013



1) semantic fix: unbalanced returns are associated with a caller-side zero...

 

1) semantic fix: unbalanced returns are associated with a caller-side zero...

Steven Arzt
committed
Oct 10, 2013


540


		

		




initial checkin



Eric Bodden
committed
Nov 14, 2012




541


542


543


544



		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


541


542


543


544


		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;

		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);		for (N m : icfg.getSuccsOf(n)) {for(Nm:icfg.getSuccsOf(n)){			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);FlowFunction<D>flowFunction=flowFunctions.getNormalFlowFunction(n,m);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




545



			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);






Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


545


			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);

			Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);Set<D>res=computeNormalFlowFunction(flowFunction,d1,d2);




initial checkin



Eric Bodden
committed
Nov 14, 2012




546


547



			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


546


547


			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));

			for (D d3 : res) {for(Dd3:res){				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));EdgeFunction<V>fprime=f.composeWith(edgeFunctions.getNormalEdgeFunction(n,d2,m,d3));




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




548



				propagate(d1, m, d3, fprime, null, false); 






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


548


				propagate(d1, m, d3, fprime, null, false); 

				propagate(d1, m, d3, fprime, null, false); propagate(d1,m,d3,fprime,null,false);




initial checkin



Eric Bodden
committed
Nov 14, 2012




549


550


551


552



			}
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


549


550


551


552


			}
		}
	}
	

			}}		}}	}}	




Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013




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







Refactored flow function computation to call FlowFunction.computeTargets in a...

 


Steven Arzt
committed
Sep 18, 2013



Refactored flow function computation to call FlowFunction.computeTargets in a...

 

Refactored flow function computation to call FlowFunction.computeTargets in a...

Steven Arzt
committed
Sep 18, 2013


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


	/**/**	 * Computes the normal flow function for the given set of start and end	 * Computes the normal flow function for the given set of start and end	 * abstractions-	 * abstractions-	 * @param flowFunction The normal flow function to compute	 * @param flowFunction The normal flow function to compute	 * @param d1 The abstraction at the method's start node	 * @param d1 The abstraction at the method's start node	 * @param d1 The abstraction at the current node	 * @param d1 The abstraction at the current node	 * @return The set of abstractions at the successor node	 * @return The set of abstractions at the successor node	 */	 */	protected Set<D> computeNormalFlowFunctionprotectedSet<D>computeNormalFlowFunction			(FlowFunction<D> flowFunction, D d1, D d2) {(FlowFunction<D>flowFunction,Dd1,Dd2){		return flowFunction.computeTargets(d2);returnflowFunction.computeTargets(d2);	}}




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




566


567


568


569


570


571


572


573


574



	/**
	 * Propagates the flow further down the exploded super graph, merging any edge function that might
	 * already have been computed for targetVal at target. 
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


566


567


568


569


570


571


572


573


574


	/**
	 * Propagates the flow further down the exploded super graph, merging any edge function that might
	 * already have been computed for targetVal at target. 
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 

	/**/**	 * Propagates the flow further down the exploded super graph, merging any edge function that might	 * Propagates the flow further down the exploded super graph, merging any edge function that might	 * already have been computed for targetVal at target. 	 * already have been computed for targetVal at target. 	 * @param sourceVal the source value of the propagated summary edge	 * @param sourceVal the source value of the propagated summary edge	 * @param target the target statement	 * @param target the target statement	 * @param targetVal the target value at the target statement	 * @param targetVal the target value at the target statement	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 	 * @param f the new edge function computed from (s0,sourceVal) to (target,targetVal) 	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




575


576



	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


575


576


	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 

	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IDESolver}) 




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




577



	 */






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


577


	 */

	 */	 */




first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013




578


579


580



	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {






first version of fw-bw lockstep analysis that seems to be working; the trick...

 


Eric Bodden
committed
Jul 09, 2013



first version of fw-bw lockstep analysis that seems to be working; the trick...

 

first version of fw-bw lockstep analysis that seems to be working; the trick...

Eric Bodden
committed
Jul 09, 2013


578


579


580


	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,
		/* deliberately exposed to clients */ N relatedCallSite,
		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {

	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f,protectedvoidpropagate(DsourceVal,Ntarget,DtargetVal,EdgeFunction<V>f,		/* deliberately exposed to clients */ N relatedCallSite,/* deliberately exposed to clients */NrelatedCallSite,		/* deliberately exposed to clients */ boolean isUnbalancedReturn) {/* deliberately exposed to clients */booleanisUnbalancedReturn){




initial checkin



Eric Bodden
committed
Nov 14, 2012




581



		EdgeFunction<V> jumpFnE;






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


581


		EdgeFunction<V> jumpFnE;

		EdgeFunction<V> jumpFnE;EdgeFunction<V>jumpFnE;




fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




582


583



		EdgeFunction<V> fPrime;
		boolean newFunction;






fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013



fixing race condition found by Steven Arzt

 

fixing race condition found by Steven Arzt

Eric Bodden
committed
Jan 08, 2013


582


583


		EdgeFunction<V> fPrime;
		boolean newFunction;

		EdgeFunction<V> fPrime;EdgeFunction<V>fPrime;		boolean newFunction;booleannewFunction;




initial checkin



Eric Bodden
committed
Nov 14, 2012




584


585



		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


584


585


		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);

		synchronized (jumpFn) {synchronized(jumpFn){			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);jumpFnE=jumpFn.reverseLookup(target,targetVal).get(sourceVal);




fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




586


587


588


589



			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {






fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013



fixing race condition found by Steven Arzt

 

fixing race condition found by Steven Arzt

Eric Bodden
committed
Jan 08, 2013


586


587


588


589


			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
			fPrime = jumpFnE.joinWith(f);
			newFunction = !fPrime.equalTo(jumpFnE);
			if(newFunction) {

			if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)if(jumpFnE==null)jumpFnE=allTop;//JumpFn is initialized to all-top (see line [2] in SRH96 paper)			fPrime = jumpFnE.joinWith(f);fPrime=jumpFnE.joinWith(f);			newFunction = !fPrime.equalTo(jumpFnE);newFunction=!fPrime.equalTo(jumpFnE);			if(newFunction) {if(newFunction){




initial checkin



Eric Bodden
committed
Nov 14, 2012




590


591



				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
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


590


591


				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}

				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);jumpFn.addFunction(sourceVal,target,targetVal,fPrime);			}}




fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013




592


593


594



		}

		if(newFunction) {






fixing race condition found by Steven Arzt

 


Eric Bodden
committed
Jan 08, 2013



fixing race condition found by Steven Arzt

 

fixing race condition found by Steven Arzt

Eric Bodden
committed
Jan 08, 2013


592


593


594


		}

		if(newFunction) {

		}}		if(newFunction) {if(newFunction){




removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




595



			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);






removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge

 

removed superfluous type parameter from PathEdge

Eric Bodden
committed
Jul 06, 2013


595


			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);

			PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);PathEdge<N,D>edge=newPathEdge<N,D>(sourceVal,target,targetVal);




minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




596



			scheduleEdgeProcessing(edge);






minor cleanups

 


Eric Bodden
committed
Jan 26, 2013



minor cleanups

 

minor cleanups

Eric Bodden
committed
Jan 26, 2013


596


			scheduleEdgeProcessing(edge);

			scheduleEdgeProcessing(edge);scheduleEdgeProcessing(edge);




initial checkin



Eric Bodden
committed
Nov 14, 2012




597










initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


597









Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




598



            if(targetVal!=zeroValue) {






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


598


            if(targetVal!=zeroValue) {

            if(targetVal!=zeroValue) {if(targetVal!=zeroValue){




Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014




599



                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );






Added missing restoreContext call in processCall

 


Johannes Lerch
committed
Mar 03, 2014



Added missing restoreContext call in processCall

 

Added missing restoreContext call in processCall

Johannes Lerch
committed
Mar 03, 2014


599


                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );

                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime );logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}",getDebugName(),icfg.getMethodOf(target),sourceVal,target,targetVal,fPrime);




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




600



            }






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


600


            }

            }}




initial checkin



Eric Bodden
committed
Nov 14, 2012




601


602


603



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


601


602


603


		}
	}
	

		}}	}}	




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




604


605


606


607


608



	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)






reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


604


605


606


607


608


	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)

	/**/**	 * Computes the final values for edge functions.	 * Computes the final values for edge functions.	 */	 */	private void computeValues() {	privatevoidcomputeValues(){		//Phase II(i)//Phase II(i)




Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013




609



        logger.debug("Computing the final values for the edge functions");






Added logging information in IDESolver.computeValues

 


Marc-André Laverdière
committed
Oct 10, 2013



Added logging information in IDESolver.computeValues

 

Added logging information in IDESolver.computeValues

Marc-André Laverdière
committed
Oct 10, 2013


609


        logger.debug("Computing the final values for the edge functions");

        logger.debug("Computing the final values for the edge functions");logger.debug("Computing the final values for the edge functions");




changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




610


611


612


613


614


615


616



		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {
				setVal(startPoint, val, valueLattice.bottomElement());
				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); 
				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
			}






changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...

 

changing initialization of analysis such that initialSeeds not is a mapping...

Eric Bodden
committed
Jul 05, 2013


610


611


612


613


614


615


616


		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue()) {
				setVal(startPoint, val, valueLattice.bottomElement());
				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); 
				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
			}

		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {for(Entry<N,Set<D>>seed:initialSeeds.entrySet()){			N startPoint = seed.getKey();NstartPoint=seed.getKey();			for(D val: seed.getValue()) {for(Dval:seed.getValue()){				setVal(startPoint, val, valueLattice.bottomElement());setVal(startPoint,val,valueLattice.bottomElement());				Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, val); Pair<N,D>superGraphNode=newPair<N,D>(startPoint,val);				scheduleValueProcessing(new ValuePropagationTask(superGraphNode));scheduleValueProcessing(newValuePropagationTask(superGraphNode));			}}




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




617



		}






reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


617


		}

		}}




Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013




618



		logger.debug("Computed the final values of the edge functions");






Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 


Marc-André Laverdière
committed
Oct 10, 2013



Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

 

Added DEBUG field as it is needed by JimpleBasedInterproceduralCFG

Marc-André Laverdière
committed
Oct 10, 2013


618


		logger.debug("Computed the final values of the edge functions");

		logger.debug("Computed the final values of the edge functions");logger.debug("Computed the final values of the edge functions");




Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




619


620


621


622


623


624



		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}






Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013



Revert "adding CountLatch"

 

Revert "adding CountLatch"

Eric Bodden
committed
Jan 28, 2013


619


620


621


622


623


624


		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//await termination of tasks//await termination of tasks		try {try{			executor.awaitCompletion();executor.awaitCompletion();		} catch (InterruptedException e) {}catch(InterruptedExceptione){			e.printStackTrace();e.printStackTrace();		}}




Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




625



		






Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013



Fixed race condition in IDESolver and simplified the code

 

Fixed race condition in IDESolver and simplified the code

Marc-André Laverdière
committed
Jan 25, 2013


625


		

		




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




626


627


628


629


630


631


632


633


634



		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;






reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


626


627


628


629


630


631


632


633


634


		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;

		//Phase II(ii)//Phase II(ii)		//we create an array of all nodes and then dispatch fractions of this array to multiple threads//we create an array of all nodes and then dispatch fractions of this array to multiple threads		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();Set<N>allNonCallStartNodes=icfg.allNonCallStartNodes();		@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];N[]nonCallStartNodesArray=(N[])newObject[allNonCallStartNodes.size()];		int i=0;inti=0;		for (N n : allNonCallStartNodes) {for(Nn:allNonCallStartNodes){			nonCallStartNodesArray[i] = n;nonCallStartNodesArray[i]=n;			i++;i++;




Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013




635


636



		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown






Fixed race condition in IDESolver and simplified the code

 


Marc-André Laverdière
committed
Jan 25, 2013



Fixed race condition in IDESolver and simplified the code

 

Fixed race condition in IDESolver and simplified the code

Marc-André Laverdière
committed
Jan 25, 2013


635


636


		}
		//No need to keep track of the number of tasks scheduled here, since we call shutdown

		}}		//No need to keep track of the number of tasks scheduled here, since we call shutdown//No need to keep track of the number of tasks scheduled here, since we call shutdown




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




637



		for(int t=0;t<numThreads; t++) {






reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


637


		for(int t=0;t<numThreads; t++) {

		for(int t=0;t<numThreads; t++) {for(intt=0;t<numThreads;t++){




refactoring

 


Eric Bodden
committed
Jan 28, 2013




638


639



			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);






refactoring

 


Eric Bodden
committed
Jan 28, 2013



refactoring

 

refactoring

Eric Bodden
committed
Jan 28, 2013


638


639


			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);
			scheduleValueComputationTask(task);

			ValueComputationTask task = new ValueComputationTask(nonCallStartNodesArray, t);ValueComputationTasktask=newValueComputationTask(nonCallStartNodesArray,t);			scheduleValueComputationTask(task);scheduleValueComputationTask(task);




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




640



		}






reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


640


		}

		}}




Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013




641


642


643


644


645


646



		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}






Revert "adding CountLatch"

 


Eric Bodden
committed
Jan 28, 2013



Revert "adding CountLatch"

 

Revert "adding CountLatch"

Eric Bodden
committed
Jan 28, 2013


641


642


643


644


645


646


		//await termination of tasks
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//await termination of tasks//await termination of tasks		try {try{			executor.awaitCompletion();executor.awaitCompletion();		} catch (InterruptedException e) {}catch(InterruptedExceptione){			e.printStackTrace();e.printStackTrace();		}}




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687



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






reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


647


648


649


650


651


652


653


654


655


656


657


658


659


660


661


662


663


664


665


666


667


668


669


670


671


672


673


674


675


676


677


678


679


680


681


682


683


684


685


686


687


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

	}}	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {privatevoidpropagateValueAtStart(Pair<N,D>nAndD,Nn){		D d = nAndD.getO2();		Dd=nAndD.getO2();		M p = icfg.getMethodOf(n);Mp=icfg.getMethodOf(n);		for(N c: icfg.getCallsFromWithin(p)) {					for(Nc:icfg.getCallsFromWithin(p)){			Set<Entry<D, EdgeFunction<V>>> entries; Set<Entry<D,EdgeFunction<V>>>entries;			synchronized (jumpFn) {synchronized(jumpFn){				entries = jumpFn.forwardLookup(d,c).entrySet();entries=jumpFn.forwardLookup(d,c).entrySet();				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {for(Map.Entry<D,EdgeFunction<V>>dPAndFP:entries){					D dPrime = dPAndFP.getKey();DdPrime=dPAndFP.getKey();					EdgeFunction<V> fPrime = dPAndFP.getValue();EdgeFunction<V>fPrime=dPAndFP.getValue();					N sP = n;NsP=n;					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));					flowFunctionApplicationCount++;flowFunctionApplicationCount++;				}}			}}		}}	}}		private void propagateValueAtCall(Pair<N, D> nAndD, N n) {privatevoidpropagateValueAtCall(Pair<N,D>nAndD,Nn){		D d = nAndD.getO2();Dd=nAndD.getO2();		for(M q: icfg.getCalleesOfCallAt(n)) {for(Mq:icfg.getCalleesOfCallAt(n)){			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);FlowFunction<D>callFlowFunction=flowFunctions.getCallFlowFunction(n,q);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;			for(D dPrime: callFlowFunction.computeTargets(d)) {for(DdPrime:callFlowFunction.computeTargets(d)){				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);EdgeFunction<V>edgeFn=edgeFunctions.getCallEdgeFunction(n,d,q,dPrime);				for(N startPoint: icfg.getStartPointsOf(q)) {for(NstartPoint:icfg.getStartPointsOf(q)){					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));propagateValue(startPoint,dPrime,edgeFn.computeTarget(val(n,d)));					flowFunctionApplicationCount++;flowFunctionApplicationCount++;				}}			}}		}}	}}		private void propagateValue(N nHashN, D nHashD, V v) {privatevoidpropagateValue(NnHashN,DnHashD,Vv){		synchronized (val) {synchronized(val){			V valNHash = val(nHashN, nHashD);VvalNHash=val(nHashN,nHashD);			V vPrime = valueLattice.join(valNHash,v);VvPrime=valueLattice.join(valNHash,v);			if(!vPrime.equals(valNHash)) {if(!vPrime.equals(valNHash)){				setVal(nHashN, nHashD, vPrime);setVal(nHashN,nHashD,vPrime);




minor cleanups

 


Eric Bodden
committed
Jan 26, 2013




688



				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));






minor cleanups

 


Eric Bodden
committed
Jan 26, 2013



minor cleanups

 

minor cleanups

Eric Bodden
committed
Jan 26, 2013


688


				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));

				scheduleValueProcessing(new ValuePropagationTask(new Pair<N,D>(nHashN,nHashD)));scheduleValueProcessing(newValuePropagationTask(newPair<N,D>(nHashN,nHashD)));




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




689


690


691


692


693



			}
		}
	}

	private V val(N nHashN, D nHashD){ 






reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


689


690


691


692


693


			}
		}
	}

	private V val(N nHashN, D nHashD){ 

			}}		}}	}}	private V val(N nHashN, D nHashD){ privateVval(NnHashN,DnHashD){




adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




694


695


696


697



		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}






adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...

 

adding synchronization on "val" due to possible race conditions (thanks to...

Eric Bodden
committed
May 29, 2013


694


695


696


697


		V l;
		synchronized (val) {
			l = val.get(nHashN, nHashD);
		}

		V l;Vl;		synchronized (val) {synchronized(val){			l = val.get(nHashN, nHashD);l=val.get(nHashN,nHashD);		}}




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




698


699


700


701



		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	






reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


698


699


700


701


		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	

		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paperif(l==null)returnvalueLattice.topElement();//implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper		else return l;elsereturnl;	}}	




memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




702


703



	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.






memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values

 

memory improvement: do not store implicit TOP values

Steven Arzt
committed
Jul 04, 2013


702


703


	private void setVal(N nHashN, D nHashD,V l){
		// TOP is the implicit default value which we do not need to store.

	private void setVal(N nHashN, D nHashD,V l){privatevoidsetVal(NnHashN,DnHashD,Vl){		// TOP is the implicit default value which we do not need to store.// TOP is the implicit default value which we do not need to store.




adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




704



		synchronized (val) {






adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...

 

adding synchronization on "val" due to possible race conditions (thanks to...

Eric Bodden
committed
May 29, 2013


704


		synchronized (val) {

		synchronized (val) {synchronized(val){




memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




705


706


707


708



			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);






memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values

 

memory improvement: do not store implicit TOP values

Steven Arzt
committed
Jul 04, 2013


705


706


707


708


			if (l == valueLattice.topElement())     // do not store top values
				val.remove(nHashN, nHashD);
			else
				val.put(nHashN, nHashD,l);

			if (l == valueLattice.topElement())     // do not store top valuesif(l==valueLattice.topElement())// do not store top values				val.remove(nHashN, nHashD);val.remove(nHashN,nHashD);			elseelse				val.put(nHashN, nHashD,l);val.put(nHashN,nHashD,l);




adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




709



		}






adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...

 

adding synchronization on "val" due to possible race conditions (thanks to...

Eric Bodden
committed
May 29, 2013


709


		}

		}}




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




710



        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


710


        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);

        logger.debug("VALUE: {} {} {} {}", icfg.getMethodOf(nHashN), nHashN, nHashD, l);logger.debug("VALUE: {} {} {} {}",icfg.getMethodOf(nHashN),nHashN,nHashD,l);




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




711


712



	}







reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


711


712


	}


	}}




removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




713



	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {






removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge

 

removed superfluous type parameter from PathEdge

Eric Bodden
committed
Jul 06, 2013


713


	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {

	private EdgeFunction<V> jumpFunction(PathEdge<N,D> edge) {privateEdgeFunction<V>jumpFunction(PathEdge<N,D>edge){




reordered some methods

 


Eric Bodden
committed
Dec 12, 2012




714


715


716


717


718


719


720



		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}







reordered some methods

 


Eric Bodden
committed
Dec 12, 2012



reordered some methods

 

reordered some methods

Eric Bodden
committed
Dec 12, 2012


714


715


716


717


718


719


720


		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}


		synchronized (jumpFn) {synchronized(jumpFn){			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());EdgeFunction<V>function=jumpFn.forwardLookup(edge.factAtSource(),edge.getTarget()).get(edge.factAtTarget());			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paperif(function==null)returnallTop;//JumpFn initialized to all-top, see line [2] in SRH96 paper			return function;returnfunction;		}}	}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




721


722


723


724


725


726


727


728


729


730


731


732



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






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


721


722


723


724


725


726


727


728


729


730


731


732


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

	private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {privateSet<Cell<N,D,EdgeFunction<V>>>endSummary(NsP,Dd3){		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);Table<N,D,EdgeFunction<V>>map=endSummary.get(sP,d3);		if(map==null) return Collections.emptySet();if(map==null)returnCollections.emptySet();		return map.cellSet();returnmap.cellSet();	}}	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {privatevoidaddEndSummary(NsP,Dd1,NeP,Dd2,EdgeFunction<V>f){		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);Table<N,D,EdgeFunction<V>>summaries=endSummary.get(sP,d1);		if(summaries==null) {if(summaries==null){			summaries = HashBasedTable.create();summaries=HashBasedTable.create();			endSummary.put(sP, d1, summaries);endSummary.put(sP,d1,summaries);		}}




undoing previous "fix"; as discussed with Steven, it is not required (see comment)

 


Eric Bodden
committed
Dec 12, 2012




733


734


735


736



		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);






undoing previous "fix"; as discussed with Steven, it is not required (see comment)

 


Eric Bodden
committed
Dec 12, 2012



undoing previous "fix"; as discussed with Steven, it is not required (see comment)

 

undoing previous "fix"; as discussed with Steven, it is not required (see comment)

Eric Bodden
committed
Dec 12, 2012


733


734


735


736


		//note: at this point we don't need to join with a potential previous f
		//because f is a jump function, which is already properly joined
		//within propagate(..)
		summaries.put(eP,d2,f);

		//note: at this point we don't need to join with a potential previous f//note: at this point we don't need to join with a potential previous f		//because f is a jump function, which is already properly joined//because f is a jump function, which is already properly joined		//within propagate(..)//within propagate(..)		summaries.put(eP,d2,f);summaries.put(eP,d2,f);




initial checkin



Eric Bodden
committed
Nov 14, 2012




737


738



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


737


738


	}	
	

	}	}	




1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




739



	private Map<N, Set<D>> incoming(D d1, N sP) {






1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013



1) added some override annotations

 

1) added some override annotations

Steven Arzt
committed
Oct 25, 2013


739


	private Map<N, Set<D>> incoming(D d1, N sP) {

	private Map<N, Set<D>> incoming(D d1, N sP) {privateMap<N,Set<D>>incoming(Dd1,NsP){




added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




740


741



		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);






added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013



added some synchronization statements

 

added some synchronization statements

Steven Arzt
committed
Oct 23, 2013


740


741


		synchronized (incoming) {
			Map<N, Set<D>> map = incoming.get(sP, d1);

		synchronized (incoming) {synchronized(incoming){			Map<N, Set<D>> map = incoming.get(sP, d1);Map<N,Set<D>>map=incoming.get(sP,d1);




1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013




742


743



			if(map==null) return Collections.emptyMap();
			return map;






1) added some override annotations

 


Steven Arzt
committed
Oct 25, 2013



1) added some override annotations

 

1) added some override annotations

Steven Arzt
committed
Oct 25, 2013


742


743


			if(map==null) return Collections.emptyMap();
			return map;

			if(map==null) return Collections.emptyMap();if(map==null)returnCollections.emptyMap();			return map;returnmap;




added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




744



		}






added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013



added some synchronization statements

 

added some synchronization statements

Steven Arzt
committed
Oct 23, 2013


744


		}

		}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




745


746



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


745


746


	}
	

	}}	




made a method protected

 


Steven Arzt
committed
Oct 14, 2013




747



	protected void addIncoming(N sP, D d3, N n, D d2) {






made a method protected

 


Steven Arzt
committed
Oct 14, 2013



made a method protected

 

made a method protected

Steven Arzt
committed
Oct 14, 2013


747


	protected void addIncoming(N sP, D d3, N n, D d2) {

	protected void addIncoming(N sP, D d3, N n, D d2) {protectedvoidaddIncoming(NsP,Dd3,Nn,Dd2){




added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013




748


749


750


751


752


753


754


755


756


757


758


759



		synchronized (incoming) {
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






added some synchronization statements

 


Steven Arzt
committed
Oct 23, 2013



added some synchronization statements

 

added some synchronization statements

Steven Arzt
committed
Oct 23, 2013


748


749


750


751


752


753


754


755


756


757


758


759


		synchronized (incoming) {
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

		synchronized (incoming) {synchronized(incoming){			Map<N, Set<D>> summaries = incoming.get(sP, d3);Map<N,Set<D>>summaries=incoming.get(sP,d3);			if(summaries==null) {if(summaries==null){				summaries = new HashMap<N, Set<D>>();summaries=newHashMap<N,Set<D>>();				incoming.put(sP, d3, summaries);incoming.put(sP,d3,summaries);			}}			Set<D> set = summaries.get(n);Set<D>set=summaries.get(n);			if(set==null) {if(set==null){				set = new HashSet<D>();set=newHashSet<D>();				summaries.put(n,set);summaries.put(n,set);			}}			set.add(d2);set.add(d2);




initial checkin



Eric Bodden
committed
Nov 14, 2012




760


761


762


763



		}
	}	
	
	/**






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


760


761


762


763


		}
	}	
	
	/**

		}}	}	}		/**/**




memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




764


765



	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.






memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values

 

memory improvement: do not store implicit TOP values

Steven Arzt
committed
Jul 04, 2013


764


765


	 * Returns the V-type result for the given value at the given statement.
	 * TOP values are never returned.

	 * Returns the V-type result for the given value at the given statement.	 * Returns the V-type result for the given value at the given statement.	 * TOP values are never returned.	 * TOP values are never returned.




initial checkin



Eric Bodden
committed
Nov 14, 2012




766


767



	 */
	public V resultAt(N stmt, D value) {






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


766


767


	 */
	public V resultAt(N stmt, D value) {

	 */	 */	public V resultAt(N stmt, D value) {publicVresultAt(Nstmt,Dvalue){




adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




768



		//no need to synchronize here as all threads are known to have terminated






adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...

 

adding synchronization on "val" due to possible race conditions (thanks to...

Eric Bodden
committed
May 29, 2013


768


		//no need to synchronize here as all threads are known to have terminated

		//no need to synchronize here as all threads are known to have terminated//no need to synchronize here as all threads are known to have terminated




initial checkin



Eric Bodden
committed
Nov 14, 2012




769


770


771


772


773



		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


769


770


771


772


773


		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.

		return val.get(stmt, value);returnval.get(stmt,value);	}}		/**/**	 * Returns the resulting environment for the given statement.	 * Returns the resulting environment for the given statement.




memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013




774


775



	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.






memory improvement: do not store implicit TOP values

 


Steven Arzt
committed
Jul 04, 2013



memory improvement: do not store implicit TOP values

 

memory improvement: do not store implicit TOP values

Steven Arzt
committed
Jul 04, 2013


774


775


	 * The artificial zero value is automatically stripped. TOP values are
	 * never returned.

	 * The artificial zero value is automatically stripped. TOP values are	 * The artificial zero value is automatically stripped. TOP values are	 * never returned.	 * never returned.




initial checkin



Eric Bodden
committed
Nov 14, 2012




776


777


778



	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


776


777


778


	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value

	 */	 */	public Map<D,V> resultsAt(N stmt) {publicMap<D,V>resultsAt(Nstmt){		//filter out the artificial zero-value//filter out the artificial zero-value




adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013




779



		//no need to synchronize here as all threads are known to have terminated






adding synchronization on "val" due to possible race conditions (thanks to...

 


Eric Bodden
committed
May 29, 2013



adding synchronization on "val" due to possible race conditions (thanks to...

 

adding synchronization on "val" due to possible race conditions (thanks to...

Eric Bodden
committed
May 29, 2013


779


		//no need to synchronize here as all threads are known to have terminated

		//no need to synchronize here as all threads are known to have terminated//no need to synchronize here as all threads are known to have terminated




initial checkin



Eric Bodden
committed
Nov 14, 2012




780


781


782


783


784


785


786



		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
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


780


781


782


783


784


785


786


		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}

		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {returnMaps.filterKeys(val.row(stmt),newPredicate<D>(){			public boolean apply(D val) {publicbooleanapply(Dval){				return val!=zeroValue;returnval!=zeroValue;			}}		});});	}}




making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013




787


788


789


790


791


792


793



	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}






making executor exchangeable

 


Eric Bodden
committed
Jan 29, 2013



making executor exchangeable

 

making executor exchangeable

Eric Bodden
committed
Jan 29, 2013


787


788


789


790


791


792


793


	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

		/**/**	 * Factory method for this solver's thread-pool executor.	 * Factory method for this solver's thread-pool executor.	 */	 */	protected CountingThreadPoolExecutor getExecutor() {protectedCountingThreadPoolExecutorgetExecutor(){		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());returnnewCountingThreadPoolExecutor(1,this.numThreads,30,TimeUnit.SECONDS,newLinkedBlockingQueue<Runnable>());	}}




changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013




794



	






changed signature of "propagate" to include original call site for return and call flows

 


Eric Bodden
committed
Jul 06, 2013



changed signature of "propagate" to include original call site for return and call flows

 

changed signature of "propagate" to include original call site for return and call flows

Eric Bodden
committed
Jul 06, 2013


794


	

	




added support for debug name

 


Eric Bodden
committed
Jul 06, 2013




795


796


797


798


799


800


801


802



	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "";
	}







added support for debug name

 


Eric Bodden
committed
Jul 06, 2013



added support for debug name

 

added support for debug name

Eric Bodden
committed
Jul 06, 2013


795


796


797


798


799


800


801


802


	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "";
	}


	/**/**	 * Returns a String used to identify the output of this solver in debug mode.	 * Returns a String used to identify the output of this solver in debug mode.	 * Subclasses can overwrite this string to distinguish the output from different solvers.	 * Subclasses can overwrite this string to distinguish the output from different solvers.	 */	 */	protected String getDebugName() {protectedStringgetDebugName(){		return "";return"";	}}




initial checkin



Eric Bodden
committed
Nov 14, 2012




803



	public void printStats() {






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


803


	public void printStats() {

	public void printStats() {publicvoidprintStats(){




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




804



		if(logger.isDebugEnabled()) {






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


804


		if(logger.isDebugEnabled()) {

		if(logger.isDebugEnabled()) {if(logger.isDebugEnabled()){




initial checkin



Eric Bodden
committed
Nov 14, 2012




805


806


807


808


809



			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


805


806


807


808


809


			if(ffCache!=null)
				ffCache.printStats();
			if(efCache!=null)
				efCache.printStats();
		} else {

			if(ffCache!=null)if(ffCache!=null)				ffCache.printStats();ffCache.printStats();			if(efCache!=null)if(efCache!=null)				efCache.printStats();efCache.printStats();		} else {}else{




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




810



			logger.info("No statistics were collected, as DEBUG is disabled.");






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


810


			logger.info("No statistics were collected, as DEBUG is disabled.");

			logger.info("No statistics were collected, as DEBUG is disabled.");logger.info("No statistics were collected, as DEBUG is disabled.");




initial checkin



Eric Bodden
committed
Nov 14, 2012




811


812


813


814



		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


811


812


813


814


		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {

		}}	}}		private class PathEdgeProcessingTask implements Runnable {privateclassPathEdgeProcessingTaskimplementsRunnable{




removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




815



		private final PathEdge<N,D> edge;






removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge

 

removed superfluous type parameter from PathEdge

Eric Bodden
committed
Jul 06, 2013


815


		private final PathEdge<N,D> edge;

		private final PathEdge<N,D> edge;privatefinalPathEdge<N,D>edge;




initial checkin



Eric Bodden
committed
Nov 14, 2012




816










initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


816









removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013




817



		public PathEdgeProcessingTask(PathEdge<N,D> edge) {






removed superfluous type parameter from PathEdge

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge

 

removed superfluous type parameter from PathEdge

Eric Bodden
committed
Jul 06, 2013


817


		public PathEdgeProcessingTask(PathEdge<N,D> edge) {

		public PathEdgeProcessingTask(PathEdge<N,D> edge) {publicPathEdgeProcessingTask(PathEdge<N,D>edge){




initial checkin



Eric Bodden
committed
Nov 14, 2012




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






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


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

			this.edge = edge;this.edge=edge;		}}		public void run() {publicvoidrun(){			if(icfg.isCallStmt(edge.getTarget())) {if(icfg.isCallStmt(edge.getTarget())){				processCall(edge);processCall(edge);			} else {}else{				//note that some statements, such as "throw" may be//note that some statements, such as "throw" may be				//both an exit statement and a "normal" statement//both an exit statement and a "normal" statement				if(icfg.isExitStmt(edge.getTarget())) {if(icfg.isExitStmt(edge.getTarget())){					processExit(edge);processExit(edge);				}}				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()){					processNormalFlow(edge);processNormalFlow(edge);				}}			}}		}}	}}		private class ValuePropagationTask implements Runnable {privateclassValuePropagationTaskimplementsRunnable{		private final Pair<N, D> nAndD;privatefinalPair<N,D>nAndD;		public ValuePropagationTask(Pair<N,D> nAndD) {publicValuePropagationTask(Pair<N,D>nAndD){			this.nAndD = nAndD;this.nAndD=nAndD;		}}		public void run() {publicvoidrun(){			N n = nAndD.getO1();Nn=nAndD.getO1();




bug fix for value computation (need to treat initialSeeds just as method start nodes)

 


Eric Bodden
committed
Feb 14, 2013




846



			if(icfg.isStartPoint(n) ||






bug fix for value computation (need to treat initialSeeds just as method start nodes)

 


Eric Bodden
committed
Feb 14, 2013



bug fix for value computation (need to treat initialSeeds just as method start nodes)

 

bug fix for value computation (need to treat initialSeeds just as method start nodes)

Eric Bodden
committed
Feb 14, 2013


846


			if(icfg.isStartPoint(n) ||

			if(icfg.isStartPoint(n) ||if(icfg.isStartPoint(n)||




changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013




847



				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such






changing initialization of analysis such that initialSeeds not is a mapping...

 


Eric Bodden
committed
Jul 05, 2013



changing initialization of analysis such that initialSeeds not is a mapping...

 

changing initialization of analysis such that initialSeeds not is a mapping...

Eric Bodden
committed
Jul 05, 2013


847


				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as such

				initialSeeds.containsKey(n)) { 		//our initial seeds are not necessarily method-start points but here they should be treated as suchinitialSeeds.containsKey(n)){//our initial seeds are not necessarily method-start points but here they should be treated as such




initial checkin



Eric Bodden
committed
Nov 14, 2012




848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886



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





initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


848


849


850


851


852


853


854


855


856


857


858


859


860


861


862


863


864


865


866


867


868


869


870


871


872


873


874


875


876


877


878


879


880


881


882


883


884


885


886


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
				propagateValueAtStart(nAndD, n);propagateValueAtStart(nAndD,n);			}}			if(icfg.isCallStmt(n)) {if(icfg.isCallStmt(n)){				propagateValueAtCall(nAndD, n);propagateValueAtCall(nAndD,n);			}}		}}	}}		private class ValueComputationTask implements Runnable {privateclassValueComputationTaskimplementsRunnable{		private final N[] values;privatefinalN[]values;		final int num;finalintnum;		public ValueComputationTask(N[] values, int num) {publicValueComputationTask(N[]values,intnum){			this.values = values;this.values=values;			this.num = num;this.num=num;		}}		public void run() {publicvoidrun(){			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;intsectionSize=(int)Math.floor(values.length/numThreads)+numThreads;			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {for(inti=sectionSize*num;i<Math.min(sectionSize*(num+1),values.length);i++){				N n = values[i];Nn=values[i];				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					for(NsP:icfg.getStartPointsOf(icfg.getMethodOf(n))){					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;Set<Cell<D,D,EdgeFunction<V>>>lookupByTarget;					lookupByTarget = jumpFn.lookupByTarget(n);lookupByTarget=jumpFn.lookupByTarget(n);					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {for(Cell<D,D,EdgeFunction<V>>sourceValTargetValAndFunction:lookupByTarget){						D dPrime = sourceValTargetValAndFunction.getRowKey();DdPrime=sourceValTargetValAndFunction.getRowKey();						D d = sourceValTargetValAndFunction.getColumnKey();Dd=sourceValTargetValAndFunction.getColumnKey();						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();EdgeFunction<V>fPrime=sourceValTargetValAndFunction.getValue();						synchronized (val) {synchronized(val){							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));						}}						flowFunctionApplicationCount++;flowFunctionApplicationCount++;					}}				}}			}}		}}	}}}}





