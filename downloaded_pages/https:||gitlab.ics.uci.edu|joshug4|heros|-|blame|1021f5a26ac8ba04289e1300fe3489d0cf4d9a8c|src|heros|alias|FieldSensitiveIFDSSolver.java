



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

1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c

















1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



27.2 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






1




/*******************************************************************************









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






2




 * Copyright (c) 2014 Johannes Lerch, Johannes Späth.









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






9




 *     Johannes Lerch, Johannes Späth - initial API and implementation









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






10




11




12




 ******************************************************************************/
package heros.alias;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






13




14




15




16




import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






17




import heros.alias.AccessPath.PrefixTestResult;









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






18




import heros.alias.FlowFunction.ConstrainedFact;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






19




import heros.alias.FlowFunction.Constraint;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






20




21




22




23




import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




import java.util.Collection;
import java.util.Collections;









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






26




import java.util.HashMap;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






27




import java.util.LinkedList;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






28




29




import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






30




import java.util.Set;









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




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






37




import com.google.common.base.Optional;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






38




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






39




import com.google.common.cache.CacheBuilder;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






40




import com.google.common.collect.Lists;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






41




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






42














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






43




public class FieldSensitiveIFDSSolver<N, BaseValue, FieldRef, D extends FieldSensitiveFact<BaseValue, FieldRef, D>, M, I extends InterproceduralCFG<N, M>> {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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






	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel
			(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	
    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);

    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace
    public static final boolean DEBUG = logger.isDebugEnabled();

	protected CountingThreadPoolExecutor executor;
	
	@DontSynchronize("only used by single thread")
	protected int numThreads;
	
	@SynchronizedBy("thread safe data structure, consistent locking when used")
	protected final JumpFunctions<N,D> jumpFn;
	
	@SynchronizedBy("thread safe data structure, only modified internally")
	protected final I icfg;
	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






68




69




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






70




71




72




73




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






74




75




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






76




	









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






77




	protected final MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>>();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






78




	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






79




	@DontSynchronize("stateless")









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






80




	protected final FlowFunctions<N, FieldRef, D, M> flowFunctions;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	
	@DontSynchronize("only used by single thread")
	protected final Map<N,Set<D>> initialSeeds;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("stateless")
	protected final D zeroValue;
	
	@DontSynchronize("readOnly")
	protected final FlowFunctionCache<N,D,M> ffCache = null; 
	
	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






96




97





	private LinkedList<Runnable> worklist;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




100




101




102




103




	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






104




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef, D,M,I> tabulationProblem) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




108




109




110




111




112




113




		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






114




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




117




118




119




120




		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






121




		FlowFunctions<N, FieldRef, D, M> flowFunctions = tabulationProblem.flowFunctions(); 









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		/*if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}*/
		this.flowFunctions = flowFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.jumpFn = new JumpFunctions<N,D>();
		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






132




		this.numThreads = 1; //Math.max(1,tabulationProblem.numThreads()); //solution is in the current state not thread safe









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






133




		this.executor = getExecutor();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






134




		this.worklist = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






135




136




137




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




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */
	public void solve() {		
		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.
	 */
	protected void submitInitialSeeds() {
		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue())









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






154




				propagate(new PathEdge<>(zeroValue, startPoint, val), null, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




178




179




180




181




182




183




			jumpFn.addFunction(new PathEdge<N, D>(zeroValue, startPoint, zeroValue));
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{
			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();
		}
		if(logger.isDebugEnabled())
			printStats();

		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway
		executor.shutdown();
		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed
		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






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




194




//		try {
//			executor.awaitCompletion();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Throwable exception = executor.getException();
//		if(exception!=null) {
//			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
//		}
		while(!worklist.isEmpty()) {
			worklist.pop().run();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






195




196




197




198




199




200




201




202




203




204




		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






205




206




207




208




//    	if (executor.isTerminating())
//    		return;
//    	executor.execute(new PathEdgeProcessingTask(edge));
    	worklist.add(new PathEdgeProcessingTask(edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






209




210




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




229




230




231




232




233




    	propagationCount++;
    }
	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 
	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 
	 * 
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...

        logger.trace("Processing call to {}", n);

		final D d2 = edge.factAtTarget();
		assert d2 != null;
		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		
		//for each possible callee
		Collection<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			//compute the call-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






234




			FlowFunction<FieldRef, D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






235




			Set<ConstrainedFact<FieldRef, D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






236




237




238




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






239




			for(ConstrainedFact<FieldRef, D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




242




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









avoiding unnecessary calls to clone a fact


 

 


Johannes Lerch
committed
Jan 08, 2015






243




					D abstractStartPointFact = AccessPathUtil.cloneWithAccessPath(d3.getFact(), new AccessPath<FieldRef>());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






244




					propagate(new PathEdge<>(abstractStartPointFact, sP, abstractStartPointFact), n, false); //line 15









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






245




246




247




248




				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






249




250




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






251




252




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






253




				resumeEdges(sCalledProcN, d3.getFact());









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






254




255




				registerInterestedCaller(sCalledProcN, incomingEdge);
				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






256




257




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






258




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






259




260




261




262




263




264




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






265




					for(SummaryEdge<D, N> summary: endSumm) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






266




267




268




269




270




271




272




273




274




275




						Optional<D> d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
						if(d4.isPresent()) {
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(ConstrainedFact<FieldRef, D> d5: computeReturnFlowFunction(retFunction, d4.get(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(new PathEdge<>(d1, retSiteN, d5p_restoredCtx), n, false);









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






276




277




								}
							}









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






278




						}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






279




280




281




282




283




284




					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






285




			FlowFunction<FieldRef, D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






286




			for(ConstrainedFact<FieldRef, D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






287




				propagate(new PathEdge<>(d1, returnSiteN, d3.getFact()), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






288




289




290




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






291




292




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






293




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.get(method);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






294




295




		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






296




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) == PrefixTestResult.GUARANTEED_PREFIX) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






297




298




					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






299




						propagate(edge, edge instanceof ConcretizationPathEdge ? edge.getTarget() : null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






300




301




302




303




304




					}
				}
			}
		}
	}









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






305




306




307




308




	
	private void registerInterestedCaller(M method, IncomingEdge<D, N> incomingEdge) {
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






309




			for(final PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






310




				if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), edge.factAtSource()).atLeast(PrefixTestResult.POTENTIAL_PREFIX)) {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






311




312




					logger.trace("RECHECKING-PAUSED-EDGE: {} for new incoming edge {}", edge, incomingEdge);
					









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






313




314




					Constraint<FieldRef> constraint = new DeltaConstraint<FieldRef>(incomingEdge.getCalleeSourceFact().getAccessPath(), edge.factAtSource().getAccessPath());
					propagateConstrained(new ConcretizationPathEdge<>(









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






315




316




							applyConstraint(constraint, incomingEdge.getCallerSourceFact()), 
							incomingEdge.getCallSite(), 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






317




318




							applyConstraint(constraint, incomingEdge.getCallerCallSiteFact()),
							method,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






319




							applyConstraint(constraint, incomingEdge.getCalleeSourceFact())));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






320




321




322




323




				}
			}
		}
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






324














FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






325




326




327




328




329




330




331




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






332




	protected Set<ConstrainedFact<FieldRef, D>> computeCallFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






333




			(FlowFunction<FieldRef, D> callFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		return callFlowFunction.computeTargets(d2);
	}

	/**
	 * Computes the call-to-return flow function for the given call-site
	 * abstraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






346




	protected Set<ConstrainedFact<FieldRef, D>> computeCallToReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






347




			(FlowFunction<FieldRef, D> callToReturnFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






348




349




350




351




352




353




354




355




356




357




358




359




360




361




362




363




364




365




366




367




368




369




370




		return callToReturnFlowFunction.computeTargets(d2);
	}
	
	/**
	 * Lines 21-32 of the algorithm.
	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits
	 */
	protected void processExit(PathEdge<N,D> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		//for each of the method's start points, determine incoming calls
		
		//line 21.1 of Naeem/Lhotak/Rodriguez
		//register end-summary









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






371




372




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






373




			return; //FIXME: should never be reached?! -> assert ?









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






374




375




376




		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






377




378




379




380




381




382




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






383




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




				









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






385




				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






386




387




					Optional<D> concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);
					if(concreteCalleeExitFact.isPresent()) {









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






388




						Set<ConstrainedFact<FieldRef, D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact.get(), callSite);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






389




390




	
						// for each incoming-call value









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






391




						for (ConstrainedFact<FieldRef, D> callerTargetAnnotatedFact : callerTargetFacts) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






392




							D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






393




							propagate(new PathEdge<>(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact), callSite, false);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






394




						}









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






395




					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






396




397




				}
			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






398




399




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






400




401




402




403




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









bug fix in unbalanced return handling


 

 


Johannes Lerch
committed
Jan 29, 2015






404




		if(followReturnsPastSeeds && d1 == zeroValue) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






405




406




407




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






408




					FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






409




410




					Set<ConstrainedFact<FieldRef, D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(ConstrainedFact<FieldRef, D> d5: targets)









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






411




						propagate(new PathEdge<>(zeroValue, retSiteC, d5.getFact()), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






412




413




414




415




416




417




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






418




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




431




				retFunction.computeTargets(d2);
			}
		}
	}
	
	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callSite The call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






432




	protected Set<ConstrainedFact<FieldRef, D>> computeReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






433




			(FlowFunction<FieldRef, D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






434




435




436




437




438




439




440




441




442




443




444




445




446




447




		return retFunction.computeTargets(d2);
	}

	/**
	 * Lines 33-37 of the algorithm.
	 * Simply propagate normal, intra-procedural flows.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		
		for (N m : icfg.getSuccsOf(n)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






448




			FlowFunction<FieldRef, D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






449




450




			Set<ConstrainedFact<FieldRef, D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (ConstrainedFact<FieldRef, D> d3 : res) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






451




				if(d3.getConstraint() != null) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






452




					propagateConstrained(new PathEdge<>(applyConstraint(d3.getConstraint(), d1), m, d3.getFact()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






453




				}









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






454




				else









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






455




					propagate(new PathEdge<>(d1, m, d3.getFact()), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






456




457




458




459




			}
		}
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






460




461




462




463




464




	private D applyConstraint(Constraint<FieldRef> constraint, D fact) {
		if(fact.equals(zeroValue))
			return zeroValue;
		else
			return fact.cloneWithAccessPath(constraint.applyToAccessPath(fact.getAccessPath()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






465




	}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






466




	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






467




468




469




470




471




	private boolean propagateConstrained(PathEdge<N, D> pathEdge) {
		return propagateConstrained(pathEdge, new HashMap<N, Boolean>());
	}
	
	private boolean propagateConstrained(PathEdge<N, D> pathEdge, Map<N, Boolean> visited) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






472




		M calleeMethod = icfg.getMethodOf(pathEdge.getTarget());









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






473




		logger.trace("Checking interest at method {} in fact {}", calleeMethod, pathEdge.factAtSource());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






474














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






475




476




477




		boolean propagate = false;
		if(pathEdge.factAtSource().equals(zeroValue))
			propagate = true;









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






478




479




		else if(hasPausedEdges(calleeMethod, pathEdge))
			propagate = false;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






480




481




		else {
			Set<N> callSitesWithInterest = Sets.newHashSet();









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






482




			for(IncomingEdge<D, N> incEdge : incomingEdgesPrefixedWith(calleeMethod, pathEdge.factAtSource())) { //guaranteed









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






483




				callSitesWithInterest.add(incEdge.getCallSite());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




			}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






485




486




			propagate = !callSitesWithInterest.isEmpty();
			









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






487




			for(IncomingEdge<D, N> incEdge : incomingEdgesPotentialPrefixesOf(calleeMethod, pathEdge.factAtSource())) { //potential









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






488




489




490




491




492




				if(visited.containsKey(incEdge.getCallSite())) {
					if(visited.get(incEdge.getCallSite()) != null)
						propagate |= visited.get(incEdge.getCallSite());
				}
				else {









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






493




					if(!callSitesWithInterest.contains(incEdge.getCallSite())) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






494




						Constraint<FieldRef> callerConstraint = new DeltaConstraint<FieldRef>(incEdge.getCalleeSourceFact().getAccessPath(), pathEdge.factAtSource().getAccessPath());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






495




						









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






496




497




498




499




500




						PathEdge<N,D> callerEdge = new ConcretizationPathEdge<>(
								applyConstraint(callerConstraint, incEdge.getCallerSourceFact()), 
								incEdge.getCallSite(), 
								applyConstraint(callerConstraint, incEdge.getCallerCallSiteFact()),
								calleeMethod,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






501




								applyConstraint(callerConstraint, incEdge.getCalleeSourceFact()));









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






502




503




504




505




506




						visited.put(incEdge.getCallSite(), null);
						boolean result = propagateConstrained(callerEdge, visited);
						visited.put(incEdge.getCallSite(), result);
						propagate |= result;
					}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






507




				}









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






508




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






509




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






510




		









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






511




		if(propagate) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






512




			propagate(pathEdge, pathEdge instanceof ConcretizationPathEdge ? pathEdge.getTarget() : null, false);









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






513




514




515




516




517




518




			return true;
		} else {
			pauseEdge(pathEdge);
			return false;
		}
	}









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






519




520




521




522




523





	private boolean hasPausedEdges(M calleeMethod, PathEdge<N, D> pathEdge) {
		ConcurrentHashSet<PathEdge<N, D>> pe = pausedEdges.get(calleeMethod);
		if(pe != null) {
			for(PathEdge<N, D> edge : pe) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






524




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), pathEdge.factAtSource()) == PrefixTestResult.GUARANTEED_PREFIX)









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






525




526




527




528




529




530




					return true;
			}
		}
		return false;
	}










cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






531




532




	private void pauseEdge(PathEdge<N,D> edge) {
		M method = icfg.getMethodOf(edge.getTarget());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






533




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






534




535




536




		if(edges.add(edge)) {
			logger.trace("PAUSED: {}: {}", method, edge);
		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






537




538




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






539




540




541




542




543




544




545




546




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






547




	protected Set<ConstrainedFact<FieldRef, D>> computeNormalFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






548




			(FlowFunction<FieldRef, D> flowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






549




550




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




567




568




569




570




571




		return flowFunction.computeTargets(d2);
	}
	
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
		d5.setCallingContext(d4);
		return d5;
	}
	
	
	/**
	 * Propagates the flow further down the exploded super graph. 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






572




	 * @param edge the PathEdge that should be propagated









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






573




574




575




576




577




	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






578




	protected void propagate(PathEdge<N,D> edge,









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






579




580




			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






581




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






582




		final D existingVal = jumpFn.addFunction(edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






583




584




585




586




587




588




589




590




591




592




593




594




595




596




597




598




599




600




601




602




603




		
		if(edge instanceof ConcretizationPathEdge) {
			ConcretizationPathEdge<M, N, D> concEdge = (ConcretizationPathEdge<M,N,D>) edge;
			IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(concEdge.getCalleeSourceFact(), 
					concEdge.getTarget(), concEdge.factAtSource(), concEdge.factAtTarget());
			if (!addIncoming(concEdge.getCalleeMethod(), incomingEdge))
				return;
			
			resumeEdges(concEdge.getCalleeMethod(), concEdge.getCalleeSourceFact());
			registerInterestedCaller(concEdge.getCalleeMethod(), incomingEdge);
		} else {
			//TODO: Merge d.* and d.*\{x} as d.*
			if (existingVal != null) {
				if (existingVal != edge.factAtTarget())
					existingVal.addNeighbor(edge.factAtTarget());
			}
			else {
				scheduleEdgeProcessing(edge);
				if(edge.factAtTarget()!=zeroValue)
					logger.trace("EDGE: {}: {}", icfg.getMethodOf(edge.getTarget()), edge);
			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






604




605




606




		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






607




608




609




610




611




612




613




614




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






615




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) == PrefixTestResult.GUARANTEED_PREFIX;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






616




617




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




619




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






620




621




622




623




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






624




625




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






626




627




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






628




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






629




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






630




631




632




633




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






634




635




636




637




638




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






639




				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






640




641




642




643




			}
		});
	}
	









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






644




	protected Set<IncomingEdge<D, N>> incomingEdgesPotentialPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






645




646




647




648




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






649




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact).atLeast(PrefixTestResult.POTENTIAL_PREFIX);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






650




651




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






652




653




	}
	









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






654




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






655




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






656




657




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




688




689




690




691




692




693




694




695




696




697




698




699




700




701




702




703




704




705




706




707




708




	}
	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "FAST IFDS SOLVER";
	}

	public void printStats() {
		if(logger.isDebugEnabled()) {
			if(ffCache!=null)
				ffCache.printStats();
		} else {
			logger.info("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {
		private final PathEdge<N,D> edge;

		public PathEdgeProcessingTask(PathEdge<N,D> edge) {
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

1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c

















1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



27.2 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






1




/*******************************************************************************









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






2




 * Copyright (c) 2014 Johannes Lerch, Johannes Späth.









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






9




 *     Johannes Lerch, Johannes Späth - initial API and implementation









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






10




11




12




 ******************************************************************************/
package heros.alias;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






13




14




15




16




import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






17




import heros.alias.AccessPath.PrefixTestResult;









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






18




import heros.alias.FlowFunction.ConstrainedFact;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






19




import heros.alias.FlowFunction.Constraint;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






20




21




22




23




import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




import java.util.Collection;
import java.util.Collections;









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






26




import java.util.HashMap;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






27




import java.util.LinkedList;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






28




29




import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






30




import java.util.Set;









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




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






37




import com.google.common.base.Optional;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






38




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






39




import com.google.common.cache.CacheBuilder;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






40




import com.google.common.collect.Lists;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






41




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






42














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






43




public class FieldSensitiveIFDSSolver<N, BaseValue, FieldRef, D extends FieldSensitiveFact<BaseValue, FieldRef, D>, M, I extends InterproceduralCFG<N, M>> {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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






	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel
			(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	
    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);

    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace
    public static final boolean DEBUG = logger.isDebugEnabled();

	protected CountingThreadPoolExecutor executor;
	
	@DontSynchronize("only used by single thread")
	protected int numThreads;
	
	@SynchronizedBy("thread safe data structure, consistent locking when used")
	protected final JumpFunctions<N,D> jumpFn;
	
	@SynchronizedBy("thread safe data structure, only modified internally")
	protected final I icfg;
	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






68




69




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






70




71




72




73




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






74




75




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






76




	









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






77




	protected final MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>>();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






78




	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






79




	@DontSynchronize("stateless")









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






80




	protected final FlowFunctions<N, FieldRef, D, M> flowFunctions;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	
	@DontSynchronize("only used by single thread")
	protected final Map<N,Set<D>> initialSeeds;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("stateless")
	protected final D zeroValue;
	
	@DontSynchronize("readOnly")
	protected final FlowFunctionCache<N,D,M> ffCache = null; 
	
	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






96




97





	private LinkedList<Runnable> worklist;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




100




101




102




103




	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






104




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef, D,M,I> tabulationProblem) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




108




109




110




111




112




113




		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






114




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




117




118




119




120




		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






121




		FlowFunctions<N, FieldRef, D, M> flowFunctions = tabulationProblem.flowFunctions(); 









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		/*if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}*/
		this.flowFunctions = flowFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.jumpFn = new JumpFunctions<N,D>();
		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






