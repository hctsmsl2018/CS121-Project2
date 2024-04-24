



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

d77de58184ee341f2fe4abe9c605405f2799c5d1

















d77de58184ee341f2fe4abe9c605405f2799c5d1


Switch branch/tag










heros


test


heros


fieldsens


AccessPathTest.java



Find file
Normal viewHistoryPermalink






AccessPathTest.java



5.44 KB









Newer










Older









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






1




/*******************************************************************************









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






2




 * Copyright (c) 2015 Johannes Lerch.









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






9




 *     Johannes Lerch - initial API and implementation









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






10




 ******************************************************************************/









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






11




package heros.fieldsens;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






12














renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






13




import static heros.fieldsens.AccessPath.PrefixTestResult.*;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






14




15




16




17




import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






18














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






19




20




21




import java.util.regex.Matcher;
import java.util.regex.Pattern;










renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






22




23




24




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPath.Delta;
import heros.fieldsens.AccessPath.PrefixTestResult;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






25




26




27




28




29




30




31





import org.junit.Test;

import com.google.common.collect.Sets;

public class AccessPathTest {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	public static AccessPath<String> ap(String ap) {
		Pattern pattern = Pattern.compile("(\\.|\\^)?([^\\.\\^]+)");
		Matcher matcher = pattern.matcher(ap);
		AccessPath<String> accessPath = new AccessPath<>();
		boolean addedExclusions = false;
		
		while(matcher.find()) {
			String separator = matcher.group(1);
			String identifier = matcher.group(2);
			
			if(".".equals(separator) || separator == null) {
				if(addedExclusions)
					throw new IllegalArgumentException("Access path contains field references after exclusions.");
				accessPath = accessPath.append(identifier);
			} else {
				addedExclusions=true;
				String[] excl = identifier.split(",");
				accessPath = accessPath.appendExcludedFieldReference(excl);
			}
		}
		return accessPath;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






53




54




55




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






56




57




	public void append() {
		AccessPath<String> sut = ap("a");









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






58




		assertEquals(ap("a.b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






59




60




61




62




	}
	
	@Test
	public void addOnExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






64




		assertEquals(ap("b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






65




66




67




68




	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMergedFieldsOnSingleExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






69




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






70




		sut.append("a");	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






71




72




73




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






74




	public void prepend() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






75




		assertEquals(ap("c.a.b"), ap("a.b").prepend("c"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






76




77




78




79




	}
	
	@Test
	public void remove() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






80




		assertEquals(ap("b"), ap("a.b").removeFirst());









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






81




82




83




84




	}
	
	@Test
	public void deltaDepth1() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		assertArrayEquals(new String[] { "b" }, ap("a").getDeltaTo(ap("a.b")).accesses);









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






86




87




88




89




	}
	
	@Test
	public void deltaDepth2() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






90




		assertArrayEquals(new String[] { "b", "c" }, ap("a").getDeltaTo(ap("a.b.c")).accesses);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






91




92




	}
	









fix of what i just did wrong


 

 


Johannes Lerch
committed
Apr 15, 2015






93




94




95




96




97




98




99




	@Test
	public void deltaOnNonEmptyAccPathsWithExclusions() {
		Delta<String> delta = ap("a^f").getDeltaTo(ap("a.b^g"));
		assertArrayEquals(new Object[] { "b" }, delta.accesses);
		assertEquals(Sets.newHashSet("g"), delta.exclusions);
	}
	









Fixed delta computation of exclusions at potential prefixes


 

 


Johannes Lerch
committed
Apr 15, 2015






100




101




102




103




104




	@Test
	public void deltaOnPotentialPrefix() {
		assertEquals(Sets.newHashSet("f", "g"), ap("^f").getDeltaTo(ap("^g")).exclusions);
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






105




106




	@Test
	public void emptyDeltaOnEqualExclusions() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






107




108




109




110




		AccessPath<String> actual = ap("^f");
		Object[] accesses = actual.getDeltaTo(ap("^f")).accesses;
		assertEquals(0, accesses.length);
		assertTrue(actual.getDeltaTo(ap("^f")).exclusions.equals(Sets.newHashSet("f")));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






111




112




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






113




114




	@Test
	public void multipleExclPrefixOfMultipleExcl() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




		assertEquals(PrefixTestResult.POTENTIAL_PREFIX, ap("^f,g").isPrefixOf(ap("^f,h")));
	}
	
	@Test
	public void testBaseValuePrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("f")));
		assertEquals(NO_PREFIX, ap("f").isPrefixOf(ap("")));
	}
	
	@Test
	public void testBaseValueIdentity() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("")));
	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("b").isPrefixOf(ap("b.c")));
		assertEquals(NO_PREFIX, ap("b.c").isPrefixOf(ap("b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g")));
		assertEquals(NO_PREFIX,ap("g").isPrefixOf(ap("^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f")));
		assertEquals(GUARANTEED_PREFIX,ap("^f,g").isPrefixOf(ap("^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {
		assertEquals(POTENTIAL_PREFIX,ap("^f").isPrefixOf(ap("^g")));
	}
	
	@Test
	public void testMixedFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.g")));
		assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("f.h")));
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^f")));
		assertEquals(POTENTIAL_PREFIX,ap("^f,h").isPrefixOf(ap("^f,g")));
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^g")));
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f,g")));
	}

	@Test
	public void testDifferentAccessPathLength() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.h")));









subumption + debugging


 

 


Johannes Lerch
committed
Feb 09, 2015






