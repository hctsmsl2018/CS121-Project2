



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

0c22069e6757cf9ad722a6c71edb498456c275aa

















0c22069e6757cf9ad722a6c71edb498456c275aa


Switch branch/tag










heros


test


heros


fieldsens


FieldSensitiveIFDSSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolverTest.java



37.1 KB









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








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14














rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29












Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
	}
	








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
	}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842












abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		







For faster browsing, not all history is shown.

View entire blame








Prev


1


2


Next











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

0c22069e6757cf9ad722a6c71edb498456c275aa

















0c22069e6757cf9ad722a6c71edb498456c275aa


Switch branch/tag










heros


test


heros


fieldsens


FieldSensitiveIFDSSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolverTest.java



37.1 KB









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








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14














rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29












Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
	}
	








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
	}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842












abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		







For faster browsing, not all history is shown.

View entire blame








Prev


1


2


Next










Open sidebar



Joshua Garcia heros

0c22069e6757cf9ad722a6c71edb498456c275aa







Open sidebar



Joshua Garcia heros

0c22069e6757cf9ad722a6c71edb498456c275aa




Open sidebar

Joshua Garcia heros

0c22069e6757cf9ad722a6c71edb498456c275aa


Joshua Garciaherosheros
0c22069e6757cf9ad722a6c71edb498456c275aa










0c22069e6757cf9ad722a6c71edb498456c275aa


Switch branch/tag










heros


test


heros


fieldsens


FieldSensitiveIFDSSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolverTest.java



37.1 KB









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








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14














rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29












Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
	}
	








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
	}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842












abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		







For faster browsing, not all history is shown.

View entire blame








Prev


1


2


Next













0c22069e6757cf9ad722a6c71edb498456c275aa


Switch branch/tag










heros


test


heros


fieldsens


FieldSensitiveIFDSSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolverTest.java



37.1 KB









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








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14














rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29












Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
	}
	








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
	}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842












abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		







For faster browsing, not all history is shown.

View entire blame








Prev


1


2


Next









0c22069e6757cf9ad722a6c71edb498456c275aa


Switch branch/tag










heros


test


heros


fieldsens


FieldSensitiveIFDSSolverTest.java



Find file
Normal viewHistoryPermalink




0c22069e6757cf9ad722a6c71edb498456c275aa


Switch branch/tag










heros


test


heros


fieldsens


FieldSensitiveIFDSSolverTest.java





0c22069e6757cf9ad722a6c71edb498456c275aa


Switch branch/tag








0c22069e6757cf9ad722a6c71edb498456c275aa


Switch branch/tag





0c22069e6757cf9ad722a6c71edb498456c275aa

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

fieldsens

FieldSensitiveIFDSSolverTest.java
Find file
Normal viewHistoryPermalink




FieldSensitiveIFDSSolverTest.java



37.1 KB









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








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14














rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29












Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
	}
	








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
	}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842












abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		







For faster browsing, not all history is shown.

View entire blame







FieldSensitiveIFDSSolverTest.java



37.1 KB










FieldSensitiveIFDSSolverTest.java



37.1 KB









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








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14














rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29












Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
	}
	








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
	}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842












abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		












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








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14














rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29












Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
	}
	








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
	}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842












abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		










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








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14














rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29












Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");








skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));








use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



	}








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
	}
	








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached








handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
	}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 








cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),








handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),








Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));








Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842












abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		








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

/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/




renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;






renaming package

 


Johannes Lerch
committed
Jun 01, 2015



renaming package

 

renaming package

Johannes Lerch
committed
Jun 01, 2015


11


package heros.fieldsens;

package heros.fieldsens;packageheros.fieldsens;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




12


13


14












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











rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




15



import heros.InterproceduralCFG;






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


15


import heros.InterproceduralCFG;

import heros.InterproceduralCFG;importheros.InterproceduralCFG;




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




16


17


18


19


20



import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


16


17


18


19


20


import heros.utilities.FieldSensitiveTestHelper;
import heros.utilities.Statement;
import heros.utilities.TestDebugger;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;

import heros.utilities.FieldSensitiveTestHelper;importheros.utilities.FieldSensitiveTestHelper;import heros.utilities.Statement;importheros.utilities.Statement;import heros.utilities.TestDebugger;importheros.utilities.TestDebugger;import heros.utilities.TestFact;importheros.utilities.TestFact;import heros.utilities.TestMethod;importheros.utilities.TestMethod;




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




21










rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


21









FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




22



import org.junit.Before;






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


22


import org.junit.Before;

import org.junit.Before;importorg.junit.Before;




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




23


24



import org.junit.Ignore;
import org.junit.Rule;






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


23


24


import org.junit.Ignore;
import org.junit.Rule;

import org.junit.Ignore;importorg.junit.Ignore;import org.junit.Rule;importorg.junit.Rule;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



import org.junit.Test;






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


25


import org.junit.Test;

import org.junit.Test;importorg.junit.Test;




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




26



import org.junit.rules.TestWatcher;






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


26


import org.junit.rules.TestWatcher;

import org.junit.rules.TestWatcher;importorg.junit.rules.TestWatcher;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




27










FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


27









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




28



import static heros.utilities.FieldSensitiveTestHelper.*;






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


28


import static heros.utilities.FieldSensitiveTestHelper.*;

import static heros.utilities.FieldSensitiveTestHelper.*;importstaticheros.utilities.FieldSensitiveTestHelper.*;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




29










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









Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




30



public class FieldSensitiveIFDSSolverTest {






Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.

 

Do not check interest of callers if paused edge already available.

Johannes Lerch
committed
Jan 16, 2015


30


public class FieldSensitiveIFDSSolverTest {

public class FieldSensitiveIFDSSolverTest {publicclassFieldSensitiveIFDSSolverTest{




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




31










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









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32



	private FieldSensitiveTestHelper helper;






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


32


	private FieldSensitiveTestHelper helper;

	private FieldSensitiveTestHelper helper;privateFieldSensitiveTestHelperhelper;




skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




33



	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;






skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015



skipping resolvers if interest is transitively given

 

skipping resolvers if interest is transitively given

Johannes Lerch
committed
Jul 09, 2015


33


	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;

	private TestDebugger<String, TestFact, Statement, TestMethod> debugger;privateTestDebugger<String,TestFact,Statement,TestMethod>debugger;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




34


35


36




	@Before
	public void before() {






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



	@Before
	public void before() {

	@Before@Before	public void before() {publicvoidbefore(){




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




37



		System.err.println("-----");






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


37


		System.err.println("-----");

		System.err.println("-----");System.err.println("-----");




skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015




38



		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();






skipping resolvers if interest is transitively given

 


Johannes Lerch
committed
Jul 09, 2015



skipping resolvers if interest is transitively given

 

skipping resolvers if interest is transitively given

Johannes Lerch
committed
Jul 09, 2015


38


		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();

		debugger = new TestDebugger<String, TestFact, Statement, TestMethod>();debugger=newTestDebugger<String,TestFact,Statement,TestMethod>();




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		helper = new FieldSensitiveTestHelper(debugger);






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


39


		helper = new FieldSensitiveTestHelper(debugger);

		helper = new FieldSensitiveTestHelper(debugger);helper=newFieldSensitiveTestHelper(debugger);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




40


41



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


40


41


	}
	

	}}	




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




42


43


44



	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


42


43


44


	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {

	@Rule@Rule	public TestWatcher watcher = new TestWatcher() {publicTestWatcherwatcher=newTestWatcher(){		protected void failed(Throwable e, org.junit.runner.Description description) {protectedvoidfailed(Throwablee,org.junit.runner.Descriptiondescription){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




45



			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");






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


			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");

			debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");debugger.writeJsonDebugFile("debug/"+description.getMethodName()+".json");




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




46


47


48


49



			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


46


47


48


49


			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	

			System.err.println("---failed: "+description.getMethodName()+" ----");System.err.println("---failed: "+description.getMethodName()+" ----");		};};	};};	




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




50


51


52


53



	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


50


51


52


53


	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),

	@Test@Test	public void fieldReadAndWrite() {publicvoidfieldReadAndWrite(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




54


55


56


57


58



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


54


55


56


57


58


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				normalStmt("c", flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),normalStmt("b",flow("1",prependField("f"),"2")).succ("c"),				normalStmt("c", flow("2", "2")).succ("d"),normalStmt("c",flow("2","2")).succ("d"),				normalStmt("d", flow("2", readField("f"), "4")).succ("e"),normalStmt("d",flow("2",readField("f"),"4")).succ("e"),				normalStmt("e", kill("4")).succ("f"));normalStmt("e",kill("4")).succ("f"));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




59


60


61


62


63


64


65


66



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


59


60


61


62


63


64


65


66


		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void createSummaryForBaseValue() {publicvoidcreateSummaryForBaseValue(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




67


68


69



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


67


68


69


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),normalStmt("b",flow("1",overwriteField("field"),"2")).succ("c"),				callSite("c").calls("foo", flow("2", "3")));callSite("c").calls("foo",flow("2","3")));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




70


71



		
		helper.method("foo",startPoints("d"),






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


		
		helper.method("foo",startPoints("d"),

				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




72


73



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


72


73


				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"));

				normalStmt("d", flow("3", "4")).succ("e"),normalStmt("d",flow("3","4")).succ("e"),				normalStmt("e", flow("4","4")).succ("f"));normalStmt("e",flow("4","4")).succ("f"));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




74


75


76


77



		helper.runSolver(false, "a");
	}
	
	@Test






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


74


75


76


77


		helper.runSolver(false, "a");
	}
	
	@Test

		helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test




use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




78



