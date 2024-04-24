



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

8d40408f471bdaa24090e949fe134944c1fca13e

















8d40408f471bdaa24090e949fe134944c1fca13e


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



26.2 KB









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




17




18




19




20




21




22




23




import static heros.alias.AccessPathUtil.cloneWithConcatenatedAccessPath;
import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;
import heros.alias.FieldReference.SpecificFieldReference;
import heros.alias.FlowFunction.AnnotatedFact;
import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




26




27




import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






28




import java.util.Set;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






29




30




31




32




33




34




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






35




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






36




import com.google.common.cache.CacheBuilder;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






37




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





public class FieldSensitiveIFDSSolver<N, BaseValue, D extends FieldSensitiveFact<BaseValue, D>, M, I extends InterproceduralCFG<N, M>> {


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






64




65




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






66




67




68




69




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






70




71




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






72




	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






73




74




	protected final MyConcurrentHashMap<M, Set<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, Set<PathEdge<N,D>>>();
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




194




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




205




206




207




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




218




219




220




221




222




223




	@DontSynchronize("stateless")
	protected final FlowFunctions<N, D, M> flowFunctions;
	
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
	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {
		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 
		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.flowFunctions(); 
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
		this.numThreads = Math.max(1,tabulationProblem.numThreads());
		this.executor = getExecutor();
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
				propagate(zeroValue, startPoint, val, null, false);
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
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;
    	executor.execute(new PathEdgeProcessingTask(edge));
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
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






224




			Set<AnnotatedFact<D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






228




			for(AnnotatedFact<D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






229




230




231




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






232




					D abstractStartPointFact = d3.getFact().cloneWithAccessPath();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






233




234




235




236




237




					propagate(abstractStartPointFact, sP, abstractStartPointFact, n, false); //line 15
				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






238




239




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






242




				resumeEdges(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






243




244




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






245




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






246




247




248




249




250




251




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






252




					for(SummaryEdge<D, N> summary: endSumm) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






253




254




255




256




257




258




259




260




261




262




263




264




265




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




276




277




						if(AccessPathUtil.isPrefixOf(summary.getSourceFact(), d3.getFact())) {
							D d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
							
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, d4, n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1, retSiteN, d5p_restoredCtx, n, false);
								}
							}
						} else {
							// incoming fact is prefix of summary: create new edge on caller side with complemented access path 
							D d1_concretized = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, d3.getFact());
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, summary.getTargetFact(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1_concretized, retSiteN, d5p_restoredCtx, n, false);
								}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






278




279




280




281




282




283




284




285




286




							}
						}
					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






287




288




			for(AnnotatedFact<D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))
				propagate(d1, returnSiteN, d3.getFact(), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






289




290




291




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






292




293




294




295




296




297




298




299




300




301




302




303




304




305




306




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {
				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) || AccessPathUtil.isPrefixOf(factAtMethodStartPoint, edge.factAtSource())) {
					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);
						propagate(edge.factAtSource(), edge.getTarget(), edge.factAtTarget(), null, false);
					}
				}
			}
		}
	}










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






307




308




309




310




311




312




313




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






314




	protected Set<AnnotatedFact<D>> computeCallFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






315




316




317




318




319




320




321




322




323




324




325




326




327




			(FlowFunction<D> callFlowFunction, D d1, D d2) {
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






328




	protected Set<AnnotatedFact<D>> computeCallToReturnFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




348




349




350




351




352




			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
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






353




354




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






355




356




357




358




			return;
		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






359




360




361




362




363




364




365




366




367




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);
				
				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact())) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






368




					D concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






369




					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact, callSite);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






370




371





					// for each incoming-call value









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






372




373




					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






374




375




						propagate(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact, callSite, false);
					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






376




				}









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




				else if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), d1)) {
					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, d2, callSite);

					// for each incoming-call value
					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






383




						D callerSourceFact = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, summaryEdge.getSourceFact());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




385




386




						propagate(callerSourceFact, retSiteC, callerTargetFact, callSite, false);
					}
				}				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






387




			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






388




389




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






390




391




392




393




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






394




		if(followReturnsPastSeeds && d1 == zeroValue && incoming(methodThatNeedsSummary).isEmpty()) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






395




396




397




398




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






399




400




401




					Set<AnnotatedFact<D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(AnnotatedFact<D> d5: targets)
						propagate(zeroValue, retSiteC, d5.getFact(), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




416




417




418




419




420




421




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






422




	protected Set<AnnotatedFact<D>> computeReturnFlowFunction









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






423




			(FlowFunction<D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






424




425




426




427




428




429




430




431




432




433




434




435




436




437




438




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
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






439




440




			Set<AnnotatedFact<D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (AnnotatedFact<D> d3 : res) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






441




442




443




				//TODO: double check if concurrency issues may arise
				
				//if reading field f









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






444




445




446




447




448




449




450




451




				// if d1.f element of incoming edges:
				//    create and propagate (d1.f, d2.f)
				// else 
				//	  create and set (d1.f, d2.f) on hold
				//	  create for each incoming edge inc: (inc.call-d1.f, inc.call-d2.f) and put on hold
				
				if(d3.getReadField() instanceof SpecificFieldReference) {
					SpecificFieldReference fieldRef = (SpecificFieldReference) d3.getReadField();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






452




453




					D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(d1, fieldRef);
					if(checkForInterestedCallers(d1, n, fieldRef)) {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






454




						propagate(concretizedSourceValue, m, d3.getFact(), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






455




456




					} else {
						pauseEdge(concretizedSourceValue, m, d3.getFact());









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






457




458




					}
				}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






459




				else {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






460




461




462




463




464




465




466




467




468




				
				//TODO: if writing field f
				// create edge e = (d1, d2.*\{f})
				// if d2.*\{f} element of incoming edges
				// 		continue with e
				// else 
				//		put e on hold
				// always kill (d1, d2)
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




480




481




482




483




484




485




486




487




488




489




490




491




492




493




494




495




496




497




498




499




500




501




502




503




504




505




506




					propagate(d1, m, d3.getFact(), null, false);
				}
			}
		}
	}
	
	private void pauseEdge(D sourceValue, N targetStmt, D targetValue) {
		M method = icfg.getMethodOf(targetStmt);
		Set<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());
		edges.add(new PathEdge<N,D>(sourceValue, targetStmt, targetValue));
		logger.trace("PAUSED: <{},{}> -> <{},{}>", method, sourceValue, targetStmt, targetValue);
	}

	private boolean checkForInterestedCallers(D calleeSourceFact, N targetStmt, SpecificFieldReference fieldRef) {
		M calleeMethod = icfg.getMethodOf(targetStmt);
		logger.trace("Checking interest at method {} in fact {} with field access {}", calleeMethod, calleeSourceFact, fieldRef);
		
		if(calleeSourceFact.equals(zeroValue))
			return true;
		
		if(hasInterestedCallers(calleeSourceFact, calleeMethod, fieldRef)) {
			return true;
		}
		
		Set<IncomingEdge<D, N>> inc = incomingEdgesPrefixesOf(calleeMethod, calleeSourceFact); 
		for (IncomingEdge<D, N> incomingEdge : inc) {
			if(checkForInterestedCallers(incomingEdge.getCallerSourceFact(), incomingEdge.getCallSite(), fieldRef)) {
				propagate(incomingEdge.getCallerSourceFact().equals(zeroValue) ? 
							incomingEdge.getCallerSourceFact() : 
							cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef), null, false); 
				return true;
			}
			else {
				pauseEdge(cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef));









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






507




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






508




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






509




510




511




512




513




514




515




516




		
		return false;
	}
	
	private boolean hasInterestedCallers(D calleeSourceFact, M calleeMethod, SpecificFieldReference fieldRef) {
		D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(calleeSourceFact, fieldRef);
		Set<IncomingEdge<D, N>> incomingEdges = incomingEdgesPrefixedWith(calleeMethod, concretizedSourceValue);
		return !incomingEdges.isEmpty();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






517




518




519




520




521




522




523




524




525




526




	}
	
	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






527




	protected Set<AnnotatedFact<D>> computeNormalFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






528




529




530




531




532




533




534




535




536




537




538




539




540




541




542




543




544




545




546




547




548




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




572




573




574




575




576




577




578




			(FlowFunction<D> flowFunction, D d1, D d2) {
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
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */
	protected void propagate(D sourceVal, N target, D targetVal,
			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {
		final PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);
		final D existingVal = jumpFn.addFunction(edge);
		//TODO: Merge d.* and d.x for arbitrary x as d.*
		//TODO: Merge d.* and d.*\{x} as d.*
		//TODO: Merge d.*\{a} and d.*/{b} as d.*
		if (existingVal != null) {
			if (existingVal != targetVal)
				existingVal.addNeighbor(targetVal);
		}
		else {
			scheduleEdgeProcessing(edge);
			if(targetVal!=zeroValue)
				logger.trace("EDGE: <{},{}> -> <{},{}>", icfg.getMethodOf(target), sourceVal, target, targetVal);
		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






579




580




581




582




583




584




585




586




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






587




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) || AccessPathUtil.isPrefixOf(d3, edge.getSourceFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






588




589




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






590




591




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






592




593




594




595




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






596




597




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






598




599




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






600




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






601




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






602




603




604




605




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






606




607




608




609




610




611




612




613




614




615




616




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {
				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact());
			}
		});
	}
	
	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






617




618




619




620




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






621




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






622




623




			}
		});









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




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






627




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






628




629




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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

8d40408f471bdaa24090e949fe134944c1fca13e

















8d40408f471bdaa24090e949fe134944c1fca13e


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



26.2 KB









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




17




18




19




20




21




22




23




import static heros.alias.AccessPathUtil.cloneWithConcatenatedAccessPath;
import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;
import heros.alias.FieldReference.SpecificFieldReference;
import heros.alias.FlowFunction.AnnotatedFact;
import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




26




27




import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






28




import java.util.Set;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






29




30




31




32




33




34




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






35




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






36




import com.google.common.cache.CacheBuilder;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






37




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





public class FieldSensitiveIFDSSolver<N, BaseValue, D extends FieldSensitiveFact<BaseValue, D>, M, I extends InterproceduralCFG<N, M>> {


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






64




65




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






66




67




68




69




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






70




71




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






72




	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






73




74




	protected final MyConcurrentHashMap<M, Set<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, Set<PathEdge<N,D>>>();
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




194




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




205




206




207




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




218




219




220




221




222




223




	@DontSynchronize("stateless")
	protected final FlowFunctions<N, D, M> flowFunctions;
	
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
	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {
		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 
		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.flowFunctions(); 
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
		this.numThreads = Math.max(1,tabulationProblem.numThreads());
		this.executor = getExecutor();
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
				propagate(zeroValue, startPoint, val, null, false);
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
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;
    	executor.execute(new PathEdgeProcessingTask(edge));
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
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






224




			Set<AnnotatedFact<D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






228




			for(AnnotatedFact<D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






229




230




231




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






232




					D abstractStartPointFact = d3.getFact().cloneWithAccessPath();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






233




234




235




236




237




					propagate(abstractStartPointFact, sP, abstractStartPointFact, n, false); //line 15
				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






238




239




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






242




				resumeEdges(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






243




244




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






245




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






246




247




248




249




250




251




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






252




					for(SummaryEdge<D, N> summary: endSumm) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






253




254




255




256




257




258




259




260




261




262




263




264




265




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




276




277




						if(AccessPathUtil.isPrefixOf(summary.getSourceFact(), d3.getFact())) {
							D d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
							
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, d4, n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1, retSiteN, d5p_restoredCtx, n, false);
								}
							}
						} else {
							// incoming fact is prefix of summary: create new edge on caller side with complemented access path 
							D d1_concretized = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, d3.getFact());
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, summary.getTargetFact(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1_concretized, retSiteN, d5p_restoredCtx, n, false);
								}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






278




279




280




281




282




283




284




285




286




							}
						}
					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






287




288




			for(AnnotatedFact<D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))
				propagate(d1, returnSiteN, d3.getFact(), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






289




290




291




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






292




293




294




295




296




297




298




299




300




301




302




303




304




305




306




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {
				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) || AccessPathUtil.isPrefixOf(factAtMethodStartPoint, edge.factAtSource())) {
					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);
						propagate(edge.factAtSource(), edge.getTarget(), edge.factAtTarget(), null, false);
					}
				}
			}
		}
	}










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






307




308




309




310




311




312




313




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






314




	protected Set<AnnotatedFact<D>> computeCallFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






315




316




317




318




319




320




321




322




323




324




325




326




327




			(FlowFunction<D> callFlowFunction, D d1, D d2) {
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






328




	protected Set<AnnotatedFact<D>> computeCallToReturnFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




348




349




350




351




352




			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
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






353




354




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






355




356




357




358




			return;
		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






359




360




361




362




363




364




365




366




367




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);
				
				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact())) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






368




					D concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






369




					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact, callSite);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






370




371





					// for each incoming-call value









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






372




373




					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






374




375




						propagate(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact, callSite, false);
					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






376




				}









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




				else if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), d1)) {
					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, d2, callSite);

					// for each incoming-call value
					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






383




						D callerSourceFact = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, summaryEdge.getSourceFact());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




385




386




						propagate(callerSourceFact, retSiteC, callerTargetFact, callSite, false);
					}
				}				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






387




			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






388




389




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






390




391




392




393




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






394




		if(followReturnsPastSeeds && d1 == zeroValue && incoming(methodThatNeedsSummary).isEmpty()) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






395




396




397




398




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






399




400




