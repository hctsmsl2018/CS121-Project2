



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

f3b1b1f1f6f688378b630717cafbd80ad21cbfb3

















f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Switch branch/tag










heros


src


heros


solver


Pair.java



Find file
Normal viewHistoryPermalink






Pair.java



1.87 KB









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









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12




13




14




15




16





//copied from soot.toolkits.scalar
public class Pair<T, U> {
	protected T o1;
	protected U o2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






17




18




	
	protected int hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





	public Pair() {
		o1 = null;
		o2 = null;
	}

	public Pair(T o1, U o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	@Override
	public int hashCode() {









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






32




33




34




		if (hashCode != 0)
			return hashCode;
		









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






35




36




37




38




		final int prime = 31;
		int result = 1;
		result = prime * result + ((o1 == null) ? 0 : o1.hashCode());
		result = prime * result + ((o2 == null) ? 0 : o2.hashCode());









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






39




40




41




		hashCode = result;
		
		return hashCode;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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
		Pair other = (Pair) obj;
		if (o1 == null) {
			if (other.o1 != null)
				return false;
		} else if (!o1.equals(other.o1))
			return false;
		if (o2 == null) {
			if (other.o2 != null)
				return false;
		} else if (!o2.equals(other.o2))
			return false;
		return true;
	}

	public String toString() {
		return "Pair " + o1 + "," + o2;
	}

	public T getO1() {
		return o1;
	}

	public U getO2() {
		return o2;
	}

	public void setO1(T no1) {
		o1 = no1;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






81




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






82




83




84




85




	}

	public void setO2(U no2) {
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






86




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






87




88




89




90




91




	}

	public void setPair(T no1, U no2) {
		o1 = no1;
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






92




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






93




94




	}










license headers


 

 


Eric Bodden
committed
Nov 29, 2012






95




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

f3b1b1f1f6f688378b630717cafbd80ad21cbfb3

















f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Switch branch/tag










heros


src


heros


solver


Pair.java



Find file
Normal viewHistoryPermalink






Pair.java



1.87 KB









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









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12




13




14




15




16





//copied from soot.toolkits.scalar
public class Pair<T, U> {
	protected T o1;
	protected U o2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






17




18




	
	protected int hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





	public Pair() {
		o1 = null;
		o2 = null;
	}

	public Pair(T o1, U o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	@Override
	public int hashCode() {









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






32




33




34




		if (hashCode != 0)
			return hashCode;
		









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






35




36




37




38




		final int prime = 31;
		int result = 1;
		result = prime * result + ((o1 == null) ? 0 : o1.hashCode());
		result = prime * result + ((o2 == null) ? 0 : o2.hashCode());









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






39




40




41




		hashCode = result;
		
		return hashCode;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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
		Pair other = (Pair) obj;
		if (o1 == null) {
			if (other.o1 != null)
				return false;
		} else if (!o1.equals(other.o1))
			return false;
		if (o2 == null) {
			if (other.o2 != null)
				return false;
		} else if (!o2.equals(other.o2))
			return false;
		return true;
	}

	public String toString() {
		return "Pair " + o1 + "," + o2;
	}

	public T getO1() {
		return o1;
	}

	public U getO2() {
		return o2;
	}

	public void setO1(T no1) {
		o1 = no1;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






81




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






82




83




84




85




	}

	public void setO2(U no2) {
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






86




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






87




88




89




90




91




	}

	public void setPair(T no1, U no2) {
		o1 = no1;
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






92




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






93




94




	}










license headers


 

 


Eric Bodden
committed
Nov 29, 2012






95




}











Open sidebar



Joshua Garcia heros

f3b1b1f1f6f688378b630717cafbd80ad21cbfb3







Open sidebar



Joshua Garcia heros

f3b1b1f1f6f688378b630717cafbd80ad21cbfb3




Open sidebar

Joshua Garcia heros

f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Joshua Garciaherosheros
f3b1b1f1f6f688378b630717cafbd80ad21cbfb3










f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Switch branch/tag










heros


src


heros


solver


Pair.java



Find file
Normal viewHistoryPermalink






Pair.java



1.87 KB









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









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12




13




14




15




16





//copied from soot.toolkits.scalar
public class Pair<T, U> {
	protected T o1;
	protected U o2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






17




18




	
	protected int hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





	public Pair() {
		o1 = null;
		o2 = null;
	}

	public Pair(T o1, U o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	@Override
	public int hashCode() {









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






32




33




34




		if (hashCode != 0)
			return hashCode;
		









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






35




36




37




38




		final int prime = 31;
		int result = 1;
		result = prime * result + ((o1 == null) ? 0 : o1.hashCode());
		result = prime * result + ((o2 == null) ? 0 : o2.hashCode());









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






39




40




41




		hashCode = result;
		
		return hashCode;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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
		Pair other = (Pair) obj;
		if (o1 == null) {
			if (other.o1 != null)
				return false;
		} else if (!o1.equals(other.o1))
			return false;
		if (o2 == null) {
			if (other.o2 != null)
				return false;
		} else if (!o2.equals(other.o2))
			return false;
		return true;
	}

	public String toString() {
		return "Pair " + o1 + "," + o2;
	}

	public T getO1() {
		return o1;
	}

	public U getO2() {
		return o2;
	}

	public void setO1(T no1) {
		o1 = no1;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






81




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






82




83




84




85




	}

	public void setO2(U no2) {
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






86




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






87




88




89




90




91




	}

	public void setPair(T no1, U no2) {
		o1 = no1;
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






92




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






93




94




	}










license headers


 

 


Eric Bodden
committed
Nov 29, 2012






95




}














f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Switch branch/tag










heros


src


heros


solver


Pair.java



Find file
Normal viewHistoryPermalink






Pair.java



1.87 KB









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









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12




13




14




15




16





//copied from soot.toolkits.scalar
public class Pair<T, U> {
	protected T o1;
	protected U o2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






17




18




	
	protected int hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





	public Pair() {
		o1 = null;
		o2 = null;
	}

	public Pair(T o1, U o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	@Override
	public int hashCode() {









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






32




33




34




		if (hashCode != 0)
			return hashCode;
		









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






35




36




37




38




		final int prime = 31;
		int result = 1;
		result = prime * result + ((o1 == null) ? 0 : o1.hashCode());
		result = prime * result + ((o2 == null) ? 0 : o2.hashCode());









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






39




40




41




		hashCode = result;
		
		return hashCode;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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
		Pair other = (Pair) obj;
		if (o1 == null) {
			if (other.o1 != null)
				return false;
		} else if (!o1.equals(other.o1))
			return false;
		if (o2 == null) {
			if (other.o2 != null)
				return false;
		} else if (!o2.equals(other.o2))
			return false;
		return true;
	}

	public String toString() {
		return "Pair " + o1 + "," + o2;
	}

	public T getO1() {
		return o1;
	}

	public U getO2() {
		return o2;
	}

	public void setO1(T no1) {
		o1 = no1;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






81




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






82




83




84




85




	}

	public void setO2(U no2) {
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






86




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






87




88




89




90




91




	}

	public void setPair(T no1, U no2) {
		o1 = no1;
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






92




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






93




94




	}










license headers


 

 


Eric Bodden
committed
Nov 29, 2012






95




}










f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Switch branch/tag










heros


src


heros


solver


Pair.java



Find file
Normal viewHistoryPermalink




f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Switch branch/tag










heros


src


heros


solver


Pair.java





f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Switch branch/tag








f3b1b1f1f6f688378b630717cafbd80ad21cbfb3


Switch branch/tag





f3b1b1f1f6f688378b630717cafbd80ad21cbfb3

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

Pair.java
Find file
Normal viewHistoryPermalink




Pair.java



1.87 KB









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









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12




13




14




15




16





//copied from soot.toolkits.scalar
public class Pair<T, U> {
	protected T o1;
	protected U o2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






17




18




	
	protected int hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





	public Pair() {
		o1 = null;
		o2 = null;
	}

	public Pair(T o1, U o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	@Override
	public int hashCode() {









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






32




33




34




		if (hashCode != 0)
			return hashCode;
		









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






35




36




37




38




		final int prime = 31;
		int result = 1;
		result = prime * result + ((o1 == null) ? 0 : o1.hashCode());
		result = prime * result + ((o2 == null) ? 0 : o2.hashCode());









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






39




40




41




		hashCode = result;
		
		return hashCode;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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
		Pair other = (Pair) obj;
		if (o1 == null) {
			if (other.o1 != null)
				return false;
		} else if (!o1.equals(other.o1))
			return false;
		if (o2 == null) {
			if (other.o2 != null)
				return false;
		} else if (!o2.equals(other.o2))
			return false;
		return true;
	}

	public String toString() {
		return "Pair " + o1 + "," + o2;
	}

	public T getO1() {
		return o1;
	}

	public U getO2() {
		return o2;
	}

	public void setO1(T no1) {
		o1 = no1;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






81




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






82




83




84




85




	}

	public void setO2(U no2) {
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






86




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






87




88




89




90




91




	}

	public void setPair(T no1, U no2) {
		o1 = no1;
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






92




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






93




94




	}










license headers


 

 


Eric Bodden
committed
Nov 29, 2012






95




}








Pair.java



1.87 KB










Pair.java



1.87 KB









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









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






12




13




14




15




16





//copied from soot.toolkits.scalar
public class Pair<T, U> {
	protected T o1;
	protected U o2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






17




18




	
	protected int hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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





	public Pair() {
		o1 = null;
		o2 = null;
	}

	public Pair(T o1, U o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	@Override
	public int hashCode() {









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






32




33




34




		if (hashCode != 0)
			return hashCode;
		









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






35




36




37




38




		final int prime = 31;
		int result = 1;
		result = prime * result + ((o1 == null) ? 0 : o1.hashCode());
		result = prime * result + ((o2 == null) ? 0 : o2.hashCode());









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






39




40




41




		hashCode = result;
		
		return hashCode;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






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
		Pair other = (Pair) obj;
		if (o1 == null) {
			if (other.o1 != null)
				return false;
		} else if (!o1.equals(other.o1))
			return false;
		if (o2 == null) {
			if (other.o2 != null)
				return false;
		} else if (!o2.equals(other.o2))
			return false;
		return true;
	}

	public String toString() {
		return "Pair " + o1 + "," + o2;
	}

	public T getO1() {
		return o1;
	}

	public U getO2() {
		return o2;
	}

	public void setO1(T no1) {
		o1 = no1;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






81




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






82




83




84




85




	}

	public void setO2(U no2) {
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






86




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






87




88




89




90




91




	}

	public void setPair(T no1, U no2) {
		o1 = no1;
		o2 = no2;









now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014






92




		hashCode = 0;









adding missing files



 


Eric Bodden
committed
Nov 29, 2012






93




94




	}










license headers


 

 


Eric Bodden
committed
Nov 29, 2012






95




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

13

14

15

16
//copied from soot.toolkits.scalar//copied from soot.toolkits.scalarpublic class Pair<T, U> {publicclassPair<T,U>{	protected T o1;protectedTo1;	protected U o2;protectedUo2;



now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014



now caching the hash code of the Pair class


 

 

now caching the hash code of the Pair class

 

Steven Arzt
committed
Apr 23, 2014

17

18
		protected int hashCode = 0;protectedinthashCode=0;



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

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
	public Pair() {publicPair(){		o1 = null;o1=null;		o2 = null;o2=null;	}}	public Pair(T o1, U o2) {publicPair(To1,Uo2){		this.o1 = o1;this.o1=o1;		this.o2 = o2;this.o2=o2;	}}	@Override@Override	public int hashCode() {publicinthashCode(){



now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014



now caching the hash code of the Pair class


 

 

now caching the hash code of the Pair class

 

Steven Arzt
committed
Apr 23, 2014

32

33

34
		if (hashCode != 0)if(hashCode!=0)			return hashCode;returnhashCode;		



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

35

36

37

38
		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + ((o1 == null) ? 0 : o1.hashCode());result=prime*result+((o1==null)?0:o1.hashCode());		result = prime * result + ((o2 == null) ? 0 : o2.hashCode());result=prime*result+((o2==null)?0:o2.hashCode());



now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014



now caching the hash code of the Pair class


 

 

now caching the hash code of the Pair class

 

Steven Arzt
committed
Apr 23, 2014

39

40

41
		hashCode = result;hashCode=result;				return hashCode;returnhashCode;



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

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
	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;		if (getClass() != obj.getClass())if(getClass()!=obj.getClass())			return false;returnfalse;		@SuppressWarnings("rawtypes")@SuppressWarnings("rawtypes")		Pair other = (Pair) obj;Pairother=(Pair)obj;		if (o1 == null) {if(o1==null){			if (other.o1 != null)if(other.o1!=null)				return false;returnfalse;		} else if (!o1.equals(other.o1))}elseif(!o1.equals(other.o1))			return false;returnfalse;		if (o2 == null) {if(o2==null){			if (other.o2 != null)if(other.o2!=null)				return false;returnfalse;		} else if (!o2.equals(other.o2))}elseif(!o2.equals(other.o2))			return false;returnfalse;		return true;returntrue;	}}	public String toString() {publicStringtoString(){		return "Pair " + o1 + "," + o2;return"Pair "+o1+","+o2;	}}	public T getO1() {publicTgetO1(){		return o1;returno1;	}}	public U getO2() {publicUgetO2(){		return o2;returno2;	}}	public void setO1(T no1) {publicvoidsetO1(Tno1){		o1 = no1;o1=no1;



now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014



now caching the hash code of the Pair class


 

 

now caching the hash code of the Pair class

 

Steven Arzt
committed
Apr 23, 2014

81
		hashCode = 0;hashCode=0;



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

82

83

84

85
	}}	public void setO2(U no2) {publicvoidsetO2(Uno2){		o2 = no2;o2=no2;



now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014



now caching the hash code of the Pair class


 

 

now caching the hash code of the Pair class

 

Steven Arzt
committed
Apr 23, 2014

86
		hashCode = 0;hashCode=0;



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

87

88

89

90

91
	}}	public void setPair(T no1, U no2) {publicvoidsetPair(Tno1,Uno2){		o1 = no1;o1=no1;		o2 = no2;o2=no2;



now caching the hash code of the Pair class


 

 


Steven Arzt
committed
Apr 23, 2014



now caching the hash code of the Pair class


 

 

now caching the hash code of the Pair class

 

Steven Arzt
committed
Apr 23, 2014

92
		hashCode = 0;hashCode=0;



adding missing files



 


Eric Bodden
committed
Nov 29, 2012



adding missing files



 

adding missing files


Eric Bodden
committed
Nov 29, 2012

93

94
	}}



license headers


 

 


Eric Bodden
committed
Nov 29, 2012



license headers


 

 

license headers

 

Eric Bodden
committed
Nov 29, 2012

95
}}





