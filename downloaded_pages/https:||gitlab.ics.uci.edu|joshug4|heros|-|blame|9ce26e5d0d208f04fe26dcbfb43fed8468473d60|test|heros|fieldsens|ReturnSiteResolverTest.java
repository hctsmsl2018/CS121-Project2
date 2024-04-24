



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

9ce26e5d0d208f04fe26dcbfb43fed8468473d60

















9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Switch branch/tag










heros


test


heros


fieldsens


ReturnSiteResolverTest.java



Find file
Normal viewHistoryPermalink






ReturnSiteResolverTest.java



13.8 KB









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












fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



import heros.fieldsens.AccessPath.Delta;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35












adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




108


109



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));









switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;








changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



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

9ce26e5d0d208f04fe26dcbfb43fed8468473d60

















9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Switch branch/tag










heros


test


heros


fieldsens


ReturnSiteResolverTest.java



Find file
Normal viewHistoryPermalink






ReturnSiteResolverTest.java



13.8 KB









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












fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



import heros.fieldsens.AccessPath.Delta;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35












adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




108


109



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));









switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;








changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



		}
	}
}












Open sidebar



Joshua Garcia heros

9ce26e5d0d208f04fe26dcbfb43fed8468473d60







Open sidebar



Joshua Garcia heros

9ce26e5d0d208f04fe26dcbfb43fed8468473d60




Open sidebar

Joshua Garcia heros

9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Joshua Garciaherosheros
9ce26e5d0d208f04fe26dcbfb43fed8468473d60










9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Switch branch/tag










heros


test


heros


fieldsens


ReturnSiteResolverTest.java



Find file
Normal viewHistoryPermalink






ReturnSiteResolverTest.java



13.8 KB









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












fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



import heros.fieldsens.AccessPath.Delta;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35












adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




108


109



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));









switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;








changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



		}
	}
}















9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Switch branch/tag










heros


test


heros


fieldsens


ReturnSiteResolverTest.java



Find file
Normal viewHistoryPermalink






ReturnSiteResolverTest.java



13.8 KB









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












fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



import heros.fieldsens.AccessPath.Delta;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35












adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




108


109



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));









switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;








changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



		}
	}
}











9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Switch branch/tag










heros


test


heros


fieldsens


ReturnSiteResolverTest.java



Find file
Normal viewHistoryPermalink




9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Switch branch/tag










heros


test


heros


fieldsens


ReturnSiteResolverTest.java





9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Switch branch/tag








9ce26e5d0d208f04fe26dcbfb43fed8468473d60


Switch branch/tag





9ce26e5d0d208f04fe26dcbfb43fed8468473d60

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

fieldsens

ReturnSiteResolverTest.java
Find file
Normal viewHistoryPermalink




ReturnSiteResolverTest.java



13.8 KB









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












fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



import heros.fieldsens.AccessPath.Delta;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35












adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




108


109



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));









switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;








changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



		}
	}
}









ReturnSiteResolverTest.java



13.8 KB










ReturnSiteResolverTest.java



13.8 KB









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












fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



import heros.fieldsens.AccessPath.Delta;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35












adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




108


109



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));









switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;








changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



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












fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



import heros.fieldsens.AccessPath.Delta;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35












adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




108


109



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));









switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;








changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



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












fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



import heros.fieldsens.AccessPath.Delta;








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35












adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



		return new DeltaConstraint<String>(getDelta(fieldRefs));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



	}









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



	protected static AccessPath<String> createAccessPath(String... fieldRefs) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		AccessPath<String> accPath = new AccessPath<String>();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



		for (String fieldRef : fieldRefs) {








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



			accPath = accPath.append(fieldRef);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



		}
		return accPath;
	}









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



	private TestFact fact;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



		assertTrue(sut.isInterestGiven());
	}

	@Test
	public void resolveViaIncomingFact() {
		sut.resolve(getDeltaConstraint("a"), callback);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




108


109



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



				argCallback.interest(analyzer, nestedResolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



		sut.resolve(getDeltaConstraint("a"), callback);
		








adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));









switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	








correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



		private AccessPath<String> accPath;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;








changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



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









fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




13