401




					Set<AnnotatedFact<D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(AnnotatedFact<D> d5: targets)
						propagate(zeroValue, retSiteC, d5.getFact(), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




416




417




418




419




420




421




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






422




	protected Set<AnnotatedFact<D>> computeReturnFlowFunction









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






423




			(FlowFunction<D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






424




425




426




427




428




429




430




431




432




433




434




435




436




437




438




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
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






439




440




			Set<AnnotatedFact<D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (AnnotatedFact<D> d3 : res) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






441




442




443




				//TODO: double check if concurrency issues may arise
				
				//if reading field f









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






444




445




446




447




448




449




450




451




				// if d1.f element of incoming edges:
				//    create and propagate (d1.f, d2.f)
				// else 
				//	  create and set (d1.f, d2.f) on hold
				//	  create for each incoming edge inc: (inc.call-d1.f, inc.call-d2.f) and put on hold
				
				if(d3.getReadField() instanceof SpecificFieldReference) {
					SpecificFieldReference fieldRef = (SpecificFieldReference) d3.getReadField();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






452




453




					D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(d1, fieldRef);
					if(checkForInterestedCallers(d1, n, fieldRef)) {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






454




						propagate(concretizedSourceValue, m, d3.getFact(), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






455




456




					} else {
						pauseEdge(concretizedSourceValue, m, d3.getFact());









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






457




458




					}
				}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






459




				else {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






460




461




462




463




464




465




466




467




468




				
				//TODO: if writing field f
				// create edge e = (d1, d2.*\{f})
				// if d2.*\{f} element of incoming edges
				// 		continue with e
				// else 
				//		put e on hold
				// always kill (d1, d2)
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




480




481




482




483




484




485




486




487




488




489




490




491




492




493




494




495




496




497




498




499




500




501




502




503




504




505




506




					propagate(d1, m, d3.getFact(), null, false);
				}
			}
		}
	}
	
	private void pauseEdge(D sourceValue, N targetStmt, D targetValue) {
		M method = icfg.getMethodOf(targetStmt);
		Set<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());
		edges.add(new PathEdge<N,D>(sourceValue, targetStmt, targetValue));
		logger.trace("PAUSED: <{},{}> -> <{},{}>", method, sourceValue, targetStmt, targetValue);
	}

	private boolean checkForInterestedCallers(D calleeSourceFact, N targetStmt, SpecificFieldReference fieldRef) {
		M calleeMethod = icfg.getMethodOf(targetStmt);
		logger.trace("Checking interest at method {} in fact {} with field access {}", calleeMethod, calleeSourceFact, fieldRef);
		
		if(calleeSourceFact.equals(zeroValue))
			return true;
		
		if(hasInterestedCallers(calleeSourceFact, calleeMethod, fieldRef)) {
			return true;
		}
		
		Set<IncomingEdge<D, N>> inc = incomingEdgesPrefixesOf(calleeMethod, calleeSourceFact); 
		for (IncomingEdge<D, N> incomingEdge : inc) {
			if(checkForInterestedCallers(incomingEdge.getCallerSourceFact(), incomingEdge.getCallSite(), fieldRef)) {
				propagate(incomingEdge.getCallerSourceFact().equals(zeroValue) ? 
							incomingEdge.getCallerSourceFact() : 
							cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef), null, false); 
				return true;
			}
			else {
				pauseEdge(cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef));









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






507




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






508




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






509




510




511




512




513




514




515




516




		
		return false;
	}
	
	private boolean hasInterestedCallers(D calleeSourceFact, M calleeMethod, SpecificFieldReference fieldRef) {
		D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(calleeSourceFact, fieldRef);
		Set<IncomingEdge<D, N>> incomingEdges = incomingEdgesPrefixedWith(calleeMethod, concretizedSourceValue);
		return !incomingEdges.isEmpty();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






517




518




519




520




521




522




523




524




525




526




	}
	
	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






527




	protected Set<AnnotatedFact<D>> computeNormalFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






528




529




530




531




532




533




534




535




536




537




538




539




540




541




542




543




544




545




546




547




548




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




572




573




574




575




576




577




578




			(FlowFunction<D> flowFunction, D d1, D d2) {
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
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */
	protected void propagate(D sourceVal, N target, D targetVal,
			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {
		final PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);
		final D existingVal = jumpFn.addFunction(edge);
		//TODO: Merge d.* and d.x for arbitrary x as d.*
		//TODO: Merge d.* and d.*\{x} as d.*
		//TODO: Merge d.*\{a} and d.*/{b} as d.*
		if (existingVal != null) {
			if (existingVal != targetVal)
				existingVal.addNeighbor(targetVal);
		}
		else {
			scheduleEdgeProcessing(edge);
			if(targetVal!=zeroValue)
				logger.trace("EDGE: <{},{}> -> <{},{}>", icfg.getMethodOf(target), sourceVal, target, targetVal);
		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






579




580




581




582




583




584




585




586




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






587




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) || AccessPathUtil.isPrefixOf(d3, edge.getSourceFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






588




589




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






590




591




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






592




593




594




595




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






596




597




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






598




599




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






600




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






601




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






602




603




604




605




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






606




607




608




609




610




611




612




613




614




615




616




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {
				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact());
			}
		});
	}
	
	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






617




618




619




620




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






621




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






622




623




			}
		});









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




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






627




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






628




629




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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

8d40408f471bdaa24090e949fe134944c1fca13e







Open sidebar



Joshua Garcia heros

8d40408f471bdaa24090e949fe134944c1fca13e




Open sidebar

Joshua Garcia heros

8d40408f471bdaa24090e949fe134944c1fca13e


Joshua Garciaherosheros
8d40408f471bdaa24090e949fe134944c1fca13e










8d40408f471bdaa24090e949fe134944c1fca13e


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



26.2 KB









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




17




18




19




20




21




22




23




import static heros.alias.AccessPathUtil.cloneWithConcatenatedAccessPath;
import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;
import heros.alias.FieldReference.SpecificFieldReference;
import heros.alias.FlowFunction.AnnotatedFact;
import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




26




27




import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






28




import java.util.Set;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






29




30




31




32




33




34




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






35




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






36




import com.google.common.cache.CacheBuilder;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






37




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





public class FieldSensitiveIFDSSolver<N, BaseValue, D extends FieldSensitiveFact<BaseValue, D>, M, I extends InterproceduralCFG<N, M>> {


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






64




65




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






66




67




68




69




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






70




71




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






72




	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






73




74




	protected final MyConcurrentHashMap<M, Set<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, Set<PathEdge<N,D>>>();
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




194




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




205




206




207




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




218




219




220




221




222




223




	@DontSynchronize("stateless")
	protected final FlowFunctions<N, D, M> flowFunctions;
	
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
	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {
		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 
		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.flowFunctions(); 
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
		this.numThreads = Math.max(1,tabulationProblem.numThreads());
		this.executor = getExecutor();
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
				propagate(zeroValue, startPoint, val, null, false);
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
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;
    	executor.execute(new PathEdgeProcessingTask(edge));
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
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






224




			Set<AnnotatedFact<D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






228




			for(AnnotatedFact<D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






229




230




231




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






232




					D abstractStartPointFact = d3.getFact().cloneWithAccessPath();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






233




234




235




236




237




					propagate(abstractStartPointFact, sP, abstractStartPointFact, n, false); //line 15
				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






238




239




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






242




				resumeEdges(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






243




244




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






245




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






246




247




248




249




250




251




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






252




					for(SummaryEdge<D, N> summary: endSumm) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






253




254




255




256




257




258




259




260




261




262




263




264




265




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




276




277




						if(AccessPathUtil.isPrefixOf(summary.getSourceFact(), d3.getFact())) {
							D d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
							
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, d4, n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1, retSiteN, d5p_restoredCtx, n, false);
								}
							}
						} else {
							// incoming fact is prefix of summary: create new edge on caller side with complemented access path 
							D d1_concretized = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, d3.getFact());
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, summary.getTargetFact(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1_concretized, retSiteN, d5p_restoredCtx, n, false);
								}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






278




279




280




281




282




283




284




285




286




							}
						}
					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






287




288




			for(AnnotatedFact<D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))
				propagate(d1, returnSiteN, d3.getFact(), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






289




290




291




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






292




293




294




295




296




297




298




299




300




301




302




303




304




305




306




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {
				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) || AccessPathUtil.isPrefixOf(factAtMethodStartPoint, edge.factAtSource())) {
					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);
						propagate(edge.factAtSource(), edge.getTarget(), edge.factAtTarget(), null, false);
					}
				}
			}
		}
	}










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






307




308




309




310




311




312




313




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






314




	protected Set<AnnotatedFact<D>> computeCallFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






315




316




317




318




319




320




321




322




323




324




325




326




327




			(FlowFunction<D> callFlowFunction, D d1, D d2) {
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






328




	protected Set<AnnotatedFact<D>> computeCallToReturnFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




348




349




350




351




352




			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
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






353




354




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






355




356




357




358




			return;
		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






359




360




361




362




363




364




365




366




367




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);
				
				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact())) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






368




					D concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






369




					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact, callSite);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






370




371





					// for each incoming-call value









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






372




373




					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






374




375




						propagate(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact, callSite, false);
					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






376




				}









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




				else if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), d1)) {
					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, d2, callSite);

					// for each incoming-call value
					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






383




						D callerSourceFact = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, summaryEdge.getSourceFact());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




385




386




						propagate(callerSourceFact, retSiteC, callerTargetFact, callSite, false);
					}
				}				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






387




			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






388




389




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






390




391




392




393




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






394




		if(followReturnsPastSeeds && d1 == zeroValue && incoming(methodThatNeedsSummary).isEmpty()) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






395




396




397




398




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






399




400




401




					Set<AnnotatedFact<D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(AnnotatedFact<D> d5: targets)
						propagate(zeroValue, retSiteC, d5.getFact(), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




416




417




418




419




420




421




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






422




	protected Set<AnnotatedFact<D>> computeReturnFlowFunction









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






423




			(FlowFunction<D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






424




425




426




427




428




429




430




431




432




433




434




435




436




437




438




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
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






439




440




			Set<AnnotatedFact<D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (AnnotatedFact<D> d3 : res) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






441




442




443




				//TODO: double check if concurrency issues may arise
				
				//if reading field f









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






444




445




446




447




448




449




450




451




				// if d1.f element of incoming edges:
				//    create and propagate (d1.f, d2.f)
				// else 
				//	  create and set (d1.f, d2.f) on hold
				//	  create for each incoming edge inc: (inc.call-d1.f, inc.call-d2.f) and put on hold
				
				if(d3.getReadField() instanceof SpecificFieldReference) {
					SpecificFieldReference fieldRef = (SpecificFieldReference) d3.getReadField();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






452




453




					D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(d1, fieldRef);
					if(checkForInterestedCallers(d1, n, fieldRef)) {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






454




						propagate(concretizedSourceValue, m, d3.getFact(), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






455




456




					} else {
						pauseEdge(concretizedSourceValue, m, d3.getFact());









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






457




458




					}
				}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






459




				else {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






460




461




462




463




464




465




466




467




468




				
				//TODO: if writing field f
				// create edge e = (d1, d2.*\{f})
				// if d2.*\{f} element of incoming edges
				// 		continue with e
				// else 
				//		put e on hold
				// always kill (d1, d2)
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




480




481




482




483




484




485




486




487




488




489




490




491




492




493




494




495




496




497




498




499




500




501




502




503




504




505




506




					propagate(d1, m, d3.getFact(), null, false);
				}
			}
		}
	}
	
	private void pauseEdge(D sourceValue, N targetStmt, D targetValue) {
		M method = icfg.getMethodOf(targetStmt);
		Set<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());
		edges.add(new PathEdge<N,D>(sourceValue, targetStmt, targetValue));
		logger.trace("PAUSED: <{},{}> -> <{},{}>", method, sourceValue, targetStmt, targetValue);
	}

	private boolean checkForInterestedCallers(D calleeSourceFact, N targetStmt, SpecificFieldReference fieldRef) {
		M calleeMethod = icfg.getMethodOf(targetStmt);
		logger.trace("Checking interest at method {} in fact {} with field access {}", calleeMethod, calleeSourceFact, fieldRef);
		
		if(calleeSourceFact.equals(zeroValue))
			return true;
		
		if(hasInterestedCallers(calleeSourceFact, calleeMethod, fieldRef)) {
			return true;
		}
		
		Set<IncomingEdge<D, N>> inc = incomingEdgesPrefixesOf(calleeMethod, calleeSourceFact); 
		for (IncomingEdge<D, N> incomingEdge : inc) {
			if(checkForInterestedCallers(incomingEdge.getCallerSourceFact(), incomingEdge.getCallSite(), fieldRef)) {
				propagate(incomingEdge.getCallerSourceFact().equals(zeroValue) ? 
							incomingEdge.getCallerSourceFact() : 
							cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef), null, false); 
				return true;
			}
			else {
				pauseEdge(cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef));









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






507




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






508




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






509




510




511




512




513




514




515




516




		
		return false;
	}
	
	private boolean hasInterestedCallers(D calleeSourceFact, M calleeMethod, SpecificFieldReference fieldRef) {
		D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(calleeSourceFact, fieldRef);
		Set<IncomingEdge<D, N>> incomingEdges = incomingEdgesPrefixedWith(calleeMethod, concretizedSourceValue);
		return !incomingEdges.isEmpty();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






517




518




519




520




521




522




523




524




525




526




	}
	
	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






527




	protected Set<AnnotatedFact<D>> computeNormalFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






