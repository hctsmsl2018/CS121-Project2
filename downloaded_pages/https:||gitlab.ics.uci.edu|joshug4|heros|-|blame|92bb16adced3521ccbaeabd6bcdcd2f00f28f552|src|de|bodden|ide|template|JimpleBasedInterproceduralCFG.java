



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

92bb16adced3521ccbaeabd6bcdcd2f00f28f552

















92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Switch branch/tag










heros


src


de


bodden


ide


template


JimpleBasedInterproceduralCFG.java



Find file
Normal viewHistoryPermalink






JimpleBasedInterproceduralCFG.java



7.36 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.template;









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




27




28




29




30





import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






31




32




33




34




35




36




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ThreadSafe;
import de.bodden.ide.solver.IDESolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Default implementation for the {@link InterproceduralCFG} interface.
 * Includes all statements reachable from {@link Scene#getEntryPoints()} through
 * explicit call statements or through calls to {@link Thread#start()}.
 * 
 * This class is designed to be thread safe, and subclasses of this class must be designed
 * in a thread-safe way, too.
 */
@ThreadSafe
public class JimpleBasedInterproceduralCFG implements InterproceduralCFG<Unit,SootMethod> {
	
	//retains only callers that are explicit call sites or Thread.start()
	protected static class EdgeFilter extends Filter {		
		protected EdgeFilter() {
			super(new EdgePredicate() {
				public boolean want(Edge e) {				
					return e.kind().isExplicit() || e.kind().isThread();
				}
			});
		}
	}
	
	@DontSynchronize("readonly")
	protected final CallGraph cg;
	
	@DontSynchronize("written by single thread; read afterwards")
	protected final Map<Unit,Body> unitToOwner = new HashMap<Unit,Body>();	
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Body,DirectedGraph<Unit>> bodyToUnitGraph =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Body,DirectedGraph<Unit>>() {
				public DirectedGraph<Unit> load(Body body) throws Exception {
					return makeGraph(body);
				}
			});
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Unit,Set<SootMethod>> unitToCallees =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Unit,Set<SootMethod>>() {
				public Set<SootMethod> load(Unit u) throws Exception {
					Set<SootMethod> res = new LinkedHashSet<SootMethod>();
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(u));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						if(edge.getTgt()==null) {
							System.err.println();
						}
						SootMethod m = edge.getTgt().method();
						if(m.hasActiveBody())
						res.add(m);
					}
					return res; 
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallers =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();					
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesInto(m));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallsFromWithin =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();
					//only retain calls that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(m));
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	
	public JimpleBasedInterproceduralCFG() {
		cg = Scene.v().getCallGraph();
		
		List<MethodOrMethodContext> eps = new ArrayList<MethodOrMethodContext>();
		eps.addAll(Scene.v().getEntryPoints());
		ReachableMethods reachableMethods = new ReachableMethods(cg, eps.iterator(), new EdgeFilter());
		reachableMethods.update();
		
		for(Iterator<MethodOrMethodContext> iter = reachableMethods.listener(); iter.hasNext(); ) {
			SootMethod m = iter.next().method();
			if(m.hasActiveBody()) {
				Body b = m.getActiveBody();
				PatchingChain<Unit> units = b.getUnits();
				for (Unit unit : units) {
					unitToOwner.put(unit, b);
				}
			}
		}
	}

	@Override
	public SootMethod getMethodOf(Unit u) {
		return unitToOwner.get(u).getMethod();
	}

	@Override
	public List<Unit> getSuccsOf(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getSuccsOf(u);
	}

	private DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {
		return bodyToUnitGraph.getUnchecked(body);
	}

	protected synchronized DirectedGraph<Unit> makeGraph(Body body) {
		return new ExceptionalUnitGraph(body, UnitThrowAnalysis.v() ,true);
	}

	@Override
	public Set<SootMethod> getCalleesOfCallAt(Unit u) {
		return unitToCallees.getUnchecked(u);
	}

	@Override
	public List<Unit> getReturnSitesOfCallAt(Unit u) {
		return getSuccsOf(u);
	}

	@Override
	public boolean isCallStmt(Unit u) {
		return ((Stmt)u).containsInvokeExpr();
	}

	@Override
	public boolean isExitStmt(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getTails().contains(u);
	}

	@Override
	public Set<Unit> getCallersOf(SootMethod m) {
		return methodToCallers.getUnchecked(m);
	}
	
	@Override
	public Set<Unit> getCallsFromWithin(SootMethod m) {
		return methodToCallsFromWithin.getUnchecked(m);		
	}

	@Override
	public Set<Unit> getStartPointsOf(SootMethod m) {
		if(m.hasActiveBody()) {
			Body body = m.getActiveBody();
			DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
			return new LinkedHashSet<Unit>(unitGraph.getHeads());
		}
		return null;
	}

	@Override
	public boolean isStartPoint(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);		
		return unitGraph.getHeads().contains(u);
	}

	@Override
	//TODO do we need to replace call by return for backwards analysis?
	public Set<Unit> allNonCallStartNodes() {
		Set<Unit> res = new LinkedHashSet<Unit>(unitToOwner.keySet());
		for (Iterator<Unit> iter = res.iterator(); iter.hasNext();) {
			Unit u = iter.next();
			if(isStartPoint(u) || isCallStmt(u)) iter.remove();
		}
		return res;
	}

	@Override
	public boolean isFallThroughSuccessor(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.fallsThrough()) return false;
		Body body = unitToOwner.get(u);
		return body.getUnits().getSuccOf(u) == succ;
	}

	@Override
	public boolean isBranchTarget(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.branches()) return false;
		for (UnitBox ub : succ.getUnitBoxes()) {
			if(ub.getUnit()==succ) return true;
		}
		return false;
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

92bb16adced3521ccbaeabd6bcdcd2f00f28f552

















92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Switch branch/tag










heros


src


de


bodden


ide


template


JimpleBasedInterproceduralCFG.java



Find file
Normal viewHistoryPermalink






JimpleBasedInterproceduralCFG.java



7.36 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.template;









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




27




28




29




30





import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






31




32




33




34




35




