



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

55cdd2d88cc7d78204a4508a1dd46234cb52b74c

















55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Switch branch/tag










heros


src


heros


alias


SummaryEdge.java



Find file
Normal viewHistoryPermalink






SummaryEdge.java



2.08 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
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

55cdd2d88cc7d78204a4508a1dd46234cb52b74c

















55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Switch branch/tag










heros


src


heros


alias


SummaryEdge.java



Find file
Normal viewHistoryPermalink






SummaryEdge.java



2.08 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}












Open sidebar



Joshua Garcia heros

55cdd2d88cc7d78204a4508a1dd46234cb52b74c







Open sidebar



Joshua Garcia heros

55cdd2d88cc7d78204a4508a1dd46234cb52b74c




Open sidebar

Joshua Garcia heros

55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Joshua Garciaherosheros
55cdd2d88cc7d78204a4508a1dd46234cb52b74c










55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Switch branch/tag










heros


src


heros


alias


SummaryEdge.java



Find file
Normal viewHistoryPermalink






SummaryEdge.java



2.08 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}















55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Switch branch/tag










heros


src


heros


alias


SummaryEdge.java



Find file
Normal viewHistoryPermalink






SummaryEdge.java



2.08 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}











55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Switch branch/tag










heros


src


heros


alias


SummaryEdge.java



Find file
Normal viewHistoryPermalink




55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Switch branch/tag










heros


src


heros


alias


SummaryEdge.java





55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Switch branch/tag








55cdd2d88cc7d78204a4508a1dd46234cb52b74c


Switch branch/tag





55cdd2d88cc7d78204a4508a1dd46234cb52b74c

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

alias

SummaryEdge.java
Find file
Normal viewHistoryPermalink




SummaryEdge.java



2.08 KB









Newer










Older









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}









SummaryEdge.java



2.08 KB










SummaryEdge.java



2.08 KB









Newer










Older
NewerOlder







use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}











use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}









use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}








abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}








use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}







use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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


15


16


17


18


19


20


21


22


23



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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}






use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


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


15


16


17


18


19


20


21


22


23


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

public class SummaryEdge<D, N> {

	private D sourceFact;
	private N targetStmt;
	private D targetFact;
	
	public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {
		this.sourceFact = sourceFact;
		this.targetStmt = targetStmt;
		this.targetFact = targetFact;
	}

/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;public class SummaryEdge<D, N> {publicclassSummaryEdge<D,N>{	private D sourceFact;privateDsourceFact;	private N targetStmt;privateNtargetStmt;	private D targetFact;privateDtargetFact;		public SummaryEdge(D sourceFact, N targetStmt, D targetFact) {publicSummaryEdge(DsourceFact,NtargetStmt,DtargetFact){		this.sourceFact = sourceFact;this.sourceFact=sourceFact;		this.targetStmt = targetStmt;this.targetStmt=targetStmt;		this.targetFact = targetFact;this.targetFact=targetFact;	}}




abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015




24


25


26


27


28



	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}






abstract at return edges

 


Johannes Lerch
committed
Feb 23, 2015



abstract at return edges

 

abstract at return edges

Johannes Lerch
committed
Feb 23, 2015


24


25


26


27


28


	
	@Override
	public String toString() {
		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";
	}

		@Override@Override	public String toString() {publicStringtoString(){		return "[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";return"[SummaryEdge: "+sourceFact+" -> "+targetFact+" @stmt: "+targetStmt+"]";	}}




use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014




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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}





use of abstracted summaries



Johannes Lerch
committed
Oct 22, 2014



use of abstracted summaries


use of abstracted summaries

Johannes Lerch
committed
Oct 22, 2014


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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());
		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());
		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SummaryEdge))
			return false;
		SummaryEdge other = (SummaryEdge) obj;
		if (sourceFact == null) {
			if (other.sourceFact != null)
				return false;
		} else if (!sourceFact.equals(other.sourceFact))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		if (targetStmt == null) {
			if (other.targetStmt != null)
				return false;
		} else if (!targetStmt.equals(other.targetStmt))
			return false;
		return true;
	}

	public D getSourceFact() {
		return sourceFact;
	}
	
	public D getTargetFact() {
		return targetFact;
	}
	
	public N getTargetStmt() {
		return targetStmt;
	}
	
}
	@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + ((sourceFact == null) ? 0 : sourceFact.hashCode());result=prime*result+((sourceFact==null)?0:sourceFact.hashCode());		result = prime * result + ((targetFact == null) ? 0 : targetFact.hashCode());result=prime*result+((targetFact==null)?0:targetFact.hashCode());		result = prime * result + ((targetStmt == null) ? 0 : targetStmt.hashCode());result=prime*result+((targetStmt==null)?0:targetStmt.hashCode());		return result;returnresult;	}}		@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;		if (!(obj instanceof SummaryEdge))if(!(objinstanceofSummaryEdge))			return false;returnfalse;		SummaryEdge other = (SummaryEdge) obj;SummaryEdgeother=(SummaryEdge)obj;		if (sourceFact == null) {if(sourceFact==null){			if (other.sourceFact != null)if(other.sourceFact!=null)				return false;returnfalse;		} else if (!sourceFact.equals(other.sourceFact))}elseif(!sourceFact.equals(other.sourceFact))			return false;returnfalse;		if (targetFact == null) {if(targetFact==null){			if (other.targetFact != null)if(other.targetFact!=null)				return false;returnfalse;		} else if (!targetFact.equals(other.targetFact))}elseif(!targetFact.equals(other.targetFact))			return false;returnfalse;		if (targetStmt == null) {if(targetStmt==null){			if (other.targetStmt != null)if(other.targetStmt!=null)				return false;returnfalse;		} else if (!targetStmt.equals(other.targetStmt))}elseif(!targetStmt.equals(other.targetStmt))			return false;returnfalse;		return true;returntrue;	}}	public D getSourceFact() {publicDgetSourceFact(){		return sourceFact;returnsourceFact;	}}		public D getTargetFact() {publicDgetTargetFact(){		return targetFact;returntargetFact;	}}		public N getTargetStmt() {publicNgetTargetStmt(){		return targetStmt;returntargetStmt;	}}	}}





