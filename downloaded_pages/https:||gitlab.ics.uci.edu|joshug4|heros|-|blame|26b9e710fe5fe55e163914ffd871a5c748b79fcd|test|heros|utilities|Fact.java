



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

26b9e710fe5fe55e163914ffd871a5c748b79fcd

















26b9e710fe5fe55e163914ffd871a5c748b79fcd


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink






Fact.java



1.41 KB









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














package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import heros.solver.LinkedNode;

public class Fact implements LinkedNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51





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






52




53




54





	@Override
	public void addNeighbor(Fact originalAbstraction) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






55




56




57




58




59




		
	}

	@Override
	public void setCallingContext(Fact callingContext) {









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






60




61




		
	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






62




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

26b9e710fe5fe55e163914ffd871a5c748b79fcd

















26b9e710fe5fe55e163914ffd871a5c748b79fcd


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink






Fact.java



1.41 KB









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














package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import heros.solver.LinkedNode;

public class Fact implements LinkedNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51





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






52




53




54





	@Override
	public void addNeighbor(Fact originalAbstraction) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






55




56




57




58




59




		
	}

	@Override
	public void setCallingContext(Fact callingContext) {









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






60




61




		
	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






62




}











Open sidebar



Joshua Garcia heros

26b9e710fe5fe55e163914ffd871a5c748b79fcd







Open sidebar



Joshua Garcia heros

26b9e710fe5fe55e163914ffd871a5c748b79fcd




Open sidebar

Joshua Garcia heros

26b9e710fe5fe55e163914ffd871a5c748b79fcd


Joshua Garciaherosheros
26b9e710fe5fe55e163914ffd871a5c748b79fcd










26b9e710fe5fe55e163914ffd871a5c748b79fcd


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink






Fact.java



1.41 KB









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














package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import heros.solver.LinkedNode;

public class Fact implements LinkedNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51





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






52




53




54





	@Override
	public void addNeighbor(Fact originalAbstraction) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






55




56




57




58




59




		
	}

	@Override
	public void setCallingContext(Fact callingContext) {









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






60




61




		
	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






62




}














26b9e710fe5fe55e163914ffd871a5c748b79fcd


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink






Fact.java



1.41 KB









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














package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import heros.solver.LinkedNode;

public class Fact implements LinkedNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51





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






52




53




54





	@Override
	public void addNeighbor(Fact originalAbstraction) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






55




56




57




58




59




		
	}

	@Override
	public void setCallingContext(Fact callingContext) {









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






60




61




		
	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






62




}










26b9e710fe5fe55e163914ffd871a5c748b79fcd


Switch branch/tag










heros


test


heros


utilities


Fact.java



Find file
Normal viewHistoryPermalink




26b9e710fe5fe55e163914ffd871a5c748b79fcd


Switch branch/tag










heros


test


heros


utilities


Fact.java





26b9e710fe5fe55e163914ffd871a5c748b79fcd


Switch branch/tag








26b9e710fe5fe55e163914ffd871a5c748b79fcd


Switch branch/tag





26b9e710fe5fe55e163914ffd871a5c748b79fcd

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



1.41 KB









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














package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import heros.solver.LinkedNode;

public class Fact implements LinkedNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51





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






52




53




54





	@Override
	public void addNeighbor(Fact originalAbstraction) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






55




56




57




58




59




		
	}

	@Override
	public void setCallingContext(Fact callingContext) {









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






60




61




		
	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






62




}








Fact.java



1.41 KB










Fact.java



1.41 KB









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














package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






13




14




15




import heros.solver.LinkedNode;

public class Fact implements LinkedNode<Fact> {









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






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




49




50




51





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






52




53




54





	@Override
	public void addNeighbor(Fact originalAbstraction) {









store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014






55




56




57




58




59




		
	}

	@Override
	public void setCallingContext(Fact callingContext) {









package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014






60




61




		
	}









Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014






62




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




package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver


 

 

package restructuring & initial test for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

13

14

15
import heros.solver.LinkedNode;importheros.solver.LinkedNode;public class Fact implements LinkedNode<Fact> {publicclassFactimplementsLinkedNode<Fact>{



Tests for IDESolver



 


Johannes Lerch
committed
Jun 25, 2014



Tests for IDESolver



 

Tests for IDESolver


Johannes Lerch
committed
Jun 25, 2014

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

49

50

51
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

52

53

54
	@Override@Override	public void addNeighbor(Fact originalAbstraction) {publicvoidaddNeighbor(FactoriginalAbstraction){



store calling context in abstraction to enable context sensitive path


 

 


Johannes Lerch
committed
Jun 25, 2014



store calling context in abstraction to enable context sensitive path


 

 

store calling context in abstraction to enable context sensitive path

 

Johannes Lerch
committed
Jun 25, 2014

55

56

57

58

59
			}}	@Override@Override	public void setCallingContext(Fact callingContext) {publicvoidsetCallingContext(FactcallingContext){



package restructuring & initial test for BiDiSolver


 

 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver


 

 

package restructuring & initial test for BiDiSolver

 

Johannes Lerch
committed
Jun 25, 2014

60

61
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

62
}}





