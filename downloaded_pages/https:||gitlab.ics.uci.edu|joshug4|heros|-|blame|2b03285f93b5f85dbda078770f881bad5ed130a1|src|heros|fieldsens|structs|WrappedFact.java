



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

2b03285f93b5f85dbda078770f881bad5ed130a1

















2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


src


heros


fieldsens


structs


WrappedFact.java



Find file
Normal viewHistoryPermalink






WrappedFact.java



2.98 KB









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








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
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

2b03285f93b5f85dbda078770f881bad5ed130a1

















2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


src


heros


fieldsens


structs


WrappedFact.java



Find file
Normal viewHistoryPermalink






WrappedFact.java



2.98 KB









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








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
}












Open sidebar



Joshua Garcia heros

2b03285f93b5f85dbda078770f881bad5ed130a1







Open sidebar



Joshua Garcia heros

2b03285f93b5f85dbda078770f881bad5ed130a1




Open sidebar

Joshua Garcia heros

2b03285f93b5f85dbda078770f881bad5ed130a1


Joshua Garciaherosheros
2b03285f93b5f85dbda078770f881bad5ed130a1










2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


src


heros


fieldsens


structs


WrappedFact.java



Find file
Normal viewHistoryPermalink






WrappedFact.java



2.98 KB









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








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
}















2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


src


heros


fieldsens


structs


WrappedFact.java



Find file
Normal viewHistoryPermalink






WrappedFact.java



2.98 KB









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








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
}











2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


src


heros


fieldsens


structs


WrappedFact.java



Find file
Normal viewHistoryPermalink




2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag










heros


src


heros


fieldsens


structs


WrappedFact.java





2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag








2b03285f93b5f85dbda078770f881bad5ed130a1


Switch branch/tag





2b03285f93b5f85dbda078770f881bad5ed130a1

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

fieldsens

structs

WrappedFact.java
Find file
Normal viewHistoryPermalink




WrappedFact.java



2.98 KB









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








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
}









WrappedFact.java



2.98 KB










WrappedFact.java



2.98 KB









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








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
}











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








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
}









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








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18












removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	








removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else








switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
}







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

/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/




restructuring

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens.structs;






restructuring

 


Johannes Lerch
committed
Jun 01, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Jun 01, 2015


11


package heros.fieldsens.structs;

package heros.fieldsens.structs;packageheros.fieldsens.structs;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




12










rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


12









restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;






restructuring

 


Johannes Lerch
committed
Jun 01, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Jun 01, 2015


13


14


15


16


import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;
import heros.fieldsens.AccessPath.Delta;

import heros.fieldsens.AccessPath;importheros.fieldsens.AccessPath;import heros.fieldsens.FlowFunction;importheros.fieldsens.FlowFunction;import heros.fieldsens.Resolver;importheros.fieldsens.Resolver;import heros.fieldsens.AccessPath.Delta;importheros.fieldsens.AccessPath.Delta;




renaming package

 


Johannes Lerch
committed
Jun 01, 2015




17



import heros.fieldsens.FlowFunction.Constraint;






renaming package

 


Johannes Lerch
committed
Jun 01, 2015



renaming package

 

renaming package

Johannes Lerch
committed
Jun 01, 2015


17


import heros.fieldsens.FlowFunction.Constraint;

import heros.fieldsens.FlowFunction.Constraint;importheros.fieldsens.FlowFunction.Constraint;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




18










rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