170




	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	@Test
	public void testExclusionRequiresFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("^f")));
		assertEquals(NO_PREFIX, ap("^f").isPrefixOf(ap("")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
		assertEquals(NO_PREFIX,ap("f^g").isPrefixOf(ap("f")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g^h")));
		assertEquals(NO_PREFIX,ap("f^g^h").isPrefixOf(ap("f")));
	}
	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






184




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

d77de58184ee341f2fe4abe9c605405f2799c5d1

















d77de58184ee341f2fe4abe9c605405f2799c5d1


Switch branch/tag










heros


test


heros


fieldsens


AccessPathTest.java



Find file
Normal viewHistoryPermalink






AccessPathTest.java



5.44 KB









Newer










Older









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






1




/*******************************************************************************









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






2




 * Copyright (c) 2015 Johannes Lerch.









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






9




 *     Johannes Lerch - initial API and implementation









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






10




 ******************************************************************************/









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






11




package heros.fieldsens;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






12














renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






13




import static heros.fieldsens.AccessPath.PrefixTestResult.*;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






14




15




16




17




import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






18














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






19




20




21




import java.util.regex.Matcher;
import java.util.regex.Pattern;










renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






22




23




24




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPath.Delta;
import heros.fieldsens.AccessPath.PrefixTestResult;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






25




26




27




28




29




30




31





import org.junit.Test;

import com.google.common.collect.Sets;

public class AccessPathTest {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	public static AccessPath<String> ap(String ap) {
		Pattern pattern = Pattern.compile("(\\.|\\^)?([^\\.\\^]+)");
		Matcher matcher = pattern.matcher(ap);
		AccessPath<String> accessPath = new AccessPath<>();
		boolean addedExclusions = false;
		
		while(matcher.find()) {
			String separator = matcher.group(1);
			String identifier = matcher.group(2);
			
			if(".".equals(separator) || separator == null) {
				if(addedExclusions)
					throw new IllegalArgumentException("Access path contains field references after exclusions.");
				accessPath = accessPath.append(identifier);
			} else {
				addedExclusions=true;
				String[] excl = identifier.split(",");
				accessPath = accessPath.appendExcludedFieldReference(excl);
			}
		}
		return accessPath;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






53




54




55




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






56




57




	public void append() {
		AccessPath<String> sut = ap("a");









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






58




		assertEquals(ap("a.b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






59




60




61




62




	}
	
	@Test
	public void addOnExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






64




		assertEquals(ap("b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






65




66




67




68




	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMergedFieldsOnSingleExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






69




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






70




		sut.append("a");	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






71




72




73




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






74




	public void prepend() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






75




		assertEquals(ap("c.a.b"), ap("a.b").prepend("c"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






76




77




78




79




	}
	
	@Test
	public void remove() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






80




		assertEquals(ap("b"), ap("a.b").removeFirst());









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






81




82




83




84




	}
	
	@Test
	public void deltaDepth1() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		assertArrayEquals(new String[] { "b" }, ap("a").getDeltaTo(ap("a.b")).accesses);









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






86




87




88




89




	}
	
	@Test
	public void deltaDepth2() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






90




		assertArrayEquals(new String[] { "b", "c" }, ap("a").getDeltaTo(ap("a.b.c")).accesses);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






91




92




	}
	









fix of what i just did wrong


 

 


Johannes Lerch
committed
Apr 15, 2015






93




94




95




96




97




98




99




	@Test
	public void deltaOnNonEmptyAccPathsWithExclusions() {
		Delta<String> delta = ap("a^f").getDeltaTo(ap("a.b^g"));
		assertArrayEquals(new Object[] { "b" }, delta.accesses);
		assertEquals(Sets.newHashSet("g"), delta.exclusions);
	}
	









Fixed delta computation of exclusions at potential prefixes


 

 


Johannes Lerch
committed
Apr 15, 2015






100




101




102




103




104




	@Test
	public void deltaOnPotentialPrefix() {
		assertEquals(Sets.newHashSet("f", "g"), ap("^f").getDeltaTo(ap("^g")).exclusions);
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






105




106




	@Test
	public void emptyDeltaOnEqualExclusions() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






107




108




109




110




		AccessPath<String> actual = ap("^f");
		Object[] accesses = actual.getDeltaTo(ap("^f")).accesses;
		assertEquals(0, accesses.length);
		assertTrue(actual.getDeltaTo(ap("^f")).exclusions.equals(Sets.newHashSet("f")));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






111




112




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






113




114




	@Test
	public void multipleExclPrefixOfMultipleExcl() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




		assertEquals(PrefixTestResult.POTENTIAL_PREFIX, ap("^f,g").isPrefixOf(ap("^f,h")));
	}
	
	@Test
	public void testBaseValuePrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("f")));
		assertEquals(NO_PREFIX, ap("f").isPrefixOf(ap("")));
	}
	
	@Test
	public void testBaseValueIdentity() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("")));
	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("b").isPrefixOf(ap("b.c")));
		assertEquals(NO_PREFIX, ap("b.c").isPrefixOf(ap("b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g")));
		assertEquals(NO_PREFIX,ap("g").isPrefixOf(ap("^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f")));
		assertEquals(GUARANTEED_PREFIX,ap("^f,g").isPrefixOf(ap("^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {
		assertEquals(POTENTIAL_PREFIX,ap("^f").isPrefixOf(ap("^g")));
	}
	
	@Test
	public void testMixedFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.g")));
		assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("f.h")));
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^f")));
		assertEquals(POTENTIAL_PREFIX,ap("^f,h").isPrefixOf(ap("^f,g")));
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^g")));
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f,g")));
	}

	@Test
	public void testDifferentAccessPathLength() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.h")));









subumption + debugging


 

 


Johannes Lerch
committed
Feb 09, 2015






