



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

e4ca369d04e36c72c71c3a908e7beeb8a25f90a0

















e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Switch branch/tag










heros


src


heros


fieldsens


ControlFlowJoinResolver.java



Find file
Normal viewHistoryPermalink






ControlFlowJoinResolver.java



3.66 KB









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












renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17












refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25












merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



	}
	








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



	}








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
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

e4ca369d04e36c72c71c3a908e7beeb8a25f90a0

















e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Switch branch/tag










heros


src


heros


fieldsens


ControlFlowJoinResolver.java



Find file
Normal viewHistoryPermalink






ControlFlowJoinResolver.java



3.66 KB









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












renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17












refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25












merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



	}
	








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



	}








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
	}
}












Open sidebar



Joshua Garcia heros

e4ca369d04e36c72c71c3a908e7beeb8a25f90a0







Open sidebar



Joshua Garcia heros

e4ca369d04e36c72c71c3a908e7beeb8a25f90a0




Open sidebar

Joshua Garcia heros

e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Joshua Garciaherosheros
e4ca369d04e36c72c71c3a908e7beeb8a25f90a0










e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Switch branch/tag










heros


src


heros


fieldsens


ControlFlowJoinResolver.java



Find file
Normal viewHistoryPermalink






ControlFlowJoinResolver.java



3.66 KB









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












renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17












refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25












merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



	}
	








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



	}








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
	}
}















e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Switch branch/tag










heros


src


heros


fieldsens


ControlFlowJoinResolver.java



Find file
Normal viewHistoryPermalink






ControlFlowJoinResolver.java



3.66 KB









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












renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17












refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25












merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



	}
	








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



	}








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
	}
}











e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Switch branch/tag










heros


src


heros


fieldsens


ControlFlowJoinResolver.java



Find file
Normal viewHistoryPermalink




e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Switch branch/tag










heros


src


heros


fieldsens


ControlFlowJoinResolver.java





e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Switch branch/tag








e4ca369d04e36c72c71c3a908e7beeb8a25f90a0


Switch branch/tag





e4ca369d04e36c72c71c3a908e7beeb8a25f90a0

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

fieldsens

ControlFlowJoinResolver.java
Find file
Normal viewHistoryPermalink




ControlFlowJoinResolver.java



3.66 KB









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












renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17












refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25












merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



	}
	








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



	}








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
	}
}









ControlFlowJoinResolver.java



3.66 KB










ControlFlowJoinResolver.java



3.66 KB









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












renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17












refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25












merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



	}
	








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



	}








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
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












renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17












refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25












merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



	}
	








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



	}








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
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












renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



import heros.fieldsens.AccessPath.Delta;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17












refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25












merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



	}
	








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



	}








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {








fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



	}
	








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {








merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
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









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13



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


13


import heros.fieldsens.AccessPath.Delta;

import heros.fieldsens.AccessPath.Delta;importheros.fieldsens.AccessPath.Delta;




restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14


15


16



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


14


15


16


import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;

import heros.fieldsens.structs.DeltaConstraint;importheros.fieldsens.structs.DeltaConstraint;import heros.fieldsens.structs.WrappedFact;importheros.fieldsens.structs.WrappedFact;import heros.fieldsens.structs.WrappedFactAtStatement;importheros.fieldsens.structs.WrappedFactAtStatement;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




17










rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


17









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




18



public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


18


public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {

public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {publicclassControlFlowJoinResolver<Field,Fact,Stmt,Method>extendsResolverTemplate<Field,Fact,Stmt,Method,WrappedFact<Field,Fact,Stmt,Method>>{




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




19


20


21


22




	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


19


20


21


22



	private Stmt joinStmt;
	private AccessPath<Field> resolvedAccPath;
	private boolean propagated = false;

	private Stmt joinStmt;privateStmtjoinStmt;	private AccessPath<Field> resolvedAccPath;privateAccessPath<Field>resolvedAccPath;	private boolean propagated = false;privatebooleanpropagated=false;




merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




23


24



	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;






merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015



merging incoming facts

 

merging incoming facts

Johannes Lerch
committed
Jun 10, 2015


23


24


	private Fact sourceFact;
	private FactMergeHandler<Fact> factMergeHandler;

	private Fact sourceFact;privateFactsourceFact;	private FactMergeHandler<Fact> factMergeHandler;privateFactMergeHandler<Fact>factMergeHandler;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




25










rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


25









merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




26


27


28



	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;






merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015



merging incoming facts

 

merging incoming facts

Johannes Lerch
committed
Jun 10, 2015


26


27


28


	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {
		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);
		this.factMergeHandler = factMergeHandler;

	public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt) {publicControlFlowJoinResolver(FactMergeHandler<Fact>factMergeHandler,PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer,StmtjoinStmt){		this(factMergeHandler, analyzer, joinStmt, null, new AccessPath<Field>(), null);this(factMergeHandler,analyzer,joinStmt,null,newAccessPath<Field>(),null);		this.factMergeHandler = factMergeHandler;this.factMergeHandler=factMergeHandler;




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




29



		propagated=false;






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


29


		propagated=false;

		propagated=false;propagated=false;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




30


31



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


30


31


	}
	

	}}	




merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




32


33



	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {






merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015



merging incoming facts

 

merging incoming facts

Johannes Lerch
committed
Jun 10, 2015


32


33


	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {

	private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, privateControlFlowJoinResolver(FactMergeHandler<Fact>factMergeHandler,PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer,			Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {StmtjoinStmt,FactsourceFact,AccessPath<Field>resolvedAccPath,ControlFlowJoinResolver<Field,Fact,Stmt,Method>parent){




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




34



		super(analyzer, parent);






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


34


		super(analyzer, parent);

		super(analyzer, parent);super(analyzer,parent);




merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




35



		this.factMergeHandler = factMergeHandler;






merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015



merging incoming facts

 

merging incoming facts

Johannes Lerch
committed
Jun 10, 2015


35


		this.factMergeHandler = factMergeHandler;

		this.factMergeHandler = factMergeHandler;this.factMergeHandler=factMergeHandler;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




36



		this.joinStmt = joinStmt;






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


36


		this.joinStmt = joinStmt;

		this.joinStmt = joinStmt;this.joinStmt=joinStmt;




merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




37



		this.sourceFact = sourceFact;






merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015



merging incoming facts

 

merging incoming facts

Johannes Lerch
committed
Jun 10, 2015


37


		this.sourceFact = sourceFact;

		this.sourceFact = sourceFact;this.sourceFact=sourceFact;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




38



		this.resolvedAccPath = resolvedAccPath;






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


		this.resolvedAccPath = resolvedAccPath;

		this.resolvedAccPath = resolvedAccPath;this.resolvedAccPath=resolvedAccPath;




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




39



		propagated=true;






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


39


		propagated=true;

		propagated=true;propagated=true;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




40


41



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


40


41


	}
	

	}}	




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




42


43


44



	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


42


43


44


	@Override
	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
		return inc.getAccessPath();

	@Override@Override	protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {protectedAccessPath<Field>getAccessPathOf(WrappedFact<Field,Fact,Stmt,Method>inc){		return inc.getAccessPath();returninc.getAccessPath();




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




45



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


45


	}

	}}




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




46










refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


46









restructuring

 


Johannes Lerch
committed
Jun 01, 2015




47



	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {






restructuring

 


Johannes Lerch
committed
Jun 01, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Jun 01, 2015


47


	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {

	protected void processIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method> fact) {protectedvoidprocessIncomingGuaranteedPrefix(heros.fieldsens.structs.WrappedFact<Field,Fact,Stmt,Method>fact){




merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




48


49


50


51



		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {






merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015



merging incoming facts

 

merging incoming facts

Johannes Lerch
committed
Jun 10, 2015


48


49


50


51


		if(propagated) {
			factMergeHandler.merge(sourceFact, fact.getFact());
		}
		else {

		if(propagated) {if(propagated){			factMergeHandler.merge(sourceFact, fact.getFact());factMergeHandler.merge(sourceFact,fact.getFact());		}}		else {else{




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




52



			propagated=true;






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


52


			propagated=true;

			propagated=true;propagated=true;




merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




53



			sourceFact = fact.getFact();






merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015



merging incoming facts

 

merging incoming facts

Johannes Lerch
committed
Jun 10, 2015


53


			sourceFact = fact.getFact();

			sourceFact = fact.getFact();sourceFact=fact.getFact();




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




54



			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


54


			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(

			analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<Field, Fact, Stmt, Method>(joinStmt, new WrappedFact<Field, Fact, Stmt, Method>(analyzer.processFlowFromJoinStmt(newWrappedFactAtStatement<Field,Fact,Stmt,Method>(joinStmt,newWrappedFact<Field,Fact,Stmt,Method>(




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




55


56


57



					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


55


56


57


					fact.getFact(), new AccessPath<Field>(), this)));
		}
	};

					fact.getFact(), new AccessPath<Field>(), this)));fact.getFact(),newAccessPath<Field>(),this)));		}}	};};




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