	public void reuseSummaryForBaseValue() {






use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries

 

use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


78


	public void reuseSummaryForBaseValue() {

	public void reuseSummaryForBaseValue() {publicvoidreuseSummaryForBaseValue(){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




79


80



		helper.method("bar", 
				startPoints("a"),






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


80


		helper.method("bar", 
				startPoints("a"),

		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




81


82


83



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


81


82


83


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", overwriteField("field"), "2")).succ("c"),normalStmt("b",flow("1",overwriteField("field"),"2")).succ("c"),				callSite("c").calls("foo", flow("2", "3")).retSite("retC", flow("2", "2")));callSite("c").calls("foo",flow("2","3")).retSite("retC",flow("2","2")));




use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




84


85



		
		helper.method("foo",startPoints("d"),






use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries

 

use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


84


85


		
		helper.method("foo",startPoints("d"),

				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




86


87



				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),






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


				normalStmt("d", flow("3", "4")).succ("e"),
				normalStmt("e", flow("4","4")).succ("f"),

				normalStmt("d", flow("3", "4")).succ("e"),normalStmt("d",flow("3","4")).succ("e"),				normalStmt("e", flow("4","4")).succ("f"),normalStmt("e",flow("4","4")).succ("f"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




88



				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


88


				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));

				exitStmt("f").returns(over("c"), to("retC"), flow("4", "5")).returns(over("g"), to("retG"), flow("4", "6")));exitStmt("f").returns(over("c"),to("retC"),flow("4","5")).returns(over("g"),to("retG"),flow("4","6")));




use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




89


90


91




		helper.method("xyz", 
				startPoints("g"),






use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries

 

use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


89


90


91



		helper.method("xyz", 
				startPoints("g"),

		helper.method("xyz", helper.method("xyz",				startPoints("g"),startPoints("g"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




92



				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


92


				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));

				callSite("g").calls("foo", flow("0", overwriteField("anotherField"), "3")).retSite("retG", kill("0")));callSite("g").calls("foo",flow("0",overwriteField("anotherField"),"3")).retSite("retG",kill("0")));




use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014




93


94


95


96


97


98


99


100



		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),






use of abstracted summaries

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries

 

use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


93


94


95


96


97


98


99


100


		
		helper.runSolver(false, "a", "g");
	}
	
	@Test
	public void hold() {
		helper.method("bar", 
				startPoints("a"),

				helper.runSolver(false, "a", "g");helper.runSolver(false,"a","g");	}}		@Test@Test	public void hold() {publicvoidhold(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




101


102


103



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


101


102


103


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),normalStmt("b",flow("1",prependField("field"),"2")).succ("c"),				callSite("c").calls("foo", flow("2", "3")));callSite("c").calls("foo",flow("2","3")));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




104



		






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


104


		

		




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




105


106



		helper.method("foo",
				startPoints("d"),






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


106


		helper.method("foo",
				startPoints("d"),

		helper.method("foo",helper.method("foo",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




107


108



				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


107


108


				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),
				normalStmt("e", flow("3","4")).succ("f"));

				normalStmt("d", flow("3", readField("notfield"), "5"), flow("3", "3")).succ("e"),normalStmt("d",flow("3",readField("notfield"),"5"),flow("3","3")).succ("e"),				normalStmt("e", flow("3","4")).succ("f"));normalStmt("e",flow("3","4")).succ("f"));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




109


110


111


112


113


114


115



		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


109


110


111


112


113


114


115


		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),

		helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void holdAndResume() {publicvoidholdAndResume(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


116


117


118


119


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),
				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),
				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", prependField("field"), "2")).succ("c"),normalStmt("b",flow("1",prependField("field"),"2")).succ("c"),				callSite("c").calls("foo", flow("2", "3")).retSite("rs", kill("2")),callSite("c").calls("foo",flow("2","3")).retSite("rs",kill("2")),				callSite("rs").calls("foo", flow("5", prependField("notfield"), "3")));callSite("rs").calls("foo",flow("5",prependField("notfield"),"3")));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




120


121



		
		helper.method("foo",startPoints("d"),






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


		
		helper.method("foo",startPoints("d"),

				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




122


123



				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),






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


				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),
				normalStmt("e", flow("3","4"), kill("6")).succ("f"),

				normalStmt("d", flow("3", "3"), flow("3", readField("notfield"), "6")).succ("e"),normalStmt("d",flow("3","3"),flow("3",readField("notfield"),"6")).succ("e"),				normalStmt("e", flow("3","4"), kill("6")).succ("f"),normalStmt("e",flow("3","4"),kill("6")).succ("f"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




124



				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


124


				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));

				exitStmt("f").returns(over("c"), to("rs"), flow("4", "5")));exitStmt("f").returns(over("c"),to("rs"),flow("4","5")));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




125



		






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


125


		

		




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




126



		helper.runSolver(false, "a");






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


126


		helper.runSolver(false, "a");

		helper.runSolver(false, "a");helper.runSolver(false,"a");




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




127



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


127


	}

	}}




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




128


129


130


131


132




	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


128


129


130


131


132



	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),

	@Test@Test	public void doNotHoldIfInterestedTransitiveCallerExists() {publicvoiddoNotHoldIfInterestedTransitiveCallerExists(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




133


134



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


133


134


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")));callSite("b").calls("bar",flow("1","2")));




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




135


136


137



		
		helper.method("bar",
				startPoints("c"),






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


135


136


137


		
		helper.method("bar",
				startPoints("c"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




138



				callSite("c").calls("xyz", flow("2", "3")));






"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as

 

"interest/concrretization" edges in callers are no longer propagated as

Johannes Lerch
committed
Jan 07, 2015


138


				callSite("c").calls("xyz", flow("2", "3")));

				callSite("c").calls("xyz", flow("2", "3")));callSite("c").calls("xyz",flow("2","3")));




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




139


140


141



		
		helper.method("xyz", 
				startPoints("d"),






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


139


140


141


		
		helper.method("xyz", 
				startPoints("d"),

				helper.method("xyz", helper.method("xyz",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




142


143



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


142


143


				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));

				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),normalStmt("d",flow("3",readField("f"),"4")).succ("e"),				normalStmt("e", kill("4")).succ("f"));normalStmt("e",kill("4")).succ("f"));




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




144


145


146


147


148



		
		helper.runSolver(false, "a");
	}
	
	@Test






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


144


145


146


147


148


		
		helper.runSolver(false, "a");
	}
	
	@Test

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




149



	public void prefixFactOfSummaryIgnored() {






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


149


	public void prefixFactOfSummaryIgnored() {

	public void prefixFactOfSummaryIgnored() {publicvoidprefixFactOfSummaryIgnored(){




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




150


151



		helper.method("foo",
				startPoints("a"),






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


150


151


		helper.method("foo",
				startPoints("a"),

		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




152


153



				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


152


153


				normalStmt("a", flow("0","1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),

				normalStmt("a", flow("0","1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				callSite("b").calls("bar", flow("1", prependField("f"), "2")).retSite("e", kill("1")),callSite("b").calls("bar",flow("1",prependField("f"),"2")).retSite("e",kill("1")),




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




154



				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


154


				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),

				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),callSite("e").calls("bar",flow("4","2")).retSite("f",kill("4")),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




155



				normalStmt("f", kill("5")).succ("g"));






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


				normalStmt("f", kill("5")).succ("g"));

				normalStmt("f", kill("5")).succ("g"));normalStmt("f",kill("5")).succ("g"));




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




156



		






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


156


		

		




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




157



		helper.method("bar",






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


157


		helper.method("bar",

		helper.method("bar",helper.method("bar",




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




158



				startPoints("c"),






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


158


				startPoints("c"),

				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




159


160



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));






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


				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));

				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),normalStmt("c",flow("2",readField("f"),"3")).succ("d"),				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), flow("3", "5")));exitStmt("d").returns(over("b"),to("e"),flow("3","4")).returns(over("e"),to("f"),flow("3","5")));




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




161


162


163


164



		
		helper.runSolver(false, "a");
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


161


162


163


164


		
		helper.runSolver(false, "a");
	}
	

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	




handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




165



	@Test






handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary

 

handling the case that incoming edge is prefix of existing summary

Johannes Lerch
committed
Nov 27, 2014


165


	@Test

	@Test@Test




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




166



	public void doNotPauseZeroSources() {






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


166


	public void doNotPauseZeroSources() {

	public void doNotPauseZeroSources() {publicvoiddoNotPauseZeroSources(){




handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




167


168



		helper.method("foo",
				startPoints("a"),






handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary

 

handling the case that incoming edge is prefix of existing summary

Johannes Lerch
committed
Nov 27, 2014


167


168


		helper.method("foo",
				startPoints("a"),

		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




169


170



				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));






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


170


				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),
				normalStmt("b", kill("1")).succ("c"));

				normalStmt("a", flow("0", readField("f"), "1")).succ("b"),normalStmt("a",flow("0",readField("f"),"1")).succ("b"),				normalStmt("b", kill("1")).succ("c"));normalStmt("b",kill("1")).succ("c"));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




171


172


173


174


175


176


177



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


171


172


173


174


175


176


