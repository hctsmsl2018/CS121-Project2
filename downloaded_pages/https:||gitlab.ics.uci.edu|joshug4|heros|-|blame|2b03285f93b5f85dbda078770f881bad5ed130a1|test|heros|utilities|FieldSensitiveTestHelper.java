



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

2b03285f93b5f85dbda078770f881bad5ed130a1

















2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


test


heros


utilities


FieldSensitiveTestHelper.java



Find file
Normal viewHistoryPermalink






FieldSensitiveTestHelper.java



20.8 KB









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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






11




package heros.utilities;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






12




13




14





import static org.junit.Assert.assertTrue;
import heros.InterproceduralCFG;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






15




16




17




18




19




20




21




22




import heros.utilities.Edge.Call2ReturnEdge;
import heros.utilities.Edge.CallEdge;
import heros.utilities.Edge.EdgeVisitor;
import heros.utilities.Edge.NormalEdge;
import heros.utilities.Edge.ReturnEdge;
import heros.utilities.EdgeBuilder.CallSiteBuilder;
import heros.utilities.EdgeBuilder.ExitStmtBuilder;
import heros.utilities.EdgeBuilder.NormalStmtBuilder;









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






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




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.BiDiFieldSensitiveIFDSSolver;
import heros.fieldsens.FactMergeHandler;
import heros.fieldsens.FieldSensitiveIFDSSolver;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.FlowFunctions;
import heros.fieldsens.IFDSTabulationProblem;
import heros.fieldsens.Scheduler;
import heros.fieldsens.ZeroHandler;
import heros.fieldsens.FlowFunction.ConstrainedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






52




public class FieldSensitiveTestHelper {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






53














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






54




	private Multimap<TestMethod, Statement> method2startPoint = HashMultimap.create();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






55




56




57




58




	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






59




	private Map<Statement, TestMethod> stmt2method = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






60




	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






61




	private TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






62














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




