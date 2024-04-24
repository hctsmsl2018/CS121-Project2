



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

40ad6af0e98074081d5be18ced306926640c77b1

















40ad6af0e98074081d5be18ced306926640c77b1


Switch branch/tag










heros


test


heros


BiDiIFDSSolverTest.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolverTest.java



6.36 KB









Newer










Older









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros;










Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import java.util.Collection;
import java.util.LinkedList;










package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






16




import org.junit.Test;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






17




18




19




20




21




import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






22














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






23




import static heros.utilities.TestHelper.*;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






24




import heros.utilities.TestHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






25




import heros.utilities.TestHelper.TabulationProblemExchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






26














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






27




@RunWith(Parameterized.class)









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






28




29




30




31




public class BiDiIFDSSolverTest {

	private TestHelper forwardHelper;
	private TestHelper backwardHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






32




	private TabulationProblemExchange exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






33




	









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






34




35




	public BiDiIFDSSolverTest(TabulationProblemExchange exchange) {
		this.exchange = exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






36




37




38




		forwardHelper = new TestHelper();
		backwardHelper = new TestHelper();
	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






39




40




41




42




43




44




45




46





	@Parameters(name="{0}")
	public static Collection<Object[]> parameters() {
		LinkedList<Object[]> result = Lists.newLinkedList();
		result.add(new Object[] {TabulationProblemExchange.AsSpecified});
		result.add(new Object[] {TabulationProblemExchange.ExchangeForwardAndBackward});
		return result;
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void happyPath() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").expectArtificalFlow(flow("1")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").expectArtificalFlow(flow("2")));
		









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsInBothDirections() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(flow("2")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(flow("3")));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsNonMatchingCallSites() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y1"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y2"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






113




	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void returnsOnlyOneDirection() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






130




131