177


		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void loopAndMerge() {
		helper.method("foo",

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void loopAndMerge() {publicvoidloopAndMerge(){		helper.method("foo",helper.method("foo",




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178



				startPoints("a0"),






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


178


				startPoints("a0"),

				startPoints("a0"),startPoints("a0"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




179


180



				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


179


180


				normalStmt("a0", flow("0", "1")).succ("a1"),
				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));

				normalStmt("a0", flow("0", "1")).succ("a1"),normalStmt("a0",flow("0","1")).succ("a1"),				callSite("a1").calls("bar", flow("1", prependField("g"), "1")));callSite("a1").calls("bar",flow("1",prependField("g"),"1")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




181



		






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


181


		

		




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183



		helper.method("bar",
				startPoints("b"),






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


182


183


		helper.method("bar",
				startPoints("b"),

		helper.method("bar",helper.method("bar",				startPoints("b"),startPoints("b"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




184


185


186


187



				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


184


185


186


187


				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("b").succ("d"),
				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));

				normalStmt("b", flow("1", prependField("f"), "1")).succ("c"),normalStmt("b",flow("1",prependField("f"),"1")).succ("c"),				normalStmt("c", flow("1", "1")).succ("b").succ("d"),normalStmt("c",flow("1","1")).succ("b").succ("d"),				normalStmt("d", flow("1", readField("f"), "2")).succ("e"),normalStmt("d",flow("1",readField("f"),"2")).succ("e"),				normalStmt("e", kill("2")).succ("f"));normalStmt("e",kill("2")).succ("f"));




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




188


189


190


191


192


193


194


195


196



		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


188


189


190


191


192


193


194


195


196


		
		helper.runSolver(false, "a0");
	}
	
	@Test
	@Ignore("not implemented optimization")
	public void loopAndMergeExclusion() {
		helper.method("foo",
				startPoints("a0"),

				helper.runSolver(false, "a0");helper.runSolver(false,"a0");	}}		@Test@Test	@Ignore("not implemented optimization")@Ignore("not implemented optimization")	public void loopAndMergeExclusion() {publicvoidloopAndMergeExclusion(){		helper.method("foo",helper.method("foo",				startPoints("a0"),startPoints("a0"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




197



				normalStmt("a0", flow("0", "1")).succ("a1"),






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


				normalStmt("a0", flow("0", "1")).succ("a1"),

				normalStmt("a0", flow("0", "1")).succ("a1"),normalStmt("a0",flow("0","1")).succ("a1"),




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




198


199


200


201



				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


198


199


200


201


				callSite("a1").calls("bar", flow("1", "1.f")));
		
		helper.method("bar",
				startPoints("b"),

				callSite("a1").calls("bar", flow("1", "1.f")));callSite("a1").calls("bar",flow("1","1.f")));				helper.method("bar",helper.method("bar",				startPoints("b"),startPoints("b"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




202


203


204


205



				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


202


203


204


205


				normalStmt("b", flow("1", "1", "1^f")).succ("c"),
				normalStmt("c", flow("1", "1")).succ("d").succ("b"),
				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),
				normalStmt("e", kill("2")).succ("f"));

				normalStmt("b", flow("1", "1", "1^f")).succ("c"),normalStmt("b",flow("1","1","1^f")).succ("c"),				normalStmt("c", flow("1", "1")).succ("d").succ("b"),normalStmt("c",flow("1","1")).succ("d").succ("b"),				normalStmt("d", flow("1", overwriteField("f"), "2")).succ("e"),normalStmt("d",flow("1",overwriteField("f"),"2")).succ("e"),				normalStmt("e", kill("2")).succ("f"));normalStmt("e",kill("2")).succ("f"));




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




206


207


208



			
		
		helper.runSolver(false, "a0");






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


206


207


208


			
		
		helper.runSolver(false, "a0");

							helper.runSolver(false, "a0");helper.runSolver(false,"a0");




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




209


210


211


212


213


214



	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


209


210


211


212


213


214


	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),

	}}		@Test@Test	public void pauseOnOverwrittenFieldOfInterest() {publicvoidpauseOnOverwrittenFieldOfInterest(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




215


216



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));






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


216


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")));callSite("b").calls("bar",flow("1","2")));




handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




217


218


219



		
		helper.method("bar",
				startPoints("c"),






handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary

 

handling the case that incoming edge is prefix of existing summary

Johannes Lerch
committed
Nov 27, 2014


217


218


219


		
		helper.method("bar",
				startPoints("c"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




220



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


220


				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),

				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"2")).succ("d"),




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




221



				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


221


				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached

				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reachednormalStmt("d").succ("e"));//only interested in 2.f, but f excluded so this should not be reached




handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014




222


223


224


225



		
		helper.runSolver(false, "a");
	}
	






handling the case that incoming edge is prefix of existing summary

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary

 

handling the case that incoming edge is prefix of existing summary

Johannes Lerch
committed
Nov 27, 2014


222


223


224


225


		
		helper.runSolver(false, "a");
	}
	

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




226



	@Test






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


226


	@Test

	@Test@Test




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




227



	public void pauseOnOverwrittenFieldOfInterest2() {






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


227


	public void pauseOnOverwrittenFieldOfInterest2() {

	public void pauseOnOverwrittenFieldOfInterest2() {publicvoidpauseOnOverwrittenFieldOfInterest2(){




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




228


229



		helper.method("foo",
				startPoints("a"),






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


228


229


		helper.method("foo",
				startPoints("a"),

		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




230


231



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


230


231


				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("f"), "2")));

				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),normalStmt("a",flow("0",prependField("g"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", prependField("f"), "2")));callSite("b").calls("bar",flow("1",prependField("f"),"2")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




232


233


234



		
		helper.method("bar",
				startPoints("c"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


232


233


234


		
		helper.method("bar",
				startPoints("c"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




235



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


235


				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),

				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"2")).succ("d"),




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




236



				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


236


				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached

				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reachednormalStmt("d").succ("e"));//only interested in 2.f.g, but f excluded so this should not be reached




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




237


238


239



		
		helper.runSolver(false, "a");
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


237


238


239


		
		helper.runSolver(false, "a");
	}

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




240



	






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


	

	




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




241


242


243


244



	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


241


242


243


244


	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),

	@Test@Test	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {publicvoiddoNotPauseOnOverwrittenFieldOfInterestedPrefix(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




245


246



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


245


246


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("g"), "2")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", prependField("g"), "2")));callSite("b").calls("bar",flow("1",prependField("g"),"2")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




247


248


249



		
		helper.method("bar",
				startPoints("c"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


247


248


249


		
		helper.method("bar",
				startPoints("c"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




250


251



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


250


251


				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e")); 

				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"2")).succ("d"),				normalStmt("d", kill("2")).succ("e")); normalStmt("d",kill("2")).succ("e"));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




252


253


254


255



		
		helper.runSolver(false, "a");
	}
	






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


252


253


254


255


		
		helper.runSolver(false, "a");
	}
	

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




256


257


258


259



	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


256


257


258


259


	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),

	@Test@Test	public void pauseOnTransitiveExclusion() {publicvoidpauseOnTransitiveExclusion(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




260


261



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


260


261


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")));callSite("b").calls("bar",flow("1","2")));




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




262


263


264


265


266


267


268



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


262


263


264


265


266


267


268


		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				callSite("c").calls("xyz", flow("2", "3")));callSite("c").calls("xyz",flow("2","3")));				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




269



				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


269


				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),

				normalStmt("d", flow("3", overwriteField("f"), "3")).succ("e"),normalStmt("d",flow("3",overwriteField("f"),"3")).succ("e"),




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




270


271


272


273


274


275


276


277


278



				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


270


271


272


273


274


275


276


277


278


				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),

				normalStmt("e").succ("f")); normalStmt("e").succ("f"));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void resumePausedOnTransitiveExclusion() {publicvoidresumePausedOnTransitiveExclusion(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




279


280



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


279


280


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")));callSite("b").calls("bar",flow("1","2")));




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




281


282


283



		
		helper.method("bar",
				startPoints("c"),






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


281


282


283


		
		helper.method("bar",
				startPoints("c"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




284



				callSite("c").calls("xyz", flow("2", "3")));






"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as

 

"interest/concrretization" edges in callers are no longer propagated as

Johannes Lerch
committed
Jan 07, 2015


284


				callSite("c").calls("xyz", flow("2", "3")));

				callSite("c").calls("xyz", flow("2", "3")));callSite("c").calls("xyz",flow("2","3")));




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




285


286


287



		
		helper.method("xyz",
				startPoints("d"),






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


286


287


		
		helper.method("xyz",
				startPoints("d"),

				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




288


289



				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


288


289


				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),
				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); 

				normalStmt("d", flow("3", overwriteField("f"), "3"), flow("3", "4")).succ("e"),normalStmt("d",flow("3",overwriteField("f"),"3"),flow("3","4")).succ("e"),				callSite("e").calls("bar", flow("4", prependField("g"), "2"), kill("3"))); callSite("e").calls("bar",flow("4",prependField("g"),"2"),kill("3")));




cleaning code

 


Johannes Lerch
committed
Jan 07, 2015




290


291


292


293



		
		helper.runSolver(false, "a");
	}
	






cleaning code

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code

 

cleaning code

Johannes Lerch
committed
Jan 07, 2015


290


291


292


293


		
		helper.runSolver(false, "a");
	}
	

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




294


295


296


297



	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


294


295


296