170




	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	@Test
	public void testExclusionRequiresFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("^f")));
		assertEquals(NO_PREFIX, ap("^f").isPrefixOf(ap("")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
		assertEquals(NO_PREFIX,ap("f^g").isPrefixOf(ap("f")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g^h")));
		assertEquals(NO_PREFIX,ap("f^g^h").isPrefixOf(ap("f")));
	}
	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






184




}











Open sidebar



Joshua Garcia heros

d77de58184ee341f2fe4abe9c605405f2799c5d1







Open sidebar



Joshua Garcia heros

d77de58184ee341f2fe4abe9c605405f2799c5d1




Open sidebar

Joshua Garcia heros

d77de58184ee341f2fe4abe9c605405f2799c5d1


Joshua Garciaherosheros
d77de58184ee341f2fe4abe9c605405f2799c5d1










d77de58184ee341f2fe4abe9c605405f2799c5d1


Switch branch/tag










heros


test


heros


fieldsens


AccessPathTest.java



Find file
Normal viewHistoryPermalink






AccessPathTest.java



5.44 KB









Newer










Older









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






1




/*******************************************************************************









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






2




 * Copyright (c) 2015 Johannes Lerch.









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






9




 *     Johannes Lerch - initial API and implementation









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






10




 ******************************************************************************/









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






11




package heros.fieldsens;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






12














renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






13




import static heros.fieldsens.AccessPath.PrefixTestResult.*;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






14




15




16




17




import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






18














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






19




20




21




import java.util.regex.Matcher;
import java.util.regex.Pattern;










renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






22




23




24




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPath.Delta;
import heros.fieldsens.AccessPath.PrefixTestResult;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






25




26




27




28




29




30




31





import org.junit.Test;

import com.google.common.collect.Sets;

public class AccessPathTest {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	public static AccessPath<String> ap(String ap) {
		Pattern pattern = Pattern.compile("(\\.|\\^)?([^\\.\\^]+)");
		Matcher matcher = pattern.matcher(ap);
		AccessPath<String> accessPath = new AccessPath<>();
		boolean addedExclusions = false;
		
		while(matcher.find()) {
			String separator = matcher.group(1);
			String identifier = matcher.group(2);
			
			if(".".equals(separator) || separator == null) {
				if(addedExclusions)
					throw new IllegalArgumentException("Access path contains field references after exclusions.");
				accessPath = accessPath.append(identifier);
			} else {
				addedExclusions=true;
				String[] excl = identifier.split(",");
				accessPath = accessPath.appendExcludedFieldReference(excl);
			}
		}
		return accessPath;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






53




54




55




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






56




57




	public void append() {
		AccessPath<String> sut = ap("a");









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






58




		assertEquals(ap("a.b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






59




60




61




62




	}
	
	@Test
	public void addOnExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






64




		assertEquals(ap("b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






65




66




67




68




	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMergedFieldsOnSingleExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






69




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






70




		sut.append("a");	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






71




72




73




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






74




	public void prepend() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






75




		assertEquals(ap("c.a.b"), ap("a.b").prepend("c"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






76




77




78




79




	}
	
	@Test
	public void remove() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






80




		assertEquals(ap("b"), ap("a.b").removeFirst());









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






81




82




83




84




	}
	
	@Test
	public void deltaDepth1() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		assertArrayEquals(new String[] { "b" }, ap("a").getDeltaTo(ap("a.b")).accesses);









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






86




87




88




89




	}
	
	@Test
	public void deltaDepth2() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






90




		assertArrayEquals(new String[] { "b", "c" }, ap("a").getDeltaTo(ap("a.b.c")).accesses);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






91




92




	}
	









fix of what i just did wrong


 

 


Johannes Lerch
committed
Apr 15, 2015






93




94




95




96




97




98




99




	@Test
	public void deltaOnNonEmptyAccPathsWithExclusions() {
		Delta<String> delta = ap("a^f").getDeltaTo(ap("a.b^g"));
		assertArrayEquals(new Object[] { "b" }, delta.accesses);
		assertEquals(Sets.newHashSet("g"), delta.exclusions);
	}
	









Fixed delta computation of exclusions at potential prefixes


 

 


Johannes Lerch
committed
Apr 15, 2015






100




101




102




103




104




	@Test
	public void deltaOnPotentialPrefix() {
		assertEquals(Sets.newHashSet("f", "g"), ap("^f").getDeltaTo(ap("^g")).exclusions);
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






105




106




	@Test
	public void emptyDeltaOnEqualExclusions() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






107




108




109




110




		AccessPath<String> actual = ap("^f");
		Object[] accesses = actual.getDeltaTo(ap("^f")).accesses;
		assertEquals(0, accesses.length);
		assertTrue(actual.getDeltaTo(ap("^f")).exclusions.equals(Sets.newHashSet("f")));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






111




112




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






113




114




	@Test
	public void multipleExclPrefixOfMultipleExcl() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




		assertEquals(PrefixTestResult.POTENTIAL_PREFIX, ap("^f,g").isPrefixOf(ap("^f,h")));
	}
	
	@Test
	public void testBaseValuePrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("f")));
		assertEquals(NO_PREFIX, ap("f").isPrefixOf(ap("")));
	}
	
	@Test
	public void testBaseValueIdentity() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("")));
	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("b").isPrefixOf(ap("b.c")));
		assertEquals(NO_PREFIX, ap("b.c").isPrefixOf(ap("b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g")));
		assertEquals(NO_PREFIX,ap("g").isPrefixOf(ap("^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f")));
		assertEquals(GUARANTEED_PREFIX,ap("^f,g").isPrefixOf(ap("^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {
		assertEquals(POTENTIAL_PREFIX,ap("^f").isPrefixOf(ap("^g")));
	}
	
	@Test
	public void testMixedFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.g")));
		assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("f.h")));
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^f")));
		assertEquals(POTENTIAL_PREFIX,ap("^f,h").isPrefixOf(ap("^f,g")));
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^g")));
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f,g")));
	}

	@Test
	public void testDifferentAccessPathLength() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.h")));









subumption + debugging


 

 


Johannes Lerch
committed
Feb 09, 2015