import static org.junit.Assert.assertEquals;






fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015



fixed interest method in Resolver

 

fixed interest method in Resolver

Johannes Lerch
committed
Apr 20, 2015


13


import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertEquals;importstaticorg.junit.Assert.assertEquals;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




14



import static org.junit.Assert.assertTrue;






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


14


import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertTrue;importstaticorg.junit.Assert.assertTrue;




adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




15


16


17



import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;






adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015



adapting testcases to changed behavior

 

adapting testcases to changed behavior

Johannes Lerch
committed
Jul 09, 2015


15


16


17


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

import static org.mockito.Matchers.any;importstaticorg.mockito.Matchers.any;import static org.mockito.Matchers.argThat;importstaticorg.mockito.Matchers.argThat;import static org.mockito.Matchers.eq;importstaticorg.mockito.Matchers.eq;




correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




18


19


20



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;






correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015



correct handling of ZeroCallEdgeResolver as incoming resolver on return

 

correct handling of ZeroCallEdgeResolver as incoming resolver on return

Johannes Lerch
committed
Jul 10, 2015


18


19


20


import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;importstaticorg.mockito.Mockito.RETURNS_DEEP_STUBS;import static org.mockito.Mockito.RETURNS_MOCKS;importstaticorg.mockito.Mockito.RETURNS_MOCKS;import static org.mockito.Mockito.RETURNS_SMART_NULLS;importstaticorg.mockito.Mockito.RETURNS_SMART_NULLS;




adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




21


22


23


24



import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;






adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015



adapting testcases to changed behavior

 

adapting testcases to changed behavior

Johannes Lerch
committed
Jul 09, 2015


21


22


23


24


import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.doAnswer;importstaticorg.mockito.Mockito.doAnswer;import static org.mockito.Mockito.mock;importstaticorg.mockito.Mockito.mock;import static org.mockito.Mockito.never;importstaticorg.mockito.Mockito.never;import static org.mockito.Mockito.verify;importstaticorg.mockito.Mockito.verify;




correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




25


26



import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;






correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015



correct handling of ZeroCallEdgeResolver as incoming resolver on return

 

correct handling of ZeroCallEdgeResolver as incoming resolver on return

Johannes Lerch
committed
Jul 10, 2015


25


26


import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import static org.mockito.Mockito.when;importstaticorg.mockito.Mockito.when;import static org.mockito.Mockito.withSettings;importstaticorg.mockito.Mockito.withSettings;




renaming package

 


Johannes Lerch
committed
Jun 01, 2015




27



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


27


import heros.fieldsens.AccessPath.Delta;

import heros.fieldsens.AccessPath.Delta;importheros.fieldsens.AccessPath.Delta;




correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




28



import heros.fieldsens.FlowFunction.Constraint;






correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015



correct handling of ZeroCallEdgeResolver as incoming resolver on return

 

correct handling of ZeroCallEdgeResolver as incoming resolver on return

Johannes Lerch
committed
Jul 10, 2015


28


import heros.fieldsens.FlowFunction.Constraint;

import heros.fieldsens.FlowFunction.Constraint;importheros.fieldsens.FlowFunction.Constraint;




restructuring

 


Johannes Lerch
committed
Jun 01, 2015




29


30


31



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


29


30


31


import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;

import heros.fieldsens.structs.DeltaConstraint;importheros.fieldsens.structs.DeltaConstraint;import heros.fieldsens.structs.WrappedFact;importheros.fieldsens.structs.WrappedFact;import heros.fieldsens.structs.WrappedFactAtStatement;importheros.fieldsens.structs.WrappedFactAtStatement;




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




32


33


34



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


32


33


34


import heros.utilities.Statement;
import heros.utilities.TestFact;
import heros.utilities.TestMethod;

import heros.utilities.Statement;importheros.utilities.Statement;import heros.utilities.TestFact;importheros.utilities.TestFact;import heros.utilities.TestMethod;importheros.utilities.TestMethod;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




35










rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


35









adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




36


37



import java.util.List;







adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015



adapting testcases to changed behavior

 

adapting testcases to changed behavior

Johannes Lerch
committed
Jul 09, 2015


36


37


import java.util.List;


