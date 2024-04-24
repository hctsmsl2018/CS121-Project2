



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

16ed129f14925504194a6d3a8bc8257ef2d1aa64

















16ed129f14925504194a6d3a8bc8257ef2d1aa64


Switch branch/tag










heros


src


heros


solver


PathEdge.java



Find file
Normal viewHistoryPermalink






PathEdge.java



2.7 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.InterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15




16




17




18




19




20




21




22





/**
 * A path edge as described in the IFDS/IDE algorithms.
 * The source node is implicit: it can be computed from the target by using the {@link InterproceduralCFG}.
 * Hence, we don't store it.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






23




public class PathEdge<N,D> {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




56




57




58




59




60




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




94




95




96




97




98




99




100




101




102




103





	protected final N target;
	protected final D dSource, dTarget;

	/**
	 * @param dSource The fact at the source.
	 * @param target The target statement.
	 * @param dTarget The fact at the target.
	 */
	public PathEdge(D dSource, N target, D dTarget) {
		super();
		this.target = target;
		this.dSource = dSource;
		this.dTarget = dTarget;
	}
	
	public N getTarget() {
		return target;
	}

	public D factAtSource() {
		return dSource;
	}

	public D factAtTarget() {
		return dTarget;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dSource == null) ? 0 : dSource.hashCode());
		result = prime * result + ((dTarget == null) ? 0 : dTarget.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		PathEdge other = (PathEdge) obj;
		if (dSource == null) {
			if (other.dSource != null)
				return false;
		} else if (!dSource.equals(other.dSource))
			return false;
		if (dTarget == null) {
			if (other.dTarget != null)
				return false;
		} else if (!dTarget.equals(other.dTarget))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("<");
		result.append(dSource);
		result.append("> -> <");
		result.append(target.toString());
		result.append(",");
		result.append(dTarget);
		result.append(">");
		return result.toString();
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

16ed129f14925504194a6d3a8bc8257ef2d1aa64

















16ed129f14925504194a6d3a8bc8257ef2d1aa64


Switch branch/tag










heros


src


heros


solver


PathEdge.java



Find file
Normal viewHistoryPermalink






PathEdge.java



2.7 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.InterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15




16




17




18




19




20




21




22





/**
 * A path edge as described in the IFDS/IDE algorithms.
 * The source node is implicit: it can be computed from the target by using the {@link InterproceduralCFG}.
 * Hence, we don't store it.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






23




public class PathEdge<N,D> {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




56




57




58




59




60




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




94




95




96




97




98




99




100




101




102




103





	protected final N target;
	protected final D dSource, dTarget;

	/**
	 * @param dSource The fact at the source.
	 * @param target The target statement.
	 * @param dTarget The fact at the target.
	 */
	public PathEdge(D dSource, N target, D dTarget) {
		super();
		this.target = target;
		this.dSource = dSource;
		this.dTarget = dTarget;
	}
	
	public N getTarget() {
		return target;
	}

	public D factAtSource() {
		return dSource;
	}

	public D factAtTarget() {
		return dTarget;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dSource == null) ? 0 : dSource.hashCode());
		result = prime * result + ((dTarget == null) ? 0 : dTarget.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		PathEdge other = (PathEdge) obj;
		if (dSource == null) {
			if (other.dSource != null)
				return false;
		} else if (!dSource.equals(other.dSource))
			return false;
		if (dTarget == null) {
			if (other.dTarget != null)
				return false;
		} else if (!dTarget.equals(other.dTarget))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("<");
		result.append(dSource);
		result.append("> -> <");
		result.append(target.toString());
		result.append(",");
		result.append(dTarget);
		result.append(">");
		return result.toString();
	}

}











Open sidebar



Joshua Garcia heros

16ed129f14925504194a6d3a8bc8257ef2d1aa64







Open sidebar



Joshua Garcia heros

16ed129f14925504194a6d3a8bc8257ef2d1aa64




Open sidebar

Joshua Garcia heros

16ed129f14925504194a6d3a8bc8257ef2d1aa64


Joshua Garciaherosheros
16ed129f14925504194a6d3a8bc8257ef2d1aa64










16ed129f14925504194a6d3a8bc8257ef2d1aa64


Switch branch/tag










heros


src


heros


solver


PathEdge.java



Find file
Normal viewHistoryPermalink






PathEdge.java



2.7 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.InterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15




16




17




18




19




20




21




22