528




529




530




531




532




533




534




535




536




537




538




539




540




541




542




543




544




545




546




547




548




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




572




573




574




575




576




577




578




			(FlowFunction<D> flowFunction, D d1, D d2) {
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
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */
	protected void propagate(D sourceVal, N target, D targetVal,
			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {
		final PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);
		final D existingVal = jumpFn.addFunction(edge);
		//TODO: Merge d.* and d.x for arbitrary x as d.*
		//TODO: Merge d.* and d.*\{x} as d.*
		//TODO: Merge d.*\{a} and d.*/{b} as d.*
		if (existingVal != null) {
			if (existingVal != targetVal)
				existingVal.addNeighbor(targetVal);
		}
		else {
			scheduleEdgeProcessing(edge);
			if(targetVal!=zeroValue)
				logger.trace("EDGE: <{},{}> -> <{},{}>", icfg.getMethodOf(target), sourceVal, target, targetVal);
		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






579




580




581




582




583




584




585




586




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






587




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) || AccessPathUtil.isPrefixOf(d3, edge.getSourceFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






588




589




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






590




591




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






592




593




594




595




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






596




597




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






598




599




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






600




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






601




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






602




603




604




605




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






606




607




608




609




610




611




612




613




614




615




616




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {
				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact());
			}
		});
	}
	
	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






617




618




619




620




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






621




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






622




623




			}
		});









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




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






627




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






628




629




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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














8d40408f471bdaa24090e949fe134944c1fca13e


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



26.2 KB









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




17




18




19




20




21




22




23




import static heros.alias.AccessPathUtil.cloneWithConcatenatedAccessPath;
import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;
import heros.alias.FieldReference.SpecificFieldReference;
import heros.alias.FlowFunction.AnnotatedFact;
import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




26




27




import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






28




import java.util.Set;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






29




30




31




32




33




34




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






35




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






36




import com.google.common.cache.CacheBuilder;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






37




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





public class FieldSensitiveIFDSSolver<N, BaseValue, D extends FieldSensitiveFact<BaseValue, D>, M, I extends InterproceduralCFG<N, M>> {


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






64




65




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






66




67




68




69




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






70




71




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






72




	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






73




74




	protected final MyConcurrentHashMap<M, Set<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, Set<PathEdge<N,D>>>();
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




194




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




205




206




207




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




218




219




220




221




222




223




	@DontSynchronize("stateless")
	protected final FlowFunctions<N, D, M> flowFunctions;
	
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
	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {
		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 
		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.flowFunctions(); 
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
		this.numThreads = Math.max(1,tabulationProblem.numThreads());
		this.executor = getExecutor();
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
				propagate(zeroValue, startPoint, val, null, false);
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
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;
    	executor.execute(new PathEdgeProcessingTask(edge));
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
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






224




			Set<AnnotatedFact<D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






228




			for(AnnotatedFact<D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






229




230




231




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






232




					D abstractStartPointFact = d3.getFact().cloneWithAccessPath();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






233




234




235




236




237




					propagate(abstractStartPointFact, sP, abstractStartPointFact, n, false); //line 15
				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






238




239




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






242




				resumeEdges(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






243




244




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






245




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






246




247




248




249




250




251




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






252




					for(SummaryEdge<D, N> summary: endSumm) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






253




254




255




256




257




258




259




260




261




262




263




264




265




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




276




277




						if(AccessPathUtil.isPrefixOf(summary.getSourceFact(), d3.getFact())) {
							D d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
							
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, d4, n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1, retSiteN, d5p_restoredCtx, n, false);
								}
							}
						} else {
							// incoming fact is prefix of summary: create new edge on caller side with complemented access path 
							D d1_concretized = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, d3.getFact());
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, summary.getTargetFact(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1_concretized, retSiteN, d5p_restoredCtx, n, false);
								}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






278




279




280




281




282




283




284




285




286




							}
						}
					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






287




288




			for(AnnotatedFact<D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))
				propagate(d1, returnSiteN, d3.getFact(), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






289




290




291




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






292




293




294




295




296




297




298




299




300




301




302




303




304




305




306




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {
				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) || AccessPathUtil.isPrefixOf(factAtMethodStartPoint, edge.factAtSource())) {
					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);
						propagate(edge.factAtSource(), edge.getTarget(), edge.factAtTarget(), null, false);
					}
				}
			}
		}
	}










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






307




308




309




310




311




312




313




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






314




	protected Set<AnnotatedFact<D>> computeCallFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






315




316




317




318




319




320




321




322




323




324




325




326




327




			(FlowFunction<D> callFlowFunction, D d1, D d2) {
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






328




	protected Set<AnnotatedFact<D>> computeCallToReturnFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




348




349




350




351




352




			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
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






353




354




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






355




356




357




358




			return;
		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






359




360




361




362




363




364




365




366




367




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);
				
				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact())) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






368




					D concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






369




					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact, callSite);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






370




371





					// for each incoming-call value









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






372




373




					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






374




375




						propagate(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact, callSite, false);
					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






376




				}









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




				else if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), d1)) {
					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, d2, callSite);

					// for each incoming-call value
					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






383




						D callerSourceFact = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, summaryEdge.getSourceFact());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




385




386




						propagate(callerSourceFact, retSiteC, callerTargetFact, callSite, false);
					}
				}				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






387




			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






388




389




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






390




391




392




393




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






394




		if(followReturnsPastSeeds && d1 == zeroValue && incoming(methodThatNeedsSummary).isEmpty()) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






395




396




397




398




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






399




400




401




					Set<AnnotatedFact<D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(AnnotatedFact<D> d5: targets)
						propagate(zeroValue, retSiteC, d5.getFact(), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




416




417




418




419




420




421




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






422




	protected Set<AnnotatedFact<D>> computeReturnFlowFunction









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






423




			(FlowFunction<D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






424




425




426




427




428




429




430




431




432




433




434




435




436




437




438




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
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






439




440




			Set<AnnotatedFact<D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (AnnotatedFact<D> d3 : res) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






441




442




443




				//TODO: double check if concurrency issues may arise
				
				//if reading field f









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






444




445




446




447




448




449




450




451




				// if d1.f element of incoming edges:
				//    create and propagate (d1.f, d2.f)
				// else 
				//	  create and set (d1.f, d2.f) on hold
				//	  create for each incoming edge inc: (inc.call-d1.f, inc.call-d2.f) and put on hold
				
				if(d3.getReadField() instanceof SpecificFieldReference) {
					SpecificFieldReference fieldRef = (SpecificFieldReference) d3.getReadField();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






452




453




					D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(d1, fieldRef);
					if(checkForInterestedCallers(d1, n, fieldRef)) {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






454




						propagate(concretizedSourceValue, m, d3.getFact(), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






455




456




					} else {
						pauseEdge(concretizedSourceValue, m, d3.getFact());









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






457




458




					}
				}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






459




				else {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






460




461




462




463




464




465




466




467




468




				
				//TODO: if writing field f
				// create edge e = (d1, d2.*\{f})
				// if d2.*\{f} element of incoming edges
				// 		continue with e
				// else 
				//		put e on hold
				// always kill (d1, d2)
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




480




481




482




483




484




485




486




487




488




489




490




491




492




493




494




495




496




497




498




499




500




501




502




503




504




505




506




					propagate(d1, m, d3.getFact(), null, false);
				}
			}
		}
	}
	
	private void pauseEdge(D sourceValue, N targetStmt, D targetValue) {
		M method = icfg.getMethodOf(targetStmt);
		Set<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());
		edges.add(new PathEdge<N,D>(sourceValue, targetStmt, targetValue));
		logger.trace("PAUSED: <{},{}> -> <{},{}>", method, sourceValue, targetStmt, targetValue);
	}

	private boolean checkForInterestedCallers(D calleeSourceFact, N targetStmt, SpecificFieldReference fieldRef) {
		M calleeMethod = icfg.getMethodOf(targetStmt);
		logger.trace("Checking interest at method {} in fact {} with field access {}", calleeMethod, calleeSourceFact, fieldRef);
		
		if(calleeSourceFact.equals(zeroValue))
			return true;
		
		if(hasInterestedCallers(calleeSourceFact, calleeMethod, fieldRef)) {
			return true;
		}
		
		Set<IncomingEdge<D, N>> inc = incomingEdgesPrefixesOf(calleeMethod, calleeSourceFact); 
		for (IncomingEdge<D, N> incomingEdge : inc) {
			if(checkForInterestedCallers(incomingEdge.getCallerSourceFact(), incomingEdge.getCallSite(), fieldRef)) {
				propagate(incomingEdge.getCallerSourceFact().equals(zeroValue) ? 
							incomingEdge.getCallerSourceFact() : 
							cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef), null, false); 
				return true;
			}
			else {
				pauseEdge(cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef));









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






507




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






508




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






509




510




511




512




513




514




515




516




		
		return false;
	}
	
	private boolean hasInterestedCallers(D calleeSourceFact, M calleeMethod, SpecificFieldReference fieldRef) {
		D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(calleeSourceFact, fieldRef);
		Set<IncomingEdge<D, N>> incomingEdges = incomingEdgesPrefixedWith(calleeMethod, concretizedSourceValue);
		return !incomingEdges.isEmpty();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






517




518




519




520




521




522




523




524




525




526




	}
	
	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






527




	protected Set<AnnotatedFact<D>> computeNormalFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






528




529




530




531




532




533




534




535




536




537




538




539




540




541




542




543




544




545




546




547




548




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




572




573




574




575




576




577




578




			(FlowFunction<D> flowFunction, D d1, D d2) {
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
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */
	protected void propagate(D sourceVal, N target, D targetVal,
			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {
		final PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);
		final D existingVal = jumpFn.addFunction(edge);
		//TODO: Merge d.* and d.x for arbitrary x as d.*
		//TODO: Merge d.* and d.*\{x} as d.*
		//TODO: Merge d.*\{a} and d.*/{b} as d.*
		if (existingVal != null) {
			if (existingVal != targetVal)
				existingVal.addNeighbor(targetVal);
		}
		else {
			scheduleEdgeProcessing(edge);
			if(targetVal!=zeroValue)
				logger.trace("EDGE: <{},{}> -> <{},{}>", icfg.getMethodOf(target), sourceVal, target, targetVal);
		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






579




580




581




582




583




584




585




586




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






587




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) || AccessPathUtil.isPrefixOf(d3, edge.getSourceFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






588




589




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






590




591




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






592




593




594




595




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






596




597




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






598




599




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






600




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






601




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






602




603




604




605




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






606




607




608




609




610




611




612




613




614




615




616




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {
				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact());
			}
		});
	}
	
	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






617




618




619




620




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






621




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






622




623




			}
		});









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




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






627




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






628




629




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










8d40408f471bdaa24090e949fe134944c1fca13e


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink




8d40408f471bdaa24090e949fe134944c1fca13e


Switch branch/tag










heros


src


heros


alias


FieldSensitiveIFDSSolver.java





8d40408f471bdaa24090e949fe134944c1fca13e


Switch branch/tag








8d40408f471bdaa24090e949fe134944c1fca13e


Switch branch/tag





8d40408f471bdaa24090e949fe134944c1fca13e

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



26.2 KB









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




17




18




19




20




21




22




23




import static heros.alias.AccessPathUtil.cloneWithConcatenatedAccessPath;
import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;
import heros.alias.FieldReference.SpecificFieldReference;
import heros.alias.FlowFunction.AnnotatedFact;
import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




26




27




import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






28




import java.util.Set;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






29




30




31




32




33




34




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






35




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






36




import com.google.common.cache.CacheBuilder;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






37




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





public class FieldSensitiveIFDSSolver<N, BaseValue, D extends FieldSensitiveFact<BaseValue, D>, M, I extends InterproceduralCFG<N, M>> {


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






64




65




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






66




67




68




69




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






70




71




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






72




	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






73




74




	protected final MyConcurrentHashMap<M, Set<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, Set<PathEdge<N,D>>>();
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




194




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




205




206




207




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




218




219




220




221




222




223




	@DontSynchronize("stateless")
	protected final FlowFunctions<N, D, M> flowFunctions;
	
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
	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {
		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 
		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.flowFunctions(); 
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
		this.numThreads = Math.max(1,tabulationProblem.numThreads());
		this.executor = getExecutor();
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
				propagate(zeroValue, startPoint, val, null, false);
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
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;
    	executor.execute(new PathEdgeProcessingTask(edge));
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
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






224




			Set<AnnotatedFact<D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






228




			for(AnnotatedFact<D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






229




230




231




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






232




					D abstractStartPointFact = d3.getFact().cloneWithAccessPath();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






233




234




235




236




237




					propagate(abstractStartPointFact, sP, abstractStartPointFact, n, false); //line 15
				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






238




239




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






242




				resumeEdges(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






243




244




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






245




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






246




247




248




249




250




251




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






252




					for(SummaryEdge<D, N> summary: endSumm) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






253




254




255




256




257




258




259




260




261




262




263




264




265




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




276




277




						if(AccessPathUtil.isPrefixOf(summary.getSourceFact(), d3.getFact())) {
							D d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
							
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, d4, n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1, retSiteN, d5p_restoredCtx, n, false);
								}
							}
						} else {
							// incoming fact is prefix of summary: create new edge on caller side with complemented access path 
							D d1_concretized = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, d3.getFact());
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, summary.getTargetFact(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1_concretized, retSiteN, d5p_restoredCtx, n, false);
								}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






278




279




280




281




282




283




284




285




286




							}
						}
					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






287