import java.util.List;importjava.util.List;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38


39


40


41


42


43



import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


38


39


40


41


42


43


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.junit.Before;importorg.junit.Before;import org.junit.Test;importorg.junit.Test;import org.mockito.ArgumentMatcher;importorg.mockito.ArgumentMatcher;import org.mockito.Mockito;importorg.mockito.Mockito;import org.mockito.invocation.InvocationOnMock;importorg.mockito.invocation.InvocationOnMock;import org.mockito.stubbing.Answer;importorg.mockito.stubbing.Answer;




fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




44


45




import com.google.common.collect.Lists;






fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015



fixed interest method in Resolver

 

fixed interest method in Resolver

Johannes Lerch
committed
Apr 20, 2015


44


45



import com.google.common.collect.Lists;

import com.google.common.collect.Lists;importcom.google.common.collect.Lists;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




46


47


48




public class ReturnSiteResolverTest {







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


48



public class ReturnSiteResolverTest {


public class ReturnSiteResolverTest {publicclassReturnSiteResolverTest{




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




49



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


49


	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {

	private static DeltaConstraint<String> getDeltaConstraint(String... fieldRefs) {privatestaticDeltaConstraint<String>getDeltaConstraint(String...fieldRefs){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




50



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


50


		return new DeltaConstraint<String>(getDelta(fieldRefs));

		return new DeltaConstraint<String>(getDelta(fieldRefs));returnnewDeltaConstraint<String>(getDelta(fieldRefs));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




51


52



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


51


52


	}


	}}




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




53


54


55



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


53


54


55


	private static Delta<String> getDelta(String... fieldRefs) {
		AccessPath<String> accPath = createAccessPath(fieldRefs);
		return new AccessPath<String>().getDeltaTo(accPath);

	private static Delta<String> getDelta(String... fieldRefs) {privatestaticDelta<String>getDelta(String...fieldRefs){		AccessPath<String> accPath = createAccessPath(fieldRefs);AccessPath<String>accPath=createAccessPath(fieldRefs);		return new AccessPath<String>().getDeltaTo(accPath);returnnewAccessPath<String>().getDeltaTo(accPath);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




56


57



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


56


57


	}


	}}




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




58



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


58


	protected static AccessPath<String> createAccessPath(String... fieldRefs) {

	protected static AccessPath<String> createAccessPath(String... fieldRefs) {protectedstaticAccessPath<String>createAccessPath(String...fieldRefs){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



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


59


		AccessPath<String> accPath = new AccessPath<String>();

		AccessPath<String> accPath = new AccessPath<String>();AccessPath<String>accPath=newAccessPath<String>();




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




60



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


60


		for (String fieldRef : fieldRefs) {

		for (String fieldRef : fieldRefs) {for(StringfieldRef:fieldRefs){




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




61



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


61


			accPath = accPath.append(fieldRef);

			accPath = accPath.append(fieldRef);accPath=accPath.append(fieldRef);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




62


63


64


65



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


62


63


64


65


		}
		return accPath;
	}


		}}		return accPath;returnaccPath;	}}




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66


67


68



	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;






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


67


68


	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;
	private Statement returnSite;
	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;

	private PerAccessPathMethodAnalyzer<String, TestFact, Statement, TestMethod> analyzer;privatePerAccessPathMethodAnalyzer<String,TestFact,Statement,TestMethod>analyzer;	private Statement returnSite;privateStatementreturnSite;	private ReturnSiteResolver<String, TestFact, Statement, TestMethod> sut;privateReturnSiteResolver<String,TestFact,Statement,TestMethod>sut;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




69



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


69


	private TestFact fact;

	private TestFact fact;privateTestFactfact;




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




70


71



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


70


71


	private InterestCallback<String, TestFact, Statement, TestMethod> callback;
	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;

	private InterestCallback<String, TestFact, Statement, TestMethod> callback;privateInterestCallback<String,TestFact,Statement,TestMethod>callback;	private Resolver<String, TestFact, Statement, TestMethod> callEdgeResolver;privateResolver<String,TestFact,Statement,TestMethod>callEdgeResolver;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




72


73


74


75




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


72


73


74


75



