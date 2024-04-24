



GitLab


















Projects
Groups
Topics
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
Topics
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
Topics
Snippets



GitLab








GitLab









Projects
Groups
Topics
Snippets






Projects
Groups
Topics
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


Contributor statistics


Graph


Compare revisions







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


Artifacts


Schedules







Deployments




Deployments




Environments


Releases







Packages and registries




Packages and registries




Model experiments







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












Joshua Garcia heros

e60e669d8960a04c584fcc00bb971e7e34b3cce5



















heros


test


heros


alias


TestHelper.java




Find file



Normal view


History


Permalink








TestHelper.java



22.4 KiB









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








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



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




106


107



	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



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



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);








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
				return "prepend("+fieldName+")";
			}
		};








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




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



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);








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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



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




195


196



	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();








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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
					}
				}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



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




617



		Scheduler scheduler = new Scheduler();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



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




628



				}, debugger, scheduler);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
	}
	
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



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


Contributor statistics


Graph


Compare revisions







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


Artifacts


Schedules







Deployments




Deployments




Environments


Releases







Packages and registries




Packages and registries




Model experiments







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


Contributor statistics


Graph


Compare revisions






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

Contributor statistics


Contributor statistics

Graph


Graph

Compare revisions


Compare revisions




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


Artifacts


Schedules






CI/CD


CI/CD




CI/CD


Pipelines


Pipelines

Jobs


Jobs

Artifacts


Artifacts

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




Packages and registries




Packages and registries




Model experiments






Packages and registries


Packages and registries




Packages and registries


Model experiments


Model experiments




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








Joshua Garcia heros

e60e669d8960a04c584fcc00bb971e7e34b3cce5



















heros


test


heros


alias


TestHelper.java




Find file



Normal view


History


Permalink








TestHelper.java



22.4 KiB









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








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



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




106


107



	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



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



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);








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
				return "prepend("+fieldName+")";
			}
		};








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




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



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);








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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



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




195


196



	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();








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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
					}
				}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



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




617



		Scheduler scheduler = new Scheduler();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



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




628



				}, debugger, scheduler);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
	}
	
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



			}
		};
	}
}
















Joshua Garcia heros

e60e669d8960a04c584fcc00bb971e7e34b3cce5












Joshua Garcia heros

e60e669d8960a04c584fcc00bb971e7e34b3cce5










Joshua Garcia heros

e60e669d8960a04c584fcc00bb971e7e34b3cce5




Joshua Garciaherosheros
e60e669d8960a04c584fcc00bb971e7e34b3cce5












heros


test


heros


alias


TestHelper.java




Find file



Normal view


History


Permalink








TestHelper.java



22.4 KiB









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








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



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




106


107



	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



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



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);








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
				return "prepend("+fieldName+")";
			}
		};








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




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



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);








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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



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




195


196



	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();








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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
					}
				}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



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




617



		Scheduler scheduler = new Scheduler();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



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




628



				}, debugger, scheduler);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
	}
	
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



			}
		};
	}
}

















heros


test


heros


alias


TestHelper.java




Find file



Normal view


History


Permalink








TestHelper.java



22.4 KiB









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








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



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




106


107



	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



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



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);








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
				return "prepend("+fieldName+")";
			}
		};








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




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



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);








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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



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




195


196



	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();








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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
					}
				}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



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




617



		Scheduler scheduler = new Scheduler();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



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




628



				}, debugger, scheduler);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
	}
	
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



			}
		};
	}
}













heros


test


heros


alias


TestHelper.java




Find file



Normal view


History


Permalink








heros


test


heros


alias


TestHelper.java





heros

test

heros

alias

TestHelper.java

Find file



Normal view


History


Permalink


Find file


Normal view

History

Permalink





TestHelper.java



22.4 KiB









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








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



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




106


107



	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



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



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);








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
				return "prepend("+fieldName+")";
			}
		};








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




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



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);








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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



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




195


196



	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();








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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
					}
				}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



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




617



		Scheduler scheduler = new Scheduler();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



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




628



				}, debugger, scheduler);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
	}
	
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



			}
		};
	}
}









TestHelper.java



22.4 KiB










TestHelper.java



22.4 KiB









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








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



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




106


107



	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



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



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);








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
				return "prepend("+fieldName+")";
			}
		};








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




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



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);








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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



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




195


196



	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();








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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
					}
				}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



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




617



		Scheduler scheduler = new Scheduler();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



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




628



				}, debugger, scheduler);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
	}
	
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



			}
		};
	}
}











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








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



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




106


107



	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



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



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);








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
				return "prepend("+fieldName+")";
			}
		};








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




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



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);








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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



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




195


196



	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();








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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
					}
				}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



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




617



		Scheduler scheduler = new Scheduler();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



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




628



				}, debugger, scheduler);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
	}
	
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



			}
		};
	}
}









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








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



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




106


107



	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



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



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);








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
				return "prepend("+fieldName+")";
			}
		};








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




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



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);








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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



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




195


196



	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();








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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514












rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
					}
				}








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



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




617



		Scheduler scheduler = new Scheduler();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



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




628



				}, debugger, scheduler);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
	}
	
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



			}
		};
	}
}







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

/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import static org.junit.Assert.assertTrue;importstaticorg.junit.Assert.assertTrue;




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




14



import heros.alias.FlowFunction.ConstrainedFact;






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


14


import heros.alias.FlowFunction.ConstrainedFact;

import heros.alias.FlowFunction.ConstrainedFact;importheros.alias.FlowFunction.ConstrainedFact;




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




15



import heros.alias.FlowFunction.Constraint;






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


15


import heros.alias.FlowFunction.Constraint;

import heros.alias.FlowFunction.Constraint;importheros.alias.FlowFunction.Constraint;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16


17


18



import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


16


17


18


import heros.alias.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;

import heros.alias.IFDSTabulationProblem;importheros.alias.IFDSTabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;import heros.solver.IFDSSolver;importheros.solver.IFDSSolver;




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




19



import heros.solver.Pair;






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


19


import heros.solver.Pair;

import heros.solver.Pair;importheros.solver.Pair;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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