288




			for(AnnotatedFact<D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))
				propagate(d1, returnSiteN, d3.getFact(), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






289




290




291




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






292




293




294




295




296




297




298




299




300




301




302




303




304




305




306




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {
				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) || AccessPathUtil.isPrefixOf(factAtMethodStartPoint, edge.factAtSource())) {
					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);
						propagate(edge.factAtSource(), edge.getTarget(), edge.factAtTarget(), null, false);
					}
				}
			}
		}
	}










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






307




308




309




310




311




312




313




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






314




	protected Set<AnnotatedFact<D>> computeCallFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






315




316




317




318




319




320




321




322




323




324




325




326




327




			(FlowFunction<D> callFlowFunction, D d1, D d2) {
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






328




	protected Set<AnnotatedFact<D>> computeCallToReturnFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




348




349




350




351




352




			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
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






353




354




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






355




356




357




358




			return;
		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






359




360




361




362




363




364




365




366




367




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);
				
				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact())) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






368




					D concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






369




					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact, callSite);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






370




371





					// for each incoming-call value









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






372




373




					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






374




375




						propagate(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact, callSite, false);
					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






376




				}









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




				else if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), d1)) {
					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, d2, callSite);

					// for each incoming-call value
					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






383




						D callerSourceFact = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, summaryEdge.getSourceFact());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




385




386




						propagate(callerSourceFact, retSiteC, callerTargetFact, callSite, false);
					}
				}				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






387




			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






388




389




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






390




391




392




393




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






394




		if(followReturnsPastSeeds && d1 == zeroValue && incoming(methodThatNeedsSummary).isEmpty()) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






395




396




397




398




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






399




400




401




					Set<AnnotatedFact<D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(AnnotatedFact<D> d5: targets)
						propagate(zeroValue, retSiteC, d5.getFact(), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




416




417




418




419




420




421




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






422




	protected Set<AnnotatedFact<D>> computeReturnFlowFunction









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






423




			(FlowFunction<D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






424




425




426




427




428




429




430




431




432




433




434




435




436




437




438




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
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






439




440




			Set<AnnotatedFact<D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (AnnotatedFact<D> d3 : res) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






441




442




443




				//TODO: double check if concurrency issues may arise
				
				//if reading field f









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






444




445




446




447




448




449




450




451




				// if d1.f element of incoming edges:
				//    create and propagate (d1.f, d2.f)
				// else 
				//	  create and set (d1.f, d2.f) on hold
				//	  create for each incoming edge inc: (inc.call-d1.f, inc.call-d2.f) and put on hold
				
				if(d3.getReadField() instanceof SpecificFieldReference) {
					SpecificFieldReference fieldRef = (SpecificFieldReference) d3.getReadField();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






452




453




					D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(d1, fieldRef);
					if(checkForInterestedCallers(d1, n, fieldRef)) {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






454




						propagate(concretizedSourceValue, m, d3.getFact(), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






455




456




					} else {
						pauseEdge(concretizedSourceValue, m, d3.getFact());









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






457




458




					}
				}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






459




				else {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






460




461




462




463




464




465




466




467




468




				
				//TODO: if writing field f
				// create edge e = (d1, d2.*\{f})
				// if d2.*\{f} element of incoming edges
				// 		continue with e
				// else 
				//		put e on hold
				// always kill (d1, d2)
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




480




481




482




483




484




485




486




487




488




489




490




491




492




493




494




495




496




497




498




499




500




501




502




503




504




505




506




					propagate(d1, m, d3.getFact(), null, false);
				}
			}
		}
	}
	
	private void pauseEdge(D sourceValue, N targetStmt, D targetValue) {
		M method = icfg.getMethodOf(targetStmt);
		Set<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());
		edges.add(new PathEdge<N,D>(sourceValue, targetStmt, targetValue));
		logger.trace("PAUSED: <{},{}> -> <{},{}>", method, sourceValue, targetStmt, targetValue);
	}

	private boolean checkForInterestedCallers(D calleeSourceFact, N targetStmt, SpecificFieldReference fieldRef) {
		M calleeMethod = icfg.getMethodOf(targetStmt);
		logger.trace("Checking interest at method {} in fact {} with field access {}", calleeMethod, calleeSourceFact, fieldRef);
		
		if(calleeSourceFact.equals(zeroValue))
			return true;
		
		if(hasInterestedCallers(calleeSourceFact, calleeMethod, fieldRef)) {
			return true;
		}
		
		Set<IncomingEdge<D, N>> inc = incomingEdgesPrefixesOf(calleeMethod, calleeSourceFact); 
		for (IncomingEdge<D, N> incomingEdge : inc) {
			if(checkForInterestedCallers(incomingEdge.getCallerSourceFact(), incomingEdge.getCallSite(), fieldRef)) {
				propagate(incomingEdge.getCallerSourceFact().equals(zeroValue) ? 
							incomingEdge.getCallerSourceFact() : 
							cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef), null, false); 
				return true;
			}
			else {
				pauseEdge(cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef));









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






507




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






508




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






509




510




511




512




513




514




515




516




		
		return false;
	}
	
	private boolean hasInterestedCallers(D calleeSourceFact, M calleeMethod, SpecificFieldReference fieldRef) {
		D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(calleeSourceFact, fieldRef);
		Set<IncomingEdge<D, N>> incomingEdges = incomingEdgesPrefixedWith(calleeMethod, concretizedSourceValue);
		return !incomingEdges.isEmpty();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






517




518




519




520




521




522




523




524




525




526




	}
	
	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






527




	protected Set<AnnotatedFact<D>> computeNormalFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






528




529




530




531




532




533




534




535




536




537




538




539




540




541




542




543




544




545




546




547




548




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




572




573




574




575




576




577




578




			(FlowFunction<D> flowFunction, D d1, D d2) {
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
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */
	protected void propagate(D sourceVal, N target, D targetVal,
			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {
		final PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);
		final D existingVal = jumpFn.addFunction(edge);
		//TODO: Merge d.* and d.x for arbitrary x as d.*
		//TODO: Merge d.* and d.*\{x} as d.*
		//TODO: Merge d.*\{a} and d.*/{b} as d.*
		if (existingVal != null) {
			if (existingVal != targetVal)
				existingVal.addNeighbor(targetVal);
		}
		else {
			scheduleEdgeProcessing(edge);
			if(targetVal!=zeroValue)
				logger.trace("EDGE: <{},{}> -> <{},{}>", icfg.getMethodOf(target), sourceVal, target, targetVal);
		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






579




580




581




582




583




584




585




586




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






587




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) || AccessPathUtil.isPrefixOf(d3, edge.getSourceFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






588




589




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






590




591




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






592




593




594




595




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






596




597




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






598




599




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






600




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






601




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






602




603




604




605




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






606




607




608




609




610




611




612




613




614




615




616




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {
				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact());
			}
		});
	}
	
	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






617




618




619




620




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






621




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






622




623




			}
		});









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




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






627




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






628




629




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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



26.2 KB










FieldSensitiveIFDSSolver.java



26.2 KB









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




17




18




19




20




21




22




23




import static heros.alias.AccessPathUtil.cloneWithConcatenatedAccessPath;
import heros.DontSynchronize;
import heros.FlowFunctionCache;
import heros.InterproceduralCFG;
import heros.SynchronizedBy;
import heros.alias.FieldReference.SpecificFieldReference;
import heros.alias.FlowFunction.AnnotatedFact;
import heros.solver.CountingThreadPoolExecutor;
import heros.solver.IFDSSolver;
import heros.solver.PathEdge;










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






24




25




26




27




import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






28




import java.util.Set;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






29




30




31




32




33




34




import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






35




import com.google.common.base.Predicate;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






36




import com.google.common.cache.CacheBuilder;









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






37




import com.google.common.collect.Sets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





public class FieldSensitiveIFDSSolver<N, BaseValue, D extends FieldSensitiveFact<BaseValue, D>, M, I extends InterproceduralCFG<N, M>> {


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






64




65




	protected final MyConcurrentHashMap<M,Set<SummaryEdge<D, N>>> endSummary =
			new MyConcurrentHashMap<M, Set<SummaryEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






66




67




68




69




	
	//edges going along calls
	//see CC 2010 paper by Naeem, Lhotak and Rodriguez
	@SynchronizedBy("consistent lock on field")









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






70




71




	protected final MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>> incoming =
			new MyConcurrentHashMap<M, Set<IncomingEdge<D, N>>>();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






72




	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






73




74




	protected final MyConcurrentHashMap<M, Set<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, Set<PathEdge<N,D>>>();
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




194




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




205




206




207




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




218




219




220




221




222




223




	@DontSynchronize("stateless")
	protected final FlowFunctions<N, D, M> flowFunctions;
	
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
	
	
	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER);
	}

	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {
		if(logger.isDebugEnabled())
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();		
	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?
				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 
		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.flowFunctions(); 
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
		this.numThreads = Math.max(1,tabulationProblem.numThreads());
		this.executor = getExecutor();
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
				propagate(zeroValue, startPoint, val, null, false);
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
		try {
			executor.awaitCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Throwable exception = executor.getException();
		if(exception!=null) {
			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);
		}
	}

    /**
     * Dispatch the processing of a given edge. It may be executed in a different thread.
     * @param edge the edge to process
     */
    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){
    	// If the executor has been killed, there is little point
    	// in submitting new tasks
    	if (executor.isTerminating())
    		return;
    	executor.execute(new PathEdgeProcessingTask(edge));
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
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






224




			Set<AnnotatedFact<D>> res = computeCallFlowFunction(function, d1, d2);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




			
			Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);
			//for each result node of the call-flow function









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






228




			for(AnnotatedFact<D> d3: res) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






229




230




231




				//for each callee's start point(s)
				for(N sP: startPointsOf) {
					//create initial self-loop









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






232




					D abstractStartPointFact = d3.getFact().cloneWithAccessPath();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






233




234




235




236




237




					propagate(abstractStartPointFact, sP, abstractStartPointFact, n, false); //line 15
				}
				
				//register the fact that <sp,d3> has an incoming edge from <n,d2>
				//line 15.1 of Naeem/Lhotak/Rodriguez









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






238




239




				IncomingEdge<D, N> incomingEdge = new IncomingEdge<D, N>(d3.getFact(),n,d1,d2);
				if (!addIncoming(sCalledProcN, incomingEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






240




241




					continue;
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






242




				resumeEdges(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






243




244




				
				//line 15.2









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






245




				Set<SummaryEdge<D, N>> endSumm = endSummary(sCalledProcN, d3.getFact());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






246




247




248




249




250




251




					
				//still line 15.2 of Naeem/Lhotak/Rodriguez
				//for each already-queried exit value <eP,d4> reachable from <sP,d3>,
				//create new caller-side jump functions to the return sites
				//because we have observed a potentially new incoming edge into <sP,d3>
				if (endSumm != null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






252




					for(SummaryEdge<D, N> summary: endSumm) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






253




254




255




256




257




258




259




260




261




262




263




264




265




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




276




277




						if(AccessPathUtil.isPrefixOf(summary.getSourceFact(), d3.getFact())) {
							D d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);
							
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, d4, n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1, retSiteN, d5p_restoredCtx, n, false);
								}
							}
						} else {
							// incoming fact is prefix of summary: create new edge on caller side with complemented access path 
							D d1_concretized = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, d3.getFact());
							//for each return site
							for(N retSiteN: returnSiteNs) {
								//compute return-flow function
								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);
								//for each target value of the function
								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, summary.getTargetFact(), n)) {
									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());
									propagate(d1_concretized, retSiteN, d5p_restoredCtx, n, false);
								}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






278




279




280




281




282




283




284




285




286




							}
						}
					}
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez		
		//process intra-procedural flows along call-to-return flow functions
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






287




288




			for(AnnotatedFact<D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))
				propagate(d1, returnSiteN, d3.getFact(), n, false);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






289




290




291




		}
	}










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






292




293




294




295




296




297




298




299




300




301




302




303




304




305




306




	private void resumeEdges(M method, D factAtMethodStartPoint) {
		//TODO: Check for concurrency issues
		Set<PathEdge<N, D>> edges = pausedEdges.get(method);
		if(edges != null) {
			for(PathEdge<N, D> edge : edges) {
				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) || AccessPathUtil.isPrefixOf(factAtMethodStartPoint, edge.factAtSource())) {
					if(edges.remove(edge))  {
						logger.trace("RESUME-EDGE: {}", edge);
						propagate(edge.factAtSource(), edge.getTarget(), edge.factAtTarget(), null, false);
					}
				}
			}
		}
	}










FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






307




308




309




310




311




312