297


	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),

	@Test@Test	public void resumeEdgePausedOnOverwrittenField() {publicvoidresumeEdgePausedOnOverwrittenField(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




298


299


300



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


298


299


300


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("e",kill("1")),				callSite("e").calls("bar", flow("4", prependField("g"), "2")).retSite("f", kill("4")));callSite("e").calls("bar",flow("4",prependField("g"),"2")).retSite("f",kill("4")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




301


302


303



		
		helper.method("bar",
				startPoints("c"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


301


302


303


		
		helper.method("bar",
				startPoints("c"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




304


305



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


304


305


				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); 

				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"2"),flow("2","3")).succ("d"),				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f"), kill("3"), kill("2"))); exitStmt("d").returns(over("b"),to("e"),flow("3","4")).returns(over("e"),to("f"),kill("3"),kill("2")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




306


307


308


309


310


311


312


313



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


306


307


308


309


310


311


312


313


		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {publicvoidresumeEdgePausedOnOverwrittenFieldForPrefixes(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




314


315


316



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


314


315


316


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("e",kill("1")),				normalStmt("e", flow("4", readField("f"), "2")).succ("f"),normalStmt("e",flow("4",readField("f"),"2")).succ("f"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




317



				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


317


				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));

				callSite("f").calls("bar", flow("2", "2")).retSite("g", kill("2")));callSite("f").calls("bar",flow("2","2")).retSite("g",kill("2")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




318


319


320



		
		helper.method("bar",
				startPoints("c"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


318


319


320


		
		helper.method("bar",
				startPoints("c"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




321


322



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


321


322


				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); 

				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", "3")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"2"),flow("2","3")).succ("d"),				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("f"), to("g"), kill("3"), kill("2"))); exitStmt("d").returns(over("b"),to("e"),flow("3","4")).returns(over("f"),to("g"),kill("3"),kill("2")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




323


324


325


326



		
		helper.runSolver(false, "a");
	}
	






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


323


324


325


326


		
		helper.runSolver(false, "a");
	}
	

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	




handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




327


328


329


330



	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),






handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths

 

handling for writing fields / excluding access paths

Johannes Lerch
committed
Jan 05, 2015


327


328


329


330


	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),

	@Test@Test	public void exclusionOnPotentiallyInterestedCaller() {publicvoidexclusionOnPotentiallyInterestedCaller(){		helper.method("foo",helper.method("foo",				startPoints("sp"),startPoints("sp"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




331


332



				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


331


332


				normalStmt("sp", flow("0", "1")).succ("a"),
				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));

				normalStmt("sp", flow("0", "1")).succ("a"),normalStmt("sp",flow("0","1")).succ("a"),				callSite("a").calls("bar", flow("1", overwriteField("f"), "1")).retSite("d", kill("1")));callSite("a").calls("bar",flow("1",overwriteField("f"),"1")).retSite("d",kill("1")));




handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




333


334


335



		
		helper.method("bar",
				startPoints("b"),






handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths

 

handling for writing fields / excluding access paths

Johannes Lerch
committed
Jan 05, 2015


333


334


335


		
		helper.method("bar",
				startPoints("b"),

				helper.method("bar",helper.method("bar",				startPoints("b"),startPoints("b"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




336



				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),






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


				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),

				normalStmt("b", flow("1", readField("f"), "2")).succ("c"),normalStmt("b",flow("1",readField("f"),"2")).succ("c"),




handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015




337


338


339


340


341



				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	






handling for writing fields / excluding access paths

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths

 

handling for writing fields / excluding access paths

Johannes Lerch
committed
Jan 05, 2015


337


338


339


340


341


				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	

				exitStmt("c").returns(over("a"), to("d")));exitStmt("c").returns(over("a"),to("d")));				helper.runSolver(false, "sp");helper.runSolver(false,"sp");	}}	




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




342


343


344


345



	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


342


343


344


345


	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),

	@Test@Test	public void registerPausedEdgeInLateCallers() {publicvoidregisterPausedEdgeInLateCallers(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




346


347


348



				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


346


347


348


				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),
				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),

				normalStmt("a", flow("0", prependField("g"), "1")).succ("b"),normalStmt("a",flow("0",prependField("g"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "1")).retSite("e", kill("1")),callSite("b").calls("bar",flow("1","1")).retSite("e",kill("1")),				normalStmt("e", flow("1", readField("g"), "3")).succ("f"),normalStmt("e",flow("1",readField("g"),"3")).succ("f"),




"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




349



				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 






"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as

 

"interest/concrretization" edges in callers are no longer propagated as

Johannes Lerch
committed
Jan 07, 2015


349


				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 

				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); callSite("f").calls("bar",flow("3","1")).retSite("g",kill("3")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




350


351


352



		
		helper.method("bar",
				startPoints("c"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


350


351


352


		
		helper.method("bar",
				startPoints("c"),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




353


354


355



				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


353


354


355


				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)
							 .returns(over("f"), to("g"), kill("1"), kill("2")));

				normalStmt("c", flow("1", readField("f"), "2"), flow("1", "1")).succ("d"),normalStmt("c",flow("1",readField("f"),"2"),flow("1","1")).succ("d"),				exitStmt("d").returns(over("b"), to("e"), flow("1", "1") /* ignore fact 2, not possible with this caller ctx*/)exitStmt("d").returns(over("b"),to("e"),flow("1","1")/* ignore fact 2, not possible with this caller ctx*/)							 .returns(over("f"), to("g"), kill("1"), kill("2")));.returns(over("f"),to("g"),kill("1"),kill("2")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




356


357


358


359


360



		
		helper.runSolver(false, "a");
	}
	
	@Test






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


356


357


358


359


360


		
		helper.runSolver(false, "a");
	}
	
	@Test

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




361



	@Ignore("not implemented optimization")






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


361


	@Ignore("not implemented optimization")

	@Ignore("not implemented optimization")@Ignore("not implemented optimization")




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




362


363


364



	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


362


363


364


	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),

	public void mergeExcludedField() {publicvoidmergeExcludedField(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




365


366


367



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


365


366


367


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", "2", "2^f")).succ("c"),
				normalStmt("c", kill("2")).succ("d"));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", "2", "2^f")).succ("c"),normalStmt("b",flow("1","2","2^f")).succ("c"),				normalStmt("c", kill("2")).succ("d"));normalStmt("c",kill("2")).succ("d"));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




368


369


370


371


372


373


374


375



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


368


369


370


371


372


373


374


375


		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void resumeOnTransitiveInterestedCaller() {publicvoidresumeOnTransitiveInterestedCaller(){		helper.method("foo",helper.method("foo",				startPoints("sp"),startPoints("sp"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




376


377


378



				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


376


377


378


				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),
				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),
				callSite("f").calls("bar", flow("2", prependField("g"), "1")));

				normalStmt("sp", flow("0", prependField("f"), "1")).succ("a"),normalStmt("sp",flow("0",prependField("f"),"1")).succ("a"),				callSite("a").calls("bar", flow("1", "1")).retSite("f", kill("1")),callSite("a").calls("bar",flow("1","1")).retSite("f",kill("1")),				callSite("f").calls("bar", flow("2", prependField("g"), "1")));callSite("f").calls("bar",flow("2",prependField("g"),"1")));




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




379


380


381



				
		helper.method("bar",
				startPoints("b"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


379


380


381


				
		helper.method("bar",
				startPoints("b"),

						helper.method("bar",helper.method("bar",				startPoints("b"),startPoints("b"),




"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




382



				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),






"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as

 

"interest/concrretization" edges in callers are no longer propagated as

Johannes Lerch
committed
Jan 07, 2015


382


				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),

				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),callSite("b").calls("xyz",flow("1","1")).retSite("e",kill("1")),




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




383


384


385


386



				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


383


384


385


386


				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),

				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));exitStmt("e").returns(over("a"),to("f"),flow("2","2")));				helper.method("xyz",helper.method("xyz",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




387



				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),






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


				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),

				normalStmt("c", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")).succ("d"),normalStmt("c",flow("1",readField("g"),"3"),flow("1",readField("f"),"2")).succ("d"),




Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014




388


389


390


391


392


393



				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	






Bug/test fixes

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes

 

Bug/test fixes

Johannes Lerch
committed
Dec 10, 2014


388


389


390


391


392


393


				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	

				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));exitStmt("d").returns(over("b"),to("e"),flow("2","2"),kill("3")));								helper.runSolver(false, "sp");helper.runSolver(false,"sp");	}}	




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




394


395


396


397



	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


394


395


396


397


	@Test
	public void happyPath() {
		helper.method("bar", 
				startPoints("a"),

	@Test@Test	public void happyPath() {publicvoidhappyPath(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




398


399



				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


398


399


				normalStmt("a", flow("0", "x")).succ("b"),
				normalStmt("b", flow("x", "x")).succ("c"),

				normalStmt("a", flow("0", "x")).succ("b"),normalStmt("a",flow("0","x")).succ("b"),				normalStmt("b", flow("x", "x")).succ("c"),normalStmt("b",flow("x","x")).succ("c"),




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




400


401


402


403



				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),






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


				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),

				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));callSite("c").calls("foo",flow("x","y")).retSite("f",flow("x","x")));				helper.method("foo",helper.method("foo",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




404



				normalStmt("d", flow("y", "y", "z")).succ("e"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


404


				normalStmt("d", flow("y", "y", "z")).succ("e"),

				normalStmt("d", flow("y", "y", "z")).succ("e"),normalStmt("d",flow("y","y","z")).succ("e"),




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




405


406


407


408


409


410


411


412


413



				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),






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


408


409


410


411


412


413


				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),

				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));exitStmt("e").returns(over("c"),to("f"),flow("z","u"),flow("y")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void reuseSummary() {publicvoidreuseSummary(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




414


415


416



				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


414


415


416


				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),
				normalStmt("c", flow("w", "0")).succ("c0"));

				callSite("a").calls("bar", flow("0", "x")).retSite("b", kill("0")),callSite("a").calls("bar",flow("0","x")).retSite("b",kill("0")),				callSite("b").calls("bar", flow("y", "x")).retSite("c", kill("y")),callSite("b").calls("bar",flow("y","x")).retSite("c",kill("y")),				normalStmt("c", flow("w", "0")).succ("c0"));normalStmt("c",flow("w","0")).succ("c0"));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




417


418


419



		
		helper.method("bar",
				startPoints("d"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


417


418


419


		
		helper.method("bar",
				startPoints("d"),

				helper.method("bar",helper.method("bar",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




420



				normalStmt("d", flow("x", "z")).succ("e"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


420


				normalStmt("d", flow("x", "z")).succ("e"),

				normalStmt("d", flow("x", "z")).succ("e"),normalStmt("d",flow("x","z")).succ("e"),




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




421



				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


421


				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))

				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))exitStmt("e").returns(over("a"),to("b"),flow("z","y"))




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




422



							  .returns(over("b"), to("c"), flow("z", "w")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


422


							  .returns(over("b"), to("c"), flow("z", "w")));

							  .returns(over("b"), to("c"), flow("z", "w")));.returns(over("b"),to("c"),flow("z","w")));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




423


424


425


426


427


428


429


430


431



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


423


424


425


426


427


428


429


430


431


		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void reuseSummaryForRecursiveCall() {publicvoidreuseSummaryForRecursiveCall(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),callSite("a").calls("bar",flow("0","1")).retSite("b",flow("0")),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




432



				normalStmt("b", flow("2", "3")).succ("c"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


432


				normalStmt("b", flow("2", "3")).succ("c"));

				normalStmt("b", flow("2", "3")).succ("c"));normalStmt("b",flow("2","3")).succ("c"));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




433


434


435



		
		helper.method("bar",
				startPoints("g"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


433


434


435


		
		helper.method("bar",
				startPoints("g"),

				helper.method("bar",helper.method("bar",				startPoints("g"),startPoints("g"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




436



				normalStmt("g", flow("1", "1")).succ("i").succ("h"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


436


				normalStmt("g", flow("1", "1")).succ("i").succ("h"),

				normalStmt("g", flow("1", "1")).succ("i").succ("h"),normalStmt("g",flow("1","1")).succ("i").succ("h"),




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),






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


				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),

				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),callSite("i").calls("bar",flow("1","1")).retSite("h",flow("1")),				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))exitStmt("h").returns(over("a"),to("b"),flow("1"),flow("2","2"))							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));.returns(over("i"),to("h"),flow("1","2"),flow("2","2")));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void branch() {publicvoidbranch(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




448


449


450


451


452



				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));






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


449


450


451


452


				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),
				normalStmt("b1", flow("x", "x", "y")).succ("c"),
				normalStmt("b2", flow("x", "x")).succ("c"),
				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),
				normalStmt("d", flow("z"), flow("w")).succ("e"));

				normalStmt("a", flow("0", "x")).succ("b2").succ("b1"),normalStmt("a",flow("0","x")).succ("b2").succ("b1"),				normalStmt("b1", flow("x", "x", "y")).succ("c"),normalStmt("b1",flow("x","x","y")).succ("c"),				normalStmt("b2", flow("x", "x")).succ("c"),normalStmt("b2",flow("x","x")).succ("c"),				normalStmt("c", flow("x", "z"), flow("y", "w")).succ("d"),normalStmt("c",flow("x","z"),flow("y","w")).succ("d"),				normalStmt("d", flow("z"), flow("w")).succ("e"));normalStmt("d",flow("z"),flow("w")).succ("e"));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




453


454


455


456


457


458


459


460



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


453


454


455


456


457


458


459


