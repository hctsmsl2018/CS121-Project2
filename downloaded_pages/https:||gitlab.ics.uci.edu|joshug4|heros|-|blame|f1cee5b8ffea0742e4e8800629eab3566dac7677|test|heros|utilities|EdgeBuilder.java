



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

f1cee5b8ffea0742e4e8800629eab3566dac7677

















f1cee5b8ffea0742e4e8800629eab3566dac7677


Switch branch/tag










heros


test


heros


utilities


EdgeBuilder.java



Find file
Normal viewHistoryPermalink






EdgeBuilder.java



2.41 KB









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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
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

f1cee5b8ffea0742e4e8800629eab3566dac7677

















f1cee5b8ffea0742e4e8800629eab3566dac7677


Switch branch/tag










heros


test


heros


utilities


EdgeBuilder.java



Find file
Normal viewHistoryPermalink






EdgeBuilder.java



2.41 KB









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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
}












Open sidebar



Joshua Garcia heros

f1cee5b8ffea0742e4e8800629eab3566dac7677







Open sidebar



Joshua Garcia heros

f1cee5b8ffea0742e4e8800629eab3566dac7677




Open sidebar

Joshua Garcia heros

f1cee5b8ffea0742e4e8800629eab3566dac7677


Joshua Garciaherosheros
f1cee5b8ffea0742e4e8800629eab3566dac7677










f1cee5b8ffea0742e4e8800629eab3566dac7677


Switch branch/tag










heros


test


heros


utilities


EdgeBuilder.java



Find file
Normal viewHistoryPermalink






EdgeBuilder.java



2.41 KB









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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
}















f1cee5b8ffea0742e4e8800629eab3566dac7677


Switch branch/tag










heros


test


heros


utilities


EdgeBuilder.java



Find file
Normal viewHistoryPermalink






EdgeBuilder.java



2.41 KB









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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
}











f1cee5b8ffea0742e4e8800629eab3566dac7677


Switch branch/tag










heros


test


heros


utilities


EdgeBuilder.java



Find file
Normal viewHistoryPermalink




f1cee5b8ffea0742e4e8800629eab3566dac7677


Switch branch/tag










heros


test


heros


utilities


EdgeBuilder.java





f1cee5b8ffea0742e4e8800629eab3566dac7677


Switch branch/tag








f1cee5b8ffea0742e4e8800629eab3566dac7677


Switch branch/tag





f1cee5b8ffea0742e4e8800629eab3566dac7677

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

utilities

EdgeBuilder.java
Find file
Normal viewHistoryPermalink




EdgeBuilder.java



2.41 KB









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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
}









EdgeBuilder.java



2.41 KB










EdgeBuilder.java



2.41 KB









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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;









package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
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


13


14


15


16


17




import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;







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


13


14


15


16


17



import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;


import java.util.Collection;importjava.util.Collection;import java.util.List;importjava.util.List;import com.google.common.collect.Lists;importcom.google.common.collect.Lists;




package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014




18


19



import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;






package restructuring & initial test for BiDiSolver

 


Johannes Lerch
committed
Jun 25, 2014



package restructuring & initial test for BiDiSolver

 

package restructuring & initial test for BiDiSolver

Johannes Lerch
committed
Jun 25, 2014


18


19


import heros.utilities.TestHelper.Edge;
import heros.utilities.TestHelper.ExpectedFlowFunction;

import heros.utilities.TestHelper.Edge;importheros.utilities.TestHelper.Edge;import heros.utilities.TestHelper.ExpectedFlowFunction;importheros.utilities.TestHelper.ExpectedFlowFunction;




Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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




public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
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



public abstract class EdgeBuilder {
	
	protected List<Edge> edges = Lists.newLinkedList();
	public Collection<Edge> edges() {
		if(edges.isEmpty()) {
			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());
		}
		