170




	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	@Test
	public void testExclusionRequiresFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("^f")));
		assertEquals(NO_PREFIX, ap("^f").isPrefixOf(ap("")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
		assertEquals(NO_PREFIX,ap("f^g").isPrefixOf(ap("f")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g^h")));
		assertEquals(NO_PREFIX,ap("f^g^h").isPrefixOf(ap("f")));
	}
	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






184




}














d77de58184ee341f2fe4abe9c605405f2799c5d1


Switch branch/tag










heros


test


heros


fieldsens


AccessPathTest.java



Find file
Normal viewHistoryPermalink






AccessPathTest.java



5.44 KB









Newer










Older









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






1




/*******************************************************************************









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






2




 * Copyright (c) 2015 Johannes Lerch.









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






9




 *     Johannes Lerch - initial API and implementation









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






10




 ******************************************************************************/









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






11




package heros.fieldsens;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






12














renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






13




import static heros.fieldsens.AccessPath.PrefixTestResult.*;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






14




15




16




17




import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






18














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






19




20




21




import java.util.regex.Matcher;
import java.util.regex.Pattern;










renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






22




23




24




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPath.Delta;
import heros.fieldsens.AccessPath.PrefixTestResult;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






25




26




27




28




29




30




31





import org.junit.Test;

import com.google.common.collect.Sets;

public class AccessPathTest {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	public static AccessPath<String> ap(String ap) {
		Pattern pattern = Pattern.compile("(\\.|\\^)?([^\\.\\^]+)");
		Matcher matcher = pattern.matcher(ap);
		AccessPath<String> accessPath = new AccessPath<>();
		boolean addedExclusions = false;
		
		while(matcher.find()) {
			String separator = matcher.group(1);
			String identifier = matcher.group(2);
			
			if(".".equals(separator) || separator == null) {
				if(addedExclusions)
					throw new IllegalArgumentException("Access path contains field references after exclusions.");
				accessPath = accessPath.append(identifier);
			} else {
				addedExclusions=true;
				String[] excl = identifier.split(",");
				accessPath = accessPath.appendExcludedFieldReference(excl);
			}
		}
		return accessPath;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






53




54




55




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






56




57




	public void append() {
		AccessPath<String> sut = ap("a");









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






58




		assertEquals(ap("a.b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






59




60




61




62




	}
	
	@Test
	public void addOnExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






64




		assertEquals(ap("b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






65




66




67




68




	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMergedFieldsOnSingleExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






69




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






70




		sut.append("a");	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






71




72




73




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






74




	public void prepend() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






75




		assertEquals(ap("c.a.b"), ap("a.b").prepend("c"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






76




77




78




79




	}
	
	@Test
	public void remove() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






80




		assertEquals(ap("b"), ap("a.b").removeFirst());









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






81




82




83




84




	}
	
	@Test
	public void deltaDepth1() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		assertArrayEquals(new String[] { "b" }, ap("a").getDeltaTo(ap("a.b")).accesses);









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






86




87




88




89




	}
	
	@Test
	public void deltaDepth2() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






90




		assertArrayEquals(new String[] { "b", "c" }, ap("a").getDeltaTo(ap("a.b.c")).accesses);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






91




92




	}
	









fix of what i just did wrong


 

 


Johannes Lerch
committed
Apr 15, 2015






93




94




95




96




97




98




99




	@Test
	public void deltaOnNonEmptyAccPathsWithExclusions() {
		Delta<String> delta = ap("a^f").getDeltaTo(ap("a.b^g"));
		assertArrayEquals(new Object[] { "b" }, delta.accesses);
		assertEquals(Sets.newHashSet("g"), delta.exclusions);
	}
	









Fixed delta computation of exclusions at potential prefixes


 

 


Johannes Lerch
committed
Apr 15, 2015






100




101




102




103




104




	@Test
	public void deltaOnPotentialPrefix() {
		assertEquals(Sets.newHashSet("f", "g"), ap("^f").getDeltaTo(ap("^g")).exclusions);
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






105




106




	@Test
	public void emptyDeltaOnEqualExclusions() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






107




108




109




110




		AccessPath<String> actual = ap("^f");
		Object[] accesses = actual.getDeltaTo(ap("^f")).accesses;
		assertEquals(0, accesses.length);
		assertTrue(actual.getDeltaTo(ap("^f")).exclusions.equals(Sets.newHashSet("f")));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






111




112




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






113




114




	@Test
	public void multipleExclPrefixOfMultipleExcl() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




		assertEquals(PrefixTestResult.POTENTIAL_PREFIX, ap("^f,g").isPrefixOf(ap("^f,h")));
	}
	
	@Test
	public void testBaseValuePrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("f")));
		assertEquals(NO_PREFIX, ap("f").isPrefixOf(ap("")));
	}
	
	@Test
	public void testBaseValueIdentity() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("")));
	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("b").isPrefixOf(ap("b.c")));
		assertEquals(NO_PREFIX, ap("b.c").isPrefixOf(ap("b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g")));
		assertEquals(NO_PREFIX,ap("g").isPrefixOf(ap("^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f")));
		assertEquals(GUARANTEED_PREFIX,ap("^f,g").isPrefixOf(ap("^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {
		assertEquals(POTENTIAL_PREFIX,ap("^f").isPrefixOf(ap("^g")));
	}
	
	@Test
	public void testMixedFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.g")));
		assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("f.h")));
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^f")));
		assertEquals(POTENTIAL_PREFIX,ap("^f,h").isPrefixOf(ap("^f,g")));
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^g")));
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f,g")));
	}

	@Test
	public void testDifferentAccessPathLength() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.h")));









subumption + debugging


 

 


Johannes Lerch
committed
Feb 09, 2015