36




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ThreadSafe;
import de.bodden.ide.solver.IDESolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Default implementation for the {@link InterproceduralCFG} interface.
 * Includes all statements reachable from {@link Scene#getEntryPoints()} through
 * explicit call statements or through calls to {@link Thread#start()}.
 * 
 * This class is designed to be thread safe, and subclasses of this class must be designed
 * in a thread-safe way, too.
 */
@ThreadSafe
public class JimpleBasedInterproceduralCFG implements InterproceduralCFG<Unit,SootMethod> {
	
	//retains only callers that are explicit call sites or Thread.start()
	protected static class EdgeFilter extends Filter {		
		protected EdgeFilter() {
			super(new EdgePredicate() {
				public boolean want(Edge e) {				
					return e.kind().isExplicit() || e.kind().isThread();
				}
			});
		}
	}
	
	@DontSynchronize("readonly")
	protected final CallGraph cg;
	
	@DontSynchronize("written by single thread; read afterwards")
	protected final Map<Unit,Body> unitToOwner = new HashMap<Unit,Body>();	
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Body,DirectedGraph<Unit>> bodyToUnitGraph =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Body,DirectedGraph<Unit>>() {
				public DirectedGraph<Unit> load(Body body) throws Exception {
					return makeGraph(body);
				}
			});
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Unit,Set<SootMethod>> unitToCallees =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Unit,Set<SootMethod>>() {
				public Set<SootMethod> load(Unit u) throws Exception {
					Set<SootMethod> res = new LinkedHashSet<SootMethod>();
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(u));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						if(edge.getTgt()==null) {
							System.err.println();
						}
						SootMethod m = edge.getTgt().method();
						if(m.hasActiveBody())
						res.add(m);
					}
					return res; 
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallers =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();					
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesInto(m));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallsFromWithin =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();
					//only retain calls that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(m));
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	
	public JimpleBasedInterproceduralCFG() {
		cg = Scene.v().getCallGraph();
		
		List<MethodOrMethodContext> eps = new ArrayList<MethodOrMethodContext>();
		eps.addAll(Scene.v().getEntryPoints());
		ReachableMethods reachableMethods = new ReachableMethods(cg, eps.iterator(), new EdgeFilter());
		reachableMethods.update();
		
		for(Iterator<MethodOrMethodContext> iter = reachableMethods.listener(); iter.hasNext(); ) {
			SootMethod m = iter.next().method();
			if(m.hasActiveBody()) {
				Body b = m.getActiveBody();
				PatchingChain<Unit> units = b.getUnits();
				for (Unit unit : units) {
					unitToOwner.put(unit, b);
				}
			}
		}
	}

	@Override
	public SootMethod getMethodOf(Unit u) {
		return unitToOwner.get(u).getMethod();
	}

	@Override
	public List<Unit> getSuccsOf(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getSuccsOf(u);
	}

	private DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {
		return bodyToUnitGraph.getUnchecked(body);
	}

	protected synchronized DirectedGraph<Unit> makeGraph(Body body) {
		return new ExceptionalUnitGraph(body, UnitThrowAnalysis.v() ,true);
	}

	@Override
	public Set<SootMethod> getCalleesOfCallAt(Unit u) {
		return unitToCallees.getUnchecked(u);
	}

	@Override
	public List<Unit> getReturnSitesOfCallAt(Unit u) {
		return getSuccsOf(u);
	}

	@Override
	public boolean isCallStmt(Unit u) {
		return ((Stmt)u).containsInvokeExpr();
	}

	@Override
	public boolean isExitStmt(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getTails().contains(u);
	}

	@Override
	public Set<Unit> getCallersOf(SootMethod m) {
		return methodToCallers.getUnchecked(m);
	}
	
	@Override
	public Set<Unit> getCallsFromWithin(SootMethod m) {
		return methodToCallsFromWithin.getUnchecked(m);		
	}

	@Override
	public Set<Unit> getStartPointsOf(SootMethod m) {
		if(m.hasActiveBody()) {
			Body body = m.getActiveBody();
			DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
			return new LinkedHashSet<Unit>(unitGraph.getHeads());
		}
		return null;
	}

	@Override
	public boolean isStartPoint(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);		
		return unitGraph.getHeads().contains(u);
	}

	@Override
	//TODO do we need to replace call by return for backwards analysis?
	public Set<Unit> allNonCallStartNodes() {
		Set<Unit> res = new LinkedHashSet<Unit>(unitToOwner.keySet());
		for (Iterator<Unit> iter = res.iterator(); iter.hasNext();) {
			Unit u = iter.next();
			if(isStartPoint(u) || isCallStmt(u)) iter.remove();
		}
		return res;
	}

	@Override
	public boolean isFallThroughSuccessor(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.fallsThrough()) return false;
		Body body = unitToOwner.get(u);
		return body.getUnits().getSuccOf(u) == succ;
	}

	@Override
	public boolean isBranchTarget(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.branches()) return false;
		for (UnitBox ub : succ.getUnitBoxes()) {
			if(ub.getUnit()==succ) return true;
		}
		return false;
	}
}











Open sidebar



Joshua Garcia heros

92bb16adced3521ccbaeabd6bcdcd2f00f28f552







Open sidebar



Joshua Garcia heros

92bb16adced3521ccbaeabd6bcdcd2f00f28f552




Open sidebar

Joshua Garcia heros

92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Joshua Garciaherosheros
92bb16adced3521ccbaeabd6bcdcd2f00f28f552










92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Switch branch/tag










heros


src


de


bodden


ide


template


JimpleBasedInterproceduralCFG.java



Find file
Normal viewHistoryPermalink






JimpleBasedInterproceduralCFG.java



7.36 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.template;









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




27




28




29




30





import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






31




32




33




34




35




