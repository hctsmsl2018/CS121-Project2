



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

ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89

















ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Switch branch/tag










heros


src


heros


alias


ZeroCallEdgeResolver.java



Find file
Normal viewHistoryPermalink






ZeroCallEdgeResolver.java



2.04 KB









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




11




12




13




14




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
package heros.alias;

import heros.alias.FlowFunction.Constraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






15




public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private ZeroHandler<Field> zeroHandler;

	public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler) {
		super(analyzer);
		this.zeroHandler = zeroHandler;
	}

	@Override
	public void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






26




		if(zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath<Field>())))









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






27




28




29




30




31




32




			callback.interest(analyzer, this);
	}
	
	@Override
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
	}









refactoring


 

 


Johannes Lerch
committed
Apr 01, 2015






33




34




35




36




37




	
	@Override
	protected ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
		return this;
	}









hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of


 

 


Johannes Lerch
committed
Apr 16, 2015






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





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((zeroHandler == null) ? 0 : zeroHandler.hashCode());
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
		ZeroCallEdgeResolver other = (ZeroCallEdgeResolver) obj;
		if (zeroHandler == null) {
			if (other.zeroHandler != null)
				return false;
		} else if (!zeroHandler.equals(other.zeroHandler))
			return false;
		return true;
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






64




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

ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89

















ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Switch branch/tag










heros


src


heros


alias


ZeroCallEdgeResolver.java



Find file
Normal viewHistoryPermalink






ZeroCallEdgeResolver.java



2.04 KB









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




11




12




13




14




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
package heros.alias;

import heros.alias.FlowFunction.Constraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






15




public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private ZeroHandler<Field> zeroHandler;

	public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler) {
		super(analyzer);
		this.zeroHandler = zeroHandler;
	}

	@Override
	public void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






26




		if(zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath<Field>())))









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






27




28




29




30




31




32




			callback.interest(analyzer, this);
	}
	
	@Override
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
	}









refactoring


 

 


Johannes Lerch
committed
Apr 01, 2015






33




34




35




36




37




	
	@Override
	protected ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
		return this;
	}









hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of


 

 


Johannes Lerch
committed
Apr 16, 2015






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





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((zeroHandler == null) ? 0 : zeroHandler.hashCode());
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
		ZeroCallEdgeResolver other = (ZeroCallEdgeResolver) obj;
		if (zeroHandler == null) {
			if (other.zeroHandler != null)
				return false;
		} else if (!zeroHandler.equals(other.zeroHandler))
			return false;
		return true;
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






64




}











Open sidebar



Joshua Garcia heros

ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89







Open sidebar



Joshua Garcia heros

ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89




Open sidebar

Joshua Garcia heros

ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Joshua Garciaherosheros
ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89










ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Switch branch/tag










heros


src


heros


alias


ZeroCallEdgeResolver.java



Find file
Normal viewHistoryPermalink






ZeroCallEdgeResolver.java



2.04 KB









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




11




12




13




14




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
package heros.alias;

import heros.alias.FlowFunction.Constraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






15




public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private ZeroHandler<Field> zeroHandler;

	public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler) {
		super(analyzer);
		this.zeroHandler = zeroHandler;
	}

	@Override
	public void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






26




		if(zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath<Field>())))









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






27




28




29




30




31




32




			callback.interest(analyzer, this);
	}
	
	@Override
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
	}









refactoring


 

 


Johannes Lerch
committed
Apr 01, 2015






33




34




35




36




37




	
	@Override
	protected ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
		return this;
	}









hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of


 

 


Johannes Lerch
committed
Apr 16, 2015






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





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((zeroHandler == null) ? 0 : zeroHandler.hashCode());
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
		ZeroCallEdgeResolver other = (ZeroCallEdgeResolver) obj;
		if (zeroHandler == null) {
			if (other.zeroHandler != null)
				return false;
		} else if (!zeroHandler.equals(other.zeroHandler))
			return false;
		return true;
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






64




}














ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Switch branch/tag










heros


src


heros


alias


ZeroCallEdgeResolver.java



Find file
Normal viewHistoryPermalink






ZeroCallEdgeResolver.java



2.04 KB









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




11




12




13




14




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
package heros.alias;

import heros.alias.FlowFunction.Constraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






15




public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private ZeroHandler<Field> zeroHandler;

	public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler) {
		super(analyzer);
		this.zeroHandler = zeroHandler;
	}

	@Override
	public void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






26




		if(zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath<Field>())))









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






27




28




29




30




31




32




			callback.interest(analyzer, this);
	}
	
	@Override
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
	}









refactoring


 

 


Johannes Lerch
committed
Apr 01, 2015






33




34




35




36




37




	
	@Override
	protected ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
		return this;
	}









hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of


 

 


Johannes Lerch
committed
Apr 16, 2015






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





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((zeroHandler == null) ? 0 : zeroHandler.hashCode());
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
		ZeroCallEdgeResolver other = (ZeroCallEdgeResolver) obj;
		if (zeroHandler == null) {
			if (other.zeroHandler != null)
				return false;
		} else if (!zeroHandler.equals(other.zeroHandler))
			return false;
		return true;
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






64




}










ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Switch branch/tag










