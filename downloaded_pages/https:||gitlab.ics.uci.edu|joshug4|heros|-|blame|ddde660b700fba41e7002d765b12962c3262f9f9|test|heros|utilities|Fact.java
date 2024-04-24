



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

ddde660b700fba41e7002d765b12962c3262f9f9

















ddde660b700fba41e7002d765b12962c3262f9f9


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink






Fact.java



1.97 KB









Newer










Older









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






11




package heros.utilities;









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






12














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






13




import heros.solver.JoinHandlingNode;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






14




import heros.solver.LinkedNode;









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






15




import heros.solver.JoinHandlingNode.JoinKey;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






16














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






17




public class Fact implements JoinHandlingNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51




52




53





	public final String name;
	
	public Fact(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Fact))
			return false;
		Fact other = (Fact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Fact "+name+"]";
	}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






54




55





	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






56




	public void setCallingContext(Fact callingContext) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






57




58




59




60




		
	}

	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






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




	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override
	public boolean handleJoin(Fact joiningNode) {
		return true;
	}

	private class TestJoinKey extends JoinKey {

		private Fact getFact() {
			return Fact.this;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TestJoinKey) {
				return getFact().equals(((TestJoinKey) obj).getFact());
			}
			throw new IllegalArgumentException();
		}

		@Override
		public int hashCode() {
			return Fact.this.hashCode();
		}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






88




	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






89




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

ddde660b700fba41e7002d765b12962c3262f9f9

















ddde660b700fba41e7002d765b12962c3262f9f9


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink






Fact.java



1.97 KB









Newer










Older









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






11




package heros.utilities;









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






12














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






13




import heros.solver.JoinHandlingNode;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






14




import heros.solver.LinkedNode;









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






15




import heros.solver.JoinHandlingNode.JoinKey;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






16














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






17




public class Fact implements JoinHandlingNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51




52




53





	public final String name;
	
	public Fact(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Fact))
			return false;
		Fact other = (Fact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Fact "+name+"]";
	}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






54




55





	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






56




	public void setCallingContext(Fact callingContext) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






57




58




59




60




		
	}

	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






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




	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override
	public boolean handleJoin(Fact joiningNode) {
		return true;
	}

	private class TestJoinKey extends JoinKey {

		private Fact getFact() {
			return Fact.this;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TestJoinKey) {
				return getFact().equals(((TestJoinKey) obj).getFact());
			}
			throw new IllegalArgumentException();
		}

		@Override
		public int hashCode() {
			return Fact.this.hashCode();
		}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






88




	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






89




}











Open sidebar



Joshua Garcia heros

ddde660b700fba41e7002d765b12962c3262f9f9







Open sidebar



Joshua Garcia heros

ddde660b700fba41e7002d765b12962c3262f9f9




Open sidebar

Joshua Garcia heros

ddde660b700fba41e7002d765b12962c3262f9f9


Joshua Garciaherosheros
ddde660b700fba41e7002d765b12962c3262f9f9










ddde660b700fba41e7002d765b12962c3262f9f9


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink






Fact.java



1.97 KB









Newer










Older









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






11




package heros.utilities;









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






12














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






13




import heros.solver.JoinHandlingNode;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






14




import heros.solver.LinkedNode;









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






15




import heros.solver.JoinHandlingNode.JoinKey;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






16














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






17




public class Fact implements JoinHandlingNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51




52




53





	public final String name;
	
	public Fact(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Fact))
			return false;
		Fact other = (Fact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Fact "+name+"]";
	}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






54




55





	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






56




	public void setCallingContext(Fact callingContext) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






57




58




59




60




		
	}

	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






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




	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override
	public boolean handleJoin(Fact joiningNode) {
		return true;
	}

	private class TestJoinKey extends JoinKey {

		private Fact getFact() {
			return Fact.this;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TestJoinKey) {
				return getFact().equals(((TestJoinKey) obj).getFact());
			}
			throw new IllegalArgumentException();
		}

		@Override
		public int hashCode() {
			return Fact.this.hashCode();
		}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