132




		this.numThreads = 1; //Math.max(1,tabulationProblem.numThreads()); //solution is in the current state not thread safe









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






133




		this.executor = getExecutor();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






134




		this.worklist = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






135




136




137




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




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */
	public void solve() {		
		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.
	 */
	protected void submitInitialSeeds() {
		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue())









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






154




				propagate(new PathEdge<>(zeroValue, startPoint, val), null, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




178




179




180




181




182




183




			jumpFn.addFunction(new PathEdge<N, D>(zeroValue, startPoint, zeroValue));
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{
			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();
		}
		if(logger.isDebugEnabled())
			printStats();

		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway
		executor.shutdown();
		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed
		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






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




194




//		try {
//			executor.awaitCompletion();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Throwable exception = executor.getException();
//		if(exception!=null) {
//			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
//		}
		while(!worklist.isEmpty()) {
			worklist.pop().run();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






195




196




197




198




199




200




201




202




203




204




		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






205




206




207




208




//    	if (executor.isTerminating())
//    		return;
//    	executor.execute(new PathEdgeProcessingTask(edge));
    	worklist.add(new PathEdgeProcessingTask(edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






209




210




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




229




230




231




232




233




    	propagationCount++;
    }
	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 
	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 
	 * 
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...

        logger.trace("Processing call to {}", n);

		final D d2 = edge.factAtTarget();
		assert d2 != null;
		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		
		//for each possible callee
		Collection<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			//compute the call-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






234




			FlowFunction<FieldRef, D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






235




			Set<ConstrainedFact<FieldRef, D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






236




237




238




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






239




			for(ConstrainedFact<FieldRef, D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




242




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









avoiding unnecessary calls to clone a fact


 

 


Johannes Lerch
committed
Jan 08, 2015






243




					D abstractStartPointFact = AccessPathUtil.cloneWithAccessPath(d3.getFact(), new AccessPath<FieldRef>());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






244




					propagate(new PathEdge<>(abstractStartPointFact, sP, abstractStartPointFact), n, false); //line 15









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






245




246




247




248




				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






249




250




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






251




252




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






253




				resumeEdges(sCalledProcN, d3.getFact());









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






254




255




				registerInterestedCaller(sCalledProcN, incomingEdge);
				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






256




257




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






258




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






259




260




261




262




263




264




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






265




					for(SummaryEdge<D, N> summary: endSumm) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






266




267




268




269




270




271




272




273




274




275




						Optional<D> d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
						if(d4.isPresent()) {
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(ConstrainedFact<FieldRef, D> d5: computeReturnFlowFunction(retFunction, d4.get(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(new PathEdge<>(d1, retSiteN, d5p_restoredCtx), n, false);









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






276




277




								}
							}









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






278




						}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






279




280




281




282




283




284




					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






285




			FlowFunction<FieldRef, D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






286




			for(ConstrainedFact<FieldRef, D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






287




				propagate(new PathEdge<>(d1, returnSiteN, d3.getFact()), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






288




289




290




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






291




292




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






293




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.get(method);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






294




295




		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






296




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) == PrefixTestResult.GUARANTEED_PREFIX) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






297




298




					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






299




						propagate(edge, edge instanceof ConcretizationPathEdge ? edge.getTarget() : null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






300




301




302




303




304




					}
				}
			}
		}
	}









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






305




306




307




308




	
	private void registerInterestedCaller(M method, IncomingEdge<D, N> incomingEdge) {
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






309




			for(final PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






310




				if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), edge.factAtSource()).atLeast(PrefixTestResult.POTENTIAL_PREFIX)) {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






311




312




					logger.trace("RECHECKING-PAUSED-EDGE: {} for new incoming edge {}", edge, incomingEdge);
					









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






313




314




					Constraint<FieldRef> constraint = new DeltaConstraint<FieldRef>(incomingEdge.getCalleeSourceFact().getAccessPath(), edge.factAtSource().getAccessPath());
					propagateConstrained(new ConcretizationPathEdge<>(









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






315




316




							applyConstraint(constraint, incomingEdge.getCallerSourceFact()), 
							incomingEdge.getCallSite(), 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






317




318




							applyConstraint(constraint, incomingEdge.getCallerCallSiteFact()),
							method,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






319




							applyConstraint(constraint, incomingEdge.getCalleeSourceFact())));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






320




321




322




323




				}
			}
		}
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






324














FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






325




326




327




328




329




330




331




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






332




	protected Set<ConstrainedFact<FieldRef, D>> computeCallFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






333




			(FlowFunction<FieldRef, D> callFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		return callFlowFunction.computeTargets(d2);
	}

	/**
	 * Computes the call-to-return flow function for the given call-site
	 * abstraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






346




	protected Set<ConstrainedFact<FieldRef, D>> computeCallToReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






347




			(FlowFunction<FieldRef, D> callToReturnFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






348




349




350




351




352




353




354




355




356




357




358




359




360




361




362




363




364




365




366




367




368




369




370




		return callToReturnFlowFunction.computeTargets(d2);
	}
	
	/**
	 * Lines 21-32 of the algorithm.
	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits
	 */
	protected void processExit(PathEdge<N,D> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		//for each of the method's start points, determine incoming calls
		
		//line 21.1 of Naeem/Lhotak/Rodriguez
		//register end-summary









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






371




372




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






373




			return; //FIXME: should never be reached?! -> assert ?









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






374




375




376




		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






377




378




379




380




381




382




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






383




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




				









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






385




				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






386




387




					Optional<D> concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);
					if(concreteCalleeExitFact.isPresent()) {









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






388




						Set<ConstrainedFact<FieldRef, D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact.get(), callSite);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






389




390




	
						// for each incoming-call value









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






391




						for (ConstrainedFact<FieldRef, D> callerTargetAnnotatedFact : callerTargetFacts) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






392




							D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






393




							propagate(new PathEdge<>(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact), callSite, false);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






394




						}









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






395




					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






396




397




				}
			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






398




399




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






400




401




402




403




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









bug fix in unbalanced return handling


 

 


Johannes Lerch
committed
Jan 29, 2015






404




		if(followReturnsPastSeeds && d1 == zeroValue) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






405




406




407




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






408




					FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






409




410




					Set<ConstrainedFact<FieldRef, D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(ConstrainedFact<FieldRef, D> d5: targets)









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






411




						propagate(new PathEdge<>(zeroValue, retSiteC, d5.getFact()), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






412




413




414




415




416




417




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






418




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




431




				retFunction.computeTargets(d2);
			}
		}
	}
	
	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callSite The call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






432




	protected Set<ConstrainedFact<FieldRef, D>> computeReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






433




			(FlowFunction<FieldRef, D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






434




435




436




437




438




439




440




441




442




443




444




445




446




447




		return retFunction.computeTargets(d2);
	}

	/**
	 * Lines 33-37 of the algorithm.
	 * Simply propagate normal, intra-procedural flows.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		
		for (N m : icfg.getSuccsOf(n)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






448




			FlowFunction<FieldRef, D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






449




450




			Set<ConstrainedFact<FieldRef, D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (ConstrainedFact<FieldRef, D> d3 : res) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






451




				if(d3.getConstraint() != null) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






452




					propagateConstrained(new PathEdge<>(applyConstraint(d3.getConstraint(), d1), m, d3.getFact()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






453




				}









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






454




				else









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






455




					propagate(new PathEdge<>(d1, m, d3.getFact()), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






456




457




458




459




			}
		}
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






460




461




462




463




464




	private D applyConstraint(Constraint<FieldRef> constraint, D fact) {
		if(fact.equals(zeroValue))
			return zeroValue;
		else
			return fact.cloneWithAccessPath(constraint.applyToAccessPath(fact.getAccessPath()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






465




	}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






466




	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






467




468




469




470




471




	private boolean propagateConstrained(PathEdge<N, D> pathEdge) {
		return propagateConstrained(pathEdge, new HashMap<N, Boolean>());
	}
	
	private boolean propagateConstrained(PathEdge<N, D> pathEdge, Map<N, Boolean> visited) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






472




		M calleeMethod = icfg.getMethodOf(pathEdge.getTarget());









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






473




		logger.trace("Checking interest at method {} in fact {}", calleeMethod, pathEdge.factAtSource());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






474














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






475




476




477




		boolean propagate = false;
		if(pathEdge.factAtSource().equals(zeroValue))
			propagate = true;









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






478




479




		else if(hasPausedEdges(calleeMethod, pathEdge))
			propagate = false;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






480




481




		else {
			Set<N> callSitesWithInterest = Sets.newHashSet();









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






482




			for(IncomingEdge<D, N> incEdge : incomingEdgesPrefixedWith(calleeMethod, pathEdge.factAtSource())) { //guaranteed









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






483




				callSitesWithInterest.add(incEdge.getCallSite());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




			}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






485




486




			propagate = !callSitesWithInterest.isEmpty();
			









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






487




			for(IncomingEdge<D, N> incEdge : incomingEdgesPotentialPrefixesOf(calleeMethod, pathEdge.factAtSource())) { //potential









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






488




489




490




491




492




				if(visited.containsKey(incEdge.getCallSite())) {
					if(visited.get(incEdge.getCallSite()) != null)
						propagate |= visited.get(incEdge.getCallSite());
				}
				else {









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






493




					if(!callSitesWithInterest.contains(incEdge.getCallSite())) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






494




						Constraint<FieldRef> callerConstraint = new DeltaConstraint<FieldRef>(incEdge.getCalleeSourceFact().getAccessPath(), pathEdge.factAtSource().getAccessPath());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






495




						









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






496




497




498




499




500




						PathEdge<N,D> callerEdge = new ConcretizationPathEdge<>(
								applyConstraint(callerConstraint, incEdge.getCallerSourceFact()), 
								incEdge.getCallSite(), 
								applyConstraint(callerConstraint, incEdge.getCallerCallSiteFact()),
								calleeMethod,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






501




								applyConstraint(callerConstraint, incEdge.getCalleeSourceFact()));









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






502




503




504




505




506




						visited.put(incEdge.getCallSite(), null);
						boolean result = propagateConstrained(callerEdge, visited);
						visited.put(incEdge.getCallSite(), result);
						propagate |= result;
					}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






507




				}









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






508




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






509




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






510




		









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






511




		if(propagate) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






512




			propagate(pathEdge, pathEdge instanceof ConcretizationPathEdge ? pathEdge.getTarget() : null, false);









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






513




514




515




516




517




518




			return true;
		} else {
			pauseEdge(pathEdge);
			return false;
		}
	}









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






519




520




521




522




523





	private boolean hasPausedEdges(M calleeMethod, PathEdge<N, D> pathEdge) {
		ConcurrentHashSet<PathEdge<N, D>> pe = pausedEdges.get(calleeMethod);
		if(pe != null) {
			for(PathEdge<N, D> edge : pe) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






524




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), pathEdge.factAtSource()) == PrefixTestResult.GUARANTEED_PREFIX)









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






525




526




527




528




529




530




					return true;
			}
		}
		return false;
	}










cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






531




532




	private void pauseEdge(PathEdge<N,D> edge) {
		M method = icfg.getMethodOf(edge.getTarget());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






533




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






534




535




536




		if(edges.add(edge)) {
			logger.trace("PAUSED: {}: {}", method, edge);
		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






537




538




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






539




540




541




542




543




544




545




546




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






547




	protected Set<ConstrainedFact<FieldRef, D>> computeNormalFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






548




			(FlowFunction<FieldRef, D> flowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






549




550




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




567




568




569




570




571




		return flowFunction.computeTargets(d2);
	}
	
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
		d5.setCallingContext(d4);
		return d5;
	}
	
	
	/**
	 * Propagates the flow further down the exploded super graph. 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






572




	 * @param edge the PathEdge that should be propagated









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






573




574




575




576




577




	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






578




	protected void propagate(PathEdge<N,D> edge,









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






579




580




			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






581




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






582




		final D existingVal = jumpFn.addFunction(edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






583




584




585




586




587




588




589




590




591




592




593




594




595




596




597




598




599




600




601




602




603




		
		if(edge instanceof ConcretizationPathEdge) {
			ConcretizationPathEdge<M, N, D> concEdge = (ConcretizationPathEdge<M,N,D>) edge;
			IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(concEdge.getCalleeSourceFact(), 
					concEdge.getTarget(), concEdge.factAtSource(), concEdge.factAtTarget());
			if (!addIncoming(concEdge.getCalleeMethod(), incomingEdge))
				return;
			
			resumeEdges(concEdge.getCalleeMethod(), concEdge.getCalleeSourceFact());
			registerInterestedCaller(concEdge.getCalleeMethod(), incomingEdge);
		} else {
			//TODO: Merge d.* and d.*\{x} as d.*
			if (existingVal != null) {
				if (existingVal != edge.factAtTarget())
					existingVal.addNeighbor(edge.factAtTarget());
			}
			else {
				scheduleEdgeProcessing(edge);
				if(edge.factAtTarget()!=zeroValue)
					logger.trace("EDGE: {}: {}", icfg.getMethodOf(edge.getTarget()), edge);
			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






604




605




606




		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






607




608




609




610




611




612




613




614




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






615




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) == PrefixTestResult.GUARANTEED_PREFIX;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






616




617




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




619




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






620




621




622




623




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






624




625




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






626




627




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






628




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






629




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






630




631




632




633




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






634




635




636




637




638




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






639




				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






640




641




642




643




			}
		});
	}
	









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






644




	protected Set<IncomingEdge<D, N>> incomingEdgesPotentialPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






645




646




647




648




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






649




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact).atLeast(PrefixTestResult.POTENTIAL_PREFIX);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






650




651




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






652




653




	}
	









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






654




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






655




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






656




657




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




688




689




690




691




692




693




694




695




696




697




698




699




700




701




702




703




704




705




706




707




708




	}
	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "FAST IFDS SOLVER";
	}

	public void printStats() {
		if(logger.isDebugEnabled()) {
			if(ffCache!=null)
				ffCache.printStats();
		} else {
			logger.info("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {
		private final PathEdge<N,D> edge;

		public PathEdgeProcessingTask(PathEdge<N,D> edge) {
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
	

}











Open sidebar



Joshua Garcia heros

1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c







Open sidebar



Joshua Garcia heros

1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c




Open sidebar

Joshua Garcia heros

1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Joshua Garciaherosheros
1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c










1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



27.2 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






1




/*******************************************************************************









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






2




 * Copyright (c) 2014 Johannes Lerch, Johannes Späth.









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






9




 *     Johannes Lerch, Johannes Späth - initial API and implementation









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






10




11




12




 ******************************************************************************/
package heros.alias;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






13




14




15




16




import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






17




import heros.alias.AccessPath.PrefixTestResult;









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






18




import heros.alias.FlowFunction.ConstrainedFact;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






19




import heros.alias.FlowFunction.Constraint;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






20




21




22




23




import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




import java.util.Collection;
import java.util.Collections;









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






26




import java.util.HashMap;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






27




import java.util.LinkedList;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






28




29




import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






30




import java.util.Set;









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




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






37




import com.google.common.base.Optional;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






38




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






39




import com.google.common.cache.CacheBuilder;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






40




import com.google.common.collect.Lists;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






41




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






42














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






43




public class FieldSensitiveIFDSSolver<N, BaseValue, FieldRef, D extends FieldSensitiveFact<BaseValue, FieldRef, D>, M, I extends InterproceduralCFG<N, M>> {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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






	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel
			(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	
    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);

    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace
    public static final boolean DEBUG = logger.isDebugEnabled();

	protected CountingThreadPoolExecutor executor;
	
	@DontSynchronize("only used by single thread")
	protected int numThreads;
	
	@SynchronizedBy("thread safe data structure, consistent locking when used")
	protected final JumpFunctions<N,D> jumpFn;
	
	@SynchronizedBy("thread safe data structure, only modified internally")
	protected final I icfg;
	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






68




69




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






70




71




72




73




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






74




75




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






76




	









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






77




	protected final MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>>();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






78




	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






79




	@DontSynchronize("stateless")









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






80




	protected final FlowFunctions<N, FieldRef, D, M> flowFunctions;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	
	@DontSynchronize("only used by single thread")
	protected final Map<N,Set<D>> initialSeeds;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("stateless")
	protected final D zeroValue;
	
	@DontSynchronize("readOnly")
	protected final FlowFunctionCache<N,D,M> ffCache = null; 
	
	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






96




97





	private LinkedList<Runnable> worklist;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




100




101




102




103




	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






104




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef, D,M,I> tabulationProblem) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




108




109




110




111




112




113




		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






114




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




117




118




119




120




		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






121




		FlowFunctions<N, FieldRef, D, M> flowFunctions = tabulationProblem.flowFunctions(); 









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		/*if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}*/
		this.flowFunctions = flowFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.jumpFn = new JumpFunctions<N,D>();
		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






132




		this.numThreads = 1; //Math.max(1,tabulationProblem.numThreads()); //solution is in the current state not thread safe









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






133




		this.executor = getExecutor();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






134




		this.worklist = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






135




136




137




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




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */
	public void solve() {		
		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.
	 */
	protected void submitInitialSeeds() {
		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue())









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






154




				propagate(new PathEdge<>(zeroValue, startPoint, val), null, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




178




179




180




181




182




183




			jumpFn.addFunction(new PathEdge<N, D>(zeroValue, startPoint, zeroValue));
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{
			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();
		}
		if(logger.isDebugEnabled())
			printStats();

		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway
		executor.shutdown();
		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed
		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






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




194




//		try {
//			executor.awaitCompletion();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Throwable exception = executor.getException();
//		if(exception!=null) {
//			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
//		}
		while(!worklist.isEmpty()) {
			worklist.pop().run();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






195




196




197




198




199




200




201




202




203




204




		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






205




206




207




208




//    	if (executor.isTerminating())
//    		return;
//    	executor.execute(new PathEdgeProcessingTask(edge));
    	worklist.add(new PathEdgeProcessingTask(edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






209




210




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




229




230




231




232




233




    	propagationCount++;
    }
	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 
	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 
	 * 
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...

        logger.trace("Processing call to {}", n);

		final D d2 = edge.factAtTarget();
		assert d2 != null;
		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		
		//for each possible callee
		Collection<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			//compute the call-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






234




			FlowFunction<FieldRef, D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






235




			Set<ConstrainedFact<FieldRef, D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






236




237




238




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






239




			for(ConstrainedFact<FieldRef, D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




242




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









avoiding unnecessary calls to clone a fact


 

 


Johannes Lerch
committed
Jan 08, 2015






243




					D abstractStartPointFact = AccessPathUtil.cloneWithAccessPath(d3.getFact(), new AccessPath<FieldRef>());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






244




					propagate(new PathEdge<>(abstractStartPointFact, sP, abstractStartPointFact), n, false); //line 15









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






245




246




247




248




				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






249




250




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






251




252




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






253




				resumeEdges(sCalledProcN, d3.getFact());









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






254




255




				registerInterestedCaller(sCalledProcN, incomingEdge);
				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






256




257




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






258




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






259




260




261




262




263




264




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






265




					for(SummaryEdge<D, N> summary: endSumm) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






266




267




268




269




270




271




272




273




274




275




						Optional<D> d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
						if(d4.isPresent()) {
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(ConstrainedFact<FieldRef, D> d5: computeReturnFlowFunction(retFunction, d4.get(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(new PathEdge<>(d1, retSiteN, d5p_restoredCtx), n, false);









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






276




277




								}
							}









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






278




						}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






279




280




281




282




283




284




					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






285




			FlowFunction<FieldRef, D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






286




			for(ConstrainedFact<FieldRef, D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






287




				propagate(new PathEdge<>(d1, returnSiteN, d3.getFact()), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






288




289




290




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






291




292




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






293




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.get(method);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






294




295




		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






296




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) == PrefixTestResult.GUARANTEED_PREFIX) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






297




298




					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






299




						propagate(edge, edge instanceof ConcretizationPathEdge ? edge.getTarget() : null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






300




301




302




303




304




					}
				}
			}
		}
	}









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






305




306




307




308




	
	private void registerInterestedCaller(M method, IncomingEdge<D, N> incomingEdge) {
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






309




			for(final PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






310




				if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), edge.factAtSource()).atLeast(PrefixTestResult.POTENTIAL_PREFIX)) {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






311




312




					logger.trace("RECHECKING-PAUSED-EDGE: {} for new incoming edge {}", edge, incomingEdge);
					









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






313




314




					Constraint<FieldRef> constraint = new DeltaConstraint<FieldRef>(incomingEdge.getCalleeSourceFact().getAccessPath(), edge.factAtSource().getAccessPath());
					propagateConstrained(new ConcretizationPathEdge<>(









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






315




316




							applyConstraint(constraint, incomingEdge.getCallerSourceFact()), 
							incomingEdge.getCallSite(), 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






317




318




							applyConstraint(constraint, incomingEdge.getCallerCallSiteFact()),
							method,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






319




							applyConstraint(constraint, incomingEdge.getCalleeSourceFact())));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






320




321




322




323




				}
			}
		}
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