313




	/**
	 * Computes the call flow function for the given call-site abstraction
	 * @param callFlowFunction The call flow function to compute
	 * @param d1 The abstraction at the current method's start node.
	 * @param d2 The abstraction at the call site
	 * @return The set of caller-side abstractions at the callee's start node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






314




	protected Set<AnnotatedFact<D>> computeCallFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






315




316




317




318




319




320




321




322




323




324




325




326




327




			(FlowFunction<D> callFlowFunction, D d1, D d2) {
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






328




	protected Set<AnnotatedFact<D>> computeCallToReturnFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




348




349




350




351




352




			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
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






353




354




		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);
		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






355




356




357




358




			return;
		
		//for each incoming call edge already processed
		//(see processCall(..))









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






359




360




361




362




363




364




365




366




367




		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {
			// line 22
			N callSite = incomingEdge.getCallSite();
			// for each return site
			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {
				// compute return-flow function
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);
				
				if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact())) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






368




					D concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






369




					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact, callSite);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






370




371





					// for each incoming-call value









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






372




373




					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






374




375




						propagate(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact, callSite, false);
					}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






376




				}









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




				else if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), d1)) {
					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, d2, callSite);

					// for each incoming-call value
					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {
						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






383




						D callerSourceFact = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, summaryEdge.getSourceFact());









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






384




385




386




						propagate(callerSourceFact, retSiteC, callerTargetFact, callSite, false);
					}
				}				









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






387




			}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






388




389




		}
		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






390




391




392




393




		
		//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow
		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only
		//be propagated into callers that have an incoming edge for this condition









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






394




		if(followReturnsPastSeeds && d1 == zeroValue && incoming(methodThatNeedsSummary).isEmpty()) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






395




396




397




398




			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);
			for(N c: callers) {
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






399




400




401




					Set<AnnotatedFact<D>> targets = computeReturnFlowFunction(retFunction, d2, c);
					for(AnnotatedFact<D> d5: targets)
						propagate(zeroValue, retSiteC, d5.getFact(), c, true);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




416




417




418




419




420




421




				}
			}
			//in cases where there are no callers, the return statement would normally not be processed at all;
			//this might be undesirable if the flow function has a side effect such as registering a taint;
			//instead we thus call the return flow function will a null caller
			if(callers.isEmpty()) {
				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);
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









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






422




	protected Set<AnnotatedFact<D>> computeReturnFlowFunction









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






423




			(FlowFunction<D> retFunction, D d2, N callSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






424




425




426




427




428




429




430




431




432




433




434




435




436




437




438




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
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






439




440




			Set<AnnotatedFact<D>> res = computeNormalFlowFunction(flowFunction, d1, d2);
			for (AnnotatedFact<D> d3 : res) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






441




442




443




				//TODO: double check if concurrency issues may arise
				
				//if reading field f









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






444




445




446




447




448




449




450




451




				// if d1.f element of incoming edges:
				//    create and propagate (d1.f, d2.f)
				// else 
				//	  create and set (d1.f, d2.f) on hold
				//	  create for each incoming edge inc: (inc.call-d1.f, inc.call-d2.f) and put on hold
				
				if(d3.getReadField() instanceof SpecificFieldReference) {
					SpecificFieldReference fieldRef = (SpecificFieldReference) d3.getReadField();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






452




453




					D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(d1, fieldRef);
					if(checkForInterestedCallers(d1, n, fieldRef)) {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






454




						propagate(concretizedSourceValue, m, d3.getFact(), null, false);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






455




456




					} else {
						pauseEdge(concretizedSourceValue, m, d3.getFact());









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






457




458




					}
				}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






459




				else {









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






460




461




462




463




464




465




466




467




468




				
				//TODO: if writing field f
				// create edge e = (d1, d2.*\{f})
				// if d2.*\{f} element of incoming edges
				// 		continue with e
				// else 
				//		put e on hold
				// always kill (d1, d2)
				









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




480




481




482




483




484




485




486




487




488




489




490




491




492




493




494




495




496




497




498




499




500




501




502




503




504




505




506




					propagate(d1, m, d3.getFact(), null, false);
				}
			}
		}
	}
	
	private void pauseEdge(D sourceValue, N targetStmt, D targetValue) {
		M method = icfg.getMethodOf(targetStmt);
		Set<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());
		edges.add(new PathEdge<N,D>(sourceValue, targetStmt, targetValue));
		logger.trace("PAUSED: <{},{}> -> <{},{}>", method, sourceValue, targetStmt, targetValue);
	}

	private boolean checkForInterestedCallers(D calleeSourceFact, N targetStmt, SpecificFieldReference fieldRef) {
		M calleeMethod = icfg.getMethodOf(targetStmt);
		logger.trace("Checking interest at method {} in fact {} with field access {}", calleeMethod, calleeSourceFact, fieldRef);
		
		if(calleeSourceFact.equals(zeroValue))
			return true;
		
		if(hasInterestedCallers(calleeSourceFact, calleeMethod, fieldRef)) {
			return true;
		}
		
		Set<IncomingEdge<D, N>> inc = incomingEdgesPrefixesOf(calleeMethod, calleeSourceFact); 
		for (IncomingEdge<D, N> incomingEdge : inc) {
			if(checkForInterestedCallers(incomingEdge.getCallerSourceFact(), incomingEdge.getCallSite(), fieldRef)) {
				propagate(incomingEdge.getCallerSourceFact().equals(zeroValue) ? 
							incomingEdge.getCallerSourceFact() : 
							cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef), null, false); 
				return true;
			}
			else {
				pauseEdge(cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), 
						incomingEdge.getCallSite(), 
						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef));









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






507




			}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






508




		}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






509




510




511




512




513




514




515




516




		
		return false;
	}
	
	private boolean hasInterestedCallers(D calleeSourceFact, M calleeMethod, SpecificFieldReference fieldRef) {
		D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(calleeSourceFact, fieldRef);
		Set<IncomingEdge<D, N>> incomingEdges = incomingEdgesPrefixedWith(calleeMethod, concretizedSourceValue);
		return !incomingEdges.isEmpty();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






517




518




519




520




521




522




523




524




525




526




	}
	
	/**
	 * Computes the normal flow function for the given set of start and end
	 * abstractions.
	 * @param flowFunction The normal flow function to compute
	 * @param d1 The abstraction at the method's start node
	 * @param d1 The abstraction at the current node
	 * @return The set of abstractions at the successor node
	 */









annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014






527




	protected Set<AnnotatedFact<D>> computeNormalFlowFunction









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






528




529




530




531




532




533




534




535




536




537




538




539




540




541




542




543




544




545




546




547




548




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




572




573




574




575




576




577




578




			(FlowFunction<D> flowFunction, D d1, D d2) {
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
	 * @param sourceVal the source value of the propagated summary edge
	 * @param target the target statement
	 * @param targetVal the target value at the target statement
	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 
	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return
	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})
	 */
	protected void propagate(D sourceVal, N target, D targetVal,
			/* deliberately exposed to clients */ N relatedCallSite,
			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {
		final PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);
		final D existingVal = jumpFn.addFunction(edge);
		//TODO: Merge d.* and d.x for arbitrary x as d.*
		//TODO: Merge d.* and d.*\{x} as d.*
		//TODO: Merge d.*\{a} and d.*/{b} as d.*
		if (existingVal != null) {
			if (existingVal != targetVal)
				existingVal.addNeighbor(targetVal);
		}
		else {
			scheduleEdgeProcessing(edge);
			if(targetVal!=zeroValue)
				logger.trace("EDGE: <{},{}> -> <{},{}>", icfg.getMethodOf(target), sourceVal, target, targetVal);
		}
	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






579




580




581




582




583




584




585




586




	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {
		Set<SummaryEdge<D, N>> map = endSummary.get(m);
		if(map == null)
			return null;
		
		return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {
			@Override
			public boolean apply(SummaryEdge<D, N> edge) {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






587




				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) || AccessPathUtil.isPrefixOf(d3, edge.getSourceFact());









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






588




589




			}
		});









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






590




591




	}










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






592




593




594




595




	private boolean addEndSummary(M m, SummaryEdge<D,N> summaryEdge) {
		Set<SummaryEdge<D, N>> summaries = endSummary.putIfAbsentElseGet
				(m, new ConcurrentHashSet<SummaryEdge<D, N>>());
		return summaries.add(summaryEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






596




597




	}	










use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






598




599




	protected Set<IncomingEdge<D, N>> incoming(M m) {
		Set<IncomingEdge<D, N>> result = incoming.get(m);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






600




		if(result == null)









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






601




			return Collections.emptySet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






602




603




604




605




		else
			return result;
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






606




607




608




609




610




611




612




613




614




615




616




	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {
		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {
				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact());
			}
		});
	}
	
	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixesOf(M m, final D fact) {









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






617




618




619




620




		Set<IncomingEdge<D, N>> result = incoming(m);
		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {
			@Override
			public boolean apply(IncomingEdge<D, N> edge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






621




				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






622




623




			}
		});









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




	protected boolean addIncoming(M m, IncomingEdge<D, N> incomingEdge) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






627




		logger.trace("Incoming Edge for method {}: {}", m, incomingEdge);









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






628




629




		Set<IncomingEdge<D,N>> set = incoming.putIfAbsentElseGet(m, new ConcurrentHashSet<IncomingEdge<D,N>>());
		return set.add(incomingEdge);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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

17

18

19

20

21

22

23
import static heros.alias.AccessPathUtil.cloneWithConcatenatedAccessPath;importstaticheros.alias.AccessPathUtil.cloneWithConcatenatedAccessPath;import heros.DontSynchronize;importheros.DontSynchronize;import heros.FlowFunctionCache;importheros.FlowFunctionCache;import heros.InterproceduralCFG;importheros.InterproceduralCFG;import heros.SynchronizedBy;importheros.SynchronizedBy;import heros.alias.FieldReference.SpecificFieldReference;importheros.alias.FieldReference.SpecificFieldReference;import heros.alias.FlowFunction.AnnotatedFact;importheros.alias.FlowFunction.AnnotatedFact;import heros.solver.CountingThreadPoolExecutor;importheros.solver.CountingThreadPoolExecutor;import heros.solver.IFDSSolver;importheros.solver.IFDSSolver;import heros.solver.PathEdge;importheros.solver.PathEdge;



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

26

27
import java.util.Collection;importjava.util.Collection;import java.util.Collections;importjava.util.Collections;import java.util.Map;importjava.util.Map;import java.util.Map.Entry;importjava.util.Map.Entry;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

28
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

29

30

31

32

33

34
import java.util.concurrent.LinkedBlockingQueue;importjava.util.concurrent.LinkedBlockingQueue;import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;import org.slf4j.Logger;importorg.slf4j.Logger;import org.slf4j.LoggerFactory;importorg.slf4j.LoggerFactory;



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

35
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

36
import com.google.common.cache.CacheBuilder;importcom.google.common.cache.CacheBuilder;



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