/**
 * A path edge as described in the IFDS/IDE algorithms.
 * The source node is implicit: it can be computed from the target by using the {@link InterproceduralCFG}.
 * Hence, we don't store it.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






23




public class PathEdge<N,D> {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




56




57




58




59




60




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




94




95




96




97




98




99




100




101




102




103





	protected final N target;
	protected final D dSource, dTarget;

	/**
	 * @param dSource The fact at the source.
	 * @param target The target statement.
	 * @param dTarget The fact at the target.
	 */
	public PathEdge(D dSource, N target, D dTarget) {
		super();
		this.target = target;
		this.dSource = dSource;
		this.dTarget = dTarget;
	}
	
	public N getTarget() {
		return target;
	}

	public D factAtSource() {
		return dSource;
	}

	public D factAtTarget() {
		return dTarget;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dSource == null) ? 0 : dSource.hashCode());
		result = prime * result + ((dTarget == null) ? 0 : dTarget.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		PathEdge other = (PathEdge) obj;
		if (dSource == null) {
			if (other.dSource != null)
				return false;
		} else if (!dSource.equals(other.dSource))
			return false;
		if (dTarget == null) {
			if (other.dTarget != null)
				return false;
		} else if (!dTarget.equals(other.dTarget))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("<");
		result.append(dSource);
		result.append("> -> <");
		result.append(target.toString());
		result.append(",");
		result.append(dTarget);
		result.append(">");
		return result.toString();
	}

}














16ed129f14925504194a6d3a8bc8257ef2d1aa64


Switch branch/tag










heros


src


heros


solver


PathEdge.java



Find file
Normal viewHistoryPermalink






PathEdge.java



2.7 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.InterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15




16




17




18




19




20




21




22





/**
 * A path edge as described in the IFDS/IDE algorithms.
 * The source node is implicit: it can be computed from the target by using the {@link InterproceduralCFG}.
 * Hence, we don't store it.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






23




public class PathEdge<N,D> {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




56




57




58




59




60




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




94




95




96




97




98




99




100




101




102




103





	protected final N target;
	protected final D dSource, dTarget;

	/**
	 * @param dSource The fact at the source.
	 * @param target The target statement.
	 * @param dTarget The fact at the target.
	 */
	public PathEdge(D dSource, N target, D dTarget) {
		super();
		this.target = target;
		this.dSource = dSource;
		this.dTarget = dTarget;
	}
	
	public N getTarget() {
		return target;
	}

	public D factAtSource() {
		return dSource;
	}

	public D factAtTarget() {
		return dTarget;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dSource == null) ? 0 : dSource.hashCode());
		result = prime * result + ((dTarget == null) ? 0 : dTarget.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		PathEdge other = (PathEdge) obj;
		if (dSource == null) {
			if (other.dSource != null)
				return false;
		} else if (!dSource.equals(other.dSource))
			return false;
		if (dTarget == null) {
			if (other.dTarget != null)
				return false;
		} else if (!dTarget.equals(other.dTarget))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("<");
		result.append(dSource);
		result.append("> -> <");
		result.append(target.toString());
		result.append(",");
		result.append(dTarget);
		result.append(">");
		return result.toString();
	}

}










16ed129f14925504194a6d3a8bc8257ef2d1aa64


Switch branch/tag










heros


src


heros


solver


PathEdge.java



Find file
Normal viewHistoryPermalink




16ed129f14925504194a6d3a8bc8257ef2d1aa64


Switch branch/tag










heros


src


heros


solver


PathEdge.java





16ed129f14925504194a6d3a8bc8257ef2d1aa64


Switch branch/tag








16ed129f14925504194a6d3a8bc8257ef2d1aa64


Switch branch/tag





16ed129f14925504194a6d3a8bc8257ef2d1aa64

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

PathEdge.java
Find file
Normal viewHistoryPermalink




PathEdge.java



2.7 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.InterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15




16




17




18




19




20




21




22





/**
 * A path edge as described in the IFDS/IDE algorithms.
 * The source node is implicit: it can be computed from the target by using the {@link InterproceduralCFG}.
 * Hence, we don't store it.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






23




public class PathEdge<N,D> {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




56




57




58




59




60




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




94




95




96




97




98




99




100




101




102




103





	protected final N target;
	protected final D dSource, dTarget;

	/**
	 * @param dSource The fact at the source.
	 * @param target The target statement.
	 * @param dTarget The fact at the target.
	 */
	public PathEdge(D dSource, N target, D dTarget) {
		super();
		this.target = target;
		this.dSource = dSource;
		this.dTarget = dTarget;
	}
	
	public N getTarget() {
		return target;
	}

	public D factAtSource() {
		return dSource;
	}

	public D factAtTarget() {
		return dTarget;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dSource == null) ? 0 : dSource.hashCode());
		result = prime * result + ((dTarget == null) ? 0 : dTarget.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		PathEdge other = (PathEdge) obj;
		if (dSource == null) {
			if (other.dSource != null)
				return false;
		} else if (!dSource.equals(other.dSource))
			return false;
		if (dTarget == null) {
			if (other.dTarget != null)
				return false;
		} else if (!dTarget.equals(other.dTarget))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("<");
		result.append(dSource);
		result.append("> -> <");
		result.append(target.toString());
		result.append(",");
		result.append(dTarget);
		result.append(">");
		return result.toString();
	}

}








PathEdge.java



2.7 KB










PathEdge.java



2.7 KB









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




package heros.solver;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.InterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14




15




16




17




18




19




20




21




22





/**
 * A path edge as described in the IFDS/IDE algorithms.
 * The source node is implicit: it can be computed from the target by using the {@link InterproceduralCFG}.
 * Hence, we don't store it.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 */









removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013






23




public class PathEdge<N,D> {









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




56




57




58




59




60




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




94




95




96




97




98




99




100




101




102




103