36




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ThreadSafe;
import de.bodden.ide.solver.IDESolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Default implementation for the {@link InterproceduralCFG} interface.
 * Includes all statements reachable from {@link Scene#getEntryPoints()} through
 * explicit call statements or through calls to {@link Thread#start()}.
 * 
 * This class is designed to be thread safe, and subclasses of this class must be designed
 * in a thread-safe way, too.
 */
@ThreadSafe
public class JimpleBasedInterproceduralCFG implements InterproceduralCFG<Unit,SootMethod> {
	
	//retains only callers that are explicit call sites or Thread.start()
	protected static class EdgeFilter extends Filter {		
		protected EdgeFilter() {
			super(new EdgePredicate() {
				public boolean want(Edge e) {				
					return e.kind().isExplicit() || e.kind().isThread();
				}
			});
		}
	}
	
	@DontSynchronize("readonly")
	protected final CallGraph cg;
	
	@DontSynchronize("written by single thread; read afterwards")
	protected final Map<Unit,Body> unitToOwner = new HashMap<Unit,Body>();	
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Body,DirectedGraph<Unit>> bodyToUnitGraph =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Body,DirectedGraph<Unit>>() {
				public DirectedGraph<Unit> load(Body body) throws Exception {
					return makeGraph(body);
				}
			});
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Unit,Set<SootMethod>> unitToCallees =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Unit,Set<SootMethod>>() {
				public Set<SootMethod> load(Unit u) throws Exception {
					Set<SootMethod> res = new LinkedHashSet<SootMethod>();
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(u));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						if(edge.getTgt()==null) {
							System.err.println();
						}
						SootMethod m = edge.getTgt().method();
						if(m.hasActiveBody())
						res.add(m);
					}
					return res; 
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallers =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();					
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesInto(m));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallsFromWithin =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();
					//only retain calls that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(m));
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	
	public JimpleBasedInterproceduralCFG() {
		cg = Scene.v().getCallGraph();
		
		List<MethodOrMethodContext> eps = new ArrayList<MethodOrMethodContext>();
		eps.addAll(Scene.v().getEntryPoints());
		ReachableMethods reachableMethods = new ReachableMethods(cg, eps.iterator(), new EdgeFilter());
		reachableMethods.update();
		
		for(Iterator<MethodOrMethodContext> iter = reachableMethods.listener(); iter.hasNext(); ) {
			SootMethod m = iter.next().method();
			if(m.hasActiveBody()) {
				Body b = m.getActiveBody();
				PatchingChain<Unit> units = b.getUnits();
				for (Unit unit : units) {
					unitToOwner.put(unit, b);
				}
			}
		}
	}

	@Override
	public SootMethod getMethodOf(Unit u) {
		return unitToOwner.get(u).getMethod();
	}

	@Override
	public List<Unit> getSuccsOf(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getSuccsOf(u);
	}

	private DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {
		return bodyToUnitGraph.getUnchecked(body);
	}

	protected synchronized DirectedGraph<Unit> makeGraph(Body body) {
		return new ExceptionalUnitGraph(body, UnitThrowAnalysis.v() ,true);
	}

	@Override
	public Set<SootMethod> getCalleesOfCallAt(Unit u) {
		return unitToCallees.getUnchecked(u);
	}

	@Override
	public List<Unit> getReturnSitesOfCallAt(Unit u) {
		return getSuccsOf(u);
	}

	@Override
	public boolean isCallStmt(Unit u) {
		return ((Stmt)u).containsInvokeExpr();
	}

	@Override
	public boolean isExitStmt(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getTails().contains(u);
	}

	@Override
	public Set<Unit> getCallersOf(SootMethod m) {
		return methodToCallers.getUnchecked(m);
	}
	
	@Override
	public Set<Unit> getCallsFromWithin(SootMethod m) {
		return methodToCallsFromWithin.getUnchecked(m);		
	}

	@Override
	public Set<Unit> getStartPointsOf(SootMethod m) {
		if(m.hasActiveBody()) {
			Body body = m.getActiveBody();
			DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
			return new LinkedHashSet<Unit>(unitGraph.getHeads());
		}
		return null;
	}

	@Override
	public boolean isStartPoint(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);		
		return unitGraph.getHeads().contains(u);
	}

	@Override
	//TODO do we need to replace call by return for backwards analysis?
	public Set<Unit> allNonCallStartNodes() {
		Set<Unit> res = new LinkedHashSet<Unit>(unitToOwner.keySet());
		for (Iterator<Unit> iter = res.iterator(); iter.hasNext();) {
			Unit u = iter.next();
			if(isStartPoint(u) || isCallStmt(u)) iter.remove();
		}
		return res;
	}

	@Override
	public boolean isFallThroughSuccessor(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.fallsThrough()) return false;
		Body body = unitToOwner.get(u);
		return body.getUnits().getSuccOf(u) == succ;
	}

	@Override
	public boolean isBranchTarget(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.branches()) return false;
		for (UnitBox ub : succ.getUnitBoxes()) {
			if(ub.getUnit()==succ) return true;
		}
		return false;
	}
}














92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Switch branch/tag










heros


src


de


bodden


ide


template


JimpleBasedInterproceduralCFG.java



Find file
Normal viewHistoryPermalink






JimpleBasedInterproceduralCFG.java



7.36 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.template;









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




27




28




29




30





import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






31




32




33




34




35