		return edges;
	}

	public static class CallSiteBuilder extends EdgeBuilder {

		private Statement callSite;

		public CallSiteBuilder(Statement callSite) {
			this.callSite = callSite;
		}

		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));
			return this;
		}
		
		public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));
			return this;
		}
	}
	
	public static class NormalStmtBuilder extends EdgeBuilder {

		private Statement stmt;

		public NormalStmtBuilder(Statement stmt) {
			this.stmt = stmt;
		}

		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));
			return this;
		}
		
	}
	
	public static class ExitStmtBuilder extends EdgeBuilder {

		private Statement exitStmt;

		public ExitStmtBuilder(Statement exitStmt) {
			this.exitStmt = exitStmt;
		}
		
		public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {
			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));
			return this;
		}

		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {
			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));
			return this;
		}
		
	}
}
public abstract class EdgeBuilder {publicabstractclassEdgeBuilder{		protected List<Edge> edges = Lists.newLinkedList();protectedList<Edge>edges=Lists.newLinkedList();	public Collection<Edge> edges() {publicCollection<Edge>edges(){		if(edges.isEmpty()) {if(edges.isEmpty()){			throw new IllegalStateException("Not a single edge created on EdgeBuilder: "+toString());thrownewIllegalStateException("Not a single edge created on EdgeBuilder: "+toString());		}}				return edges;returnedges;	}}	public static class CallSiteBuilder extends EdgeBuilder {publicstaticclassCallSiteBuilderextendsEdgeBuilder{		private Statement callSite;privateStatementcallSite;		public CallSiteBuilder(Statement callSite) {publicCallSiteBuilder(StatementcallSite){			this.callSite = callSite;this.callSite=callSite;		}}		public CallSiteBuilder calls(String method, ExpectedFlowFunction...flows) {publicCallSiteBuildercalls(Stringmethod,ExpectedFlowFunction...flows){			edges.add(new TestHelper.CallEdge(callSite, new Method(method), flows));edges.add(newTestHelper.CallEdge(callSite,newMethod(method),flows));			return this;returnthis;		}}				public CallSiteBuilder retSite(String returnSite, ExpectedFlowFunction...flows) {publicCallSiteBuilderretSite(StringreturnSite,ExpectedFlowFunction...flows){			edges.add(new TestHelper.Call2ReturnEdge(callSite, new Statement(returnSite), flows));edges.add(newTestHelper.Call2ReturnEdge(callSite,newStatement(returnSite),flows));			return this;returnthis;		}}	}}		public static class NormalStmtBuilder extends EdgeBuilder {publicstaticclassNormalStmtBuilderextendsEdgeBuilder{		private Statement stmt;privateStatementstmt;		public NormalStmtBuilder(Statement stmt) {publicNormalStmtBuilder(Statementstmt){			this.stmt = stmt;this.stmt=stmt;		}}		public NormalStmtBuilder succ(String succ, ExpectedFlowFunction... flows) {publicNormalStmtBuildersucc(Stringsucc,ExpectedFlowFunction...flows){			edges.add(new TestHelper.NormalEdge(stmt, new Statement(succ), flows));edges.add(newTestHelper.NormalEdge(stmt,newStatement(succ),flows));			return this;returnthis;		}}			}}		public static class ExitStmtBuilder extends EdgeBuilder {publicstaticclassExitStmtBuilderextendsEdgeBuilder{		private Statement exitStmt;privateStatementexitStmt;		public ExitStmtBuilder(Statement exitStmt) {publicExitStmtBuilder(StatementexitStmt){			this.exitStmt = exitStmt;this.exitStmt=exitStmt;		}}				public ExitStmtBuilder expectArtificalFlow(ExpectedFlowFunction...flows) {publicExitStmtBuilderexpectArtificalFlow(ExpectedFlowFunction...flows){			edges.add(new TestHelper.ReturnEdge(null, exitStmt, null, flows));edges.add(newTestHelper.ReturnEdge(null,exitStmt,null,flows));			return this;returnthis;		}}		public ExitStmtBuilder returns(Statement callSite, Statement returnSite, ExpectedFlowFunction... flows) {publicExitStmtBuilderreturns(StatementcallSite,StatementreturnSite,ExpectedFlowFunction...flows){			edges.add(new TestHelper.ReturnEdge(callSite, exitStmt, returnSite, flows));edges.add(newTestHelper.ReturnEdge(callSite,exitStmt,returnSite,flows));			return this;returnthis;		}}			}}}}