88




	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






89




}














ddde660b700fba41e7002d765b12962c3262f9f9


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink






Fact.java



1.97 KB









Newer










Older









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






11




package heros.utilities;









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






12














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






13




import heros.solver.JoinHandlingNode;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






14




import heros.solver.LinkedNode;









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






15




import heros.solver.JoinHandlingNode.JoinKey;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






16














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






17




public class Fact implements JoinHandlingNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51




52




53





	public final String name;
	
	public Fact(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Fact))
			return false;
		Fact other = (Fact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Fact "+name+"]";
	}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






54




55





	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






56




	public void setCallingContext(Fact callingContext) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






57




58




59




60




		
	}

	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






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




	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override
	public boolean handleJoin(Fact joiningNode) {
		return true;
	}

	private class TestJoinKey extends JoinKey {

		private Fact getFact() {
			return Fact.this;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TestJoinKey) {
				return getFact().equals(((TestJoinKey) obj).getFact());
			}
			throw new IllegalArgumentException();
		}

		@Override
		public int hashCode() {
			return Fact.this.hashCode();
		}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






88




	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






89




}










ddde660b700fba41e7002d765b12962c3262f9f9


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink




ddde660b700fba41e7002d765b12962c3262f9f9


Switch branch/tag










heros


test


heros


utilities


Fact.java





ddde660b700fba41e7002d765b12962c3262f9f9


Switch branch/tag








ddde660b700fba41e7002d765b12962c3262f9f9


Switch branch/tag





ddde660b700fba41e7002d765b12962c3262f9f9

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

utilities

Fact.java
Find file
Normal viewHistoryPermalink




Fact.java



1.97 KB









Newer










Older









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






11




package heros.utilities;









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






12














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






13




import heros.solver.JoinHandlingNode;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






14




import heros.solver.LinkedNode;









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






15




import heros.solver.JoinHandlingNode.JoinKey;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






16














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






17




public class Fact implements JoinHandlingNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51




52




53





	public final String name;
	
	public Fact(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Fact))
			return false;
		Fact other = (Fact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Fact "+name+"]";
	}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






54




55





	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






56




	public void setCallingContext(Fact callingContext) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






57




58




59




60




		
	}

	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






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




	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override
	public boolean handleJoin(Fact joiningNode) {
		return true;
	}

	private class TestJoinKey extends JoinKey {

		private Fact getFact() {
			return Fact.this;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TestJoinKey) {
				return getFact().equals(((TestJoinKey) obj).getFact());
			}
			throw new IllegalArgumentException();
		}

		@Override
		public int hashCode() {
			return Fact.this.hashCode();
		}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






88




	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






89




}








Fact.java



1.97 KB










Fact.java



1.97 KB









Newer










Older
NewerOlder







Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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
 * Copyright (c) 2014 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






11




package heros.utilities;









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






12














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






13




import heros.solver.JoinHandlingNode;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






14




import heros.solver.LinkedNode;









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






15




import heros.solver.JoinHandlingNode.JoinKey;









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






16














Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






17




public class Fact implements JoinHandlingNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51




52




53





	public final String name;
	
	public Fact(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Fact))
			return false;
		Fact other = (Fact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Fact "+name+"]";
	}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






54




55





	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






56




	public void setCallingContext(Fact callingContext) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






57




58




59




60




		
	}

	@Override









Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014






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




	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override
	public boolean handleJoin(Fact joiningNode) {
		return true;
	}

	private class TestJoinKey extends JoinKey {

		private Fact getFact() {
			return Fact.this;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TestJoinKey) {
				return getFact().equals(((TestJoinKey) obj).getFact());
			}
			throw new IllegalArgumentException();
		}

		@Override
		public int hashCode() {
			return Fact.this.hashCode();
		}









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






88




	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






89




}







Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014



Tests for IDESolver



 

Tests for IDESolver


Johannes Lerch
committed
Jun 25, 2014

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/



package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver


 

 

package restructuring & initial test for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

11
package heros.utilities;packageheros.utilities;



Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014