				normalStmt("b").succ("a", kill("0")),
				exitStmt("a").returns(over("y"), to("x") /*none*/));









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void reuseSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("1", "2")).retSite("d", kill("1")),
				exitStmt("d").expectArtificalFlow(kill("1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow("2", "1"))
							.returns(over("c"), to("d"), flow("2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}
	
	@Test
	public void multipleSeedsPreventReusingSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a1").succ("b", flow("0", "1")),
				normalStmt("a2").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow(times(2), "1", "2")).retSite("c", kill(times(2), "1")),
				callSite("c").calls("bar", flow(times(2), "1", "2")).retSite("d", kill(times(2), "1")),
				exitStmt("d").expectArtificalFlow(kill(times(2), "1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow(times(2), "2", "1"))
							 .returns(over("c"), to("d"), flow(times(2), "2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a1").expectArtificalFlow(kill("0")),
				exitStmt("a2").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a1", "a2");
	}









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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





	@Test
	public void dontUnpauseIfReturnFlowIsKilled() {
		forwardHelper.method("foo",
				startPoints(), 
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("cs"), to("y"), kill("1")));
		
		forwardHelper.method("bar",
				startPoints(),
				normalStmt("y").succ("z" /* none */));
		
		backwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("cs"), to("x"), flow("1", "2")));
		
		backwardHelper.method("bar",
				startPoints(),
				normalStmt("x").succ("z" /*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






208




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

40ad6af0e98074081d5be18ced306926640c77b1

















40ad6af0e98074081d5be18ced306926640c77b1


Switch branch/tag










heros


test


heros


BiDiIFDSSolverTest.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolverTest.java



6.36 KB









Newer










Older









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros;










Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import java.util.Collection;
import java.util.LinkedList;










package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






16




import org.junit.Test;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






17




18




19




20




21




import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






22














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






23




import static heros.utilities.TestHelper.*;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






24




import heros.utilities.TestHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






25




import heros.utilities.TestHelper.TabulationProblemExchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






26














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






27




@RunWith(Parameterized.class)









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






28




29




30




31




public class BiDiIFDSSolverTest {

	private TestHelper forwardHelper;
	private TestHelper backwardHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






32




	private TabulationProblemExchange exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






33




	









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






34




35




	public BiDiIFDSSolverTest(TabulationProblemExchange exchange) {
		this.exchange = exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






36




37




38




		forwardHelper = new TestHelper();
		backwardHelper = new TestHelper();
	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






39




40




41




42




43




44




45




46





	@Parameters(name="{0}")
	public static Collection<Object[]> parameters() {
		LinkedList<Object[]> result = Lists.newLinkedList();
		result.add(new Object[] {TabulationProblemExchange.AsSpecified});
		result.add(new Object[] {TabulationProblemExchange.ExchangeForwardAndBackward});
		return result;
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void happyPath() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").expectArtificalFlow(flow("1")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").expectArtificalFlow(flow("2")));
		









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsInBothDirections() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(flow("2")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(flow("3")));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsNonMatchingCallSites() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y1"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y2"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






113




	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void returnsOnlyOneDirection() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






130




131




				normalStmt("b").succ("a", kill("0")),
				exitStmt("a").returns(over("y"), to("x") /*none*/));









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void reuseSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("1", "2")).retSite("d", kill("1")),
				exitStmt("d").expectArtificalFlow(kill("1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow("2", "1"))
							.returns(over("c"), to("d"), flow("2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}
	
	@Test
	public void multipleSeedsPreventReusingSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a1").succ("b", flow("0", "1")),
				normalStmt("a2").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow(times(2), "1", "2")).retSite("c", kill(times(2), "1")),
				callSite("c").calls("bar", flow(times(2), "1", "2")).retSite("d", kill(times(2), "1")),
				exitStmt("d").expectArtificalFlow(kill(times(2), "1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow(times(2), "2", "1"))
							 .returns(over("c"), to("d"), flow(times(2), "2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a1").expectArtificalFlow(kill("0")),
				exitStmt("a2").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a1", "a2");
	}









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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





	@Test
	public void dontUnpauseIfReturnFlowIsKilled() {
		forwardHelper.method("foo",
				startPoints(), 
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("cs"), to("y"), kill("1")));
		
		forwardHelper.method("bar",
				startPoints(),
				normalStmt("y").succ("z" /* none */));
		
		backwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("cs"), to("x"), flow("1", "2")));
		
		backwardHelper.method("bar",
				startPoints(),
				normalStmt("x").succ("z" /*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






208




}











Open sidebar



Joshua Garcia heros

40ad6af0e98074081d5be18ced306926640c77b1







Open sidebar



Joshua Garcia heros

40ad6af0e98074081d5be18ced306926640c77b1




Open sidebar

Joshua Garcia heros

40ad6af0e98074081d5be18ced306926640c77b1


Joshua Garciaherosheros
40ad6af0e98074081d5be18ced306926640c77b1










40ad6af0e98074081d5be18ced306926640c77b1


Switch branch/tag










heros


test


heros


BiDiIFDSSolverTest.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolverTest.java



6.36 KB









Newer










Older









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros;










Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import java.util.Collection;
import java.util.LinkedList;










package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






16




import org.junit.Test;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






17




18




19




20




21




import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






22














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






23




import static heros.utilities.TestHelper.*;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






24




import heros.utilities.TestHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






25




import heros.utilities.TestHelper.TabulationProblemExchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






26














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






27




@RunWith(Parameterized.class)









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






28




29




30




31




public class BiDiIFDSSolverTest {

	private TestHelper forwardHelper;
	private TestHelper backwardHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






32




	private TabulationProblemExchange exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






33




	









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






34




35




	public BiDiIFDSSolverTest(TabulationProblemExchange exchange) {
		this.exchange = exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






36




37




38




		forwardHelper = new TestHelper();
		backwardHelper = new TestHelper();
	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






39




40




41




42




43




44




45




46





	@Parameters(name="{0}")
	public static Collection<Object[]> parameters() {
		LinkedList<Object[]> result = Lists.newLinkedList();
		result.add(new Object[] {TabulationProblemExchange.AsSpecified});
		result.add(new Object[] {TabulationProblemExchange.ExchangeForwardAndBackward});
		return result;
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void happyPath() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").expectArtificalFlow(flow("1")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").expectArtificalFlow(flow("2")));
		









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsInBothDirections() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(flow("2")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(flow("3")));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsNonMatchingCallSites() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y1"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y2"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






113




	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void returnsOnlyOneDirection() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






130




131




				normalStmt("b").succ("a", kill("0")),
				exitStmt("a").returns(over("y"), to("x") /*none*/));









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void reuseSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("1", "2")).retSite("d", kill("1")),
				exitStmt("d").expectArtificalFlow(kill("1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow("2", "1"))
							.returns(over("c"), to("d"), flow("2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}
	
	@Test
	public void multipleSeedsPreventReusingSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a1").succ("b", flow("0", "1")),
				normalStmt("a2").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow(times(2), "1", "2")).retSite("c", kill(times(2), "1")),
				callSite("c").calls("bar", flow(times(2), "1", "2")).retSite("d", kill(times(2), "1")),
				exitStmt("d").expectArtificalFlow(kill(times(2), "1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow(times(2), "2", "1"))
							 .returns(over("c"), to("d"), flow(times(2), "2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a1").expectArtificalFlow(kill("0")),
				exitStmt("a2").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a1", "a2");
	}









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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





	@Test
	public void dontUnpauseIfReturnFlowIsKilled() {
		forwardHelper.method("foo",
				startPoints(), 
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("cs"), to("y"), kill("1")));
		
		forwardHelper.method("bar",
				startPoints(),
				normalStmt("y").succ("z" /* none */));
		
		backwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("cs"), to("x"), flow("1", "2")));
		
		backwardHelper.method("bar",
				startPoints(),
				normalStmt("x").succ("z" /*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






208




}














40ad6af0e98074081d5be18ced306926640c77b1


Switch branch/tag










heros


test


heros


BiDiIFDSSolverTest.java



Find file
Normal viewHistoryPermalink






BiDiIFDSSolverTest.java



6.36 KB









Newer










Older









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros;










Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import java.util.Collection;
import java.util.LinkedList;










package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






16




import org.junit.Test;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






17




18




19




20




21




import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






22














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






23




import static heros.utilities.TestHelper.*;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






24




import heros.utilities.TestHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






25




import heros.utilities.TestHelper.TabulationProblemExchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






26














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






27




@RunWith(Parameterized.class)









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






28




29




30




31




public class BiDiIFDSSolverTest {

	private TestHelper forwardHelper;
	private TestHelper backwardHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






32




	private TabulationProblemExchange exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






33




	









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






34




35




	public BiDiIFDSSolverTest(TabulationProblemExchange exchange) {
		this.exchange = exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






36




37




38




		forwardHelper = new TestHelper();
		backwardHelper = new TestHelper();
	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






39




40




41




42




43




44




45




46





	@Parameters(name="{0}")
	public static Collection<Object[]> parameters() {
		LinkedList<Object[]> result = Lists.newLinkedList();
		result.add(new Object[] {TabulationProblemExchange.AsSpecified});
		result.add(new Object[] {TabulationProblemExchange.ExchangeForwardAndBackward});
		return result;
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void happyPath() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").expectArtificalFlow(flow("1")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").expectArtificalFlow(flow("2")));
		









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsInBothDirections() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(flow("2")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(flow("3")));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsNonMatchingCallSites() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y1"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y2"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






113




	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void returnsOnlyOneDirection() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






130




131




				normalStmt("b").succ("a", kill("0")),
				exitStmt("a").returns(over("y"), to("x") /*none*/));









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void reuseSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("1", "2")).retSite("d", kill("1")),
				exitStmt("d").expectArtificalFlow(kill("1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow("2", "1"))
							.returns(over("c"), to("d"), flow("2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}
	
	@Test
	public void multipleSeedsPreventReusingSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a1").succ("b", flow("0", "1")),
				normalStmt("a2").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow(times(2), "1", "2")).retSite("c", kill(times(2), "1")),
				callSite("c").calls("bar", flow(times(2), "1", "2")).retSite("d", kill(times(2), "1")),
				exitStmt("d").expectArtificalFlow(kill(times(2), "1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow(times(2), "2", "1"))
							 .returns(over("c"), to("d"), flow(times(2), "2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a1").expectArtificalFlow(kill("0")),
				exitStmt("a2").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a1", "a2");
	}









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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





	@Test
	public void dontUnpauseIfReturnFlowIsKilled() {
		forwardHelper.method("foo",
				startPoints(), 
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("cs"), to("y"), kill("1")));
		
		forwardHelper.method("bar",
				startPoints(),
				normalStmt("y").succ("z" /* none */));
		
		backwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("cs"), to("x"), flow("1", "2")));
		
		backwardHelper.method("bar",
				startPoints(),
				normalStmt("x").succ("z" /*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






208




}










40ad6af0e98074081d5be18ced306926640c77b1


Switch branch/tag










heros


test


heros


BiDiIFDSSolverTest.java



Find file
Normal viewHistoryPermalink




40ad6af0e98074081d5be18ced306926640c77b1


Switch branch/tag










heros


test


heros


BiDiIFDSSolverTest.java





40ad6af0e98074081d5be18ced306926640c77b1


Switch branch/tag








40ad6af0e98074081d5be18ced306926640c77b1


Switch branch/tag





40ad6af0e98074081d5be18ced306926640c77b1

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

BiDiIFDSSolverTest.java
Find file
Normal viewHistoryPermalink




BiDiIFDSSolverTest.java



6.36 KB









Newer










Older









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros;










Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import java.util.Collection;
import java.util.LinkedList;










package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






16




import org.junit.Test;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






17




18




19




20




21




import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






22














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






23




import static heros.utilities.TestHelper.*;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






24




import heros.utilities.TestHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






25




import heros.utilities.TestHelper.TabulationProblemExchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






26














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






27




@RunWith(Parameterized.class)









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






28




29




30




31




public class BiDiIFDSSolverTest {

	private TestHelper forwardHelper;
	private TestHelper backwardHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






32




	private TabulationProblemExchange exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






33




	









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






34




35




	public BiDiIFDSSolverTest(TabulationProblemExchange exchange) {
		this.exchange = exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






36




37




38




		forwardHelper = new TestHelper();
		backwardHelper = new TestHelper();
	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






39




40




41




42




43




44




45




46





	@Parameters(name="{0}")
	public static Collection<Object[]> parameters() {
		LinkedList<Object[]> result = Lists.newLinkedList();
		result.add(new Object[] {TabulationProblemExchange.AsSpecified});
		result.add(new Object[] {TabulationProblemExchange.ExchangeForwardAndBackward});
		return result;
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void happyPath() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").expectArtificalFlow(flow("1")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").expectArtificalFlow(flow("2")));
		









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsInBothDirections() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(flow("2")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(flow("3")));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsNonMatchingCallSites() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y1"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y2"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






113




	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void returnsOnlyOneDirection() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






130




131




				normalStmt("b").succ("a", kill("0")),
				exitStmt("a").returns(over("y"), to("x") /*none*/));









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void reuseSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("1", "2")).retSite("d", kill("1")),
				exitStmt("d").expectArtificalFlow(kill("1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow("2", "1"))
							.returns(over("c"), to("d"), flow("2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}
	
	@Test
	public void multipleSeedsPreventReusingSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a1").succ("b", flow("0", "1")),
				normalStmt("a2").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow(times(2), "1", "2")).retSite("c", kill(times(2), "1")),
				callSite("c").calls("bar", flow(times(2), "1", "2")).retSite("d", kill(times(2), "1")),
				exitStmt("d").expectArtificalFlow(kill(times(2), "1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow(times(2), "2", "1"))
							 .returns(over("c"), to("d"), flow(times(2), "2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a1").expectArtificalFlow(kill("0")),
				exitStmt("a2").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a1", "a2");
	}









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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





	@Test
	public void dontUnpauseIfReturnFlowIsKilled() {
		forwardHelper.method("foo",
				startPoints(), 
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("cs"), to("y"), kill("1")));
		
		forwardHelper.method("bar",
				startPoints(),
				normalStmt("y").succ("z" /* none */));
		
		backwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("cs"), to("x"), flow("1", "2")));
		
		backwardHelper.method("bar",
				startPoints(),
				normalStmt("x").succ("z" /*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






208




}








BiDiIFDSSolverTest.java



6.36 KB










BiDiIFDSSolverTest.java



6.36 KB









Newer










Older
NewerOlder







package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/
package heros;










Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import java.util.Collection;
import java.util.LinkedList;










package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






16




import org.junit.Test;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






17




18




19




20




21




import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






22














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






23




import static heros.utilities.TestHelper.*;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






24




import heros.utilities.TestHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






25




import heros.utilities.TestHelper.TabulationProblemExchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






26














Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






27




@RunWith(Parameterized.class)









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






28




29




30




31




public class BiDiIFDSSolverTest {

	private TestHelper forwardHelper;
	private TestHelper backwardHelper;









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






32




	private TabulationProblemExchange exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






33




	









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






34




35




	public BiDiIFDSSolverTest(TabulationProblemExchange exchange) {
		this.exchange = exchange;









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






36




37




38




		forwardHelper = new TestHelper();
		backwardHelper = new TestHelper();
	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






39




40




41




42




43




44




45




46





	@Parameters(name="{0}")
	public static Collection<Object[]> parameters() {
		LinkedList<Object[]> result = Lists.newLinkedList();
		result.add(new Object[] {TabulationProblemExchange.AsSpecified});
		result.add(new Object[] {TabulationProblemExchange.ExchangeForwardAndBackward});
		return result;
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void happyPath() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").expectArtificalFlow(flow("1")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").expectArtificalFlow(flow("2")));
		









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsInBothDirections() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(flow("2")));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(flow("3")));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void unbalancedReturnsNonMatchingCallSites() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y1"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),
				normalStmt("b").succ("a", flow("0", "2")),
				exitStmt("a").returns(over("y2"), to("x"), flow("2", "3")));
		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






113




	}









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




	
	@Test
	public void returnsOnlyOneDirection() {
		forwardHelper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b"),
				normalStmt("b").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));
		
		forwardHelper.method("bar",
				startPoints(),
				exitStmt("z").expectArtificalFlow(/*none*/));
		
		backwardHelper.method("foo",
				startPoints("c"),
				normalStmt("c").succ("b"),









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






130




131




				normalStmt("b").succ("a", kill("0")),
				exitStmt("a").returns(over("y"), to("x") /*none*/));









Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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




		
		backwardHelper.method("bar",
				startPoints(),
				exitStmt("x").expectArtificalFlow(/*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");
	}
	
	@Test
	public void reuseSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("1", "2")).retSite("d", kill("1")),
				exitStmt("d").expectArtificalFlow(kill("1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow("2", "1"))
							.returns(over("c"), to("d"), flow("2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}
	
	@Test
	public void multipleSeedsPreventReusingSummary() {
		forwardHelper.method("foo",
				startPoints(),
				normalStmt("a1").succ("b", flow("0", "1")),
				normalStmt("a2").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow(times(2), "1", "2")).retSite("c", kill(times(2), "1")),
				callSite("c").calls("bar", flow(times(2), "1", "2")).retSite("d", kill(times(2), "1")),
				exitStmt("d").expectArtificalFlow(kill(times(2), "1")));
		
		forwardHelper.method("bar",
				startPoints("x"),
				normalStmt("x").succ("y", flow("2", "2")),
				exitStmt("y").returns(over("b"), to("c"), flow(times(2), "2", "1"))
							 .returns(over("c"), to("d"), flow(times(2), "2", "1")));
		
		backwardHelper.method("foo",
				startPoints(),
				exitStmt("a1").expectArtificalFlow(kill("0")),
				exitStmt("a2").expectArtificalFlow(kill("0")));
					
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a1", "a2");
	}









test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






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





	@Test
	public void dontUnpauseIfReturnFlowIsKilled() {
		forwardHelper.method("foo",
				startPoints(), 
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("cs"), to("y"), kill("1")));
		
		forwardHelper.method("bar",
				startPoints(),
				normalStmt("y").succ("z" /* none */));
		
		backwardHelper.method("foo",
				startPoints(),
				normalStmt("a").succ("c", flow("0", "1")),
				exitStmt("c").returns(over("cs"), to("x"), flow("1", "2")));
		
		backwardHelper.method("bar",
				startPoints(),
				normalStmt("x").succ("z" /*none*/));
		
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");
	}









package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014






208




}







package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros;packageheros;



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

13

14

15
import java.util.Collection;importjava.util.Collection;import java.util.LinkedList;importjava.util.LinkedList;



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

16
import org.junit.Test;importorg.junit.Test;



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

17

18

19

20

21
import org.junit.runner.RunWith;importorg.junit.runner.RunWith;import org.junit.runners.Parameterized;importorg.junit.runners.Parameterized;import org.junit.runners.Parameterized.Parameters;importorg.junit.runners.Parameterized.Parameters;import com.google.common.collect.Lists;importcom.google.common.collect.Lists;



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

22




Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

23
import static heros.utilities.TestHelper.*;importstaticheros.utilities.TestHelper.*;



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

24
import heros.utilities.TestHelper;importheros.utilities.TestHelper;



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

25
import heros.utilities.TestHelper.TabulationProblemExchange;importheros.utilities.TestHelper.TabulationProblemExchange;



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

26




Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

27
@RunWith(Parameterized.class)@RunWith(Parameterized.class)



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

28

29

30

31
public class BiDiIFDSSolverTest {publicclassBiDiIFDSSolverTest{	private TestHelper forwardHelper;privateTestHelperforwardHelper;	private TestHelper backwardHelper;privateTestHelperbackwardHelper;



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

32
	private TabulationProblemExchange exchange;privateTabulationProblemExchangeexchange;



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

33
	



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

34

35
	public BiDiIFDSSolverTest(TabulationProblemExchange exchange) {publicBiDiIFDSSolverTest(TabulationProblemExchangeexchange){		this.exchange = exchange;this.exchange=exchange;



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

36

37

38
		forwardHelper = new TestHelper();forwardHelper=newTestHelper();		backwardHelper = new TestHelper();backwardHelper=newTestHelper();	}}



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

39

40

41

42

43

44

45

46
	@Parameters(name="{0}")@Parameters(name="{0}")	public static Collection<Object[]> parameters() {publicstaticCollection<Object[]>parameters(){		LinkedList<Object[]> result = Lists.newLinkedList();LinkedList<Object[]>result=Lists.newLinkedList();		result.add(new Object[] {TabulationProblemExchange.AsSpecified});result.add(newObject[]{TabulationProblemExchange.AsSpecified});		result.add(new Object[] {TabulationProblemExchange.ExchangeForwardAndBackward});result.add(newObject[]{TabulationProblemExchange.ExchangeForwardAndBackward});		return result;returnresult;	}}



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

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
		@Test@Test	public void happyPath() {publicvoidhappyPath(){		forwardHelper.method("foo",forwardHelper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b"),normalStmt("a").succ("b"),				normalStmt("b").succ("c", flow("0", "1")),normalStmt("b").succ("c",flow("0","1")),				exitStmt("c").expectArtificalFlow(flow("1")));exitStmt("c").expectArtificalFlow(flow("1")));				backwardHelper.method("foo",backwardHelper.method("foo",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("b"),normalStmt("c").succ("b"),				normalStmt("b").succ("a", flow("0", "2")),normalStmt("b").succ("a",flow("0","2")),				exitStmt("a").expectArtificalFlow(flow("2")));exitStmt("a").expectArtificalFlow(flow("2")));		



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

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
		forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");forwardHelper.runBiDiSolver(backwardHelper,exchange,"b");	}}		@Test@Test	public void unbalancedReturnsInBothDirections() {publicvoidunbalancedReturnsInBothDirections(){		forwardHelper.method("foo",forwardHelper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b"),normalStmt("a").succ("b"),				normalStmt("b").succ("c", flow("0", "1")),normalStmt("b").succ("c",flow("0","1")),				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));exitStmt("c").returns(over("y"),to("z"),flow("1","2")));				forwardHelper.method("bar",forwardHelper.method("bar",				startPoints(),startPoints(),				exitStmt("z").expectArtificalFlow(flow("2")));exitStmt("z").expectArtificalFlow(flow("2")));				backwardHelper.method("foo",backwardHelper.method("foo",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("b"),normalStmt("c").succ("b"),				normalStmt("b").succ("a", flow("0", "2")),normalStmt("b").succ("a",flow("0","2")),				exitStmt("a").returns(over("y"), to("x"), flow("2", "3")));exitStmt("a").returns(over("y"),to("x"),flow("2","3")));				backwardHelper.method("bar",backwardHelper.method("bar",				startPoints(),startPoints(),				exitStmt("x").expectArtificalFlow(flow("3")));exitStmt("x").expectArtificalFlow(flow("3")));				forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");forwardHelper.runBiDiSolver(backwardHelper,exchange,"b");	}}		@Test@Test	public void unbalancedReturnsNonMatchingCallSites() {publicvoidunbalancedReturnsNonMatchingCallSites(){		forwardHelper.method("foo",forwardHelper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b"),normalStmt("a").succ("b"),				normalStmt("b").succ("c", flow("0", "1")),normalStmt("b").succ("c",flow("0","1")),				exitStmt("c").returns(over("y1"), to("z"), flow("1", "2")));exitStmt("c").returns(over("y1"),to("z"),flow("1","2")));				forwardHelper.method("bar",forwardHelper.method("bar",				startPoints(),startPoints(),				exitStmt("z").expectArtificalFlow(/*none*/));exitStmt("z").expectArtificalFlow(/*none*/));				backwardHelper.method("foo",backwardHelper.method("foo",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("b"),normalStmt("c").succ("b"),				normalStmt("b").succ("a", flow("0", "2")),normalStmt("b").succ("a",flow("0","2")),				exitStmt("a").returns(over("y2"), to("x"), flow("2", "3")));exitStmt("a").returns(over("y2"),to("x"),flow("2","3")));				backwardHelper.method("bar",backwardHelper.method("bar",				startPoints(),startPoints(),				exitStmt("x").expectArtificalFlow(/*none*/));exitStmt("x").expectArtificalFlow(/*none*/));				forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");forwardHelper.runBiDiSolver(backwardHelper,exchange,"b");



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

113
	}}



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

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
		@Test@Test	public void returnsOnlyOneDirection() {publicvoidreturnsOnlyOneDirection(){		forwardHelper.method("foo",forwardHelper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b"),normalStmt("a").succ("b"),				normalStmt("b").succ("c", flow("0", "1")),normalStmt("b").succ("c",flow("0","1")),				exitStmt("c").returns(over("y"), to("z"), flow("1", "2")));exitStmt("c").returns(over("y"),to("z"),flow("1","2")));				forwardHelper.method("bar",forwardHelper.method("bar",				startPoints(),startPoints(),				exitStmt("z").expectArtificalFlow(/*none*/));exitStmt("z").expectArtificalFlow(/*none*/));				backwardHelper.method("foo",backwardHelper.method("foo",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("b"),normalStmt("c").succ("b"),



test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



test case capturing that in the current implementation of BiDiIFDSSolver


 

 

test case capturing that in the current implementation of BiDiIFDSSolver

 

Johannes Lerch
committed
Jun 25, 2014

130

131
				normalStmt("b").succ("a", kill("0")),normalStmt("b").succ("a",kill("0")),				exitStmt("a").returns(over("y"), to("x") /*none*/));exitStmt("a").returns(over("y"),to("x")/*none*/));



Additional tests for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



Additional tests for BiDiSolver


 

 

Additional tests for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

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
				backwardHelper.method("bar",backwardHelper.method("bar",				startPoints(),startPoints(),				exitStmt("x").expectArtificalFlow(/*none*/));exitStmt("x").expectArtificalFlow(/*none*/));				forwardHelper.runBiDiSolver(backwardHelper, exchange, "b");forwardHelper.runBiDiSolver(backwardHelper,exchange,"b");	}}		@Test@Test	public void reuseSummary() {publicvoidreuseSummary(){		forwardHelper.method("foo",forwardHelper.method("foo",				startPoints(),startPoints(),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("c",kill("1")),				callSite("c").calls("bar", flow("1", "2")).retSite("d", kill("1")),callSite("c").calls("bar",flow("1","2")).retSite("d",kill("1")),				exitStmt("d").expectArtificalFlow(kill("1")));exitStmt("d").expectArtificalFlow(kill("1")));				forwardHelper.method("bar",forwardHelper.method("bar",				startPoints("x"),startPoints("x"),				normalStmt("x").succ("y", flow("2", "2")),normalStmt("x").succ("y",flow("2","2")),				exitStmt("y").returns(over("b"), to("c"), flow("2", "1"))exitStmt("y").returns(over("b"),to("c"),flow("2","1"))							.returns(over("c"), to("d"), flow("2", "1")));.returns(over("c"),to("d"),flow("2","1")));				backwardHelper.method("foo",backwardHelper.method("foo",				startPoints(),startPoints(),				exitStmt("a").expectArtificalFlow(kill("0")));exitStmt("a").expectArtificalFlow(kill("0")));							forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");forwardHelper.runBiDiSolver(backwardHelper,exchange,"a");	}}		@Test@Test	public void multipleSeedsPreventReusingSummary() {publicvoidmultipleSeedsPreventReusingSummary(){		forwardHelper.method("foo",forwardHelper.method("foo",				startPoints(),startPoints(),				normalStmt("a1").succ("b", flow("0", "1")),normalStmt("a1").succ("b",flow("0","1")),				normalStmt("a2").succ("b", flow("0", "1")),normalStmt("a2").succ("b",flow("0","1")),				callSite("b").calls("bar", flow(times(2), "1", "2")).retSite("c", kill(times(2), "1")),callSite("b").calls("bar",flow(times(2),"1","2")).retSite("c",kill(times(2),"1")),				callSite("c").calls("bar", flow(times(2), "1", "2")).retSite("d", kill(times(2), "1")),callSite("c").calls("bar",flow(times(2),"1","2")).retSite("d",kill(times(2),"1")),				exitStmt("d").expectArtificalFlow(kill(times(2), "1")));exitStmt("d").expectArtificalFlow(kill(times(2),"1")));				forwardHelper.method("bar",forwardHelper.method("bar",				startPoints("x"),startPoints("x"),				normalStmt("x").succ("y", flow("2", "2")),normalStmt("x").succ("y",flow("2","2")),				exitStmt("y").returns(over("b"), to("c"), flow(times(2), "2", "1"))exitStmt("y").returns(over("b"),to("c"),flow(times(2),"2","1"))							 .returns(over("c"), to("d"), flow(times(2), "2", "1")));.returns(over("c"),to("d"),flow(times(2),"2","1")));				backwardHelper.method("foo",backwardHelper.method("foo",				startPoints(),startPoints(),				exitStmt("a1").expectArtificalFlow(kill("0")),exitStmt("a1").expectArtificalFlow(kill("0")),				exitStmt("a2").expectArtificalFlow(kill("0")));exitStmt("a2").expectArtificalFlow(kill("0")));							forwardHelper.runBiDiSolver(backwardHelper, exchange, "a1", "a2");forwardHelper.runBiDiSolver(backwardHelper,exchange,"a1","a2");	}}



test case capturing that in the current implementation of BiDiIFDSSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



test case capturing that in the current implementation of BiDiIFDSSolver


 

 

test case capturing that in the current implementation of BiDiIFDSSolver

 

Johannes Lerch
committed
Jun 25, 2014

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
	@Test@Test	public void dontUnpauseIfReturnFlowIsKilled() {publicvoiddontUnpauseIfReturnFlowIsKilled(){		forwardHelper.method("foo",forwardHelper.method("foo",				startPoints(), startPoints(),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				exitStmt("b").returns(over("cs"), to("y"), kill("1")));exitStmt("b").returns(over("cs"),to("y"),kill("1")));				forwardHelper.method("bar",forwardHelper.method("bar",				startPoints(),startPoints(),				normalStmt("y").succ("z" /* none */));normalStmt("y").succ("z"/* none */));				backwardHelper.method("foo",backwardHelper.method("foo",				startPoints(),startPoints(),				normalStmt("a").succ("c", flow("0", "1")),normalStmt("a").succ("c",flow("0","1")),				exitStmt("c").returns(over("cs"), to("x"), flow("1", "2")));exitStmt("c").returns(over("cs"),to("x"),flow("1","2")));				backwardHelper.method("bar",backwardHelper.method("bar",				startPoints(),startPoints(),				normalStmt("x").succ("z" /*none*/));normalStmt("x").succ("z"/*none*/));				forwardHelper.runBiDiSolver(backwardHelper, exchange, "a");forwardHelper.runBiDiSolver(backwardHelper,exchange,"a");	}}



package restructuring & initial test for BiDiSolver



 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver



 

package restructuring & initial test for BiDiSolver


Johannes Lerch
committed
Jun 25, 2014

208
}}