	public FieldSensitiveTestHelper(TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






64




65




66




		this.debugger = debugger;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






67




	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






68




		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






69




70




71




72




73




74




75




		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






76




77




	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






78




		for (int i = 0; i < result.length; i++) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






79




			result[i] = new Statement(startingPoints[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






80




81




82




83




		}
		return result;
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






84




	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		return new NormalStmtBuilder(new Statement(stmt), flowFunctions);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






86




87




88




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






89




		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






90




91




92




	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






93




		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






96




97




	public static Statement over(String callSite) {
		return new Statement(callSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






100




101




	public static Statement to(String returnSite) {
		return new Statement(returnSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






102




103




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






104




	public static ExpectedFlowFunction<TestFact> kill(String source) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




		return kill(1, source);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






108




109




	public static ExpectedFlowFunction<TestFact> kill(int times, String source) {
		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






110




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






111




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






112




113




114




115




116




117




118




119




				throw new IllegalStateException();
			}
			
			@Override
			public String transformerString() {
				return "";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






120




121




	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






122




123




124




	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






125




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






126




				return accPathHandler.read(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






127




128




129




130




131




132




133




			}

			@Override
			public String toString() {
				return "read("+fieldName+")";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






134




135




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






136




137




138




	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






139




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






140




				return accPathHandler.prepend(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






141




142




143




144




145




146




147




			}
			
			@Override
			public String toString() {
				return "prepend("+fieldName+")";
			}
		};









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






148




149




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






150




151




152




	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






153




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






154




				return accPathHandler.overwrite(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






155




156




157




158




159




160




161




162




163




			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






164




	public static ExpectedFlowFunction<TestFact> flow(String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






165




		return flow(1, source, transformer, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






166




167




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






168




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






169




		TestFact[] targetFacts = new TestFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






170




		for(int i=0; i<targets.length; i++) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






171




			targetFacts[i] = new TestFact(targets[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






172




		}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






173




		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source), targetFacts) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






174




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






175




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






188




		ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler); 









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






189




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






190




191




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






192




	public static ExpectedFlowFunction<TestFact> flow(String source, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






193




194




195




		return flow(1, source, targets);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






196




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






197




198




		return flow(times, source, new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






199




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






200




201




202




203




204




205




206




207




208




				return accPathHandler.generate(target);
			}
			
			@Override
			public String toString() {
				return "";
			}
			
		}, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






209




210




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






211




212




213




214




	public static int times(int times) {
		return times;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






215




216




	public InterproceduralCFG<Statement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






217




218





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






219




			public boolean isStartPoint(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






220




221




222




223




				return method2startPoint.values().contains(stmt);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






224




			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




228




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






229




			public boolean isExitStmt(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






230




231




232




233




234




235




236




237




				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






238




			public boolean isCallStmt(final Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






239




240




241




242




243




244




245




246




247




				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






248




			public boolean isBranchTarget(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






249




250




251




252




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






253




254




			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






255




256




257




258




259




260




261




262




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






263




264




			public List<Statement> getPredsOf(Statement stmt) {
				LinkedList<Statement> result = Lists.newLinkedList();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






265




266




267




268




269




270




271




272




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






273




			public Collection<Statement> getStartPointsOf(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






274




275




276




277




				return method2startPoint.get(m);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






278




279




			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






292




			public TestMethod getMethodOf(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






293




294




295




296




				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






297




298




299




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






300




			public Set<Statement> getCallsFromWithin(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






301




302




303




304




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






305




306




			public Collection<Statement> getCallersOf(TestMethod m) {
				Set<Statement> result = Sets.newHashSet();









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




314




315




316




317




318




319




320




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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






321




			public Collection<TestMethod> getCalleesOfCallAt(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






322




				List<TestMethod> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






323




324




325




326




327




328




329




330




331




				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






332




			public Set<Statement> allNonCallStartNodes() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






343




	private void addOrVerifyStmt2Method(Statement stmt, TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






344




345




346




347




348




349




		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






350




	public MethodHelper method(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






351




352




353




354




355




356




		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






357




		private TestMethod method;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






358














rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






359




		public MethodHelper(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






360




361




362




363




364




			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






365




				for(ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






366




367




					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






401




		public void startPoints(Statement[] startingPoints) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






402




403




404




405




			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






406




	private static String expectedFlowFunctionsToString(ExpectedFlowFunction<TestFact>[] flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






407




		String result = "";









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






408




		for(ExpectedFlowFunction<TestFact> ff : flowFunctions)









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






409




410




411




412




			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






413




414




415




416




417




418




419




	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






420




421




	public FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<Statement, String, TestFact, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






422




423





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






424




			public FlowFunction<String, TestFact, Statement, TestMethod> getReturnFlowFunction(Statement callSite, TestMethod calleeMethod, Statement exitStmt, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






436




			public FlowFunction<String, TestFact, Statement, TestMethod> getNormalFlowFunction(final Statement curr) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






437




				for (final NormalEdge edge : normalEdges) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






438




					if (edge.unit.equals(curr)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






439




440




441




						return createFlowFunction(edge);
					}
				}









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






442




				throw new AssertionError(String.format("No Flow Function expected for %s", curr));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






443




444




445




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






446




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






447




448




449




450




451




452




453




454




455




				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






456




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallFlowFunction(Statement callStmt, TestMethod destinationMethod) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






457




458




459




460




461




462




463




464




				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






465




466




			private FlowFunction<String, TestFact, Statement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






467




					@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






468




469




470




					public Set<FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, Statement, TestMethod>> result = Sets.newHashSet();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






471




						boolean found = false;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






472




						for (ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






473




							if (ff.source.equals(source)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






474




								if (remainingFlowFunctions.remove(ff)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






475




476




									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));









abstract at return edges


 

 


Johannes Lerch
committed
Feb 23, 2015






477




									}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






478




									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






479




480




481




482




483




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




485




486




487




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






488




489




490




491




492




493




494




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






495




		Scheduler scheduler = new Scheduler();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






496




		FieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>>(









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






497




498




499




500




501




502




503




504




505




				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {
					@Override
					public void merge(TestFact previousFact, TestFact currentFact) {
					}

					@Override
					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
					}
					









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






506




				}, debugger, scheduler);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






507




		addExpectationsToDebugger();









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






508




		scheduler.runAndAwaitCompletion();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






509




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






510




511




512




		assertAllFlowFunctionsUsed();
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




	private void addExpectationsToDebugger() {
		for(NormalEdge edge : normalEdges) {
			debugger.expectNormalFlow(edge.unit, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(CallEdge edge : callEdges) {
			debugger.expectCallFlow(edge.callSite, edge.destinationMethod, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(Call2ReturnEdge edge : call2retEdges) {
			debugger.expectNormalFlow(edge.callSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(ReturnEdge edge : returnEdges) {
			debugger.expectReturnFlow(edge.exitStmt, edge.returnSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






528




529




530




	private IFDSTabulationProblem<Statement, String, TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, TestMethod> icfg = buildIcfg();
		final FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions = flowFunctions();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






531




		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






532




		return new IFDSTabulationProblem<Statement,String,  TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






555




			public FlowFunctions<Statement,String,  TestFact, TestMethod> flowFunctions() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






556




557




558




559




				return flowFunctions;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






560




			public InterproceduralCFG<Statement, TestMethod> interproceduralCFG() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






561




562




563




564




				return icfg;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






565




566




			public Map<Statement, Set<TestFact>> initialSeeds() {
				Map<Statement, Set<TestFact>> result = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






567




				for (String stmt : initialSeeds) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






568




					result.put(new Statement(stmt), Sets.newHashSet(new TestFact("0")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






569




570




571




572




573




				}
				return result;
			}

			@Override









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






574




575




576




577




578




			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






579




580




			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






581




					@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






582




					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






583




584




585




						return true;
					}
				};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






586




587




588




			}
		};
	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	public static enum TabulationProblemExchange {AsSpecified, ExchangeForwardAndBackward};
	public void runBiDiSolver(FieldSensitiveTestHelper backwardHelper, TabulationProblemExchange direction, final String...initialSeeds) {
		FactMergeHandler<TestFact> factMergeHandler = new FactMergeHandler<TestFact>() {
			@Override
			public void merge(TestFact previousFact, TestFact currentFact) {
			}

			@Override
			public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
			}
			
		};
		Scheduler scheduler = new Scheduler();
		BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> solver =
				direction == TabulationProblemExchange.AsSpecified ? 









switching to Java 6 compatibility


 

 


Johannes Lerch
committed
Jun 01, 2015






605




606




607




608




609




610




				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						createTabulationProblem(true, initialSeeds), 
						backwardHelper.createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler) :
				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						backwardHelper.createTabulationProblem(true, initialSeeds), 









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






611




612




613




614




615




616




617




						createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler);
		
		scheduler.runAndAwaitCompletion();
		assertAllFlowFunctionsUsed();
		backwardHelper.assertAllFlowFunctionsUsed();
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




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

2b03285f93b5f85dbda078770f881bad5ed130a1

















2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


test


heros


utilities


FieldSensitiveTestHelper.java



Find file
Normal viewHistoryPermalink






FieldSensitiveTestHelper.java



20.8 KB









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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






11




package heros.utilities;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






12




13




14





import static org.junit.Assert.assertTrue;
import heros.InterproceduralCFG;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






15




16




17




18




19




20




21




22




import heros.utilities.Edge.Call2ReturnEdge;
import heros.utilities.Edge.CallEdge;
import heros.utilities.Edge.EdgeVisitor;
import heros.utilities.Edge.NormalEdge;
import heros.utilities.Edge.ReturnEdge;
import heros.utilities.EdgeBuilder.CallSiteBuilder;
import heros.utilities.EdgeBuilder.ExitStmtBuilder;
import heros.utilities.EdgeBuilder.NormalStmtBuilder;









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






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




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.BiDiFieldSensitiveIFDSSolver;
import heros.fieldsens.FactMergeHandler;
import heros.fieldsens.FieldSensitiveIFDSSolver;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.FlowFunctions;
import heros.fieldsens.IFDSTabulationProblem;
import heros.fieldsens.Scheduler;
import heros.fieldsens.ZeroHandler;
import heros.fieldsens.FlowFunction.ConstrainedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






52




public class FieldSensitiveTestHelper {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






53














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






54




	private Multimap<TestMethod, Statement> method2startPoint = HashMultimap.create();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






55




56




57




58




	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






59




	private Map<Statement, TestMethod> stmt2method = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






60




	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






61




	private TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






62














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




	public FieldSensitiveTestHelper(TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






64




65




66




		this.debugger = debugger;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






67




	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






68




		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






69




70




71




72




73




74




75




		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






76




77




	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






78




		for (int i = 0; i < result.length; i++) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






79




			result[i] = new Statement(startingPoints[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






80




81




82




83




		}
		return result;
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






84




	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		return new NormalStmtBuilder(new Statement(stmt), flowFunctions);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






86




87




88




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






89




		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






90




91




92




	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






93




		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






96




97




	public static Statement over(String callSite) {
		return new Statement(callSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






100




101




	public static Statement to(String returnSite) {
		return new Statement(returnSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






102




103




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






104




	public static ExpectedFlowFunction<TestFact> kill(String source) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




		return kill(1, source);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






108




109




	public static ExpectedFlowFunction<TestFact> kill(int times, String source) {
		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






110




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






111




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






112




113




114




115




116




117




118




119




				throw new IllegalStateException();
			}
			
			@Override
			public String transformerString() {
				return "";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






120




121




	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






122




123




124




	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






125




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






126




				return accPathHandler.read(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






127




128




129




130




131




132




133




			}

			@Override
			public String toString() {
				return "read("+fieldName+")";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






134




135




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






136




137




138




	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






139




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






140




				return accPathHandler.prepend(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






141




142




143




144




145




146




147




			}
			
			@Override
			public String toString() {
				return "prepend("+fieldName+")";
			}
		};









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






148




149




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






150




151




152




	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






153




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






154




				return accPathHandler.overwrite(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






155




156




157




158




159




160




161




162




163




			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






164




	public static ExpectedFlowFunction<TestFact> flow(String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






165




		return flow(1, source, transformer, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






166




167




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






168




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






169




		TestFact[] targetFacts = new TestFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






170




		for(int i=0; i<targets.length; i++) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






171




			targetFacts[i] = new TestFact(targets[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






172




		}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






173




		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source), targetFacts) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






174




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






175




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






188




		ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler); 









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






189




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






190




191




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






192




	public static ExpectedFlowFunction<TestFact> flow(String source, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






193




194




195




		return flow(1, source, targets);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






196




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






197




198




		return flow(times, source, new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






199




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






200




201




202




203




204




205




206




207




208




				return accPathHandler.generate(target);
			}
			
			@Override
			public String toString() {
				return "";
			}
			
		}, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






209




210




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






211




212




213




214




	public static int times(int times) {
		return times;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






215




216




	public InterproceduralCFG<Statement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






217




218





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






219




			public boolean isStartPoint(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






220




221




222




223




				return method2startPoint.values().contains(stmt);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






224




			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




228




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






229




			public boolean isExitStmt(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






230




231




232




233




234




235




236




237




				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






238




			public boolean isCallStmt(final Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






239




240




241




242




243




244




245




246




247




				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






248




			public boolean isBranchTarget(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






249




250




251




252




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






253




254




			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






255




256




257




258




259




260




261




262




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






263




264




			public List<Statement> getPredsOf(Statement stmt) {
				LinkedList<Statement> result = Lists.newLinkedList();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






265




266




267




268




269




270




271




272




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






273




			public Collection<Statement> getStartPointsOf(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






274




275




276




277




				return method2startPoint.get(m);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






278




279




			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






292




			public TestMethod getMethodOf(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






293




294




295




296




				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






297




298




299




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






300




			public Set<Statement> getCallsFromWithin(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






301




302




303




304




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






305




306




			public Collection<Statement> getCallersOf(TestMethod m) {
				Set<Statement> result = Sets.newHashSet();









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




314




315




316




317




318




319




320




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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






321




			public Collection<TestMethod> getCalleesOfCallAt(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






322




				List<TestMethod> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






323




324




325




326




327




328




329




330




331




				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






332




			public Set<Statement> allNonCallStartNodes() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






343




	private void addOrVerifyStmt2Method(Statement stmt, TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






344




345




346




347




348




349




		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






350




	public MethodHelper method(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






351




352




353




354




355




356




		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






357




		private TestMethod method;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






358














rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






359




		public MethodHelper(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






360




361




362




363




364




			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






365




				for(ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






366




367




					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






401




		public void startPoints(Statement[] startingPoints) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






402




403




404




405




			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






406




	private static String expectedFlowFunctionsToString(ExpectedFlowFunction<TestFact>[] flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






407




		String result = "";









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






408




		for(ExpectedFlowFunction<TestFact> ff : flowFunctions)









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






409




410




411




412




			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






413




414




415




416




417




418




419




	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






420




421




	public FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<Statement, String, TestFact, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






422




423





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






424




			public FlowFunction<String, TestFact, Statement, TestMethod> getReturnFlowFunction(Statement callSite, TestMethod calleeMethod, Statement exitStmt, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






436




			public FlowFunction<String, TestFact, Statement, TestMethod> getNormalFlowFunction(final Statement curr) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






437




				for (final NormalEdge edge : normalEdges) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






438




					if (edge.unit.equals(curr)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






439




440




441




						return createFlowFunction(edge);
					}
				}









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






442




				throw new AssertionError(String.format("No Flow Function expected for %s", curr));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






443




444




445




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






446




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






447




448




449




450




451




452




453




454




455




				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






456




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallFlowFunction(Statement callStmt, TestMethod destinationMethod) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






457




458




459




460




461




462




463




464




				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






465




466




			private FlowFunction<String, TestFact, Statement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






467




					@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






468




469




470




					public Set<FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, Statement, TestMethod>> result = Sets.newHashSet();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






471




						boolean found = false;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






472




						for (ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






473




							if (ff.source.equals(source)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






474




								if (remainingFlowFunctions.remove(ff)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






475




476




									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));









abstract at return edges


 

 


Johannes Lerch
committed
Feb 23, 2015






477




									}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






478




									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






479




480




481




482




483




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




485




486




487




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






488




489




490




491




492




493




494




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






495




		Scheduler scheduler = new Scheduler();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






496




		FieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>>(









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






497




498




499




500




501




502




503




504




505




				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {
					@Override
					public void merge(TestFact previousFact, TestFact currentFact) {
					}

					@Override
					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
					}
					









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






506




				}, debugger, scheduler);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






507




		addExpectationsToDebugger();









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






508




		scheduler.runAndAwaitCompletion();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






509




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






510




511




512




		assertAllFlowFunctionsUsed();
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




	private void addExpectationsToDebugger() {
		for(NormalEdge edge : normalEdges) {
			debugger.expectNormalFlow(edge.unit, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(CallEdge edge : callEdges) {
			debugger.expectCallFlow(edge.callSite, edge.destinationMethod, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(Call2ReturnEdge edge : call2retEdges) {
			debugger.expectNormalFlow(edge.callSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(ReturnEdge edge : returnEdges) {
			debugger.expectReturnFlow(edge.exitStmt, edge.returnSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






528




529




530




	private IFDSTabulationProblem<Statement, String, TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, TestMethod> icfg = buildIcfg();
		final FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions = flowFunctions();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






531




		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






532




		return new IFDSTabulationProblem<Statement,String,  TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






555




			public FlowFunctions<Statement,String,  TestFact, TestMethod> flowFunctions() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






556




557




558




559




				return flowFunctions;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






560




			public InterproceduralCFG<Statement, TestMethod> interproceduralCFG() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






561




562




563




564




				return icfg;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






565




566




			public Map<Statement, Set<TestFact>> initialSeeds() {
				Map<Statement, Set<TestFact>> result = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






567




				for (String stmt : initialSeeds) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






568




					result.put(new Statement(stmt), Sets.newHashSet(new TestFact("0")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






569




570




571




572




573




				}
				return result;
			}

			@Override









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






574




575




576




577




578




			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






579




580




			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






581




					@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






582




					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






583




584




585




						return true;
					}
				};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






586




587




588




			}
		};
	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	public static enum TabulationProblemExchange {AsSpecified, ExchangeForwardAndBackward};
	public void runBiDiSolver(FieldSensitiveTestHelper backwardHelper, TabulationProblemExchange direction, final String...initialSeeds) {
		FactMergeHandler<TestFact> factMergeHandler = new FactMergeHandler<TestFact>() {
			@Override
			public void merge(TestFact previousFact, TestFact currentFact) {
			}

			@Override
			public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
			}
			
		};
		Scheduler scheduler = new Scheduler();
		BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> solver =
				direction == TabulationProblemExchange.AsSpecified ? 









switching to Java 6 compatibility


 

 


Johannes Lerch
committed
Jun 01, 2015






605




606




607




608




609




610




				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						createTabulationProblem(true, initialSeeds), 
						backwardHelper.createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler) :
				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						backwardHelper.createTabulationProblem(true, initialSeeds), 









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






611




612




613




614




615




616




617




						createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler);
		
		scheduler.runAndAwaitCompletion();
		assertAllFlowFunctionsUsed();
		backwardHelper.assertAllFlowFunctionsUsed();
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




}











Open sidebar



Joshua Garcia heros

2b03285f93b5f85dbda078770f881bad5ed130a1







Open sidebar



Joshua Garcia heros

2b03285f93b5f85dbda078770f881bad5ed130a1




Open sidebar

Joshua Garcia heros

2b03285f93b5f85dbda078770f881bad5ed130a1


Joshua Garciaherosheros
2b03285f93b5f85dbda078770f881bad5ed130a1










2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


test


heros


utilities


FieldSensitiveTestHelper.java



Find file
Normal viewHistoryPermalink






FieldSensitiveTestHelper.java



20.8 KB









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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






11




package heros.utilities;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






12




13




14





import static org.junit.Assert.assertTrue;
import heros.InterproceduralCFG;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






15




16




17




18




19




20




21




22




import heros.utilities.Edge.Call2ReturnEdge;
import heros.utilities.Edge.CallEdge;
import heros.utilities.Edge.EdgeVisitor;
import heros.utilities.Edge.NormalEdge;
import heros.utilities.Edge.ReturnEdge;
import heros.utilities.EdgeBuilder.CallSiteBuilder;
import heros.utilities.EdgeBuilder.ExitStmtBuilder;
import heros.utilities.EdgeBuilder.NormalStmtBuilder;









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






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




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.BiDiFieldSensitiveIFDSSolver;
import heros.fieldsens.FactMergeHandler;
import heros.fieldsens.FieldSensitiveIFDSSolver;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.FlowFunctions;
import heros.fieldsens.IFDSTabulationProblem;
import heros.fieldsens.Scheduler;
import heros.fieldsens.ZeroHandler;
import heros.fieldsens.FlowFunction.ConstrainedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






52




public class FieldSensitiveTestHelper {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






53














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






54




	private Multimap<TestMethod, Statement> method2startPoint = HashMultimap.create();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






55




56




57




58




	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






59




	private Map<Statement, TestMethod> stmt2method = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






60




	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






61




	private TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






62














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




	public FieldSensitiveTestHelper(TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






64




65




66




		this.debugger = debugger;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






67




	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






68




		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






69




70




71




72




73




74




75




		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






76




77




	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






78




		for (int i = 0; i < result.length; i++) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






79




			result[i] = new Statement(startingPoints[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






80




81




82




83




		}
		return result;
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






84




	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		return new NormalStmtBuilder(new Statement(stmt), flowFunctions);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






86




87




88




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






89




		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






90




91




92




	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






93




		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






96




97




	public static Statement over(String callSite) {
		return new Statement(callSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






100




101




	public static Statement to(String returnSite) {
		return new Statement(returnSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






102




103




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






104




	public static ExpectedFlowFunction<TestFact> kill(String source) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




		return kill(1, source);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






108




109




	public static ExpectedFlowFunction<TestFact> kill(int times, String source) {
		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






110




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






111




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






112




113




114




115




116




117




118




119




				throw new IllegalStateException();
			}
			
			@Override
			public String transformerString() {
				return "";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






120




121




	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






122




123




124




	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






125




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






126




				return accPathHandler.read(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






127




128




129




130




131




132




133




			}

			@Override
			public String toString() {
				return "read("+fieldName+")";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






134




135




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






136




137




138




	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






139




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






140




				return accPathHandler.prepend(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






141




142




143




144




145




146




147




			}
			
			@Override
			public String toString() {
				return "prepend("+fieldName+")";
			}
		};









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






148




149




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






150




151




152




	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






153




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






154




				return accPathHandler.overwrite(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






155




156




157




158




159




160




161




162




163




			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






164




	public static ExpectedFlowFunction<TestFact> flow(String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






165




		return flow(1, source, transformer, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






166




167




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






168




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






169




		TestFact[] targetFacts = new TestFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






170




		for(int i=0; i<targets.length; i++) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






171




			targetFacts[i] = new TestFact(targets[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






172




		}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






173




		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source), targetFacts) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






174




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






175




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






188




		ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler); 









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






189




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






190




191




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






192




	public static ExpectedFlowFunction<TestFact> flow(String source, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






193




194




195




		return flow(1, source, targets);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






196




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






197




198




		return flow(times, source, new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






199




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






200




201




202




203




204




205




206




207




208




				return accPathHandler.generate(target);
			}
			
			@Override
			public String toString() {
				return "";
			}
			
		}, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






209




210




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






211




212




213




214




	public static int times(int times) {
		return times;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






215




216




	public InterproceduralCFG<Statement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






217




218





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






219




			public boolean isStartPoint(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






220




221




222




223




				return method2startPoint.values().contains(stmt);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






224




			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




228




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






229




			public boolean isExitStmt(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






230




231




232




233




234




235




236




237




				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






238




			public boolean isCallStmt(final Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






239




240




241




242




243




244




245




246




247




				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






248




			public boolean isBranchTarget(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






249




250




251




252




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






253




254




			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






255




256




257




258




259




260




261




262




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






263




264




			public List<Statement> getPredsOf(Statement stmt) {
				LinkedList<Statement> result = Lists.newLinkedList();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






265




266




267




268




269




270




271




272




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






273




			public Collection<Statement> getStartPointsOf(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






274




275




276




277




				return method2startPoint.get(m);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






278




279




			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






292




			public TestMethod getMethodOf(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






293




294




295




296




				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






297




298




299




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






300




			public Set<Statement> getCallsFromWithin(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






301




302




303




304




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






305




306




			public Collection<Statement> getCallersOf(TestMethod m) {
				Set<Statement> result = Sets.newHashSet();









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




314




315




316




317




318




319




320




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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






321




			public Collection<TestMethod> getCalleesOfCallAt(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






322




				List<TestMethod> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






323




324




325




326




327




328




329




330




331




				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






332




			public Set<Statement> allNonCallStartNodes() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






343




	private void addOrVerifyStmt2Method(Statement stmt, TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






344




345




346




347




348




349




		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






350




	public MethodHelper method(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






351




352




353




354




355




356




		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






357




		private TestMethod method;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






358














rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






359




		public MethodHelper(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






360




361




362




363




364




			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






365




				for(ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






366




367




					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






401




		public void startPoints(Statement[] startingPoints) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






402




403




404




405




			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






406




	private static String expectedFlowFunctionsToString(ExpectedFlowFunction<TestFact>[] flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






407




		String result = "";









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






408




		for(ExpectedFlowFunction<TestFact> ff : flowFunctions)









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






409




410




411




412




			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






413




414




415




416




417




418




419




	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






420




421




	public FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<Statement, String, TestFact, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






422




423





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






424




			public FlowFunction<String, TestFact, Statement, TestMethod> getReturnFlowFunction(Statement callSite, TestMethod calleeMethod, Statement exitStmt, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






436




			public FlowFunction<String, TestFact, Statement, TestMethod> getNormalFlowFunction(final Statement curr) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






437




				for (final NormalEdge edge : normalEdges) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






438




					if (edge.unit.equals(curr)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






439




440




441




						return createFlowFunction(edge);
					}
				}









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






442




				throw new AssertionError(String.format("No Flow Function expected for %s", curr));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






443




444




445




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






446




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






447




448




449




450




451




452




453




454




455




				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






456




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallFlowFunction(Statement callStmt, TestMethod destinationMethod) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






457




458




459




460




461




462




463




464




				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






465




466




			private FlowFunction<String, TestFact, Statement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






467




					@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






468




469




470




					public Set<FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, Statement, TestMethod>> result = Sets.newHashSet();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






471




						boolean found = false;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






472




						for (ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






473




							if (ff.source.equals(source)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






474




								if (remainingFlowFunctions.remove(ff)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






475




476




									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));









abstract at return edges


 

 


Johannes Lerch
committed
Feb 23, 2015






477




									}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






478




									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






479




480




481




482




483




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




485




486




487




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






488




489




490




491




492




493




494




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






495




		Scheduler scheduler = new Scheduler();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






496




		FieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>>(









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






497




498




499




500




501




502




503




504




505




				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {
					@Override
					public void merge(TestFact previousFact, TestFact currentFact) {
					}

					@Override
					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
					}
					









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






506




				}, debugger, scheduler);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






507




		addExpectationsToDebugger();









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






508




		scheduler.runAndAwaitCompletion();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






509




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






510




511




512




		assertAllFlowFunctionsUsed();
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




	private void addExpectationsToDebugger() {
		for(NormalEdge edge : normalEdges) {
			debugger.expectNormalFlow(edge.unit, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(CallEdge edge : callEdges) {
			debugger.expectCallFlow(edge.callSite, edge.destinationMethod, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(Call2ReturnEdge edge : call2retEdges) {
			debugger.expectNormalFlow(edge.callSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(ReturnEdge edge : returnEdges) {
			debugger.expectReturnFlow(edge.exitStmt, edge.returnSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






528




529




530




	private IFDSTabulationProblem<Statement, String, TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, TestMethod> icfg = buildIcfg();
		final FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions = flowFunctions();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






531




		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






532




		return new IFDSTabulationProblem<Statement,String,  TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






555




			public FlowFunctions<Statement,String,  TestFact, TestMethod> flowFunctions() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






556




557




558




559




				return flowFunctions;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






560




			public InterproceduralCFG<Statement, TestMethod> interproceduralCFG() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






561




562




563




564




				return icfg;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






565




566




			public Map<Statement, Set<TestFact>> initialSeeds() {
				Map<Statement, Set<TestFact>> result = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






567




				for (String stmt : initialSeeds) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






568




					result.put(new Statement(stmt), Sets.newHashSet(new TestFact("0")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






569




570




571




572




573




				}
				return result;
			}

			@Override









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






574




575




576




577




578




			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






579




580




			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






581




					@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






582




					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






583




584




585




						return true;
					}
				};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






586




587




588




			}
		};
	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	public static enum TabulationProblemExchange {AsSpecified, ExchangeForwardAndBackward};
	public void runBiDiSolver(FieldSensitiveTestHelper backwardHelper, TabulationProblemExchange direction, final String...initialSeeds) {
		FactMergeHandler<TestFact> factMergeHandler = new FactMergeHandler<TestFact>() {
			@Override
			public void merge(TestFact previousFact, TestFact currentFact) {
			}

			@Override
			public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
			}
			
		};
		Scheduler scheduler = new Scheduler();
		BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> solver =
				direction == TabulationProblemExchange.AsSpecified ? 









switching to Java 6 compatibility


 

 


Johannes Lerch
committed
Jun 01, 2015






605




606




607




608




609




610




				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						createTabulationProblem(true, initialSeeds), 
						backwardHelper.createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler) :
				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						backwardHelper.createTabulationProblem(true, initialSeeds), 









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






611




612




613




614




615




616




617




						createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler);
		
		scheduler.runAndAwaitCompletion();
		assertAllFlowFunctionsUsed();
		backwardHelper.assertAllFlowFunctionsUsed();
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




}














2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


test


heros


utilities


FieldSensitiveTestHelper.java



Find file
Normal viewHistoryPermalink






FieldSensitiveTestHelper.java



20.8 KB









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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






11




package heros.utilities;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






12




13




14





import static org.junit.Assert.assertTrue;
import heros.InterproceduralCFG;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






15




16




17




18




19




20




21




22




import heros.utilities.Edge.Call2ReturnEdge;
import heros.utilities.Edge.CallEdge;
import heros.utilities.Edge.EdgeVisitor;
import heros.utilities.Edge.NormalEdge;
import heros.utilities.Edge.ReturnEdge;
import heros.utilities.EdgeBuilder.CallSiteBuilder;
import heros.utilities.EdgeBuilder.ExitStmtBuilder;
import heros.utilities.EdgeBuilder.NormalStmtBuilder;









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






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




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.BiDiFieldSensitiveIFDSSolver;
import heros.fieldsens.FactMergeHandler;
import heros.fieldsens.FieldSensitiveIFDSSolver;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.FlowFunctions;
import heros.fieldsens.IFDSTabulationProblem;
import heros.fieldsens.Scheduler;
import heros.fieldsens.ZeroHandler;
import heros.fieldsens.FlowFunction.ConstrainedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






52




public class FieldSensitiveTestHelper {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






53














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






54




	private Multimap<TestMethod, Statement> method2startPoint = HashMultimap.create();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






55




56




57




58




	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






59




	private Map<Statement, TestMethod> stmt2method = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






60




	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






61




	private TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






62














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




	public FieldSensitiveTestHelper(TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






64




65




66




		this.debugger = debugger;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






67




	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






68




		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






69




70




71




72




73




74




75




		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






76




77




	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






78




		for (int i = 0; i < result.length; i++) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






79




			result[i] = new Statement(startingPoints[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






80




81




82




83




		}
		return result;
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






84




	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		return new NormalStmtBuilder(new Statement(stmt), flowFunctions);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






86




87




88




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






89




		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






90




91




92




	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






93




		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






96




97




	public static Statement over(String callSite) {
		return new Statement(callSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






100




101




	public static Statement to(String returnSite) {
		return new Statement(returnSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






102




103




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






104




	public static ExpectedFlowFunction<TestFact> kill(String source) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




		return kill(1, source);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






108




109




	public static ExpectedFlowFunction<TestFact> kill(int times, String source) {
		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






110




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






111




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






112




113




114




115




116




117




118




119




				throw new IllegalStateException();
			}
			
			@Override
			public String transformerString() {
				return "";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






120




121




	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






122




123




124




	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






125




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






126




				return accPathHandler.read(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






127




128




129




130




131




132




133




			}

			@Override
			public String toString() {
				return "read("+fieldName+")";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






134




135




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






136




137




138




	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






139




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






140




				return accPathHandler.prepend(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






141




142




143




144




145




146




147




			}
			
			@Override
			public String toString() {
				return "prepend("+fieldName+")";
			}
		};









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






148




149




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






150




151




152




	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






153




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






154




				return accPathHandler.overwrite(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






155




156




157




158




159




160




161




162




163




			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






164




	public static ExpectedFlowFunction<TestFact> flow(String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






165




		return flow(1, source, transformer, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






166




167




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






168




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






169




		TestFact[] targetFacts = new TestFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






170




		for(int i=0; i<targets.length; i++) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






171




			targetFacts[i] = new TestFact(targets[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






172




		}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






173




		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source), targetFacts) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






174




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






175




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






188




		ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler); 









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






189




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






190




191




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






192




	public static ExpectedFlowFunction<TestFact> flow(String source, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






193




194




195




		return flow(1, source, targets);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






196




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






197




198




		return flow(times, source, new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






199




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






200




201




202




203




204




205




206




207




208




				return accPathHandler.generate(target);
			}
			
			@Override
			public String toString() {
				return "";
			}
			
		}, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






209




210




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






211




212




213




214




	public static int times(int times) {
		return times;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






215




216




	public InterproceduralCFG<Statement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






217




218





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






219




			public boolean isStartPoint(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






220




221




222




223




				return method2startPoint.values().contains(stmt);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






224




			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




228




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






229




			public boolean isExitStmt(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






230




231




232




233




234




235




236




237




				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






238




			public boolean isCallStmt(final Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






239




240




241




242




243




244




245




246




247




				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






248




			public boolean isBranchTarget(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






249




250




251




252




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






253




254




			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






255




256




257




258




259




260




261




262




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






263




264




			public List<Statement> getPredsOf(Statement stmt) {
				LinkedList<Statement> result = Lists.newLinkedList();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






265




266




267




268




269




270




271




272




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






273




			public Collection<Statement> getStartPointsOf(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






274




275




276




277




				return method2startPoint.get(m);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






278




279




			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






292




			public TestMethod getMethodOf(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






293




294




295




296




				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






297




298




299




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






300




			public Set<Statement> getCallsFromWithin(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






301




302




303




304




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






305




306




			public Collection<Statement> getCallersOf(TestMethod m) {
				Set<Statement> result = Sets.newHashSet();









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




314




315




316




317




318




319




320




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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






321




			public Collection<TestMethod> getCalleesOfCallAt(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






322




				List<TestMethod> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






323




324




325




326




327




328




329




330




331




				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






332




			public Set<Statement> allNonCallStartNodes() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






343




	private void addOrVerifyStmt2Method(Statement stmt, TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






344




345




346




347




348




349




		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






350




	public MethodHelper method(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






351




352




353




354




355




356




		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






357




		private TestMethod method;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






358














rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






359




		public MethodHelper(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






360




361




362




363




364




			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






365




				for(ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






366




367




					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






401




		public void startPoints(Statement[] startingPoints) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






402




403




404




405




			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






406




	private static String expectedFlowFunctionsToString(ExpectedFlowFunction<TestFact>[] flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






407




		String result = "";









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






408




		for(ExpectedFlowFunction<TestFact> ff : flowFunctions)









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






409




410




411




412




			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






413




414




415




416




417




418




419




	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






420




421




	public FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<Statement, String, TestFact, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






422




423





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






424




			public FlowFunction<String, TestFact, Statement, TestMethod> getReturnFlowFunction(Statement callSite, TestMethod calleeMethod, Statement exitStmt, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






436




			public FlowFunction<String, TestFact, Statement, TestMethod> getNormalFlowFunction(final Statement curr) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






437




				for (final NormalEdge edge : normalEdges) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






438




					if (edge.unit.equals(curr)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






439




440




441




						return createFlowFunction(edge);
					}
				}









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






442




				throw new AssertionError(String.format("No Flow Function expected for %s", curr));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






443




444




445




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






446




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






447




448




449




450




451




452




453




454




455




				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






456




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallFlowFunction(Statement callStmt, TestMethod destinationMethod) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






457




458




459




460




461




462




463




464




				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






465




466




			private FlowFunction<String, TestFact, Statement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






467




					@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






468




469




470




					public Set<FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, Statement, TestMethod>> result = Sets.newHashSet();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






471




						boolean found = false;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






472




						for (ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






473




							if (ff.source.equals(source)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






474




								if (remainingFlowFunctions.remove(ff)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






475




476




									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));









abstract at return edges


 

 


Johannes Lerch
committed
Feb 23, 2015






477




									}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






478




									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






479




480




481




482




483




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




485




486




487




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






488




489




490




491




492




493




494




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






495




		Scheduler scheduler = new Scheduler();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






496




		FieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>>(









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






497




498




499




500




501




502




503




504




505




				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {
					@Override
					public void merge(TestFact previousFact, TestFact currentFact) {
					}

					@Override
					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
					}
					









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






506




				}, debugger, scheduler);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






507




		addExpectationsToDebugger();









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






508




		scheduler.runAndAwaitCompletion();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






509




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






510




511




512




		assertAllFlowFunctionsUsed();
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




	private void addExpectationsToDebugger() {
		for(NormalEdge edge : normalEdges) {
			debugger.expectNormalFlow(edge.unit, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(CallEdge edge : callEdges) {
			debugger.expectCallFlow(edge.callSite, edge.destinationMethod, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(Call2ReturnEdge edge : call2retEdges) {
			debugger.expectNormalFlow(edge.callSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(ReturnEdge edge : returnEdges) {
			debugger.expectReturnFlow(edge.exitStmt, edge.returnSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






528




529




530




	private IFDSTabulationProblem<Statement, String, TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, TestMethod> icfg = buildIcfg();
		final FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions = flowFunctions();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






531




		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






532




		return new IFDSTabulationProblem<Statement,String,  TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






555




			public FlowFunctions<Statement,String,  TestFact, TestMethod> flowFunctions() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






556




557




558




559




				return flowFunctions;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






560




			public InterproceduralCFG<Statement, TestMethod> interproceduralCFG() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






561




562




563




564




				return icfg;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






565




566




			public Map<Statement, Set<TestFact>> initialSeeds() {
				Map<Statement, Set<TestFact>> result = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






567




				for (String stmt : initialSeeds) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






568




					result.put(new Statement(stmt), Sets.newHashSet(new TestFact("0")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






569




570




571




572




573




				}
				return result;
			}

			@Override









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






574




575




576




577




578




			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






579




580




			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






581




					@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






582




					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






583




584




585




						return true;
					}
				};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






586




587




588




			}
		};
	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	public static enum TabulationProblemExchange {AsSpecified, ExchangeForwardAndBackward};
	public void runBiDiSolver(FieldSensitiveTestHelper backwardHelper, TabulationProblemExchange direction, final String...initialSeeds) {
		FactMergeHandler<TestFact> factMergeHandler = new FactMergeHandler<TestFact>() {
			@Override
			public void merge(TestFact previousFact, TestFact currentFact) {
			}

			@Override
			public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
			}
			
		};
		Scheduler scheduler = new Scheduler();
		BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> solver =
				direction == TabulationProblemExchange.AsSpecified ? 









switching to Java 6 compatibility


 

 


Johannes Lerch
committed
Jun 01, 2015






605




606




607




608




609




610




				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						createTabulationProblem(true, initialSeeds), 
						backwardHelper.createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler) :
				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						backwardHelper.createTabulationProblem(true, initialSeeds), 









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






611




612




613




614




615




616




617




						createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler);
		
		scheduler.runAndAwaitCompletion();
		assertAllFlowFunctionsUsed();
		backwardHelper.assertAllFlowFunctionsUsed();
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




}










2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


test


heros


utilities


FieldSensitiveTestHelper.java



Find file
Normal viewHistoryPermalink




2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


test


heros


utilities


FieldSensitiveTestHelper.java





2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag








2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag





2b03285f93b5f85dbda078770f881bad5ed130a1

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

utilities

FieldSensitiveTestHelper.java
Find file
Normal viewHistoryPermalink




FieldSensitiveTestHelper.java



20.8 KB









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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






11




package heros.utilities;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






12




13




14





import static org.junit.Assert.assertTrue;
import heros.InterproceduralCFG;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






15




16




17




18




19




20




21




22




import heros.utilities.Edge.Call2ReturnEdge;
import heros.utilities.Edge.CallEdge;
import heros.utilities.Edge.EdgeVisitor;
import heros.utilities.Edge.NormalEdge;
import heros.utilities.Edge.ReturnEdge;
import heros.utilities.EdgeBuilder.CallSiteBuilder;
import heros.utilities.EdgeBuilder.ExitStmtBuilder;
import heros.utilities.EdgeBuilder.NormalStmtBuilder;









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






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




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.BiDiFieldSensitiveIFDSSolver;
import heros.fieldsens.FactMergeHandler;
import heros.fieldsens.FieldSensitiveIFDSSolver;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.FlowFunctions;
import heros.fieldsens.IFDSTabulationProblem;
import heros.fieldsens.Scheduler;
import heros.fieldsens.ZeroHandler;
import heros.fieldsens.FlowFunction.ConstrainedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






52




public class FieldSensitiveTestHelper {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






53














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






54




	private Multimap<TestMethod, Statement> method2startPoint = HashMultimap.create();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






55




56




57




58




	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






59




	private Map<Statement, TestMethod> stmt2method = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






60




	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






61




	private TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






62














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




	public FieldSensitiveTestHelper(TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






64




65




66




		this.debugger = debugger;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






67




	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






68




		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






69




70




71




72




73




74




75




		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






76




77




	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






78




		for (int i = 0; i < result.length; i++) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






79




			result[i] = new Statement(startingPoints[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






80




81




82




83




		}
		return result;
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






84




	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		return new NormalStmtBuilder(new Statement(stmt), flowFunctions);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






86




87




88




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






89




		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






90




91




92




	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






93




		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






96




97




	public static Statement over(String callSite) {
		return new Statement(callSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






100




101




	public static Statement to(String returnSite) {
		return new Statement(returnSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






102




103




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






104




	public static ExpectedFlowFunction<TestFact> kill(String source) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




		return kill(1, source);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






108




109




	public static ExpectedFlowFunction<TestFact> kill(int times, String source) {
		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






110




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






111




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






112




113




114




115




116




117




118




119




				throw new IllegalStateException();
			}
			
			@Override
			public String transformerString() {
				return "";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






120




121




	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






122




123




124




	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






125




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






126




				return accPathHandler.read(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






127




128




129




130




131




132




133




			}

			@Override
			public String toString() {
				return "read("+fieldName+")";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






134




135




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






136




137




138




	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






139




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






140




				return accPathHandler.prepend(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






141




142




143




144




145




146




147




			}
			
			@Override
			public String toString() {
				return "prepend("+fieldName+")";
			}
		};









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






148




149




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






150




151




152




	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






153




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






154




				return accPathHandler.overwrite(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






155




156




157




158




159




160




161




162




163




			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






164




	public static ExpectedFlowFunction<TestFact> flow(String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






165




		return flow(1, source, transformer, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






166




167




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






168




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






169




		TestFact[] targetFacts = new TestFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






170




		for(int i=0; i<targets.length; i++) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






171




			targetFacts[i] = new TestFact(targets[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






172




		}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






173




		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source), targetFacts) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






174




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






175




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






188




		ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler); 









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






189




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






190




191




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






192




	public static ExpectedFlowFunction<TestFact> flow(String source, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






193




194




195




		return flow(1, source, targets);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






196




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






197




198




		return flow(times, source, new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






199




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






200




201




202




203




204




205




206




207




208




				return accPathHandler.generate(target);
			}
			
			@Override
			public String toString() {
				return "";
			}
			
		}, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






209




210




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






211




212




213




214




	public static int times(int times) {
		return times;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






215




216




	public InterproceduralCFG<Statement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






217




218





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






219




			public boolean isStartPoint(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






220




221




222




223




				return method2startPoint.values().contains(stmt);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






224




			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




228




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






229




			public boolean isExitStmt(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






230




231




232




233




234




235




236




237




				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






238




			public boolean isCallStmt(final Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






239




240




241




242




243




244




245




246




247




				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






248




			public boolean isBranchTarget(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






249




250




251




252




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






253




254




			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






255




256




257




258




259




260




261




262




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






263




264




			public List<Statement> getPredsOf(Statement stmt) {
				LinkedList<Statement> result = Lists.newLinkedList();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






265




266




267




268




269




270




271




272




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






273




			public Collection<Statement> getStartPointsOf(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






274




275




276




277




				return method2startPoint.get(m);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






278




279




			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






292




			public TestMethod getMethodOf(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






293




294




295




296




				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






297




298




299




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






300




			public Set<Statement> getCallsFromWithin(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






301




302




303




304




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






305




306




			public Collection<Statement> getCallersOf(TestMethod m) {
				Set<Statement> result = Sets.newHashSet();









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




314




315




316




317




318




319




320




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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






321




			public Collection<TestMethod> getCalleesOfCallAt(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






322




				List<TestMethod> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






323




324




325




326




327




328




329




330




331




				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






332




			public Set<Statement> allNonCallStartNodes() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






343




	private void addOrVerifyStmt2Method(Statement stmt, TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






344




345




346




347




348




349




		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






350




	public MethodHelper method(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






351




352




353




354




355




356




		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






357




		private TestMethod method;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






358














rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






359




		public MethodHelper(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






360




361




362




363




364




			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






365




				for(ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






366




367




					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






401




		public void startPoints(Statement[] startingPoints) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






402




403




404




405




			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






406




	private static String expectedFlowFunctionsToString(ExpectedFlowFunction<TestFact>[] flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






407




		String result = "";









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






408




		for(ExpectedFlowFunction<TestFact> ff : flowFunctions)









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






409




410




411




412




			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






413




414




415




416




417




418




419




	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






420




421




	public FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<Statement, String, TestFact, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






422




423





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






424




			public FlowFunction<String, TestFact, Statement, TestMethod> getReturnFlowFunction(Statement callSite, TestMethod calleeMethod, Statement exitStmt, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






436




			public FlowFunction<String, TestFact, Statement, TestMethod> getNormalFlowFunction(final Statement curr) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






437




				for (final NormalEdge edge : normalEdges) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






438




					if (edge.unit.equals(curr)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






439




440




441




						return createFlowFunction(edge);
					}
				}









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






442




				throw new AssertionError(String.format("No Flow Function expected for %s", curr));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






443




444




445




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






446




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






447




448




449




450




451




452




453




454




455




				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






456




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallFlowFunction(Statement callStmt, TestMethod destinationMethod) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






457




458




459




460




461




462




463




464




				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






465




466




			private FlowFunction<String, TestFact, Statement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






467




					@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






468




469




470




					public Set<FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, Statement, TestMethod>> result = Sets.newHashSet();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






471




						boolean found = false;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






472




						for (ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






473




							if (ff.source.equals(source)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






474




								if (remainingFlowFunctions.remove(ff)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






475




476




									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));









abstract at return edges


 

 


Johannes Lerch
committed
Feb 23, 2015






477




									}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






478




									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






479




480




481




482




483




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




485




486




487




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






488




489




490




491




492




493




494




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






495




		Scheduler scheduler = new Scheduler();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






496




		FieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>>(









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






497




498




499




500




501




502




503




504




505




				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {
					@Override
					public void merge(TestFact previousFact, TestFact currentFact) {
					}

					@Override
					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
					}
					









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






506




				}, debugger, scheduler);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






507




		addExpectationsToDebugger();









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






508




		scheduler.runAndAwaitCompletion();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






509




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






510




511




512




		assertAllFlowFunctionsUsed();
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




	private void addExpectationsToDebugger() {
		for(NormalEdge edge : normalEdges) {
			debugger.expectNormalFlow(edge.unit, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(CallEdge edge : callEdges) {
			debugger.expectCallFlow(edge.callSite, edge.destinationMethod, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(Call2ReturnEdge edge : call2retEdges) {
			debugger.expectNormalFlow(edge.callSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(ReturnEdge edge : returnEdges) {
			debugger.expectReturnFlow(edge.exitStmt, edge.returnSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






528




529




530




	private IFDSTabulationProblem<Statement, String, TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, TestMethod> icfg = buildIcfg();
		final FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions = flowFunctions();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






531




		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






532




		return new IFDSTabulationProblem<Statement,String,  TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






555




			public FlowFunctions<Statement,String,  TestFact, TestMethod> flowFunctions() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






556




557




558




559




				return flowFunctions;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






560




			public InterproceduralCFG<Statement, TestMethod> interproceduralCFG() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






561




562




563




564




				return icfg;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






565




566




			public Map<Statement, Set<TestFact>> initialSeeds() {
				Map<Statement, Set<TestFact>> result = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






567




				for (String stmt : initialSeeds) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






568




					result.put(new Statement(stmt), Sets.newHashSet(new TestFact("0")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






569




570




571




572




573




				}
				return result;
			}

			@Override









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






574




575




576




577




578




			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






579




580




			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






581




					@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






582




					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






583




584




585




						return true;
					}
				};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






586




587




588




			}
		};
	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	public static enum TabulationProblemExchange {AsSpecified, ExchangeForwardAndBackward};
	public void runBiDiSolver(FieldSensitiveTestHelper backwardHelper, TabulationProblemExchange direction, final String...initialSeeds) {
		FactMergeHandler<TestFact> factMergeHandler = new FactMergeHandler<TestFact>() {
			@Override
			public void merge(TestFact previousFact, TestFact currentFact) {
			}

			@Override
			public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
			}
			
		};
		Scheduler scheduler = new Scheduler();
		BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> solver =
				direction == TabulationProblemExchange.AsSpecified ? 









switching to Java 6 compatibility


 

 


Johannes Lerch
committed
Jun 01, 2015






605




606




607




608




609




610




				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						createTabulationProblem(true, initialSeeds), 
						backwardHelper.createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler) :
				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						backwardHelper.createTabulationProblem(true, initialSeeds), 









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






611




612




613




614




615




616




617




						createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler);
		
		scheduler.runAndAwaitCompletion();
		assertAllFlowFunctionsUsed();
		backwardHelper.assertAllFlowFunctionsUsed();
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




}








FieldSensitiveTestHelper.java



20.8 KB










FieldSensitiveTestHelper.java



20.8 KB









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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






11




package heros.utilities;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






12




13




14





import static org.junit.Assert.assertTrue;
import heros.InterproceduralCFG;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






15




16




17




18




19




20




21




22




import heros.utilities.Edge.Call2ReturnEdge;
import heros.utilities.Edge.CallEdge;
import heros.utilities.Edge.EdgeVisitor;
import heros.utilities.Edge.NormalEdge;
import heros.utilities.Edge.ReturnEdge;
import heros.utilities.EdgeBuilder.CallSiteBuilder;
import heros.utilities.EdgeBuilder.ExitStmtBuilder;
import heros.utilities.EdgeBuilder.NormalStmtBuilder;









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






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




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.BiDiFieldSensitiveIFDSSolver;
import heros.fieldsens.FactMergeHandler;
import heros.fieldsens.FieldSensitiveIFDSSolver;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.FlowFunctions;
import heros.fieldsens.IFDSTabulationProblem;
import heros.fieldsens.Scheduler;
import heros.fieldsens.ZeroHandler;
import heros.fieldsens.FlowFunction.ConstrainedFact;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






52




public class FieldSensitiveTestHelper {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






53














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






54




	private Multimap<TestMethod, Statement> method2startPoint = HashMultimap.create();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






55




56




57




58




	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






59




	private Map<Statement, TestMethod> stmt2method = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






60




	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






61




	private TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






62














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




	public FieldSensitiveTestHelper(TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






64




65




66




		this.debugger = debugger;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






67




	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






68




		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






69




70




71




72




73




74




75




		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






76




77




	public static Statement[] startPoints(String... startingPoints) {
		Statement[] result = new Statement[startingPoints.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






78




		for (int i = 0; i < result.length; i++) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






79




			result[i] = new Statement(startingPoints[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






80




81




82




83




		}
		return result;
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






84




	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		return new NormalStmtBuilder(new Statement(stmt), flowFunctions);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






86




87




88




	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






89




		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






90




91




92




	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






93




		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






94




95




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






96




97




	public static Statement over(String callSite) {
		return new Statement(callSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






98




99




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






100




101




	public static Statement to(String returnSite) {
		return new Statement(returnSite);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






102




103




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






104




	public static ExpectedFlowFunction<TestFact> kill(String source) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






105




106




107




		return kill(1, source);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






108




109




	public static ExpectedFlowFunction<TestFact> kill(int times, String source) {
		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






110




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






111




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






112




113




114




115




116




117




118




119




				throw new IllegalStateException();
			}
			
			@Override
			public String transformerString() {
				return "";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






120




121




	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






122




123




124




	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






125




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






126




				return accPathHandler.read(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






127




128




129




130




131




132




133




			}

			@Override
			public String toString() {
				return "read("+fieldName+")";
			}
		};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






134




135




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






136




137




138




	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






139




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






140




				return accPathHandler.prepend(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






141




142




143




144




145




146




147




			}
			
			@Override
			public String toString() {
				return "prepend("+fieldName+")";
			}
		};









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






148




149




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






150




151




152




	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






153




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






154




				return accPathHandler.overwrite(new String(fieldName)).generate(target);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






155




156




157




158




159




160




161




162




163




			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






164




	public static ExpectedFlowFunction<TestFact> flow(String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






165




		return flow(1, source, transformer, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






166




167




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






168




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, final AccessPathTransformer transformer, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






169




		TestFact[] targetFacts = new TestFact[targets.length];









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






170




		for(int i=0; i<targets.length; i++) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






171




			targetFacts[i] = new TestFact(targets[i]);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






172




		}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






173




		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source), targetFacts) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






174




			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






175




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






188




		ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler); 









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






189




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






190




191




	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






192




	public static ExpectedFlowFunction<TestFact> flow(String source, String... targets) {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






193




194




195




		return flow(1, source, targets);
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






196




	public static ExpectedFlowFunction<TestFact> flow(int times, String source, String... targets) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






197




198




		return flow(times, source, new AccessPathTransformer() {
			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






199




			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






200




201




202




203




204




205




206




207




208




				return accPathHandler.generate(target);
			}
			
			@Override
			public String toString() {
				return "";
			}
			
		}, targets);









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






209




210




	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






211




212




213




214




	public static int times(int times) {
		return times;
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






215




216




	public InterproceduralCFG<Statement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






217




218





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






219




			public boolean isStartPoint(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






220




221




222




223




				return method2startPoint.values().contains(stmt);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






224




			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






225




226




227




228




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






229




			public boolean isExitStmt(Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






230




231




232




233




234




235




236




237




				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






238




			public boolean isCallStmt(final Statement stmt) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






239




240




241




242




243




244




245




246




247




				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






248




			public boolean isBranchTarget(Statement stmt, Statement succ) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






249




250




251




252




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






253




254




			public List<Statement> getSuccsOf(Statement n) {
				LinkedList<Statement> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






255




256




257




258




259




260




261




262




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






263




264




			public List<Statement> getPredsOf(Statement stmt) {
				LinkedList<Statement> result = Lists.newLinkedList();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






265




266




267




268




269




270




271




272




				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






273




			public Collection<Statement> getStartPointsOf(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






274




275




276




277




				return method2startPoint.get(m);
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






278




279




			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {
				Set<Statement> result = Sets.newHashSet();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






292




			public TestMethod getMethodOf(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






293




294




295




296




				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






297




298




299




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






300




			public Set<Statement> getCallsFromWithin(TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






301




302




303




304




				throw new IllegalStateException();
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






305




306




			public Collection<Statement> getCallersOf(TestMethod m) {
				Set<Statement> result = Sets.newHashSet();









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




314




315




316




317




318




319




320




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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






321




			public Collection<TestMethod> getCalleesOfCallAt(Statement n) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






322




				List<TestMethod> result = Lists.newLinkedList();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






323




324




325




326




327




328




329




330




331




				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






332




			public Set<Statement> allNonCallStartNodes() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






343




	private void addOrVerifyStmt2Method(Statement stmt, TestMethod m) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






344




345




346




347




348




349




		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






350




	public MethodHelper method(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






351




352




353




354




355




356




		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {










rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






357




		private TestMethod method;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






358














rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






359




		public MethodHelper(TestMethod method) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






360




361




362




363




364




			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






365




				for(ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






366




367




					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






401




		public void startPoints(Statement[] startingPoints) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






402




403




404




405




			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






406




	private static String expectedFlowFunctionsToString(ExpectedFlowFunction<TestFact>[] flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






407




		String result = "";









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






408




		for(ExpectedFlowFunction<TestFact> ff : flowFunctions)









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






409




410




411




412




			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






413




414




415




416




417




418




419




	private static boolean nullAwareEquals(Object a, Object b) {
		if(a == null)
			return b==null;
		else
			return a.equals(b);
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






420




421




	public FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<Statement, String, TestFact, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






422




423





			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






424




			public FlowFunction<String, TestFact, Statement, TestMethod> getReturnFlowFunction(Statement callSite, TestMethod calleeMethod, Statement exitStmt, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






436




			public FlowFunction<String, TestFact, Statement, TestMethod> getNormalFlowFunction(final Statement curr) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






437




				for (final NormalEdge edge : normalEdges) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






438




					if (edge.unit.equals(curr)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






439




440




441




						return createFlowFunction(edge);
					}
				}









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






442




				throw new AssertionError(String.format("No Flow Function expected for %s", curr));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






443




444




445




			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






446




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






447




448




449




450




451




452




453




454




455




				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






456




			public FlowFunction<String, TestFact, Statement, TestMethod> getCallFlowFunction(Statement callStmt, TestMethod destinationMethod) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






457




458




459




460




461




462




463




464




				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






465




466




			private FlowFunction<String, TestFact, Statement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, Statement, TestMethod>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






467




					@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






468




469




470




					public Set<FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, Statement, TestMethod>> result = Sets.newHashSet();









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






471




						boolean found = false;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






472




						for (ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






473




							if (ff.source.equals(source)) {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






474




								if (remainingFlowFunctions.remove(ff)) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






475




476




									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));









abstract at return edges


 

 


Johannes Lerch
committed
Feb 23, 2015






477




									}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






478




									found = true;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






479




480




481




482




483




								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






484




485




486




487




						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






488




489




490




491




492




493




494




					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






495




		Scheduler scheduler = new Scheduler();









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






496




		FieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>>(









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






497




498




499




500




501




502




503




504




505




				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {
					@Override
					public void merge(TestFact previousFact, TestFact currentFact) {
					}

					@Override
					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
					}
					









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






506




				}, debugger, scheduler);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






507




		addExpectationsToDebugger();









bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015






508




		scheduler.runAndAwaitCompletion();









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






509




		









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






510




511




512




		assertAllFlowFunctionsUsed();
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






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




	private void addExpectationsToDebugger() {
		for(NormalEdge edge : normalEdges) {
			debugger.expectNormalFlow(edge.unit, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(CallEdge edge : callEdges) {
			debugger.expectCallFlow(edge.callSite, edge.destinationMethod, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(Call2ReturnEdge edge : call2retEdges) {
			debugger.expectNormalFlow(edge.callSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
		for(ReturnEdge edge : returnEdges) {
			debugger.expectReturnFlow(edge.exitStmt, edge.returnSite, expectedFlowFunctionsToString(edge.flowFunctions));
		}
	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






528




529




530




	private IFDSTabulationProblem<Statement, String, TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {
		final InterproceduralCFG<Statement, TestMethod> icfg = buildIcfg();
		final FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions = flowFunctions();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






531




		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






532




		return new IFDSTabulationProblem<Statement,String,  TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>>() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






555




			public FlowFunctions<Statement,String,  TestFact, TestMethod> flowFunctions() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






556




557




558




559




				return flowFunctions;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






560




			public InterproceduralCFG<Statement, TestMethod> interproceduralCFG() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






561




562




563




564




				return icfg;
			}

			@Override









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






565




566




			public Map<Statement, Set<TestFact>> initialSeeds() {
				Map<Statement, Set<TestFact>> result = Maps.newHashMap();









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






567




				for (String stmt : initialSeeds) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






568




					result.put(new Statement(stmt), Sets.newHashSet(new TestFact("0")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






569




570




571




572




573




				}
				return result;
			}

			@Override









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






574




575




576




577




578




			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






579




580




			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






581




					@Override









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






582




					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






583




584




585




						return true;
					}
				};









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






586




587




588




			}
		};
	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	public static enum TabulationProblemExchange {AsSpecified, ExchangeForwardAndBackward};
	public void runBiDiSolver(FieldSensitiveTestHelper backwardHelper, TabulationProblemExchange direction, final String...initialSeeds) {
		FactMergeHandler<TestFact> factMergeHandler = new FactMergeHandler<TestFact>() {
			@Override
			public void merge(TestFact previousFact, TestFact currentFact) {
			}

			@Override
			public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
			}
			
		};
		Scheduler scheduler = new Scheduler();
		BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> solver =
				direction == TabulationProblemExchange.AsSpecified ? 









switching to Java 6 compatibility


 

 


Johannes Lerch
committed
Jun 01, 2015






605




606




607




608




609




610




				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						createTabulationProblem(true, initialSeeds), 
						backwardHelper.createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler) :
				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(
						backwardHelper.createTabulationProblem(true, initialSeeds), 









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






611




612




613




614




615




616




617




						createTabulationProblem(true, initialSeeds),
						factMergeHandler, debugger, scheduler);
		
		scheduler.runAndAwaitCompletion();
		assertAllFlowFunctionsUsed();
		backwardHelper.assertAllFlowFunctionsUsed();
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






618




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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

11
package heros.utilities;packageheros.utilities;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

12

13

14
import static org.junit.Assert.assertTrue;importstaticorg.junit.Assert.assertTrue;import heros.InterproceduralCFG;importheros.InterproceduralCFG;



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

15

16

17

18

19

20

21

22
import heros.utilities.Edge.Call2ReturnEdge;importheros.utilities.Edge.Call2ReturnEdge;import heros.utilities.Edge.CallEdge;importheros.utilities.Edge.CallEdge;import heros.utilities.Edge.EdgeVisitor;importheros.utilities.Edge.EdgeVisitor;import heros.utilities.Edge.NormalEdge;importheros.utilities.Edge.NormalEdge;import heros.utilities.Edge.ReturnEdge;importheros.utilities.Edge.ReturnEdge;import heros.utilities.EdgeBuilder.CallSiteBuilder;importheros.utilities.EdgeBuilder.CallSiteBuilder;import heros.utilities.EdgeBuilder.ExitStmtBuilder;importheros.utilities.EdgeBuilder.ExitStmtBuilder;import heros.utilities.EdgeBuilder.NormalStmtBuilder;importheros.utilities.EdgeBuilder.NormalStmtBuilder;



renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015



renaming package


 

 

renaming package

 

Johannes Lerch
committed
Jun 01, 2015

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
import heros.fieldsens.AccessPath;importheros.fieldsens.AccessPath;import heros.fieldsens.AccessPathHandler;importheros.fieldsens.AccessPathHandler;import heros.fieldsens.BiDiFieldSensitiveIFDSSolver;importheros.fieldsens.BiDiFieldSensitiveIFDSSolver;import heros.fieldsens.FactMergeHandler;importheros.fieldsens.FactMergeHandler;import heros.fieldsens.FieldSensitiveIFDSSolver;importheros.fieldsens.FieldSensitiveIFDSSolver;import heros.fieldsens.FlowFunction;importheros.fieldsens.FlowFunction;import heros.fieldsens.FlowFunctions;importheros.fieldsens.FlowFunctions;import heros.fieldsens.IFDSTabulationProblem;importheros.fieldsens.IFDSTabulationProblem;import heros.fieldsens.Scheduler;importheros.fieldsens.Scheduler;import heros.fieldsens.ZeroHandler;importheros.fieldsens.ZeroHandler;import heros.fieldsens.FlowFunction.ConstrainedFact;importheros.fieldsens.FlowFunction.ConstrainedFact;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
import java.util.Collection;importjava.util.Collection;import java.util.LinkedList;importjava.util.LinkedList;import java.util.List;importjava.util.List;import java.util.Map;importjava.util.Map;import java.util.Set;importjava.util.Set;import com.google.common.base.Joiner;importcom.google.common.base.Joiner;import com.google.common.base.Predicate;importcom.google.common.base.Predicate;import com.google.common.collect.HashMultimap;importcom.google.common.collect.HashMultimap;import com.google.common.collect.HashMultiset;importcom.google.common.collect.HashMultiset;import com.google.common.collect.Iterables;importcom.google.common.collect.Iterables;import com.google.common.collect.Lists;importcom.google.common.collect.Lists;import com.google.common.collect.Maps;importcom.google.common.collect.Maps;import com.google.common.collect.Multimap;importcom.google.common.collect.Multimap;import com.google.common.collect.Multiset;importcom.google.common.collect.Multiset;import com.google.common.collect.Sets;importcom.google.common.collect.Sets;



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

52
public class FieldSensitiveTestHelper {publicclassFieldSensitiveTestHelper{



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

53




restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

54
	private Multimap<TestMethod, Statement> method2startPoint = HashMultimap.create();privateMultimap<TestMethod,Statement>method2startPoint=HashMultimap.create();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

55

56

57

58
	private List<NormalEdge> normalEdges = Lists.newLinkedList();privateList<NormalEdge>normalEdges=Lists.newLinkedList();	private List<CallEdge> callEdges = Lists.newLinkedList();privateList<CallEdge>callEdges=Lists.newLinkedList();	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();privateList<Call2ReturnEdge>call2retEdges=Lists.newLinkedList();	private List<ReturnEdge> returnEdges = Lists.newLinkedList();privateList<ReturnEdge>returnEdges=Lists.newLinkedList();



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

59
	private Map<Statement, TestMethod> stmt2method = Maps.newHashMap();privateMap<Statement,TestMethod>stmt2method=Maps.newHashMap();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

60
	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();privateMultiset<ExpectedFlowFunction>remainingFlowFunctions=HashMultiset.create();



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

61
	private TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger;privateTestDebugger<String,TestFact,Statement,TestMethod,InterproceduralCFG<Statement,TestMethod>>debugger;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

62




restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

63
	public FieldSensitiveTestHelper(TestDebugger<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> debugger) {publicFieldSensitiveTestHelper(TestDebugger<String,TestFact,Statement,TestMethod,InterproceduralCFG<Statement,TestMethod>>debugger){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

64

65

66
		this.debugger = debugger;this.debugger=debugger;	}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

67
	public MethodHelper method(String methodName, Statement[] startingPoints, EdgeBuilder... edgeBuilders) {publicMethodHelpermethod(StringmethodName,Statement[]startingPoints,EdgeBuilder...edgeBuilders){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

68
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));MethodHelpermethodHelper=newMethodHelper(newTestMethod(methodName));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

69

70

71

72

73

74

75
		methodHelper.startPoints(startingPoints);methodHelper.startPoints(startingPoints);		for(EdgeBuilder edgeBuilder : edgeBuilders){for(EdgeBuilderedgeBuilder:edgeBuilders){			methodHelper.edges(edgeBuilder.edges());methodHelper.edges(edgeBuilder.edges());		}}		return methodHelper;returnmethodHelper;	}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

76

77
	public static Statement[] startPoints(String... startingPoints) {publicstaticStatement[]startPoints(String...startingPoints){		Statement[] result = new Statement[startingPoints.length];Statement[]result=newStatement[startingPoints.length];



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

78
		for (int i = 0; i < result.length; i++) {for(inti=0;i<result.length;i++){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

79
			result[i] = new Statement(startingPoints[i]);result[i]=newStatement(startingPoints[i]);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

80

81

82

83
		}}		return result;returnresult;	}}



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

84
	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {publicstaticEdgeBuilder.NormalStmtBuildernormalStmt(Stringstmt,ExpectedFlowFunction...flowFunctions){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

85
		return new NormalStmtBuilder(new Statement(stmt), flowFunctions);returnnewNormalStmtBuilder(newStatement(stmt),flowFunctions);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

86

87

88
	}}		public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {publicstaticEdgeBuilder.CallSiteBuildercallSite(StringcallSite){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

89
		return new EdgeBuilder.CallSiteBuilder(new Statement(callSite));returnnewEdgeBuilder.CallSiteBuilder(newStatement(callSite));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

90

91

92
	}}		public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {publicstaticEdgeBuilder.ExitStmtBuilderexitStmt(StringexitStmt){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

93
		return new EdgeBuilder.ExitStmtBuilder(new Statement(exitStmt));returnnewEdgeBuilder.ExitStmtBuilder(newStatement(exitStmt));



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



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

96

97
	public static Statement over(String callSite) {publicstaticStatementover(StringcallSite){		return new Statement(callSite);returnnewStatement(callSite);



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
	}}	



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

100

101
	public static Statement to(String returnSite) {publicstaticStatementto(StringreturnSite){		return new Statement(returnSite);returnnewStatement(returnSite);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

102

103
	}}	



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

104
	public static ExpectedFlowFunction<TestFact> kill(String source) {publicstaticExpectedFlowFunction<TestFact>kill(Stringsource){



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
		return kill(1, source);returnkill(1,source);	}}	



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

108

109
	public static ExpectedFlowFunction<TestFact> kill(int times, String source) {publicstaticExpectedFlowFunction<TestFact>kill(inttimes,Stringsource){		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source)) {returnnewExpectedFlowFunction<TestFact>(times,newTestFact(source)){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

110
			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

111
			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,Statement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

112

113

114

115

116

117

118

119
				throw new IllegalStateException();thrownewIllegalStateException();			}}						@Override@Override			public String transformerString() {publicStringtransformerString(){				return "";return"";			}}		};};



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
	}}



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

122

123

124
	public static AccessPathTransformer readField(final String fieldName) {publicstaticAccessPathTransformerreadField(finalStringfieldName){		return new AccessPathTransformer() {returnnewAccessPathTransformer(){			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

125
			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,Statement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler){



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

126
				return accPathHandler.read(new String(fieldName)).generate(target);returnaccPathHandler.read(newString(fieldName)).generate(target);



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

127

128

129

130

131

132

133
			}}			@Override@Override			public String toString() {publicStringtoString(){				return "read("+fieldName+")";return"read("+fieldName+")";			}}		};};



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

134

135
	}}	



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

136

137

138
	public static AccessPathTransformer prependField(final String fieldName) {publicstaticAccessPathTransformerprependField(finalStringfieldName){		return new AccessPathTransformer() {returnnewAccessPathTransformer(){			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

139
			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,Statement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler){



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

140
				return accPathHandler.prepend(new String(fieldName)).generate(target);returnaccPathHandler.prepend(newString(fieldName)).generate(target);



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

141

142

143

144

145

146

147
			}}						@Override@Override			public String toString() {publicStringtoString(){				return "prepend("+fieldName+")";return"prepend("+fieldName+")";			}}		};};



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

148

149
	}}	



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

150

151

152
	public static AccessPathTransformer overwriteField(final String fieldName) {publicstaticAccessPathTransformeroverwriteField(finalStringfieldName){		return new AccessPathTransformer() {returnnewAccessPathTransformer(){			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

153
			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,Statement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler){



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

154
				return accPathHandler.overwrite(new String(fieldName)).generate(target);returnaccPathHandler.overwrite(newString(fieldName)).generate(target);



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

155

156

157

158

159

160

161

162

163
			}}						@Override@Override			public String toString() {publicStringtoString(){				return "write("+fieldName+")";return"write("+fieldName+")";			}}		};};	}}	



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

164
	public static ExpectedFlowFunction<TestFact> flow(String source, final AccessPathTransformer transformer, String... targets) {publicstaticExpectedFlowFunction<TestFact>flow(Stringsource,finalAccessPathTransformertransformer,String...targets){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

165
		return flow(1, source, transformer, targets);returnflow(1,source,transformer,targets);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

166

167
	}}	



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

168
	public static ExpectedFlowFunction<TestFact> flow(int times, String source, final AccessPathTransformer transformer, String... targets) {publicstaticExpectedFlowFunction<TestFact>flow(inttimes,Stringsource,finalAccessPathTransformertransformer,String...targets){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

169
		TestFact[] targetFacts = new TestFact[targets.length];TestFact[]targetFacts=newTestFact[targets.length];



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

170
		for(int i=0; i<targets.length; i++) {for(inti=0;i<targets.length;i++){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

171
			targetFacts[i] = new TestFact(targets[i]);targetFacts[i]=newTestFact(targets[i]);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

172
		}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

173
		return new ExpectedFlowFunction<TestFact>(times, new TestFact(source), targetFacts) {returnnewExpectedFlowFunction<TestFact>(times,newTestFact(source),targetFacts){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

174
			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

175
			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,Statement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

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
				return transformer.apply(target, accPathHandler);returntransformer.apply(target,accPathHandler);			}}						@Override@Override			public String transformerString() {publicStringtransformerString(){				return transformer.toString();returntransformer.toString();			}}		};};	}}		private static interface AccessPathTransformer {privatestaticinterfaceAccessPathTransformer{



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

188
		ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler); ConstrainedFact<String,TestFact,Statement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler);



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

189
		



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

190

191
	}}	



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

192
	public static ExpectedFlowFunction<TestFact> flow(String source, String... targets) {publicstaticExpectedFlowFunction<TestFact>flow(Stringsource,String...targets){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

193

194

195
		return flow(1, source, targets);returnflow(1,source,targets);	}}	



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

196
	public static ExpectedFlowFunction<TestFact> flow(int times, String source, String... targets) {publicstaticExpectedFlowFunction<TestFact>flow(inttimes,Stringsource,String...targets){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

197

198
		return flow(times, source, new AccessPathTransformer() {returnflow(times,source,newAccessPathTransformer(){			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

199
			public ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,Statement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

200

201

202

203

204

205

206

207

208
				return accPathHandler.generate(target);returnaccPathHandler.generate(target);			}}						@Override@Override			public String toString() {publicStringtoString(){				return "";return"";			}}					}, targets);},targets);



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

209

210
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

211

212

213

214
	public static int times(int times) {publicstaticinttimes(inttimes){		return times;returntimes;	}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

215

216
	public InterproceduralCFG<Statement, TestMethod> buildIcfg() {publicInterproceduralCFG<Statement,TestMethod>buildIcfg(){		return new InterproceduralCFG<Statement, TestMethod>() {returnnewInterproceduralCFG<Statement,TestMethod>(){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

217

218
			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

219
			public boolean isStartPoint(Statement stmt) {publicbooleanisStartPoint(Statementstmt){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

220

221

222

223
				return method2startPoint.values().contains(stmt);returnmethod2startPoint.values().contains(stmt);			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

224
			public boolean isFallThroughSuccessor(Statement stmt, Statement succ) {publicbooleanisFallThroughSuccessor(Statementstmt,Statementsucc){



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

228
				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

229
			public boolean isExitStmt(Statement stmt) {publicbooleanisExitStmt(Statementstmt){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

230

231

232

233

234

235

236

237
				for(ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if(edge.exitStmt.equals(stmt))if(edge.exitStmt.equals(stmt))						return true;returntrue;				}}				return false;returnfalse;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

238
			public boolean isCallStmt(final Statement stmt) {publicbooleanisCallStmt(finalStatementstmt){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

239

240

241

242

243

244

245

246

247
				return Iterables.any(callEdges, new Predicate<CallEdge>() {returnIterables.any(callEdges,newPredicate<CallEdge>(){					@Override@Override					public boolean apply(CallEdge edge) {publicbooleanapply(CallEdgeedge){						return edge.callSite.equals(stmt);returnedge.callSite.equals(stmt);					}}				});});			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

248
			public boolean isBranchTarget(Statement stmt, Statement succ) {publicbooleanisBranchTarget(Statementstmt,Statementsucc){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

249

250

251

252
				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

253

254
			public List<Statement> getSuccsOf(Statement n) {publicList<Statement>getSuccsOf(Statementn){				LinkedList<Statement> result = Lists.newLinkedList();LinkedList<Statement>result=Lists.newLinkedList();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

255

256

257

258

259

260

261

262
				for (NormalEdge edge : normalEdges) {for(NormalEdgeedge:normalEdges){					if (edge.includeInCfg && edge.unit.equals(n))if(edge.includeInCfg&&edge.unit.equals(n))						result.add(edge.succUnit);result.add(edge.succUnit);				}}				return result;returnresult;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

263

264
			public List<Statement> getPredsOf(Statement stmt) {publicList<Statement>getPredsOf(Statementstmt){				LinkedList<Statement> result = Lists.newLinkedList();LinkedList<Statement>result=Lists.newLinkedList();



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

265

266

267

268

269

270

271

272
				for (NormalEdge edge : normalEdges) {for(NormalEdgeedge:normalEdges){					if (edge.includeInCfg && edge.succUnit.equals(stmt))if(edge.includeInCfg&&edge.succUnit.equals(stmt))						result.add(edge.unit);result.add(edge.unit);				}}				return result;returnresult;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

273
			public Collection<Statement> getStartPointsOf(TestMethod m) {publicCollection<Statement>getStartPointsOf(TestMethodm){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

274

275

276

277
				return method2startPoint.get(m);returnmethod2startPoint.get(m);			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

278

279
			public Collection<Statement> getReturnSitesOfCallAt(Statement n) {publicCollection<Statement>getReturnSitesOfCallAt(Statementn){				Set<Statement> result = Sets.newHashSet();Set<Statement>result=Sets.newHashSet();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
				for (Call2ReturnEdge edge : call2retEdges) {for(Call2ReturnEdgeedge:call2retEdges){					if (edge.includeInCfg && edge.callSite.equals(n))if(edge.includeInCfg&&edge.callSite.equals(n))						result.add(edge.returnSite);result.add(edge.returnSite);				}}				for(ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if(edge.includeInCfg && edge.callSite.equals(n))if(edge.includeInCfg&&edge.callSite.equals(n))						result.add(edge.returnSite);result.add(edge.returnSite);				}}				return result;returnresult;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

292
			public TestMethod getMethodOf(Statement n) {publicTestMethodgetMethodOf(Statementn){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

293

294

295

296
				if(stmt2method.containsKey(n))if(stmt2method.containsKey(n))					return stmt2method.get(n);returnstmt2method.get(n);				elseelse					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");thrownewIllegalArgumentException("Statement "+n+" is not defined in any method.");



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

297

298

299
			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

300
			public Set<Statement> getCallsFromWithin(TestMethod m) {publicSet<Statement>getCallsFromWithin(TestMethodm){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

301

302

303

304
				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

305

306
			public Collection<Statement> getCallersOf(TestMethod m) {publicCollection<Statement>getCallersOf(TestMethodm){				Set<Statement> result = Sets.newHashSet();Set<Statement>result=Sets.newHashSet();



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

314

315

316

317

318

319

320
				for (CallEdge edge : callEdges) {for(CallEdgeedge:callEdges){					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {if(edge.includeInCfg&&edge.destinationMethod.equals(m)){						result.add(edge.callSite);result.add(edge.callSite);					}}				}}				for (ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {if(edge.includeInCfg&&edge.calleeMethod.equals(m)){						result.add(edge.callSite);result.add(edge.callSite);					}}				}}				return result;returnresult;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

321
			public Collection<TestMethod> getCalleesOfCallAt(Statement n) {publicCollection<TestMethod>getCalleesOfCallAt(Statementn){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

322
				List<TestMethod> result = Lists.newLinkedList();List<TestMethod>result=Lists.newLinkedList();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

323

324

325

326

327

328

329

330

331
				for (CallEdge edge : callEdges) {for(CallEdgeedge:callEdges){					if (edge.includeInCfg && edge.callSite.equals(n)) {if(edge.includeInCfg&&edge.callSite.equals(n)){						result.add(edge.destinationMethod);result.add(edge.destinationMethod);					}}				}}				return result;returnresult;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

332
			public Set<Statement> allNonCallStartNodes() {publicSet<Statement>allNonCallStartNodes(){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
				throw new IllegalStateException();thrownewIllegalStateException();			}}		};};	}}	public void assertAllFlowFunctionsUsed() {publicvoidassertAllFlowFunctionsUsed(){		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),assertTrue("These Flow Functions were expected, but never used: \n"+Joiner.on(",\n").join(remainingFlowFunctions),				remainingFlowFunctions.isEmpty());remainingFlowFunctions.isEmpty());	}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

343
	private void addOrVerifyStmt2Method(Statement stmt, TestMethod m) {privatevoidaddOrVerifyStmt2Method(Statementstmt,TestMethodm){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

344

345

346

347

348

349
		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {if(stmt2method.containsKey(stmt)&&!stmt2method.get(stmt).equals(m)){			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));thrownewIllegalArgumentException("Statement "+stmt+" is used in multiple methods: "+m+" and "+stmt2method.get(stmt));		}}		stmt2method.put(stmt, m);stmt2method.put(stmt,m);	}}



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

350
	public MethodHelper method(TestMethod method) {publicMethodHelpermethod(TestMethodmethod){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

351

352

353

354

355

356
		MethodHelper h = new MethodHelper(method);MethodHelperh=newMethodHelper(method);		return h;returnh;	}}	public class MethodHelper {publicclassMethodHelper{



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

357
		private TestMethod method;privateTestMethodmethod;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

358




rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

359
		public MethodHelper(TestMethod method) {publicMethodHelper(TestMethodmethod){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

360

361

362

363

364
			this.method = method;this.method=method;		}}		public void edges(Collection<Edge> edges) {publicvoidedges(Collection<Edge>edges){			for(Edge edge : edges) {for(Edgeedge:edges){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

365
				for(ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {for(ExpectedFlowFunction<TestFact>ff:edge.flowFunctions){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

366

367
					if(!remainingFlowFunctions.contains(ff))if(!remainingFlowFunctions.contains(ff))						remainingFlowFunctions.add(ff, ff.times);remainingFlowFunctions.add(ff,ff.times);



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
				}}								edge.accept(new EdgeVisitor() {edge.accept(newEdgeVisitor(){					@Override@Override					public void visit(ReturnEdge edge) {publicvoidvisit(ReturnEdgeedge){						addOrVerifyStmt2Method(edge.exitStmt, method);addOrVerifyStmt2Method(edge.exitStmt,method);						edge.calleeMethod = method;edge.calleeMethod=method;						returnEdges.add(edge);returnEdges.add(edge);					}}										@Override@Override					public void visit(Call2ReturnEdge edge) {publicvoidvisit(Call2ReturnEdgeedge){						addOrVerifyStmt2Method(edge.callSite, method);addOrVerifyStmt2Method(edge.callSite,method);						addOrVerifyStmt2Method(edge.returnSite, method);addOrVerifyStmt2Method(edge.returnSite,method);						call2retEdges.add(edge);call2retEdges.add(edge);					}}										@Override@Override					public void visit(CallEdge edge) {publicvoidvisit(CallEdgeedge){						addOrVerifyStmt2Method(edge.callSite, method);addOrVerifyStmt2Method(edge.callSite,method);						callEdges.add(edge);callEdges.add(edge);					}}										@Override@Override					public void visit(NormalEdge edge) {publicvoidvisit(NormalEdgeedge){						addOrVerifyStmt2Method(edge.unit, method);addOrVerifyStmt2Method(edge.unit,method);						addOrVerifyStmt2Method(edge.succUnit, method);addOrVerifyStmt2Method(edge.succUnit,method);						normalEdges.add(edge);normalEdges.add(edge);					}}				});});			}}		}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

401
		public void startPoints(Statement[] startingPoints) {publicvoidstartPoints(Statement[]startingPoints){



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
			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));method2startPoint.putAll(method,Lists.newArrayList(startingPoints));		}}	}}	



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

406
	private static String expectedFlowFunctionsToString(ExpectedFlowFunction<TestFact>[] flowFunctions) {privatestaticStringexpectedFlowFunctionsToString(ExpectedFlowFunction<TestFact>[]flowFunctions){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

407
		String result = "";Stringresult="";



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

408
		for(ExpectedFlowFunction<TestFact> ff : flowFunctions)for(ExpectedFlowFunction<TestFact>ff:flowFunctions)



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

409

410

411

412
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";result+=ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";		return result;returnresult;	}}	



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

413

414

415

416

417

418

419
	private static boolean nullAwareEquals(Object a, Object b) {privatestaticbooleannullAwareEquals(Objecta,Objectb){		if(a == null)if(a==null)			return b==null;returnb==null;		elseelse			return a.equals(b);returna.equals(b);	}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

420

421
	public FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions() {publicFlowFunctions<Statement,String,TestFact,TestMethod>flowFunctions(){		return new FlowFunctions<Statement, String, TestFact, TestMethod>() {returnnewFlowFunctions<Statement,String,TestFact,TestMethod>(){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

422

423
			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

424
			public FlowFunction<String, TestFact, Statement, TestMethod> getReturnFlowFunction(Statement callSite, TestMethod calleeMethod, Statement exitStmt, Statement returnSite) {publicFlowFunction<String,TestFact,Statement,TestMethod>getReturnFlowFunction(StatementcallSite,TestMethodcalleeMethod,StatementexitStmt,StatementreturnSite){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
				for (final ReturnEdge edge : returnEdges) {for(finalReturnEdgeedge:returnEdges){					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)if(nullAwareEquals(callSite,edge.callSite)&&edge.calleeMethod.equals(calleeMethod)							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {&&edge.exitStmt.equals(exitStmt)&&nullAwareEquals(edge.returnSite,returnSite)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,thrownewAssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)",exitStmt,						returnSite, callSite, calleeMethod));returnSite,callSite,calleeMethod));			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

436
			public FlowFunction<String, TestFact, Statement, TestMethod> getNormalFlowFunction(final Statement curr) {publicFlowFunction<String,TestFact,Statement,TestMethod>getNormalFlowFunction(finalStatementcurr){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

437
				for (final NormalEdge edge : normalEdges) {for(finalNormalEdgeedge:normalEdges){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

438
					if (edge.unit.equals(curr)) {if(edge.unit.equals(curr)){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

439

440

441
						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

442
				throw new AssertionError(String.format("No Flow Function expected for %s", curr));thrownewAssertionError(String.format("No Flow Function expected for %s",curr));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

443

444

445
			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

446
			public FlowFunction<String, TestFact, Statement, TestMethod> getCallToReturnFlowFunction(Statement callSite, Statement returnSite) {publicFlowFunction<String,TestFact,Statement,TestMethod>getCallToReturnFlowFunction(StatementcallSite,StatementreturnSite){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

447

448

449

450

451

452

453

454

455
				for (final Call2ReturnEdge edge : call2retEdges) {for(finalCall2ReturnEdgeedge:call2retEdges){					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {if(edge.callSite.equals(callSite)&&edge.returnSite.equals(returnSite)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));thrownewAssertionError(String.format("No Flow Function expected for call to return edge %s -> %s",callSite,returnSite));			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

456
			public FlowFunction<String, TestFact, Statement, TestMethod> getCallFlowFunction(Statement callStmt, TestMethod destinationMethod) {publicFlowFunction<String,TestFact,Statement,TestMethod>getCallFlowFunction(StatementcallStmt,TestMethoddestinationMethod){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

457

458

459

460

461

462

463

464
				for (final CallEdge edge : callEdges) {for(finalCallEdgeedge:callEdges){					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {if(edge.callSite.equals(callStmt)&&edge.destinationMethod.equals(destinationMethod)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));thrownewAssertionError(String.format("No Flow Function expected for call %s -> %s",callStmt,destinationMethod));			}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

465

466
			private FlowFunction<String, TestFact, Statement, TestMethod> createFlowFunction(final Edge edge) {privateFlowFunction<String,TestFact,Statement,TestMethod>createFlowFunction(finalEdgeedge){				return new FlowFunction<String, TestFact, Statement, TestMethod>() {returnnewFlowFunction<String,TestFact,Statement,TestMethod>(){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

467
					@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

468

469

470
					public Set<FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod>> computeTargets(TestFact source,publicSet<FlowFunction.ConstrainedFact<String,TestFact,Statement,TestMethod>>computeTargets(TestFactsource,							AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler) {AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler){						Set<ConstrainedFact<String, TestFact, Statement, TestMethod>> result = Sets.newHashSet();Set<ConstrainedFact<String,TestFact,Statement,TestMethod>>result=Sets.newHashSet();



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

471
						boolean found = false;booleanfound=false;



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

472
						for (ExpectedFlowFunction<TestFact> ff : edge.flowFunctions) {for(ExpectedFlowFunction<TestFact>ff:edge.flowFunctions){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

473
							if (ff.source.equals(source)) {if(ff.source.equals(source)){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

474
								if (remainingFlowFunctions.remove(ff)) {if(remainingFlowFunctions.remove(ff)){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

475

476
									for(TestFact target : ff.targets) {for(TestFacttarget:ff.targets){										result.add(ff.apply(target, accPathHandler));result.add(ff.apply(target,accPathHandler));



abstract at return edges


 

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges


 

 

abstract at return edges

 

Johannes Lerch
committed
Feb 23, 2015

477
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

478
									found = true;found=true;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

479

480

481

482

483
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

484

485

486

487
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

488

489

490

491

492

493

494
					}}				};};			}}		};};	}}	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {publicvoidrunSolver(finalbooleanfollowReturnsPastSeeds,finalString...initialSeeds){



bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver


 

 

bidi solver

 

Johannes Lerch
committed
Mar 20, 2015

495
		Scheduler scheduler = new Scheduler();Schedulerscheduler=newScheduler();



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

496
		FieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, Statement, TestMethod, InterproceduralCFG<Statement,TestMethod>>(FieldSensitiveIFDSSolver<String,TestFact,Statement,TestMethod,InterproceduralCFG<Statement,TestMethod>>solver=newFieldSensitiveIFDSSolver<String,TestFact,Statement,TestMethod,InterproceduralCFG<Statement,TestMethod>>(



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

497

498

499

500

501

502

503

504

505
				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {createTabulationProblem(followReturnsPastSeeds,initialSeeds),newFactMergeHandler<TestFact>(){					@Override@Override					public void merge(TestFact previousFact, TestFact currentFact) {publicvoidmerge(TestFactpreviousFact,TestFactcurrentFact){					}}					@Override@Override					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {publicvoidrestoreCallingContext(TestFactfactAtReturnSite,TestFactfactAtCallSite){					}}					



bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver


 

 

bidi solver

 

Johannes Lerch
committed
Mar 20, 2015

506
				}, debugger, scheduler);},debugger,scheduler);



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

507
		addExpectationsToDebugger();addExpectationsToDebugger();



bidi solver


 

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver


 

 

bidi solver

 

Johannes Lerch
committed
Mar 20, 2015

508
		scheduler.runAndAwaitCompletion();scheduler.runAndAwaitCompletion();



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

509
		



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

510

511

512
		assertAllFlowFunctionsUsed();assertAllFlowFunctionsUsed();	}}	



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

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
	private void addExpectationsToDebugger() {privatevoidaddExpectationsToDebugger(){		for(NormalEdge edge : normalEdges) {for(NormalEdgeedge:normalEdges){			debugger.expectNormalFlow(edge.unit, expectedFlowFunctionsToString(edge.flowFunctions));debugger.expectNormalFlow(edge.unit,expectedFlowFunctionsToString(edge.flowFunctions));		}}		for(CallEdge edge : callEdges) {for(CallEdgeedge:callEdges){			debugger.expectCallFlow(edge.callSite, edge.destinationMethod, expectedFlowFunctionsToString(edge.flowFunctions));debugger.expectCallFlow(edge.callSite,edge.destinationMethod,expectedFlowFunctionsToString(edge.flowFunctions));		}}		for(Call2ReturnEdge edge : call2retEdges) {for(Call2ReturnEdgeedge:call2retEdges){			debugger.expectNormalFlow(edge.callSite, expectedFlowFunctionsToString(edge.flowFunctions));debugger.expectNormalFlow(edge.callSite,expectedFlowFunctionsToString(edge.flowFunctions));		}}		for(ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){			debugger.expectReturnFlow(edge.exitStmt, edge.returnSite, expectedFlowFunctionsToString(edge.flowFunctions));debugger.expectReturnFlow(edge.exitStmt,edge.returnSite,expectedFlowFunctionsToString(edge.flowFunctions));		}}	}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

528

529

530
	private IFDSTabulationProblem<Statement, String, TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {privateIFDSTabulationProblem<Statement,String,TestFact,TestMethod,InterproceduralCFG<Statement,TestMethod>>createTabulationProblem(finalbooleanfollowReturnsPastSeeds,finalString[]initialSeeds){		final InterproceduralCFG<Statement, TestMethod> icfg = buildIcfg();finalInterproceduralCFG<Statement,TestMethod>icfg=buildIcfg();		final FlowFunctions<Statement, String, TestFact, TestMethod> flowFunctions = flowFunctions();finalFlowFunctions<Statement,String,TestFact,TestMethod>flowFunctions=flowFunctions();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

531
		



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

532
		return new IFDSTabulationProblem<Statement,String,  TestFact, TestMethod, InterproceduralCFG<Statement, TestMethod>>() {returnnewIFDSTabulationProblem<Statement,String,TestFact,TestMethod,InterproceduralCFG<Statement,TestMethod>>(){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
			@Override@Override			public boolean followReturnsPastSeeds() {publicbooleanfollowReturnsPastSeeds(){				return followReturnsPastSeeds;returnfollowReturnsPastSeeds;			}}			@Override@Override			public boolean autoAddZero() {publicbooleanautoAddZero(){				return false;returnfalse;			}}			@Override@Override			public int numThreads() {publicintnumThreads(){				return 1;return1;			}}			@Override@Override			public boolean computeValues() {publicbooleancomputeValues(){				return false;returnfalse;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

555
			public FlowFunctions<Statement,String,  TestFact, TestMethod> flowFunctions() {publicFlowFunctions<Statement,String,TestFact,TestMethod>flowFunctions(){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

556

557

558

559
				return flowFunctions;returnflowFunctions;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

560
			public InterproceduralCFG<Statement, TestMethod> interproceduralCFG() {publicInterproceduralCFG<Statement,TestMethod>interproceduralCFG(){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

561

562

563

564
				return icfg;returnicfg;			}}			@Override@Override



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

565

566
			public Map<Statement, Set<TestFact>> initialSeeds() {publicMap<Statement,Set<TestFact>>initialSeeds(){				Map<Statement, Set<TestFact>> result = Maps.newHashMap();Map<Statement,Set<TestFact>>result=Maps.newHashMap();



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

567
				for (String stmt : initialSeeds) {for(Stringstmt:initialSeeds){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

568
					result.put(new Statement(stmt), Sets.newHashSet(new TestFact("0")));result.put(newStatement(stmt),Sets.newHashSet(newTestFact("0")));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

569

570

571

572

573
				}}				return result;returnresult;			}}			@Override@Override



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

574

575

576

577

578
			public TestFact zeroValue() {publicTestFactzeroValue(){				return new TestFact("0");returnnewTestFact("0");			}}						@Override@Override



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

579

580
			public ZeroHandler<String> zeroHandler() {publicZeroHandler<String>zeroHandler(){				return new ZeroHandler<String>() {returnnewZeroHandler<String>(){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

581
					@Override@Override



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

582
					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {publicbooleanshouldGenerateAccessPath(AccessPath<String>accPath){



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

583

584

585
						return true;returntrue;					}}				};};



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

586

587

588
			}}		};};	}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

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
		public static enum TabulationProblemExchange {AsSpecified, ExchangeForwardAndBackward};publicstaticenumTabulationProblemExchange{AsSpecified,ExchangeForwardAndBackward};	public void runBiDiSolver(FieldSensitiveTestHelper backwardHelper, TabulationProblemExchange direction, final String...initialSeeds) {publicvoidrunBiDiSolver(FieldSensitiveTestHelperbackwardHelper,TabulationProblemExchangedirection,finalString...initialSeeds){		FactMergeHandler<TestFact> factMergeHandler = new FactMergeHandler<TestFact>() {FactMergeHandler<TestFact>factMergeHandler=newFactMergeHandler<TestFact>(){			@Override@Override			public void merge(TestFact previousFact, TestFact currentFact) {publicvoidmerge(TestFactpreviousFact,TestFactcurrentFact){			}}			@Override@Override			public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {publicvoidrestoreCallingContext(TestFactfactAtReturnSite,TestFactfactAtCallSite){			}}					};};		Scheduler scheduler = new Scheduler();Schedulerscheduler=newScheduler();		BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>> solver =BiDiFieldSensitiveIFDSSolver<String,TestFact,Statement,TestMethod,InterproceduralCFG<Statement,TestMethod>>solver=				direction == TabulationProblemExchange.AsSpecified ? direction==TabulationProblemExchange.AsSpecified?



switching to Java 6 compatibility


 

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility


 

 

switching to Java 6 compatibility

 

Johannes Lerch
committed
Jun 01, 2015

605

606

607

608

609

610
				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(newBiDiFieldSensitiveIFDSSolver<String,TestFact,Statement,TestMethod,InterproceduralCFG<Statement,TestMethod>>(						createTabulationProblem(true, initialSeeds), createTabulationProblem(true,initialSeeds),						backwardHelper.createTabulationProblem(true, initialSeeds),backwardHelper.createTabulationProblem(true,initialSeeds),						factMergeHandler, debugger, scheduler) :factMergeHandler,debugger,scheduler):				new BiDiFieldSensitiveIFDSSolver<String, TestFact, Statement, TestMethod, InterproceduralCFG<Statement, TestMethod>>(newBiDiFieldSensitiveIFDSSolver<String,TestFact,Statement,TestMethod,InterproceduralCFG<Statement,TestMethod>>(						backwardHelper.createTabulationProblem(true, initialSeeds), backwardHelper.createTabulationProblem(true,initialSeeds),



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

611

612

613

614

615

616

617
						createTabulationProblem(true, initialSeeds),createTabulationProblem(true,initialSeeds),						factMergeHandler, debugger, scheduler);factMergeHandler,debugger,scheduler);				scheduler.runAndAwaitCompletion();scheduler.runAndAwaitCompletion();		assertAllFlowFunctionsUsed();assertAllFlowFunctionsUsed();		backwardHelper.assertAllFlowFunctionsUsed();backwardHelper.assertAllFlowFunctionsUsed();	}}



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
}}





