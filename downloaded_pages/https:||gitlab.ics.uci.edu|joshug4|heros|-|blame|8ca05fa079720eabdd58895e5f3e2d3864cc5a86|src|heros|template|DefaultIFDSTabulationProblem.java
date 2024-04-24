



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

8ca05fa079720eabdd58895e5f3e2d3864cc5a86

















8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Switch branch/tag










heros


src


heros


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultIFDSTabulationProblem.java



2.15 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




package heros.template;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




14




15




import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override









interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...


 

 


Steven Arzt
committed
May 14, 2013






49




	public I interproceduralCFG() {









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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




		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






60




61




62




63




64




	
	@Override
	public boolean followReturnsPastSeeds() {
		return false;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






65














refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






66




67




68




69




	@Override
	public boolean autoAddZero() {
		return true;
	}









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






70




71




72




73




74




	
	@Override
	public int numThreads() {
		return Runtime.getRuntime().availableProcessors();
	}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






75




76




77




78




79




	
	@Override
	public boolean computeValues() {
		return true;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






80




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

8ca05fa079720eabdd58895e5f3e2d3864cc5a86

















8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Switch branch/tag










heros


src


heros


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultIFDSTabulationProblem.java



2.15 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




package heros.template;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




14




15




import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override









interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...


 

 


Steven Arzt
committed
May 14, 2013






49




	public I interproceduralCFG() {









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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




		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






60




61




62




63




64




	
	@Override
	public boolean followReturnsPastSeeds() {
		return false;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






65














refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






66




67




68




69




	@Override
	public boolean autoAddZero() {
		return true;
	}









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






70




71




72




73




74




	
	@Override
	public int numThreads() {
		return Runtime.getRuntime().availableProcessors();
	}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






75




76




77




78




79




	
	@Override
	public boolean computeValues() {
		return true;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






80




}











Open sidebar



Joshua Garcia heros

8ca05fa079720eabdd58895e5f3e2d3864cc5a86







Open sidebar



Joshua Garcia heros

8ca05fa079720eabdd58895e5f3e2d3864cc5a86




Open sidebar

Joshua Garcia heros

8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Joshua Garciaherosheros
8ca05fa079720eabdd58895e5f3e2d3864cc5a86










8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Switch branch/tag










heros


src


heros


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultIFDSTabulationProblem.java



2.15 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




package heros.template;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




14




15




import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override









interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...


 

 


Steven Arzt
committed
May 14, 2013






49




	public I interproceduralCFG() {









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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




		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






60




61




62




63




64




	
	@Override
	public boolean followReturnsPastSeeds() {
		return false;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






65














refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






66




67




68




69




	@Override
	public boolean autoAddZero() {
		return true;
	}









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






70




71




72




73




74




	
	@Override
	public int numThreads() {
		return Runtime.getRuntime().availableProcessors();
	}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






75




76




77




78




79




	
	@Override
	public boolean computeValues() {
		return true;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






80




}














8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Switch branch/tag










heros


src


heros


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink






DefaultIFDSTabulationProblem.java



2.15 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




package heros.template;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




14




15




import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override









interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...


 

 


Steven Arzt
committed
May 14, 2013






49




	public I interproceduralCFG() {









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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




		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






60




61




62




63




64




	
	@Override
	public boolean followReturnsPastSeeds() {
		return false;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






65














refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






66




67




68




69




	@Override
	public boolean autoAddZero() {
		return true;
	}









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






70




71




72




73




74




	
	@Override
	public int numThreads() {
		return Runtime.getRuntime().availableProcessors();
	}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






75




76




77




78




79




	
	@Override
	public boolean computeValues() {
		return true;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






80




}










8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Switch branch/tag










heros


src


heros


template


DefaultIFDSTabulationProblem.java



Find file
Normal viewHistoryPermalink




8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Switch branch/tag










heros


src


heros


template


DefaultIFDSTabulationProblem.java





8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Switch branch/tag








8ca05fa079720eabdd58895e5f3e2d3864cc5a86


Switch branch/tag





8ca05fa079720eabdd58895e5f3e2d3864cc5a86

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

template

DefaultIFDSTabulationProblem.java
Find file
Normal viewHistoryPermalink




DefaultIFDSTabulationProblem.java



2.15 KB









Newer










Older









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




package heros.template;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




14




15




import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override









interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...


 

 


Steven Arzt
committed
May 14, 2013






49




	public I interproceduralCFG() {









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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




		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






60




61




62




63




64




	
	@Override
	public boolean followReturnsPastSeeds() {
		return false;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






65














refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






66




67




68




69




	@Override
	public boolean autoAddZero() {
		return true;
	}









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






70




71




72




73




74




	
	@Override
	public int numThreads() {
		return Runtime.getRuntime().availableProcessors();
	}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






75




76




77




78




79




	
	@Override
	public boolean computeValues() {
		return true;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






80




}








DefaultIFDSTabulationProblem.java



2.15 KB










DefaultIFDSTabulationProblem.java



2.15 KB









Newer










Older
NewerOlder







license headers


 

 


Eric Bodden
committed
Nov 29, 2012






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
 * Copyright (c) 2012 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






11




package heros.template;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




14




15




import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





/**
 * This is a template for {@link IFDSTabulationProblem}s that automatically caches values
 * that ought to be cached. This class uses the Factory Method design pattern.
 * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently
 * reused for solving multiple different {@link IFDSTabulationProblem}s.
 * This class is specific to Soot. 
 * 
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */
public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {

	private final I icfg;
	private FlowFunctions<N,D,M> flowFunctions;
	private D zeroValue;
	
	public DefaultIFDSTabulationProblem(I icfg) {
		this.icfg = icfg;
	}
	
	protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

	protected abstract D createZeroValue();

	@Override
	public final FlowFunctions<N,D,M> flowFunctions() {
		if(flowFunctions==null) {
			flowFunctions = createFlowFunctionsFactory();
		}
		return flowFunctions;
	}

	@Override









interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...


 

 


Steven Arzt
committed
May 14, 2013






49




	public I interproceduralCFG() {









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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




		return icfg;
	}

	@Override
	public final D zeroValue() {
		if(zeroValue==null) {
			zeroValue = createZeroValue();
		}
		return zeroValue;
	}









making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012






60




61




62




63




64




	
	@Override
	public boolean followReturnsPastSeeds() {
		return false;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






65














refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013






66




67




68




69




	@Override
	public boolean autoAddZero() {
		return true;
	}









number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013






70




71




72




73




74




	
	@Override
	public int numThreads() {
		return Runtime.getRuntime().availableProcessors();
	}









make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013






75




76




77




78




79




	
	@Override
	public boolean computeValues() {
		return true;
	}









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






80




}







license headers


 

 


Eric Bodden
committed
Nov 29, 2012



license headers


 

 

license headers

 

Eric Bodden
committed
Nov 29, 2012

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/



renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

11
package heros.template;packageheros.template;



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

12




renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

13

14

15
import heros.FlowFunctions;importheros.FlowFunctions;import heros.IFDSTabulationProblem;importheros.IFDSTabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

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
/**/** * This is a template for {@link IFDSTabulationProblem}s that automatically caches values * This is a template for {@link IFDSTabulationProblem}s that automatically caches values * that ought to be cached. This class uses the Factory Method design pattern. * that ought to be cached. This class uses the Factory Method design pattern. * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently * The {@link InterproceduralCFG} is passed into the constructor so that it can be conveniently * reused for solving multiple different {@link IFDSTabulationProblem}s. * reused for solving multiple different {@link IFDSTabulationProblem}s. * This class is specific to Soot.  * This class is specific to Soot.  *  *  * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem. */ */public abstract class DefaultIFDSTabulationProblem<N,D,M, I extends InterproceduralCFG<N,M>> implements IFDSTabulationProblem<N,D,M,I> {publicabstractclassDefaultIFDSTabulationProblem<N,D,M,IextendsInterproceduralCFG<N,M>>implementsIFDSTabulationProblem<N,D,M,I>{	private final I icfg;privatefinalIicfg;	private FlowFunctions<N,D,M> flowFunctions;privateFlowFunctions<N,D,M>flowFunctions;	private D zeroValue;privateDzeroValue;		public DefaultIFDSTabulationProblem(I icfg) {publicDefaultIFDSTabulationProblem(Iicfg){		this.icfg = icfg;this.icfg=icfg;	}}		protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();protectedabstractFlowFunctions<N,D,M>createFlowFunctionsFactory();	protected abstract D createZeroValue();protectedabstractDcreateZeroValue();	@Override@Override	public final FlowFunctions<N,D,M> flowFunctions() {publicfinalFlowFunctions<N,D,M>flowFunctions(){		if(flowFunctions==null) {if(flowFunctions==null){			flowFunctions = createFlowFunctionsFactory();flowFunctions=createFlowFunctionsFactory();		}}		return flowFunctions;returnflowFunctions;	}}	@Override@Override



interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...


 

 


Steven Arzt
committed
May 14, 2013



interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...


 

 

interproceduralCFG() in DefaultIFDSTabulationProblem.java can now be...

 

Steven Arzt
committed
May 14, 2013

49
	public I interproceduralCFG() {publicIinterproceduralCFG(){



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

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
		return icfg;returnicfg;	}}	@Override@Override	public final D zeroValue() {publicfinalDzeroValue(){		if(zeroValue==null) {if(zeroValue==null){			zeroValue = createZeroValue();zeroValue=createZeroValue();		}}		return zeroValue;returnzeroValue;	}}



making computation of unbalanced edges optional


 

 


Eric Bodden
committed
Dec 12, 2012



making computation of unbalanced edges optional


 

 

making computation of unbalanced edges optional

 

Eric Bodden
committed
Dec 12, 2012

60

61

62

63

64
		@Override@Override	public boolean followReturnsPastSeeds() {publicbooleanfollowReturnsPastSeeds(){		return false;returnfalse;	}}



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

65




refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 


Eric Bodden
committed
Jan 28, 2013



refactoring: autoAddZero is now set in IFDSTabulationProblem


 

 

refactoring: autoAddZero is now set in IFDSTabulationProblem

 

Eric Bodden
committed
Jan 28, 2013

66

67

68

69
	@Override@Override	public boolean autoAddZero() {publicbooleanautoAddZero(){		return true;returntrue;	}}



number of threads is now configured through SolverConfiguration, a new super...


 

 


Eric Bodden
committed
Jan 29, 2013



number of threads is now configured through SolverConfiguration, a new super...


 

 

number of threads is now configured through SolverConfiguration, a new super...

 

Eric Bodden
committed
Jan 29, 2013

70

71

72

73

74
		@Override@Override	public int numThreads() {publicintnumThreads(){		return Runtime.getRuntime().availableProcessors();returnRuntime.getRuntime().availableProcessors();	}}



make computation of values optional


 

 


Eric Bodden
committed
Jan 29, 2013



make computation of values optional


 

 

make computation of values optional

 

Eric Bodden
committed
Jan 29, 2013

75

76

77

78

79
		@Override@Override	public boolean computeValues() {publicbooleancomputeValues(){		return true;returntrue;	}}



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

80
}}





