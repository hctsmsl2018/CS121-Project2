



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

1f74a4f24b7c21a2edae5691e525dc33f35158de

















1f74a4f24b7c21a2edae5691e525dc33f35158de


Switch branch/tag










heros


test


heros


alias


TestFieldRef.java



Find file
Normal viewHistoryPermalink






TestFieldRef.java



1.35 KB









Newer










Older









merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16












remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
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

1f74a4f24b7c21a2edae5691e525dc33f35158de

















1f74a4f24b7c21a2edae5691e525dc33f35158de


Switch branch/tag










heros


test


heros


alias


TestFieldRef.java



Find file
Normal viewHistoryPermalink






TestFieldRef.java



1.35 KB









Newer










Older









merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16












remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}












Open sidebar



Joshua Garcia heros

1f74a4f24b7c21a2edae5691e525dc33f35158de







Open sidebar



Joshua Garcia heros

1f74a4f24b7c21a2edae5691e525dc33f35158de




Open sidebar

Joshua Garcia heros

1f74a4f24b7c21a2edae5691e525dc33f35158de


Joshua Garciaherosheros
1f74a4f24b7c21a2edae5691e525dc33f35158de










1f74a4f24b7c21a2edae5691e525dc33f35158de


Switch branch/tag










heros


test


heros


alias


TestFieldRef.java



Find file
Normal viewHistoryPermalink






TestFieldRef.java



1.35 KB









Newer










Older









merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16












remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}















1f74a4f24b7c21a2edae5691e525dc33f35158de


Switch branch/tag










heros


test


heros


alias


TestFieldRef.java



Find file
Normal viewHistoryPermalink






TestFieldRef.java



1.35 KB









Newer










Older









merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16












remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}











1f74a4f24b7c21a2edae5691e525dc33f35158de


Switch branch/tag










heros


test


heros


alias


TestFieldRef.java



Find file
Normal viewHistoryPermalink




1f74a4f24b7c21a2edae5691e525dc33f35158de


Switch branch/tag










heros


test


heros


alias


TestFieldRef.java





1f74a4f24b7c21a2edae5691e525dc33f35158de


Switch branch/tag








1f74a4f24b7c21a2edae5691e525dc33f35158de


Switch branch/tag





1f74a4f24b7c21a2edae5691e525dc33f35158de

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

alias

TestFieldRef.java
Find file
Normal viewHistoryPermalink




TestFieldRef.java



1.35 KB









Newer










Older









merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16












remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}









TestFieldRef.java



1.35 KB










TestFieldRef.java



1.35 KB









Newer










Older
NewerOlder







merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16












remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}











merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16












remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}









merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16












remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;








merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}







merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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







merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types


merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


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


/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




13



public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


13


public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {

public class TestFieldRef implements AccessPath.FieldRef<TestFieldRef> {publicclassTestFieldRefimplementsAccessPath.FieldRef<TestFieldRef>{




merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




14



	






merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types


merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


14


	

	




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




15



	public final String f;






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


15


	public final String f;

	public final String f;publicfinalStringf;




merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




16










merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types


merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


16









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




17



	public TestFieldRef(String f) {






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


17


	public TestFieldRef(String f) {

	public TestFieldRef(String f) {publicTestFieldRef(Stringf){




merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




18


19


20


21



		this.f = f;
	}
	
	@Override






merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types


merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


18


19


20


21


		this.f = f;
	}
	
	@Override

		this.f = f;this.f=f;	}}		@Override@Override




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




22



	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


22


	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {

	public boolean shouldBeMergedWith(TestFieldRef fieldRef) {publicbooleanshouldBeMergedWith(TestFieldReffieldRef){




merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




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



		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;






merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types


merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


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


		return f.equals(fieldRef.f);
	}
	
	@Override
	public String toString() {
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		return f.equals(fieldRef.f);returnf.equals(fieldRef.f);	}}		@Override@Override	public String toString() {publicStringtoString(){		return f;returnf;	}}	@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + ((f == null) ? 0 : f.hashCode());result=prime*result+((f==null)?0:f.hashCode());		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




45



		if (!(obj instanceof TestFieldRef))






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


45


		if (!(obj instanceof TestFieldRef))

		if (!(obj instanceof TestFieldRef))if(!(objinstanceofTestFieldRef))




merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




46



			return false;






merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types


merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


46


			return false;

			return false;returnfalse;




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



		TestFieldRef other = (TestFieldRef) obj;






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


47


		TestFieldRef other = (TestFieldRef) obj;

		TestFieldRef other = (TestFieldRef) obj;TestFieldRefother=(TestFieldRef)obj;




merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015




48


49


50


51


52


53


54


55



		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}





merge only on equal field types



Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types


merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


48


49


50


51


52


53


54


55


		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}
		if (f == null) {if(f==null){			if (other.f != null)if(other.f!=null)				return false;returnfalse;		} else if (!f.equals(other.f))}elseif(!f.equals(other.f))			return false;returnfalse;		return true;returntrue;	}}}}