import java.util.Collection;importjava.util.Collection;import java.util.LinkedList;importjava.util.LinkedList;import java.util.List;importjava.util.List;import java.util.Map;importjava.util.Map;import java.util.Set;importjava.util.Set;import com.google.common.base.Joiner;importcom.google.common.base.Joiner;import com.google.common.base.Predicate;importcom.google.common.base.Predicate;import com.google.common.collect.HashMultimap;importcom.google.common.collect.HashMultimap;import com.google.common.collect.HashMultiset;importcom.google.common.collect.HashMultiset;import com.google.common.collect.Iterables;importcom.google.common.collect.Iterables;import com.google.common.collect.Lists;importcom.google.common.collect.Lists;import com.google.common.collect.Maps;importcom.google.common.collect.Maps;import com.google.common.collect.Multimap;importcom.google.common.collect.Multimap;import com.google.common.collect.Multiset;importcom.google.common.collect.Multiset;import com.google.common.collect.Sets;importcom.google.common.collect.Sets;public class TestHelper {publicclassTestHelper{




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




40



	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


40


	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();

	private Multimap<TestMethod, TestStatement> method2startPoint = HashMultimap.create();privateMultimap<TestMethod,TestStatement>method2startPoint=HashMultimap.create();




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




41


42


43


44



	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


41


42


43


44


	private List<NormalEdge> normalEdges = Lists.newLinkedList();
	private List<CallEdge> callEdges = Lists.newLinkedList();
	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();
	private List<ReturnEdge> returnEdges = Lists.newLinkedList();

	private List<NormalEdge> normalEdges = Lists.newLinkedList();privateList<NormalEdge>normalEdges=Lists.newLinkedList();	private List<CallEdge> callEdges = Lists.newLinkedList();privateList<CallEdge>callEdges=Lists.newLinkedList();	private List<Call2ReturnEdge> call2retEdges = Lists.newLinkedList();privateList<Call2ReturnEdge>call2retEdges=Lists.newLinkedList();	private List<ReturnEdge> returnEdges = Lists.newLinkedList();privateList<ReturnEdge>returnEdges=Lists.newLinkedList();




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


45


	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();

	private Map<TestStatement, TestMethod> stmt2method = Maps.newHashMap();privateMap<TestStatement,TestMethod>stmt2method=Maps.newHashMap();




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




46



	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


46


	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();

	private Multiset<ExpectedFlowFunction> remainingFlowFunctions = HashMultiset.create();privateMultiset<ExpectedFlowFunction>remainingFlowFunctions=HashMultiset.create();




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


47


	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;

	private TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger;privateTestDebugger<String,TestFact,TestStatement,TestMethod,InterproceduralCFG<TestStatement,TestMethod>>debugger;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




48










FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


48









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


49


	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {

	public TestHelper(TestDebugger<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> debugger) {publicTestHelper(TestDebugger<String,TestFact,TestStatement,TestMethod,InterproceduralCFG<TestStatement,TestMethod>>debugger){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




50


51


52


53


54



		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


50


51


52


53


54


		this.debugger = debugger;
	}

	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {
		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));

		this.debugger = debugger;this.debugger=debugger;	}}	public MethodHelper method(String methodName, TestStatement[] startingPoints, EdgeBuilder... edgeBuilders) {publicMethodHelpermethod(StringmethodName,TestStatement[]startingPoints,EdgeBuilder...edgeBuilders){		MethodHelper methodHelper = new MethodHelper(new TestMethod(methodName));MethodHelpermethodHelper=newMethodHelper(newTestMethod(methodName));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




55


56


57


58


59


60


61



		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
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


55


56


57


58


59


60


61


		methodHelper.startPoints(startingPoints);
		for(EdgeBuilder edgeBuilder : edgeBuilders){
			methodHelper.edges(edgeBuilder.edges());
		}
		return methodHelper;
	}


		methodHelper.startPoints(startingPoints);methodHelper.startPoints(startingPoints);		for(EdgeBuilder edgeBuilder : edgeBuilders){for(EdgeBuilderedgeBuilder:edgeBuilders){			methodHelper.edges(edgeBuilder.edges());methodHelper.edges(edgeBuilder.edges());		}}		return methodHelper;returnmethodHelper;	}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




62


63



	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


62


63


	public static TestStatement[] startPoints(String... startingPoints) {
		TestStatement[] result = new TestStatement[startingPoints.length];

	public static TestStatement[] startPoints(String... startingPoints) {publicstaticTestStatement[]startPoints(String...startingPoints){		TestStatement[] result = new TestStatement[startingPoints.length];TestStatement[]result=newTestStatement[startingPoints.length];




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




64



		for (int i = 0; i < result.length; i++) {






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


64


		for (int i = 0; i < result.length; i++) {

		for (int i = 0; i < result.length; i++) {for(inti=0;i<result.length;i++){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




65



			result[i] = new TestStatement(startingPoints[i]);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


65


			result[i] = new TestStatement(startingPoints[i]);

			result[i] = new TestStatement(startingPoints[i]);result[i]=newTestStatement(startingPoints[i]);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




66


67


68


69



		}
		return result;
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


66


67


68


69


		}
		return result;
	}


		}}		return result;returnresult;	}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




70


71



	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


70


71


	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {
		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);

	public static EdgeBuilder.NormalStmtBuilder normalStmt(String stmt, ExpectedFlowFunction...flowFunctions) {publicstaticEdgeBuilder.NormalStmtBuildernormalStmt(Stringstmt,ExpectedFlowFunction...flowFunctions){		return new EdgeBuilder.NormalStmtBuilder(new TestStatement(stmt), flowFunctions);returnnewEdgeBuilder.NormalStmtBuilder(newTestStatement(stmt),flowFunctions);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




72


73


74



	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {






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


73


74


	}
	
	public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {

	}}		public static EdgeBuilder.CallSiteBuilder callSite(String callSite) {publicstaticEdgeBuilder.CallSiteBuildercallSite(StringcallSite){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




75



		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


75


		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));

		return new EdgeBuilder.CallSiteBuilder(new TestStatement(callSite));returnnewEdgeBuilder.CallSiteBuilder(newTestStatement(callSite));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




76


77


78



	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {






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


77


78


	}
	
	public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {

	}}		public static EdgeBuilder.ExitStmtBuilder exitStmt(String exitStmt) {publicstaticEdgeBuilder.ExitStmtBuilderexitStmt(StringexitStmt){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




79



		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


79


		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));

		return new EdgeBuilder.ExitStmtBuilder(new TestStatement(exitStmt));returnnewEdgeBuilder.ExitStmtBuilder(newTestStatement(exitStmt));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




80


81



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


80


81


	}
	

	}}	




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




82


83



	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


82


83


	public static TestStatement over(String callSite) {
		return new TestStatement(callSite);

	public static TestStatement over(String callSite) {publicstaticTestStatementover(StringcallSite){		return new TestStatement(callSite);returnnewTestStatement(callSite);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




84


85



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


84


85


	}
	

	}}	




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


86


87


	public static TestStatement to(String returnSite) {
		return new TestStatement(returnSite);

	public static TestStatement to(String returnSite) {publicstaticTestStatementto(StringreturnSite){		return new TestStatement(returnSite);returnnewTestStatement(returnSite);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




88


89


90


91


92


93


94



	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


88


89


90


91


92


93


94


	}
	
	public static ExpectedFlowFunction kill(String source) {
		return kill(1, source);
	}
	
	public static ExpectedFlowFunction kill(int times, String source) {

	}}		public static ExpectedFlowFunction kill(String source) {publicstaticExpectedFlowFunctionkill(Stringsource){		return kill(1, source);returnkill(1,source);	}}		public static ExpectedFlowFunction kill(int times, String source) {publicstaticExpectedFlowFunctionkill(inttimes,Stringsource){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




95


96



		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


95


96


		return new ExpectedFlowFunction(times, new TestFact(source)) {
			@Override

		return new ExpectedFlowFunction(times, new TestFact(source)) {returnnewExpectedFlowFunction(times,newTestFact(source)){			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




97



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


97


			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {

			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,TestStatement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




98


99


100


101


102


103


104


105



				throw new IllegalStateException();
			}
			
			@Override
			public String transformerString() {
				return "";
			}
		};






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


98


99


100


101


102


103


104


105


				throw new IllegalStateException();
			}
			
			@Override
			public String transformerString() {
				return "";
			}
		};

				throw new IllegalStateException();thrownewIllegalStateException();			}}						@Override@Override			public String transformerString() {publicStringtransformerString(){				return "";return"";			}}		};};




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




106


107



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


106


107


	}


	}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




108


109


110



	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


108


109


110


	public static AccessPathTransformer readField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override

	public static AccessPathTransformer readField(final String fieldName) {publicstaticAccessPathTransformerreadField(finalStringfieldName){		return new AccessPathTransformer() {returnnewAccessPathTransformer(){			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




111


112



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


111


112


			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.read(new String(fieldName)).generate(target);

			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,TestStatement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler){				return accPathHandler.read(new String(fieldName)).generate(target);returnaccPathHandler.read(newString(fieldName)).generate(target);




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




113


114


115


116


117


118


119



			}

			@Override
			public String toString() {
				return "read("+fieldName+")";
			}
		};






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


113


114


115


116


117


118


119


			}

			@Override
			public String toString() {
				return "read("+fieldName+")";
			}
		};

			}}			@Override@Override			public String toString() {publicStringtoString(){				return "read("+fieldName+")";return"read("+fieldName+")";			}}		};};




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



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