37
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
public class FieldSensitiveIFDSSolver<N, BaseValue, D extends FieldSensitiveFact<BaseValue, D>, M, I extends InterproceduralCFG<N, M>> {publicclassFieldSensitiveIFDSSolver<N,BaseValue,DextendsFieldSensitiveFact<BaseValue,D>,M,IextendsInterproceduralCFG<N,M>>{	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevelpublicstaticCacheBuilder<Object,Object>DEFAULT_CACHE_BUILDER=CacheBuilder.newBuilder().concurrencyLevel			(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();	    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);protectedstaticfinalLoggerlogger=LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);    //enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace//enable with -Dorg.slf4j.simpleLogger.defaultLogLevel=trace    public static final boolean DEBUG = logger.isDebugEnabled();publicstaticfinalbooleanDEBUG=logger.isDebugEnabled();	protected CountingThreadPoolExecutor executor;protectedCountingThreadPoolExecutorexecutor;		@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")	protected int numThreads;protectedintnumThreads;		@SynchronizedBy("thread safe data structure, consistent locking when used")@SynchronizedBy("thread safe data structure, consistent locking when used")	protected final JumpFunctions<N,D> jumpFn;protectedfinalJumpFunctions<N,D>jumpFn;		@SynchronizedBy("thread safe data structure, only modified internally")@SynchronizedBy("thread safe data structure, only modified internally")	protected final I icfg;protectedfinalIicfg;		//stores summaries that were queried before they were computed//stores summaries that were queried before they were computed	//see CC 2010 paper by Naeem, Lhotak and Rodriguez//see CC 2010 paper by Naeem, Lhotak and Rodriguez	@SynchronizedBy("consistent lock on 'incoming'")@SynchronizedBy("consistent lock on 'incoming'")



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

64

65
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

66

67

68

69
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

70

71
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

72
	



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

73

74
	protected final MyConcurrentHashMap<M, Set<PathEdge<N,D>>> pausedEdges = new MyConcurrentHashMap<M, Set<PathEdge<N,D>>>();protectedfinalMyConcurrentHashMap<M,Set<PathEdge<N,D>>>pausedEdges=newMyConcurrentHashMap<M,Set<PathEdge<N,D>>>();	



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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

194

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

205

206

207

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

218

219

220

221

222

223
	@DontSynchronize("stateless")@DontSynchronize("stateless")	protected final FlowFunctions<N, D, M> flowFunctions;protectedfinalFlowFunctions<N,D,M>flowFunctions;		@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")	protected final Map<N,Set<D>> initialSeeds;protectedfinalMap<N,Set<D>>initialSeeds;		@DontSynchronize("benign races")@DontSynchronize("benign races")	public long propagationCount;publiclongpropagationCount;		@DontSynchronize("stateless")@DontSynchronize("stateless")	protected final D zeroValue;protectedfinalDzeroValue;		@DontSynchronize("readOnly")@DontSynchronize("readOnly")	protected final FlowFunctionCache<N,D,M> ffCache = null; protectedfinalFlowFunctionCache<N,D,M>ffCache=null;		@DontSynchronize("readOnly")@DontSynchronize("readOnly")	protected final boolean followReturnsPastSeeds;protectedfinalbooleanfollowReturnsPastSeeds;			/**/**	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * The solver must then be started by calling {@link #solve()}.	 * The solver must then be started by calling {@link #solve()}.	 */	 */	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem) {publicFieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I>tabulationProblem){		this(tabulationProblem, DEFAULT_CACHE_BUILDER);this(tabulationProblem,DEFAULT_CACHE_BUILDER);	}}	/**/**	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * {@link #solve()}.	 * {@link #solve()}.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 */	 */	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder) {publicFieldSensitiveIFDSSolver(IFDSTabulationProblem<N,D,M,I>tabulationProblem,@SuppressWarnings("rawtypes")CacheBuilderflowFunctionCacheBuilder){		if(logger.isDebugEnabled())if(logger.isDebugEnabled())			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();flowFunctionCacheBuilder=flowFunctionCacheBuilder.recordStats();		this.zeroValue = tabulationProblem.zeroValue();this.zeroValue=tabulationProblem.zeroValue();		this.icfg = tabulationProblem.interproceduralCFG();		this.icfg=tabulationProblem.interproceduralCFG();	/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?/*	FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ?				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/ 				new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), zeroValue) : tabulationProblem.flowFunctions();*/		FlowFunctions<N, D, M> flowFunctions = tabulationProblem.flowFunctions(); FlowFunctions<N,D,M>flowFunctions=tabulationProblem.flowFunctions();		/*if(flowFunctionCacheBuilder!=null) {/*if(flowFunctionCacheBuilder!=null) {			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);			flowFunctions = ffCache;			flowFunctions = ffCache;		} else {		} else {			ffCache = null;			ffCache = null;		}*/		}*/		this.flowFunctions = flowFunctions;this.flowFunctions=flowFunctions;		this.initialSeeds = tabulationProblem.initialSeeds();this.initialSeeds=tabulationProblem.initialSeeds();		this.jumpFn = new JumpFunctions<N,D>();this.jumpFn=newJumpFunctions<N,D>();		this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();this.followReturnsPastSeeds=tabulationProblem.followReturnsPastSeeds();		this.numThreads = Math.max(1,tabulationProblem.numThreads());this.numThreads=Math.max(1,tabulationProblem.numThreads());		this.executor = getExecutor();this.executor=getExecutor();	}}	/**/**	 * Runs the solver on the configured problem. This can take some time.	 * Runs the solver on the configured problem. This can take some time.	 */	 */	public void solve() {		publicvoidsolve(){		submitInitialSeeds();submitInitialSeeds();		awaitCompletionComputeValuesAndShutdown();awaitCompletionComputeValuesAndShutdown();	}}	/**/**	 * Schedules the processing of initial seeds, initiating the analysis.	 * Schedules the processing of initial seeds, initiating the analysis.	 * Clients should only call this methods if performing synchronization on	 * Clients should only call this methods if performing synchronization on	 * their own. Normally, {@link #solve()} should be called instead.	 * their own. Normally, {@link #solve()} should be called instead.	 */	 */	protected void submitInitialSeeds() {protectedvoidsubmitInitialSeeds(){		for(Entry<N, Set<D>> seed: initialSeeds.entrySet()) {for(Entry<N,Set<D>>seed:initialSeeds.entrySet()){			N startPoint = seed.getKey();NstartPoint=seed.getKey();			for(D val: seed.getValue())for(Dval:seed.getValue())				propagate(zeroValue, startPoint, val, null, false);propagate(zeroValue,startPoint,val,null,false);			jumpFn.addFunction(new PathEdge<N, D>(zeroValue, startPoint, zeroValue));jumpFn.addFunction(newPathEdge<N,D>(zeroValue,startPoint,zeroValue));		}}	}}	/**/**	 * Awaits the completion of the exploded super graph. When complete, computes result values,	 * Awaits the completion of the exploded super graph. When complete, computes result values,	 * shuts down the executor and returns.	 * shuts down the executor and returns.	 */	 */	protected void awaitCompletionComputeValuesAndShutdown() {protectedvoidawaitCompletionComputeValuesAndShutdown(){		{{			//run executor and await termination of tasks//run executor and await termination of tasks			runExecutorAndAwaitCompletion();runExecutorAndAwaitCompletion();		}}		if(logger.isDebugEnabled())if(logger.isDebugEnabled())			printStats();printStats();		//ask executor to shut down;//ask executor to shut down;		//this will cause new submissions to the executor to be rejected,//this will cause new submissions to the executor to be rejected,		//but at this point all tasks should have completed anyway//but at this point all tasks should have completed anyway		executor.shutdown();executor.shutdown();		//similarly here: we await termination, but this should happen instantaneously,//similarly here: we await termination, but this should happen instantaneously,		//as all tasks should have completed//as all tasks should have completed		runExecutorAndAwaitCompletion();runExecutorAndAwaitCompletion();	}}	/**/**	 * Runs execution, re-throwing exceptions that might be thrown during its execution.	 * Runs execution, re-throwing exceptions that might be thrown during its execution.	 */	 */	private void runExecutorAndAwaitCompletion() {privatevoidrunExecutorAndAwaitCompletion(){		try {try{			executor.awaitCompletion();executor.awaitCompletion();		} catch (InterruptedException e) {}catch(InterruptedExceptione){			e.printStackTrace();e.printStackTrace();		}}		Throwable exception = executor.getException();Throwableexception=executor.getException();		if(exception!=null) {if(exception!=null){			throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);thrownewRuntimeException("There were exceptions during IFDS analysis. Exiting.",exception);		}}	}}    /**/**     * Dispatch the processing of a given edge. It may be executed in a different thread.     * Dispatch the processing of a given edge. It may be executed in a different thread.     * @param edge the edge to process     * @param edge the edge to process     */     */    protected void scheduleEdgeProcessing(PathEdge<N,D> edge){protectedvoidscheduleEdgeProcessing(PathEdge<N,D>edge){    	// If the executor has been killed, there is little point// If the executor has been killed, there is little point    	// in submitting new tasks// in submitting new tasks    	if (executor.isTerminating())if(executor.isTerminating())    		return;return;    	executor.execute(new PathEdgeProcessingTask(edge));executor.execute(newPathEdgeProcessingTask(edge));    	propagationCount++;propagationCount++;    }}		/**/**	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.	 * Lines 13-20 of the algorithm; processing a call site in the caller's context.	 * 	 * 	 * For each possible callee, registers incoming call edges.	 * For each possible callee, registers incoming call edges.	 * Also propagates call-to-return flows and summarized callee flows within the caller. 	 * Also propagates call-to-return flows and summarized callee flows within the caller. 	 * 	 * 	 * @param edge an edge whose target node resembles a method call	 * @param edge an edge whose target node resembles a method call	 */	 */	private void processCall(PathEdge<N,D> edge) {privatevoidprocessCall(PathEdge<N,D>edge){		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); // a call node; line 14...finalNn=edge.getTarget();// a call node; line 14...        logger.trace("Processing call to {}", n);logger.trace("Processing call to {}",n);		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();		assert d2 != null;assertd2!=null;		Collection<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);Collection<N>returnSiteNs=icfg.getReturnSitesOfCallAt(n);				//for each possible callee//for each possible callee		Collection<M> callees = icfg.getCalleesOfCallAt(n);Collection<M>callees=icfg.getCalleesOfCallAt(n);		for(M sCalledProcN: callees) { //still line 14for(MsCalledProcN:callees){//still line 14			//compute the call-flow function//compute the call-flow function			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);FlowFunction<D>function=flowFunctions.getCallFlowFunction(n,sCalledProcN);



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

224
			Set<AnnotatedFact<D>> res = computeCallFlowFunction(function, d1, d2);Set<AnnotatedFact<D>>res=computeCallFlowFunction(function,d1,d2);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

225

226

227
						Collection<N> startPointsOf = icfg.getStartPointsOf(sCalledProcN);Collection<N>startPointsOf=icfg.getStartPointsOf(sCalledProcN);			//for each result node of the call-flow function//for each result node of the call-flow function



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

228
			for(AnnotatedFact<D> d3: res) {for(AnnotatedFact<D>d3:res){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

229

230

231
				//for each callee's start point(s)//for each callee's start point(s)				for(N sP: startPointsOf) {for(NsP:startPointsOf){					//create initial self-loop//create initial self-loop



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

232
					D abstractStartPointFact = d3.getFact().cloneWithAccessPath();DabstractStartPointFact=d3.getFact().cloneWithAccessPath();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

233

234

235

236

237
					propagate(abstractStartPointFact, sP, abstractStartPointFact, n, false); //line 15propagate(abstractStartPointFact,sP,abstractStartPointFact,n,false);//line 15				}}								//register the fact that <sp,d3> has an incoming edge from <n,d2>//register the fact that <sp,d3> has an incoming edge from <n,d2>				//line 15.1 of Naeem/Lhotak/Rodriguez//line 15.1 of Naeem/Lhotak/Rodriguez



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

238

239
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

240

241
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

242
				resumeEdges(sCalledProcN, d3.getFact());resumeEdges(sCalledProcN,d3.getFact());



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

243

244
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

245
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

246

247

248

249

250

251
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

252
					for(SummaryEdge<D, N> summary: endSumm) {for(SummaryEdge<D,N>summary:endSumm){



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

253

254

255

256

257

258

259

260

261

262

263

264

265

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

276

277
						if(AccessPathUtil.isPrefixOf(summary.getSourceFact(), d3.getFact())) {if(AccessPathUtil.isPrefixOf(summary.getSourceFact(),d3.getFact())){							D d4 = AccessPathUtil.applyAbstractedSummary(d3.getFact(), summary);Dd4=AccessPathUtil.applyAbstractedSummary(d3.getFact(),summary);														//for each return site//for each return site							for(N retSiteN: returnSiteNs) {for(NretSiteN:returnSiteNs){								//compute return-flow function//compute return-flow function								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(n,sCalledProcN,summary.getTargetStmt(),retSiteN);								//for each target value of the function//for each target value of the function								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, d4, n)) {for(AnnotatedFact<D>d5:computeReturnFlowFunction(retFunction,d4,n)){									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());Dd5p_restoredCtx=restoreContextOnReturnedFact(d2,d5.getFact());									propagate(d1, retSiteN, d5p_restoredCtx, n, false);propagate(d1,retSiteN,d5p_restoredCtx,n,false);								}}							}}						} else {}else{							// incoming fact is prefix of summary: create new edge on caller side with complemented access path // incoming fact is prefix of summary: create new edge on caller side with complemented access path 							D d1_concretized = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, d3.getFact());Dd1_concretized=AccessPathUtil.concretizeCallerSourceFact(incomingEdge,d3.getFact());							//for each return site//for each return site							for(N retSiteN: returnSiteNs) {for(NretSiteN:returnSiteNs){								//compute return-flow function//compute return-flow function								FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, summary.getTargetStmt(), retSiteN);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(n,sCalledProcN,summary.getTargetStmt(),retSiteN);								//for each target value of the function//for each target value of the function								for(AnnotatedFact<D> d5: computeReturnFlowFunction(retFunction, summary.getTargetFact(), n)) {for(AnnotatedFact<D>d5:computeReturnFlowFunction(retFunction,summary.getTargetFact(),n)){									D d5p_restoredCtx = restoreContextOnReturnedFact(d2, d5.getFact());Dd5p_restoredCtx=restoreContextOnReturnedFact(d2,d5.getFact());									propagate(d1_concretized, retSiteN, d5p_restoredCtx, n, false);propagate(d1_concretized,retSiteN,d5p_restoredCtx,n,false);								}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

278

279

280

281

282

283

284

285

286
							}}						}}					}}			}}		}}		//line 17-19 of Naeem/Lhotak/Rodriguez		//line 17-19 of Naeem/Lhotak/Rodriguez				//process intra-procedural flows along call-to-return flow functions//process intra-procedural flows along call-to-return flow functions		for (N returnSiteN : returnSiteNs) {for(NreturnSiteN:returnSiteNs){			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);FlowFunction<D>callToReturnFlowFunction=flowFunctions.getCallToReturnFlowFunction(n,returnSiteN);



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

287

288
			for(AnnotatedFact<D> d3: computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2))for(AnnotatedFact<D>d3:computeCallToReturnFlowFunction(callToReturnFlowFunction,d1,d2))				propagate(d1, returnSiteN, d3.getFact(), n, false);propagate(d1,returnSiteN,d3.getFact(),n,false);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

289

290

291
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

292

293

294

295

296

297

298

299

300

301

302

303

304

305