170




	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	@Test
	public void testExclusionRequiresFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("^f")));
		assertEquals(NO_PREFIX, ap("^f").isPrefixOf(ap("")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
		assertEquals(NO_PREFIX,ap("f^g").isPrefixOf(ap("f")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g^h")));
		assertEquals(NO_PREFIX,ap("f^g^h").isPrefixOf(ap("f")));
	}
	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






184




}










d77de58184ee341f2fe4abe9c605405f2799c5d1


Switch branch/tag










heros


test


heros


fieldsens


AccessPathTest.java



Find file
Normal viewHistoryPermalink




d77de58184ee341f2fe4abe9c605405f2799c5d1


Switch branch/tag










heros


test


heros


fieldsens


AccessPathTest.java





d77de58184ee341f2fe4abe9c605405f2799c5d1


Switch branch/tag








d77de58184ee341f2fe4abe9c605405f2799c5d1


Switch branch/tag





d77de58184ee341f2fe4abe9c605405f2799c5d1

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

fieldsens

AccessPathTest.java
Find file
Normal viewHistoryPermalink




AccessPathTest.java



5.44 KB









Newer










Older









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






1




/*******************************************************************************









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






2




 * Copyright (c) 2015 Johannes Lerch.









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






9




 *     Johannes Lerch - initial API and implementation









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






10




 ******************************************************************************/









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






11




package heros.fieldsens;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






12














renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






13




import static heros.fieldsens.AccessPath.PrefixTestResult.*;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






14




15




16




17




import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






18














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






19




20




21




import java.util.regex.Matcher;
import java.util.regex.Pattern;










renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






22




23




24




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPath.Delta;
import heros.fieldsens.AccessPath.PrefixTestResult;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






25




26




27




28




29




30




31





import org.junit.Test;

import com.google.common.collect.Sets;

public class AccessPathTest {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	public static AccessPath<String> ap(String ap) {
		Pattern pattern = Pattern.compile("(\\.|\\^)?([^\\.\\^]+)");
		Matcher matcher = pattern.matcher(ap);
		AccessPath<String> accessPath = new AccessPath<>();
		boolean addedExclusions = false;
		
		while(matcher.find()) {
			String separator = matcher.group(1);
			String identifier = matcher.group(2);
			
			if(".".equals(separator) || separator == null) {
				if(addedExclusions)
					throw new IllegalArgumentException("Access path contains field references after exclusions.");
				accessPath = accessPath.append(identifier);
			} else {
				addedExclusions=true;
				String[] excl = identifier.split(",");
				accessPath = accessPath.appendExcludedFieldReference(excl);
			}
		}
		return accessPath;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






53




54




55




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






56




57




	public void append() {
		AccessPath<String> sut = ap("a");









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






58




		assertEquals(ap("a.b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






59




60




61




62




	}
	
	@Test
	public void addOnExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






64




		assertEquals(ap("b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






65




66




67




68




	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMergedFieldsOnSingleExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






69




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






70




		sut.append("a");	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






71




72




73




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






74




	public void prepend() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






75




		assertEquals(ap("c.a.b"), ap("a.b").prepend("c"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






76




77




78




79




	}
	
	@Test
	public void remove() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






80




		assertEquals(ap("b"), ap("a.b").removeFirst());









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






81




82




83




84




	}
	
	@Test
	public void deltaDepth1() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		assertArrayEquals(new String[] { "b" }, ap("a").getDeltaTo(ap("a.b")).accesses);









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






86




87




88




89




	}
	
	@Test
	public void deltaDepth2() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






90




		assertArrayEquals(new String[] { "b", "c" }, ap("a").getDeltaTo(ap("a.b.c")).accesses);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






91




92




	}
	









fix of what i just did wrong


 

 


Johannes Lerch
committed
Apr 15, 2015






93




94




95




96




97




98




99




	@Test
	public void deltaOnNonEmptyAccPathsWithExclusions() {
		Delta<String> delta = ap("a^f").getDeltaTo(ap("a.b^g"));
		assertArrayEquals(new Object[] { "b" }, delta.accesses);
		assertEquals(Sets.newHashSet("g"), delta.exclusions);
	}
	









Fixed delta computation of exclusions at potential prefixes


 

 


Johannes Lerch
committed
Apr 15, 2015






100




101




102




103




104




	@Test
	public void deltaOnPotentialPrefix() {
		assertEquals(Sets.newHashSet("f", "g"), ap("^f").getDeltaTo(ap("^g")).exclusions);
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






105




106




	@Test
	public void emptyDeltaOnEqualExclusions() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






107




108




109




110




		AccessPath<String> actual = ap("^f");
		Object[] accesses = actual.getDeltaTo(ap("^f")).accesses;
		assertEquals(0, accesses.length);
		assertTrue(actual.getDeltaTo(ap("^f")).exclusions.equals(Sets.newHashSet("f")));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






111




112




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






113




114




	@Test
	public void multipleExclPrefixOfMultipleExcl() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




		assertEquals(PrefixTestResult.POTENTIAL_PREFIX, ap("^f,g").isPrefixOf(ap("^f,h")));
	}
	
	@Test
	public void testBaseValuePrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("f")));
		assertEquals(NO_PREFIX, ap("f").isPrefixOf(ap("")));
	}
	
	@Test
	public void testBaseValueIdentity() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("")));
	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("b").isPrefixOf(ap("b.c")));
		assertEquals(NO_PREFIX, ap("b.c").isPrefixOf(ap("b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g")));
		assertEquals(NO_PREFIX,ap("g").isPrefixOf(ap("^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f")));
		assertEquals(GUARANTEED_PREFIX,ap("^f,g").isPrefixOf(ap("^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {
		assertEquals(POTENTIAL_PREFIX,ap("^f").isPrefixOf(ap("^g")));
	}
	
	@Test
	public void testMixedFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.g")));
		assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("f.h")));
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^f")));
		assertEquals(POTENTIAL_PREFIX,ap("^f,h").isPrefixOf(ap("^f,g")));
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^g")));
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f,g")));
	}

	@Test
	public void testDifferentAccessPathLength() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.h")));









subumption + debugging


 

 


Johannes Lerch
committed
Feb 09, 2015






