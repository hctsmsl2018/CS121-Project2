



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

b67fe4e40a3fd5a831cf3e71194ed6e2de22364d



















heros


test


heros


fieldsens


ControlFlowJoinResolverTest.java




Find file



Normal view


History


Permalink








ControlFlowJoinResolverTest.java



5.36 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
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








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
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

b67fe4e40a3fd5a831cf3e71194ed6e2de22364d



















heros


test


heros


fieldsens


ControlFlowJoinResolverTest.java




Find file



Normal view


History


Permalink








ControlFlowJoinResolverTest.java



5.36 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
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








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
	}
}
















Joshua Garcia heros

b67fe4e40a3fd5a831cf3e71194ed6e2de22364d












Joshua Garcia heros

b67fe4e40a3fd5a831cf3e71194ed6e2de22364d










Joshua Garcia heros

b67fe4e40a3fd5a831cf3e71194ed6e2de22364d




Joshua Garciaherosheros
b67fe4e40a3fd5a831cf3e71194ed6e2de22364d












heros


test


heros


fieldsens


ControlFlowJoinResolverTest.java




Find file



Normal view


History


Permalink








ControlFlowJoinResolverTest.java



5.36 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
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








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
	}
}

















heros


test


heros


fieldsens


ControlFlowJoinResolverTest.java




Find file



Normal view


History


Permalink








ControlFlowJoinResolverTest.java



5.36 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
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








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
	}
}













heros


test


heros


fieldsens


ControlFlowJoinResolverTest.java




Find file



Normal view


History


Permalink








heros


test


heros


fieldsens


ControlFlowJoinResolverTest.java





heros

test

heros

fieldsens

ControlFlowJoinResolverTest.java

Find file



Normal view


History


Permalink


Find file


Normal view

History

Permalink





ControlFlowJoinResolverTest.java



5.36 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
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








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
	}
}









ControlFlowJoinResolverTest.java



5.36 KiB










ControlFlowJoinResolverTest.java



5.36 KiB









Newer










Older
NewerOlder







rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
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








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
	}
}











rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
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








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
	}
}









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
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








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
	}
}







rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


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
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/

/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/




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




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12


13


14




import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


12


13


14



import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertTrue;importstaticorg.junit.Assert.assertTrue;import static org.mockito.Mockito.*;importstaticorg.mockito.Mockito.*;




renaming package

 


Johannes Lerch
committed
Jun 01, 2015




15


16


17


18


19


20


21



import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;






renaming package

 


Johannes Lerch
committed
Jun 01, 2015



renaming package

 

renaming package

Johannes Lerch
committed
Jun 01, 2015


15


16


17


18


19


20


21


import heros.fieldsens.CallEdgeResolver;
import heros.fieldsens.ControlFlowJoinResolver;
import heros.fieldsens.InterestCallback;
import heros.fieldsens.PerAccessPathMethodAnalyzer;
import heros.fieldsens.Resolver;
import heros.fieldsens.ReturnSiteResolver;
import heros.fieldsens.AccessPath.Delta;

import heros.fieldsens.CallEdgeResolver;importheros.fieldsens.CallEdgeResolver;import heros.fieldsens.ControlFlowJoinResolver;importheros.fieldsens.ControlFlowJoinResolver;import heros.fieldsens.InterestCallback;importheros.fieldsens.InterestCallback;import heros.fieldsens.PerAccessPathMethodAnalyzer;importheros.fieldsens.PerAccessPathMethodAnalyzer;import heros.fieldsens.Resolver;importheros.fieldsens.Resolver;import heros.fieldsens.ReturnSiteResolver;importheros.fieldsens.ReturnSiteResolver;import heros.fieldsens.AccessPath.Delta;importheros.fieldsens.AccessPath.Delta;




restructuring

 


Johannes Lerch
committed
Jun 01, 2015




22


23


24



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;






restructuring

 


Johannes Lerch
committed
Jun 01, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Jun 01, 2015


22


23


24


import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;

import heros.fieldsens.structs.DeltaConstraint;importheros.fieldsens.structs.DeltaConstraint;import heros.fieldsens.structs.WrappedFact;importheros.fieldsens.structs.WrappedFact;import heros.fieldsens.structs.WrappedFactAtStatement;importheros.fieldsens.structs.WrappedFactAtStatement;




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




25


26


27



import heros.utilities.Statement;
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


25


26


27


import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;

