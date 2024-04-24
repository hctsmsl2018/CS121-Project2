



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

f7c0f2f622af3688a85e5cff335f57d270512b65

















f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-generic


de


bodden


ide


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



24.6 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




18




19




20




21




22




23




24




25




26






import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.EdgeFunction;
import de.bodden.ide.EdgeFunctionCache;
import de.bodden.ide.EdgeFunctions;
import de.bodden.ide.FlowFunction;
import de.bodden.ide.FlowFunctionCache;
import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IDETabulationProblem;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.JoinLattice;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ZeroedFlowFunctions;
import de.bodden.ide.edgefunc.EdgeIdentity;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






51




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






52




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






53




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






54




55




56




57




58




59




60




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






61




	protected static final boolean DEBUG = false;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






62




63




64




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






65




	protected ExecutorService executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






66




67




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






68




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




	
	//the number of currently running tasks









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






71




	protected final AtomicInteger numTasks = new AtomicInteger();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






72




73




74




75





	@SynchronizedBy("consistent lock on field")
	//We are using a LinkedHashSet here to enforce FIFO semantics, which leads to a breath-first construction
	//of the exploded super graph. As we observed in experiments, this can speed up the construction.









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






76




	protected final Collection<PathEdge<N,D,M>> pathWorklist = new LinkedHashSet<PathEdge<N,D,M>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






77




78




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






79




	protected final JumpFunctions<N,D,V> jumpFn;









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




	protected final SummaryFunctions<N,D,V> summaryFunctions = new SummaryFunctions<N,D,V>();









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









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






104




	protected final Set<N> initialSeeds;









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




112




	
	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






113




	protected final List<Pair<N,D>> nodeWorklist = new LinkedList<Pair<N,D>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






114




115





	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






116




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






134




	protected final D zeroValue;









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




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






138




139





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






140




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




234




235




236




237




238




239




240




241




242




243




244




245




246




247




248




249




250




251




252




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




278




279




280




281




282




283




284




285




286




287




288




289




290




291




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




307




308




309




310




311




312




313




314




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




371




372




373




374




