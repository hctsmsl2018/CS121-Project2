



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

ec76098bf485690bdc897f06846af4c20060d829

















ec76098bf485690bdc897f06846af4c20060d829


Switch branch/tag










heros


test


heros


alias


TestHelper.java



Find file
Normal viewHistoryPermalink






TestHelper.java



16.6 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;

import static org.junit.Assert.assertTrue;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






14




import heros.alias.FlowFunction.AnnotatedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






15




16




17




import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






18




import heros.solver.Pair;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

public class TestHelper {

	private Multimap<Method, Statement> method2startPoint = HashMultimap.create();
	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();
	private Map<Statement, Method> stmt2method = Maps.newHashMap();
	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();

	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new Method(methodName));
		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}

	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Statement(startingPoints[i]);
		}
		return result;
	}

	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






65




		return new EdgeBuilder.NormalStmtBuilder(new Statement(stmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {
		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));
	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {
		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));
	}
	
	public static Statement over(String callSite) {
		return new Statement(callSite);
	}
	
	public static Statement to(String returnSite) {
		return new Statement(returnSite);
	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {
		return new ExpectedFlowFunction(times, new Fact(source));
	}










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




93




	public static Pair<String, String> readField(String fieldName) {
		return new Pair<String, String>(fieldName, null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






96




97




	public static Pair<String, String> writeField(String fieldName) {
		return new Pair<String, String>(null, fieldName);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






98




99




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






100




	public static ExpectedFlowFunction flow(String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






101




102




103




		return flow(1, source, fieldAccess, targets);
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






104




	public static ExpectedFlowFunction flow(int times, String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






105




		AnnotatedFact<Fact>[] targetFacts = new AnnotatedFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






106




		for(int i=0; i<targets.length; i++) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






107




			targetFacts[i] = new AnnotatedFact<Fact>(new Fact(targets[i]), fieldAccess.getO1(), fieldAccess.getO2());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






108




109




110




111




		}
		return new ExpectedFlowFunction(times, new Fact(source), targetFacts);
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






112




113




114




115




116




	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






117




		return flow(times, source, new Pair<String, String>(null, null), targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






118




119




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	public static int times(int times) {
		return times;
	}

	public InterproceduralCFG<Statement, Method> buildIcfg() {
		return new InterproceduralCFG<Statement, Method>() {

			@Override
			public boolean isStartPoint(Statement stmt) {
				return method2startPoint.values().contains(stmt);
			}

			@Override
			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public boolean isExitStmt(Statement stmt) {
				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override
			public boolean isCallStmt(final Statement stmt) {
				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override
			public boolean isBranchTarget(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override
			public Collection<Statement> getStartPointsOf(Method m) {
				return method2startPoint.get(m);
			}

			@Override
			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();
				for (Call2ReturnEdge edge : call2retEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				for(ReturnEdge edge : returnEdges) {
					if(edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				return result;
			}

			@Override
			public Method getMethodOf(Statement n) {
				return stmt2method.get(n);
			}

			@Override
			public Set<Statement> getCallsFromWithin(Method m) {
				throw new IllegalStateException();
			}

			@Override
			public Collection<Statement> getCallersOf(Method m) {
				Set<Statement> result = Sets.newHashSet();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				for (ReturnEdge edge : returnEdges) {
					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				return result;
			}

			@Override
			public Collection<Method> getCalleesOfCallAt(Statement n) {
				List<Method> result = Lists.newLinkedList();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override
			public Set<Statement> allNonCallStartNodes() {
				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}

	private void addOrVerifyStmt2Method(Statement stmt, Method m) {
		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}

	public MethodHelper method(Method method) {
		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {

		private Method method;

		public MethodHelper(Method method) {
			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {
					remainingFlowFunctions.add(ff, ff.times);
				}
				
				edge.accept(new EdgeVisitor() {
					@Override
					public void visit(ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.exitStmt, method);
						edge.calleeMethod = method;
						returnEdges.add(edge);
					}
					
					@Override
					public void visit(Call2ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						addOrVerifyStmt2Method(edge.returnSite, method);
						call2retEdges.add(edge);
					}
					
					@Override
					public void visit(CallEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						callEdges.add(edge);
					}
					
					@Override
					public void visit(NormalEdge edge) {
						addOrVerifyStmt2Method(edge.unit, method);
						addOrVerifyStmt2Method(edge.succUnit, method);
						normalEdges.add(edge);
					}
				});
			}
		}

		public void startPoints(Statement[] startingPoints) {
			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	
	public static class ExpectedFlowFunction {

		public final Fact source;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






304




		public final AnnotatedFact<Fact>[] targets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






305




306




307




		public Edge edge;
		private int times;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






308




		public ExpectedFlowFunction(int times, Fact source, AnnotatedFact<Fact>... targets) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}
	}
	
	private static interface EdgeVisitor {
		void visit(NormalEdge edge);
		void visit(CallEdge edge);
		void visit(Call2ReturnEdge edge);
		void visit(ReturnEdge edge);
	}

	public static abstract class Edge {
		public final ExpectedFlowFunction[] flowFunctions;
		public boolean includeInCfg = true;

		public Edge(ExpectedFlowFunction...flowFunctions) {
			this.flowFunctions = flowFunctions;
			for(ExpectedFlowFunction ff : flowFunctions) {
				ff.edge = this;
			}
		}
		
		public abstract void accept(EdgeVisitor visitor);
	}

	public static class NormalEdge extends Edge {

		private Statement unit;
		private Statement succUnit;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






346




		public NormalEdge(Statement unit, Statement succUnit, ExpectedFlowFunction...flowFunctions) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




376




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




			super(flowFunctions);
			this.unit = unit;
			this.succUnit = succUnit;
		}

		@Override
		public String toString() {
			return String.format("%s -normal-> %s", unit, succUnit);
		}

		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class CallEdge extends Edge {

		private Statement callSite;
		private Method destinationMethod;

		public CallEdge(Statement callSite, Method destinationMethod, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.destinationMethod = destinationMethod;
		}

		@Override
		public String toString() {
			return String.format("%s -call-> %s", callSite, destinationMethod);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class Call2ReturnEdge extends Edge {
		private Statement callSite;
		private Statement returnSite;

		public Call2ReturnEdge(Statement callSite, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.returnSite = returnSite;
		}

		@Override
		public String toString() {
			return String.format("%s -call2ret-> %s", callSite, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ReturnEdge extends Edge {

		private Statement exitStmt;
		private Statement returnSite;
		private Statement callSite;
		private Method calleeMethod;

		public ReturnEdge(Statement callSite, Statement exitStmt, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
			if(callSite == null || returnSite == null)
				includeInCfg = false;
		}

		@Override
		public String toString() {
			return String.format("%s -return-> %s", exitStmt, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}
	
	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}

	public FlowFunctions<Statement, Fact, Method> flowFunctions() {
		return new FlowFunctions<Statement, Fact, Method>() {

			@Override
			public FlowFunction<Fact> getReturnFlowFunction(Statement callSite, Method calleeMethod, Statement exitStmt, Statement returnSite) {
				for (final ReturnEdge edge : returnEdges) {
					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)
							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,
						returnSite, callSite, calleeMethod));
			}

			@Override
			public FlowFunction<Fact> getNormalFlowFunction(final Statement curr, final Statement succ) {
				for (final NormalEdge edge : normalEdges) {
					if (edge.unit.equals(curr) && edge.succUnit.equals(succ)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for %s -> %s", curr, succ));
			}

			@Override
			public FlowFunction<Fact> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {
				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override
			public FlowFunction<Fact> getCallFlowFunction(Statement callStmt, Method destinationMethod) {
				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}

			private FlowFunction<Fact> createFlowFunction(final Edge edge) {
				return new FlowFunction<Fact>() {
					@Override









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






488




489




490




					public Set<AnnotatedFact<Fact>> computeTargets(Fact source) {
						Set<AnnotatedFact<Fact>> result = Sets.newHashSet();
						boolean found = false;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






491




492




493




						for (ExpectedFlowFunction ff : edge.flowFunctions) {
							if (ff.source.equals(source)) {
								if (remainingFlowFunctions.remove(ff)) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






494




495




									result.addAll(Sets.newHashSet(ff.targets));
									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






496




497




498




499




500




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






501




502




503




504




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {
		FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>> solver = new FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>>(createTabulationProblem(followReturnsPastSeeds, initialSeeds));
		solver.solve();
		assertAllFlowFunctionsUsed();
	}
	
	
	private IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, Method> icfg = buildIcfg();
		final FlowFunctions<Statement, Fact, Method> flowFunctions = flowFunctions();
		
		return new IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>>() {

			@Override
			public boolean followReturnsPastSeeds() {
				return followReturnsPastSeeds;
			}

			@Override
			public boolean autoAddZero() {
				return false;
			}

			@Override
			public int numThreads() {
				return 1;
			}

			@Override
			public boolean computeValues() {
				return false;
			}

			@Override
			public FlowFunctions<Statement, Fact, Method> flowFunctions() {
				return flowFunctions;
			}

			@Override
			public InterproceduralCFG<Statement, Method> interproceduralCFG() {
				return icfg;
			}

			@Override
			public Map<Statement, Set<Fact>> initialSeeds() {
				Map<Statement, Set<Fact>> result = Maps.newHashMap();
				for (String stmt : initialSeeds) {
					result.put(new Statement(stmt), Sets.newHashSet(new Fact("0")));
				}
				return result;
			}

			@Override
			public Fact zeroValue() {
				return new Fact("0");
			}
		};
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

ec76098bf485690bdc897f06846af4c20060d829

















ec76098bf485690bdc897f06846af4c20060d829


Switch branch/tag










heros


test


heros


alias


TestHelper.java



Find file
Normal viewHistoryPermalink






TestHelper.java



16.6 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;

import static org.junit.Assert.assertTrue;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






14




import heros.alias.FlowFunction.AnnotatedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






15




16




17




import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






18




import heros.solver.Pair;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

public class TestHelper {

	private Multimap<Method, Statement> method2startPoint = HashMultimap.create();
	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();
	private Map<Statement, Method> stmt2method = Maps.newHashMap();
	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();

	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new Method(methodName));
		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}

	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Statement(startingPoints[i]);
		}
		return result;
	}

	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






65




		return new EdgeBuilder.NormalStmtBuilder(new Statement(stmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {
		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));
	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {
		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));
	}
	
	public static Statement over(String callSite) {
		return new Statement(callSite);
	}
	
	public static Statement to(String returnSite) {
		return new Statement(returnSite);
	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {
		return new ExpectedFlowFunction(times, new Fact(source));
	}










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




93




	public static Pair<String, String> readField(String fieldName) {
		return new Pair<String, String>(fieldName, null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






96




97




	public static Pair<String, String> writeField(String fieldName) {
		return new Pair<String, String>(null, fieldName);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






98




99




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






100




	public static ExpectedFlowFunction flow(String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






101




102




103




		return flow(1, source, fieldAccess, targets);
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






104




	public static ExpectedFlowFunction flow(int times, String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






105




		AnnotatedFact<Fact>[] targetFacts = new AnnotatedFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






106




		for(int i=0; i<targets.length; i++) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






107




			targetFacts[i] = new AnnotatedFact<Fact>(new Fact(targets[i]), fieldAccess.getO1(), fieldAccess.getO2());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






108




109




110




111




		}
		return new ExpectedFlowFunction(times, new Fact(source), targetFacts);
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






112




113




114




115




116




	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






117




		return flow(times, source, new Pair<String, String>(null, null), targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






118




119




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	public static int times(int times) {
		return times;
	}

	public InterproceduralCFG<Statement, Method> buildIcfg() {
		return new InterproceduralCFG<Statement, Method>() {

			@Override
			public boolean isStartPoint(Statement stmt) {
				return method2startPoint.values().contains(stmt);
			}

			@Override
			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public boolean isExitStmt(Statement stmt) {
				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override
			public boolean isCallStmt(final Statement stmt) {
				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override
			public boolean isBranchTarget(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override
			public Collection<Statement> getStartPointsOf(Method m) {
				return method2startPoint.get(m);
			}

			@Override
			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();
				for (Call2ReturnEdge edge : call2retEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				for(ReturnEdge edge : returnEdges) {
					if(edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				return result;
			}

			@Override
			public Method getMethodOf(Statement n) {
				return stmt2method.get(n);
			}

			@Override
			public Set<Statement> getCallsFromWithin(Method m) {
				throw new IllegalStateException();
			}

			@Override
			public Collection<Statement> getCallersOf(Method m) {
				Set<Statement> result = Sets.newHashSet();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				for (ReturnEdge edge : returnEdges) {
					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				return result;
			}

			@Override
			public Collection<Method> getCalleesOfCallAt(Statement n) {
				List<Method> result = Lists.newLinkedList();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override
			public Set<Statement> allNonCallStartNodes() {
				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}

	private void addOrVerifyStmt2Method(Statement stmt, Method m) {
		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}

	public MethodHelper method(Method method) {
		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {

		private Method method;

		public MethodHelper(Method method) {
			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {
					remainingFlowFunctions.add(ff, ff.times);
				}
				
				edge.accept(new EdgeVisitor() {
					@Override
					public void visit(ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.exitStmt, method);
						edge.calleeMethod = method;
						returnEdges.add(edge);
					}
					
					@Override
					public void visit(Call2ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						addOrVerifyStmt2Method(edge.returnSite, method);
						call2retEdges.add(edge);
					}
					
					@Override
					public void visit(CallEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						callEdges.add(edge);
					}
					
					@Override
					public void visit(NormalEdge edge) {
						addOrVerifyStmt2Method(edge.unit, method);
						addOrVerifyStmt2Method(edge.succUnit, method);
						normalEdges.add(edge);
					}
				});
			}
		}

		public void startPoints(Statement[] startingPoints) {
			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	
	public static class ExpectedFlowFunction {

		public final Fact source;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






304




		public final AnnotatedFact<Fact>[] targets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






305




306




307




		public Edge edge;
		private int times;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






308




		public ExpectedFlowFunction(int times, Fact source, AnnotatedFact<Fact>... targets) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}
	}
	
	private static interface EdgeVisitor {
		void visit(NormalEdge edge);
		void visit(CallEdge edge);
		void visit(Call2ReturnEdge edge);
		void visit(ReturnEdge edge);
	}

	public static abstract class Edge {
		public final ExpectedFlowFunction[] flowFunctions;
		public boolean includeInCfg = true;

		public Edge(ExpectedFlowFunction...flowFunctions) {
			this.flowFunctions = flowFunctions;
			for(ExpectedFlowFunction ff : flowFunctions) {
				ff.edge = this;
			}
		}
		
		public abstract void accept(EdgeVisitor visitor);
	}

	public static class NormalEdge extends Edge {

		private Statement unit;
		private Statement succUnit;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






346




		public NormalEdge(Statement unit, Statement succUnit, ExpectedFlowFunction...flowFunctions) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




376




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




			super(flowFunctions);
			this.unit = unit;
			this.succUnit = succUnit;
		}

		@Override
		public String toString() {
			return String.format("%s -normal-> %s", unit, succUnit);
		}

		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class CallEdge extends Edge {

		private Statement callSite;
		private Method destinationMethod;

		public CallEdge(Statement callSite, Method destinationMethod, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.destinationMethod = destinationMethod;
		}

		@Override
		public String toString() {
			return String.format("%s -call-> %s", callSite, destinationMethod);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class Call2ReturnEdge extends Edge {
		private Statement callSite;
		private Statement returnSite;

		public Call2ReturnEdge(Statement callSite, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.returnSite = returnSite;
		}

		@Override
		public String toString() {
			return String.format("%s -call2ret-> %s", callSite, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ReturnEdge extends Edge {

		private Statement exitStmt;
		private Statement returnSite;
		private Statement callSite;
		private Method calleeMethod;

		public ReturnEdge(Statement callSite, Statement exitStmt, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
			if(callSite == null || returnSite == null)
				includeInCfg = false;
		}

		@Override
		public String toString() {
			return String.format("%s -return-> %s", exitStmt, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}
	
	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}

	public FlowFunctions<Statement, Fact, Method> flowFunctions() {
		return new FlowFunctions<Statement, Fact, Method>() {

			@Override
			public FlowFunction<Fact> getReturnFlowFunction(Statement callSite, Method calleeMethod, Statement exitStmt, Statement returnSite) {
				for (final ReturnEdge edge : returnEdges) {
					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)
							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,
						returnSite, callSite, calleeMethod));
			}

			@Override
			public FlowFunction<Fact> getNormalFlowFunction(final Statement curr, final Statement succ) {
				for (final NormalEdge edge : normalEdges) {
					if (edge.unit.equals(curr) && edge.succUnit.equals(succ)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for %s -> %s", curr, succ));
			}

			@Override
			public FlowFunction<Fact> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {
				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override
			public FlowFunction<Fact> getCallFlowFunction(Statement callStmt, Method destinationMethod) {
				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}

			private FlowFunction<Fact> createFlowFunction(final Edge edge) {
				return new FlowFunction<Fact>() {
					@Override









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






488




489




490




					public Set<AnnotatedFact<Fact>> computeTargets(Fact source) {
						Set<AnnotatedFact<Fact>> result = Sets.newHashSet();
						boolean found = false;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






491




492




493




						for (ExpectedFlowFunction ff : edge.flowFunctions) {
							if (ff.source.equals(source)) {
								if (remainingFlowFunctions.remove(ff)) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






494




495




									result.addAll(Sets.newHashSet(ff.targets));
									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






496




497




498




499




500




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






501




502




503




504




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {
		FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>> solver = new FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>>(createTabulationProblem(followReturnsPastSeeds, initialSeeds));
		solver.solve();
		assertAllFlowFunctionsUsed();
	}
	
	
	private IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, Method> icfg = buildIcfg();
		final FlowFunctions<Statement, Fact, Method> flowFunctions = flowFunctions();
		
		return new IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>>() {

			@Override
			public boolean followReturnsPastSeeds() {
				return followReturnsPastSeeds;
			}

			@Override
			public boolean autoAddZero() {
				return false;
			}

			@Override
			public int numThreads() {
				return 1;
			}

			@Override
			public boolean computeValues() {
				return false;
			}

			@Override
			public FlowFunctions<Statement, Fact, Method> flowFunctions() {
				return flowFunctions;
			}

			@Override
			public InterproceduralCFG<Statement, Method> interproceduralCFG() {
				return icfg;
			}

			@Override
			public Map<Statement, Set<Fact>> initialSeeds() {
				Map<Statement, Set<Fact>> result = Maps.newHashMap();
				for (String stmt : initialSeeds) {
					result.put(new Statement(stmt), Sets.newHashSet(new Fact("0")));
				}
				return result;
			}

			@Override
			public Fact zeroValue() {
				return new Fact("0");
			}
		};
	}
}











Open sidebar



Joshua Garcia heros

ec76098bf485690bdc897f06846af4c20060d829







Open sidebar



Joshua Garcia heros

ec76098bf485690bdc897f06846af4c20060d829




Open sidebar

Joshua Garcia heros

ec76098bf485690bdc897f06846af4c20060d829


Joshua Garciaherosheros
ec76098bf485690bdc897f06846af4c20060d829










ec76098bf485690bdc897f06846af4c20060d829


Switch branch/tag










heros


test


heros


alias


TestHelper.java



Find file
Normal viewHistoryPermalink






TestHelper.java



16.6 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;

import static org.junit.Assert.assertTrue;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






14




import heros.alias.FlowFunction.AnnotatedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






15




16




17




import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






18




import heros.solver.Pair;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

public class TestHelper {

	private Multimap<Method, Statement> method2startPoint = HashMultimap.create();
	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();
	private Map<Statement, Method> stmt2method = Maps.newHashMap();
	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();

	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new Method(methodName));
		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}

	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Statement(startingPoints[i]);
		}
		return result;
	}

	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






65




		return new EdgeBuilder.NormalStmtBuilder(new Statement(stmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {
		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));
	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {
		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));
	}
	
	public static Statement over(String callSite) {
		return new Statement(callSite);
	}
	
	public static Statement to(String returnSite) {
		return new Statement(returnSite);
	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {
		return new ExpectedFlowFunction(times, new Fact(source));
	}










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




93




	public static Pair<String, String> readField(String fieldName) {
		return new Pair<String, String>(fieldName, null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






96




97




	public static Pair<String, String> writeField(String fieldName) {
		return new Pair<String, String>(null, fieldName);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






98




99




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






100




	public static ExpectedFlowFunction flow(String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






101




102




103




		return flow(1, source, fieldAccess, targets);
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






104




	public static ExpectedFlowFunction flow(int times, String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






105




		AnnotatedFact<Fact>[] targetFacts = new AnnotatedFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






106




		for(int i=0; i<targets.length; i++) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






107




			targetFacts[i] = new AnnotatedFact<Fact>(new Fact(targets[i]), fieldAccess.getO1(), fieldAccess.getO2());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






108




109




110




111




		}
		return new ExpectedFlowFunction(times, new Fact(source), targetFacts);
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






112




113




114




115




116




	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






117




		return flow(times, source, new Pair<String, String>(null, null), targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






118




119




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	public static int times(int times) {
		return times;
	}

	public InterproceduralCFG<Statement, Method> buildIcfg() {
		return new InterproceduralCFG<Statement, Method>() {

			@Override
			public boolean isStartPoint(Statement stmt) {
				return method2startPoint.values().contains(stmt);
			}

			@Override
			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public boolean isExitStmt(Statement stmt) {
				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override
			public boolean isCallStmt(final Statement stmt) {
				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override
			public boolean isBranchTarget(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override
			public Collection<Statement> getStartPointsOf(Method m) {
				return method2startPoint.get(m);
			}

			@Override
			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();
				for (Call2ReturnEdge edge : call2retEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				for(ReturnEdge edge : returnEdges) {
					if(edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				return result;
			}

			@Override
			public Method getMethodOf(Statement n) {
				return stmt2method.get(n);
			}

			@Override
			public Set<Statement> getCallsFromWithin(Method m) {
				throw new IllegalStateException();
			}

			@Override
			public Collection<Statement> getCallersOf(Method m) {
				Set<Statement> result = Sets.newHashSet();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				for (ReturnEdge edge : returnEdges) {
					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				return result;
			}

			@Override
			public Collection<Method> getCalleesOfCallAt(Statement n) {
				List<Method> result = Lists.newLinkedList();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override
			public Set<Statement> allNonCallStartNodes() {
				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}

	private void addOrVerifyStmt2Method(Statement stmt, Method m) {
		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}

	public MethodHelper method(Method method) {
		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {

		private Method method;

		public MethodHelper(Method method) {
			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {
					remainingFlowFunctions.add(ff, ff.times);
				}
				
				edge.accept(new EdgeVisitor() {
					@Override
					public void visit(ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.exitStmt, method);
						edge.calleeMethod = method;
						returnEdges.add(edge);
					}
					
					@Override
					public void visit(Call2ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						addOrVerifyStmt2Method(edge.returnSite, method);
						call2retEdges.add(edge);
					}
					
					@Override
					public void visit(CallEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						callEdges.add(edge);
					}
					
					@Override
					public void visit(NormalEdge edge) {
						addOrVerifyStmt2Method(edge.unit, method);
						addOrVerifyStmt2Method(edge.succUnit, method);
						normalEdges.add(edge);
					}
				});
			}
		}

		public void startPoints(Statement[] startingPoints) {
			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	
	public static class ExpectedFlowFunction {

		public final Fact source;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






304




		public final AnnotatedFact<Fact>[] targets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






305




306




307




		public Edge edge;
		private int times;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






308




		public ExpectedFlowFunction(int times, Fact source, AnnotatedFact<Fact>... targets) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}
	}
	
	private static interface EdgeVisitor {
		void visit(NormalEdge edge);
		void visit(CallEdge edge);
		void visit(Call2ReturnEdge edge);
		void visit(ReturnEdge edge);
	}

	public static abstract class Edge {
		public final ExpectedFlowFunction[] flowFunctions;
		public boolean includeInCfg = true;

		public Edge(ExpectedFlowFunction...flowFunctions) {
			this.flowFunctions = flowFunctions;
			for(ExpectedFlowFunction ff : flowFunctions) {
				ff.edge = this;
			}
		}
		
		public abstract void accept(EdgeVisitor visitor);
	}

	public static class NormalEdge extends Edge {

		private Statement unit;
		private Statement succUnit;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






346




		public NormalEdge(Statement unit, Statement succUnit, ExpectedFlowFunction...flowFunctions) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




376




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




			super(flowFunctions);
			this.unit = unit;
			this.succUnit = succUnit;
		}

		@Override
		public String toString() {
			return String.format("%s -normal-> %s", unit, succUnit);
		}

		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class CallEdge extends Edge {

		private Statement callSite;
		private Method destinationMethod;

		public CallEdge(Statement callSite, Method destinationMethod, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.destinationMethod = destinationMethod;
		}

		@Override
		public String toString() {
			return String.format("%s -call-> %s", callSite, destinationMethod);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class Call2ReturnEdge extends Edge {
		private Statement callSite;
		private Statement returnSite;

		public Call2ReturnEdge(Statement callSite, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.returnSite = returnSite;
		}

		@Override
		public String toString() {
			return String.format("%s -call2ret-> %s", callSite, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ReturnEdge extends Edge {

		private Statement exitStmt;
		private Statement returnSite;
		private Statement callSite;
		private Method calleeMethod;

		public ReturnEdge(Statement callSite, Statement exitStmt, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
			if(callSite == null || returnSite == null)
				includeInCfg = false;
		}

		@Override
		public String toString() {
			return String.format("%s -return-> %s", exitStmt, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}
	
	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}

	public FlowFunctions<Statement, Fact, Method> flowFunctions() {
		return new FlowFunctions<Statement, Fact, Method>() {

			@Override
			public FlowFunction<Fact> getReturnFlowFunction(Statement callSite, Method calleeMethod, Statement exitStmt, Statement returnSite) {
				for (final ReturnEdge edge : returnEdges) {
					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)
							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,
						returnSite, callSite, calleeMethod));
			}

			@Override
			public FlowFunction<Fact> getNormalFlowFunction(final Statement curr, final Statement succ) {
				for (final NormalEdge edge : normalEdges) {
					if (edge.unit.equals(curr) && edge.succUnit.equals(succ)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for %s -> %s", curr, succ));
			}

			@Override
			public FlowFunction<Fact> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {
				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override
			public FlowFunction<Fact> getCallFlowFunction(Statement callStmt, Method destinationMethod) {
				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}

			private FlowFunction<Fact> createFlowFunction(final Edge edge) {
				return new FlowFunction<Fact>() {
					@Override









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






488




489




490




					public Set<AnnotatedFact<Fact>> computeTargets(Fact source) {
						Set<AnnotatedFact<Fact>> result = Sets.newHashSet();
						boolean found = false;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






491




492




493




						for (ExpectedFlowFunction ff : edge.flowFunctions) {
							if (ff.source.equals(source)) {
								if (remainingFlowFunctions.remove(ff)) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






494




495




									result.addAll(Sets.newHashSet(ff.targets));
									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






496




497




498




499




500




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






501




502




503




504




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {
		FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>> solver = new FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>>(createTabulationProblem(followReturnsPastSeeds, initialSeeds));
		solver.solve();
		assertAllFlowFunctionsUsed();
	}
	
	
	private IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, Method> icfg = buildIcfg();
		final FlowFunctions<Statement, Fact, Method> flowFunctions = flowFunctions();
		
		return new IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>>() {

			@Override
			public boolean followReturnsPastSeeds() {
				return followReturnsPastSeeds;
			}

			@Override
			public boolean autoAddZero() {
				return false;
			}

			@Override
			public int numThreads() {
				return 1;
			}

			@Override
			public boolean computeValues() {
				return false;
			}

			@Override
			public FlowFunctions<Statement, Fact, Method> flowFunctions() {
				return flowFunctions;
			}

			@Override
			public InterproceduralCFG<Statement, Method> interproceduralCFG() {
				return icfg;
			}

			@Override
			public Map<Statement, Set<Fact>> initialSeeds() {
				Map<Statement, Set<Fact>> result = Maps.newHashMap();
				for (String stmt : initialSeeds) {
					result.put(new Statement(stmt), Sets.newHashSet(new Fact("0")));
				}
				return result;
			}

			@Override
			public Fact zeroValue() {
				return new Fact("0");
			}
		};
	}
}














ec76098bf485690bdc897f06846af4c20060d829


Switch branch/tag










heros


test


heros


alias


TestHelper.java



Find file
Normal viewHistoryPermalink






TestHelper.java



16.6 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;

import static org.junit.Assert.assertTrue;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






14




import heros.alias.FlowFunction.AnnotatedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






15




16




17




import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






18




import heros.solver.Pair;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

public class TestHelper {

	private Multimap<Method, Statement> method2startPoint = HashMultimap.create();
	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();
	private Map<Statement, Method> stmt2method = Maps.newHashMap();
	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();

	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new Method(methodName));
		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}

	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Statement(startingPoints[i]);
		}
		return result;
	}

	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






65




		return new EdgeBuilder.NormalStmtBuilder(new Statement(stmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {
		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));
	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {
		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));
	}
	
	public static Statement over(String callSite) {
		return new Statement(callSite);
	}
	
	public static Statement to(String returnSite) {
		return new Statement(returnSite);
	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {
		return new ExpectedFlowFunction(times, new Fact(source));
	}










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




93




	public static Pair<String, String> readField(String fieldName) {
		return new Pair<String, String>(fieldName, null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






96




97




	public static Pair<String, String> writeField(String fieldName) {
		return new Pair<String, String>(null, fieldName);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






98




99




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






100




	public static ExpectedFlowFunction flow(String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






101




102




103




		return flow(1, source, fieldAccess, targets);
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






104




	public static ExpectedFlowFunction flow(int times, String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






105




		AnnotatedFact<Fact>[] targetFacts = new AnnotatedFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






106




		for(int i=0; i<targets.length; i++) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






107




			targetFacts[i] = new AnnotatedFact<Fact>(new Fact(targets[i]), fieldAccess.getO1(), fieldAccess.getO2());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






108




109




110




111




		}
		return new ExpectedFlowFunction(times, new Fact(source), targetFacts);
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






112




113




114




115




116




	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






117




		return flow(times, source, new Pair<String, String>(null, null), targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






118




119




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	public static int times(int times) {
		return times;
	}

	public InterproceduralCFG<Statement, Method> buildIcfg() {
		return new InterproceduralCFG<Statement, Method>() {

			@Override
			public boolean isStartPoint(Statement stmt) {
				return method2startPoint.values().contains(stmt);
			}

			@Override
			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public boolean isExitStmt(Statement stmt) {
				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override
			public boolean isCallStmt(final Statement stmt) {
				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override
			public boolean isBranchTarget(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override
			public Collection<Statement> getStartPointsOf(Method m) {
				return method2startPoint.get(m);
			}

			@Override
			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();
				for (Call2ReturnEdge edge : call2retEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				for(ReturnEdge edge : returnEdges) {
					if(edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				return result;
			}

			@Override
			public Method getMethodOf(Statement n) {
				return stmt2method.get(n);
			}

			@Override
			public Set<Statement> getCallsFromWithin(Method m) {
				throw new IllegalStateException();
			}

			@Override
			public Collection<Statement> getCallersOf(Method m) {
				Set<Statement> result = Sets.newHashSet();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				for (ReturnEdge edge : returnEdges) {
					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				return result;
			}

			@Override
			public Collection<Method> getCalleesOfCallAt(Statement n) {
				List<Method> result = Lists.newLinkedList();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override
			public Set<Statement> allNonCallStartNodes() {
				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}

	private void addOrVerifyStmt2Method(Statement stmt, Method m) {
		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}

	public MethodHelper method(Method method) {
		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {

		private Method method;

		public MethodHelper(Method method) {
			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {
					remainingFlowFunctions.add(ff, ff.times);
				}
				
				edge.accept(new EdgeVisitor() {
					@Override
					public void visit(ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.exitStmt, method);
						edge.calleeMethod = method;
						returnEdges.add(edge);
					}
					
					@Override
					public void visit(Call2ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						addOrVerifyStmt2Method(edge.returnSite, method);
						call2retEdges.add(edge);
					}
					
					@Override
					public void visit(CallEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						callEdges.add(edge);
					}
					
					@Override
					public void visit(NormalEdge edge) {
						addOrVerifyStmt2Method(edge.unit, method);
						addOrVerifyStmt2Method(edge.succUnit, method);
						normalEdges.add(edge);
					}
				});
			}
		}

		public void startPoints(Statement[] startingPoints) {
			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	
	public static class ExpectedFlowFunction {

		public final Fact source;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






304




		public final AnnotatedFact<Fact>[] targets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






305




306




307




		public Edge edge;
		private int times;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






308




		public ExpectedFlowFunction(int times, Fact source, AnnotatedFact<Fact>... targets) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}
	}
	
	private static interface EdgeVisitor {
		void visit(NormalEdge edge);
		void visit(CallEdge edge);
		void visit(Call2ReturnEdge edge);
		void visit(ReturnEdge edge);
	}

	public static abstract class Edge {
		public final ExpectedFlowFunction[] flowFunctions;
		public boolean includeInCfg = true;

		public Edge(ExpectedFlowFunction...flowFunctions) {
			this.flowFunctions = flowFunctions;
			for(ExpectedFlowFunction ff : flowFunctions) {
				ff.edge = this;
			}
		}
		
		public abstract void accept(EdgeVisitor visitor);
	}

	public static class NormalEdge extends Edge {

		private Statement unit;
		private Statement succUnit;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






346




		public NormalEdge(Statement unit, Statement succUnit, ExpectedFlowFunction...flowFunctions) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




376




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




			super(flowFunctions);
			this.unit = unit;
			this.succUnit = succUnit;
		}

		@Override
		public String toString() {
			return String.format("%s -normal-> %s", unit, succUnit);
		}

		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class CallEdge extends Edge {

		private Statement callSite;
		private Method destinationMethod;

		public CallEdge(Statement callSite, Method destinationMethod, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.destinationMethod = destinationMethod;
		}

		@Override
		public String toString() {
			return String.format("%s -call-> %s", callSite, destinationMethod);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class Call2ReturnEdge extends Edge {
		private Statement callSite;
		private Statement returnSite;

		public Call2ReturnEdge(Statement callSite, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.returnSite = returnSite;
		}

		@Override
		public String toString() {
			return String.format("%s -call2ret-> %s", callSite, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ReturnEdge extends Edge {

		private Statement exitStmt;
		private Statement returnSite;
		private Statement callSite;
		private Method calleeMethod;

		public ReturnEdge(Statement callSite, Statement exitStmt, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
			if(callSite == null || returnSite == null)
				includeInCfg = false;
		}

		@Override
		public String toString() {
			return String.format("%s -return-> %s", exitStmt, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}
	
	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}

	public FlowFunctions<Statement, Fact, Method> flowFunctions() {
		return new FlowFunctions<Statement, Fact, Method>() {

			@Override
			public FlowFunction<Fact> getReturnFlowFunction(Statement callSite, Method calleeMethod, Statement exitStmt, Statement returnSite) {
				for (final ReturnEdge edge : returnEdges) {
					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)
							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,
						returnSite, callSite, calleeMethod));
			}

			@Override
			public FlowFunction<Fact> getNormalFlowFunction(final Statement curr, final Statement succ) {
				for (final NormalEdge edge : normalEdges) {
					if (edge.unit.equals(curr) && edge.succUnit.equals(succ)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for %s -> %s", curr, succ));
			}

			@Override
			public FlowFunction<Fact> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {
				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override
			public FlowFunction<Fact> getCallFlowFunction(Statement callStmt, Method destinationMethod) {
				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}

			private FlowFunction<Fact> createFlowFunction(final Edge edge) {
				return new FlowFunction<Fact>() {
					@Override









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






488




489




490




					public Set<AnnotatedFact<Fact>> computeTargets(Fact source) {
						Set<AnnotatedFact<Fact>> result = Sets.newHashSet();
						boolean found = false;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






491




492




493




						for (ExpectedFlowFunction ff : edge.flowFunctions) {
							if (ff.source.equals(source)) {
								if (remainingFlowFunctions.remove(ff)) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






494




495




									result.addAll(Sets.newHashSet(ff.targets));
									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






496




497




498




499




500




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






501




502




503




504




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {
		FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>> solver = new FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>>(createTabulationProblem(followReturnsPastSeeds, initialSeeds));
		solver.solve();
		assertAllFlowFunctionsUsed();
	}
	
	
	private IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, Method> icfg = buildIcfg();
		final FlowFunctions<Statement, Fact, Method> flowFunctions = flowFunctions();
		
		return new IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>>() {

			@Override
			public boolean followReturnsPastSeeds() {
				return followReturnsPastSeeds;
			}

			@Override
			public boolean autoAddZero() {
				return false;
			}

			@Override
			public int numThreads() {
				return 1;
			}

			@Override
			public boolean computeValues() {
				return false;
			}

			@Override
			public FlowFunctions<Statement, Fact, Method> flowFunctions() {
				return flowFunctions;
			}

			@Override
			public InterproceduralCFG<Statement, Method> interproceduralCFG() {
				return icfg;
			}

			@Override
			public Map<Statement, Set<Fact>> initialSeeds() {
				Map<Statement, Set<Fact>> result = Maps.newHashMap();
				for (String stmt : initialSeeds) {
					result.put(new Statement(stmt), Sets.newHashSet(new Fact("0")));
				}
				return result;
			}

			@Override
			public Fact zeroValue() {
				return new Fact("0");
			}
		};
	}
}










ec76098bf485690bdc897f06846af4c20060d829


Switch branch/tag










heros


test


heros


alias


TestHelper.java



Find file
Normal viewHistoryPermalink




ec76098bf485690bdc897f06846af4c20060d829


Switch branch/tag










heros


test


heros


alias


TestHelper.java





ec76098bf485690bdc897f06846af4c20060d829


Switch branch/tag








ec76098bf485690bdc897f06846af4c20060d829


Switch branch/tag





ec76098bf485690bdc897f06846af4c20060d829

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

alias

TestHelper.java
Find file
Normal viewHistoryPermalink




TestHelper.java



16.6 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;

import static org.junit.Assert.assertTrue;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






14




import heros.alias.FlowFunction.AnnotatedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






15




16




17




import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






18




import heros.solver.Pair;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

public class TestHelper {

	private Multimap<Method, Statement> method2startPoint = HashMultimap.create();
	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();
	private Map<Statement, Method> stmt2method = Maps.newHashMap();
	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();

	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new Method(methodName));
		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}

	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Statement(startingPoints[i]);
		}
		return result;
	}

	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






65




		return new EdgeBuilder.NormalStmtBuilder(new Statement(stmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {
		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));
	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {
		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));
	}
	
	public static Statement over(String callSite) {
		return new Statement(callSite);
	}
	
	public static Statement to(String returnSite) {
		return new Statement(returnSite);
	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {
		return new ExpectedFlowFunction(times, new Fact(source));
	}










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




93




	public static Pair<String, String> readField(String fieldName) {
		return new Pair<String, String>(fieldName, null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






96




97




	public static Pair<String, String> writeField(String fieldName) {
		return new Pair<String, String>(null, fieldName);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






98




99




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






100




	public static ExpectedFlowFunction flow(String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






101




102




103




		return flow(1, source, fieldAccess, targets);
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






104




	public static ExpectedFlowFunction flow(int times, String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






105




		AnnotatedFact<Fact>[] targetFacts = new AnnotatedFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






106




		for(int i=0; i<targets.length; i++) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






107




			targetFacts[i] = new AnnotatedFact<Fact>(new Fact(targets[i]), fieldAccess.getO1(), fieldAccess.getO2());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






108




109




110




111




		}
		return new ExpectedFlowFunction(times, new Fact(source), targetFacts);
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






112




113




114




115




116




	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






117




		return flow(times, source, new Pair<String, String>(null, null), targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






118




119




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	public static int times(int times) {
		return times;
	}

	public InterproceduralCFG<Statement, Method> buildIcfg() {
		return new InterproceduralCFG<Statement, Method>() {

			@Override
			public boolean isStartPoint(Statement stmt) {
				return method2startPoint.values().contains(stmt);
			}

			@Override
			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public boolean isExitStmt(Statement stmt) {
				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override
			public boolean isCallStmt(final Statement stmt) {
				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override
			public boolean isBranchTarget(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override
			public Collection<Statement> getStartPointsOf(Method m) {
				return method2startPoint.get(m);
			}

			@Override
			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();
				for (Call2ReturnEdge edge : call2retEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				for(ReturnEdge edge : returnEdges) {
					if(edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				return result;
			}

			@Override
			public Method getMethodOf(Statement n) {
				return stmt2method.get(n);
			}

			@Override
			public Set<Statement> getCallsFromWithin(Method m) {
				throw new IllegalStateException();
			}

			@Override
			public Collection<Statement> getCallersOf(Method m) {
				Set<Statement> result = Sets.newHashSet();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				for (ReturnEdge edge : returnEdges) {
					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				return result;
			}

			@Override
			public Collection<Method> getCalleesOfCallAt(Statement n) {
				List<Method> result = Lists.newLinkedList();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override
			public Set<Statement> allNonCallStartNodes() {
				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}

	private void addOrVerifyStmt2Method(Statement stmt, Method m) {
		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}

	public MethodHelper method(Method method) {
		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {

		private Method method;

		public MethodHelper(Method method) {
			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {
					remainingFlowFunctions.add(ff, ff.times);
				}
				
				edge.accept(new EdgeVisitor() {
					@Override
					public void visit(ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.exitStmt, method);
						edge.calleeMethod = method;
						returnEdges.add(edge);
					}
					
					@Override
					public void visit(Call2ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						addOrVerifyStmt2Method(edge.returnSite, method);
						call2retEdges.add(edge);
					}
					
					@Override
					public void visit(CallEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						callEdges.add(edge);
					}
					
					@Override
					public void visit(NormalEdge edge) {
						addOrVerifyStmt2Method(edge.unit, method);
						addOrVerifyStmt2Method(edge.succUnit, method);
						normalEdges.add(edge);
					}
				});
			}
		}

		public void startPoints(Statement[] startingPoints) {
			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	
	public static class ExpectedFlowFunction {

		public final Fact source;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






304




		public final AnnotatedFact<Fact>[] targets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






305




306




307




		public Edge edge;
		private int times;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






308




		public ExpectedFlowFunction(int times, Fact source, AnnotatedFact<Fact>... targets) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}
	}
	
	private static interface EdgeVisitor {
		void visit(NormalEdge edge);
		void visit(CallEdge edge);
		void visit(Call2ReturnEdge edge);
		void visit(ReturnEdge edge);
	}

	public static abstract class Edge {
		public final ExpectedFlowFunction[] flowFunctions;
		public boolean includeInCfg = true;

		public Edge(ExpectedFlowFunction...flowFunctions) {
			this.flowFunctions = flowFunctions;
			for(ExpectedFlowFunction ff : flowFunctions) {
				ff.edge = this;
			}
		}
		
		public abstract void accept(EdgeVisitor visitor);
	}

	public static class NormalEdge extends Edge {

		private Statement unit;
		private Statement succUnit;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






346




		public NormalEdge(Statement unit, Statement succUnit, ExpectedFlowFunction...flowFunctions) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




376




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




			super(flowFunctions);
			this.unit = unit;
			this.succUnit = succUnit;
		}

		@Override
		public String toString() {
			return String.format("%s -normal-> %s", unit, succUnit);
		}

		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class CallEdge extends Edge {

		private Statement callSite;
		private Method destinationMethod;

		public CallEdge(Statement callSite, Method destinationMethod, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.destinationMethod = destinationMethod;
		}

		@Override
		public String toString() {
			return String.format("%s -call-> %s", callSite, destinationMethod);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class Call2ReturnEdge extends Edge {
		private Statement callSite;
		private Statement returnSite;

		public Call2ReturnEdge(Statement callSite, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.returnSite = returnSite;
		}

		@Override
		public String toString() {
			return String.format("%s -call2ret-> %s", callSite, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ReturnEdge extends Edge {

		private Statement exitStmt;
		private Statement returnSite;
		private Statement callSite;
		private Method calleeMethod;

		public ReturnEdge(Statement callSite, Statement exitStmt, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
			if(callSite == null || returnSite == null)
				includeInCfg = false;
		}

		@Override
		public String toString() {
			return String.format("%s -return-> %s", exitStmt, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}
	
	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}

	public FlowFunctions<Statement, Fact, Method> flowFunctions() {
		return new FlowFunctions<Statement, Fact, Method>() {

			@Override
			public FlowFunction<Fact> getReturnFlowFunction(Statement callSite, Method calleeMethod, Statement exitStmt, Statement returnSite) {
				for (final ReturnEdge edge : returnEdges) {
					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)
							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,
						returnSite, callSite, calleeMethod));
			}

			@Override
			public FlowFunction<Fact> getNormalFlowFunction(final Statement curr, final Statement succ) {
				for (final NormalEdge edge : normalEdges) {
					if (edge.unit.equals(curr) && edge.succUnit.equals(succ)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for %s -> %s", curr, succ));
			}

			@Override
			public FlowFunction<Fact> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {
				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override
			public FlowFunction<Fact> getCallFlowFunction(Statement callStmt, Method destinationMethod) {
				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}

			private FlowFunction<Fact> createFlowFunction(final Edge edge) {
				return new FlowFunction<Fact>() {
					@Override









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






488




489




490




					public Set<AnnotatedFact<Fact>> computeTargets(Fact source) {
						Set<AnnotatedFact<Fact>> result = Sets.newHashSet();
						boolean found = false;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






491




492




493




						for (ExpectedFlowFunction ff : edge.flowFunctions) {
							if (ff.source.equals(source)) {
								if (remainingFlowFunctions.remove(ff)) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






494




495




									result.addAll(Sets.newHashSet(ff.targets));
									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






496




497




498




499




500




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






501




502




503




504




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {
		FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>> solver = new FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>>(createTabulationProblem(followReturnsPastSeeds, initialSeeds));
		solver.solve();
		assertAllFlowFunctionsUsed();
	}
	
	
	private IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, Method> icfg = buildIcfg();
		final FlowFunctions<Statement, Fact, Method> flowFunctions = flowFunctions();
		
		return new IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>>() {

			@Override
			public boolean followReturnsPastSeeds() {
				return followReturnsPastSeeds;
			}

			@Override
			public boolean autoAddZero() {
				return false;
			}

			@Override
			public int numThreads() {
				return 1;
			}

			@Override
			public boolean computeValues() {
				return false;
			}

			@Override
			public FlowFunctions<Statement, Fact, Method> flowFunctions() {
				return flowFunctions;
			}

			@Override
			public InterproceduralCFG<Statement, Method> interproceduralCFG() {
				return icfg;
			}

			@Override
			public Map<Statement, Set<Fact>> initialSeeds() {
				Map<Statement, Set<Fact>> result = Maps.newHashMap();
				for (String stmt : initialSeeds) {
					result.put(new Statement(stmt), Sets.newHashSet(new Fact("0")));
				}
				return result;
			}

			@Override
			public Fact zeroValue() {
				return new Fact("0");
			}
		};
	}
}








TestHelper.java



16.6 KB










TestHelper.java



16.6 KB









Newer










Older
NewerOlder







FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




/*******************************************************************************
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros.alias;

import static org.junit.Assert.assertTrue;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






14




import heros.alias.FlowFunction.AnnotatedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






15




16




17




import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






18




import heros.solver.Pair;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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





import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

public class TestHelper {

	private Multimap<Method, Statement> method2startPoint = HashMultimap.create();
	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();
	private Map<Statement, Method> stmt2method = Maps.newHashMap();
	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();

	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new Method(methodName));
		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}

	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Statement(startingPoints[i]);
		}
		return result;
	}

	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






65




		return new EdgeBuilder.NormalStmtBuilder(new Statement(stmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {
		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));
	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {
		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));
	}
	
	public static Statement over(String callSite) {
		return new Statement(callSite);
	}
	
	public static Statement to(String returnSite) {
		return new Statement(returnSite);
	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {
		return new ExpectedFlowFunction(times, new Fact(source));
	}










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




93




	public static Pair<String, String> readField(String fieldName) {
		return new Pair<String, String>(fieldName, null);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






96




97




	public static Pair<String, String> writeField(String fieldName) {
		return new Pair<String, String>(null, fieldName);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






98




99




	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






100




	public static ExpectedFlowFunction flow(String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






101




102




103




		return flow(1, source, fieldAccess, targets);
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






104




	public static ExpectedFlowFunction flow(int times, String source, Pair<String, String> fieldAccess, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






105




		AnnotatedFact<Fact>[] targetFacts = new AnnotatedFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






106




		for(int i=0; i<targets.length; i++) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






107




			targetFacts[i] = new AnnotatedFact<Fact>(new Fact(targets[i]), fieldAccess.getO1(), fieldAccess.getO2());









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






108




109




110




111




		}
		return new ExpectedFlowFunction(times, new Fact(source), targetFacts);
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






112




113




114




115




116




	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






117




		return flow(times, source, new Pair<String, String>(null, null), targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






118




119




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




	public static int times(int times) {
		return times;
	}

	public InterproceduralCFG<Statement, Method> buildIcfg() {
		return new InterproceduralCFG<Statement, Method>() {

			@Override
			public boolean isStartPoint(Statement stmt) {
				return method2startPoint.values().contains(stmt);
			}

			@Override
			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public boolean isExitStmt(Statement stmt) {
				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override
			public boolean isCallStmt(final Statement stmt) {
				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override
			public boolean isBranchTarget(Statement stmt, Statement succ) {
				throw new IllegalStateException();
			}

			@Override
			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override
			public Collection<Statement> getStartPointsOf(Method m) {
				return method2startPoint.get(m);
			}

			@Override
			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();
				for (Call2ReturnEdge edge : call2retEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				for(ReturnEdge edge : returnEdges) {
					if(edge.includeInCfg && edge.callSite.equals(n))
						result.add(edge.returnSite);
				}
				return result;
			}

			@Override
			public Method getMethodOf(Statement n) {
				return stmt2method.get(n);
			}

			@Override
			public Set<Statement> getCallsFromWithin(Method m) {
				throw new IllegalStateException();
			}

			@Override
			public Collection<Statement> getCallersOf(Method m) {
				Set<Statement> result = Sets.newHashSet();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				for (ReturnEdge edge : returnEdges) {
					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {
						result.add(edge.callSite);
					}
				}
				return result;
			}

			@Override
			public Collection<Method> getCalleesOfCallAt(Statement n) {
				List<Method> result = Lists.newLinkedList();
				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override
			public Set<Statement> allNonCallStartNodes() {
				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}

	private void addOrVerifyStmt2Method(Statement stmt, Method m) {
		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}

	public MethodHelper method(Method method) {
		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {

		private Method method;

		public MethodHelper(Method method) {
			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {
					remainingFlowFunctions.add(ff, ff.times);
				}
				
				edge.accept(new EdgeVisitor() {
					@Override
					public void visit(ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.exitStmt, method);
						edge.calleeMethod = method;
						returnEdges.add(edge);
					}
					
					@Override
					public void visit(Call2ReturnEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						addOrVerifyStmt2Method(edge.returnSite, method);
						call2retEdges.add(edge);
					}
					
					@Override
					public void visit(CallEdge edge) {
						addOrVerifyStmt2Method(edge.callSite, method);
						callEdges.add(edge);
					}
					
					@Override
					public void visit(NormalEdge edge) {
						addOrVerifyStmt2Method(edge.unit, method);
						addOrVerifyStmt2Method(edge.succUnit, method);
						normalEdges.add(edge);
					}
				});
			}
		}

		public void startPoints(Statement[] startingPoints) {
			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	
	public static class ExpectedFlowFunction {

		public final Fact source;









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






304




		public final AnnotatedFact<Fact>[] targets;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






305




306




307




		public Edge edge;
		private int times;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






308




		public ExpectedFlowFunction(int times, Fact source, AnnotatedFact<Fact>... targets) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}
	}
	
	private static interface EdgeVisitor {
		void visit(NormalEdge edge);
		void visit(CallEdge edge);
		void visit(Call2ReturnEdge edge);
		void visit(ReturnEdge edge);
	}

	public static abstract class Edge {
		public final ExpectedFlowFunction[] flowFunctions;
		public boolean includeInCfg = true;

		public Edge(ExpectedFlowFunction...flowFunctions) {
			this.flowFunctions = flowFunctions;
			for(ExpectedFlowFunction ff : flowFunctions) {
				ff.edge = this;
			}
		}
		
		public abstract void accept(EdgeVisitor visitor);
	}

	public static class NormalEdge extends Edge {

		private Statement unit;
		private Statement succUnit;










edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






346




		public NormalEdge(Statement unit, Statement succUnit, ExpectedFlowFunction...flowFunctions) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




376




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




			super(flowFunctions);
			this.unit = unit;
			this.succUnit = succUnit;
		}

		@Override
		public String toString() {
			return String.format("%s -normal-> %s", unit, succUnit);
		}

		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class CallEdge extends Edge {

		private Statement callSite;
		private Method destinationMethod;

		public CallEdge(Statement callSite, Method destinationMethod, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.destinationMethod = destinationMethod;
		}

		@Override
		public String toString() {
			return String.format("%s -call-> %s", callSite, destinationMethod);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class Call2ReturnEdge extends Edge {
		private Statement callSite;
		private Statement returnSite;

		public Call2ReturnEdge(Statement callSite, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.returnSite = returnSite;
		}

		@Override
		public String toString() {
			return String.format("%s -call2ret-> %s", callSite, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ReturnEdge extends Edge {

		private Statement exitStmt;
		private Statement returnSite;
		private Statement callSite;
		private Method calleeMethod;

		public ReturnEdge(Statement callSite, Statement exitStmt, Statement returnSite, ExpectedFlowFunction...flowFunctions) {
			super(flowFunctions);
			this.callSite = callSite;
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
			if(callSite == null || returnSite == null)
				includeInCfg = false;
		}

		@Override
		public String toString() {
			return String.format("%s -return-> %s", exitStmt, returnSite);
		}
		
		@Override
		public void accept(EdgeVisitor visitor) {
			visitor.visit(this);
		}
	}
	
	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}

	public FlowFunctions<Statement, Fact, Method> flowFunctions() {
		return new FlowFunctions<Statement, Fact, Method>() {

			@Override
			public FlowFunction<Fact> getReturnFlowFunction(Statement callSite, Method calleeMethod, Statement exitStmt, Statement returnSite) {
				for (final ReturnEdge edge : returnEdges) {
					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)
							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,
						returnSite, callSite, calleeMethod));
			}

			@Override
			public FlowFunction<Fact> getNormalFlowFunction(final Statement curr, final Statement succ) {
				for (final NormalEdge edge : normalEdges) {
					if (edge.unit.equals(curr) && edge.succUnit.equals(succ)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for %s -> %s", curr, succ));
			}

			@Override
			public FlowFunction<Fact> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {
				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override
			public FlowFunction<Fact> getCallFlowFunction(Statement callStmt, Method destinationMethod) {
				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}

			private FlowFunction<Fact> createFlowFunction(final Edge edge) {
				return new FlowFunction<Fact>() {
					@Override









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






488




489




490




					public Set<AnnotatedFact<Fact>> computeTargets(Fact source) {
						Set<AnnotatedFact<Fact>> result = Sets.newHashSet();
						boolean found = false;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






491




492




493




						for (ExpectedFlowFunction ff : edge.flowFunctions) {
							if (ff.source.equals(source)) {
								if (remainingFlowFunctions.remove(ff)) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






494




495




									result.addAll(Sets.newHashSet(ff.targets));
									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






496




497




498




499




500




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






501




502




503




504




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {
		FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>> solver = new FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>>(createTabulationProblem(followReturnsPastSeeds, initialSeeds));
		solver.solve();
		assertAllFlowFunctionsUsed();
	}
	
	
	private IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, Method> icfg = buildIcfg();
		final FlowFunctions<Statement, Fact, Method> flowFunctions = flowFunctions();
		
		return new IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>>() {

			@Override
			public boolean followReturnsPastSeeds() {
				return followReturnsPastSeeds;
			}

			@Override
			public boolean autoAddZero() {
				return false;
			}

			@Override
			public int numThreads() {
				return 1;
			}

			@Override
			public boolean computeValues() {
				return false;
			}

			@Override
			public FlowFunctions<Statement, Fact, Method> flowFunctions() {
				return flowFunctions;
			}

			@Override
			public InterproceduralCFG<Statement, Method> interproceduralCFG() {
				return icfg;
			}

			@Override
			public Map<Statement, Set<Fact>> initialSeeds() {
				Map<Statement, Set<Fact>> result = Maps.newHashMap();
				for (String stmt : initialSeeds) {
					result.put(new Statement(stmt), Sets.newHashSet(new Fact("0")));
				}
				return result;
			}

			@Override
			public Fact zeroValue() {
				return new Fact("0");
			}
		};
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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import static org.junit.Assert.assertTrue;importstaticorg.junit.Assert.assertTrue;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

14
import heros.alias.FlowFunction.AnnotatedFact;importheros.alias.FlowFunction.AnnotatedFact;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

15

16

17
import heros.alias.IFDSTabulationProblem;importheros.alias.IFDSTabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;import heros.solver.IFDSSolver;importheros.solver.IFDSSolver;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

18
import heros.solver.Pair;importheros.solver.Pair;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
import java.util.Collection;importjava.util.Collection;import java.util.LinkedList;importjava.util.LinkedList;import java.util.List;importjava.util.List;import java.util.Map;importjava.util.Map;import java.util.Set;importjava.util.Set;import com.google.common.base.Joiner;importcom.google.common.base.Joiner;import com.google.common.base.Predicate;importcom.google.common.base.Predicate;import com.google.common.collect.HashMultimap;importcom.google.common.collect.HashMultimap;import com.google.common.collect.HashMultiset;importcom.google.common.collect.HashMultiset;import com.google.common.collect.Iterables;importcom.google.common.collect.Iterables;import com.google.common.collect.Lists;importcom.google.common.collect.Lists;import com.google.common.collect.Maps;importcom.google.common.collect.Maps;import com.google.common.collect.Multimap;importcom.google.common.collect.Multimap;import com.google.common.collect.Multiset;importcom.google.common.collect.Multiset;import com.google.common.collect.Sets;importcom.google.common.collect.Sets;public class TestHelper {publicclassTestHelper{	private Multimap<Method, Statement> method2startPoint = HashMultimap.create();privateMultimap<Method,Statement>method2startPoint=HashMultimap.create();	private List<NormalEdge> normalEdges = Lists.newLinkedList();privateList<NormalEdge>normalEdges=Lists.newLinkedList();	private List<CallEdge> callEdges = Lists.newLinkedList();privateList<CallEdge>callEdges=Lists.newLinkedList();	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();privateList<Call2ReturnEdge>call2retEdges=Lists.newLinkedList();	private List<ReturnEdge> returnEdges = Lists.newLinkedList();privateList<ReturnEdge>returnEdges=Lists.newLinkedList();	private Map<Statement, Method> stmt2method = Maps.newHashMap();privateMap<Statement,Method>stmt2method=Maps.newHashMap();	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();privateMultiset<ExpectedFlowFunction>remainingFlowFunctions=HashMultiset.create();	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {publicMethodHelpermethod(StringmethodName,Statement[]startingPoints,EdgeBuilder...edgeBuilders){		MethodHelper methodHelper = new MethodHelper(new Method(methodName));MethodHelpermethodHelper=newMethodHelper(newMethod(methodName));		methodHelper.startPoints(startingPoints);methodHelper.startPoints(startingPoints);		for(EdgeBuilder edgeBuilder : edgeBuilders){for(EdgeBuilderedgeBuilder:edgeBuilders){			methodHelper.edges(edgeBuilder.edges());methodHelper.edges(edgeBuilder.edges());		}}		return methodHelper;returnmethodHelper;	}}	public static Statement[] startPoints(String... startingPoints) {publicstaticStatement[]startPoints(String...startingPoints){		Statement[] result = new Statement[startingPoints.length];Statement[]result=newStatement[startingPoints.length];		for (int i = 0; i < result.length; i++) {for(inti=0;i<result.length;i++){			result[i] = new Statement(startingPoints[i]);result[i]=newStatement(startingPoints[i]);		}}		return result;returnresult;	}}	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt) {publicstaticEdgeBuilder.NormalStmtBuildernormalStmt(Stringstmt){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

65
		return new EdgeBuilder.NormalStmtBuilder(new Statement(stmt));returnnewEdgeBuilder.NormalStmtBuilder(newStatement(stmt));



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
	}}		public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {publicstaticEdgeBuilder.CallSiteBuildercallSite(StringcallSite){		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));returnnewEdgeBuilder.CallSiteBuilder(newStatement(callSite));	}}		public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {publicstaticEdgeBuilder.ExitStmtBuilderexitStmt(StringexitStmt){		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));returnnewEdgeBuilder.ExitStmtBuilder(newStatement(exitStmt));	}}		public static Statement over(String callSite) {publicstaticStatementover(StringcallSite){		return new Statement(callSite);returnnewStatement(callSite);	}}		public static Statement to(String returnSite) {publicstaticStatementto(StringreturnSite){		return new Statement(returnSite);returnnewStatement(returnSite);	}}		public static ExpectedFlowFunction kill(String source) {publicstaticExpectedFlowFunctionkill(Stringsource){		return kill(1, source);returnkill(1,source);	}}		public static ExpectedFlowFunction kill(int times, String source) {publicstaticExpectedFlowFunctionkill(inttimes,Stringsource){		return new ExpectedFlowFunction(times, new Fact(source));returnnewExpectedFlowFunction(times,newFact(source));	}}



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

92

93
	public static Pair<String, String> readField(String fieldName) {publicstaticPair<String,String>readField(StringfieldName){		return new Pair<String, String>(fieldName, null);returnnewPair<String,String>(fieldName,null);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

94

95
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

96

97
	public static Pair<String, String> writeField(String fieldName) {publicstaticPair<String,String>writeField(StringfieldName){		return new Pair<String, String>(null, fieldName);returnnewPair<String,String>(null,fieldName);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

98

99
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

100
	public static ExpectedFlowFunction flow(String source, Pair<String, String> fieldAccess, String... targets) {publicstaticExpectedFlowFunctionflow(Stringsource,Pair<String,String>fieldAccess,String...targets){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

101

102

103
		return flow(1, source, fieldAccess, targets);returnflow(1,source,fieldAccess,targets);	}}	



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

104
	public static ExpectedFlowFunction flow(int times, String source, Pair<String, String> fieldAccess, String... targets) {publicstaticExpectedFlowFunctionflow(inttimes,Stringsource,Pair<String,String>fieldAccess,String...targets){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

105
		AnnotatedFact<Fact>[] targetFacts = new AnnotatedFact[targets.length];AnnotatedFact<Fact>[]targetFacts=newAnnotatedFact[targets.length];



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

106
		for(int i=0; i<targets.length; i++) {for(inti=0;i<targets.length;i++){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

107
			targetFacts[i] = new AnnotatedFact<Fact>(new Fact(targets[i]), fieldAccess.getO1(), fieldAccess.getO2());targetFacts[i]=newAnnotatedFact<Fact>(newFact(targets[i]),fieldAccess.getO1(),fieldAccess.getO2());



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

108

109

110

111
		}}		return new ExpectedFlowFunction(times, new Fact(source), targetFacts);returnnewExpectedFlowFunction(times,newFact(source),targetFacts);	}}	



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

112

113

114

115

116
	public static ExpectedFlowFunction flow(String source, String... targets) {publicstaticExpectedFlowFunctionflow(Stringsource,String...targets){		return flow(1, source, targets);returnflow(1,source,targets);	}}		public static ExpectedFlowFunction flow(int times, String source, String... targets) {publicstaticExpectedFlowFunctionflow(inttimes,Stringsource,String...targets){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

117
		return flow(times, source, new Pair<String, String>(null, null), targets);returnflow(times,source,newPair<String,String>(null,null),targets);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

118

119
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
	public static int times(int times) {publicstaticinttimes(inttimes){		return times;returntimes;	}}	public InterproceduralCFG<Statement, Method> buildIcfg() {publicInterproceduralCFG<Statement,Method>buildIcfg(){		return new InterproceduralCFG<Statement, Method>() {returnnewInterproceduralCFG<Statement,Method>(){			@Override@Override			public boolean isStartPoint(Statement stmt) {publicbooleanisStartPoint(Statementstmt){				return method2startPoint.values().contains(stmt);returnmethod2startPoint.values().contains(stmt);			}}			@Override@Override			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {publicbooleanisFallThroughSuccessor(Statementstmt,Statementsucc){				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override			public boolean isExitStmt(Statement stmt) {publicbooleanisExitStmt(Statementstmt){				for(ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if(edge.exitStmt.equals(stmt))if(edge.exitStmt.equals(stmt))						return true;returntrue;				}}				return false;returnfalse;			}}			@Override@Override			public boolean isCallStmt(final Statement stmt) {publicbooleanisCallStmt(finalStatementstmt){				return Iterables.any(callEdges, new Predicate<CallEdge>() {returnIterables.any(callEdges,newPredicate<CallEdge>(){					@Override@Override					public boolean apply(CallEdge edge) {publicbooleanapply(CallEdgeedge){						return edge.callSite.equals(stmt);returnedge.callSite.equals(stmt);					}}				});});			}}			@Override@Override			public boolean isBranchTarget(Statement stmt, Statement succ) {publicbooleanisBranchTarget(Statementstmt,Statementsucc){				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override			public List<Statement> getSuccsOf(Statement n) {publicList<Statement>getSuccsOf(Statementn){				LinkedList<Statement> result = Lists.newLinkedList();LinkedList<Statement>result=Lists.newLinkedList();				for (NormalEdge edge : normalEdges) {for(NormalEdgeedge:normalEdges){					if (edge.includeInCfg && edge.unit.equals(n))if(edge.includeInCfg&&edge.unit.equals(n))						result.add(edge.succUnit);result.add(edge.succUnit);				}}				return result;returnresult;			}}			@Override@Override			public Collection<Statement> getStartPointsOf(Method m) {publicCollection<Statement>getStartPointsOf(Methodm){				return method2startPoint.get(m);returnmethod2startPoint.get(m);			}}			@Override@Override			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {publicCollection<Statement>getReturnSitesOfCallAt(Statementn){				Set<Statement> result = Sets.newHashSet();Set<Statement>result=Sets.newHashSet();				for (Call2ReturnEdge edge : call2retEdges) {for(Call2ReturnEdgeedge:call2retEdges){					if (edge.includeInCfg && edge.callSite.equals(n))if(edge.includeInCfg&&edge.callSite.equals(n))						result.add(edge.returnSite);result.add(edge.returnSite);				}}				for(ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if(edge.includeInCfg && edge.callSite.equals(n))if(edge.includeInCfg&&edge.callSite.equals(n))						result.add(edge.returnSite);result.add(edge.returnSite);				}}				return result;returnresult;			}}			@Override@Override			public Method getMethodOf(Statement n) {publicMethodgetMethodOf(Statementn){				return stmt2method.get(n);returnstmt2method.get(n);			}}			@Override@Override			public Set<Statement> getCallsFromWithin(Method m) {publicSet<Statement>getCallsFromWithin(Methodm){				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override			public Collection<Statement> getCallersOf(Method m) {publicCollection<Statement>getCallersOf(Methodm){				Set<Statement> result = Sets.newHashSet();Set<Statement>result=Sets.newHashSet();				for (CallEdge edge : callEdges) {for(CallEdgeedge:callEdges){					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {if(edge.includeInCfg&&edge.destinationMethod.equals(m)){						result.add(edge.callSite);result.add(edge.callSite);					}}				}}				for (ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {if(edge.includeInCfg&&edge.calleeMethod.equals(m)){						result.add(edge.callSite);result.add(edge.callSite);					}}				}}				return result;returnresult;			}}			@Override@Override			public Collection<Method> getCalleesOfCallAt(Statement n) {publicCollection<Method>getCalleesOfCallAt(Statementn){				List<Method> result = Lists.newLinkedList();List<Method>result=Lists.newLinkedList();				for (CallEdge edge : callEdges) {for(CallEdgeedge:callEdges){					if (edge.includeInCfg && edge.callSite.equals(n)) {if(edge.includeInCfg&&edge.callSite.equals(n)){						result.add(edge.destinationMethod);result.add(edge.destinationMethod);					}}				}}				return result;returnresult;			}}			@Override@Override			public Set<Statement> allNonCallStartNodes() {publicSet<Statement>allNonCallStartNodes(){				throw new IllegalStateException();thrownewIllegalStateException();			}}		};};	}}	public void assertAllFlowFunctionsUsed() {publicvoidassertAllFlowFunctionsUsed(){		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),assertTrue("These Flow Functions were expected, but never used: \n"+Joiner.on(",\n").join(remainingFlowFunctions),				remainingFlowFunctions.isEmpty());remainingFlowFunctions.isEmpty());	}}	private void addOrVerifyStmt2Method(Statement stmt, Method m) {privatevoidaddOrVerifyStmt2Method(Statementstmt,Methodm){		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {if(stmt2method.containsKey(stmt)&&!stmt2method.get(stmt).equals(m)){			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));thrownewIllegalArgumentException("Statement "+stmt+" is used in multiple methods: "+m+" and "+stmt2method.get(stmt));		}}		stmt2method.put(stmt, m);stmt2method.put(stmt,m);	}}	public MethodHelper method(Method method) {publicMethodHelpermethod(Methodmethod){		MethodHelper h = new MethodHelper(method);MethodHelperh=newMethodHelper(method);		return h;returnh;	}}	public class MethodHelper {publicclassMethodHelper{		private Method method;privateMethodmethod;		public MethodHelper(Method method) {publicMethodHelper(Methodmethod){			this.method = method;this.method=method;		}}		public void edges(Collection<Edge> edges) {publicvoidedges(Collection<Edge>edges){			for(Edge edge : edges) {for(Edgeedge:edges){				for(ExpectedFlowFunction ff : edge.flowFunctions) {for(ExpectedFlowFunctionff:edge.flowFunctions){					remainingFlowFunctions.add(ff, ff.times);remainingFlowFunctions.add(ff,ff.times);				}}								edge.accept(new EdgeVisitor() {edge.accept(newEdgeVisitor(){					@Override@Override					public void visit(ReturnEdge edge) {publicvoidvisit(ReturnEdgeedge){						addOrVerifyStmt2Method(edge.exitStmt, method);addOrVerifyStmt2Method(edge.exitStmt,method);						edge.calleeMethod = method;edge.calleeMethod=method;						returnEdges.add(edge);returnEdges.add(edge);					}}										@Override@Override					public void visit(Call2ReturnEdge edge) {publicvoidvisit(Call2ReturnEdgeedge){						addOrVerifyStmt2Method(edge.callSite, method);addOrVerifyStmt2Method(edge.callSite,method);						addOrVerifyStmt2Method(edge.returnSite, method);addOrVerifyStmt2Method(edge.returnSite,method);						call2retEdges.add(edge);call2retEdges.add(edge);					}}										@Override@Override					public void visit(CallEdge edge) {publicvoidvisit(CallEdgeedge){						addOrVerifyStmt2Method(edge.callSite, method);addOrVerifyStmt2Method(edge.callSite,method);						callEdges.add(edge);callEdges.add(edge);					}}										@Override@Override					public void visit(NormalEdge edge) {publicvoidvisit(NormalEdgeedge){						addOrVerifyStmt2Method(edge.unit, method);addOrVerifyStmt2Method(edge.unit,method);						addOrVerifyStmt2Method(edge.succUnit, method);addOrVerifyStmt2Method(edge.succUnit,method);						normalEdges.add(edge);normalEdges.add(edge);					}}				});});			}}		}}		public void startPoints(Statement[] startingPoints) {publicvoidstartPoints(Statement[]startingPoints){			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));method2startPoint.putAll(method,Lists.newArrayList(startingPoints));		}}	}}		public static class ExpectedFlowFunction {publicstaticclassExpectedFlowFunction{		public final Fact source;publicfinalFactsource;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

304
		public final AnnotatedFact<Fact>[] targets;publicfinalAnnotatedFact<Fact>[]targets;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

305

306

307
		public Edge edge;publicEdgeedge;		private int times;privateinttimes;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

308
		public ExpectedFlowFunction(int times, Fact source, AnnotatedFact<Fact>... targets) {publicExpectedFlowFunction(inttimes,Factsource,AnnotatedFact<Fact>...targets){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
			this.times = times;this.times=times;			this.source = source;this.source=source;			this.targets = targets;this.targets=targets;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));returnString.format("%s: %s -> {%s}",edge,source,Joiner.on(",").join(targets));		}}	}}		private static interface EdgeVisitor {privatestaticinterfaceEdgeVisitor{		void visit(NormalEdge edge);voidvisit(NormalEdgeedge);		void visit(CallEdge edge);voidvisit(CallEdgeedge);		void visit(Call2ReturnEdge edge);voidvisit(Call2ReturnEdgeedge);		void visit(ReturnEdge edge);voidvisit(ReturnEdgeedge);	}}	public static abstract class Edge {publicstaticabstractclassEdge{		public final ExpectedFlowFunction[] flowFunctions;publicfinalExpectedFlowFunction[]flowFunctions;		public boolean includeInCfg = true;publicbooleanincludeInCfg=true;		public Edge(ExpectedFlowFunction...flowFunctions) {publicEdge(ExpectedFlowFunction...flowFunctions){			this.flowFunctions = flowFunctions;this.flowFunctions=flowFunctions;			for(ExpectedFlowFunction ff : flowFunctions) {for(ExpectedFlowFunctionff:flowFunctions){				ff.edge = this;ff.edge=this;			}}		}}				public abstract void accept(EdgeVisitor visitor);publicabstractvoidaccept(EdgeVisitorvisitor);	}}	public static class NormalEdge extends Edge {publicstaticclassNormalEdgeextendsEdge{		private Statement unit;privateStatementunit;		private Statement succUnit;privateStatementsuccUnit;



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

346
		public NormalEdge(Statement unit, Statement succUnit, ExpectedFlowFunction...flowFunctions) {publicNormalEdge(Statementunit,StatementsuccUnit,ExpectedFlowFunction...flowFunctions){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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

376

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
			super(flowFunctions);super(flowFunctions);			this.unit = unit;this.unit=unit;			this.succUnit = succUnit;this.succUnit=succUnit;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s -normal-> %s", unit, succUnit);returnString.format("%s -normal-> %s",unit,succUnit);		}}		@Override@Override		public void accept(EdgeVisitor visitor) {publicvoidaccept(EdgeVisitorvisitor){			visitor.visit(this);visitor.visit(this);		}}	}}	public static class CallEdge extends Edge {publicstaticclassCallEdgeextendsEdge{		private Statement callSite;privateStatementcallSite;		private Method destinationMethod;privateMethoddestinationMethod;		public CallEdge(Statement callSite, Method destinationMethod, ExpectedFlowFunction...flowFunctions) {publicCallEdge(StatementcallSite,MethoddestinationMethod,ExpectedFlowFunction...flowFunctions){			super(flowFunctions);super(flowFunctions);			this.callSite = callSite;this.callSite=callSite;			this.destinationMethod = destinationMethod;this.destinationMethod=destinationMethod;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s -call-> %s", callSite, destinationMethod);returnString.format("%s -call-> %s",callSite,destinationMethod);		}}				@Override@Override		public void accept(EdgeVisitor visitor) {publicvoidaccept(EdgeVisitorvisitor){			visitor.visit(this);visitor.visit(this);		}}	}}	public static class Call2ReturnEdge extends Edge {publicstaticclassCall2ReturnEdgeextendsEdge{		private Statement callSite;privateStatementcallSite;		private Statement returnSite;privateStatementreturnSite;		public Call2ReturnEdge(Statement callSite, Statement returnSite, ExpectedFlowFunction...flowFunctions) {publicCall2ReturnEdge(StatementcallSite,StatementreturnSite,ExpectedFlowFunction...flowFunctions){			super(flowFunctions);super(flowFunctions);			this.callSite = callSite;this.callSite=callSite;			this.returnSite = returnSite;this.returnSite=returnSite;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s -call2ret-> %s", callSite, returnSite);returnString.format("%s -call2ret-> %s",callSite,returnSite);		}}				@Override@Override		public void accept(EdgeVisitor visitor) {publicvoidaccept(EdgeVisitorvisitor){			visitor.visit(this);visitor.visit(this);		}}	}}	public static class ReturnEdge extends Edge {publicstaticclassReturnEdgeextendsEdge{		private Statement exitStmt;privateStatementexitStmt;		private Statement returnSite;privateStatementreturnSite;		private Statement callSite;privateStatementcallSite;		private Method calleeMethod;privateMethodcalleeMethod;		public ReturnEdge(Statement callSite, Statement exitStmt, Statement returnSite, ExpectedFlowFunction...flowFunctions) {publicReturnEdge(StatementcallSite,StatementexitStmt,StatementreturnSite,ExpectedFlowFunction...flowFunctions){			super(flowFunctions);super(flowFunctions);			this.callSite = callSite;this.callSite=callSite;			this.exitStmt = exitStmt;this.exitStmt=exitStmt;			this.returnSite = returnSite;this.returnSite=returnSite;			if(callSite == null || returnSite == null)if(callSite==null||returnSite==null)				includeInCfg = false;includeInCfg=false;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s -return-> %s", exitStmt, returnSite);returnString.format("%s -return-> %s",exitStmt,returnSite);		}}				@Override@Override		public void accept(EdgeVisitor visitor) {publicvoidaccept(EdgeVisitorvisitor){			visitor.visit(this);visitor.visit(this);		}}	}}		private static boolean nullAwareEquals(Object a, Object b) {privatestaticbooleannullAwareEquals(Objecta,Objectb){		if(a == null)if(a==null)			return b==null;returnb==null;		elseelse			return a.equals(b);returna.equals(b);	}}	public FlowFunctions<Statement, Fact, Method> flowFunctions() {publicFlowFunctions<Statement,Fact,Method>flowFunctions(){		return new FlowFunctions<Statement, Fact, Method>() {returnnewFlowFunctions<Statement,Fact,Method>(){			@Override@Override			public FlowFunction<Fact> getReturnFlowFunction(Statement callSite, Method calleeMethod, Statement exitStmt, Statement returnSite) {publicFlowFunction<Fact>getReturnFlowFunction(StatementcallSite,MethodcalleeMethod,StatementexitStmt,StatementreturnSite){				for (final ReturnEdge edge : returnEdges) {for(finalReturnEdgeedge:returnEdges){					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)if(nullAwareEquals(callSite,edge.callSite)&&edge.calleeMethod.equals(calleeMethod)							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {&&edge.exitStmt.equals(exitStmt)&&nullAwareEquals(edge.returnSite,returnSite)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,thrownewAssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)",exitStmt,						returnSite, callSite, calleeMethod));returnSite,callSite,calleeMethod));			}}			@Override@Override			public FlowFunction<Fact> getNormalFlowFunction(final Statement curr, final Statement succ) {publicFlowFunction<Fact>getNormalFlowFunction(finalStatementcurr,finalStatementsucc){				for (final NormalEdge edge : normalEdges) {for(finalNormalEdgeedge:normalEdges){					if (edge.unit.equals(curr) && edge.succUnit.equals(succ)) {if(edge.unit.equals(curr)&&edge.succUnit.equals(succ)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for %s -> %s", curr, succ));thrownewAssertionError(String.format("No Flow Function expected for %s -> %s",curr,succ));			}}			@Override@Override			public FlowFunction<Fact> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {publicFlowFunction<Fact>getCallToReturnFlowFunction(StatementcallSite,StatementreturnSite){				for (final Call2ReturnEdge edge : call2retEdges) {for(finalCall2ReturnEdgeedge:call2retEdges){					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {if(edge.callSite.equals(callSite)&&edge.returnSite.equals(returnSite)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));thrownewAssertionError(String.format("No Flow Function expected for call to return edge %s -> %s",callSite,returnSite));			}}			@Override@Override			public FlowFunction<Fact> getCallFlowFunction(Statement callStmt, Method destinationMethod) {publicFlowFunction<Fact>getCallFlowFunction(StatementcallStmt,MethoddestinationMethod){				for (final CallEdge edge : callEdges) {for(finalCallEdgeedge:callEdges){					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {if(edge.callSite.equals(callStmt)&&edge.destinationMethod.equals(destinationMethod)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));thrownewAssertionError(String.format("No Flow Function expected for call %s -> %s",callStmt,destinationMethod));			}}			private FlowFunction<Fact> createFlowFunction(final Edge edge) {privateFlowFunction<Fact>createFlowFunction(finalEdgeedge){				return new FlowFunction<Fact>() {returnnewFlowFunction<Fact>(){					@Override@Override



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

488

489

490
					public Set<AnnotatedFact<Fact>> computeTargets(Fact source) {publicSet<AnnotatedFact<Fact>>computeTargets(Factsource){						Set<AnnotatedFact<Fact>> result = Sets.newHashSet();Set<AnnotatedFact<Fact>>result=Sets.newHashSet();						boolean found = false;booleanfound=false;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

491

492

493
						for (ExpectedFlowFunction ff : edge.flowFunctions) {for(ExpectedFlowFunctionff:edge.flowFunctions){							if (ff.source.equals(source)) {if(ff.source.equals(source)){								if (remainingFlowFunctions.remove(ff)) {if(remainingFlowFunctions.remove(ff)){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

494

495
									result.addAll(Sets.newHashSet(ff.targets));result.addAll(Sets.newHashSet(ff.targets));									found = true;found=true;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

496

497

498

499

500
								} else {}else{									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));thrownewAssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'",ff,edge));								}}							}}						}}



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

501

502

503

504
						if(found)if(found)							return result;returnresult;						elseelse							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));thrownewAssertionError(String.format("Fact '%s' was not expected at edge '%s'",source,edge));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
					}}				};};			}}		};};	}}	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {publicvoidrunSolver(finalbooleanfollowReturnsPastSeeds,finalString...initialSeeds){		FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>> solver = new FieldSensitiveIFDSSolver<Statement, String,Fact, Method, InterproceduralCFG<Statement,Method>>(createTabulationProblem(followReturnsPastSeeds, initialSeeds));FieldSensitiveIFDSSolver<Statement,String,Fact,Method,InterproceduralCFG<Statement,Method>>solver=newFieldSensitiveIFDSSolver<Statement,String,Fact,Method,InterproceduralCFG<Statement,Method>>(createTabulationProblem(followReturnsPastSeeds,initialSeeds));		solver.solve();solver.solve();		assertAllFlowFunctionsUsed();assertAllFlowFunctionsUsed();	}}			private IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {privateIFDSTabulationProblem<Statement,Fact,Method,InterproceduralCFG<Statement,Method>>createTabulationProblem(finalbooleanfollowReturnsPastSeeds,finalString[]initialSeeds){		final InterproceduralCFG<Statement, Method> icfg = buildIcfg();finalInterproceduralCFG<Statement,Method>icfg=buildIcfg();		final FlowFunctions<Statement, Fact, Method> flowFunctions = flowFunctions();finalFlowFunctions<Statement,Fact,Method>flowFunctions=flowFunctions();				return new IFDSTabulationProblem<Statement, Fact, Method, InterproceduralCFG<Statement, Method>>() {returnnewIFDSTabulationProblem<Statement,Fact,Method,InterproceduralCFG<Statement,Method>>(){			@Override@Override			public boolean followReturnsPastSeeds() {publicbooleanfollowReturnsPastSeeds(){				return followReturnsPastSeeds;returnfollowReturnsPastSeeds;			}}			@Override@Override			public boolean autoAddZero() {publicbooleanautoAddZero(){				return false;returnfalse;			}}			@Override@Override			public int numThreads() {publicintnumThreads(){				return 1;return1;			}}			@Override@Override			public boolean computeValues() {publicbooleancomputeValues(){				return false;returnfalse;			}}			@Override@Override			public FlowFunctions<Statement, Fact, Method> flowFunctions() {publicFlowFunctions<Statement,Fact,Method>flowFunctions(){				return flowFunctions;returnflowFunctions;			}}			@Override@Override			public InterproceduralCFG<Statement, Method> interproceduralCFG() {publicInterproceduralCFG<Statement,Method>interproceduralCFG(){				return icfg;returnicfg;			}}			@Override@Override			public Map<Statement, Set<Fact>> initialSeeds() {publicMap<Statement,Set<Fact>>initialSeeds(){				Map<Statement, Set<Fact>> result = Maps.newHashMap();Map<Statement,Set<Fact>>result=Maps.newHashMap();				for (String stmt : initialSeeds) {for(Stringstmt:initialSeeds){					result.put(new Statement(stmt), Sets.newHashSet(new Fact("0")));result.put(newStatement(stmt),Sets.newHashSet(newFact("0")));				}}				return result;returnresult;			}}			@Override@Override			public Fact zeroValue() {publicFactzeroValue(){				return new Fact("0");returnnewFact("0");			}}		};};	}}}}