460


		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void unbalancedReturn() {publicvoidunbalancedReturn(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




461



				normalStmt("a", flow("0", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


461


				normalStmt("a", flow("0", "1")).succ("b"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




462


463


464


465



				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


462


463


464


465


				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),

				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));exitStmt("b").returns(over("x"),to("y"),flow("1","1")));				helper.method("bar", helper.method("bar",				startPoints("unused"),startPoints("unused"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




466


467



				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


466


467


				normalStmt("x").succ("y"),
				normalStmt("y", flow("1", "2")).succ("z"));

				normalStmt("x").succ("y"),normalStmt("x").succ("y"),				normalStmt("y", flow("1", "2")).succ("z"));normalStmt("y",flow("1","2")).succ("z"));




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




468


469


470


471


472


473


474


475



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


468


469


470


471


472


473


474


475


		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void artificalReturnEdgeForNoCallersCase() {publicvoidartificalReturnEdgeForNoCallersCase(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




476



				normalStmt("a", flow("0", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


476


				normalStmt("a", flow("0", "1")).succ("b"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




477


478


479


480


481



				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
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


477


478


479


480


481


				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
	

				exitStmt("b").returns(null, null, flow("1", "1")));exitStmt("b").returns(null,null,flow("1","1")));				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}	




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




482


483


484


485



	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


482


483


484


485


	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),

	@Test@Test	public void pauseEdgeMutuallyRecursiveCallers() {publicvoidpauseEdgeMutuallyRecursiveCallers(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




486


487



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


486


487


				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("bar",flow("1", "2")));

				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),normalStmt("a",flow("0",prependField("x"),"1")).succ("b"),				callSite("b").calls("bar",flow("1", "2")));callSite("b").calls("bar",flow("1","2")));




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




488


489


490


491


492


493


494


495



		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),






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


493


494


495


		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),

				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				callSite("c").calls("xyz", flow("2", "3")));callSite("c").calls("xyz",flow("2","3")));				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),callSite("d").calls("bar",flow("3","2")).retSite("e",flow("3","3")),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




496



				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


496


				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));

				normalStmt("e", flow("3", readField("f"), "4")).succ("f"));normalStmt("e",flow("3",readField("f"),"4")).succ("f"));




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




497


498


499


500


501


502


503


504



				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


497


498


499


500


501


502


503


504


				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),

						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void pauseDiamondShapedCallerChain() {publicvoidpauseDiamondShapedCallerChain(){		helper.method("bar",helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




505


506



				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


505


506


				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));

				normalStmt("a", flow("0", prependField("x"), "1")).succ("b"),normalStmt("a",flow("0",prependField("x"),"1")).succ("b"),				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));callSite("b").calls("foo1",flow("1","2")).calls("foo2",flow("1","2")));




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


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


		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),

				helper.method("foo1",helper.method("foo1",				startPoints("c1"),startPoints("c1"),				callSite("c1").calls("xyz", flow("2", "3")));callSite("c1").calls("xyz",flow("2","3")));				helper.method("foo2",helper.method("foo2",				startPoints("c2"),startPoints("c2"),				callSite("c2").calls("xyz", flow("2", "3")));callSite("c2").calls("xyz",flow("2","3")));				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




518



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


518


				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),

				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),normalStmt("d",flow("3",readField("f"),"4")).succ("e"),




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




519


520


521


522


523


524


525


526


527



				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


519


520


521


522


523


524


525


526


527


				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),

				normalStmt("e").succ("f"));normalStmt("e").succ("f"));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	@Test@Test	public void dontPauseDiamondShapedCallerChain() {publicvoiddontPauseDiamondShapedCallerChain(){		helper.method("bar",helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




528


529



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


528


529


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("foo1", flow("1", "2")).calls("foo2", flow("1", "2")));callSite("b").calls("foo1",flow("1","2")).calls("foo2",flow("1","2")));




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




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



		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


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


		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),

				helper.method("foo1",helper.method("foo1",				startPoints("c1"),startPoints("c1"),				callSite("c1").calls("xyz", flow("2", "3")));callSite("c1").calls("xyz",flow("2","3")));				helper.method("foo2",helper.method("foo2",				startPoints("c2"),startPoints("c2"),				callSite("c2").calls("xyz", flow("2", "3")));callSite("c2").calls("xyz",flow("2","3")));				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




541


542



				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


541


542


				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));

				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),normalStmt("d",flow("3",readField("f"),"4")).succ("e"),				normalStmt("e", kill("4")).succ("f"));normalStmt("e",kill("4")).succ("f"));




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




543


544


545


546


547


548


549


550



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


543


544


545


546


547


548


549


550


		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void correctDeltaConstraintApplication() {publicvoidcorrectDeltaConstraintApplication(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




551



				normalStmt("a", flow("0", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


551


				normalStmt("a", flow("0", "1")).succ("b"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




552


553


554


555



				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


552


553


554


555


				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),

				callSite("b").calls("bar", flow("1", "1")));callSite("b").calls("bar",flow("1","1")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




556


557



				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


556


557


				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),
				callSite("d").calls("xyz", flow("1", "1")));

				normalStmt("c", flow("1", overwriteField("a"), "1")).succ("d"),normalStmt("c",flow("1",overwriteField("a"),"1")).succ("d"),				callSite("d").calls("xyz", flow("1", "1")));callSite("d").calls("xyz",flow("1","1")));




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




558


559


560



		
		helper.method("xyz",
				startPoints("e"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


558


559


560


		
		helper.method("xyz",
				startPoints("e"),

				helper.method("xyz",helper.method("xyz",				startPoints("e"),startPoints("e"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




561



				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


561


				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),

				normalStmt("e", flow("1", readField("f"), "2")).succ("f"),normalStmt("e",flow("1",readField("f"),"2")).succ("f"),




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




562


563


564


565



				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


562


563


564


565


				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),

				callSite("f").calls("baz", flow("2", "3")));callSite("f").calls("baz",flow("2","3")));				helper.method("baz",helper.method("baz",				startPoints("g"),startPoints("g"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




566



				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


566


				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));

				normalStmt("g", flow("3", readField("a"), "4")).succ("h"));normalStmt("g",flow("3",readField("a"),"4")).succ("h"));




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




567


568


569



		
		helper.runSolver(false, "a");
	}






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


567


568


569


		
		helper.runSolver(false, "a");
	}

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}




Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




570


571


572


573


574



	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),






Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.

 

Do not check interest of callers if paused edge already available.

Johannes Lerch
committed
Jan 16, 2015


570


571


572


573


574


	
	@Test
	public void pauseForSameSourceMultipleTimes() {
		helper.method("foo",
				startPoints("a"),

		@Test@Test	public void pauseForSameSourceMultipleTimes() {publicvoidpauseForSameSourceMultipleTimes(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




575


576



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


575


576


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")));callSite("b").calls("bar",flow("1","2")));




Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




577


578


579



				
		helper.method("bar",
				startPoints("c"),






Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.

 

Do not check interest of callers if paused edge already available.

Johannes Lerch
committed
Jan 16, 2015


577


578


579


				
		helper.method("bar",
				startPoints("c"),

						helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




580


581



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


580


581


				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));

				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),normalStmt("c",flow("2",readField("x"),"3"),flow("2","2")).succ("d"),				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));normalStmt("d",flow("2",readField("x"),"4")).succ("e"));




Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




582


583


584


585


586


587


588


589



		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),






Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.

 

Do not check interest of callers if paused edge already available.

Johannes Lerch
committed
Jan 16, 2015


582


583


584


585


586


587


588


589


		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseForSameSourceMultipleTimesTransitively() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void pauseForSameSourceMultipleTimesTransitively() {publicvoidpauseForSameSourceMultipleTimesTransitively(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




590


591


592



				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


590


591


592


				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),
				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),
				callSite("f").calls("xyz", flow("1", "2")));

				normalStmt("a", flow("0", prependField("f"), "1")).succ("b"),normalStmt("a",flow("0",prependField("f"),"1")).succ("b"),				callSite("b").calls("xyz", flow("1", "2")).retSite("f", flow("1", "1")),callSite("b").calls("xyz",flow("1","2")).retSite("f",flow("1","1")),				callSite("f").calls("xyz", flow("1", "2")));callSite("f").calls("xyz",flow("1","2")));




Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




593


594


595


596


597


598


599



		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),






Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.

 

Do not check interest of callers if paused edge already available.

Johannes Lerch
committed
Jan 16, 2015


593


594


595


596


597


598


599


		
		helper.method("xyz",
				startPoints("g"),
				callSite("g").calls("bar", flow("2", "2")));
				
		helper.method("bar",
				startPoints("c"),

				helper.method("xyz",helper.method("xyz",				startPoints("g"),startPoints("g"),				callSite("g").calls("bar", flow("2", "2")));callSite("g").calls("bar",flow("2","2")));						helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




600


601



				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


600


601


				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),
				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));

				normalStmt("c", flow("2", readField("x"), "3"), flow("2", "2")).succ("d"),normalStmt("c",flow("2",readField("x"),"3"),flow("2","2")).succ("d"),				normalStmt("d", flow("2", readField("x"), "4")).succ("e"));normalStmt("d",flow("2",readField("x"),"4")).succ("e"));




Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015




602


603


604



		
		helper.runSolver(false, "a");
	}






Do not check interest of callers if paused edge already available.

 


Johannes Lerch
committed
Jan 16, 2015



Do not check interest of callers if paused edge already available.

 

Do not check interest of callers if paused edge already available.

Johannes Lerch
committed
Jan 16, 2015


602


603


604


		
		helper.runSolver(false, "a");
	}

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




605


606


607


608


609



	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


605


606


607


608


