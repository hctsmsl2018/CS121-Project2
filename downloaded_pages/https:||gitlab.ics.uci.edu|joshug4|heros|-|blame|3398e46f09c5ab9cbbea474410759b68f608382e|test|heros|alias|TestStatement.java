



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

3398e46f09c5ab9cbbea474410759b68f608382e

















3398e46f09c5ab9cbbea474410759b68f608382e


Switch branch/tag










heros


test


heros


alias


TestStatement.java



Find file
Normal viewHistoryPermalink






TestStatement.java



1.32 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
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

3398e46f09c5ab9cbbea474410759b68f608382e

















3398e46f09c5ab9cbbea474410759b68f608382e


Switch branch/tag










heros


test


heros


alias


TestStatement.java



Find file
Normal viewHistoryPermalink






TestStatement.java



1.32 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
	}
}












Open sidebar



Joshua Garcia heros

3398e46f09c5ab9cbbea474410759b68f608382e







Open sidebar



Joshua Garcia heros

3398e46f09c5ab9cbbea474410759b68f608382e




Open sidebar

Joshua Garcia heros

3398e46f09c5ab9cbbea474410759b68f608382e


Joshua Garciaherosheros
3398e46f09c5ab9cbbea474410759b68f608382e










3398e46f09c5ab9cbbea474410759b68f608382e


Switch branch/tag










heros


test


heros


alias


TestStatement.java



Find file
Normal viewHistoryPermalink






TestStatement.java



1.32 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
	}
}















3398e46f09c5ab9cbbea474410759b68f608382e


Switch branch/tag










heros


test


heros


alias


TestStatement.java



Find file
Normal viewHistoryPermalink






TestStatement.java



1.32 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
	}
}











3398e46f09c5ab9cbbea474410759b68f608382e


Switch branch/tag










heros


test


heros


alias


TestStatement.java



Find file
Normal viewHistoryPermalink




3398e46f09c5ab9cbbea474410759b68f608382e


Switch branch/tag










heros


test


heros


alias


TestStatement.java





3398e46f09c5ab9cbbea474410759b68f608382e


Switch branch/tag








3398e46f09c5ab9cbbea474410759b68f608382e


Switch branch/tag





3398e46f09c5ab9cbbea474410759b68f608382e

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

alias

TestStatement.java
Find file
Normal viewHistoryPermalink




TestStatement.java



1.32 KB









Newer










Older









FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
	}
}









TestStatement.java



1.32 KB










TestStatement.java



1.32 KB









Newer










Older
NewerOlder







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
	}
}











FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
	}
}









FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;









rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;








rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;








FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
	}
}







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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
package heros.alias;







FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


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
package heros.alias;


/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




13



public class TestStatement {






rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver

 

rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


13


public class TestStatement {

public class TestStatement {publicclassTestStatement{




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




14


15


16




	public final String identifier;
	






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


14


15


16



	public final String identifier;
	

	public final String identifier;publicfinalStringidentifier;	




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




17



	public TestStatement(String identifier) {






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


	public TestStatement(String identifier) {

	public TestStatement(String identifier) {publicTestStatement(Stringidentifier){




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;






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


		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		this.identifier = identifier;this.identifier=identifier;	}}	@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());result=prime*result+((identifier==null)?0:identifier.hashCode());		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




35



		if (!(obj instanceof TestStatement))






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


		if (!(obj instanceof TestStatement))

		if (!(obj instanceof TestStatement))if(!(objinstanceofTestStatement))




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




36



			return false;






FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014



FieldSensitiveIFDSSolver - concept outline


FieldSensitiveIFDSSolver - concept outline

Johannes Späth
committed
Oct 15, 2014


36


			return false;

			return false;returnfalse;




rewrite of ifds solver

 


Johannes Lerch
committed
Mar 19, 2015




37



		TestStatement other = (TestStatement) obj;






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


		TestStatement other = (TestStatement) obj;

		TestStatement other = (TestStatement) obj;TestStatementother=(TestStatement)obj;




FieldSensitiveIFDSSolver - concept outline



Johannes Späth
committed
Oct 15, 2014




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



		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
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


		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[Statement "+identifier+"]";
	}
}
		if (identifier == null) {if(identifier==null){			if (other.identifier != null)if(other.identifier!=null)				return false;returnfalse;		} else if (!identifier.equals(other.identifier))}elseif(!identifier.equals(other.identifier))			return false;returnfalse;		return true;returntrue;	}}		@Override@Override	public String toString() {publicStringtoString(){		return "[Statement "+identifier+"]";return"[Statement "+identifier+"]";	}}}}