170




	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	@Test
	public void testExclusionRequiresFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("^f")));
		assertEquals(NO_PREFIX, ap("^f").isPrefixOf(ap("")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
		assertEquals(NO_PREFIX,ap("f^g").isPrefixOf(ap("f")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g^h")));
		assertEquals(NO_PREFIX,ap("f^g^h").isPrefixOf(ap("f")));
	}
	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






184




}








AccessPathTest.java



5.44 KB










AccessPathTest.java



5.44 KB









Newer










Older
NewerOlder







regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






1




/*******************************************************************************









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






2




 * Copyright (c) 2015 Johannes Lerch.









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






3




4




5




6




7




8




 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:









Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015






9




 *     Johannes Lerch - initial API and implementation









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






10




 ******************************************************************************/









renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






11




package heros.fieldsens;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






12














renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






13




import static heros.fieldsens.AccessPath.PrefixTestResult.*;









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






14




15




16




17




import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






18














restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






19




20




21




import java.util.regex.Matcher;
import java.util.regex.Pattern;










renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015






22




23




24




import heros.fieldsens.AccessPath;
import heros.fieldsens.AccessPath.Delta;
import heros.fieldsens.AccessPath.PrefixTestResult;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






25




26




27




28




29




30




31





import org.junit.Test;

import com.google.common.collect.Sets;

public class AccessPathTest {










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	public static AccessPath<String> ap(String ap) {
		Pattern pattern = Pattern.compile("(\\.|\\^)?([^\\.\\^]+)");
		Matcher matcher = pattern.matcher(ap);
		AccessPath<String> accessPath = new AccessPath<>();
		boolean addedExclusions = false;
		
		while(matcher.find()) {
			String separator = matcher.group(1);
			String identifier = matcher.group(2);
			
			if(".".equals(separator) || separator == null) {
				if(addedExclusions)
					throw new IllegalArgumentException("Access path contains field references after exclusions.");
				accessPath = accessPath.append(identifier);
			} else {
				addedExclusions=true;
				String[] excl = identifier.split(",");
				accessPath = accessPath.appendExcludedFieldReference(excl);
			}
		}
		return accessPath;









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






53




54




55




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






56




57




	public void append() {
		AccessPath<String> sut = ap("a");









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






58




		assertEquals(ap("a.b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






59




60




61




62




	}
	
	@Test
	public void addOnExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






63




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






64




		assertEquals(ap("b"), sut.append("b"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






65




66




67




68




	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMergedFieldsOnSingleExclusion() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






69




		AccessPath<String> sut = ap("^a");









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






70




		sut.append("a");	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






71




72




73




	}
	
	@Test









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






74




	public void prepend() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






75




		assertEquals(ap("c.a.b"), ap("a.b").prepend("c"));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






76




77




78




79




	}
	
	@Test
	public void remove() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






80




		assertEquals(ap("b"), ap("a.b").removeFirst());









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






81




82




83




84




	}
	
	@Test
	public void deltaDepth1() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




		assertArrayEquals(new String[] { "b" }, ap("a").getDeltaTo(ap("a.b")).accesses);









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






86




87




88




89




	}
	
	@Test
	public void deltaDepth2() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






90




		assertArrayEquals(new String[] { "b", "c" }, ap("a").getDeltaTo(ap("a.b.c")).accesses);









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






91




92




	}
	









fix of what i just did wrong


 

 


Johannes Lerch
committed
Apr 15, 2015






93




94




95




96




97




98




99




	@Test
	public void deltaOnNonEmptyAccPathsWithExclusions() {
		Delta<String> delta = ap("a^f").getDeltaTo(ap("a.b^g"));
		assertArrayEquals(new Object[] { "b" }, delta.accesses);
		assertEquals(Sets.newHashSet("g"), delta.exclusions);
	}
	









Fixed delta computation of exclusions at potential prefixes


 

 


Johannes Lerch
committed
Apr 15, 2015






100




101




102




103




104




	@Test
	public void deltaOnPotentialPrefix() {
		assertEquals(Sets.newHashSet("f", "g"), ap("^f").getDeltaTo(ap("^g")).exclusions);
	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






105




106




	@Test
	public void emptyDeltaOnEqualExclusions() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






107




108




109




110




		AccessPath<String> actual = ap("^f");
		Object[] accesses = actual.getDeltaTo(ap("^f")).accesses;
		assertEquals(0, accesses.length);
		assertTrue(actual.getDeltaTo(ap("^f")).exclusions.equals(Sets.newHashSet("f")));









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






111




112




	}
	









rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015






113




114




	@Test
	public void multipleExclPrefixOfMultipleExcl() {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




		assertEquals(PrefixTestResult.POTENTIAL_PREFIX, ap("^f,g").isPrefixOf(ap("^f,h")));
	}
	
	@Test
	public void testBaseValuePrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("f")));
		assertEquals(NO_PREFIX, ap("f").isPrefixOf(ap("")));
	}
	
	@Test
	public void testBaseValueIdentity() {
		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("")));
	}
	
	@Test
	public void testFieldAccessPrefixOfFieldAccess() {
		assertEquals(GUARANTEED_PREFIX, ap("b").isPrefixOf(ap("b.c")));
		assertEquals(NO_PREFIX, ap("b.c").isPrefixOf(ap("b")));
	}
	
	@Test
	public void testPrefixOfFieldAccessWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g")));
		assertEquals(NO_PREFIX,ap("g").isPrefixOf(ap("^f")));
	}
	
	@Test
	public void testIdentityWithExclusion() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f")));
		assertEquals(GUARANTEED_PREFIX,ap("^f,g").isPrefixOf(ap("^f,g")));
	}
	
	@Test
	public void testDifferentExclusions() {
		assertEquals(POTENTIAL_PREFIX,ap("^f").isPrefixOf(ap("^g")));
	}
	
	@Test
	public void testMixedFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.g")));
		assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("f.h")));
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
	}
	
	@Test
	public void testMultipleExclusions() {
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^f")));
		assertEquals(POTENTIAL_PREFIX,ap("^f,h").isPrefixOf(ap("^f,g")));
		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^g")));
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f,g")));
	}

	@Test
	public void testDifferentAccessPathLength() {
		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.h")));









subumption + debugging


 

 


Johannes Lerch
committed
Feb 09, 2015






170




	}









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






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




	
	@Test
	public void testExclusionRequiresFieldAccess() {
		assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("^f")));
		assertEquals(NO_PREFIX, ap("^f").isPrefixOf(ap("")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));
		assertEquals(NO_PREFIX,ap("f^g").isPrefixOf(ap("f")));
		
		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g^h")));
		assertEquals(NO_PREFIX,ap("f^g^h").isPrefixOf(ap("f")));
	}
	









regexp access path



 


Johannes Lerch
committed
Feb 05, 2015






184




}







regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

1
/*******************************************************************************/*******************************************************************************



Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015



Fixing licence headers


 

 

Fixing licence headers

 

Johannes Lerch
committed
Jun 01, 2015

2
 * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch.



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

3

4

5

6

7

8
 * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors:



Fixing licence headers


 

 


Johannes Lerch
committed
Jun 01, 2015



Fixing licence headers


 

 

Fixing licence headers

 

Johannes Lerch
committed
Jun 01, 2015

9
 *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

10
 ******************************************************************************/ ******************************************************************************/



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
package heros.fieldsens;packageheros.fieldsens;



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

12




renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015



renaming package


 

 

renaming package

 

Johannes Lerch
committed
Jun 01, 2015

13
import static heros.fieldsens.AccessPath.PrefixTestResult.*;importstaticheros.fieldsens.AccessPath.PrefixTestResult.*;



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