306
	private void resumeEdges(M method, D factAtMethodStartPoint) {privatevoidresumeEdges(Mmethod,DfactAtMethodStartPoint){		//TODO: Check for concurrency issues//TODO: Check for concurrency issues		Set<PathEdge<N, D>> edges = pausedEdges.get(method);Set<PathEdge<N,D>>edges=pausedEdges.get(method);		if(edges != null) {if(edges!=null){			for(PathEdge<N, D> edge : edges) {for(PathEdge<N,D>edge:edges){				if(AccessPathUtil.isPrefixOf(edge.factAtSource(), factAtMethodStartPoint) || AccessPathUtil.isPrefixOf(factAtMethodStartPoint, edge.factAtSource())) {if(AccessPathUtil.isPrefixOf(edge.factAtSource(),factAtMethodStartPoint)||AccessPathUtil.isPrefixOf(factAtMethodStartPoint,edge.factAtSource())){					if(edges.remove(edge))  {if(edges.remove(edge)){						logger.trace("RESUME-EDGE: {}", edge);logger.trace("RESUME-EDGE: {}",edge);						propagate(edge.factAtSource(), edge.getTarget(), edge.factAtTarget(), null, false);propagate(edge.factAtSource(),edge.getTarget(),edge.factAtTarget(),null,false);					}}				}}			}}		}}	}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

307

308

309

310

311

312

313
	/**/**	 * Computes the call flow function for the given call-site abstraction	 * Computes the call flow function for the given call-site abstraction	 * @param callFlowFunction The call flow function to compute	 * @param callFlowFunction The call flow function to compute	 * @param d1 The abstraction at the current method's start node.	 * @param d1 The abstraction at the current method's start node.	 * @param d2 The abstraction at the call site	 * @param d2 The abstraction at the call site	 * @return The set of caller-side abstractions at the callee's start node	 * @return The set of caller-side abstractions at the callee's start node	 */	 */



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

314
	protected Set<AnnotatedFact<D>> computeCallFlowFunctionprotectedSet<AnnotatedFact<D>>computeCallFlowFunction



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

315

316

317

318

319

320

321

322

323

324

325

326

327
			(FlowFunction<D> callFlowFunction, D d1, D d2) {(FlowFunction<D>callFlowFunction,Dd1,Dd2){		return callFlowFunction.computeTargets(d2);returncallFlowFunction.computeTargets(d2);	}}	/**/**	 * Computes the call-to-return flow function for the given call-site	 * Computes the call-to-return flow function for the given call-site	 * abstraction	 * abstraction	 * @param callToReturnFlowFunction The call-to-return flow function to	 * @param callToReturnFlowFunction The call-to-return flow function to	 * compute	 * compute	 * @param d1 The abstraction at the current method's start node.	 * @param d1 The abstraction at the current method's start node.	 * @param d2 The abstraction at the call site	 * @param d2 The abstraction at the call site	 * @return The set of caller-side abstractions at the return site	 * @return The set of caller-side abstractions at the return site	 */	 */



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

328
	protected Set<AnnotatedFact<D>> computeCallToReturnFlowFunctionprotectedSet<AnnotatedFact<D>>computeCallToReturnFlowFunction



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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

348

349

350

351

352
			(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {(FlowFunction<D>callToReturnFlowFunction,Dd1,Dd2){		return callToReturnFlowFunction.computeTargets(d2);returncallToReturnFlowFunction.computeTargets(d2);	}}		/**/**	 * Lines 21-32 of the algorithm.	 * Lines 21-32 of the algorithm.	 * 	 * 	 * Stores callee-side summaries.	 * Stores callee-side summaries.	 * Also, at the side of the caller, propagates intra-procedural flows to return sites	 * Also, at the side of the caller, propagates intra-procedural flows to return sites	 * using those newly computed summaries.	 * using those newly computed summaries.	 * 	 * 	 * @param edge an edge whose target node resembles a method exits	 * @param edge an edge whose target node resembles a method exits	 */	 */	protected void processExit(PathEdge<N,D> edge) {protectedvoidprocessExit(PathEdge<N,D>edge){		final N n = edge.getTarget(); // an exit node; line 21...finalNn=edge.getTarget();// an exit node; line 21...		M methodThatNeedsSummary = icfg.getMethodOf(n);MmethodThatNeedsSummary=icfg.getMethodOf(n);				final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();				//for each of the method's start points, determine incoming calls//for each of the method's start points, determine incoming calls				//line 21.1 of Naeem/Lhotak/Rodriguez//line 21.1 of Naeem/Lhotak/Rodriguez		//register end-summary//register end-summary



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

353

354
		SummaryEdge<D, N> summaryEdge = new SummaryEdge<D, N>(d1, n, d2);SummaryEdge<D,N>summaryEdge=newSummaryEdge<D,N>(d1,n,d2);		if (!addEndSummary(methodThatNeedsSummary, summaryEdge))if(!addEndSummary(methodThatNeedsSummary,summaryEdge))



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

355

356

357

358
			return;return;				//for each incoming call edge already processed//for each incoming call edge already processed		//(see processCall(..))//(see processCall(..))



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

359

360

361

362

363

364

365

366

367
		for (IncomingEdge<D, N> incomingEdge : incoming(methodThatNeedsSummary)) {for(IncomingEdge<D,N>incomingEdge:incoming(methodThatNeedsSummary)){			// line 22// line 22			N callSite = incomingEdge.getCallSite();NcallSite=incomingEdge.getCallSite();			// for each return site// for each return site			for (N retSiteC : icfg.getReturnSitesOfCallAt(callSite)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(callSite)){				// compute return-flow function// compute return-flow function				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(callSite, methodThatNeedsSummary, n, retSiteC);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(callSite,methodThatNeedsSummary,n,retSiteC);								if(AccessPathUtil.isPrefixOf(d1, incomingEdge.getCalleeSourceFact())) {if(AccessPathUtil.isPrefixOf(d1,incomingEdge.getCalleeSourceFact())){



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

368
					D concreteCalleeExitFact = AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(), summaryEdge);DconcreteCalleeExitFact=AccessPathUtil.applyAbstractedSummary(incomingEdge.getCalleeSourceFact(),summaryEdge);



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

369
					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, concreteCalleeExitFact, callSite);Set<AnnotatedFact<D>>callerTargetFacts=computeReturnFlowFunction(retFunction,concreteCalleeExitFact,callSite);



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

370

371
					// for each incoming-call value// for each incoming-call value



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

372

373
					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {for(AnnotatedFact<D>callerTargetAnnotatedFact:callerTargetFacts){						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());DcallerTargetFact=restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(),callerTargetAnnotatedFact.getFact());



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

374

375
						propagate(incomingEdge.getCallerSourceFact(), retSiteC, callerTargetFact, callSite, false);propagate(incomingEdge.getCallerSourceFact(),retSiteC,callerTargetFact,callSite,false);					}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

376
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

377

378

379

380

381

382
				else if(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(), d1)) {elseif(AccessPathUtil.isPrefixOf(incomingEdge.getCalleeSourceFact(),d1)){					Set<AnnotatedFact<D>> callerTargetFacts = computeReturnFlowFunction(retFunction, d2, callSite);Set<AnnotatedFact<D>>callerTargetFacts=computeReturnFlowFunction(retFunction,d2,callSite);					// for each incoming-call value// for each incoming-call value					for (AnnotatedFact<D> callerTargetAnnotatedFact : callerTargetFacts) {for(AnnotatedFact<D>callerTargetAnnotatedFact:callerTargetFacts){						D callerTargetFact = restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(), callerTargetAnnotatedFact.getFact());DcallerTargetFact=restoreContextOnReturnedFact(incomingEdge.getCallerCallSiteFact(),callerTargetAnnotatedFact.getFact());



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

383
						D callerSourceFact = AccessPathUtil.concretizeCallerSourceFact(incomingEdge, summaryEdge.getSourceFact());DcallerSourceFact=AccessPathUtil.concretizeCallerSourceFact(incomingEdge,summaryEdge.getSourceFact());



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

385

386
						propagate(callerSourceFact, retSiteC, callerTargetFact, callSite, false);propagate(callerSourceFact,retSiteC,callerTargetFact,callSite,false);					}}				}				}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

387
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

388

389
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

390

391

392

393
				//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow//handling for unbalanced problems where we return out of a method with a fact for which we have no incoming flow		//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only//note: we propagate that way only values that originate from ZERO, as conditionally generated values should only		//be propagated into callers that have an incoming edge for this condition//be propagated into callers that have an incoming edge for this condition



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

394
		if(followReturnsPastSeeds && d1 == zeroValue && incoming(methodThatNeedsSummary).isEmpty()) {if(followReturnsPastSeeds&&d1==zeroValue&&incoming(methodThatNeedsSummary).isEmpty()){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

395

396

397

398
			Collection<N> callers = icfg.getCallersOf(methodThatNeedsSummary);Collection<N>callers=icfg.getCallersOf(methodThatNeedsSummary);			for(N c: callers) {for(Nc:callers){				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(c)){					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(c,methodThatNeedsSummary,n,retSiteC);



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

399

400

401
					Set<AnnotatedFact<D>> targets = computeReturnFlowFunction(retFunction, d2, c);Set<AnnotatedFact<D>>targets=computeReturnFlowFunction(retFunction,d2,c);					for(AnnotatedFact<D> d5: targets)for(AnnotatedFact<D>d5:targets)						propagate(zeroValue, retSiteC, d5.getFact(), c, true);propagate(zeroValue,retSiteC,d5.getFact(),c,true);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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

416

417

418

419

420

421
				}}			}}			//in cases where there are no callers, the return statement would normally not be processed at all;//in cases where there are no callers, the return statement would normally not be processed at all;			//this might be undesirable if the flow function has a side effect such as registering a taint;//this might be undesirable if the flow function has a side effect such as registering a taint;			//instead we thus call the return flow function will a null caller//instead we thus call the return flow function will a null caller			if(callers.isEmpty()) {if(callers.isEmpty()){				FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary,n,null);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(null,methodThatNeedsSummary,n,null);				retFunction.computeTargets(d2);retFunction.computeTargets(d2);			}}		}}	}}		/**/**	 * Computes the return flow function for the given set of caller-side	 * Computes the return flow function for the given set of caller-side	 * abstractions.	 * abstractions.	 * @param retFunction The return flow function to compute	 * @param retFunction The return flow function to compute	 * @param d2 The abstraction at the exit node in the callee	 * @param d2 The abstraction at the exit node in the callee	 * @param callSite The call site	 * @param callSite The call site	 * @return The set of caller-side abstractions at the return site	 * @return The set of caller-side abstractions at the return site	 */	 */



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

422
	protected Set<AnnotatedFact<D>> computeReturnFlowFunctionprotectedSet<AnnotatedFact<D>>computeReturnFlowFunction



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

423
			(FlowFunction<D> retFunction, D d2, N callSite) {(FlowFunction<D>retFunction,Dd2,NcallSite){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

424

425

426

427

428

429

430

431

432

433

434

435

436

437

438
		return retFunction.computeTargets(d2);returnretFunction.computeTargets(d2);	}}	/**/**	 * Lines 33-37 of the algorithm.	 * Lines 33-37 of the algorithm.	 * Simply propagate normal, intra-procedural flows.	 * Simply propagate normal, intra-procedural flows.	 * @param edge	 * @param edge	 */	 */	private void processNormalFlow(PathEdge<N,D> edge) {privatevoidprocessNormalFlow(PathEdge<N,D>edge){		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); finalNn=edge.getTarget();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();				for (N m : icfg.getSuccsOf(n)) {for(Nm:icfg.getSuccsOf(n)){			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);FlowFunction<D>flowFunction=flowFunctions.getNormalFlowFunction(n,m);



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

439

440
			Set<AnnotatedFact<D>> res = computeNormalFlowFunction(flowFunction, d1, d2);Set<AnnotatedFact<D>>res=computeNormalFlowFunction(flowFunction,d1,d2);			for (AnnotatedFact<D> d3 : res) {for(AnnotatedFact<D>d3:res){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

441

442

443
				//TODO: double check if concurrency issues may arise//TODO: double check if concurrency issues may arise								//if reading field f//if reading field f



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

444

445

446

447

448

449

450

451
				// if d1.f element of incoming edges:// if d1.f element of incoming edges:				//    create and propagate (d1.f, d2.f)//    create and propagate (d1.f, d2.f)				// else // else 				//	  create and set (d1.f, d2.f) on hold//	  create and set (d1.f, d2.f) on hold				//	  create for each incoming edge inc: (inc.call-d1.f, inc.call-d2.f) and put on hold//	  create for each incoming edge inc: (inc.call-d1.f, inc.call-d2.f) and put on hold								if(d3.getReadField() instanceof SpecificFieldReference) {if(d3.getReadField()instanceofSpecificFieldReference){					SpecificFieldReference fieldRef = (SpecificFieldReference) d3.getReadField();SpecificFieldReferencefieldRef=(SpecificFieldReference)d3.getReadField();



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

452

453
					D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(d1, fieldRef);DconcretizedSourceValue=AccessPathUtil.cloneWithConcatenatedAccessPath(d1,fieldRef);					if(checkForInterestedCallers(d1, n, fieldRef)) {if(checkForInterestedCallers(d1,n,fieldRef)){



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

454
						propagate(concretizedSourceValue, m, d3.getFact(), null, false);propagate(concretizedSourceValue,m,d3.getFact(),null,false);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

455

456
					} else {}else{						pauseEdge(concretizedSourceValue, m, d3.getFact());pauseEdge(concretizedSourceValue,m,d3.getFact());



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

457

458
					}}				}}



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

459
				else {else{



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

460

461

462

463

464

465

466

467

468
								//TODO: if writing field f//TODO: if writing field f				// create edge e = (d1, d2.*\{f})// create edge e = (d1, d2.*\{f})				// if d2.*\{f} element of incoming edges// if d2.*\{f} element of incoming edges				// 		continue with e// 		continue with e				// else // else 				//		put e on hold//		put e on hold				// always kill (d1, d2)// always kill (d1, d2)				



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

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

480

481

482

483

484

485

486

487

488

489

490

491

492

493

494

495

496

497

498

499

500

501

502

503

504

505

506
					propagate(d1, m, d3.getFact(), null, false);propagate(d1,m,d3.getFact(),null,false);				}}			}}		}}	}}		private void pauseEdge(D sourceValue, N targetStmt, D targetValue) {privatevoidpauseEdge(DsourceValue,NtargetStmt,DtargetValue){		M method = icfg.getMethodOf(targetStmt);Mmethod=icfg.getMethodOf(targetStmt);		Set<PathEdge<N, D>> edges = pausedEdges.putIfAbsentElseGet(method, new ConcurrentHashSet<PathEdge<N,D>>());Set<PathEdge<N,D>>edges=pausedEdges.putIfAbsentElseGet(method,newConcurrentHashSet<PathEdge<N,D>>());		edges.add(new PathEdge<N,D>(sourceValue, targetStmt, targetValue));edges.add(newPathEdge<N,D>(sourceValue,targetStmt,targetValue));		logger.trace("PAUSED: <{},{}> -> <{},{}>", method, sourceValue, targetStmt, targetValue);logger.trace("PAUSED: <{},{}> -> <{},{}>",method,sourceValue,targetStmt,targetValue);	}}	private boolean checkForInterestedCallers(D calleeSourceFact, N targetStmt, SpecificFieldReference fieldRef) {privatebooleancheckForInterestedCallers(DcalleeSourceFact,NtargetStmt,SpecificFieldReferencefieldRef){		M calleeMethod = icfg.getMethodOf(targetStmt);McalleeMethod=icfg.getMethodOf(targetStmt);		logger.trace("Checking interest at method {} in fact {} with field access {}", calleeMethod, calleeSourceFact, fieldRef);logger.trace("Checking interest at method {} in fact {} with field access {}",calleeMethod,calleeSourceFact,fieldRef);				if(calleeSourceFact.equals(zeroValue))if(calleeSourceFact.equals(zeroValue))			return true;returntrue;				if(hasInterestedCallers(calleeSourceFact, calleeMethod, fieldRef)) {if(hasInterestedCallers(calleeSourceFact,calleeMethod,fieldRef)){			return true;returntrue;		}}				Set<IncomingEdge<D, N>> inc = incomingEdgesPrefixesOf(calleeMethod, calleeSourceFact); Set<IncomingEdge<D,N>>inc=incomingEdgesPrefixesOf(calleeMethod,calleeSourceFact);		for (IncomingEdge<D, N> incomingEdge : inc) {for(IncomingEdge<D,N>incomingEdge:inc){			if(checkForInterestedCallers(incomingEdge.getCallerSourceFact(), incomingEdge.getCallSite(), fieldRef)) {if(checkForInterestedCallers(incomingEdge.getCallerSourceFact(),incomingEdge.getCallSite(),fieldRef)){				propagate(incomingEdge.getCallerSourceFact().equals(zeroValue) ? propagate(incomingEdge.getCallerSourceFact().equals(zeroValue)?							incomingEdge.getCallerSourceFact() : incomingEdge.getCallerSourceFact():							cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(),fieldRef),						incomingEdge.getCallSite(), incomingEdge.getCallSite(),						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef), null, false); cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(),fieldRef),null,false);				return true;returntrue;			}}			else {else{				pauseEdge(cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(), fieldRef), pauseEdge(cloneWithConcatenatedAccessPath(incomingEdge.getCallerSourceFact(),fieldRef),						incomingEdge.getCallSite(), incomingEdge.getCallSite(),						cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(), fieldRef));cloneWithConcatenatedAccessPath(incomingEdge.getCallerCallSiteFact(),fieldRef));



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

507
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

508
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

509

510

511

512

513

514

515

516
				return false;returnfalse;	}}		private boolean hasInterestedCallers(D calleeSourceFact, M calleeMethod, SpecificFieldReference fieldRef) {privatebooleanhasInterestedCallers(DcalleeSourceFact,McalleeMethod,SpecificFieldReferencefieldRef){		D concretizedSourceValue = AccessPathUtil.cloneWithConcatenatedAccessPath(calleeSourceFact, fieldRef);DconcretizedSourceValue=AccessPathUtil.cloneWithConcatenatedAccessPath(calleeSourceFact,fieldRef);		Set<IncomingEdge<D, N>> incomingEdges = incomingEdgesPrefixedWith(calleeMethod, concretizedSourceValue);Set<IncomingEdge<D,N>>incomingEdges=incomingEdgesPrefixedWith(calleeMethod,concretizedSourceValue);		return !incomingEdges.isEmpty();return!incomingEdges.isEmpty();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

517

518

519

520

521

522

523

524

525

526
	}}		/**/**	 * Computes the normal flow function for the given set of start and end	 * Computes the normal flow function for the given set of start and end	 * abstractions.	 * abstractions.	 * @param flowFunction The normal flow function to compute	 * @param flowFunction The normal flow function to compute	 * @param d1 The abstraction at the method's start node	 * @param d1 The abstraction at the method's start node	 * @param d1 The abstraction at the current node	 * @param d1 The abstraction at the current node	 * @return The set of abstractions at the successor node	 * @return The set of abstractions at the successor node	 */	 */