324














FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






325




326




327




328




329




330




331




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






332




	protected Set<ConstrainedFact<FieldRef, D>> computeCallFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






333




			(FlowFunction<FieldRef, D> callFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		return callFlowFunction.computeTargets(d2);
	}

	/**
	 * Computes the call-to-return flow function for the given call-site
	 * abstraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






346




	protected Set<ConstrainedFact<FieldRef, D>> computeCallToReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






347




			(FlowFunction<FieldRef, D> callToReturnFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






348




349




350




351




352




353




354




355




356




357




358




359




360




361




362




363




364




365




366




367




368




369




370




		return callToReturnFlowFunction.computeTargets(d2);
	}
	
	/**
	 * Lines 21-32 of the algorithm.
	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits
	 */
	protected void processExit(PathEdge<N,D> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		//for each of the method's start points, determine incoming calls
		
		//line 21.1 of Naeem/Lhotak/Rodriguez
		//register end-summary









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






371




372




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






373




			return; //FIXME: should never be reached?! -> assert ?









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






374




375




376




		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






377




378




379




380




381




382




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






383




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




				









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






385




				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






386




387




					Optional<D> concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);
					if(concreteCalleeExitFact.isPresent()) {









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






388




						Set<ConstrainedFact<FieldRef, D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact.get(), callSite);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






389




390




	
						// for each incoming-call value









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






391




						for (ConstrainedFact<FieldRef, D> callerTargetAnnotatedFact : callerTargetFacts) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






392




							D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






393




							propagate(new PathEdge<>(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact), callSite, false);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






394




						}









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






395




					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






396




397




				}
			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






398




399




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






400




401




402




403




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









bug fix in unbalanced return handling


 

 


Johannes Lerch
committed
Jan 29, 2015






404




		if(followReturnsPastSeeds && d1 == zeroValue) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






405




406




407




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






408




					FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






409




410




					Set<ConstrainedFact<FieldRef, D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(ConstrainedFact<FieldRef, D> d5: targets)









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






411




						propagate(new PathEdge<>(zeroValue, retSiteC, d5.getFact()), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






412




413




414




415




416




417




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






418




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




431




				retFunction.computeTargets(d2);
			}
		}
	}
	
	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callSite The call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






432




	protected Set<ConstrainedFact<FieldRef, D>> computeReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






433




			(FlowFunction<FieldRef, D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






434




435




436




437




438




439




440




441




442




443




444




445




446




447




		return retFunction.computeTargets(d2);
	}

	/**
	 * Lines 33-37 of the algorithm.
	 * Simply propagate normal, intra-procedural flows.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		
		for (N m : icfg.getSuccsOf(n)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






448




			FlowFunction<FieldRef, D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






449




450




			Set<ConstrainedFact<FieldRef, D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (ConstrainedFact<FieldRef, D> d3 : res) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






451




				if(d3.getConstraint() != null) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






452




					propagateConstrained(new PathEdge<>(applyConstraint(d3.getConstraint(), d1), m, d3.getFact()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






453




				}









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






454




				else









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






455




					propagate(new PathEdge<>(d1, m, d3.getFact()), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






456




457




458




459




			}
		}
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






460




461




462




463




464




	private D applyConstraint(Constraint<FieldRef> constraint, D fact) {
		if(fact.equals(zeroValue))
			return zeroValue;
		else
			return fact.cloneWithAccessPath(constraint.applyToAccessPath(fact.getAccessPath()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






465




	}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






466




	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






467




468




469




470




471




	private boolean propagateConstrained(PathEdge<N, D> pathEdge) {
		return propagateConstrained(pathEdge, new HashMap<N, Boolean>());
	}
	
	private boolean propagateConstrained(PathEdge<N, D> pathEdge, Map<N, Boolean> visited) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






472




		M calleeMethod = icfg.getMethodOf(pathEdge.getTarget());









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






473




		logger.trace("Checking interest at method {} in fact {}", calleeMethod, pathEdge.factAtSource());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






474














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






475




476




477




		boolean propagate = false;
		if(pathEdge.factAtSource().equals(zeroValue))
			propagate = true;









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






478




479




		else if(hasPausedEdges(calleeMethod, pathEdge))
			propagate = false;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






480




481




		else {
			Set<N> callSitesWithInterest = Sets.newHashSet();









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






482




			for(IncomingEdge<D, N> incEdge : incomingEdgesPrefixedWith(calleeMethod, pathEdge.factAtSource())) { //guaranteed









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






483




				callSitesWithInterest.add(incEdge.getCallSite());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




			}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






485




486




			propagate = !callSitesWithInterest.isEmpty();
			









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






487




			for(IncomingEdge<D, N> incEdge : incomingEdgesPotentialPrefixesOf(calleeMethod, pathEdge.factAtSource())) { //potential









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






488




489




490




491




492




				if(visited.containsKey(incEdge.getCallSite())) {
					if(visited.get(incEdge.getCallSite()) != null)
						propagate |= visited.get(incEdge.getCallSite());
				}
				else {









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






493




					if(!callSitesWithInterest.contains(incEdge.getCallSite())) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






494




						Constraint<FieldRef> callerConstraint = new DeltaConstraint<FieldRef>(incEdge.getCalleeSourceFact().getAccessPath(), pathEdge.factAtSource().getAccessPath());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






495




						









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






496




497




498




499




500




						PathEdge<N,D> callerEdge = new ConcretizationPathEdge<>(
								applyConstraint(callerConstraint, incEdge.getCallerSourceFact()), 
								incEdge.getCallSite(), 
								applyConstraint(callerConstraint, incEdge.getCallerCallSiteFact()),
								calleeMethod,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






501




								applyConstraint(callerConstraint, incEdge.getCalleeSourceFact()));









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






502




503




504




505




506




						visited.put(incEdge.getCallSite(), null);
						boolean result = propagateConstrained(callerEdge, visited);
						visited.put(incEdge.getCallSite(), result);
						propagate |= result;
					}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






507




				}









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






508




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






509




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






510




		









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






511




		if(propagate) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






512




			propagate(pathEdge, pathEdge instanceof ConcretizationPathEdge ? pathEdge.getTarget() : null, false);









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






513




514




515




516




517




518




			return true;
		} else {
			pauseEdge(pathEdge);
			return false;
		}
	}









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






519




520




521




522




523





	private boolean hasPausedEdges(M calleeMethod, PathEdge<N, D> pathEdge) {
		ConcurrentHashSet<PathEdge<N, D>> pe = pausedEdges.get(calleeMethod);
		if(pe != null) {
			for(PathEdge<N, D> edge : pe) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






524




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), pathEdge.factAtSource()) == PrefixTestResult.GUARANTEED_PREFIX)









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






525




526




527




528




529




530




					return true;
			}
		}
		return false;
	}










cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






531




532




	private void pauseEdge(PathEdge<N,D> edge) {
		M method = icfg.getMethodOf(edge.getTarget());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






533




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






534




535




536




		if(edges.add(edge)) {
			logger.trace("PAUSED: {}: {}", method, edge);
		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






537




538




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






539




540




541




542




543




544




545




546




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






547




	protected Set<ConstrainedFact<FieldRef, D>> computeNormalFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






548




			(FlowFunction<FieldRef, D> flowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






549




550




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




567




568




569




570




571




		return flowFunction.computeTargets(d2);
	}
	
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
		d5.setCallingContext(d4);
		return d5;
	}
	
	
	/**
	 * Propagates the flow further down the exploded super graph. 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






572




	 * @param edge the PathEdge that should be propagated









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






573




574




575




576




577




	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






578




	protected void propagate(PathEdge<N,D> edge,









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






579




580




			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






581




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






582




		final D existingVal = jumpFn.addFunction(edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






583




584




585




586




587




588




589




590




591




592




593




594




595




596




597




598




599




600




601




602




603




		
		if(edge instanceof ConcretizationPathEdge) {
			ConcretizationPathEdge<M, N, D> concEdge = (ConcretizationPathEdge<M,N,D>) edge;
			IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(concEdge.getCalleeSourceFact(), 
					concEdge.getTarget(), concEdge.factAtSource(), concEdge.factAtTarget());
			if (!addIncoming(concEdge.getCalleeMethod(), incomingEdge))
				return;
			
			resumeEdges(concEdge.getCalleeMethod(), concEdge.getCalleeSourceFact());
			registerInterestedCaller(concEdge.getCalleeMethod(), incomingEdge);
		} else {
			//TODO: Merge d.* and d.*\{x} as d.*
			if (existingVal != null) {
				if (existingVal != edge.factAtTarget())
					existingVal.addNeighbor(edge.factAtTarget());
			}
			else {
				scheduleEdgeProcessing(edge);
				if(edge.factAtTarget()!=zeroValue)
					logger.trace("EDGE: {}: {}", icfg.getMethodOf(edge.getTarget()), edge);
			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






604




605




606




		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






607




608




609




610




611




612




613




614




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






615




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) == PrefixTestResult.GUARANTEED_PREFIX;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






616




617




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




619




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






620




621




622




623




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






624




625




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






626




627




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






628




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






629




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






630




631




632




633




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






634




635




636




637




638




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






639




				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






640




641




642




643




			}
		});
	}
	









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






644




	protected Set<IncomingEdge<D, N>> incomingEdgesPotentialPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






645




646




647




648




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






649




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact).atLeast(PrefixTestResult.POTENTIAL_PREFIX);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






650




651




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






652




653




	}
	









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






654




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






655




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






656




657




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




688




689




690




691




692




693




694




695




696




697




698




699




700




701




702




703




704




705




706




707




708




	}
	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "FAST IFDS SOLVER";
	}

	public void printStats() {
		if(logger.isDebugEnabled()) {
			if(ffCache!=null)
				ffCache.printStats();
		} else {
			logger.info("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {
		private final PathEdge<N,D> edge;

		public PathEdgeProcessingTask(PathEdge<N,D> edge) {
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
	

}














1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



27.2 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






1




/*******************************************************************************









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






2




 * Copyright (c) 2014 Johannes Lerch, Johannes Späth.









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






9




 *     Johannes Lerch, Johannes Späth - initial API and implementation









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






10




11




12




 ******************************************************************************/
package heros.alias;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






13




14




15




16




import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






17




import heros.alias.AccessPath.PrefixTestResult;









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






18




import heros.alias.FlowFunction.ConstrainedFact;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






19




import heros.alias.FlowFunction.Constraint;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






20




21




22




23




import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




import java.util.Collection;
import java.util.Collections;









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






26




import java.util.HashMap;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






27




import java.util.LinkedList;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






28




29




import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






30




import java.util.Set;









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




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






37




import com.google.common.base.Optional;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






38




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






39




import com.google.common.cache.CacheBuilder;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






40




import com.google.common.collect.Lists;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






41




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






42














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






43




public class FieldSensitiveIFDSSolver<N, BaseValue, FieldRef, D extends FieldSensitiveFact<BaseValue, FieldRef, D>, M, I extends InterproceduralCFG<N, M>> {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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






	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel
			(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	
    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);

    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace
    public static final boolean DEBUG = logger.isDebugEnabled();

	protected CountingThreadPoolExecutor executor;
	
	@DontSynchronize("only used by single thread")
	protected int numThreads;
	
	@SynchronizedBy("thread safe data structure, consistent locking when used")
	protected final JumpFunctions<N,D> jumpFn;
	
	@SynchronizedBy("thread safe data structure, only modified internally")
	protected final I icfg;
	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






68




69




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






70




71




72




73




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






74




75




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






76




	









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






77




	protected final MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>>();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






78




	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






79




	@DontSynchronize("stateless")









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






80




	protected final FlowFunctions<N, FieldRef, D, M> flowFunctions;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	
	@DontSynchronize("only used by single thread")
	protected final Map<N,Set<D>> initialSeeds;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("stateless")
	protected final D zeroValue;
	
	@DontSynchronize("readOnly")
	protected final FlowFunctionCache<N,D,M> ffCache = null; 
	
	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






96




97





	private LinkedList<Runnable> worklist;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




100




101




102




103




	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






104




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef, D,M,I> tabulationProblem) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




108




109




110




111




112




113




		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






114




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




117




118




119




120




		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






121




		FlowFunctions<N, FieldRef, D, M> flowFunctions = tabulationProblem.flowFunctions(); 









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		/*if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}*/
		this.flowFunctions = flowFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.jumpFn = new JumpFunctions<N,D>();
		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






132




		this.numThreads = 1; //Math.max(1,tabulationProblem.numThreads()); //solution is in the current state not thread safe









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






133




		this.executor = getExecutor();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






134




		this.worklist = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






135




136




137




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




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */
	public void solve() {		
		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.
	 */
	protected void submitInitialSeeds() {
		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue())









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






154




				propagate(new PathEdge<>(zeroValue, startPoint, val), null, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




178




179




180




181




182




183




			jumpFn.addFunction(new PathEdge<N, D>(zeroValue, startPoint, zeroValue));
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{
			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();
		}
		if(logger.isDebugEnabled())
			printStats();

		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway
		executor.shutdown();
		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed
		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






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




194




//		try {
//			executor.awaitCompletion();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Throwable exception = executor.getException();
//		if(exception!=null) {
//			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
//		}
		while(!worklist.isEmpty()) {
			worklist.pop().run();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






195




196




197




198




199




200




201




202




203




204




		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






205




206




207




208




//    	if (executor.isTerminating())
//    		return;
//    	executor.execute(new PathEdgeProcessingTask(edge));
    	worklist.add(new PathEdgeProcessingTask(edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






209




210




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




229




230




231




232




233




    	propagationCount++;
    }
	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 
	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 
	 * 
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...

        logger.trace("Processing call to {}", n);

		final D d2 = edge.factAtTarget();
		assert d2 != null;
		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		
		//for each possible callee
		Collection<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			//compute the call-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






234




			FlowFunction<FieldRef, D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






235




			Set<ConstrainedFact<FieldRef, D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






236




237




238




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






239




			for(ConstrainedFact<FieldRef, D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




242




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









avoiding unnecessary calls to clone a fact


 

 


Johannes Lerch
committed
Jan 08, 2015






243




					D abstractStartPointFact = AccessPathUtil.cloneWithAccessPath(d3.getFact(), new AccessPath<FieldRef>());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






244




					propagate(new PathEdge<>(abstractStartPointFact, sP, abstractStartPointFact), n, false); //line 15









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






245




246




247




248




				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






249




250




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






251




252




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






253




				resumeEdges(sCalledProcN, d3.getFact());









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






254




255




				registerInterestedCaller(sCalledProcN, incomingEdge);
				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






256




257




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






258




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






259




260




261




262




263




264




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






265




					for(SummaryEdge<D, N> summary: endSumm) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






266




267




268




269




270




271




272




273




274




275




						Optional<D> d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
						if(d4.isPresent()) {
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(ConstrainedFact<FieldRef, D> d5: computeReturnFlowFunction(retFunction, d4.get(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(new PathEdge<>(d1, retSiteN, d5p_restoredCtx), n, false);









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






276




277




								}
							}









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






278




						}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






279




280




281




282




283




284




					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






285




			FlowFunction<FieldRef, D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






286




			for(ConstrainedFact<FieldRef, D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






287




				propagate(new PathEdge<>(d1, returnSiteN, d3.getFact()), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






288




289




290




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






291




292




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






293




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.get(method);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






294




295




		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






296




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) == PrefixTestResult.GUARANTEED_PREFIX) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






297




298




					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






299




						propagate(edge, edge instanceof ConcretizationPathEdge ? edge.getTarget() : null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






300




301




302




303




304




					}
				}
			}
		}
	}









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






305




306




307




308




	
	private void registerInterestedCaller(M method, IncomingEdge<D, N> incomingEdge) {
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






309




			for(final PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






310




				if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), edge.factAtSource()).atLeast(PrefixTestResult.POTENTIAL_PREFIX)) {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






311




312




					logger.trace("RECHECKING-PAUSED-EDGE: {} for new incoming edge {}", edge, incomingEdge);
					









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






313




314




					Constraint<FieldRef> constraint = new DeltaConstraint<FieldRef>(incomingEdge.getCalleeSourceFact().getAccessPath(), edge.factAtSource().getAccessPath());
					propagateConstrained(new ConcretizationPathEdge<>(









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






315




316




							applyConstraint(constraint, incomingEdge.getCallerSourceFact()), 
							incomingEdge.getCallSite(), 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






317




318




							applyConstraint(constraint, incomingEdge.getCallerCallSiteFact()),
							method,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






319




							applyConstraint(constraint, incomingEdge.getCalleeSourceFact())));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






320




321




322




323




				}
			}
		}
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






324














FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






325




326




327




328




329




330




331




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






332




	protected Set<ConstrainedFact<FieldRef, D>> computeCallFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






333




			(FlowFunction<FieldRef, D> callFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		return callFlowFunction.computeTargets(d2);
	}

	/**
	 * Computes the call-to-return flow function for the given call-site
	 * abstraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






346




	protected Set<ConstrainedFact<FieldRef, D>> computeCallToReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






347




			(FlowFunction<FieldRef, D> callToReturnFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






348




349




350




351




352




353




354




355




356




357




358




359




360




361




362




363




364




365




366




367




368




369




370




		return callToReturnFlowFunction.computeTargets(d2);
	}
	
	/**
	 * Lines 21-32 of the algorithm.
	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits
	 */
	protected void processExit(PathEdge<N,D> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		//for each of the method's start points, determine incoming calls
		
		//line 21.1 of Naeem/Lhotak/Rodriguez
		//register end-summary









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






371




372




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






373




			return; //FIXME: should never be reached?! -> assert ?









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






374




375




376




		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






377




378




379




380




381




382




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






383




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




				









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






385




				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






386




387




					Optional<D> concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);
					if(concreteCalleeExitFact.isPresent()) {









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






388




						Set<ConstrainedFact<FieldRef, D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact.get(), callSite);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