36




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ThreadSafe;
import de.bodden.ide.solver.IDESolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Default implementation for the {@link InterproceduralCFG} interface.
 * Includes all statements reachable from {@link Scene#getEntryPoints()} through
 * explicit call statements or through calls to {@link Thread#start()}.
 * 
 * This class is designed to be thread safe, and subclasses of this class must be designed
 * in a thread-safe way, too.
 */
@ThreadSafe
public class JimpleBasedInterproceduralCFG implements InterproceduralCFG<Unit,SootMethod> {
	
	//retains only callers that are explicit call sites or Thread.start()
	protected static class EdgeFilter extends Filter {		
		protected EdgeFilter() {
			super(new EdgePredicate() {
				public boolean want(Edge e) {				
					return e.kind().isExplicit() || e.kind().isThread();
				}
			});
		}
	}
	
	@DontSynchronize("readonly")
	protected final CallGraph cg;
	
	@DontSynchronize("written by single thread; read afterwards")
	protected final Map<Unit,Body> unitToOwner = new HashMap<Unit,Body>();	
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Body,DirectedGraph<Unit>> bodyToUnitGraph =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Body,DirectedGraph<Unit>>() {
				public DirectedGraph<Unit> load(Body body) throws Exception {
					return makeGraph(body);
				}
			});
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Unit,Set<SootMethod>> unitToCallees =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Unit,Set<SootMethod>>() {
				public Set<SootMethod> load(Unit u) throws Exception {
					Set<SootMethod> res = new LinkedHashSet<SootMethod>();
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(u));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						if(edge.getTgt()==null) {
							System.err.println();
						}
						SootMethod m = edge.getTgt().method();
						if(m.hasActiveBody())
						res.add(m);
					}
					return res; 
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallers =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();					
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesInto(m));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallsFromWithin =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();
					//only retain calls that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(m));
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	
	public JimpleBasedInterproceduralCFG() {
		cg = Scene.v().getCallGraph();
		
		List<MethodOrMethodContext> eps = new ArrayList<MethodOrMethodContext>();
		eps.addAll(Scene.v().getEntryPoints());
		ReachableMethods reachableMethods = new ReachableMethods(cg, eps.iterator(), new EdgeFilter());
		reachableMethods.update();
		
		for(Iterator<MethodOrMethodContext> iter = reachableMethods.listener(); iter.hasNext(); ) {
			SootMethod m = iter.next().method();
			if(m.hasActiveBody()) {
				Body b = m.getActiveBody();
				PatchingChain<Unit> units = b.getUnits();
				for (Unit unit : units) {
					unitToOwner.put(unit, b);
				}
			}
		}
	}

	@Override
	public SootMethod getMethodOf(Unit u) {
		return unitToOwner.get(u).getMethod();
	}

	@Override
	public List<Unit> getSuccsOf(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getSuccsOf(u);
	}

	private DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {
		return bodyToUnitGraph.getUnchecked(body);
	}

	protected synchronized DirectedGraph<Unit> makeGraph(Body body) {
		return new ExceptionalUnitGraph(body, UnitThrowAnalysis.v() ,true);
	}

	@Override
	public Set<SootMethod> getCalleesOfCallAt(Unit u) {
		return unitToCallees.getUnchecked(u);
	}

	@Override
	public List<Unit> getReturnSitesOfCallAt(Unit u) {
		return getSuccsOf(u);
	}

	@Override
	public boolean isCallStmt(Unit u) {
		return ((Stmt)u).containsInvokeExpr();
	}

	@Override
	public boolean isExitStmt(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getTails().contains(u);
	}

	@Override
	public Set<Unit> getCallersOf(SootMethod m) {
		return methodToCallers.getUnchecked(m);
	}
	
	@Override
	public Set<Unit> getCallsFromWithin(SootMethod m) {
		return methodToCallsFromWithin.getUnchecked(m);		
	}

	@Override
	public Set<Unit> getStartPointsOf(SootMethod m) {
		if(m.hasActiveBody()) {
			Body body = m.getActiveBody();
			DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
			return new LinkedHashSet<Unit>(unitGraph.getHeads());
		}
		return null;
	}

	@Override
	public boolean isStartPoint(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);		
		return unitGraph.getHeads().contains(u);
	}

	@Override
	//TODO do we need to replace call by return for backwards analysis?
	public Set<Unit> allNonCallStartNodes() {
		Set<Unit> res = new LinkedHashSet<Unit>(unitToOwner.keySet());
		for (Iterator<Unit> iter = res.iterator(); iter.hasNext();) {
			Unit u = iter.next();
			if(isStartPoint(u) || isCallStmt(u)) iter.remove();
		}
		return res;
	}

	@Override
	public boolean isFallThroughSuccessor(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.fallsThrough()) return false;
		Body body = unitToOwner.get(u);
		return body.getUnits().getSuccOf(u) == succ;
	}

	@Override
	public boolean isBranchTarget(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.branches()) return false;
		for (UnitBox ub : succ.getUnitBoxes()) {
			if(ub.getUnit()==succ) return true;
		}
		return false;
	}
}










92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Switch branch/tag










heros


src


de


bodden


ide


template


JimpleBasedInterproceduralCFG.java



Find file
Normal viewHistoryPermalink




92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Switch branch/tag










heros


src


de


bodden


ide


template


JimpleBasedInterproceduralCFG.java





92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Switch branch/tag








92bb16adced3521ccbaeabd6bcdcd2f00f28f552


Switch branch/tag





92bb16adced3521ccbaeabd6bcdcd2f00f28f552

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

de

bodden

ide

template

JimpleBasedInterproceduralCFG.java
Find file
Normal viewHistoryPermalink




JimpleBasedInterproceduralCFG.java



7.36 KB









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.template;









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




27




28




29




30





import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






31




32




33




34




35