	@Before
	public void before() {
		analyzer = mock(PerAccessPathMethodAnalyzer.class);

	@Before@Before	public void before() {publicvoidbefore(){		analyzer = mock(PerAccessPathMethodAnalyzer.class);analyzer=mock(PerAccessPathMethodAnalyzer.class);




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




76



		returnSite = new Statement("returnSite");






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


		returnSite = new Statement("returnSite");

		returnSite = new Statement("returnSite");returnSite=newStatement("returnSite");




adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




77


78



		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
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


77


78


		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,
				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());

		sut = new ReturnSiteResolver<String, TestFact, Statement, TestMethod>(mock(FactMergeHandler.class), analyzer, returnSite,sut=newReturnSiteResolver<String,TestFact,Statement,TestMethod>(mock(FactMergeHandler.class),analyzer,returnSite,				new Debugger.NullDebugger<String, TestFact, Statement, TestMethod>());newDebugger.NullDebugger<String,TestFact,Statement,TestMethod>());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




79


80


81


82


83


84


85



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


79


80


81


82


83


84


85


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




86


87



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


86


87


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());
		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver),callEdgeResolver,getDelta());		verify(analyzer).scheduleEdgeTo(eq(new WrappedFactAtStatement<String, TestFact, Statement, TestMethod>(returnSite, new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), sut))));verify(analyzer).scheduleEdgeTo(eq(newWrappedFactAtStatement<String,TestFact,Statement,TestMethod>(returnSite,newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),sut))));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




88


89


90


91


92


93



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


88


89


90


91


92


93


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




94



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


94


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath("a"), callEdgeResolver), callEdgeResolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath("a"),callEdgeResolver),callEdgeResolver,getDelta());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




95


96


97


98


99



		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
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


95


96


97


98


99


		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}

	@Test
	public void registerCallbackAtIncomingResolver() {

		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));verify(callback).interest(eq(analyzer),argThat(newReturnSiteResolverArgumentMatcher(createAccessPath("a"))));	}}	@Test@Test	public void registerCallbackAtIncomingResolver() {publicvoidregisterCallbackAtIncomingResolver(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




100



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


100


		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);

		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




101



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


101


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),resolver),callEdgeResolver,getDelta());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




102


103


104


105


106


107



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


102


103


104


105


106


107


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




108


109



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


108


109


		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);

		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);finalResolver<String,TestFact,Statement,TestMethod>nestedResolver=mock(Resolver.class);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




110


111


112



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


110


111


112


		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {

		Mockito.doAnswer(new Answer(){Mockito.doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




113


114



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


113


114


				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];

				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = InterestCallback<String,TestFact,Statement,TestMethod>argCallback=						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];(InterestCallback<String,TestFact,Statement,TestMethod>)invocation.getArguments()[1];




fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




115



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


115


				argCallback.interest(analyzer, nestedResolver);

				argCallback.interest(analyzer, nestedResolver);argCallback.interest(analyzer,nestedResolver);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




116


117


118


119



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


116


117


118


119


				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		

				return null;returnnull;			}}		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));}).when(resolver).resolve(eq(getDeltaConstraint("a")),any(InterestCallback.class));		




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




120



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


120


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),resolver),callEdgeResolver,getDelta());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




121


122


123


124


125



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
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


121


122


123


124


125


		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);				verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));verify(callback).interest(eq(analyzer),argThat(newReturnSiteResolverArgumentMatcher(createAccessPath("a"))));	}}	




fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		






fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015



fixed interest method in Resolver

 

fixed interest method in Resolver

Johannes Lerch
committed
Apr 20, 2015


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


	@Test
	public void resolveViaLateInterestAtIncomingResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);
		final List<InterestCallback> callbacks = Lists.newLinkedList();
		
		Mockito.doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = 
						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];
				callbacks.add(argCallback);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		

	@Test@Test	public void resolveViaLateInterestAtIncomingResolver() {publicvoidresolveViaLateInterestAtIncomingResolver(){		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);finalResolver<String,TestFact,Statement,TestMethod>nestedResolver=mock(Resolver.class);		final List<InterestCallback> callbacks = Lists.newLinkedList();finalList<InterestCallback>callbacks=Lists.newLinkedList();				Mockito.doAnswer(new Answer(){Mockito.doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{				InterestCallback<String, TestFact, Statement, TestMethod> argCallback = InterestCallback<String,TestFact,Statement,TestMethod>argCallback=						(InterestCallback<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];(InterestCallback<String,TestFact,Statement,TestMethod>)invocation.getArguments()[1];				callbacks.add(argCallback);callbacks.add(argCallback);				return null;returnnull;			}}		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));}).when(resolver).resolve(eq(getDeltaConstraint("a")),any(InterestCallback.class));		




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