389




390




	
						// for each incoming-call value









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






391




						for (ConstrainedFact<FieldRef, D> callerTargetAnnotatedFact : callerTargetFacts) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






392




							D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






393




							propagate(new PathEdge<>(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact), callSite, false);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






394




						}









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






395




					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






396




397




				}
			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






398




399




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






400




401




402




403




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









bug fix in unbalanced return handling


 

 


Johannes Lerch
committed
Jan 29, 2015






404




		if(followReturnsPastSeeds && d1 == zeroValue) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






405




406




407




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






408




					FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






409




410




					Set<ConstrainedFact<FieldRef, D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(ConstrainedFact<FieldRef, D> d5: targets)









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






411




						propagate(new PathEdge<>(zeroValue, retSiteC, d5.getFact()), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






412




413




414




415




416




417




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






418




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




431




				retFunction.computeTargets(d2);
			}
		}
	}
	
	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callSite The call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






432




	protected Set<ConstrainedFact<FieldRef, D>> computeReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






433




			(FlowFunction<FieldRef, D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






434




435




436




437




438




439




440




441




442




443




444




445




446




447




		return retFunction.computeTargets(d2);
	}

	/**
	 * Lines 33-37 of the algorithm.
	 * Simply propagate normal, intra-procedural flows.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		
		for (N m : icfg.getSuccsOf(n)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






448




			FlowFunction<FieldRef, D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






449




450




			Set<ConstrainedFact<FieldRef, D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (ConstrainedFact<FieldRef, D> d3 : res) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






451




				if(d3.getConstraint() != null) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






452




					propagateConstrained(new PathEdge<>(applyConstraint(d3.getConstraint(), d1), m, d3.getFact()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






453




				}









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






454




				else









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






455




					propagate(new PathEdge<>(d1, m, d3.getFact()), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






456




457




458




459




			}
		}
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






460




461




462




463




464




	private D applyConstraint(Constraint<FieldRef> constraint, D fact) {
		if(fact.equals(zeroValue))
			return zeroValue;
		else
			return fact.cloneWithAccessPath(constraint.applyToAccessPath(fact.getAccessPath()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






465




	}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






466




	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






467




468




469




470




471




	private boolean propagateConstrained(PathEdge<N, D> pathEdge) {
		return propagateConstrained(pathEdge, new HashMap<N, Boolean>());
	}
	
	private boolean propagateConstrained(PathEdge<N, D> pathEdge, Map<N, Boolean> visited) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






472




		M calleeMethod = icfg.getMethodOf(pathEdge.getTarget());









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






473




		logger.trace("Checking interest at method {} in fact {}", calleeMethod, pathEdge.factAtSource());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






474














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






475




476




477




		boolean propagate = false;
		if(pathEdge.factAtSource().equals(zeroValue))
			propagate = true;









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






478




479




		else if(hasPausedEdges(calleeMethod, pathEdge))
			propagate = false;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






480




481




		else {
			Set<N> callSitesWithInterest = Sets.newHashSet();









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






482




			for(IncomingEdge<D, N> incEdge : incomingEdgesPrefixedWith(calleeMethod, pathEdge.factAtSource())) { //guaranteed









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






483




				callSitesWithInterest.add(incEdge.getCallSite());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




			}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






485




486




			propagate = !callSitesWithInterest.isEmpty();
			









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






487




			for(IncomingEdge<D, N> incEdge : incomingEdgesPotentialPrefixesOf(calleeMethod, pathEdge.factAtSource())) { //potential









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






488




489




490




491




492




				if(visited.containsKey(incEdge.getCallSite())) {
					if(visited.get(incEdge.getCallSite()) != null)
						propagate |= visited.get(incEdge.getCallSite());
				}
				else {









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






493




					if(!callSitesWithInterest.contains(incEdge.getCallSite())) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






494




						Constraint<FieldRef> callerConstraint = new DeltaConstraint<FieldRef>(incEdge.getCalleeSourceFact().getAccessPath(), pathEdge.factAtSource().getAccessPath());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






495




						









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






496




497




498




499




500




						PathEdge<N,D> callerEdge = new ConcretizationPathEdge<>(
								applyConstraint(callerConstraint, incEdge.getCallerSourceFact()), 
								incEdge.getCallSite(), 
								applyConstraint(callerConstraint, incEdge.getCallerCallSiteFact()),
								calleeMethod,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






501




								applyConstraint(callerConstraint, incEdge.getCalleeSourceFact()));









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






502




503




504




505




506




						visited.put(incEdge.getCallSite(), null);
						boolean result = propagateConstrained(callerEdge, visited);
						visited.put(incEdge.getCallSite(), result);
						propagate |= result;
					}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






507




				}









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






508




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






509




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






510




		









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






511




		if(propagate) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






512




			propagate(pathEdge, pathEdge instanceof ConcretizationPathEdge ? pathEdge.getTarget() : null, false);









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






513




514




515




516




517




518




			return true;
		} else {
			pauseEdge(pathEdge);
			return false;
		}
	}









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






519




520




521




522




523





	private boolean hasPausedEdges(M calleeMethod, PathEdge<N, D> pathEdge) {
		ConcurrentHashSet<PathEdge<N, D>> pe = pausedEdges.get(calleeMethod);
		if(pe != null) {
			for(PathEdge<N, D> edge : pe) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






524




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), pathEdge.factAtSource()) == PrefixTestResult.GUARANTEED_PREFIX)









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






525




526




527




528




529




530




					return true;
			}
		}
		return false;
	}










cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






531




532




	private void pauseEdge(PathEdge<N,D> edge) {
		M method = icfg.getMethodOf(edge.getTarget());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






533




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






534




535




536




		if(edges.add(edge)) {
			logger.trace("PAUSED: {}: {}", method, edge);
		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






537




538




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






539




540




541




542




543




544




545




546




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






547




	protected Set<ConstrainedFact<FieldRef, D>> computeNormalFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






548




			(FlowFunction<FieldRef, D> flowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






549




550




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




567




568




569




570




571




		return flowFunction.computeTargets(d2);
	}
	
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
		d5.setCallingContext(d4);
		return d5;
	}
	
	
	/**
	 * Propagates the flow further down the exploded super graph. 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






572




	 * @param edge the PathEdge that should be propagated









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






573




574




575




576




577




	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






578




	protected void propagate(PathEdge<N,D> edge,









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






579




580




			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






581




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






582




		final D existingVal = jumpFn.addFunction(edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






583




584




585




586




587




588




589




590




591




592




593




594




595




596




597




598




599




600




601




602




603




		
		if(edge instanceof ConcretizationPathEdge) {
			ConcretizationPathEdge<M, N, D> concEdge = (ConcretizationPathEdge<M,N,D>) edge;
			IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(concEdge.getCalleeSourceFact(), 
					concEdge.getTarget(), concEdge.factAtSource(), concEdge.factAtTarget());
			if (!addIncoming(concEdge.getCalleeMethod(), incomingEdge))
				return;
			
			resumeEdges(concEdge.getCalleeMethod(), concEdge.getCalleeSourceFact());
			registerInterestedCaller(concEdge.getCalleeMethod(), incomingEdge);
		} else {
			//TODO: Merge d.* and d.*\{x} as d.*
			if (existingVal != null) {
				if (existingVal != edge.factAtTarget())
					existingVal.addNeighbor(edge.factAtTarget());
			}
			else {
				scheduleEdgeProcessing(edge);
				if(edge.factAtTarget()!=zeroValue)
					logger.trace("EDGE: {}: {}", icfg.getMethodOf(edge.getTarget()), edge);
			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






604




605




606




		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






607




608




609




610




611




612




613




614




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






615




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) == PrefixTestResult.GUARANTEED_PREFIX;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






616




617




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




619




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






620




621




622




623




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






624




625




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






626




627




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






628




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






629




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






630




631




632




633




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






634




635




636




637




638




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






639




				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






640




641




642




643




			}
		});
	}
	









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






644




	protected Set<IncomingEdge<D, N>> incomingEdgesPotentialPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






645




646




647




648




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






649




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact).atLeast(PrefixTestResult.POTENTIAL_PREFIX);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






650




651




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






652




653




	}
	









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






654




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






655




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






656




657




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




688




689




690




691




692




693




694




695




696




697




698




699




700




701




702




703




704




705




706




707




708




	}
	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "FAST IFDS SOLVER";
	}

	public void printStats() {
		if(logger.isDebugEnabled()) {
			if(ffCache!=null)
				ffCache.printStats();
		} else {
			logger.info("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {
		private final PathEdge<N,D> edge;

		public PathEdgeProcessingTask(PathEdge<N,D> edge) {
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
	

}










1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink




1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java





1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Switch branch/tag








1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c


Switch branch/tag





1021f5a26ac8ba04289e1300fe3489d0cf4d9a8c

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

alias

FieldSensitiveIFDSSolver.java
Find file
Normal viewHistoryPermalink




FieldSensitiveIFDSSolver.java



27.2 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






1




/*******************************************************************************









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






2




 * Copyright (c) 2014 Johannes Lerch, Johannes Späth.









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






9




 *     Johannes Lerch, Johannes Späth - initial API and implementation









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






10




11




12




 ******************************************************************************/
package heros.alias;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






13




14




15




16




import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






17




import heros.alias.AccessPath.PrefixTestResult;









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






18




import heros.alias.FlowFunction.ConstrainedFact;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






19




import heros.alias.FlowFunction.Constraint;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






20




21




22




23




import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




import java.util.Collection;
import java.util.Collections;









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






26




import java.util.HashMap;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






27




import java.util.LinkedList;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






28




29




import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






30




import java.util.Set;









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




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






37




import com.google.common.base.Optional;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






38




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






39




import com.google.common.cache.CacheBuilder;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






40




import com.google.common.collect.Lists;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






41




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






42














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






43




public class FieldSensitiveIFDSSolver<N, BaseValue, FieldRef, D extends FieldSensitiveFact<BaseValue, FieldRef, D>, M, I extends InterproceduralCFG<N, M>> {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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






	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel
			(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	
    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);

    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace
    public static final boolean DEBUG = logger.isDebugEnabled();

	protected CountingThreadPoolExecutor executor;
	
	@DontSynchronize("only used by single thread")
	protected int numThreads;
	
	@SynchronizedBy("thread safe data structure, consistent locking when used")
	protected final JumpFunctions<N,D> jumpFn;
	
	@SynchronizedBy("thread safe data structure, only modified internally")
	protected final I icfg;
	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






68




69




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






70




71




72




73




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






74




75




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






76




	









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






77




	protected final MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>>();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






78




	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






79




	@DontSynchronize("stateless")









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






80




	protected final FlowFunctions<N, FieldRef, D, M> flowFunctions;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	
	@DontSynchronize("only used by single thread")
	protected final Map<N,Set<D>> initialSeeds;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("stateless")
	protected final D zeroValue;
	
	@DontSynchronize("readOnly")
	protected final FlowFunctionCache<N,D,M> ffCache = null; 
	
	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






96




97





	private LinkedList<Runnable> worklist;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




100




101




102




103




	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






104




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef, D,M,I> tabulationProblem) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




108




109




110




111




112




113




		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






114




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




117




118




119




120




		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






121




		FlowFunctions<N, FieldRef, D, M> flowFunctions = tabulationProblem.flowFunctions(); 









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		/*if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}*/
		this.flowFunctions = flowFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.jumpFn = new JumpFunctions<N,D>();
		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






132




		this.numThreads = 1; //Math.max(1,tabulationProblem.numThreads()); //solution is in the current state not thread safe









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






133




		this.executor = getExecutor();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






134




		this.worklist = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






135




136




137




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




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */
	public void solve() {		
		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.
	 */
	protected void submitInitialSeeds() {
		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue())









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






154




				propagate(new PathEdge<>(zeroValue, startPoint, val), null, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




178




179




180




181




182




183




			jumpFn.addFunction(new PathEdge<N, D>(zeroValue, startPoint, zeroValue));
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{
			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();
		}
		if(logger.isDebugEnabled())
			printStats();

		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway
		executor.shutdown();
		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed
		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






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




194




//		try {
//			executor.awaitCompletion();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Throwable exception = executor.getException();
//		if(exception!=null) {
//			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
//		}
		while(!worklist.isEmpty()) {
			worklist.pop().run();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






195




196




197




198




199




200




201




202




203




204




		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






205




206




207




208




//    	if (executor.isTerminating())
//    		return;
//    	executor.execute(new PathEdgeProcessingTask(edge));
    	worklist.add(new PathEdgeProcessingTask(edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






209




210




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




229




230




231




232




233




    	propagationCount++;
    }
	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 
	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 
	 * 
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...

        logger.trace("Processing call to {}", n);

		final D d2 = edge.factAtTarget();
		assert d2 != null;
		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		
		//for each possible callee
		Collection<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			//compute the call-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






234




			FlowFunction<FieldRef, D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






235




			Set<ConstrainedFact<FieldRef, D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






236




237




238




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






239




			for(ConstrainedFact<FieldRef, D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




242




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









avoiding unnecessary calls to clone a fact


 

 


Johannes Lerch
committed
Jan 08, 2015






243




					D abstractStartPointFact = AccessPathUtil.cloneWithAccessPath(d3.getFact(), new AccessPath<FieldRef>());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






244




					propagate(new PathEdge<>(abstractStartPointFact, sP, abstractStartPointFact), n, false); //line 15









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






245




246




247




248




				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






249




250




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






251




252




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






253




				resumeEdges(sCalledProcN, d3.getFact());









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






254




255




				registerInterestedCaller(sCalledProcN, incomingEdge);
				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






256




257




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






258




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






259




260




261




262




263




264




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






265




					for(SummaryEdge<D, N> summary: endSumm) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






266




267




268




269




270




271




272




273




274




275




						Optional<D> d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
						if(d4.isPresent()) {
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(ConstrainedFact<FieldRef, D> d5: computeReturnFlowFunction(retFunction, d4.get(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(new PathEdge<>(d1, retSiteN, d5p_restoredCtx), n, false);









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






276




277




								}
							}









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






278




						}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






279




280




281




282




283




284




					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






285




			FlowFunction<FieldRef, D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






286




			for(ConstrainedFact<FieldRef, D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






287




				propagate(new PathEdge<>(d1, returnSiteN, d3.getFact()), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






288




289




290




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






291




292




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






293




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.get(method);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






294




295




		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






296




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) == PrefixTestResult.GUARANTEED_PREFIX) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






297




298




					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






299




						propagate(edge, edge instanceof ConcretizationPathEdge ? edge.getTarget() : null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






300




301




302




303




304




					}
				}
			}
		}
	}









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






305




306




307




308




	
	private void registerInterestedCaller(M method, IncomingEdge<D, N> incomingEdge) {
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






309




			for(final PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






310




				if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), edge.factAtSource()).atLeast(PrefixTestResult.POTENTIAL_PREFIX)) {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






311




312




					logger.trace("RECHECKING-PAUSED-EDGE: {} for new incoming edge {}", edge, incomingEdge);
					









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






313




314




					Constraint<FieldRef> constraint = new DeltaConstraint<FieldRef>(incomingEdge.getCalleeSourceFact().getAccessPath(), edge.factAtSource().getAccessPath());
					propagateConstrained(new ConcretizationPathEdge<>(









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






315




316




							applyConstraint(constraint, incomingEdge.getCallerSourceFact()), 
							incomingEdge.getCallSite(), 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






317




318




							applyConstraint(constraint, incomingEdge.getCallerCallSiteFact()),
							method,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






319




							applyConstraint(constraint, incomingEdge.getCalleeSourceFact())));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






320




321




322




323




				}
			}
		}
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






324














FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






325




326




327




328




329




330




331




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






332




	protected Set<ConstrainedFact<FieldRef, D>> computeCallFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






333




			(FlowFunction<FieldRef, D> callFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		return callFlowFunction.computeTargets(d2);
	}

	/**
	 * Computes the call-to-return flow function for the given call-site
	 * abstraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






346




	protected Set<ConstrainedFact<FieldRef, D>> computeCallToReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






347




			(FlowFunction<FieldRef, D> callToReturnFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






348




349




350




351




352




353




354




355




356




357




358




359




360




361




362




363




364




365




366




367




368




369




370




		return callToReturnFlowFunction.computeTargets(d2);
	}
	
	/**
	 * Lines 21-32 of the algorithm.
	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits
	 */
	protected void processExit(PathEdge<N,D> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		//for each of the method's start points, determine incoming calls
		
		//line 21.1 of Naeem/Lhotak/Rodriguez
		//register end-summary









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






371




372




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






373




			return; //FIXME: should never be reached?! -> assert ?









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






374




375




376




		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






377




378




379




380




381




382




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






383




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




				









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






385




				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






386




387




					Optional<D> concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);
					if(concreteCalleeExitFact.isPresent()) {









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






388




						Set<ConstrainedFact<FieldRef, D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact.get(), callSite);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






389




390




	
						// for each incoming-call value









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






391




						for (ConstrainedFact<FieldRef, D> callerTargetAnnotatedFact : callerTargetFacts) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






392




							D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






393




							propagate(new PathEdge<>(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact), callSite, false);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






394




						}









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






395




					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






396




397




				}
			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






398




399




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






400




401




402




403




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









bug fix in unbalanced return handling


 

 


Johannes Lerch
committed
Jan 29, 2015






404




		if(followReturnsPastSeeds && d1 == zeroValue) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






405




406




407




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






408




					FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






409




410




					Set<ConstrainedFact<FieldRef, D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(ConstrainedFact<FieldRef, D> d5: targets)









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






411




						propagate(new PathEdge<>(zeroValue, retSiteC, d5.getFact()), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






412




413




414




415




416




417




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






418




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




431




				retFunction.computeTargets(d2);
			}
		}
	}
	
	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callSite The call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






432




	protected Set<ConstrainedFact<FieldRef, D>> computeReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