36




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ThreadSafe;
import de.bodden.ide.solver.IDESolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Default implementation for the {@link InterproceduralCFG} interface.
 * Includes all statements reachable from {@link Scene#getEntryPoints()} through
 * explicit call statements or through calls to {@link Thread#start()}.
 * 
 * This class is designed to be thread safe, and subclasses of this class must be designed
 * in a thread-safe way, too.
 */
@ThreadSafe
public class JimpleBasedInterproceduralCFG implements InterproceduralCFG<Unit,SootMethod> {
	
	//retains only callers that are explicit call sites or Thread.start()
	protected static class EdgeFilter extends Filter {		
		protected EdgeFilter() {
			super(new EdgePredicate() {
				public boolean want(Edge e) {				
					return e.kind().isExplicit() || e.kind().isThread();
				}
			});
		}
	}
	
	@DontSynchronize("readonly")
	protected final CallGraph cg;
	
	@DontSynchronize("written by single thread; read afterwards")
	protected final Map<Unit,Body> unitToOwner = new HashMap<Unit,Body>();	
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Body,DirectedGraph<Unit>> bodyToUnitGraph =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Body,DirectedGraph<Unit>>() {
				public DirectedGraph<Unit> load(Body body) throws Exception {
					return makeGraph(body);
				}
			});
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Unit,Set<SootMethod>> unitToCallees =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Unit,Set<SootMethod>>() {
				public Set<SootMethod> load(Unit u) throws Exception {
					Set<SootMethod> res = new LinkedHashSet<SootMethod>();
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(u));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						if(edge.getTgt()==null) {
							System.err.println();
						}
						SootMethod m = edge.getTgt().method();
						if(m.hasActiveBody())
						res.add(m);
					}
					return res; 
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallers =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();					
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesInto(m));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallsFromWithin =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();
					//only retain calls that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(m));
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	
	public JimpleBasedInterproceduralCFG() {
		cg = Scene.v().getCallGraph();
		
		List<MethodOrMethodContext> eps = new ArrayList<MethodOrMethodContext>();
		eps.addAll(Scene.v().getEntryPoints());
		ReachableMethods reachableMethods = new ReachableMethods(cg, eps.iterator(), new EdgeFilter());
		reachableMethods.update();
		
		for(Iterator<MethodOrMethodContext> iter = reachableMethods.listener(); iter.hasNext(); ) {
			SootMethod m = iter.next().method();
			if(m.hasActiveBody()) {
				Body b = m.getActiveBody();
				PatchingChain<Unit> units = b.getUnits();
				for (Unit unit : units) {
					unitToOwner.put(unit, b);
				}
			}
		}
	}

	@Override
	public SootMethod getMethodOf(Unit u) {
		return unitToOwner.get(u).getMethod();
	}

	@Override
	public List<Unit> getSuccsOf(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getSuccsOf(u);
	}

	private DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {
		return bodyToUnitGraph.getUnchecked(body);
	}

	protected synchronized DirectedGraph<Unit> makeGraph(Body body) {
		return new ExceptionalUnitGraph(body, UnitThrowAnalysis.v() ,true);
	}

	@Override
	public Set<SootMethod> getCalleesOfCallAt(Unit u) {
		return unitToCallees.getUnchecked(u);
	}

	@Override
	public List<Unit> getReturnSitesOfCallAt(Unit u) {
		return getSuccsOf(u);
	}

	@Override
	public boolean isCallStmt(Unit u) {
		return ((Stmt)u).containsInvokeExpr();
	}

	@Override
	public boolean isExitStmt(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getTails().contains(u);
	}

	@Override
	public Set<Unit> getCallersOf(SootMethod m) {
		return methodToCallers.getUnchecked(m);
	}
	
	@Override
	public Set<Unit> getCallsFromWithin(SootMethod m) {
		return methodToCallsFromWithin.getUnchecked(m);		
	}

	@Override
	public Set<Unit> getStartPointsOf(SootMethod m) {
		if(m.hasActiveBody()) {
			Body body = m.getActiveBody();
			DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
			return new LinkedHashSet<Unit>(unitGraph.getHeads());
		}
		return null;
	}

	@Override
	public boolean isStartPoint(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);		
		return unitGraph.getHeads().contains(u);
	}

	@Override
	//TODO do we need to replace call by return for backwards analysis?
	public Set<Unit> allNonCallStartNodes() {
		Set<Unit> res = new LinkedHashSet<Unit>(unitToOwner.keySet());
		for (Iterator<Unit> iter = res.iterator(); iter.hasNext();) {
			Unit u = iter.next();
			if(isStartPoint(u) || isCallStmt(u)) iter.remove();
		}
		return res;
	}

	@Override
	public boolean isFallThroughSuccessor(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.fallsThrough()) return false;
		Body body = unitToOwner.get(u);
		return body.getUnits().getSuccOf(u) == succ;
	}

	@Override
	public boolean isBranchTarget(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.branches()) return false;
		for (UnitBox ub : succ.getUnitBoxes()) {
			if(ub.getUnit()==succ) return true;
		}
		return false;
	}
}








JimpleBasedInterproceduralCFG.java



7.36 KB










JimpleBasedInterproceduralCFG.java



7.36 KB









Newer










Older
NewerOlder







renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package de.bodden.ide.template;









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




27




28




29




30





import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






31




32




33




34




35




36