	protected final N target;
	protected final D dSource, dTarget;

	/**
	 * @param dSource The fact at the source.
	 * @param target The target statement.
	 * @param dTarget The fact at the target.
	 */
	public PathEdge(D dSource, N target, D dTarget) {
		super();
		this.target = target;
		this.dSource = dSource;
		this.dTarget = dTarget;
	}
	
	public N getTarget() {
		return target;
	}

	public D factAtSource() {
		return dSource;
	}

	public D factAtTarget() {
		return dTarget;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dSource == null) ? 0 : dSource.hashCode());
		result = prime * result + ((dTarget == null) ? 0 : dTarget.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		PathEdge other = (PathEdge) obj;
		if (dSource == null) {
			if (other.dSource != null)
				return false;
		} else if (!dSource.equals(other.dSource))
			return false;
		if (dTarget == null) {
			if (other.dTarget != null)
				return false;
		} else if (!dTarget.equals(other.dTarget))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("<");
		result.append(dSource);
		result.append("> -> <");
		result.append(target.toString());
		result.append(",");
		result.append(dTarget);
		result.append(">");
		return result.toString();
	}

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
package heros.solver;packageheros.solver;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
import heros.InterproceduralCFG;importheros.InterproceduralCFG;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

14

15

16

17

18

19

20

21

22
/**/** * A path edge as described in the IFDS/IDE algorithms. * A path edge as described in the IFDS/IDE algorithms. * The source node is implicit: it can be computed from the target by using the {@link InterproceduralCFG}. * The source node is implicit: it can be computed from the target by using the {@link InterproceduralCFG}. * Hence, we don't store it. * Hence, we don't store it. * * * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}. * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}. * @param <D> The type of data-flow facts to be computed by the tabulation problem. * @param <D> The type of data-flow facts to be computed by the tabulation problem. */ */



removed superfluous type parameter from PathEdge


 

 


Eric Bodden
committed
Jul 06, 2013



removed superfluous type parameter from PathEdge


 

 

removed superfluous type parameter from PathEdge

 

Eric Bodden
committed
Jul 06, 2013

23
public class PathEdge<N,D> {publicclassPathEdge<N,D>{



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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

56

57

58

59

60

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

94

95

96

97

98

99

100

101

102

103
	protected final N target;protectedfinalNtarget;	protected final D dSource, dTarget;protectedfinalDdSource,dTarget;	/**/**	 * @param dSource The fact at the source.	 * @param dSource The fact at the source.	 * @param target The target statement.	 * @param target The target statement.	 * @param dTarget The fact at the target.	 * @param dTarget The fact at the target.	 */	 */	public PathEdge(D dSource, N target, D dTarget) {publicPathEdge(DdSource,Ntarget,DdTarget){		super();super();		this.target = target;this.target=target;		this.dSource = dSource;this.dSource=dSource;		this.dTarget = dTarget;this.dTarget=dTarget;	}}		public N getTarget() {publicNgetTarget(){		return target;returntarget;	}}	public D factAtSource() {publicDfactAtSource(){		return dSource;returndSource;	}}	public D factAtTarget() {publicDfactAtTarget(){		return dTarget;returndTarget;	}}	@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + ((dSource == null) ? 0 : dSource.hashCode());result=prime*result+((dSource==null)?0:dSource.hashCode());		result = prime * result + ((dTarget == null) ? 0 : dTarget.hashCode());result=prime*result+((dTarget==null)?0:dTarget.hashCode());		result = prime * result + ((target == null) ? 0 : target.hashCode());result=prime*result+((target==null)?0:target.hashCode());		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;		if (getClass() != obj.getClass())if(getClass()!=obj.getClass())			return false;returnfalse;		@SuppressWarnings("rawtypes")@SuppressWarnings("rawtypes")		PathEdge other = (PathEdge) obj;PathEdgeother=(PathEdge)obj;		if (dSource == null) {if(dSource==null){			if (other.dSource != null)if(other.dSource!=null)				return false;returnfalse;		} else if (!dSource.equals(other.dSource))}elseif(!dSource.equals(other.dSource))			return false;returnfalse;		if (dTarget == null) {if(dTarget==null){			if (other.dTarget != null)if(other.dTarget!=null)				return false;returnfalse;		} else if (!dTarget.equals(other.dTarget))}elseif(!dTarget.equals(other.dTarget))			return false;returnfalse;		if (target == null) {if(target==null){			if (other.target != null)if(other.target!=null)				return false;returnfalse;		} else if (!target.equals(other.target))}elseif(!target.equals(other.target))			return false;returnfalse;		return true;returntrue;	}}	@Override@Override	public String toString() {publicStringtoString(){		StringBuffer result = new StringBuffer();StringBufferresult=newStringBuffer();		result.append("<");result.append("<");		result.append(dSource);result.append(dSource);		result.append("> -> <");result.append("> -> <");		result.append(target.toString());result.append(target.toString());		result.append(",");result.append(",");		result.append(dTarget);result.append(dTarget);		result.append(">");result.append(">");		return result.toString();returnresult.toString();	}}}}