433




			(FlowFunction<FieldRef, D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






434




435




436




437




438




439




440




441




442




443




444




445




446




447




		return retFunction.computeTargets(d2);
	}

	/**
	 * Lines 33-37 of the algorithm.
	 * Simply propagate normal, intra-procedural flows.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		
		for (N m : icfg.getSuccsOf(n)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






448




			FlowFunction<FieldRef, D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






449




450




			Set<ConstrainedFact<FieldRef, D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (ConstrainedFact<FieldRef, D> d3 : res) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






451




				if(d3.getConstraint() != null) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






452




					propagateConstrained(new PathEdge<>(applyConstraint(d3.getConstraint(), d1), m, d3.getFact()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






453




				}









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






454




				else









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






455




					propagate(new PathEdge<>(d1, m, d3.getFact()), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






456




457




458




459




			}
		}
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






460




461




462




463




464




	private D applyConstraint(Constraint<FieldRef> constraint, D fact) {
		if(fact.equals(zeroValue))
			return zeroValue;
		else
			return fact.cloneWithAccessPath(constraint.applyToAccessPath(fact.getAccessPath()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






465




	}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






466




	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






467




468




469




470




471




	private boolean propagateConstrained(PathEdge<N, D> pathEdge) {
		return propagateConstrained(pathEdge, new HashMap<N, Boolean>());
	}
	
	private boolean propagateConstrained(PathEdge<N, D> pathEdge, Map<N, Boolean> visited) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






472




		M calleeMethod = icfg.getMethodOf(pathEdge.getTarget());









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






473




		logger.trace("Checking interest at method {} in fact {}", calleeMethod, pathEdge.factAtSource());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






474














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






475




476




477




		boolean propagate = false;
		if(pathEdge.factAtSource().equals(zeroValue))
			propagate = true;









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






478




479




		else if(hasPausedEdges(calleeMethod, pathEdge))
			propagate = false;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






480




481




		else {
			Set<N> callSitesWithInterest = Sets.newHashSet();









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






482




			for(IncomingEdge<D, N> incEdge : incomingEdgesPrefixedWith(calleeMethod, pathEdge.factAtSource())) { //guaranteed









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






483




				callSitesWithInterest.add(incEdge.getCallSite());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




			}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






485




486




			propagate = !callSitesWithInterest.isEmpty();
			









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






487




			for(IncomingEdge<D, N> incEdge : incomingEdgesPotentialPrefixesOf(calleeMethod, pathEdge.factAtSource())) { //potential









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






488




489




490




491




492




				if(visited.containsKey(incEdge.getCallSite())) {
					if(visited.get(incEdge.getCallSite()) != null)
						propagate |= visited.get(incEdge.getCallSite());
				}
				else {









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






493




					if(!callSitesWithInterest.contains(incEdge.getCallSite())) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






494




						Constraint<FieldRef> callerConstraint = new DeltaConstraint<FieldRef>(incEdge.getCalleeSourceFact().getAccessPath(), pathEdge.factAtSource().getAccessPath());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






495




						









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






496




497




498




499




500




						PathEdge<N,D> callerEdge = new ConcretizationPathEdge<>(
								applyConstraint(callerConstraint, incEdge.getCallerSourceFact()), 
								incEdge.getCallSite(), 
								applyConstraint(callerConstraint, incEdge.getCallerCallSiteFact()),
								calleeMethod,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






501




								applyConstraint(callerConstraint, incEdge.getCalleeSourceFact()));









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






502




503




504




505




506




						visited.put(incEdge.getCallSite(), null);
						boolean result = propagateConstrained(callerEdge, visited);
						visited.put(incEdge.getCallSite(), result);
						propagate |= result;
					}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






507




				}









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






508




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






509




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






510




		









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






511




		if(propagate) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






512




			propagate(pathEdge, pathEdge instanceof ConcretizationPathEdge ? pathEdge.getTarget() : null, false);









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






513




514




515




516




517




518




			return true;
		} else {
			pauseEdge(pathEdge);
			return false;
		}
	}









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






519




520




521




522




523





	private boolean hasPausedEdges(M calleeMethod, PathEdge<N, D> pathEdge) {
		ConcurrentHashSet<PathEdge<N, D>> pe = pausedEdges.get(calleeMethod);
		if(pe != null) {
			for(PathEdge<N, D> edge : pe) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






524




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), pathEdge.factAtSource()) == PrefixTestResult.GUARANTEED_PREFIX)









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






525




526




527




528




529




530




					return true;
			}
		}
		return false;
	}










cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






531




532




	private void pauseEdge(PathEdge<N,D> edge) {
		M method = icfg.getMethodOf(edge.getTarget());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






533




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






534




535




536




		if(edges.add(edge)) {
			logger.trace("PAUSED: {}: {}", method, edge);
		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






537




538




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






539




540




541




542




543




544




545




546




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






547




	protected Set<ConstrainedFact<FieldRef, D>> computeNormalFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






548




			(FlowFunction<FieldRef, D> flowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






549




550




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




567




568




569




570




571




		return flowFunction.computeTargets(d2);
	}
	
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
		d5.setCallingContext(d4);
		return d5;
	}
	
	
	/**
	 * Propagates the flow further down the exploded super graph. 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






572




	 * @param edge the PathEdge that should be propagated









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






573




574




575




576




577




	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






578




	protected void propagate(PathEdge<N,D> edge,









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






579




580




			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






581




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






582




		final D existingVal = jumpFn.addFunction(edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






583




584




585




586




587




588




589




590




591




592




593




594




595




596




597




598




599




600




601




602




603




		
		if(edge instanceof ConcretizationPathEdge) {
			ConcretizationPathEdge<M, N, D> concEdge = (ConcretizationPathEdge<M,N,D>) edge;
			IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(concEdge.getCalleeSourceFact(), 
					concEdge.getTarget(), concEdge.factAtSource(), concEdge.factAtTarget());
			if (!addIncoming(concEdge.getCalleeMethod(), incomingEdge))
				return;
			
			resumeEdges(concEdge.getCalleeMethod(), concEdge.getCalleeSourceFact());
			registerInterestedCaller(concEdge.getCalleeMethod(), incomingEdge);
		} else {
			//TODO: Merge d.* and d.*\{x} as d.*
			if (existingVal != null) {
				if (existingVal != edge.factAtTarget())
					existingVal.addNeighbor(edge.factAtTarget());
			}
			else {
				scheduleEdgeProcessing(edge);
				if(edge.factAtTarget()!=zeroValue)
					logger.trace("EDGE: {}: {}", icfg.getMethodOf(edge.getTarget()), edge);
			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






604




605




606




		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






607




608




609




610




611




612




613




614




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






615




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) == PrefixTestResult.GUARANTEED_PREFIX;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






616




617




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




619




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






620




621




622




623




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






624




625




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






626




627




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






628




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






629




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






630




631




632




633




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






634




635




636




637




638




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






639




				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






640




641




642




643




			}
		});
	}
	









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






644




	protected Set<IncomingEdge<D, N>> incomingEdgesPotentialPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






645




646




647




648




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






649




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact).atLeast(PrefixTestResult.POTENTIAL_PREFIX);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






650




651




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






652




653




	}
	









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






654




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






655




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






656




657




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




688




689




690




691




692




693




694




695




696




697




698




699




700




701




702




703




704




705




706




707




708




	}
	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "FAST IFDS SOLVER";
	}

	public void printStats() {
		if(logger.isDebugEnabled()) {
			if(ffCache!=null)
				ffCache.printStats();
		} else {
			logger.info("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {
		private final PathEdge<N,D> edge;

		public PathEdgeProcessingTask(PathEdge<N,D> edge) {
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
	

}








FieldSensitiveIFDSSolver.java



27.2 KB










FieldSensitiveIFDSSolver.java



27.2 KB









Newer










Older
NewerOlder







FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






1




/*******************************************************************************









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






2




 * Copyright (c) 2014 Johannes Lerch, Johannes Späth.









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






9




 *     Johannes Lerch, Johannes Späth - initial API and implementation









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






10




11




12




 ******************************************************************************/
package heros.alias;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






13




14




15




16




import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






17




import heros.alias.AccessPath.PrefixTestResult;









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






18




import heros.alias.FlowFunction.ConstrainedFact;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






19




import heros.alias.FlowFunction.Constraint;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






20




21




22




23




import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




import java.util.Collection;
import java.util.Collections;









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






26




import java.util.HashMap;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






27




import java.util.LinkedList;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






28




29




import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






30




import java.util.Set;









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




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






37




import com.google.common.base.Optional;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






38




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






39




import com.google.common.cache.CacheBuilder;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






40




import com.google.common.collect.Lists;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






41




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






42














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






43




public class FieldSensitiveIFDSSolver<N, BaseValue, FieldRef, D extends FieldSensitiveFact<BaseValue, FieldRef, D>, M, I extends InterproceduralCFG<N, M>> {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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






	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel
			(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	
    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);

    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace
    public static final boolean DEBUG = logger.isDebugEnabled();

	protected CountingThreadPoolExecutor executor;
	
	@DontSynchronize("only used by single thread")
	protected int numThreads;
	
	@SynchronizedBy("thread safe data structure, consistent locking when used")
	protected final JumpFunctions<N,D> jumpFn;
	
	@SynchronizedBy("thread safe data structure, only modified internally")
	protected final I icfg;
	
	//stores summaries that were queried before they were computed
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on 'incoming'")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






68




69




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






70




71




72




73




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






74




75




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






76




	









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






77




	protected final MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>>();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






78




	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






79




	@DontSynchronize("stateless")









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






80




	protected final FlowFunctions<N, FieldRef, D, M> flowFunctions;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	
	@DontSynchronize("only used by single thread")
	protected final Map<N,Set<D>> initialSeeds;
	
	@DontSynchronize("benign races")
	public long propagationCount;
	
	@DontSynchronize("stateless")
	protected final D zeroValue;
	
	@DontSynchronize("readOnly")
	protected final FlowFunctionCache<N,D,M> ffCache = null; 
	
	@DontSynchronize("readOnly")
	protected final boolean followReturnsPastSeeds;









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






96




97





	private LinkedList<Runnable> worklist;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




100




101




102




103




	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






104




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef, D,M,I> tabulationProblem) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




108




109




110




111




112




113




		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






114




	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




117




118




119




120




		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






121




		FlowFunctions<N, FieldRef, D, M> flowFunctions = tabulationProblem.flowFunctions(); 









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		/*if(flowFunctionCacheBuilder!=null) {
			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);
			flowFunctions = ffCache;
		} else {
			ffCache = null;
		}*/
		this.flowFunctions = flowFunctions;
		this.initialSeeds = tabulationProblem.initialSeeds();
		this.jumpFn = new JumpFunctions<N,D>();
		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






132




		this.numThreads = 1; //Math.max(1,tabulationProblem.numThreads()); //solution is in the current state not thread safe









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






133




		this.executor = getExecutor();









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






134




		this.worklist = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






135




136




137




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




	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 */
	public void solve() {		
		submitInitialSeeds();
		awaitCompletionComputeValuesAndShutdown();
	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 * Clients should only call this methods if performing synchronization on
	 * their own. Normally, {@link #solve()} should be called instead.
	 */
	protected void submitInitialSeeds() {
		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {
			N startPoint = seed.getKey();
			for(D val: seed.getValue())









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






154




				propagate(new PathEdge<>(zeroValue, startPoint, val), null, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




178




179




180




181




182




183




			jumpFn.addFunction(new PathEdge<N, D>(zeroValue, startPoint, zeroValue));
		}
	}

	/**
	 * Awaits the completion of the exploded super graph. When complete, computes result values,
	 * shuts down the executor and returns.
	 */
	protected void awaitCompletionComputeValuesAndShutdown() {
		{
			//run executor and await termination of tasks
			runExecutorAndAwaitCompletion();
		}
		if(logger.isDebugEnabled())
			printStats();

		//ask executor to shut down;
		//this will cause new submissions to the executor to be rejected,
		//but at this point all tasks should have completed anyway
		executor.shutdown();
		//similarly here: we await termination, but this should happen instantaneously,
		//as all tasks should have completed
		runExecutorAndAwaitCompletion();
	}

	/**
	 * Runs execution, re-throwing exceptions that might be thrown during its execution.
	 */
	private void runExecutorAndAwaitCompletion() {









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






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




194




//		try {
//			executor.awaitCompletion();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Throwable exception = executor.getException();
//		if(exception!=null) {
//			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
//		}
		while(!worklist.isEmpty()) {
			worklist.pop().run();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






195




196




197




198




199




200




201




202




203




204




		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






205




206




207




208




//    	if (executor.isTerminating())
//    		return;
//    	executor.execute(new PathEdgeProcessingTask(edge));
    	worklist.add(new PathEdgeProcessingTask(edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






209




210




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




229




230




231




232




233




    	propagationCount++;
    }
	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.
	 * 
	 * For each possible callee, registers incoming call edges.
	 * Also propagates call-to-return flows and summarized callee flows within the caller. 
	 * 
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...

        logger.trace("Processing call to {}", n);

		final D d2 = edge.factAtTarget();
		assert d2 != null;
		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		
		//for each possible callee
		Collection<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			//compute the call-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






234




			FlowFunction<FieldRef, D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






235




			Set<ConstrainedFact<FieldRef, D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






236




237




238




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






239




			for(ConstrainedFact<FieldRef, D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




242




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









avoiding unnecessary calls to clone a fact


 

 


Johannes Lerch
committed
Jan 08, 2015






243




					D abstractStartPointFact = AccessPathUtil.cloneWithAccessPath(d3.getFact(), new AccessPath<FieldRef>());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






244




					propagate(new PathEdge<>(abstractStartPointFact, sP, abstractStartPointFact), n, false); //line 15









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






245




246




247




248




				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






249




250




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






251




252




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






253




				resumeEdges(sCalledProcN, d3.getFact());









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






254




255




				registerInterestedCaller(sCalledProcN, incomingEdge);
				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






256




257




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






258




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






259




260




261




262




263




264




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






265




					for(SummaryEdge<D, N> summary: endSumm) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






266




267




268




269




270




271




272




273




274




275




						Optional<D> d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
						if(d4.isPresent()) {
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(ConstrainedFact<FieldRef, D> d5: computeReturnFlowFunction(retFunction, d4.get(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(new PathEdge<>(d1, retSiteN, d5p_restoredCtx), n, false);









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






276




277




								}
							}









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






278




						}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






279




280




281




282




283




284




					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






285




			FlowFunction<FieldRef, D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






286




			for(ConstrainedFact<FieldRef, D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






287




				propagate(new PathEdge<>(d1, returnSiteN, d3.getFact()), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






288




289




290




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






291




292




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






293




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.get(method);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






294




295




		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






296




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) == PrefixTestResult.GUARANTEED_PREFIX) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






297




298




					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






299




						propagate(edge, edge instanceof ConcretizationPathEdge ? edge.getTarget() : null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






300




301




302




303




304




					}
				}
			}
		}
	}









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






305




306




307




308




	
	private void registerInterestedCaller(M method, IncomingEdge<D, N> incomingEdge) {
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






309




			for(final PathEdge<N, D> edge : edges) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






310




				if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), edge.factAtSource()).atLeast(PrefixTestResult.POTENTIAL_PREFIX)) {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






311




312




					logger.trace("RECHECKING-PAUSED-EDGE: {} for new incoming edge {}", edge, incomingEdge);
					









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






313




314




					Constraint<FieldRef> constraint = new DeltaConstraint<FieldRef>(incomingEdge.getCalleeSourceFact().getAccessPath(), edge.factAtSource().getAccessPath());
					propagateConstrained(new ConcretizationPathEdge<>(









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






315




316




							applyConstraint(constraint, incomingEdge.getCallerSourceFact()), 
							incomingEdge.getCallSite(), 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






317




318




							applyConstraint(constraint, incomingEdge.getCallerCallSiteFact()),
							method,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






319




							applyConstraint(constraint, incomingEdge.getCalleeSourceFact())));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






320




321




322




323




				}
			}
		}
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






324














FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






325




326




327




328




329




330




331




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






332




	protected Set<ConstrainedFact<FieldRef, D>> computeCallFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






333




			(FlowFunction<FieldRef, D> callFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




		return callFlowFunction.computeTargets(d2);
	}

	/**
	 * Computes the call-to-return flow function for the given call-site
	 * abstraction
	 * @param callToReturnFlowFunction The call-to-return flow function to
	 * compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






346




	protected Set<ConstrainedFact<FieldRef, D>> computeCallToReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






347




			(FlowFunction<FieldRef, D> callToReturnFlowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






348




349




350




351




352




353




354




355




356




357




358




359




360




361




362




363




364




365




366




367




368




369




370




		return callToReturnFlowFunction.computeTargets(d2);
	}
	
	/**
	 * Lines 21-32 of the algorithm.
	 * 
	 * Stores callee-side summaries.
	 * Also, at the side of the caller, propagates intra-procedural flows to return sites
	 * using those newly computed summaries.
	 * 
	 * @param edge an edge whose target node resembles a method exits
	 */
	protected void processExit(PathEdge<N,D> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		//for each of the method's start points, determine incoming calls
		
		//line 21.1 of Naeem/Lhotak/Rodriguez
		//register end-summary









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






371




372




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






373




			return; //FIXME: should never be reached?! -> assert ?









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






374




375




376




		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






377




378




379




380




381




382




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






383




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




				









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






385




				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






386




387




					Optional<D> concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);
					if(concreteCalleeExitFact.isPresent()) {









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






388




						Set<ConstrainedFact<FieldRef, D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact.get(), callSite);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






389




390




	
						// for each incoming-call value









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






391




						for (ConstrainedFact<FieldRef, D> callerTargetAnnotatedFact : callerTargetFacts) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






392




							D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






393




							propagate(new PathEdge<>(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact), callSite, false);









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






394




						}









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






395




					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






396




397




				}
			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






398




399




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






400




401




402




403




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









bug fix in unbalanced return handling


 

 


Johannes Lerch
committed
Jan 29, 2015






404




		if(followReturnsPastSeeds && d1 == zeroValue) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






405




406




407




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






408




					FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






409




410




					Set<ConstrainedFact<FieldRef, D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(ConstrainedFact<FieldRef, D> d5: targets)









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