142



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


142


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),resolver),callEdgeResolver,getDelta());




fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




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



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	






fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015



fixed interest method in Resolver

 

fixed interest method in Resolver

Johannes Lerch
committed
Apr 20, 2015


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


		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		
		assertEquals(1, callbacks.size());
		Resolver transitiveResolver = mock(Resolver.class);
		callbacks.get(0).interest(analyzer, transitiveResolver);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);				verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));verify(callback,never()).interest(any(PerAccessPathMethodAnalyzer.class),any(Resolver.class));				assertEquals(1, callbacks.size());assertEquals(1,callbacks.size());		Resolver transitiveResolver = mock(Resolver.class);ResolvertransitiveResolver=mock(Resolver.class);		callbacks.get(0).interest(analyzer, transitiveResolver);callbacks.get(0).interest(analyzer,transitiveResolver);		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));verify(callback).interest(eq(analyzer),argThat(newReturnSiteResolverArgumentMatcher(createAccessPath("a"))));	}}	




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




153


154



	@Test
	public void resolveViaDelta() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


153


154


	@Test
	public void resolveViaDelta() {

	@Test@Test	public void resolveViaDelta() {publicvoidresolveViaDelta(){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




155



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


155


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver),callEdgeResolver,getDelta("a"));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




156


157


158


159


160


161



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


156


157


158


159


160


161


		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
	}
	
	@Test
	public void resolveViaDeltaTwice() {

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);		verify(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));verify(callback).interest(eq(analyzer),argThat(newReturnSiteResolverArgumentMatcher(createAccessPath("a"))));	}}		@Test@Test	public void resolveViaDeltaTwice() {publicvoidresolveViaDeltaTwice(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




162



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


162


		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);

		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);finalInterestCallback<String,TestFact,Statement,TestMethod>innerCallback=mock(InterestCallback.class);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




163


164


165



		doAnswer(new Answer() {
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


163


164


165


		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {

		doAnswer(new Answer() {doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




166



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


166


				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];

				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];ReturnSiteResolver<String,TestFact,Statement,TestMethod>resolver=(ReturnSiteResolver<String,TestFact,Statement,TestMethod>)invocation.getArguments()[1];




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




167


168


169


170


171



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


167


168


169


170


171


				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		

				resolver.resolve(getDeltaConstraint("b"), innerCallback);resolver.resolve(getDeltaConstraint("b"),innerCallback);				return null;returnnull;			}}		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));}).when(callback).interest(eq(analyzer),argThat(newReturnSiteResolverArgumentMatcher(createAccessPath("a"))));		




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




172



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


172


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a", "b"));sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver),callEdgeResolver,getDelta("a","b"));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




173


174


175


176


177


178


179



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


173


174


175


176


177


178


179


		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));
	}
	
	@Test
	public void resolveViaDeltaAndThenViaCallSite() {

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);				verify(innerCallback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a", "b"))));verify(innerCallback).interest(eq(analyzer),argThat(newReturnSiteResolverArgumentMatcher(createAccessPath("a","b"))));	}}		@Test@Test	public void resolveViaDeltaAndThenViaCallSite() {publicvoidresolveViaDeltaAndThenViaCallSite(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




180



		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


180


		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);

		final InterestCallback<String, TestFact, Statement, TestMethod> innerCallback = mock(InterestCallback.class);finalInterestCallback<String,TestFact,Statement,TestMethod>innerCallback=mock(InterestCallback.class);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




181


182


