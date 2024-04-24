



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

2de8ef315dc517ffd02fda343bdfd0e0f664a570



















heros


test


heros


utilities


JoinableFact.java




Find file



Normal view


History


Permalink








JoinableFact.java



2.06 KiB









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












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {








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



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



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

2de8ef315dc517ffd02fda343bdfd0e0f664a570



















heros


test


heros


utilities


JoinableFact.java




Find file



Normal view


History


Permalink








JoinableFact.java



2.06 KiB









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












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {








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



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



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
















Joshua Garcia heros

2de8ef315dc517ffd02fda343bdfd0e0f664a570












Joshua Garcia heros

2de8ef315dc517ffd02fda343bdfd0e0f664a570










Joshua Garcia heros

2de8ef315dc517ffd02fda343bdfd0e0f664a570




Joshua Garciaherosheros
2de8ef315dc517ffd02fda343bdfd0e0f664a570












heros


test


heros


utilities


JoinableFact.java




Find file



Normal view


History


Permalink








JoinableFact.java



2.06 KiB









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












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {








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



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



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

















heros


test


heros


utilities


JoinableFact.java




Find file



Normal view


History


Permalink








JoinableFact.java



2.06 KiB









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












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {








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



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



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













heros


test


heros


utilities


JoinableFact.java




Find file



Normal view


History


Permalink








heros


test


heros


utilities


JoinableFact.java





heros

test

heros

utilities

JoinableFact.java

Find file



Normal view


History


Permalink


Find file


Normal view

History

Permalink





JoinableFact.java



2.06 KiB









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












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {








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



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



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









JoinableFact.java



2.06 KiB










JoinableFact.java



2.06 KiB









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












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {








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



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



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












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {








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



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



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












restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {








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



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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








restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();








Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



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

/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/




package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




11



package heros.utilities;






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


package heros.utilities;

package heros.utilities;packageheros.utilities;




Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




12










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




13



import heros.solver.JoinHandlingNode;






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


import heros.solver.JoinHandlingNode;

import heros.solver.JoinHandlingNode;importheros.solver.JoinHandlingNode;




package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




14



import heros.solver.LinkedNode;






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


import heros.solver.LinkedNode;

import heros.solver.LinkedNode;importheros.solver.LinkedNode;




Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




15



import heros.solver.JoinHandlingNode.JoinKey;






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


import heros.solver.JoinHandlingNode.JoinKey;

import heros.solver.JoinHandlingNode.JoinKey;importheros.solver.JoinHandlingNode.JoinKey;




package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




16










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









restructuring

 


Johannes Lerch
committed
Mar 26, 2015




17



public class JoinableFact implements JoinHandlingNode<JoinableFact> {






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


17


public class JoinableFact implements JoinHandlingNode<JoinableFact> {

public class JoinableFact implements JoinHandlingNode<JoinableFact> {publicclassJoinableFactimplementsJoinHandlingNode<JoinableFact>{




Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




18


19


20




	public final String name;
	






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



	public final String name;
	

	public final String name;publicfinalStringname;	




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




21



	public JoinableFact(String name) {






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


21


	public JoinableFact(String name) {

	public JoinableFact(String name) {publicJoinableFact(Stringname){




Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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






Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014



Tests for IDESolver


Tests for IDESolver

Johannes Lerch
committed
Jun 25, 2014


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

		this.name = name;this.name=name;	}}	@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + ((name == null) ? 0 : name.hashCode());result=prime*result+((name==null)?0:name.hashCode());		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




39



		if (!(obj instanceof JoinableFact))






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


39


		if (!(obj instanceof JoinableFact))

		if (!(obj instanceof JoinableFact))if(!(objinstanceofJoinableFact))




Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




40



			return false;






Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014



Tests for IDESolver


Tests for IDESolver

Johannes Lerch
committed
Jun 25, 2014


40


			return false;

			return false;returnfalse;




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




41



		JoinableFact other = (JoinableFact) obj;






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


41


		JoinableFact other = (JoinableFact) obj;

		JoinableFact other = (JoinableFact) obj;JoinableFactother=(JoinableFact)obj;




Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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






Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014



Tests for IDESolver


Tests for IDESolver

Johannes Lerch
committed
Jun 25, 2014


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

		if (name == null) {if(name==null){			if (other.name != null)if(other.name!=null)				return false;returnfalse;		} else if (!name.equals(other.name))}elseif(!name.equals(other.name))			return false;returnfalse;		return true;returntrue;	}}		@Override@Override	public String toString() {publicStringtoString(){		return "[Fact "+name+"]";return"[Fact "+name+"]";	}}




package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




54


55




	@Override






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



	@Override

	@Override@Override




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




56



	public void setCallingContext(JoinableFact callingContext) {






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


	public void setCallingContext(JoinableFact callingContext) {

	public void setCallingContext(JoinableFact callingContext) {publicvoidsetCallingContext(JoinableFactcallingContext){




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


		
	}

	@Override

			}}	@Override@Override




Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




61


62


63


64


65



	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override






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


	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {
		return new TestJoinKey();
	}

	@Override

	public heros.solver.JoinHandlingNode.JoinKey createJoinKey() {publicheros.solver.JoinHandlingNode.JoinKeycreateJoinKey(){		return new TestJoinKey();returnnewTestJoinKey();	}}	@Override@Override




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




66



	public boolean handleJoin(JoinableFact joiningNode) {






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


	public boolean handleJoin(JoinableFact joiningNode) {

	public boolean handleJoin(JoinableFact joiningNode) {publicbooleanhandleJoin(JoinableFactjoiningNode){




Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




67


68


69


70


71



		return true;
	}

	private class TestJoinKey extends JoinKey {







Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014



Introducing more abstract/flexible version of PathTrackingIFDSSolver

 

Introducing more abstract/flexible version of PathTrackingIFDSSolver

Johannes Lerch
committed
Sep 26, 2014


67


68


69


70


71


		return true;
	}

	private class TestJoinKey extends JoinKey {


		return true;returntrue;	}}	private class TestJoinKey extends JoinKey {privateclassTestJoinKeyextendsJoinKey{




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




72


73



		private JoinableFact getFact() {
			return JoinableFact.this;






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


72


73


		private JoinableFact getFact() {
			return JoinableFact.this;

		private JoinableFact getFact() {privateJoinableFactgetFact(){			return JoinableFact.this;returnJoinableFact.this;




Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




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






Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014



Introducing more abstract/flexible version of PathTrackingIFDSSolver

 

Introducing more abstract/flexible version of PathTrackingIFDSSolver

Johannes Lerch
committed
Sep 26, 2014


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

		}}		@Override@Override		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (obj instanceof TestJoinKey) {if(objinstanceofTestJoinKey){				return getFact().equals(((TestJoinKey) obj).getFact());returngetFact().equals(((TestJoinKey)obj).getFact());			}}			throw new IllegalArgumentException();thrownewIllegalArgumentException();		}}		@Override@Override		public int hashCode() {publicinthashCode(){




restructuring

 


Johannes Lerch
committed
Mar 26, 2015




86



			return JoinableFact.this.hashCode();






restructuring

 


Johannes Lerch
committed
Mar 26, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Mar 26, 2015


86


			return JoinableFact.this.hashCode();

			return JoinableFact.this.hashCode();returnJoinableFact.this.hashCode();




Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014




87



		}






Introducing more abstract/flexible version of PathTrackingIFDSSolver

 


Johannes Lerch
committed
Sep 26, 2014



Introducing more abstract/flexible version of PathTrackingIFDSSolver

 

Introducing more abstract/flexible version of PathTrackingIFDSSolver

Johannes Lerch
committed
Sep 26, 2014


87


		}

		}}




package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




88



	}






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


	}

	}}




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


89


}
}}