411




						propagate(new PathEdge<>(zeroValue, retSiteC, d5.getFact()), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






412




413




414




415




416




417




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






418




				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




431




				retFunction.computeTargets(d2);
			}
		}
	}
	
	/**
	 * Computes the return flow function for the given set of caller-side
	 * abstractions.
	 * @param retFunction The return flow function to compute
	 * @param d2 The abstraction at the exit node in the callee
	 * @param callSite The call site
	 * @return The set of caller-side abstractions at the return site
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






432




	protected Set<ConstrainedFact<FieldRef, D>> computeReturnFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






433




			(FlowFunction<FieldRef, D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






434




435




436




437




438




439




440




441




442




443




444




445




446




447




		return retFunction.computeTargets(d2);
	}

	/**
	 * Lines 33-37 of the algorithm.
	 * Simply propagate normal, intra-procedural flows.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		
		for (N m : icfg.getSuccsOf(n)) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






448




			FlowFunction<FieldRef, D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






449




450




			Set<ConstrainedFact<FieldRef, D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (ConstrainedFact<FieldRef, D> d3 : res) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






451




				if(d3.getConstraint() != null) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






452




					propagateConstrained(new PathEdge<>(applyConstraint(d3.getConstraint(), d1), m, d3.getFact()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






453




				}









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






454




				else









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






455




					propagate(new PathEdge<>(d1, m, d3.getFact()), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






456




457




458




459




			}
		}
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






460




461




462




463




464




	private D applyConstraint(Constraint<FieldRef> constraint, D fact) {
		if(fact.equals(zeroValue))
			return zeroValue;
		else
			return fact.cloneWithAccessPath(constraint.applyToAccessPath(fact.getAccessPath()));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






465




	}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






466




	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






467




468




469




470




471




	private boolean propagateConstrained(PathEdge<N, D> pathEdge) {
		return propagateConstrained(pathEdge, new HashMap<N, Boolean>());
	}
	
	private boolean propagateConstrained(PathEdge<N, D> pathEdge, Map<N, Boolean> visited) {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






472




		M calleeMethod = icfg.getMethodOf(pathEdge.getTarget());









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






473




		logger.trace("Checking interest at method {} in fact {}", calleeMethod, pathEdge.factAtSource());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






474














cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






475




476




477




		boolean propagate = false;
		if(pathEdge.factAtSource().equals(zeroValue))
			propagate = true;









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






478




479




		else if(hasPausedEdges(calleeMethod, pathEdge))
			propagate = false;









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






480




481




		else {
			Set<N> callSitesWithInterest = Sets.newHashSet();









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






482




			for(IncomingEdge<D, N> incEdge : incomingEdgesPrefixedWith(calleeMethod, pathEdge.factAtSource())) { //guaranteed









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






483




				callSitesWithInterest.add(incEdge.getCallSite());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




			}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






485




486




			propagate = !callSitesWithInterest.isEmpty();
			









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






487




			for(IncomingEdge<D, N> incEdge : incomingEdgesPotentialPrefixesOf(calleeMethod, pathEdge.factAtSource())) { //potential









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






488




489




490




491




492




				if(visited.containsKey(incEdge.getCallSite())) {
					if(visited.get(incEdge.getCallSite()) != null)
						propagate |= visited.get(incEdge.getCallSite());
				}
				else {









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






493




					if(!callSitesWithInterest.contains(incEdge.getCallSite())) {









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






494




						Constraint<FieldRef> callerConstraint = new DeltaConstraint<FieldRef>(incEdge.getCalleeSourceFact().getAccessPath(), pathEdge.factAtSource().getAccessPath());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






495




						









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






496




497




498




499




500




						PathEdge<N,D> callerEdge = new ConcretizationPathEdge<>(
								applyConstraint(callerConstraint, incEdge.getCallerSourceFact()), 
								incEdge.getCallSite(), 
								applyConstraint(callerConstraint, incEdge.getCallerCallSiteFact()),
								calleeMethod,









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






501




								applyConstraint(callerConstraint, incEdge.getCalleeSourceFact()));









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






502




503




504




505




506




						visited.put(incEdge.getCallSite(), null);
						boolean result = propagateConstrained(callerEdge, visited);
						visited.put(incEdge.getCallSite(), result);
						propagate |= result;
					}









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






507




				}









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






508




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






509




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






510




		









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






511




		if(propagate) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






512




			propagate(pathEdge, pathEdge instanceof ConcretizationPathEdge ? pathEdge.getTarget() : null, false);









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






513




514




515




516




517




518




			return true;
		} else {
			pauseEdge(pathEdge);
			return false;
		}
	}









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






519




520




521




522




523





	private boolean hasPausedEdges(M calleeMethod, PathEdge<N, D> pathEdge) {
		ConcurrentHashSet<PathEdge<N, D>> pe = pausedEdges.get(calleeMethod);
		if(pe != null) {
			for(PathEdge<N, D> edge : pe) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






524




				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), pathEdge.factAtSource()) == PrefixTestResult.GUARANTEED_PREFIX)









Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015






525




526




527




528




529




530




					return true;
			}
		}
		return false;
	}










cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






531




532




	private void pauseEdge(PathEdge<N,D> edge) {
		M method = icfg.getMethodOf(edge.getTarget());









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






533




		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






534




535




536




		if(edges.add(edge)) {
			logger.trace("PAUSED: {}: {}", method, edge);
		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






537




538




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






539




540




541




542




543




544




545




546




	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015






547




	protected Set<ConstrainedFact<FieldRef, D>> computeNormalFlowFunction









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






548




			(FlowFunction<FieldRef, D> flowFunction, D d1, D d2) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






549




550




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




567




568




569




570




571




		return flowFunction.computeTargets(d2);
	}
	
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
		d5.setCallingContext(d4);
		return d5;
	}
	
	
	/**
	 * Propagates the flow further down the exploded super graph. 









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






572




	 * @param edge the PathEdge that should be propagated









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






573




574




575




576




577




	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






578




	protected void propagate(PathEdge<N,D> edge,









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






579




580




			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






581




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






582




		final D existingVal = jumpFn.addFunction(edge);









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






583




584




585




586




587




588




589




590




591




592




593




594




595




596




597




598




599




600




601




602




603




		
		if(edge instanceof ConcretizationPathEdge) {
			ConcretizationPathEdge<M, N, D> concEdge = (ConcretizationPathEdge<M,N,D>) edge;
			IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(concEdge.getCalleeSourceFact(), 
					concEdge.getTarget(), concEdge.factAtSource(), concEdge.factAtTarget());
			if (!addIncoming(concEdge.getCalleeMethod(), incomingEdge))
				return;
			
			resumeEdges(concEdge.getCalleeMethod(), concEdge.getCalleeSourceFact());
			registerInterestedCaller(concEdge.getCalleeMethod(), incomingEdge);
		} else {
			//TODO: Merge d.* and d.*\{x} as d.*
			if (existingVal != null) {
				if (existingVal != edge.factAtTarget())
					existingVal.addNeighbor(edge.factAtTarget());
			}
			else {
				scheduleEdgeProcessing(edge);
				if(edge.factAtTarget()!=zeroValue)
					logger.trace("EDGE: {}: {}", icfg.getMethodOf(edge.getTarget()), edge);
			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






604




605




606




		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






607




608




609




610




611




612




613




614




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






615




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) == PrefixTestResult.GUARANTEED_PREFIX;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






616




617




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




619




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






620




621




622




623




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






624




625




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






626




627




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






628




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






629




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






630




631




632




633




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






634




635




636




637




638




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






639




				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






640




641




642




643




			}
		});
	}
	









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






644




	protected Set<IncomingEdge<D, N>> incomingEdgesPotentialPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






645




646




647




648




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015






649




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact).atLeast(PrefixTestResult.POTENTIAL_PREFIX);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






650




651




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






652




653




	}
	









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






654




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






655




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






656




657




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




688




689




690




691




692




693




694




695




696




697




698




699




700




701




702




703




704




705




706




707




708




	}
	
	/**
	 * Factory method for this solver's thread-pool executor.
	 */
	protected CountingThreadPoolExecutor getExecutor() {
		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * Returns a String used to identify the output of this solver in debug mode.
	 * Subclasses can overwrite this string to distinguish the output from different solvers.
	 */
	protected String getDebugName() {
		return "FAST IFDS SOLVER";
	}

	public void printStats() {
		if(logger.isDebugEnabled()) {
			if(ffCache!=null)
				ffCache.printStats();
		} else {
			logger.info("No statistics were collected, as DEBUG is disabled.");
		}
	}
	
	private class PathEdgeProcessingTask implements Runnable {
		private final PathEdge<N,D> edge;

		public PathEdgeProcessingTask(PathEdge<N,D> edge) {
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

1
/*******************************************************************************/*******************************************************************************



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

2
 * Copyright (c) 2014 Johannes Lerch, Johannes Späth. * Copyright (c) 2014 Johannes Lerch, Johannes Späth.



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

3

4

5

6

7

8
 * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors:



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

9
 *     Johannes Lerch, Johannes Späth - initial API and implementation *     Johannes Lerch, Johannes Späth - initial API and implementation



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

10

11

12
 ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

13

14

15

16
import heros.DontSynchronize;importheros.DontSynchronize;import heros.FlowFunctionCache;importheros.FlowFunctionCache;import heros.InterproceduralCFG;importheros.InterproceduralCFG;import heros.SynchronizedBy;importheros.SynchronizedBy;



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

17
import heros.alias.AccessPath.PrefixTestResult;importheros.alias.AccessPath.PrefixTestResult;



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

18
import heros.alias.FlowFunction.ConstrainedFact;importheros.alias.FlowFunction.ConstrainedFact;



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

19
import heros.alias.FlowFunction.Constraint;importheros.alias.FlowFunction.Constraint;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

20

21

22

23
import heros.solver.CountingThreadPoolExecutor;importheros.solver.CountingThreadPoolExecutor;import heros.solver.IFDSSolver;importheros.solver.IFDSSolver;import heros.solver.PathEdge;importheros.solver.PathEdge;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

24

25
import java.util.Collection;importjava.util.Collection;import java.util.Collections;importjava.util.Collections;



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

26
import java.util.HashMap;importjava.util.HashMap;



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

27
import java.util.LinkedList;importjava.util.LinkedList;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

28

29
import java.util.Map;importjava.util.Map;import java.util.Map.Entry;importjava.util.Map.Entry;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

30
import java.util.Set;importjava.util.Set;



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
import java.util.concurrent.LinkedBlockingQueue;importjava.util.concurrent.LinkedBlockingQueue;import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;import org.slf4j.Logger;importorg.slf4j.Logger;import org.slf4j.LoggerFactory;importorg.slf4j.LoggerFactory;



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

37
import com.google.common.base.Optional;importcom.google.common.base.Optional;



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

38
import com.google.common.base.Predicate;importcom.google.common.base.Predicate;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

39
import com.google.common.cache.CacheBuilder;importcom.google.common.cache.CacheBuilder;



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

40
import com.google.common.collect.Lists;importcom.google.common.collect.Lists;



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

41
import com.google.common.collect.Sets;importcom.google.common.collect.Sets;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

42




cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

43
public class FieldSensitiveIFDSSolver<N, BaseValue, FieldRef, D extends FieldSensitiveFact<BaseValue, FieldRef, D>, M, I extends InterproceduralCFG<N, M>> {publicclassFieldSensitiveIFDSSolver<N,BaseValue,FieldRef,DextendsFieldSensitiveFact<BaseValue,FieldRef,D>,M,IextendsInterproceduralCFG<N,M>>{



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevelpublicstaticCacheBuilder<Object,Object>DEFAULT_CACHE_BUILDER=CacheBuilder.newBuilder().concurrencyLevel			(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();	    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);protectedstaticfinalLoggerlogger=LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace//enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace    public static final boolean DEBUG = logger.isDebugEnabled();publicstaticfinalbooleanDEBUG=logger.isDebugEnabled();	protected CountingThreadPoolExecutor executor;protectedCountingThreadPoolExecutorexecutor;		@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")	protected int numThreads;protectedintnumThreads;		@SynchronizedBy("thread safe data structure, consistent locking when used")@SynchronizedBy("thread safe data structure, consistent locking when used")	protected final JumpFunctions<N,D> jumpFn;protectedfinalJumpFunctions<N,D>jumpFn;		@SynchronizedBy("thread safe data structure, only modified internally")@SynchronizedBy("thread safe data structure, only modified internally")	protected final I icfg;protectedfinalIicfg;		//stores summaries that were queried before they were computed//stores summaries that were queried before they were computed	//see CC 2010 paper by Naeem, Lhotak and Rodriguez//see CC 2010 paper by Naeem, Lhotak and Rodriguez	@SynchronizedBy("consistent lock on 'incoming'")@SynchronizedBy("consistent lock on 'incoming'")



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

68

69
	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =protectedfinalMyConcurrentHashMap<M,Set<SummaryEdge<D,N>>>endSummary=			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();newMyConcurrentHashMap<M,Set<SummaryEdge<D,N>>>();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

70

71

72

73
		//edges going along calls//edges going along calls	//see CC 2010 paper by Naeem, Lhotak and Rodriguez//see CC 2010 paper by Naeem, Lhotak and Rodriguez	@SynchronizedBy("consistent lock on field")@SynchronizedBy("consistent lock on field")



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

74

75
	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =protectedfinalMyConcurrentHashMap<M,Set<IncomingEdge<D,N>>>incoming=			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();newMyConcurrentHashMap<M,Set<IncomingEdge<D,N>>>();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

76
	



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

77
	protected final MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, ConcurrentHashSet<PathEdge<N,D>>>();protectedfinalMyConcurrentHashMap<M,ConcurrentHashSet<PathEdge<N,D>>>pausedEdges=newMyConcurrentHashMap<M,ConcurrentHashSet<PathEdge<N,D>>>();



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

78
	



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

79
	@DontSynchronize("stateless")@DontSynchronize("stateless")



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

80
	protected final FlowFunctions<N, FieldRef, D, M> flowFunctions;protectedfinalFlowFunctions<N,FieldRef,D,M>flowFunctions;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
		@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")	protected final Map<N,Set<D>> initialSeeds;protectedfinalMap<N,Set<D>>initialSeeds;		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long propagationCount;publiclongpropagationCount;		@DontSynchronize("stateless")@DontSynchronize("stateless")	protected final D zeroValue;protectedfinalDzeroValue;		@DontSynchronize("readOnly")@DontSynchronize("readOnly")	protected final FlowFunctionCache<N,D,M> ffCache = null; protectedfinalFlowFunctionCache<N,D,M>ffCache=null;		@DontSynchronize("readOnly")@DontSynchronize("readOnly")	protected final boolean followReturnsPastSeeds;protectedfinalbooleanfollowReturnsPastSeeds;



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

96

97
	private LinkedList<Runnable> worklist;privateLinkedList<Runnable>worklist;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

98

99

100

101

102

103
			/**/**	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * The solver must then be started by calling {@link #solve()}.	 * The solver must then be started by calling {@link #solve()}.	 */	 */



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

104
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef, D,M,I> tabulationProblem) {publicFieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I>tabulationProblem){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

105

106

107

108

109

110

111

112

113
		this(tabulationProblem, DEFAULT_CACHE_BUILDER);this(tabulationProblem,DEFAULT_CACHE_BUILDER);	}}	/**/**	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * {@link #solve()}.	 * {@link #solve()}.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 */	 */



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

114
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {publicFieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I>tabulationProblem,@SuppressWarnings("rawtypes")CacheBuilderflowFunctionCacheBuilder){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

115

116

117

118

119

120
		if(logger.isDebugEnabled())if(logger.isDebugEnabled())			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();flowFunctionCacheBuilder=flowFunctionCacheBuilder.recordStats();		this.zeroValue = tabulationProblem.zeroValue();this.zeroValue=tabulationProblem.zeroValue();		this.icfg = tabulationProblem.interproceduralCFG();		this.icfg=tabulationProblem.interproceduralCFG();	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

121
		FlowFunctions<N, FieldRef, D, M> flowFunctions = tabulationProblem.flowFunctions(); FlowFunctions<N,FieldRef,D,M>flowFunctions=tabulationProblem.flowFunctions();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
		/*if(flowFunctionCacheBuilder!=null) {/*if(flowFunctionCacheBuilder!=null) {			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);			flowFunctions = ffCache;			flowFunctions = ffCache;		} else {		} else {			ffCache = null;			ffCache = null;		}*/		}*/		this.flowFunctions = flowFunctions;this.flowFunctions=flowFunctions;		this.initialSeeds = tabulationProblem.initialSeeds();this.initialSeeds=tabulationProblem.initialSeeds();		this.jumpFn = new JumpFunctions<N,D>();this.jumpFn=newJumpFunctions<N,D>();		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();this.followReturnsPastSeeds=tabulationProblem.followReturnsPastSeeds();



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

132
		this.numThreads = 1; //Math.max(1,tabulationProblem.numThreads()); //solution is in the current state not thread safethis.numThreads=1;//Math.max(1,tabulationProblem.numThreads()); //solution is in the current state not thread safe



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

133
		this.executor = getExecutor();this.executor=getExecutor();



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

134
		this.worklist = Lists.newLinkedList();this.worklist=Lists.newLinkedList();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

135

136

137

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
	}}	/**/**	 * Runs the solver on the configured problem. This can take some time.	 * Runs the solver on the configured problem. This can take some time.	 */	 */	public void solve() {		publicvoidsolve(){		submitInitialSeeds();submitInitialSeeds();		awaitCompletionComputeValuesAndShutdown();awaitCompletionComputeValuesAndShutdown();	}}	/**/**	 * Schedules the processing of initial seeds, initiating the analysis.	 * Schedules the processing of initial seeds, initiating the analysis.	 * Clients should only call this methods if performing synchronization on	 * Clients should only call this methods if performing synchronization on	 * their own. Normally, {@link #solve()} should be called instead.	 * their own. Normally, {@link #solve()} should be called instead.	 */	 */	protected void submitInitialSeeds() {protectedvoidsubmitInitialSeeds(){		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {for(Entry<N,Set<D>>seed:initialSeeds.entrySet()){			N startPoint = seed.getKey();NstartPoint=seed.getKey();			for(D val: seed.getValue())for(Dval:seed.getValue())



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

154
				propagate(new PathEdge<>(zeroValue, startPoint, val), null, false);propagate(newPathEdge<>(zeroValue,startPoint,val),null,false);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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

178

179

180

181

182

183
			jumpFn.addFunction(new PathEdge<N, D>(zeroValue, startPoint, zeroValue));jumpFn.addFunction(newPathEdge<N,D>(zeroValue,startPoint,zeroValue));		}}	}}	/**/**	 * Awaits the completion of the exploded super graph. When complete, computes result values,	 * Awaits the completion of the exploded super graph. When complete, computes result values,	 * shuts down the executor and returns.	 * shuts down the executor and returns.	 */	 */	protected void awaitCompletionComputeValuesAndShutdown() {protectedvoidawaitCompletionComputeValuesAndShutdown(){		{{			//run executor and await termination of tasks//run executor and await termination of tasks			runExecutorAndAwaitCompletion();runExecutorAndAwaitCompletion();		}}		if(logger.isDebugEnabled())if(logger.isDebugEnabled())			printStats();printStats();		//ask executor to shut down;//ask executor to shut down;		//this will cause new submissions to the executor to be rejected,//this will cause new submissions to the executor to be rejected,		//but at this point all tasks should have completed anyway//but at this point all tasks should have completed anyway		executor.shutdown();executor.shutdown();		//similarly here: we await termination, but this should happen instantaneously,//similarly here: we await termination, but this should happen instantaneously,		//as all tasks should have completed//as all tasks should have completed		runExecutorAndAwaitCompletion();runExecutorAndAwaitCompletion();	}}	/**/**	 * Runs execution, re-throwing exceptions that might be thrown during its execution.	 * Runs execution, re-throwing exceptions that might be thrown during its execution.	 */	 */	private void runExecutorAndAwaitCompletion() {privatevoidrunExecutorAndAwaitCompletion(){



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

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

194
//		try {//		try {//			executor.awaitCompletion();//			executor.awaitCompletion();//		} catch (InterruptedException e) {//		} catch (InterruptedException e) {//			e.printStackTrace();//			e.printStackTrace();//		}//		}//		Throwable exception = executor.getException();//		Throwable exception = executor.getException();//		if(exception!=null) {//		if(exception!=null) {//			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);//			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);//		}//		}		while(!worklist.isEmpty()) {while(!worklist.isEmpty()){			worklist.pop().run();worklist.pop().run();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

195

196

197

198

199

200

201

202

203

204
		}}	}}    /**/**     * Dispatch the processing of a given edge. It may be executed in a different thread.     * Dispatch the processing of a given edge. It may be executed in a different thread.     * @param edge the edge to process     * @param edge the edge to process     */     */    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){protectedvoidscheduleEdgeProcessing(PathEdge<N,D>edge){    	// If the executor has been killed, there is little point// If the executor has been killed, there is little point    	// in submitting new tasks// in submitting new tasks



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

205

206

207

208
//    	if (executor.isTerminating())//    	if (executor.isTerminating())//    		return;//    		return;//    	executor.execute(new PathEdgeProcessingTask(edge));//    	executor.execute(new PathEdgeProcessingTask(edge));    	worklist.add(new PathEdgeProcessingTask(edge));worklist.add(newPathEdgeProcessingTask(edge));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

209

210

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

229

230

231

232

233
    	propagationCount++;propagationCount++;    }}		/**/**	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.	 * 	 * 	 * For each possible callee, registers incoming call edges.	 * For each possible callee, registers incoming call edges.	 * Also propagates call-to-return flows and summarized callee flows within the caller. 	 * Also propagates call-to-return flows and summarized callee flows within the caller. 	 * 	 * 	 * @param edge an edge whose target node resembles a method call	 * @param edge an edge whose target node resembles a method call	 */	 */	private void processCall(PathEdge<N,D> edge) {privatevoidprocessCall(PathEdge<N,D>edge){		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); // a call node; line 14...finalNn=edge.getTarget();// a call node; line 14...        logger.trace("Processing call to {}", n);logger.trace("Processing call to {}",n);		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();		assert d2 != null;assertd2!=null;		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);Collection<N>returnSiteNs=icfg.getReturnSitesOfCallAt(n);				//for each possible callee//for each possible callee		Collection<M> callees = icfg.getCalleesOfCallAt(n);Collection<M>callees=icfg.getCalleesOfCallAt(n);		for(M sCalledProcN: callees) { //still line 14for(MsCalledProcN:callees){//still line 14			//compute the call-flow function//compute the call-flow function



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

234
			FlowFunction<FieldRef, D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);FlowFunction<FieldRef,D>function=flowFunctions.getCallFlowFunction(n,sCalledProcN);



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