183



		doAnswer(new Answer() {
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


181


182


183


		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {

		doAnswer(new Answer() {doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




184



				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


184


				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];

				ReturnSiteResolver<String, TestFact, Statement, TestMethod> resolver = (ReturnSiteResolver<String, TestFact, Statement, TestMethod>) invocation.getArguments()[1];ReturnSiteResolver<String,TestFact,Statement,TestMethod>resolver=(ReturnSiteResolver<String,TestFact,Statement,TestMethod>)invocation.getArguments()[1];




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




185


186


187


188


189



				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


185


186


187


188


189


				resolver.resolve(getDeltaConstraint("b"), innerCallback);
				return null;
			}
		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));
		

				resolver.resolve(getDeltaConstraint("b"), innerCallback);resolver.resolve(getDeltaConstraint("b"),innerCallback);				return null;returnnull;			}}		}).when(callback).interest(eq(analyzer), argThat(new ReturnSiteResolverArgumentMatcher(createAccessPath("a"))));}).when(callback).interest(eq(analyzer),argThat(newReturnSiteResolverArgumentMatcher(createAccessPath("a"))));		




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




190



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


190


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta("a"));sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver),callEdgeResolver,getDelta("a"));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




191


192


193


194


195


196



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


191


192


193


194


195


196


		sut.resolve(getDeltaConstraint("a"), callback);
		verify(innerCallback).canBeResolvedEmpty();
	}

	@Test
	public void resolveViaCallEdgeResolverAtCallSite() {

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);		verify(innerCallback).canBeResolvedEmpty();verify(innerCallback).canBeResolvedEmpty();	}}	@Test@Test	public void resolveViaCallEdgeResolverAtCallSite() {publicvoidresolveViaCallEdgeResolverAtCallSite(){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




197



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


197


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), callEdgeResolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver),callEdgeResolver,getDelta());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




198


199


200


201


202


203



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


198


199


200


201


202


203


		sut.resolve(getDeltaConstraint("a"), callback);
		verify(callback).canBeResolvedEmpty();
	}
	
	@Test
	public void resolveViaResolverAtCallSite() {

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);		verify(callback).canBeResolvedEmpty();verify(callback).canBeResolvedEmpty();	}}		@Test@Test	public void resolveViaResolverAtCallSite() {publicvoidresolveViaResolverAtCallSite(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




204



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


204


		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);

		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




205



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


205


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver),resolver,getDelta());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




206


207


208


209


210


211



		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


206


207


208


209


210


211


		sut.resolve(getDeltaConstraint("a"), callback);
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	
	@Test
	public void resolveViaResolverAtCallSiteTwice() {

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));verify(resolver).resolve(eq(getDeltaConstraint("a")),any(InterestCallback.class));	}}		@Test@Test	public void resolveViaResolverAtCallSiteTwice() {publicvoidresolveViaResolverAtCallSiteTwice(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




212


213



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


212


213


		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);

		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);		final Resolver<String, TestFact, Statement, TestMethod> nestedResolver = mock(Resolver.class);finalResolver<String,TestFact,Statement,TestMethod>nestedResolver=mock(Resolver.class);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


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


		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.interest(analyzer, nestedResolver);
				return null;
			}
		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));
		

		doAnswer(new Answer() {doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];InterestCallbackinnerCallback=(InterestCallback)invocation.getArguments()[1];				innerCallback.interest(analyzer, nestedResolver);innerCallback.interest(analyzer,nestedResolver);				return null;returnnull;			}}		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));}).when(resolver).resolve(eq(getDeltaConstraint("a")),any(InterestCallback.class));		doAnswer(new Answer() {doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];InterestCallbackinnerCallback=(InterestCallback)invocation.getArguments()[1];				innerCallback.interest(analyzer, nestedResolver);innerCallback.interest(analyzer,nestedResolver);				return null;returnnull;			}}		}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")), any(InterestCallback.class));}).when(nestedResolver).resolve(eq(getDeltaConstraint("b")),any(InterestCallback.class));		




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




231



		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


231


		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);

		final InterestCallback<String, TestFact, Statement, TestMethod> secondCallback = mock(InterestCallback.class);finalInterestCallback<String,TestFact,Statement,TestMethod>secondCallback=mock(InterestCallback.class);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




232


233