import heros.utilities.Statement;importheros.utilities.Statement;import heros.utilities.TestFact;importheros.utilities.TestFact;import heros.utilities.TestMethod;importheros.utilities.TestMethod;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {







rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


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



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class ControlFlowJoinResolverTest {


import org.junit.Before;importorg.junit.Before;import org.junit.Test;importorg.junit.Test;import org.mockito.ArgumentMatcher;importorg.mockito.ArgumentMatcher;import org.mockito.Mockito;importorg.mockito.Mockito;import org.mockito.invocation.InvocationOnMock;importorg.mockito.invocation.InvocationOnMock;import org.mockito.stubbing.Answer;importorg.mockito.stubbing.Answer;import org.mockito.verification.VerificationMode;importorg.mockito.verification.VerificationMode;public class ControlFlowJoinResolverTest {publicclassControlFlowJoinResolverTest{




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


39


	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {

	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {privatestaticDeltaConstraint<String>getDeltaConstraint(String...fieldRefs){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new DeltaConstraint<String>(getDelta(fieldRefs));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


40


		return new DeltaConstraint<String>(getDelta(fieldRefs));

		return new DeltaConstraint<String>(getDelta(fieldRefs));returnnewDeltaConstraint<String>(getDelta(fieldRefs));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42



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


41


42


	}


	}}




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




43


44


45



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


43


44


45


	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);

	private static Delta<String> getDelta(String... fieldRefs) {privatestaticDelta<String>getDelta(String...fieldRefs){		AccessPath<String> accPath = createAccessPath(fieldRefs);AccessPath<String>accPath=createAccessPath(fieldRefs);		return new AccessPath<String>().getDeltaTo(accPath);returnnewAccessPath<String>().getDeltaTo(accPath);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47



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


46


47


	}


	}}




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




48



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


48


	protected static AccessPath<String> createAccessPath(String... fieldRefs) {

	protected static AccessPath<String> createAccessPath(String... fieldRefs) {protectedstaticAccessPath<String>createAccessPath(String...fieldRefs){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




49



		AccessPath<String> accPath = new AccessPath<String>();






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


49


		AccessPath<String> accPath = new AccessPath<String>();

		AccessPath<String> accPath = new AccessPath<String>();AccessPath<String>accPath=newAccessPath<String>();




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




50



		for (String fieldRef : fieldRefs) {






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


		for (String fieldRef : fieldRefs) {

		for (String fieldRef : fieldRefs) {for(StringfieldRef:fieldRefs){




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




51



			accPath = accPath.append(fieldRef);






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


51


			accPath = accPath.append(fieldRef);

			accPath = accPath.append(fieldRef);accPath=accPath.append(fieldRef);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




52


53


54


55



		}
		return accPath;
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


52


53


54


55


		}
		return accPath;
	}


		}}		return accPath;returnaccPath;	}}




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56


57


58



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


56


57


58


	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement joinStmt;
	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;

	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;privatePerAccessPathMethodAnalyzer<String,TestFact,Statement,TestMethod>analyzer;	private Statement joinStmt;privateStatementjoinStmt;	private ControlFlowJoinResolver<String, TestFact, Statement, TestMethod> sut;privateControlFlowJoinResolver<String,TestFact,Statement,TestMethod>sut;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




59



	private TestFact fact;






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


59


	private TestFact fact;

	private TestFact fact;privateTestFactfact;




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




60


61



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


60


61


	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;

	private InterestCallback<String, TestFact, Statement, TestMethod> callback;privateInterestCallback<String,TestFact,Statement,TestMethod>callback;	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;privateResolver<String,TestFact,Statement,TestMethod>callEdgeResolver;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);






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


64


65



	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);

	@Before@Before	public void before() {publicvoidbefore(){		analyzer = mock(PerAccessPathMethodAnalyzer.class);analyzer=mock(PerAccessPathMethodAnalyzer.class);




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



		joinStmt = new Statement("joinStmt");






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


66


		joinStmt = new Statement("joinStmt");

		joinStmt = new Statement("joinStmt");joinStmt=newStatement("joinStmt");




adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




67


68



		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());






adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015



adapting testcases to changed behavior

 

adapting testcases to changed behavior

Johannes Lerch
committed
Jul 09, 2015


67


68


		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, 
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());

		sut = new ControlFlowJoinResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, joinStmt, sut=newControlFlowJoinResolver<String,TestFact,Statement,TestMethod>(mock(FactMergeHandler.class),analyzer,joinStmt,				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());newDebugger.NullDebugger<String,TestFact,Statement,TestMethod>());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69


70


71


72


73


74


75



		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


69


70


71


72


73


74


75


		fact = new TestFact("value");
		callback = mock(InterestCallback.class);
		callEdgeResolver = mock(CallEdgeResolver.class);
	}

	@Test
	public void emptyIncomingFact() {

		fact = new TestFact("value");fact=newTestFact("value");		callback = mock(InterestCallback.class);callback=mock(InterestCallback.class);		callEdgeResolver = mock(CallEdgeResolver.class);callEdgeResolver=mock(CallEdgeResolver.class);	}}	@Test@Test	public void emptyIncomingFact() {publicvoidemptyIncomingFact(){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




76


77



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


76


77


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));
		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver));sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver));		verify(analyzer).processFlowFromJoinStmt(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(joinStmt, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));verify(analyzer).processFlowFromJoinStmt(eq(newWrappedFactAtStatement<String,TestFact,Statement,TestMethod>(joinStmt,newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),sut))));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