609


	
	@Test
	public void multipleExclusions() {
		helper.method("foo",
				startPoints("a"),

		@Test@Test	public void multipleExclusions() {publicvoidmultipleExclusions(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




610


611



				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


610


611


				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")));

				normalStmt("a", flow("0", overwriteField("h"), "1")).succ("b"),normalStmt("a",flow("0",overwriteField("h"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")));callSite("b").calls("bar",flow("1","2")));




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




612


613


614



				
		helper.method("bar",
				startPoints("c"),






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


612


613


614


				
		helper.method("bar",
				startPoints("c"),

						helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




615


616



				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


615


616


				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),
				callSite("d").calls("xyz", flow("3", "4")));

				normalStmt("c", flow("2", overwriteField("f"), "3")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"3")).succ("d"),				callSite("d").calls("xyz", flow("3", "4")));callSite("d").calls("xyz",flow("3","4")));




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




617


618


619



		
		helper.method("xyz", 
				startPoints("e"),






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


617


618


619


		
		helper.method("xyz", 
				startPoints("e"),

				helper.method("xyz", helper.method("xyz",				startPoints("e"),startPoints("e"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




620


621



				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


620


621


				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),
				normalStmt("f", kill("5")).succ("g"));

				normalStmt("e", flow("4", overwriteField("g"), "5")).succ("f"),normalStmt("e",flow("4",overwriteField("g"),"5")).succ("f"),				normalStmt("f", kill("5")).succ("g"));normalStmt("f",kill("5")).succ("g"));




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




622


623


624



		
		helper.runSolver(false, "a");
	}






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


622


623


624


		
		helper.runSolver(false, "a");
	}

				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




625


626


627


628


629



	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


625


626


627


628


629


	
	@Test
	public void unbalancedReturnWithFieldRead() {
		helper.method("foo",
				startPoints("a"),

		@Test@Test	public void unbalancedReturnWithFieldRead() {publicvoidunbalancedReturnWithFieldRead(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




630



				normalStmt("a", flow("0", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


630


				normalStmt("a", flow("0", "1")).succ("b"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




631


632


633


634



				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


631


632


633


634


				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));
		
		helper.method("xyz",
				startPoints("n/a"),

				exitStmt("b").returns(over("cs"), to("c"), flow("1", "2")));exitStmt("b").returns(over("cs"),to("c"),flow("1","2")));				helper.method("xyz",helper.method("xyz",				startPoints("n/a"),startPoints("n/a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




635



				normalStmt("cs").succ("c"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


635


				normalStmt("cs").succ("c"),

				normalStmt("cs").succ("c"),normalStmt("cs").succ("c"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




636


637


638


639



				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


636


637


638


639


				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));
		
		helper.method("bar",
				startPoints("unused"),

				exitStmt("c").returns(over("cs2"), to("d"), flow("2", "2")));exitStmt("c").returns(over("cs2"),to("d"),flow("2","2")));				helper.method("bar",helper.method("bar",				startPoints("unused"),startPoints("unused"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




640


641


642



				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


640


641


642


				normalStmt("cs2").succ("d"),
				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("e", kill("3")).succ("f"));

				normalStmt("cs2").succ("d"),normalStmt("cs2").succ("d"),				normalStmt("d", flow("2", readField("f"), "3")).succ("e"),normalStmt("d",flow("2",readField("f"),"3")).succ("e"),				normalStmt("e", kill("3")).succ("f"));normalStmt("e",kill("3")).succ("f"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




643


644


645


646


647


648


649


650



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


643


644


645


646


647


648


649


650


		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnAbstraction() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void unbalancedReturnAbstraction() {publicvoidunbalancedReturnAbstraction(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




651


652


653



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


651


652


653


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),normalStmt("b",flow("1",prependField("f"),"2")).succ("c"),				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));exitStmt("c").returns(over("cs"),to("rs"),flow("2","2")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




654


655


656



		
		helper.method("bar",
				startPoints("unused"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


654


655


656


		
		helper.method("bar",
				startPoints("unused"),

				helper.method("bar",helper.method("bar",				startPoints("unused"),startPoints("unused"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




657


658



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


657


658


				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"));

				normalStmt("cs").succ("rs"),normalStmt("cs").succ("rs"),				normalStmt("rs", flow("2", "3")).succ("d"));normalStmt("rs",flow("2","3")).succ("d"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




659


660


661


662


663


664


665


666



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


659


660


661


662


663


664


665


666


		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnReadAbstractedField() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void unbalancedReturnReadAbstractedField() {publicvoidunbalancedReturnReadAbstractedField(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




667


668


669



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


667


668


669


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),normalStmt("b",flow("1",prependField("f"),"2")).succ("c"),				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));exitStmt("c").returns(over("cs"),to("rs"),flow("2","2")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




670


671


672



		
		helper.method("bar",
				startPoints("unused"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


670


671


672


		
		helper.method("bar",
				startPoints("unused"),

				helper.method("bar",helper.method("bar",				startPoints("unused"),startPoints("unused"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




673


674


675


676



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


673


674


675


676


				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),
				normalStmt("e", kill("4")).succ("f"));

				normalStmt("cs").succ("rs"),normalStmt("cs").succ("rs"),				normalStmt("rs", flow("2", "3")).succ("d"),normalStmt("rs",flow("2","3")).succ("d"),				normalStmt("d", flow("3", readField("f"), "4")).succ("e"),normalStmt("d",flow("3",readField("f"),"4")).succ("e"),				normalStmt("e", kill("4")).succ("f"));normalStmt("e",kill("4")).succ("f"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




677


678


679


680


681


682


683


684



		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


677


678


679


680


681


682


683


684


		
		helper.runSolver(true, "a");
	}

	@Test
	public void unbalancedReturnReadUnwrittenAbstractedField() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}	@Test@Test	public void unbalancedReturnReadUnwrittenAbstractedField() {publicvoidunbalancedReturnReadUnwrittenAbstractedField(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




685


686


687



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


685


686


687


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),normalStmt("b",flow("1",prependField("f"),"2")).succ("c"),				exitStmt("c").returns(over("cs"), to("rs"), flow("2", "2")));exitStmt("c").returns(over("cs"),to("rs"),flow("2","2")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




688


689


690



		
		helper.method("bar",
				startPoints("unused"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


688


689


690


		
		helper.method("bar",
				startPoints("unused"),

				helper.method("bar",helper.method("bar",				startPoints("unused"),startPoints("unused"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




691


692


693



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),






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


692


693


				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "3")).succ("d"),
				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),

				normalStmt("cs").succ("rs"),normalStmt("cs").succ("rs"),				normalStmt("rs", flow("2", "3")).succ("d"),normalStmt("rs",flow("2","3")).succ("d"),				normalStmt("d", flow("3", readField("h"), "4")).succ("e"),normalStmt("d",flow("3",readField("h"),"4")).succ("e"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




694


695


696


697


698


699


700


701


702



				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


694


695


696


697


698


699


700


701


702


				normalStmt("e").succ("f"));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnTransitiveAbstraction() {
		helper.method("foo",
				startPoints("a"),

				normalStmt("e").succ("f"));normalStmt("e").succ("f"));				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void unbalancedReturnTransitiveAbstraction() {publicvoidunbalancedReturnTransitiveAbstraction(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




703


704


705



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


703


704


705


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),
				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("b", flow("1", prependField("f"), "2")).succ("c"),normalStmt("b",flow("1",prependField("f"),"2")).succ("c"),				exitStmt("c").returns(over("cs1"), to("rs1"), flow("2", "2")));exitStmt("c").returns(over("cs1"),to("rs1"),flow("2","2")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




706


707


708



		
		helper.method("bar",
				startPoints("unused1"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


706


707


708


		
		helper.method("bar",
				startPoints("unused1"),

				helper.method("bar",helper.method("bar",				startPoints("unused1"),startPoints("unused1"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




709


710


711



				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


709


710


711


				normalStmt("cs1").succ("rs1"),
				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),
				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));

				normalStmt("cs1").succ("rs1"),normalStmt("cs1").succ("rs1"),				normalStmt("rs1", flow("2", prependField("g"), "3")).succ("d"),normalStmt("rs1",flow("2",prependField("g"),"3")).succ("d"),				exitStmt("d").returns(over("cs2"), to("rs2"), flow("3", "4")));exitStmt("d").returns(over("cs2"),to("rs2"),flow("3","4")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




712


713


714



		
		helper.method("xyz",
				startPoints("unused2"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


712


713


714


		
		helper.method("xyz",
				startPoints("unused2"),

				helper.method("xyz",helper.method("xyz",				startPoints("unused2"),startPoints("unused2"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




715


716


717


718


719



				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


715


716


717


718


719


				normalStmt("cs2").succ("rs2"),
				normalStmt("rs2", flow("4", "5")).succ("e"),
				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),
				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),
				normalStmt("g", kill("7")).succ("h"));

				normalStmt("cs2").succ("rs2"),normalStmt("cs2").succ("rs2"),				normalStmt("rs2", flow("4", "5")).succ("e"),normalStmt("rs2",flow("4","5")).succ("e"),				normalStmt("e", flow("5", readField("g"), "6")).succ("f"),normalStmt("e",flow("5",readField("g"),"6")).succ("f"),				normalStmt("f", flow("6", readField("f"), "7")).succ("g"),normalStmt("f",flow("6",readField("f"),"7")).succ("g"),				normalStmt("g", kill("7")).succ("h"));normalStmt("g",kill("7")).succ("h"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




720


721


722


723


724


725


726


727



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


720


721


722


723


724


725


726


727


		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void unbalancedReturnPauseAndResume() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void unbalancedReturnPauseAndResume() {publicvoidunbalancedReturnPauseAndResume(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




728


729



				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


728


729


				normalStmt("a", flow("0", "1")).succ("b"),
				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				exitStmt("b").returns(over("cs"), to("rs"), flow("1", prependField("g"), "2")));exitStmt("b").returns(over("cs"),to("rs"),flow("1",prependField("g"),"2")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




730


731


732



		
		helper.method("bar",
				startPoints("unused"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


730


731


732


		
		helper.method("bar",
				startPoints("unused"),

				helper.method("bar",helper.method("bar",				startPoints("unused"),startPoints("unused"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




733


734


735


736


737



				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


733


734


735


736


737


				normalStmt("cs").succ("rs"),
				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),
				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),
				normalStmt("d2", kill("3")).succ("e"));

				normalStmt("cs").succ("rs"),normalStmt("cs").succ("rs"),				normalStmt("rs", flow("2", "2")).succ("c").succ("d1"),normalStmt("rs",flow("2","2")).succ("c").succ("d1"),				exitStmt("c").returns(over("cs"), to("rs"), flow("2", prependField("f"), "2")),exitStmt("c").returns(over("cs"),to("rs"),flow("2",prependField("f"),"2")),				normalStmt("d1", flow("2", readField("f"), "3")).succ("d2"),normalStmt("d1",flow("2",readField("f"),"3")).succ("d2"),				normalStmt("d2", kill("3")).succ("e"));normalStmt("d2",kill("3")).succ("e"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




738


739


740


741


742


743


744


745



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


738


739


740


741


742


743


744


745


		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void abstractedReturnUseCallerInterest() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void abstractedReturnUseCallerInterest() {publicvoidabstractedReturnUseCallerInterest(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




746



				normalStmt("a", flow("0", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


746


				normalStmt("a", flow("0", "1")).succ("b"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




747



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


747


				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),

				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("c",kill("1")),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




748


749



				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


748


749


				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));

				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),normalStmt("c",flow("2",readField("f"),"3")).succ("d"),				normalStmt("d", kill("3")).succ("e"));normalStmt("d",kill("3")).succ("e"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




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


760


761



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


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


760


761


		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void abstractedReturnDeltaBlockingCallerInterest() {
		helper.method("foo",
				startPoints("a"),

				helper.method("bar",helper.method("bar",				startPoints("f"),startPoints("f"),				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));exitStmt("f").returns(over("b"),to("c"),flow("2","2")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void abstractedReturnDeltaBlockingCallerInterest() {publicvoidabstractedReturnDeltaBlockingCallerInterest(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




762


763


764



				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


762


763


764


				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				callSite("b").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),callSite("b").calls("bar",flow("1",prependField("h"),"2")).retSite("c",kill("1")),				normalStmt("c", flow("2", readField("f"), "3")).succ("d"),normalStmt("c",flow("2",readField("f"),"3")).succ("d"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




765


766


767


768


769


770


771


772


773


774


775


776


777



				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


765


766


767


768


769


770


771


772


773


774


775


776


777


				normalStmt("d").succ("e"));
		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void abstractedReturnResolveThroughDelta() {
		helper.method("foo",
				startPoints("a"),

				normalStmt("d").succ("e"));normalStmt("d").succ("e"));				helper.method("bar",helper.method("bar",				startPoints("f"),startPoints("f"),				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));exitStmt("f").returns(over("b"),to("c"),flow("2","2")));				helper.runSolver(false, "a");		helper.runSolver(false,"a");	}}		@Test@Test	public void abstractedReturnResolveThroughDelta() {publicvoidabstractedReturnResolveThroughDelta(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




778


779



				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


778


779


				normalStmt("a", flow("0", "1")).succ("b1"),
				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));

				normalStmt("a", flow("0", "1")).succ("b1"),normalStmt("a",flow("0","1")).succ("b1"),				callSite("b1").calls("xyz", flow("1", prependField("f"), "1")));callSite("b1").calls("xyz",flow("1",prependField("f"),"1")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




780


781


782



				
		helper.method("xyz",
				startPoints("b2"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


780


781


782


				
		helper.method("xyz",
				startPoints("b2"),

						helper.method("xyz",helper.method("xyz",				startPoints("b2"),startPoints("b2"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




783


784


785



				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


783


784


785


				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),
				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),
				normalStmt("d", kill("3")).succ("e"));

				callSite("b2").calls("bar", flow("1", prependField("h"), "2")).retSite("c", kill("1")),callSite("b2").calls("bar",flow("1",prependField("h"),"2")).retSite("c",kill("1")),				normalStmt("c", flow("2", readField("h"), "3")).succ("d"),normalStmt("c",flow("2",readField("h"),"3")).succ("d"),				normalStmt("d", kill("3")).succ("e"));normalStmt("d",kill("3")).succ("e"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




786


787


788


789


790


791


792


793


794


795


796


797



		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


786


787


788


789


790


791


792


793


794


795


796


797


		
		helper.method("bar",
				startPoints("f"),
				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "a");		
	}
	
	@Test
	public void unbalancedAbstractedReturnRecursive() {
		helper.method("bar", 
				startPoints("a"),

				helper.method("bar",helper.method("bar",				startPoints("f"),startPoints("f"),				exitStmt("f").returns(over("b2"), to("c"), flow("2", "2")));exitStmt("f").returns(over("b2"),to("c"),flow("2","2")));				helper.runSolver(false, "a");		helper.runSolver(false,"a");	}}		@Test@Test	public void unbalancedAbstractedReturnRecursive() {publicvoidunbalancedAbstractedReturnRecursive(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




798


799


800


801



				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


798


799


800


801


				normalStmt("a", flow("0", "1")).succ("b"),
				normalStmt("cs1").succ("b"),
				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))
							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				normalStmt("cs1").succ("b"),normalStmt("cs1").succ("b"),				exitStmt("b").returns(over("cs1"), to("b"), flow(2, "1", "1"))exitStmt("b").returns(over("cs1"),to("b"),flow(2,"1","1"))							 .returns(over("cs2"), to("c"), flow(2, "1", "1")));.returns(over("cs2"),to("c"),flow(2,"1","1")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




802


803


804



		
		helper.method("foo",
				startPoints("unused"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


802


803


804


		
		helper.method("foo",
				startPoints("unused"),

				helper.method("foo",helper.method("foo",				startPoints("unused"),startPoints("unused"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




805


806


807



				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


805


806


807


				normalStmt("cs2").succ("c"),
				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),
				normalStmt("d", kill("2")).succ("e"));

				normalStmt("cs2").succ("c"),normalStmt("cs2").succ("c"),				normalStmt("c", flow("1", readField("f"), "2")).succ("d"),normalStmt("c",flow("1",readField("f"),"2")).succ("d"),				normalStmt("d", kill("2")).succ("e"));normalStmt("d",kill("2")).succ("e"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




808


809


810


811


812


813


814


815



		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


808


809


810


811


812


813


814


815


		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas() {
		helper.method("foo",
				startPoints("a"),

				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void includeResolversInCallDeltas() {publicvoidincludeResolversInCallDeltas(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




816



				normalStmt("a", flow("0", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


816


				normalStmt("a", flow("0", "1")).succ("b"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




817


818


819


820


821



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


817


818


819


820


821


				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")));
		
		helper.method("bar",
				startPoints("c"),

				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("e",kill("1")),				callSite("e").calls("xyz", flow("3", "3")));callSite("e").calls("xyz",flow("3","3")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




822


823



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


822


823


				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));

				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"2")).succ("d"),				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));exitStmt("d").returns(over("b"),to("e"),flow("2","3")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




824


825


826



		
		helper.method("xyz", 
				startPoints("f"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


824


825


826


		
		helper.method("xyz", 
				startPoints("f"),

				helper.method("xyz", helper.method("xyz",				startPoints("f"),startPoints("f"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




827



				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


827


				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),

				normalStmt("f", flow("3", readField("f"), "4")).succ("g"),normalStmt("f",flow("3",readField("f"),"4")).succ("g"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




828


829


830


831


832


833


834


835


836



				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


828


829


830


831


832


833


834


835


836


				normalStmt("g").succ("h"));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas2() {
		helper.method("foo",
				startPoints("a"),

				normalStmt("g").succ("h"));normalStmt("g").succ("h"));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void includeResolversInCallDeltas2() {publicvoidincludeResolversInCallDeltas2(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




837



				normalStmt("a", flow("0", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


837


				normalStmt("a", flow("0", "1")).succ("b"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




838


839



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


838


839


				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),

				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("e",kill("1")),				callSite("e").calls("xyz", flow("3", "3")).retSite("g", kill("3")),callSite("e").calls("xyz",flow("3","3")).retSite("g",kill("3")),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




840



				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


840


				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),

				normalStmt("g", flow("3", readField("f"), "4")).succ("h"),normalStmt("g",flow("3",readField("f"),"4")).succ("h"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




841



				normalStmt("h").succ("i"));






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


841


				normalStmt("h").succ("i"));

				normalStmt("h").succ("i"));normalStmt("h").succ("i"));




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




842










rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


842









abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




843


844



		helper.method("bar",
				startPoints("c"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


843


844


		helper.method("bar",
				startPoints("c"),

		helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




845


846



				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


845


846


				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));

				normalStmt("c", flow("2", overwriteField("f"), "2")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"2")).succ("d"),				exitStmt("d").returns(over("b"), to("e"), flow("2", "3")));exitStmt("d").returns(over("b"),to("e"),flow("2","3")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




847


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



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


847


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


		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas3() {
		helper.method("main",
				startPoints("m_a"),

				helper.method("xyz", helper.method("xyz",				startPoints("f"),startPoints("f"),				exitStmt("f").returns(over("e"), to("g"), flow("3", "3")));exitStmt("f").returns(over("e"),to("g"),flow("3","3")));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void includeResolversInCallDeltas3() {publicvoidincludeResolversInCallDeltas3(){		helper.method("main",helper.method("main",				startPoints("m_a"),startPoints("m_a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




859


860


861


862



				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


859


860


861


862


				normalStmt("m_a", flow("0", "1")).succ("m_b"),
				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),
				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),
				normalStmt("m_d", kill("6")).succ("m_e"));

				normalStmt("m_a", flow("0", "1")).succ("m_b"),normalStmt("m_a",flow("0","1")).succ("m_b"),				callSite("m_b").calls("foo", flow("1", prependField("g"), "1")).retSite("m_c", kill("1")),callSite("m_b").calls("foo",flow("1",prependField("g"),"1")).retSite("m_c",kill("1")),				callSite("m_c").calls("foo", flow("5", prependField("f"), "1")).retSite("m_d", kill("5")),callSite("m_c").calls("foo",flow("5",prependField("f"),"1")).retSite("m_d",kill("5")),				normalStmt("m_d", kill("6")).succ("m_e"));normalStmt("m_d",kill("6")).succ("m_e"));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




863


864


865



		
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


863


864


865


		
		helper.method("foo",
				startPoints("a"),

				helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




866



				normalStmt("a", flow("1", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


866


				normalStmt("a", flow("1", "1")).succ("b"),

				normalStmt("a", flow("1", "1")).succ("b"),normalStmt("a",flow("1","1")).succ("b"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




867


868



				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


867


868


				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),
				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),

				callSite("b").calls("bar", flow("1", "2")).retSite("e", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("e",kill("1")),				callSite("e").calls("xyz", flow(2, "3", "3")).retSite("g", kill(2, "3")),callSite("e").calls("xyz",flow(2,"3","3")).retSite("g",kill(2,"3")),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




869



				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


869


				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),

				normalStmt("g", flow(2, "3", readField("f"), "4"), flow(2, "3", readField("g"), "5")).succ("h"),normalStmt("g",flow(2,"3",readField("f"),"4"),flow(2,"3",readField("g"),"5")).succ("h"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




870


871


872


873



				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


870


871


872


873


				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));
		
		helper.method("bar",
				startPoints("c"),

				exitStmt("h").returns(over("m_c"), to("m_d"), flow("4", "6")).returns(over("m_b"), to("m_c"), flow("5", "5")));exitStmt("h").returns(over("m_c"),to("m_d"),flow("4","6")).returns(over("m_b"),to("m_c"),flow("5","5")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




874


875



				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


874


875


				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));

				normalStmt("c", flow("2", overwriteField("f"), "2"), flow("2", overwriteField("g"), "2")).succ("d"),normalStmt("c",flow("2",overwriteField("f"),"2"),flow("2",overwriteField("g"),"2")).succ("d"),				exitStmt("d").returns(over("b"), to("e"), flow(2, "2", "3")));exitStmt("d").returns(over("b"),to("e"),flow(2,"2","3")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




876


877


878


879


880


881


882


883



		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
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


876


877


878


879


880


881


882


883


		
		helper.method("xyz", 
				startPoints("f"),
				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));
				
		helper.runSolver(false, "m_a");
	}
	

				helper.method("xyz", helper.method("xyz",				startPoints("f"),startPoints("f"),				exitStmt("f").returns(over("e"), to("g"), flow(2, "3", "3")));exitStmt("f").returns(over("e"),to("g"),flow(2,"3","3")));						helper.runSolver(false, "m_a");helper.runSolver(false,"m_a");	}}	




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968



	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
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


884


885


886


887


888


889


890


891


892


893


894


895


896


897


898


899


900


901


902


903


904


905


906


907


908


909


910


911


912


913


914


915


916


917


918


919


920


921


922


923


924


925


926


927


928


929


930


931


932


933


934


935


936


937


938


939


940


941


942


943


944


945


946


947


948


949


950


951


952


953


954


955


956


957


958


959


960


961


962


963


964


965


966


967


968


	@Test
	public void includeResolversInCallDeltas4() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a", flow("0", "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),
				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),
				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),
				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),
				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),
				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));
		
		helper.method("xyz", 
				startPoints("f1"),
				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),
				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas5() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l"), kill("6")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void includeResolversInCallDeltas6() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),
				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),
				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),
				normalStmt("l").succ("m"));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),
				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),
				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),
				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),
				exitStmt("k").returns(over("b"), to("l")));
		
		helper.method("xyz",
				startPoints("e"),
				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),
				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))
							 .returns(over("a2"), to("b"), flow("4", "1")));
		
		helper.method("baz",
				startPoints("f"),
				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),
				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));
		
		helper.runSolver(false, "a");
	}
	

	@Test@Test	public void includeResolversInCallDeltas4() {publicvoidincludeResolversInCallDeltas4(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")).retSite("e1", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("e1",kill("1")),				normalStmt("e1", flow("3", prependField("g"), "3")).succ("e2"),normalStmt("e1",flow("3",prependField("g"),"3")).succ("e2"),				callSite("e2").calls("xyz", flow("3", "3")).retSite("g", kill("3")),callSite("e2").calls("xyz",flow("3","3")).retSite("g",kill("3")),				normalStmt("g", flow("3", readField("h"), "4")).succ("h"),normalStmt("g",flow("3",readField("h"),"4")).succ("h"),				normalStmt("h", flow("4", readField("g"), "5")).succ("i"),normalStmt("h",flow("4",readField("g"),"5")).succ("i"),				normalStmt("i", flow("5", readField("f"), "6")).succ("j"));normalStmt("i",flow("5",readField("f"),"6")).succ("j"));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),normalStmt("c",flow("2",prependField("f"),"2")).succ("d"),				exitStmt("d").returns(over("b"), to("e1"), flow("2", "3")));exitStmt("d").returns(over("b"),to("e1"),flow("2","3")));				helper.method("xyz", helper.method("xyz",				startPoints("f1"),startPoints("f1"),				normalStmt("f1", flow("3", prependField("h"), "3")).succ("f2"),normalStmt("f1",flow("3",prependField("h"),"3")).succ("f2"),				exitStmt("f2").returns(over("e2"), to("g"), flow("3", "3")));exitStmt("f2").returns(over("e2"),to("g"),flow("3","3")));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void includeResolversInCallDeltas5() {publicvoidincludeResolversInCallDeltas5(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a", flow("0", prependField("z"), "1")).succ("b"),normalStmt("a",flow("0",prependField("z"),"1")).succ("b"),				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("l",kill("1")),				normalStmt("l").succ("m"));normalStmt("l").succ("m"));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c", flow("2", prependField("f"), "2")).succ("d"),normalStmt("c",flow("2",prependField("f"),"2")).succ("d"),				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),callSite("d").calls("xyz",flow("2","3")).retSite("i",kill("2")),				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),normalStmt("i",flow("4",readField("g"),"5")).succ("j"),				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),normalStmt("j",flow("5",readField("f"),"6")).succ("k"),				exitStmt("k").returns(over("b"), to("l"), kill("6")));exitStmt("k").returns(over("b"),to("l"),kill("6")));				helper.method("xyz",helper.method("xyz",				startPoints("e"),startPoints("e"),				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),callSite("e").calls("baz",flow("3","3")).retSite("h",kill("3")),				exitStmt("h").returns(over("d"), to("i"), flow("4", "4")));exitStmt("h").returns(over("d"),to("i"),flow("4","4")));				helper.method("baz",helper.method("baz",				startPoints("f"),startPoints("f"),				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),normalStmt("f",flow("3",prependField("g"),"4")).succ("g"),				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));exitStmt("g").returns(over("e"),to("h"),flow("4","4")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void includeResolversInCallDeltas6() {publicvoidincludeResolversInCallDeltas6(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a", flow("0", prependField("f"), "1")).succ("a2"),normalStmt("a",flow("0",prependField("f"),"1")).succ("a2"),				callSite("a2").calls("xyz", flow("1", "3")).retSite("b", kill("1")),callSite("a2").calls("xyz",flow("1","3")).retSite("b",kill("1")),				callSite("b").calls("bar", flow("1", "2")).retSite("l", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("l",kill("1")),				normalStmt("l").succ("m"));normalStmt("l").succ("m"));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c", flow("2", prependField("z"), "2")).succ("d"),normalStmt("c",flow("2",prependField("z"),"2")).succ("d"),				callSite("d").calls("xyz", flow("2", "3")).retSite("i", kill("2")),callSite("d").calls("xyz",flow("2","3")).retSite("i",kill("2")),				normalStmt("i", flow("4", readField("g"), "5")).succ("j"),normalStmt("i",flow("4",readField("g"),"5")).succ("j"),				normalStmt("j", flow("5", readField("f"), "6")).succ("k"),normalStmt("j",flow("5",readField("f"),"6")).succ("k"),				exitStmt("k").returns(over("b"), to("l")));exitStmt("k").returns(over("b"),to("l")));				helper.method("xyz",helper.method("xyz",				startPoints("e"),startPoints("e"),				callSite("e").calls("baz", flow("3", "3")).retSite("h", kill("3")),callSite("e").calls("baz",flow("3","3")).retSite("h",kill("3")),				exitStmt("h").returns(over("d"), to("i"), flow("4", "4"))exitStmt("h").returns(over("d"),to("i"),flow("4","4"))							 .returns(over("a2"), to("b"), flow("4", "1")));.returns(over("a2"),to("b"),flow("4","1")));				helper.method("baz",helper.method("baz",				startPoints("f"),startPoints("f"),				normalStmt("f", flow("3", prependField("g"), "4")).succ("g"),normalStmt("f",flow("3",prependField("g"),"4")).succ("g"),				exitStmt("g").returns(over("e"), to("h"), flow("4", "4")));exitStmt("g").returns(over("e"),to("h"),flow("4","4")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




969


970


971


972



	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


969


970


971


972


	@Test
	public void recursiveCallReturnCase() {
		helper.method("xyz",
				startPoints("x"),

	@Test@Test	public void recursiveCallReturnCase() {publicvoidrecursiveCallReturnCase(){		helper.method("xyz",helper.method("xyz",				startPoints("x"),startPoints("x"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




973


974



				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


973


974


				normalStmt("x", flow("0", "1")).succ("y"),
				callSite("y").calls("foo", flow("1", prependField("g"), "1")));

				normalStmt("x", flow("0", "1")).succ("y"),normalStmt("x",flow("0","1")).succ("y"),				callSite("y").calls("foo", flow("1", prependField("g"), "1")));callSite("y").calls("foo",flow("1",prependField("g"),"1")));




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




975


976


977



		
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


975


976


977


		
		helper.method("foo",
				startPoints("a"),

				helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




978



				normalStmt("a", flow("1", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


978


				normalStmt("a", flow("1", "1")).succ("b"),

				normalStmt("a", flow("1", "1")).succ("b"),normalStmt("a",flow("1","1")).succ("b"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




979


980


981


982


983



				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


979


980


981


982


983


				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),
				callSite("c").calls("bar", flow("2", "2")));
		
		helper.method("bar", 
				startPoints("d"),

				callSite("b").calls("bar", flow("1", "2")).retSite("c", kill("1")),callSite("b").calls("bar",flow("1","2")).retSite("c",kill("1")),				callSite("c").calls("bar", flow("2", "2")));callSite("c").calls("bar",flow("2","2")));				helper.method("bar", helper.method("bar",				startPoints("d"),startPoints("d"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




984


985


986



				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


984


985


986


				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),
				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),
				normalStmt("d2", flow("2", "2")).succ("f"),

				normalStmt("d", flow("2", "2")).succ("d1").succ("d2"),normalStmt("d",flow("2","2")).succ("d1").succ("d2"),				normalStmt("d1", flow("2", readField("f"), "3")).succ("e"),normalStmt("d1",flow("2",readField("f"),"3")).succ("e"),				normalStmt("d2", flow("2", "2")).succ("f"),normalStmt("d2",flow("2","2")).succ("f"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




987


988


989


990


991


992


993


994


995



				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


987


988


989


990


991


992


993


994


995


				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));
		
		helper.runSolver(false, "x");
	}
	
	@Test
	public void recursivelyUseIncompatibleReturnResolver() {
		helper.method("foo",
				startPoints("a"),

				exitStmt("f").returns(over("b"), to("c"), flow("2", "2")));exitStmt("f").returns(over("b"),to("c"),flow("2","2")));				helper.runSolver(false, "x");helper.runSolver(false,"x");	}}		@Test@Test	public void recursivelyUseIncompatibleReturnResolver() {publicvoidrecursivelyUseIncompatibleReturnResolver(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




996



				normalStmt("a", flow("0", "1")).succ("b"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


996


				normalStmt("a", flow("0", "1")).succ("b"),

				normalStmt("a", flow("0", "1")).succ("b"),normalStmt("a",flow("0","1")).succ("b"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




997



				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


997


				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),

				callSite("b").calls("bar", flow("1", "1")).retSite("f", kill("1")),callSite("b").calls("bar",flow("1","1")).retSite("f",kill("1")),




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




998



				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


998


				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),

				normalStmt("f", flow("2", readField("f"), "3")).succ("g"),normalStmt("f",flow("2",readField("f"),"3")).succ("g"),




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




999


1000



				normalStmt("h").succ("i"));
		






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


999


1000


				normalStmt("h").succ("i"));
		

				normalStmt("h").succ("i"));normalStmt("h").succ("i"));		
For faster browsing, not all history is shown.

View entire blame



View entire blame





Prev


1


2


Next





Prev

1

2

Next