120


121


	}
	

	}}	




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123


124



	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override






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


	public static AccessPathTransformer prependField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override

	public static AccessPathTransformer prependField(final String fieldName) {publicstaticAccessPathTransformerprependField(finalStringfieldName){		return new AccessPathTransformer() {returnnewAccessPathTransformer(){			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




125


126



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


125


126


			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.prepend(new String(fieldName)).generate(target);

			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,TestStatement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler){				return accPathHandler.prepend(new String(fieldName)).generate(target);returnaccPathHandler.prepend(newString(fieldName)).generate(target);




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
				return "prepend("+fieldName+")";
			}
		};






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


			}
			
			@Override
			public String toString() {
				return "prepend("+fieldName+")";
			}
		};

			}}						@Override@Override			public String toString() {publicStringtoString(){				return "prepend("+fieldName+")";return"prepend("+fieldName+")";			}}		};};




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




134


135



	}
	






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


134


135


	}
	

	}}	




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




136


137


138



	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override






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


	public static AccessPathTransformer overwriteField(final String fieldName) {
		return new AccessPathTransformer() {
			@Override

	public static AccessPathTransformer overwriteField(final String fieldName) {publicstaticAccessPathTransformeroverwriteField(finalStringfieldName){		return new AccessPathTransformer() {returnnewAccessPathTransformer(){			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




139


140



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


139


140


			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
				return accPathHandler.overwrite(new String(fieldName)).generate(target);

			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,TestStatement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler){				return accPathHandler.overwrite(new String(fieldName)).generate(target);returnaccPathHandler.overwrite(newString(fieldName)).generate(target);




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


148


149


150


151



			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);






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


148


149


150


151


			}
			
			@Override
			public String toString() {
				return "write("+fieldName+")";
			}
		};
	}
	
	public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {
		return flow(1, source, transformer, targets);

			}}						@Override@Override			public String toString() {publicStringtoString(){				return "write("+fieldName+")";return"write("+fieldName+")";			}}		};};	}}		public static ExpectedFlowFunction flow(String source, final AccessPathTransformer transformer, String... targets) {publicstaticExpectedFlowFunctionflow(Stringsource,finalAccessPathTransformertransformer,String...targets){		return flow(1, source, transformer, targets);returnflow(1,source,transformer,targets);




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




152


153



	}
	






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


152


153


	}
	

	}}	




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




154


155



	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


154


155


	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {
		TestFact[] targetFacts = new TestFact[targets.length];

	public static ExpectedFlowFunction flow(int times, String source, final AccessPathTransformer transformer, String... targets) {publicstaticExpectedFlowFunctionflow(inttimes,Stringsource,finalAccessPathTransformertransformer,String...targets){		TestFact[] targetFacts = new TestFact[targets.length];TestFact[]targetFacts=newTestFact[targets.length];




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




156



		for(int i=0; i<targets.length; i++) {






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


156


		for(int i=0; i<targets.length; i++) {

		for(int i=0; i<targets.length; i++) {for(inti=0;i<targets.length;i++){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




157



			targetFacts[i] = new TestFact(targets[i]);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


157


			targetFacts[i] = new TestFact(targets[i]);

			targetFacts[i] = new TestFact(targets[i]);targetFacts[i]=newTestFact(targets[i]);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




158



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


158


		}

		}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


159


160


		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {
			@Override

		return new ExpectedFlowFunction(times, new TestFact(source), targetFacts) {returnnewExpectedFlowFunction(times,newTestFact(source),targetFacts){			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




161



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


161


			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {

			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,TestStatement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {







rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


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


				return transformer.apply(target, accPathHandler);
			}
			
			@Override
			public String transformerString() {
				return transformer.toString();
			}
		};
	}
	
	private static interface AccessPathTransformer {


				return transformer.apply(target, accPathHandler);returntransformer.apply(target,accPathHandler);			}}						@Override@Override			public String transformerString() {publicStringtransformerString(){				return transformer.toString();returntransformer.toString();			}}		};};	}}		private static interface AccessPathTransformer {privatestaticinterfaceAccessPathTransformer{




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




174



		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


174


		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); 

		ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler); ConstrainedFact<String,TestFact,TestStatement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler);




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




175



		






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


175


		

		




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




176


177



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


176


177


	}
	

	}}	




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




178


179


180


181


182



	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


178


179


180


181


182


	public static ExpectedFlowFunction flow(String source, String... targets) {
		return flow(1, source, targets);
	}
	
	public static ExpectedFlowFunction flow(int times, String source, String... targets) {

	public static ExpectedFlowFunction flow(String source, String... targets) {publicstaticExpectedFlowFunctionflow(Stringsource,String...targets){		return flow(1, source, targets);returnflow(1,source,targets);	}}		public static ExpectedFlowFunction flow(int times, String source, String... targets) {publicstaticExpectedFlowFunctionflow(inttimes,Stringsource,String...targets){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




183


184



		return flow(times, source, new AccessPathTransformer() {
			@Override






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


183


184


		return flow(times, source, new AccessPathTransformer() {
			@Override

		return flow(times, source, new AccessPathTransformer() {returnflow(times,source,newAccessPathTransformer(){			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




185



			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


185


			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {

			public ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {publicConstrainedFact<String,TestFact,TestStatement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




186


187


188


189


190


191


192


193


194



				return accPathHandler.generate(target);
			}
			
			@Override
			public String toString() {
				return "";
			}
			
		}, targets);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


186


187


188


189


190


191


192


193


194


				return accPathHandler.generate(target);
			}
			
			@Override
			public String toString() {
				return "";
			}
			
		}, targets);

				return accPathHandler.generate(target);returnaccPathHandler.generate(target);			}}						@Override@Override			public String toString() {publicStringtoString(){				return "";return"";			}}					}, targets);},targets);




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




195


196



	}
	






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


195


196


	}
	

	}}	




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




197


198


199


