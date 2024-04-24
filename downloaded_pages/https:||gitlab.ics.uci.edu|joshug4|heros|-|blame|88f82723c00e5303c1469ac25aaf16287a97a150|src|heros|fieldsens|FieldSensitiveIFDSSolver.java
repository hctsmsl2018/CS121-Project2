



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

88f82723c00e5303c1469ac25aaf16287a97a150

















88f82723c00e5303c1469ac25aaf16287a97a150


Switch branch/tag










heros


src


heros


fieldsens


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



3 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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












edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18












bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37












fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



	}








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



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

88f82723c00e5303c1469ac25aaf16287a97a150

















88f82723c00e5303c1469ac25aaf16287a97a150


Switch branch/tag










heros


src


heros


fieldsens


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



3 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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












edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18












bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37












fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



	}








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



		}
	}
}












Open sidebar



Joshua Garcia heros

88f82723c00e5303c1469ac25aaf16287a97a150







Open sidebar



Joshua Garcia heros

88f82723c00e5303c1469ac25aaf16287a97a150




Open sidebar

Joshua Garcia heros

88f82723c00e5303c1469ac25aaf16287a97a150


Joshua Garciaherosheros
88f82723c00e5303c1469ac25aaf16287a97a150










88f82723c00e5303c1469ac25aaf16287a97a150


Switch branch/tag










heros


src


heros


fieldsens


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



3 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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












edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18












bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37












fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



	}








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



		}
	}
}















88f82723c00e5303c1469ac25aaf16287a97a150


Switch branch/tag










heros


src


heros


fieldsens


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink






FieldSensitiveIFDSSolver.java



3 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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












edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18












bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37












fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



	}








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



		}
	}
}











88f82723c00e5303c1469ac25aaf16287a97a150


Switch branch/tag










heros


src


heros


fieldsens


FieldSensitiveIFDSSolver.java



Find file
Normal viewHistoryPermalink




88f82723c00e5303c1469ac25aaf16287a97a150


Switch branch/tag










heros


src


heros


fieldsens


FieldSensitiveIFDSSolver.java





88f82723c00e5303c1469ac25aaf16287a97a150


Switch branch/tag








88f82723c00e5303c1469ac25aaf16287a97a150


Switch branch/tag





88f82723c00e5303c1469ac25aaf16287a97a150

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

fieldsens

FieldSensitiveIFDSSolver.java
Find file
Normal viewHistoryPermalink




FieldSensitiveIFDSSolver.java



3 KB









Newer










Older









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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












edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18












bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37












fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



	}








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



		}
	}
}









FieldSensitiveIFDSSolver.java



3 KB










FieldSensitiveIFDSSolver.java



3 KB









Newer










Older
NewerOlder







fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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












edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18












bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37












fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



	}








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



		}
	}
}











fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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












edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18












bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37












fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



	}








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



		}
	}
}









fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************








fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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








fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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












edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15












FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;








edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18












bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37












fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



	}








bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);








"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



		}
	}
}







fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




1



﻿/*******************************************************************************






fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015



fixed characters that made the build server fail

 

fixed characters that made the build server fail

Steven Arzt
committed
Jun 03, 2015


1


﻿/*******************************************************************************

﻿/*******************************************************************************﻿/*******************************************************************************




fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015




2



 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.






fixed more non-ASCII characters

 


Steven Arzt
committed
Jun 03, 2015



fixed more non-ASCII characters

 

fixed more non-ASCII characters

Steven Arzt
committed
Jun 03, 2015


2


 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.

 * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth. * Copyright (c) 2014 Johannes Lerch, Johannes Spaeth.




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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

 * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors:




fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015




9



 *     Johannes Lerch, Johannes Spaeth - initial API and implementation






fixed characters that made the build server fail

 


Steven Arzt
committed
Jun 03, 2015



fixed characters that made the build server fail

 

fixed characters that made the build server fail

Steven Arzt
committed
Jun 03, 2015


9


 *     Johannes Lerch, Johannes Spaeth - initial API and implementation

 *     Johannes Lerch, Johannes Spaeth - initial API and implementation *     Johannes Lerch, Johannes Spaeth - initial API and implementation




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




10



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


10


 ******************************************************************************/

 ******************************************************************************/ ******************************************************************************/




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









edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




13



import heros.InterproceduralCFG;






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


13


import heros.InterproceduralCFG;

import heros.InterproceduralCFG;importheros.InterproceduralCFG;




restructuring

 


Johannes Lerch
committed
Jun 01, 2015




14



import heros.utilities.DefaultValueMap;






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


import heros.utilities.DefaultValueMap;

import heros.utilities.DefaultValueMap;importheros.utilities.DefaultValueMap;




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




15










edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


15









FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




16



import java.util.Map.Entry;






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


import java.util.Map.Entry;

import java.util.Map.Entry;importjava.util.Map.Entry;




edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014




17



import java.util.Set;






edges on hold + resume

 


Johannes Lerch
committed
Nov 25, 2014



edges on hold + resume

 

edges on hold + resume

Johannes Lerch
committed
Nov 25, 2014


17


import java.util.Set;

import java.util.Set;importjava.util.Set;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




18










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









bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


19


20


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;importorg.slf4j.Logger;import org.slf4j.LoggerFactory;importorg.slf4j.LoggerFactory;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




21










FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


21









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




22



public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


22


public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {

public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {publicclassFieldSensitiveIFDSSolver<FieldRef,D,N,M,IextendsInterproceduralCFG<N,M>>{




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




23


24




	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


23


24



	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);

	protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);protectedstaticfinalLoggerlogger=LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




25



	






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


	

	




restructuring

 


Johannes Lerch
committed
Jun 01, 2015




26



	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {






restructuring

 


Johannes Lerch
committed
Jun 01, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Jun 01, 2015


26


	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {

	private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D,N, M>>() {privateDefaultValueMap<M,MethodAnalyzer<FieldRef,D,N,M>>methodAnalyzers=newDefaultValueMap<M,MethodAnalyzer<FieldRef,D,N,M>>(){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




27


28



		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


27


28


		@Override
		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {

		@Override@Override		protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {protectedMethodAnalyzer<FieldRef,D,N,M>createItem(Mkey){




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




29



			return createMethodAnalyzer(key);






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


29


			return createMethodAnalyzer(key);

			return createMethodAnalyzer(key);returncreateMethodAnalyzer(key);




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




30


31


32


33



		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;






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


32


33


		}
	};

	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;

		}}	};};	private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;privateIFDSTabulationProblem<N,FieldRef,D,M,I>tabulationProblem;




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




34



	protected Context<FieldRef, D, N,M> context;






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


34


	protected Context<FieldRef, D, N,M> context;

	protected Context<FieldRef, D, N,M> context;protectedContext<FieldRef,D,N,M>context;




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



	private Debugger<FieldRef, D, N, M, I> debugger;






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


	private Debugger<FieldRef, D, N, M, I> debugger;

	private Debugger<FieldRef, D, N, M, I> debugger;privateDebugger<FieldRef,D,N,M,I>debugger;




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




36



	private Scheduler scheduler;






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


36


	private Scheduler scheduler;

	private Scheduler scheduler;privateSchedulerscheduler;




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37










rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


37









fixing build script

 


Johannes Lerch
committed
Jun 01, 2015




38



	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {






fixing build script

 


Johannes Lerch
committed
Jun 01, 2015



fixing build script

 

fixing build script

Johannes Lerch
committed
Jun 01, 2015


38


	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {

	public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M, I> debugger, Scheduler scheduler) {publicFieldSensitiveIFDSSolver(IFDSTabulationProblem<N,FieldRef,D,M,I>tabulationProblem,FactMergeHandler<D>factHandler,Debugger<FieldRef,D,N,M,I>debugger,Schedulerscheduler){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




39



		this.tabulationProblem = tabulationProblem;






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


39


		this.tabulationProblem = tabulationProblem;

		this.tabulationProblem = tabulationProblem;this.tabulationProblem=tabulationProblem;




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




40



		this.scheduler = scheduler;






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


40


		this.scheduler = scheduler;

		this.scheduler = scheduler;this.scheduler=scheduler;




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




41


42



		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());






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


		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;
		this.debugger.setICFG(tabulationProblem.interproceduralCFG());

		this.debugger = debugger == null ? new Debugger.NullDebugger<FieldRef, D, N, M, I>() : debugger;this.debugger=debugger==null?newDebugger.NullDebugger<FieldRef,D,N,M,I>():debugger;		this.debugger.setICFG(tabulationProblem.interproceduralCFG());this.debugger.setICFG(tabulationProblem.interproceduralCFG());




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




43


44


45


46



		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}







bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


43


44


45


46


		context = initContext(tabulationProblem, factHandler);
		submitInitialSeeds();
	}


		context = initContext(tabulationProblem, factHandler);context=initContext(tabulationProblem,factHandler);		submitInitialSeeds();submitInitialSeeds();	}}




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