78


79


80


81


82


83



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


78


79


80


81


82


83


		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);

		assertTrue(sut.isInterestGiven());assertTrue(sut.isInterestGiven());	}}	@Test@Test	public void resolveViaIncomingFact() {publicvoidresolveViaIncomingFact(){		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




84



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


84


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver));sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath("a"),callEdgeResolver));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




85


86


87


88


89



		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


85


86


87


88


89


		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {

		verify(callback).interest(eq(analyzer), argThat(new ResolverArgumentMatcher(createAccessPath("a"))));verify(callback).interest(eq(analyzer),argThat(newResolverArgumentMatcher(createAccessPath("a"))));	}}	@Test@Test	public void registerCallbackAtIncomingResolver() {publicvoidregisterCallbackAtIncomingResolver(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




90



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);






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


		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);

		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




91



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


91


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),resolver));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




92


93


94


95


96


97



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {






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


93


94


95


96


97


		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));verify(resolver).resolve(eq(getDeltaConstraint("a")),any(InterestCallback.class));	}}		@Test@Test	public void resolveViaIncomingResolver() {publicvoidresolveViaIncomingResolver(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




98


99



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


98


99


		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);

		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);finalResolver<String,TestFact,Statement,TestMethod>nestedResolver=mock(Resolver.class);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




100


101


102



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


100


101


102


		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {

		Mockito.doAnswer(new Answer(){Mockito.doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




103


104



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


103


104


				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];

				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = InterestCallback<String,TestFact,Statement,TestMethod>argCallback=						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];(InterestCallback<String,TestFact,Statement,TestMethod>)invocation.getArguments()[1];




fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




105



				argCallback.interest(analyzer, nestedResolver);






fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015



fixed interest method in Resolver

 

fixed interest method in Resolver

Johannes Lerch
committed
Apr 20, 2015


105


				argCallback.interest(analyzer, nestedResolver);

				argCallback.interest(analyzer, nestedResolver);argCallback.interest(analyzer,nestedResolver);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




106


107


108


109



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


106


107


108


109


				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		

				return null;returnnull;			}}		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));}).when(resolver).resolve(eq(getDeltaConstraint("a")),any(InterestCallback.class));		




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




110



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


110


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver));sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),resolver));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




111


112



		sut.resolve(getDeltaConstraint("a"), callback);
		






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


111


112


		sut.resolve(getDeltaConstraint("a"), callback);
		

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);		




adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




113



		verify(callback).interest(eq(analyzer), eq(nestedResolver));






adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015



adapting testcases to changed behavior

 

adapting testcases to changed behavior

Johannes Lerch
committed
Jul 09, 2015


113


		verify(callback).interest(eq(analyzer), eq(nestedResolver));

		verify(callback).interest(eq(analyzer), eq(nestedResolver));verify(callback).interest(eq(analyzer),eq(nestedResolver));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




114


115


116


117



	}
	
	
	private class ResolverArgumentMatcher extends






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


114


115


116


117


	}
	
	
	private class ResolverArgumentMatcher extends

	}}			private class ResolverArgumentMatcher extendsprivateclassResolverArgumentMatcherextends




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




118



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


118


			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {

			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {ArgumentMatcher<ReturnSiteResolver<String,TestFact,Statement,TestMethod>>{




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




119










rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


119









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




120



		private AccessPath<String> accPath;






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


120


		private AccessPath<String> accPath;

		private AccessPath<String> accPath;privateAccessPath<String>accPath;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121










rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


121









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




122



		public ResolverArgumentMatcher(AccessPath<String> accPath) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


122


		public ResolverArgumentMatcher(AccessPath<String> accPath) {

		public ResolverArgumentMatcher(AccessPath<String> accPath) {publicResolverArgumentMatcher(AccessPath<String>accPath){




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
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


			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;
			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);
		}
	}
}
			this.accPath = accPath;this.accPath=accPath;		}}		@Override@Override		public boolean matches(Object argument) {publicbooleanmatches(Objectargument){			ControlFlowJoinResolver resolver = (ControlFlowJoinResolver) argument;ControlFlowJoinResolverresolver=(ControlFlowJoinResolver)argument;			return resolver.isInterestGiven() && resolver.getResolvedAccessPath().equals(accPath) && resolver.getJoinStmt().equals(joinStmt);returnresolver.isInterestGiven()&&resolver.getResolvedAccessPath().equals(accPath)&&resolver.getJoinStmt().equals(joinStmt);		}}	}}}}