58


59



	
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


58


59


	
	@Override

		@Override@Override




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




60


61


62



	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


60


61


62


	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
		lock();
		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);

	protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {protectedvoidprocessIncomingPotentialPrefix(WrappedFact<Field,Fact,Stmt,Method>fact){		lock();lock();		Delta<Field> delta = fact.getAccessPath().getDeltaTo(resolvedAccPath);Delta<Field>delta=fact.getAccessPath().getDeltaTo(resolvedAccPath);




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




63



		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


63


		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {

		fact.getResolver().resolve(new DeltaConstraint<Field>(delta), new InterestCallback<Field, Fact, Stmt, Method>() {fact.getResolver().resolve(newDeltaConstraint<Field>(delta),newInterestCallback<Field,Fact,Stmt,Method>(){




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




64


65


66



			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


64


65


66


			@Override
			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, 
					Resolver<Field, Fact, Stmt, Method> resolver) {

			@Override@Override			public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, publicvoidinterest(PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer,					Resolver<Field, Fact, Stmt, Method> resolver) {Resolver<Field,Fact,Stmt,Method>resolver){




fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015




67



				ControlFlowJoinResolver.this.interest();






fixed interest method in Resolver

 


Johannes Lerch
committed
Apr 20, 2015



fixed interest method in Resolver

 

fixed interest method in Resolver

Johannes Lerch
committed
Apr 20, 2015


67


				ControlFlowJoinResolver.this.interest();

				ControlFlowJoinResolver.this.interest();ControlFlowJoinResolver.this.interest();




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




68


69


70


71


72


73


74


75



			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


68


69


70


71


72


73


74


75


			}

			@Override
			public void canBeResolvedEmpty() {
				ControlFlowJoinResolver.this.canBeResolvedEmpty();
			}
		});
		unlock();

			}}			@Override@Override			public void canBeResolvedEmpty() {publicvoidcanBeResolvedEmpty(){				ControlFlowJoinResolver.this.canBeResolvedEmpty();ControlFlowJoinResolver.this.canBeResolvedEmpty();			}}		});});		unlock();unlock();




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




76


77



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


76


77


	}
	

	}}	




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




78


79



	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


78


79


	@Override
	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {

	@Override@Override	protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {protectedResolverTemplate<Field,Fact,Stmt,Method,WrappedFact<Field,Fact,Stmt,Method>>createNestedResolver(AccessPath<Field>newAccPath){




merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015




80



		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);






merging incoming facts

 


Johannes Lerch
committed
Jun 10, 2015



merging incoming facts

 

merging incoming facts

Johannes Lerch
committed
Jun 10, 2015


80


		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);

		return new ControlFlowJoinResolver<Field, Fact, Stmt, Method>(factMergeHandler, analyzer, joinStmt, sourceFact, newAccPath, this);returnnewControlFlowJoinResolver<Field,Fact,Stmt,Method>(factMergeHandler,analyzer,joinStmt,sourceFact,newAccPath,this);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
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


	}

	@Override
	protected void log(String message) {
		analyzer.log("Join Stmt "+toString()+": "+message);
	}

	@Override
	public String toString() {
		return "<"+resolvedAccPath+":"+joinStmt+">";
	}


	}}	@Override@Override	protected void log(String message) {protectedvoidlog(Stringmessage){		analyzer.log("Join Stmt "+toString()+": "+message);analyzer.log("Join Stmt "+toString()+": "+message);	}}	@Override@Override	public String toString() {publicStringtoString(){		return "<"+resolvedAccPath+":"+joinStmt+">";return"<"+resolvedAccPath+":"+joinStmt+">";	}}




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




93



	@Override






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


93


	@Override

	@Override@Override




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




94


95


96


97


98


99


100


101



	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
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


94


95


96


97


98


99


100


101


	public AccessPath<Field> getResolvedAccessPath() {
		return resolvedAccPath;
	}

	public Stmt getJoinStmt() {
		return joinStmt;
	}
}
	public AccessPath<Field> getResolvedAccessPath() {publicAccessPath<Field>getResolvedAccessPath(){		return resolvedAccPath;returnresolvedAccPath;	}}	public Stmt getJoinStmt() {publicStmtgetJoinStmt(){		return joinStmt;returnjoinStmt;	}}}}