235
			Set<ConstrainedFact<FieldRef, D>> res = computeCallFlowFunction(function, d1, d2);Set<ConstrainedFact<FieldRef,D>>res=computeCallFlowFunction(function,d1,d2);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

236

237

238
						Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);Collection<N>startPointsOf=icfg.getStartPointsOf(sCalledProcN);			//for each result node of the call-flow function//for each result node of the call-flow function



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

239
			for(ConstrainedFact<FieldRef, D> d3: res) {for(ConstrainedFact<FieldRef,D>d3:res){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

240

241

242
				//for each callee's start point(s)//for each callee's start point(s)				for(N sP: startPointsOf) {for(NsP:startPointsOf){					//create initial self-loop//create initial self-loop



avoiding unnecessary calls to clone a fact


 

 


Johannes Lerch
committed
Jan 08, 2015



avoiding unnecessary calls to clone a fact


 

 

avoiding unnecessary calls to clone a fact

 

Johannes Lerch
committed
Jan 08, 2015

243
					D abstractStartPointFact = AccessPathUtil.cloneWithAccessPath(d3.getFact(), new AccessPath<FieldRef>());DabstractStartPointFact=AccessPathUtil.cloneWithAccessPath(d3.getFact(),newAccessPath<FieldRef>());



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

244
					propagate(new PathEdge<>(abstractStartPointFact, sP, abstractStartPointFact), n, false); //line 15propagate(newPathEdge<>(abstractStartPointFact,sP,abstractStartPointFact),n,false);//line 15



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

245

246

247

248
				}}								//register the fact that <sp,d3> has an incoming edge from <n,d2>//register the fact that <sp,d3> has an incoming edge from <n,d2>				//line 15.1 of Naeem/Lhotak/Rodriguez//line 15.1 of Naeem/Lhotak/Rodriguez



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

249

250
				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);IncomingEdge<D,N>incomingEdge=newIncomingEdge<D,N>(d3.getFact(),n,d1,d2);				if (!addIncoming(sCalledProcN, incomingEdge))if(!addIncoming(sCalledProcN,incomingEdge))



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

251

252
					continue;continue;				



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

253
				resumeEdges(sCalledProcN, d3.getFact());resumeEdges(sCalledProcN,d3.getFact());



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

254

255
				registerInterestedCaller(sCalledProcN, incomingEdge);registerInterestedCaller(sCalledProcN,incomingEdge);				



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

256

257
								//line 15.2//line 15.2



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

258
				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());Set<SummaryEdge<D,N>>endSumm=endSummary(sCalledProcN,d3.getFact());



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

259

260

261

262

263

264
									//still line 15.2 of Naeem/Lhotak/Rodriguez//still line 15.2 of Naeem/Lhotak/Rodriguez				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,//for each already-queried exit value <eP,d4> reachable from <sP,d3>,				//create new caller-side jump functions to the return sites//create new caller-side jump functions to the return sites				//because we have observed a potentially new incoming edge into <sP,d3>//because we have observed a potentially new incoming edge into <sP,d3>				if (endSumm != null)if(endSumm!=null)



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

265
					for(SummaryEdge<D, N> summary: endSumm) {for(SummaryEdge<D,N>summary:endSumm){



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

266

267

268

269

270

271

272

273

274

275
						Optional<D> d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);Optional<D>d4=AccessPathUtil.applyAbstractedSummary(d3.getFact(),summary);						if(d4.isPresent()) {if(d4.isPresent()){							//for each return site//for each return site							for(N retSiteN: returnSiteNs) {for(NretSiteN:returnSiteNs){								//compute return-flow function//compute return-flow function								FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);FlowFunction<FieldRef,D>retFunction=flowFunctions.getReturnFlowFunction(n,sCalledProcN,summary.getTargetStmt(),retSiteN);								//for each target value of the function//for each target value of the function								for(ConstrainedFact<FieldRef, D> d5: computeReturnFlowFunction(retFunction, d4.get(), n)) {for(ConstrainedFact<FieldRef,D>d5:computeReturnFlowFunction(retFunction,d4.get(),n)){									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());Dd5p_restoredCtx=restoreContextOnReturnedFact(d2,d5.getFact());									propagate(new PathEdge<>(d1, retSiteN, d5p_restoredCtx), n, false);propagate(newPathEdge<>(d1,retSiteN,d5p_restoredCtx),n,false);



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

276

277
								}}							}}



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

278
						}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

279

280

281

282

283

284
					}}			}}		}}		//line 17-19 of Naeem/Lhotak/Rodriguez		//line 17-19 of Naeem/Lhotak/Rodriguez				//process intra-procedural flows along call-to-return flow functions//process intra-procedural flows along call-to-return flow functions		for (N returnSiteN : returnSiteNs) {for(NreturnSiteN:returnSiteNs){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

285
			FlowFunction<FieldRef, D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);FlowFunction<FieldRef,D>callToReturnFlowFunction=flowFunctions.getCallToReturnFlowFunction(n,returnSiteN);



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

286
			for(ConstrainedFact<FieldRef, D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))for(ConstrainedFact<FieldRef,D>d3:computeCallToReturnFlowFunction(callToReturnFlowFunction,d1,d2))



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

287
				propagate(new PathEdge<>(d1, returnSiteN, d3.getFact()), n, false);propagate(newPathEdge<>(d1,returnSiteN,d3.getFact()),n,false);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

288

289

290
		}}	}}



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

291

292
	private void resumeEdges(M method, D factAtMethodStartPoint) {privatevoidresumeEdges(Mmethod,DfactAtMethodStartPoint){		//TODO: Check for concurrency issues//TODO: Check for concurrency issues



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

293
		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.get(method);ConcurrentHashSet<PathEdge<N,D>>edges=pausedEdges.get(method);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

294

295
		if(edges != null) {if(edges!=null){			for(PathEdge<N, D> edge : edges) {for(PathEdge<N,D>edge:edges){



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

296
				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) == PrefixTestResult.GUARANTEED_PREFIX) {if(AccessPathUtil.isPrefixOf(edge.factAtSource(),factAtMethodStartPoint)==PrefixTestResult.GUARANTEED_PREFIX){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

297

298
					if(edges.remove(edge))  {if(edges.remove(edge)){						logger.trace("RESUME-EDGE: {}", edge);logger.trace("RESUME-EDGE: {}",edge);



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

299
						propagate(edge, edge instanceof ConcretizationPathEdge ? edge.getTarget() : null, false);propagate(edge,edgeinstanceofConcretizationPathEdge?edge.getTarget():null,false);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

300

301

302

303

304
					}}				}}			}}		}}	}}



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

305

306

307

308
		private void registerInterestedCaller(M method, IncomingEdge<D, N> incomingEdge) {privatevoidregisterInterestedCaller(Mmethod,IncomingEdge<D,N>incomingEdge){		Set<PathEdge<N, D>> edges = pausedEdges.get(method);Set<PathEdge<N,D>>edges=pausedEdges.get(method);		if(edges != null) {if(edges!=null){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

309
			for(final PathEdge<N, D> edge : edges) {for(finalPathEdge<N,D>edge:edges){



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

310
				if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), edge.factAtSource()).atLeast(PrefixTestResult.POTENTIAL_PREFIX)) {if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(),edge.factAtSource()).atLeast(PrefixTestResult.POTENTIAL_PREFIX)){



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

311

312
					logger.trace("RECHECKING-PAUSED-EDGE: {} for new incoming edge {}", edge, incomingEdge);logger.trace("RECHECKING-PAUSED-EDGE: {} for new incoming edge {}",edge,incomingEdge);					



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

313

314
					Constraint<FieldRef> constraint = new DeltaConstraint<FieldRef>(incomingEdge.getCalleeSourceFact().getAccessPath(), edge.factAtSource().getAccessPath());Constraint<FieldRef>constraint=newDeltaConstraint<FieldRef>(incomingEdge.getCalleeSourceFact().getAccessPath(),edge.factAtSource().getAccessPath());					propagateConstrained(new ConcretizationPathEdge<>(propagateConstrained(newConcretizationPathEdge<>(



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

315

316
							applyConstraint(constraint, incomingEdge.getCallerSourceFact()), applyConstraint(constraint,incomingEdge.getCallerSourceFact()),							incomingEdge.getCallSite(), incomingEdge.getCallSite(),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

317

318
							applyConstraint(constraint, incomingEdge.getCallerCallSiteFact()),applyConstraint(constraint,incomingEdge.getCallerCallSiteFact()),							method,method,



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

319
							applyConstraint(constraint, incomingEdge.getCalleeSourceFact())));applyConstraint(constraint,incomingEdge.getCalleeSourceFact())));



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

320

321

322

323
				}}			}}		}}	}}



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

324




FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

325

326

327

328

329

330

331
	/**/**	 * Computes the call flow function for the given call-site abstraction	 * Computes the call flow function for the given call-site abstraction	 * @param callFlowFunction The call flow function to compute	 * @param callFlowFunction The call flow function to compute	 * @param d1 The abstraction at the current method's start node.	 * @param d1 The abstraction at the current method's start node.	 * @param d2 The abstraction at the call site	 * @param d2 The abstraction at the call site	 * @return The set of caller-side abstractions at the callee's start node	 * @return The set of caller-side abstractions at the callee's start node	 */	 */



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

332
	protected Set<ConstrainedFact<FieldRef, D>> computeCallFlowFunctionprotectedSet<ConstrainedFact<FieldRef,D>>computeCallFlowFunction



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

333
			(FlowFunction<FieldRef, D> callFlowFunction, D d1, D d2) {(FlowFunction<FieldRef,D>callFlowFunction,Dd1,Dd2){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
		return callFlowFunction.computeTargets(d2);returncallFlowFunction.computeTargets(d2);	}}	/**/**	 * Computes the call-to-return flow function for the given call-site	 * Computes the call-to-return flow function for the given call-site	 * abstraction	 * abstraction	 * @param callToReturnFlowFunction The call-to-return flow function to	 * @param callToReturnFlowFunction The call-to-return flow function to	 * compute	 * compute	 * @param d1 The abstraction at the current method's start node.	 * @param d1 The abstraction at the current method's start node.	 * @param d2 The abstraction at the call site	 * @param d2 The abstraction at the call site	 * @return The set of caller-side abstractions at the return site	 * @return The set of caller-side abstractions at the return site	 */	 */



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

346
	protected Set<ConstrainedFact<FieldRef, D>> computeCallToReturnFlowFunctionprotectedSet<ConstrainedFact<FieldRef,D>>computeCallToReturnFlowFunction



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

347
			(FlowFunction<FieldRef, D> callToReturnFlowFunction, D d1, D d2) {(FlowFunction<FieldRef,D>callToReturnFlowFunction,Dd1,Dd2){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

348

349

350

351

352

353

354

355

356

357

358

359

360

361

362

363

364

365

366

367

368

369

370
		return callToReturnFlowFunction.computeTargets(d2);returncallToReturnFlowFunction.computeTargets(d2);	}}		/**/**	 * Lines 21-32 of the algorithm.	 * Lines 21-32 of the algorithm.	 * 	 * 	 * Stores callee-side summaries.	 * Stores callee-side summaries.	 * Also, at the side of the caller, propagates intra-procedural flows to return sites	 * Also, at the side of the caller, propagates intra-procedural flows to return sites	 * using those newly computed summaries.	 * using those newly computed summaries.	 * 	 * 	 * @param edge an edge whose target node resembles a method exits	 * @param edge an edge whose target node resembles a method exits	 */	 */	protected void processExit(PathEdge<N,D> edge) {protectedvoidprocessExit(PathEdge<N,D>edge){		final N n = edge.getTarget(); // an exit node; line 21...finalNn=edge.getTarget();// an exit node; line 21...		M methodThatNeedsSummary = icfg.getMethodOf(n);MmethodThatNeedsSummary=icfg.getMethodOf(n);				final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();				//for each of the method's start points, determine incoming calls//for each of the method's start points, determine incoming calls				//line 21.1 of Naeem/Lhotak/Rodriguez//line 21.1 of Naeem/Lhotak/Rodriguez		//register end-summary//register end-summary



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

371

372
		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);SummaryEdge<D,N>summaryEdge=newSummaryEdge<D,N>(d1,n,d2);		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))if(!addEndSummary(methodThatNeedsSummary,summaryEdge))



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

373
			return; //FIXME: should never be reached?! -> assert ?return;//FIXME: should never be reached?! -> assert ?



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

374

375

376
				//for each incoming call edge already processed//for each incoming call edge already processed		//(see processCall(..))//(see processCall(..))



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

377

378

379

380

381

382
		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {for(IncomingEdge<D,N>incomingEdge:incoming(methodThatNeedsSummary)){			// line 22// line 22			N callSite = incomingEdge.getCallSite();NcallSite=incomingEdge.getCallSite();			// for each return site// for each return site			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(callSite)){				// compute return-flow function// compute return-flow function



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

383
				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);FlowFunction<FieldRef,D>retFunction=flowFunctions.getReturnFlowFunction(callSite,methodThatNeedsSummary,n,retSiteC);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

384
				



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

385
				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX) {if(AccessPathUtil.isPrefixOf(d1,incomingEdge.getCalleeSourceFact())==PrefixTestResult.GUARANTEED_PREFIX){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

386

387
					Optional<D> concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);Optional<D>concreteCalleeExitFact=AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(),summaryEdge);					if(concreteCalleeExitFact.isPresent()) {if(concreteCalleeExitFact.isPresent()){



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

388
						Set<ConstrainedFact<FieldRef, D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact.get(), callSite);Set<ConstrainedFact<FieldRef,D>>callerTargetFacts=computeReturnFlowFunction(retFunction,concreteCalleeExitFact.get(),callSite);



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

389

390
							// for each incoming-call value// for each incoming-call value



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

391
						for (ConstrainedFact<FieldRef, D> callerTargetAnnotatedFact : callerTargetFacts) {for(ConstrainedFact<FieldRef,D>callerTargetAnnotatedFact:callerTargetFacts){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

392
							D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());DcallerTargetFact=restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(),callerTargetAnnotatedFact.getFact());



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

393
							propagate(new PathEdge<>(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact), callSite, false);propagate(newPathEdge<>(incomingEdge.getCallerSourceFact(),retSiteC,callerTargetFact),callSite,false);



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

394
						}}



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

395
					}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

396

397
				}}			}}



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

398

399
		}}		



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

400

401

402

403
				//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only		//be propagated into callers that have an incoming edge for this condition//be propagated into callers that have an incoming edge for this condition



bug fix in unbalanced return handling


 

 


Johannes Lerch
committed
Jan 29, 2015



bug fix in unbalanced return handling


 

 

bug fix in unbalanced return handling

 

Johannes Lerch
committed
Jan 29, 2015

404
		if(followReturnsPastSeeds && d1 == zeroValue) {if(followReturnsPastSeeds&&d1==zeroValue){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

405

406

407
			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);Collection<N>callers=icfg.getCallersOf(methodThatNeedsSummary);			for(N c: callers) {for(Nc:callers){				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(c)){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

408
					FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);FlowFunction<FieldRef,D>retFunction=flowFunctions.getReturnFlowFunction(c,methodThatNeedsSummary,n,retSiteC);



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

409

410
					Set<ConstrainedFact<FieldRef, D>> targets = computeReturnFlowFunction(retFunction, d2, c);Set<ConstrainedFact<FieldRef,D>>targets=computeReturnFlowFunction(retFunction,d2,c);					for(ConstrainedFact<FieldRef, D> d5: targets)for(ConstrainedFact<FieldRef,D>d5:targets)



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

411
						propagate(new PathEdge<>(zeroValue, retSiteC, d5.getFact()), c, true);propagate(newPathEdge<>(zeroValue,retSiteC,d5.getFact()),c,true);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

412

413

414

415

416

417
				}}			}}			//in cases where there are no callers, the return statement would normally not be processed at all;//in cases where there are no callers, the return statement would normally not be processed at all;			//this might be undesirable if the flow function has a side effect such as registering a taint;//this might be undesirable if the flow function has a side effect such as registering a taint;			//instead we thus call the return flow function will a null caller//instead we thus call the return flow function will a null caller			if(callers.isEmpty()) {if(callers.isEmpty()){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

418
				FlowFunction<FieldRef, D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);FlowFunction<FieldRef,D>retFunction=flowFunctions.getReturnFlowFunction(null,methodThatNeedsSummary,n,null);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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

431
				retFunction.computeTargets(d2);retFunction.computeTargets(d2);			}}		}}	}}		/**/**	 * Computes the return flow function for the given set of caller-side	 * Computes the return flow function for the given set of caller-side	 * abstractions.	 * abstractions.	 * @param retFunction The return flow function to compute	 * @param retFunction The return flow function to compute	 * @param d2 The abstraction at the exit node in the callee	 * @param d2 The abstraction at the exit node in the callee	 * @param callSite The call site	 * @param callSite The call site	 * @return The set of caller-side abstractions at the return site	 * @return The set of caller-side abstractions at the return site	 */	 */



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

432
	protected Set<ConstrainedFact<FieldRef, D>> computeReturnFlowFunctionprotectedSet<ConstrainedFact<FieldRef,D>>computeReturnFlowFunction



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

433
			(FlowFunction<FieldRef, D> retFunction, D d2, N callSite) {(FlowFunction<FieldRef,D>retFunction,Dd2,NcallSite){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

434

435

436

437

438

439

440

441

442

443

444

445

446

447
		return retFunction.computeTargets(d2);returnretFunction.computeTargets(d2);	}}	/**/**	 * Lines 33-37 of the algorithm.	 * Lines 33-37 of the algorithm.	 * Simply propagate normal, intra-procedural flows.	 * Simply propagate normal, intra-procedural flows.	 * @param edge	 * @param edge	 */	 */	private void processNormalFlow(PathEdge<N,D> edge) {privatevoidprocessNormalFlow(PathEdge<N,D>edge){		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); finalNn=edge.getTarget();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();				for (N m : icfg.getSuccsOf(n)) {for(Nm:icfg.getSuccsOf(n)){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

448
			FlowFunction<FieldRef, D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);FlowFunction<FieldRef,D>flowFunction=flowFunctions.getNormalFlowFunction(n,m);



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

449

450
			Set<ConstrainedFact<FieldRef, D>> res = computeNormalFlowFunction(flowFunction, d1, d2);Set<ConstrainedFact<FieldRef,D>>res=computeNormalFlowFunction(flowFunction,d1,d2);			for (ConstrainedFact<FieldRef, D> d3 : res) {for(ConstrainedFact<FieldRef,D>d3:res){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

451
				if(d3.getConstraint() != null) {if(d3.getConstraint()!=null){



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

452
					propagateConstrained(new PathEdge<>(applyConstraint(d3.getConstraint(), d1), m, d3.getFact()));propagateConstrained(newPathEdge<>(applyConstraint(d3.getConstraint(),d1),m,d3.getFact()));



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

453
				}}



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

454
				elseelse



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

455
					propagate(new PathEdge<>(d1, m, d3.getFact()), null, false);propagate(newPathEdge<>(d1,m,d3.getFact()),null,false);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

456

457

458

459
			}}		}}	}}	



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

