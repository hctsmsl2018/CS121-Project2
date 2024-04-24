



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

3b06434708aedf1d00a74d2ccf2572cdc5c5531a

















3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveSolverTest.java



5.82 KB









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



import org.junit.Before;
import org.junit.Test;

import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.3")),
				normalStmt("c").succ("d", flow("2.3", "2.3")),
				readFieldStmt("d", "3").succ("e", flow("2.3", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")));
		helper.runSolver(false, "a");
	}
	
	@Test









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






56




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






57




58




59




60




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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




				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("retC", flow("2.field", "2.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")),
				exitStmt("f").returns(over("c"), to("retC"), flow("4.field", "5.field")).returns(over("g"), to("retG"), flow("4.anotherField", "6.anotherField")));

		helper.method("xyz", 
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.anotherField")).retSite("retG", kill("0")));
		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









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




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				readFieldStmt("d", "notfield").succ("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






94




				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






95




96




97




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






98




				readFieldStmt("d", "notfield").succ("e", flow("3", "3"), kill("3.notfield")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				normalStmt("e").succ("f", flow("3","4")));
		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.notfield")));
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
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

3b06434708aedf1d00a74d2ccf2572cdc5c5531a

















3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveSolverTest.java



5.82 KB









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



import org.junit.Before;
import org.junit.Test;

import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.3")),
				normalStmt("c").succ("d", flow("2.3", "2.3")),
				readFieldStmt("d", "3").succ("e", flow("2.3", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")));
		helper.runSolver(false, "a");
	}
	
	@Test









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






56




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






57




58




59




60




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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




				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("retC", flow("2.field", "2.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")),
				exitStmt("f").returns(over("c"), to("retC"), flow("4.field", "5.field")).returns(over("g"), to("retG"), flow("4.anotherField", "6.anotherField")));

		helper.method("xyz", 
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.anotherField")).retSite("retG", kill("0")));
		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









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




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				readFieldStmt("d", "notfield").succ("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






94




				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






95




96




97




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






98




				readFieldStmt("d", "notfield").succ("e", flow("3", "3"), kill("3.notfield")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				normalStmt("e").succ("f", flow("3","4")));
		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.notfield")));
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	
}











Open sidebar



Joshua Garcia heros

3b06434708aedf1d00a74d2ccf2572cdc5c5531a







Open sidebar



Joshua Garcia heros

3b06434708aedf1d00a74d2ccf2572cdc5c5531a




Open sidebar

Joshua Garcia heros

3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Joshua Garciaherosheros
3b06434708aedf1d00a74d2ccf2572cdc5c5531a










3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveSolverTest.java



5.82 KB









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



import org.junit.Before;
import org.junit.Test;

import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.3")),
				normalStmt("c").succ("d", flow("2.3", "2.3")),
				readFieldStmt("d", "3").succ("e", flow("2.3", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")));
		helper.runSolver(false, "a");
	}
	
	@Test









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






56




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






57




58




59




60




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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




				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("retC", flow("2.field", "2.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")),
				exitStmt("f").returns(over("c"), to("retC"), flow("4.field", "5.field")).returns(over("g"), to("retG"), flow("4.anotherField", "6.anotherField")));

		helper.method("xyz", 
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.anotherField")).retSite("retG", kill("0")));
		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









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




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				readFieldStmt("d", "notfield").succ("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






94




				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






95




96




97




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






98




				readFieldStmt("d", "notfield").succ("e", flow("3", "3"), kill("3.notfield")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				normalStmt("e").succ("f", flow("3","4")));
		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.notfield")));
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	
}














3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveSolverTest.java



5.82 KB









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



import org.junit.Before;
import org.junit.Test;

import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.3")),
				normalStmt("c").succ("d", flow("2.3", "2.3")),
				readFieldStmt("d", "3").succ("e", flow("2.3", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")));
		helper.runSolver(false, "a");
	}
	
	@Test









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






56




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






57




58




59




60




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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




				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("retC", flow("2.field", "2.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")),
				exitStmt("f").returns(over("c"), to("retC"), flow("4.field", "5.field")).returns(over("g"), to("retG"), flow("4.anotherField", "6.anotherField")));

		helper.method("xyz", 
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.anotherField")).retSite("retG", kill("0")));
		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









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




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				readFieldStmt("d", "notfield").succ("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






94




				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






95




96




97




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






98




				readFieldStmt("d", "notfield").succ("e", flow("3", "3"), kill("3.notfield")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				normalStmt("e").succ("f", flow("3","4")));
		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.notfield")));
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	
}










3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink




3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java





3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Switch branch/tag








3b06434708aedf1d00a74d2ccf2572cdc5c5531a


Switch branch/tag





3b06434708aedf1d00a74d2ccf2572cdc5c5531a

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

alias

FieldSensitiveSolverTest.java
Find file
Normal viewHistoryPermalink




FieldSensitiveSolverTest.java



5.82 KB









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



import org.junit.Before;
import org.junit.Test;

import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.3")),
				normalStmt("c").succ("d", flow("2.3", "2.3")),
				readFieldStmt("d", "3").succ("e", flow("2.3", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")));
		helper.runSolver(false, "a");
	}
	
	@Test









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






56




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






57




58




59




60




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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




				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("retC", flow("2.field", "2.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")),
				exitStmt("f").returns(over("c"), to("retC"), flow("4.field", "5.field")).returns(over("g"), to("retG"), flow("4.anotherField", "6.anotherField")));

		helper.method("xyz", 
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.anotherField")).retSite("retG", kill("0")));
		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









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




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				readFieldStmt("d", "notfield").succ("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






94




				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






95




96




97




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






98




				readFieldStmt("d", "notfield").succ("e", flow("3", "3"), kill("3.notfield")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				normalStmt("e").succ("f", flow("3","4")));
		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.notfield")));
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	
}








FieldSensitiveSolverTest.java



5.82 KB










FieldSensitiveSolverTest.java



5.82 KB









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



import org.junit.Before;
import org.junit.Test;

import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.3")),
				normalStmt("c").succ("d", flow("2.3", "2.3")),
				readFieldStmt("d", "3").succ("e", flow("2.3", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")));
		helper.runSolver(false, "a");
	}
	
	@Test









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






56




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






57




58




59




60




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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




				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("retC", flow("2.field", "2.field")));
		
		helper.method("foo",startPoints("d"),
				normalStmt("d").succ("e", flow("3", "4")),
				normalStmt("e").succ("f", flow("4","4")),
				exitStmt("f").returns(over("c"), to("retC"), flow("4.field", "5.field")).returns(over("g"), to("retG"), flow("4.anotherField", "6.anotherField")));

		helper.method("xyz", 
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.anotherField")).retSite("retG", kill("0")));
		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









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




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),
				readFieldStmt("d", "notfield").succ("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






94




				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






95




96




97




				callSite("c").calls("foo", flow("2.field", "3.field")));
		
		helper.method("foo",startPoints("d"),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






98




				readFieldStmt("d", "notfield").succ("e", flow("3", "3"), kill("3.notfield")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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




				normalStmt("e").succ("f", flow("3","4")));
		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("foo", flow("0", "3.notfield")));
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import org.junit.Before;importorg.junit.Before;import org.junit.Test;importorg.junit.Test;import static heros.alias.TestHelper.*;importstaticheros.alias.TestHelper.*;public class FieldSensitiveSolverTest {publicclassFieldSensitiveSolverTest{	private TestHelper helper;privateTestHelperhelper;	@Before@Before	public void before() {publicvoidbefore(){		helper = new TestHelper();helper=newTestHelper();	}}		@Test@Test	public void fieldReadAndWrite() {publicvoidfieldReadAndWrite(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				writeFieldStmt("b", "3").succ("c", flow("1", "2.3")),writeFieldStmt("b","3").succ("c",flow("1","2.3")),				normalStmt("c").succ("d", flow("2.3", "2.3")),normalStmt("c").succ("d",flow("2.3","2.3")),				readFieldStmt("d", "3").succ("e", flow("2.3", "4")));readFieldStmt("d","3").succ("e",flow("2.3","4")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void createSummaryForBaseValue() {publicvoidcreateSummaryForBaseValue(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),writeFieldStmt("b","3").succ("c",flow("1","2.field")),				callSite("c").calls("foo", flow("2.field", "3.field")));callSite("c").calls("foo",flow("2.field","3.field")));				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),				normalStmt("d").succ("e", flow("3", "4")),normalStmt("d").succ("e",flow("3","4")),				normalStmt("e").succ("f", flow("4","4")));normalStmt("e").succ("f",flow("4","4")));		helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

56
	public void reuseSummaryForBaseValue() {publicvoidreuseSummaryForBaseValue(){



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

57

58

59

60
		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				writeFieldStmt("b", "3").succ("c", flow("1", "2.field")),writeFieldStmt("b","3").succ("c",flow("1","2.field")),



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

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
				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("retC", flow("2.field", "2.field")));callSite("c").calls("foo",flow("2.field","3.field")).retSite("retC",flow("2.field","2.field")));				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),				normalStmt("d").succ("e", flow("3", "4")),normalStmt("d").succ("e",flow("3","4")),				normalStmt("e").succ("f", flow("4","4")),normalStmt("e").succ("f",flow("4","4")),				exitStmt("f").returns(over("c"), to("retC"), flow("4.field", "5.field")).returns(over("g"), to("retG"), flow("4.anotherField", "6.anotherField")));exitStmt("f").returns(over("c"),to("retC"),flow("4.field","5.field")).returns(over("g"),to("retG"),flow("4.anotherField","6.anotherField")));		helper.method("xyz", helper.method("xyz",				startPoints("g"),startPoints("g"),				callSite("g").calls("foo", flow("0", "3.anotherField")).retSite("retG", kill("0")));callSite("g").calls("foo",flow("0","3.anotherField")).retSite("retG",kill("0")));				helper.runSolver(false, "a", "g");helper.runSolver(false,"a","g");	}}		@Test@Test	public void hold() {publicvoidhold(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),writeFieldStmt("b","field").succ("c",flow("1","2.field")),



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
				callSite("c").calls("foo", flow("2.field", "3.field")));callSite("c").calls("foo",flow("2.field","3.field")));				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),				readFieldStmt("d", "notfield").succ("e", flow("3", "3")),readFieldStmt("d","notfield").succ("e",flow("3","3")),				normalStmt("e").succ("f", flow("3","4")));normalStmt("e").succ("f",flow("3","4")));		helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void holdAndResume() {publicvoidholdAndResume(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

94
				writeFieldStmt("b", "field").succ("c", flow("1", "2.field")),writeFieldStmt("b","field").succ("c",flow("1","2.field")),



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

95

96

97
				callSite("c").calls("foo", flow("2.field", "3.field")));callSite("c").calls("foo",flow("2.field","3.field")));				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