Tests for IDESolver



 

Tests for IDESolver


Johannes Lerch
committed
Jun 25, 2014

12




Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014



Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 

Introducing more abstract/flexible version of PathTrackingIFDSSolver

 

Johannes Lerch
committed
Sep 26, 2014

13
import heros.solver.JoinHandlingNode;importheros.solver.JoinHandlingNode;



package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver


 

 

package restructuring & initial test for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

14
import heros.solver.LinkedNode;importheros.solver.LinkedNode;



Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014



Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 

Introducing more abstract/flexible version of PathTrackingIFDSSolver

 

Johannes Lerch
committed
Sep 26, 2014

15
import heros.solver.JoinHandlingNode.JoinKey;importheros.solver.JoinHandlingNode.JoinKey;



package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver


 

 

package restructuring & initial test for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

16




Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014



Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 

Introducing more abstract/flexible version of PathTrackingIFDSSolver

 

Johannes Lerch
committed
Sep 26, 2014

17
public class Fact implements JoinHandlingNode<Fact> {publicclassFactimplementsJoinHandlingNode<Fact>{



Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014



Tests for IDESolver



 

Tests for IDESolver


Johannes Lerch
committed
Jun 25, 2014

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

49

50

51

52

53
	public final String name;publicfinalStringname;		public Fact(String name) {publicFact(Stringname){		this.name = name;this.name=name;	}}	@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + ((name == null) ? 0 : name.hashCode());result=prime*result+((name==null)?0:name.hashCode());		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;		if (!(obj instanceof Fact))if(!(objinstanceofFact))			return false;returnfalse;		Fact other = (Fact) obj;Factother=(Fact)obj;		if (name == null) {if(name==null){			if (other.name != null)if(other.name!=null)				return false;returnfalse;		} else if (!name.equals(other.name))}elseif(!name.equals(other.name))			return false;returnfalse;		return true;returntrue;	}}		@Override@Override	public String toString() {publicStringtoString(){		return "[Fact "+name+"]";return"[Fact "+name+"]";	}}



package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver


 

 

package restructuring & initial test for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

54

55
	@Override@Override



Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014



Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 

Introducing more abstract/flexible version of PathTrackingIFDSSolver

 

Johannes Lerch
committed
Sep 26, 2014

56
	public void setCallingContext(Fact callingContext) {publicvoidsetCallingContext(FactcallingContext){



store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014



store calling context in abstraction to enable context sensitive path


 

 

store calling context in abstraction to enable context sensitive path

 

Johannes Lerch
committed
Jun 25, 2014

57

58

59

60
			}}	@Override@Override



Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 


Johannes Lerch
committed
Sep 26, 2014



Introducing more abstract/flexible version of PathTrackingIFDSSolver


 

 

Introducing more abstract/flexible version of PathTrackingIFDSSolver

 

Johannes Lerch
committed
Sep 26, 2014

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
	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {publicheros.solver.JoinHandlingNode.JoinKeycreateJoinKey(){		return new TestJoinKey();returnnewTestJoinKey();	}}	@Override@Override	public boolean handleJoin(Fact joiningNode) {publicbooleanhandleJoin(FactjoiningNode){		return true;returntrue;	}}	private class TestJoinKey extends JoinKey {privateclassTestJoinKeyextendsJoinKey{		private Fact getFact() {privateFactgetFact(){			return Fact.this;returnFact.this;		}}		@Override@Override		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (obj instanceof TestJoinKey) {if(objinstanceofTestJoinKey){				return getFact().equals(((TestJoinKey) obj).getFact());returngetFact().equals(((TestJoinKey)obj).getFact());			}}			throw new IllegalArgumentException();thrownewIllegalArgumentException();		}}		@Override@Override		public int hashCode() {publicinthashCode(){			return Fact.this.hashCode();returnFact.this.hashCode();		}}



package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver


 

 

package restructuring & initial test for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

88
	}}



Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014



Tests for IDESolver



 

Tests for IDESolver


Johannes Lerch
committed
Jun 25, 2014

89
}}