200



	public static int times(int times) {
		return times;
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


197


198


199


200


	public static int times(int times) {
		return times;
	}


	public static int times(int times) {publicstaticinttimes(inttimes){		return times;returntimes;	}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




201


202



	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


201


202


	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {
		return new InterproceduralCFG<TestStatement, TestMethod>() {

	public InterproceduralCFG<TestStatement, TestMethod> buildIcfg() {publicInterproceduralCFG<TestStatement,TestMethod>buildIcfg(){		return new InterproceduralCFG<TestStatement, TestMethod>() {returnnewInterproceduralCFG<TestStatement,TestMethod>(){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




203


204




			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


203


204



			@Override

			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




205



			public boolean isStartPoint(TestStatement stmt) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


205


			public boolean isStartPoint(TestStatement stmt) {

			public boolean isStartPoint(TestStatement stmt) {publicbooleanisStartPoint(TestStatementstmt){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




206


207


208


209



				return method2startPoint.values().contains(stmt);
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


206


207


208


209


				return method2startPoint.values().contains(stmt);
			}

			@Override

				return method2startPoint.values().contains(stmt);returnmethod2startPoint.values().contains(stmt);			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




210



			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


210


			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {

			public boolean isFallThroughSuccessor(TestStatement stmt, TestStatement succ) {publicbooleanisFallThroughSuccessor(TestStatementstmt,TestStatementsucc){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




211


212


213


214



				throw new IllegalStateException();
			}

			@Override






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


				throw new IllegalStateException();
			}

			@Override

				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215



			public boolean isExitStmt(TestStatement stmt) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


215


			public boolean isExitStmt(TestStatement stmt) {

			public boolean isExitStmt(TestStatement stmt) {publicbooleanisExitStmt(TestStatementstmt){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




216


217


218


219


220


221


222


223



				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


216


217


218


219


220


221


222


223


				for(ReturnEdge edge : returnEdges) {
					if(edge.exitStmt.equals(stmt))
						return true;
				}
				return false;
			}

			@Override

				for(ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if(edge.exitStmt.equals(stmt))if(edge.exitStmt.equals(stmt))						return true;returntrue;				}}				return false;returnfalse;			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




224



			public boolean isCallStmt(final TestStatement stmt) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


224


			public boolean isCallStmt(final TestStatement stmt) {

			public boolean isCallStmt(final TestStatement stmt) {publicbooleanisCallStmt(finalTestStatementstmt){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




225


226


227


228


229


230


231


232


233



				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override






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


229


230


231


232


233


				return Iterables.any(callEdges, new Predicate<CallEdge>() {
					@Override
					public boolean apply(CallEdge edge) {
						return edge.callSite.equals(stmt);
					}
				});
			}

			@Override

				return Iterables.any(callEdges, new Predicate<CallEdge>() {returnIterables.any(callEdges,newPredicate<CallEdge>(){					@Override@Override					public boolean apply(CallEdge edge) {publicbooleanapply(CallEdgeedge){						return edge.callSite.equals(stmt);returnedge.callSite.equals(stmt);					}}				});});			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




234



			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


234


			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {

			public boolean isBranchTarget(TestStatement stmt, TestStatement succ) {publicbooleanisBranchTarget(TestStatementstmt,TestStatementsucc){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




235


236


237


238



				throw new IllegalStateException();
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


235


236


237


238


				throw new IllegalStateException();
			}

			@Override

				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




239


240



			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


239


240


			public List<TestStatement> getSuccsOf(TestStatement n) {
				LinkedList<TestStatement> result = Lists.newLinkedList();

			public List<TestStatement> getSuccsOf(TestStatement n) {publicList<TestStatement>getSuccsOf(TestStatementn){				LinkedList<TestStatement> result = Lists.newLinkedList();LinkedList<TestStatement>result=Lists.newLinkedList();




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




241


242


243


244


245


246


247


248



				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


241


242


243


244


245


246


247


248


				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.unit.equals(n))
						result.add(edge.succUnit);
				}
				return result;
			}

			@Override

				for (NormalEdge edge : normalEdges) {for(NormalEdgeedge:normalEdges){					if (edge.includeInCfg && edge.unit.equals(n))if(edge.includeInCfg&&edge.unit.equals(n))						result.add(edge.succUnit);result.add(edge.succUnit);				}}				return result;returnresult;			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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



			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


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


			public List<TestStatement> getPredsOf(TestStatement stmt) {
				LinkedList<TestStatement> result = Lists.newLinkedList();
				for (NormalEdge edge : normalEdges) {
					if (edge.includeInCfg && edge.succUnit.equals(stmt))
						result.add(edge.unit);
				}
				return result;
			}

			@Override
			public Collection<TestStatement> getStartPointsOf(TestMethod m) {

			public List<TestStatement> getPredsOf(TestStatement stmt) {publicList<TestStatement>getPredsOf(TestStatementstmt){				LinkedList<TestStatement> result = Lists.newLinkedList();LinkedList<TestStatement>result=Lists.newLinkedList();				for (NormalEdge edge : normalEdges) {for(NormalEdgeedge:normalEdges){					if (edge.includeInCfg && edge.succUnit.equals(stmt))if(edge.includeInCfg&&edge.succUnit.equals(stmt))						result.add(edge.unit);result.add(edge.unit);				}}				return result;returnresult;			}}			@Override@Override			public Collection<TestStatement> getStartPointsOf(TestMethod m) {publicCollection<TestStatement>getStartPointsOf(TestMethodm){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




260


261


262


263



				return method2startPoint.get(m);
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


260


261


262


263


				return method2startPoint.get(m);
			}

			@Override

				return method2startPoint.get(m);returnmethod2startPoint.get(m);			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




264


265



			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


264


265


			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {
				Set<TestStatement> result = Sets.newHashSet();

			public Collection<TestStatement> getReturnSitesOfCallAt(TestStatement n) {publicCollection<TestStatement>getReturnSitesOfCallAt(TestStatementn){				Set<TestStatement> result = Sets.newHashSet();Set<TestStatement>result=Sets.newHashSet();




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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

				for (Call2ReturnEdge edge : call2retEdges) {for(Call2ReturnEdgeedge:call2retEdges){					if (edge.includeInCfg && edge.callSite.equals(n))if(edge.includeInCfg&&edge.callSite.equals(n))						result.add(edge.returnSite);result.add(edge.returnSite);				}}				for(ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if(edge.includeInCfg && edge.callSite.equals(n))if(edge.includeInCfg&&edge.callSite.equals(n))						result.add(edge.returnSite);result.add(edge.returnSite);				}}				return result;returnresult;			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




278


279


280


281


282



			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


278


279


280


281


282


			public TestMethod getMethodOf(TestStatement n) {
				if(stmt2method.containsKey(n))
					return stmt2method.get(n);
				else
					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");

			public TestMethod getMethodOf(TestStatement n) {publicTestMethodgetMethodOf(TestStatementn){				if(stmt2method.containsKey(n))if(stmt2method.containsKey(n))					return stmt2method.get(n);returnstmt2method.get(n);				elseelse					throw new IllegalArgumentException("Statement "+n+" is not defined in any method.");thrownewIllegalArgumentException("Statement "+n+" is not defined in any method.");




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




283


284


285



			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


283


284


285


			}

			@Override

			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




286



			public Set<TestStatement> getCallsFromWithin(TestMethod m) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


286


			public Set<TestStatement> getCallsFromWithin(TestMethod m) {

			public Set<TestStatement> getCallsFromWithin(TestMethod m) {publicSet<TestStatement>getCallsFromWithin(TestMethodm){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




287


288


289


290



				throw new IllegalStateException();
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


287


288


289


290


				throw new IllegalStateException();
			}

			@Override

				throw new IllegalStateException();thrownewIllegalStateException();			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




291


292



			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


291


292


			public Collection<TestStatement> getCallersOf(TestMethod m) {
				Set<TestStatement> result = Sets.newHashSet();

			public Collection<TestStatement> getCallersOf(TestMethod m) {publicCollection<TestStatement>getCallersOf(TestMethodm){				Set<TestStatement> result = Sets.newHashSet();Set<TestStatement>result=Sets.newHashSet();




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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

				for (CallEdge edge : callEdges) {for(CallEdgeedge:callEdges){					if (edge.includeInCfg && edge.destinationMethod.equals(m)) {if(edge.includeInCfg&&edge.destinationMethod.equals(m)){						result.add(edge.callSite);result.add(edge.callSite);					}}				}}				for (ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){					if (edge.includeInCfg && edge.calleeMethod.equals(m)) {if(edge.includeInCfg&&edge.calleeMethod.equals(m)){						result.add(edge.callSite);result.add(edge.callSite);					}}				}}				return result;returnresult;			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




307


308



			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


307


308


			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {
				List<TestMethod> result = Lists.newLinkedList();

			public Collection<TestMethod> getCalleesOfCallAt(TestStatement n) {publicCollection<TestMethod>getCalleesOfCallAt(TestStatementn){				List<TestMethod> result = Lists.newLinkedList();List<TestMethod>result=Lists.newLinkedList();




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



				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override






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


				for (CallEdge edge : callEdges) {
					if (edge.includeInCfg && edge.callSite.equals(n)) {
						result.add(edge.destinationMethod);
					}
				}
				return result;
			}

			@Override

				for (CallEdge edge : callEdges) {for(CallEdgeedge:callEdges){					if (edge.includeInCfg && edge.callSite.equals(n)) {if(edge.includeInCfg&&edge.callSite.equals(n)){						result.add(edge.destinationMethod);result.add(edge.destinationMethod);					}}				}}				return result;returnresult;			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




318



			public Set<TestStatement> allNonCallStartNodes() {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


318


			public Set<TestStatement> allNonCallStartNodes() {

			public Set<TestStatement> allNonCallStartNodes() {publicSet<TestStatement>allNonCallStartNodes(){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
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


				throw new IllegalStateException();
			}
		};
	}

	public void assertAllFlowFunctionsUsed() {
		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),
				remainingFlowFunctions.isEmpty());
	}


				throw new IllegalStateException();thrownewIllegalStateException();			}}		};};	}}	public void assertAllFlowFunctionsUsed() {publicvoidassertAllFlowFunctionsUsed(){		assertTrue("These Flow Functions were expected, but never used: \n" + Joiner.on(",\n").join(remainingFlowFunctions),assertTrue("These Flow Functions were expected, but never used: \n"+Joiner.on(",\n").join(remainingFlowFunctions),				remainingFlowFunctions.isEmpty());remainingFlowFunctions.isEmpty());	}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




329



	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


329


	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {

	private void addOrVerifyStmt2Method(TestStatement stmt, TestMethod m) {privatevoidaddOrVerifyStmt2Method(TestStatementstmt,TestMethodm){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




330


331


332


333


334


335



		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
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


330


331


332


333


334


335


		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {
			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));
		}
		stmt2method.put(stmt, m);
	}


		if (stmt2method.containsKey(stmt) && !stmt2method.get(stmt).equals(m)) {if(stmt2method.containsKey(stmt)&&!stmt2method.get(stmt).equals(m)){			throw new IllegalArgumentException("Statement " + stmt + " is used in multiple methods: " + m + " and " + stmt2method.get(stmt));thrownewIllegalArgumentException("Statement "+stmt+" is used in multiple methods: "+m+" and "+stmt2method.get(stmt));		}}		stmt2method.put(stmt, m);stmt2method.put(stmt,m);	}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



	public MethodHelper method(TestMethod method) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


336


	public MethodHelper method(TestMethod method) {

	public MethodHelper method(TestMethod method) {publicMethodHelpermethod(TestMethodmethod){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




337


338


339


340


341


342



		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


337


338


339


340


341


342


		MethodHelper h = new MethodHelper(method);
		return h;
	}

	public class MethodHelper {


		MethodHelper h = new MethodHelper(method);MethodHelperh=newMethodHelper(method);		return h;returnh;	}}	public class MethodHelper {publicclassMethodHelper{




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




343



		private TestMethod method;






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


343


		private TestMethod method;

		private TestMethod method;privateTestMethodmethod;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




344










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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




345



		public MethodHelper(TestMethod method) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


345


		public MethodHelper(TestMethod method) {

		public MethodHelper(TestMethod method) {publicMethodHelper(TestMethodmethod){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




346


347


348


349


350


351



			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


346


347


348


349


350


351


			this.method = method;
		}

		public void edges(Collection<Edge> edges) {
			for(Edge edge : edges) {
				for(ExpectedFlowFunction ff : edge.flowFunctions) {

			this.method = method;this.method=method;		}}		public void edges(Collection<Edge> edges) {publicvoidedges(Collection<Edge>edges){			for(Edge edge : edges) {for(Edgeedge:edges){				for(ExpectedFlowFunction ff : edge.flowFunctions) {for(ExpectedFlowFunctionff:edge.flowFunctions){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




352


353



					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


352


353


					if(!remainingFlowFunctions.contains(ff))
						remainingFlowFunctions.add(ff, ff.times);

					if(!remainingFlowFunctions.contains(ff))if(!remainingFlowFunctions.contains(ff))						remainingFlowFunctions.add(ff, ff.times);remainingFlowFunctions.add(ff,ff.times);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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


				}}								edge.accept(new EdgeVisitor() {edge.accept(newEdgeVisitor(){					@Override@Override					public void visit(ReturnEdge edge) {publicvoidvisit(ReturnEdgeedge){						addOrVerifyStmt2Method(edge.exitStmt, method);addOrVerifyStmt2Method(edge.exitStmt,method);						edge.calleeMethod = method;edge.calleeMethod=method;						returnEdges.add(edge);returnEdges.add(edge);					}}										@Override@Override					public void visit(Call2ReturnEdge edge) {publicvoidvisit(Call2ReturnEdgeedge){						addOrVerifyStmt2Method(edge.callSite, method);addOrVerifyStmt2Method(edge.callSite,method);						addOrVerifyStmt2Method(edge.returnSite, method);addOrVerifyStmt2Method(edge.returnSite,method);						call2retEdges.add(edge);call2retEdges.add(edge);					}}										@Override@Override					public void visit(CallEdge edge) {publicvoidvisit(CallEdgeedge){						addOrVerifyStmt2Method(edge.callSite, method);addOrVerifyStmt2Method(edge.callSite,method);						callEdges.add(edge);callEdges.add(edge);					}}										@Override@Override					public void visit(NormalEdge edge) {publicvoidvisit(NormalEdgeedge){						addOrVerifyStmt2Method(edge.unit, method);addOrVerifyStmt2Method(edge.unit,method);						addOrVerifyStmt2Method(edge.succUnit, method);addOrVerifyStmt2Method(edge.succUnit,method);						normalEdges.add(edge);normalEdges.add(edge);					}}				});});			}}		}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



		public void startPoints(TestStatement[] startingPoints) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


387


		public void startPoints(TestStatement[] startingPoints) {

		public void startPoints(TestStatement[] startingPoints) {publicvoidstartPoints(TestStatement[]startingPoints){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




388


389


390


391



			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
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


388


389


390


391


			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));
		}
	}
	

			method2startPoint.putAll(method, Lists.newArrayList(startingPoints));method2startPoint.putAll(method,Lists.newArrayList(startingPoints));		}}	}}	




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




392


393


394


395


396


397


398


399



	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


392


393


394


395


396


397


398


399


	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {
		String result = "";
		for(ExpectedFlowFunction ff : flowFunctions)
			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";
		return result;
	}
	
	public static abstract class ExpectedFlowFunction {

	private static String expectedFlowFunctionsToString(ExpectedFlowFunction[] flowFunctions) {privatestaticStringexpectedFlowFunctionsToString(ExpectedFlowFunction[]flowFunctions){		String result = "";Stringresult="";		for(ExpectedFlowFunction ff : flowFunctions)for(ExpectedFlowFunctionff:flowFunctions)			result += ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";result+=ff.source+"->"+Joiner.on(",").join(ff.targets)+ff.transformerString()+", ";		return result;returnresult;	}}		public static abstract class ExpectedFlowFunction {publicstaticabstractclassExpectedFlowFunction{




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400










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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




401


402



		public final TestFact source;
		public final TestFact[] targets;






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


401


402


		public final TestFact source;
		public final TestFact[] targets;

		public final TestFact source;publicfinalTestFactsource;		public final TestFact[] targets;publicfinalTestFact[]targets;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




403


404


405



		public Edge edge;
		private int times;







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


403


404


405


		public Edge edge;
		private int times;


		public Edge edge;publicEdgeedge;		private int times;privateinttimes;




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




406



		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


406


		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {

		public ExpectedFlowFunction(int times, TestFact source, TestFact... targets) {publicExpectedFlowFunction(inttimes,TestFactsource,TestFact...targets){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




407


408


409


410


411


412


413


414


415



			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
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


407


408


409


410


411


412


413


414


415


			this.times = times;
			this.source = source;
			this.targets = targets;
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
		}

			this.times = times;this.times=times;			this.source = source;this.source=source;			this.targets = targets;this.targets=targets;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));returnString.format("%s: %s -> {%s}",edge,source,Joiner.on(",").join(targets));		}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




416


417


418



		
		public abstract String transformerString();







rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


416


417


418


		
		public abstract String transformerString();


				public abstract String transformerString();publicabstractStringtransformerString();




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




419



		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


419


		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);

		public abstract FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler);publicabstractFlowFunction.ConstrainedFact<String,TestFact,TestStatement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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


	}}		private static interface EdgeVisitor {privatestaticinterfaceEdgeVisitor{		void visit(NormalEdge edge);voidvisit(NormalEdgeedge);		void visit(CallEdge edge);voidvisit(CallEdgeedge);		void visit(Call2ReturnEdge edge);voidvisit(Call2ReturnEdgeedge);		void visit(ReturnEdge edge);voidvisit(ReturnEdgeedge);	}}	public static abstract class Edge {publicstaticabstractclassEdge{		public final ExpectedFlowFunction[] flowFunctions;publicfinalExpectedFlowFunction[]flowFunctions;		public boolean includeInCfg = true;publicbooleanincludeInCfg=true;		public Edge(ExpectedFlowFunction...flowFunctions) {publicEdge(ExpectedFlowFunction...flowFunctions){			this.flowFunctions = flowFunctions;this.flowFunctions=flowFunctions;			for(ExpectedFlowFunction ff : flowFunctions) {for(ExpectedFlowFunctionff:flowFunctions){				ff.edge = this;ff.edge=this;			}}		}}				public abstract void accept(EdgeVisitor visitor);publicabstractvoidaccept(EdgeVisitorvisitor);	}}	public static class NormalEdge extends Edge {publicstaticclassNormalEdgeextendsEdge{




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




445


446



		private TestStatement unit;
		private TestStatement succUnit;






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


445


446


		private TestStatement unit;
		private TestStatement succUnit;

		private TestStatement unit;privateTestStatementunit;		private TestStatement succUnit;privateTestStatementsuccUnit;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




447










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









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448



		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


448


		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {

		public NormalEdge(TestStatement unit, TestStatement succUnit, ExpectedFlowFunction...flowFunctions) {publicNormalEdge(TestStatementunit,TestStatementsuccUnit,ExpectedFlowFunction...flowFunctions){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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


			super(flowFunctions);super(flowFunctions);			this.unit = unit;this.unit=unit;			this.succUnit = succUnit;this.succUnit=succUnit;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s -normal-> %s", unit, succUnit);returnString.format("%s -normal-> %s",unit,succUnit);		}}		@Override@Override		public void accept(EdgeVisitor visitor) {publicvoidaccept(EdgeVisitorvisitor){			visitor.visit(this);visitor.visit(this);		}}	}}	public static class CallEdge extends Edge {publicstaticclassCallEdgeextendsEdge{




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




467


468



		private TestStatement callSite;
		private TestMethod destinationMethod;






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


467


468


		private TestStatement callSite;
		private TestMethod destinationMethod;

		private TestStatement callSite;privateTestStatementcallSite;		private TestMethod destinationMethod;privateTestMethoddestinationMethod;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




469










FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


469









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




470



		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


470


		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {

		public CallEdge(TestStatement callSite, TestMethod destinationMethod, ExpectedFlowFunction...flowFunctions) {publicCallEdge(TestStatementcallSite,TestMethoddestinationMethod,ExpectedFlowFunction...flowFunctions){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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

			super(flowFunctions);super(flowFunctions);			this.callSite = callSite;this.callSite=callSite;			this.destinationMethod = destinationMethod;this.destinationMethod=destinationMethod;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s -call-> %s", callSite, destinationMethod);returnString.format("%s -call-> %s",callSite,destinationMethod);		}}				@Override@Override		public void accept(EdgeVisitor visitor) {publicvoidaccept(EdgeVisitorvisitor){			visitor.visit(this);visitor.visit(this);		}}	}}	public static class Call2ReturnEdge extends Edge {publicstaticclassCall2ReturnEdgeextendsEdge{




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




488


489



		private TestStatement callSite;
		private TestStatement returnSite;






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


488


489


		private TestStatement callSite;
		private TestStatement returnSite;

		private TestStatement callSite;privateTestStatementcallSite;		private TestStatement returnSite;privateTestStatementreturnSite;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




490










FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


490









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




491



		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


491


		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {

		public Call2ReturnEdge(TestStatement callSite, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {publicCall2ReturnEdge(TestStatementcallSite,TestStatementreturnSite,ExpectedFlowFunction...flowFunctions){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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


			super(flowFunctions);super(flowFunctions);			this.callSite = callSite;this.callSite=callSite;			this.returnSite = returnSite;this.returnSite=returnSite;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s -call2ret-> %s", callSite, returnSite);returnString.format("%s -call2ret-> %s",callSite,returnSite);		}}				@Override@Override		public void accept(EdgeVisitor visitor) {publicvoidaccept(EdgeVisitorvisitor){			visitor.visit(this);visitor.visit(this);		}}	}}	public static class ReturnEdge extends Edge {publicstaticclassReturnEdgeextendsEdge{




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




510


511


512


513



		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


510


511


512


513


		private TestStatement exitStmt;
		private TestStatement returnSite;
		private TestStatement callSite;
		private TestMethod calleeMethod;

		private TestStatement exitStmt;privateTestStatementexitStmt;		private TestStatement returnSite;privateTestStatementreturnSite;		private TestStatement callSite;privateTestStatementcallSite;		private TestMethod calleeMethod;privateTestMethodcalleeMethod;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




514










FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


514









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




515



		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


515


		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {

		public ReturnEdge(TestStatement callSite, TestStatement exitStmt, TestStatement returnSite, ExpectedFlowFunction...flowFunctions) {publicReturnEdge(TestStatementcallSite,TestStatementexitStmt,TestStatementreturnSite,ExpectedFlowFunction...flowFunctions){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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


			super(flowFunctions);super(flowFunctions);			this.callSite = callSite;this.callSite=callSite;			this.exitStmt = exitStmt;this.exitStmt=exitStmt;			this.returnSite = returnSite;this.returnSite=returnSite;			if(callSite == null || returnSite == null)if(callSite==null||returnSite==null)				includeInCfg = false;includeInCfg=false;		}}		@Override@Override		public String toString() {publicStringtoString(){			return String.format("%s -return-> %s", exitStmt, returnSite);returnString.format("%s -return-> %s",exitStmt,returnSite);		}}				@Override@Override		public void accept(EdgeVisitor visitor) {publicvoidaccept(EdgeVisitorvisitor){			visitor.visit(this);visitor.visit(this);		}}	}}		private static boolean nullAwareEquals(Object a, Object b) {privatestaticbooleannullAwareEquals(Objecta,Objectb){		if(a == null)if(a==null)			return b==null;returnb==null;		elseelse			return a.equals(b);returna.equals(b);	}}




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




542


543



	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


542


543


	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {
		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {

	public FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions() {publicFlowFunctions<TestStatement,String,TestFact,TestMethod>flowFunctions(){		return new FlowFunctions<TestStatement, String, TestFact, TestMethod>() {returnnewFlowFunctions<TestStatement,String,TestFact,TestMethod>(){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




544


545




			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


544


545



			@Override

			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




546



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


546


			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {

			public FlowFunction<String, TestFact, TestStatement, TestMethod> getReturnFlowFunction(TestStatement callSite, TestMethod calleeMethod, TestStatement exitStmt, TestStatement returnSite) {publicFlowFunction<String,TestFact,TestStatement,TestMethod>getReturnFlowFunction(TestStatementcallSite,TestMethodcalleeMethod,TestStatementexitStmt,TestStatementreturnSite){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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

				for (final ReturnEdge edge : returnEdges) {for(finalReturnEdgeedge:returnEdges){					if (nullAwareEquals(callSite, edge.callSite) && edge.calleeMethod.equals(calleeMethod)if(nullAwareEquals(callSite,edge.callSite)&&edge.calleeMethod.equals(calleeMethod)							&& edge.exitStmt.equals(exitStmt) && nullAwareEquals(edge.returnSite, returnSite)) {&&edge.exitStmt.equals(exitStmt)&&nullAwareEquals(edge.returnSite,returnSite)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)", exitStmt,thrownewAssertionError(String.format("No Flow Function expected for return edge %s -> %s (call edge: %s -> %s)",exitStmt,						returnSite, callSite, calleeMethod));returnSite,callSite,calleeMethod));			}}			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




558



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


558


			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {

			public FlowFunction<String, TestFact, TestStatement, TestMethod> getNormalFlowFunction(final TestStatement curr) {publicFlowFunction<String,TestFact,TestStatement,TestMethod>getNormalFlowFunction(finalTestStatementcurr){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




559



				for (final NormalEdge edge : normalEdges) {






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


559


				for (final NormalEdge edge : normalEdges) {

				for (final NormalEdge edge : normalEdges) {for(finalNormalEdgeedge:normalEdges){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




560



					if (edge.unit.equals(curr)) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


560


					if (edge.unit.equals(curr)) {

					if (edge.unit.equals(curr)) {if(edge.unit.equals(curr)){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




561


562


563



						return createFlowFunction(edge);
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


561


562


563


						return createFlowFunction(edge);
					}
				}

						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




564



				throw new AssertionError(String.format("No Flow Function expected for %s", curr));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


564


				throw new AssertionError(String.format("No Flow Function expected for %s", curr));

				throw new AssertionError(String.format("No Flow Function expected for %s", curr));thrownewAssertionError(String.format("No Flow Function expected for %s",curr));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




565


566


567



			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


565


566


567


			}

			@Override

			}}			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




568



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


568


			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {

			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallToReturnFlowFunction(TestStatement callSite, TestStatement returnSite) {publicFlowFunction<String,TestFact,TestStatement,TestMethod>getCallToReturnFlowFunction(TestStatementcallSite,TestStatementreturnSite){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




569


570


571


572


573


574


575


576


577



				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override






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


574


575


576


577


				for (final Call2ReturnEdge edge : call2retEdges) {
					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));
			}

			@Override

				for (final Call2ReturnEdge edge : call2retEdges) {for(finalCall2ReturnEdgeedge:call2retEdges){					if (edge.callSite.equals(callSite) && edge.returnSite.equals(returnSite)) {if(edge.callSite.equals(callSite)&&edge.returnSite.equals(returnSite)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for call to return edge %s -> %s", callSite, returnSite));thrownewAssertionError(String.format("No Flow Function expected for call to return edge %s -> %s",callSite,returnSite));			}}			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




578



			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


578


			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {

			public FlowFunction<String, TestFact, TestStatement, TestMethod> getCallFlowFunction(TestStatement callStmt, TestMethod destinationMethod) {publicFlowFunction<String,TestFact,TestStatement,TestMethod>getCallFlowFunction(TestStatementcallStmt,TestMethoddestinationMethod){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




579


580


581


582


583


584


585


586



				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
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


579


580


581


582


583


584


585


586


				for (final CallEdge edge : callEdges) {
					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {
						return createFlowFunction(edge);
					}
				}
				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));
			}


				for (final CallEdge edge : callEdges) {for(finalCallEdgeedge:callEdges){					if (edge.callSite.equals(callStmt) && edge.destinationMethod.equals(destinationMethod)) {if(edge.callSite.equals(callStmt)&&edge.destinationMethod.equals(destinationMethod)){						return createFlowFunction(edge);returncreateFlowFunction(edge);					}}				}}				throw new AssertionError(String.format("No Flow Function expected for call %s -> %s", callStmt, destinationMethod));thrownewAssertionError(String.format("No Flow Function expected for call %s -> %s",callStmt,destinationMethod));			}}




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




587


588



			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


587


588


			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {
				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {

			private FlowFunction<String, TestFact, TestStatement, TestMethod> createFlowFunction(final Edge edge) {privateFlowFunction<String,TestFact,TestStatement,TestMethod>createFlowFunction(finalEdgeedge){				return new FlowFunction<String, TestFact, TestStatement, TestMethod>() {returnnewFlowFunction<String,TestFact,TestStatement,TestMethod>(){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




589



					@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


589


					@Override

					@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




590


591


592



					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


590


591


592


					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,
							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {
						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();

					public Set<FlowFunction.ConstrainedFact<String, TestFact, TestStatement, TestMethod>> computeTargets(TestFact source,publicSet<FlowFunction.ConstrainedFact<String,TestFact,TestStatement,TestMethod>>computeTargets(TestFactsource,							AccessPathHandler<String, TestFact, TestStatement, TestMethod> accPathHandler) {AccessPathHandler<String,TestFact,TestStatement,TestMethod>accPathHandler){						Set<ConstrainedFact<String, TestFact, TestStatement, TestMethod>> result = Sets.newHashSet();Set<ConstrainedFact<String,TestFact,TestStatement,TestMethod>>result=Sets.newHashSet();




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




593



						boolean found = false;






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


593


						boolean found = false;

						boolean found = false;booleanfound=false;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




594



						for (ExpectedFlowFunction ff : edge.flowFunctions) {






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


594


						for (ExpectedFlowFunction ff : edge.flowFunctions) {

						for (ExpectedFlowFunction ff : edge.flowFunctions) {for(ExpectedFlowFunctionff:edge.flowFunctions){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




595



							if (ff.source.equals(source)) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


595


							if (ff.source.equals(source)) {

							if (ff.source.equals(source)) {if(ff.source.equals(source)){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




596



								if (remainingFlowFunctions.remove(ff)) {






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


								if (remainingFlowFunctions.remove(ff)) {

								if (remainingFlowFunctions.remove(ff)) {if(remainingFlowFunctions.remove(ff)){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




597


598



									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


597


598


									for(TestFact target : ff.targets) {
										result.add(ff.apply(target, accPathHandler));

									for(TestFact target : ff.targets) {for(TestFacttarget:ff.targets){										result.add(ff.apply(target, accPathHandler));result.add(ff.apply(target,accPathHandler));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




599



									}






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


599


									}

									}}




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




600



									found = true;






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


600


									found = true;

									found = true;found=true;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




601


602


603


604


605



								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
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


601


602


603


604


605


								} else {
									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));
								}
							}
						}

								} else {}else{									throw new AssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'", ff, edge));thrownewAssertionError(String.format("Flow Function '%s' was used multiple times on edge '%s'",ff,edge));								}}							}}						}}




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




606


607


608


609



						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));






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


						if(found)
							return result;
						else
							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));

						if(found)if(found)							return result;returnresult;						elseelse							throw new AssertionError(String.format("Fact '%s' was not expected at edge '%s'", source, edge));thrownewAssertionError(String.format("Fact '%s' was not expected at edge '%s'",source,edge));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




610


611


612


613


614


615


616



					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


610


611


612


613


614


615


616


					}
				};
			}
		};
	}

	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {

					}}				};};			}}		};};	}}	public void runSolver(final boolean followReturnsPastSeeds, final String...initialSeeds) {publicvoidrunSolver(finalbooleanfollowReturnsPastSeeds,finalString...initialSeeds){




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




617



		Scheduler scheduler = new Scheduler();






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


617


		Scheduler scheduler = new Scheduler();

		Scheduler scheduler = new Scheduler();Schedulerscheduler=newScheduler();




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




618



		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


618


		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(

		FieldSensitiveIFDSSolver<String, TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>> solver = new FieldSensitiveIFDSSolver<String ,TestFact, TestStatement, TestMethod, InterproceduralCFG<TestStatement,TestMethod>>(FieldSensitiveIFDSSolver<String,TestFact,TestStatement,TestMethod,InterproceduralCFG<TestStatement,TestMethod>>solver=newFieldSensitiveIFDSSolver<String,TestFact,TestStatement,TestMethod,InterproceduralCFG<TestStatement,TestMethod>>(




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




619


620


621


622


623


624


625


626


627



				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {
					@Override
					public void merge(TestFact previousFact, TestFact currentFact) {
					}

					@Override
					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
					}
					






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


619


620


621


622


623


624


625


626


627


				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {
					@Override
					public void merge(TestFact previousFact, TestFact currentFact) {
					}

					@Override
					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {
					}
					

				createTabulationProblem(followReturnsPastSeeds, initialSeeds), new FactMergeHandler<TestFact>() {createTabulationProblem(followReturnsPastSeeds,initialSeeds),newFactMergeHandler<TestFact>(){					@Override@Override					public void merge(TestFact previousFact, TestFact currentFact) {publicvoidmerge(TestFactpreviousFact,TestFactcurrentFact){					}}					@Override@Override					public void restoreCallingContext(TestFact factAtReturnSite, TestFact factAtCallSite) {publicvoidrestoreCallingContext(TestFactfactAtReturnSite,TestFactfactAtCallSite){					}}					




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




628



				}, debugger, scheduler);






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


628


				}, debugger, scheduler);

				}, debugger, scheduler);},debugger,scheduler);




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




629



		addExpectationsToDebugger();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


629


		addExpectationsToDebugger();

		addExpectationsToDebugger();addExpectationsToDebugger();




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




630



		scheduler.runAndAwaitCompletion();






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


630


		scheduler.runAndAwaitCompletion();

		scheduler.runAndAwaitCompletion();scheduler.runAndAwaitCompletion();




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




631



		






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


631


		

		




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




632


633


634


635



		assertAllFlowFunctionsUsed();
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


632


633


634


635


		assertAllFlowFunctionsUsed();
	}
	
	

		assertAllFlowFunctionsUsed();assertAllFlowFunctionsUsed();	}}		




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




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







rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


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


	private void addExpectationsToDebugger() {privatevoidaddExpectationsToDebugger(){		for(NormalEdge edge : normalEdges) {for(NormalEdgeedge:normalEdges){			debugger.expectNormalFlow(edge.unit, expectedFlowFunctionsToString(edge.flowFunctions));debugger.expectNormalFlow(edge.unit,expectedFlowFunctionsToString(edge.flowFunctions));		}}		for(CallEdge edge : callEdges) {for(CallEdgeedge:callEdges){			debugger.expectCallFlow(edge.callSite, edge.destinationMethod, expectedFlowFunctionsToString(edge.flowFunctions));debugger.expectCallFlow(edge.callSite,edge.destinationMethod,expectedFlowFunctionsToString(edge.flowFunctions));		}}		for(Call2ReturnEdge edge : call2retEdges) {for(Call2ReturnEdgeedge:call2retEdges){			debugger.expectNormalFlow(edge.callSite, expectedFlowFunctionsToString(edge.flowFunctions));debugger.expectNormalFlow(edge.callSite,expectedFlowFunctionsToString(edge.flowFunctions));		}}		for(ReturnEdge edge : returnEdges) {for(ReturnEdgeedge:returnEdges){			debugger.expectReturnFlow(edge.exitStmt, edge.returnSite, expectedFlowFunctionsToString(edge.flowFunctions));debugger.expectReturnFlow(edge.exitStmt,edge.returnSite,expectedFlowFunctionsToString(edge.flowFunctions));		}}	}}




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




651



	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


651


	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {

	private IFDSTabulationProblem<TestStatement, String, TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>> createTabulationProblem(final boolean followReturnsPastSeeds, final String[] initialSeeds) {privateIFDSTabulationProblem<TestStatement,String,TestFact,TestMethod,InterproceduralCFG<TestStatement,TestMethod>>createTabulationProblem(finalbooleanfollowReturnsPastSeeds,finalString[]initialSeeds){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




652



		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


652


		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();

		final InterproceduralCFG<TestStatement, TestMethod> icfg = buildIcfg();finalInterproceduralCFG<TestStatement,TestMethod>icfg=buildIcfg();




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




653



		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


653


		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();

		final FlowFunctions<TestStatement, String, TestFact, TestMethod> flowFunctions = flowFunctions();finalFlowFunctions<TestStatement,String,TestFact,TestMethod>flowFunctions=flowFunctions();




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




654



		






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


654


		

		




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




655



		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


655


		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {

		return new IFDSTabulationProblem<TestStatement,String,  TestFact, TestMethod, InterproceduralCFG<TestStatement, TestMethod>>() {returnnewIFDSTabulationProblem<TestStatement,String,TestFact,TestMethod,InterproceduralCFG<TestStatement,TestMethod>>(){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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

			@Override@Override			public boolean followReturnsPastSeeds() {publicbooleanfollowReturnsPastSeeds(){				return followReturnsPastSeeds;returnfollowReturnsPastSeeds;			}}			@Override@Override			public boolean autoAddZero() {publicbooleanautoAddZero(){				return false;returnfalse;			}}			@Override@Override			public int numThreads() {publicintnumThreads(){				return 1;return1;			}}			@Override@Override			public boolean computeValues() {publicbooleancomputeValues(){				return false;returnfalse;			}}			@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




678



			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


678


			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {

			public FlowFunctions<TestStatement,String,  TestFact, TestMethod> flowFunctions() {publicFlowFunctions<TestStatement,String,TestFact,TestMethod>flowFunctions(){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




679


680


681


682



				return flowFunctions;
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


679


680


681


682


				return flowFunctions;
			}

			@Override

				return flowFunctions;returnflowFunctions;			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




683



			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


683


			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {

			public InterproceduralCFG<TestStatement, TestMethod> interproceduralCFG() {publicInterproceduralCFG<TestStatement,TestMethod>interproceduralCFG(){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




684


685


686


687



				return icfg;
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


684


685


686


687


				return icfg;
			}

			@Override

				return icfg;returnicfg;			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




688


689



			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


688


689


			public Map<TestStatement, Set<TestFact>> initialSeeds() {
				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();

			public Map<TestStatement, Set<TestFact>> initialSeeds() {publicMap<TestStatement,Set<TestFact>>initialSeeds(){				Map<TestStatement, Set<TestFact>> result = Maps.newHashMap();Map<TestStatement,Set<TestFact>>result=Maps.newHashMap();




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




690



				for (String stmt : initialSeeds) {






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


690


				for (String stmt : initialSeeds) {

				for (String stmt : initialSeeds) {for(Stringstmt:initialSeeds){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691



					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


691


					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));

					result.put(new TestStatement(stmt), Sets.newHashSet(new TestFact("0")));result.put(newTestStatement(stmt),Sets.newHashSet(newTestFact("0")));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




692


693


694


695


696



				}
				return result;
			}

			@Override






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


692


693


694


695


696


				}
				return result;
			}

			@Override

				}}				return result;returnresult;			}}			@Override@Override




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




697


698


699


700


701



			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


697


698


699


700


701


			public TestFact zeroValue() {
				return new TestFact("0");
			}
			
			@Override

			public TestFact zeroValue() {publicTestFactzeroValue(){				return new TestFact("0");returnnewTestFact("0");			}}						@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




702


703



			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


702


703


			public ZeroHandler<String> zeroHandler() {
				return new ZeroHandler<String>() {

			public ZeroHandler<String> zeroHandler() {publicZeroHandler<String>zeroHandler(){				return new ZeroHandler<String>() {returnnewZeroHandler<String>(){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




704



					@Override






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


704


					@Override

					@Override@Override




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




705



					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


705


					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {

					public boolean shouldGenerateAccessPath(AccessPath<String> accPath) {publicbooleanshouldGenerateAccessPath(AccessPath<String>accPath){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




706


707


708



						return true;
					}
				};






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


706


707


708


						return true;
					}
				};

						return true;returntrue;					}}				};};




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




709


710


711


712



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


709


710


711


712


			}
		};
	}
}
			}}		};};	}}}}