98
				readFieldStmt("d", "notfield").succ("e", flow("3", "3"), kill("3.notfield")),readFieldStmt("d","notfield").succ("e",flow("3","3"),kill("3.notfield")),



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
				normalStmt("e").succ("f", flow("3","4")));normalStmt("e").succ("f",flow("3","4")));				helper.method("xyz",helper.method("xyz",				startPoints("g"),startPoints("g"),				callSite("g").calls("foo", flow("0", "3.notfield")));callSite("g").calls("foo",flow("0","3.notfield")));		helper.runSolver(false, "a", "g");helper.runSolver(false,"a","g");	}}		@Test@Test	public void happyPath() {publicvoidhappyPath(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "x")),normalStmt("a").succ("b",flow("0","x")),				normalStmt("b").succ("c", flow("x", "x")),normalStmt("b").succ("c",flow("x","x")),				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));callSite("c").calls("foo",flow("x","y")).retSite("f",flow("x","x")));				helper.method("foo",helper.method("foo",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("y", "y", "z")),normalStmt("d").succ("e",flow("y","y","z")),				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));exitStmt("e").returns(over("c"),to("f"),flow("z","u"),flow("y")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void reuseSummary() {publicvoidreuseSummary(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),callSite("a").calls("bar",flow("0","x")).retSite("b",flow("0","y")),				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),callSite("b").calls("bar",flow("y","x")).retSite("c",flow("y")),				normalStmt("c").succ("c0", flow("w", "0")));normalStmt("c").succ("c0",flow("w","0")));				helper.method("bar",helper.method("bar",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("x", "z")),normalStmt("d").succ("e",flow("x","z")),				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))exitStmt("e").returns(over("a"),to("b"),flow("z","y"))							  .returns(over("b"), to("c"), flow("z", "w")));.returns(over("b"),to("c"),flow("z","w")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void reuseSummaryForRecursiveCall() {publicvoidreuseSummaryForRecursiveCall(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),callSite("a").calls("bar",flow("0","1")).retSite("b",flow("0")),				normalStmt("b").succ("c", flow("2", "3")));normalStmt("b").succ("c",flow("2","3")));				helper.method("bar",helper.method("bar",				startPoints("g"),startPoints("g"),				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),normalStmt("g").succ("h",flow("1","1")).succ("i",flow("1","1")),				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),callSite("i").calls("bar",flow("1","1")).retSite("h",flow("1")),				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))exitStmt("h").returns(over("a"),to("b"),flow("1"),flow("2","2"))							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));.returns(over("i"),to("h"),flow("1","2"),flow("2","2")));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void branch() {publicvoidbranch(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),normalStmt("a").succ("b1",flow("0","x")).succ("b2",flow("0","x")),				normalStmt("b1").succ("c", flow("x", "x", "y")),normalStmt("b1").succ("c",flow("x","x","y")),				normalStmt("b2").succ("c", flow("x", "x")),normalStmt("b2").succ("c",flow("x","x")),				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),normalStmt("c").succ("d",flow("x","z"),flow("y","w")),				normalStmt("d").succ("e", flow("z"), flow("w")));normalStmt("d").succ("e",flow("z"),flow("w")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void unbalancedReturn() {publicvoidunbalancedReturn(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));exitStmt("b").returns(over("x"),to("y"),flow("1","1")));				helper.method("bar", helper.method("bar",				startPoints("unused"),startPoints("unused"),				normalStmt("y").succ("z", flow("1", "2")));normalStmt("y").succ("z",flow("1","2")));				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void artificalReturnEdgeForNoCallersCase() {publicvoidartificalReturnEdgeForNoCallersCase(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				exitStmt("b").returns(null, null, flow("1", "1")));exitStmt("b").returns(null,null,flow("1","1")));				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}	}}