234



		doAnswer(new Answer() {
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


232


233


234


		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {

		doAnswer(new Answer() {doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{




adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




235



				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];






adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015



adapting testcases to changed behavior

 

adapting testcases to changed behavior

Johannes Lerch
committed
Jul 09, 2015


235


				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];

				Resolver<String, TestFact, Statement, TestMethod> resolver = (Resolver) invocation.getArguments()[1];Resolver<String,TestFact,Statement,TestMethod>resolver=(Resolver)invocation.getArguments()[1];




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




236


237


238


239



				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
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


236


237


238


239


				resolver.resolve(getDeltaConstraint("b"), secondCallback);
				return null;
			}
			

				resolver.resolve(getDeltaConstraint("b"), secondCallback);resolver.resolve(getDeltaConstraint("b"),secondCallback);				return null;returnnull;			}}			




adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




240



		}).when(callback).interest(eq(analyzer), eq(nestedResolver));






adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015



adapting testcases to changed behavior

 

adapting testcases to changed behavior

Johannes Lerch
committed
Jul 09, 2015


240


		}).when(callback).interest(eq(analyzer), eq(nestedResolver));

		}).when(callback).interest(eq(analyzer), eq(nestedResolver));}).when(callback).interest(eq(analyzer),eq(nestedResolver));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




241



		






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


241


		

		




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




242



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


242


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver),resolver,getDelta());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




243


244



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


243


244


		sut.resolve(getDeltaConstraint("a"), callback);
		

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);		




adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015




245



		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));






adapting testcases to changed behavior

 


Johannes Lerch
committed
Jul 09, 2015



adapting testcases to changed behavior

 

adapting testcases to changed behavior

Johannes Lerch
committed
Jul 09, 2015


245


		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));

		verify(secondCallback).interest(eq(analyzer), eq(nestedResolver));verify(secondCallback).interest(eq(analyzer),eq(nestedResolver));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




246


247


248


249



	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


246


247


248


249


	}
	
	@Test
	public void resolveAsEmptyViaIncomingResolver() {

	}}		@Test@Test	public void resolveAsEmptyViaIncomingResolver() {publicvoidresolveAsEmptyViaIncomingResolver(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




250



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


250


		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);

		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




251



		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


251


		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));

		Delta<String> delta = new AccessPath<String>().getDeltaTo(new AccessPath<String>().appendExcludedFieldReference(new String("a")));Delta<String>delta=newAccessPath<String>().getDeltaTo(newAccessPath<String>().appendExcludedFieldReference(newString("a")));




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
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


		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];
				innerCallback.canBeResolvedEmpty();
				return null;
			}
		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));


				doAnswer(new Answer() {doAnswer(newAnswer(){			@Override@Override			public Object answer(InvocationOnMock invocation) throws Throwable {publicObjectanswer(InvocationOnMockinvocation)throwsThrowable{				InterestCallback innerCallback = (InterestCallback) invocation.getArguments()[1];InterestCallbackinnerCallback=(InterestCallback)invocation.getArguments()[1];				innerCallback.canBeResolvedEmpty();innerCallback.canBeResolvedEmpty();				return null;returnnull;			}}		}).when(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));}).when(resolver).resolve(eq(getDeltaConstraint("a")),any(InterestCallback.class));




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




262



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


262


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), resolver), callEdgeResolver, delta);sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),resolver),callEdgeResolver,delta);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




263


264


265


266


267


268


269


270



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


263


264


265


266


267


268


269


270


		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(callback, never()).canBeResolvedEmpty();
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
	}
	
	@Test
	public void resolveViaCallSiteResolver() {

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);				verify(callback, never()).canBeResolvedEmpty();verify(callback,never()).canBeResolvedEmpty();		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));verify(callback,never()).interest(any(PerAccessPathMethodAnalyzer.class),any(Resolver.class));	}}		@Test@Test	public void resolveViaCallSiteResolver() {publicvoidresolveViaCallSiteResolver(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




271



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


271


		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);

		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




272



		






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


272


		

		




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




273



		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


273


		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());

		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), callEdgeResolver), resolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),callEdgeResolver),resolver,getDelta());




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




274


275


276


277


278



		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
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


274


275


276


277


278


		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));
	}
	

		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);				verify(resolver).resolve(eq(getDeltaConstraint("a")), any(InterestCallback.class));verify(resolver).resolve(eq(getDeltaConstraint("a")),any(InterestCallback.class));	}}	




correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015




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



	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	






correct handling of ZeroCallEdgeResolver as incoming resolver on return

 


Johannes Lerch
committed
Jul 10, 2015



correct handling of ZeroCallEdgeResolver as incoming resolver on return

 

correct handling of ZeroCallEdgeResolver as incoming resolver on return

Johannes Lerch
committed
Jul 10, 2015


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


	@Test
	public void incomingZeroCallEdgeResolver() {
		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);
		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); 
		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());
		sut.resolve(getDeltaConstraint("a"), callback);
		
		verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));
		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));
		verify(callback, never()).canBeResolvedEmpty();
	}
	

	@Test@Test	public void incomingZeroCallEdgeResolver() {publicvoidincomingZeroCallEdgeResolver(){		Resolver<String, TestFact, Statement, TestMethod> resolver = mock(Resolver.class);Resolver<String,TestFact,Statement,TestMethod>resolver=mock(Resolver.class);		ZeroCallEdgeResolver<String, TestFact, Statement, TestMethod> zeroResolver = mock(ZeroCallEdgeResolver.class); ZeroCallEdgeResolver<String,TestFact,Statement,TestMethod>zeroResolver=mock(ZeroCallEdgeResolver.class);		sut.addIncoming(new WrappedFact<String, TestFact, Statement, TestMethod>(fact, createAccessPath(), zeroResolver), resolver, getDelta());sut.addIncoming(newWrappedFact<String,TestFact,Statement,TestMethod>(fact,createAccessPath(),zeroResolver),resolver,getDelta());		sut.resolve(getDeltaConstraint("a"), callback);sut.resolve(getDeltaConstraint("a"),callback);				verify(resolver, never()).resolve(any(Constraint.class), any(InterestCallback.class));verify(resolver,never()).resolve(any(Constraint.class),any(InterestCallback.class));		verify(callback, never()).interest(any(PerAccessPathMethodAnalyzer.class), any(Resolver.class));verify(callback,never()).interest(any(PerAccessPathMethodAnalyzer.class),any(Resolver.class));		verify(callback, never()).canBeResolvedEmpty();verify(callback,never()).canBeResolvedEmpty();	}}	




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




291



	private class ReturnSiteResolverArgumentMatcher extends






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


	private class ReturnSiteResolverArgumentMatcher extends

	private class ReturnSiteResolverArgumentMatcher extendsprivateclassReturnSiteResolverArgumentMatcherextends




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




292



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


292


			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {

			ArgumentMatcher<ReturnSiteResolver<String, TestFact, Statement, TestMethod>> {ArgumentMatcher<ReturnSiteResolver<String,TestFact,Statement,TestMethod>>{




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




293










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









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




294



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


294


		private AccessPath<String> accPath;

		private AccessPath<String> accPath;privateAccessPath<String>accPath;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




295










rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


295









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




296



		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


296


		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {

		public ReturnSiteResolverArgumentMatcher(AccessPath<String> accPath) {publicReturnSiteResolverArgumentMatcher(AccessPath<String>accPath){




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




297


298


299


300


301


302



			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


297


298


299


300


301


302


			this.accPath = accPath;
		}

		@Override
		public boolean matches(Object argument) {
			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;

			this.accPath = accPath;this.accPath=accPath;		}}		@Override@Override		public boolean matches(Object argument) {publicbooleanmatches(Objectargument){			ReturnSiteResolver resolver = (ReturnSiteResolver) argument;ReturnSiteResolverresolver=(ReturnSiteResolver)argument;




changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015




303



			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);






changing nested resolver trees to graphs + test cases

 


Johannes Lerch
committed
Jul 17, 2015



changing nested resolver trees to graphs + test cases

 

changing nested resolver trees to graphs + test cases

Johannes Lerch
committed
Jul 17, 2015


303


			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);

			return resolver.isInterestGiven() && resolver.resolvedAccessPath.equals(accPath) && resolver.getReturnSite().equals(returnSite);returnresolver.isInterestGiven()&&resolver.resolvedAccessPath.equals(accPath)&&resolver.getReturnSite().equals(returnSite);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




304


305


306



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


304


305


306


		}
	}
}
		}}	}}}}