18









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public class WrappedFact<Field, Fact, Stmt, Method>{






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


19


public class WrappedFact<Field, Fact, Stmt, Method>{

public class WrappedFact<Field, Fact, Stmt, Method>{publicclassWrappedFact<Field,Fact,Stmt,Method>{




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




20


21


22


23


24




	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


20


21


22


23


24



	private final Fact fact;
	private final AccessPath<Field> accessPath;
	private final Resolver<Field, Fact, Stmt, Method> resolver;
	

	private final Fact fact;privatefinalFactfact;	private final AccessPath<Field> accessPath;privatefinalAccessPath<Field>accessPath;	private final Resolver<Field, Fact, Stmt, Method> resolver;privatefinalResolver<Field,Fact,Stmt,Method>resolver;	




restructuring

 


Johannes Lerch
committed
Jun 01, 2015




25



	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {






restructuring

 


Johannes Lerch
committed
Jun 01, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Jun 01, 2015


25


	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {

	public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {publicWrappedFact(Factfact,AccessPath<Field>accessPath,Resolver<Field,Fact,Stmt,Method>resolver){




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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



		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
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


		assert fact != null;
		assert accessPath != null;
		assert resolver != null;
		
		this.fact = fact;
		this.accessPath = accessPath;
		this.resolver = resolver;
	}
	
	public Fact getFact() {
		return fact;
	}
	

		assert fact != null;assertfact!=null;		assert accessPath != null;assertaccessPath!=null;		assert resolver != null;assertresolver!=null;				this.fact = fact;this.fact=fact;		this.accessPath = accessPath;this.accessPath=accessPath;		this.resolver = resolver;this.resolver=resolver;	}}		public Fact getFact() {publicFactgetFact(){		return fact;returnfact;	}}	




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




39



	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


39


	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {

	public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {publicWrappedFact<Field,Fact,Stmt,Method>applyDelta(AccessPath.Delta<Field>delta){




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




40



		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


40


		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?

		return new WrappedFact<Field, Fact, Stmt, Method>(fact, delta.applyTo(accessPath), resolver); //TODO keep resolver?returnnewWrappedFact<Field,Fact,Stmt,Method>(fact,delta.applyTo(accessPath),resolver);//TODO keep resolver?




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




41


42


43


44


45


46



	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
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


41


42


43


44


45


46


	}

	public AccessPath<Field> getAccessPath() {
		return accessPath;
	}
	

	}}	public AccessPath<Field> getAccessPath() {publicAccessPath<Field>getAccessPath(){		return accessPath;returnaccessPath;	}}	




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




47



	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


47


	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {

	public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(Constraint<Field> constraint, Fact zeroValue) {publicWrappedFact<Field,Fact,Stmt,Method>applyConstraint(Constraint<Field>constraint,FactzeroValue){




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




48


49


50



		if(fact.equals(zeroValue))
			return this;
		else






rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


48


49


50


		if(fact.equals(zeroValue))
			return this;
		else

		if(fact.equals(zeroValue))if(fact.equals(zeroValue))			return this;returnthis;		elseelse




switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015




51



			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);






switching to Java 6 compatibility

 


Johannes Lerch
committed
Jun 01, 2015



switching to Java 6 compatibility

 

switching to Java 6 compatibility

Johannes Lerch
committed
Jun 01, 2015


51


			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);

			return new WrappedFact<Field, Fact, Stmt, Method>(fact, constraint.applyToAccessPath(accessPath), resolver);returnnewWrappedFact<Field,Fact,Stmt,Method>(fact,constraint.applyToAccessPath(accessPath),resolver);




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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


104



	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
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


104


	}
	
	@Override
	public String toString() {
		String result = fact.toString()+accessPath;
		if(resolver != null)
			result+=resolver.toString();
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
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
		WrappedFact other = (WrappedFact) obj;
		if (accessPath == null) {
			if (other.accessPath != null)
				return false;
		} else if (!accessPath.equals(other.accessPath))
			return false;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

	public Resolver<Field, Fact, Stmt, Method> getResolver() {
		return resolver;
	}
	
	
}
	}}		@Override@Override	public String toString() {publicStringtoString(){		String result = fact.toString()+accessPath;Stringresult=fact.toString()+accessPath;		if(resolver != null)if(resolver!=null)			result+=resolver.toString();result+=resolver.toString();		return result;returnresult;	}}		@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + ((accessPath == null) ? 0 : accessPath.hashCode());result=prime*result+((accessPath==null)?0:accessPath.hashCode());		result = prime * result + ((fact == null) ? 0 : fact.hashCode());result=prime*result+((fact==null)?0:fact.hashCode());		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());result=prime*result+((resolver==null)?0:resolver.hashCode());		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;		if (getClass() != obj.getClass())if(getClass()!=obj.getClass())			return false;returnfalse;		WrappedFact other = (WrappedFact) obj;WrappedFactother=(WrappedFact)obj;		if (accessPath == null) {if(accessPath==null){			if (other.accessPath != null)if(other.accessPath!=null)				return false;returnfalse;		} else if (!accessPath.equals(other.accessPath))}elseif(!accessPath.equals(other.accessPath))			return false;returnfalse;		if (fact == null) {if(fact==null){			if (other.fact != null)if(other.fact!=null)				return false;returnfalse;		} else if (!fact.equals(other.fact))}elseif(!fact.equals(other.fact))			return false;returnfalse;		if (resolver == null) {if(resolver==null){			if (other.resolver != null)if(other.resolver!=null)				return false;returnfalse;		} else if (!resolver.equals(other.resolver))}elseif(!resolver.equals(other.resolver))			return false;returnfalse;		return true;returntrue;	}}	public Resolver<Field, Fact, Stmt, Method> getResolver() {publicResolver<Field,Fact,Stmt,Method>getResolver(){		return resolver;returnresolver;	}}		}}