375





	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);
	}
	
	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {
		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();
		FlowFunctions<N, D, M> flowFunctions = new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue());
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
	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * Uses a number of threads equal to the return value of
	 * <code>Runtime.getRuntime().availableProcessors()</code>.
	 */
	public void solve() {
		solve(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * @param numThreads The number of threads to use.
	 */
	public void solve(int numThreads) {
		if(numThreads<2) {
			this.executor = Executors.newSingleThreadExecutor();
			this.numThreads = 1;
		} else {
			this.executor = Executors.newFixedThreadPool(numThreads);
			this.numThreads = numThreads;
		}
		
		for(N startPoint: initialSeeds) {
			propagate(zeroValue, startPoint, zeroValue, allTop);
			pathWorklist.add(new PathEdge<N,D,M>(zeroValue, startPoint, zeroValue));
			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
		{
			final long before = System.currentTimeMillis();
			forwardComputeJumpFunctionsSLRPs();		
			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}
		{
			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		
		executor.shutdown();
	}

	/**
	 * Forward-tabulates the same-level realizable paths and associated functions.
	 * Note that this is a little different from the original IFDS formulations because
	 * we can have statements that are, for instance, both "normal" and "exit" statements.
	 * This is for instance the case on a "throw" statement that may on the one hand
	 * lead to a catch block but on the other hand exit the method depending
	 * on the exception being thrown.
	 */
	private void forwardComputeJumpFunctionsSLRPs() {
		while(true) {
			
			synchronized (pathWorklist) {
				if(!pathWorklist.isEmpty()) {
					//pop edge
					Iterator<PathEdge<N,D,M>> iter = pathWorklist.iterator();
					PathEdge<N,D,M> edge = iter.next();
					iter.remove();
					numTasks.getAndIncrement();

					//dispatch processing of edge (potentially in a different thread)
					executor.execute(new PathEdgeProcessingTask(edge));
					propagationCount++;
				} else if(numTasks.intValue()==0){
					//path worklist is empty; no running tasks, we are done
					return;
				} else {
					//the path worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						pathWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)
		for(N startPoint: initialSeeds) {
			setVal(startPoint, zeroValue, valueLattice.bottomElement());
			Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, zeroValue); 
			nodeWorklist.add(superGraphNode);
		}
		while(true) {
			synchronized (nodeWorklist) {
				if(!nodeWorklist.isEmpty()) {
					//pop job
					Pair<N,D> nAndD = nodeWorklist.remove(0);	
					numTasks.getAndIncrement();
					
					//dispatch processing of job (potentially in a different thread)
					executor.execute(new ValuePropagationTask(nAndD));
				} else if(numTasks.intValue()==0) {
					//node worklist is empty; no running tasks, we are done
					break;
				} else {
					//the node worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						nodeWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;
		}		
		for(int t=0;t<numThreads; t++) {
			executor.execute(new ValueComputationTask(nonCallStartNodesArray, t));
		}
		//wait until done
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
				synchronized (nodeWorklist) {
					nodeWorklist.add(new Pair<N,D>(nHashN,nHashD));
				}
			}
		}
	}

	private V val(N nHashN, D nHashD){ 
		V l = val.get(nHashN, nHashD);
		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	
	private void setVal(N nHashN, D nHashD,V l){ 
		val.put(nHashN, nHashD,l);
		if(DEBUG)









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






376




			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






377




378




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




391




392




393




394




395




396




397




398




399




400




401




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




432




433




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




458




459




460




461




462




463




464




465




466




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




527




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




579




580




581




582




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




604




605




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

	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();
		
		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);
			for(N sP: icfg.getStartPointsOf(sCalledProcN)) {			
				for(D d3: res) {
					propagate(d3, sP, d3, EdgeIdentity.<V>v()); //line 15
	
					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads
						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));						
					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez
					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();
						for(N retSiteN: icfg.getReturnSitesOfCallAt(n)) {
							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;
							for(D d5: retFunction.computeTargets(d4)) {
								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);
								synchronized (summaryFunctions) {
									EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(n, d2, retSiteN).get(d5);			
									if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
									EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5).joinWith(summaryFunction);
									if(!fPrime.equalTo(summaryFunction)) {
										summaryFunctions.insertFunction(n,d2,retSiteN,d5,fPrime);
									}	
								}
							}
						}
					}
				}		
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez
		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;
			for(D d3: callToReturnFlowFunction.computeTargets(d2)) {
				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);
				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE));
			}

			Map<D,EdgeFunction<V>> d3sAndF3s = summaryFunctions.summariesFor(n, d2, returnSiteN);
			for (Map.Entry<D,EdgeFunction<V>> d3AndF3 : d3sAndF3s.entrySet()) {
				D d3 = d3AndF3.getKey();
				EdgeFunction<V> f3 = d3AndF3.getValue();
				if(f3==null) f3 = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
				propagate(d1, returnSiteN, d3, f.composeWith(f3));
			}
		}
	}

	private EdgeFunction<V> jumpFunction(PathEdge<N, D, M> edge) {
		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}

	/**
	 * Lines 21-32 of the algorithm.	
	 */
	private void processExit(PathEdge<N,D,M> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		for(N sP: icfg.getStartPointsOf(methodThatNeedsSummary)) {
			//line 21.1 of Naeem/Lhotak/Rodriguez
			
			Set<Entry<N, Set<D>>> inc;
			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads
				inc = new HashSet<Map.Entry<N,Set<D>>>(incoming(d1, sP));
			}
			
			for (Entry<N,Set<D>> entry: inc) {
				//line 22
				N c = entry.getKey();
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
					flowFunctionConstructionCount++;
					Set<D> targets = retFunction.computeTargets(d2);
					for(D d4: entry.getValue()) {
						//line 23
						for(D d5: targets) {
							EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
							EdgeFunction<V> fPrime;
							synchronized (summaryFunctions) {
								EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(c,d4,retSiteC).get(d5);			
								if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
								fPrime = f4.composeWith(f).composeWith(f5).joinWith(summaryFunction);
								if(!fPrime.equalTo(summaryFunction)) {
									summaryFunctions.insertFunction(c,d4,retSiteC,d5,fPrime);
								}
							}
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)); {
									D d3 = valAndFunc.getKey();
									propagate(d3, retSiteC, d5, f3.composeWith(fPrime));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Lines 33-37 of the algorithm.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;
			Set<D> res = flowFunction.computeTargets(d2);
			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));
				propagate(d1, m, d3, fprime); 
			}
		}
	}
	
	private void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f) {
		EdgeFunction<V> jumpFnE;
		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);
		}
		if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
		EdgeFunction<V> fPrime = jumpFnE.joinWith(f);
		if(!fPrime.equalTo(jumpFnE)) {
			synchronized (jumpFn) {
				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}
			
			PathEdge<N,D,M> edge = new PathEdge<N,D,M>(sourceVal, target, targetVal);
			synchronized (pathWorklist) {
				pathWorklist.add(edge);
			}

			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();
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
		summaries.put(eP,d2,f);
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
	 * Returns the V-type result for the given value at the given statement. 
	 */
	public V resultAt(N stmt, D value) {
		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.
	 * The artificial zero value is automatically stripped.
	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value
		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}

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
		private final PathEdge<N, D, M> edge;

		public PathEdgeProcessingTask(PathEdge<N, D, M> edge) {
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
			synchronized (pathWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				pathWorklist.notify();
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
			if(icfg.isStartPoint(n)) {
				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
			synchronized (nodeWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				nodeWorklist.notify();
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

f7c0f2f622af3688a85e5cff335f57d270512b65

















f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-generic


de


bodden


ide


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



24.6 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




18




19




20




21




22




23




24




25




26






import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.EdgeFunction;
import de.bodden.ide.EdgeFunctionCache;
import de.bodden.ide.EdgeFunctions;
import de.bodden.ide.FlowFunction;
import de.bodden.ide.FlowFunctionCache;
import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IDETabulationProblem;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.JoinLattice;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ZeroedFlowFunctions;
import de.bodden.ide.edgefunc.EdgeIdentity;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






51




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






52




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






53




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






54




55




56




57




58




59




60




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






61




	protected static final boolean DEBUG = false;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






62




63




64




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






65




	protected ExecutorService executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






66




67




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






68




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




	
	//the number of currently running tasks









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






71




	protected final AtomicInteger numTasks = new AtomicInteger();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






72




73




74




75





	@SynchronizedBy("consistent lock on field")
	//We are using a LinkedHashSet here to enforce FIFO semantics, which leads to a breath-first construction
	//of the exploded super graph. As we observed in experiments, this can speed up the construction.









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






76




	protected final Collection<PathEdge<N,D,M>> pathWorklist = new LinkedHashSet<PathEdge<N,D,M>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






77




78




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






79




	protected final JumpFunctions<N,D,V> jumpFn;









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




	protected final SummaryFunctions<N,D,V> summaryFunctions = new SummaryFunctions<N,D,V>();









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









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






104




	protected final Set<N> initialSeeds;









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




112




	
	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






113




	protected final List<Pair<N,D>> nodeWorklist = new LinkedList<Pair<N,D>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






114




115





	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






116




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






134




	protected final D zeroValue;









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




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






138




139





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






140




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




234




235




236




237




238




239




240




241




242




243




244




245




246




247




248




249




250




251




252




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




278




279




280




281




282




283




284




285




286




287




288




289




290




291




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




307




308




309




310




311




312




313




314




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




371




372




373




374




375





	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);
	}
	
	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {
		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();
		FlowFunctions<N, D, M> flowFunctions = new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue());
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
	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * Uses a number of threads equal to the return value of
	 * <code>Runtime.getRuntime().availableProcessors()</code>.
	 */
	public void solve() {
		solve(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * @param numThreads The number of threads to use.
	 */
	public void solve(int numThreads) {
		if(numThreads<2) {
			this.executor = Executors.newSingleThreadExecutor();
			this.numThreads = 1;
		} else {
			this.executor = Executors.newFixedThreadPool(numThreads);
			this.numThreads = numThreads;
		}
		
		for(N startPoint: initialSeeds) {
			propagate(zeroValue, startPoint, zeroValue, allTop);
			pathWorklist.add(new PathEdge<N,D,M>(zeroValue, startPoint, zeroValue));
			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
		{
			final long before = System.currentTimeMillis();
			forwardComputeJumpFunctionsSLRPs();		
			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}
		{
			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		
		executor.shutdown();
	}

	/**
	 * Forward-tabulates the same-level realizable paths and associated functions.
	 * Note that this is a little different from the original IFDS formulations because
	 * we can have statements that are, for instance, both "normal" and "exit" statements.
	 * This is for instance the case on a "throw" statement that may on the one hand
	 * lead to a catch block but on the other hand exit the method depending
	 * on the exception being thrown.
	 */
	private void forwardComputeJumpFunctionsSLRPs() {
		while(true) {
			
			synchronized (pathWorklist) {
				if(!pathWorklist.isEmpty()) {
					//pop edge
					Iterator<PathEdge<N,D,M>> iter = pathWorklist.iterator();
					PathEdge<N,D,M> edge = iter.next();
					iter.remove();
					numTasks.getAndIncrement();

					//dispatch processing of edge (potentially in a different thread)
					executor.execute(new PathEdgeProcessingTask(edge));
					propagationCount++;
				} else if(numTasks.intValue()==0){
					//path worklist is empty; no running tasks, we are done
					return;
				} else {
					//the path worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						pathWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)
		for(N startPoint: initialSeeds) {
			setVal(startPoint, zeroValue, valueLattice.bottomElement());
			Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, zeroValue); 
			nodeWorklist.add(superGraphNode);
		}
		while(true) {
			synchronized (nodeWorklist) {
				if(!nodeWorklist.isEmpty()) {
					//pop job
					Pair<N,D> nAndD = nodeWorklist.remove(0);	
					numTasks.getAndIncrement();
					
					//dispatch processing of job (potentially in a different thread)
					executor.execute(new ValuePropagationTask(nAndD));
				} else if(numTasks.intValue()==0) {
					//node worklist is empty; no running tasks, we are done
					break;
				} else {
					//the node worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						nodeWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;
		}		
		for(int t=0;t<numThreads; t++) {
			executor.execute(new ValueComputationTask(nonCallStartNodesArray, t));
		}
		//wait until done
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
				synchronized (nodeWorklist) {
					nodeWorklist.add(new Pair<N,D>(nHashN,nHashD));
				}
			}
		}
	}

	private V val(N nHashN, D nHashD){ 
		V l = val.get(nHashN, nHashD);
		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	
	private void setVal(N nHashN, D nHashD,V l){ 
		val.put(nHashN, nHashD,l);
		if(DEBUG)









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






376




			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






377




378




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




391




392




393




394




395




396




397




398




399




400




401




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




432




433




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




458




459




460




461




462




463




464




465




466




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




527




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




579




580




581




582




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




604




605




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

	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();
		
		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);
			for(N sP: icfg.getStartPointsOf(sCalledProcN)) {			
				for(D d3: res) {
					propagate(d3, sP, d3, EdgeIdentity.<V>v()); //line 15
	
					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads
						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));						
					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez
					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();
						for(N retSiteN: icfg.getReturnSitesOfCallAt(n)) {
							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;
							for(D d5: retFunction.computeTargets(d4)) {
								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);
								synchronized (summaryFunctions) {
									EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(n, d2, retSiteN).get(d5);			
									if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
									EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5).joinWith(summaryFunction);
									if(!fPrime.equalTo(summaryFunction)) {
										summaryFunctions.insertFunction(n,d2,retSiteN,d5,fPrime);
									}	
								}
							}
						}
					}
				}		
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez
		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;
			for(D d3: callToReturnFlowFunction.computeTargets(d2)) {
				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);
				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE));
			}

			Map<D,EdgeFunction<V>> d3sAndF3s = summaryFunctions.summariesFor(n, d2, returnSiteN);
			for (Map.Entry<D,EdgeFunction<V>> d3AndF3 : d3sAndF3s.entrySet()) {
				D d3 = d3AndF3.getKey();
				EdgeFunction<V> f3 = d3AndF3.getValue();
				if(f3==null) f3 = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
				propagate(d1, returnSiteN, d3, f.composeWith(f3));
			}
		}
	}

	private EdgeFunction<V> jumpFunction(PathEdge<N, D, M> edge) {
		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}

	/**
	 * Lines 21-32 of the algorithm.	
	 */
	private void processExit(PathEdge<N,D,M> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		for(N sP: icfg.getStartPointsOf(methodThatNeedsSummary)) {
			//line 21.1 of Naeem/Lhotak/Rodriguez
			
			Set<Entry<N, Set<D>>> inc;
			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads
				inc = new HashSet<Map.Entry<N,Set<D>>>(incoming(d1, sP));
			}
			
			for (Entry<N,Set<D>> entry: inc) {
				//line 22
				N c = entry.getKey();
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
					flowFunctionConstructionCount++;
					Set<D> targets = retFunction.computeTargets(d2);
					for(D d4: entry.getValue()) {
						//line 23
						for(D d5: targets) {
							EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
							EdgeFunction<V> fPrime;
							synchronized (summaryFunctions) {
								EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(c,d4,retSiteC).get(d5);			
								if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
								fPrime = f4.composeWith(f).composeWith(f5).joinWith(summaryFunction);
								if(!fPrime.equalTo(summaryFunction)) {
									summaryFunctions.insertFunction(c,d4,retSiteC,d5,fPrime);
								}
							}
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)); {
									D d3 = valAndFunc.getKey();
									propagate(d3, retSiteC, d5, f3.composeWith(fPrime));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Lines 33-37 of the algorithm.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;
			Set<D> res = flowFunction.computeTargets(d2);
			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));
				propagate(d1, m, d3, fprime); 
			}
		}
	}
	
	private void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f) {
		EdgeFunction<V> jumpFnE;
		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);
		}
		if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
		EdgeFunction<V> fPrime = jumpFnE.joinWith(f);
		if(!fPrime.equalTo(jumpFnE)) {
			synchronized (jumpFn) {
				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}
			
			PathEdge<N,D,M> edge = new PathEdge<N,D,M>(sourceVal, target, targetVal);
			synchronized (pathWorklist) {
				pathWorklist.add(edge);
			}

			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();
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
		summaries.put(eP,d2,f);
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
	 * Returns the V-type result for the given value at the given statement. 
	 */
	public V resultAt(N stmt, D value) {
		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.
	 * The artificial zero value is automatically stripped.
	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value
		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}

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
		private final PathEdge<N, D, M> edge;

		public PathEdgeProcessingTask(PathEdge<N, D, M> edge) {
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
			synchronized (pathWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				pathWorklist.notify();
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
			if(icfg.isStartPoint(n)) {
				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
			synchronized (nodeWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				nodeWorklist.notify();
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

f7c0f2f622af3688a85e5cff335f57d270512b65







Open sidebar



Joshua Garcia heros

f7c0f2f622af3688a85e5cff335f57d270512b65




Open sidebar

Joshua Garcia heros

f7c0f2f622af3688a85e5cff335f57d270512b65


Joshua Garciaherosheros
f7c0f2f622af3688a85e5cff335f57d270512b65










f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-generic


de


bodden


ide


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



24.6 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




18




19




20




21




22




23




24




25




26






import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.EdgeFunction;
import de.bodden.ide.EdgeFunctionCache;
import de.bodden.ide.EdgeFunctions;
import de.bodden.ide.FlowFunction;
import de.bodden.ide.FlowFunctionCache;
import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IDETabulationProblem;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.JoinLattice;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ZeroedFlowFunctions;
import de.bodden.ide.edgefunc.EdgeIdentity;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






51




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






52




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






53




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






54




55




56




57




58




59




60




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






61




	protected static final boolean DEBUG = false;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






62




63




64




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






65




	protected ExecutorService executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






66




67




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






68




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




	
	//the number of currently running tasks









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






71




	protected final AtomicInteger numTasks = new AtomicInteger();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






72




73




74




75





	@SynchronizedBy("consistent lock on field")
	//We are using a LinkedHashSet here to enforce FIFO semantics, which leads to a breath-first construction
	//of the exploded super graph. As we observed in experiments, this can speed up the construction.









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






76




	protected final Collection<PathEdge<N,D,M>> pathWorklist = new LinkedHashSet<PathEdge<N,D,M>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






77




78




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






79




	protected final JumpFunctions<N,D,V> jumpFn;









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




	protected final SummaryFunctions<N,D,V> summaryFunctions = new SummaryFunctions<N,D,V>();









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









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






104




	protected final Set<N> initialSeeds;









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




112




	
	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






113




	protected final List<Pair<N,D>> nodeWorklist = new LinkedList<Pair<N,D>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






114




115





	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






116




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






134




	protected final D zeroValue;









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




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






138




139





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






140




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




234




235




236




237




238




239




240




241




242




243




244




245




246




247




248




249




250




251




252




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




278




279




280




281




282




283




284




285




286




287




288




289




290




291




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




307




308




309




310




311




312




313




314




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




371




372




373




374




375





	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);
	}
	
	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {
		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();
		FlowFunctions<N, D, M> flowFunctions = new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue());
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
	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * Uses a number of threads equal to the return value of
	 * <code>Runtime.getRuntime().availableProcessors()</code>.
	 */
	public void solve() {
		solve(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * @param numThreads The number of threads to use.
	 */
	public void solve(int numThreads) {
		if(numThreads<2) {
			this.executor = Executors.newSingleThreadExecutor();
			this.numThreads = 1;
		} else {
			this.executor = Executors.newFixedThreadPool(numThreads);
			this.numThreads = numThreads;
		}
		
		for(N startPoint: initialSeeds) {
			propagate(zeroValue, startPoint, zeroValue, allTop);
			pathWorklist.add(new PathEdge<N,D,M>(zeroValue, startPoint, zeroValue));
			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
		{
			final long before = System.currentTimeMillis();
			forwardComputeJumpFunctionsSLRPs();		
			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}
		{
			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		
		executor.shutdown();
	}

	/**
	 * Forward-tabulates the same-level realizable paths and associated functions.
	 * Note that this is a little different from the original IFDS formulations because
	 * we can have statements that are, for instance, both "normal" and "exit" statements.
	 * This is for instance the case on a "throw" statement that may on the one hand
	 * lead to a catch block but on the other hand exit the method depending
	 * on the exception being thrown.
	 */
	private void forwardComputeJumpFunctionsSLRPs() {
		while(true) {
			
			synchronized (pathWorklist) {
				if(!pathWorklist.isEmpty()) {
					//pop edge
					Iterator<PathEdge<N,D,M>> iter = pathWorklist.iterator();
					PathEdge<N,D,M> edge = iter.next();
					iter.remove();
					numTasks.getAndIncrement();

					//dispatch processing of edge (potentially in a different thread)
					executor.execute(new PathEdgeProcessingTask(edge));
					propagationCount++;
				} else if(numTasks.intValue()==0){
					//path worklist is empty; no running tasks, we are done
					return;
				} else {
					//the path worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						pathWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)
		for(N startPoint: initialSeeds) {
			setVal(startPoint, zeroValue, valueLattice.bottomElement());
			Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, zeroValue); 
			nodeWorklist.add(superGraphNode);
		}
		while(true) {
			synchronized (nodeWorklist) {
				if(!nodeWorklist.isEmpty()) {
					//pop job
					Pair<N,D> nAndD = nodeWorklist.remove(0);	
					numTasks.getAndIncrement();
					
					//dispatch processing of job (potentially in a different thread)
					executor.execute(new ValuePropagationTask(nAndD));
				} else if(numTasks.intValue()==0) {
					//node worklist is empty; no running tasks, we are done
					break;
				} else {
					//the node worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						nodeWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;
		}		
		for(int t=0;t<numThreads; t++) {
			executor.execute(new ValueComputationTask(nonCallStartNodesArray, t));
		}
		//wait until done
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
				synchronized (nodeWorklist) {
					nodeWorklist.add(new Pair<N,D>(nHashN,nHashD));
				}
			}
		}
	}

	private V val(N nHashN, D nHashD){ 
		V l = val.get(nHashN, nHashD);
		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	
	private void setVal(N nHashN, D nHashD,V l){ 
		val.put(nHashN, nHashD,l);
		if(DEBUG)









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






376




			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






377




378




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




391




392




393




394




395




396




397




398




399




400




401




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




432




433




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




458




459




460




461




462




463




464




465




466




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




527




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




579




580




581




582




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




604




605




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

	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();
		
		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);
			for(N sP: icfg.getStartPointsOf(sCalledProcN)) {			
				for(D d3: res) {
					propagate(d3, sP, d3, EdgeIdentity.<V>v()); //line 15
	
					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads
						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));						
					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez
					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();
						for(N retSiteN: icfg.getReturnSitesOfCallAt(n)) {
							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;
							for(D d5: retFunction.computeTargets(d4)) {
								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);
								synchronized (summaryFunctions) {
									EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(n, d2, retSiteN).get(d5);			
									if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
									EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5).joinWith(summaryFunction);
									if(!fPrime.equalTo(summaryFunction)) {
										summaryFunctions.insertFunction(n,d2,retSiteN,d5,fPrime);
									}	
								}
							}
						}
					}
				}		
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez
		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;
			for(D d3: callToReturnFlowFunction.computeTargets(d2)) {
				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);
				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE));
			}

			Map<D,EdgeFunction<V>> d3sAndF3s = summaryFunctions.summariesFor(n, d2, returnSiteN);
			for (Map.Entry<D,EdgeFunction<V>> d3AndF3 : d3sAndF3s.entrySet()) {
				D d3 = d3AndF3.getKey();
				EdgeFunction<V> f3 = d3AndF3.getValue();
				if(f3==null) f3 = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
				propagate(d1, returnSiteN, d3, f.composeWith(f3));
			}
		}
	}

	private EdgeFunction<V> jumpFunction(PathEdge<N, D, M> edge) {
		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}

	/**
	 * Lines 21-32 of the algorithm.	
	 */
	private void processExit(PathEdge<N,D,M> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		for(N sP: icfg.getStartPointsOf(methodThatNeedsSummary)) {
			//line 21.1 of Naeem/Lhotak/Rodriguez
			
			Set<Entry<N, Set<D>>> inc;
			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads
				inc = new HashSet<Map.Entry<N,Set<D>>>(incoming(d1, sP));
			}
			
			for (Entry<N,Set<D>> entry: inc) {
				//line 22
				N c = entry.getKey();
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
					flowFunctionConstructionCount++;
					Set<D> targets = retFunction.computeTargets(d2);
					for(D d4: entry.getValue()) {
						//line 23
						for(D d5: targets) {
							EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
							EdgeFunction<V> fPrime;
							synchronized (summaryFunctions) {
								EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(c,d4,retSiteC).get(d5);			
								if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
								fPrime = f4.composeWith(f).composeWith(f5).joinWith(summaryFunction);
								if(!fPrime.equalTo(summaryFunction)) {
									summaryFunctions.insertFunction(c,d4,retSiteC,d5,fPrime);
								}
							}
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)); {
									D d3 = valAndFunc.getKey();
									propagate(d3, retSiteC, d5, f3.composeWith(fPrime));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Lines 33-37 of the algorithm.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;
			Set<D> res = flowFunction.computeTargets(d2);
			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));
				propagate(d1, m, d3, fprime); 
			}
		}
	}
	
	private void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f) {
		EdgeFunction<V> jumpFnE;
		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);
		}
		if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
		EdgeFunction<V> fPrime = jumpFnE.joinWith(f);
		if(!fPrime.equalTo(jumpFnE)) {
			synchronized (jumpFn) {
				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}
			
			PathEdge<N,D,M> edge = new PathEdge<N,D,M>(sourceVal, target, targetVal);
			synchronized (pathWorklist) {
				pathWorklist.add(edge);
			}

			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();
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
		summaries.put(eP,d2,f);
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
	 * Returns the V-type result for the given value at the given statement. 
	 */
	public V resultAt(N stmt, D value) {
		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.
	 * The artificial zero value is automatically stripped.
	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value
		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}

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
		private final PathEdge<N, D, M> edge;

		public PathEdgeProcessingTask(PathEdge<N, D, M> edge) {
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
			synchronized (pathWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				pathWorklist.notify();
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
			if(icfg.isStartPoint(n)) {
				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
			synchronized (nodeWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				nodeWorklist.notify();
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














f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-generic


de


bodden


ide


solver


IDESolver.java



Find file
Normal viewHistoryPermalink






IDESolver.java



24.6 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




18




19




20




21




22




23




24




25




26






import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.EdgeFunction;
import de.bodden.ide.EdgeFunctionCache;
import de.bodden.ide.EdgeFunctions;
import de.bodden.ide.FlowFunction;
import de.bodden.ide.FlowFunctionCache;
import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IDETabulationProblem;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.JoinLattice;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ZeroedFlowFunctions;
import de.bodden.ide.edgefunc.EdgeIdentity;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






51




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






52




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






53




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






54




55




56




57




58




59




60




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






61




	protected static final boolean DEBUG = false;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






62




63




64




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






65




	protected ExecutorService executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






66




67




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






68




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




	
	//the number of currently running tasks









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






71




	protected final AtomicInteger numTasks = new AtomicInteger();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






72




73




74




75





	@SynchronizedBy("consistent lock on field")
	//We are using a LinkedHashSet here to enforce FIFO semantics, which leads to a breath-first construction
	//of the exploded super graph. As we observed in experiments, this can speed up the construction.









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






76




	protected final Collection<PathEdge<N,D,M>> pathWorklist = new LinkedHashSet<PathEdge<N,D,M>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






77




78




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






79




	protected final JumpFunctions<N,D,V> jumpFn;









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




	protected final SummaryFunctions<N,D,V> summaryFunctions = new SummaryFunctions<N,D,V>();









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









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






104




	protected final Set<N> initialSeeds;









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




112




	
	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






113




	protected final List<Pair<N,D>> nodeWorklist = new LinkedList<Pair<N,D>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






114




115





	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






116




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






134




	protected final D zeroValue;









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




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






138




139





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






140




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




234




235




236




237




238




239




240




241




242




243




244




245




246




247




248




249




250




251




252




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




278




279




280




281




282




283




284




285




286




287




288




289




290




291




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




307




308




309




310




311




312




313




314




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




371




372




373




374




375





	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);
	}
	
	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {
		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();
		FlowFunctions<N, D, M> flowFunctions = new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue());
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
	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * Uses a number of threads equal to the return value of
	 * <code>Runtime.getRuntime().availableProcessors()</code>.
	 */
	public void solve() {
		solve(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * @param numThreads The number of threads to use.
	 */
	public void solve(int numThreads) {
		if(numThreads<2) {
			this.executor = Executors.newSingleThreadExecutor();
			this.numThreads = 1;
		} else {
			this.executor = Executors.newFixedThreadPool(numThreads);
			this.numThreads = numThreads;
		}
		
		for(N startPoint: initialSeeds) {
			propagate(zeroValue, startPoint, zeroValue, allTop);
			pathWorklist.add(new PathEdge<N,D,M>(zeroValue, startPoint, zeroValue));
			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
		{
			final long before = System.currentTimeMillis();
			forwardComputeJumpFunctionsSLRPs();		
			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}
		{
			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		
		executor.shutdown();
	}

	/**
	 * Forward-tabulates the same-level realizable paths and associated functions.
	 * Note that this is a little different from the original IFDS formulations because
	 * we can have statements that are, for instance, both "normal" and "exit" statements.
	 * This is for instance the case on a "throw" statement that may on the one hand
	 * lead to a catch block but on the other hand exit the method depending
	 * on the exception being thrown.
	 */
	private void forwardComputeJumpFunctionsSLRPs() {
		while(true) {
			
			synchronized (pathWorklist) {
				if(!pathWorklist.isEmpty()) {
					//pop edge
					Iterator<PathEdge<N,D,M>> iter = pathWorklist.iterator();
					PathEdge<N,D,M> edge = iter.next();
					iter.remove();
					numTasks.getAndIncrement();

					//dispatch processing of edge (potentially in a different thread)
					executor.execute(new PathEdgeProcessingTask(edge));
					propagationCount++;
				} else if(numTasks.intValue()==0){
					//path worklist is empty; no running tasks, we are done
					return;
				} else {
					//the path worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						pathWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)
		for(N startPoint: initialSeeds) {
			setVal(startPoint, zeroValue, valueLattice.bottomElement());
			Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, zeroValue); 
			nodeWorklist.add(superGraphNode);
		}
		while(true) {
			synchronized (nodeWorklist) {
				if(!nodeWorklist.isEmpty()) {
					//pop job
					Pair<N,D> nAndD = nodeWorklist.remove(0);	
					numTasks.getAndIncrement();
					
					//dispatch processing of job (potentially in a different thread)
					executor.execute(new ValuePropagationTask(nAndD));
				} else if(numTasks.intValue()==0) {
					//node worklist is empty; no running tasks, we are done
					break;
				} else {
					//the node worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						nodeWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;
		}		
		for(int t=0;t<numThreads; t++) {
			executor.execute(new ValueComputationTask(nonCallStartNodesArray, t));
		}
		//wait until done
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
				synchronized (nodeWorklist) {
					nodeWorklist.add(new Pair<N,D>(nHashN,nHashD));
				}
			}
		}
	}

	private V val(N nHashN, D nHashD){ 
		V l = val.get(nHashN, nHashD);
		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	
	private void setVal(N nHashN, D nHashD,V l){ 
		val.put(nHashN, nHashD,l);
		if(DEBUG)









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






376




			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






377




378




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




391




392




393




394




395




396




397




398




399




400




401




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




432




433




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




458




459




460




461




462




463




464




465




466




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




527




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




579




580




581




582




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




604




605




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

	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();
		
		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);
			for(N sP: icfg.getStartPointsOf(sCalledProcN)) {			
				for(D d3: res) {
					propagate(d3, sP, d3, EdgeIdentity.<V>v()); //line 15
	
					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads
						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));						
					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez
					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();
						for(N retSiteN: icfg.getReturnSitesOfCallAt(n)) {
							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;
							for(D d5: retFunction.computeTargets(d4)) {
								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);
								synchronized (summaryFunctions) {
									EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(n, d2, retSiteN).get(d5);			
									if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
									EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5).joinWith(summaryFunction);
									if(!fPrime.equalTo(summaryFunction)) {
										summaryFunctions.insertFunction(n,d2,retSiteN,d5,fPrime);
									}	
								}
							}
						}
					}
				}		
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez
		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;
			for(D d3: callToReturnFlowFunction.computeTargets(d2)) {
				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);
				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE));
			}

			Map<D,EdgeFunction<V>> d3sAndF3s = summaryFunctions.summariesFor(n, d2, returnSiteN);
			for (Map.Entry<D,EdgeFunction<V>> d3AndF3 : d3sAndF3s.entrySet()) {
				D d3 = d3AndF3.getKey();
				EdgeFunction<V> f3 = d3AndF3.getValue();
				if(f3==null) f3 = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
				propagate(d1, returnSiteN, d3, f.composeWith(f3));
			}
		}
	}

	private EdgeFunction<V> jumpFunction(PathEdge<N, D, M> edge) {
		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}

	/**
	 * Lines 21-32 of the algorithm.	
	 */
	private void processExit(PathEdge<N,D,M> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		for(N sP: icfg.getStartPointsOf(methodThatNeedsSummary)) {
			//line 21.1 of Naeem/Lhotak/Rodriguez
			
			Set<Entry<N, Set<D>>> inc;
			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads
				inc = new HashSet<Map.Entry<N,Set<D>>>(incoming(d1, sP));
			}
			
			for (Entry<N,Set<D>> entry: inc) {
				//line 22
				N c = entry.getKey();
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
					flowFunctionConstructionCount++;
					Set<D> targets = retFunction.computeTargets(d2);
					for(D d4: entry.getValue()) {
						//line 23
						for(D d5: targets) {
							EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
							EdgeFunction<V> fPrime;
							synchronized (summaryFunctions) {
								EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(c,d4,retSiteC).get(d5);			
								if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
								fPrime = f4.composeWith(f).composeWith(f5).joinWith(summaryFunction);
								if(!fPrime.equalTo(summaryFunction)) {
									summaryFunctions.insertFunction(c,d4,retSiteC,d5,fPrime);
								}
							}
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)); {
									D d3 = valAndFunc.getKey();
									propagate(d3, retSiteC, d5, f3.composeWith(fPrime));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Lines 33-37 of the algorithm.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;
			Set<D> res = flowFunction.computeTargets(d2);
			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));
				propagate(d1, m, d3, fprime); 
			}
		}
	}
	
	private void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f) {
		EdgeFunction<V> jumpFnE;
		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);
		}
		if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
		EdgeFunction<V> fPrime = jumpFnE.joinWith(f);
		if(!fPrime.equalTo(jumpFnE)) {
			synchronized (jumpFn) {
				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}
			
			PathEdge<N,D,M> edge = new PathEdge<N,D,M>(sourceVal, target, targetVal);
			synchronized (pathWorklist) {
				pathWorklist.add(edge);
			}

			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();
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
		summaries.put(eP,d2,f);
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
	 * Returns the V-type result for the given value at the given statement. 
	 */
	public V resultAt(N stmt, D value) {
		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.
	 * The artificial zero value is automatically stripped.
	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value
		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}

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
		private final PathEdge<N, D, M> edge;

		public PathEdgeProcessingTask(PathEdge<N, D, M> edge) {
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
			synchronized (pathWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				pathWorklist.notify();
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
			if(icfg.isStartPoint(n)) {
				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
			synchronized (nodeWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				nodeWorklist.notify();
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










f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-generic


de


bodden


ide


solver


IDESolver.java



Find file
Normal viewHistoryPermalink




f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-generic


de


bodden


ide


solver


IDESolver.java





f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag








f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag





f7c0f2f622af3688a85e5cff335f57d270512b65

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src-generic

de

bodden

ide

solver

IDESolver.java
Find file
Normal viewHistoryPermalink




IDESolver.java



24.6 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




18




19




20




21




22




23




24




25




26






import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.EdgeFunction;
import de.bodden.ide.EdgeFunctionCache;
import de.bodden.ide.EdgeFunctions;
import de.bodden.ide.FlowFunction;
import de.bodden.ide.FlowFunctionCache;
import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IDETabulationProblem;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.JoinLattice;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ZeroedFlowFunctions;
import de.bodden.ide.edgefunc.EdgeIdentity;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






51




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






52




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






53




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






54




55




56




57




58




59




60




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






61




	protected static final boolean DEBUG = false;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






62




63




64




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






65




	protected ExecutorService executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






66




67




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






68




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




	
	//the number of currently running tasks









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






71




	protected final AtomicInteger numTasks = new AtomicInteger();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






72




73




74




75





	@SynchronizedBy("consistent lock on field")
	//We are using a LinkedHashSet here to enforce FIFO semantics, which leads to a breath-first construction
	//of the exploded super graph. As we observed in experiments, this can speed up the construction.









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






76




	protected final Collection<PathEdge<N,D,M>> pathWorklist = new LinkedHashSet<PathEdge<N,D,M>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






77




78




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






79




	protected final JumpFunctions<N,D,V> jumpFn;









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




	protected final SummaryFunctions<N,D,V> summaryFunctions = new SummaryFunctions<N,D,V>();









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









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






104




	protected final Set<N> initialSeeds;









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




112




	
	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






113




	protected final List<Pair<N,D>> nodeWorklist = new LinkedList<Pair<N,D>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






114




115





	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






116




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






134




	protected final D zeroValue;









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




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






138




139





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






140




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




234




235




236




237




238




239




240




241




242




243




244




245




246




247




248




249




250




251




252




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




278




279




280




281




282




283




284




285




286




287




288




289




290




291




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




307




308




309




310




311




312




313




314




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




371




372




373




374




375





	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);
	}
	
	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {
		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();
		FlowFunctions<N, D, M> flowFunctions = new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue());
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
	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * Uses a number of threads equal to the return value of
	 * <code>Runtime.getRuntime().availableProcessors()</code>.
	 */
	public void solve() {
		solve(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * @param numThreads The number of threads to use.
	 */
	public void solve(int numThreads) {
		if(numThreads<2) {
			this.executor = Executors.newSingleThreadExecutor();
			this.numThreads = 1;
		} else {
			this.executor = Executors.newFixedThreadPool(numThreads);
			this.numThreads = numThreads;
		}
		
		for(N startPoint: initialSeeds) {
			propagate(zeroValue, startPoint, zeroValue, allTop);
			pathWorklist.add(new PathEdge<N,D,M>(zeroValue, startPoint, zeroValue));
			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
		{
			final long before = System.currentTimeMillis();
			forwardComputeJumpFunctionsSLRPs();		
			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}
		{
			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		
		executor.shutdown();
	}

	/**
	 * Forward-tabulates the same-level realizable paths and associated functions.
	 * Note that this is a little different from the original IFDS formulations because
	 * we can have statements that are, for instance, both "normal" and "exit" statements.
	 * This is for instance the case on a "throw" statement that may on the one hand
	 * lead to a catch block but on the other hand exit the method depending
	 * on the exception being thrown.
	 */
	private void forwardComputeJumpFunctionsSLRPs() {
		while(true) {
			
			synchronized (pathWorklist) {
				if(!pathWorklist.isEmpty()) {
					//pop edge
					Iterator<PathEdge<N,D,M>> iter = pathWorklist.iterator();
					PathEdge<N,D,M> edge = iter.next();
					iter.remove();
					numTasks.getAndIncrement();

					//dispatch processing of edge (potentially in a different thread)
					executor.execute(new PathEdgeProcessingTask(edge));
					propagationCount++;
				} else if(numTasks.intValue()==0){
					//path worklist is empty; no running tasks, we are done
					return;
				} else {
					//the path worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						pathWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)
		for(N startPoint: initialSeeds) {
			setVal(startPoint, zeroValue, valueLattice.bottomElement());
			Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, zeroValue); 
			nodeWorklist.add(superGraphNode);
		}
		while(true) {
			synchronized (nodeWorklist) {
				if(!nodeWorklist.isEmpty()) {
					//pop job
					Pair<N,D> nAndD = nodeWorklist.remove(0);	
					numTasks.getAndIncrement();
					
					//dispatch processing of job (potentially in a different thread)
					executor.execute(new ValuePropagationTask(nAndD));
				} else if(numTasks.intValue()==0) {
					//node worklist is empty; no running tasks, we are done
					break;
				} else {
					//the node worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						nodeWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;
		}		
		for(int t=0;t<numThreads; t++) {
			executor.execute(new ValueComputationTask(nonCallStartNodesArray, t));
		}
		//wait until done
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
				synchronized (nodeWorklist) {
					nodeWorklist.add(new Pair<N,D>(nHashN,nHashD));
				}
			}
		}
	}

	private V val(N nHashN, D nHashD){ 
		V l = val.get(nHashN, nHashD);
		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	
	private void setVal(N nHashN, D nHashD,V l){ 
		val.put(nHashN, nHashD,l);
		if(DEBUG)









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






376




			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






377




378




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




391




392




393




394




395




396




397




398




399




400




401




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




432




433




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




458




459




460




461




462




463




464




465




466




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




527




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




579




580




581




582




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




604




605




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

	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();
		
		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);
			for(N sP: icfg.getStartPointsOf(sCalledProcN)) {			
				for(D d3: res) {
					propagate(d3, sP, d3, EdgeIdentity.<V>v()); //line 15
	
					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads
						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));						
					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez
					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();
						for(N retSiteN: icfg.getReturnSitesOfCallAt(n)) {
							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;
							for(D d5: retFunction.computeTargets(d4)) {
								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);
								synchronized (summaryFunctions) {
									EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(n, d2, retSiteN).get(d5);			
									if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
									EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5).joinWith(summaryFunction);
									if(!fPrime.equalTo(summaryFunction)) {
										summaryFunctions.insertFunction(n,d2,retSiteN,d5,fPrime);
									}	
								}
							}
						}
					}
				}		
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez
		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;
			for(D d3: callToReturnFlowFunction.computeTargets(d2)) {
				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);
				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE));
			}

			Map<D,EdgeFunction<V>> d3sAndF3s = summaryFunctions.summariesFor(n, d2, returnSiteN);
			for (Map.Entry<D,EdgeFunction<V>> d3AndF3 : d3sAndF3s.entrySet()) {
				D d3 = d3AndF3.getKey();
				EdgeFunction<V> f3 = d3AndF3.getValue();
				if(f3==null) f3 = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
				propagate(d1, returnSiteN, d3, f.composeWith(f3));
			}
		}
	}

	private EdgeFunction<V> jumpFunction(PathEdge<N, D, M> edge) {
		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}

	/**
	 * Lines 21-32 of the algorithm.	
	 */
	private void processExit(PathEdge<N,D,M> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		for(N sP: icfg.getStartPointsOf(methodThatNeedsSummary)) {
			//line 21.1 of Naeem/Lhotak/Rodriguez
			
			Set<Entry<N, Set<D>>> inc;
			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads
				inc = new HashSet<Map.Entry<N,Set<D>>>(incoming(d1, sP));
			}
			
			for (Entry<N,Set<D>> entry: inc) {
				//line 22
				N c = entry.getKey();
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
					flowFunctionConstructionCount++;
					Set<D> targets = retFunction.computeTargets(d2);
					for(D d4: entry.getValue()) {
						//line 23
						for(D d5: targets) {
							EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
							EdgeFunction<V> fPrime;
							synchronized (summaryFunctions) {
								EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(c,d4,retSiteC).get(d5);			
								if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
								fPrime = f4.composeWith(f).composeWith(f5).joinWith(summaryFunction);
								if(!fPrime.equalTo(summaryFunction)) {
									summaryFunctions.insertFunction(c,d4,retSiteC,d5,fPrime);
								}
							}
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)); {
									D d3 = valAndFunc.getKey();
									propagate(d3, retSiteC, d5, f3.composeWith(fPrime));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Lines 33-37 of the algorithm.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;
			Set<D> res = flowFunction.computeTargets(d2);
			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));
				propagate(d1, m, d3, fprime); 
			}
		}
	}
	
	private void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f) {
		EdgeFunction<V> jumpFnE;
		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);
		}
		if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
		EdgeFunction<V> fPrime = jumpFnE.joinWith(f);
		if(!fPrime.equalTo(jumpFnE)) {
			synchronized (jumpFn) {
				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}
			
			PathEdge<N,D,M> edge = new PathEdge<N,D,M>(sourceVal, target, targetVal);
			synchronized (pathWorklist) {
				pathWorklist.add(edge);
			}

			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();
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
		summaries.put(eP,d2,f);
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
	 * Returns the V-type result for the given value at the given statement. 
	 */
	public V resultAt(N stmt, D value) {
		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.
	 * The artificial zero value is automatically stripped.
	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value
		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}

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
		private final PathEdge<N, D, M> edge;

		public PathEdgeProcessingTask(PathEdge<N, D, M> edge) {
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
			synchronized (pathWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				pathWorklist.notify();
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
			if(icfg.isStartPoint(n)) {
				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
			synchronized (nodeWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				nodeWorklist.notify();
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



24.6 KB










IDESolver.java



24.6 KB









Newer










Older
NewerOlder







renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




18




19




20




21




22




23




24




25




26






import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.EdgeFunction;
import de.bodden.ide.EdgeFunctionCache;
import de.bodden.ide.EdgeFunctions;
import de.bodden.ide.FlowFunction;
import de.bodden.ide.FlowFunctionCache;
import de.bodden.ide.FlowFunctions;
import de.bodden.ide.IDETabulationProblem;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.JoinLattice;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ZeroedFlowFunctions;
import de.bodden.ide.edgefunc.EdgeIdentity;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






51




 * @param <N> The type of nodes in the interprocedural control-flow graph. 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






52




 * @param <D> The type of data-flow facts to be computed by the tabulation problem.









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






53




 * @param <M> The type of objects used to represent methods.









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






54




55




56




57




58




59




60




 * @param <V> The type of values to be computed along flow edges.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {
	
	public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
	









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






61




	protected static final boolean DEBUG = false;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






62




63




64




	
	//executor for dispatching individual compute jobs (may be multi-threaded)
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






65




	protected ExecutorService executor;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






66




67




	
	@DontSynchronize("only used by single thread")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






68




	protected int numThreads;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






69




70




	
	//the number of currently running tasks









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






71




	protected final AtomicInteger numTasks = new AtomicInteger();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






72




73




74




75





	@SynchronizedBy("consistent lock on field")
	//We are using a LinkedHashSet here to enforce FIFO semantics, which leads to a breath-first construction
	//of the exploded super graph. As we observed in experiments, this can speed up the construction.









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






76




	protected final Collection<PathEdge<N,D,M>> pathWorklist = new LinkedHashSet<PathEdge<N,D,M>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






77




78




	
	@SynchronizedBy("thread safe data structure, consistent locking when used")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






79




	protected final JumpFunctions<N,D,V> jumpFn;









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




	protected final SummaryFunctions<N,D,V> summaryFunctions = new SummaryFunctions<N,D,V>();









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









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






104




	protected final Set<N> initialSeeds;









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




112




	
	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






113




	protected final List<Pair<N,D>> nodeWorklist = new LinkedList<Pair<N,D>>();









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






114




115





	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






116




	protected final Table<N,D,V> val = HashBasedTable.create();	









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






134




	protected final D zeroValue;









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




	protected final FlowFunctionCache<N,D,M> ffCache; 









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






138




139





	@DontSynchronize("readOnly")









added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012






140




	protected final EdgeFunctionCache<N,D,M,V> efCache;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




234




235




236




237




238




239




240




241




242




243




244




245




246




247




248




249




250




251




252




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




278




279




280




281




282




283




284




285




286




287




288




289




290




291




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




307




308




309




310




311




312




313




314




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




371




372




373




374




375





	/**
	 * Creates a solver for the given problem, which caches flow functions and edge functions.
	 * The solver must then be started by calling {@link #solve()}.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {
		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);
	}
	
	/**
	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling
	 * {@link #solve()}.
	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.
	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.
	 */
	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {
		if(DEBUG) {
			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
		}
		this.zeroValue = tabulationProblem.zeroValue();
		this.icfg = tabulationProblem.interproceduralCFG();
		FlowFunctions<N, D, M> flowFunctions = new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue());
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
	}

	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * Uses a number of threads equal to the return value of
	 * <code>Runtime.getRuntime().availableProcessors()</code>.
	 */
	public void solve() {
		solve(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Runs the solver on the configured problem. This can take some time.
	 * @param numThreads The number of threads to use.
	 */
	public void solve(int numThreads) {
		if(numThreads<2) {
			this.executor = Executors.newSingleThreadExecutor();
			this.numThreads = 1;
		} else {
			this.executor = Executors.newFixedThreadPool(numThreads);
			this.numThreads = numThreads;
		}
		
		for(N startPoint: initialSeeds) {
			propagate(zeroValue, startPoint, zeroValue, allTop);
			pathWorklist.add(new PathEdge<N,D,M>(zeroValue, startPoint, zeroValue));
			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());
		}
		{
			final long before = System.currentTimeMillis();
			forwardComputeJumpFunctionsSLRPs();		
			durationFlowFunctionConstruction = System.currentTimeMillis() - before;
		}
		{
			final long before = System.currentTimeMillis();
			computeValues();
			durationFlowFunctionApplication = System.currentTimeMillis() - before;
		}
		if(DEBUG) 
			printStats();
		
		executor.shutdown();
	}

	/**
	 * Forward-tabulates the same-level realizable paths and associated functions.
	 * Note that this is a little different from the original IFDS formulations because
	 * we can have statements that are, for instance, both "normal" and "exit" statements.
	 * This is for instance the case on a "throw" statement that may on the one hand
	 * lead to a catch block but on the other hand exit the method depending
	 * on the exception being thrown.
	 */
	private void forwardComputeJumpFunctionsSLRPs() {
		while(true) {
			
			synchronized (pathWorklist) {
				if(!pathWorklist.isEmpty()) {
					//pop edge
					Iterator<PathEdge<N,D,M>> iter = pathWorklist.iterator();
					PathEdge<N,D,M> edge = iter.next();
					iter.remove();
					numTasks.getAndIncrement();

					//dispatch processing of edge (potentially in a different thread)
					executor.execute(new PathEdgeProcessingTask(edge));
					propagationCount++;
				} else if(numTasks.intValue()==0){
					//path worklist is empty; no running tasks, we are done
					return;
				} else {
					//the path worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						pathWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	/**
	 * Computes the final values for edge functions.
	 */
	private void computeValues() {	
		//Phase II(i)
		for(N startPoint: initialSeeds) {
			setVal(startPoint, zeroValue, valueLattice.bottomElement());
			Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, zeroValue); 
			nodeWorklist.add(superGraphNode);
		}
		while(true) {
			synchronized (nodeWorklist) {
				if(!nodeWorklist.isEmpty()) {
					//pop job
					Pair<N,D> nAndD = nodeWorklist.remove(0);	
					numTasks.getAndIncrement();
					
					//dispatch processing of job (potentially in a different thread)
					executor.execute(new ValuePropagationTask(nAndD));
				} else if(numTasks.intValue()==0) {
					//node worklist is empty; no running tasks, we are done
					break;
				} else {
					//the node worklist is empty but we still have running tasks
					//wait until woken up, then try again
					try {
						nodeWorklist.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		//Phase II(ii)
		//we create an array of all nodes and then dispatch fractions of this array to multiple threads
		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();
		@SuppressWarnings("unchecked")
		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];
		int i=0;
		for (N n : allNonCallStartNodes) {
			nonCallStartNodesArray[i] = n;
			i++;
		}		
		for(int t=0;t<numThreads; t++) {
			executor.execute(new ValueComputationTask(nonCallStartNodesArray, t));
		}
		//wait until done
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
				synchronized (nodeWorklist) {
					nodeWorklist.add(new Pair<N,D>(nHashN,nHashD));
				}
			}
		}
	}

	private V val(N nHashN, D nHashD){ 
		V l = val.get(nHashN, nHashD);
		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper
		else return l;
	}
	
	private void setVal(N nHashN, D nHashD,V l){ 
		val.put(nHashN, nHashD,l);
		if(DEBUG)









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






376




			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






377




378




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




391




392




393




394




395




396




397




398




399




400




401




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




432




433




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




458




459




460




461




462




463




464




465




466




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




527




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




579




580




581




582




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




604




605




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

	
	/**
	 * Lines 13-20 of the algorithm; processing a call site in the caller's context
	 * @param edge an edge whose target node resembles a method call
	 */
	private void processCall(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); // a call node; line 14...
		final D d2 = edge.factAtTarget();
		
		Set<M> callees = icfg.getCalleesOfCallAt(n);
		for(M sCalledProcN: callees) { //still line 14
			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);
			flowFunctionConstructionCount++;
			Set<D> res = function.computeTargets(d2);
			for(N sP: icfg.getStartPointsOf(sCalledProcN)) {			
				for(D d3: res) {
					propagate(d3, sP, d3, EdgeIdentity.<V>v()); //line 15
	
					Set<Cell<N, D, EdgeFunction<V>>> endSumm;
					synchronized (incoming) {
						//line 15.1 of Naeem/Lhotak/Rodriguez
						addIncoming(sP,d3,n,d2);
						//line 15.2, copy to avoid concurrent modification exceptions by other threads
						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));						
					}
					
					//still line 15.2 of Naeem/Lhotak/Rodriguez
					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {
						N eP = entry.getRowKey();
						D d4 = entry.getColumnKey();
						EdgeFunction<V> fCalleeSummary = entry.getValue();
						for(N retSiteN: icfg.getReturnSitesOfCallAt(n)) {
							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
							flowFunctionConstructionCount++;
							for(D d5: retFunction.computeTargets(d4)) {
								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);
								synchronized (summaryFunctions) {
									EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(n, d2, retSiteN).get(d5);			
									if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
									EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5).joinWith(summaryFunction);
									if(!fPrime.equalTo(summaryFunction)) {
										summaryFunctions.insertFunction(n,d2,retSiteN,d5,fPrime);
									}	
								}
							}
						}
					}
				}		
			}
		}
		//line 17-19 of Naeem/Lhotak/Rodriguez
		EdgeFunction<V> f = jumpFunction(edge);
		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);
		for (N returnSiteN : returnSiteNs) {
			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
			flowFunctionConstructionCount++;
			for(D d3: callToReturnFlowFunction.computeTargets(d2)) {
				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);
				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE));
			}

			Map<D,EdgeFunction<V>> d3sAndF3s = summaryFunctions.summariesFor(n, d2, returnSiteN);
			for (Map.Entry<D,EdgeFunction<V>> d3AndF3 : d3sAndF3s.entrySet()) {
				D d3 = d3AndF3.getKey();
				EdgeFunction<V> f3 = d3AndF3.getValue();
				if(f3==null) f3 = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
				propagate(d1, returnSiteN, d3, f.composeWith(f3));
			}
		}
	}

	private EdgeFunction<V> jumpFunction(PathEdge<N, D, M> edge) {
		synchronized (jumpFn) {
			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paper
			return function;
		}
	}

	/**
	 * Lines 21-32 of the algorithm.	
	 */
	private void processExit(PathEdge<N,D,M> edge) {
		final N n = edge.getTarget(); // an exit node; line 21...
		EdgeFunction<V> f = jumpFunction(edge);
		M methodThatNeedsSummary = icfg.getMethodOf(n);
		
		final D d1 = edge.factAtSource();
		final D d2 = edge.factAtTarget();
		
		for(N sP: icfg.getStartPointsOf(methodThatNeedsSummary)) {
			//line 21.1 of Naeem/Lhotak/Rodriguez
			
			Set<Entry<N, Set<D>>> inc;
			synchronized (incoming) {
				addEndSummary(sP, d1, n, d2, f);
				//copy to avoid concurrent modification exceptions by other threads
				inc = new HashSet<Map.Entry<N,Set<D>>>(incoming(d1, sP));
			}
			
			for (Entry<N,Set<D>> entry: inc) {
				//line 22
				N c = entry.getKey();
				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {
					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);
					flowFunctionConstructionCount++;
					Set<D> targets = retFunction.computeTargets(d2);
					for(D d4: entry.getValue()) {
						//line 23
						for(D d5: targets) {
							EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);
							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);
							EdgeFunction<V> fPrime;
							synchronized (summaryFunctions) {
								EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(c,d4,retSiteC).get(d5);			
								if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paper
								fPrime = f4.composeWith(f).composeWith(f5).joinWith(summaryFunction);
								if(!fPrime.equalTo(summaryFunction)) {
									summaryFunctions.insertFunction(c,d4,retSiteC,d5,fPrime);
								}
							}
							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {
								EdgeFunction<V> f3 = valAndFunc.getValue();
								if(!f3.equalTo(allTop)); {
									D d3 = valAndFunc.getKey();
									propagate(d3, retSiteC, d5, f3.composeWith(fPrime));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Lines 33-37 of the algorithm.
	 * @param edge
	 */
	private void processNormalFlow(PathEdge<N,D,M> edge) {
		final D d1 = edge.factAtSource();
		final N n = edge.getTarget(); 
		final D d2 = edge.factAtTarget();
		EdgeFunction<V> f = jumpFunction(edge);
		for (N m : icfg.getSuccsOf(n)) {
			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);
			flowFunctionConstructionCount++;
			Set<D> res = flowFunction.computeTargets(d2);
			for (D d3 : res) {
				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));
				propagate(d1, m, d3, fprime); 
			}
		}
	}
	
	private void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f) {
		EdgeFunction<V> jumpFnE;
		synchronized (jumpFn) {
			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);
		}
		if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)
		EdgeFunction<V> fPrime = jumpFnE.joinWith(f);
		if(!fPrime.equalTo(jumpFnE)) {
			synchronized (jumpFn) {
				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
			}
			
			PathEdge<N,D,M> edge = new PathEdge<N,D,M>(sourceVal, target, targetVal);
			synchronized (pathWorklist) {
				pathWorklist.add(edge);
			}

			if(DEBUG) {
				if(targetVal!=zeroValue) {			
					StringBuilder result = new StringBuilder();
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
		summaries.put(eP,d2,f);
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
	 * Returns the V-type result for the given value at the given statement. 
	 */
	public V resultAt(N stmt, D value) {
		return val.get(stmt, value);
	}
	
	/**
	 * Returns the resulting environment for the given statement.
	 * The artificial zero value is automatically stripped.
	 */
	public Map<D,V> resultsAt(N stmt) {
		//filter out the artificial zero-value
		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {

			public boolean apply(D val) {
				return val!=zeroValue;
			}
		});
	}

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
		private final PathEdge<N, D, M> edge;

		public PathEdgeProcessingTask(PathEdge<N, D, M> edge) {
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
			synchronized (pathWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				pathWorklist.notify();
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
			if(icfg.isStartPoint(n)) {
				propagateValueAtStart(nAndD, n);
			}
			if(icfg.isCallStmt(n)) {
				propagateValueAtCall(nAndD, n);
			}
			synchronized (nodeWorklist) {
				numTasks.getAndDecrement();
				//potentially wake up waiting broker thread
				//(see forwardComputeJumpFunctionsSLRPs())
				nodeWorklist.notify();
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







renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

1
package de.bodden.ide.solver;packagede.bodden.ide.solver;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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

18

19

20

21

22

23

24

25

26
import java.util.Collection;importjava.util.Collection;import java.util.Collections;importjava.util.Collections;import java.util.HashMap;importjava.util.HashMap;import java.util.HashSet;importjava.util.HashSet;import java.util.Iterator;importjava.util.Iterator;import java.util.LinkedHashSet;importjava.util.LinkedHashSet;import java.util.LinkedList;importjava.util.LinkedList;import java.util.List;importjava.util.List;import java.util.Map;importjava.util.Map;import java.util.Map.Entry;importjava.util.Map.Entry;import java.util.Set;importjava.util.Set;import java.util.concurrent.ExecutorService;importjava.util.concurrent.ExecutorService;import java.util.concurrent.Executors;importjava.util.concurrent.Executors;import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;import java.util.concurrent.atomic.AtomicInteger;importjava.util.concurrent.atomic.AtomicInteger;import com.google.common.base.Predicate;importcom.google.common.base.Predicate;import com.google.common.cache.CacheBuilder;importcom.google.common.cache.CacheBuilder;import com.google.common.collect.HashBasedTable;importcom.google.common.collect.HashBasedTable;import com.google.common.collect.Maps;importcom.google.common.collect.Maps;import com.google.common.collect.Table;importcom.google.common.collect.Table;import com.google.common.collect.Table.Cell;importcom.google.common.collect.Table.Cell;



renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

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
import de.bodden.ide.DontSynchronize;importde.bodden.ide.DontSynchronize;import de.bodden.ide.EdgeFunction;importde.bodden.ide.EdgeFunction;import de.bodden.ide.EdgeFunctionCache;importde.bodden.ide.EdgeFunctionCache;import de.bodden.ide.EdgeFunctions;importde.bodden.ide.EdgeFunctions;import de.bodden.ide.FlowFunction;importde.bodden.ide.FlowFunction;import de.bodden.ide.FlowFunctionCache;importde.bodden.ide.FlowFunctionCache;import de.bodden.ide.FlowFunctions;importde.bodden.ide.FlowFunctions;import de.bodden.ide.IDETabulationProblem;importde.bodden.ide.IDETabulationProblem;import de.bodden.ide.InterproceduralCFG;importde.bodden.ide.InterproceduralCFG;import de.bodden.ide.JoinLattice;importde.bodden.ide.JoinLattice;import de.bodden.ide.SynchronizedBy;importde.bodden.ide.SynchronizedBy;import de.bodden.ide.ZeroedFlowFunctions;importde.bodden.ide.ZeroedFlowFunctions;import de.bodden.ide.edgefunc.EdgeIdentity;importde.bodden.ide.edgefunc.EdgeIdentity;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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

51
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

52
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

53
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

54

55

56

57

58

59

60
 * @param <V> The type of values to be computed along flow edges. * @param <V> The type of values to be computed along flow edges. * @param <I> The type of inter-procedural control-flow graph being used. * @param <I> The type of inter-procedural control-flow graph being used. */ */public class IDESolver<N,D,M,V,I extends InterproceduralCFG<N, M>> {publicclassIDESolver<N,D,M,V,IextendsInterproceduralCFG<N,M>>{		public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();publicstaticCacheBuilder<Object,Object>DEFAULT_CACHE_BUILDER=CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();	



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

61
	protected static final boolean DEBUG = false;protectedstaticfinalbooleanDEBUG=false;



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

63

64
		//executor for dispatching individual compute jobs (may be multi-threaded)//executor for dispatching individual compute jobs (may be multi-threaded)	@DontSynchronize("only used by single thread")@DontSynchronize("only used by single thread")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

65
	protected ExecutorService executor;protectedExecutorServiceexecutor;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

66

67
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

68
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

69

70
		//the number of currently running tasks//the number of currently running tasks



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

71
	protected final AtomicInteger numTasks = new AtomicInteger();protectedfinalAtomicIntegernumTasks=newAtomicInteger();



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

72

73

74

75
	@SynchronizedBy("consistent lock on field")@SynchronizedBy("consistent lock on field")	//We are using a LinkedHashSet here to enforce FIFO semantics, which leads to a breath-first construction//We are using a LinkedHashSet here to enforce FIFO semantics, which leads to a breath-first construction	//of the exploded super graph. As we observed in experiments, this can speed up the construction.//of the exploded super graph. As we observed in experiments, this can speed up the construction.



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

76
	protected final Collection<PathEdge<N,D,M>> pathWorklist = new LinkedHashSet<PathEdge<N,D,M>>();protectedfinalCollection<PathEdge<N,D,M>>pathWorklist=newLinkedHashSet<PathEdge<N,D,M>>();



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

79
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

80

81
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

82
	protected final SummaryFunctions<N,D,V> summaryFunctions = new SummaryFunctions<N,D,V>();protectedfinalSummaryFunctions<N,D,V>summaryFunctions=newSummaryFunctions<N,D,V>();



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

85
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

86

87

88

89
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

90
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

91

92

93

94
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

95
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

96

97
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

98
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

99

100
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

101
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

102

103
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

104
	protected final Set<N> initialSeeds;protectedfinalSet<N>initialSeeds;



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

107
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

108

109
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

110
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

111

112
		@DontSynchronize("only used by single thread - phase II not parallelized (yet)")@DontSynchronize("only used by single thread - phase II not parallelized (yet)")



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
	protected final List<Pair<N,D>> nodeWorklist = new LinkedList<Pair<N,D>>();protectedfinalList<Pair<N,D>>nodeWorklist=newLinkedList<Pair<N,D>>();



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
	@DontSynchronize("only used by single thread - phase II not parallelized (yet)")@DontSynchronize("only used by single thread - phase II not parallelized (yet)")



added dumping code again for Soot/Jimple versions


 

 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions


 

 

added dumping code again for Soot/Jimple versions

 

Eric Bodden
committed
Nov 29, 2012

116
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

134
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

135

136
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

137
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

138

139
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

140
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

234

235

236

237

238

239

240

241

242

243

244

245

246

247

248

249

250

251

252

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

278

279

280

281

282

283

284

285

286

287

288

289

290

291

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

307

308

309

310

311

312

313

314

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

371

372

373

374

375
	/**/**	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * Creates a solver for the given problem, which caches flow functions and edge functions.	 * The solver must then be started by calling {@link #solve()}.	 * The solver must then be started by calling {@link #solve()}.	 */	 */	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem) {publicIDESolver(IDETabulationProblem<N,D,M,V,I>tabulationProblem){		this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);this(tabulationProblem,DEFAULT_CACHE_BUILDER,DEFAULT_CACHE_BUILDER);	}}		/**/**	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * Creates a solver for the given problem, constructing caches with the given {@link CacheBuilder}. The solver must then be started by calling	 * {@link #solve()}.	 * {@link #solve()}.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param flowFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for flow functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 * @param edgeFunctionCacheBuilder A valid {@link CacheBuilder} or <code>null</code> if no caching is to be used for edge functions.	 */	 */	public IDESolver(IDETabulationProblem<N,D,M,V,I> tabulationProblem, @SuppressWarnings("rawtypes") CacheBuilder flowFunctionCacheBuilder, @SuppressWarnings("rawtypes") CacheBuilder edgeFunctionCacheBuilder) {publicIDESolver(IDETabulationProblem<N,D,M,V,I>tabulationProblem,@SuppressWarnings("rawtypes")CacheBuilderflowFunctionCacheBuilder,@SuppressWarnings("rawtypes")CacheBuilderedgeFunctionCacheBuilder){		if(DEBUG) {if(DEBUG){			flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();flowFunctionCacheBuilder=flowFunctionCacheBuilder.recordStats();			edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();edgeFunctionCacheBuilder=edgeFunctionCacheBuilder.recordStats();		}}		this.zeroValue = tabulationProblem.zeroValue();this.zeroValue=tabulationProblem.zeroValue();		this.icfg = tabulationProblem.interproceduralCFG();this.icfg=tabulationProblem.interproceduralCFG();		FlowFunctions<N, D, M> flowFunctions = new ZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue());FlowFunctions<N,D,M>flowFunctions=newZeroedFlowFunctions<N,D,M>(tabulationProblem.flowFunctions(),tabulationProblem.zeroValue());		EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();EdgeFunctions<N,D,M,V>edgeFunctions=tabulationProblem.edgeFunctions();		if(flowFunctionCacheBuilder!=null) {if(flowFunctionCacheBuilder!=null){			ffCache = new FlowFunctionCache<N,D,M>(flowFunctions, flowFunctionCacheBuilder);ffCache=newFlowFunctionCache<N,D,M>(flowFunctions,flowFunctionCacheBuilder);			flowFunctions = ffCache;flowFunctions=ffCache;		} else {}else{			ffCache = null;ffCache=null;		}}		if(edgeFunctionCacheBuilder!=null) {if(edgeFunctionCacheBuilder!=null){			efCache = new EdgeFunctionCache<N,D,M,V>(edgeFunctions, edgeFunctionCacheBuilder);efCache=newEdgeFunctionCache<N,D,M,V>(edgeFunctions,edgeFunctionCacheBuilder);			edgeFunctions = efCache;edgeFunctions=efCache;		} else {}else{			efCache = null;efCache=null;		}}		this.flowFunctions = flowFunctions;this.flowFunctions=flowFunctions;		this.edgeFunctions = edgeFunctions;this.edgeFunctions=edgeFunctions;		this.initialSeeds = tabulationProblem.initialSeeds();this.initialSeeds=tabulationProblem.initialSeeds();		this.valueLattice = tabulationProblem.joinLattice();this.valueLattice=tabulationProblem.joinLattice();		this.allTop = tabulationProblem.allTopFunction();this.allTop=tabulationProblem.allTopFunction();		this.jumpFn = new JumpFunctions<N,D,V>(allTop);this.jumpFn=newJumpFunctions<N,D,V>(allTop);	}}	/**/**	 * Runs the solver on the configured problem. This can take some time.	 * Runs the solver on the configured problem. This can take some time.	 * Uses a number of threads equal to the return value of	 * Uses a number of threads equal to the return value of	 * <code>Runtime.getRuntime().availableProcessors()</code>.	 * <code>Runtime.getRuntime().availableProcessors()</code>.	 */	 */	public void solve() {publicvoidsolve(){		solve(Runtime.getRuntime().availableProcessors());solve(Runtime.getRuntime().availableProcessors());	}}		/**/**	 * Runs the solver on the configured problem. This can take some time.	 * Runs the solver on the configured problem. This can take some time.	 * @param numThreads The number of threads to use.	 * @param numThreads The number of threads to use.	 */	 */	public void solve(int numThreads) {publicvoidsolve(intnumThreads){		if(numThreads<2) {if(numThreads<2){			this.executor = Executors.newSingleThreadExecutor();this.executor=Executors.newSingleThreadExecutor();			this.numThreads = 1;this.numThreads=1;		} else {}else{			this.executor = Executors.newFixedThreadPool(numThreads);this.executor=Executors.newFixedThreadPool(numThreads);			this.numThreads = numThreads;this.numThreads=numThreads;		}}				for(N startPoint: initialSeeds) {for(NstartPoint:initialSeeds){			propagate(zeroValue, startPoint, zeroValue, allTop);propagate(zeroValue,startPoint,zeroValue,allTop);			pathWorklist.add(new PathEdge<N,D,M>(zeroValue, startPoint, zeroValue));pathWorklist.add(newPathEdge<N,D,M>(zeroValue,startPoint,zeroValue));			jumpFn.addFunction(zeroValue, startPoint, zeroValue, EdgeIdentity.<V>v());jumpFn.addFunction(zeroValue,startPoint,zeroValue,EdgeIdentity.<V>v());		}}		{{			final long before = System.currentTimeMillis();finallongbefore=System.currentTimeMillis();			forwardComputeJumpFunctionsSLRPs();		forwardComputeJumpFunctionsSLRPs();			durationFlowFunctionConstruction = System.currentTimeMillis() - before;durationFlowFunctionConstruction=System.currentTimeMillis()-before;		}}		{{			final long before = System.currentTimeMillis();finallongbefore=System.currentTimeMillis();			computeValues();computeValues();			durationFlowFunctionApplication = System.currentTimeMillis() - before;durationFlowFunctionApplication=System.currentTimeMillis()-before;		}}		if(DEBUG) if(DEBUG)			printStats();printStats();				executor.shutdown();executor.shutdown();	}}	/**/**	 * Forward-tabulates the same-level realizable paths and associated functions.	 * Forward-tabulates the same-level realizable paths and associated functions.	 * Note that this is a little different from the original IFDS formulations because	 * Note that this is a little different from the original IFDS formulations because	 * we can have statements that are, for instance, both "normal" and "exit" statements.	 * we can have statements that are, for instance, both "normal" and "exit" statements.	 * This is for instance the case on a "throw" statement that may on the one hand	 * This is for instance the case on a "throw" statement that may on the one hand	 * lead to a catch block but on the other hand exit the method depending	 * lead to a catch block but on the other hand exit the method depending	 * on the exception being thrown.	 * on the exception being thrown.	 */	 */	private void forwardComputeJumpFunctionsSLRPs() {privatevoidforwardComputeJumpFunctionsSLRPs(){		while(true) {while(true){						synchronized (pathWorklist) {synchronized(pathWorklist){				if(!pathWorklist.isEmpty()) {if(!pathWorklist.isEmpty()){					//pop edge//pop edge					Iterator<PathEdge<N,D,M>> iter = pathWorklist.iterator();Iterator<PathEdge<N,D,M>>iter=pathWorklist.iterator();					PathEdge<N,D,M> edge = iter.next();PathEdge<N,D,M>edge=iter.next();					iter.remove();iter.remove();					numTasks.getAndIncrement();numTasks.getAndIncrement();					//dispatch processing of edge (potentially in a different thread)//dispatch processing of edge (potentially in a different thread)					executor.execute(new PathEdgeProcessingTask(edge));executor.execute(newPathEdgeProcessingTask(edge));					propagationCount++;propagationCount++;				} else if(numTasks.intValue()==0){}elseif(numTasks.intValue()==0){					//path worklist is empty; no running tasks, we are done//path worklist is empty; no running tasks, we are done					return;return;				} else {}else{					//the path worklist is empty but we still have running tasks//the path worklist is empty but we still have running tasks					//wait until woken up, then try again//wait until woken up, then try again					try {try{						pathWorklist.wait();pathWorklist.wait();					} catch (InterruptedException e) {}catch(InterruptedExceptione){						throw new RuntimeException(e);thrownewRuntimeException(e);					}}				}}			}}		}}	}}		/**/**	 * Computes the final values for edge functions.	 * Computes the final values for edge functions.	 */	 */	private void computeValues() {	privatevoidcomputeValues(){		//Phase II(i)//Phase II(i)		for(N startPoint: initialSeeds) {for(NstartPoint:initialSeeds){			setVal(startPoint, zeroValue, valueLattice.bottomElement());setVal(startPoint,zeroValue,valueLattice.bottomElement());			Pair<N, D> superGraphNode = new Pair<N,D>(startPoint, zeroValue); Pair<N,D>superGraphNode=newPair<N,D>(startPoint,zeroValue);			nodeWorklist.add(superGraphNode);nodeWorklist.add(superGraphNode);		}}		while(true) {while(true){			synchronized (nodeWorklist) {synchronized(nodeWorklist){				if(!nodeWorklist.isEmpty()) {if(!nodeWorklist.isEmpty()){					//pop job//pop job					Pair<N,D> nAndD = nodeWorklist.remove(0);	Pair<N,D>nAndD=nodeWorklist.remove(0);					numTasks.getAndIncrement();numTasks.getAndIncrement();										//dispatch processing of job (potentially in a different thread)//dispatch processing of job (potentially in a different thread)					executor.execute(new ValuePropagationTask(nAndD));executor.execute(newValuePropagationTask(nAndD));				} else if(numTasks.intValue()==0) {}elseif(numTasks.intValue()==0){					//node worklist is empty; no running tasks, we are done//node worklist is empty; no running tasks, we are done					break;break;				} else {}else{					//the node worklist is empty but we still have running tasks//the node worklist is empty but we still have running tasks					//wait until woken up, then try again//wait until woken up, then try again					try {try{						nodeWorklist.wait();nodeWorklist.wait();					} catch (InterruptedException e) {}catch(InterruptedExceptione){						throw new RuntimeException(e);thrownewRuntimeException(e);					}}				}}			}}		}}		//Phase II(ii)//Phase II(ii)		//we create an array of all nodes and then dispatch fractions of this array to multiple threads//we create an array of all nodes and then dispatch fractions of this array to multiple threads		Set<N> allNonCallStartNodes = icfg.allNonCallStartNodes();Set<N>allNonCallStartNodes=icfg.allNonCallStartNodes();		@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")		N[] nonCallStartNodesArray = (N[]) new Object[allNonCallStartNodes.size()];N[]nonCallStartNodesArray=(N[])newObject[allNonCallStartNodes.size()];		int i=0;inti=0;		for (N n : allNonCallStartNodes) {for(Nn:allNonCallStartNodes){			nonCallStartNodesArray[i] = n;nonCallStartNodesArray[i]=n;			i++;i++;		}		}		for(int t=0;t<numThreads; t++) {for(intt=0;t<numThreads;t++){			executor.execute(new ValueComputationTask(nonCallStartNodesArray, t));executor.execute(newValueComputationTask(nonCallStartNodesArray,t));		}}		//wait until done//wait until done		executor.shutdown();executor.shutdown();		try {try{			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);executor.awaitTermination(Long.MAX_VALUE,TimeUnit.DAYS);		} catch (InterruptedException e) {}catch(InterruptedExceptione){			throw new RuntimeException(e);thrownewRuntimeException(e);		}}	}}	private void propagateValueAtStart(Pair<N, D> nAndD, N n) {privatevoidpropagateValueAtStart(Pair<N,D>nAndD,Nn){		D d = nAndD.getO2();		Dd=nAndD.getO2();		M p = icfg.getMethodOf(n);Mp=icfg.getMethodOf(n);		for(N c: icfg.getCallsFromWithin(p)) {					for(Nc:icfg.getCallsFromWithin(p)){			Set<Entry<D, EdgeFunction<V>>> entries; Set<Entry<D,EdgeFunction<V>>>entries;			synchronized (jumpFn) {synchronized(jumpFn){				entries = jumpFn.forwardLookup(d,c).entrySet();entries=jumpFn.forwardLookup(d,c).entrySet();				for(Map.Entry<D,EdgeFunction<V>> dPAndFP: entries) {for(Map.Entry<D,EdgeFunction<V>>dPAndFP:entries){					D dPrime = dPAndFP.getKey();DdPrime=dPAndFP.getKey();					EdgeFunction<V> fPrime = dPAndFP.getValue();EdgeFunction<V>fPrime=dPAndFP.getValue();					N sP = n;NsP=n;					propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));propagateValue(c,dPrime,fPrime.computeTarget(val(sP,d)));					flowFunctionApplicationCount++;flowFunctionApplicationCount++;				}}			}}		}}	}}		private void propagateValueAtCall(Pair<N, D> nAndD, N n) {privatevoidpropagateValueAtCall(Pair<N,D>nAndD,Nn){		D d = nAndD.getO2();Dd=nAndD.getO2();		for(M q: icfg.getCalleesOfCallAt(n)) {for(Mq:icfg.getCalleesOfCallAt(n)){			FlowFunction<D> callFlowFunction = flowFunctions.getCallFlowFunction(n, q);FlowFunction<D>callFlowFunction=flowFunctions.getCallFlowFunction(n,q);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;			for(D dPrime: callFlowFunction.computeTargets(d)) {for(DdPrime:callFlowFunction.computeTargets(d)){				EdgeFunction<V> edgeFn = edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);EdgeFunction<V>edgeFn=edgeFunctions.getCallEdgeFunction(n,d,q,dPrime);				for(N startPoint: icfg.getStartPointsOf(q)) {for(NstartPoint:icfg.getStartPointsOf(q)){					propagateValue(startPoint,dPrime, edgeFn.computeTarget(val(n,d)));propagateValue(startPoint,dPrime,edgeFn.computeTarget(val(n,d)));					flowFunctionApplicationCount++;flowFunctionApplicationCount++;				}}			}}		}}	}}		private void propagateValue(N nHashN, D nHashD, V v) {privatevoidpropagateValue(NnHashN,DnHashD,Vv){		synchronized (val) {synchronized(val){			V valNHash = val(nHashN, nHashD);VvalNHash=val(nHashN,nHashD);			V vPrime = valueLattice.join(valNHash,v);VvPrime=valueLattice.join(valNHash,v);			if(!vPrime.equals(valNHash)) {if(!vPrime.equals(valNHash)){				setVal(nHashN, nHashD, vPrime);setVal(nHashN,nHashD,vPrime);				synchronized (nodeWorklist) {synchronized(nodeWorklist){					nodeWorklist.add(new Pair<N,D>(nHashN,nHashD));nodeWorklist.add(newPair<N,D>(nHashN,nHashD));				}}			}}		}}	}}	private V val(N nHashN, D nHashD){ privateVval(NnHashN,DnHashD){		V l = val.get(nHashN, nHashD);Vl=val.get(nHashN,nHashD);		if(l==null) return valueLattice.topElement(); //implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paperif(l==null)returnvalueLattice.topElement();//implicitly initialized to top; see line [1] of Fig. 7 in SRH96 paper		else return l;elsereturnl;	}}		private void setVal(N nHashN, D nHashD,V l){ privatevoidsetVal(NnHashN,DnHashD,Vl){		val.put(nHashN, nHashD,l);val.put(nHashN,nHashD,l);		if(DEBUG)if(DEBUG)



moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012



moved dependencies on soot into separate package


 

 

moved dependencies on soot into separate package

 

Eric Bodden
committed
Nov 28, 2012

376
			System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+ " " + l);System.err.println("VALUE: "+icfg.getMethodOf(nHashN)+" "+nHashN+" "+nHashD+" "+l);



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

377

378

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

391

392

393

394

395

396

397

398

399

400

401

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

432

433

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

458

459

460

461

462

463

464

465

466

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

527

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

579

580

581

582

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

604

605

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
	}}		/**/**	 * Lines 13-20 of the algorithm; processing a call site in the caller's context	 * Lines 13-20 of the algorithm; processing a call site in the caller's context	 * @param edge an edge whose target node resembles a method call	 * @param edge an edge whose target node resembles a method call	 */	 */	private void processCall(PathEdge<N,D,M> edge) {privatevoidprocessCall(PathEdge<N,D,M>edge){		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); // a call node; line 14...finalNn=edge.getTarget();// a call node; line 14...		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();				Set<M> callees = icfg.getCalleesOfCallAt(n);Set<M>callees=icfg.getCalleesOfCallAt(n);		for(M sCalledProcN: callees) { //still line 14for(MsCalledProcN:callees){//still line 14			FlowFunction<D> function = flowFunctions.getCallFlowFunction(n, sCalledProcN);FlowFunction<D>function=flowFunctions.getCallFlowFunction(n,sCalledProcN);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;			Set<D> res = function.computeTargets(d2);Set<D>res=function.computeTargets(d2);			for(N sP: icfg.getStartPointsOf(sCalledProcN)) {			for(NsP:icfg.getStartPointsOf(sCalledProcN)){				for(D d3: res) {for(Dd3:res){					propagate(d3, sP, d3, EdgeIdentity.<V>v()); //line 15propagate(d3,sP,d3,EdgeIdentity.<V>v());//line 15						Set<Cell<N, D, EdgeFunction<V>>> endSumm;Set<Cell<N,D,EdgeFunction<V>>>endSumm;					synchronized (incoming) {synchronized(incoming){						//line 15.1 of Naeem/Lhotak/Rodriguez//line 15.1 of Naeem/Lhotak/Rodriguez						addIncoming(sP,d3,n,d2);addIncoming(sP,d3,n,d2);						//line 15.2, copy to avoid concurrent modification exceptions by other threads//line 15.2, copy to avoid concurrent modification exceptions by other threads						endSumm = new HashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP, d3));						endSumm=newHashSet<Table.Cell<N,D,EdgeFunction<V>>>(endSummary(sP,d3));					}}										//still line 15.2 of Naeem/Lhotak/Rodriguez//still line 15.2 of Naeem/Lhotak/Rodriguez					for(Cell<N, D, EdgeFunction<V>> entry: endSumm) {for(Cell<N,D,EdgeFunction<V>>entry:endSumm){						N eP = entry.getRowKey();NeP=entry.getRowKey();						D d4 = entry.getColumnKey();Dd4=entry.getColumnKey();						EdgeFunction<V> fCalleeSummary = entry.getValue();EdgeFunction<V>fCalleeSummary=entry.getValue();						for(N retSiteN: icfg.getReturnSitesOfCallAt(n)) {for(NretSiteN:icfg.getReturnSitesOfCallAt(n)){							FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(n,sCalledProcN,eP,retSiteN);							flowFunctionConstructionCount++;flowFunctionConstructionCount++;							for(D d5: retFunction.computeTargets(d4)) {for(Dd5:retFunction.computeTargets(d4)){								EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);EdgeFunction<V>f4=edgeFunctions.getCallEdgeFunction(n,d2,sCalledProcN,d3);								EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);EdgeFunction<V>f5=edgeFunctions.getReturnEdgeFunction(n,sCalledProcN,eP,d4,retSiteN,d5);								synchronized (summaryFunctions) {synchronized(summaryFunctions){									EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(n, d2, retSiteN).get(d5);			EdgeFunction<V>summaryFunction=summaryFunctions.summariesFor(n,d2,retSiteN).get(d5);									if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paperif(summaryFunction==null)summaryFunction=allTop;//SummaryFn initialized to all-top, see line [4] in SRH96 paper									EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5).joinWith(summaryFunction);EdgeFunction<V>fPrime=f4.composeWith(fCalleeSummary).composeWith(f5).joinWith(summaryFunction);									if(!fPrime.equalTo(summaryFunction)) {if(!fPrime.equalTo(summaryFunction)){										summaryFunctions.insertFunction(n,d2,retSiteN,d5,fPrime);summaryFunctions.insertFunction(n,d2,retSiteN,d5,fPrime);									}	}								}}							}}						}}					}}				}		}			}}		}}		//line 17-19 of Naeem/Lhotak/Rodriguez//line 17-19 of Naeem/Lhotak/Rodriguez		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);		List<N> returnSiteNs = icfg.getReturnSitesOfCallAt(n);List<N>returnSiteNs=icfg.getReturnSitesOfCallAt(n);		for (N returnSiteN : returnSiteNs) {for(NreturnSiteN:returnSiteNs){			FlowFunction<D> callToReturnFlowFunction = flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);FlowFunction<D>callToReturnFlowFunction=flowFunctions.getCallToReturnFlowFunction(n,returnSiteN);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;			for(D d3: callToReturnFlowFunction.computeTargets(d2)) {for(Dd3:callToReturnFlowFunction.computeTargets(d2)){				EdgeFunction<V> edgeFnE = edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d3);EdgeFunction<V>edgeFnE=edgeFunctions.getCallToReturnEdgeFunction(n,d2,returnSiteN,d3);				propagate(d1, returnSiteN, d3, f.composeWith(edgeFnE));propagate(d1,returnSiteN,d3,f.composeWith(edgeFnE));			}}			Map<D,EdgeFunction<V>> d3sAndF3s = summaryFunctions.summariesFor(n, d2, returnSiteN);Map<D,EdgeFunction<V>>d3sAndF3s=summaryFunctions.summariesFor(n,d2,returnSiteN);			for (Map.Entry<D,EdgeFunction<V>> d3AndF3 : d3sAndF3s.entrySet()) {for(Map.Entry<D,EdgeFunction<V>>d3AndF3:d3sAndF3s.entrySet()){				D d3 = d3AndF3.getKey();Dd3=d3AndF3.getKey();				EdgeFunction<V> f3 = d3AndF3.getValue();EdgeFunction<V>f3=d3AndF3.getValue();				if(f3==null) f3 = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paperif(f3==null)f3=allTop;//SummaryFn initialized to all-top, see line [4] in SRH96 paper				propagate(d1, returnSiteN, d3, f.composeWith(f3));propagate(d1,returnSiteN,d3,f.composeWith(f3));			}}		}}	}}	private EdgeFunction<V> jumpFunction(PathEdge<N, D, M> edge) {privateEdgeFunction<V>jumpFunction(PathEdge<N,D,M>edge){		synchronized (jumpFn) {synchronized(jumpFn){			EdgeFunction<V> function = jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());EdgeFunction<V>function=jumpFn.forwardLookup(edge.factAtSource(),edge.getTarget()).get(edge.factAtTarget());			if(function==null) return allTop; //JumpFn initialized to all-top, see line [2] in SRH96 paperif(function==null)returnallTop;//JumpFn initialized to all-top, see line [2] in SRH96 paper			return function;returnfunction;		}}	}}	/**/**	 * Lines 21-32 of the algorithm.		 * Lines 21-32 of the algorithm.		 */	 */	private void processExit(PathEdge<N,D,M> edge) {privatevoidprocessExit(PathEdge<N,D,M>edge){		final N n = edge.getTarget(); // an exit node; line 21...finalNn=edge.getTarget();// an exit node; line 21...		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);		M methodThatNeedsSummary = icfg.getMethodOf(n);MmethodThatNeedsSummary=icfg.getMethodOf(n);				final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();				for(N sP: icfg.getStartPointsOf(methodThatNeedsSummary)) {for(NsP:icfg.getStartPointsOf(methodThatNeedsSummary)){			//line 21.1 of Naeem/Lhotak/Rodriguez//line 21.1 of Naeem/Lhotak/Rodriguez						Set<Entry<N, Set<D>>> inc;Set<Entry<N,Set<D>>>inc;			synchronized (incoming) {synchronized(incoming){				addEndSummary(sP, d1, n, d2, f);addEndSummary(sP,d1,n,d2,f);				//copy to avoid concurrent modification exceptions by other threads//copy to avoid concurrent modification exceptions by other threads				inc = new HashSet<Map.Entry<N,Set<D>>>(incoming(d1, sP));inc=newHashSet<Map.Entry<N,Set<D>>>(incoming(d1,sP));			}}						for (Entry<N,Set<D>> entry: inc) {for(Entry<N,Set<D>>entry:inc){				//line 22//line 22				N c = entry.getKey();Nc=entry.getKey();				for(N retSiteC: icfg.getReturnSitesOfCallAt(c)) {for(NretSiteC:icfg.getReturnSitesOfCallAt(c)){					FlowFunction<D> retFunction = flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary,n,retSiteC);FlowFunction<D>retFunction=flowFunctions.getReturnFlowFunction(c,methodThatNeedsSummary,n,retSiteC);					flowFunctionConstructionCount++;flowFunctionConstructionCount++;					Set<D> targets = retFunction.computeTargets(d2);Set<D>targets=retFunction.computeTargets(d2);					for(D d4: entry.getValue()) {for(Dd4:entry.getValue()){						//line 23//line 23						for(D d5: targets) {for(Dd5:targets){							EdgeFunction<V> f4 = edgeFunctions.getCallEdgeFunction(c, d4, icfg.getMethodOf(n), d1);EdgeFunction<V>f4=edgeFunctions.getCallEdgeFunction(c,d4,icfg.getMethodOf(n),d1);							EdgeFunction<V> f5 = edgeFunctions.getReturnEdgeFunction(c, icfg.getMethodOf(n), n, d2, retSiteC, d5);EdgeFunction<V>f5=edgeFunctions.getReturnEdgeFunction(c,icfg.getMethodOf(n),n,d2,retSiteC,d5);							EdgeFunction<V> fPrime;EdgeFunction<V>fPrime;							synchronized (summaryFunctions) {synchronized(summaryFunctions){								EdgeFunction<V> summaryFunction = summaryFunctions.summariesFor(c,d4,retSiteC).get(d5);			EdgeFunction<V>summaryFunction=summaryFunctions.summariesFor(c,d4,retSiteC).get(d5);								if(summaryFunction==null) summaryFunction = allTop; //SummaryFn initialized to all-top, see line [4] in SRH96 paperif(summaryFunction==null)summaryFunction=allTop;//SummaryFn initialized to all-top, see line [4] in SRH96 paper								fPrime = f4.composeWith(f).composeWith(f5).joinWith(summaryFunction);fPrime=f4.composeWith(f).composeWith(f5).joinWith(summaryFunction);								if(!fPrime.equalTo(summaryFunction)) {if(!fPrime.equalTo(summaryFunction)){									summaryFunctions.insertFunction(c,d4,retSiteC,d5,fPrime);summaryFunctions.insertFunction(c,d4,retSiteC,d5,fPrime);								}}							}}							for(Map.Entry<D,EdgeFunction<V>> valAndFunc: jumpFn.reverseLookup(c,d4).entrySet()) {for(Map.Entry<D,EdgeFunction<V>>valAndFunc:jumpFn.reverseLookup(c,d4).entrySet()){								EdgeFunction<V> f3 = valAndFunc.getValue();EdgeFunction<V>f3=valAndFunc.getValue();								if(!f3.equalTo(allTop)); {if(!f3.equalTo(allTop));{									D d3 = valAndFunc.getKey();Dd3=valAndFunc.getKey();									propagate(d3, retSiteC, d5, f3.composeWith(fPrime));propagate(d3,retSiteC,d5,f3.composeWith(fPrime));								}}							}}						}}					}}				}}			}}		}}	}}		/**/**	 * Lines 33-37 of the algorithm.	 * Lines 33-37 of the algorithm.	 * @param edge	 * @param edge	 */	 */	private void processNormalFlow(PathEdge<N,D,M> edge) {privatevoidprocessNormalFlow(PathEdge<N,D,M>edge){		final D d1 = edge.factAtSource();finalDd1=edge.factAtSource();		final N n = edge.getTarget(); finalNn=edge.getTarget();		final D d2 = edge.factAtTarget();finalDd2=edge.factAtTarget();		EdgeFunction<V> f = jumpFunction(edge);EdgeFunction<V>f=jumpFunction(edge);		for (N m : icfg.getSuccsOf(n)) {for(Nm:icfg.getSuccsOf(n)){			FlowFunction<D> flowFunction = flowFunctions.getNormalFlowFunction(n,m);FlowFunction<D>flowFunction=flowFunctions.getNormalFlowFunction(n,m);			flowFunctionConstructionCount++;flowFunctionConstructionCount++;			Set<D> res = flowFunction.computeTargets(d2);Set<D>res=flowFunction.computeTargets(d2);			for (D d3 : res) {for(Dd3:res){				EdgeFunction<V> fprime = f.composeWith(edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));EdgeFunction<V>fprime=f.composeWith(edgeFunctions.getNormalEdgeFunction(n,d2,m,d3));				propagate(d1, m, d3, fprime); propagate(d1,m,d3,fprime);			}}		}}	}}		private void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f) {privatevoidpropagate(DsourceVal,Ntarget,DtargetVal,EdgeFunction<V>f){		EdgeFunction<V> jumpFnE;EdgeFunction<V>jumpFnE;		synchronized (jumpFn) {synchronized(jumpFn){			jumpFnE = jumpFn.reverseLookup(target, targetVal).get(sourceVal);jumpFnE=jumpFn.reverseLookup(target,targetVal).get(sourceVal);		}}		if(jumpFnE==null) jumpFnE = allTop; //JumpFn is initialized to all-top (see line [2] in SRH96 paper)if(jumpFnE==null)jumpFnE=allTop;//JumpFn is initialized to all-top (see line [2] in SRH96 paper)		EdgeFunction<V> fPrime = jumpFnE.joinWith(f);EdgeFunction<V>fPrime=jumpFnE.joinWith(f);		if(!fPrime.equalTo(jumpFnE)) {if(!fPrime.equalTo(jumpFnE)){			synchronized (jumpFn) {synchronized(jumpFn){				jumpFn.addFunction(sourceVal, target, targetVal, fPrime);jumpFn.addFunction(sourceVal,target,targetVal,fPrime);			}}						PathEdge<N,D,M> edge = new PathEdge<N,D,M>(sourceVal, target, targetVal);PathEdge<N,D,M>edge=newPathEdge<N,D,M>(sourceVal,target,targetVal);			synchronized (pathWorklist) {synchronized(pathWorklist){				pathWorklist.add(edge);pathWorklist.add(edge);			}}			if(DEBUG) {if(DEBUG){				if(targetVal!=zeroValue) {			if(targetVal!=zeroValue){					StringBuilder result = new StringBuilder();StringBuilderresult=newStringBuilder();					result.append("EDGE:  <");result.append("EDGE:  <");					result.append(icfg.getMethodOf(target));result.append(icfg.getMethodOf(target));					result.append(",");result.append(",");					result.append(sourceVal);result.append(sourceVal);					result.append("> -> <");result.append("> -> <");					result.append(target);result.append(target);					result.append(",");result.append(",");					result.append(targetVal);result.append(targetVal);					result.append("> - ");result.append("> - ");					result.append(fPrime);result.append(fPrime);					System.err.println(result.toString());System.err.println(result.toString());				}}			}}		}}	}}		private Set<Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {privateSet<Cell<N,D,EdgeFunction<V>>>endSummary(NsP,Dd3){		Table<N, D, EdgeFunction<V>> map = endSummary.get(sP, d3);Table<N,D,EdgeFunction<V>>map=endSummary.get(sP,d3);		if(map==null) return Collections.emptySet();if(map==null)returnCollections.emptySet();		return map.cellSet();returnmap.cellSet();	}}	private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {privatevoidaddEndSummary(NsP,Dd1,NeP,Dd2,EdgeFunction<V>f){		Table<N, D, EdgeFunction<V>> summaries = endSummary.get(sP, d1);Table<N,D,EdgeFunction<V>>summaries=endSummary.get(sP,d1);		if(summaries==null) {if(summaries==null){			summaries = HashBasedTable.create();summaries=HashBasedTable.create();			endSummary.put(sP, d1, summaries);endSummary.put(sP,d1,summaries);		}}		summaries.put(eP,d2,f);summaries.put(eP,d2,f);	}	}		private Set<Entry<N, Set<D>>> incoming(D d1, N sP) {privateSet<Entry<N,Set<D>>>incoming(Dd1,NsP){		Map<N, Set<D>> map = incoming.get(sP, d1);Map<N,Set<D>>map=incoming.get(sP,d1);		if(map==null) return Collections.emptySet();if(map==null)returnCollections.emptySet();		return map.entrySet();		returnmap.entrySet();	}}		private void addIncoming(N sP, D d3, N n, D d2) {privatevoidaddIncoming(NsP,Dd3,Nn,Dd2){		Map<N, Set<D>> summaries = incoming.get(sP, d3);Map<N,Set<D>>summaries=incoming.get(sP,d3);		if(summaries==null) {if(summaries==null){			summaries = new HashMap<N, Set<D>>();summaries=newHashMap<N,Set<D>>();			incoming.put(sP, d3, summaries);incoming.put(sP,d3,summaries);		}}		Set<D> set = summaries.get(n);Set<D>set=summaries.get(n);		if(set==null) {if(set==null){			set = new HashSet<D>();set=newHashSet<D>();			summaries.put(n,set);summaries.put(n,set);		}}		set.add(d2);set.add(d2);	}	}		/**/**	 * Returns the V-type result for the given value at the given statement. 	 * Returns the V-type result for the given value at the given statement. 	 */	 */	public V resultAt(N stmt, D value) {publicVresultAt(Nstmt,Dvalue){		return val.get(stmt, value);returnval.get(stmt,value);	}}		/**/**	 * Returns the resulting environment for the given statement.	 * Returns the resulting environment for the given statement.	 * The artificial zero value is automatically stripped.	 * The artificial zero value is automatically stripped.	 */	 */	public Map<D,V> resultsAt(N stmt) {publicMap<D,V>resultsAt(Nstmt){		//filter out the artificial zero-value//filter out the artificial zero-value		return Maps.filterKeys(val.row(stmt), new Predicate<D>() {returnMaps.filterKeys(val.row(stmt),newPredicate<D>(){			public boolean apply(D val) {publicbooleanapply(Dval){				return val!=zeroValue;returnval!=zeroValue;			}}		});});	}}	public void printStats() {publicvoidprintStats(){		if(DEBUG) {if(DEBUG){			if(ffCache!=null)if(ffCache!=null)				ffCache.printStats();ffCache.printStats();			if(efCache!=null)if(efCache!=null)				efCache.printStats();efCache.printStats();		} else {}else{			System.err.println("No statistics were collected, as DEBUG is disabled.");System.err.println("No statistics were collected, as DEBUG is disabled.");		}}	}}		private class PathEdgeProcessingTask implements Runnable {privateclassPathEdgeProcessingTaskimplementsRunnable{		private final PathEdge<N, D, M> edge;privatefinalPathEdge<N,D,M>edge;		public PathEdgeProcessingTask(PathEdge<N, D, M> edge) {publicPathEdgeProcessingTask(PathEdge<N,D,M>edge){			this.edge = edge;this.edge=edge;		}}		public void run() {publicvoidrun(){			if(icfg.isCallStmt(edge.getTarget())) {if(icfg.isCallStmt(edge.getTarget())){				processCall(edge);processCall(edge);			} else {}else{				//note that some statements, such as "throw" may be//note that some statements, such as "throw" may be				//both an exit statement and a "normal" statement//both an exit statement and a "normal" statement				if(icfg.isExitStmt(edge.getTarget())) {if(icfg.isExitStmt(edge.getTarget())){					processExit(edge);processExit(edge);				}}				if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()) {if(!icfg.getSuccsOf(edge.getTarget()).isEmpty()){					processNormalFlow(edge);processNormalFlow(edge);				}}			}}			synchronized (pathWorklist) {synchronized(pathWorklist){				numTasks.getAndDecrement();numTasks.getAndDecrement();				//potentially wake up waiting broker thread//potentially wake up waiting broker thread				//(see forwardComputeJumpFunctionsSLRPs())//(see forwardComputeJumpFunctionsSLRPs())				pathWorklist.notify();pathWorklist.notify();			}}		}}	}}		private class ValuePropagationTask implements Runnable {privateclassValuePropagationTaskimplementsRunnable{		private final Pair<N, D> nAndD;privatefinalPair<N,D>nAndD;		public ValuePropagationTask(Pair<N,D> nAndD) {publicValuePropagationTask(Pair<N,D>nAndD){			this.nAndD = nAndD;this.nAndD=nAndD;		}}		public void run() {publicvoidrun(){			N n = nAndD.getO1();Nn=nAndD.getO1();			if(icfg.isStartPoint(n)) {if(icfg.isStartPoint(n)){				propagateValueAtStart(nAndD, n);propagateValueAtStart(nAndD,n);			}}			if(icfg.isCallStmt(n)) {if(icfg.isCallStmt(n)){				propagateValueAtCall(nAndD, n);propagateValueAtCall(nAndD,n);			}}			synchronized (nodeWorklist) {synchronized(nodeWorklist){				numTasks.getAndDecrement();numTasks.getAndDecrement();				//potentially wake up waiting broker thread//potentially wake up waiting broker thread				//(see forwardComputeJumpFunctionsSLRPs())//(see forwardComputeJumpFunctionsSLRPs())				nodeWorklist.notify();nodeWorklist.notify();			}}		}}	}}		private class ValueComputationTask implements Runnable {privateclassValueComputationTaskimplementsRunnable{		private final N[] values;privatefinalN[]values;		final int num;finalintnum;		public ValueComputationTask(N[] values, int num) {publicValueComputationTask(N[]values,intnum){			this.values = values;this.values=values;			this.num = num;this.num=num;		}}		public void run() {publicvoidrun(){			int sectionSize = (int) Math.floor(values.length / numThreads) + numThreads;intsectionSize=(int)Math.floor(values.length/numThreads)+numThreads;			for(int i = sectionSize * num; i < Math.min(sectionSize * (num+1),values.length); i++) {for(inti=sectionSize*num;i<Math.min(sectionSize*(num+1),values.length);i++){				N n = values[i];Nn=values[i];				for(N sP: icfg.getStartPointsOf(icfg.getMethodOf(n))) {					for(NsP:icfg.getStartPointsOf(icfg.getMethodOf(n))){					Set<Cell<D, D, EdgeFunction<V>>> lookupByTarget;Set<Cell<D,D,EdgeFunction<V>>>lookupByTarget;					lookupByTarget = jumpFn.lookupByTarget(n);lookupByTarget=jumpFn.lookupByTarget(n);					for(Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {for(Cell<D,D,EdgeFunction<V>>sourceValTargetValAndFunction:lookupByTarget){						D dPrime = sourceValTargetValAndFunction.getRowKey();DdPrime=sourceValTargetValAndFunction.getRowKey();						D d = sourceValTargetValAndFunction.getColumnKey();Dd=sourceValTargetValAndFunction.getColumnKey();						EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();EdgeFunction<V>fPrime=sourceValTargetValAndFunction.getValue();						synchronized (val) {synchronized(val){							setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));setVal(n,d,valueLattice.join(val(n,d),fPrime.computeTarget(val(sP,dPrime))));						}}						flowFunctionApplicationCount++;flowFunctionApplicationCount++;					}}				}}			}}		}}	}}}}





