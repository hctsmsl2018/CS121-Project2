



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

6ebef3f50f9f851afb1f54531b3d056eee65f275

















6ebef3f50f9f851afb1f54531b3d056eee65f275


Switch branch/tag










heros


test


heros


alias


AccessPathUtilTest.java



Find file
Normal viewHistoryPermalink






AccessPathUtilTest.java



5.03 KB









Newer










Older









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






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
 * Copyright (c) 2014 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






13




14




import static heros.alias.AccessPathUtil.applyAbstractedSummary;
import static heros.alias.AccessPathUtil.isPrefixOf;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






15




16




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






17




import static org.junit.Assert.assertNull;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






18




19




import static org.junit.Assert.assertTrue;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






20




import org.junit.Assert;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






21




22




23




24




25




26




import org.junit.Test;

public class AccessPathUtilTest {

	@Test
	public void testBaseValuePrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






27




28




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a.f")));
		assertFalse(isPrefixOf(new Fact("a.f"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






29




30




31




32




	}
	
	@Test
	public void testBaseValueIdentity() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






33




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






34




35




36




37




	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertTrue(isPrefixOf(new Fact("a.b"), new Fact("a.b.c")));
		assertFalse(isPrefixOf(new Fact("a.b.c"), new Fact("a.b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g")));
		assertFalse(isPrefixOf(new Fact("a.g"), new Fact("a^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






56




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






57




58




59




60




	}
	
	@Test
	public void testMixedFieldAccess() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






61




62




		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.g")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a.f.h")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






63




64




65




66




67




		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






68




69




		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^g")));









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






70




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^f,g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






71




72




73




74




75




76




77




78




	}

	@Test
	public void testDifferentAccessPathLength() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.h")));
	}
	
	@Test









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






79




80




81




82




83




84




85




86




87




	public void testExclusionRequiresFieldAccess() {
		assertTrue(isPrefixOf(new Fact("a"), new Fact("a^f")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
		assertFalse(isPrefixOf(new Fact("a.f^g"), new Fact("a.f")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g^h")));
		assertFalse(isPrefixOf(new Fact("a.f^g^h"), new Fact("a.f")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






88




89




90




91




	}
	
	@Test
	public void testAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




		assertEquals(new Fact("z.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






93




94




95




96




	}
	
	@Test
	public void testAbstractedFieldAccessSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






97




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






98




99




100




101




	}
	
	@Test
	public void testSummaryIntroducesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






102




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.c"), new SummaryEdge<>(new Fact("a"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






103




104




105




106




	}
	
	@Test
	public void testSummaryRemovesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






107




		assertEquals(new Fact("z.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






108




109




110




111




	}
	
	@Test
	public void testNonAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertEquals(new Fact("z"), applyAbstractedSummary(new Fact("a"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());
	}
	
	@Test
	public void testSummaryWithExcludedField() {
		assertEquals(new Fact("a.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testSummaryWithMultipleExcludedFields() {
		assertEquals(new Fact("a.f^h,i"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
		assertEquals(new Fact("a.f.f"), applyAbstractedSummary(new Fact("a.f.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
	}
	
	@Test
	public void testIdentityForExclusions() {
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a"))).get());
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).get());
	}
	
	@Test
	public void testMergeExclusions() {
		assertEquals(new Fact("a^f,g"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testNullOnImpossibleSubsumption() {
		assertFalse(applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).isPresent());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






140




141




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

6ebef3f50f9f851afb1f54531b3d056eee65f275

















6ebef3f50f9f851afb1f54531b3d056eee65f275


Switch branch/tag










heros


test


heros


alias


AccessPathUtilTest.java



Find file
Normal viewHistoryPermalink






AccessPathUtilTest.java



5.03 KB









Newer










Older









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






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
 * Copyright (c) 2014 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






13




14




import static heros.alias.AccessPathUtil.applyAbstractedSummary;
import static heros.alias.AccessPathUtil.isPrefixOf;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






15




16




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






17




import static org.junit.Assert.assertNull;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






18




19




import static org.junit.Assert.assertTrue;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






20




import org.junit.Assert;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






21




22




23




24




25




26




import org.junit.Test;

public class AccessPathUtilTest {

	@Test
	public void testBaseValuePrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






27




28




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a.f")));
		assertFalse(isPrefixOf(new Fact("a.f"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






29




30




31




32




	}
	
	@Test
	public void testBaseValueIdentity() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






33




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






34




35




36




37




	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertTrue(isPrefixOf(new Fact("a.b"), new Fact("a.b.c")));
		assertFalse(isPrefixOf(new Fact("a.b.c"), new Fact("a.b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g")));
		assertFalse(isPrefixOf(new Fact("a.g"), new Fact("a^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






56




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






57




58




59




60




	}
	
	@Test
	public void testMixedFieldAccess() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






61




62




		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.g")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a.f.h")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






63




64




65




66




67




		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






68




69




		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^g")));









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






70




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^f,g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






71




72




73




74




75




76




77




78




	}

	@Test
	public void testDifferentAccessPathLength() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.h")));
	}
	
	@Test









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






79




80




81




82




83




84




85




86




87




	public void testExclusionRequiresFieldAccess() {
		assertTrue(isPrefixOf(new Fact("a"), new Fact("a^f")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
		assertFalse(isPrefixOf(new Fact("a.f^g"), new Fact("a.f")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g^h")));
		assertFalse(isPrefixOf(new Fact("a.f^g^h"), new Fact("a.f")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






88




89




90




91




	}
	
	@Test
	public void testAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




		assertEquals(new Fact("z.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






93




94




95




96




	}
	
	@Test
	public void testAbstractedFieldAccessSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






97




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






98




99




100




101




	}
	
	@Test
	public void testSummaryIntroducesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






102




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.c"), new SummaryEdge<>(new Fact("a"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






103




104




105




106




	}
	
	@Test
	public void testSummaryRemovesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






107




		assertEquals(new Fact("z.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






108




109




110




111




	}
	
	@Test
	public void testNonAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertEquals(new Fact("z"), applyAbstractedSummary(new Fact("a"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());
	}
	
	@Test
	public void testSummaryWithExcludedField() {
		assertEquals(new Fact("a.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testSummaryWithMultipleExcludedFields() {
		assertEquals(new Fact("a.f^h,i"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
		assertEquals(new Fact("a.f.f"), applyAbstractedSummary(new Fact("a.f.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
	}
	
	@Test
	public void testIdentityForExclusions() {
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a"))).get());
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).get());
	}
	
	@Test
	public void testMergeExclusions() {
		assertEquals(new Fact("a^f,g"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testNullOnImpossibleSubsumption() {
		assertFalse(applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).isPresent());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






140




141




	}
}











Open sidebar



Joshua Garcia heros

6ebef3f50f9f851afb1f54531b3d056eee65f275







Open sidebar



Joshua Garcia heros

6ebef3f50f9f851afb1f54531b3d056eee65f275




Open sidebar

Joshua Garcia heros

6ebef3f50f9f851afb1f54531b3d056eee65f275


Joshua Garciaherosheros
6ebef3f50f9f851afb1f54531b3d056eee65f275










6ebef3f50f9f851afb1f54531b3d056eee65f275


Switch branch/tag










heros


test


heros


alias


AccessPathUtilTest.java



Find file
Normal viewHistoryPermalink






AccessPathUtilTest.java



5.03 KB









Newer










Older









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






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
 * Copyright (c) 2014 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






13




14




import static heros.alias.AccessPathUtil.applyAbstractedSummary;
import static heros.alias.AccessPathUtil.isPrefixOf;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






15




16




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






17




import static org.junit.Assert.assertNull;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






18




19




import static org.junit.Assert.assertTrue;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






20




import org.junit.Assert;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






21




22




23




24




25




26




import org.junit.Test;

public class AccessPathUtilTest {

	@Test
	public void testBaseValuePrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






27




28




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a.f")));
		assertFalse(isPrefixOf(new Fact("a.f"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






29




30




31




32




	}
	
	@Test
	public void testBaseValueIdentity() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






33




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






34




35




36




37




	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertTrue(isPrefixOf(new Fact("a.b"), new Fact("a.b.c")));
		assertFalse(isPrefixOf(new Fact("a.b.c"), new Fact("a.b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g")));
		assertFalse(isPrefixOf(new Fact("a.g"), new Fact("a^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






56




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






57




58




59




60




	}
	
	@Test
	public void testMixedFieldAccess() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






61




62




		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.g")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a.f.h")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






63




64




65




66




67




		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






68




69




		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^g")));









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






70




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^f,g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






71




72




73




74




75




76




77




78




	}

	@Test
	public void testDifferentAccessPathLength() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.h")));
	}
	
	@Test









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






79




80




81




82




83




84




85




86




87




	public void testExclusionRequiresFieldAccess() {
		assertTrue(isPrefixOf(new Fact("a"), new Fact("a^f")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
		assertFalse(isPrefixOf(new Fact("a.f^g"), new Fact("a.f")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g^h")));
		assertFalse(isPrefixOf(new Fact("a.f^g^h"), new Fact("a.f")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






88




89




90




91




	}
	
	@Test
	public void testAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




		assertEquals(new Fact("z.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






93




94




95




96




	}
	
	@Test
	public void testAbstractedFieldAccessSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






97




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






98




99




100




101




	}
	
	@Test
	public void testSummaryIntroducesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






102




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.c"), new SummaryEdge<>(new Fact("a"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






103




104




105




106




	}
	
	@Test
	public void testSummaryRemovesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






107




		assertEquals(new Fact("z.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






108




109




110




111




	}
	
	@Test
	public void testNonAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertEquals(new Fact("z"), applyAbstractedSummary(new Fact("a"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());
	}
	
	@Test
	public void testSummaryWithExcludedField() {
		assertEquals(new Fact("a.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testSummaryWithMultipleExcludedFields() {
		assertEquals(new Fact("a.f^h,i"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
		assertEquals(new Fact("a.f.f"), applyAbstractedSummary(new Fact("a.f.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
	}
	
	@Test
	public void testIdentityForExclusions() {
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a"))).get());
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).get());
	}
	
	@Test
	public void testMergeExclusions() {
		assertEquals(new Fact("a^f,g"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testNullOnImpossibleSubsumption() {
		assertFalse(applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).isPresent());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






140




141




	}
}














6ebef3f50f9f851afb1f54531b3d056eee65f275


Switch branch/tag










heros


test


heros


alias


AccessPathUtilTest.java



Find file
Normal viewHistoryPermalink






AccessPathUtilTest.java



5.03 KB









Newer










Older









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






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
 * Copyright (c) 2014 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






13




14




import static heros.alias.AccessPathUtil.applyAbstractedSummary;
import static heros.alias.AccessPathUtil.isPrefixOf;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






15




16




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






17




import static org.junit.Assert.assertNull;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






18




19




import static org.junit.Assert.assertTrue;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






20




import org.junit.Assert;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






21




22




23




24




25




26




import org.junit.Test;

public class AccessPathUtilTest {

	@Test
	public void testBaseValuePrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






27




28




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a.f")));
		assertFalse(isPrefixOf(new Fact("a.f"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






29




30




31




32




	}
	
	@Test
	public void testBaseValueIdentity() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






33




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






34




35




36




37




	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertTrue(isPrefixOf(new Fact("a.b"), new Fact("a.b.c")));
		assertFalse(isPrefixOf(new Fact("a.b.c"), new Fact("a.b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g")));
		assertFalse(isPrefixOf(new Fact("a.g"), new Fact("a^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






56




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






57




58




59




60




	}
	
	@Test
	public void testMixedFieldAccess() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






61




62




		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.g")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a.f.h")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






63




64




65




66




67




		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






68




69




		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^g")));









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






70




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^f,g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






71




72




73




74




75




76




77




78




	}

	@Test
	public void testDifferentAccessPathLength() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.h")));
	}
	
	@Test









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






79




80




81




82




83




84




85




86




87




	public void testExclusionRequiresFieldAccess() {
		assertTrue(isPrefixOf(new Fact("a"), new Fact("a^f")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
		assertFalse(isPrefixOf(new Fact("a.f^g"), new Fact("a.f")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g^h")));
		assertFalse(isPrefixOf(new Fact("a.f^g^h"), new Fact("a.f")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






88




89




90




91




	}
	
	@Test
	public void testAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




		assertEquals(new Fact("z.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






93




94




95




96




	}
	
	@Test
	public void testAbstractedFieldAccessSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






97




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






98




99




100




101




	}
	
	@Test
	public void testSummaryIntroducesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






102




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.c"), new SummaryEdge<>(new Fact("a"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






103




104




105




106




	}
	
	@Test
	public void testSummaryRemovesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






107




		assertEquals(new Fact("z.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






108




109




110




111




	}
	
	@Test
	public void testNonAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertEquals(new Fact("z"), applyAbstractedSummary(new Fact("a"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());
	}
	
	@Test
	public void testSummaryWithExcludedField() {
		assertEquals(new Fact("a.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testSummaryWithMultipleExcludedFields() {
		assertEquals(new Fact("a.f^h,i"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
		assertEquals(new Fact("a.f.f"), applyAbstractedSummary(new Fact("a.f.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
	}
	
	@Test
	public void testIdentityForExclusions() {
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a"))).get());
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).get());
	}
	
	@Test
	public void testMergeExclusions() {
		assertEquals(new Fact("a^f,g"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testNullOnImpossibleSubsumption() {
		assertFalse(applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).isPresent());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






140




141




	}
}










6ebef3f50f9f851afb1f54531b3d056eee65f275


Switch branch/tag










heros


test


heros


alias


AccessPathUtilTest.java



Find file
Normal viewHistoryPermalink




6ebef3f50f9f851afb1f54531b3d056eee65f275


Switch branch/tag










heros


test


heros


alias


AccessPathUtilTest.java





6ebef3f50f9f851afb1f54531b3d056eee65f275


Switch branch/tag








6ebef3f50f9f851afb1f54531b3d056eee65f275


Switch branch/tag





6ebef3f50f9f851afb1f54531b3d056eee65f275

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

alias

AccessPathUtilTest.java
Find file
Normal viewHistoryPermalink




AccessPathUtilTest.java



5.03 KB









Newer










Older









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






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
 * Copyright (c) 2014 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






13




14




import static heros.alias.AccessPathUtil.applyAbstractedSummary;
import static heros.alias.AccessPathUtil.isPrefixOf;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






15




16




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






17




import static org.junit.Assert.assertNull;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






18




19




import static org.junit.Assert.assertTrue;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






20




import org.junit.Assert;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






21




22




23




24




25




26




import org.junit.Test;

public class AccessPathUtilTest {

	@Test
	public void testBaseValuePrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






27




28




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a.f")));
		assertFalse(isPrefixOf(new Fact("a.f"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






29




30




31




32




	}
	
	@Test
	public void testBaseValueIdentity() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






33




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






34




35




36




37




	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertTrue(isPrefixOf(new Fact("a.b"), new Fact("a.b.c")));
		assertFalse(isPrefixOf(new Fact("a.b.c"), new Fact("a.b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g")));
		assertFalse(isPrefixOf(new Fact("a.g"), new Fact("a^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






56




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






57




58




59




60




	}
	
	@Test
	public void testMixedFieldAccess() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






61




62




		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.g")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a.f.h")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






63




64




65




66




67




		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






68




69




		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^g")));









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






70




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^f,g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






71




72




73




74




75




76




77




78




	}

	@Test
	public void testDifferentAccessPathLength() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.h")));
	}
	
	@Test









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






79




80




81




82




83




84




85




86




87




	public void testExclusionRequiresFieldAccess() {
		assertTrue(isPrefixOf(new Fact("a"), new Fact("a^f")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
		assertFalse(isPrefixOf(new Fact("a.f^g"), new Fact("a.f")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g^h")));
		assertFalse(isPrefixOf(new Fact("a.f^g^h"), new Fact("a.f")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






88




89




90




91




	}
	
	@Test
	public void testAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




		assertEquals(new Fact("z.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






93




94




95




96




	}
	
	@Test
	public void testAbstractedFieldAccessSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






97




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






98




99




100




101




	}
	
	@Test
	public void testSummaryIntroducesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






102




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.c"), new SummaryEdge<>(new Fact("a"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






103




104




105




106




	}
	
	@Test
	public void testSummaryRemovesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






107




		assertEquals(new Fact("z.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






108




109




110




111




	}
	
	@Test
	public void testNonAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertEquals(new Fact("z"), applyAbstractedSummary(new Fact("a"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());
	}
	
	@Test
	public void testSummaryWithExcludedField() {
		assertEquals(new Fact("a.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testSummaryWithMultipleExcludedFields() {
		assertEquals(new Fact("a.f^h,i"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
		assertEquals(new Fact("a.f.f"), applyAbstractedSummary(new Fact("a.f.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
	}
	
	@Test
	public void testIdentityForExclusions() {
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a"))).get());
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).get());
	}
	
	@Test
	public void testMergeExclusions() {
		assertEquals(new Fact("a^f,g"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testNullOnImpossibleSubsumption() {
		assertFalse(applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).isPresent());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






140




141




	}
}








AccessPathUtilTest.java



5.03 KB










AccessPathUtilTest.java



5.03 KB









Newer










Older
NewerOlder







use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






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
 * Copyright (c) 2014 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros.alias;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






13




14




import static heros.alias.AccessPathUtil.applyAbstractedSummary;
import static heros.alias.AccessPathUtil.isPrefixOf;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






15




16




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






17




import static org.junit.Assert.assertNull;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






18




19




import static org.junit.Assert.assertTrue;










handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






20




import org.junit.Assert;









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






21




22




23




24




25




26




import org.junit.Test;

public class AccessPathUtilTest {

	@Test
	public void testBaseValuePrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






27




28




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a.f")));
		assertFalse(isPrefixOf(new Fact("a.f"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






29




30




31




32




	}
	
	@Test
	public void testBaseValueIdentity() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






33




		assertTrue(isPrefixOf(new Fact("a"), new Fact("a")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






34




35




36




37




	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertTrue(isPrefixOf(new Fact("a.b"), new Fact("a.b.c")));
		assertFalse(isPrefixOf(new Fact("a.b.c"), new Fact("a.b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g")));
		assertFalse(isPrefixOf(new Fact("a.g"), new Fact("a^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






56




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






57




58




59




60




	}
	
	@Test
	public void testMixedFieldAccess() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






61




62




		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.g")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a.f.h")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






63




64




65




66




67




		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






68




69




		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f")));
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^g")));









changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015






70




		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^f,g")));









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






71




72




73




74




75




76




77




78




	}

	@Test
	public void testDifferentAccessPathLength() {
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.h")));
	}
	
	@Test









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






79




80




81




82




83




84




85




86




87




	public void testExclusionRequiresFieldAccess() {
		assertTrue(isPrefixOf(new Fact("a"), new Fact("a^f")));
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));
		assertFalse(isPrefixOf(new Fact("a.f^g"), new Fact("a.f")));
		
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g^h")));
		assertFalse(isPrefixOf(new Fact("a.f^g^h"), new Fact("a.f")));









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






88




89




90




91




	}
	
	@Test
	public void testAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






92




		assertEquals(new Fact("z.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






93




94




95




96




	}
	
	@Test
	public void testAbstractedFieldAccessSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






97




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






98




99




100




101




	}
	
	@Test
	public void testSummaryIntroducesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






102




		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.c"), new SummaryEdge<>(new Fact("a"), null, new Fact("z.b"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






103




104




105




106




	}
	
	@Test
	public void testSummaryRemovesFieldAccess() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






107




		assertEquals(new Fact("z.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z"))).get());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






108




109




110




111




	}
	
	@Test
	public void testNonAbstractedSummary() {









handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015






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




		assertEquals(new Fact("z"), applyAbstractedSummary(new Fact("a"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());
	}
	
	@Test
	public void testSummaryWithExcludedField() {
		assertEquals(new Fact("a.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testSummaryWithMultipleExcludedFields() {
		assertEquals(new Fact("a.f^h,i"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
		assertEquals(new Fact("a.f.f"), applyAbstractedSummary(new Fact("a.f.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());
	}
	
	@Test
	public void testIdentityForExclusions() {
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a"))).get());
		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).get());
	}
	
	@Test
	public void testMergeExclusions() {
		assertEquals(new Fact("a^f,g"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());
	}
	
	@Test
	public void testNullOnImpossibleSubsumption() {
		assertFalse(applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).isPresent());









use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014






140




141




	}
}







use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Eric Bodden. * Copyright (c) 2014 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

13

14
import static heros.alias.AccessPathUtil.applyAbstractedSummary;importstaticheros.alias.AccessPathUtil.applyAbstractedSummary;import static heros.alias.AccessPathUtil.isPrefixOf;importstaticheros.alias.AccessPathUtil.isPrefixOf;



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

15

16
import static org.junit.Assert.assertEquals;importstaticorg.junit.Assert.assertEquals;import static org.junit.Assert.assertFalse;importstaticorg.junit.Assert.assertFalse;



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

17
import static org.junit.Assert.assertNull;importstaticorg.junit.Assert.assertNull;



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

18

19
import static org.junit.Assert.assertTrue;importstaticorg.junit.Assert.assertTrue;



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

20
import org.junit.Assert;importorg.junit.Assert;



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

21

22

23

24

25

26
import org.junit.Test;importorg.junit.Test;public class AccessPathUtilTest {publicclassAccessPathUtilTest{	@Test@Test	public void testBaseValuePrefixOfFieldAccess() {publicvoidtestBaseValuePrefixOfFieldAccess(){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

27

28
		assertTrue(isPrefixOf(new Fact("a"), new Fact("a.f")));assertTrue(isPrefixOf(newFact("a"),newFact("a.f")));		assertFalse(isPrefixOf(new Fact("a.f"), new Fact("a")));assertFalse(isPrefixOf(newFact("a.f"),newFact("a")));



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

29

30

31

32
	}}		@Test@Test	public void testBaseValueIdentity() {publicvoidtestBaseValueIdentity(){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

33
		assertTrue(isPrefixOf(new Fact("a"), new Fact("a")));assertTrue(isPrefixOf(newFact("a"),newFact("a")));



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

34

35

36

37
	}}		@Test@Test	public void testFieldAccessPrefixOfFieldAccess() {publicvoidtestFieldAccessPrefixOfFieldAccess(){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

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
		assertTrue(isPrefixOf(new Fact("a.b"), new Fact("a.b.c")));assertTrue(isPrefixOf(newFact("a.b"),newFact("a.b.c")));		assertFalse(isPrefixOf(new Fact("a.b.c"), new Fact("a.b")));assertFalse(isPrefixOf(newFact("a.b.c"),newFact("a.b")));	}}		@Test@Test	public void testPrefixOfFieldAccessWithExclusion() {publicvoidtestPrefixOfFieldAccessWithExclusion(){		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g")));assertTrue(isPrefixOf(newFact("a^f"),newFact("a.g")));		assertFalse(isPrefixOf(new Fact("a.g"), new Fact("a^f")));assertFalse(isPrefixOf(newFact("a.g"),newFact("a^f")));	}}		@Test@Test	public void testIdentityWithExclusion() {publicvoidtestIdentityWithExclusion(){		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a^f")));assertTrue(isPrefixOf(newFact("a^f"),newFact("a^f")));		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f,g")));assertTrue(isPrefixOf(newFact("a^f,g"),newFact("a^f,g")));	}}		@Test@Test	public void testDifferentExclusions() {publicvoidtestDifferentExclusions(){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

56
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^g")));assertFalse(isPrefixOf(newFact("a^f"),newFact("a^g")));



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

57

58

59

60
	}}		@Test@Test	public void testMixedFieldAccess() {publicvoidtestMixedFieldAccess(){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

61

62
		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.g")));assertTrue(isPrefixOf(newFact("a^f"),newFact("a.g.g")));		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a.f.h")));assertFalse(isPrefixOf(newFact("a^f"),newFact("a.f.h")));



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

63

64

65

66

67
		assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));assertTrue(isPrefixOf(newFact("a.f"),newFact("a.f^g")));	}}		@Test@Test	public void testMultipleExclusions() {publicvoidtestMultipleExclusions(){



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

68

69
		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^f")));assertTrue(isPrefixOf(newFact("a^f,g"),newFact("a^f")));		assertTrue(isPrefixOf(new Fact("a^f,g"), new Fact("a^g")));assertTrue(isPrefixOf(newFact("a^f,g"),newFact("a^g")));



changed to single threaded; bugfix


 

 


Johannes Lerch
committed
Jan 15, 2015



changed to single threaded; bugfix


 

 

changed to single threaded; bugfix

 

Johannes Lerch
committed
Jan 15, 2015

70
		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a^f,g")));assertFalse(isPrefixOf(newFact("a^f"),newFact("a^f,g")));



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

71

72

73

74

75

76

77

78
	}}	@Test@Test	public void testDifferentAccessPathLength() {publicvoidtestDifferentAccessPathLength(){		assertTrue(isPrefixOf(new Fact("a^f"), new Fact("a.g.h")));assertTrue(isPrefixOf(newFact("a^f"),newFact("a.g.h")));	}}		@Test@Test



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

79

80

81

82

83

84

85

86

87
	public void testExclusionRequiresFieldAccess() {publicvoidtestExclusionRequiresFieldAccess(){		assertTrue(isPrefixOf(new Fact("a"), new Fact("a^f")));assertTrue(isPrefixOf(newFact("a"),newFact("a^f")));		assertFalse(isPrefixOf(new Fact("a^f"), new Fact("a")));assertFalse(isPrefixOf(newFact("a^f"),newFact("a")));				assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g")));assertTrue(isPrefixOf(newFact("a.f"),newFact("a.f^g")));		assertFalse(isPrefixOf(new Fact("a.f^g"), new Fact("a.f")));assertFalse(isPrefixOf(newFact("a.f^g"),newFact("a.f")));				assertTrue(isPrefixOf(new Fact("a.f"), new Fact("a.f^g^h")));assertTrue(isPrefixOf(newFact("a.f"),newFact("a.f^g^h")));		assertFalse(isPrefixOf(new Fact("a.f^g^h"), new Fact("a.f")));assertFalse(isPrefixOf(newFact("a.f^g^h"),newFact("a.f")));



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

88

89

90

91
	}}		@Test@Test	public void testAbstractedSummary() {publicvoidtestAbstractedSummary(){



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
		assertEquals(new Fact("z.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());assertEquals(newFact("z.f"),applyAbstractedSummary(newFact("a.f"),newSummaryEdge<>(newFact("a"),null,newFact("z"))).get());



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
	}}		@Test@Test	public void testAbstractedFieldAccessSummary() {publicvoidtestAbstractedFieldAccessSummary(){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

97
		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z.b"))).get());assertEquals(newFact("z.b.c"),applyAbstractedSummary(newFact("a.b.c"),newSummaryEdge<>(newFact("a.b"),null,newFact("z.b"))).get());



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

99

100

101
	}}		@Test@Test	public void testSummaryIntroducesFieldAccess() {publicvoidtestSummaryIntroducesFieldAccess(){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

102
		assertEquals(new Fact("z.b.c"), applyAbstractedSummary(new Fact("a.c"), new SummaryEdge<>(new Fact("a"), null, new Fact("z.b"))).get());assertEquals(newFact("z.b.c"),applyAbstractedSummary(newFact("a.c"),newSummaryEdge<>(newFact("a"),null,newFact("z.b"))).get());



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

103

104

105

106
	}}		@Test@Test	public void testSummaryRemovesFieldAccess() {publicvoidtestSummaryRemovesFieldAccess(){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

107
		assertEquals(new Fact("z.c"), applyAbstractedSummary(new Fact("a.b.c"), new SummaryEdge<>(new Fact("a.b"), null, new Fact("z"))).get());assertEquals(newFact("z.c"),applyAbstractedSummary(newFact("a.b.c"),newSummaryEdge<>(newFact("a.b"),null,newFact("z"))).get());



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

108

109

110

111
	}}		@Test@Test	public void testNonAbstractedSummary() {publicvoidtestNonAbstractedSummary(){



handling for writing fields / excluding access paths


 

 


Johannes Lerch
committed
Jan 05, 2015



handling for writing fields / excluding access paths


 

 

handling for writing fields / excluding access paths

 

Johannes Lerch
committed
Jan 05, 2015

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
		assertEquals(new Fact("z"), applyAbstractedSummary(new Fact("a"), new SummaryEdge<>(new Fact("a"), null, new Fact("z"))).get());assertEquals(newFact("z"),applyAbstractedSummary(newFact("a"),newSummaryEdge<>(newFact("a"),null,newFact("z"))).get());	}}		@Test@Test	public void testSummaryWithExcludedField() {publicvoidtestSummaryWithExcludedField(){		assertEquals(new Fact("a.f"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());assertEquals(newFact("a.f"),applyAbstractedSummary(newFact("a.f"),newSummaryEdge<>(newFact("a"),null,newFact("a^g"))).get());	}}		@Test@Test	public void testSummaryWithMultipleExcludedFields() {publicvoidtestSummaryWithMultipleExcludedFields(){		assertEquals(new Fact("a.f^h,i"), applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());assertEquals(newFact("a.f^h,i"),applyAbstractedSummary(newFact("a.f"),newSummaryEdge<>(newFact("a"),null,newFact("a^g^h,i"))).get());		assertEquals(new Fact("a.f.f"), applyAbstractedSummary(new Fact("a.f.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g^h,i"))).get());assertEquals(newFact("a.f.f"),applyAbstractedSummary(newFact("a.f.f"),newSummaryEdge<>(newFact("a"),null,newFact("a^g^h,i"))).get());	}}		@Test@Test	public void testIdentityForExclusions() {publicvoidtestIdentityForExclusions(){		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a"))).get());assertEquals(newFact("a^f"),applyAbstractedSummary(newFact("a^f"),newSummaryEdge<>(newFact("a"),null,newFact("a"))).get());		assertEquals(new Fact("a^f"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).get());assertEquals(newFact("a^f"),applyAbstractedSummary(newFact("a^f"),newSummaryEdge<>(newFact("a"),null,newFact("a^f"))).get());	}}		@Test@Test	public void testMergeExclusions() {publicvoidtestMergeExclusions(){		assertEquals(new Fact("a^f,g"), applyAbstractedSummary(new Fact("a^f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^g"))).get());assertEquals(newFact("a^f,g"),applyAbstractedSummary(newFact("a^f"),newSummaryEdge<>(newFact("a"),null,newFact("a^g"))).get());	}}		@Test@Test	public void testNullOnImpossibleSubsumption() {publicvoidtestNullOnImpossibleSubsumption(){		assertFalse(applyAbstractedSummary(new Fact("a.f"), new SummaryEdge<>(new Fact("a"), null, new Fact("a^f"))).isPresent());assertFalse(applyAbstractedSummary(newFact("a.f"),newSummaryEdge<>(newFact("a"),null,newFact("a^f"))).isPresent());



use of abstracted summaries



 


Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries



 

use of abstracted summaries


Johannes Lerch
committed
Oct 22, 2014

140

141
	}}}}