14

15

16

17
import static org.junit.Assert.assertArrayEquals;importstaticorg.junit.Assert.assertArrayEquals;import static org.junit.Assert.assertEquals;importstaticorg.junit.Assert.assertEquals;import static org.junit.Assert.assertFalse;importstaticorg.junit.Assert.assertFalse;import static org.junit.Assert.assertTrue;importstaticorg.junit.Assert.assertTrue;



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

18




restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

19

20

21
import java.util.regex.Matcher;importjava.util.regex.Matcher;import java.util.regex.Pattern;importjava.util.regex.Pattern;



renaming package


 

 


Johannes Lerch
committed
Jun 01, 2015



renaming package


 

 

renaming package

 

Johannes Lerch
committed
Jun 01, 2015

22

23

24
import heros.fieldsens.AccessPath;importheros.fieldsens.AccessPath;import heros.fieldsens.AccessPath.Delta;importheros.fieldsens.AccessPath.Delta;import heros.fieldsens.AccessPath.PrefixTestResult;importheros.fieldsens.AccessPath.PrefixTestResult;



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

25

26

27

28

29

30

31
import org.junit.Test;importorg.junit.Test;import com.google.common.collect.Sets;importcom.google.common.collect.Sets;public class AccessPathTest {publicclassAccessPathTest{



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
	public static AccessPath<String> ap(String ap) {publicstaticAccessPath<String>ap(Stringap){		Pattern pattern = Pattern.compile("(\\.|\\^)?([^\\.\\^]+)");Patternpattern=Pattern.compile("(\\.|\\^)?([^\\.\\^]+)");		Matcher matcher = pattern.matcher(ap);Matchermatcher=pattern.matcher(ap);		AccessPath<String> accessPath = new AccessPath<>();AccessPath<String>accessPath=newAccessPath<>();		boolean addedExclusions = false;booleanaddedExclusions=false;				while(matcher.find()) {while(matcher.find()){			String separator = matcher.group(1);Stringseparator=matcher.group(1);			String identifier = matcher.group(2);Stringidentifier=matcher.group(2);						if(".".equals(separator) || separator == null) {if(".".equals(separator)||separator==null){				if(addedExclusions)if(addedExclusions)					throw new IllegalArgumentException("Access path contains field references after exclusions.");thrownewIllegalArgumentException("Access path contains field references after exclusions.");				accessPath = accessPath.append(identifier);accessPath=accessPath.append(identifier);			} else {}else{				addedExclusions=true;addedExclusions=true;				String[] excl = identifier.split(",");String[]excl=identifier.split(",");				accessPath = accessPath.appendExcludedFieldReference(excl);accessPath=accessPath.appendExcludedFieldReference(excl);			}}		}}		return accessPath;returnaccessPath;



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

53

54

55
	}}		@Test@Test



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

56

57
	public void append() {publicvoidappend(){		AccessPath<String> sut = ap("a");AccessPath<String>sut=ap("a");



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

58
		assertEquals(ap("a.b"), sut.append("b"));assertEquals(ap("a.b"),sut.append("b"));



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

59

60

61

62
	}}		@Test@Test	public void addOnExclusion() {publicvoidaddOnExclusion(){



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
		AccessPath<String> sut = ap("^a");AccessPath<String>sut=ap("^a");



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

64
		assertEquals(ap("b"), sut.append("b"));assertEquals(ap("b"),sut.append("b"));



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

65

66

67

68
	}}		@Test(expected=IllegalArgumentException.class)@Test(expected=IllegalArgumentException.class)	public void addMergedFieldsOnSingleExclusion() {publicvoidaddMergedFieldsOnSingleExclusion(){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

69
		AccessPath<String> sut = ap("^a");AccessPath<String>sut=ap("^a");



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

70
		sut.append("a");	sut.append("a");



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

71

72

73
	}}		@Test@Test



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

74
	public void prepend() {publicvoidprepend(){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

75
		assertEquals(ap("c.a.b"), ap("a.b").prepend("c"));assertEquals(ap("c.a.b"),ap("a.b").prepend("c"));



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

76

77

78

79
	}}		@Test@Test	public void remove() {publicvoidremove(){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

80
		assertEquals(ap("b"), ap("a.b").removeFirst());assertEquals(ap("b"),ap("a.b").removeFirst());



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

81

82

83

84
	}}		@Test@Test	public void deltaDepth1() {publicvoiddeltaDepth1(){



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
		assertArrayEquals(new String[] { "b" }, ap("a").getDeltaTo(ap("a.b")).accesses);assertArrayEquals(newString[]{"b"},ap("a").getDeltaTo(ap("a.b")).accesses);



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

86

87

88

89
	}}		@Test@Test	public void deltaDepth2() {publicvoiddeltaDepth2(){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

90
		assertArrayEquals(new String[] { "b", "c" }, ap("a").getDeltaTo(ap("a.b.c")).accesses);assertArrayEquals(newString[]{"b","c"},ap("a").getDeltaTo(ap("a.b.c")).accesses);



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

91

92
	}}	



fix of what i just did wrong


 

 


Johannes Lerch
committed
Apr 15, 2015



fix of what i just did wrong


 

 

fix of what i just did wrong

 

Johannes Lerch
committed
Apr 15, 2015

93

94

95

96

97

98

99
	@Test@Test	public void deltaOnNonEmptyAccPathsWithExclusions() {publicvoiddeltaOnNonEmptyAccPathsWithExclusions(){		Delta<String> delta = ap("a^f").getDeltaTo(ap("a.b^g"));Delta<String>delta=ap("a^f").getDeltaTo(ap("a.b^g"));		assertArrayEquals(new Object[] { "b" }, delta.accesses);assertArrayEquals(newObject[]{"b"},delta.accesses);		assertEquals(Sets.newHashSet("g"), delta.exclusions);assertEquals(Sets.newHashSet("g"),delta.exclusions);	}}	



Fixed delta computation of exclusions at potential prefixes


 

 


Johannes Lerch
committed
Apr 15, 2015



Fixed delta computation of exclusions at potential prefixes


 

 

Fixed delta computation of exclusions at potential prefixes

 

Johannes Lerch
committed
Apr 15, 2015

100

101

102

103

104
	@Test@Test	public void deltaOnPotentialPrefix() {publicvoiddeltaOnPotentialPrefix(){		assertEquals(Sets.newHashSet("f", "g"), ap("^f").getDeltaTo(ap("^g")).exclusions);assertEquals(Sets.newHashSet("f","g"),ap("^f").getDeltaTo(ap("^g")).exclusions);	}}	



rewrite of ifds solver


 

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


 

 

rewrite of ifds solver

 

Johannes Lerch
committed
Mar 19, 2015

105

106
	@Test@Test	public void emptyDeltaOnEqualExclusions() {publicvoidemptyDeltaOnEqualExclusions(){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

107

108

109

110
		AccessPath<String> actual = ap("^f");AccessPath<String>actual=ap("^f");		Object[] accesses = actual.getDeltaTo(ap("^f")).accesses;Object[]accesses=actual.getDeltaTo(ap("^f")).accesses;		assertEquals(0, accesses.length);assertEquals(0,accesses.length);		assertTrue(actual.getDeltaTo(ap("^f")).exclusions.equals(Sets.newHashSet("f")));assertTrue(actual.getDeltaTo(ap("^f")).exclusions.equals(Sets.newHashSet("f")));



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

111

112
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

113

114
	@Test@Test	public void multipleExclPrefixOfMultipleExcl() {publicvoidmultipleExclPrefixOfMultipleExcl(){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

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
		assertEquals(PrefixTestResult.POTENTIAL_PREFIX, ap("^f,g").isPrefixOf(ap("^f,h")));assertEquals(PrefixTestResult.POTENTIAL_PREFIX,ap("^f,g").isPrefixOf(ap("^f,h")));	}}		@Test@Test	public void testBaseValuePrefixOfFieldAccess() {publicvoidtestBaseValuePrefixOfFieldAccess(){		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("f")));assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("f")));		assertEquals(NO_PREFIX, ap("f").isPrefixOf(ap("")));assertEquals(NO_PREFIX,ap("f").isPrefixOf(ap("")));	}}		@Test@Test	public void testBaseValueIdentity() {publicvoidtestBaseValueIdentity(){		assertEquals(GUARANTEED_PREFIX, ap("").isPrefixOf(ap("")));assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("")));	}}		@Test@Test	public void testFieldAccessPrefixOfFieldAccess() {publicvoidtestFieldAccessPrefixOfFieldAccess(){		assertEquals(GUARANTEED_PREFIX, ap("b").isPrefixOf(ap("b.c")));assertEquals(GUARANTEED_PREFIX,ap("b").isPrefixOf(ap("b.c")));		assertEquals(NO_PREFIX, ap("b.c").isPrefixOf(ap("b")));assertEquals(NO_PREFIX,ap("b.c").isPrefixOf(ap("b")));	}}		@Test@Test	public void testPrefixOfFieldAccessWithExclusion() {publicvoidtestPrefixOfFieldAccessWithExclusion(){		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g")));assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g")));		assertEquals(NO_PREFIX,ap("g").isPrefixOf(ap("^f")));assertEquals(NO_PREFIX,ap("g").isPrefixOf(ap("^f")));	}}		@Test@Test	public void testIdentityWithExclusion() {publicvoidtestIdentityWithExclusion(){		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f")));assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f")));		assertEquals(GUARANTEED_PREFIX,ap("^f,g").isPrefixOf(ap("^f,g")));assertEquals(GUARANTEED_PREFIX,ap("^f,g").isPrefixOf(ap("^f,g")));	}}		@Test@Test	public void testDifferentExclusions() {publicvoidtestDifferentExclusions(){		assertEquals(POTENTIAL_PREFIX,ap("^f").isPrefixOf(ap("^g")));assertEquals(POTENTIAL_PREFIX,ap("^f").isPrefixOf(ap("^g")));	}}		@Test@Test	public void testMixedFieldAccess() {publicvoidtestMixedFieldAccess(){		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.g")));assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.g")));		assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("f.h")));assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("f.h")));		assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));	}}		@Test@Test	public void testMultipleExclusions() {publicvoidtestMultipleExclusions(){		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^f")));assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^f")));		assertEquals(POTENTIAL_PREFIX,ap("^f,h").isPrefixOf(ap("^f,g")));assertEquals(POTENTIAL_PREFIX,ap("^f,h").isPrefixOf(ap("^f,g")));		assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^g")));assertEquals(NO_PREFIX,ap("^f,g").isPrefixOf(ap("^g")));		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f,g")));assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("^f,g")));	}}	@Test@Test	public void testDifferentAccessPathLength() {publicvoidtestDifferentAccessPathLength(){		assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.h")));assertEquals(GUARANTEED_PREFIX,ap("^f").isPrefixOf(ap("g.h")));



subumption + debugging


 

 


Johannes Lerch
committed
Feb 09, 2015



subumption + debugging


 

 

subumption + debugging

 

Johannes Lerch
committed
Feb 09, 2015

170
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
		@Test@Test	public void testExclusionRequiresFieldAccess() {publicvoidtestExclusionRequiresFieldAccess(){		assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("^f")));assertEquals(GUARANTEED_PREFIX,ap("").isPrefixOf(ap("^f")));		assertEquals(NO_PREFIX, ap("^f").isPrefixOf(ap("")));assertEquals(NO_PREFIX,ap("^f").isPrefixOf(ap("")));				assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g")));		assertEquals(NO_PREFIX,ap("f^g").isPrefixOf(ap("f")));assertEquals(NO_PREFIX,ap("f^g").isPrefixOf(ap("f")));				assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g^h")));assertEquals(GUARANTEED_PREFIX,ap("f").isPrefixOf(ap("f^g^h")));		assertEquals(NO_PREFIX,ap("f^g^h").isPrefixOf(ap("f")));assertEquals(NO_PREFIX,ap("f^g^h").isPrefixOf(ap("f")));	}}	



regexp access path



 


Johannes Lerch
committed
Feb 05, 2015



regexp access path



 

regexp access path


Johannes Lerch
committed
Feb 05, 2015

184
}}