47


48



	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


47


48


	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {

	private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {privateContext<FieldRef,D,N,M>initContext(IFDSTabulationProblem<N,FieldRef,D,M,I>tabulationProblem,FactMergeHandler<D>factHandler){		 return new Context<FieldRef, D, N, M>(tabulationProblem, scheduler, factHandler) {returnnewContext<FieldRef,D,N,M>(tabulationProblem,scheduler,factHandler){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




49


50


51


52


53


54


55



			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
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


49


50


51


52


53


54


55


			@Override
			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
				if(method == null)
					throw new IllegalArgumentException("Method must be not null");
				return methodAnalyzers.getOrCreate(method);
			}
		};

			@Override@Override			public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {publicMethodAnalyzer<FieldRef,D,N,M>getAnalyzer(Mmethod){				if(method == null)if(method==null)					throw new IllegalArgumentException("Method must be not null");thrownewIllegalArgumentException("Method must be not null");				return methodAnalyzers.getOrCreate(method);returnmethodAnalyzers.getOrCreate(method);			}}		};};




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




56



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


56


	}

	}}




bidi solver

 


Johannes Lerch
committed
Mar 20, 2015




57


58



	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {






bidi solver

 


Johannes Lerch
committed
Mar 20, 2015



bidi solver

 

bidi solver

Johannes Lerch
committed
Mar 20, 2015


57


58


	
	protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {

		protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {protectedMethodAnalyzer<FieldRef,D,N,M>createMethodAnalyzer(Mmethod){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




59



		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);






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


		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);

		return new MethodAnalyzerImpl<FieldRef, D, N, M>(method, context);returnnewMethodAnalyzerImpl<FieldRef,D,N,M>(method,context);




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




60


61


62


63


64



	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


60


61


62


63


64


	}

	/**
	 * Schedules the processing of initial seeds, initiating the analysis.
	 */

	}}	/**/**	 * Schedules the processing of initial seeds, initiating the analysis.	 * Schedules the processing of initial seeds, initiating the analysis.	 */	 */




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




65



	private void submitInitialSeeds() {






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


65


	private void submitInitialSeeds() {

	private void submitInitialSeeds() {privatevoidsubmitInitialSeeds(){




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




66



		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


66


		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {

		for(Entry<N, Set<D>> seed: tabulationProblem.initialSeeds().entrySet()) {for(Entry<N,Set<D>>seed:tabulationProblem.initialSeeds().entrySet()){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




67



			N startPoint = seed.getKey();






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


67


			N startPoint = seed.getKey();

			N startPoint = seed.getKey();NstartPoint=seed.getKey();




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




68


69


70


71



			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


68


69


70


71


			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
			for(D val: seed.getValue()) {
				analyzer.addInitialSeed(startPoint, val);
				debugger.initialSeed(startPoint);

			MethodAnalyzer<FieldRef, D,N,M> analyzer = methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));MethodAnalyzer<FieldRef,D,N,M>analyzer=methodAnalyzers.getOrCreate(tabulationProblem.interproceduralCFG().getMethodOf(startPoint));			for(D val: seed.getValue()) {for(Dval:seed.getValue()){				analyzer.addInitialSeed(startPoint, val);analyzer.addInitialSeed(startPoint,val);				debugger.initialSeed(startPoint);debugger.initialSeed(startPoint);




"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015




72



			}






"interest/concrretization" edges in callers are no longer propagated as

 


Johannes Lerch
committed
Jan 07, 2015



"interest/concrretization" edges in callers are no longer propagated as

 

"interest/concrretization" edges in callers are no longer propagated as

Johannes Lerch
committed
Jan 07, 2015


72


			}

			}}




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




73


74


75



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


73


74


75


		}
	}
}
		}}	}}}}