heros


src


heros


alias


ZeroCallEdgeResolver.java



Find file
Normal viewHistoryPermalink




ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Switch branch/tag










heros


src


heros


alias


ZeroCallEdgeResolver.java





ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Switch branch/tag








ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89


Switch branch/tag





ee6cd14ce45f8e1a2ac7a25d6fe648f4496e9d89

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

alias

ZeroCallEdgeResolver.java
Find file
Normal viewHistoryPermalink




ZeroCallEdgeResolver.java



2.04 KB









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




11




12




13




14




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
package heros.alias;

import heros.alias.FlowFunction.Constraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






15




public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private ZeroHandler<Field> zeroHandler;

	public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler) {
		super(analyzer);
		this.zeroHandler = zeroHandler;
	}

	@Override
	public void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






26




		if(zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath<Field>())))









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






27




28




29




30




31




32




			callback.interest(analyzer, this);
	}
	
	@Override
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
	}









refactoring


 

 


Johannes Lerch
committed
Apr 01, 2015






33




34




35




36




37




	
	@Override
	protected ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
		return this;
	}









hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of


 

 


Johannes Lerch
committed
Apr 16, 2015






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





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((zeroHandler == null) ? 0 : zeroHandler.hashCode());
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
		ZeroCallEdgeResolver other = (ZeroCallEdgeResolver) obj;
		if (zeroHandler == null) {
			if (other.zeroHandler != null)
				return false;
		} else if (!zeroHandler.equals(other.zeroHandler))
			return false;
		return true;
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






64




}








ZeroCallEdgeResolver.java



2.04 KB










ZeroCallEdgeResolver.java



2.04 KB









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




11




12




13




14




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
package heros.alias;

import heros.alias.FlowFunction.Constraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






15




public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private ZeroHandler<Field> zeroHandler;

	public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler) {
		super(analyzer);
		this.zeroHandler = zeroHandler;
	}

	@Override
	public void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






26




		if(zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath<Field>())))









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






27




28




29




30




31




32




			callback.interest(analyzer, this);
	}
	
	@Override
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
	}









refactoring


 

 


Johannes Lerch
committed
Apr 01, 2015






33




34




35




36




37




	
	@Override
	protected ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
		return this;
	}









hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of


 

 


Johannes Lerch
committed
Apr 16, 2015






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





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((zeroHandler == null) ? 0 : zeroHandler.hashCode());
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
		ZeroCallEdgeResolver other = (ZeroCallEdgeResolver) obj;
		if (zeroHandler == null) {
			if (other.zeroHandler != null)
				return false;
		} else if (!zeroHandler.equals(other.zeroHandler))
			return false;
		return true;
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






64




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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import heros.alias.FlowFunction.Constraint;importheros.alias.FlowFunction.Constraint;



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

15
public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {publicclassZeroCallEdgeResolver<Field,Fact,Stmt,Method>extendsCallEdgeResolver<Field,Fact,Stmt,Method>{



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

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
	private ZeroHandler<Field> zeroHandler;privateZeroHandler<Field>zeroHandler;	public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler) {publicZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer,ZeroHandler<Field>zeroHandler){		super(analyzer);super(analyzer);		this.zeroHandler = zeroHandler;this.zeroHandler=zeroHandler;	}}	@Override@Override	public void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {publicvoidresolve(Constraint<Field>constraint,InterestCallback<Field,Fact,Stmt,Method>callback){



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

26
		if(zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath<Field>())))if(zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(newAccessPath<Field>())))



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

29

30

31

32
			callback.interest(analyzer, this);callback.interest(analyzer,this);	}}		@Override@Override	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {publicvoidinterest(PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer,Resolver<Field,Fact,Stmt,Method>resolver){	}}



refactoring


 

 


Johannes Lerch
committed
Apr 01, 2015



refactoring


 

 

refactoring

 

Johannes Lerch
committed
Apr 01, 2015

33

34

35

36

37
		@Override@Override	protected ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {protectedZeroCallEdgeResolver<Field,Fact,Stmt,Method>getOrCreateNestedResolver(AccessPath<Field>newAccPath){		return this;returnthis;	}}



hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of


 

 


Johannes Lerch
committed
Apr 16, 2015



hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of


 

 

hashcode&equals in ZeroCallEdgeResolver to avoid infinite amounts of

 

Johannes Lerch
committed
Apr 16, 2015

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
	@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * resultresult=prime*result				+ ((zeroHandler == null) ? 0 : zeroHandler.hashCode());+((zeroHandler==null)?0:zeroHandler.hashCode());		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;		if (getClass() != obj.getClass())if(getClass()!=obj.getClass())			return false;returnfalse;		ZeroCallEdgeResolver other = (ZeroCallEdgeResolver) obj;ZeroCallEdgeResolverother=(ZeroCallEdgeResolver)obj;		if (zeroHandler == null) {if(zeroHandler==null){			if (other.zeroHandler != null)if(other.zeroHandler!=null)				return false;returnfalse;		} else if (!zeroHandler.equals(other.zeroHandler))}elseif(!zeroHandler.equals(other.zeroHandler))			return false;returnfalse;		return true;returntrue;	}}



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

64
}}