annotated facts (WIP)


 

 


Johannes Lerch
committed
Nov 13, 2014



annotated facts (WIP)


 

 

annotated facts (WIP)

 

Johannes Lerch
committed
Nov 13, 2014

527
	protected Set<AnnotatedFact<D>> computeNormalFlowFunctionprotectedSet<AnnotatedFact<D>>computeNormalFlowFunction



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

528

529

530

531

532

533

534

535

536

537

538

539

540

541

542

543

544

545

546

547

548

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

572

573

574

575

576

577

578
			(FlowFunction<D> flowFunction, D d1, D d2) {(FlowFunction<D>flowFunction,Dd1,Dd2){		return flowFunction.computeTargets(d2);returnflowFunction.computeTargets(d2);	}}		/**/**	 * This method will be called for each incoming edge and can be used to	 * This method will be called for each incoming edge and can be used to	 * transfer knowledge from the calling edge to the returning edge, without	 * transfer knowledge from the calling edge to the returning edge, without	 * affecting the summary edges at the callee.	 * affecting the summary edges at the callee.	 * 	 * 	 * @param d4	 * @param d4	 *            Fact stored with the incoming edge, i.e., present at the	 *            Fact stored with the incoming edge, i.e., present at the	 *            caller side	 *            caller side	 * @param d5	 * @param d5	 *            Fact that originally should be propagated to the caller.	 *            Fact that originally should be propagated to the caller.	 * @return Fact that will be propagated to the caller.	 * @return Fact that will be propagated to the caller.	 */	 */	protected D restoreContextOnReturnedFact(D d4, D d5) {protectedDrestoreContextOnReturnedFact(Dd4,Dd5){		d5.setCallingContext(d4);d5.setCallingContext(d4);		return d5;returnd5;	}}			/**/**	 * Propagates the flow further down the exploded super graph. 	 * Propagates the flow further down the exploded super graph. 	 * @param sourceVal the source value of the propagated summary edge	 * @param sourceVal the source value of the propagated summary edge	 * @param target the target statement	 * @param target the target statement	 * @param targetVal the target value at the target statement	 * @param targetVal the target value at the target statement	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise	 * @param relatedCallSite for call and return flows the related call statement, <code>null</code> otherwise	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver}) 	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return	 * @param isUnbalancedReturn <code>true</code> if this edge is propagating an unbalanced return	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})	 *        (this value is not used within this implementation but may be useful for subclasses of {@link IFDSSolver})	 */	 */	protected void propagate(D sourceVal, N target, D targetVal,protectedvoidpropagate(DsourceVal,Ntarget,DtargetVal,			/* deliberately exposed to clients */ N relatedCallSite,/* deliberately exposed to clients */NrelatedCallSite,			/* deliberately exposed to clients */ boolean isUnbalancedReturn) {/* deliberately exposed to clients */booleanisUnbalancedReturn){		final PathEdge<N,D> edge = new PathEdge<N,D>(sourceVal, target, targetVal);finalPathEdge<N,D>edge=newPathEdge<N,D>(sourceVal,target,targetVal);		final D existingVal = jumpFn.addFunction(edge);finalDexistingVal=jumpFn.addFunction(edge);		//TODO: Merge d.* and d.x for arbitrary x as d.*//TODO: Merge d.* and d.x for arbitrary x as d.*		//TODO: Merge d.* and d.*\{x} as d.*//TODO: Merge d.* and d.*\{x} as d.*		//TODO: Merge d.*\{a} and d.*/{b} as d.*//TODO: Merge d.*\{a} and d.*/{b} as d.*		if (existingVal != null) {if(existingVal!=null){			if (existingVal != targetVal)if(existingVal!=targetVal)				existingVal.addNeighbor(targetVal);existingVal.addNeighbor(targetVal);		}}		else {else{			scheduleEdgeProcessing(edge);scheduleEdgeProcessing(edge);			if(targetVal!=zeroValue)if(targetVal!=zeroValue)				logger.trace("EDGE: <{},{}> -> <{},{}>", icfg.getMethodOf(target), sourceVal, target, targetVal);logger.trace("EDGE: <{},{}> -> <{},{}>",icfg.getMethodOf(target),sourceVal,target,targetVal);		}}	}}



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

579

580

581

582

583

584

585

586
	private Set<SummaryEdge<D, N>> endSummary(M m, final D d3) {privateSet<SummaryEdge<D,N>>endSummary(Mm,finalDd3){		Set<SummaryEdge<D, N>> map = endSummary.get(m);Set<SummaryEdge<D,N>>map=endSummary.get(m);		if(map == null)if(map==null)			return null;returnnull;				return Sets.filter(map, new Predicate<SummaryEdge<D,N>>() {returnSets.filter(map,newPredicate<SummaryEdge<D,N>>(){			@Override@Override			public boolean apply(SummaryEdge<D, N> edge) {publicbooleanapply(SummaryEdge<D,N>edge){



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

587
				return AccessPathUtil.isPrefixOf(edge.getSourceFact(), d3) || AccessPathUtil.isPrefixOf(d3, edge.getSourceFact());returnAccessPathUtil.isPrefixOf(edge.getSourceFact(),d3)||AccessPathUtil.isPrefixOf(d3,edge.getSourceFact());



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

588

589
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

590

591
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

592

593

594

595
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

596

597
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

598

599
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

600
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

601
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

602

603

604

605
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

606

607

608

609

610

611

612

613

614

615

616
	protected Set<IncomingEdge<D, N>> incomingEdgesPrefixedWith(M m, final D fact) {protectedSet<IncomingEdge<D,N>>incomingEdgesPrefixedWith(Mm,finalDfact){		Set<IncomingEdge<D, N>> result = incoming(m);Set<IncomingEdge<D,N>>result=incoming(m);		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {returnSets.filter(result,newPredicate<IncomingEdge<D,N>>(){			@Override@Override			public boolean apply(IncomingEdge<D, N> edge) {publicbooleanapply(IncomingEdge<D,N>edge){				return AccessPathUtil.isPrefixOf(fact, edge.getCalleeSourceFact());returnAccessPathUtil.isPrefixOf(fact,edge.getCalleeSourceFact());			}}		});});	}}		protected Set<IncomingEdge<D, N>> incomingEdgesPrefixesOf(M m, final D fact) {protectedSet<IncomingEdge<D,N>>incomingEdgesPrefixesOf(Mm,finalDfact){



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

617

618

619

620
		Set<IncomingEdge<D, N>> result = incoming(m);Set<IncomingEdge<D,N>>result=incoming(m);		return Sets.filter(result, new Predicate<IncomingEdge<D,N>>() {returnSets.filter(result,newPredicate<IncomingEdge<D,N>>(){			@Override@Override			public boolean apply(IncomingEdge<D, N> edge) {publicbooleanapply(IncomingEdge<D,N>edge){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

621
				return AccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(), fact);returnAccessPathUtil.isPrefixOf(edge.getCalleeSourceFact(),fact);



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

622

623
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

624

625
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

626
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

627
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

628

629
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
	}}		/**/**	 * Factory method for this solver's thread-pool executor.	 * Factory method for this solver's thread-pool executor.	 */	 */	protected CountingThreadPoolExecutor getExecutor() {protectedCountingThreadPoolExecutorgetExecutor(){		return new CountingThreadPoolExecutor(1, this.numThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());returnnewCountingThreadPoolExecutor(1,this.numThreads,30,TimeUnit.SECONDS,newLinkedBlockingQueue<Runnable>());	}}		/**/**	 * Returns a String used to identify the output of this solver in debug mode.	 * Returns a String used to identify the output of this solver in debug mode.	 * Subclasses can overwrite this string to distinguish the output from different solvers.	 * Subclasses can overwrite this string to distinguish the output from different solvers.	 */	 */	protected String getDebugName() {protectedStringgetDebugName(){		return "FAST IFDS SOLVER";return"FAST IFDS SOLVER";	}}	public void printStats() {publicvoidprintStats(){		if(logger.isDebugEnabled()) {if(logger.isDebugEnabled()){			if(ffCache!=null)if(ffCache!=null)				ffCache.printStats();ffCache.printStats();		} else {}else{			logger.info("No statistics were collected, as DEBUG is disabled.");logger.info("No statistics were collected, as DEBUG is disabled.");		}}	}}		private class PathEdgeProcessingTask implements Runnable {privateclassPathEdgeProcessingTaskimplementsRunnable{		private final PathEdge<N,D> edge;privatefinalPathEdge<N,D>edge;		public PathEdgeProcessingTask(PathEdge<N,D> edge) {publicPathEdgeProcessingTask(PathEdge<N,D>edge){			this.edge = edge;this.edge=edge;		}}		public void run() {publicvoidrun(){			if(icfg.isCallStmt(edge.getTarget())) {if(icfg.isCallStmt(edge.getTarget())){				processCall(edge);processCall(edge);			} else {}else{				//note that some statements, such as "throw" may be//note that some statements, such as "throw" may be				//both an exit statement and a "normal" statement//both an exit statement and a "normal" statement				if(icfg.isExitStmt(edge.getTarget())) {if(icfg.isExitStmt(edge.getTarget())){					processExit(edge);processExit(edge);				}}				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()){					processNormalFlow(edge);processNormalFlow(edge);				}}			}}		}}	}}	}}