460

461

462

463

464
	private D applyConstraint(Constraint<FieldRef> constraint, D fact) {privateDapplyConstraint(Constraint<FieldRef>constraint,Dfact){		if(fact.equals(zeroValue))if(fact.equals(zeroValue))			return zeroValue;returnzeroValue;		elseelse			return fact.cloneWithAccessPath(constraint.applyToAccessPath(fact.getAccessPath()));returnfact.cloneWithAccessPath(constraint.applyToAccessPath(fact.getAccessPath()));



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

465
	}}



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

466
	



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

467

468

469

470

471
	private boolean propagateConstrained(PathEdge<N, D> pathEdge) {privatebooleanpropagateConstrained(PathEdge<N,D>pathEdge){		return propagateConstrained(pathEdge, new HashMap<N, Boolean>());returnpropagateConstrained(pathEdge,newHashMap<N,Boolean>());	}}		private boolean propagateConstrained(PathEdge<N, D> pathEdge, Map<N, Boolean> visited) {privatebooleanpropagateConstrained(PathEdge<N,D>pathEdge,Map<N,Boolean>visited){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

472
		M calleeMethod = icfg.getMethodOf(pathEdge.getTarget());McalleeMethod=icfg.getMethodOf(pathEdge.getTarget());



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

473
		logger.trace("Checking interest at method {} in fact {}", calleeMethod, pathEdge.factAtSource());logger.trace("Checking interest at method {} in fact {}",calleeMethod,pathEdge.factAtSource());



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

474




cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

475

476

477
		boolean propagate = false;booleanpropagate=false;		if(pathEdge.factAtSource().equals(zeroValue))if(pathEdge.factAtSource().equals(zeroValue))			propagate = true;propagate=true;



Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.


 

 

Do not check interest of callers if paused edge already available.

 

Johannes Lerch
committed
Jan 16, 2015

478

479
		else if(hasPausedEdges(calleeMethod, pathEdge))elseif(hasPausedEdges(calleeMethod,pathEdge))			propagate = false;propagate=false;



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

480

481
		else {else{			Set<N> callSitesWithInterest = Sets.newHashSet();Set<N>callSitesWithInterest=Sets.newHashSet();



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

482
			for(IncomingEdge<D, N> incEdge : incomingEdgesPrefixedWith(calleeMethod, pathEdge.factAtSource())) { //guaranteedfor(IncomingEdge<D,N>incEdge:incomingEdgesPrefixedWith(calleeMethod,pathEdge.factAtSource())){//guaranteed



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

483
				callSitesWithInterest.add(incEdge.getCallSite());callSitesWithInterest.add(incEdge.getCallSite());



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

484
			}}



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

485

486
			propagate = !callSitesWithInterest.isEmpty();propagate=!callSitesWithInterest.isEmpty();			



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

487
			for(IncomingEdge<D, N> incEdge : incomingEdgesPotentialPrefixesOf(calleeMethod, pathEdge.factAtSource())) { //potentialfor(IncomingEdge<D,N>incEdge:incomingEdgesPotentialPrefixesOf(calleeMethod,pathEdge.factAtSource())){//potential



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

488

489

490

491

492
				if(visited.containsKey(incEdge.getCallSite())) {if(visited.containsKey(incEdge.getCallSite())){					if(visited.get(incEdge.getCallSite()) != null)if(visited.get(incEdge.getCallSite())!=null)						propagate |= visited.get(incEdge.getCallSite());propagate|=visited.get(incEdge.getCallSite());				}}				else {else{



Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.


 

 

Do not check interest of callers if paused edge already available.

 

Johannes Lerch
committed
Jan 16, 2015

493
					if(!callSitesWithInterest.contains(incEdge.getCallSite())) {if(!callSitesWithInterest.contains(incEdge.getCallSite())){



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

494
						Constraint<FieldRef> callerConstraint = new DeltaConstraint<FieldRef>(incEdge.getCalleeSourceFact().getAccessPath(), pathEdge.factAtSource().getAccessPath());Constraint<FieldRef>callerConstraint=newDeltaConstraint<FieldRef>(incEdge.getCalleeSourceFact().getAccessPath(),pathEdge.factAtSource().getAccessPath());



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

495
						



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

496

497

498

499

500
						PathEdge<N,D> callerEdge = new ConcretizationPathEdge<>(PathEdge<N,D>callerEdge=newConcretizationPathEdge<>(								applyConstraint(callerConstraint, incEdge.getCallerSourceFact()), applyConstraint(callerConstraint,incEdge.getCallerSourceFact()),								incEdge.getCallSite(), incEdge.getCallSite(),								applyConstraint(callerConstraint, incEdge.getCallerCallSiteFact()),applyConstraint(callerConstraint,incEdge.getCallerCallSiteFact()),								calleeMethod,calleeMethod,



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

501
								applyConstraint(callerConstraint, incEdge.getCalleeSourceFact()));applyConstraint(callerConstraint,incEdge.getCalleeSourceFact()));



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

502

503

504

505

506
						visited.put(incEdge.getCallSite(), null);visited.put(incEdge.getCallSite(),null);						boolean result = propagateConstrained(callerEdge, visited);booleanresult=propagateConstrained(callerEdge,visited);						visited.put(incEdge.getCallSite(), result);visited.put(incEdge.getCallSite(),result);						propagate |= result;propagate|=result;					}}



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

507
				}}



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

508
			}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

509
		}}



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

510
		



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

511
		if(propagate) {if(propagate){



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

512
			propagate(pathEdge, pathEdge instanceof ConcretizationPathEdge ? pathEdge.getTarget() : null, false);propagate(pathEdge,pathEdgeinstanceofConcretizationPathEdge?pathEdge.getTarget():null,false);



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

513

514

515

516

517

518
			return true;returntrue;		} else {}else{			pauseEdge(pathEdge);pauseEdge(pathEdge);			return false;returnfalse;		}}	}}



Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.


 

 

Do not check interest of callers if paused edge already available.

 

Johannes Lerch
committed
Jan 16, 2015

519

520

521

522

523
	private boolean hasPausedEdges(M calleeMethod, PathEdge<N, D> pathEdge) {privatebooleanhasPausedEdges(McalleeMethod,PathEdge<N,D>pathEdge){		ConcurrentHashSet<PathEdge<N, D>> pe = pausedEdges.get(calleeMethod);ConcurrentHashSet<PathEdge<N,D>>pe=pausedEdges.get(calleeMethod);		if(pe != null) {if(pe!=null){			for(PathEdge<N, D> edge : pe) {for(PathEdge<N,D>edge:pe){



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

524
				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), pathEdge.factAtSource()) == PrefixTestResult.GUARANTEED_PREFIX)if(AccessPathUtil.isPrefixOf(edge.factAtSource(),pathEdge.factAtSource())==PrefixTestResult.GUARANTEED_PREFIX)



Do not check interest of callers if paused edge already available.


 

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.


 

 

Do not check interest of callers if paused edge already available.

 

Johannes Lerch
committed
Jan 16, 2015

525

526

527

528

529

530
					return true;returntrue;			}}		}}		return false;returnfalse;	}}



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

531

532
	private void pauseEdge(PathEdge<N,D> edge) {privatevoidpauseEdge(PathEdge<N,D>edge){		M method = icfg.getMethodOf(edge.getTarget());Mmethod=icfg.getMethodOf(edge.getTarget());



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

533
		ConcurrentHashSet<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());ConcurrentHashSet<PathEdge<N,D>>edges=pausedEdges.putIfAbsentElseGet(method,newConcurrentHashSet<PathEdge<N,D>>());



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

534

535

536
		if(edges.add(edge)) {if(edges.add(edge)){			logger.trace("PAUSED: {}: {}", method, edge);logger.trace("PAUSED: {}: {}",method,edge);		}}



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

537

538
	}}	



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

539

540

541

542

543

544

545

546
	/**/**	 * Computes the normal flow function for the given set of start and end	 * Computes the normal flow function for the given set of start and end	 * abstractions.	 * abstractions.	 * @param flowFunction The normal flow function to compute	 * @param flowFunction The normal flow function to compute	 * @param d1 The abstraction at the method's start node	 * @param d1 The abstraction at the method's start node	 * @param d1 The abstraction at the current node	 * @param d1 The abstraction at the current node	 * @return The set of abstractions at the successor node	 * @return The set of abstractions at the successor node	 */	 */



Bugfix, Helper functions, and renaming


 

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming


 

 

Bugfix, Helper functions, and renaming

 

Johannes Lerch
committed
Jan 08, 2015

547
	protected Set<ConstrainedFact<FieldRef, D>> computeNormalFlowFunctionprotectedSet<ConstrainedFact<FieldRef,D>>computeNormalFlowFunction



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

548
			(FlowFunction<FieldRef, D> flowFunction, D d1, D d2) {(FlowFunction<FieldRef,D>flowFunction,Dd1,Dd2){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

549

550

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

567

568

569

570

571
		return flowFunction.computeTargets(d2);returnflowFunction.computeTargets(d2);	}}		/**/**	 * This method will be called for each incoming edge and can be used to	 * This method will be called for each incoming edge and can be used to	 * transfer knowledge from the calling edge to the returning edge, without	 * transfer knowledge from the calling edge to the returning edge, without	 * affecting the summary edges at the callee.	 * affecting the summary edges at the callee.	 * 	 * 	 * @param d4	 * @param d4	 *            Fact stored with the incoming edge, i.e., present at the	 *            Fact stored with the incoming edge, i.e., present at the	 *            caller side	 *            caller side	 * @param d5	 * @param d5	 *            Fact that originally should be propagated to the caller.	 *            Fact that originally should be propagated to the caller.	 * @return Fact that will be propagated to the caller.	 * @return Fact that will be propagated to the caller.	 */	 */	protected D restoreContextOnReturnedFact(D d4, D d5) {protectedDrestoreContextOnReturnedFact(Dd4,Dd5){		d5.setCallingContext(d4);d5.setCallingContext(d4);		return d5;returnd5;	}}			/**/**	 * Propagates the flow further down the exploded super graph. 	 * Propagates the flow further down the exploded super graph. 



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

572
	 * @param edge the PathEdge that should be propagated	 * @param edge the PathEdge that should be propagated



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

573

574

575

576

577
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})	 */	 */



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

578
	protected void propagate(PathEdge<N,D> edge,protectedvoidpropagate(PathEdge<N,D>edge,



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

579

580
			/* deliberately exposed to clients */ N relatedCallSite,/* deliberately exposed to clients */NrelatedCallSite,			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {/* deliberately exposed to clients */booleanisUnbalancedReturn){



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

581
		



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

582
		final D existingVal = jumpFn.addFunction(edge);finalDexistingVal=jumpFn.addFunction(edge);



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

583

584

585

586

587

588

589

590

591

592

593

594

595

596

597

598

599

600

601

602

603
				if(edge instanceof ConcretizationPathEdge) {if(edgeinstanceofConcretizationPathEdge){			ConcretizationPathEdge<M, N, D> concEdge = (ConcretizationPathEdge<M,N,D>) edge;ConcretizationPathEdge<M,N,D>concEdge=(ConcretizationPathEdge<M,N,D>)edge;			IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(concEdge.getCalleeSourceFact(), IncomingEdge<D,N>incomingEdge=newIncomingEdge<D,N>(concEdge.getCalleeSourceFact(),					concEdge.getTarget(), concEdge.factAtSource(), concEdge.factAtTarget());concEdge.getTarget(),concEdge.factAtSource(),concEdge.factAtTarget());			if (!addIncoming(concEdge.getCalleeMethod(), incomingEdge))if(!addIncoming(concEdge.getCalleeMethod(),incomingEdge))				return;return;						resumeEdges(concEdge.getCalleeMethod(), concEdge.getCalleeSourceFact());resumeEdges(concEdge.getCalleeMethod(),concEdge.getCalleeSourceFact());			registerInterestedCaller(concEdge.getCalleeMethod(), incomingEdge);registerInterestedCaller(concEdge.getCalleeMethod(),incomingEdge);		} else {}else{			//TODO: Merge d.* and d.*\{x} as d.*//TODO: Merge d.* and d.*\{x} as d.*			if (existingVal != null) {if(existingVal!=null){				if (existingVal != edge.factAtTarget())if(existingVal!=edge.factAtTarget())					existingVal.addNeighbor(edge.factAtTarget());existingVal.addNeighbor(edge.factAtTarget());			}}			else {else{				scheduleEdgeProcessing(edge);scheduleEdgeProcessing(edge);				if(edge.factAtTarget()!=zeroValue)if(edge.factAtTarget()!=zeroValue)					logger.trace("EDGE: {}: {}", icfg.getMethodOf(edge.getTarget()), edge);logger.trace("EDGE: {}: {}",icfg.getMethodOf(edge.getTarget()),edge);			}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

604

605

606
		}}	}}



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

607

608

609

610

611

612

613

614
	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {privateSet<SummaryEdge<D,N>>endSummary(Mm,finalDd3){		Set<SummaryEdge<D, N>> map = endSummary.get(m);Set<SummaryEdge<D,N>>map=endSummary.get(m);		if(map == null)if(map==null)			return null;returnnull;				return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {returnSets.filter(map,newPredicate<SummaryEdge<D,N>>(){			@Override@Override			public boolean apply(SummaryEdge<D, N> edge) {publicbooleanapply(SummaryEdge<D,N>edge){



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

615
				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) == PrefixTestResult.GUARANTEED_PREFIX;returnAccessPathUtil.isPrefixOf(edge.getSourceFact(),d3)==PrefixTestResult.GUARANTEED_PREFIX;



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

616

617
			}}		});});



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

618

619
	}}



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

620

621

622

623
	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {privatebooleanaddEndSummary(Mm,SummaryEdge<D,N>summaryEdge){		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGetSet<SummaryEdge<D,N>>summaries=endSummary.putIfAbsentElseGet				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());(m,newConcurrentHashSet<SummaryEdge<D,N>>());		return summaries.add(summaryEdge);returnsummaries.add(summaryEdge);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

624

625
	}	}



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

626

627
	protected Set<IncomingEdge<D, N>> incoming(M m) {protectedSet<IncomingEdge<D,N>>incoming(Mm){		Set<IncomingEdge<D, N>> result = incoming.get(m);Set<IncomingEdge<D,N>>result=incoming.get(m);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

628
		if(result == null)if(result==null)



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

629
			return Collections.emptySet();returnCollections.emptySet();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

630

631

632

633
		elseelse			return result;returnresult;	}}	



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

634

635

636

637

638
	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {protectedSet<IncomingEdge<D,N>>incomingEdgesPrefixedWith(Mm,finalDfact){		Set<IncomingEdge<D, N>> result = incoming(m);Set<IncomingEdge<D,N>>result=incoming(m);		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {returnSets.filter(result,newPredicate<IncomingEdge<D,N>>(){			@Override@Override			public boolean apply(IncomingEdge<D, N> edge) {publicbooleanapply(IncomingEdge<D,N>edge){



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

639
				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact()) == PrefixTestResult.GUARANTEED_PREFIX;returnAccessPathUtil.isPrefixOf(fact,edge.getCalleeSourceFact())==PrefixTestResult.GUARANTEED_PREFIX;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

640

641

642

643
			}}		});});	}}	



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

644
	protected Set<IncomingEdge<D, N>> incomingEdgesPotentialPrefixesOf(M m, final D fact) {protectedSet<IncomingEdge<D,N>>incomingEdgesPotentialPrefixesOf(Mm,finalDfact){



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

645

646

647

648
		Set<IncomingEdge<D, N>> result = incoming(m);Set<IncomingEdge<D,N>>result=incoming(m);		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {returnSets.filter(result,newPredicate<IncomingEdge<D,N>>(){			@Override@Override			public boolean apply(IncomingEdge<D, N> edge) {publicbooleanapply(IncomingEdge<D,N>edge){



Split prefix check into potential and guaranteed prefix


 

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix


 

 

Split prefix check into potential and guaranteed prefix

 

Johannes Lerch
committed
Jan 22, 2015

649
				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact).atLeast(PrefixTestResult.POTENTIAL_PREFIX);returnAccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(),fact).atLeast(PrefixTestResult.POTENTIAL_PREFIX);



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

650

651
			}}		});});



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

652

653
	}}	



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

654
	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {protectedbooleanaddIncoming(Mm,IncomingEdge<D,N>incomingEdge){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

655
		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);logger.trace("Incoming Edge for method {}: {}",m,incomingEdge);



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

656

657
		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());Set<IncomingEdge<D,N>>set=incoming.putIfAbsentElseGet(m,newConcurrentHashSet<IncomingEdge<D,N>>());		return set.add(incomingEdge);returnset.add(incomingEdge);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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

688

689

690

691

692

693

694

695

696

697

698

699

700

701

702

703

704

705

706

707

708
	}}		/**/**	 * Factory method for this solver's thread-pool executor.	 * Factory method for this solver's thread-pool executor.	 */	 */	protected CountingThreadPoolExecutor getExecutor() {protectedCountingThreadPoolExecutorgetExecutor(){		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());returnnewCountingThreadPoolExecutor(1,this.numThreads,30,TimeUnit.SECONDS,newLinkedBlockingQueue<Runnable>());	}}		/**/**	 * Returns a String used to identify the output of this solver in debug mode.	 * Returns a String used to identify the output of this solver in debug mode.	 * Subclasses can overwrite this string to distinguish the output from different solvers.	 * Subclasses can overwrite this string to distinguish the output from different solvers.	 */	 */	protected String getDebugName() {protectedStringgetDebugName(){		return "FAST IFDS SOLVER";return"FAST IFDS SOLVER";	}}	public void printStats() {publicvoidprintStats(){		if(logger.isDebugEnabled()) {if(logger.isDebugEnabled()){			if(ffCache!=null)if(ffCache!=null)				ffCache.printStats();ffCache.printStats();		} else {}else{			logger.info("No statistics were collected, as DEBUG is disabled.");logger.info("No statistics were collected, as DEBUG is disabled.");		}}	}}		private class PathEdgeProcessingTask implements Runnable {privateclassPathEdgeProcessingTaskimplementsRunnable{		private final PathEdge<N,D> edge;privatefinalPathEdge<N,D>edge;		public PathEdgeProcessingTask(PathEdge<N,D> edge) {publicPathEdgeProcessingTask(PathEdge<N,D>edge){			this.edge = edge;this.edge=edge;		}}		public void run() {publicvoidrun(){			if(icfg.isCallStmt(edge.getTarget())) {if(icfg.isCallStmt(edge.getTarget())){				processCall(edge);processCall(edge);			} else {}else{				//note that some statements, such as "throw" may be//note that some statements, such as "throw" may be				//both an exit statement and a "normal" statement//both an exit statement and a "normal" statement				if(icfg.isExitStmt(edge.getTarget())) {if(icfg.isExitStmt(edge.getTarget())){					processExit(edge);processExit(edge);				}}				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()){					processNormalFlow(edge);processNormalFlow(edge);				}}			}}		}}	}}	}}





