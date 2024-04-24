



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

f08a8e492c7c5ca97e16c48dab2d199af5190da0

















f08a8e492c7c5ca97e16c48dab2d199af5190da0


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveSolverTest.java



17.1 KB









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









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






16




17




import org.junit.Ignore;
import org.junit.Rule;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






18




import org.junit.Test;









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






19




import org.junit.rules.TestWatcher;









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





import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






29




		System.err.println("-----");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






30




31




32




		helper = new TestHelper();
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {
			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void mergeWithExistingPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("b", flow("1", "1.f")).succ("c", flow("1", "2")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void dontMergeWithExistingNonPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				normalStmt("b").succ("b", flow("1.f", "1"), kill("1")).succ("c", flow("1.f", "2"), kill("1")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






63




64




65




66




67




	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






68




69




70




				normalStmt("b").succ("c", flow("1", writeField("f"), "2.f")),
				normalStmt("c").succ("d", flow("2.f", "2.f")),
				normalStmt("d").succ("e", flow("2.f", readField("f"), "4")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






71




72




73




74




75




76




77




78




79




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






80




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









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






90




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






91




92




93




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






94




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






114




				normalStmt("b").succ("c", flow("1", readField("field"), "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




				callSite("c").calls("foo", flow("2.field", "3.field")));
		









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






117




118




119




		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("notfield"), "5"), flow("3", "3")),









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




				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






129




130




131




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("rs", kill("2.field")),
				callSite("rs").calls("foo", flow("5", "3.notfield")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






132




133




		
		helper.method("foo",startPoints("d"),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






134




135




136




				normalStmt("d").succ("e", flow("3", "3"), flow("3", readField("notfield"), "6")),
				normalStmt("e").succ("f", flow("3","4"), kill("6")),
				exitStmt("f").returns(over("c"), to("rs"), flow("4.field", "5")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






137




138




139




		
		helper.runSolver(false, "a", "g");
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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





	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






150




				callSite("c").calls("xyz", flow("2", "3")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




		
		helper.method("xyz", 
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"	, kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






161




	public void prefixFactOfSummaryIgnored() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






162




163




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






164




				normalStmt("a").succ("b", flow("0","1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






165




				callSite("b").calls("bar", flow("1", "2.f")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






166




167




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),
				normalStmt("f").succ("g"));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






168




		









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






169




		helper.method("bar",









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






170




				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






171




172




				normalStmt("c").succ("d", flow("2", readField("f"), "3")),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






173




174




175




176




		
		helper.runSolver(false, "a");
	}
	









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






177




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






178




	public void doNotPauseZeroSources() {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






179




180




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				normalStmt("b").succ("c", kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void loopAndMerge() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", readField("f"), "2"), flow("1", "1"), flow("2", "2")),
				normalStmt("c").succ("d", flow("1", "1", "1.f"), flow("2", "2")),
				normalStmt("d").succ("e", flow("1", "1"), flow("2", "2")).succ("b", flow("1", "1"), flow("2", "2")),
				normalStmt("e").succ("f", kill("1"), kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void doNotMergePrefixFacts() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "1")).succ("b2", flow("0", "1")),
				normalStmt("b1").succ("c", flow("1", "1")),
				normalStmt("b2").succ("c", flow("1", "1.f")),
				normalStmt("c").succ("d", kill("1"), kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






219




220




221




		
		helper.method("bar",
				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






222




223




				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






224




225




226




227




		
		helper.runSolver(false, "a");
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






228




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






229




	public void pauseOnOverwrittenFieldOfInterest2() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






230




231




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






232




233




234




235




236




237




238




				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f.g")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






239




240




241




		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






242




	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






243




244




245




246




247




	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






248




				callSite("b").calls("bar", flow("1.f", "2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






249




250




251




252




253




254




255




256




257




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e", kill("2^f"))); 
		
		helper.runSolver(false, "a");
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






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




	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f")),
				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






286




				callSite("c").calls("xyz", flow("2", "3")));









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






287




288




289




290




291




292




293




294




295




		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f"), flow("3", "4")),
				callSite("e").calls("bar", flow("4", "2.g"), kill("3^f"))); 
		
		helper.runSolver(false, "a");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),
				callSite("e").calls("bar", flow("4", "2.g")).retSite("f", kill("4")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






307




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3.g"), kill("2.g" /* 2^f is back substituted to 2.g*/))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






318




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






319




320




321




322




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






323




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3"), kill("2^f"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






324




325




326




327




		
		helper.runSolver(false, "a");
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1")),
				callSite("a").calls("bar", flow("1", "1^f")).retSite("d", kill("1")));
		
		helper.method("bar",
				startPoints("b"),
				normalStmt("b").succ("c", flow("1", readField("f"), "2.f")),
				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






343




344




345




346




347




348




349




	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.g")),
				callSite("b").calls("bar", flow("1.g", "1.g")).retSite("e", kill("1.g")),
				normalStmt("e").succ("f", flow("1.g", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






350




				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






351




352




353




354




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("f"), "2"), flow("1", "1")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






355




				exitStmt("d").returns(over("b"), to("e"), flow("1.g", "1.g") /* ignore fact 2, not possible with this caller ctx*/).returns(over("f"), to("g"), kill("1"), kill("2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", "2", "2^f")),
				normalStmt("c").succ("d", kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1.f")),
				callSite("a").calls("bar", flow("1.f", "1.f")).retSite("f", kill("1.f")),
				callSite("f").calls("bar", flow("2", "1.g")));
				
		helper.method("bar",
				startPoints("b"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






381




				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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
	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






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




	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("bar",flow("1.x", "2.x")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3", readField("f"), "4")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.x")).calls("foo2", flow("1.x", "2.x")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.f")).calls("foo2", flow("1.x", "2.f")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f", kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", writeField("a"), "1^a")),
				callSite("d").calls("xyz", flow("1^a", "1^a")));
		
		helper.method("xyz",
				startPoints("e"),
				normalStmt("e").succ("f", flow("1", readField("f"), "2")),
				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),
				normalStmt("g").succ("h", flow("3", readField("a"), "4")));
		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






568




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

f08a8e492c7c5ca97e16c48dab2d199af5190da0

















f08a8e492c7c5ca97e16c48dab2d199af5190da0


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveSolverTest.java



17.1 KB









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









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






16




17




import org.junit.Ignore;
import org.junit.Rule;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






18




import org.junit.Test;









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






19




import org.junit.rules.TestWatcher;









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





import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






29




		System.err.println("-----");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






30




31




32




		helper = new TestHelper();
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {
			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void mergeWithExistingPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("b", flow("1", "1.f")).succ("c", flow("1", "2")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void dontMergeWithExistingNonPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				normalStmt("b").succ("b", flow("1.f", "1"), kill("1")).succ("c", flow("1.f", "2"), kill("1")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






63




64




65




66




67




	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






68




69




70




				normalStmt("b").succ("c", flow("1", writeField("f"), "2.f")),
				normalStmt("c").succ("d", flow("2.f", "2.f")),
				normalStmt("d").succ("e", flow("2.f", readField("f"), "4")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






71




72




73




74




75




76




77




78




79




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






80




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









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






90




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






91




92




93




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






94




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






114




				normalStmt("b").succ("c", flow("1", readField("field"), "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




				callSite("c").calls("foo", flow("2.field", "3.field")));
		









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






117




118




119




		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("notfield"), "5"), flow("3", "3")),









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




				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






129




130




131




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("rs", kill("2.field")),
				callSite("rs").calls("foo", flow("5", "3.notfield")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






132




133




		
		helper.method("foo",startPoints("d"),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






134




135




136




				normalStmt("d").succ("e", flow("3", "3"), flow("3", readField("notfield"), "6")),
				normalStmt("e").succ("f", flow("3","4"), kill("6")),
				exitStmt("f").returns(over("c"), to("rs"), flow("4.field", "5")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






137




138




139




		
		helper.runSolver(false, "a", "g");
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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





	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






150




				callSite("c").calls("xyz", flow("2", "3")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




		
		helper.method("xyz", 
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"	, kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






161




	public void prefixFactOfSummaryIgnored() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






162




163




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






164




				normalStmt("a").succ("b", flow("0","1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






165




				callSite("b").calls("bar", flow("1", "2.f")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






166




167




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),
				normalStmt("f").succ("g"));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






168




		









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






169




		helper.method("bar",









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






170




				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






171




172




				normalStmt("c").succ("d", flow("2", readField("f"), "3")),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






173




174




175




176




		
		helper.runSolver(false, "a");
	}
	









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






177




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






178




	public void doNotPauseZeroSources() {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






179




180




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				normalStmt("b").succ("c", kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void loopAndMerge() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", readField("f"), "2"), flow("1", "1"), flow("2", "2")),
				normalStmt("c").succ("d", flow("1", "1", "1.f"), flow("2", "2")),
				normalStmt("d").succ("e", flow("1", "1"), flow("2", "2")).succ("b", flow("1", "1"), flow("2", "2")),
				normalStmt("e").succ("f", kill("1"), kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void doNotMergePrefixFacts() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "1")).succ("b2", flow("0", "1")),
				normalStmt("b1").succ("c", flow("1", "1")),
				normalStmt("b2").succ("c", flow("1", "1.f")),
				normalStmt("c").succ("d", kill("1"), kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






219




220




221




		
		helper.method("bar",
				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






222




223




				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






224




225




226




227




		
		helper.runSolver(false, "a");
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






228




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






229




	public void pauseOnOverwrittenFieldOfInterest2() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






230




231




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






232




233




234




235




236




237




238




				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f.g")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






239




240




241




		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






242




	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






243




244




245




246




247




	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






248




				callSite("b").calls("bar", flow("1.f", "2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






249




250




251




252




253




254




255




256




257




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e", kill("2^f"))); 
		
		helper.runSolver(false, "a");
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






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




	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f")),
				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






286




				callSite("c").calls("xyz", flow("2", "3")));









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






287




288




289




290




291




292




293




294




295




		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f"), flow("3", "4")),
				callSite("e").calls("bar", flow("4", "2.g"), kill("3^f"))); 
		
		helper.runSolver(false, "a");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),
				callSite("e").calls("bar", flow("4", "2.g")).retSite("f", kill("4")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






307




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3.g"), kill("2.g" /* 2^f is back substituted to 2.g*/))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






318




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






319




320




321




322




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






323




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3"), kill("2^f"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






324




325




326




327




		
		helper.runSolver(false, "a");
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1")),
				callSite("a").calls("bar", flow("1", "1^f")).retSite("d", kill("1")));
		
		helper.method("bar",
				startPoints("b"),
				normalStmt("b").succ("c", flow("1", readField("f"), "2.f")),
				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






343




344




345




346




347




348




349




	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.g")),
				callSite("b").calls("bar", flow("1.g", "1.g")).retSite("e", kill("1.g")),
				normalStmt("e").succ("f", flow("1.g", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






350




				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






351




352




353




354




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("f"), "2"), flow("1", "1")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






355




				exitStmt("d").returns(over("b"), to("e"), flow("1.g", "1.g") /* ignore fact 2, not possible with this caller ctx*/).returns(over("f"), to("g"), kill("1"), kill("2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", "2", "2^f")),
				normalStmt("c").succ("d", kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1.f")),
				callSite("a").calls("bar", flow("1.f", "1.f")).retSite("f", kill("1.f")),
				callSite("f").calls("bar", flow("2", "1.g")));
				
		helper.method("bar",
				startPoints("b"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






381




				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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
	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






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




	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("bar",flow("1.x", "2.x")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3", readField("f"), "4")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.x")).calls("foo2", flow("1.x", "2.x")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.f")).calls("foo2", flow("1.x", "2.f")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f", kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", writeField("a"), "1^a")),
				callSite("d").calls("xyz", flow("1^a", "1^a")));
		
		helper.method("xyz",
				startPoints("e"),
				normalStmt("e").succ("f", flow("1", readField("f"), "2")),
				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),
				normalStmt("g").succ("h", flow("3", readField("a"), "4")));
		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






568




}











Open sidebar



Joshua Garcia heros

f08a8e492c7c5ca97e16c48dab2d199af5190da0







Open sidebar



Joshua Garcia heros

f08a8e492c7c5ca97e16c48dab2d199af5190da0




Open sidebar

Joshua Garcia heros

f08a8e492c7c5ca97e16c48dab2d199af5190da0


Joshua Garciaherosheros
f08a8e492c7c5ca97e16c48dab2d199af5190da0










f08a8e492c7c5ca97e16c48dab2d199af5190da0


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveSolverTest.java



17.1 KB









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









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






16




17




import org.junit.Ignore;
import org.junit.Rule;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






18




import org.junit.Test;









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






19




import org.junit.rules.TestWatcher;









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





import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






29




		System.err.println("-----");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






30




31




32




		helper = new TestHelper();
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {
			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void mergeWithExistingPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("b", flow("1", "1.f")).succ("c", flow("1", "2")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void dontMergeWithExistingNonPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				normalStmt("b").succ("b", flow("1.f", "1"), kill("1")).succ("c", flow("1.f", "2"), kill("1")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






63




64




65




66




67




	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






68




69




70




				normalStmt("b").succ("c", flow("1", writeField("f"), "2.f")),
				normalStmt("c").succ("d", flow("2.f", "2.f")),
				normalStmt("d").succ("e", flow("2.f", readField("f"), "4")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






71




72




73




74




75




76




77




78




79




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






80




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









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






90




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






91




92




93




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






94




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






114




				normalStmt("b").succ("c", flow("1", readField("field"), "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




				callSite("c").calls("foo", flow("2.field", "3.field")));
		









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






117




118




119




		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("notfield"), "5"), flow("3", "3")),









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




				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






129




130




131




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("rs", kill("2.field")),
				callSite("rs").calls("foo", flow("5", "3.notfield")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






132




133




		
		helper.method("foo",startPoints("d"),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






134




135




136




				normalStmt("d").succ("e", flow("3", "3"), flow("3", readField("notfield"), "6")),
				normalStmt("e").succ("f", flow("3","4"), kill("6")),
				exitStmt("f").returns(over("c"), to("rs"), flow("4.field", "5")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






137




138




139




		
		helper.runSolver(false, "a", "g");
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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





	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






150




				callSite("c").calls("xyz", flow("2", "3")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




		
		helper.method("xyz", 
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"	, kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






161




	public void prefixFactOfSummaryIgnored() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






162




163




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






164




				normalStmt("a").succ("b", flow("0","1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






165




				callSite("b").calls("bar", flow("1", "2.f")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






166




167




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),
				normalStmt("f").succ("g"));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






168




		









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






169




		helper.method("bar",









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






170




				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






171




172




				normalStmt("c").succ("d", flow("2", readField("f"), "3")),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






173




174




175




176




		
		helper.runSolver(false, "a");
	}
	









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






177




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






178




	public void doNotPauseZeroSources() {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






179




180




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				normalStmt("b").succ("c", kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void loopAndMerge() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", readField("f"), "2"), flow("1", "1"), flow("2", "2")),
				normalStmt("c").succ("d", flow("1", "1", "1.f"), flow("2", "2")),
				normalStmt("d").succ("e", flow("1", "1"), flow("2", "2")).succ("b", flow("1", "1"), flow("2", "2")),
				normalStmt("e").succ("f", kill("1"), kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void doNotMergePrefixFacts() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "1")).succ("b2", flow("0", "1")),
				normalStmt("b1").succ("c", flow("1", "1")),
				normalStmt("b2").succ("c", flow("1", "1.f")),
				normalStmt("c").succ("d", kill("1"), kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






219




220




221




		
		helper.method("bar",
				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






222




223




				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






224




225




226




227




		
		helper.runSolver(false, "a");
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






228




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






229




	public void pauseOnOverwrittenFieldOfInterest2() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






230




231




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






232




233




234




235




236




237




238




				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f.g")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






239




240




241




		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






242




	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






243




244




245




246




247




	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






248




				callSite("b").calls("bar", flow("1.f", "2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






249




250




251




252




253




254




255




256




257




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e", kill("2^f"))); 
		
		helper.runSolver(false, "a");
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






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




	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f")),
				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






286




				callSite("c").calls("xyz", flow("2", "3")));









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






287




288




289




290




291




292




293




294




295




		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f"), flow("3", "4")),
				callSite("e").calls("bar", flow("4", "2.g"), kill("3^f"))); 
		
		helper.runSolver(false, "a");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),
				callSite("e").calls("bar", flow("4", "2.g")).retSite("f", kill("4")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






307




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3.g"), kill("2.g" /* 2^f is back substituted to 2.g*/))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






318




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






319




320




321




322




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






323




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3"), kill("2^f"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






324




325




326




327




		
		helper.runSolver(false, "a");
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1")),
				callSite("a").calls("bar", flow("1", "1^f")).retSite("d", kill("1")));
		
		helper.method("bar",
				startPoints("b"),
				normalStmt("b").succ("c", flow("1", readField("f"), "2.f")),
				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






343




344




345




346




347




348




349




	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.g")),
				callSite("b").calls("bar", flow("1.g", "1.g")).retSite("e", kill("1.g")),
				normalStmt("e").succ("f", flow("1.g", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






350




				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






351




352




353




354




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("f"), "2"), flow("1", "1")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






355




				exitStmt("d").returns(over("b"), to("e"), flow("1.g", "1.g") /* ignore fact 2, not possible with this caller ctx*/).returns(over("f"), to("g"), kill("1"), kill("2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", "2", "2^f")),
				normalStmt("c").succ("d", kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1.f")),
				callSite("a").calls("bar", flow("1.f", "1.f")).retSite("f", kill("1.f")),
				callSite("f").calls("bar", flow("2", "1.g")));
				
		helper.method("bar",
				startPoints("b"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






381




				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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
	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






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




	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("bar",flow("1.x", "2.x")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3", readField("f"), "4")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.x")).calls("foo2", flow("1.x", "2.x")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.f")).calls("foo2", flow("1.x", "2.f")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f", kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", writeField("a"), "1^a")),
				callSite("d").calls("xyz", flow("1^a", "1^a")));
		
		helper.method("xyz",
				startPoints("e"),
				normalStmt("e").succ("f", flow("1", readField("f"), "2")),
				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),
				normalStmt("g").succ("h", flow("3", readField("a"), "4")));
		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






568




}














f08a8e492c7c5ca97e16c48dab2d199af5190da0


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink






FieldSensitiveSolverTest.java



17.1 KB









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









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






16




17




import org.junit.Ignore;
import org.junit.Rule;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






18




import org.junit.Test;









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






19




import org.junit.rules.TestWatcher;









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





import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






29




		System.err.println("-----");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






30




31




32




		helper = new TestHelper();
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {
			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void mergeWithExistingPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("b", flow("1", "1.f")).succ("c", flow("1", "2")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void dontMergeWithExistingNonPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				normalStmt("b").succ("b", flow("1.f", "1"), kill("1")).succ("c", flow("1.f", "2"), kill("1")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






63




64




65




66




67




	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






68




69




70




				normalStmt("b").succ("c", flow("1", writeField("f"), "2.f")),
				normalStmt("c").succ("d", flow("2.f", "2.f")),
				normalStmt("d").succ("e", flow("2.f", readField("f"), "4")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






71




72




73




74




75




76




77




78




79




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






80




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









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






90




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






91




92




93




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






94




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






114




				normalStmt("b").succ("c", flow("1", readField("field"), "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




				callSite("c").calls("foo", flow("2.field", "3.field")));
		









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






117




118




119




		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("notfield"), "5"), flow("3", "3")),









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




				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






129




130




131




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("rs", kill("2.field")),
				callSite("rs").calls("foo", flow("5", "3.notfield")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






132




133




		
		helper.method("foo",startPoints("d"),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






134




135




136




				normalStmt("d").succ("e", flow("3", "3"), flow("3", readField("notfield"), "6")),
				normalStmt("e").succ("f", flow("3","4"), kill("6")),
				exitStmt("f").returns(over("c"), to("rs"), flow("4.field", "5")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






137




138




139




		
		helper.runSolver(false, "a", "g");
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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





	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






150




				callSite("c").calls("xyz", flow("2", "3")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




		
		helper.method("xyz", 
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"	, kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






161




	public void prefixFactOfSummaryIgnored() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






162




163




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






164




				normalStmt("a").succ("b", flow("0","1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






165




				callSite("b").calls("bar", flow("1", "2.f")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






166




167




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),
				normalStmt("f").succ("g"));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






168




		









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






169




		helper.method("bar",









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






170




				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






171




172




				normalStmt("c").succ("d", flow("2", readField("f"), "3")),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






173




174




175




176




		
		helper.runSolver(false, "a");
	}
	









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






177




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






178




	public void doNotPauseZeroSources() {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






179




180




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				normalStmt("b").succ("c", kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void loopAndMerge() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", readField("f"), "2"), flow("1", "1"), flow("2", "2")),
				normalStmt("c").succ("d", flow("1", "1", "1.f"), flow("2", "2")),
				normalStmt("d").succ("e", flow("1", "1"), flow("2", "2")).succ("b", flow("1", "1"), flow("2", "2")),
				normalStmt("e").succ("f", kill("1"), kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void doNotMergePrefixFacts() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "1")).succ("b2", flow("0", "1")),
				normalStmt("b1").succ("c", flow("1", "1")),
				normalStmt("b2").succ("c", flow("1", "1.f")),
				normalStmt("c").succ("d", kill("1"), kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






219




220




221




		
		helper.method("bar",
				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






222




223




				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






224




225




226




227




		
		helper.runSolver(false, "a");
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






228




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






229




	public void pauseOnOverwrittenFieldOfInterest2() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






230




231




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






232




233




234




235




236




237




238




				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f.g")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






239




240




241




		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






242




	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






243




244




245




246




247




	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






248




				callSite("b").calls("bar", flow("1.f", "2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






249




250




251




252




253




254




255




256




257




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e", kill("2^f"))); 
		
		helper.runSolver(false, "a");
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






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




	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f")),
				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






286




				callSite("c").calls("xyz", flow("2", "3")));









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






287




288




289




290




291




292




293




294




295




		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f"), flow("3", "4")),
				callSite("e").calls("bar", flow("4", "2.g"), kill("3^f"))); 
		
		helper.runSolver(false, "a");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),
				callSite("e").calls("bar", flow("4", "2.g")).retSite("f", kill("4")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






307




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3.g"), kill("2.g" /* 2^f is back substituted to 2.g*/))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






318




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






319




320




321




322




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






323




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3"), kill("2^f"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






324




325




326




327




		
		helper.runSolver(false, "a");
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1")),
				callSite("a").calls("bar", flow("1", "1^f")).retSite("d", kill("1")));
		
		helper.method("bar",
				startPoints("b"),
				normalStmt("b").succ("c", flow("1", readField("f"), "2.f")),
				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






343




344




345




346




347




348




349




	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.g")),
				callSite("b").calls("bar", flow("1.g", "1.g")).retSite("e", kill("1.g")),
				normalStmt("e").succ("f", flow("1.g", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






350




				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






351




352




353




354




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("f"), "2"), flow("1", "1")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






355




				exitStmt("d").returns(over("b"), to("e"), flow("1.g", "1.g") /* ignore fact 2, not possible with this caller ctx*/).returns(over("f"), to("g"), kill("1"), kill("2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", "2", "2^f")),
				normalStmt("c").succ("d", kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1.f")),
				callSite("a").calls("bar", flow("1.f", "1.f")).retSite("f", kill("1.f")),
				callSite("f").calls("bar", flow("2", "1.g")));
				
		helper.method("bar",
				startPoints("b"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






381




				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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
	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






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




	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("bar",flow("1.x", "2.x")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3", readField("f"), "4")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.x")).calls("foo2", flow("1.x", "2.x")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.f")).calls("foo2", flow("1.x", "2.f")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f", kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", writeField("a"), "1^a")),
				callSite("d").calls("xyz", flow("1^a", "1^a")));
		
		helper.method("xyz",
				startPoints("e"),
				normalStmt("e").succ("f", flow("1", readField("f"), "2")),
				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),
				normalStmt("g").succ("h", flow("3", readField("a"), "4")));
		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






568




}










f08a8e492c7c5ca97e16c48dab2d199af5190da0


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java



Find file
Normal viewHistoryPermalink




f08a8e492c7c5ca97e16c48dab2d199af5190da0


Switch branch/tag










heros


test


heros


alias


FieldSensitiveSolverTest.java





f08a8e492c7c5ca97e16c48dab2d199af5190da0


Switch branch/tag








f08a8e492c7c5ca97e16c48dab2d199af5190da0


Switch branch/tag





f08a8e492c7c5ca97e16c48dab2d199af5190da0

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



17.1 KB









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









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






16




17




import org.junit.Ignore;
import org.junit.Rule;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






18




import org.junit.Test;









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






19




import org.junit.rules.TestWatcher;









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





import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






29




		System.err.println("-----");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






30




31




32




		helper = new TestHelper();
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {
			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void mergeWithExistingPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("b", flow("1", "1.f")).succ("c", flow("1", "2")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void dontMergeWithExistingNonPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				normalStmt("b").succ("b", flow("1.f", "1"), kill("1")).succ("c", flow("1.f", "2"), kill("1")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






63




64




65




66




67




	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






68




69




70




				normalStmt("b").succ("c", flow("1", writeField("f"), "2.f")),
				normalStmt("c").succ("d", flow("2.f", "2.f")),
				normalStmt("d").succ("e", flow("2.f", readField("f"), "4")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






71




72




73




74




75




76




77




78




79




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






80




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









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






90




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






91




92




93




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






94




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






114




				normalStmt("b").succ("c", flow("1", readField("field"), "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




				callSite("c").calls("foo", flow("2.field", "3.field")));
		









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






117




118




119




		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("notfield"), "5"), flow("3", "3")),









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




				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






129




130




131




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("rs", kill("2.field")),
				callSite("rs").calls("foo", flow("5", "3.notfield")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






132




133




		
		helper.method("foo",startPoints("d"),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






134




135




136




				normalStmt("d").succ("e", flow("3", "3"), flow("3", readField("notfield"), "6")),
				normalStmt("e").succ("f", flow("3","4"), kill("6")),
				exitStmt("f").returns(over("c"), to("rs"), flow("4.field", "5")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






137




138




139




		
		helper.runSolver(false, "a", "g");
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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





	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






150




				callSite("c").calls("xyz", flow("2", "3")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




		
		helper.method("xyz", 
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"	, kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






161




	public void prefixFactOfSummaryIgnored() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






162




163




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






164




				normalStmt("a").succ("b", flow("0","1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






165




				callSite("b").calls("bar", flow("1", "2.f")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






166




167




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),
				normalStmt("f").succ("g"));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






168




		









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






169




		helper.method("bar",









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






170




				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






171




172




				normalStmt("c").succ("d", flow("2", readField("f"), "3")),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






173




174




175




176




		
		helper.runSolver(false, "a");
	}
	









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






177




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






178




	public void doNotPauseZeroSources() {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






179




180




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				normalStmt("b").succ("c", kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void loopAndMerge() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", readField("f"), "2"), flow("1", "1"), flow("2", "2")),
				normalStmt("c").succ("d", flow("1", "1", "1.f"), flow("2", "2")),
				normalStmt("d").succ("e", flow("1", "1"), flow("2", "2")).succ("b", flow("1", "1"), flow("2", "2")),
				normalStmt("e").succ("f", kill("1"), kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void doNotMergePrefixFacts() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "1")).succ("b2", flow("0", "1")),
				normalStmt("b1").succ("c", flow("1", "1")),
				normalStmt("b2").succ("c", flow("1", "1.f")),
				normalStmt("c").succ("d", kill("1"), kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






219




220




221




		
		helper.method("bar",
				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






222




223




				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






224




225




226




227




		
		helper.runSolver(false, "a");
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






228




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






229




	public void pauseOnOverwrittenFieldOfInterest2() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






230




231




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






232




233




234




235




236




237




238




				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f.g")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






239




240




241




		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






242




	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






243




244




245




246




247




	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






248




				callSite("b").calls("bar", flow("1.f", "2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






249




250




251




252




253




254




255




256




257




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e", kill("2^f"))); 
		
		helper.runSolver(false, "a");
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






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




	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f")),
				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






286




				callSite("c").calls("xyz", flow("2", "3")));









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






287




288




289




290




291




292




293




294




295




		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f"), flow("3", "4")),
				callSite("e").calls("bar", flow("4", "2.g"), kill("3^f"))); 
		
		helper.runSolver(false, "a");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),
				callSite("e").calls("bar", flow("4", "2.g")).retSite("f", kill("4")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






307




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3.g"), kill("2.g" /* 2^f is back substituted to 2.g*/))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






318




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






319




320




321




322




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






323




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3"), kill("2^f"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






324




325




326




327




		
		helper.runSolver(false, "a");
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1")),
				callSite("a").calls("bar", flow("1", "1^f")).retSite("d", kill("1")));
		
		helper.method("bar",
				startPoints("b"),
				normalStmt("b").succ("c", flow("1", readField("f"), "2.f")),
				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






343




344




345




346




347




348




349




	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.g")),
				callSite("b").calls("bar", flow("1.g", "1.g")).retSite("e", kill("1.g")),
				normalStmt("e").succ("f", flow("1.g", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






350




				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






351




352




353




354




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("f"), "2"), flow("1", "1")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






355




				exitStmt("d").returns(over("b"), to("e"), flow("1.g", "1.g") /* ignore fact 2, not possible with this caller ctx*/).returns(over("f"), to("g"), kill("1"), kill("2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", "2", "2^f")),
				normalStmt("c").succ("d", kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1.f")),
				callSite("a").calls("bar", flow("1.f", "1.f")).retSite("f", kill("1.f")),
				callSite("f").calls("bar", flow("2", "1.g")));
				
		helper.method("bar",
				startPoints("b"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






381




				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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
	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






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




	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("bar",flow("1.x", "2.x")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3", readField("f"), "4")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.x")).calls("foo2", flow("1.x", "2.x")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.f")).calls("foo2", flow("1.x", "2.f")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f", kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", writeField("a"), "1^a")),
				callSite("d").calls("xyz", flow("1^a", "1^a")));
		
		helper.method("xyz",
				startPoints("e"),
				normalStmt("e").succ("f", flow("1", readField("f"), "2")),
				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),
				normalStmt("g").succ("h", flow("3", readField("a"), "4")));
		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






568




}








FieldSensitiveSolverTest.java



17.1 KB










FieldSensitiveSolverTest.java



17.1 KB









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









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






16




17




import org.junit.Ignore;
import org.junit.Rule;









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






18




import org.junit.Test;









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






19




import org.junit.rules.TestWatcher;









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





import static heros.alias.TestHelper.*;

public class FieldSensitiveSolverTest {

	private TestHelper helper;

	@Before
	public void before() {









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






29




		System.err.println("-----");









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






30




31




32




		helper = new TestHelper();
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {
			System.err.println("---failed: "+description.getMethodName()+" ----");
		};
	};
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void mergeWithExistingPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("b", flow("1", "1.f")).succ("c", flow("1", "2")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void dontMergeWithExistingNonPrefixFacts() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				normalStmt("b").succ("b", flow("1.f", "1"), kill("1")).succ("c", flow("1.f", "2"), kill("1")),
				normalStmt("c").succ("d", kill("2")));
				
		helper.runSolver(false, "a");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






63




64




65




66




67




	@Test
	public void fieldReadAndWrite() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






68




69




70




				normalStmt("b").succ("c", flow("1", writeField("f"), "2.f")),
				normalStmt("c").succ("d", flow("2.f", "2.f")),
				normalStmt("d").succ("e", flow("2.f", readField("f"), "4")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






71




72




73




74




75




76




77




78




79




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void createSummaryForBaseValue() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






80




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









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






90




	public void reuseSummaryForBaseValue() {









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






91




92




93




		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






94




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),









use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014






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









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






114




				normalStmt("b").succ("c", flow("1", readField("field"), "2.field")),









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






115




116




				callSite("c").calls("foo", flow("2.field", "3.field")));
		









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






117




118




119




		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("notfield"), "5"), flow("3", "3")),









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




				normalStmt("e").succ("f", flow("3","4")));
		helper.runSolver(false, "a");
	}
	
	@Test
	public void holdAndResume() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






129




130




131




				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),
				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("rs", kill("2.field")),
				callSite("rs").calls("foo", flow("5", "3.notfield")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






132




133




		
		helper.method("foo",startPoints("d"),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






134




135




136




				normalStmt("d").succ("e", flow("3", "3"), flow("3", readField("notfield"), "6")),
				normalStmt("e").succ("f", flow("3","4"), kill("6")),
				exitStmt("f").returns(over("c"), to("rs"), flow("4.field", "5")));









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






137




138




139




		
		helper.runSolver(false, "a", "g");
	}









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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





	@Test
	public void doNotHoldIfInterestedTransitiveCallerExists() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






150




				callSite("c").calls("xyz", flow("2", "3")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






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




		
		helper.method("xyz", 
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"	, kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






161




	public void prefixFactOfSummaryIgnored() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






162




163




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






164




				normalStmt("a").succ("b", flow("0","1")),









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






165




				callSite("b").calls("bar", flow("1", "2.f")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






166




167




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),
				normalStmt("f").succ("g"));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






168




		









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






169




		helper.method("bar",









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






170




				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






171




172




				normalStmt("c").succ("d", flow("2", readField("f"), "3")),
				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f")));









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






173




174




175




176




		
		helper.runSolver(false, "a");
	}
	









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






177




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






178




	public void doNotPauseZeroSources() {









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






179




180




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),
				normalStmt("b").succ("c", kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	@Ignore("assumes k-limiting not used")
	public void loopAndMerge() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", readField("f"), "2"), flow("1", "1"), flow("2", "2")),
				normalStmt("c").succ("d", flow("1", "1", "1.f"), flow("2", "2")),
				normalStmt("d").succ("e", flow("1", "1"), flow("2", "2")).succ("b", flow("1", "1"), flow("2", "2")),
				normalStmt("e").succ("f", kill("1"), kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void doNotMergePrefixFacts() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "1")).succ("b2", flow("0", "1")),
				normalStmt("b1").succ("c", flow("1", "1")),
				normalStmt("b2").succ("c", flow("1", "1.f")),
				normalStmt("c").succ("d", kill("1"), kill("1.f")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseOnOverwrittenFieldOfInterest() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






219




220




221




		
		helper.method("bar",
				startPoints("c"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






222




223




				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reached









handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014






224




225




226




227




		
		helper.runSolver(false, "a");
	}
	









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






228




	@Test









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






229




	public void pauseOnOverwrittenFieldOfInterest2() {









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






230




231




		helper.method("foo",
				startPoints("a"),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






232




233




234




235




236




237




238




				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f.g")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reached









edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014






239




240




241




		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






242




	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






243




244




245




246




247




	@Test
	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






248




				callSite("b").calls("bar", flow("1.f", "2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






249




250




251




252




253




254




255




256




257




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),
				normalStmt("d").succ("e", kill("2^f"))); 
		
		helper.runSolver(false, "a");
	}
	









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






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




	@Test
	public void pauseOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f")),
				normalStmt("e").succ("f")); 
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumePausedOnTransitiveExclusion() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")));
		
		helper.method("bar",
				startPoints("c"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






286




				callSite("c").calls("xyz", flow("2", "3")));









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






287




288




289




290




291




292




293




294




295




		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f"), flow("3", "4")),
				callSite("e").calls("bar", flow("4", "2.g"), kill("3^f"))); 
		
		helper.runSolver(false, "a");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




	@Test
	public void resumeEdgePausedOnOverwrittenField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),
				callSite("e").calls("bar", flow("4", "2.g")).retSite("f", kill("4")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






307




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3.g"), kill("2.g" /* 2^f is back substituted to 2.g*/))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.f")),
				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






318




				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






319




320




321




322




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






323




				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3"), kill("2^f"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






324




325




326




327




		
		helper.runSolver(false, "a");
	}
	









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




	@Test
	public void exclusionOnPotentiallyInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1")),
				callSite("a").calls("bar", flow("1", "1^f")).retSite("d", kill("1")));
		
		helper.method("bar",
				startPoints("b"),
				normalStmt("b").succ("c", flow("1", readField("f"), "2.f")),
				exitStmt("c").returns(over("a"), to("d")));
		
		helper.runSolver(false, "sp");
	}
	









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






343




344




345




346




347




348




349




	@Test
	public void registerPausedEdgeInLateCallers() {
		helper.method("foo", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.g")),
				callSite("b").calls("bar", flow("1.g", "1.g")).retSite("e", kill("1.g")),
				normalStmt("e").succ("f", flow("1.g", "3")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






350




				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); 









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






351




352




353




354




		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("f"), "2"), flow("1", "1")),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






355




				exitStmt("d").returns(over("b"), to("e"), flow("1.g", "1.g") /* ignore fact 2, not possible with this caller ctx*/).returns(over("f"), to("g"), kill("1"), kill("2")));









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void mergeExcludedField() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				normalStmt("b").succ("c", flow("1", "2", "2^f")),
				normalStmt("c").succ("d", kill("2")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void resumeOnTransitiveInterestedCaller() {
		helper.method("foo",
				startPoints("sp"),
				normalStmt("sp").succ("a", flow("0", "1.f")),
				callSite("a").calls("bar", flow("1.f", "1.f")).retSite("f", kill("1.f")),
				callSite("f").calls("bar", flow("2", "1.g")));
				
		helper.method("bar",
				startPoints("b"),









"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015






381




				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),









Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014






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




				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));
		
		helper.method("xyz",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")),
				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));
		
				
		helper.runSolver(false, "sp");
	}
	









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






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
	









k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015






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




	@Test
	public void pauseEdgeMutuallyRecursiveCallers() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("bar",flow("1.x", "2.x")));
		
		helper.method("bar",
				startPoints("c"),
				callSite("c").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),
				normalStmt("e").succ("f", flow("3", readField("f"), "4")));
				
		helper.runSolver(false, "a");
	}
	
	@Test
	public void pauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.x")).calls("foo2", flow("1.x", "2.x")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f"));
		
		helper.runSolver(false, "a");
	}

	@Test
	public void dontPauseDiamondShapedCallerChain() {
		helper.method("bar",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1.x")),
				callSite("b").calls("foo1", flow("1.x", "2.f")).calls("foo2", flow("1.x", "2.f")));
		
		helper.method("foo1",
				startPoints("c1"),
				callSite("c1").calls("xyz", flow("2", "3")));
		
		helper.method("foo2",
				startPoints("c2"),
				callSite("c2").calls("xyz", flow("2", "3")));
		
		helper.method("xyz",
				startPoints("d"),
				normalStmt("d").succ("e", flow("3", readField("f"), "4")),
				normalStmt("e").succ("f", kill("4")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void correctDeltaConstraintApplication() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				callSite("b").calls("bar", flow("1", "1")));
		
		helper.method("bar",
				startPoints("c"),
				normalStmt("c").succ("d", flow("1", writeField("a"), "1^a")),
				callSite("d").calls("xyz", flow("1^a", "1^a")));
		
		helper.method("xyz",
				startPoints("e"),
				normalStmt("e").succ("f", flow("1", readField("f"), "2")),
				callSite("f").calls("baz", flow("2", "3")));
		
		helper.method("baz",
				startPoints("g"),
				normalStmt("g").succ("h", flow("3", readField("a"), "4")));
		
		helper.runSolver(false, "a");
	}









FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014






568




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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import org.junit.Before;importorg.junit.Before;



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

16

17
import org.junit.Ignore;importorg.junit.Ignore;import org.junit.Rule;importorg.junit.Rule;



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

18
import org.junit.Test;importorg.junit.Test;



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

19
import org.junit.rules.TestWatcher;importorg.junit.rules.TestWatcher;



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
import static heros.alias.TestHelper.*;importstaticheros.alias.TestHelper.*;public class FieldSensitiveSolverTest {publicclassFieldSensitiveSolverTest{	private TestHelper helper;privateTestHelperhelper;	@Before@Before	public void before() {publicvoidbefore(){



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

29
		System.err.println("-----");System.err.println("-----");



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

30

31

32
		helper = new TestHelper();helper=newTestHelper();	}}	



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

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
	@Rule@Rule	public TestWatcher watcher = new TestWatcher() {publicTestWatcherwatcher=newTestWatcher(){		protected void failed(Throwable e, org.junit.runner.Description description) {protectedvoidfailed(Throwablee,org.junit.runner.Descriptiondescription){			System.err.println("---failed: "+description.getMethodName()+" ----");System.err.println("---failed: "+description.getMethodName()+" ----");		};};	};};		@Test@Test	@Ignore("assumes k-limiting not used")@Ignore("assumes k-limiting not used")	public void mergeWithExistingPrefixFacts() {publicvoidmergeWithExistingPrefixFacts(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				normalStmt("b").succ("b", flow("1", "1.f")).succ("c", flow("1", "2")),normalStmt("b").succ("b",flow("1","1.f")).succ("c",flow("1","2")),				normalStmt("c").succ("d", kill("2")));normalStmt("c").succ("d",kill("2")));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void dontMergeWithExistingNonPrefixFacts() {publicvoiddontMergeWithExistingNonPrefixFacts(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.f")),normalStmt("a").succ("b",flow("0","1.f")),				normalStmt("b").succ("b", flow("1.f", "1"), kill("1")).succ("c", flow("1.f", "2"), kill("1")),normalStmt("b").succ("b",flow("1.f","1"),kill("1")).succ("c",flow("1.f","2"),kill("1")),				normalStmt("c").succ("d", kill("2")));normalStmt("c").succ("d",kill("2")));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

63

64

65

66

67
	@Test@Test	public void fieldReadAndWrite() {publicvoidfieldReadAndWrite(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

68

69

70
				normalStmt("b").succ("c", flow("1", writeField("f"), "2.f")),normalStmt("b").succ("c",flow("1",writeField("f"),"2.f")),				normalStmt("c").succ("d", flow("2.f", "2.f")),normalStmt("c").succ("d",flow("2.f","2.f")),				normalStmt("d").succ("e", flow("2.f", readField("f"), "4")));normalStmt("d").succ("e",flow("2.f",readField("f"),"4")));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

71

72

73

74

75

76

77

78

79
				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void createSummaryForBaseValue() {publicvoidcreateSummaryForBaseValue(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

80
				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),normalStmt("b").succ("c",flow("1",writeField("field"),"2.field")),



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
				callSite("c").calls("foo", flow("2.field", "3.field")));callSite("c").calls("foo",flow("2.field","3.field")));				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),				normalStmt("d").succ("e", flow("3", "4")),normalStmt("d").succ("e",flow("3","4")),				normalStmt("e").succ("f", flow("4","4")));normalStmt("e").succ("f",flow("4","4")));		helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

90
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

91

92

93
		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

94
				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),normalStmt("b").succ("c",flow("1",writeField("field"),"2.field")),



use of abstracted summaries


 

 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


 

 

use of abstracted summaries

 

Johannes Lerch
committed
Oct 22, 2014

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
				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("retC", flow("2.field", "2.field")));callSite("c").calls("foo",flow("2.field","3.field")).retSite("retC",flow("2.field","2.field")));				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),				normalStmt("d").succ("e", flow("3", "4")),normalStmt("d").succ("e",flow("3","4")),				normalStmt("e").succ("f", flow("4","4")),normalStmt("e").succ("f",flow("4","4")),				exitStmt("f").returns(over("c"), to("retC"), flow("4.field", "5.field")).returns(over("g"), to("retG"), flow("4.anotherField", "6.anotherField")));exitStmt("f").returns(over("c"),to("retC"),flow("4.field","5.field")).returns(over("g"),to("retG"),flow("4.anotherField","6.anotherField")));		helper.method("xyz", helper.method("xyz",				startPoints("g"),startPoints("g"),				callSite("g").calls("foo", flow("0", "3.anotherField")).retSite("retG", kill("0")));callSite("g").calls("foo",flow("0","3.anotherField")).retSite("retG",kill("0")));				helper.runSolver(false, "a", "g");helper.runSolver(false,"a","g");	}}		@Test@Test	public void hold() {publicvoidhold(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

114
				normalStmt("b").succ("c", flow("1", readField("field"), "2.field")),normalStmt("b").succ("c",flow("1",readField("field"),"2.field")),



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
				callSite("c").calls("foo", flow("2.field", "3.field")));callSite("c").calls("foo",flow("2.field","3.field")));		



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

117

118

119
		helper.method("foo",helper.method("foo",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("3", readField("notfield"), "5"), flow("3", "3")),normalStmt("d").succ("e",flow("3",readField("notfield"),"5"),flow("3","3")),



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
				normalStmt("e").succ("f", flow("3","4")));normalStmt("e").succ("f",flow("3","4")));		helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void holdAndResume() {publicvoidholdAndResume(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

129

130

131
				normalStmt("b").succ("c", flow("1", writeField("field"), "2.field")),normalStmt("b").succ("c",flow("1",writeField("field"),"2.field")),				callSite("c").calls("foo", flow("2.field", "3.field")).retSite("rs", kill("2.field")),callSite("c").calls("foo",flow("2.field","3.field")).retSite("rs",kill("2.field")),				callSite("rs").calls("foo", flow("5", "3.notfield")));callSite("rs").calls("foo",flow("5","3.notfield")));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

132

133
				helper.method("foo",startPoints("d"),helper.method("foo",startPoints("d"),



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

136
				normalStmt("d").succ("e", flow("3", "3"), flow("3", readField("notfield"), "6")),normalStmt("d").succ("e",flow("3","3"),flow("3",readField("notfield"),"6")),				normalStmt("e").succ("f", flow("3","4"), kill("6")),normalStmt("e").succ("f",flow("3","4"),kill("6")),				exitStmt("f").returns(over("c"), to("rs"), flow("4.field", "5")));exitStmt("f").returns(over("c"),to("rs"),flow("4.field","5")));



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

137

138

139
				helper.runSolver(false, "a", "g");helper.runSolver(false,"a","g");	}}



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

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
	@Test@Test	public void doNotHoldIfInterestedTransitiveCallerExists() {publicvoiddoNotHoldIfInterestedTransitiveCallerExists(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),normalStmt("a").succ("b",flow("0",readField("f"),"1.f")),				callSite("b").calls("bar", flow("1.f", "2.f")));callSite("b").calls("bar",flow("1.f","2.f")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

150
				callSite("c").calls("xyz", flow("2", "3")));callSite("c").calls("xyz",flow("2","3")));



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

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
				helper.method("xyz", helper.method("xyz",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("3", readField("f"), "4")),normalStmt("d").succ("e",flow("3",readField("f"),"4")),				normalStmt("e").succ("f"	, kill("4")));normalStmt("e").succ("f",kill("4")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

161
	public void prefixFactOfSummaryIgnored() {publicvoidprefixFactOfSummaryIgnored(){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

162

163
		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

164
				normalStmt("a").succ("b", flow("0","1")),normalStmt("a").succ("b",flow("0","1")),



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

165
				callSite("b").calls("bar", flow("1", "2.f")).retSite("e", kill("1")),callSite("b").calls("bar",flow("1","2.f")).retSite("e",kill("1")),



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

167
				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")),callSite("e").calls("bar",flow("4","2")).retSite("f",kill("4")),				normalStmt("f").succ("g"));normalStmt("f").succ("g"));



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

168
		



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

169
		helper.method("bar",helper.method("bar",



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

170
				startPoints("c"),startPoints("c"),



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
				normalStmt("c").succ("d", flow("2", readField("f"), "3")),normalStmt("c").succ("d",flow("2",readField("f"),"3")),				exitStmt("d").returns(over("b"), to("e"), flow("3", "4")).returns(over("e"), to("f")));exitStmt("d").returns(over("b"),to("e"),flow("3","4")).returns(over("e"),to("f")));



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

173

174

175

176
				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

177
	@Test@Test



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

178
	public void doNotPauseZeroSources() {publicvoiddoNotPauseZeroSources(){



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

179

180
		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),



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
				normalStmt("a").succ("b", flow("0", readField("f"), "1.f")),normalStmt("a").succ("b",flow("0",readField("f"),"1.f")),				normalStmt("b").succ("c", kill("1.f")));normalStmt("b").succ("c",kill("1.f")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	@Ignore("assumes k-limiting not used")@Ignore("assumes k-limiting not used")	public void loopAndMerge() {publicvoidloopAndMerge(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				normalStmt("b").succ("c", flow("1", readField("f"), "2"), flow("1", "1"), flow("2", "2")),normalStmt("b").succ("c",flow("1",readField("f"),"2"),flow("1","1"),flow("2","2")),				normalStmt("c").succ("d", flow("1", "1", "1.f"), flow("2", "2")),normalStmt("c").succ("d",flow("1","1","1.f"),flow("2","2")),				normalStmt("d").succ("e", flow("1", "1"), flow("2", "2")).succ("b", flow("1", "1"), flow("2", "2")),normalStmt("d").succ("e",flow("1","1"),flow("2","2")).succ("b",flow("1","1"),flow("2","2")),				normalStmt("e").succ("f", kill("1"), kill("2")));normalStmt("e").succ("f",kill("1"),kill("2")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void doNotMergePrefixFacts() {publicvoiddoNotMergePrefixFacts(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b1", flow("0", "1")).succ("b2", flow("0", "1")),normalStmt("a").succ("b1",flow("0","1")).succ("b2",flow("0","1")),				normalStmt("b1").succ("c", flow("1", "1")),normalStmt("b1").succ("c",flow("1","1")),				normalStmt("b2").succ("c", flow("1", "1.f")),normalStmt("b2").succ("c",flow("1","1.f")),				normalStmt("c").succ("d", kill("1"), kill("1.f")));normalStmt("c").succ("d",kill("1"),kill("1.f")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void pauseOnOverwrittenFieldOfInterest() {publicvoidpauseOnOverwrittenFieldOfInterest(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.f")),normalStmt("a").succ("b",flow("0","1.f")),				callSite("b").calls("bar", flow("1.f", "2.f")));callSite("b").calls("bar",flow("1.f","2.f")));



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

219

220

221
				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

222

223
				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),normalStmt("c").succ("d",flow("2",writeField("f"),"2^f")),				normalStmt("d").succ("e")); //only interested in 2.f, but f excluded so this should not be reachednormalStmt("d").succ("e"));//only interested in 2.f, but f excluded so this should not be reached



handling the case that incoming edge is prefix of existing summary


 

 


Johannes Lerch
committed
Nov 27, 2014



handling the case that incoming edge is prefix of existing summary


 

 

handling the case that incoming edge is prefix of existing summary

 

Johannes Lerch
committed
Nov 27, 2014

224

225

226

227
				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	



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
	@Test@Test



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

229
	public void pauseOnOverwrittenFieldOfInterest2() {publicvoidpauseOnOverwrittenFieldOfInterest2(){



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

230

231
		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),



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

235

236

237

238
				normalStmt("a").succ("b", flow("0", "1.f")),normalStmt("a").succ("b",flow("0","1.f")),				callSite("b").calls("bar", flow("1.f", "2.f.g")));callSite("b").calls("bar",flow("1.f","2.f.g")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),normalStmt("c").succ("d",flow("2",writeField("f"),"2^f")),				normalStmt("d").succ("e")); //only interested in 2.f.g, but f excluded so this should not be reachednormalStmt("d").succ("e"));//only interested in 2.f.g, but f excluded so this should not be reached



edges on hold + resume


 

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume


 

 

edges on hold + resume

 

Johannes Lerch
committed
Nov 25, 2014

239

240

241
				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

242
	



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

243

244

245

246

247
	@Test@Test	public void doNotPauseOnOverwrittenFieldOfInterestedPrefix() {publicvoiddoNotPauseOnOverwrittenFieldOfInterestedPrefix(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.f")),normalStmt("a").succ("b",flow("0","1.f")),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

248
				callSite("b").calls("bar", flow("1.f", "2")));callSite("b").calls("bar",flow("1.f","2")));



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

249

250

251

252

253

254

255

256

257
				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f")),normalStmt("c").succ("d",flow("2",writeField("f"),"2^f")),				normalStmt("d").succ("e", kill("2^f"))); normalStmt("d").succ("e",kill("2^f")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

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
	@Test@Test	public void pauseOnTransitiveExclusion() {publicvoidpauseOnTransitiveExclusion(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.f")),normalStmt("a").succ("b",flow("0","1.f")),				callSite("b").calls("bar", flow("1.f", "2.f")));callSite("b").calls("bar",flow("1.f","2.f")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				callSite("c").calls("xyz", flow("2", "3")));callSite("c").calls("xyz",flow("2","3")));				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f")),normalStmt("d").succ("e",flow("3",writeField("f"),"3^f")),				normalStmt("e").succ("f")); normalStmt("e").succ("f"));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void resumePausedOnTransitiveExclusion() {publicvoidresumePausedOnTransitiveExclusion(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.f")),normalStmt("a").succ("b",flow("0","1.f")),				callSite("b").calls("bar", flow("1.f", "2.f")));callSite("b").calls("bar",flow("1.f","2.f")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

286
				callSite("c").calls("xyz", flow("2", "3")));callSite("c").calls("xyz",flow("2","3")));



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

287

288

289

290

291

292

293

294

295
				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("3", writeField("f"), "3^f"), flow("3", "4")),normalStmt("d").succ("e",flow("3",writeField("f"),"3^f"),flow("3","4")),				callSite("e").calls("bar", flow("4", "2.g"), kill("3^f"))); callSite("e").calls("bar",flow("4","2.g"),kill("3^f")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

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
	@Test@Test	public void resumeEdgePausedOnOverwrittenField() {publicvoidresumeEdgePausedOnOverwrittenField(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.f")),normalStmt("a").succ("b",flow("0","1.f")),				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),callSite("b").calls("bar",flow("1.f","2.f")).retSite("e",kill("1.f")),				callSite("e").calls("bar", flow("4", "2.g")).retSite("f", kill("4")));callSite("e").calls("bar",flow("4","2.g")).retSite("f",kill("4")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),normalStmt("c").succ("d",flow("2",writeField("f"),"2^f"),flow("2","3")),



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

307
				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3.g"), kill("2.g" /* 2^f is back substituted to 2.g*/))); exitStmt("d").returns(over("b"),to("e"),flow("3.f","4")).returns(over("e"),to("f"),kill("3.g"),kill("2.g"/* 2^f is back substituted to 2.g*/)));



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

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
				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void resumeEdgePausedOnOverwrittenFieldForPrefixes() {publicvoidresumeEdgePausedOnOverwrittenFieldForPrefixes(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.f")),normalStmt("a").succ("b",flow("0","1.f")),				callSite("b").calls("bar", flow("1.f", "2.f")).retSite("e", kill("1.f")),callSite("b").calls("bar",flow("1.f","2.f")).retSite("e",kill("1.f")),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

318
				callSite("e").calls("bar", flow("4", "2")).retSite("f", kill("4")));callSite("e").calls("bar",flow("4","2")).retSite("f",kill("4")));



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

319

320

321

322
				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("d", flow("2", writeField("f"), "2^f"), flow("2", "3")),normalStmt("c").succ("d",flow("2",writeField("f"),"2^f"),flow("2","3")),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

323
				exitStmt("d").returns(over("b"), to("e"), flow("3.f", "4")).returns(over("e"), to("f"), kill("3"), kill("2^f"))); exitStmt("d").returns(over("b"),to("e"),flow("3.f","4")).returns(over("e"),to("f"),kill("3"),kill("2^f")));



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

324

325

326

327
				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

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
	@Test@Test	public void exclusionOnPotentiallyInterestedCaller() {publicvoidexclusionOnPotentiallyInterestedCaller(){		helper.method("foo",helper.method("foo",				startPoints("sp"),startPoints("sp"),				normalStmt("sp").succ("a", flow("0", "1")),normalStmt("sp").succ("a",flow("0","1")),				callSite("a").calls("bar", flow("1", "1^f")).retSite("d", kill("1")));callSite("a").calls("bar",flow("1","1^f")).retSite("d",kill("1")));				helper.method("bar",helper.method("bar",				startPoints("b"),startPoints("b"),				normalStmt("b").succ("c", flow("1", readField("f"), "2.f")),normalStmt("b").succ("c",flow("1",readField("f"),"2.f")),				exitStmt("c").returns(over("a"), to("d")));exitStmt("c").returns(over("a"),to("d")));				helper.runSolver(false, "sp");helper.runSolver(false,"sp");	}}	



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

343

344

345

346

347

348

349
	@Test@Test	public void registerPausedEdgeInLateCallers() {publicvoidregisterPausedEdgeInLateCallers(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.g")),normalStmt("a").succ("b",flow("0","1.g")),				callSite("b").calls("bar", flow("1.g", "1.g")).retSite("e", kill("1.g")),callSite("b").calls("bar",flow("1.g","1.g")).retSite("e",kill("1.g")),				normalStmt("e").succ("f", flow("1.g", "3")),normalStmt("e").succ("f",flow("1.g","3")),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

350
				callSite("f").calls("bar", flow("3", "1")).retSite("g", kill("3"))); callSite("f").calls("bar",flow("3","1")).retSite("g",kill("3")));



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

351

352

353

354
				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("d", flow("1", readField("f"), "2"), flow("1", "1")),normalStmt("c").succ("d",flow("1",readField("f"),"2"),flow("1","1")),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

355
				exitStmt("d").returns(over("b"), to("e"), flow("1.g", "1.g") /* ignore fact 2, not possible with this caller ctx*/).returns(over("f"), to("g"), kill("1"), kill("2")));exitStmt("d").returns(over("b"),to("e"),flow("1.g","1.g")/* ignore fact 2, not possible with this caller ctx*/).returns(over("f"),to("g"),kill("1"),kill("2")));



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
				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void mergeExcludedField() {publicvoidmergeExcludedField(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				normalStmt("b").succ("c", flow("1", "2", "2^f")),normalStmt("b").succ("c",flow("1","2","2^f")),				normalStmt("c").succ("d", kill("2")));normalStmt("c").succ("d",kill("2")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void resumeOnTransitiveInterestedCaller() {publicvoidresumeOnTransitiveInterestedCaller(){		helper.method("foo",helper.method("foo",				startPoints("sp"),startPoints("sp"),				normalStmt("sp").succ("a", flow("0", "1.f")),normalStmt("sp").succ("a",flow("0","1.f")),				callSite("a").calls("bar", flow("1.f", "1.f")).retSite("f", kill("1.f")),callSite("a").calls("bar",flow("1.f","1.f")).retSite("f",kill("1.f")),				callSite("f").calls("bar", flow("2", "1.g")));callSite("f").calls("bar",flow("2","1.g")));						helper.method("bar",helper.method("bar",				startPoints("b"),startPoints("b"),



"interest/concrretization" edges in callers are no longer propagated as


 

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as


 

 

"interest/concrretization" edges in callers are no longer propagated as

 

Johannes Lerch
committed
Jan 07, 2015

381
				callSite("b").calls("xyz", flow("1", "1")).retSite("e", kill("1")),callSite("b").calls("xyz",flow("1","1")).retSite("e",kill("1")),



Bug/test fixes


 

 


Johannes Lerch
committed
Dec 10, 2014



Bug/test fixes


 

 

Bug/test fixes

 

Johannes Lerch
committed
Dec 10, 2014

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
				exitStmt("e").returns(over("a"), to("f"), flow("2", "2")));exitStmt("e").returns(over("a"),to("f"),flow("2","2")));				helper.method("xyz",helper.method("xyz",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("d", flow("1", readField("g"), "3"), flow("1", readField("f"), "2")),normalStmt("c").succ("d",flow("1",readField("g"),"3"),flow("1",readField("f"),"2")),				exitStmt("d").returns(over("b"), to("e"), flow("2", "2"), kill("3")));exitStmt("d").returns(over("b"),to("e"),flow("2","2"),kill("3")));								helper.runSolver(false, "sp");helper.runSolver(false,"sp");	}}	



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

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
	@Test@Test	public void happyPath() {publicvoidhappyPath(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "x")),normalStmt("a").succ("b",flow("0","x")),				normalStmt("b").succ("c", flow("x", "x")),normalStmt("b").succ("c",flow("x","x")),				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));callSite("c").calls("foo",flow("x","y")).retSite("f",flow("x","x")));				helper.method("foo",helper.method("foo",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("y", "y", "z")),normalStmt("d").succ("e",flow("y","y","z")),				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));exitStmt("e").returns(over("c"),to("f"),flow("z","u"),flow("y")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void reuseSummary() {publicvoidreuseSummary(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),callSite("a").calls("bar",flow("0","x")).retSite("b",flow("0","y")),				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),callSite("b").calls("bar",flow("y","x")).retSite("c",flow("y")),				normalStmt("c").succ("c0", flow("w", "0")));normalStmt("c").succ("c0",flow("w","0")));				helper.method("bar",helper.method("bar",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("x", "z")),normalStmt("d").succ("e",flow("x","z")),				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))exitStmt("e").returns(over("a"),to("b"),flow("z","y"))							  .returns(over("b"), to("c"), flow("z", "w")));.returns(over("b"),to("c"),flow("z","w")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void reuseSummaryForRecursiveCall() {publicvoidreuseSummaryForRecursiveCall(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),callSite("a").calls("bar",flow("0","1")).retSite("b",flow("0")),				normalStmt("b").succ("c", flow("2", "3")));normalStmt("b").succ("c",flow("2","3")));				helper.method("bar",helper.method("bar",				startPoints("g"),startPoints("g"),				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),normalStmt("g").succ("h",flow("1","1")).succ("i",flow("1","1")),				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),callSite("i").calls("bar",flow("1","1")).retSite("h",flow("1")),				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))exitStmt("h").returns(over("a"),to("b"),flow("1"),flow("2","2"))							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));.returns(over("i"),to("h"),flow("1","2"),flow("2","2")));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void branch() {publicvoidbranch(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),normalStmt("a").succ("b1",flow("0","x")).succ("b2",flow("0","x")),				normalStmt("b1").succ("c", flow("x", "x", "y")),normalStmt("b1").succ("c",flow("x","x","y")),				normalStmt("b2").succ("c", flow("x", "x")),normalStmt("b2").succ("c",flow("x","x")),				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),normalStmt("c").succ("d",flow("x","z"),flow("y","w")),				normalStmt("d").succ("e", flow("z"), flow("w")));normalStmt("d").succ("e",flow("z"),flow("w")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void unbalancedReturn() {publicvoidunbalancedReturn(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));exitStmt("b").returns(over("x"),to("y"),flow("1","1")));				helper.method("bar", helper.method("bar",				startPoints("unused"),startPoints("unused"),				normalStmt("y").succ("z", flow("1", "2")));normalStmt("y").succ("z",flow("1","2")));				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void artificalReturnEdgeForNoCallersCase() {publicvoidartificalReturnEdgeForNoCallersCase(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				exitStmt("b").returns(null, null, flow("1", "1")));exitStmt("b").returns(null,null,flow("1","1")));				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}	



k-limitting; fix in constraint propagation


 

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation


 

 

k-limitting; fix in constraint propagation

 

Johannes Lerch
committed
Jan 14, 2015

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
	@Test@Test	public void pauseEdgeMutuallyRecursiveCallers() {publicvoidpauseEdgeMutuallyRecursiveCallers(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.x")),normalStmt("a").succ("b",flow("0","1.x")),				callSite("b").calls("bar",flow("1.x", "2.x")));callSite("b").calls("bar",flow("1.x","2.x")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				callSite("c").calls("xyz", flow("2", "3")));callSite("c").calls("xyz",flow("2","3")));				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),				callSite("d").calls("bar", flow("3", "2")).retSite("e", flow("3", "3")),callSite("d").calls("bar",flow("3","2")).retSite("e",flow("3","3")),				normalStmt("e").succ("f", flow("3", readField("f"), "4")));normalStmt("e").succ("f",flow("3",readField("f"),"4")));						helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void pauseDiamondShapedCallerChain() {publicvoidpauseDiamondShapedCallerChain(){		helper.method("bar",helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.x")),normalStmt("a").succ("b",flow("0","1.x")),				callSite("b").calls("foo1", flow("1.x", "2.x")).calls("foo2", flow("1.x", "2.x")));callSite("b").calls("foo1",flow("1.x","2.x")).calls("foo2",flow("1.x","2.x")));				helper.method("foo1",helper.method("foo1",				startPoints("c1"),startPoints("c1"),				callSite("c1").calls("xyz", flow("2", "3")));callSite("c1").calls("xyz",flow("2","3")));				helper.method("foo2",helper.method("foo2",				startPoints("c2"),startPoints("c2"),				callSite("c2").calls("xyz", flow("2", "3")));callSite("c2").calls("xyz",flow("2","3")));				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("3", readField("f"), "4")),normalStmt("d").succ("e",flow("3",readField("f"),"4")),				normalStmt("e").succ("f"));normalStmt("e").succ("f"));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}	@Test@Test	public void dontPauseDiamondShapedCallerChain() {publicvoiddontPauseDiamondShapedCallerChain(){		helper.method("bar",helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1.x")),normalStmt("a").succ("b",flow("0","1.x")),				callSite("b").calls("foo1", flow("1.x", "2.f")).calls("foo2", flow("1.x", "2.f")));callSite("b").calls("foo1",flow("1.x","2.f")).calls("foo2",flow("1.x","2.f")));				helper.method("foo1",helper.method("foo1",				startPoints("c1"),startPoints("c1"),				callSite("c1").calls("xyz", flow("2", "3")));callSite("c1").calls("xyz",flow("2","3")));				helper.method("foo2",helper.method("foo2",				startPoints("c2"),startPoints("c2"),				callSite("c2").calls("xyz", flow("2", "3")));callSite("c2").calls("xyz",flow("2","3")));				helper.method("xyz",helper.method("xyz",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("3", readField("f"), "4")),normalStmt("d").succ("e",flow("3",readField("f"),"4")),				normalStmt("e").succ("f", kill("4")));normalStmt("e").succ("f",kill("4")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void correctDeltaConstraintApplication() {publicvoidcorrectDeltaConstraintApplication(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				callSite("b").calls("bar", flow("1", "1")));callSite("b").calls("bar",flow("1","1")));				helper.method("bar",helper.method("bar",				startPoints("c"),startPoints("c"),				normalStmt("c").succ("d", flow("1", writeField("a"), "1^a")),normalStmt("c").succ("d",flow("1",writeField("a"),"1^a")),				callSite("d").calls("xyz", flow("1^a", "1^a")));callSite("d").calls("xyz",flow("1^a","1^a")));				helper.method("xyz",helper.method("xyz",				startPoints("e"),startPoints("e"),				normalStmt("e").succ("f", flow("1", readField("f"), "2")),normalStmt("e").succ("f",flow("1",readField("f"),"2")),				callSite("f").calls("baz", flow("2", "3")));callSite("f").calls("baz",flow("2","3")));				helper.method("baz",helper.method("baz",				startPoints("g"),startPoints("g"),				normalStmt("g").succ("h", flow("3", readField("a"), "4")));normalStmt("g").succ("h",flow("3",readField("a"),"4")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}



FieldSensitiveIFDSSolver - concept outline



 


Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline



 

FieldSensitiveIFDSSolver - concept outline


Johannes Späth
committed
Oct 15, 2014

568
}}