import de.bodden.ide.DontSynchronize;
import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.SynchronizedBy;
import de.bodden.ide.ThreadSafe;
import de.bodden.ide.solver.IDESolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * Default implementation for the {@link InterproceduralCFG} interface.
 * Includes all statements reachable from {@link Scene#getEntryPoints()} through
 * explicit call statements or through calls to {@link Thread#start()}.
 * 
 * This class is designed to be thread safe, and subclasses of this class must be designed
 * in a thread-safe way, too.
 */
@ThreadSafe
public class JimpleBasedInterproceduralCFG implements InterproceduralCFG<Unit,SootMethod> {
	
	//retains only callers that are explicit call sites or Thread.start()
	protected static class EdgeFilter extends Filter {		
		protected EdgeFilter() {
			super(new EdgePredicate() {
				public boolean want(Edge e) {				
					return e.kind().isExplicit() || e.kind().isThread();
				}
			});
		}
	}
	
	@DontSynchronize("readonly")
	protected final CallGraph cg;
	
	@DontSynchronize("written by single thread; read afterwards")
	protected final Map<Unit,Body> unitToOwner = new HashMap<Unit,Body>();	
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Body,DirectedGraph<Unit>> bodyToUnitGraph =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Body,DirectedGraph<Unit>>() {
				public DirectedGraph<Unit> load(Body body) throws Exception {
					return makeGraph(body);
				}
			});
	
	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<Unit,Set<SootMethod>> unitToCallees =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Unit,Set<SootMethod>>() {
				public Set<SootMethod> load(Unit u) throws Exception {
					Set<SootMethod> res = new LinkedHashSet<SootMethod>();
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(u));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						if(edge.getTgt()==null) {
							System.err.println();
						}
						SootMethod m = edge.getTgt().method();
						if(m.hasActiveBody())
						res.add(m);
					}
					return res; 
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallers =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();					
					//only retain callers that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesInto(m));					
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	@SynchronizedBy("by use of synchronized LoadingCache class")
	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallsFromWithin =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {
				public Set<Unit> load(SootMethod m) throws Exception {
					Set<Unit> res = new LinkedHashSet<Unit>();
					//only retain calls that are explicit call sites or Thread.start()
					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(m));
					while(edgeIter.hasNext()) {
						Edge edge = edgeIter.next();
						res.add(edge.srcUnit());			
					}
					return res;
				}
			});

	
	public JimpleBasedInterproceduralCFG() {
		cg = Scene.v().getCallGraph();
		
		List<MethodOrMethodContext> eps = new ArrayList<MethodOrMethodContext>();
		eps.addAll(Scene.v().getEntryPoints());
		ReachableMethods reachableMethods = new ReachableMethods(cg, eps.iterator(), new EdgeFilter());
		reachableMethods.update();
		
		for(Iterator<MethodOrMethodContext> iter = reachableMethods.listener(); iter.hasNext(); ) {
			SootMethod m = iter.next().method();
			if(m.hasActiveBody()) {
				Body b = m.getActiveBody();
				PatchingChain<Unit> units = b.getUnits();
				for (Unit unit : units) {
					unitToOwner.put(unit, b);
				}
			}
		}
	}

	@Override
	public SootMethod getMethodOf(Unit u) {
		return unitToOwner.get(u).getMethod();
	}

	@Override
	public List<Unit> getSuccsOf(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getSuccsOf(u);
	}

	private DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {
		return bodyToUnitGraph.getUnchecked(body);
	}

	protected synchronized DirectedGraph<Unit> makeGraph(Body body) {
		return new ExceptionalUnitGraph(body, UnitThrowAnalysis.v() ,true);
	}

	@Override
	public Set<SootMethod> getCalleesOfCallAt(Unit u) {
		return unitToCallees.getUnchecked(u);
	}

	@Override
	public List<Unit> getReturnSitesOfCallAt(Unit u) {
		return getSuccsOf(u);
	}

	@Override
	public boolean isCallStmt(Unit u) {
		return ((Stmt)u).containsInvokeExpr();
	}

	@Override
	public boolean isExitStmt(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
		return unitGraph.getTails().contains(u);
	}

	@Override
	public Set<Unit> getCallersOf(SootMethod m) {
		return methodToCallers.getUnchecked(m);
	}
	
	@Override
	public Set<Unit> getCallsFromWithin(SootMethod m) {
		return methodToCallsFromWithin.getUnchecked(m);		
	}

	@Override
	public Set<Unit> getStartPointsOf(SootMethod m) {
		if(m.hasActiveBody()) {
			Body body = m.getActiveBody();
			DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
			return new LinkedHashSet<Unit>(unitGraph.getHeads());
		}
		return null;
	}

	@Override
	public boolean isStartPoint(Unit u) {
		Body body = unitToOwner.get(u);
		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);		
		return unitGraph.getHeads().contains(u);
	}

	@Override
	//TODO do we need to replace call by return for backwards analysis?
	public Set<Unit> allNonCallStartNodes() {
		Set<Unit> res = new LinkedHashSet<Unit>(unitToOwner.keySet());
		for (Iterator<Unit> iter = res.iterator(); iter.hasNext();) {
			Unit u = iter.next();
			if(isStartPoint(u) || isCallStmt(u)) iter.remove();
		}
		return res;
	}

	@Override
	public boolean isFallThroughSuccessor(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.fallsThrough()) return false;
		Body body = unitToOwner.get(u);
		return body.getUnits().getSuccOf(u) == succ;
	}

	@Override
	public boolean isBranchTarget(Unit u, Unit succ) {
		assert getSuccsOf(u).contains(succ);
		if(!u.branches()) return false;
		for (UnitBox ub : succ.getUnitBoxes()) {
			if(ub.getUnit()==succ) return true;
		}
		return false;
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
package de.bodden.ide.template;packagede.bodden.ide.template;



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

27

28

29

30
import java.util.ArrayList;importjava.util.ArrayList;import java.util.HashMap;importjava.util.HashMap;import java.util.Iterator;importjava.util.Iterator;import java.util.LinkedHashSet;importjava.util.LinkedHashSet;import java.util.List;importjava.util.List;import java.util.Map;importjava.util.Map;import java.util.Set;importjava.util.Set;import soot.Body;importsoot.Body;import soot.MethodOrMethodContext;importsoot.MethodOrMethodContext;import soot.PatchingChain;importsoot.PatchingChain;import soot.Scene;importsoot.Scene;import soot.SootMethod;importsoot.SootMethod;import soot.Unit;importsoot.Unit;import soot.UnitBox;importsoot.UnitBox;import soot.jimple.Stmt;importsoot.jimple.Stmt;import soot.jimple.toolkits.callgraph.CallGraph;importsoot.jimple.toolkits.callgraph.CallGraph;import soot.jimple.toolkits.callgraph.Edge;importsoot.jimple.toolkits.callgraph.Edge;import soot.jimple.toolkits.callgraph.EdgePredicate;importsoot.jimple.toolkits.callgraph.EdgePredicate;import soot.jimple.toolkits.callgraph.Filter;importsoot.jimple.toolkits.callgraph.Filter;import soot.jimple.toolkits.callgraph.ReachableMethods;importsoot.jimple.toolkits.callgraph.ReachableMethods;import soot.toolkits.exceptions.UnitThrowAnalysis;importsoot.toolkits.exceptions.UnitThrowAnalysis;import soot.toolkits.graph.DirectedGraph;importsoot.toolkits.graph.DirectedGraph;import soot.toolkits.graph.ExceptionalUnitGraph;importsoot.toolkits.graph.ExceptionalUnitGraph;import com.google.common.cache.CacheLoader;importcom.google.common.cache.CacheLoader;import com.google.common.cache.LoadingCache;importcom.google.common.cache.LoadingCache;



renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

31

32

33

34

35

36
import de.bodden.ide.DontSynchronize;importde.bodden.ide.DontSynchronize;import de.bodden.ide.InterproceduralCFG;importde.bodden.ide.InterproceduralCFG;import de.bodden.ide.SynchronizedBy;importde.bodden.ide.SynchronizedBy;import de.bodden.ide.ThreadSafe;importde.bodden.ide.ThreadSafe;import de.bodden.ide.solver.IDESolver;importde.bodden.ide.solver.IDESolver;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
/**/** * Default implementation for the {@link InterproceduralCFG} interface. * Default implementation for the {@link InterproceduralCFG} interface. * Includes all statements reachable from {@link Scene#getEntryPoints()} through * Includes all statements reachable from {@link Scene#getEntryPoints()} through * explicit call statements or through calls to {@link Thread#start()}. * explicit call statements or through calls to {@link Thread#start()}. *  *  * This class is designed to be thread safe, and subclasses of this class must be designed * This class is designed to be thread safe, and subclasses of this class must be designed * in a thread-safe way, too. * in a thread-safe way, too. */ */@ThreadSafe@ThreadSafepublic class JimpleBasedInterproceduralCFG implements InterproceduralCFG<Unit,SootMethod> {publicclassJimpleBasedInterproceduralCFGimplementsInterproceduralCFG<Unit,SootMethod>{		//retains only callers that are explicit call sites or Thread.start()//retains only callers that are explicit call sites or Thread.start()	protected static class EdgeFilter extends Filter {		protectedstaticclassEdgeFilterextendsFilter{		protected EdgeFilter() {protectedEdgeFilter(){			super(new EdgePredicate() {super(newEdgePredicate(){				public boolean want(Edge e) {				publicbooleanwant(Edgee){					return e.kind().isExplicit() || e.kind().isThread();returne.kind().isExplicit()||e.kind().isThread();				}}			});});		}}	}}		@DontSynchronize("readonly")@DontSynchronize("readonly")	protected final CallGraph cg;protectedfinalCallGraphcg;		@DontSynchronize("written by single thread; read afterwards")@DontSynchronize("written by single thread; read afterwards")	protected final Map<Unit,Body> unitToOwner = new HashMap<Unit,Body>();	protectedfinalMap<Unit,Body>unitToOwner=newHashMap<Unit,Body>();		@SynchronizedBy("by use of synchronized LoadingCache class")@SynchronizedBy("by use of synchronized LoadingCache class")	protected final LoadingCache<Body,DirectedGraph<Unit>> bodyToUnitGraph =protectedfinalLoadingCache<Body,DirectedGraph<Unit>>bodyToUnitGraph=			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Body,DirectedGraph<Unit>>() {IDESolver.DEFAULT_CACHE_BUILDER.build(newCacheLoader<Body,DirectedGraph<Unit>>(){				public DirectedGraph<Unit> load(Body body) throws Exception {publicDirectedGraph<Unit>load(Bodybody)throwsException{					return makeGraph(body);returnmakeGraph(body);				}}			});});		@SynchronizedBy("by use of synchronized LoadingCache class")@SynchronizedBy("by use of synchronized LoadingCache class")	protected final LoadingCache<Unit,Set<SootMethod>> unitToCallees =protectedfinalLoadingCache<Unit,Set<SootMethod>>unitToCallees=			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Unit,Set<SootMethod>>() {IDESolver.DEFAULT_CACHE_BUILDER.build(newCacheLoader<Unit,Set<SootMethod>>(){				public Set<SootMethod> load(Unit u) throws Exception {publicSet<SootMethod>load(Unitu)throwsException{					Set<SootMethod> res = new LinkedHashSet<SootMethod>();Set<SootMethod>res=newLinkedHashSet<SootMethod>();					//only retain callers that are explicit call sites or Thread.start()//only retain callers that are explicit call sites or Thread.start()					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(u));					Iterator<Edge>edgeIter=newEdgeFilter().wrap(cg.edgesOutOf(u));					while(edgeIter.hasNext()) {while(edgeIter.hasNext()){						Edge edge = edgeIter.next();Edgeedge=edgeIter.next();						if(edge.getTgt()==null) {if(edge.getTgt()==null){							System.err.println();System.err.println();						}}						SootMethod m = edge.getTgt().method();SootMethodm=edge.getTgt().method();						if(m.hasActiveBody())if(m.hasActiveBody())						res.add(m);res.add(m);					}}					return res; returnres;				}}			});});	@SynchronizedBy("by use of synchronized LoadingCache class")@SynchronizedBy("by use of synchronized LoadingCache class")	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallers =protectedfinalLoadingCache<SootMethod,Set<Unit>>methodToCallers=			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {IDESolver.DEFAULT_CACHE_BUILDER.build(newCacheLoader<SootMethod,Set<Unit>>(){				public Set<Unit> load(SootMethod m) throws Exception {publicSet<Unit>load(SootMethodm)throwsException{					Set<Unit> res = new LinkedHashSet<Unit>();					Set<Unit>res=newLinkedHashSet<Unit>();					//only retain callers that are explicit call sites or Thread.start()//only retain callers that are explicit call sites or Thread.start()					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesInto(m));					Iterator<Edge>edgeIter=newEdgeFilter().wrap(cg.edgesInto(m));					while(edgeIter.hasNext()) {while(edgeIter.hasNext()){						Edge edge = edgeIter.next();Edgeedge=edgeIter.next();						res.add(edge.srcUnit());			res.add(edge.srcUnit());					}}					return res;returnres;				}}			});});	@SynchronizedBy("by use of synchronized LoadingCache class")@SynchronizedBy("by use of synchronized LoadingCache class")	protected final LoadingCache<SootMethod,Set<Unit>> methodToCallsFromWithin =protectedfinalLoadingCache<SootMethod,Set<Unit>>methodToCallsFromWithin=			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<SootMethod,Set<Unit>>() {IDESolver.DEFAULT_CACHE_BUILDER.build(newCacheLoader<SootMethod,Set<Unit>>(){				public Set<Unit> load(SootMethod m) throws Exception {publicSet<Unit>load(SootMethodm)throwsException{					Set<Unit> res = new LinkedHashSet<Unit>();Set<Unit>res=newLinkedHashSet<Unit>();					//only retain calls that are explicit call sites or Thread.start()//only retain calls that are explicit call sites or Thread.start()					Iterator<Edge> edgeIter = new EdgeFilter().wrap(cg.edgesOutOf(m));Iterator<Edge>edgeIter=newEdgeFilter().wrap(cg.edgesOutOf(m));					while(edgeIter.hasNext()) {while(edgeIter.hasNext()){						Edge edge = edgeIter.next();Edgeedge=edgeIter.next();						res.add(edge.srcUnit());			res.add(edge.srcUnit());					}}					return res;returnres;				}}			});});		public JimpleBasedInterproceduralCFG() {publicJimpleBasedInterproceduralCFG(){		cg = Scene.v().getCallGraph();cg=Scene.v().getCallGraph();				List<MethodOrMethodContext> eps = new ArrayList<MethodOrMethodContext>();List<MethodOrMethodContext>eps=newArrayList<MethodOrMethodContext>();		eps.addAll(Scene.v().getEntryPoints());eps.addAll(Scene.v().getEntryPoints());		ReachableMethods reachableMethods = new ReachableMethods(cg, eps.iterator(), new EdgeFilter());ReachableMethodsreachableMethods=newReachableMethods(cg,eps.iterator(),newEdgeFilter());		reachableMethods.update();reachableMethods.update();				for(Iterator<MethodOrMethodContext> iter = reachableMethods.listener(); iter.hasNext(); ) {for(Iterator<MethodOrMethodContext>iter=reachableMethods.listener();iter.hasNext();){			SootMethod m = iter.next().method();SootMethodm=iter.next().method();			if(m.hasActiveBody()) {if(m.hasActiveBody()){				Body b = m.getActiveBody();Bodyb=m.getActiveBody();				PatchingChain<Unit> units = b.getUnits();PatchingChain<Unit>units=b.getUnits();				for (Unit unit : units) {for(Unitunit:units){					unitToOwner.put(unit, b);unitToOwner.put(unit,b);				}}			}}		}}	}}	@Override@Override	public SootMethod getMethodOf(Unit u) {publicSootMethodgetMethodOf(Unitu){		return unitToOwner.get(u).getMethod();returnunitToOwner.get(u).getMethod();	}}	@Override@Override	public List<Unit> getSuccsOf(Unit u) {publicList<Unit>getSuccsOf(Unitu){		Body body = unitToOwner.get(u);Bodybody=unitToOwner.get(u);		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);DirectedGraph<Unit>unitGraph=getOrCreateUnitGraph(body);		return unitGraph.getSuccsOf(u);returnunitGraph.getSuccsOf(u);	}}	private DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {privateDirectedGraph<Unit>getOrCreateUnitGraph(Bodybody){		return bodyToUnitGraph.getUnchecked(body);returnbodyToUnitGraph.getUnchecked(body);	}}	protected synchronized DirectedGraph<Unit> makeGraph(Body body) {protectedsynchronizedDirectedGraph<Unit>makeGraph(Bodybody){		return new ExceptionalUnitGraph(body, UnitThrowAnalysis.v() ,true);returnnewExceptionalUnitGraph(body,UnitThrowAnalysis.v(),true);	}}	@Override@Override	public Set<SootMethod> getCalleesOfCallAt(Unit u) {publicSet<SootMethod>getCalleesOfCallAt(Unitu){		return unitToCallees.getUnchecked(u);returnunitToCallees.getUnchecked(u);	}}	@Override@Override	public List<Unit> getReturnSitesOfCallAt(Unit u) {publicList<Unit>getReturnSitesOfCallAt(Unitu){		return getSuccsOf(u);returngetSuccsOf(u);	}}	@Override@Override	public boolean isCallStmt(Unit u) {publicbooleanisCallStmt(Unitu){		return ((Stmt)u).containsInvokeExpr();return((Stmt)u).containsInvokeExpr();	}}	@Override@Override	public boolean isExitStmt(Unit u) {publicbooleanisExitStmt(Unitu){		Body body = unitToOwner.get(u);Bodybody=unitToOwner.get(u);		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);DirectedGraph<Unit>unitGraph=getOrCreateUnitGraph(body);		return unitGraph.getTails().contains(u);returnunitGraph.getTails().contains(u);	}}	@Override@Override	public Set<Unit> getCallersOf(SootMethod m) {publicSet<Unit>getCallersOf(SootMethodm){		return methodToCallers.getUnchecked(m);returnmethodToCallers.getUnchecked(m);	}}		@Override@Override	public Set<Unit> getCallsFromWithin(SootMethod m) {publicSet<Unit>getCallsFromWithin(SootMethodm){		return methodToCallsFromWithin.getUnchecked(m);		returnmethodToCallsFromWithin.getUnchecked(m);	}}	@Override@Override	public Set<Unit> getStartPointsOf(SootMethod m) {publicSet<Unit>getStartPointsOf(SootMethodm){		if(m.hasActiveBody()) {if(m.hasActiveBody()){			Body body = m.getActiveBody();Bodybody=m.getActiveBody();			DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);DirectedGraph<Unit>unitGraph=getOrCreateUnitGraph(body);			return new LinkedHashSet<Unit>(unitGraph.getHeads());returnnewLinkedHashSet<Unit>(unitGraph.getHeads());		}}		return null;returnnull;	}}	@Override@Override	public boolean isStartPoint(Unit u) {publicbooleanisStartPoint(Unitu){		Body body = unitToOwner.get(u);Bodybody=unitToOwner.get(u);		DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);		DirectedGraph<Unit>unitGraph=getOrCreateUnitGraph(body);		return unitGraph.getHeads().contains(u);returnunitGraph.getHeads().contains(u);	}}	@Override@Override	//TODO do we need to replace call by return for backwards analysis?//TODO do we need to replace call by return for backwards analysis?	public Set<Unit> allNonCallStartNodes() {publicSet<Unit>allNonCallStartNodes(){		Set<Unit> res = new LinkedHashSet<Unit>(unitToOwner.keySet());Set<Unit>res=newLinkedHashSet<Unit>(unitToOwner.keySet());		for (Iterator<Unit> iter = res.iterator(); iter.hasNext();) {for(Iterator<Unit>iter=res.iterator();iter.hasNext();){			Unit u = iter.next();Unitu=iter.next();			if(isStartPoint(u) || isCallStmt(u)) iter.remove();if(isStartPoint(u)||isCallStmt(u))iter.remove();		}}		return res;returnres;	}}	@Override@Override	public boolean isFallThroughSuccessor(Unit u, Unit succ) {publicbooleanisFallThroughSuccessor(Unitu,Unitsucc){		assert getSuccsOf(u).contains(succ);assertgetSuccsOf(u).contains(succ);		if(!u.fallsThrough()) return false;if(!u.fallsThrough())returnfalse;		Body body = unitToOwner.get(u);Bodybody=unitToOwner.get(u);		return body.getUnits().getSuccOf(u) == succ;returnbody.getUnits().getSuccOf(u)==succ;	}}	@Override@Override	public boolean isBranchTarget(Unit u, Unit succ) {publicbooleanisBranchTarget(Unitu,Unitsucc){		assert getSuccsOf(u).contains(succ);assertgetSuccsOf(u).contains(succ);		if(!u.branches()) return false;if(!u.branches())returnfalse;		for (UnitBox ub : succ.getUnitBoxes()) {for(UnitBoxub:succ.getUnitBoxes()){			if(ub.getUnit()==succ) return true;if(ub.getUnit()==succ)returntrue;		}}		return false;returnfalse;	}}}}





